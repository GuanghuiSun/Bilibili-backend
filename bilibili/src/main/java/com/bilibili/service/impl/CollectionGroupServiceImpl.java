package com.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.CollectionGroup;
import com.bilibili.service.CollectionGroupService;
import com.bilibili.mapper.CollectionGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author sgh
* @description 针对表【t_collection_group(用户收藏分组表)】的数据库操作Service实现
* @createDate 2022-08-07 16:10:37
*/
@Service
public class CollectionGroupServiceImpl extends ServiceImpl<CollectionGroupMapper, CollectionGroup>
    implements CollectionGroupService{

}




