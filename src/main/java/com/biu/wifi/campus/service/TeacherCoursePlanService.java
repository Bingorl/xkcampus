package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.ExcelUtils;
import com.biu.wifi.campus.dao.TeacherCoursePlanMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.TeacherCoursePlanMapperEx;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/7.
 */
@Service
public class TeacherCoursePlanService {

    private static Logger logger = LoggerFactory.getLogger(TeacherCoursePlanService.class);
    @Autowired
    private TeacherCoursePlanMapper teacherCoursePlanMapper;
    @Autowired
    private TeacherCoursePlanMapperEx teacherCoursePlanMapperEx;
    @Autowired
    private FileSupportService fileSupportService;
    @Autowired
    private ClassroomBuildingService classroomBuildingService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private ExamArrangeService examArrangeService;

    public List<TeacherCoursePlan> findList(TeacherCoursePlanCriteria example) {
        return teacherCoursePlanMapper.selectByExample(example);
    }

    /**
     * 根据学校id、所在周次和星期名查询指定日期的课程表
     * <p>
     * 并连续节次的记录拆分成单个节次,同时按照节次升序排列
     *
     * @param schoolId
     * @param courseWeek
     * @param courseWeekDay
     */
    public List<TeacherCoursePlan> findList(int schoolId, int courseWeek, String courseWeekDay) {
        PageLimitHolderFilter.setContext(1, 5, null);
        List<TeacherCoursePlan> teacherCoursePlanList = teacherCoursePlanMapperEx.findList(schoolId, courseWeek, courseWeekDay);
        Set<TeacherCoursePlan> tempList = new HashSet<>();
        for (TeacherCoursePlan teacherCoursePlan : teacherCoursePlanList) {
            int courseSection = Integer.valueOf(teacherCoursePlan.getCourseSection());
            int courseContinueSection = Integer.valueOf(teacherCoursePlan.getCourseContinueSection());
            for (int i = 0; i < courseContinueSection; i++) {
                teacherCoursePlan.setCourseSection(String.valueOf(courseSection + i));
                teacherCoursePlan.setCourseContinueSection("1");
                tempList.add(teacherCoursePlan);
            }
        }

        List<TeacherCoursePlan> newList = new ArrayList<>(tempList);
        Collections.sort(newList, new Comparator<TeacherCoursePlan>() {
            @Override
            public int compare(TeacherCoursePlan o1, TeacherCoursePlan o2) {
                return o1.getCourseSection().compareTo(o2.getCourseSection());
            }
        });

        return newList;
    }

    /**
     * 根据学校id、所在周次和星期名查询指定日期的课程表
     *
     * @param schoolId      学校ID
     * @param courseWeek    所在周次
     * @param courseWeekDay 星期
     * @param startTimeList 预约开始使用时间列表
     * @return
     */
    @Cacheable(value = "defaultCache")
    public List<TeacherCoursePlan> findList(int schoolId, int courseWeek, String courseWeekDay, List<String> startTimeList) {
        List<TeacherCoursePlan> teacherCoursePlanList = findList(schoolId, courseWeek, courseWeekDay);

        // 处理开始使用时间列表,截取HH:mm,转成节次列表
        Date examTime = null;
        List<String> startTimePeriodList = new ArrayList<>();
        for (String startTime : startTimeList) {
            if (examTime == null) {
                try {
                    examTime = DateUtils.parseDate(startTime.split(" ")[0], new String[]{"yyyy-MM-dd"});
                } catch (ParseException e) {
                    logger.error("调用根据学校id、所在周次和星期名查询指定日期的课程表接口时,日期格式转换错误");
                    e.printStackTrace();
                }
            }

            startTimePeriodList.add(startTime.split(" ")[1]);
        }
        Set<String> sectionList = new HashSet<>(teachingWeekService.getCourseIndex(schoolId, startTimePeriodList));

        Iterator<TeacherCoursePlan> iterator = teacherCoursePlanList.iterator();
        while (iterator.hasNext()) {
            TeacherCoursePlan teacherCoursePlan = iterator.next();
            // 如果当前课程为最后一节课且没有安排考试，则该教室可以被预约，需从该集合从剔除
            if (sectionList.contains(teacherCoursePlan.getCourseSection())) {
                // 该课程在该时段内是否有排考记录
                List<ExamArrange> examArrangeList = examArrangeService.findExamArrangeList(schoolId, teacherCoursePlan.getCourseNo(),
                        teacherCoursePlan.getCourseSerialNo(), examTime, startTimePeriodList);
                // 该课程是否是最后一节课
                boolean isLastSection = isLastSection(courseWeek, courseWeekDay, teacherCoursePlan);
                // TODO: 2019/2/15 哪些课程是最后一节课就安排考试的，哪些课程不是，如何区分这两类课程
                if (isLastSection && examArrangeList.size() == 0)
                    iterator.remove();
            }
        }
        return teacherCoursePlanList;
    }

    /**
     * 判断课程是否是最后一节课
     *
     * @param courseWeek        周次
     * @param courseWeekDay     星期名
     * @param teacherCoursePlan
     * @return
     */
    public boolean isLastSection(int courseWeek, String courseWeekDay, TeacherCoursePlan teacherCoursePlan) {
        TeacherCoursePlanCriteria example = new TeacherCoursePlanCriteria();
        example.createCriteria()
                .andCourseNoEqualTo(teacherCoursePlan.getCourseNo())
                .andCourseSerialNoEqualTo(teacherCoursePlan.getCourseSerialNo())
                .andCourseWeekDayEqualTo(String.valueOf(courseWeekDay));
        List<TeacherCoursePlan> teacherCoursePlanList = teacherCoursePlanMapper.selectByExample(example);

        for (TeacherCoursePlan plan : teacherCoursePlanList) {
            // 学期课时周总数
            int courseWeekLength = plan.getCourseWeek().length();
            // 当前周次之后没有课程，即该周为该课程的最后一周
            if (plan.getCourseWeek().substring(courseWeek, courseWeekLength).indexOf("1") == -1)
                return true;
        }
        return false;
    }

    public Result addOrUpdate(TeacherCoursePlan teacherCoursePlan) {
        TeacherCoursePlanCriteria example = new TeacherCoursePlanCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(teacherCoursePlan.getSchoolId())
                .andTermCodeEqualTo(teacherCoursePlan.getTermCode())
                .andClassroomNoEqualTo(teacherCoursePlan.getClassroomNo())
                .andCourseNoEqualTo(teacherCoursePlan.getCourseNo())
                .andCourseSerialNoEqualTo(teacherCoursePlan.getCourseSerialNo())
                .andTeacherNoEqualTo(teacherCoursePlan.getTeacherNo())
                .andCourseWeekEqualTo(teacherCoursePlan.getCourseWeek())
                .andCourseWeekDayEqualTo(teacherCoursePlan.getCourseWeekDay())
                .andCourseSectionEqualTo(teacherCoursePlan.getCourseSection())
                .andCourseContinueSectionEqualTo(teacherCoursePlan.getCourseContinueSection());
        List<TeacherCoursePlan> teacherCoursePlanList = teacherCoursePlanMapper.selectByExample(example);
        if (teacherCoursePlanList.size() == 0) {
            teacherCoursePlan.setCreateTime(new Date());
            teacherCoursePlanMapper.insertSelective(teacherCoursePlan);
        } else {
            teacherCoursePlan.setUpdateTime(new Date());
            teacherCoursePlanMapper.updateByPrimaryKeySelective(teacherCoursePlan);
        }
        return new Result(Result.SUCCESS, "成功");
    }

    public Result importTeacherCoursePlan(Integer schoolId, String fileId) {
        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return new Result(Result.CUSTOM_MESSAGE, "请上传excel格式的数据文件");
        }

        try {
            ExcelUtils excel = ExcelUtils.getInstance(fileIoEntity.getContent(), fileId);

            int rowCount = excel.getSheetRow(0);

            for (int i = 1; i < rowCount; i++) {
                String termCode = excel.read(0, 0, i);
                String courseNo = excel.read(0, 1, i);
                String courseSerialNo = excel.read(0, 2, i);
                String teacherName = excel.read(0, 3, i);
                String courseName = excel.read(0, 4, i);
                String teacherNo = excel.read(0, 5, i);
                String courseWeek = excel.read(0, 6, i);
                String courseWeekDay = excel.read(0, 7, i);
                String courseSection = excel.read(0, 8, i);
                String courseContinueSection = excel.read(0, 9, i);
                String classroomBuildingNo = excel.read(0, 10, i);
                String classroomBuildingName = excel.read(0, 11, i);
                String classroomNo = excel.read(0, 12, i);
                String remark = excel.read(0, 13, i);

                if (StringUtils.isBlank("termCode")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行学年学期代码不能为空，请修改");
                }
                if (StringUtils.isBlank("classroomNo")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行班级号不能为空，请修改");
                }
                if (StringUtils.isBlank("courseName")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行课程名不能为空，请修改");
                }
                if (StringUtils.isBlank("courseNo")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行课程号不能为空，请修改");
                }
                if (StringUtils.isBlank("courseSerialNo")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行课序号不能为空，请修改");
                }
                if (StringUtils.isBlank("courseWeek")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行上课周次不能为空，请修改");
                }
                if (StringUtils.isBlank("courseWeekDay")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行上课星期不能为空，请修改");
                }
                if (StringUtils.isBlank("courseSection")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行上课节次不能为空，请修改");
                }
                if (StringUtils.isBlank("courseContinueSection")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行持续节次不能为空，请修改");
                }
                if (StringUtils.isBlank("teacherNo")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行教师号不能为空，请修改");
                }
                if (StringUtils.isBlank("teacherName")) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行教师名不能为空，请修改");
                }

                //通过教学楼号和教学楼名、学校ID查询教学楼ID
                ClassroomBuildingCriteria classroomBuildingEx1 = new ClassroomBuildingCriteria();
                classroomBuildingEx1.setOrderByClause("create_time desc");
                classroomBuildingEx1.createCriteria()
                        .andNoEqualTo(classroomBuildingNo)
                        .andIsDeleteEqualTo((short) 2);
                ClassroomBuildingCriteria classroomBuildingEx2 = new ClassroomBuildingCriteria();
                classroomBuildingEx2.setOrderByClause("create_time desc");
                ClassroomBuildingCriteria.Criteria criteria = classroomBuildingEx2.createCriteria()
                        .andNameEqualTo(classroomBuildingName)
                        .andIsDeleteEqualTo((short) 2);
                classroomBuildingEx1.or(criteria);
                List<ClassroomBuilding> classroomBuildingList = classroomBuildingService.findList(classroomBuildingEx1);
                if (classroomBuildingList.isEmpty()) {
                    return new Result(Result.CUSTOM_MESSAGE, "第" + i + "行编号为【" + classroomBuildingNo + "】的教学楼不存在，请检查修改");
                }

                //删旧数据
                TeacherCoursePlanCriteria example = new TeacherCoursePlanCriteria();
                example.createCriteria()
                        .andTermCodeEqualTo(termCode)
                        .andClassroomNoEqualTo(classroomNo)
                        .andCourseNoEqualTo(courseNo)
                        .andCourseSerialNoEqualTo(courseSerialNo)
                        .andCourseWeekEqualTo(courseWeek)
                        .andCourseWeekDayEqualTo(courseWeekDay)
                        .andCourseSectionEqualTo(courseSection)
                        .andCourseContinueSectionEqualTo(courseContinueSection)
                        .andTeacherNoEqualTo(teacherNo)
                        .andIsDeleteEqualTo((short) 2);
                teacherCoursePlanMapper.deleteByExample(example);

                //新增新数据
                TeacherCoursePlan teacherCoursePlan = new TeacherCoursePlan();
                teacherCoursePlan.setTermCode(termCode);
                teacherCoursePlan.setSchoolId(schoolId);
                teacherCoursePlan.setBuildingId(classroomBuildingList.get(0).getId());
                teacherCoursePlan.setClassroomNo(classroomNo);
                teacherCoursePlan.setCourseName(courseName);
                teacherCoursePlan.setCourseNo(courseNo);
                teacherCoursePlan.setCourseSerialNo(courseSerialNo);
                teacherCoursePlan.setCourseWeek(courseWeek);
                teacherCoursePlan.setCourseWeekDay(courseWeekDay);
                teacherCoursePlan.setCourseSection(courseSection);
                teacherCoursePlan.setCourseContinueSection(courseContinueSection);
                teacherCoursePlan.setTeacherNo(teacherNo);
                teacherCoursePlan.setTeacherName(teacherName);
                teacherCoursePlan.setRemark(remark);
                addOrUpdate(teacherCoursePlan);
            }

            return new Result(Result.SUCCESS, "成功");
        } catch (Exception e) {
            logger.error("导入教师排课计划数据错误：{}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(Result.FAILURE, "失败");
        } finally {
            /*try {
                fileSupportService.remove(fileId);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(Result.FAILURE, "请上传excel格式的数据文件");
            }*/
        }
    }

    public String getCourseEndTimeSection(TeacherCoursePlan teacherCoursePlan) {
        int length = Integer.valueOf(teacherCoursePlan.getCourseContinueSection());
        StringBuffer sb = new StringBuffer();
        String start = teacherCoursePlan.getCourseSection();
        for (int i = 0; i < length; i++) {
            start = String.valueOf(Integer.valueOf(start) + i);
            sb.append(start);
            if (i != length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public String getExamTimeSection(TeacherCoursePlan teacherCoursePlan) {
        // TODO: 2019/3/12 考试时间节次默认为课程截止时间节次，后续根据需求进行调整 
        return getCourseEndTimeSection(teacherCoursePlan);
    }

    public String getExamTimePeriod(TeacherCoursePlan teacherCoursePlan) {
        String examTimeSection = getExamTimeSection(teacherCoursePlan);
        List<String> startTimeList = teachingWeekService.getPeriodByCourseIndex(String.valueOf(teacherCoursePlan.getSchoolId()), Arrays.asList(examTimeSection.split(",")));
        if (startTimeList == null) {
            return null;
        } else {
            return StringUtils.join(startTimeList, ",");
        }
    }
}
