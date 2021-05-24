package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.SuppliesPurchaseAuditMapper;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseAudit;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseAuditExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class SuppliesPurchaseAuditService {
    @Autowired
    private SuppliesPurchaseAuditMapper suppliesPurchaseAuditMapper;

    public Integer add(SuppliesPurchaseAudit suppliesPurchaseAudit) {
        suppliesPurchaseAudit.setCreateTime(new Date());
        suppliesPurchaseAudit.setIsDelete((short) 2);
        return suppliesPurchaseAuditMapper.insertSelective(suppliesPurchaseAudit);

    }

    public SuppliesPurchaseAudit selectBypurchaseId(Integer id, int currentAuditUserId) {
        SuppliesPurchaseAuditExample example=new SuppliesPurchaseAuditExample();
        example.createCriteria().andPurchaseIdEqualTo(id)
                .andUserIdEqualTo(currentAuditUserId);
        List<SuppliesPurchaseAudit> suppliesPurchaseAudits = suppliesPurchaseAuditMapper.selectByExample(example);
        return suppliesPurchaseAudits.size()!=0?suppliesPurchaseAudits.get(0):null;

    }

    public Integer update(SuppliesPurchaseAudit suppliesPurchaseAudit) {
        suppliesPurchaseAudit.setUpdateTime(new Date());
        return suppliesPurchaseAuditMapper.updateByPrimaryKeySelective(suppliesPurchaseAudit);
    }
}
