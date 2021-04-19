package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TeacherLeaveAudit;
import com.biu.wifi.campus.dao.model.TeacherLeaveAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 张彬.
 * @date 2021/4/4 0:43.
 */
@Repository
public interface TeacherLeaveAuditMapper {
    long countByExample(TeacherLeaveAuditExample example);

    int deleteByExample(TeacherLeaveAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TeacherLeaveAudit record);

    int insertSelective(TeacherLeaveAudit record);

    List<TeacherLeaveAudit> selectByExample(TeacherLeaveAuditExample example);

    TeacherLeaveAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TeacherLeaveAudit record, @Param("example") TeacherLeaveAuditExample example);

    int updateByExample(@Param("record") TeacherLeaveAudit record, @Param("example") TeacherLeaveAuditExample example);

    int updateByPrimaryKeySelective(TeacherLeaveAudit record);

    int updateByPrimaryKey(TeacherLeaveAudit record);

    HashMap selectMap(@Param("leaveId") Integer leaveId, @Param("userId") String userId);
}