package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.constant.AudUserRole;
import com.biu.wifi.campus.dao.AuditInfoMapper;
import com.biu.wifi.campus.dao.ExamArrangeAuditMapper;
import com.biu.wifi.campus.dao.ExamArrangeNoticeMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2019/2/13.
 */
@Service
public class ExamArrangeAuditService {

    // 审核人员类型(1教学秘书，2教务处)
    public final int JXMS = 1;
    public final int JWCRY = 2;
    // 状态(1通过，0驳回)
    public final int PASS = 1;
    public final int NO_PASS = 0;
    // 待审核
    public final int UN_AUDIT = 1;

    @Autowired
    private ExamArrangeAuditMapper examArrangeAuditMapper;
    @Autowired
    private AuditInfoMapper auditInfoMapper;
    @Autowired
    private ExamArrangeNoticeMapper examArrangeNoticeMapper;
    @Autowired
    private ExamArrangeService examArrangeService;
    @Autowired
    private ExamArrangeNoticeService examArrangeNoticeService;
    @Autowired
    private PushSerivce pushService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditUserRoleService auditUserRoleService;
    @Autowired
    private AuditUserAuthService auditUserAuthService;

    /**
     * 创建排考待审核记录
     *
     * @param auditUserType
     * @param auditUserId
     * @param examArrangeId
     * @param title
     */
    @Transactional
    public void addExamArrangeAudit(int auditUserType, int auditUserId, int examArrangeId, String title, String content) {
        ExamArrangeAudit examArrangeAudit = new ExamArrangeAudit();
        examArrangeAudit.setExamArrangeId(examArrangeId);
        examArrangeAudit.setAuditUserId(auditUserId);
        examArrangeAudit.setAuditUserType(auditUserType);
        examArrangeAudit.setCreateTime(new Date());
        if (examArrangeAuditMapper.insertSelective(examArrangeAudit) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        // 创建汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setUserId(auditUserId);
        auditInfo.setBizId(examArrangeAudit.getId());
        auditInfo.setType((short) 3);
        if (auditInfoMapper.insertSelective(auditInfo) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        // 创建默认为删除的排考审核通知记录
        ExamArrangeNotice examArrangeNotice = new ExamArrangeNotice();
        examArrangeNotice.setExamArrangeId(examArrangeId);
        examArrangeNotice.setToUser(auditUserId);
        examArrangeNotice.setTitle(title);
        examArrangeNotice.setContent(content);
        examArrangeNotice.setIsDelete(1);
        examArrangeNotice.setDeleteTime(new Date());
        examArrangeNotice.setCreateTime(new Date());
        if (examArrangeNoticeMapper.insertSelective(examArrangeNotice) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        // 异步推送
        User user = userService.getById(auditUserId);
        if (user != null)
            pushService.pushNotice(title, examArrangeAudit.getExamArrangeId(), auditUserId, user.getDevToken(), user.getDevType(), (short) 14);
    }

    /**
     * 排考审核操作
     *
     * @param examArrangeAudit
     */
    @Transactional
    public void updateExamArrangeAudit(ExamArrangeAudit examArrangeAudit) {
        // 更新排考状态
        ExamArrange examArrange = examArrangeService.updateExamArrangeStatusPersistence(null,
                examArrangeAudit.getExamArrangeId(), examArrangeAudit.getIsPass());

        // 更新排考审核记录的状态
        examArrangeAudit.setAuditTime(new Date());
        examArrangeAudit.setUpdateTime(new Date());
        if (examArrangeAuditMapper.updateByPrimaryKeySelective(examArrangeAudit) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        // 排考被驳回
        if (examArrangeAudit.getIsPass() == NO_PASS) return;

        // 判断审核人的身份，根据结果进行不同的业务处理
        List<Integer> userIdList;
        if (examArrangeAudit.getAuditUserType() == JXMS) {
            // 教学秘书,创建教务处人员的排考待审核记录,并通知教务处人员
            User user = userService.getById(examArrangeAudit.getAuditUserId());
            userIdList = getUserIdListByRoleCode(user.getSchoolId());
            for (Integer userId : userIdList)
                addExamArrangeAudit(JWCRY, userId, examArrangeAudit.getExamArrangeId(), getTitle(examArrange.getCourseName()), getContent(examArrange));
        } else if (examArrangeAudit.getAuditUserType() == JWCRY) {
            // 教务处人员,群发通知给授课老师、学生、监考老师、巡考老师
            userIdList = getNoticeUserList(examArrange);
            for (Integer userId : userIdList)
                examArrangeNoticeService.addExamArrangeNotice(examArrangeAudit.getExamArrangeId(),
                        userId, getTitle(examArrange.getCourseName()), getContent(examArrange), true);
        }
    }

    public ExamArrangeAudit findById(Integer examArrangeAuditId) {
        return examArrangeAuditMapper.selectByPrimaryKey(examArrangeAuditId);
    }

    /**
     * 获取教务处人员id列表
     *
     * @param schoolId
     * @return
     */
    public List<Integer> getUserIdListByRoleCode(int schoolId) {
        AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(schoolId, AudUserRole.JWRY.getCode());
        if (auditUserRole == null)
            throw new BizException(Result.CUSTOM_MESSAGE, "未设置教务处类型的角色，无法推送通知给教务处，请联系教务处");

        List<AuditUserAuth> auditUserAuthList = auditUserAuthService.findList(schoolId, null, auditUserRole.getId());
        List<Integer> userIdList = new ArrayList<>();
        for (AuditUserAuth auth : auditUserAuthList)
            userIdList.add(auth.getUserId());

        return userIdList;
    }

    /**
     * 获取排考通知用户id列表（包括授课老师、学生、监考老师、巡考老师）
     *
     * @param examArrange
     * @return
     */
    public List<Integer> getNoticeUserList(ExamArrange examArrange) {
        List<Integer> userIdList = new ArrayList<>();
        // 授课老师
        User user = new User();
        user.setStuNumber(examArrange.getTeacherNo());
        user = userService.getUser(user);
        if (user != null)
            userIdList.add(user.getId());
        // 学生列表
        if (StringUtils.isNotBlank(examArrange.getStudentId()))
            userIdList.addAll(StringUtil.stringArrayToIntegerList(examArrange.getStudentId().split(",")));
        // 监考老师
        if (StringUtils.isNotBlank(examArrange.getInvigilateTeacherId()))
            userIdList.addAll(StringUtil.stringArrayToIntegerList(examArrange.getInvigilateTeacherId().split(",")));
        // 巡考老师
        if (StringUtils.isNotBlank(examArrange.getPatrolTeacherId()))
            userIdList.addAll(StringUtil.stringArrayToIntegerList(examArrange.getPatrolTeacherId().split(",")));

        return new ArrayList<>(new HashSet<>(userIdList));
    }

    /**
     * 获取排考通知标题
     *
     * @param courseName
     * @return
     */
    public String getTitle(String courseName) {
        return new StringBuffer()
                .append("关于")
                .append(courseName)
                .append("考试安排")
                .toString();
    }

    /**
     * 获取排考通知标题
     *
     * @param courseName
     * @return
     */
    public String getTitle(String courseName, String type) {
        String title = getTitle(courseName);
        StringBuffer sb = new StringBuffer();
        if ("courseTeacher".equals(type)) {
            sb.append("结课：").append(title);
        } else if ("student".equals(type)) {
            sb.append("考试：").append(title);
        } else if ("student".equals(type)) {
            sb.append("考试：").append(title);
        } else if ("invigilateTeacher".equals(type)) {
            sb.append("监考：").append(title);
        } else if ("patrolTeacher".equals(type)) {
            sb.append("巡考：").append(title);
        } else {
            throw new BizException(Result.CUSTOM_MESSAGE, "角色身份不正确,请检查");
        }
        return sb.toString();
    }

    /**
     * 获取排考通知的内容
     *
     * @param examArrange
     * @return
     */
    public String getContent(ExamArrange examArrange) {
        StringBuffer sb = new StringBuffer();
        sb.append(examArrange.getCourseName())
                .append("课程于")
                .append(examArrange.getCourseEndTimeWeekIndex())
                .append("周  ")
                .append(examArrange.getCourseEndTimeWeekName())
                .append(examArrange.getCourseEndTimeSection())
                .append("节结课。\n")
                .append("原上课教室")
                .append(examArrange.getClassroomNo())
                .append(",")
                .append("上课人数")
                .append(StringUtils.isNotBlank(examArrange.getStudentId()) ?
                        examArrange.getStudentId().split(",").length : 0)
                .append("人,")
                .append("现拟")
                .append(examArrange.getExamTimeWeekIndex())
                .append("周  ")
                .append(examArrange.getExamTimeWeekName())
                .append(examArrange.getExamTimeSection())
                .append("节在")
                .append(examArrange.getClassroomNo())
                .append("进行考试。考试信息见附件。");
        return sb.toString();
    }

    /**
     * 根据id获取排考待办审核的map对象
     *
     * @param examArrangeAuditId
     * @return
     */
    public Map<String, Object> findExamArrangeAuditMapById(Integer examArrangeAuditId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExamArrangeAudit examArrangeAudit = examArrangeAuditMapper.selectByPrimaryKey(examArrangeAuditId);
        if (examArrangeAudit == null)
            throw new BizException(Result.CUSTOM_MESSAGE, "id为" + examArrangeAuditId + "的排考审核不存在");

        Map<String, Object> map = new HashMap<>();
        ExamArrange examArrange = examArrangeService.findById(examArrangeAudit.getExamArrangeId());

        ExamArrangeNoticeCriteria examArrangeNoticeEx = new ExamArrangeNoticeCriteria();
        examArrangeNoticeEx.setOrderByClause("create_time desc");
        examArrangeNoticeEx.createCriteria()
                .andToUserEqualTo(examArrangeAudit.getAuditUserId())
                .andIsDeleteEqualTo(1)
                .andExamArrangeIdEqualTo(examArrange.getId());
        List<ExamArrangeNotice> examArrangeNoticeList = examArrangeNoticeMapper.selectByExample(examArrangeNoticeEx);
        ExamArrangeNotice examArrangeNotice = examArrangeNoticeList.get(0);

        map.put("id", examArrangeAudit.getId());
        map.put("noticeType", "3");// 智能排考审批
        map.put("create_time", sdf.format(examArrangeAudit.getCreateTime()));
        map.put("name", "");
        map.put("title", examArrangeNotice.getTitle());
        map.put("examArrangeId", examArrangeAudit.getExamArrangeId());
        map.put("content", examArrangeNotice.getContent());
        // is_received
        map.put("is_received", examArrangeAudit.getIsPass() == null ? 2 : 1);
        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");
        return map;
    }

    /**
     * 删除我的排考审核
     *
     * @param userId
     * @param examArrangeAuditId
     */
    public void deleteMyExamArrangeAudit(Integer userId, Integer examArrangeAuditId) {
        ExamArrangeAudit examArrangeAudit = examArrangeAuditMapper.selectByPrimaryKey(examArrangeAuditId);
        if (examArrangeAudit == null) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该记录已经被删除");
        }
        if (examArrangeAudit.getAuditUserId() != userId) {
            throw new BizException(Result.CUSTOM_MESSAGE, "没有删除的权限");
        }
        if (examArrangeAudit.getIsPass() == null) {
            throw new BizException(Result.CUSTOM_MESSAGE, "请先完成排考审核,再进行删除");
        }

        examArrangeAudit.setIsDelete(1);
        examArrangeAudit.setDeleteTime(new Date());
        examArrangeAuditMapper.updateByPrimaryKeySelective(examArrangeAudit);
    }

    /**
     * 查询某一个排考除了指定用户之外,是否还有其他的审核人员
     *
     * @param userId
     * @param examArrangeAudit
     * @return
     */
    public long findExamArrangeAuditListByOtherUser(Integer userId, ExamArrangeAudit examArrangeAudit) {
        ExamArrangeAuditCriteria example = new ExamArrangeAuditCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo(2)
                .andExamArrangeIdEqualTo(examArrangeAudit.getExamArrangeId())
                .andIsPassIsNull()
                .andAuditUserTypeEqualTo(examArrangeAudit.getAuditUserType())
                .andAuditUserIdNotEqualTo(userId);
        return examArrangeAuditMapper.countByExample(example);

    }
}
