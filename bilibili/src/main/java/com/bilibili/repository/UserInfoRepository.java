package com.bilibili.repository;

import com.bilibili.model.domain.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserInfoRepository extends ElasticsearchRepository<UserInfo, Long> {
}
