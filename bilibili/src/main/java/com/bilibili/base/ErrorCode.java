package com.bilibili.base;

import static com.bilibili.constant.MessageConstant.*;

/**
 * 错误码
 *
 * @author sgh
 * @date 2022-8-1
 */
public enum ErrorCode {

    PARAM_ERROR(PARAM_ERROR_CODE, REQUEST_PARAM_ERROR, REQUEST_PARAM_ERROR),
    SYSTEM_ERROR(SYSTEM_ERROR_CODE, SERVER_INTERNAL_ERROR, SERVER_INTERNAL_ERROR),
    GET_SERVICE_ERROR(GET_MESSAGE_ERROR_CODE,GET_MESSAGE_ERROR,GET_MESSAGE_ERROR),
    UPDATE_SERVICE_ERROR(UPDATE_SERVICE_ERROR_CODE,UPDATE_ERROR,UPDATE_ERROR),
    DELETE_SERVICE_ERROR(DELETE_SERVICE_ERROR_CODE,DELETE_ERROR,DELETE_ERROR),
    PUT_SERVICE_ERROR(PUT_SERVICE_ERROR_CODE,PUT_ERROR,PUT_ERROR),
    REQUEST_SERVICE_ERROR(REQUEST_SERVICE_ERROR_CODE,REQUEST_ERROR,REQUEST_ERROR);

    private String code;
    private String message;
    private String description;

    ErrorCode(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
