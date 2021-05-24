package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.RepairCostAuditMapper;
import com.biu.wifi.campus.dao.model.OfficialWebsiteAudit;
import com.biu.wifi.campus.dao.model.OfficialWebsiteAuditExample;
import com.biu.wifi.campus.dao.model.RepairCostAudit;
import com.biu.wifi.campus.dao.model.RepairCostAuditExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class RepairCostAuditService {
    @Autowired
    private RepairCostAuditMapper repairCostAuditMapper;

    public Integer add(RepairCostAudit audit) {
        audit.setCreateTime(new Date());
        return repairCostAuditMapper.insertSelective(audit);
    }

    public RepairCostAudit selectByUseId(Integer id, int currentAuditUserId) {
        RepairCostAuditExample example = new RepairCostAuditExample();
        example.createCriteria()
                .andRepairIdEqualTo(id)
                .andUserIdEqualTo(currentAuditUserId)
                .andIsDeleteEqualTo((short) 2);
        List<RepairCostAudit> list = repairCostAuditMapper.selectByExample(example);
        return list == null ? null : list.get(0);
    }

    public Integer update(RepairCostAudit audit) {
        audit.setUpdateTime(new Date());
        return  repairCostAuditMapper.updateByPrimaryKeySelective(audit);
    }
}
