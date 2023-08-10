package com.hello.springboot.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 自定义运行时异常
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {
    @Getter
    private final Integer code;

    public BizException(String message) {
        super(message);
        this.code = 600;
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
