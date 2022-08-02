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
     * @return userId
     */
    Long register(String phone, String password);


    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return token
     */
    String login(String phone, String password) throws Exception;

    /**
     * 根据userId查询user
     * @param userId 用户Id
     * @return user
     */
    User getByUserId(Long userId);

    /**
     * 更新用户信息
     * @param user 用户
     */
    void updateUser(User user);
}
