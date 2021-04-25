package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.StampToApplyUserMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.StampToApplyUser;
import com.biu.wifi.campus.dao.model.StampToApplyUserExample;
import com.biu.wifi.campus.dao.model.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Service
public class StampToApplyUserService {
    @Autowired
    private StampToApplyUserMapper stampToApplyUserMapper;
     @Autowired
    private UserMapper userMapper;

    public List<HashMap> getAuditUserList(Integer applyUserId, Integer type) {
        List<Integer> auditUserIds = getAuditUserIds(applyUserId, type);
        List<HashMap> list = new ArrayList<>();
        for (Integer userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    public List<Integer> getAuditUserIds(Integer applyUserId, Integer type) {
        List<Integer> auditUserIds = new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(applyUserId);
        int instituteId = user.getInstituteId();
        StampToApplyUserExample example=new StampToApplyUserExample();
        example.createCriteria().andInstituteIdEqualTo(instituteId).andTypeEqualTo(type.shortValue());
        List<StampToApplyUser> stampToApplyUsers = stampToApplyUserMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(stampToApplyUsers)) {
            String[] split = stampToApplyUsers.get(0).getAuditUser().split(",");
            for (String s : split) {
                Integer userId = Integer.valueOf(s);
                auditUserIds.add(userId);
            }
        }
        return auditUserIds;
    }
}
