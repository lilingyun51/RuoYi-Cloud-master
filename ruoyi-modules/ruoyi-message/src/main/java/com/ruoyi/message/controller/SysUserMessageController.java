package com.ruoyi.message.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.message.domain.SysUserMessage;
import com.ruoyi.message.service.ISysUserMessageService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 用户消息Controller
 * 
 * @author ruoyi
 * @date 2026-01-19
 */
@RestController
@RequestMapping("/message")
public class SysUserMessageController extends BaseController
{
    @Autowired
    private ISysUserMessageService sysUserMessageService;

    /**
     * 查询用户消息列表
     */
    @RequiresPermissions("message:message:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUserMessage sysUserMessage)
    {
        startPage();
        List<SysUserMessage> list = sysUserMessageService.selectSysUserMessageList(sysUserMessage);
        return getDataTable(list);
    }

    /**
     * 导出用户消息列表
     */
    @RequiresPermissions("message:message:export")
    @Log(title = "用户消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUserMessage sysUserMessage)
    {
        List<SysUserMessage> list = sysUserMessageService.selectSysUserMessageList(sysUserMessage);
        ExcelUtil<SysUserMessage> util = new ExcelUtil<SysUserMessage>(SysUserMessage.class);
        util.exportExcel(response, list, "用户消息数据");
    }

    /**
     * 获取用户消息详细信息
     */
    @RequiresPermissions("message:message:query")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return success(sysUserMessageService.selectSysUserMessageByMessageId(messageId));
    }

    /**
     * 新增用户消息
     */
    @RequiresPermissions("message:message:add")
    @Log(title = "用户消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUserMessage sysUserMessage)
    {
        return toAjax(sysUserMessageService.insertSysUserMessage(sysUserMessage));
    }

    /**
     * 修改用户消息
     */
    @RequiresPermissions("message:message:edit")
    @Log(title = "用户消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserMessage sysUserMessage)
    {
        return toAjax(sysUserMessageService.updateSysUserMessage(sysUserMessage));
    }

    /**
     * 删除用户消息
     */
    @RequiresPermissions("message:message:remove")
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(sysUserMessageService.deleteSysUserMessageByMessageIds(messageIds));
    }
}
