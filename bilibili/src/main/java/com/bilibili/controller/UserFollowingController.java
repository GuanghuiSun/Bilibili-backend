package com.bilibili.controller;

import com.bilibili.base.BaseResponse;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.FollowingGroup;
import com.bilibili.model.domain.UserFollowing;
import com.bilibili.service.UserFollowingService;
import com.bilibili.support.UserSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.constant.MessageConstant.FOLLOW_SUCCESS;

@RestController
@Slf4j
@RequestMapping("/userFollowing")
public class UserFollowingController {

    @Resource
    private UserFollowingService userFollowingService;

    @Resource
    private UserSupport userSupport;

    /**
     * 添加关注
     *
     * @param userFollowing 关注关系
     * @return 响应结果
     */
    @PostMapping("/follow")
    public BaseResponse<Boolean> addUserFollowing(@RequestBody UserFollowing userFollowing) {
        if (userFollowing == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowing(userFollowing);
        return ResultUtils.success(Boolean.TRUE, FOLLOW_SUCCESS);
    }

    /**
     * 获取所有关注分组
     *
     * @return 全部关注分组 + 各个分组
     */
    @GetMapping()
    public BaseResponse<List<FollowingGroup>> getUserFollowings() {
        Long currentUserId = userSupport.getCurrentUserId();
        List<FollowingGroup> userFollowings = userFollowingService.getUserFollowings(currentUserId);
        if (userFollowings.isEmpty()) {
            return ResultUtils.success(Collections.emptyList());
        }
        return ResultUtils.success(userFollowings);
    }

    /**
     * 获取所有粉丝
     *
     * @return 粉丝列表
     */
    @GetMapping("/fans")
    public BaseResponse<List<UserFollowing>> getUserFans() {
        Long currentUserId = userSupport.getCurrentUserId();
        List<UserFollowing> userFans = userFollowingService.getUserFans(currentUserId);
        if (userFans.isEmpty()) {
            return ResultUtils.success(Collections.emptyList());
        }
        return ResultUtils.success(userFans);
    }
}
