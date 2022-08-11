package com.bilibili.consumer;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.exception.BusinessException;
import com.bilibili.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.bilibili.base.ErrorCode.REQUEST_SERVICE_ERROR;
import static com.bilibili.constant.KafkaConstant.BSC_SEND_TOPIC;
import static com.bilibili.constant.MessageConstant.MESSAGE_ERROR;


/**
 * kafka消费者 - 弹幕发送
 *
 * @author sgh
 */
@Component
@Slf4j
public class BscSendConsumer {

    /**
     * 将弹幕消息发送给前端
     *
     * @param record 消息记录
     */
    @KafkaListener(id = "bscSendConsumer", topics = BSC_SEND_TOPIC, groupId = "bilibili")
    public void consumeSendBscMessage(ConsumerRecord<?, ?> record) {
        //接收并处理消息
        JSONObject json = (JSONObject) record.value();
        Long sessionId = json.getLong("sessionId");
        String message = json.getString("message");
        if (sessionId == null || message == null) {
            throw new BusinessException(REQUEST_SERVICE_ERROR, MESSAGE_ERROR);
        }
        WebSocketService webSocketService = WebSocketService.WEBSOCKET_MAP.get(sessionId);
        if (webSocketService.getSession().isOpen()) {
            try {
                //发送消息
                webSocketService.sendMessage(message);
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
