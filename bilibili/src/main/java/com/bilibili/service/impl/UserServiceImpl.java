package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.base.ErrorCode;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.User;
import com.bilibili.model.domain.UserInfo;
import com.bilibili.service.UserInfoService;
import com.bilibili.service.UserService;
import com.bilibili.mapper.UserMapper;
import com.bilibili.utils.MD5Util;
import com.bilibili.utils.RSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static com.bilibili.base.ErrorCode.PUT_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;
import static com.bilibili.constant.user.UserConstant.*;

/**
 * @author sgh
 * @description 针对表【t_user(用户表)】的数据库操作Service实现
 * @createDate 2022-08-01 23:28:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoService userInfoService;

    @Override
    public String getRsaPublicKey() {
        String key = RSAUtil.getPublicKeyStr();
        if (key == null) {
            throw new BusinessException(ErrorCode.GET_SERVICE_ERROR, GET_KEY_ERROR);
        }
        return key;
    }

    @Override
    public Long register(String phone, String password, String checkPassword) {
        //校验参数是否为空
        if(StringUtils.isAnyBlank(phone,password,checkPassword)) {
            throw new BusinessException(PARAM_ERROR_CODE,PARAM_EMPTY_ERROR);
        }
        //检查手机号格式
        boolean matches = Pattern.matches("^1[3-9]\\d{9}$", phone);
        if(!matches){
            throw new BusinessException(PARAM_ERROR_CODE,PHONE_PATTERN_ERROR);
        }
        //检查密码格式
        if(password.length() < 6) {
            throw new BusinessException(PARAM_ERROR_CODE,PASSWORD_PATTERN_ERROR);
        }
        //检查两次密码是否一致
        if (!password.equals(checkPassword)) {
            throw new BusinessException(PARAM_ERROR_CODE, CHECK_REGISTER_FAIL);
        }
        //检查手机号是否已被注册
        checkPhone(phone);
        //密码加密存储
            //获取当前时间戳
        LocalDateTime now = LocalDateTime.now();
            //作为盐值
        String salt = String.valueOf(now);
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e) {
            throw new BusinessException(PARAM_ERROR_CODE, RSA_DECODE_FAIL);
        }
        //对加密密码MD5加密存储
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        //保存用户基本信息
        User user = new User();
        user.setUserPassword(md5Password);
        user.setSalt(salt);
        user.setPhone(phone);
        this.save(user);
        //保存用户详细信息表
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(DEFAULT_NICK);
        userInfo.setSign(DEFAULT_NICK);
        userInfo.setGender(GENDER_MALE);
        userInfo.setBirth(DEFAULT_BIRTH);
        userInfoService.save(userInfo);
        return user.getId();
    }
    
    public Boolean checkPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,phone);
        long count = this.count(wrapper);
        if(count > 0) {
            throw new BusinessException(PUT_SERVICE_ERROR,PHONE_EXIST_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public String login(String phone, String password) {
        return null;
    }
}




