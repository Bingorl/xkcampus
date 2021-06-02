package com.biu.wifi.campus.controller.app;


import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.constant.AuditBusinessType;
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

/**报修费用申请
 * @Author MCX
 * @Version 1.0
 **/
@Controller
@RequestMapping
public class AppRepairCostController extends AuthenticatorController{
    @Autowired
    private RepairCostAuditService repairCostAuditService;
    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;
    @Autowired
    private UserService userService;
    @Autowired
    private RepairCostAuditUserService repairCostAuditUserService;
    @Autowired
    private AppAssertsUseController appAssertsUseController;
    @Autowired
    private RepairCostInfoService repairCostInfoService;
    @Autowired
    private RepairCostNoticeService repairCostNoticeService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private InstituteService instituteService;
    /**
     * 获取申请人信息
     * @param userId
     * @param response
     */
    @RequestMapping("app_repairCostUserInfo")
    public void repairCostUserInfo(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        appTeacherLeaveInfoController.teacherPersonalInfo(userId,response);
    }
    /**
     * 申请人审批审核人员列表
     * @param userId
     * @param response
     * @return
     */
    @RequestMapping("app_repairCostAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        List<HashMap> auditUserList = repairCostAuditUserService.getAuditUserList(userId);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 资产列表
     * @param response
     */
    @RequestMapping("app_equipmentList")
    public void equipmentList(HttpServletResponse response) {
       appAssertsUseController.assetsList(response);
    }

    /**
     * 新增报修费用申请
     *
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addRepairCostInfo")
    public void addRepairCostInfo(@ModelAttribute("user_id") Integer userId, RepairCostInfo req, HttpServletResponse response) {
        Assert.notNull(req.getReason(), "更换原因不能为空");
        Assert.notNull(req.getStartDate(), "申请日期不能为空");
        Assert.notNull(req.getEquipmentId(), "更换设备不能为空");
        Assert.notNull(req.getCostMoney(), "申请金额不能为空");


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

        repairCostInfoService.addRepairCostInfo(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 取消报修费用申请
     *
     * @param userId
     * @param repairId
     * @param response
     */
    @RequestMapping("app_cancelRepairCostInfo")
    public void cancelRepairCostInfo(@ModelAttribute("user_id") Integer userId, Integer repairId, HttpServletResponse response) {
        Assert.notNull(repairId, "请选择要取消的报修申请");

        RepairCostInfo info = repairCostInfoService.selectByPrimaryKey(repairId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该报修申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != info.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (info.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该报修申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.OFFICIAL_WEBSITE.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该报修申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        repairCostInfoService.cancel(info);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 我的报修费用申请列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态
     * @param response
     */
    @RequestMapping("app_myRepairCostInfoList")
    public void myRepairCostInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize,Short status,String startDate, String endDate,HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> Infos = repairCostInfoService.myRepairCostInfoList(userId,  startDate.equals("-1")?null:startDate, endDate.equals("-1")?null:endDate, status==-1?null:status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", Infos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }
//
//
    /**
     * 查看报修费用详情
     *
     * @param userId
     * @param repairId
     * @param response
     */
    @RequestMapping("app_repairCostInfoDetail")
    public void repairCostInfoDetail(@ModelAttribute("user_id") Integer userId, Integer repairId, HttpServletResponse response) {
        Assert.notNull(repairId, "请选择要查看的报修费用申请");

        RepairCostInfo info = repairCostInfoService.selectByPrimaryKey(repairId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "报修费用申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        // 查询整体审核记录列表
        List<HashMap> auditInfoList = repairCostAuditUserService.findMapList(info);
        Map<String, Object> hashMap = BeanUtil.beanToMap(info);

        User user = userService.getById(info.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            repairCostNoticeService.confirmNotice(info.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }
//
//
    /**
     * 我的报修费用申请审批列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态1审核中2通过,3驳回,5已完成(已处理)
     * @param response
     */
    @RequestMapping("app_myAuditRepairCostInfoList")
    public void myAuditRepairCostInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status,String startDate, String endDate,HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> approveInfos = repairCostInfoService.myAuditRepairCostInfoList(userId,  startDate.equals("-1")?null:startDate, endDate.equals("-1")?null:endDate, status==-1?null:status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", approveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }



    /**
     *报修费用申请审批
     *
     * @param userId
     * @param repairId
     * @param status   审核状态（2通过,3驳回）
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditRepairCostInfo")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer repairId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(repairId, "请选择要审核的报修费用申请申请");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        RepairCostInfo info = repairCostInfoService.selectByPrimaryKey(repairId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该报修费用申请申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(info.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该报修费用申请申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (info.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        repairCostInfoService.audit(info, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

}
