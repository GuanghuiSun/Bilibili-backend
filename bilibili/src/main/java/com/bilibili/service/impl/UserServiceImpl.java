package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.base.ErrorCode;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.RefreshToken;
import com.bilibili.model.domain.User;
import com.bilibili.model.domain.UserInfo;
import com.bilibili.service.UserAuthService;
import com.bilibili.service.UserInfoService;
import com.bilibili.service.UserService;
import com.bilibili.mapper.UserMapper;
import com.bilibili.utils.MD5Util;
import com.bilibili.utils.RSAUtil;
import com.bilibili.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import static com.bilibili.base.ErrorCode.PUT_SERVICE_ERROR;
import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
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

    @Resource
    private UserAuthService userAuthService;

    @Override
    public String getRsaPublicKey() {
        String key = RSAUtil.getPublicKeyStr();
        if (key == null) {
            throw new BusinessException(ErrorCode.GET_SERVICE_ERROR, GET_KEY_ERROR);
        }
        return key;
    }

    @Override
    public Long register(String phone, String password) {
        //校验参数是否为空
        if (StringUtils.isAnyBlank(phone, password)) {
            throw new BusinessException(PARAM_ERROR_CODE, PARAM_EMPTY_ERROR);
        }
        //检查手机号格式
        boolean matches = Pattern.matches("^1[3-9]\\d{9}$", phone);
        if (!matches) {
            throw new BusinessException(PARAM_ERROR_CODE, PHONE_PATTERN_ERROR);
        }
        //检查手机号是否已被注册
        User dbUser = checkPhone(phone);
        if (dbUser != null) {
            throw new BusinessException(PUT_SERVICE_ERROR, PHONE_EXIST_ERROR);
        }
        //密码加密存储
        //获取当前时间戳
        LocalDateTime now = LocalDateTime.now();
        //作为盐值
        String salt = String.valueOf(now);
        //对传进来的密码解密
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new BusinessException(PARAM_ERROR_CODE, RSA_DECODE_FAIL);
        }
        //对解密后的密码MD5加密存储
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
        //添加用户默认权限角色
        Long userId = user.getId();
        userAuthService.addUserDefaultRole(userId);
        return userId;
    }

    /**
     * 根据手机号获取用户
     *
     * @param phone 手机号
     * @return 结果
     */
    public User checkPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        return this.getOne(wrapper);
    }

    @Override
    public Map<String, Object> login(String phone, String password) throws Exception {
        //校验非空
        if (StringUtils.isAnyBlank(phone, password)) {
            throw new BusinessException(PARAM_ERROR_CODE, PARAM_EMPTY_ERROR);
        }
        //检查用户是否存在
        User dbUser = checkPhone(phone);
        if (dbUser == null) {
            throw new BusinessException(GET_MESSAGE_ERROR, PHONE_NOT_EXIST_ERROR);
        }
        //密码解密
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new BusinessException(PARAM_ERROR_CODE, RSA_DECODE_FAIL);
        }
        //校验密码
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getUserPassword())) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, PASSWORD_ERROR);
        }
        Long userId = dbUser.getId();
        Map<String, Object> result = new HashMap<>();
        //获取双token
        String accessToken = TokenUtils.generateToken(userId);
        if (accessToken == null || StringUtils.isBlank(accessToken)) {
            throw new BusinessException(GET_MESSAGE_ERROR, GENERATE_TOKEN_ERROR);
        }
        result.put("accessToken", accessToken);
        String refreshToken = TokenUtils.generateRefreshToken(userId);
        if (refreshToken == null || StringUtils.isBlank(refreshToken)) {
            throw new BusinessException(GET_MESSAGE_ERROR, GENERATE_TOKEN_ERROR);
        }
        result.put("refreshToken", refreshToken);
        //保存refreshToken到数据库
        userMapper.deleteRefreshToken(refreshToken, userId);
        userMapper.addRefreshToken(refreshToken, userId);
        return result;
    }

    @Override
    public User getByUserId(Long userId) {
        User user = this.getById(userId);
        user.setUserPassword(null);
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserId, userId);
        UserInfo userInfo = userInfoService.getOne(wrapper);
        user.setUserInfo(userInfo);
        return user;
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(new Date());
        userMapper.updateUser(user);
    }

    @Override
    public void logout(String refreshToken, Long userId) {
        userMapper.deleteRefreshToken(refreshToken, userId);
    }

    @Override
    public String refreshAccessToken(String refreshToken) throws Exception {
        if (refreshToken == null) {
            throw new BusinessException(PARAM_ERROR_CODE, PARAM_EMPTY_ERROR);
        }
        RefreshToken token = userMapper.getRefreshTokenDetail(refreshToken);
        if (token == null) {
            throw new BusinessException(TOKEN_ERROR_CODE, USER_STATUS_ERROR, TOKEN_EXPIRE_ERROR);
        }
        return TokenUtils.generateToken(token.getUserId());
    }
}




