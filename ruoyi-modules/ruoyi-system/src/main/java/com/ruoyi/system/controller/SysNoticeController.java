package com.ruoyi.system.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

import static com.ruoyi.common.security.utils.SecurityUtils.getUserId;
import static com.ruoyi.common.security.utils.SecurityUtils.getUsername;
import com.ruoyi.system.api.domain.SysUser; // 引入 SysUser
import com.ruoyi.system.service.ISysUserService;   // 引入 UserService

/**
 * 公告 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;

    //  1. 注入 Kafka
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // 新增：注入用户服务，用来查所有员工
    @Autowired
    private ISysUserService userService;

    /**
     * 获取通知公告列表
     */
    @RequiresPermissions("system:notice:list")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @RequiresPermissions("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(getUsername());
        int rows = noticeService.insertNotice(notice);

        if (rows > 0) {
            // 1. 查询所有正常状态的用户 (status = 0)
            SysUser userQuery = new SysUser();
            userQuery.setStatus("0");
            List<SysUser> allUsers = userService.selectUserList(userQuery);

            // 2. 循环给每个人发消息
            for (SysUser user : allUsers) {
                // 可选：排除掉发布人自己 (防止自己收到自己的通知)
                if (user.getUserId().equals(getUserId())) {
                    continue;
                }

                JSONObject msg = new JSONObject();
                msg.put("title", "有新的公告");
                msg.put("content", notice.getNoticeTitle());
                // 核心：带上公告ID跳转
                msg.put("routerPath", "/system/notice?id=" + notice.getNoticeId());

                // 关键：每次循环填入不同的 userId
                msg.put("receiverId", user.getUserId());
                msg.put("sender", getUsername());

                kafkaTemplate.send("sys_message_topic", msg.toJSONString());
            }
        }

        return toAjax(rows);
    }

    /**
     * 修改通知公告
     */
    @RequiresPermissions("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @RequiresPermissions("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
