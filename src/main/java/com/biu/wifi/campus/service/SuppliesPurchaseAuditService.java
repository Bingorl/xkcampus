package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.SuppliesPurchaseAuditMapper;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}
