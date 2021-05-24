package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ContractApproveAuditUser;
import com.biu.wifi.campus.dao.model.ContractApproveAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractApproveAuditUserMapper {
    long countByExample(ContractApproveAuditUserExample example);

    int deleteByExample(ContractApproveAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContractApproveAuditUser record);

    int insertSelective(ContractApproveAuditUser record);

    List<ContractApproveAuditUser> selectByExample(ContractApproveAuditUserExample example);

    ContractApproveAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContractApproveAuditUser record, @Param("example") ContractApproveAuditUserExample example);

    int updateByExample(@Param("record") ContractApproveAuditUser record, @Param("example") ContractApproveAuditUserExample example);

    int updateByPrimaryKeySelective(ContractApproveAuditUser record);

    int updateByPrimaryKey(ContractApproveAuditUser record);
}