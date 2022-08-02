package com.bilibili.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author sgh
 * @date 2022-8-2
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 524402124808007366L;
    private String phone;
    private String password;
}
