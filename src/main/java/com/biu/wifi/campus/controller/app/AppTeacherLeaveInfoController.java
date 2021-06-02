package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.BaseDictType;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教师请假管理模块
 *
 * @author 张彬.
 * @date 2021/4/3 15:36.
 */
@Controller
public class AppTeacherLeaveInfoController extends AuthenticatorController {

    @Autowired
    private TeacherLeaveInfoService teacherLeaveInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private DictInfoService dictInfoService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private TeacherLeaveAuditUserService teacherLeaveAuditUserService;
    @Autowired
    private TeacherLeaveNoticeService teacherLeaveNoticeService;

    /**
     * 根据登录用户id获取请假审核人员列表
     *
     * @param userId
     * @param planDays
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/15 16:05.
     */
    @RequestMapping("app_teacherLeaveInfoAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id") Integer userId, Integer planDays, HttpServletResponse response) {
        Assert.notNull(planDays, "请假天数不能为空");
        List<HashMap> auditUserList = teacherLeaveInfoService.getAuditUserList(userId, planDays);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 教师请假类型列表
     *
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/7 0:24.
     */
    @RequestMapping("app_teacherLeaveInfoTypeList")
    public void teacherLeaveInfoTypeList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.TEACHER_LEAVE_TYPE.name());
        Assert.notNull(parent, "教师请假类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        // 1年假2事假3病假4调休5产假6陪产假7婚假8产检假9丧假10哺乳假
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 根据登录用户id获取教师个人基础信息
     *
     * @param userId
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/8 10:40.
     */
    @RequestMapping("app_teacherPersonalInfo")
    public void teacherPersonalInfo(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        User user = userService.getById(userId);
        String json;
        if (user == null || user.getIsDelete().intValue() == 1) {
            json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "当前登录用户不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        Map<String, Object> hashMap = BeanUtil.beanToMap(user);
        Institute institute = instituteService.getById(user.getInstituteId());
        if (institute == null || institute.getIsDelete().intValue() == 1) {
            json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "当前登录用户所在的部门不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        hashMap.put("instituteName", institute.getName());
        json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 新增请假申请
     *
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addTeacherLeaveInfo")
    public void addTeacherLeaveInfo(@ModelAttribute("user_id") Integer userId, TeacherLeaveInfo req, HttpServletResponse response) {
        Assert.notNull(req.getLeaveType(), "请假类型不能为空");
        Assert.notNull(req.getReason(), "请假原因不能为空");
        Assert.notNull(req.getStartDate(), "开始日期不能为空");
        Assert.notNull(req.getEndDate(), "结束日期不能为空");
        Assert.notNull(req.getEndDate(), "结束日期不能为空");

        if (req.getStartDate().compareTo(req.getEndDate()) > 0) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "结束日期不得早于开始日期", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 请假时的姓名、工号、手机号和部门是手填还是自动匹配显示
        User user = userService.getById(userId);
        if (user == null || user.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "当前登录用户不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        req.setApplyUserId(userId);
        req.setApplyUserName(user.getName());
        req.setApplyUserNo(user.getStuNumber());
        req.setApplyUserTel(user.getPhone());
        req.setApplyUserDeptId(user.getInstituteId().toString());

        teacherLeaveInfoService.add(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 取消请假申请
     *
     * @param userId
     * @param leaveId
     * @param response
     */
    @RequestMapping("app_cancelTeacherLeaveInfo")
    public void cancelTeacherLeaveInfo(@ModelAttribute("user_id") Integer userId, Integer leaveId, HttpServletResponse response) {
        Assert.notNull(leaveId, "请选择要取消的请假申请");

        TeacherLeaveInfo leaveInfo = teacherLeaveInfoService.selectByPrimaryKey(leaveId);
        if (leaveInfo == null || leaveInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != leaveInfo.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (leaveInfo.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.TEACHER_LEAVE.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        teacherLeaveInfoService.cancel(leaveInfo);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 我的请假申请列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myLeaveInfoList")
    public void myLeaveInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = teacherLeaveInfoService.myLeaveInfoList(userId,  startDate.equals("-1")?null:startDate, endDate.equals("-1")?null:endDate, status==-1?null:status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 查看请假单详情
     *
     * @param userId
     * @param leaveId
     * @param response
     */
    @RequestMapping("app_teacherLeaveInfoDetail")
    public void detail(@ModelAttribute("user_id") Integer userId, Integer leaveId, HttpServletResponse response) {
        Assert.notNull(leaveId, "请选择要查看的请假申请");

        TeacherLeaveInfo teacherLeaveInfo = teacherLeaveInfoService.selectByPrimaryKey(leaveId);
        if (teacherLeaveInfo == null || teacherLeaveInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 查询整体审核记录列表
        List<HashMap> auditInfoList = teacherLeaveAuditUserService.findMapList(teacherLeaveInfo);
        Map<String, Object> hashMap = BeanUtil.beanToMap(teacherLeaveInfo);

        User user = userService.getById(teacherLeaveInfo.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        hashMap.put("leaveTypeName", dictInfoService.selectById(teacherLeaveInfo.getLeaveType()).getName());

        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            teacherLeaveNoticeService.confirmNotice(teacherLeaveInfo.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 我的请假审批列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态1审核中2通过,3驳回,5已完成(已处理)
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myAuditLeaveInfoList")
    public void myAuditLeaveInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = teacherLeaveInfoService.myAuditLeaveInfoList(userId,  startDate.equals("-1")?null:startDate, endDate.equals("-1")?null:endDate, status==-1?null:status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 请假审批
     *
     * @param userId
     * @param leaveId
     * @param status   审核状态（2通过,3驳回）
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditTeacherLeaveInfo")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer leaveId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(leaveId, "请选择要审核的请假申请");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        TeacherLeaveInfo leaveInfo = teacherLeaveInfoService.selectByPrimaryKey(leaveId);
        if (leaveInfo == null || leaveInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(leaveInfo.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (leaveInfo.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        teacherLeaveInfoService.audit(leaveInfo, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
}
