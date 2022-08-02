package com.bilibili.controller;

import com.bilibili.base.BaseResponse;
import com.bilibili.base.ErrorCode;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.request.UserLoginRequest;
import com.bilibili.model.request.UserRegisterRequest;
import com.bilibili.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import static com.bilibili.constant.MessageConstant.LOGIN_SUCCESS;
import static com.bilibili.constant.MessageConstant.REGISTER_SUCCESS;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

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
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String phone = request.getPhone();
        String password = request.getPassword();
        String checkPassword = request.getCheckPassword();
        return ResultUtils.success(userService.register(phone, password, checkPassword),REGISTER_SUCCESS);
    }

    /**
     * 登录
     * @param request 登录请求体
     * @return token
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody UserLoginRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String phone = request.getPhone();
        String password = request.getPassword();
        return ResultUtils.success(userService.login(phone, password),LOGIN_SUCCESS);

    }


}
