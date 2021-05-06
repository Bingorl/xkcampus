package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.FileReceiveAuditMapper;
import com.biu.wifi.campus.dao.model.FileReceiveAudit;
import com.biu.wifi.campus.dao.model.FileReceiveAuditExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class FileReceiveAuditService {
    @Autowired
    private FileReceiveAuditMapper fileReceiveAuditMapper;

    public Integer add(FileReceiveAudit req) {
        req.setCreateTime(new Date());
        req.setIsDelete((short) 2);
        return fileReceiveAuditMapper.insertSelective(req);
    }

    public FileReceiveAudit selectByLeaveId(Integer id, int currentAuditUserId) {
        FileReceiveAuditExample example=new FileReceiveAuditExample();
        example.createCriteria().andReceiveIdEqualTo(id)
                                .andUserIdEqualTo(currentAuditUserId);
        List<FileReceiveAudit> fileReceiveAudits = fileReceiveAuditMapper.selectByExample(example);
        return fileReceiveAudits.size()!=0?fileReceiveAudits.get(0):null;
    }

    public Integer update(FileReceiveAudit fileReceiveAudit) {
        return fileReceiveAuditMapper.updateByPrimaryKeySelective(fileReceiveAudit);
    }


}
