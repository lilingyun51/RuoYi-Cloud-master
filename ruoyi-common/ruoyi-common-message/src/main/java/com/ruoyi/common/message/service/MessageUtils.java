package com.ruoyi.common.message.service;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用 Kafka 消息发送组件
 */
@Component
public class MessageUtils {

    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    // 默认发送的 Topic，必须和你的消费者监听的 Topic 保持一致
    private static final String TOPIC = "sys_message_topic";

    @Autowired(required = false) // required=false 允许在没有配置Kafka的环境下不报错
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送系统通知
     *
     * @param title      消息标题
     * @param content    消息内容
     * @param receiverId 接收人ID (对应 sys_user 表的主键)
     * @param routerPath 前端点击消息后的跳转路由
     */
    public void sendNotice(String title, String content, Long receiverId, String routerPath) {
        if (kafkaTemplate == null) {
            log.warn("未检测到 Kafka 配置，消息发送已跳过。标题：{}", title);
            return;
        }

        try {
            // 1. 组装消息体结构 (使用 Map 解耦，不依赖具体的实体类)
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("title", title);
            msgMap.put("content", content);
            msgMap.put("receiverId", receiverId);
            msgMap.put("routerPath", routerPath);
            msgMap.put("createTime", new Date());
            msgMap.put("readStatus", "0"); // 0表示未读
            msgMap.put("sender", "System");

            // 2. 转为 JSON 并发送
            String jsonStr = JSON.toJSONString(msgMap);
            kafkaTemplate.send(TOPIC, jsonStr);

            log.info("✅ [MessageUtils] 消息已投递至 Kafka, 接收人ID: {}", receiverId);
        } catch (Exception e) {
            log.error("❌ Kafka 消息发送异常", e);
        }
    }
}