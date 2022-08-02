package com.bilibili.exception;

import com.bilibili.base.ErrorCode;

import java.io.Serializable;

/**
 * 业务异常类
 *
 * @author sgh
 * #date 2022-8-1
 */
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -836387913753259763L;

    private String code;
    private String description;

    public BusinessException(String message, String code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
        this.description = message;
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode.getCode(), errorCode.getDescription());
    }

    public BusinessException(ErrorCode errorCode, String description) {
        this(errorCode.getMessage(), errorCode.getCode(), description);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
