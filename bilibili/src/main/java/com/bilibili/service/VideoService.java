package com.bilibili.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bilibili.model.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_video(视频投稿记录表)】的数据库操作Service
 * @createDate 2022-08-06 17:12:38
 */
public interface VideoService extends IService<Video> {

    /**
     * 视频投稿
     *
     * @param video 视频
     */
    void addVideo(Video video);

    /**
     * 依据分区分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param area        分区参数
     * @return 分页结果
     */
    IPage<Video> queryVideosByArea(int currentPage, int pageSize, String area);

    /**
     * 在线播放
     *
     * @param request  请求体
     * @param response 响应体
     * @param url      路径
     */
    void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url);

    /**
     * 获取视频详情
     *
     * @param videoId 视频id
     * @return 视频详情
     */
    Map<String, Object> getVideoDetails(Long videoId);
}
