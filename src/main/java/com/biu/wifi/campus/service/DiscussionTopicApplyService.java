package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.PushMsgType;
import com.biu.wifi.campus.dao.DiscussionTopicApplyMapper;
import com.biu.wifi.campus.dao.DiscussionTopicAuditUserMapper;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author 张彬.
 * @date 2021/4/4 21:21.
 */
@Service
public class DiscussionTopicApplyService extends AbstractAuditUserService {

    @Resource
    private DiscussionTopicApplyMapper discussionTopicApplyMapper;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private DiscussionTopicNoticeService discussionTopicNoticeService;
    @Autowired
    private DiscussionTopicAuditUserMapper discussionTopicAuditUserMapper;
    @Autowired
    private DiscussionTopicAuditService discussionTopicAuditService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PushMapper pushMapper;

    /**
     * 获取会议议题申请的审核流程列表
     *
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/15 15:41.
     */
    @RequestMapping("app_discussionTopicApplyAuditInfoList")
    public void app_auditInfoList(HttpServletResponse response) {

    }

    /**
     * 会议议题申请
     *
     * @param req
     * @return
     * @author 张彬.
     * @date 2021/4/4 21:23.
     */
    @Transactional(rollbackFor = BizException.class)
    public void addDiscussion(DiscussionTopicApply req) {
        setCurrentAuditUserId(req);
        // 新增申请
        req.setStatus((short) 1);
        req.setCreateTime(new Date());
        req.setIsDelete((short) 2);
        boolean addApply = discussionTopicApplyMapper.insertSelective(req) > 0;


        DiscussionTopicAudit discussionTopicAudit = new DiscussionTopicAudit();
        discussionTopicAudit.setApplyId(req.getId());
        discussionTopicAudit.setUserId(req.getCurrentAuditUserId());
        boolean addApplyAudit = discussionTopicAuditService.add(discussionTopicAudit) > 0;

        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setType(AuditBusinessType.DISCUSSION_TOPIC_APPLY.getCode().shortValue());
        auditInfo.setUserId(req.getCurrentAuditUserId());
        auditInfo.setBizId(discussionTopicAudit.getId());
        boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;

        // 推送会议议题申请审批通知给第一个审批人
        User user = userMapper.selectByPrimaryKey(req.getCurrentAuditUserId());
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(req.getId(), req.getId(), 1, "您有新的会议议题申请审批待处理", req.getCurrentAuditUserId(), deviceType, deviceToken);

        if (!(addApply && addAuditInfo && addApplyAudit)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "新增失败");
        }
    }

    /**
     * 取消会议议题申请
     *
     * @param req
     * @return
     * @author 张彬.
     * @date 2021/4/4 21:23.
     */
    public void cancelDiscussion(DiscussionTopicApply req) {
        req.setStatus((short) 4);
        req.setUpdateTime(new Date());
        boolean result = discussionTopicApplyMapper.updateByPrimaryKeySelective(req) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "取消失败");
        }
    }

    /**
     * 根据id查询会议议题申请记录
     *
     * @param bizId
     * @return
     * @author 张彬.
     * @date 2021/4/8 11:17.
     */
    public DiscussionTopicApply selectByPrimaryKey(Integer bizId) {
        return discussionTopicApplyMapper.selectByPrimaryKey(bizId);
    }

    /**
     * 我的会议申请列表
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param status
     * @return
     * @author 张彬.
     * @date 2021/4/4 22:02.
     */
    public List<HashMap> myDiscussionTopicApplyList(Integer userId, String startDate, String endDate, Short status) {
        return discussionTopicApplyMapper.myDiscussionTopicApplyList(userId, startDate, endDate, getStatusList(status, true));
    }

    /**
     * 我的会议议题审批列表
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param status
     * @return
     * @author 张彬.
     * @date 2021/4/4 22:05.
     */
    public List<HashMap> myAuditDiscussionTopicApplyList(Integer userId, String startDate, String endDate, Short status) {
        return discussionTopicApplyMapper.myAuditDiscussionTopicApplyList(userId,  startDate, endDate, getStatusList(status, false));
    }

    /**
     * 设置当前审核人id
     *
     * @param req
     * @return
     * @author 张彬.
     * @date 2021/4/4 22:13.
     */
    private void setCurrentAuditUserId(DiscussionTopicApply req) {
        // 部门负责人申报 -> 主管领导审批 -> 院长/党委书记审批 —> 办公室汇总记录
        List<Integer> auditUserIds = new ArrayList<>();
        if (StringUtils.isEmpty(req.getAuditUser())) {
            // 新增时读取审批人列表的数据
            User user = userMapper.selectByPrimaryKey(req.getUserId());
            int instituteId = user.getInstituteId();
            DiscussionTopicAuditUserExample example = new DiscussionTopicAuditUserExample();
            example.createCriteria()
                    .andInstituteIdEqualTo(instituteId)
                    .andIsDeleteEqualTo((short) 2);
            List<DiscussionTopicAuditUser> auditUsers = discussionTopicAuditUserMapper.selectByExample(example);
            Assert.notEmpty(auditUsers, "暂未设置审核人,请联系管理员");
            for (String userId : auditUsers.get(0).getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(userId));
            }
        } else {
            for (String userId : req.getAuditUser().split(",")) {
                auditUserIds.add(Integer.valueOf(userId));
            }
        }
        HashMap hashMap = setAuditUser(req.getCurrentAuditUserId(), auditUserIds);
        req.setAuditUser(MapUtils.getString(hashMap, "auditUser"));
        req.setCurrentAuditUserId(MapUtils.getInteger(hashMap, "currentAuditUserId"));
    }

    /**
     * 获取审核人列表
     *
     * @param
     * @return
     * @author 张彬.
     * @date 2021/4/15 15:56.
     */
    public List<HashMap> getAuditUserList(Integer applyUserId) {
        // 新增时读取审批人列表的数据
        User user = userMapper.selectByPrimaryKey(applyUserId);
        int instituteId = user.getInstituteId();
        DiscussionTopicAuditUserExample example = new DiscussionTopicAuditUserExample();
        example.createCriteria()
                .andInstituteIdEqualTo(instituteId)
                .andIsDeleteEqualTo((short) 2);
        List<DiscussionTopicAuditUser> auditUsers = discussionTopicAuditUserMapper.selectByExample(example);
        Assert.notEmpty(auditUsers, "暂未设置审核人,请联系管理员");
        List<String> auditUserIds = Arrays.asList(auditUsers.get(0).getAuditUser().split(","));
        List<HashMap> list = new ArrayList<>();
        for (String userId : auditUserIds) {
            list.add(userMapper.selectMap(userId));
        }
        return list;
    }

    /**
     * 会议议题申请审批推送
     *
     * @param applyId                      会议议题申请单ID
     * @param discussionTopicApplyNoticeId 会议议题申请审批通知ID
     * @param needToAudit                  是否需要审核1是0否
     * @param title                        标题
     * @param receiverId                   接收人ID
     * @param deviceType                   接收人设备类型
     * @param deviceToken                  接收人设备token
     */
    public void addPush(Integer applyId, Integer discussionTopicApplyNoticeId, Integer needToAudit,
                        String title, Integer receiverId, Short deviceType, String deviceToken) {
        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", "");
        hm.put("title", title);
        hm.put("type", PushMsgType.DISCUSSION_TOPIC_APPLY_NOTICE.getCode());//会议议题申请审批类型
        hm.put("discussionTopicApplyId", applyId == null ? "" : applyId);
        hm.put("discussionTopicApplyNoticeId", discussionTopicApplyNoticeId == null ? "" : discussionTopicApplyNoticeId);
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
        puEntity.setMessageType(PushMsgType.DISCUSSION_TOPIC_APPLY_NOTICE.getCode().shortValue());
        puEntity.setDeviceType(deviceType);
        puEntity.setTitle(title);
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushMapper.insertSelective(puEntity);
    }

    /**
     * 会议议题申请审批
     *
     * @param apply
     * @param status
     * @param remark
     * @return
     * @author 张彬.
     * @date 2021/4/8 12:36.
     */
    public void audit(DiscussionTopicApply apply, Short status, String remark) {
        // 当前审批人id
        int currentAuditUserId = apply.getCurrentAuditUserId();
        boolean currentAuditUserIsTheLast = false;
        // 是否需要进入到下一个审核节点,设置下一个审批人id
        setCurrentAuditUserId(apply);
        int nextAuditUserId = currentAuditUserId;
        if (apply.getCurrentAuditUserId() == null) {
            // 当前审核人为最后一个
            currentAuditUserIsTheLast = true;
        } else {
            nextAuditUserId = apply.getCurrentAuditUserId();
        }

        DiscussionTopicAudit applyAudit = discussionTopicAuditService.selectByApplyId(apply.getId(),currentAuditUserId);
        Assert.notNull(applyAudit, "该会议议题申请审批记录不存在");
        applyAudit.setIsPass(status.intValue() == 2 ? (short) 1 : (short) 2);
        applyAudit.setRemark(remark);
        applyAudit.setAuditTime(new Date());
        boolean updateLeaveAudit = discussionTopicAuditService.update(applyAudit) > 0;
        boolean updateAuditInfo = auditInfoService.update(applyAudit.getId(), currentAuditUserId,
                AuditBusinessType.DISCUSSION_TOPIC_APPLY.getCode().shortValue(),
                applyAudit.getIsPass()) > 0;

        if (!(updateLeaveAudit && updateAuditInfo)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
        }

        // 推送通知
        DiscussionTopicNotice req;
        Integer receiverId, noticeId, needToAudit = 0;
        String title;
        User user;
        if (status.intValue() == 3) {
            // 修改审核状态
            apply.setStatus(status);
            apply.setCurrentAuditUserId(currentAuditUserId);
            apply.setUpdateTime(new Date());
            discussionTopicApplyMapper.updateByPrimaryKeySelective(apply);

            // 审核驳回,通知申请人结果
            req = new DiscussionTopicNotice();
            req.setTitle("您的会议议题申请未通过审批");
            req.setApplyId(apply.getId());
            req.setContent(apply.getTopicName());
            req.setRemark(remark);
            req.setToUserId(apply.getUserId());
            req = discussionTopicNoticeService.add(req);
            Assert.notNull(req.getId(), "审核失败");
            // 推送审批结果通知给申请人
            receiverId = apply.getUserId();
            title = "您提交的会议议题申请未通过审批";
            user = userMapper.selectByPrimaryKey(receiverId);
            noticeId = req.getId();
        } else {
            if (currentAuditUserIsTheLast) {
                // 修改审核状态
                apply.setStatus(status);
                apply.setCurrentAuditUserId(currentAuditUserId);
                apply.setUpdateTime(new Date());
                discussionTopicApplyMapper.updateByPrimaryKeySelective(apply);

                // 通知申请人结果
                req = new DiscussionTopicNotice();
                req.setTitle("您的会议议题申请已通过审批");
                req.setApplyId(apply.getId());
                req.setContent(apply.getTopicName());
                req.setRemark(remark);
                req.setToUserId(apply.getUserId());
                req = discussionTopicNoticeService.add(req);
                Assert.notNull(req.getId(), "审核失败");
                // 推送审批结果通知给申请人
                receiverId = apply.getUserId();
                title = "您提交的会议议题申请已通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                noticeId = req.getId();
            } else {
                // 修改审核人
                apply.setCurrentAuditUserId(nextAuditUserId);
                apply.setUpdateTime(new Date());
                discussionTopicApplyMapper.updateByPrimaryKeySelective(apply);

                // 新增下一个审批人审批记录，状态为空
                DiscussionTopicAudit nextLeaveAudit = new DiscussionTopicAudit();
                nextLeaveAudit.setApplyId(apply.getId());
                nextLeaveAudit.setUserId(nextAuditUserId);
                nextLeaveAudit.setCreateTime(new Date());
                boolean addLeaveAudit = discussionTopicAuditService.add(nextLeaveAudit) > 0;
                // 新增审核操作记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType(AuditBusinessType.DISCUSSION_TOPIC_APPLY.getCode().shortValue());
                auditInfo.setBizId(nextLeaveAudit.getId());
                auditInfo.setUserId(nextAuditUserId);
                boolean addAuditInfo = auditInfoService.add(auditInfo) > 0;
                if (!(addLeaveAudit && addAuditInfo)) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "审核失败");
                }

                // 推送会议议题申请审批通知给下一个审批人
                receiverId = nextAuditUserId;
                title = "您有新的会议议题申请审批待处理";
                user = userMapper.selectByPrimaryKey(receiverId);
                noticeId = null;
                needToAudit = 1;
            }
        }

        addPush(apply.getId(), noticeId, needToAudit, title, receiverId, user.getDevType(), user.getDevToken());
    }
}

