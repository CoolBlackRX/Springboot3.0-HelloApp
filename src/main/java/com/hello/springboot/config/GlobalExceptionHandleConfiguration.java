package com.hello.springboot.config;

import com.hello.springboot.base.ApiResult;
import com.hello.springboot.exceptions.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@Slf4j
@Configuration
@ControllerAdvice
public class GlobalExceptionHandleConfiguration {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult<Object> defaultExceptionHandler(Exception exception) {
        log.error("【默认系统异常:】", exception);
        return ApiResult.fail("系统异常");
    }

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ApiResult<Object> defaultExceptionHandler(BizException exception) {
        log.error("【自定义系统异常:】", exception);
        return new ApiResult<>(exception.getCode(), null, exception.getMessage(), false);
    }
}
