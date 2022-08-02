package com.bilibili.controller;

import com.bilibili.base.BaseResponse;
import com.bilibili.base.ErrorCode;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.User;
import com.bilibili.model.domain.UserInfo;
import com.bilibili.model.request.UserRegisterAndLoginRequest;
import com.bilibili.service.UserInfoService;
import com.bilibili.service.UserService;
import com.bilibili.support.UserSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.bilibili.base.ErrorCode.GET_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 获取RSA公钥
     * @return RSA公钥
     */
    @GetMapping("/rsa-pks")
    public BaseResponse<String> getRsaPublicKey() {
        String key = userService.getRsaPublicKey();
        return ResultUtils.success(key);
    }

    /**
     * 注册
     * @param request 注册请求体
     * @return 用户Id
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterAndLoginRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String phone = request.getPhone();
        String password = request.getPassword();
        return ResultUtils.success(userService.register(phone, password),REGISTER_SUCCESS);
    }

    /**
     * 登录接口
     * @param request 登录请求体
     * @return token
     * @throws Exception 加解密异常
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserRegisterAndLoginRequest request) throws Exception {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String phone = request.getPhone();
        String password = request.getPassword();
        return ResultUtils.success(userService.login(phone, password),LOGIN_SUCCESS);
    }

    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    @GetMapping("/user")
    public BaseResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getByUserId(userId);
        if(user == null) {
            throw new BusinessException(GET_SERVICE_ERROR,GET_USER_ERROR);
        }
        return ResultUtils.success(user);
    }

    /**
     * 更新用户基本信息
     * @param user 用户信息
     * @return 是否成功
     */
    @PutMapping("/user")
    public BaseResponse<Boolean> updateUser(@RequestBody User user) {
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUser(user);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 更新用户详细信息
     * @param userInfo 用户信息
     * @return 是否成功
     */
    @PutMapping("/userInfo")
    public BaseResponse<Boolean> updateUserInfo(@RequestBody UserInfo userInfo) {
        if(userInfo == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userInfoService.updateUserInfo(userInfo);
        return ResultUtils.success(Boolean.TRUE);
    }

}
