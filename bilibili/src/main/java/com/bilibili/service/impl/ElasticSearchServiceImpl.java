package com.bilibili.service.impl;

import com.bilibili.model.domain.UserInfo;
import com.bilibili.model.domain.Video;
import com.bilibili.repository.UserInfoRepository;
import com.bilibili.repository.VideoRepository;
import com.bilibili.service.ElasticSearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * es服务实现类
 */
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Resource
    private VideoRepository videoRepository;

    @Resource
    private UserInfoRepository userInfoRepository;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<Map<String, Object>> getContents(String keyWord, Integer currentPage, Integer pageSize) throws IOException {
        String[] indices = {"videos", "user-infos"};
        SearchRequest searchRequest = new SearchRequest(indices);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(currentPage - 1);
        sourceBuilder.size(pageSize);
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery(keyWord, "title", "nick", "description");
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //高亮显示
        String[] array = {"title", "nick", "description"};
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String key : array) {
            highlightBuilder.fields().add(new HighlightBuilder.Field(key));
        }
        highlightBuilder.requireFieldMatch(false);//允许多个字段高亮
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        //执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            //处理高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            for (String key : array) {
                HighlightField field = highlightFields.get(key);
                if (field != null) {
                    Text[] fragments = field.fragments();
                    String str = Arrays.toString(fragments);
                    str = str.substring(1, str.length() - 1);
                    sourceMap.put(key, str);
                }
            }
            result.add(sourceMap);
        }
        return result;
    }


    @Override
    public void addVideo(Video video) {
        videoRepository.save(video);
    }

    @Override
    public Video getVideos(String keyWord) {
        return videoRepository.findByTitleLike(keyWord);
    }

    @Override
    public void deleteAllVideos() {
        videoRepository.deleteAll();
    }

    @Override
    public void addUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}
