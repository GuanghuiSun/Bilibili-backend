package com.bilibili.service;

import com.bilibili.model.domain.FollowingGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author sgh
 * @description 针对表【t_following_group(用户关注分组表)】的数据库操作Service
 * @createDate 2022-08-03 13:42:57
 */
public interface FollowingGroupService extends IService<FollowingGroup> {

    /**
     * 根据类型值获取分组
     *
     * @param type 类型
     * @return 分组
     */
    FollowingGroup getByType(Byte type);

    /**
     * 根据分组id获取分组
     *
     * @param id 分组id
     * @return 分组
     */
    FollowingGroup getByGroupId(Long id);

    /**
     * 根据用户id查询所有分组
     *
     * @param userId 用户id
     * @return 所有分组
     */
    List<FollowingGroup> getByUserId(Long userId);

    /**
     * 创建用户分组
     * @param followingGroup 分组
     * @return 分组id
     */
    Long addUserFollowingGroup(FollowingGroup followingGroup);

    /**
     * 获取用户创建的分组
     * @param userId 用户id
     * @return 分组结果集
     */
    List<FollowingGroup> getUserFollowingGroups(Long userId);
}
