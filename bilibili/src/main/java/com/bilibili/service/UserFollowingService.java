package com.bilibili.service;

import com.bilibili.model.domain.FollowingGroup;
import com.bilibili.model.domain.UserFollowing;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author sgh
 * @description 针对表【t_user_following(用户关注表)】的数据库操作Service
 * @createDate 2022-08-02 21:17:20
 */
public interface UserFollowingService extends IService<UserFollowing> {

    /**
     * 添加关注
     *
     * @param userFollowing 关注
     */
    void addUserFollowing(UserFollowing userFollowing);

    /**
     * 检查是否关注
     *
     * @param userId          用户id
     * @param followingUserId 被关注用户id
     * @return 查询结果
     */
    UserFollowing getOneUserFollowing(Long userId, Long followingUserId);

    /**
     * 获取用户关注列表分组
     *
     * @param userId 用户id
     * @return 关注列表
     */
    List<FollowingGroup> getUserFollowings(Long userId);

    /**
     * 获取用户粉丝列表
     *
     * @param userId 用户id
     * @return 粉丝列表
     */
    List<UserFollowing> getUserFans(Long userId);
}
