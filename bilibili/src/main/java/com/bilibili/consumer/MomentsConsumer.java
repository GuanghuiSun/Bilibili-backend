package com.bilibili.consumer;

import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.UserFollowing;
import com.bilibili.model.domain.UserMoments;
import com.bilibili.service.UserFollowingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.KafkaConstant.MOMENTS_TOPIC;
import static com.bilibili.constant.MessageConstant.MESSAGE_ERROR;
import static com.bilibili.constant.RedisConstant.SUBSCRIBED_INBOX;

/**
 * kafka消费者 - 动态
 *
 * @author sgh
 * @date 2022-8-4
 */
@Component
@Slf4j
public class MomentsConsumer {

    @Resource
    private UserFollowingService userFollowingService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 消息推送给粉丝的收件箱
     *
     * @param record 记录
     */
    @KafkaListener(id = "momentsConsumer", topics = MOMENTS_TOPIC, groupId = "bilibili")
    public void consumeMomentsMessage(ConsumerRecord<?, ?> record) {
        //接收并处理消息
        UserMoments userMoments = (UserMoments) record.value();
        if (userMoments == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, MESSAGE_ERROR);
        }
        Long momentsId = userMoments.getId();
        //获取所有粉丝列表
        Long userId = userMoments.getUserId();
        List<UserFollowing> userFans = userFollowingService.getUserFans(userId);
        for (UserFollowing fan : userFans) {
            //向redis中发送消息
            String key = SUBSCRIBED_INBOX + ":" + fan.getUserId();
            stringRedisTemplate.opsForZSet().add(key, String.valueOf(momentsId), System.currentTimeMillis());
        }
    }
}
