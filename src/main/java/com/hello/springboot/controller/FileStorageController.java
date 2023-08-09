package com.hello.springboot.controller;

import com.hello.springboot.base.ApiResult;
import com.hello.springboot.minio.FileStorageManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file_storage")
@RequiredArgsConstructor
public class FileStorageController {
    private final FileStorageManager fileStorageManager;

    @PostMapping("upload")
    public ApiResult<String> upload(@RequestPart(value = "file") MultipartFile multipartFile,
                                    @RequestParam(value = "namespace", required = false) String namespace) {
        String fileStorageName = fileStorageManager.upload(namespace, multipartFile);
        return ApiResult.success(fileStorageName);
    }

    @GetMapping("download/{namespace}")
    public void download(@PathVariable(value = "namespace") String namespace,
                         @RequestParam(value = "fileName") String fileName,
                         @RequestParam(value = "targetName", required = false) String targetName,
                         HttpServletResponse httpServletResponse) {
        fileStorageManager.download(targetName, namespace, fileName, httpServletResponse);
    }

}
