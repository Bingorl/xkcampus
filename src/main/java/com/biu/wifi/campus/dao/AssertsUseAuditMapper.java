package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AssertsUseAudit;
import com.biu.wifi.campus.dao.model.AssertsUseAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssertsUseAuditMapper {
    long countByExample(AssertsUseAuditExample example);

    int deleteByExample(AssertsUseAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AssertsUseAudit record);

    int insertSelective(AssertsUseAudit record);

    List<AssertsUseAudit> selectByExample(AssertsUseAuditExample example);

    AssertsUseAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AssertsUseAudit record, @Param("example") AssertsUseAuditExample example);

    int updateByExample(@Param("record") AssertsUseAudit record, @Param("example") AssertsUseAuditExample example);

    int updateByPrimaryKeySelective(AssertsUseAudit record);

    int updateByPrimaryKey(AssertsUseAudit record);

    HashMap selectMap(@Param("useId") Integer id, @Param("userId") String userId);

}