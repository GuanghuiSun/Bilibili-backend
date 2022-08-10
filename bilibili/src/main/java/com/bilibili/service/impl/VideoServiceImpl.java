package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.mapper.VideoTagMapper;
import com.bilibili.model.domain.UserInfo;
import com.bilibili.model.domain.Video;
import com.bilibili.model.domain.VideoTag;
import com.bilibili.service.ElasticSearchService;
import com.bilibili.service.UserInfoService;
import com.bilibili.service.VideoService;
import com.bilibili.mapper.VideoMapper;
import com.bilibili.utils.FastDFSUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bilibili.base.ErrorCode.GET_SERVICE_ERROR;
import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.VIDEO_NOT_EXIST_ERROR;

/**
 * @author sgh
 * @description 针对表【t_video(视频投稿记录表)】的数据库操作Service实现
 * @createDate 2022-08-06 17:12:38
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VideoTagMapper videoTagMapper;

    @Resource
    private FastDFSUtil fastDFSUtil;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private ElasticSearchService elasticSearchService;

    @Override
    public void addVideo(Video video) {
        this.save(video);
        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        videoTagList.forEach(item -> item.setVideoId(videoId));
        videoTagMapper.batchAddVideoTags(videoTagList);
        //添加到es
        elasticSearchService.addVideo(video);
    }

    @Override
    public IPage<Video> queryVideosByArea(int currentPage, int pageSize, String area) {
        IPage<Video> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(area != null, Video::getArea, area).orderByDesc(Video::getId);
        IPage<Video> videoIPage = videoMapper.selectPage(page, wrapper);
        if (videoIPage == null) {
            throw new BusinessException(GET_SERVICE_ERROR);
        }
        return videoIPage;
    }

    @Override
    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url) {
        fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
    }

    @Override
    public Map<String, Object> getVideoDetails(Long videoId) {
        Video video = this.getById(videoId);
        if (video == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, VIDEO_NOT_EXIST_ERROR);
        }
        Long userId = video.getUserId();
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserId, userId);
        UserInfo user = userInfoService.getOne(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("video", video);
        result.put("user", user);
        return result;
    }

}




