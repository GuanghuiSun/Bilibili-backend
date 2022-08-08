package com.bilibili.controller;

import com.bilibili.base.BaseResponse;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.FollowingGroup;
import com.bilibili.model.domain.UserFollowing;
import com.bilibili.service.FollowingGroupService;
import com.bilibili.service.UserFollowingService;
import com.bilibili.support.UserSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.base.ErrorCode.PUT_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.FOLLOW_SUCCESS;
import static com.bilibili.constant.MessageConstant.UNFOLLOW_SUCCESS;

@RestController
@Slf4j
@RequestMapping("/userFollowing")
public class UserFollowingController {

    @Resource
    private UserFollowingService userFollowingService;

    @Resource
    private FollowingGroupService followingGroupService;

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
     * 取消关注
     *
     * @param userFollowing 关注关系
     * @return 响应结果
     */
    @PostMapping("/unfollow")
    public BaseResponse<Boolean> cancelUserFollowing(@RequestBody UserFollowing userFollowing) {
        if (userFollowing == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.cancelUserFollowing(userFollowing);
        return ResultUtils.success(Boolean.TRUE, UNFOLLOW_SUCCESS);
    }

    /**
     * 按分组获取所有关注的用户
     *
     * @return 全部关注分组 + 各个分组
     */
    @GetMapping("/follows")
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

    /**
     * 创建关注分组
     *
     * @param followingGroup 分组信息
     * @return 响应分组id
     */
    @PostMapping("/group")
    public BaseResponse<Long> addUserFollowingGroup(@RequestBody FollowingGroup followingGroup) {
        if (followingGroup == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = followingGroupService.addUserFollowingGroup(followingGroup);
        if (groupId == null || groupId < 0) {
            throw new BusinessException(PUT_SERVICE_ERROR);
        }
        return ResultUtils.success(groupId);
    }

    /**
     * 获取所有用户创建的分组
     *
     * @return 用户分组
     */
    @GetMapping("/groups")
    public BaseResponse<List<FollowingGroup>> getUserFollowingGroups() {
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> result = followingGroupService.getUserFollowingGroups(userId);
        if (result == null || result.isEmpty()) {
            return ResultUtils.success(Collections.emptyList());
        }
        return ResultUtils.success(result);
    }
}
