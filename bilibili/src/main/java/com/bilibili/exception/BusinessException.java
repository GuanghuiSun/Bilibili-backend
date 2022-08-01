package com.bilibili.exception;

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

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
