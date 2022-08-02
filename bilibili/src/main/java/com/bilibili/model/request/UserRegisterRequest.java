package com.bilibili.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author sgh
 * @date 2022-8-2
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -205920810522402408L;
    private String phone;
    private String password;
    private String checkPassword;

}
