package com.ruoyi.message.kafka;

import com.ruoyi.message.domain.SysUserMessage;
import com.ruoyi.message.service.ISysUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Date;
import com.ruoyi.message.websocket.MessageWebSocketServer;

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


            // 修复：尝试从 JSON 里获取 sender
            String senderName = json.getString("sender");
            if (senderName != null && !senderName.isEmpty()) {
                message.setSender(senderName);
            } else {
                message.setSender("System"); // 只有真的没传名字时，才用 System
            }

            message.setReadStatus("0");
            message.setCreateTime(new java.util.Date());

            // 3. 入库
            messageService.insertSysUserMessage(message);

            //新增】把生成的 ID 塞进 JSON 发给前端
            json.put("messageId", message.getMessageId());

            // 【新增】4. 实时推送
            // 直接调用 WebSocket 的静态方法发送,这里要发新的 json.toJSONString()
            MessageWebSocketServer.sendToUser(message.getReceiverId(), json.toJSONString());

            System.out.println("✅ 消息已存库并尝试推送给用户 ID=" + message.getReceiverId());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}