package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Api(value = "AppLeaveInfoController", description = "请假管理模块(移动端)")
@Controller
public class AppLeaveInfoController extends AuthenticatorController {

    @Autowired
    private LeaveInfoService leaveInfoService;
    @Autowired
    private LeaveAuditUserService leaveAuditUserService;
    @Autowired
    private LeaveTypeService leaveTypeService;
    @Autowired
    private UserService userService;
    @Autowired
    private DormBuildingService dormBuildingService;
    @Autowired
    private LeaveAuditUserAuthService leaveAuditUserAuthService;
    @Autowired
    private LeaveAuditUserRoleService leaveAuditUserRoleService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private ClassroomBookAuditService classroomBookAuditService;
    @Autowired
    private ExamArrangeAuditService examArrangeAuditService;

    /**
     * 添加审批人员时，审批人员列表
     *
     * @param userId   用户ID
     * @param schoolId 学校ID
     * @param name     用户姓名关键字
     * @param response
     */
    @ApiOperation(value = "添加审批人员时，审批人员列表", notes = "添加审批人员时，审批人员列表")
    @RequestMapping("app_findUserListByAuditUserRoleId")
    public void app_findUserListByAuditUserRoleId(@ModelAttribute("user_id") Integer userId, Integer schoolId,
                                                  @RequestParam(required = false) String name, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        List<Map<String, Object>> userList = userService.findUserTeacherLists(schoolId, name);

        //移除未通过认证的教职工
        Iterator<Map<String, Object>> iterator = userList.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> user = iterator.next();
            if (user.get("isAuth") != null && Integer.valueOf(user.get("isAuth").toString()).intValue() == 2) {
                iterator.remove();
            }
        }
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", userList));
    }

    /**
     * 教导员、教辅人员、宿管人员身份认证类型列表
     *
     * @param userId   用户ID
     * @param pid      父级ID(一级类型pid=0，二级以下类型的pid为上级类型的ID)
     * @param response
     */
    @ApiOperation(value = "教导员、教辅人员、宿管人员身份认证类型列表", notes = "教导员、教辅人员、宿管人员身份认证类型列表")
    @RequestMapping("app_findLeaveAuditUserRoleList")
    public void app_findLeaveAuditUserRoleList(@ModelAttribute("user_id") Integer userId,
                                               @RequestParam(defaultValue = "0") Integer pid, HttpServletResponse response) {
        User user = userService.getById(userId);
        if (pid == 0) {
            pid = null;
        }
        List<Map<String, Object>> list = leaveAuditUserRoleService.findMapList(user.getSchoolId(), pid);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 教导员、教辅人员、宿管人员身份认证
     *
     * @param userId      用户ID
     * @param instituteId 学院ID
     * @param gradeId     年级ID
     * @param buildingId  宿舍楼ID
     * @param roleId      身份认证类型ID
     * @param reOperate   是否是重新认证，默认为false
     * @param reOperate2  是否是重新认证，默认为 0
     * @param response
     */
    @ApiOperation(value = "教导员、教辅人员、宿管人员身份认证", notes = "教导员、教辅人员、宿管人员身份认证")
    @RequestMapping("app_addLeaveAuditUserAuth")
    public void app_addLeaveAuditUserAuth(@ModelAttribute("user_id") Integer userId,
                                          @RequestParam(required = false) Integer instituteId,
                                          @RequestParam(required = false) Integer gradeId,
                                          @RequestParam(required = false) Integer buildingId,
                                          Integer roleId,
                                          @RequestParam(defaultValue = "false") Boolean reOperate,
                                          @RequestParam(defaultValue = "0") Integer reOperate2,
                                          HttpServletResponse response) {
        if (roleId == null || (instituteId == null && gradeId == null && buildingId == null)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        User user = userService.getById(userId);
        if (user.getIsTeacher() == 2) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "学生不能进行请假审批的身份认证", null)));
            return;
        }

        LeaveAuditUserAuth leaveAuditUserAuth = new LeaveAuditUserAuth();
        leaveAuditUserAuth.setSchoolId(user.getSchoolId());
        leaveAuditUserAuth.setInstituteId(instituteId);
        leaveAuditUserAuth.setGradeId(gradeId);
        leaveAuditUserAuth.setBuildingId(buildingId);
        leaveAuditUserAuth.setRoleId(roleId);
        leaveAuditUserAuth.setUserId(userId);
        Result result = leaveAuditUserAuthService.addLeaveAuditUserAuth(leaveAuditUserAuth, reOperate, reOperate2);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 获取宿舍楼列表
     *
     * @param userId   用户ID
     * @param response
     */
    @ApiOperation(value = "获取宿舍楼列表", notes = "获取宿舍楼列表")
    @RequestMapping("app_findDormBuildingList")
    public void app_findDormBuildingList(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        User user = userService.getById(userId);
        DormBuildingCriteria example = new DormBuildingCriteria();
        example.createCriteria().andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(user.getSchoolId());
        List<Map<String, Object>> dormBuildings = dormBuildingService.findMapList(example);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", dormBuildings)));
    }

    /**
     * 获取请假类型列表(按请假天数和性质来分)
     *
     * @param userId   用户ID
     * @param response
     */
    @ApiOperation(value = "获取请假类型列表(按请假天数和性质来分)", notes = "获取请假类型列表(按请假天数和性质来分)")
    @RequestMapping("app_findLeaveTypeList")
    public void app_findLeaveTypeList(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        User user = userService.getById(userId);
        List<LeaveType> list = leaveTypeService.findList(user.getSchoolId());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 请假
     *
     * @param userId            请假人ID
     * @param leaveType         请假类型（按天数来分）
     * @param reasonType        请假事由
     * @param reason            请假事由
     * @param goTo              去向
     * @param startDate         请假开始日期
     * @param endDate           请假结束日期
     * @param auditUser         审批人ID字符串，逗号分隔，按审批顺序排
     * @param linkMan           请假人自己的手机号
     * @param linkTel           紧急联系人电话
     * @param attachment        请假材料附件(照片、证明)
     * @param apartment         宿舍编号(外出或回家需要传此参数)
     * @param apartmentBuilding 宿舍楼编号(外出或回家需要传此参数)
     * @param response
     */
    @ApiOperation(value = "请假", notes = "请假")
    @RequestMapping("app_addLeaveInfo")
    public void app_addLeaveInfo(@ModelAttribute("user_id") Integer userId, @ModelAttribute("version") String version, HttpServletResponse response) {
        Map<String, Object> params = ServletHolderFilter.getContext().getParamMap();
        Object leaveTypeObj = params.get("leaveType");
        Object reasonTypeObj = params.get("reasonType");
        Object reasonObj = params.get("reason");
        Object goToObj = params.get("goTo");
        Object startDateObj = params.get("startDate");
        Object endDateObj = params.get("endDate");
        /*Object auditUserObj = params.get("auditUser");*/
        Object linkManObj = params.get("linkMan");
        Object linkTelObj = params.get("linkTel");
        Object apartmentObj = params.get("apartment");
        Object apartmentBuildingObj = params.get("apartmentBuilding");
        Object organizationObj = params.get("organization");


        if (leaveTypeObj == null || reasonTypeObj == null || reasonObj == null || startDateObj == null || endDateObj == null
                || linkManObj == null || linkTelObj == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        if (goToObj != null && (apartmentObj == null || apartmentBuildingObj == null)) {
            // 外出或回家需要填写宿舍楼编号和宿舍编号
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        LeaveInfo leaveInfo = new LeaveInfo();
        leaveInfo.setUserId(userId);
        leaveInfo.setLeaveType(Integer.valueOf(leaveTypeObj.toString()));
        leaveInfo.setReasonType(Short.valueOf(reasonTypeObj.toString()));
        leaveInfo.setReason(reasonObj.toString());
        if (goToObj != null) {
            leaveInfo.setGoTo(Short.valueOf(goToObj.toString()));
        }

        Date start = null;
        Date end = null;
        String[] format = new String[]{"yyyy-MM-dd"};
        try {
            start = DateUtils.parseDate(startDateObj.toString(), format);
            end = DateUtils.parseDate(endDateObj.toString(), format);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        leaveInfo.setStartDate(start);
        leaveInfo.setEndDate(end);
        /*leaveInfo.setAuditUser(auditUserObj.toString());*/
        leaveInfo.setTel(linkManObj.toString());
        leaveInfo.setLinkTel(linkTelObj.toString());
        if (organizationObj != null) {
            leaveInfo.setOrganization(organizationObj.toString());
        }
        if (apartmentObj != null) {
            leaveInfo.setApartment(apartmentObj.toString());
        }
        if (apartmentBuildingObj != null) {
            leaveInfo.setApartmentBuilding(apartmentBuildingObj.toString());
        }

        List<DiskFileItem> attachmentList = (List<DiskFileItem>) params.get("attachment");
        if (CollectionUtils.isNotEmpty(attachmentList)) {
            String attachment = "";
            int size = attachmentList.size();
            for (int i = 0; i < size; i++) {
                DiskFileItem diskFileItem = attachmentList.get(i);
                byte[] fileContent = diskFileItem.get();
                String fileName = FileUtilsEx.getFileNameByPath(diskFileItem.getName());
                attachment += NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
                if (i != size - 1) {
                    attachment += ",";
                }
            }
            leaveInfo.setAttachment(attachment);
        }

        boolean insertAuditInfo = false;
        if (convertVersionToDouble(version) >= 1.4) {
            insertAuditInfo = true;
        }
        Result result = leaveInfoService.addLeaveInfo(leaveInfo, insertAuditInfo);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 请假单详情
     *
     * @param id       请假单ID
     * @param response
     */
    @ApiOperation(value = "请假单详情", notes = "请假单详情")
    @RequestMapping("app_findLeaveInfoDetail")
    public void app_findLeaveInfoDetail(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Result result = leaveInfoService.getLeaveInfo(id);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 取消请假
     *
     * @param id       请假单ID
     * @param userId   用户ID
     * @param response
     */
    @ApiOperation(value = "取消请假", notes = "取消请假")
    @RequestMapping("app_cancelLeaveInfo")
    public void app_cancelLeaveInfo(Integer id, @ModelAttribute("user_id") Integer userId,
                                    HttpServletResponse response) {
        if (id == null || userId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Result result = leaveInfoService.cancelLeaveInfo(id, userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 销假
     *
     * @param id       请假单ID
     * @param backDate 销假日期
     * @param response
     */
    @ApiOperation(value = "销假", notes = "销假")
    @RequestMapping("app_reportBackForLeaveInfo")
    public void app_reportBackForLeaveInfo(Integer id, Date backDate, HttpServletResponse response) {
        if (id == null || backDate == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Result result = leaveInfoService.reportBackForLeaveInfo(id, backDate);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx用户的请假列表
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @param userId   用户ID
     * @param response
     */
    @ApiOperation(value = "xx用户的请假列表", notes = "xx用户的请假列表")
    @RequestMapping("app_findLeaveInfoListByUserId")
    public void app_findLeaveInfoListByUserId(Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                              @ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        if (page == null || userId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        Result result = leaveInfoService.findLeaveInfoList(userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx用户的待审批列表
     *
     * @param page     页码
     * @param userId   用户ID
     * @param response
     */
    @ApiOperation(value = "xx用户的待审批列表", notes = "xx用户的待审批列表")
    @RequestMapping("app_findLeaveAuditListByUserId")
    public void app_findLeaveAuditListByUserId(Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                               @ModelAttribute("user_id") Integer userId,
                                               @ModelAttribute("version") String version, HttpServletResponse response) {
        if (page == null || userId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        User user = userService.getById(userId);

        List<Map<String, Object>> list;
        PageLimitHolderFilter.setContext(page, pageSize, null);
        if (convertVersionToDouble(version) >= 1.3 && convertVersionToDouble(version) < 1.4) {
            list = leaveInfoService.findLeaveAuditMapList(userId);
        } else {
            //1.4版本将审核表汇总到audit_info表中
            List<Map> auditInfoList = auditInfoService.findList(userId);
            list = new ArrayList<>();
            for (Map audit : auditInfoList) {
                //审核类型（1请假，2教室预约，3智能排考）
                Map<String, Object> map;
                if ("1".equals(audit.get("type").toString())) {
                    //请假审批
                    map = leaveInfoService.findLeaveAuditMapById(Integer.valueOf(audit.get("bizId").toString()), user.getIsTeacher());
                } else if ("2".equals(audit.get("type").toString())) {
                    //教室预约审批
                    map = classroomBookAuditService.findAudit(Integer.valueOf(audit.get("bizId").toString()));
                } else if ("3".equals(audit.get("type").toString())) {
                    //智能排考审批
                    map = examArrangeAuditService.findExamArrangeAuditMapById(Integer.valueOf(audit.get("bizId").toString()));
                } else {
                    //其他审批类型
                    map = null;
                }
                list.add(map);
            }
        }

        int notRead_num = 0;
        for (Map<String, Object> map : list) {
            //未处理的审批数量
            if (Integer.valueOf(String.valueOf(map.get("is_received"))).intValue() == 2) {
                notRead_num++;
            }
        }
        Map<String, Object> result = new HashedMap();
        result.put("notRead_num", notRead_num);
        result.put("time", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        result.put("notice_list", list);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 审批请假
     *
     * @param leaveId   请假审批ID
     * @param userId    审批人ID
     * @param isPass    是否通过(1是2否)
     * @param auditUser 审批人ID字符串，按照审批顺序排列，逗号分隔
     * @param remark    审批意见
     * @param response
     */
    @ApiOperation(value = "审批请假", notes = "审批请假")
    @RequestMapping("app_updateLeaveAudit")
    public void app_updateLeaveAudit(@ModelAttribute("user_id") Integer userId, @ModelAttribute("version") String
            version,
                                     HttpServletResponse response) {
        Map<String, Object> params = ServletHolderFilter.getContext().getParamMap();
        Object leaveIdObj = params.get("leaveId");
        Object isPassObj = params.get("isPass");
        /*Object auditUserObj = params.get("auditUser");*/
        Object remarkObj = params.get("remark");
        if (leaveIdObj == null || isPassObj == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        LeaveAudit leaveAudit = new LeaveAudit();
        leaveAudit.setId(Integer.valueOf(leaveIdObj.toString()));
        leaveAudit.setIsPass(Short.valueOf(isPassObj.toString()));
        leaveAudit.setUserId(userId);
        if (remarkObj != null) {
            leaveAudit.setRemark(remarkObj.toString());
        }

        boolean insertAuditInfo = false;
        if (convertVersionToDouble(version) >= 1.4) {
            insertAuditInfo = true;
        }
        Result result = leaveInfoService.updateLeaveAudit(leaveAudit, "", insertAuditInfo);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx用户收到的请假通知列表
     *
     * @param page     页码
     * @param userId   用户ID
     * @param response
     */
    @ApiOperation(value = "xx用户收到的请假通知列表", notes = "xx用户收到的请假通知列表")
    @RequestMapping("app_findLeaveNoticeListByUserId")
    public void app_findLeaveNoticeListByUserId(Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                                                @ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        if (page == null || userId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = leaveInfoService.findLeaveNoticeList(userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 通知已读
     *
     * @param noticeId 请假审批通知ID字符串，逗号分隔
     * @param response
     */
    @ApiOperation(value = "通知已读", notes = "通知已读")
    @RequestMapping("app_readNotice")
    public void app_readNotice(String noticeId, @ModelAttribute("user_id") Integer userId,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(noticeId)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        List<String> idList = Arrays.asList(noticeId.split(","));

        for (String id : idList) {
            leaveInfoService.readNotice(Integer.valueOf(id), userId);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 通知收到确认
     *
     * @param id       请假审批通知ID
     * @param response
     */
    @ApiOperation(value = "通知收到确认", notes = "通知收到确认")
    @RequestMapping("app_confirmNotice")
    public void app_confirmNotice(Integer id, @ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Result result = leaveInfoService.confirmNotice(id, userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 删除通知
     *
     * @param noticeId
     * @param userId
     * @param response
     */
    @ApiOperation(value = "删除通知", notes = "删除通知")
    @RequestMapping("app_deleteNotice")
    public void app_deleteNotice(String noticeId, @ModelAttribute("user_id") Integer userId,
                                 HttpServletResponse response) {
        if (StringUtils.isBlank(noticeId)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        List<String> idList = Arrays.asList(noticeId.split(","));

        for (String id : idList) {
            leaveInfoService.deleteNotice(Integer.valueOf(id), userId);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * xx用户设置审批人队列
     *
     * @param id
     * @param auditUser 后续的审批人ID字符串
     * @param userId    当前用户ID
     * @param type      请假类型(1-3天以下，2-3~7天，3-7天以上，4-大四实习请假，5-参加重大赛事请假)
     * @param response
     */
    @ApiOperation(value = "xx用户设置审批人队列(V1.0)", notes = "xx用户设置审批人队列(V1.0)")
    @RequestMapping("app_addOrUpdateLeaveAuditUser_v1")
    public void app_addOrUpdateLeaveAuditUser(Integer id, String auditUser, @ModelAttribute("user_id") Integer
            userId,
                                              Short type, HttpServletResponse response) {
        if (StringUtils.isBlank(auditUser) || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Result result = leaveAuditUserService.addOrUpdateLeaveAuditUser(id, userId, type, auditUser);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx用户设置审批人队列
     *
     * @param userId   当前用户ID
     * @param data     审批人队列数据包(key为请假类型ID,value为审批人ID字符串):{"1":"1","2":"1,2,3,4","3":"1,2,5,6,9"}
     * @param response
     */
    @ApiOperation(value = "xx用户设置审批人队列(V2.0)", notes = "xx用户设置审批人队列(V2.0)")
    @RequestMapping("app_addOrUpdateLeaveAuditUser_v2")
    public void app_addOrUpdateLeaveAuditUser(@ModelAttribute("user_id") Integer userId, String
            data, HttpServletResponse response) {
        if (StringUtils.isBlank(data)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Map<String, String> dataMap = null;
        try {
            dataMap = new ObjectMapper().readValue(data, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "失败", null)));
        }

        Result result = leaveAuditUserService.addOrUpdateLeaveAuditUser(userId, dataMap);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx用户删除审批人队列
     *
     * @param id
     * @param userId
     * @param response
     */
    @ApiOperation(value = "xx用户删除审批人队列", notes = "xx用户删除审批人队列")
    @RequestMapping("app_deleteLeaveAuditUser")
    public void app_deleteLeaveAuditUser(Integer id, @ModelAttribute("user_id") Integer userId, HttpServletResponse
            response) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Result result = leaveAuditUserService.deleteLeaveAuditUser(id, userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx用户设置的审批人队列列表
     *
     * @param response
     * @param userId   用户ID
     * @param type     请假类型(1-3天以下，2-3~7天，3-7天以上，4-大四实习请假，5-参加重大赛事请假)
     */
    @ApiOperation(value = "xx用户删除审批人队列(V1.0)", notes = "xx用户删除审批人队列(V1.0)")
    @RequestMapping("app_findLeaveAuditUserList_v1")
    public void app_findLeaveAuditUserList(HttpServletResponse response, @ModelAttribute("user_id") Integer userId,
                                           @RequestParam(required = false, defaultValue = "0") Short type) {
        List<Map<String, Object>> list = leaveAuditUserService.getLeaveAuditUserMapList(userId, type);
        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * xx用户设置的审批人队列列表
     *
     * @param response
     * @param userId   用户ID
     */
    @ApiOperation(value = "xx用户删除审批人队列(V2.0)", notes = "xx用户删除审批人队列(V2.0)")
    @RequestMapping("app_findLeaveAuditUserList_v2")
    public void app_findLeaveAuditUserList(HttpServletResponse response, @ModelAttribute("user_id") Integer userId) {
        List<Map<String, Object>> list = leaveAuditUserService.getLeaveAuditUserMapList(userId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }
}
