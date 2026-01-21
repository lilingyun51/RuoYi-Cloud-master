package com.ruoyi.message.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 用户消息对象 sys_user_message
 */
public class SysUserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long messageId;

    /** 消息标题 */
    @Excel(name = "消息标题")
    private String title;

    /** 发送者 */
    @Excel(name = "发送者")
    private String sender;

    /** 接收者ID */
    @Excel(name = "接收者ID")
    private Long receiverId;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String content;

    /** 前端路由地址 (核心字段：点击跳转用) */
    private String routerPath;

    /** 阅读状态 (0=未读, 1=已读) */
    @Excel(name = "阅读状态", readConverterExp = "0=未读,1=已读")
    private String readStatus;

    /** 关联业务ID */
    private Long bizId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    // === Getter / Setter 方法 ===

    public void setMessageId(Long messageId) { this.messageId = messageId; }
    public Long getMessageId() { return messageId; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setSender(String sender) { this.sender = sender; }
    public String getSender() { return sender; }

    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public Long getReceiverId() { return receiverId; }

    public void setContent(String content) { this.content = content; }
    public String getContent() { return content; }

    public void setRouterPath(String routerPath) { this.routerPath = routerPath; }
    public String getRouterPath() { return routerPath; }

    public void setReadStatus(String readStatus) { this.readStatus = readStatus; }
    public String getReadStatus() { return readStatus; }

    public void setBizId(Long bizId) { this.bizId = bizId; }
    public Long getBizId() { return bizId; }

    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("messageId", getMessageId())
                .append("title", getTitle())
                .append("sender", getSender())
                .append("receiverId", getReceiverId())
                .append("content", getContent())
                .append("routerPath", getRouterPath())
                .append("readStatus", getReadStatus())
                .append("bizId", getBizId())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .toString();
    }
}