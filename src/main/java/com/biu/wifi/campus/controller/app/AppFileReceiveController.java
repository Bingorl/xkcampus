package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.constant.BaseDictType;
import com.biu.wifi.campus.dao.FileReceiveAuditInfoMapper;
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
public class AppFileReceiveController extends  AuthenticatorController {
    @Autowired
    private FileReceiveAuditUserSerive fileReceiveAuditUserSerive;
    @Autowired
    private AppTeacherLeaveInfoController appTeacherLeaveInfoController;
    @Autowired
    private DictInfoService dictInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileReceiveAuditInfoService fileReceiveAuditInfoService;
    @Autowired
    private AuditInfoService auditInfoService;
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private FileReceiveNoticeService fileReceiveNoticeService;

    /**
     * 获取用户的审核列表人员
     * @param userId
     * @param type
     * @param response
     */
    @RequestMapping("app_fileReceiveAuditUserList")
    public void getAuditUserList(@ModelAttribute("user_id") Integer userId, Integer type, HttpServletResponse response) {
        Assert.notNull(type,"文件类型不能为空");
        List<HashMap> auditUserList =fileReceiveAuditUserSerive.getAuditUserList(userId, type);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", auditUserList));
        ServletUtilsEx.renderText(response, json);

    }
    /**
     * 获取申请人信息
     * @param userId
     * @param response
     */
    @RequestMapping("app_fileUserPersonalInfo")
    public void applyUserPersonalInfo(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        appTeacherLeaveInfoController.teacherPersonalInfo(userId,response);
    }

    /**文种类型
     *
     * @param response
     */
    @RequestMapping("app_fileTypeList")
    public void stampTypeList(HttpServletResponse response) {
        DictInfo parent = dictInfoService.selectByCode(BaseDictType.FILE_TYPE.name());
        Assert.notNull(parent, "文种类型根字典不存在");

        List<Map> dictInfos = dictInfoService.selectMapByPid(parent.getId());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", dictInfos));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 新增文件签发
     * @param userId
     * @param req
     * @param response
     */
    @RequestMapping("app_addFileReceiveUserAudit")
    public void addFileReceiveUserAudit(@ModelAttribute("user_id") Integer userId, FileReceiveAuditInfo req, HttpServletResponse response){
        Assert.notNull(req.getTitle(), "文件标题不能为空");
        Assert.notNull(req.getContent(), "文件内容不能为空");
        Assert.notNull(req.getReferenceNumber(), "文号不能为空");
        Assert.notNull(req.getFileType(), "文种不能为空");
        User user = userService.getById(userId);
        if (user == null || user.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "当前登录用户不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }
        FileReceiveAuditUser fileReceiveAuditUser = fileReceiveAuditUserSerive.find(user.getSchoolId(), user.getInstituteId(), req.getFileType());
        req.setAuditUser(fileReceiveAuditUser.getAuditUser());
        req.setApplyUserId(userId);
        req.setApplyUserName(user.getName());
        req.setApplyUserNo(user.getStuNumber());
        req.setApplyUserTel(user.getPhone());
        req.setApplyUserDeptId(user.getInstituteId().toString());
        fileReceiveAuditInfoService.add(req);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 取消申请
     *
     * @param userId
     * @param receiveId
     * @param response
     */
    @RequestMapping("app_cancelFileReceiveAuditInfo")
    public void cancelFileReceiveAuditInfo(@ModelAttribute("user_id") Integer userId, Integer receiveId, HttpServletResponse response) {
        Assert.notNull(receiveId, "请选择要取消的请假申请");

        FileReceiveAuditInfo fileReceiveAuditInfo= fileReceiveAuditInfoService.selectByPrimaryKey(receiveId);
        if (fileReceiveAuditInfo == null || fileReceiveAuditInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该文件签发不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (userId.intValue() != fileReceiveAuditInfo.getApplyUserId()) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您没有操作权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (fileReceiveAuditInfo.getStatus().intValue() == 4) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "文件签发已被取消,请勿重复操作", null));
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

        fileReceiveAuditInfoService.cancel(fileReceiveAuditInfo);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 我的文件签发列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态
     * @param referenceNum
     * @param title
     * @param response
     */
    @RequestMapping("app_myFileReceiveAuditInfoList")
    public void myFileReceiveAuditInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize, Short status,String referenceNum,String title, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> fileReceiveAuditInfo = fileReceiveAuditInfoService.myInfoList(userId, referenceNum, title, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", fileReceiveAuditInfo);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 查看文件签发详情
     *
     * @param userId
     * @param receiveId
     * @param response
     */
    @RequestMapping("app_fileReceiveAuditInfoDetail")
    public void detail(@ModelAttribute("user_id") Integer userId, Integer receiveId, HttpServletResponse response) {
        Assert.notNull(receiveId, "请选择要查看的文件签发详情");

        FileReceiveAuditInfo fileReceiveAuditInfo = fileReceiveAuditInfoService.selectByPrimaryKey(receiveId);
        if (fileReceiveAuditInfo == null || fileReceiveAuditInfo.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        // 查询整体审核记录列表
        List<HashMap> auditInfoList = fileReceiveAuditUserSerive.findMapList(fileReceiveAuditInfo);
        Map<String, Object> hashMap = BeanUtil.beanToMap(fileReceiveAuditInfo);

        User user = userService.getById(fileReceiveAuditInfo.getApplyUserId());
        Institute institute = instituteService.getById(user.getInstituteId());
        hashMap.put("applyUserInstituteName", institute.getName());
        hashMap.put("auditInfoList", auditInfoList);
        hashMap.put("leaveTypeName", dictInfoService.selectById(fileReceiveAuditInfo.getFileType()).getName());

        // 确认收到
        if (userId != null) {
            //后台管理接口调用时没有userId
            fileReceiveNoticeService.confirmNotice(fileReceiveAuditInfo.getId(), userId);
        }
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }
    /**
     * 我的文件审核列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param status    审核状态1审核中2通过,3驳回,5已完成(已处理)
     * @param referenceNum
     * @param title
     * @param response
     */
    @RequestMapping("app_myFileAuditLeaveInfoList")
    public void myAuditLeaveInfoList(@ModelAttribute("user_id") Integer userId, Integer pageNum, Integer pageSize,Short status,String referenceNum,String title, HttpServletResponse response) {
        PageLimitHolderFilter.setContext(pageNum, pageSize, null);
        List<HashMap> teacherLeaveInfos = fileReceiveAuditInfoService.myAuditfileInfoList(userId, referenceNum, title, status);
        HashMap hashMap = new HashMap();
        hashMap.put("list", teacherLeaveInfos);
        hashMap.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", hashMap));
        ServletUtilsEx.renderText(response, json);
    }

    /**
     * 文件签发审核
     *
     * @param userId
     * @param receiveId
     * @param status   审核状态（2通过,3驳回）
     * @param remark
     * @param response
     */
    @RequestMapping("app_auditFileReceive")
    public void audit(@ModelAttribute("user_id") Integer userId, Integer receiveId, Short status, String remark, HttpServletResponse response) {
        Assert.notNull(receiveId, "请选择要签发的文件");
        Assert.notNull(status, "请选择您的签发结果");
        if (status.intValue() == 3 && StringUtil.isBlank(remark)) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入驳回理由", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        FileReceiveAuditInfo info = fileReceiveAuditInfoService.selectByPrimaryKey(receiveId);
        if (info == null || info.getIsDelete().intValue() == 1) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该文件签发不存在", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (Arrays.asList(2, 3).contains(info.getStatus().intValue())) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该文件已被签发,请勿重复操作", null));
            ServletUtilsEx.renderText(response, json);
            return;
        } else if (info.getCurrentAuditUserId().intValue() != userId) {
            String json = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您当前无审核权限", null));
            ServletUtilsEx.renderText(response, json);
            return;
        }

        fileReceiveAuditInfoService.audit(info, status, remark);
        String json = JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null));
        ServletUtilsEx.renderText(response, json);
    }

}
