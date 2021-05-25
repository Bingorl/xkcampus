package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.constant.AudUserRole;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/6.
 */
@Controller
public class AppClassroomBookController extends AuthenticatorController {

    private static final Logger logger = LoggerFactory.getLogger(AppClassroomBookController.class);
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ClassroomBuildingService classroomBuildingService;
    @Autowired
    private ClassroomBookUseTypeService classroomBookUseTypeService;
    @Autowired
    private ClassroomBookUseTypeOrganizationService classroomBookUseTypeOrganizationService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassroomBookService classroomBookService;
    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserService classroomBookUseTypeOrganizationAuditUserService;
    @Autowired
    private ClassroomBookAuditService classroomBookAuditService;
    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private AuditRolePermissionService auditRolePermissionService;
    @Autowired
    private AuditUserRoleService auditUserRoleService;

    /**
     * 教学楼列表
     *
     * @param schoolId
     * @param response
     */
    @RequestMapping("app_findClassroomBuildingList")
    public void app_findClassroomBuildingList(Integer schoolId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ClassroomBuildingCriteria example = new ClassroomBuildingCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        List<ClassroomBuilding> classroomBuildingList = classroomBuildingService.findList(example);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", classroomBuildingList)));
    }

    /**
     * 空教室列表
     * <p>
     * 调整教室：只需要传classroomBookId，其余参数不传
     *
     * @param classroomBookId 教室预约ID(调整教室查询空教室的时候传)
     * @param schoolId        学校ID
     * @param buildingId      教学楼ID
     * @param seatRange       人数范围（0-50）
     * @param isMedia         是否包含多媒体教室（1是，2否）
     * @param bookDateStr     预约使用日期
     * @param bookPeriod      开始时间字符串(08:00,08:50)
     * @param response
     */
    @RequestMapping("app_findClassroomList")
    public void app_findClassroomList(@ModelAttribute("user_id") Integer userId,
                                      @RequestParam(required = false) Integer classroomBookId,
                                      Integer schoolId,
                                      Integer buildingId,
                                      @RequestParam(defaultValue = "0-50", required = false) String seatRange,
                                      @RequestParam(defaultValue = "2", required = false) Integer isMedia,
                                      String bookDateStr, String bookPeriod, HttpServletResponse response) {
        ClassroomBook classroomBook = null;
        if (classroomBookId != null) {
            User user = userService.getById(userId);
            AuditUserAuth userAuth = auditUserAuthService.find(user.getSchoolId(), userId);
            boolean hasPermission = auditRolePermissionService.findPermissionByCodeAndRoleId(userAuth.getRoleId(),
                    "classroomBook/adjustClassroomNo/update");
            if (!hasPermission) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "没有调整的权限")));
                return;
            }

            classroomBook = classroomBookService.findById(classroomBookId);
            try {
                Map condition = new ObjectMapper().readValue(classroomBook.getBookCondition(), Map.class);
                buildingId = (Integer) condition.get("buildingId");
                seatRange = (String) condition.get("seatRange");
                isMedia = (Integer) condition.get("isMedia");
                bookDateStr = (String) condition.get("bookDateStr");
                bookPeriod = (String) condition.get("bookPeriod");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("调整教室查询空教室时，解析查询条件失败");
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
                return;
            }
        }

        if (buildingId == null || bookDateStr == null || bookPeriod == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        //筛选日期时间
        List<String> startTimeList = classroomBookService.getStartTimeListByBookPeriod(bookDateStr,bookPeriod);
//        Date now = new Date();
//        for (String time : bookPeriod.split(",")) {
//            try {
//                String str = bookDateStr + " " + time;
//                Date startTime = DateUtils.parseDate(str, new String[]{"yyyy-MM-dd HH:mm"});
//                if (startTime.compareTo(now) <= 0) {
//                    ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "预定时间必须大于当前时间")));
//                    return;
//                }
//                startTimeList.add(str);
//            } catch (ParseException e) {
//                e.printStackTrace();
//                logger.error("日期格式解析错误：{}", e.getMessage());
//                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "失败")));
//                return;
//            }
//        }

        //根据当前时间计算周次
        TeachingWeek teachingWeek = teachingWeekService.getTeachingWeekBySchoolId(schoolId);
        if (teachingWeek == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "没有设置学年学期，请联系教务")));
            return;
        }

        List<String> mondayList = new ArrayList<>();
        if (StringUtils.isNotBlank(teachingWeek.getMondayDate())) {
            mondayList = Arrays.asList(teachingWeek.getMondayDate().split(","));
        }
        int courseWeek = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date bookDate = null;
        for (int i = 0; i < mondayList.size(); i++) {
            String thisMonday = mondayList.get(i);
            Date m, f;
            try {
                bookDate = sdf.parse(bookDateStr);
                m = sdf.parse(thisMonday);
                f = DateUtils.addDays(m, 6);
                if (bookDate.compareTo(m) >= 0 && bookDate.compareTo(f) <= 0) {
                    courseWeek = i + 1;
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("时间解析异常：{}", e.getMessage());
            }
        }

        if (courseWeek == 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "请在本学期的时间段内进行教室预约")));
            return;
        }

        int courseWeekDay = TimeUtil.getDay(bookDate);
        List<Classroom> classroomList = classroomService.findReleaseList(schoolId, teachingWeek.getTermCode(), courseWeek,
                String.valueOf(courseWeekDay), buildingId, seatRange, isMedia, startTimeList, "seatCount", true);
        if (classroomList.size() == 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NO_DATA_RETURN_PAGE, "没有符合条件的教室")));
        } else {
            //保留buildingId、createTime、name、no、status、seatCount部分字段
            List<Map> mapList = BeanUtil.beanListToMapList(classroomList, "retainKeys",
                    "buildingId", "createTime", "name", "no", "status", "seatCount");

            if (classroomBookId != null) {
                //调整教室的时候，标识出已选择和未选择
                for (Map map : mapList) {
                    map.put("selected", false);
                }


                List<String> selectedClassroomNoList;
                if (StringUtils.isBlank(classroomBook.getAdjustClassroomNo())) {
                    selectedClassroomNoList = Arrays.asList(classroomBook.getClassroomNo().split(","));
                } else {
                    selectedClassroomNoList = Arrays.asList(classroomBook.getAdjustClassroomNo().split(","));
                }

                ClassroomCriteria classroomEx = new ClassroomCriteria();
                classroomEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andBuildingIdEqualTo(buildingId)
                        .andNoIn(selectedClassroomNoList);
                classroomList = classroomService.findList(classroomEx);
                for (Classroom classroom : classroomList) {
                    Map map = new HashMap();
                    map.put("buildingId", classroom.getBuildingId());
                    map.put("createTime", classroom.getCreateTime());
                    map.put("name", classroom.getName() + "  (座位数：" + classroom.getSeatCount() + ")");
                    map.put("no", classroom.getNo());
                    map.put("status", classroom.getStatus());
                    map.put("seatCount", classroom.getSeatCount());
                    map.put("selected", true);
                    mapList.add(map);
                }

                Collections.sort(mapList, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        Integer c1 = Integer.valueOf(o1.get("seatCount").toString());
                        Integer c2 = Integer.valueOf(o2.get("seatCount").toString());
                        return c1.compareTo(c2);
                    }
                });
            }
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", mapList)));
        }
    }

    /**
     * 新增教室预定
     * <p>
     * 非必传参数：adjustClassroomNo,isUseMedia,remark
     *
     * @param classroomBuildingId 教学楼ID
     * @param seatRange           人数范围
     * @param bookDate            预约使用日期
     * @param bookPeriod          开始时间字符串(08:00,08:50)
     * @param classroomNo         教室编号，逗号分隔
     * @param useTypeId           使用类型ID
     * @param organizationId      组织ID
     * @param title               标题
     * @param content             内容
     * @param isMedia             是否包含多媒体教室（1是，2否）
     * @param isUseMedia          是否使用了多媒体(1是，2否)
     * @param userId              申请人ID
     * @param linkMan             联系人
     * @param linkPhone           联系电话
     * @param linkManNo           工号或学号
     * @param remark
     * @param response
     */
    @RequestMapping("app_addClassroomBook")
    public void app_addClassroomBook(Integer classroomBuildingId, String seatRange, String bookDate, String bookPeriod, String classroomNo,
                                     Integer useTypeId, Integer organizationId, String title, String content, Short isMedia, Short isUseMedia,
                                     @ModelAttribute("user_id") Integer userId,
                                     String linkMan, String linkPhone, String linkManNo, String remark, HttpServletResponse response) {
        if (classroomBuildingId == null || bookDate == null || bookPeriod == null || classroomNo == null || useTypeId == null || organizationId == null
                || title == null || content == null || userId == null || linkMan == null || linkPhone == null || linkManNo == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        //将选择的开始时间进行升序排列
        List<String> bookPeriodList = Arrays.asList(bookPeriod.split(","));
        Collections.sort(bookPeriodList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        bookPeriod = StringUtils.join(bookPeriodList.toArray(), ",");

        //将查询条件保存成一个map对象的json字符串
        Map conditionMap = new HashMap();
        conditionMap.put("userId", userId);
        conditionMap.put("buildingId", classroomBuildingId);
        conditionMap.put("seatRange", seatRange);
        conditionMap.put("isMedia", isMedia);
        conditionMap.put("bookDateStr", bookDate);
        conditionMap.put("bookPeriod", bookPeriod);
        String bookCondition;
        try {
            bookCondition = new ObjectMapper().writeValueAsString(conditionMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("查询条件map转存json字符串时，解析json出错：{}", e.getMessage());
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "没有设置学年学期，请联系教务")));
            return;
        }


        ClassroomBook classroomBook = new ClassroomBook();
        classroomBook.setClassroomBuildingId(classroomBuildingId);
        classroomBook.setPersonCount(seatRange);
        classroomBook.setBookDate(bookDate);
        classroomBook.setBookPeriod(bookPeriod);
        classroomBook.setClassroomNo(classroomNo);
        classroomBook.setUseTypeId(useTypeId);
        classroomBook.setOrganizationId(organizationId);
        classroomBook.setTitle(title);
        classroomBook.setContent(content);
        classroomBook.setIsUseMedia(isUseMedia);
        classroomBook.setUserId(userId);
        classroomBook.setLinkMan(linkMan);
        classroomBook.setLinkPhone(linkPhone);
        classroomBook.setLinkManNo(linkManNo);
        classroomBook.setRemark(remark);
        //保存查询的条件，在教务调整教室的时候，根据原条件查询
        classroomBook.setBookCondition(bookCondition);

        List<Date> startTimeList = TimeUtil.getDateList(bookDate, bookPeriod);

        if (CollectionUtils.isEmpty(startTimeList)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请选择预约的时间段")));
            return;
        }

        ClassroomBookCriteria classroomBookEx = new ClassroomBookCriteria();
        ClassroomBookCriteria.Criteria criteria = classroomBookEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andStatusEqualTo(1)
                .andUseTypeIdEqualTo(classroomBook.getUseTypeId())
                .andOrganizationIdEqualTo(classroomBook.getOrganizationId())
                .andBookDateEqualTo(bookDate)
                .andBookPeriodEqualTo(bookPeriod);

        if (classroomBook.getId() != null) {
            criteria.andIdNotEqualTo(classroomBook.getId());
        }

        if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
            criteria.andAdjustClassroomNoEqualTo(classroomBook.getAdjustClassroomNo());
        } else {
            criteria.andClassroomNoEqualTo(classroomBook.getClassroomNo());
        }

        long count = classroomBookService.countByExample(classroomBookEx);
        if (count > 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该预约申请已存在，请勿重复申请")));
            return;
        }
        User user = userService.getById(userId);

        AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(user.getSchoolId(), AudUserRole.JWRY.getCode());
        boolean isJw = false;
        if (auditUserRole != null) {
            AuditUserAuthCriteria auditUserAuthEx = new AuditUserAuthCriteria();
            auditUserAuthEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andUserIdEqualTo(userId)
                    .andRoleIdEqualTo(auditUserRole.getId());
            long c = auditUserAuthService.countByExample(auditUserAuthEx);
            isJw = c > 0;
        }
        Result result = classroomBookService.addOrUpdate(classroomBook, bookDate, startTimeList, user.getSchoolId(), false, isJw);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 教务调整教室
     *
     * @param schoolId          学校ID
     * @param classroomBookId   预约ID
     * @param adjustClassroomNo 调整后的教室号（A104,A106）
     * @param response
     */
    @RequestMapping("app_updateClassroomBook")
    public void app_updateClassroomBook(@ModelAttribute("user_id") Integer userId, Integer schoolId,
                                        Integer classroomBookId, String adjustClassroomNo, HttpServletResponse response) {
        if (userId == null || schoolId == null || classroomBookId == null || adjustClassroomNo == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ClassroomBookCriteria example = new ClassroomBookCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdEqualTo(classroomBookId);
        List<ClassroomBook> classroomBookList = classroomBookService.findList(example);
        if (classroomBookList.isEmpty()) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "该记录不存在")));
            return;
        }

        ClassroomBook classroomBook = classroomBookList.get(0);
        Date date = new Date();
        classroomBook.setUpdateTime(date);
        classroomBook.setAdjustClassroomNo(adjustClassroomNo);
        List<Date> startTimeList = new ArrayList<>();
        for (String time : classroomBook.getBookPeriod().split(",")) {
            try {
                Date startTime = DateUtils.parseDate(classroomBook.getBookDate() + " " + time, new String[]{"yyyy-MM-dd HH:mm"});
                startTimeList.add(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("日期格式解析错误：{}", e.getMessage());
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "失败")));
                return;
            }
        }

        Result result = classroomBookService.addOrUpdate(classroomBook, classroomBook.getBookDate(), startTimeList, schoolId, true, true);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx用户的教室预约记录
     *
     * @param userId   用户ID
     * @param page     页码
     * @param pageSize 每页记录数，不传默认为10
     * @param response
     */
    @RequestMapping("app_findClassroomBookList")
    public void app_findClassroomBookList(@ModelAttribute("user_id") Integer userId,
                                          Integer page, @RequestParam(defaultValue = "10", required = false) Integer pageSize, HttpServletResponse response) {
        if (page == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        User user = userService.getById(userId);

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map> mapList = classroomBookService.findList(userId, user.getSchoolId());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", mapList)));
    }

    /**
     * 教室预约详情
     *
     * @param schoolId        学校ID
     * @param classroomBookId 教室预约ID
     * @param response
     */
    @RequestMapping("app_findClassroomBookDetail")
    public void app_findClassroomBookDetail(@ModelAttribute("user_id") Integer userId,
                                            Integer schoolId, Integer classroomBookId, HttpServletResponse response) {
        if (schoolId == null || classroomBookId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        Map map = classroomBookService.find(schoolId, classroomBookId);

        //审批人进入详情，控制审核按钮的显隐
        boolean allowToDoAudit = classroomBookAuditService.allowToDoClassroomBookAudit(userId, classroomBookId, map.get("status").toString());
        map.put("allowToDoAudit", allowToDoAudit);
        //控制调整按钮的显隐
        User user = userService.getById(userId);

        if (user.getIsTeacher() == 1) {
            AuditUserAuth userAuth = auditUserAuthService.find(user.getSchoolId(), userId);
            boolean allowToDoAdjust = auditRolePermissionService.findPermissionByCodeAndRoleId(userAuth.getRoleId(), "classroomBook/adjustClassroomNo/update");
            map.put("allowToDoAdjust", allowToDoAdjust);
        } else {
            map.put("allowToDoAdjust", false);
        }

        if (map == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该记录不存在", map)));
        } else {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        }
    }

    /**
     * 新增或编辑审批流程时的可供选择的组织列表
     *
     * @param schoolId
     * @param useTypeId  审批流程ID（编辑时传）
     * @param updateFlag 是否是编辑审批流程操作（1是，2否）
     * @param response
     */
    @RequestMapping("app_findClassroomBookUseTypeOrganizationList")
    public void app_findClassroomBookUseTypeOrganizationList(Integer schoolId,
                                                             @RequestParam(required = false) Integer useTypeId,
                                                             @RequestParam(defaultValue = "1", required = false) Integer updateFlag,
                                                             HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        List<Map> list = classroomBookUseTypeOrganizationService.findList(schoolId, useTypeId, updateFlag);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 根据审批流程ID和组织ID查询审批人员列表
     *
     * @param schoolId       学校ID
     * @param useTypeId      审批流程ID
     * @param organizationId 组织ID字符串,逗号分隔
     * @param response
     */
    @RequestMapping("app_findClassroomBookUseTypeOrganizationAuditUserList")
    public void app_findClassroomBookUseTypeOrganizationAuditUserList(Integer schoolId,
                                                                      @RequestParam(required = false) Integer useTypeId,
                                                                      String organizationId, HttpServletResponse response) {
        if (schoolId == null || useTypeId == null || StringUtils.isBlank(organizationId)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        List<Integer> organizationIdList = new ArrayList<>();
        for (String s : organizationId.split(",")) {
            organizationIdList.add(Integer.valueOf(s));
        }

        List<Map> userList = classroomBookUseTypeOrganizationAuditUserService.findListWithUserName(useTypeId, organizationIdList);

        if (userList == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该类型下没有绑定组织")));
        } else {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", userList)));
        }
    }

    /**
     * 新增或编辑教室预约审批流程（活动类别）
     *
     * @param id            使用类别ID
     * @param schoolId      学校ID
     * @param name          使用类别名称
     * @param organizations 选择的组织列表及设置的审批人员ID
     *                      <p>
     *                      样例：[{"organizationId":1,"auditUser":"1,2,3"},{"organizationId":2,"auditUser":"5,9,13"}]
     * @param response
     */
    @RequestMapping("app_addOrUpdateClassroomBookUseType")
    public void app_addOrUpdateClassroomBookUseType(Integer id, Integer schoolId, String name,
                                                    @ModelAttribute("user_id") Integer userId,
                                                    String organizations, HttpServletResponse response) {
        if (schoolId == null || name == null || organizations == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        List<Map> auditUserMapList = new ArrayList<>();
        try {
            List auditUserList = new ObjectMapper().readValue(organizations, List.class);
            for (Object o : auditUserList) {
                Map map = (Map) o;
                if (!map.containsKey("organizationId") || !map.containsKey("auditUser")) {
                    continue;
                }
                map.put("organizationId", map.get("organizationId").toString());
                map.put("auditUser", map.get("auditUser").toString());
                auditUserMapList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("解析教室预约审批人员列表错误：{}", e.getMessage());
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "审批人员列表参数错误")));
            return;
        }

        ClassroomBookUseType classroomBookUseType = new ClassroomBookUseType();
        classroomBookUseType.setId(id);
        classroomBookUseType.setSchoolId(schoolId);
        classroomBookUseType.setName(name);
        classroomBookUseType.setCreateBy(userId);

        Result result = classroomBookUseTypeService.addOrUpdate(classroomBookUseType, false, auditUserMapList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 审批流程列表
     *
     * @param schoolId 学校ID
     * @param response
     */
    @RequestMapping("app_findClassroomBookUseTypeList")
    public void app_findClassroomBookUseTypeList(Integer schoolId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        ClassroomBookUseTypeCriteria example = new ClassroomBookUseTypeCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        List<ClassroomBookUseType> list = classroomBookUseTypeService.findList(example);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 删除教室预约审批流程（活动类型）
     *
     * @param useTypeId 活动类型ID
     * @param response
     */
    @RequestMapping("app_deleteClassroomBookUseType")
    public void app_deleteClassroomBookUseType(Integer useTypeId, HttpServletResponse response) {
        if (useTypeId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        Result result = classroomBookUseTypeService.delete(useTypeId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 根据组织ID查询审批流程列表
     *
     * @param organizationId 组织ID
     * @param response
     */
    @RequestMapping("app_findClassroomBookUseTypeListByOrganizationId")
    public void app_findClassroomBookUseTypeListByOrganizationId(Integer organizationId, HttpServletResponse response) {
        if (organizationId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数")));
            return;
        }

        List<ClassroomBookUseType> list = classroomBookUseTypeService.findList(organizationId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 上课时间计划列表
     *
     * @param schoolId
     * @param response
     */
    @RequestMapping("app_addTeachingCourseTimePlanList")
    public void app_addTeachingCourseTimePlanList(Integer schoolId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.setOrderByClause("start_time asc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        List<Map> list = teachingWeekService.findTeachingCourseTimePlanMapList(example, "id", "name", "startTime", "periodType");

        List<Map> dataList = new ArrayList<>();
        //按时间段分类包装
        for (int i = 1; i < 5; i++) {
            Map data = new HashMap();
            List<Map> itemList = new ArrayList<>();
            data.put("itemList", itemList);
            switch (i) {
                case 1:
                    data.put("name", "上午");
                    break;
                case 2:
                    data.put("name", "中午");
                    break;
                case 3:
                    data.put("name", "下午");
                    break;
                case 4:
                    data.put("name", "晚上");
                    break;
            }

            Iterator<Map> iterator = list.iterator();
            while (iterator.hasNext()) {
                Map m = iterator.next();
                if (m.get("periodType").toString().equals(i + "")) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(data.get("name").toString())
                            .append(m.get("name").toString(), 0, 3);
                    m.put("periodType", sb.toString());
                    itemList.add(m);
                    iterator.remove();
                }
            }
            dataList.add(data);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", dataList)));
    }


    /**
     * 教室预约审批操作
     *
     * @param userId          用户ID
     * @param classroomBookId 教室预约ID
     * @param isPass          是否通过（1是、2否）,不传默认为1
     * @param remark          退回理由
     * @param response
     */
    @RequestMapping("app_updateClassroomBookAudit")
    public void app_updateClassroomBookAudit(@ModelAttribute("user_id") Integer userId,
                                             Integer classroomBookId, @RequestParam(defaultValue = "1", required = false) Short isPass, String remark, HttpServletResponse response) {
        if (userId == null || classroomBookId == null || isPass == null || (isPass.intValue() == 2 && remark == null)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        User user = userService.getById(userId);

        //  教务人员审核时，需要通知教务秘书，校验是否拥有审核权限
        boolean toTeachingSecretary = false;
        AuditUserAuth auditUserAuth = auditUserAuthService.find(user.getSchoolId(), userId);
        boolean hasPermission = auditRolePermissionService.findPermissionByCodeAndRoleId(auditUserAuth.getRoleId(), "classroomBook/audit");
        if (hasPermission) {
            toTeachingSecretary = true;
        } else {
            //不是教务处人员但在审批队列中
            ClassroomBook classroomBook = classroomBookService.findById(classroomBookId);
            ClassroomBookUseTypeOrganizationAuditUser auditUser = classroomBookUseTypeOrganizationAuditUserService.find(classroomBook.getUseTypeId(), classroomBook.getOrganizationId(), (short) 2);
            List<String> auditUserIdList = Arrays.asList(auditUser.getAuditUser().split(","));
            if (!auditUserIdList.contains(String.valueOf(userId))) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "没有审核权限")));
                return;
            }
        }

        ClassroomBookAuditCriteria example = new ClassroomBookAuditCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId)
                .andBookIdEqualTo(classroomBookId);
        List<ClassroomBookAudit> classroomBookAuditList = classroomBookAuditService.findList(example);
        if (classroomBookAuditList.isEmpty()) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该审批记录不存在")));
            return;
        }
        ClassroomBookAudit classroomBookAudit = classroomBookAuditList.get(0);

        Map classroomBookMap = classroomBookService.find(user.getSchoolId(), classroomBookId);
        Integer useTypeId = (Integer) classroomBookMap.get("useTypeId");
        Integer organizationId = (Integer) classroomBookMap.get("organizationId");
        ClassroomBookUseTypeOrganizationAuditUser auditUser = classroomBookUseTypeOrganizationAuditUserService.find(useTypeId, organizationId, (short) 2);
        List<String> users = Arrays.asList(auditUser.getAuditUser().split(","));
        Integer nextAuditUserId = null;
        int index = users.indexOf(String.valueOf(userId));
        if (index != -1 && index != users.size() - 1) {
            //当前审核人员在教务设置的审批人员列表中，且不是最后一个，拿到下一个审核人
            nextAuditUserId = Integer.valueOf(users.get(index + 1));
        }

        Result result = classroomBookAuditService.updateAudit(classroomBookAudit.getId(), userId, nextAuditUserId, toTeachingSecretary, isPass, remark);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 通知收到确认
     *
     * @param id       审批通知ID
     * @param response
     */
    @RequestMapping("app_confirmClassroomBookAuditNotice")
    public void app_confirmClassroomBookAuditNotice(Integer id, @ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Result result = classroomBookAuditService.confirmClassroomBookAuditNotice(id, userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 教室预约页面，根据用户获取用户的学院信息，姓名和学号信息
     *
     * @param userId
     * @param response
     */
    @RequestMapping("app_findUserInfoForClassroomBook")
    public void app_findUserInfoForClassroomBook(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        if (userId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        User user = userService.getById(userId);

        ClassroomBookUseTypeOrganizationCriteria classroomBookUseTypeOrganizationEx = new ClassroomBookUseTypeOrganizationCriteria();
        classroomBookUseTypeOrganizationEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andInstituteIdEqualTo(user.getInstituteId());
        List<ClassroomBookUseTypeOrganization> classroomBookUseTypeOrganizationList = classroomBookUseTypeOrganizationService.findList(classroomBookUseTypeOrganizationEx);
        ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = classroomBookUseTypeOrganizationList.get(0);

        List<ClassroomBookUseType> classroomBookUseTypeList = classroomBookUseTypeService.findList(classroomBookUseTypeOrganization.getId());

        Map data = new HashMap();
        data.put("userName", user.getName());
        data.put("stuNumber", user.getStuNumber());
        data.put("phone", user.getPhone());
        data.put("organizationId", classroomBookUseTypeOrganization.getId());
        data.put("organizationName", classroomBookUseTypeOrganization.getName());
        data.put("classroomBookUseTypeList", classroomBookUseTypeList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", data)));
    }

}
