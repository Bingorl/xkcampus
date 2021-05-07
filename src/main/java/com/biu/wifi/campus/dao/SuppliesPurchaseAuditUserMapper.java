package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SuppliesPurchaseAuditUser;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseAuditUserExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
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

    List<SuppliesPurchaseAuditUser> selectOneByTypeAndInstituteId(@Param("type") Integer type,@Param("instituteId")Integer instituteId);

    HashMap selectMap(@Param("purchaseId") Integer id,@Param("userId") String userId);
}