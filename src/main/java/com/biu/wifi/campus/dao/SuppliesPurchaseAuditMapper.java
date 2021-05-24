package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SuppliesPurchaseAudit;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseAuditExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliesPurchaseAuditMapper {
    long countByExample(SuppliesPurchaseAuditExample example);

    int deleteByExample(SuppliesPurchaseAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SuppliesPurchaseAudit record);

    int insertSelective(SuppliesPurchaseAudit record);

    List<SuppliesPurchaseAudit> selectByExample(SuppliesPurchaseAuditExample example);

    SuppliesPurchaseAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SuppliesPurchaseAudit record, @Param("example") SuppliesPurchaseAuditExample example);

    int updateByExample(@Param("record") SuppliesPurchaseAudit record, @Param("example") SuppliesPurchaseAuditExample example);

    int updateByPrimaryKeySelective(SuppliesPurchaseAudit record);

    int updateByPrimaryKey(SuppliesPurchaseAudit record);
}