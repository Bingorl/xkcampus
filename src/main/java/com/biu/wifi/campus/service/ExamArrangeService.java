package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.ExcelUtils;
import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.ExamArrangeMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.ExamArrangeMapperEx;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.support.session.SessionContext;
import com.biu.wifi.core.util.DateUtilsEx;
import com.biu.wifi.core.util.StringUtilsEx;
import com.biu.wifi.dao.TCptDataInfoMapper;
import com.biu.wifi.model.TCptDataInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2019/2/13.
 */
@Service
public class ExamArrangeService {

    // 状态(1待审核，2驳回，3添加监考完成，4添加巡考完成，5通过)
    public final int UN_AUDIT = 1;
    public final int NO_PASS = 2;
    public final int ADD_INVIGILATETEACHERID_ALREADY = 3;
    public final int ADD_PATROLTEACHERID_ALREADY = 4;
    public final int AUDIT_PASS = 5;

    private static Logger logger = LoggerFactory.getLogger(ExamArrangeService.class);
    @Autowired
    private ExamArrangeMapper examArrangeMapper;
    @Autowired
    private ExamArrangeMapperEx examArrangeMapperEx;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private UserService userService;
    @Autowired
    private TCptDataInfoMapper dataInfoMapper;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private StudentChoosedCourseService studentChoosedCourseService;
    @Autowired
    private TeacherCoursePlanService teacherCoursePlanService;
    @Autowired
    private ExamArrangeAuditService examArrangeAuditService;
    @Autowired
    private ExamArrangeNoticeService examArrangeNoticeService;
    @Autowired
    private AuditUserRoleService auditUserRoleService;
    @Autowired
    private ExamArrangeAuditAdjustLogService examArrangeAuditAdjustLogService;

    /**
     * 创建排考(根据教师排课生成排考记录)
     *
     * @param teacherCoursePlan
     */
    public ExamArrange addExamArrange(TeacherCoursePlan teacherCoursePlan, Integer currentWeekIndex) {
        // TODO: 2019/3/12 考试时间暂时用2018-10-15来测试
        Date now = null;//new Date();
        try {
            now = DateUtils.parseDate("2018-10-15", new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String nowDateStr = TimeUtil.getDayStr(now);
        ExamArrange examArrange = new ExamArrange();
        examArrange.setSchoolId(teacherCoursePlan.getSchoolId());
        examArrange.setTermCode(teacherCoursePlan.getTermCode());
        examArrange.setCourseNo(teacherCoursePlan.getCourseNo());
        examArrange.setCourseSerialNo(teacherCoursePlan.getCourseSerialNo());
        examArrange.setCourseName(teacherCoursePlan.getCourseName());
        examArrange.setTeacherName(teacherCoursePlan.getTeacherName());
        examArrange.setTeacherNo(teacherCoursePlan.getTeacherNo());
        examArrange.setClassNo(studentChoosedCourseService.getClassNoByCourseNo(teacherCoursePlan.getCourseNo(), teacherCoursePlan.getCourseSerialNo()));
        examArrange.setStudentId(studentChoosedCourseService.getStudentIdByByCourseNo(teacherCoursePlan.getCourseNo(), teacherCoursePlan.getCourseSerialNo()));
        examArrange.setClassroomNo(teacherCoursePlan.getClassroomNo());
        examArrange.setCourseEndTime(now);
        examArrange.setCourseEndTimeWeekIndex(currentWeekIndex);
        examArrange.setCourseEndTimeWeekName(nowDateStr);
        examArrange.setCourseEndTimeSection(teacherCoursePlanService.getCourseEndTimeSection(teacherCoursePlan));
        examArrange.setExamTime(now);
        examArrange.setExamTimeWeekIndex(currentWeekIndex);
        examArrange.setExamTimeWeekName(nowDateStr);
        examArrange.setExamTimeSection(teacherCoursePlanService.getExamTimeSection(teacherCoursePlan));
        examArrange.setExamTimePeriod(teacherCoursePlanService.getExamTimePeriod(teacherCoursePlan));
        examArrange.setStatus(UN_AUDIT);
        examArrange.setCreateTime(now);
        return examArrange;
    }

    public ExamArrange addExamArrangePersistence(TeacherCoursePlan teacherCoursePlan, Integer currentWeekIndex) {
        ExamArrange examArrange = addExamArrange(teacherCoursePlan, currentWeekIndex);
        if (examArrangeMapper.insertSelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        // 插入的审核记录,根据课程名是否带有期末,来确定是先推送给教学秘书,还是直接推送给教务处
        // TODO: 2019/3/12 暂时默认都发给教学秘书
//        User courseTeacher = userService.getUserByStuNumber(teacherCoursePlan.getTeacherNo());
//        List<User> jxmsUserList = auditUserRoleService.getJxmsUserList(courseTeacher.getSchoolId(), courseTeacher.getInstituteId());
//        for (User user : jxmsUserList) {
//            examArrangeNoticeService.addExamArrangeNotice(examArrange.getId(), user.getId(), examArrangeAuditService.getTitle(teacherCoursePlan.getCourseName()),
//                    examArrangeAuditService.getContent(examArrange));
//        }
        return examArrange;
    }

    /**
     * 调整排考时间
     *
     * @param examArrange
     * @param examArrangeId
     * @param adjustExamTime
     * @param adjustExamTimeWeekIndex
     * @param adjustExamTimeWeekName
     * @param adjustExamTimeSection
     */
    public ExamArrange updateExamTime(ExamArrange examArrange, int examArrangeId, Date adjustExamTime,
                                      Integer adjustExamTimeWeekIndex, String adjustExamTimeWeekName, String adjustExamTimeSection) {
        if (examArrange == null)
            examArrange = new ExamArrange();

        examArrange.setId(examArrangeId);

        examArrange.setOldExamTime(examArrange.getExamTime());
        examArrange.setOldExamTimeWeekIndex(examArrange.getExamTimeWeekIndex());
        examArrange.setOldExamTimeWeekName(examArrange.getExamTimeWeekName());
        examArrange.setOldExamTimeSection(examArrange.getExamTimeSection());

        examArrange.setExamTime(adjustExamTime);
        examArrange.setExamTimeWeekIndex(adjustExamTimeWeekIndex);
        examArrange.setExamTimeWeekName(adjustExamTimeWeekName);
        examArrange.setExamTimeSection(adjustExamTimeSection);
        examArrange.setUpdateTime(new Date());
        return examArrange;
    }

    public ExamArrange updateExamTimePersistence(ExamArrange examArrange, int examArrangeId, Date adjustExamTime,
                                                 Integer adjustExamTimeWeekIndex, String adjustExamTimeWeekName, String adjustExamTimeSection) {
        // 校验是否允许更新
        int examArrangeStatus = getExamArrangeStatusById(examArrangeId);
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS || examArrangeStatus == ADD_PATROLTEACHERID_ALREADY)
            throw new BizException(Result.CUSTOM_MESSAGE, "已经被审核的排考不允许调整考试时间");

        examArrange = updateExamTime(examArrange, examArrangeId, adjustExamTime, adjustExamTimeWeekIndex, adjustExamTimeWeekName, adjustExamTimeSection);
        // 创建考试座位安排表
        String fileId = createExamSeatArrangeExcel(examArrange);
        examArrange.setAttachment(fileId);

        if (examArrangeMapper.updateByPrimaryKeySelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        return examArrange;
    }

    /**
     * 调整教室
     *
     * @param examArrange
     * @param examArrangeId
     * @param adjustClassroomNo
     */
    public ExamArrange updateAdjustClassroomNo(ExamArrange examArrange, int examArrangeId, String adjustClassroomNo) {
        if (examArrange == null)
            examArrange = new ExamArrange();

        examArrange.setId(examArrangeId);

        examArrange.setOldClassroomNo(examArrange.getClassroomNo());
        examArrange.setClassroomNo(adjustClassroomNo);
        examArrange.setUpdateTime(new Date());
        return examArrange;
    }

    public ExamArrange updateAdjustClassroomNoPersistence(ExamArrange examArrange, int examArrangeId, String adjustClassroomNo) {
        // 校验是否允许更新
        int examArrangeStatus = getExamArrangeStatusById(examArrangeId);
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS)
            throw new BizException(Result.CUSTOM_MESSAGE, "已经被审核的排考不允许调整教室");

        examArrange = updateAdjustClassroomNo(examArrange, examArrangeId, adjustClassroomNo);
        // 创建考试座位安排表
        String fileId = createExamSeatArrangeExcel(examArrange);
        examArrange.setAttachment(fileId);
        if (examArrangeMapper.updateByPrimaryKeySelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        return examArrange;
    }

    /**
     * 更新监考人员
     *
     * @param examArrange
     * @param examArrangeId
     * @param invigilateTeacherId
     * @return
     */
    public ExamArrange updateInvigilateTeacherId(ExamArrange examArrange, int examArrangeId, String invigilateTeacherId) {
        if (examArrange == null)
            examArrange = new ExamArrange();

        examArrange.setId(examArrangeId);
        examArrange.setInvigilateTeacherId(invigilateTeacherId);
        examArrange.setUpdateTime(new Date());
        return examArrange;
    }

    public ExamArrange updateInvigilateTeacherIdPersistence(ExamArrange examArrange, int examArrangeId, String invigilateTeacherId) {
        // 校验是否允许更新
        int examArrangeStatus = getExamArrangeStatusById(examArrangeId);
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS || examArrangeStatus == ADD_INVIGILATETEACHERID_ALREADY)
            throw new BizException(Result.CUSTOM_MESSAGE, "已经被审核的排考不允许更新监考人员");

        examArrange = updateInvigilateTeacherId(examArrange, examArrangeId, invigilateTeacherId);
        // 创建考试座位安排表
        String fileId = createExamSeatArrangeExcel(examArrange);
        examArrange.setAttachment(fileId);
        if (examArrangeMapper.updateByPrimaryKeySelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        return examArrange;
    }

    /**
     * 更新巡考人员
     *
     * @param examArrange
     * @param examArrangeId
     * @param patrolTeacherId
     * @return
     */
    public ExamArrange updatePatrolTeacherId(ExamArrange examArrange, int examArrangeId, String patrolTeacherId) {
        if (examArrange == null)
            examArrange = new ExamArrange();

        examArrange.setId(examArrangeId);
        examArrange.setPatrolTeacherId(patrolTeacherId);
        examArrange.setUpdateTime(new Date());
        return examArrange;
    }

    public void updatePatrolTeacherIdPersistence(ExamArrange examArrange, int examArrangeId, String patrolTeacherId) {
        // 校验是否允许更新
        int examArrangeStatus = getExamArrangeStatusById(examArrangeId);
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS || examArrangeStatus == ADD_PATROLTEACHERID_ALREADY)
            throw new BizException(Result.CUSTOM_MESSAGE, "已经被审核的排考不允许更新巡考人员");

        examArrange = updatePatrolTeacherId(examArrange, examArrangeId, patrolTeacherId);
        // 创建考试座位安排表
        String fileId = createExamSeatArrangeExcel(examArrange);
        examArrange.setAttachment(fileId);
        if (examArrangeMapper.updateByPrimaryKeySelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");
    }

    public void updatePatrolTeacherIdPersistence(List<ExamArrange> examArrangeList, String patrolTeacherId, Integer userId) {
        for (ExamArrange examArrange : examArrangeList) {
            String oldPatrolTeacherId = examArrange.getPatrolTeacherId();

            updatePatrolTeacherIdPersistence(examArrange, examArrange.getId(), patrolTeacherId);
            examArrangeAuditAdjustLogService.addLog(examArrange.getId(), userId, examArrangeAuditAdjustLogService.UPDATE_PATROL_TEACHER,
                    patrolTeacherId, oldPatrolTeacherId);
        }
    }

    /**
     * 更新审核状态
     *
     * @param examArrange
     * @param examArrangeId
     * @param auditUserId
     * @param status
     * @return
     */
    public ExamArrange updateAuditUserId(ExamArrange examArrange, int examArrangeId, int auditUserId, int status) {
        if (examArrange == null)
            examArrange = new ExamArrange();

        examArrange.setId(examArrangeId);
        // 最终审核人id
        examArrange.setAuditUserId(auditUserId);
        examArrange.setStatus(status);
        examArrange.setAuditTime(new Date());
        examArrange.setUpdateTime(new Date());
        return examArrange;
    }

    public void updateAuditUserIdPersistence(ExamArrange examArrange, int examArrangeId, int auditUserId, int status) {
        examArrange = updateAuditUserId(examArrange, examArrangeId, auditUserId, status);
        if (examArrangeMapper.updateByPrimaryKeySelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");
    }

    /**
     * 更新附件
     *
     * @param examArrange
     * @param examArrangeId
     * @return
     */
    public ExamArrange updateAttachment(ExamArrange examArrange, int examArrangeId) {
        if (examArrange == null)
            examArrange = new ExamArrange();

        String fileId = createExamSeatArrangeExcel(examArrange);
        examArrange.setAttachment(fileId);
        examArrange.setId(examArrangeId);
        return examArrange;
    }

    public void updateAttachmentPersistence(ExamArrange examArrange, int examArrangeId) {
        examArrange = updateAttachment(examArrange, examArrangeId);
        examArrange.setUpdateTime(new Date());
        if (examArrangeMapper.updateByPrimaryKeySelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");
    }

    /**
     * 更新排考状态
     *
     * @param examArrange
     * @param examArrangeId
     * @param status
     * @return
     */
    public ExamArrange updateExamArrangeStatus(ExamArrange examArrange, int examArrangeId, int status) {
        if (examArrange == null)
            examArrange = new ExamArrange();

        examArrange.setId(examArrangeId);
        examArrange.setStatus(status);
        examArrange.setAuditTime(new Date());
        examArrange.setUpdateTime(new Date());
        return examArrange;
    }

    public ExamArrange updateExamArrangeStatusPersistence(ExamArrange examArrange, int examArrangeId, int status) {
        // 校验是否允许更新
        int examArrangeStatus = getExamArrangeStatusById(examArrangeId);
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS)
            throw new BizException(Result.CUSTOM_MESSAGE, "该排考已经审核过");

        examArrange = updateExamArrangeStatus(examArrange, examArrangeId, status);
        if (examArrangeMapper.updateByPrimaryKeySelective(examArrange) < 0)
            throw new BizException(Result.SQL_EXECUTE_FAILURE, "数据库操作失败");

        return examArrange;
    }

    public ExamArrange findById(int examArrangeId) {
        return examArrangeMapper.selectByPrimaryKey(examArrangeId);
    }

    /**
     * 获取排考的状态
     *
     * @param examArrangeId
     * @return
     */
    public int getExamArrangeStatusById(int examArrangeId) {
        ExamArrange examArrange = findById(examArrangeId);
        if (examArrange == null)
            throw new BizException(Result.CUSTOM_MESSAGE, "该排考不存在");

        return examArrange.getStatus();
    }

    public List<ExamArrange> findList(ExamArrangeCriteria example) {
        return examArrangeMapper.selectByExample(example);
    }

    /**
     * 指定日期当天的排考列表
     *
     * @param schoolId
     * @param startTimeList
     * @return
     */
    public List<ExamArrange> findExamArrangeListByExamTime(Integer schoolId, List<String> startTimeList) {
        return findExamArrangeList(schoolId, "", "", startTimeList);
    }

    /**
     * 根据学校id、课程号、课序号、考试时间返回排考记录(不包括驳回的记录)
     *
     * @param schoolId
     * @param courseNo
     * @param courseSerialNo
     * @param startTimeList
     * @return
     */
    public List<ExamArrange> findExamArrangeList(int schoolId, String courseNo, String courseSerialNo, List<String> startTimeList) {
        Date examTime;
        String examTimeStr = startTimeList.get(0).split(" ")[0];
        try {
            examTime = DateUtils.parseDate(examTimeStr, new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            throw new BizException(Result.CUSTOM_MESSAGE, "考试时间格式错误");
        }

        List<String> startTimePeriodList = new ArrayList<>();
        for (String startTime : startTimeList)
            startTimePeriodList.add(startTime.split(" ")[1]);

        return examArrangeMapperEx.findListByCourseNo(schoolId, courseNo, courseSerialNo, examTime, startTimePeriodList);
    }

    public List<ExamArrange> findExamArrangeList(int schoolId, String courseNo, String courseSerialNo, Date examTime, List<String> startTimePeriodList) {
        return examArrangeMapperEx.findListByCourseNo(schoolId, courseNo, courseSerialNo, examTime, startTimePeriodList);
    }

    /**
     * 根据学校id、教室号、考试时间返回排考记录(不包括驳回的记录)
     *
     * @param schoolId
     * @param classroomNo
     * @param examTime
     * @param startTimeList
     * @return
     */
    public List<ExamArrange> findExamArrangeList(int schoolId, String classroomNo, Date examTime, List<String> startTimeList) {
        List<ExamArrange> examArrangeList = new ArrayList<>();

        List<String> startTimePeriodList = new ArrayList<>();
        for (String startTime : startTimeList)
            startTimePeriodList.add(startTime.split(" ")[1]);

        for (String no : classroomNo.split(","))
            examArrangeList.addAll(examArrangeMapperEx.findListByClassroomNo(schoolId, no, examTime, startTimePeriodList));
        return examArrangeList;
    }

    /**
     * 创建考试座位安排excel文件
     *
     * @param examArrange
     */
    public String createExamSeatArrangeExcel(ExamArrange examArrange) {
        if (StringUtils.isBlank(examArrange.getStudentId())) {
            logger.error("未查询到考试的学生列表,无法创建考试座位安排excel文件");
            return null;
        }

        // 确定学年学期
        String examTime = TimeUtil.format(examArrange.getExamTime(), "yyyy-MM-dd");
        TeachingWeek teachingWeek = teachingWeekService.getTeachingWeekByExamTime(examArrange.getSchoolId(), examTime);

        // 确定文件名和文件id
        String fileName = new StringBuffer(teachingWeek.getTermName().split(",")[0])
                .append(examArrange.getCourseName())
                .append("考试座位表")
                .toString();
        String fileId = StringUtilsEx.getUUID();
        String storeName = "ds_upload";

        // 确定考试学生列表
        UserCriteria userEx = new UserCriteria();
        userEx.setOrderByClause("stu_number asc");
        userEx.createCriteria()
                .andSchoolIdEqualTo(examArrange.getSchoolId())
                .andIdIn(StringUtil.stringArrayToIntegerList(examArrange.getStudentId().split(",")));
        List<User> userList = userService.findList(userEx);

        // 写文件
        String excel = ExcelUtils.write(getExamSeatArrangeDataMap(fileName, examArrange, userList), fileName, storeName);
        File file = new File(excel);
        byte[] fileContent = null;
        try {
            fileContent = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            logger.error("排考{}的考试座位表创建失败", examArrange.toString());
        }

        // 增加记录
        TCptDataInfo dataInfo = new TCptDataInfo();
        dataInfo.setId(fileId);
        dataInfo.setId(new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + File.separator + StringUtilsEx.getUUID() + ".xls");
        dataInfo.setCreateUser(SessionContext.getSessionInfo() != null ? SessionContext.getSessionInfo().getUserId() : null);
        dataInfo.setCreateDate(DateUtilsEx.getSysDate());
        dataInfo.setFileName(fileName);
        dataInfo.setFileSize(BigDecimal.valueOf(fileContent.length));
        dataInfo.setStatus(BigDecimal.valueOf(0));
        dataInfo.setStoreName(storeName);
        dataInfoMapper.insert(dataInfo);

        return fileId;
    }

    /**
     * 获取考试座位安排表格的数据
     *
     * @param userList
     * @return
     */
    public Map<String, List<List<String>>> getExamSeatArrangeDataMap(String sheetName, ExamArrange examArrange, List<User> userList) {

        List<List<String>> excelList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        titleList.add("序号");
        titleList.add("课程号");
        titleList.add("课序号");
        titleList.add("课程名");
        titleList.add("考试班级");
        titleList.add("考试时间");
        titleList.add("姓名");
        titleList.add("学号");
        titleList.add("监考老师");
        titleList.add("巡考老师");
        excelList.add(titleList);

        String examTime = new StringBuffer(TimeUtil.format(examArrange.getExamTime(), "yyyy-MM-dd"))
                .append(" 第")
                .append(examArrange.getExamTimeSection())
                .append("节课")
                .toString();

        // 监考老师列表
        UserCriteria userEx = new UserCriteria();
        userEx.createCriteria()
                .andStatusEqualTo((short) 1)
                .andIsDeleteEqualTo((short) 2)
                .andIsTeacherEqualTo((short) 1)
                .andIdIn(StringUtil.stringArrayToIntegerList(examArrange.getInvigilateTeacherId().split(",")));
        List<User> invigilateTeacherList = userService.findList(userEx);
        List<String> invigilateTeacherNameList = new ArrayList<>();
        for (User teacher : invigilateTeacherList)
            invigilateTeacherNameList.add(teacher.getName());

        // 巡考老师列表
        userEx = new UserCriteria();
        userEx.createCriteria()
                .andStatusEqualTo((short) 1)
                .andIsDeleteEqualTo((short) 2)
                .andIsTeacherEqualTo((short) 1)
                .andIdIn(StringUtil.stringArrayToIntegerList(examArrange.getPatrolTeacherId().split(",")));
        List<User> patrolTeacherList = userService.findList(userEx);
        List<String> patrolTeacherNameList = new ArrayList<>();
        for (User teacher : patrolTeacherList)
            patrolTeacherNameList.add(teacher.getName());


        // 根据分配的班级来排座
        String[] classroomNoList = examArrange.getClassroomNo().split(",");

        List<List<String>> dataList = new ArrayList<>();

        // 如果存在多个班级,则查询第一个班级的考试座位数
        Classroom classroom = classroomService.findOne(examArrange.getSchoolId(), classroomNoList[0]);
        if (classroom == null)
            throw new BizException(Result.CUSTOM_MESSAGE, "教室不存在");

        int firstClassroomExSeatCount = classroom.getExSeatCount();
        String[] classroomNoListOrderByExSeatCount = new String[classroomNoList.length];
        if (classroomNoList.length > 1) {
            Classroom secondClassroom = classroomService.findOne(examArrange.getSchoolId(), classroomNoList[1]);
            // 优先把考试座位数少的教室坐满
            if (secondClassroom.getExSeatCount() < classroom.getExSeatCount()) {
                firstClassroomExSeatCount = secondClassroom.getExSeatCount();
                classroomNoListOrderByExSeatCount[0] = secondClassroom.getNo();
                classroomNoListOrderByExSeatCount[1] = classroom.getNo();
            } else {
                classroomNoListOrderByExSeatCount[0] = classroom.getNo();
                classroomNoListOrderByExSeatCount[1] = secondClassroom.getNo();
            }

            if (userList.size() > firstClassroomExSeatCount) {
                // 安排第一个教室
                dataList = getDataList(0, firstClassroomExSeatCount, examTime, examArrange.getCourseNo(),
                        examArrange.getCourseSerialNo(), examArrange.getCourseName(), classroomNoListOrderByExSeatCount[0],
                        invigilateTeacherNameList, patrolTeacherNameList, userList, dataList);
                // 安排第二个教室
                dataList = getDataList(firstClassroomExSeatCount, userList.size(), examTime, examArrange.getCourseNo(),
                        examArrange.getCourseSerialNo(), examArrange.getCourseName(), classroomNoListOrderByExSeatCount[1],
                        invigilateTeacherNameList, patrolTeacherNameList, userList, dataList);
            }
        } else {
            dataList = getDataList(0, userList.size(), examTime, examArrange.getCourseNo(),
                    examArrange.getCourseSerialNo(), examArrange.getCourseName(), classroomNoList[0],
                    invigilateTeacherNameList, patrolTeacherNameList, userList, dataList);
        }

        excelList.addAll(dataList);
        Map<String, List<List<String>>> dataMap = new LinkedHashMap<>();
        dataMap.put(sheetName, excelList);
        return dataMap;
    }

    public List<List<String>> getDataList(int start, int end, String examTime, String courseNo,
                                          String courseSerialNo, String courseName, String classroomNo,
                                          List<String> invigilateTeacherNameList, List<String> patrolTeacherNameList,
                                          List<User> userList, List<List<String>> dataList) {

        for (int i = start; i < end; i++) {
            User user = userList.get(i);
            List<String> temp = new ArrayList<>();
            temp.add(String.valueOf(i + 1));
            temp.add(courseNo);
            temp.add(courseSerialNo);
            temp.add(courseName);
            temp.add(classroomNo);
            temp.add(examTime);
            temp.add(user.getName());
            temp.add(user.getStuNumber());
            temp.add(StringUtils.join(invigilateTeacherNameList, ","));
            temp.add(StringUtils.join(patrolTeacherNameList, ","));
            dataList.add(temp);
        }

        return dataList;
    }

    /**
     * 校验排考是否存在
     *
     * @param teacherCoursePlan
     * @return
     */
    public boolean checkExamArrangeExist(TeacherCoursePlan teacherCoursePlan) {
        // TODO: 2019/3/12 考试时间暂时用2018-09-15来测试
        Date now = null;//new Date();
        try {
            now = DateUtils.parseDate("2018-09-15", new String[]{"yyyy-MM-dd"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ExamArrangeCriteria example = new ExamArrangeCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(teacherCoursePlan.getSchoolId())
                .andCourseNoEqualTo(teacherCoursePlan.getCourseNo())
                .andCourseSerialNoEqualTo(teacherCoursePlan.getCourseSerialNo())
                .andStatusNotEqualTo(NO_PASS)
                .andExamTimeGreaterThanOrEqualTo(now);
        return examArrangeMapper.countByExample(example) > 0 ? true : false;
    }

    public boolean allowToAdjustExamTime(ExamArrange arrange, int currentUserId, int schoolId) {
        int examArrangeStatus = getExamArrangeStatusById(arrange.getId());
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS) {
            return false;
        }
        // TODO: 2019/3/12 调整考试时间的权限
        return false;
    }

    public boolean allowToAdjustClassroomNo(ExamArrange arrange, int currentUserId, int schoolId) {
        int examArrangeStatus = getExamArrangeStatusById(arrange.getId());
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS) {
            return false;
        }
        // TODO: 2019/3/12 调整考试教室的权限
        return false;
    }

    public boolean allowToAddInvigilateTeacher(ExamArrange arrange, int currentUserId, int schoolId) {
        int examArrangeStatus = getExamArrangeStatusById(arrange.getId());
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS) {
            return false;
        }
        // TODO: 2019/3/12 调整监考人员的权限
        return false;
    }

    public boolean allowToAddPatrolTeacher(ExamArrange arrange, int currentUserId, int schoolId) {
        int examArrangeStatus = getExamArrangeStatusById(arrange.getId());
        if (examArrangeStatus == NO_PASS || examArrangeStatus == AUDIT_PASS) {
            return false;
        }
        // TODO: 2019/3/12 调整巡考人员的权限
        return false;
    }
}
