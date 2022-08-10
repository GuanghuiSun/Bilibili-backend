package com.bilibili.repository;

import com.bilibili.model.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 基于spring data提供的一些es相关服务
 *
 * @author sgh
 * @date 2022-8-9
 */
public interface VideoRepository extends ElasticsearchRepository<Video, Long> {

    /**
     * find by title like
     * @param keyWord 关键词
     * @return 结果
     */
    Video findByTitleLike(String keyWord);

}
