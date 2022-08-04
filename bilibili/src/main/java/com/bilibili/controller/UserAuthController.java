package com.bilibili.controller;

import com.bilibili.base.BaseResponse;
import com.bilibili.base.ResultUtils;
import com.bilibili.model.auth.UserAuthorities;
import com.bilibili.service.UserAuthService;
import com.bilibili.support.UserSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserAuthController {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserAuthService userAuthService;

    @GetMapping("/user")
    public BaseResponse<UserAuthorities> getUserAuthorities() {
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities result = userAuthService.getUserAuthorities(userId);
        return ResultUtils.success(result);
    }



}
