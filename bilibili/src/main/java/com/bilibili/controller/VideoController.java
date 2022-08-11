package com.bilibili.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bilibili.base.BaseResponse;
import com.bilibili.base.ResultUtils;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.*;
import com.bilibili.model.request.VideoCommentsPageRequest;
import com.bilibili.model.request.VideoPageByAreaRequest;
import com.bilibili.service.*;
import com.bilibili.support.UserSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.bilibili.base.ErrorCode.PARAM_ERROR;

/**
 * video controller
 */
@Slf4j
@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private UserSupport userSupport;

    @Resource
    private VideoService videoService;

    @Resource
    private VideoLikeService videoLikeService;

    @Resource
    private VideoCollectionService videoCollectionService;

    @Resource
    private VideoCoinService videoCoinService;

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private ElasticSearchService elasticSearchService;

    @Resource
    private VideoViewService videoViewService;

    /**
     * 视频投稿
     *
     * @param video 视频
     * @return 是否成功
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addVideos(@RequestBody Video video) {
        if (video == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 按分区分页查询视频列表
     *
     * @param request 请求体
     * @return 分页结果
     */
    @PostMapping("/pageByArea")
    public BaseResponse<IPage<Video>> queryVideosByArea(@RequestBody VideoPageByAreaRequest request) {
        if (request == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Integer currentPage = request.getCurrentPage();
        Integer pageSize = request.getPageSize();
        String area = request.getArea();
        if (currentPage == null || currentPage < 0 || pageSize == null || pageSize < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        IPage<Video> result = videoService.queryVideosByArea(currentPage, pageSize, area);
        return ResultUtils.success(result);
    }

    /**
     * 在线观看
     * 分片获取视频资源
     *
     * @param request  请求体
     * @param response 响应体
     * @param url      路径
     */
    @GetMapping("/play-online")
    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response,
                                        String url) {
        if (request == null || response == null || StringUtils.isBlank(url)) {
            throw new BusinessException(PARAM_ERROR);
        }
        videoService.viewVideoOnlineBySlices(request, response, url);
    }

    /**
     * 视频点赞
     *
     * @param videoId 视频id
     * @return 响应
     */
    @PostMapping("/like")
    public BaseResponse<Boolean> addVideoLike(@RequestParam Long videoId) {
        if (videoId == null || videoId < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        videoLikeService.addVideoLike(videoId, userId);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 取消视频点赞
     *
     * @param videoId 视频id
     * @return 响应
     */
    @DeleteMapping("/like")
    public BaseResponse<Boolean> cancelVideoLike(@RequestParam Long videoId) {
        if (videoId == null || videoId < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        videoLikeService.cancelVideoLike(videoId, userId);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 获取视频点赞量
     *
     * @param videoId 视频id
     * @return 视频点赞量 该用户是否点赞了
     */
    @GetMapping("likes")
    public BaseResponse<Map<String, Object>> getVideoLikes(@RequestParam Long videoId) {
        if (videoId == null || videoId < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception ignored) {

        }
        Map<String, Object> result = videoLikeService.getVideoLikes(videoId, userId);
        return ResultUtils.success(result);
    }

    /**
     * 视频收藏
     *
     * @param videoCollection 请求体
     * @return 响应体
     */
    @PostMapping("/collect")
    public BaseResponse<Boolean> addVideoCollection(@RequestBody VideoCollection videoCollection) {
        if (videoCollection == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        videoCollectionService.addVideoCollection(videoCollection, userId);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 取消收藏视频
     *
     * @param videoCollection 请求体
     * @return 成功响应
     */
    @DeleteMapping("/collect")
    public BaseResponse<Boolean> cancelVideoCollection(@RequestBody VideoCollection videoCollection) {
        if (videoCollection == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        videoCollectionService.cancelVideoCollection(videoCollection, userId);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 获取视频收藏数量
     *
     * @param videoId 视频id
     * @return 视频收藏量 该用户是否收藏
     */
    @GetMapping("/collections")
    public BaseResponse<Map<String, Object>> getVideoCollections(@RequestParam Long videoId) {
        if (videoId == null || videoId < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception ignored) {

        }
        Map<String, Object> result = videoCollectionService.getVideoLikes(videoId, userId);
        return ResultUtils.success(result);
    }

    /**
     * 视频投币
     *
     * @param videoCoin 视频投币信息
     * @return 成功响应
     */
    @PostMapping("/coin")
    public BaseResponse<Boolean> addVideoCoin(@RequestBody VideoCoin videoCoin) {
        if (videoCoin == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        videoCoinService.addVideoCoin(videoCoin, userId);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 获取视频投币数量
     *
     * @param videoId 视频id
     * @return 视频投币量 该用户是否投币
     */
    @GetMapping("/coins")
    public BaseResponse<Map<String, Object>> getVideoCoins(@RequestParam Long videoId) {
        if (videoId == null || videoId < 0) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception ignored) {

        }
        Map<String, Object> result = videoCoinService.getVideoCoins(videoId, userId);
        return ResultUtils.success(result);
    }

    /**
     * 添加评论
     *
     * @param videoComment 视频评论
     * @return 成功响应
     */
    @PostMapping("/comment")
    public BaseResponse<Boolean> addVideoComment(@RequestBody VideoComment videoComment) {
        if (videoComment == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId = userSupport.getCurrentUserId();
        videoCommentService.addVideoComment(videoComment, userId);
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 分页获取评论列表
     *
     * @param request 分页请求
     * @return 分页评论结果
     */
    @GetMapping("/comments")
    public BaseResponse<IPage<VideoComment>> pageListVideoComments(@RequestBody VideoCommentsPageRequest request) {
        if (request == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Integer currentPage = request.getCurrentPage();
        Integer pageSize = request.getPageSize();
        Long videoId = request.getVideoId();
        IPage<VideoComment> result = videoCommentService.pageListVideoComments(currentPage, pageSize, videoId);
        return ResultUtils.success(result);
    }

    /**
     * 获取视频详情
     *
     * @param videoId 视频id
     * @return 视频详情
     */
    @GetMapping("/details")
    public BaseResponse<Map<String, Object>> getVideoDetails(Long videoId) {
        if (videoId == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Map<String, Object> result = videoService.getVideoDetails(videoId);
        return ResultUtils.success(result);
    }

    /**
     * es测试-获取相关视频
     *
     * @param keyWord 关键词
     * @return 相关视频
     */
    @GetMapping("/search")
    public BaseResponse<Video> getEsVideos(@RequestParam String keyWord) {
        if (keyWord == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Video video = elasticSearchService.getVideos(keyWord);
        return ResultUtils.success(video);
    }

    /**
     * 根据关键字获取相关信息
     *
     * @param keyWord     关键字
     * @param currentPage 当前页
     * @param pageSize    页面大小
     * @return 结果集
     */
    @GetMapping("/contents")
    public BaseResponse<List<Map<String, Object>>> getContents(@RequestParam String keyWord,
                                                               @RequestParam Integer currentPage,
                                                               @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(keyWord) || currentPage == null || pageSize == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        try {
            List<Map<String, Object>> result = elasticSearchService.getContents(keyWord, currentPage, pageSize);
            return ResultUtils.success(result);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return ResultUtils.success(Collections.emptyList());
    }

    /**
     * 增加观看记录
     *
     * @param videoView 观看记录
     * @param request   请求体
     * @return 成功响应
     */
    @PostMapping("/view")
    public BaseResponse<Boolean> addVideoView(@RequestBody VideoView videoView, HttpServletRequest request) {
        if (videoView == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long userId ;
        try {
            userId = userSupport.getCurrentUserId();
            videoView.setUserId(userId);
            videoViewService.addVideoView(videoView, request);
        } catch (Exception e) {
            videoViewService.addVideoView(videoView, request);
        }
        return ResultUtils.success(Boolean.TRUE);
    }

    /**
     * 查询播放量
     *
     * @param videoId 视频id
     * @return 播放量
     */
    @GetMapping("/view-count")
    public BaseResponse<Long> getVideoViewCounts(@RequestParam Long videoId) {
        if (videoId == null) {
            throw new BusinessException(PARAM_ERROR);
        }
        Long count = videoViewService.getVideoViewCounts(videoId);
        return ResultUtils.success(count);
    }
}
