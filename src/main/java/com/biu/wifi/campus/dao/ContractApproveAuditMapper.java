package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ContractApproveAudit;
import com.biu.wifi.campus.dao.model.ContractApproveAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractApproveAuditMapper {
    long countByExample(ContractApproveAuditExample example);

    int deleteByExample(ContractApproveAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContractApproveAudit record);

    int insertSelective(ContractApproveAudit record);

    List<ContractApproveAudit> selectByExample(ContractApproveAuditExample example);

    ContractApproveAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContractApproveAudit record, @Param("example") ContractApproveAuditExample example);

    int updateByExample(@Param("record") ContractApproveAudit record, @Param("example") ContractApproveAuditExample example);

    int updateByPrimaryKeySelective(ContractApproveAudit record);

    int updateByPrimaryKey(ContractApproveAudit record);

    HashMap selectMap(@Param("approveId") Integer id, @Param("userId") String userId);
}