package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.dao.model.AuditInfo;
import com.biu.wifi.campus.dao.model.DiscussionTopicApply;
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
 * @author 张彬.
 * @date 2021/4/4 21:25.
 */
@Controller
public class AppDiscussionTopicApplyController extends AuthenticatorController {

    @Autowired
    private DiscussionTopicApplyService discussionTopicApplyService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private DiscussionTopicAuditService discussionTopicAuditService;
    @Autowired
    private DiscussionTopicNoticeService discussionTopicNoticeService;

    /**
     * 根据登录用户id获取议题申请审核人员列表
     *
     * @param userId
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/15 16:05.
     */
    @RequestMapping("app_discussionTopicApplyAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        List<HashMap> auditUserList = discussionTopicApplyService.getAuditUserList(userId);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 会议议题申请
     *
     * @param userId
     * @param req
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/4 21:26.
     */
    @RequestMapping("app_addDiscussion")
    public void addDiscussion(@ModelAttribute("user_id") Integer userId, DiscussionTopicApply req, HttpServletResponse response) {
        if (req.getTopicName() == null || req.getTopicType() == null || req.getStartDate() == null
                || req.getTopicContent() == null || req.getApplyDeptId() == null || req.getAttendDeptId() == null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        req.setUserId(userId);
        discussionTopicApplyService.addDiscussion(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 取消会议议题申请
     *
     * @param userId
     * @param bizId
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/4 21:53.
     */
    @RequestMapping("app_cancelDiscussion")
    public void cancelDiscussion(@ModelAttribute("user_id") Integer userId, Integer bizId, HttpServletResponse response) {
        Assert.notNull(bizId, "请选择要取消的申请");

        DiscussionTopicApply apply = discussionTopicApplyService.selectByPrimaryKey(bizId);
        if (apply == null || apply.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该议题申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != apply.getUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (apply.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该议题申请已被取消,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 已经被审批的记录不能取消
        List<AuditInfo> auditInfoList = auditInfoService.findList(AuditBusinessType.DISCUSSION_TOPIC_APPLY.getCode().shortValue(), userId, "id", true);
        if (!auditInfoList.isEmpty() && auditInfoList.get(0).getIsPass() != null) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该议题申请已被处理不能取消", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        discussionTopicApplyService.cancelDiscussion(apply);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 我的会议议题申请列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myDiscussionTopicApplyList")
    public void myDiscussionTopicApplyList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> applys = discussionTopicApplyService.myDiscussionTopicApplyList(userId, startDate, endDate, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", applys);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 查看会议议题申请详情
     *
     * @param userId
     * @param applyId
     * @param response
     */
    @RequestMapping("app_discussionTopicApplyDetail")
    public void detail(@ModelAttribute("user_id") Integer userId, Integer applyId, HttpServletResponse response) {
        Assert.notNull(applyId, "请选择要查看的申请");

        DiscussionTopicApply apply = discussionTopicApplyService.selectByPrimaryKey(applyId);
        if (apply == null || apply.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该会议议题申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 查询整体审核记录列表
        List<HashMap> auditInfoList = discussionTopicAuditService.findMapList(apply);
        Map<String, Object> hashMap = BeanUtil.beanToMap(apply);
        hashMap.put("auditInfoList", auditInfoList);
        hashMap.put("applyDeptName", instituteService.getInstituteNamesByIds(apply.getApplyDeptId().toString()));
        hashMap.put("attendDeptName", instituteService.getInstituteNamesByIds(apply.getAttendDeptId()));

        if (userId != null) {
            // 确认收到
            discussionTopicNoticeService.confirmNotice(apply.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 我的会议议题审批列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态2通过,3驳回,5已完成(已处理)
     * @param startDate
     * @param endDate
     * @param response
     */
    @RequestMapping("app_myAuditDiscussionTopicApplyList")
    public void myAuditDiscussionTopicApplyList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status, String startDate, String endDate, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = discussionTopicApplyService.myAuditDiscussionTopicApplyList(userId, startDate, endDate, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 会议议题申请审批
     *
     * @param userId
     * @param applyId
     * @param status   审核状态2通过,3驳回
     * @param remark
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/8 12:33.
     */
    @RequestMapping("app_auditDiscussionTopicApply")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer applyId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(applyId, "请选择要审批的记录");
        Assert.notNull(status, "请选择您的审核结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        DiscussionTopicApply apply = discussionTopicApplyService.selectByPrimaryKey(applyId);
        if (apply == null || apply.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该会议议题申请不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(apply.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该会议议题申请已被审核,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (apply.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        discussionTopicApplyService.audit(apply, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
}
