package com.ruoyi.flowable.listener;

import com.ruoyi.common.message.service.MessageUtils;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作流任务到达监听器
 * 作用：当流程产生新任务时，自动通过我们封装的组件投递到 Kafka
 */
@Component("sendNoticeListener") // 这里的名字就是以后你在画流程图时要填的表达式
public class SendNoticeListener implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(SendNoticeListener.class);

    // 👈 直接注入你刚刚封装好的通用消息工具！
    @Autowired
    private MessageUtils messageUtils;

    @Override
    public void notify(DelegateTask delegateTask) {
        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {

            String assignee = delegateTask.getAssignee(); // 审批人ID
            String taskName = delegateTask.getName();     // 任务名称

            if (assignee != null && !assignee.trim().isEmpty()) {
                try {
                    Long receiverId = Long.valueOf(assignee);
                    String title = "工作流待办任务提醒";
                    String content = "您有新的流程任务需要处理：【" + taskName + "】";
                    String routerPath = "/work/todo"; // 前端待办页面路由

                    // 🚀 调用 SDK 发送消息！一行代码搞定！
                    messageUtils.sendNotice(title, content, receiverId, routerPath);

                } catch (NumberFormatException e) {
                    log.error("办理人ID格式不正确: {}", assignee);
                }
            }
        }
    }
}