package com.bilibili.consumer;

import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.BulletScreenComments;
import com.bilibili.service.BulletScreenCommentsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.KafkaConstant.BSC_SAVE_TOPIC;
import static com.bilibili.constant.MessageConstant.MESSAGE_ERROR;

/**
 * kafka消费者 - 用于存储弹幕到数据库
 */
@Component
@Slf4j
public class BscSaveConsumer {

    @Resource
    private BulletScreenCommentsService bulletScreenCommentsService;

    /**
     * 将弹幕消息发送给前端
     *
     * @param record 消息记录
     */
    @KafkaListener(id = "bscSaveConsumer", topics = BSC_SAVE_TOPIC, groupId = "bilibili")
    public void consumeBscSaveMessage(ConsumerRecord<?, ?> record) {
        //接收并处理消息
        BulletScreenComments comment = (BulletScreenComments) record.value();
        if (comment == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, MESSAGE_ERROR);
        }
        bulletScreenCommentsService.save(comment);
    }
}
