package com.bilibili.mapper;

import com.bilibili.model.domain.UserFollowing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author sgh
* @description 针对表【t_user_following(用户关注表)】的数据库操作Mapper
* @createDate 2022-08-02 21:17:20
* @Entity com.bilibili.model.domain.UserFollowing
*/
public interface UserFollowingMapper extends BaseMapper<UserFollowing> {

    List<UserFollowing> getUserFollowings(Long userId);

    List<UserFollowing> getUserFans(Long userId);
}




