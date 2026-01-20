package com.ruoyi.system.service;

/**
 * 消息生产接口
 * 
 * @author ruoyi
 */
public interface ISysMessageProducer
{
    /**
     * 发送消息
     * 
     * @param topic 主题
     * @param msg 消息内容
     */
    public void sendMessage(String topic, String msg);
}
