package com.hello.springboot.minio;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.MoreObjects;
import com.hello.springboot.config.MinioClientConfigurer;
import io.minio.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Component
public class FileStorageManager {
    private MinioClientConfigurer minioClientConfigurer;

    @Autowired
    public void setMinioClientConfigurer(MinioClientConfigurer minioClientConfigurer) {
        this.minioClientConfigurer = minioClientConfigurer;
    }

    public String upload(String bucketName, MultipartFile multipartFile) {
        String namespace = MoreObjects.firstNonNull(bucketName, minioClientConfigurer.getNamespace());
        try {
            MinioClient minioClient = MinioClient.builder().endpoint(minioClientConfigurer.getEndpoint())
                    .credentials(minioClientConfigurer.getAccessKey(), minioClientConfigurer.getSecretKey())
                    .build();
            // 文件存储空间不存在，创建指定的存储空间
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(namespace).build();
            if (!minioClient.bucketExists(bucketExistsArgs)) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(namespace).build();
                minioClient.makeBucket(makeBucketArgs);
            }
            // 上传的文件名称
            String originalFilename = multipartFile.getOriginalFilename();
            if (StrUtil.isEmpty(originalFilename)) {
                throw new IllegalArgumentException("客户端上传文件获取文件名失败,请检查上传文件");
            }
            String contentType = multipartFile.getContentType();
            String timestamp = Long.toString(DateUtil.current());
            // 储存在minio内的文件名
            String minioFileName = timestamp + "." +
                    StrUtil.subAfter(originalFilename, ".", true);

            try (InputStream inputStream = multipartFile.getInputStream()) {
                PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                        .contentType(contentType)
                        .bucket(namespace)
                        .stream(inputStream, inputStream.available(), -1)
                        .object(minioFileName)
                        .build();
                minioClient.putObject(uploadObjectArgs);
                log.info("""
                        \n
                        {
                            客户端上传的原始文件名称:{}
                            文件类型:{}
                            上传时间:{}
                            文件服务器namespace:{}
                            文件服务器内文件名称:{}
                        }
                        """, originalFilename, contentType, DateUtil.now(), namespace, minioFileName);
                return minioFileName;
            }
        } catch (Exception exception) {
            log.error("上传文件失败:", exception);
            throw new IllegalArgumentException("上传文件失败");
        }
    }

    /**
     * 调用此接口请手动关闭流
     *
     * @param bucketName    文件存储空间
     * @param minioFileName 文件名
     * @return 文件流
     */
    public InputStream download(String bucketName, String minioFileName) {
        if (StrUtil.isEmpty(minioFileName)) {
            throw new IllegalArgumentException("文件不存在");
        }
        try {
            MinioClient minioClient = MinioClient.builder().endpoint(minioClientConfigurer.getEndpoint())
                    .credentials(minioClientConfigurer.getAccessKey(), minioClientConfigurer.getSecretKey())
                    .build();
            // 文件存储空间不存在
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            if (!minioClient.bucketExists(bucketExistsArgs)) {
                log.error("文件下载空间:{}", bucketName);
                throw new IllegalArgumentException("文件存储空间不存在");
            }
            // 检查文件是否存在
            StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(minioFileName)
                    .build();
            StatObjectResponse statObjectResponse;
            try {
                statObjectResponse = minioClient.statObject(statObjectArgs);
            } catch (Exception exception) {
                throw new IllegalArgumentException("文件不存在");
            }
            if (ObjectUtil.isEmpty(statObjectResponse)) {
                throw new IllegalArgumentException("文件不存在");
            }
            // 下载文件
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(minioFileName)
                    .build();
            return minioClient.getObject(getObjectArgs);
        } catch (Exception exception) {
            log.error("下载文件失败:", exception);
            throw new IllegalArgumentException("下载文件失败");
        }
    }

    public void download(String fileName, String bucketName, String minioFileName, HttpServletResponse httpServletResponse) {
        String targetName = MoreObjects.firstNonNull(fileName, minioFileName);
        try (InputStream inputStream = download(bucketName, minioFileName);
             OutputStream outputStream = httpServletResponse.getOutputStream()) {
            if (StrUtil.isNotEmpty(fileName)) {
                httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + targetName);
            }
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/force-download");
            IoUtil.copy(inputStream, outputStream);
        } catch (Exception exception) {
            log.error("下载文件失败:", exception);
            throw new IllegalArgumentException("下载文件失败");
        }
    }

}
