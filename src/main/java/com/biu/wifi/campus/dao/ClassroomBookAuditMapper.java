package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBookAudit;
import com.biu.wifi.campus.dao.model.ClassroomBookAuditCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBookAuditMapper {
    long countByExample(ClassroomBookAuditCriteria example);

    int deleteByExample(ClassroomBookAuditCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBookAudit record);

    int insertSelective(ClassroomBookAudit record);

    List<ClassroomBookAudit> selectByExample(ClassroomBookAuditCriteria example);

    ClassroomBookAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBookAudit record, @Param("example") ClassroomBookAuditCriteria example);

    int updateByExample(@Param("record") ClassroomBookAudit record, @Param("example") ClassroomBookAuditCriteria example);

    int updateByPrimaryKeySelective(ClassroomBookAudit record);

    int updateByPrimaryKey(ClassroomBookAudit record);
}