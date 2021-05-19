package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.BiuTravelExpenseAuditMapper;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseAudit;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseAuditExample;
import com.biu.wifi.campus.dao.model.TeacherLeaveAudit;
import com.biu.wifi.campus.dao.model.TeacherLeaveAuditExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class BiuTravelExpenseAuditService {
    @Autowired
    private BiuTravelExpenseAuditMapper biuTravelExpenseAuditMapper;

    public Integer add(BiuTravelExpenseAudit travelExpenseAudit) {
        travelExpenseAudit.setCreateTime(new Date());
        travelExpenseAudit.setIsDelete((short)2);

        return biuTravelExpenseAuditMapper.insertSelective(travelExpenseAudit);
    }

    public Integer update(BiuTravelExpenseAudit travelExpenseAudit) {
        travelExpenseAudit.setUpdateTime(new Date());
        return biuTravelExpenseAuditMapper.updateByPrimaryKeySelective(travelExpenseAudit);
    }

    public BiuTravelExpenseAudit selectByExpenseId(Integer expenseInfoId, Integer currentAuditUserId) {
        BiuTravelExpenseAuditExample example = new BiuTravelExpenseAuditExample();
        example.createCriteria()
                .andExpenseIdEqualTo(expenseInfoId)
                .andUserIdEqualTo(currentAuditUserId)
                .andIsDeleteEqualTo((short) 2);
        List<BiuTravelExpenseAudit> list = biuTravelExpenseAuditMapper.selectByExample(example);
        return list == null ? null : list.get(0);
    }
}
