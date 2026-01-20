package com.ruoyi.message.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.message.mapper.SysUserMessageMapper;
import com.ruoyi.message.domain.SysUserMessage;
import com.ruoyi.message.service.ISysUserMessageService;

/**
 * 用户消息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-19
 */
@Service
public class SysUserMessageServiceImpl implements ISysUserMessageService 
{
    @Autowired
    private SysUserMessageMapper sysUserMessageMapper;

    /**
     * 查询用户消息
     * 
     * @param messageId 用户消息主键
     * @return 用户消息
     */
    @Override
    public SysUserMessage selectSysUserMessageByMessageId(Long messageId)
    {
        return sysUserMessageMapper.selectSysUserMessageByMessageId(messageId);
    }

    /**
     * 查询用户消息列表
     * 
     * @param sysUserMessage 用户消息
     * @return 用户消息
     */
    @Override
    public List<SysUserMessage> selectSysUserMessageList(SysUserMessage sysUserMessage)
    {
        return sysUserMessageMapper.selectSysUserMessageList(sysUserMessage);
    }

    /**
     * 新增用户消息
     * 
     * @param sysUserMessage 用户消息
     * @return 结果
     */
    @Override
    public int insertSysUserMessage(SysUserMessage sysUserMessage)
    {
        sysUserMessage.setCreateTime(DateUtils.getNowDate());
        return sysUserMessageMapper.insertSysUserMessage(sysUserMessage);
    }

    /**
     * 修改用户消息
     * 
     * @param sysUserMessage 用户消息
     * @return 结果
     */
    @Override
    public int updateSysUserMessage(SysUserMessage sysUserMessage)
    {
        return sysUserMessageMapper.updateSysUserMessage(sysUserMessage);
    }

    /**
     * 批量删除用户消息
     * 
     * @param messageIds 需要删除的用户消息主键
     * @return 结果
     */
    @Override
    public int deleteSysUserMessageByMessageIds(Long[] messageIds)
    {
        return sysUserMessageMapper.deleteSysUserMessageByMessageIds(messageIds);
    }

    /**
     * 删除用户消息信息
     * 
     * @param messageId 用户消息主键
     * @return 结果
     */
    @Override
    public int deleteSysUserMessageByMessageId(Long messageId)
    {
        return sysUserMessageMapper.deleteSysUserMessageByMessageId(messageId);
    }
}
