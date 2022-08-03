package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.FollowingGroup;
import com.bilibili.service.FollowingGroupService;
import com.bilibili.mapper.FollowingGroupMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.bilibili.base.ErrorCode.GET_SERVICE_ERROR;
import static com.bilibili.base.ErrorCode.PARAM_ERROR;
import static com.bilibili.constant.MessageConstant.*;
import static com.bilibili.constant.MessageConstant.GROUP_NOT_EXIST_ERROR;

/**
* @author sgh
* @description 针对表【t_following_group(用户关注分组表)】的数据库操作Service实现
* @createDate 2022-08-03 13:42:57
*/
@Service
public class FollowingGroupServiceImpl extends ServiceImpl<FollowingGroupMapper, FollowingGroup>
    implements FollowingGroupService{
    @Override
    public FollowingGroup getByType(Byte type) {
        if(type == null || type < 0) {
            throw new BusinessException(PARAM_ERROR, GROUP_TYPE_ID_ERROR);
        }
        LambdaQueryWrapper<FollowingGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FollowingGroup::getType, type);
        FollowingGroup result = this.getOne(wrapper);
        if(result == null) {
            throw new BusinessException(GET_SERVICE_ERROR, GROUP_NOT_EXIST_ERROR);
        }
        return result;
    }

    @Override
    public FollowingGroup getByGroupId(Long id) {
        if(id < 0) {
            throw new BusinessException(PARAM_ERROR, GROUP_ID_ERROR);
        }
        FollowingGroup result = this.getById(id);
        if(result == null) {
            throw new BusinessException(GET_SERVICE_ERROR, GROUP_NOT_EXIST_ERROR);
        }
        return result;
    }

    @Override
    public List<FollowingGroup> getByUserId(Long userId) {
        if(userId == null || userId < 0) {
            throw new BusinessException(PARAM_ERROR, USER_ID_ERROR);
        }
        LambdaQueryWrapper<FollowingGroup> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(FollowingGroup::getUserId, userId).or().in(FollowingGroup::getType,0,1,2);
        List<FollowingGroup> list = this.list(wrapper);
        if(list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list;
    }
}




