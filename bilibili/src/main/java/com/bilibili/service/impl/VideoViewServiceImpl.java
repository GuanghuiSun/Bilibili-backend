package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.model.domain.VideoView;
import com.bilibili.service.VideoViewService;
import com.bilibili.mapper.VideoViewMapper;
import com.bilibili.utils.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sgh
 * @description 针对表【t_video_view(视频观看统计表)】的数据库操作Service实现
 * @createDate 2022-08-09 22:31:36
 */
@Service
public class VideoViewServiceImpl extends ServiceImpl<VideoViewMapper, VideoView>
        implements VideoViewService {

    @Resource
    private VideoViewMapper videoViewMapper;

    @Override
    public void addVideoView(VideoView videoView, HttpServletRequest request) {
        Long userId = videoView.getUserId();
        Long videoId = videoView.getVideoId();
        //生成ClientId
        String agent = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        String clientId = String.valueOf(userAgent.getId());
        String ip = IpUtil.getIP(request);
        Map<String, Object> params = new HashMap<>();
        if (userId != null) {
            params.put("useId", userId);
        } else {
            params.put("ip", ip);
            params.put("clientId", clientId);
        }
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        params.put("today", sdf.format(now));
        params.put("videoId", videoId);
        //添加观看记录
        VideoView dbVideoView = videoViewMapper.getVideoView(params);
        if (dbVideoView == null) {
            videoView.setIp(ip);
            videoView.setClientId(clientId);
            this.save(videoView);
        }
    }

    @Override
    public Long getVideoViewCounts(Long videoId) {
        LambdaQueryWrapper<VideoView> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VideoView::getVideoId, videoId);
        return this.count(wrapper);
    }
}




