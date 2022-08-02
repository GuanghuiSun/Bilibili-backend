package com.bilibili.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册登录请求体
 *
 * @author sgh
 * @date 2022-8-2
 */
@Data
public class UserRegisterAndLoginRequest implements Serializable {
    private static final long serialVersionUID = -4574208574150848943L;
    private String phone;
    private String password;
}
