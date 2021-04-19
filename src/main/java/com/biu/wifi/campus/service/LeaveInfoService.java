package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.LeaveAuditMapperEx;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin
 */
@Service
public class LeaveInfoService {

    @Autowired
    private LeaveInfoMapper leaveInfoMapper;

    @Autowired
    private LeaveAuditMapper leaveAuditMapper;

    @Autowired
    private LeaveAuditMapperEx leaveAuditMapperEx;

    @Autowired
    private LeaveNoticeMapper leaveNoticeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LeaveAuditUserAuthMapper leaveAuditUserAuthMapper;

    @Autowired
    private LeaveAuditUserMapper leaveAuditUserMapper;

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Autowired
    private DormBuildingMapper dormBuildingMapper;

    @Autowired
    private PushMapper pushMapper;

    @Autowired
    private AuditInfoMapper auditInfoMapper;

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;
    @Autowired
    private AuditUserAuthMapper auditUserAuthMapper;

    /**
     * 新增请假
     *
     * @param record
     * @param insertAuditInfo
     * @return
     */
    @Transactional
    public Result addLeaveInfo(LeaveInfo record, boolean insertAuditInfo) {
        // 新增请假记录
        long unit = 24 * 60 * 60 * 1000;
        long days = (record.getEndDate().getTime() - record.getStartDate().getTime()) / unit;
        if (days < 0) {
            return new Result(Result.CUSTOM_MESSAGE, "结束日期不能早于开始日期", null);
        }

        User user = userMapper.selectByPrimaryKey(record.getUserId());
        record.setRealName(user.getName());
        if (StringUtils.isBlank(record.getTel())) {
            record.setTel(user.getPhone());
        }
        record.setClassId(user.getClassId());
        record.setStuNo(user.getStuNumber());

        //判断请假人身份
        if (user.getIsTeacher() == 2) {
            Integer auditUserId;
            if (insertAuditInfo) {
                //1.4版本后的
                AuditUserAuthCriteria example = new AuditUserAuthCriteria();
                example.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andSchoolIdEqualTo(user.getSchoolId())
                        .andInstituteIdEqualTo(user.getInstituteId())
                        .andGradeIdEqualTo(user.getGradeId());
                List<AuditUserAuth> auditUserAuthList = auditUserAuthMapper.selectByExample(example);
                if (CollectionUtils.isEmpty(auditUserAuthList)) {
                    return new Result(Result.CUSTOM_MESSAGE, "你所在年级的辅导员未进行身份认证，请联系辅导员", null);
                } else {
                    auditUserId = auditUserAuthList.get(0).getUserId();
                }
            } else {
                //学生,审批人为本学院的所在年级的辅导员
                LeaveAuditUserAuthCriteria example = new LeaveAuditUserAuthCriteria();
                example.setOrderByClause("create_time desc");
                example.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andSchoolIdEqualTo(user.getSchoolId())
                        .andInstituteIdEqualTo(user.getInstituteId())
                        .andGradeIdEqualTo(user.getGradeId());
                List<LeaveAuditUserAuth> leaveAuditUserAuthList = leaveAuditUserAuthMapper.selectByExample(example);
                if (CollectionUtils.isEmpty(leaveAuditUserAuthList)) {
                    return new Result(Result.CUSTOM_MESSAGE, "你所在年级的辅导员未进行身份认证，请联系辅导员", null);
                } else {
                    auditUserId = leaveAuditUserAuthList.get(0).getUserId();
                }
            }

            record.setAuditUser(String.valueOf(auditUserId));
        }

        int planDays = Integer.valueOf(String.valueOf(days));
        record.setPlanDays(planDays);
        record.setCreateTime(new Date());
        leaveInfoMapper.insertSelective(record);

        int receiverId;
        LeaveAudit leaveAudit;
        if (StringUtils.isNotBlank(record.getAuditUser())) {
            // 新增第一个审批人审批记录，状态为空
            String[] userIdList = record.getAuditUser().split(",");
            int auditUserId = Integer.valueOf(userIdList[0]);
            leaveAudit = new LeaveAudit();
            leaveAudit.setLeaveId(record.getId());
            leaveAudit.setUserId(auditUserId);
            leaveAudit.setCreateTime(new Date());
            leaveAuditMapper.insertSelective(leaveAudit);
            if (insertAuditInfo) {
                //插入汇总记录
                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setType((short) 1);
                auditInfo.setBizId(leaveAudit.getId());
                auditInfo.setUserId(auditUserId);
                auditInfoMapper.insertSelective(auditInfo);
            }

            receiverId = auditUserId;
        } else {
            //老师进行请假的时候，未找到审批人员进行处理
            return new Result(Result.CUSTOM_MESSAGE, "你的请假审批人员未进行身份认证，请联系上级审批人员", null);
        }

        // 推送请假审批通知给第一个审批人
        String deviceToken = user.getDevToken();
        Short deviceType = user.getDevType();
        addPush(record.getId(), leaveAudit.getId(), 1, "您有新的请假审批待处理", receiverId, deviceType, deviceToken);
        return new Result(Result.SUCCESS, "成功", record);
    }

    /**
     * 请假单详情
     *
     * @param id
     * @return
     */
    public Result getLeaveInfo(Integer id) {
        // 请假信息
        LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(id);
        if (leaveInfo == null || (leaveInfo.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null);
        }

        Map<String, Object> map = new HashMap<>();
        BeanMap beanMap = BeanMap.create(leaveInfo);
        Map leaveInfoMap = new HashMap();
        map.put("leaveInfo", leaveInfoMap);
        for (Object key : beanMap.keySet()) {
            leaveInfoMap.put(key, beanMap.get(key));
        }
        LeaveType leaveType = leaveTypeMapper.selectByPrimaryKey(leaveInfo.getLeaveType());
        leaveInfoMap.put("leaveTypeName", leaveType.getName());
        String dormBuildingName = "";
        if (StringUtils.isNotBlank(leaveInfo.getApartmentBuilding())) {
            DormBuilding dormBuilding = dormBuildingMapper.selectByPrimaryKey(Integer.valueOf(leaveInfo.getApartmentBuilding()));
            if (dormBuilding != null) {
                if (StringUtils.isNotBlank(dormBuilding.getAreaPosition())) {
                    dormBuildingName += dormBuilding.getAreaPosition();
                }
                if (StringUtils.isNotBlank(dormBuilding.getNo())) {
                    dormBuildingName += dormBuilding.getNo();
                }
                if (StringUtils.isNotBlank(dormBuilding.getUnitNo())) {
                    dormBuildingName += dormBuilding.getUnitNo();
                }
                dormBuildingName += leaveInfo.getApartment();
            }
        }
        leaveInfoMap.put("dormBuildingName", dormBuildingName);

        // 请假审批人列表
        List<Map<String, Object>> auditUserList = new ArrayList<>();
        Map<String, Object> auditUser = null;
        User user = null;
        String[] auditUserIdArray = leaveInfo.getAuditUser().split(",");
        LeaveAuditCriteria auditEx = null;
        for (String auditUserId : auditUserIdArray) {
            int userId = Integer.valueOf(auditUserId);
            auditEx = new LeaveAuditCriteria();
            auditEx.setOrderByClause("create_time desc");
            auditEx.createCriteria().andIsDeleteEqualTo((short) 2).andLeaveIdEqualTo(id).andUserIdEqualTo(userId);
            List<LeaveAudit> audits = leaveAuditMapper.selectByExample(auditEx);
            user = userMapper.selectByPrimaryKey(userId);
            auditUser = new HashMap<>();
            auditUser.put("auditUserName", user.getName());
            if (CollectionUtils.isNotEmpty(audits)) {
                LeaveAudit audit = audits.get(0);
                if (audit.getIsPass() != null) {
                    auditUser.put("isPass", audit.getIsPass() == (short) 1 ? "通过" : "驳回");
                    auditUser.put("remark", audit.getRemark());
                } else {
                    auditUser.put("isPass", "审批中");
                    auditUser.put("remark", "");
                }
            } else {
                auditUser.put("isPass", "");
                auditUser.put("remark", "");
            }
            auditUserList.add(auditUser);
        }
        map.put("auditUserList", auditUserList);
        return new Result(Result.SUCCESS, "成功", map);
    }

    /**
     * xx用户的请假单列表
     *
     * @param userId
     * @return
     */
    public Result findLeaveInfoList(Integer userId) {
        LeaveInfoCriteria example = new LeaveInfoCriteria();
        example.setOrderByClause("create_time desc");
        LeaveInfoCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }

        List<LeaveInfo> leaveInfos = leaveInfoMapper.selectByExample(example);

        List<Map<String, Object>> maps = new ArrayList<>();
        Map<String, Object> map;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        LeaveType leaveType;
        String title = "";
        String leaveTime;
        for (LeaveInfo leaveInfo : leaveInfos) {
            map = new HashMap<>();
            map.put("id", leaveInfo.getId());
            map.put("leaveType", leaveInfo.getLeaveType());
            leaveType = leaveTypeMapper.selectByPrimaryKey(leaveInfo.getLeaveType());
            map.put("leaveTypeName", leaveType.getName());
            map.put("reasonType", leaveInfo.getReasonType());
            leaveTime = sdf.format(leaveInfo.getStartDate()) + " ~ " + sdf.format(leaveInfo.getEndDate());
            if (leaveInfo.getReasonType() == 1) {
                title += "【事假】" + leaveTime;
            } else {
                title += "【病假】" + leaveTime;
            }
            map.put("title", title);
            map.put("realName", leaveInfo.getRealName());
            map.put("reason", leaveInfo.getReason());
            map.put("status", leaveInfo.getStatus());
            map.put("statusText", getLeaveInfoStatusText(leaveInfo.getStatus()));
            map.put("leaveTime", leaveTime);
            map.put("createTime", leaveInfo.getCreateTime());
            maps.add(map);
        }

        return new Result(Result.SUCCESS, "成功", maps);
    }

    /**
     * 查询所有审批通过，但未发送审批通知给教职工的请假单列表
     *
     * @return
     */
    public List<LeaveInfo> findAuditPassLeaveInfoList() {
        //查询审核通过的请假
        LeaveInfoCriteria leaveInfoEx = new LeaveInfoCriteria();
        leaveInfoEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andStatusEqualTo((short) 2);
        List<LeaveInfo> leaveInfoList = leaveInfoMapper.selectByExample(leaveInfoEx);
        Iterator<LeaveInfo> iterator = leaveInfoList.iterator();
        while (iterator.hasNext()) {
            //筛选出没有发送过通知给教职工的请假
            LeaveInfo leaveInfo = iterator.next();
            LeaveNoticeCriteria leaveNoticeEx = new LeaveNoticeCriteria();
            leaveNoticeEx.createCriteria()
                    .andLeaveIdEqualTo(leaveInfo.getId())
                    .andToUserTypeNotEqualTo((short) 1);
            long count = leaveNoticeMapper.countByExample(leaveNoticeEx);
            if (count > 0) {
                //移除发送过通知的请假
                iterator.remove();
            }
        }
        return leaveInfoList;
    }

    /**
     * 取消请假
     *
     * @param id
     * @return
     */
    @Transactional
    public Result cancelLeaveInfo(Integer id, Integer userId) {
        LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(id);
        if (leaveInfo == null) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null);
        }

        if (leaveInfo.getStatus() == 4) {
            return new Result(Result.CUSTOM_MESSAGE, "该请假已取消", null);
        }

        if (leaveInfo.getUserId() != userId) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限", null);
        }

        leaveInfo.setStatus((short) 4);
        leaveInfo.setUpdateTime(new Date());
        leaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);
        return new Result(Result.SUCCESS, "成功", null);
    }

    /**
     * 删除请假记录
     *
     * @param id
     * @return
     */
    public Result deleteLeaveInfo(Integer id, Integer userId) {
        LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(id);
        if (leaveInfo == null || (leaveInfo != null && leaveInfo.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null);
        }

        if (leaveInfo.getUserId() != userId) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限", null);
        }

        leaveInfo.setIsDelete((short) 1);
        leaveInfo.setDeleteTime(new Date());
        leaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);
        return new Result(Result.SUCCESS, "成功", null);
    }

    /**
     * 销假
     *
     * @param id       请假单ID
     * @param backDate 销假日期
     * @return
     */
    public Result reportBackForLeaveInfo(Integer id, Date backDate) {
        LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(id);
        if (leaveInfo == null) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null);
        }

        // 计算实际请假天数
        long unit = 24 * 60 * 60 * 1000;
        long days = (backDate.getTime() - leaveInfo.getStartDate().getTime()) / unit;
        if (days < 0) {
            return new Result(Result.CUSTOM_MESSAGE, "销假日期不能早于开始日期", null);
        }
        leaveInfo.setBackDate(backDate);
        int actDays = Integer.valueOf(String.valueOf(days));
        leaveInfo.setActDays(actDays);
        leaveInfo.setUpdateTime(new Date());
        try {
            leaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);
            return new Result(Result.SUCCESS, "成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Result.FAILURE, "失败", null);
        }
    }

    /**
     * 审批请假
     *
     * @param leaveAudit      请假审批
     * @param auditUser       后续的审批人ID字符串
     * @param insertAuditInfo 是否插入auditInfo数据（添加教室审批模块后，添加了汇总）
     */
    @Transactional
    public Result updateLeaveAudit(LeaveAudit leaveAudit, String auditUser, boolean insertAuditInfo) {
        int userId = leaveAudit.getUserId();
        short isPass = leaveAudit.getIsPass();
        String remark = leaveAudit.getRemark();
        leaveAudit = leaveAuditMapper.selectByPrimaryKey(leaveAudit.getId());
        if (leaveAudit == null || (leaveAudit != null && leaveAudit.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "暂无可审批的记录", null);
        }

        if (userId != leaveAudit.getUserId()) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限", null);
        }

        // 审批请假
        leaveAudit.setIsPass(isPass);
        leaveAudit.setRemark(remark);
        leaveAudit.setAuditTime(new Date());
        leaveAudit.setUpdateTime(new Date());
        leaveAuditMapper.updateByPrimaryKeySelective(leaveAudit);

        //查询当前审批人对当前请假类型有没有设置审批人队列,有设置则更新审批人
        LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(leaveAudit.getLeaveId());
        LeaveAuditUserCriteria auditUserExample = new LeaveAuditUserCriteria();
        auditUserExample.setOrderByClause("create_time desc");
        auditUserExample.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(leaveAudit.getUserId())
                .andTypeEqualTo(leaveInfo.getLeaveType().shortValue());
        List<LeaveAuditUser> leaveAuditUserList = leaveAuditUserMapper.selectByExample(auditUserExample);
        if (CollectionUtils.isNotEmpty(leaveAuditUserList)) {

            LeaveAuditUser leaveAuditUser = leaveAuditUserList.get(0);
            auditUser = leaveInfo.getAuditUser() + "," + leaveAuditUser.getAuditUser();
            leaveInfo.setAuditUser(auditUser);
            leaveInfo.setUpdateTime(new Date());
            leaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);
        }


        Integer receiverId;
        String title, deviceToken;
        Short deviceType;
        User user;
        LeaveNotice leaveNotice;
        Integer leaveNoticeId;
        Integer needToAudit = 0;

        try {
            if (leaveAudit.getIsPass() == 2) {
                // 任一审批人驳回，则终止审批，通知请假人结果
                leaveInfo.setStatus((short) 3);
                leaveInfo.setUpdateTime(new Date());
                leaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);
                // 通知请假人请假结果
                String content = getLeaveTime(leaveInfo.getStartDate(), leaveInfo.getEndDate(), (short) 1, null);
                leaveNotice = addLeaveNotice(leaveInfo.getId(), "您的请假申请未通过审批", content, remark, leaveInfo.getUserId(), (short) 1);
                // 推送请假审批结果通知给请假人
                receiverId = leaveInfo.getUserId();
                title = "您提交的请假未通过审批";
                user = userMapper.selectByPrimaryKey(receiverId);
                leaveNoticeId = leaveNotice.getId();
            } else {
                // 判断当前审批人是第几个
                LeaveAuditCriteria example = new LeaveAuditCriteria();
                example.createCriteria().andIsDeleteEqualTo((short) 2).andLeaveIdEqualTo(leaveAudit.getLeaveId());
                List<LeaveAudit> leaveAudits = leaveAuditMapper.selectByExample(example);
                auditUser = "";
                for (int i = 0; i < leaveAudits.size(); i++) {
                    auditUser += leaveAudits.get(i).getUserId();
                    if (i != leaveAudits.size() - 1) {
                        auditUser += ",";
                    }
                }
                auditUser = leaveInfo.getAuditUser().replace(auditUser, "");
                if (StringUtils.isNotBlank(auditUser)) {
                    // 通知下一个审批人
                    String[] auditUserIds = auditUser.split(",");
                    int toUserId = 0;
                    for (String id : auditUserIds) {
                        if (StringUtils.isNotBlank(id)) {
                            toUserId = Integer.valueOf(id);
                            break;
                        }
                    }
                    // 新增下一个审批人审批记录，状态为空
                    LeaveAudit nextLeaveAudit = new LeaveAudit();
                    nextLeaveAudit.setLeaveId(leaveInfo.getId());
                    nextLeaveAudit.setUserId(toUserId);
                    nextLeaveAudit.setCreateTime(new Date());
                    leaveAuditMapper.insertSelective(nextLeaveAudit);
                    if (insertAuditInfo) {
                        //插入汇总记录
                        AuditInfo auditInfo = new AuditInfo();
                        auditInfo.setType((short) 1);
                        auditInfo.setBizId(nextLeaveAudit.getId());
                        auditInfo.setUserId(toUserId);
                        auditInfoMapper.insertSelective(auditInfo);
                    }

                    // 推送请假审批通知给下一个审批人
                    receiverId = toUserId;
                    title = "您有新的请假审批待处理";
                    user = userMapper.selectByPrimaryKey(receiverId);
                    leaveNoticeId = null;
                    needToAudit = 1;
                } else {
                    // 当前是最后一个审批人
                    leaveInfo.setStatus((short) 2);
                    leaveInfo.setUpdateTime(new Date());
                    leaveInfoMapper.updateByPrimaryKeySelective(leaveInfo);
                    // 通知请假人请假结果
                    String content = getLeaveTime(leaveInfo.getStartDate(), leaveInfo.getEndDate(), (short) 1, null);
                    remark = getLeaveNoticeRemark(leaveInfo.getGoTo());
                    leaveNotice = addLeaveNotice(leaveInfo.getId(), "您的请假申请已通过审批", content, remark, leaveInfo.getUserId(), (short) 1);
                    // 推送请假审批结果通知给请假人
                    receiverId = leaveInfo.getUserId();
                    title = "您提交的请假已通过审批";
                    user = userMapper.selectByPrimaryKey(receiverId);
                    leaveNoticeId = leaveNotice.getId();
                }
            }

            deviceToken = user.getDevToken();
            deviceType = user.getDevType();
            addPush(leaveInfo.getId(), leaveNoticeId, needToAudit, title, receiverId, deviceType, deviceToken);

            return new Result(Result.SUCCESS, "成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Result.FAILURE, "失败", null);
        }
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
        hm.put("type", 12);//请假审批类型
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
        puEntity.setMessageType((short) 12);
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
     * 添加通知
     *
     * @param leaveId
     * @param title
     * @param content
     * @param remark
     * @param toUserId
     * @param toUserType
     */
    public LeaveNotice addLeaveNotice(Integer leaveId, String title, String content, String remark, Integer toUserId,
                                      Short toUserType) {
        LeaveNotice leaveNotice = new LeaveNotice();
        leaveNotice.setLeaveId(leaveId);
        leaveNotice.setTitle(title);
        leaveNotice.setContent(content);
        leaveNotice.setRemark(remark);
        leaveNotice.setToUserId(toUserId);
        leaveNotice.setToUserType(toUserType);
        leaveNotice.setCreateTime(new Date());
        leaveNoticeMapper.insertSelective(leaveNotice);
        //插入通知汇总表
        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setBizId(leaveNotice.getId());
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.STUDENT_LEAVE_NOTICE.getCode().shortValue());
        noticeInfo.setIsDelete((short) 2);
        noticeInfoMapper.insertSelective(noticeInfo);
        return leaveNotice;
    }

    /**
     * 获取请假日期字符串
     *
     * @param startDate  请假开始时间
     * @param endDate    请假结束时间
     * @param toUserType 通知接收人类型(1学生，2教师，3宿管）
     * @param plan       教师收到的请假通知日期类型(例如：第八周周四8.9节课、第九周周一3.4节课)
     * @return
     */
    public String getLeaveTime(Date startDate, Date endDate, short toUserType, String plan) {
        String content = "";
        SimpleDateFormat sdf = null;
        if (toUserType != (short) 2) {
            sdf = new SimpleDateFormat("yyyy年MM月dd日");
            content = "假期：" + sdf.format(startDate) + " 至 " + sdf.format(endDate);
        } else {
            content = "假期：" + plan;
        }
        return content;
    }

    /**
     * 获取请假通知的备注
     *
     * @param goTo
     * @return
     */
    public String getLeaveNoticeRemark(short goTo) {
        String remark = "";
        // 去向(1外出，2回家，3留校)
        switch (goTo) {
            case (short) 1:
                remark = "假期出行请注意安全，务必按时返校";
                break;
            case (short) 2:
                remark = "假期回家请注意安全，务必按时返校";
                break;
            case (short) 3:
                remark = "";
                break;
            default:
                break;
        }
        return remark;
    }

    /**
     * 根据请假状态获取请假状态字符串
     *
     * @param status
     * @return
     */
    public String getLeaveInfoStatusText(int status) {
        String str = "";
        switch (status) {
            //状态(1审批中,2通过,3驳回,4取消,5已完成)
            case 1:
                str = "正在审批中";
                break;
            case 2:
                str = "已通过";
                break;
            case 3:
                str = "已驳回";
                break;
            case 4:
                str = "已取消";
                break;
            case 5:
                str = "已完成";
                break;
        }
        return str;
    }

    /**
     * xx用户收到的请假通知列表
     *
     * @param userId
     */
    public List<Map<String, Object>> findLeaveNoticeList(Integer userId) {
        LeaveNoticeCriteria example = new LeaveNoticeCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria().andIsDeleteEqualTo((short) 2).andToUserIdEqualTo(userId);

        List<LeaveNotice> leaveNotices = leaveNoticeMapper.selectByExample(example);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        for (LeaveNotice leaveNotice : leaveNotices) {
            map = new HashMap<>();
            map.put("id", leaveNotice.getId());
            map.put("title", leaveNotice.getTitle());
            map.put("content", leaveNotice.getContent());
            map.put("remark", leaveNotice.getRemark());
            map.put("createTime", leaveNotice.getCreateTime());
            map.put("isRead", leaveNotice.getIsRead());
            map.put("isConfirm", leaveNotice.getIsConfirm());
            map.put("confirmTime", leaveNotice.getConfirmTime());
            list.add(map);
        }
        return list;
    }

    /**
     * xx用户收到的请假通知列表
     *
     * @param userId
     */
    public List<Map<String, Object>> findLeaveNoticeMapList(Integer userId) {
        LeaveNoticeCriteria example = new LeaveNoticeCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria().andIsDeleteEqualTo((short) 2).andToUserIdEqualTo(userId);

        List<LeaveNotice> leaveNotices = leaveNoticeMapper.selectByExample(example);
        List<Map<String, Object>> list = new ArrayList<>();
        for (LeaveNotice leaveNotice : leaveNotices) {
            Map<String, Object> map = formatLeaveNoticeData(leaveNotice, userId);
            list.add(map);
        }
        return list;
    }

    public Map<String, Object> findLeaveNoticeMapById(Integer id) {
        LeaveNotice leaveNotice = leaveNoticeMapper.selectByPrimaryKey(id);
        return formatLeaveNoticeData(leaveNotice, leaveNotice.getToUserId());
    }

    /**
     * 格式化请假通知数据
     *
     * @param leaveNotice
     * @param userId
     * @return
     */
    public Map<String, Object> formatLeaveNoticeData(LeaveNotice leaveNotice, Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", leaveNotice.getId());
        map.put("noticeType", NoticeType.STUDENT_LEAVE_NOTICE.getCode());//请假审批通知
        map.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(leaveNotice.getCreateTime()));
        map.put("name", "请假");
        map.put("title", leaveNotice.getTitle());
        map.put("content", leaveNotice.getContent());
        //is_received
        //学生用户是否已读
        //老师用户是否已收到
        map.put("is_received", leaveNotice.getIsConfirm() == 1 ? 1 : 2);

        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");
        LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(leaveNotice.getLeaveId());

        if (leaveInfo != null) {
            //是否申请复议
            boolean isRepeatApply = false;
            if (userId.intValue() == leaveInfo.getUserId().intValue() && leaveInfo.getStatus().shortValue() == (short) 3) {
                //自己的被驳回的请假才能申请复议
                isRepeatApply = true;
            }
            map.put("isRepeatApply", isRepeatApply);
            map.put("leaveId", leaveNotice.getLeaveId());
        } else {
            map.put("isRepeatApply", null);
            map.put("leaveId", null);
        }

        return map;
    }

    public Map<String, Object> findLeaveAuditMapById(Integer id, short isTeacher) {
        return formatLeaveAuditData(leaveAuditMapperEx.findById(id), isTeacher);
    }

    /**
     * xx用户的待审批列表
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findLeaveAuditList(Integer userId) {
        List<Map<String, Object>> list = leaveAuditMapperEx.findLeaveAuditList(userId);
        Map<String, Object> map;
        for (Map<String, Object> leaveAudit : list) {
            LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(Integer.valueOf(leaveAudit.get("leaveId").toString()));
            if (leaveInfo == null) {
                continue;
            }
            User user = userMapper.selectByPrimaryKey(leaveInfo.getUserId());
            map = new HashMap<>();
            map.put("isAudit", leaveAudit.get("isPass") == null ? false : true);
            map.put("applyUser", user.getName());
            map.put("leaveInfo", leaveInfo);
            map.remove("passType");
        }

        return list;
    }

    /**
     * xx用户的待审批列表
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findLeaveAuditMapList(Integer userId) {
        List<Map<String, Object>> leaveAuditList = leaveAuditMapperEx.findLeaveAuditList(userId);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        User user = userMapper.selectByPrimaryKey(userId);
        for (Map<String, Object> audit : leaveAuditList) {
            map = formatLeaveAuditData(audit, user.getIsTeacher());
            list.add(map);
        }

        return list;
    }

    /**
     * 格式化请假审批数据
     *
     * @param audit
     * @param isTeacher
     * @return
     */
    public Map<String, Object> formatLeaveAuditData(Map audit, short isTeacher) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", audit.get("id"));
        map.put("noticeType", "2");//请假审批通知
        map.put("create_time", audit.get("createTime"));
        map.put("name", "请假");

        LeaveInfo leaveInfo = leaveInfoMapper.selectByPrimaryKey(Integer.valueOf(audit.get("leaveId").toString()));
        String reasonType;
        if (leaveInfo.getReasonType() == 1) {
            reasonType = "事假";
        } else {
            reasonType = "病假";
        }
        User leaveUser = userMapper.selectByPrimaryKey(leaveInfo.getUserId());
        String title = "" + leaveUser.getName() + "    " + leaveUser.getStuNumber() + "    " + reasonType;
        map.put("title", title);
        map.put("leaveId", leaveInfo.getId());
        String content = "请假事由：\n" + leaveInfo.getReason();
        map.put("content", content);
        //is_received
        //老师用户是否已收到
        if (isTeacher == 1) {
            map.put("is_received", audit.get("isPass") == null ? 2 : 1);
        }

        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");
        return map;
    }

//    /**
//     * xx用户是否有待办审核
//     *
//     * @param userId
//     * @return
//     */
//    public boolean hasLeaveAuditTodo(Integer userId) {
//        //认证成功后就显示待办
//        LeaveAuditUserAuthCriteria example = new LeaveAuditUserAuthCriteria();
//        example.createCriteria()
//                .andIsDeleteEqualTo((short) 2)
//                .andUserIdEqualTo(userId);
//        long count = leaveAuditUserAuthMapper.countByExample(example);
//
//
//        /*LeaveAuditCriteria example = new LeaveAuditCriteria();
//        example.createCriteria()
//                .andIsDeleteEqualTo((short) 2)
//                .andUserIdEqualTo(userId);
//        count = leaveAuditMapper.countByExample(example);*/
//        return count > 0 ? true : false;
//    }

    /**
     * 通知已读
     *
     * @param id
     */
    public Result readNotice(Integer id, Integer userId) {
        LeaveNotice notice = leaveNoticeMapper.selectByPrimaryKey(id);
        if (notice == null || (notice != null && notice.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该通知不存在", null);
        }

        if (notice.getIsRead() == 1) {
            return new Result(Result.CUSTOM_MESSAGE, "请勿重复操作", null);
        }

        if (notice.getToUserId() != userId) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限", null);
        }

        notice.setIsRead((short) 1);
        notice.setUpdateTime(new Date());
        leaveNoticeMapper.updateByPrimaryKeySelective(notice);
        return new Result(Result.SUCCESS, "成功", null);
    }

    /**
     * 通知收到确认
     *
     * @param id
     */
    public Result confirmNotice(Integer id, Integer userId) {
        LeaveNotice notice = leaveNoticeMapper.selectByPrimaryKey(id);
        if (notice == null || (notice != null && notice.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该通知不存在", null);
        }
        if (notice.getIsConfirm() == 1) {
            return new Result(Result.CUSTOM_MESSAGE, "请勿重复操作", null);
        }
        if (notice.getToUserId().intValue() != userId.intValue()) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限", null);
        }

        notice.setIsConfirm((short) 1);
        notice.setConfirmTime(new Date());
        leaveNoticeMapper.updateByPrimaryKeySelective(notice);
        return new Result(Result.SUCCESS, "成功", null);
    }

    /**
     * 删除通知
     *
     * @param id
     * @param userId
     * @return
     */
    public Result deleteNotice(Integer id, Integer userId) {
        LeaveNotice notice = leaveNoticeMapper.selectByPrimaryKey(id);
        if (notice == null || (notice != null && notice.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该通知不存在", null);
        }

        if (notice.getToUserId() != userId) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限", null);
        }

        notice.setIsDelete((short) 1);
        notice.setDeleteTime(new Date());
        leaveNoticeMapper.updateByPrimaryKeySelective(notice);
        return new Result(Result.SUCCESS, "成功", null);
    }
}
