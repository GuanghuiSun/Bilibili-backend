package com.bilibili.service;

import com.bilibili.model.domain.VideoView;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sgh
 * @description 针对表【t_video_view(视频观看统计表)】的数据库操作Service
 * @createDate 2022-08-09 22:31:36
 */
public interface VideoViewService extends IService<VideoView> {

    /**
     * 添加观看记录
     *
     * @param videoView 观看记录
     * @param request   请求体
     */
    void addVideoView(VideoView videoView, HttpServletRequest request);

    /**
     * 获取视频播放量
     *
     * @param videoId 视频id
     * @return 播放量
     */
    Long getVideoViewCounts(Long videoId);
}
