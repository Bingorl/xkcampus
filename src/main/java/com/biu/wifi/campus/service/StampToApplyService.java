package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.StampToApplyMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class StampToApplyService {
    @Autowired
    private StampToApplyMapper stampToApplyMapper;
   @Autowired
    private UserMapper userMapper;
   @Autowired
    private InstituteMapper instituteMapper;


    public List<HashMap> findMapList(StampToApplyInfo stampToApplyInfo) {
        List<String> auditUserIds = Arrays.asList(stampToApplyInfo.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = stampToApplyMapper.findMap(stampToApplyInfo.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.STAMP_TO_APPLY.getCode());
                hashMap.put("bizId", stampToApplyInfo.getId());
                hashMap.put("userId", Integer.valueOf(userId));
                hashMap.put("isPass", null);
            }
            User auditUser = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
            Institute institute = instituteMapper.selectByPrimaryKey(auditUser.getInstituteId());
            hashMap.put("userName", auditUser.getName());
            hashMap.put("instituteName", institute.getName());
            list.add(hashMap);
        }

        return list;
    }

    public StampToApply selectByLeaveId(Integer id, int currentAuditUserId) {
        StampToApplyExample example=new StampToApplyExample();
        example.createCriteria().andApplyIdEqualTo(id)
                .andUserIdEqualTo(currentAuditUserId)
                .andIsDeleteEqualTo((short) 2);
        List<StampToApply> stampToApplies = stampToApplyMapper.selectByExample(example);
        return stampToApplies.size()>0?stampToApplies.get(0):null;
    }

    public Integer update(StampToApply stampToApply) {
        return stampToApplyMapper.updateByPrimaryKeySelective(stampToApply);
    }

    public Integer add(StampToApply req) {

            req.setCreateTime(new Date());
            req.setIsDelete((short) 2);
            return stampToApplyMapper.insertSelective(req);
        }

}
