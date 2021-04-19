package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TeacherLeaveAuditUser;
import com.biu.wifi.campus.dao.model.TeacherLeaveAuditUserExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 张彬.
 * @date 2021/4/4 0:24.
 */
@Repository
public interface TeacherLeaveAuditUserMapper {
    long countByExample(TeacherLeaveAuditUserExample example);

    int deleteByExample(TeacherLeaveAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TeacherLeaveAuditUser record);

    int insertSelective(TeacherLeaveAuditUser record);

    List<TeacherLeaveAuditUser> selectByExample(TeacherLeaveAuditUserExample example);

    TeacherLeaveAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TeacherLeaveAuditUser record, @Param("example") TeacherLeaveAuditUserExample example);

    int updateByExample(@Param("record") TeacherLeaveAuditUser record, @Param("example") TeacherLeaveAuditUserExample example);

    int updateByPrimaryKeySelective(TeacherLeaveAuditUser record);

    int updateByPrimaryKey(TeacherLeaveAuditUser record);

    List<TeacherLeaveAuditUser> selectOneByTypeAndInstituteId(@Param("type") int type,@Param("instituteId") int instituteId);
}