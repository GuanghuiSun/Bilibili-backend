package com.bilibili.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bilibili.model.domain.VideoComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author sgh
 * @description 针对表【t_video_comment(视频评论表)】的数据库操作Service
 * @createDate 2022-08-07 19:55:02
 */
public interface VideoCommentService extends IService<VideoComment> {

    /**
     * 添加视频评论
     *
     * @param videoComment 视频评论
     * @param userId       用户id
     */
    void addVideoComment(VideoComment videoComment, Long userId);

    /**
     * 分页获取视频评论
     *
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @param videoId     视频id
     * @return 分页结果
     */
    IPage<VideoComment> pageListVideoComments(Integer currentPage, Integer pageSize, Long videoId);
}
