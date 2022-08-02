package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.FollowingGroup;
import com.bilibili.service.FollowingGroupService;
import com.bilibili.mapper.FollowingGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author huawei
* @description 针对表【t_following_group(用户关注分组表)】的数据库操作Service实现
* @createDate 2022-08-02 21:17:13
*/
@Service
public class FollowingGroupServiceImpl extends ServiceImpl<FollowingGroupMapper, FollowingGroup>
    implements FollowingGroupService{

}




