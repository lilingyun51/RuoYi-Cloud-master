package com.ruoyi.system.domain;

import com.ruoyi.common.core.web.domain.BaseEntity;

public class SysLeave extends BaseEntity {
    private Long leaveId;
    private Long userId;
    private String username;
    private Integer days;
    private String reason;
    private String status; // 0=待审批, 1=同意, 2=拒绝
    private String auditComment;

    // Getter 和 Setter 方法 (你可以用 Generate 自动生成，或者用 Lombok @Data)
    public Long getLeaveId() { return leaveId; }
    public void setLeaveId(Long leaveId) { this.leaveId = leaveId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAuditComment() { return auditComment; }
    public void setAuditComment(String auditComment) { this.auditComment = auditComment; }
}