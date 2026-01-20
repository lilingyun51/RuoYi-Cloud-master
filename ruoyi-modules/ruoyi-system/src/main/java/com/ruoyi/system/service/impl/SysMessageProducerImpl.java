package com.ruoyi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.ruoyi.system.service.ISysMessageProducer;

/**
 * 消息生产实现
 * 
 * @author ruoyi
 */
@Service
public class SysMessageProducerImpl implements ISysMessageProducer
{
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String msg)
    {
        kafkaTemplate.send(topic, msg);
    }
}
