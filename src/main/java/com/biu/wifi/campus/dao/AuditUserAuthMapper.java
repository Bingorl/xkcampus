package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AuditUserAuth;
import com.biu.wifi.campus.dao.model.AuditUserAuthCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditUserAuthMapper {
    long countByExample(AuditUserAuthCriteria example);

    int deleteByExample(AuditUserAuthCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(AuditUserAuth record);

    int insertSelective(AuditUserAuth record);

    List<AuditUserAuth> selectByExample(AuditUserAuthCriteria example);

    AuditUserAuth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AuditUserAuth record, @Param("example") AuditUserAuthCriteria example);

    int updateByExample(@Param("record") AuditUserAuth record, @Param("example") AuditUserAuthCriteria example);

    int updateByPrimaryKeySelective(AuditUserAuth record);

    int updateByPrimaryKey(AuditUserAuth record);
}