package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.FollowingGroup;
import com.bilibili.model.domain.User;
import com.bilibili.model.domain.UserFollowing;
import com.bilibili.model.domain.UserInfo;
import com.bilibili.service.FollowingGroupService;
import com.bilibili.service.UserFollowingService;
import com.bilibili.mapper.UserFollowingMapper;
import com.bilibili.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bilibili.base.ErrorCode.*;
import static com.bilibili.constant.MessageConstant.*;
import static com.bilibili.constant.user.UserConstant.USER_FOLLOWING_GROUP_NAME_DEFAULT;
import static com.bilibili.constant.user.UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT;

/**
 * @author sgh
 * @description 针对表【t_user_following(用户关注表)】的数据库操作Service实现
 * @createDate 2022-08-02 21:17:20
 */
@Service
@Slf4j
public class UserFollowingServiceImpl extends ServiceImpl<UserFollowingMapper, UserFollowing>
        implements UserFollowingService {

    @Resource
    private FollowingGroupService followingGroupService;

    @Resource
    private UserService userService;

    @Resource
    private UserFollowingMapper userFollowingMapper;

    @Override
    public void addUserFollowing(UserFollowing userFollowing) {
        if (userFollowing == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        //校验或设置默认分组
        Long groupId = userFollowing.getGroupId();
        if (groupId == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            //检查是否存在
            followingGroupService.getByGroupId(groupId);
        }
        //检查用户是否存在
        Long followingId = userFollowing.getFollowingId();
        if (followingId == null || followingId < 0) {
            throw new BusinessException(PARAM_ERROR, FOLLOWING_USER_NOT_EXIST_ERROR);
        }
        User followingUser = userService.getById(followingId);
        if (followingUser == null) {
            throw new BusinessException(PARAM_ERROR, FOLLOWING_USER_NOT_EXIST_ERROR);
        }
        //检查是否重复关注
        UserFollowing queryFollowing = getOneUserFollowing(userFollowing.getUserId(), followingId);
        if (queryFollowing != null) {
            throw new BusinessException(PUT_SERVICE_ERROR, ADD_FOLLOWING_REPEAT_ERROR);
        }
        this.save(userFollowing);
    }

    @Override
    public UserFollowing getOneUserFollowing(Long userId, Long followingUserId) {
        //校验用户id
        if (userId == null || userId < 0 || followingUserId == null || followingUserId < 0) {
            throw new BusinessException(PARAM_ERROR, USER_ID_ERROR);
        }
        //查询关注关系是否存在
        LambdaQueryWrapper<UserFollowing> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFollowing::getUserId, userId)
                .eq(UserFollowing::getFollowingId, followingUserId)
                .eq(UserFollowing::getIsUnfollowed, 0);
        return this.getOne(wrapper);
    }

    @Override
    public List<FollowingGroup> getUserFollowings(Long userId) {
        if (userId == null || userId < 0) {
            throw new BusinessException(PARAM_ERROR, USER_ID_ERROR);
        }
        //查询用户所有关注关系
        List<UserFollowing> list = userFollowingMapper.getUserFollowings(userId);
        log.debug("{}", list);
        List<UserInfo> followingUserInfoList = new ArrayList<>();
        for (UserFollowing userFollowing : list) {
            followingUserInfoList.add(userFollowing.getFollowingUserInfo());
        }
        //获取用户所有分组
        List<FollowingGroup> result = new ArrayList<>();
        //获取全部关注分组
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setGroupName(USER_FOLLOWING_GROUP_NAME_DEFAULT);
        allGroup.setFollowingUserInfoList(followingUserInfoList);
        result.add(allGroup);
        //获取各个分组
        List<FollowingGroup> groups = followingGroupService.getByUserId(userId);
        for (FollowingGroup group : groups) {
            Long currentGroupId = group.getId();
            List<UserInfo> temp = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                if (currentGroupId.equals(userFollowing.getGroupId())) {
                    temp.add(userFollowing.getFollowingUserInfo());
                }
            }
            group.setFollowingUserInfoList(temp);
            result.add(group);
        }
        log.debug("{}", result);
        return result;
    }

    @Override
    public List<UserFollowing> getUserFans(Long userId) {
        if (userId == null || userId < 0) {
            throw new BusinessException(PARAM_ERROR, USER_ID_ERROR);
        }
        //获取所有粉丝列表
        List<UserFollowing> fans = userFollowingMapper.getUserFans(userId);
        if(fans.isEmpty()) {
            return Collections.emptyList();
        }
        //获取当前用户的关注列表
        LambdaQueryWrapper<UserFollowing> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFollowing::getUserId, userId).eq(UserFollowing::getIsUnfollowed, 0);
        List<UserFollowing> followingList = this.list(wrapper);
        //检查是否互相关注
        for (UserFollowing fan : fans) {
            fan.setFollowed(Boolean.FALSE);
            for (UserFollowing following : followingList) {
                if (following.getFollowingId().equals(fan.getUserId())) {
                    fan.setFollowed(Boolean.TRUE);
                    break;
                }
            }
        }
        return fans;
    }

    @Override
    public void cancelUserFollowing(UserFollowing userFollowing) {
        //检查关注关系是否存在
        Long userId = userFollowing.getUserId();
        Long followingId = userFollowing.getFollowingId();
        UserFollowing queryResult = getOneUserFollowing(userId, followingId);
        if(queryResult == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, FOLLOWING_NOT_EXIST_ERROR);
        }
        userFollowingMapper.cancelUserFollowing(userFollowing);
    }
}




