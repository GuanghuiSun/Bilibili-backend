package com.bilibili.service;

import com.bilibili.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

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
     * @param phone    手机号
     * @param password 密码
     * @return userId
     */
    Long register(String phone, String password);


    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return 双token
     */
    Map<String, Object> login(String phone, String password) throws Exception;

    /**
     * 根据userId查询user
     *
     * @param userId 用户Id
     * @return user
     */
    User getByUserId(Long userId);

    /**
     * 更新用户信息
     *
     * @param user 用户
     */
    void updateUser(User user);

    /**
     * 退出登录
     *
     * @param refreshToken 刷新令牌
     * @param userId       用户id
     */
    void logout(String refreshToken, Long userId);

    /**
     * 获取accessToken
     *
     * @param refreshToken 刷新令牌
     * @return token
     */
    String refreshAccessToken(String refreshToken) throws Exception;
}
