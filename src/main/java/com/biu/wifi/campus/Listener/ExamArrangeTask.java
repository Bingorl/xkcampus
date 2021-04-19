package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.ClassroomMapper;
import com.biu.wifi.campus.dao.ClassroomOccupyMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.ClassroomBookItemMapperEx;
import com.biu.wifi.campus.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author zhangbin.
 * @date 2019/2/16.
 */
// @Component
public class ExamArrangeTask {

    private static Logger logger = LoggerFactory.getLogger(ClassroomBookTask.class);
    @Autowired
    private ExamArrangeService examArrangeService;
    @Autowired
    private ClassroomBookItemMapperEx classroomBookItemMapperEx;
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private ClassroomOccupyMapper classroomOccupyMapper;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private TeacherCoursePlanService teacherCoursePlanService;
    @Autowired
    private ExamArrangeNoticeService examArrangeNoticeService;

    /**
     * 智能排考创建排考记录
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void addExamArrange() {
        School school = new School();
        school.setIsDelete((short) 2);
        List<School> schoolList = schoolService.findList(school);
        for (School sch : schoolList) {
            // 当前学年学期
            TeachingWeek teachingWeek = getTeachingWeekBySchoolId(sch);
            if (teachingWeek == null) continue;

            // 根据当前时间推算所在周次
            Integer currentWeekIndex = getWeekIndexByCurrentTime(teachingWeek);
            if (currentWeekIndex == null) continue;

            // 根据当前时间推算是周几
            int dayOfWeek = TimeUtil.getDay(new Date());

            //排课记录
            List<TeacherCoursePlan> teacherCoursePlanList = getTeacherCoursePlanList(teachingWeek, currentWeekIndex, dayOfWeek);
            for (TeacherCoursePlan teacherCoursePlan : teacherCoursePlanList) {
                logger.info("排课：{}", teacherCoursePlan.toString());
                // 排考验重
                if (examArrangeService.checkExamArrangeExist(teacherCoursePlan)) continue;

                final ExamArrange arrange = examArrangeService.addExamArrangePersistence(teacherCoursePlan, currentWeekIndex);
                // 异步创建考试座位表的excel
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        examArrangeService.updateAttachmentPersistence(arrange, arrange.getId());
                    }
                }).start();
            }
        }
    }

    /**
     * 智能排考推送排考通知(推送给教学秘书或者教务处人员)
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void pushExamArrangeNotice() {
        List<ExamArrangeNotice> examArrangeNoticeList = examArrangeNoticeService.findUnPushedExamArrangeNoticeList();
        for (ExamArrangeNotice examArrangeNotice : examArrangeNoticeList) {
            examArrangeNoticeService.pushExamArrangeNotice(examArrangeNotice, examArrangeNotice.getToUser(), examArrangeNotice.getTitle());
        }
    }

    /**
     * 智能排考占用教室
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void occupyClassroom() {
        School school = new School();
        school.setIsDelete((short) 2);
        Date now = new Date();
        ExamArrangeCriteria example = new ExamArrangeCriteria();
        example.createCriteria()
                .andExamTimeGreaterThanOrEqualTo(now)
                .andStatusEqualTo(examArrangeService.AUDIT_PASS);
        List<ExamArrange> examArrangeList = examArrangeService.findList(example);

        for (ExamArrange examArrange : examArrangeList) {
            //根据教室号查询教室
            ClassroomCriteria classroomEx = new ClassroomCriteria();
            classroomEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andStatusEqualTo((short) 1)
                    .andNoIn(Arrays.asList(examArrange.getClassroomNo().split(",")));
            List<Classroom> classroomList = classroomService.findList(classroomEx);
            for (Classroom classroom : classroomList) {
                if (classroom == null) {
                    logger.error("教室【" + classroom.getNo() + "】不存在，请联系教务");
                    return;
                } else if (classroom.getIsDelete() == 1) {
                    logger.error("教室【" + classroom.getNo() + "】不存在，请联系教务");
                    return;
                } else if (classroom.getStatus().intValue() == 2) {
                    logger.error("教室【" + classroom.getNo() + "】已经被停用");
                    return;
                } else if (classroom.getStatus().intValue() == 3) {
                    logger.error("教室【" + classroom.getNo() + "】已经被占用");
                    return;
                }

                String startTime = null, endTime = null;
                List<String> HHmmList = teachingWeekService.getPeriodByCourseIndex(String.valueOf(examArrange.getSchoolId()),
                        Arrays.asList(examArrange.getExamTimeSection().split(",")));
                for (String hhmm : HHmmList) {
                    startTime = TimeUtil.format(examArrange.getExamTime(), "yyyy-MM-dd") + " " + hhmm;
                    endTime = teachingWeekService.getEndTimeBySchooldAndStartTime(examArrange.getSchoolId(), hhmm);

                    //教室在相同时间被预定
                    long bookedCount = classroomBookItemMapperEx.findBookedCount(null, classroom.getBuildingId(), classroom.getNo(), startTime);
                    if (bookedCount > 0) {
                        logger.error("排考" + examArrange.toString() + "执行占用教室，教室【" + classroom.getNo() + "】已经被占用");
                        return;
                    }

                    // 教室在相同时间被排考占用
                    List<String> startTimeList = new ArrayList<>();
                    for (String period : examArrange.getExamTimePeriod().split(","))
                        startTimeList.add(TimeUtil.format(examArrange.getExamTime(), "yyyy-MM-dd") + " " + period);
                    List<ExamArrange> queryExamArrangeList = examArrangeService.findExamArrangeList(examArrange.getSchoolId(), examArrange.getClassroomNo(),
                            examArrange.getExamTime(), startTimeList);
                    if (!queryExamArrangeList.isEmpty()) {
                        logger.error("排考" + examArrange.toString() + "执行占用教室，教室【" + classroom.getNo() + "】已经被占用");
                        return;
                    }

                    ClassroomOccupy classroomOccupy = new ClassroomOccupy();
                    classroomOccupy.setClassroomId(classroom.getId());
                    classroomOccupy.setStartTime(startTime);
                    classroomOccupy.setEndTime(endTime);
                    int count1 = classroomOccupyMapper.insertSelective(classroomOccupy);

                    classroomEx = new ClassroomCriteria();
                    classroomEx.createCriteria()
                            .andIdEqualTo(classroom.getId())
                            .andVersionEqualTo(classroom.getVersion());

                    classroom.setStatus((short) 3);
                    Date date = new Date();
                    classroom.setUpdateTime(date);
                    classroom.setVersion(String.valueOf(date.getTime()));

                    int count2 = classroomMapper.updateByExampleSelective(classroom, classroomEx);
                    if (count1 <= 0 && count2 <= 0)
                        logger.error("排考" + examArrange.toString() + "执行占用教室失败");
                }
            }
        }
    }

    private TeachingWeek getTeachingWeekBySchoolId(School school) {
        TeachingWeekCriteria teachingWeekEx = new TeachingWeekCriteria();
        teachingWeekEx.setOrderByClause("create_time desc");
        teachingWeekEx.createCriteria()
                .andSchoolIdEqualTo(school.getId());
        List<TeachingWeek> teachingWeekList = teachingWeekService.findList(teachingWeekEx);
        if (teachingWeekList.isEmpty()) {
            logger.error("学校{}暂未设置学年学期,创建排考记录任务终止", school.getName());
            return null;
        }
        return teachingWeekList.get(0);
    }

    private Integer getWeekIndexByCurrentTime(TeachingWeek teachingWeek) {
        String now = "2018-10-15";//TimeUtil.format(new Date(), "yyyy-MM-dd");
        String[] mondayDateList = teachingWeek.getMondayDate().split(",");
        Integer currentWeekIndex = null;
        for (int i = 0; i < mondayDateList.length; i++) {
            String current = mondayDateList[i];
            if (i != mondayDateList.length - 1) {
                String next = mondayDateList[i + 1];
                if (now.compareTo(current) >= 0 && now.compareTo(next) < 0) {
                    currentWeekIndex = i + 1;
                    break;
                }
            } else {
                currentWeekIndex = mondayDateList.length;
            }
        }
        if (currentWeekIndex + 2 > teachingWeek.getWeekCount()) {
            logger.error("排考需提前两周进行,当前周次[{}/{}]不满足条件", currentWeekIndex, teachingWeek.getWeekCount());
            return null;
        } else {
            return currentWeekIndex;
        }
    }

    private List<TeacherCoursePlan> getTeacherCoursePlanList(TeachingWeek teachingWeek, int currentWeekIndex, int dayOfWeek) {
        TeacherCoursePlanCriteria teacherCoursePlanEx = new TeacherCoursePlanCriteria();
        teacherCoursePlanEx.createCriteria()
                .andSchoolIdEqualTo(teachingWeek.getSchoolId())
                .andTermCodeEqualTo(teachingWeek.getTermCode())
                .andCourseWeekDayEqualTo(String.valueOf(dayOfWeek))
                .andIsDeleteEqualTo((short) 2);
        List<TeacherCoursePlan> teacherCoursePlanList = teacherCoursePlanService.findList(teacherCoursePlanEx);
        Iterator<TeacherCoursePlan> iterator = teacherCoursePlanList.iterator();
        while (iterator.hasNext()) {
            TeacherCoursePlan teacherCoursePlan = iterator.next();
            int length = teacherCoursePlan.getCourseWeek().length();
            // 当前周没有课
            String str1 = teacherCoursePlan.getCourseWeek().substring(currentWeekIndex - 1, currentWeekIndex);
            // 当前周之后就没有课程了
            String str2 = teacherCoursePlan.getCourseWeek().substring(currentWeekIndex, length);
            if ("0".equals(str1) || str2.indexOf("1") != -1)
                iterator.remove();
        }
        return teacherCoursePlanList;
    }
}
