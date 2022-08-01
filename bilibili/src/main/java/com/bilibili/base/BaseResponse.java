package com.bilibili.base;

import lombok.Data;

/**
 * 通用消息响应体
 *
 * @param <T> 数据类型
 * @author sgh
 * @date 2022-8-1
 */
@Data
public class BaseResponse<T> {

    private String code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(String code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(String code, T data, String message) {
        this(code, data, message, message);
    }

    public BaseResponse(String code, String message, String description) {
        this(code, null, message, description);
    }
}
