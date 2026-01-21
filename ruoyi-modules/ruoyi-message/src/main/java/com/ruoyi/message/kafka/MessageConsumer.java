package com.ruoyi.message.kafka;

import com.ruoyi.message.domain.SysUserMessage;
import com.ruoyi.message.service.ISysUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class MessageConsumer
{
    @Autowired
    private ISysUserMessageService messageService;

    /**
     * 监听消息队列
     * 重要修改：这里参数类型必须是 String，因为生产者发的是 String
     */
    @KafkaListener(topics = "sys_message_topic", groupId = "ruoyi-message-group")
    public void listen(String messageStr)
    {
        try {
            // 1. 解析 JSON
            com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSON.parseObject(messageStr);

            // 2. 映射到实体类
            SysUserMessage message = new SysUserMessage();
            message.setTitle(json.getString("title"));
            message.setContent(json.getString("content"));
            message.setReceiverId(json.getLong("receiverId")); // 存入接收人ID
            message.setRouterPath(json.getString("routerPath")); // 存入跳转路径
            message.setBizId(json.getLong("bizId"));

            // 默认值
            message.setSender("System");
            message.setReadStatus("0");
            message.setCreateTime(new java.util.Date());

            // 3. 入库
            messageService.insertSysUserMessage(message);
            System.out.println("✅ 消息已入库！通知经理 ID=" + message.getReceiverId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}