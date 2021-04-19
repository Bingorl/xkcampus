package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.DiscussionTopicAuditMapper;
import com.biu.wifi.campus.dao.DiscussionTopicAuditUserMapper;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 张彬.
 * @date 2021/4/8 12:50.
 */
@Service
public class DiscussionTopicAuditService {

    @Resource
    private DiscussionTopicAuditMapper discussionTopicAuditMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private InstituteMapper instituteMapper;
    @Resource
    private DiscussionTopicAuditUserMapper discussionTopicAuditUserMapper;

    public int add(DiscussionTopicAudit req) {
        req.setIsDelete((short) 2);
        req.setCreateTime(new Date());
        return discussionTopicAuditMapper.insertSelective(req);
    }

    public DiscussionTopicAudit selectByApplyId(Integer applyId, Integer currentAuditUserId) {
        DiscussionTopicAuditExample example = new DiscussionTopicAuditExample();
        example.createCriteria()
                .andApplyIdEqualTo(applyId)
                .andUserIdEqualTo(currentAuditUserId)
                .andIsDeleteEqualTo((short) 2);
        List<DiscussionTopicAudit> list = discussionTopicAuditMapper.selectByExample(example);
        return list == null ? null : list.get(0);
    }

    public int update(DiscussionTopicAudit applyAudit) {
        applyAudit.setUpdateTime(new Date());
        return discussionTopicAuditMapper.updateByPrimaryKeySelective(applyAudit);
    }

    public List<HashMap> findMapList(DiscussionTopicApply apply) {
        List<String> auditUserIds = Arrays.asList(apply.getAuditUser().split(","));

        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            HashMap hashMap = discussionTopicAuditMapper.selectMap(apply.getId(), userId);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("id", null);
                hashMap.put("type", AuditBusinessType.TEACHER_LEAVE.getCode());
                hashMap.put("bizId", apply.getId());
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

    public List<HashMap> findAuditUserList(Integer instituteId) {
        DiscussionTopicAuditUserExample example = new DiscussionTopicAuditUserExample();
        example.setOrderByClause("id desc");
        example.createCriteria()
                .andInstituteIdEqualTo(instituteId)
                .andIsDeleteEqualTo((short) 2);
        List<DiscussionTopicAuditUser> auditUsers = discussionTopicAuditUserMapper.selectByExample(example);

        List<HashMap> list = new ArrayList<>();
        if (auditUsers.isEmpty()) {
            return list;
        } else {
            List<String> auditUserIds = Arrays.asList(auditUsers.get(0).getAuditUser().split(","));

            for (String userId : auditUserIds) {
                User user = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
                Institute institute = instituteMapper.selectByPrimaryKey(user.getInstituteId());
                HashMap hashMap = new HashMap();
                hashMap.put("userId", user.getId());
                hashMap.put("username", user.getName());
                hashMap.put("instituteId", user.getInstituteId());
                hashMap.put("instituteName", institute == null ? "" : institute.getName());
                list.add(hashMap);
            }
            return list;
        }
    }

    @Transactional(rollbackFor = BizException.class)
    public void updateDiscussionTopicAuditUserList(Integer instituteId, String userIds) {
        Institute institute = instituteMapper.selectByPrimaryKey(instituteId);

        DiscussionTopicAuditUserExample example = new DiscussionTopicAuditUserExample();
        example.createCriteria()
                .andInstituteIdEqualTo(instituteId)
                .andIsDeleteEqualTo((short) 2);
        boolean delete = discussionTopicAuditUserMapper.deleteByExample(example) >= 0;

        DiscussionTopicAuditUser auditUser = new DiscussionTopicAuditUser();
        auditUser.setAuditUser(userIds);
        auditUser.setInstituteId(instituteId);
        auditUser.setSchoolId(institute.getSchoolId());
        auditUser.setIsDelete((short) 2);
        auditUser.setCreateTime(new Date());
        boolean add = discussionTopicAuditUserMapper.insertSelective(auditUser) > 0;

        if (!(add && delete)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "添加审核人失败");
        }
    }
}
