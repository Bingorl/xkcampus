package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AssertsUseAuditUser;
import com.biu.wifi.campus.dao.model.AssertsUseAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssertsUseAuditUserMapper {
    long countByExample(AssertsUseAuditUserExample example);

    int deleteByExample(AssertsUseAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AssertsUseAuditUser record);

    int insertSelective(AssertsUseAuditUser record);

    List<AssertsUseAuditUser> selectByExample(AssertsUseAuditUserExample example);

    AssertsUseAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AssertsUseAuditUser record, @Param("example") AssertsUseAuditUserExample example);

    int updateByExample(@Param("record") AssertsUseAuditUser record, @Param("example") AssertsUseAuditUserExample example);

    int updateByPrimaryKeySelective(AssertsUseAuditUser record);

    int updateByPrimaryKey(AssertsUseAuditUser record);
}