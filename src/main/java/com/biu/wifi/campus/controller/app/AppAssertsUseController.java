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
 * @Author MCX
 * @Version 1.0
 **/

@Controller
@RequestMapping
public class AppAssertsUseController extends AuthenticatorController {
    @Autowired
    private AssertsUseAuditUserService assertsUseAuditUserService;
    @Autowired
    private AssertsUseInfoService assertsUseInfoService;
    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;
    @Autowired
    private DictInfoService dictInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private AssertsUseNoticeService assertsUseNoticeService;
    /**
     * 根据登录用户id获取合同审批审核人员列表
     * @param userId
     * @param response
     * @return
     */
    @RequestMapping("app_assertUseAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        List<HashMap> auditUserList = assertsUseAuditUserService.getAuditUserList(userId);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 获取申请人信息
     * @param userId
     * @param response
     */
    @RequestMapping("app_assertUserInfo")
    public void assertUserInfo(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        appTeacherLeaveInfoController.teacherPersonalInfo(userId,response);
    }

    /**
     * 资产使用规模列表
     *
     * @param response
     * @return
     */
    @RequestMapping("app_scanList")
    public void scanList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.SCAN_TYPE.name());
        Assert.notNull(parent, "资产使用规模类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 新增资产使用
     *
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addAssertsUseInfo")
    public void addAssertsUseInfo(@ModelAttribute("user_id") Integer userId, AssertsUseInfo req, HttpServletResponse response) {
        Assert.notNull(req.getUnit(), "申请单位不能为空");
        Assert.notNull(req.getAst(), "使用资产不能为空");
        Assert.notNull(req.getReason(), "申请原因不能为空");
        Assert.notNull(req.getScanId(), "规模不能为空");
        Assert.notNull(req.getLeader(), "活动负责人不能为空");
        Assert.notNull(req.getPhone(), "联系电话不能为空");
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

        assertsUseInfoService.addAssertsUseInfo(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 取消资产申请
     *
     * @param userId
     * @param useId
     * @param response
     */
    @RequestMapping("app_cancelAssertsUseInfo")
    public void cancelContractApproveInfo(@ModelAttribute("user_id") Integer userId, Integer useId, HttpServletResponse response) {
        Assert.notNull(useId, "请选择要取消的资产申请");

        AssertsUseInfo info = assertsUseInfoService.selectByPrimaryKey(useId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该资产申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != info.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (info.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该资产申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.ASSERTS_USE.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该合同申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        assertsUseInfoService.cancel(info);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 我的资产申请列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态
     * @param response
     */
    @RequestMapping("app_myAssertsUseInfoList")
    public void myAssertsUseInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize,Short status, String startDate, String endDate,HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = assertsUseInfoService.myUseInfoList(userId, startDate,endDate, status);
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
    @RequestMapping("app_assertsUseInfoDetail")
    public void assertsUseInfoDetail(@ModelAttribute("user_id") Integer userId, Integer approveId, HttpServletResponse response) {
        Assert.notNull(approveId, "请选择要查看的资产申请");

        AssertsUseInfo assertsUseInfo = assertsUseInfoService.selectByPrimaryKey(approveId);
        if (assertsUseInfo == null || assertsUseInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "资产申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        // 查询整体审核记录列表
        List<HashMap> auditInfoList = assertsUseAuditUserService.findMapList(assertsUseInfo);
        Map<String, Object> hashMap = BeanUtil.beanToMap(assertsUseInfo);

        User user = userService.getById(assertsUseInfo.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            assertsUseNoticeService.confirmNotice(assertsUseInfo.getId(), userId);
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
    @RequestMapping("app_myAuditUseInfoList")
    public void myAuditUseInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status,String startDate, String endDate,HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> approveInfos = assertsUseInfoService.myAuditUseInfoList(userId, startDate,endDate, status);
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
     * @param useId
     * @param status   审核状态（2通过,3驳回）
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditAssertsUseInfo")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer useId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(useId, "请选择要审核的资产申请");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        AssertsUseInfo info = assertsUseInfoService.selectByPrimaryKey(useId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该资产申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(info.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该资产申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (info.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        assertsUseInfoService.audit(info, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
}
