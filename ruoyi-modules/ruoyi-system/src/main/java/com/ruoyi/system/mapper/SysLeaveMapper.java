package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysLeave;
import org.apache.ibatis.annotations.*;

import java.util.List;

// 为了简单，我们直接用注解写SQL，不用XML了，效果一样
@Mapper
public interface SysLeaveMapper {

    @Insert("INSERT INTO sys_leave(user_id, username, days, reason, status, create_time) " +
            "VALUES(#{userId}, #{username}, #{days}, #{reason}, '0', sysdate())")
    @Options(useGeneratedKeys = true, keyProperty = "leaveId")
    int insertLeave(SysLeave leave);

    // 强制把数据库的 create_time 字段，起别名为 Java 里的 createTime
    // 强制映射 leave_id 为 leaveId
    // 加上 user_id as userId
    // 加上 audit_comment as auditComment
    @Select("SELECT *, leave_id as leaveId, create_time as createTime, user_id as userId, audit_comment as auditComment FROM sys_leave WHERE leave_id = #{leaveId}")
    SysLeave selectLeaveById(Long leaveId);

    @Update("UPDATE sys_leave SET status = #{status}, audit_comment = #{auditComment}, update_time = sysdate() WHERE leave_id = #{leaveId}")
    int updateLeave(SysLeave leave);

    // 新增：查询列表
    List<SysLeave> selectLeaveList(SysLeave leave);
}