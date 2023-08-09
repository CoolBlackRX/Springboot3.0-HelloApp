package com.hello.springboot.base;

import lombok.Data;

@Data
public class ApiResult<T> {
    private Integer code;
    private T data;
    private String message;
    private Boolean result;

    public ApiResult(Integer code, T data, String message, Boolean result) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResult<T> success() {
        return success(null);
    }

    public static <T> ApiResult<T> success(T object) {
        return success(object, "调用成功");
    }

    public static <T> ApiResult<T> success(T object, String message) {
        return new ApiResult<>(20000, object, message, true);
    }


    public static <T> ApiResult<T> fail() {
        return fail("调用失败");
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(600, null, message, false);
    }
}
