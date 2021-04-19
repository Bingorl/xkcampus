package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.util.DateUtilsEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/3 15:37.
 */
@Service
public class TeacherLeaveInfoService extends AbstractAuditUserService {

    @Autowired
    private TeacherLeaveInfoMapper teacherLeaveInfoMapper;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private TeacherLeaveAuditUserMapper teacherLeaveAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeacherLeaveAuditService teacherLeaveAuditService;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private TeacherLeaveNoticeService teacherLeaveNoticeService;
    @Autowired
    private TeacherLeaveNoticeMapper teacherLeaveNoticeMapper;

    /**
     * 新增请假
     *
     * @param req
     */
    @Transactional(rollbackFor = BizException.class)
    public void add(TeacherLeaveInfo req) {
        setDefaultValue(req);
        setCurrentAuditUserId(req);

        boolean result = teacherLeaveInfoMapper.insertSelective(req) > 0;

        TeacherLeaveAudit teacherLeaveAudit = new TeacherLeaveAudit();
        teacherLeaveAudit.setLeaveId(req.getId());
        teacherLeaveAudit.setUserId(req.getCurrentAuditUserId());
        boolean addLeaveAudit = teacherLeaveAuditService.add(teacherLeaveAudit) > 0;

        //插入汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.TEACHER_LEAVE.getCode().shortValue());
        auditInfo.setBizId(teacherLeaveAudit.getId());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;

        // 推送请假审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), teacherLeaveAudit.getId(), 1, "您有新的请假审批待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(result && addLeaveAudit && addAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "申请失败");
        }
    }

    /**
     * 根据请假id查询
     *
     * @param leaveId
     * @return
     */
    public TeacherLeaveInfo selectByPrimaryKey(Integer leaveId) {
        return teacherLeaveInfoMapper.selectByPrimaryKey(leaveId);
    }

    /**
     * 取消请假
     *
     * @param leaveInfo
     */
    public void cancel(TeacherLeaveInfo leaveInfo) {
        TeacherLeaveInfo update = new TeacherLeaveInfo();
        update.setStatus((short) 4);
        update.setUpdateTime(new Date());

        TeacherLeaveInfoExample example = new TeacherLeaveInfoExample();
        example.createCriteria()
                .andIdEqualTo(leaveInfo.getId());
        boolean result = teacherLeaveInfoMapper.updateByExampleSelective(update, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    /**
     * 我的请假申请列表
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param status
     * @return
     */
    public List<HashMap> myLeaveInfoList(Integer userId, String startDate, String endDate, Short status) {
        return teacherLeaveInfoMapper.myLeaveInfoList(userId, startDate, endDate, getStatusList(status, true));
    }

    /**
     * 我的请假审批列表
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param status
     * @return
     */
    public List<HashMap> myAuditLeaveInfoList(Integer userId, String startDate, String endDate, Short status) {
        return teacherLeaveInfoMapper.myAuditLeaveInfoList(userId, startDate, endDate, getStatusList(status, false));
    }

    /**
     * 请假审核
     *
     * @param leaveInfo
     * @param status    审核状态（2通过,3驳回）
     * @param remark
     * @return
     * @author 张彬.
     * @date 2021/4/3 19:57.
     */
    @Transactional(rollbackFor = {BizException.class, IllegalArgumentException.class})
    public void audit(TeacherLeaveInfo leaveInfo, Short status, String remark) {
        // 当前审批人id
        int currentAuditUserId = leaveInfo.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // 是否需要进入到下一个审核节点,设置下一个审批人id
        setCurrentAuditUserId(leaveInfo);
        int nextAuditUserId = currentAuditUserId;
        if (leaveInfo.getCurrentAuditUserId() == null) {
            // 当前审核人为最后一个
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = leaveInfo.getCurrentAuditUserId();
        }

        TeacherLeaveAudit teacherLeaveAudit = teacherLeaveAuditService.selectByLeaveId(leaveInfo.getId(), currentAuditUserId);
        Assert.notNull(teacherLeaveAudit, "该请假审批记录不存在");
        teacherLeaveAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        teacherLeaveAudit.setRemark(remark);
        teacherLeaveAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = teacherLeaveAuditService.update(teacherLeaveAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(teacherLeaveAudit.getId(), currentAuditUserId,
                AuditBusinessType.TEACHER_LEAVE.getCode().shortValue(), teacherLeaveAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        TeacherLeaveNotice teacherLeaveNotice;
        Integer receiverId, leaveNoticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            leaveInfo.setStatus(status);
            leaveInfo.setCurrentAuditUserId(currentAuditUserId);
            leaveInfo.setUpdateTime(new Date());
            teacherLeaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);

            // 审核驳回,通知请假人请假结果
            teacherLeaveNotice = teacherLeaveNoticeService.addLeaveNotice(leaveInfo.getId(), "您的请假申请未通过审批", leaveInfo.getReason(), remark, leaveInfo.getApplyUserId());
            Assert.notNull(teacherLeaveNotice.getId(), "审核失败");
            // 推送请假审批结果通知给请假人
            receiverId = leaveInfo.getApplyUserId();
            title = "您提交的请假未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            leaveNoticeId = teacherLeaveNotice.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                leaveInfo.setStatus(status);
                leaveInfo.setCurrentAuditUserId(currentAuditUserId);
                leaveInfo.setUpdateTime(new Date());
                teacherLeaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);

                // 通知请假人请假结果
                teacherLeaveNotice = teacherLeaveNoticeService.addLeaveNotice(leaveInfo.getId(), "您的请假申请已通过审批", leaveInfo.getReason(), remark, leaveInfo.getApplyUserId());
                Assert.notNull(teacherLeaveNotice.getId(), "审核失败");
                // 推送请假审批结果通知给请假人
                receiverId = leaveInfo.getApplyUserId();
                title = "您提交的请假已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                leaveNoticeId = teacherLeaveNotice.getId();
            } else {
                // 修改当前审核人
                leaveInfo.setCurrentAuditUserId(nextAuditUserId);
                leaveInfo.setUpdateTime(new Date());
                teacherLeaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);

                // 新增下一个审批人审批记录，状态为空
                TeacherLeaveAudit nextLeaveAudit = new TeacherLeaveAudit();
                nextLeaveAudit.setLeaveId(leaveInfo.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = teacherLeaveAuditService.add(nextLeaveAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.TEACHER_LEAVE.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送请假审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的请假审批待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                leaveNoticeId = null;
                needToAudit = 1;
            }
        }

        addPush(leaveInfo.getId(), leaveNoticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());
    }


    /**
     * 请假审批推送
     *
     * @param leaveId       请假单ID
     * @param leaveNoticeId 请假审批通知ID
     * @param needToAudit   是否需要审核1是0否
     * @param title         标题
     * @param receiverId    接收人ID
     * @param deviceType    接收人设备类型
     * @param deviceToken   接收人设备token
     */
    public void addPush(Integer leaveId, Integer leaveNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.STUDENT_LEAVE_NOTICE.getCode());//请假审批类型
        hm.put("leaveId", leaveId == null ? "" : leaveId);
        hm.put("leaveNoticeId", leaveNoticeId == null ? "" : leaveNoticeId);
        hm.put("needToAudit", needToAudit);

        try {
            flag = PushTool.pushToUser(receiverId, "", title, hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 入推送表
        Push puEntity = new Push();
        puEntity.setToken(deviceToken);
        puEntity.setContent("");
        puEntity.setUserId(receiverId);
        puEntity.setMessageType(PushMsgType.STUDENT_LEAVE_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }

    private void setCurrentAuditUserId(TeacherLeaveInfo req) {
        // TODO: 张彬 2021/4/4 0:33 暂时默认最多设置三个审核节点,每个节点最多一人审核（暂不适用同一职务存在正副职位都能审核的情况）
        List<Integer> auditUserIds = new ArrayList<>();
        if (StringUtils.isEmpty(req.getAuditUser())) {
            // 新增时读取审批人列表的数据
            // 3天以下,部门负责人审批
            auditUserIds = getAuditUserIds(req.getApplyUserId(), req.getPlanDays());
        } else {
            for (String userId : req.getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(userId));
            }
        }
        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }

    private List<Integer> getAuditUserIds(Integer applyUserId, Integer planDays) {
        List<Integer> auditUserIds = new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(applyUserId);
        int instituteId = user.getInstituteId();
        List<TeacherLeaveAuditUser> teacherLeaveAuditUsers = teacherLeaveAuditUserMapper.selectOneByTypeAndInstituteId(1, instituteId);
        if (CollectionUtils.isNotEmpty(teacherLeaveAuditUsers)) {
            Integer userId = Integer.valueOf(teacherLeaveAuditUsers.get(0).getAuditUser().split(",")[0]);
            auditUserIds.add(userId);
        }
        // 3-7天,主管领导审批
        if (planDays.intValue() > 2) {
            teacherLeaveAuditUsers = teacherLeaveAuditUserMapper.selectOneByTypeAndInstituteId(2, instituteId);
            if (CollectionUtils.isNotEmpty(teacherLeaveAuditUsers)) {
                Integer userId = Integer.valueOf(teacherLeaveAuditUsers.get(0).getAuditUser().split(",")[1]);
                auditUserIds.add(userId);
            }
        }
        // 7天以上,院长审批
        if (planDays.intValue() > 6) {
            teacherLeaveAuditUsers = teacherLeaveAuditUserMapper.selectOneByTypeAndInstituteId(3, instituteId);
            if (CollectionUtils.isNotEmpty(teacherLeaveAuditUsers)) {
                Integer userId = Integer.valueOf(teacherLeaveAuditUsers.get(0).getAuditUser().split(",")[2]);
                auditUserIds.add(userId);
            }
        }
        return auditUserIds;
    }

    private void setDefaultValue(TeacherLeaveInfo req) {
        int planDays = DateUtilsEx.getDayBetween(req.getStartDate(), req.getEndDate());
        int actDays = 0;
        if (req.getBackDate() != null) {
            actDays = DateUtilsEx.getDayBetween(req.getStartDate(), req.getBackDate());
        }
        req.setPlanDays(planDays);
        req.setActDays(req.getActDays() == null || req.getActDays() == 0 ? 0 : actDays);
        req.setIsDelete((short) 2);
        req.setStatus((short) 1);
        req.setCreateTime(new Date());
    }

    public List<HashMap> getAuditUserList(Integer applyUserId, Integer planDays) {
        List<Integer> auditUserIds = getAuditUserIds(applyUserId, planDays);
        List<HashMap> list = new ArrayList<>();
        for (Integer userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    public List<HashMap> search(Integer schoolId, Integer leaveType, Short status, String keyword, String startTime, String endTime) {
        return teacherLeaveInfoMapper.search(schoolId,leaveType, status, keyword, startTime, endTime);
    }
}
