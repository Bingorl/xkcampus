package com.biu.wifi.campus.service;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/8 11:37.
 */
public abstract class AbstractAuditUserService {

    public HashMap setAuditUser(Integer currentAuditUserId, List<Integer> auditUserIds) {
        Assert.notEmpty(auditUserIds, "暂未设置审核人,请联系管理员");

        // 组装多个审核人id字符串
        String auditUser = CollectionUtil.join(auditUserIds, ",");
        // 设置下一个审核人
        if (currentAuditUserId == null) {
            // 新增请假申请,取第一个审核人的id
            currentAuditUserId = auditUserIds.get(0);
        } else {
            // 查询下一个审核人的id
            int index = auditUserIds.indexOf(currentAuditUserId);
            if (index != -1 && index < auditUserIds.size() - 1) {
                currentAuditUserId = auditUserIds.get(index + 1);
            } else {
                // 当前审核人为最后一个审核人
                currentAuditUserId = null;
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("auditUser", auditUser);
        hashMap.put("currentAuditUserId", currentAuditUserId);
        return hashMap;
    }

    public List<Short> getStatusList(Short status, boolean apply) {
        List<Short> statusList;
        if (status == null) {
            statusList = new ArrayList<>();
        } else if (status.intValue() == 1) {
            // 审核中
            if (apply) {
                statusList = Arrays.asList(status);
            } else {
                statusList = new ArrayList<>();
            }
        } else if (status.intValue() == 5) {
            // 已处理的
            if (apply) {
                // 申请列表
                statusList = Arrays.asList((short) 2, (short) 3);
            } else {
                // 审批列表
                statusList = Arrays.asList((short) 1, (short) 2);
            }
        } else {
            statusList = Arrays.asList(status);
        }
        return statusList;
    }
}
