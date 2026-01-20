package com.ruoyi.message.mapper;

import java.util.List;
import com.ruoyi.message.domain.SysUserMessage;

/**
 * 用户消息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-19
 */
public interface SysUserMessageMapper 
{
    /**
     * 查询用户消息
     * 
     * @param messageId 用户消息主键
     * @return 用户消息
     */
    public SysUserMessage selectSysUserMessageByMessageId(Long messageId);

    /**
     * 查询用户消息列表
     * 
     * @param sysUserMessage 用户消息
     * @return 用户消息集合
     */
    public List<SysUserMessage> selectSysUserMessageList(SysUserMessage sysUserMessage);

    /**
     * 新增用户消息
     * 
     * @param sysUserMessage 用户消息
     * @return 结果
     */
    public int insertSysUserMessage(SysUserMessage sysUserMessage);

    /**
     * 修改用户消息
     * 
     * @param sysUserMessage 用户消息
     * @return 结果
     */
    public int updateSysUserMessage(SysUserMessage sysUserMessage);

    /**
     * 删除用户消息
     * 
     * @param messageId 用户消息主键
     * @return 结果
     */
    public int deleteSysUserMessageByMessageId(Long messageId);

    /**
     * 批量删除用户消息
     * 
     * @param messageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserMessageByMessageIds(Long[] messageIds);
}
