package com.ruoyi.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.system.service.ISysMessageProducer;

/**
 * 测试消息发送
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/test")
public class TestMessageController extends BaseController
{
    @Autowired
    private ISysMessageProducer messageProducer;

    /**
     * 发送消息测试
     */
    @GetMapping("/send")
    public AjaxResult send(@RequestParam("msg") String msg)
    {
        messageProducer.sendMessage("sys_message_topic", msg);
        return success("消息发送成功");
    }
}
