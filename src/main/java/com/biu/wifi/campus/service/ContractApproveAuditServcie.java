package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ContractApproveAuditMapper;
import com.biu.wifi.campus.dao.model.ContractApproveAudit;
import com.biu.wifi.campus.dao.model.ContractApproveAuditExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class ContractApproveAuditServcie {
    @Autowired
    private ContractApproveAuditMapper contractApproveAuditMapper;


    public Integer add(ContractApproveAudit contractApproveAudit) {
        contractApproveAudit.setCreateTime(new Date());
        return contractApproveAuditMapper.insertSelective(contractApproveAudit);
    }

    public ContractApproveAudit selectByApproveId(Integer id, int currentAuditUserId) {
        ContractApproveAuditExample example=new ContractApproveAuditExample();
        example.createCriteria().andApproveIdEqualTo(id).andUserIdEqualTo(currentAuditUserId);
        List<ContractApproveAudit> list = contractApproveAuditMapper.selectByExample(example);
        return  list.size()!=0?list.get(0):null;
    }

    public Integer update(ContractApproveAudit contractApproveAudit) {
        ContractApproveAuditExample example=new ContractApproveAuditExample();
        example.createCriteria().andIdEqualTo(contractApproveAudit.getId());
        contractApproveAudit.setUpdateTime(new Date());
        return contractApproveAuditMapper.updateByExampleSelective(contractApproveAudit,example );
    }
}
