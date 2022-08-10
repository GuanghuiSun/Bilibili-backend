package com.bilibili.service;

import com.bilibili.model.domain.UserInfo;
import com.bilibili.model.domain.Video;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * es相关服务
 *
 * @author sgh
 * @date 2002-8-9
 */
public interface ElasticSearchService {

    /**
     * 添加视频到es
     *
     * @param video 视频
     */
    void addVideo(Video video);

    /**
     * 获取视频
     *
     * @param keyWord 关键字
     * @return 相关视频
     */
    Video getVideos(String keyWord);

    /**
     * 删除视频
     */
    void deleteAllVideos();

    /**
     * 添加用户信息到es
     *
     * @param userInfo 用户信息
     */
    void addUserInfo(UserInfo userInfo);

    /**
     * 获取相关信息
     *
     * @param keyWord     关键字
     * @param currentPage 当期页
     * @param pageSize    页面大小
     * @return 结果集
     */
    List<Map<String, Object>> getContents(String keyWord, Integer currentPage, Integer pageSize) throws IOException;
}
