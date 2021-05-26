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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 差旅费用
 * @Author MCX
 * @Version 1.0
 **/
@Controller
public class AppTravelExpenseController extends AuthenticatorController{

    @Autowired
    private BiuTravelExpenseNoticeService biuTravelExpenseNoticeService;
    @Autowired
    private BiuTravelExpenseAuditUserService biuTravelExpenseAuditUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private BiuTravelExpenseInfoService biuTravelExpenseInfoService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private DictInfoService dictInfoService;
    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;

    /**
     * 审核人列表
     * @param userId
     * @param planDays
     * @param costMoney
     * @param response
     */
    @RequestMapping("app_travelExpenseAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id")Integer userId, Integer planDays, BigDecimal costMoney, HttpServletResponse response) {
        Assert.notNull(planDays, "出差天数不能为空");
        Assert.notNull(costMoney, "合计金额不能为空");
        List<HashMap> auditUserList = biuTravelExpenseAuditUserService.getAuditUserList(userId, planDays,costMoney);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 申请人信息
     * @param userId
     * @param response
     */
    @RequestMapping("app_userInfo")
    public void getPersonalInfo(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        appTeacherLeaveInfoController.teacherPersonalInfo(userId, response);
    }

    /**
     * 付款方式类型
     * @param response
     */
    @RequestMapping("app_travelExpenseTypeList")
    public void travelExpenseTypeList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.PAYMENT_TYPE.name());
        Assert.notNull(parent, "付款方式类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 出差方式类型
     * @param response
     */
    @RequestMapping("app_vehicleTypeList")
    public void vehicleTypeList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.VEHICLE_TYPE.name());
        Assert.notNull(parent, "出差方式类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 添加差旅申请
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addTravelExpenseInfo")
    public void addTravelExpenseInfo(@ModelAttribute("user_id") Integer userId, BiuTravelExpenseInfo req, HttpServletResponse response) {
        Assert.notNull(req.getStartDate(), "出发日期不能为空");
        Assert.notNull(req.getEndDate(), "结束日期不能为空");
        Assert.notNull(req.getPersons(), "人数不能为空");
        Assert.notNull(req.getVehicle(), "出差方式不能为空");
        Assert.notNull(req.getAddress(), "出差地点不能为空");
        for (BiuTravelExpenseDetail detail : req.getDetailList()) {
            Assert.notNull(detail.getCostTitle(), "费用名称不能为空");
            Assert.notNull(detail.getCostMoney(), "金额不能为空");
        }
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

        biuTravelExpenseInfoService.add(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 取消差旅申请
     * @param userId
     * @param expenseId
     * @param response
     */
    @RequestMapping("app_cancelTravelExpenseInfo")
    public void cancelTravelExpenseInfo(@ModelAttribute("user_id") Integer userId, Integer expenseId, HttpServletResponse response) {
        Assert.notNull(expenseId, "请选择要取消的差旅申请");

        BiuTravelExpenseInfo expenseInfo = biuTravelExpenseInfoService.selectByPrimaryKey(expenseId);
        if (expenseInfo == null || expenseInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该差旅申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != expenseInfo.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (expenseInfo.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该差旅申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.TRAVEL_EXPENSE.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该差旅申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        biuTravelExpenseInfoService.cancel(expenseInfo);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 我的申请列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myExpenseInfoList")
    public void myExpenseInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = biuTravelExpenseInfoService.myExpenseInfoList(userId, startDate, endDate, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 申请详情
     * @param userId
     * @param expenseId
     * @param response
     */
    @RequestMapping("app_expenseInfoDetail")
    public void detail(@ModelAttribute("user_id") Integer userId, Integer expenseId, HttpServletResponse response) {
        Assert.notNull(expenseId, "请选择要查看的差旅申请");

        BiuTravelExpenseInfo expenseInfo = biuTravelExpenseInfoService.selectByPrimaryKey(expenseId);
        if (expenseInfo == null || expenseInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该差旅申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 查询整体审核记录列表
        List<HashMap> auditInfoList = biuTravelExpenseAuditUserService.findMapList(expenseInfo);
        Map<String, Object> hashMap = BeanUtil.beanToMap(expenseInfo);

        User user = userService.getById(expenseInfo.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        hashMap.put("expenseTypeName", dictInfoService.selectById(expenseInfo.getPaymentType()).getName());

        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            biuTravelExpenseNoticeService.confirmNotice(expenseInfo.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 我的审核列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myAuditExpenseInfoList")
    public void myAuditExpenseInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = biuTravelExpenseInfoService.myAuditExpenseInfoList(userId, startDate, endDate, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 审核申请
     * @param userId
     * @param expenseId
     * @param status
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditTravelExpenseInfo")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer expenseId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(expenseId, "请选择要审核的差旅申请");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        BiuTravelExpenseInfo leaveInfo = biuTravelExpenseInfoService.selectByPrimaryKey(expenseId);
        if (leaveInfo == null || leaveInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该差旅申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(leaveInfo.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该差旅申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (leaveInfo.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        biuTravelExpenseInfoService.audit(leaveInfo, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
}
