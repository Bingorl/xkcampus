package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.core.util.DateUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 排考控制器
 *
 * @author zhangbin.
 * @date 2019/2/13.
 */
@Controller
public class AppExamArrangeController extends AuthenticatorController {

    @Autowired
    private ExamArrangeService examArrangeService;
    @Autowired
    private FileSupportService fileSupportService;
    @Autowired
    private ExamArrangeAuditService examArrangeAuditService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ClassroomBookService classroomBookService;
    @Autowired
    private ExamArrangeNoticeService examArrangeNoticeService;
    @Autowired
    private ExamArrangeAuditAdjustLogService examArrangeAuditAdjustLogService;

//    /**
//     * 上传附件
//     *
//     * @param userId
//     * @param attachment    附件二进制文件
//     * @param examArrangeId 排考id
//     * @param response
//     */
//    @RequestMapping("/app_uploadAttachment")
//    public void app_uploadAttachment(@ModelAttribute("user_id") Integer userId,
//                                     @RequestParam(required = false, defaultValue = "") String attachment,
//                                     @RequestParam(required = false) Integer examArrangeId,
//                                     HttpServletResponse response) {
//        Map<String, Object> paramMap = ServletHolderFilter.getContext().getParamMap();
//        if (paramMap.get("attachment") == null || paramMap.get("examArrangeId") == null) {
//            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "请上传附件")));
//            return;
//        }
//
//        if (paramMap.get("examArrangeId") == null) {
//            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少参数")));
//            return;
//        }
//
//        List<DiskFileItem> fileList = (List<DiskFileItem>) paramMap.get("file");
//        if (fileList == null) {
//            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "请上传附件")));
//            return;
//        }
//
//        String fileName;
//        for (int i = 0; i < fileList.size(); i++) {
//            DiskFileItem file = fileList.get(i);
//            fileName = FileUtilsEx.getFileNameByPath(file.getName());
//            byte[] fileContent = file.get();
//            attachment += fileSupportService.add(fileName, fileContent, "ds_upload");
//            if (i != fileList.size() - 1)
//                attachment += ",";
//        }
//
//        examArrangeId = (Integer) paramMap.get("examArrangeId");
//        examArrangeService.updateAttachmentPersistence(null, examArrangeId, attachment);
//        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
//    }

    /**
     * 调整排考时间
     *
     * @param userId
     * @param examArrangeId           排考id
     * @param adjustExamTime          调整后的考试时间
     * @param adjustExamTimeWeekIndex 调整后的考试时间所在周次
     * @param adjustExamTimeSection   调整后的考试时间所在节次
     * @param response
     */
    @RequestMapping("/app_updateExamTime")
    public void app_updateExamTime(@ModelAttribute("user_id") int userId, Integer examArrangeId, String adjustExamTime,
                                   Integer adjustExamTimeWeekIndex, String adjustExamTimeSection, HttpServletResponse response) {
        if (examArrangeId == null || adjustExamTime == null || adjustExamTimeWeekIndex == null || StringUtils.isBlank(adjustExamTimeSection)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        // 根据排考时间推算星期名
        Date adjustExamTimeDate;
        String adjustExamTimeWeekName;
        try {
            adjustExamTimeDate = DateUtils.parseDate(adjustExamTime, new String[]{"yyyy-MM-dd"});
            adjustExamTimeWeekName = DateUtilsEx.getDayofWeek(adjustExamTimeDate).replace("星期", "周");
        } catch (Exception e) {
            throw new BizException(Result.CUSTOM_MESSAGE, "时间参数格式错误");
        }

        ExamArrange examArrange = examArrangeService.updateExamTimePersistence(null, examArrangeId,
                adjustExamTimeDate, adjustExamTimeWeekIndex, adjustExamTimeWeekName, adjustExamTimeSection);
        examArrangeAuditAdjustLogService.addLog(examArrangeId, userId, examArrangeAuditAdjustLogService.UPDATE_EXAM_TIME,
                TimeUtil.format(examArrange.getExamTime(), "yyyy-MM-dd"), TimeUtil.format(examArrange.getOldExamTime(), "yyyy-MM-dd"));
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 调整教室
     *
     * @param userId
     * @param examArrangeId     排考id
     * @param adjustClassroomNo 调整后的教室号，逗号分割
     * @param response
     */
    @RequestMapping("/app_updateAdjustClassroomNo")
    public void app_updateAdjustClassroomNo(@ModelAttribute("user_id") int userId, Integer examArrangeId,
                                            String adjustClassroomNo, HttpServletResponse response) {
        if (examArrangeId == null || StringUtils.isBlank(adjustClassroomNo)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ExamArrange examArrange = examArrangeService.updateAdjustClassroomNoPersistence(null, examArrangeId, adjustClassroomNo);
        examArrangeAuditAdjustLogService.addLog(examArrangeId, userId, examArrangeAuditAdjustLogService.UPDATE_EXAM_CLASSROOM,
                examArrange.getClassroomNo(), examArrange.getOldClassroomNo());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 更新监考人员
     *
     * @param userId
     * @param examArrangeId       排考id
     * @param invigilateTeacherId 监考人员id，逗号分隔
     * @param response
     */
    @RequestMapping("/app_updateInvigilateTeacherId")
    public void app_updateInvigilateTeacherId(@ModelAttribute("user_id") int userId, Integer examArrangeId,
                                              String invigilateTeacherId, HttpServletResponse response) {
        if (examArrangeId == null || StringUtils.isBlank(invigilateTeacherId)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ExamArrange examArrange = examArrangeService.findById(examArrangeId);
        String oldInvigilateTeacherId = examArrange.getInvigilateTeacherId();
        examArrange = examArrangeService.updateInvigilateTeacherIdPersistence(null, examArrangeId, invigilateTeacherId);
        examArrangeAuditAdjustLogService.addLog(examArrangeId, userId, examArrangeAuditAdjustLogService.UPDATE_INVIGILATE_TEACHER,
                examArrange.getInvigilateTeacherId(), oldInvigilateTeacherId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 更新巡考人员
     *
     * @param userId
     * @param examTimeWeekIndex 考试时间所在周次
     * @param examTimeWeekName  考试时间所在星期名
     * @param examTimeSection   考试时间所在节次
     * @param patrolTeacherId   巡考人员id，逗号分隔
     * @param response
     */
    @RequestMapping("/app_updatePatrolTeacherId")
    public void app_updatePatrolTeacherId(@ModelAttribute("user_id") int userId, Integer examTimeWeekIndex,
                                          String examTimeWeekName, String examTimeSection, String patrolTeacherId, HttpServletResponse response) {
        if (examTimeWeekIndex == null || StringUtils.isBlank(examTimeWeekName) || StringUtils.isBlank(examTimeSection) || StringUtils.isBlank(patrolTeacherId)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ExamArrangeCriteria example = new ExamArrangeCriteria();
        example.createCriteria()
                .andExamTimeWeekIndexEqualTo(examTimeWeekIndex)
                .andExamTimeWeekNameEqualTo(examTimeSection)
                .andExamTimeSectionEqualTo(examTimeSection);
        List<ExamArrange> examArrangeList = examArrangeService.findList(example);
        examArrangeService.updatePatrolTeacherIdPersistence(examArrangeList, patrolTeacherId, userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 排考审核操作(教学秘书或教务处确认操作)
     *
     * @param userId
     * @param examArrangeAuditId 排考审核id
     * @param isPass             1通过,0不通过
     * @param remark             审核意见,不通过时必传
     * @param response
     */
    @RequestMapping("/app_updateExamArrangeAudit")
    public void app_updateExamArrangeAudit(@ModelAttribute("user_id") Integer userId, Integer examArrangeAuditId,
                                           @RequestParam(required = false, defaultValue = "1") Integer isPass,
                                           @RequestParam(required = false) String remark, HttpServletResponse response) {
        if (examArrangeAuditId == null || (isPass == 0 && StringUtils.isBlank(remark))) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ExamArrangeAudit examArrangeAudit = examArrangeAuditService.findById(examArrangeAuditId);
        if (examArrangeAudit == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该审核记录不存在")));
            return;
        }

        examArrangeAudit.setIsPass(isPass);
        examArrangeAudit.setAuditRemark(remark);
        examArrangeAuditService.updateExamArrangeAudit(examArrangeAudit);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 调整考试时间选择周次和节次
     *
     * @param schoolId 学校id
     * @param response
     */
    @RequestMapping("/app_findWeekIndexList")
    public void app_findWeekIndexList(Integer schoolId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        // 根据当前时间确定所在学期
//        String termCode = teachingWeekService.getTermCode(new Date());
        String termCode = "2018-2019-1-1";
        TeachingWeekCriteria example = new TeachingWeekCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andTermCodeEqualTo(termCode);
        List<TeachingWeek> teachingWeekList = teachingWeekService.findList(example);
        if (teachingWeekList.isEmpty())
            throw new BizException(Result.CUSTOM_MESSAGE, "暂未查询到学年学期列表，请联系教务处");

        TeachingWeek teachingWeek = teachingWeekList.get(0);
        String[] mondayList = teachingWeek.getMondayDate().split(",");

        // 周次列表
        List<Map> weekList = new ArrayList<>();
        int index = 0;
        for (String monday : mondayList) {
            index++;
            Map map = new HashMap();
            map.put("index", index);

            List<String> dateList = new ArrayList<>();
            for (int i = 0; i < 5; i++)
                dateList.add(TimeUtil.addDays(monday, i));

            map.put("dateList", dateList);
            weekList.add(map);
        }

        // 节次列表
        TeachingCourseTimePlanCriteria teachingCourseTimePlanEx = new TeachingCourseTimePlanCriteria();
        teachingCourseTimePlanEx.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        List<TeachingCourseTimePlan> teachingCourseTimePlanList = teachingWeekService.findTeachingCourseTimePlanList(teachingCourseTimePlanEx);

        Map data = new HashMap();
        data.put("weekList", weekList);
        data.put("courseTimePlanList", teachingCourseTimePlanList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", data)));
    }

    /**
     * 排考调整教室查询空教室列表
     *
     * @param schoolId      学校ID
     * @param examArrangeId 排考ID
     * @param isMedia       是否包含多媒体教室（1是，2否）
     * @param response
     */
    @RequestMapping("app_findClassroomListForExamArrange")
    public void app_findClassroomListForExamArrange(@ModelAttribute("user_id") Integer userId,
                                                    @RequestParam Integer schoolId,
                                                    @RequestParam Integer examArrangeId,
                                                    @RequestParam(defaultValue = "2", required = false) Integer isMedia, HttpServletResponse response) {

        if (schoolId == null || examArrangeId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        // 查询排考记录获取考试时间
        ExamArrange examArrange = examArrangeService.findById(examArrangeId);
        // 考试时间日期
        Date bookDate = examArrange.getExamTime();
        String bookDateStr = TimeUtil.format(bookDate, "yyyy-MM-dd");
        int courseWeek = examArrange.getExamTimeWeekIndex();
        int courseWeekDay = TimeUtil.getDay(bookDate);
        // 考试时间段字符串(如08:00,08:50)
        String courseIndex = examArrange.getExamTimeSection();
        String bookPeriod = teachingWeekService.getPeriodByCourseIndex(schoolId, Arrays.asList(courseIndex.split(",")));

        //将选择的开始使用时间进行升序排列
        List<String> bookPeriodList = Arrays.asList(bookPeriod.split(","));
        Collections.sort(bookPeriodList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        bookPeriod = StringUtils.join(bookPeriodList.toArray(), ",");

        //筛选日期时间
        List<String> startTimeList = classroomBookService.getStartTimeListByBookPeriod(bookDateStr, bookPeriod);

        TeachingWeek teachingWeek = teachingWeekService.getTeachingWeekBySchoolId(schoolId);
        if (teachingWeek == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "没有设置学年学期，请联系教务")));
            return;
        }

        List<Classroom> classroomList = classroomService.findReleaseList(schoolId, teachingWeek.getTermCode(), courseWeek,
                String.valueOf(courseWeekDay), null, null, isMedia, startTimeList, "seatCount", true);

        if (classroomList.isEmpty()) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NO_DATA_RETURN_PAGE, "没有符合条件的教室")));
        } else {
            //保留buildingId、createTime、name、no、status、seatCount、exSeatCount部分字段
            List<Map> mapList = BeanUtil.beanListToMapList(classroomList, "retainKeys",
                    new String[]{"buildingId", "createTime", "name", "no", "status", "seatCount", "exSeatCount"});

            //调整教室的时候，标识出已选择和未选择
            for (Map map : mapList) {
                map.put("selected", false);
                map.put("name", MapUtils.getString(map, "name") + "  (座位数：" + MapUtils.getIntValue(map, "exSeatCount") + ")");
            }

            //排考当前选择的教室
            List<String> selectedClassroomNoList = Arrays.asList(examArrange.getClassroomNo().split(","));

            ClassroomCriteria classroomEx = new ClassroomCriteria();
            classroomEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andNoIn(selectedClassroomNoList);
            classroomList = classroomService.findList(classroomEx);
            for (Classroom classroom : classroomList) {
                Map map = new HashMap();
                map.put("buildingId", classroom.getBuildingId());
                map.put("createTime", classroom.getCreateTime());
                map.put("name", classroom.getName() + "  (座位数：" + classroom.getExSeatCount() + ")");
                map.put("no", classroom.getNo());
                map.put("status", classroom.getStatus());
                map.put("seatCount", classroom.getSeatCount());
                map.put("exSeatCount", classroom.getExSeatCount());
                map.put("selected", true);
                mapList.add(map);
            }

            Collections.sort(mapList, new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    Integer c1 = Integer.valueOf(o1.get("exSeatCount").toString());
                    Integer c2 = Integer.valueOf(o2.get("exSeatCount").toString());
                    return c1.compareTo(c2);
                }
            });
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", mapList)));
        }
    }

    /**
     * 排考通知详情
     *
     * @param examArrangeNoticeId 排考通知id
     * @param response
     */
    @RequestMapping("app_findExamArrangeNoticeDetails")
    public void app_findExamArrangeNoticeDetails(@ModelAttribute("user_id") Integer userId,
                                                 @RequestParam Integer examArrangeNoticeId, HttpServletResponse response) {
        if (examArrangeNoticeId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        Map noticeMap = examArrangeNoticeService.findExamArrangeNoticeMapById(examArrangeNoticeId);
        if (noticeMap == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NODATA, "没有相关数据")));
            return;
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", noticeMap)));
    }

    /**
     * 排考审核详情
     *
     * @param examArrangeAuditId 排考审核id
     * @param response
     */
    @RequestMapping("app_findExamArrangeAuditDetails")
    public void app_findExamArrangeAuditDetails(@ModelAttribute("user_id") Integer userId,
                                                @RequestParam Integer examArrangeAuditId, HttpServletResponse response) {
        if (examArrangeAuditId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        Map auditMap = examArrangeAuditService.findExamArrangeAuditMapById(examArrangeAuditId);
        if (auditMap == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NODATA, "没有相关数据")));
            return;
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", auditMap)));
    }

    /**
     * 排考详情
     *
     * @param userId
     * @param examArrangeId 排考id
     * @param schoolId      学校id
     * @param response
     */
    @RequestMapping("app_findExamArrangeDetails")
    public void app_findExamArrangeDetails(@ModelAttribute("user_id") Integer userId,
                                           @RequestParam Integer examArrangeId,
                                           @RequestParam Integer schoolId, HttpServletResponse response) {
        if (examArrangeId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ExamArrange examArrange = examArrangeService.findById(examArrangeId);
        if (examArrange == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NODATA, "没有相关数据")));
            return;
        }
        Map examArrangeMap = new HashMap(BeanMap.create(examArrange));
        // 根据身份来确定是否具有排考审核的相关调整权限
        examArrangeMap.put("allowToAdjustExamTime", examArrangeService.allowToAdjustExamTime(examArrange, userId, schoolId));
        examArrangeMap.put("allowToAdjustClassroomNo", examArrangeService.allowToAdjustClassroomNo(examArrange, userId, schoolId));
        examArrangeMap.put("allowToAddInvigilateTeacher", examArrangeService.allowToAddInvigilateTeacher(examArrange, userId, schoolId));
        examArrangeMap.put("allowToAddPatrolTeacher", examArrangeService.allowToAddPatrolTeacher(examArrange, userId, schoolId));
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", examArrangeMap)));
    }

    /**
     * 删除我的排考通知
     *
     * @param userId
     * @param examArrangeNoticeId 排考通知id
     * @param response
     */
    @RequestMapping("app_deleteMyExamArrangeNotice")
    public void app_deleteMyExamArrangeNotice(@ModelAttribute("user_id") Integer userId,
                                              @RequestParam Integer examArrangeNoticeId, HttpServletResponse response) {
        if (examArrangeNoticeId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        examArrangeNoticeService.deleteMyExamArrangeNotice(userId, examArrangeNoticeId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 删除我的排考审核
     *
     * @param userId
     * @param examArrangeAuditId 排考审核id
     * @param response
     */
    @RequestMapping("app_deleteMyExamArrangeAudit")
    public void app_deleteMyExamArrangeAudit(@ModelAttribute("user_id") Integer userId,
                                             @RequestParam Integer examArrangeAuditId, HttpServletResponse response) {
        if (examArrangeAuditId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        examArrangeAuditService.deleteMyExamArrangeAudit(userId, examArrangeAuditId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }
}
