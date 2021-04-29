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
 * 用章申请模块
 * @Author MCX
 * @Version 1.0
 **/
@Controller
public class AppStampToApplyController extends AuthenticatorController {

    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;
    @Autowired
    private StampToApplyInfoService stampToApplyInfoService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private StampToApplyUserService stampToApplyUserService;
    @Autowired
    private DictInfoService dictInfoService;
    @Autowired
    private StampToApplyService stampToApplyService;
    @Autowired
    private StampToApplyNoticeService stampToApplyNoticeService;


    /**
     * 获取用户的审核列表人员
     * @param userId
     * @param type
     * @param response
     */
    @RequestMapping("app_stampToApplyAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id") Integer userId, Integer type, HttpServletResponse response) {
        Assert.notNull(type,"用途类型不能为空");
        List<HashMap> auditUserList = stampToApplyUserService.getAuditUserList(userId, type);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);

    }


    /**用章申请类型
     *
     * @param response
     */
    @RequestMapping("app_stampToApplyTypeList")
    public void stampToApplyTypeList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.STAMP_TO_APPLY_TYPE.name());
        Assert.notNull(parent, "用户申请类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
        ServletUtilsEx.renderText(response, json);
    }
    /**印章类型
     *
     * @param response
     */
    @RequestMapping("app_stampTypeList")
    public void stampTypeList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.STAMP_TYPE.name());
        Assert.notNull(parent, "用户申请类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
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
     * 新增用章申请申请
     *
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addStampToApplyInfo")
    public void addStampToApplyInfo(@ModelAttribute("user_id") Integer userId, StampToApplyInfo req, HttpServletResponse response) {
        Assert.notNull(req.getApplyType(), "用途类型不能为空");
        Assert.notNull(req.getStampType(), "印章类型不能为空");
        Assert.notNull(req.getReason(), "申请原因不能为空");
        Assert.notNull(req.getStartDate(), "开始日期不能为空");
        Assert.notNull(req.getEndDate(), "结束日期不能为空");


        if (req.getStartDate().compareTo(req.getEndDate()) > 0) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "结束日期不得早于开始日期", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        User user = userService.getById(userId);
        if (user == null || user.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "当前登录用户不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        StampToApplyUser stampToApplyUser=stampToApplyUserService.find(user.getSchoolId(),user.getInstituteId(),req.getApplyType());
        req.setAuditUser(stampToApplyUser.getAuditUser());
        req.setApplyUserId(userId);
        req.setApplyUserName(user.getName());
        req.setApplyUserNo(user.getStuNumber());
        req.setApplyUserTel(user.getPhone());
        req.setApplyUserDeptId(user.getInstituteId().toString());
        stampToApplyInfoService.add(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 取消用章申请申请
     *
     * @param userId
     * @param applyId
     * @param response
     */
    @RequestMapping("app_cancelStampToApplyInfo")
    public void cancelStampToApplyInfo(@ModelAttribute("user_id") Integer userId, Integer applyId, HttpServletResponse response) {
        Assert.notNull(applyId, "请选择要取消的请假申请");

        StampToApplyInfo ApplyInfo = stampToApplyInfoService.selectByPrimaryKey(applyId);
        if (ApplyInfo == null || ApplyInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != ApplyInfo.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (ApplyInfo.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.STAMP_TO_APPLY.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        stampToApplyInfoService.cancel(ApplyInfo);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

    /**我的用章申请列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myStampToApplyList")
    public void myStampToApplyList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> stampToApplyInfos = stampToApplyInfoService.myStampInfoList(userId, startDate, endDate, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", stampToApplyInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }


    /**
     * 查看用章申请单详情
     *
     * @param userId
     * @param applyId
     * @param response
     */
    @RequestMapping("app_stampToApplyInfoDetail")
    public void detail(@ModelAttribute("user_id") Integer userId, Integer applyId, HttpServletResponse response) {
        Assert.notNull(applyId, "请选择要查看的请假申请");

        StampToApplyInfo stampToApplyInfo = stampToApplyInfoService.selectByPrimaryKey(applyId);
        if (stampToApplyInfo == null || stampToApplyInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 查询整体审核记录列表
        List<HashMap> auditInfoList = stampToApplyService.findMapList(stampToApplyInfo);
        Map<String, Object> hashMap = BeanUtil.beanToMap(stampToApplyInfo);

        User user = userService.getById(stampToApplyInfo.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        hashMap.put("applyType", dictInfoService.selectById(stampToApplyInfo.getApplyType()).getName());
        hashMap.put("stampTypeName", dictInfoService.selectById(stampToApplyInfo.getStampType()).getName());

        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            stampToApplyNoticeService.confirmNotice(stampToApplyInfo.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 我的用章审批列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态1审核中2通过,3驳回,5已完成(已处理)
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myAuditApplyInfoList")
    public void myAuditApplyInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = stampToApplyInfoService.myAuditApplyInfoList(userId, startDate, endDate, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }



    /**
     * 用章审批
     *
     * @param userId
     * @param applyId
     * @param status   审核状态（2通过,3驳回）
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditStampAuditInfo")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer applyId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(applyId, "请选择要审核的用章申请");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        StampToApplyInfo stampToApplyInfo = stampToApplyInfoService.selectByPrimaryKey(applyId);
        if (stampToApplyInfo == null || stampToApplyInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(stampToApplyInfo.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该请假申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (stampToApplyInfo.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        stampToApplyInfoService.audit(stampToApplyInfo, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
}
