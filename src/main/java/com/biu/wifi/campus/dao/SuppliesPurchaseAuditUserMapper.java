package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SuppliesPurchaseAuditUser;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SuppliesPurchaseAuditUserMapper {
    long countByExample(SuppliesPurchaseAuditUserExample example);

    int deleteByExample(SuppliesPurchaseAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SuppliesPurchaseAuditUser record);

    int insertSelective(SuppliesPurchaseAuditUser record);

    List<SuppliesPurchaseAuditUser> selectByExample(SuppliesPurchaseAuditUserExample example);

    SuppliesPurchaseAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SuppliesPurchaseAuditUser record, @Param("example") SuppliesPurchaseAuditUserExample example);

    int updateByExample(@Param("record") SuppliesPurchaseAuditUser record, @Param("example") SuppliesPurchaseAuditUserExample example);

    int updateByPrimaryKeySelective(SuppliesPurchaseAuditUser record);

    int updateByPrimaryKey(SuppliesPurchaseAuditUser record);
}