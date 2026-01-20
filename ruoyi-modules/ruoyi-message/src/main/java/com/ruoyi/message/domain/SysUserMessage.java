package com.ruoyi.message.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 用户消息对象 sys_user_message
 * 
 * @author ruoyi
 * @date 2026-01-19
 */
public class SysUserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long messageId;

    /** 发送者 */
    @Excel(name = "发送者")
    private String sender;

    /** 接收者 */
    @Excel(name = "接收者")
    private String receiver;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String content;

    /** 状态（0未读 1已读） */
    @Excel(name = "状态", readConverterExp = "0=未读,1=已读")
    private String status;

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setSender(String sender) 
    {
        this.sender = sender;
    }

    public String getSender() 
    {
        return sender;
    }

    public void setReceiver(String receiver) 
    {
        this.receiver = receiver;
    }

    public String getReceiver() 
    {
        return receiver;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("sender", getSender())
            .append("receiver", getReceiver())
            .append("content", getContent())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
