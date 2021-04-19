package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AuditInfo;
import com.biu.wifi.campus.dao.model.AuditInfoCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditInfoMapper {
    long countByExample(AuditInfoCriteria example);

    int deleteByExample(AuditInfoCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(AuditInfo record);

    int insertSelective(AuditInfo record);

    List<AuditInfo> selectByExample(AuditInfoCriteria example);

    AuditInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AuditInfo record, @Param("example") AuditInfoCriteria example);

    int updateByExample(@Param("record") AuditInfo record, @Param("example") AuditInfoCriteria example);

    int updateByPrimaryKeySelective(AuditInfo record);

    int updateByPrimaryKey(AuditInfo record);

    int update(@Param("bizId") Integer bizId,@Param("userId") Integer userId,@Param("type")  short type,@Param("isPass")  Short isPass);
}