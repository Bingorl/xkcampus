package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.OfficialWebsiteAuditMapper;
import com.biu.wifi.campus.dao.model.AssertsUseAudit;
import com.biu.wifi.campus.dao.model.AssertsUseAuditExample;
import com.biu.wifi.campus.dao.model.OfficialWebsiteAudit;
import com.biu.wifi.campus.dao.model.OfficialWebsiteAuditExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class OfficialWebsiteAuditService {
    @Autowired
    private OfficialWebsiteAuditMapper officialWebsiteAuditMapper;

    public Integer add(OfficialWebsiteAudit audit) {
        audit.setCreateTime(new Date());
        return officialWebsiteAuditMapper.insertSelective(audit);
    }

    public Integer update(OfficialWebsiteAudit audit) {
        audit.setUpdateTime(new Date());

        return officialWebsiteAuditMapper.updateByPrimaryKeySelective(audit);
    }

    public OfficialWebsiteAudit selectByUseId(Integer id, int currentAuditUserId) {
        OfficialWebsiteAuditExample example = new OfficialWebsiteAuditExample();
        example.createCriteria()
                .andWebsiteIdEqualTo(id)
                .andUserIdEqualTo(currentAuditUserId)
                .andIsDeleteEqualTo((short) 2);
        List<OfficialWebsiteAudit> list = officialWebsiteAuditMapper.selectByExample(example);
        return list == null ? null : list.get(0);
    }
}
