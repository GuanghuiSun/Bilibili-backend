package com.bilibili.base;

/**
 * 响应消息工具类
 *
 * @author sgh
 * @date 2022-8-1
 */
public class ResultUtils {
    /**
     * 响应成功
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 响应体
     */
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>("200", data, message, message);
    }

    public static <T> BaseResponse<T> success(String code, T data, String message) {
        return new BaseResponse<>(code, data, message);
    }

    public static <T> BaseResponse<T> success(String code, T data, String message, String description) {
        return new BaseResponse<>(code, data, message, description);
    }

    public static <T> BaseResponse<T> error(String code, T data, String message) {
        return new BaseResponse<>(code, data, message);
    }


    public static <T> BaseResponse<T> error(String code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), errorCode.getDescription());
    }
}
