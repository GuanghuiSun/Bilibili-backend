package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.UserFollowing;
import com.bilibili.service.UserFollowingService;
import com.bilibili.mapper.UserFollowingMapper;
import org.springframework.stereotype.Service;

/**
* @author huawei
* @description 针对表【t_user_following(用户关注表)】的数据库操作Service实现
* @createDate 2022-08-02 21:17:20
*/
@Service
public class UserFollowingServiceImpl extends ServiceImpl<UserFollowingMapper, UserFollowing>
    implements UserFollowingService{

}




