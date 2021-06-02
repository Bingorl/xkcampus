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
 * @Author MCX
 * @Version 1.0
 **/
@Controller
public class AppSuppliesPurchaseController extends AuthenticatorController{
    @Autowired
    private SuppliesPurchaseInfoService suppliesPurchaseInfoService;
    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;
    @Autowired
    private DictInfoService dictInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private SuppliesPurchaseAuditUserService suppliesPurchaseAuditUserService;
    @Autowired
    private SuppliesPurchaseNoticeService suppliesPurchaseNoticeService;

    /**
     * 查询物资费用对应的审批人员
     * @param userId
     * @param money
     * @param response
     */
    @RequestMapping("app_getSuppliesAuditUser")
    public void getSuppliesAuditUser(@ModelAttribute("user_id")Integer userId, BigDecimal money, HttpServletResponse response){
        Assert.notNull(money, "采购费用不能为空");
        List<HashMap> auditUserList = suppliesPurchaseInfoService.getAuditUserList(userId, money);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 采购部门
     * @param response
     */
    @RequestMapping("app_instituteList")
    public void instituteList(HttpServletResponse response) {
        List<Institute> institute= instituteService.findAll();
        Assert.notNull(institute, "部门未设置");
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", institute));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 采购类型
     * @param response
     */
    @RequestMapping("app_PurchaseTypeList")
    public void purchaseTypeList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.PURCHASE_TYPE.name());
        Assert.notNull(parent, "采购类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 申请人信息
     * @param userId
     * @param response
     */
    @RequestMapping("app_personalInfo")
    public void getPersonalInfo(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        appTeacherLeaveInfoController.teacherPersonalInfo(userId, response);
    }
    /**
     * 新增采购申请
     *
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addSuppliesPurchaseInfo")
    public void addSuppliesPurchaseInfo(@ModelAttribute("user_id") Integer userId, SuppliesPurchaseInfo req, HttpServletResponse response) {
        Assert.notNull(req.getPurchaseType(), "采购类型不能为空");
        Assert.notNull(req.getInstitute(), "采购部门不能为空");
        Assert.notNull(req.getReason(), "采购原因不能为空");
        Assert.notNull(req.getMoney(), "采购费用不能为空");
        Assert.notNull(req.getStartDate(), "开始日期不能为空");
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

        suppliesPurchaseInfoService.add(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 取消采购申请
     *
     * @param userId
     * @param purchaseId
     * @param response
     */
    @RequestMapping("app_cancelSuppliesPurchaseInfo")
    public void cancelSuppliesPurchaseInfo(@ModelAttribute("user_id") Integer userId, Integer purchaseId, HttpServletResponse response) {
        Assert.notNull(purchaseId, "请选择要取消的采购申请");

        SuppliesPurchaseInfo leaveInfo = suppliesPurchaseInfoService.selectByPrimaryKey(purchaseId);
        if (leaveInfo == null || leaveInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该采购申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != leaveInfo.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (leaveInfo.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该采购申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.SUPPLIES_PURCHASE.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该采购申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        suppliesPurchaseInfoService.cancel(leaveInfo);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 我的采购申请列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_mySuppliesPurchaseInfoList")
    public void mySuppliesPurchaseInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> suppliesPurchaseInfos = suppliesPurchaseInfoService.mySuppliesPurchaseInfoList(userId,  startDate.equals("-1")?null:startDate, endDate.equals("-1")?null:endDate, status==-1?null:status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", suppliesPurchaseInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 查看采购申请详情
     *
     * @param userId
     * @param purchaseId
     * @param response
     */
    @RequestMapping("app_SuppliesPurchaseInfoDetail")
    public void SuppliesPurchaseInfoDetail(@ModelAttribute("user_id") Integer userId, Integer purchaseId, HttpServletResponse response) {
        Assert.notNull(purchaseId, "请选择要查看的请假申请");

        SuppliesPurchaseInfo purchaseInfo = suppliesPurchaseInfoService.selectByPrimaryKey(purchaseId);
        if (purchaseInfo == null || purchaseInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该采购申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 查询整体审核记录列表
        List<HashMap> auditInfoList = suppliesPurchaseAuditUserService.findMapList(purchaseInfo);
        Map<String, Object> hashMap = BeanUtil.beanToMap(purchaseInfo);

        User user = userService.getById(purchaseInfo.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        hashMap.put("leaveTypeName", dictInfoService.selectById(purchaseInfo.getPurchaseType()).getName());

        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            suppliesPurchaseNoticeService.confirmNotice(purchaseInfo.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 我的采购审批列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态1审核中2通过,3驳回,5已完成(已处理)
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myAuditSuppliesPurchaseInfoList")
    public void myAuditSuppliesPurchaseInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> purchaseInfos = suppliesPurchaseInfoService.myAuditpurchaseInfoList(userId,  startDate.equals("-1")?null:startDate, endDate.equals("-1")?null:endDate, status==-1?null:status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", purchaseInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 采购审批
     *
     * @param userId
     * @param purchaseId
     * @param status   审核状态（2通过,3驳回）
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditSuppliesPurchaseInfo")
    public void auditSuppliesPurchaseInfo(@ModelAttribute("user_id") Integer userId, Integer purchaseId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(purchaseId, "请选择要审核的采购申请");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        SuppliesPurchaseInfo purchaseInfo = suppliesPurchaseInfoService.selectByPrimaryKey(purchaseId);
        if (purchaseInfo == null || purchaseInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该采购申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(purchaseInfo.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该采购申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (purchaseInfo.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        suppliesPurchaseInfoService.audit(purchaseInfo, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

}
