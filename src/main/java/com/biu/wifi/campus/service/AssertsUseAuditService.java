package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.AssertsUseAuditMapper;
import com.biu.wifi.campus.dao.model.AssertsUseAudit;
import com.biu.wifi.campus.dao.model.AssertsUseAuditExample;
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
public class AssertsUseAuditService {
    @Autowired
    private AssertsUseAuditMapper assertsUseAuditMapper;
    public Integer add(AssertsUseAudit assertsUseAudit) {
        assertsUseAudit.setCreateTime(new Date());
        return assertsUseAuditMapper.insertSelective(assertsUseAudit);
    }

    public AssertsUseAudit selectByUseId(Integer id, int currentAuditUserId) {
        AssertsUseAuditExample example = new AssertsUseAuditExample();
        example.createCriteria()
                .andUseIdEqualTo(id)
                .andUserIdEqualTo(currentAuditUserId)
                .andIsDeleteEqualTo((short) 2);
        List<AssertsUseAudit> list = assertsUseAuditMapper.selectByExample(example);
        return list == null ? null : list.get(0);
    }

    public Integer update(AssertsUseAudit assertsUseAudit) {
        assertsUseAudit.setUpdateTime(new Date());

        return assertsUseAuditMapper.updateByPrimaryKeySelective(assertsUseAudit);
    }
}
