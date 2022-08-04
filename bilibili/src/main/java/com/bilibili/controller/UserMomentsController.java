package com.bilibili.controller;

import com.bilibili.annotation.ApiLimitedRole;
import com.bilibili.annotation.DataLimited;
import com.bilibili.base.BaseResponse;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.UserMoments;
import com.bilibili.model.request.ScrollSubscribedMomentsRequest;
import com.bilibili.model.result.ScrollResult;
import com.bilibili.service.UserMomentsService;
import com.bilibili.support.UserSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.constant.user.AuthRoleConstant.ROLE_CODE_LV0;

@RestController
@RequestMapping("/userMoments")
@Slf4j
public class UserMomentsController {

    @Resource
    private UserMomentsService userMomentsService;

    @Resource
    private UserSupport userSupport;

    /**
     * 发布动态
     *
     * @param userMoments 动态
     * @return 响应结果
     */
    @ApiLimitedRole(limitedRoleCodeList = {ROLE_CODE_LV0})
    @DataLimited
    @PostMapping("/add")
    public BaseResponse<Boolean> addUserMoments(@RequestBody UserMoments userMoments) {
        if (userMoments == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        userMoments.setUserId(userId);
        userMomentsService.addUserMoments(userMoments);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 获取用户所有订阅的动态
     *
     * @return 动态
     */
    @GetMapping
    public BaseResponse<ScrollResult> getUserSubscribedMoments(@RequestBody ScrollSubscribedMomentsRequest request) {
        if (request == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        Long max = request.getMax();
        Long offset = request.getOffset();
        Long size = request.getSize();
        if(size == null || size < 0 || offset == null || offset < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        if(max == null) {
            max = System.currentTimeMillis();
        }
        ScrollResult result = userMomentsService.getUserSubscribedMoments(userId, max, offset, size);
        return ResultUtils.success(result);
    }


}
