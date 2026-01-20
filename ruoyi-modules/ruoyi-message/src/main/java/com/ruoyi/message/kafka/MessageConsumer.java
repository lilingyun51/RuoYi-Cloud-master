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
    public void listen(String content)
    {
        System.out.println("------------------------------------------");
        System.out.println("【消费者】收到消息: " + content);

        try {
            // 1. 手动创建对象
            SysUserMessage message = new SysUserMessage();
            message.setContent(content);          // 把收到的字符串放进去
            message.setSender("System");          // 默认发送者
            message.setReceiver("All User");      // 默认接收者
            message.setStatus("0");               // 状态：未读
            message.setCreateTime(new Date());

            // 2. 保存到数据库
            messageService.insertSysUserMessage(message);
            System.out.println("【消费者】数据库保存成功！(ID: " + message.getMessageId() + ")");
        } catch (Exception e) {
            System.err.println("【消费者】保存失败：" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("------------------------------------------");
    }
}