package com.bilibili.mapper;

import com.bilibili.model.domain.BulletScreenComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_bullet_screen_comments(弹幕记录表)】的数据库操作Mapper
 * @createDate 2022-08-08 16:04:57
 * @Entity com.bilibili.model.domain.BulletScreenComments
 */
public interface BulletScreenCommentsMapper extends BaseMapper<BulletScreenComments> {

    List<BulletScreenComments> getBulletScreenComments(Map<String, Object> param);
}




