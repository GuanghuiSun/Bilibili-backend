package com.bilibili.mapper;

import com.bilibili.model.domain.VideoView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_video_view(视频观看统计表)】的数据库操作Mapper
 * @createDate 2022-08-09 22:31:36
 * @Entity com.bilibili.model.domain.VideoView
 */
public interface VideoViewMapper extends BaseMapper<VideoView> {

    VideoView getVideoView(Map<String, Object> params);
}




