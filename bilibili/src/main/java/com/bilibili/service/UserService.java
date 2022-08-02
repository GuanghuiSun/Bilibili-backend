package com.bilibili.service;

import com.bilibili.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author sgh
 * @description 针对表【t_user(用户表)】的数据库操作Service
 * @createDate 2022-08-01 23:28:26
 */
public interface UserService extends IService<User> {

    /**
     * 获取rsa公钥
     *
     * @return key
     */
    String getRsaPublicKey();

    /**
     * 注册
     *
     * @param phone         手机号
     * @param password      密码
     * @param checkPassword 确认密码
     * @return
     */
    Long register(String phone, String password, String checkPassword);


    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return
     */
    String login(String phone, String password);
}
