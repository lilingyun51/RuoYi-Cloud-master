package com.ruoyi.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.domain.SysLeave;
import com.ruoyi.system.mapper.SysLeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.web.page.TableDataInfo; // 记得导入分页对象
import java.util.List;
/**
 * 请假业务控制器 (正式版 - 对应数据库 sys_leave 表)
 */
@RestController
@RequestMapping("/leave")
public class LeaveController extends BaseController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SysLeaveMapper leaveMapper; // 注入 Mapper，如果不注入就没法存库了

    /**
     * 1. 员工提交请假 -> 存库 -> 通知经理
     */
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody JSONObject leaveForm) {
        // A. 存入数据库
        SysLeave leave = new SysLeave();
        leave.setDays(leaveForm.getInteger("days"));
        leave.setReason(leaveForm.getString("reason"));
        leave.setUserId(SecurityUtils.getUserId()); // 记录是谁请的
        leave.setUsername(SecurityUtils.getUsername());

        // 插入后，leave.getLeaveId() 会自动有值 (MyBatis 自动回填)
        leaveMapper.insertLeave(leave);

        // B. 发送消息给经理 (Admin ID = 1)
        JSONObject msg = new JSONObject();
        msg.put("title", "待审批：员工 " + leave.getUsername() + " 的请假");
        msg.put("content", "申请人：" + leave.getUsername() + "，天数：" + leave.getDays() + "，原因：" + leave.getReason());
        msg.put("receiverId", 1L); // 发给经理 (假设经理ID=1)
        msg.put("routerPath", "/business/audit?id=" + leave.getLeaveId()); // 跳转到审批页
        msg.put("bizId", leave.getLeaveId());
        // 显式指定发送者为当前申请人的名字
        msg.put("sender", leave.getUsername());

        kafkaTemplate.send("sys_message_topic", msg.toJSONString());

        return success("提交成功");
    }

    /**
     * 2. 获取详情 (用于审批页回显)
     */
    @GetMapping(value = "/{leaveId}")
    public AjaxResult getInfo(@PathVariable("leaveId") Long leaveId) {
        SysLeave leave = leaveMapper.selectLeaveById(leaveId);
        if (leave == null) {
            return error("单据不存在");
        }
        return AjaxResult.success(leave);
    }

    /**
     * 3. 经理审批 -> 更新库 ->  回复消息给员工
     */
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody JSONObject auditForm) {
        Long leaveId = auditForm.getLong("leaveId");
        String result = auditForm.getString("result"); // "pass" 或 "reject"
        String comment = auditForm.getString("comment");

        // A. 查询原单据 (为了知道发给谁)
        SysLeave leave = leaveMapper.selectLeaveById(leaveId);
        if (leave == null) return error("单据不存在");

        // B. 更新数据库状态
        String statusStr = "pass".equals(result) ? "1" : "2"; // 1同意 2拒绝
        leave.setStatus(statusStr);
        leave.setAuditComment(comment);
        leaveMapper.updateLeave(leave);

        // C. 核心：反向通知员工 (双向通信)
        JSONObject msg = new JSONObject();

        // 标题：显示审批结果
        String resultText = "pass".equals(result) ? "【已通过】" : "【已拒绝】";
        msg.put("title", "审批通知：" + resultText + " 您的请假申请");

        // 内容
        msg.put("content",  "审批意见：" + (comment != null ? comment : "无"));

        // 接收人：必须是当初的申请人 ID (从数据库查出来的)
        msg.put("receiverId", leave.getUserId());

        // 跳转：让员工也可以点进去看详情 (复用审批页)
        msg.put("routerPath", "/business/audit?id=" + leave.getLeaveId());

        msg.put("bizId", leaveId);

        // 显式指定发送者为当前操作人（经理 Admin）的名字
        msg.put("sender", SecurityUtils.getUsername());

        kafkaTemplate.send("sys_message_topic", msg.toJSONString());

        return success("审批完成，已通知员工！");
    }

    /**
     * 查询请假记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysLeave leave) {
        startPage(); // 开启分页

        // 核心逻辑：判断当前登录人是不是管理员
        // SecurityUtils.getUserId() 获取当前人ID
        // 如果 ID 不是 1 (超级管理员)，则强制只查自己的数据
        Long currentUserId = SecurityUtils.getUserId();

        if (!SecurityUtils.isAdmin(currentUserId)) {
            // 如果不是Admin，强制把查询条件里的 userId 设为当前用户
            leave.setUserId(currentUserId);
        }

        // 如果是 Admin，leave.setUserId 就不设值，MyBatis 就会查所有
        List<SysLeave> list = leaveMapper.selectLeaveList(leave);

        return getDataTable(list);
    }

}