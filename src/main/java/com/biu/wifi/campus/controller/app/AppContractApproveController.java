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

/**
 * @Author MCX
 * @Version 1.0
 **/
@Controller
public class AppContractApproveController extends AuthenticatorController {

    @Autowired
    private ContractApproveAuditUserService contractApproveAuditUserService;
    @Autowired
    private ContractApproveNoticeService contractApproveNoticeService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;
    @Autowired
    private UserService userService;
    @Autowired
    private ContractApproveInfoService contractApproveInfoService;
     @Autowired
    private AuditInfoService auditInfoService;
    /**
     * 根据登录用户id获取合同审批审核人员列表
     * @param userId
     * @param response
     * @return
     */
    @RequestMapping("app_contractApproveAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        List<HashMap> auditUserList = contractApproveAuditUserService.getAuditUserList(userId);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 获取申请人信息
     * @param userId
     * @param response
     */
    @RequestMapping("app_applyUserPersonalInfo")
    public void applyUserPersonalInfo(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        appTeacherLeaveInfoController.teacherPersonalInfo(userId,response);
    }
    /**
     * 新增合同申请
     *
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addContractApproveInfo")
    public void addContractApproveInfo(@ModelAttribute("user_id") Integer userId, ContractApproveInfo req, HttpServletResponse response) {
        Assert.notNull(req.getTitle(), "合同标题不能为空");
        Assert.notNull(req.getProject(), "相关项目不能为空");
        Assert.notNull(req.getOrganization(), "承办单位不能为空");
        Assert.notNull(req.getPrincipal(), "负责人不能为空");
        Assert.notNull(req.getContractNumber(), "合同编号不能为空");
        Assert.notNull(req.getStartDate(), "立项审批日期不能为空");

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

        contractApproveInfoService.addContractApproveInfo(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 取消合同申请
     *
     * @param userId
     * @param approveId
     * @param response
     */
    @RequestMapping("app_cancelContractApproveInfo")
    public void cancelContractApproveInfo(@ModelAttribute("user_id") Integer userId, Integer approveId, HttpServletResponse response) {
        Assert.notNull(approveId, "请选择要取消的合同申请");

        ContractApproveInfo info = contractApproveInfoService.selectByPrimaryKey(approveId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该合同申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != info.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (info.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该合同申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.CONTRACT_APPROVE.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该合同申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        contractApproveInfoService.cancel(info);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 我的合同申请列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态
     * @param title
     * @param response
     */
    @RequestMapping("app_myApproveInfoList")
    public void myApproveInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status,String title,HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = contractApproveInfoService.myApproveInfoList(userId, title, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 查看合同单详情
     *
     * @param userId
     * @param approveId
     * @param response
     */
    @RequestMapping("app_approveInfoDetail")
    public void approveInfoDetail(@ModelAttribute("user_id") Integer userId, Integer approveId, HttpServletResponse response) {
        Assert.notNull(approveId, "请选择要查看的合同申请");

        ContractApproveInfo approveInfo = contractApproveInfoService.selectByPrimaryKey(approveId);
        if (approveInfo == null || approveInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该合同申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        // 查询整体审核记录列表
        List<HashMap> auditInfoList = contractApproveAuditUserService.findMapList(approveInfo);
        Map<String, Object> hashMap = BeanUtil.beanToMap(approveInfo);

        User user = userService.getById(approveInfo.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            contractApproveNoticeService.confirmNotice(approveInfo.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 我的合同申请审批列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态1审核中2通过,3驳回,5已完成(已处理)
     * @param response
     */
    @RequestMapping("app_myAuditApproveInfoList")
    public void myAuditApproveInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status,String title, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> approveInfos = contractApproveInfoService.myAuditapproveInfoList(userId, title, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", approveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }



    /**
     * 合同审批
     *
     * @param userId
     * @param approveId
     * @param status   审核状态（2通过,3驳回）
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditContractApproveInfo")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer approveId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(approveId, "请选择要审核的合同申请");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        ContractApproveInfo info = contractApproveInfoService.selectByPrimaryKey(approveId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该合同申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(info.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该合同申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (info.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        contractApproveInfoService.audit(info, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
}
