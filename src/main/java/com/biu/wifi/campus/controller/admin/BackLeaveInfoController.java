package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.dao.model.LeaveAudit;
import com.biu.wifi.campus.dao.model.LeaveAuditUserRole;
import com.biu.wifi.campus.dao.model.LeaveType;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.LeaveAuditUserRoleService;
import com.biu.wifi.campus.service.LeaveInfoService;
import com.biu.wifi.campus.service.LeaveTypeService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author zhangbin
 */
@Controller
public class BackLeaveInfoController {

    @Autowired
    private LeaveInfoService leaveInfoService;
    @Autowired
    private LeaveAuditUserRoleService leaveAuditUserRoleService;
    @Autowired
    private LeaveTypeService leaveTypeService;

    /**
     * 学校管理员从平台设置的请假类型列表中添加请假类型
     *
     * @param schoolId
     * @param leaveTypeId
     * @param response
     */
    @RequestMapping("back_api_addLeaveTypeBySchoolAdmin")
    public void back_api_addLeaveTypeBySchoolAdmin(String schoolId, String leaveTypeId, HttpServletResponse response) {
        if (StringUtils.isBlank(schoolId) || StringUtils.isBlank(leaveTypeId)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }


        LeaveType query = leaveTypeService.find(Integer.valueOf(leaveTypeId));
        LeaveType leaveType = new LeaveType();
        leaveType.setName(query.getName());
        leaveType.setSchoolId(Integer.valueOf(schoolId));

        leaveTypeService.addOrUpdate(leaveType);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 平台管理员新增请假类型
     *
     * @param id       请假类型ID
     * @param schoolId 学校ID
     * @param name     请假类型名
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateLeaveType")
    public void back_api_addOrUpdateLeaveType(String id, String schoolId, String name, HttpServletResponse response) {
        if (StringUtils.isBlank(name)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        LeaveType leaveType = new LeaveType();
        leaveType.setName(name);

        try {
            if (StringUtils.isNotBlank(id)) {
                leaveType.setId(Integer.valueOf(id));
            }
            if (StringUtils.isNotBlank(schoolId)) {
                leaveType.setSchoolId(Integer.valueOf(schoolId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        leaveTypeService.addOrUpdate(leaveType);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 删除请假类型
     *
     * @param id       类型ID
     * @param response
     */
    @RequestMapping("back_api_deleteLeaveType")
    public void back_api_deleteLeaveType(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        leaveTypeService.delete(id);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 请假类型列表
     *
     * @param schoolId 学校ID(平台为bull)
     * @param name     类型名称关键字
     * @param response
     */
    @RequestMapping("back_api_findLeaveTypeList")
    public void back_api_findLeaveTypeList(String schoolId, String name, HttpServletResponse response) {
        List<LeaveType> leaveTypeList = leaveTypeService.findList(StringUtils.isBlank(schoolId) ? null : Integer.valueOf(schoolId), name);
        Collections.sort(leaveTypeList, new Comparator<LeaveType>() {
            @Override
            public int compare(LeaveType o1, LeaveType o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", leaveTypeList));
    }

    /**
     * 根据身份认证类型和用户名称关键字搜索对应身份类型的人员
     *
     * @param schoolId 学校ID
     * @param roleId   身份认证类型ID
     * @param name     用户姓名关键字
     * @param response
     */
    @RequestMapping("back_api_findUserListByAuditUserRoleId")
    public void back_api_findUserListByAuditUserRoleId(Integer schoolId,
                                                       @RequestParam(defaultValue = "0", required = false) Integer instituteId,
                                                       @RequestParam(defaultValue = "0", required = false) Integer majorId,
                                                       @RequestParam(defaultValue = "0", required = false) Integer classId,
                                                       @RequestParam(defaultValue = "0", required = false) Integer gradeId,
                                                       Integer roleId, String name, HttpServletResponse response) {
        if (schoolId == null /*|| StringUtils.isBlank(name)*/ || roleId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }
        List<Map<String, Object>> userList = leaveAuditUserRoleService.findUserListByAuditUserRoleId(schoolId, instituteId, majorId, classId, gradeId, roleId, name);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", userList));
    }

    /**
     * 新增或编辑请假审批身份认证类型
     *
     * @param id
     * @param schoolId 学校ID
     * @param pid      父级ID(一级类型pid=0，二级以下类型的pid为上级类型的ID)
     * @param name     身份认证类型名称
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateLeaveAuditUserRole")
    public void back_api_addOrUpdateLeaveAuditUserRole(@RequestParam(required = false) String id, Integer schoolId,
                                                       @RequestParam(defaultValue = "0", required = false) Integer pid, String name,
                                                       @RequestParam(defaultValue = "0", required = false) Short allowCreateAuditUser,
                                                       HttpServletResponse response) {
        if (schoolId == null || name == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        Integer roleId = null;
        if (StringUtils.isNotBlank(id)) {
            roleId = Integer.valueOf(id);
        }

        if (pid == 0) {
            pid = null;
        }

        Result result = leaveAuditUserRoleService.addOrUpdateLeaveAuditUserRole(roleId, schoolId, pid, name, allowCreateAuditUser);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 删除请假审批身份认证类型
     *
     * @param id
     * @param response
     */
    @RequestMapping("back_api_deleteLeaveAuditUserRole")
    public void back_api_deleteLeaveAuditUserRole(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }
        Result result = leaveAuditUserRoleService.delete(id);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 获取xx学校的请假审批身份认证类型列表
     *
     * @param schoolId 学校ID
     * @param pid      父ID
     * @param response
     */
    @RequestMapping("back_api_findLeaveAuditUserRoleList")
    public void back_api_findLeaveAuditUserRoleList(Integer schoolId, @RequestParam(defaultValue = "0", required = false) Integer pid, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        if (pid == 0) {
            pid = null;
        }

        List<LeaveAuditUserRole> list = leaveAuditUserRoleService.findList(schoolId, pid);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }


    /**
     * 销假
     *
     * @param id       请假单ID
     * @param backDate 销假日期
     * @param response
     */
    @RequestMapping("back_api_reportBackForLeaveInfo")
    public void back_api_reportBackForLeaveInfo(Integer id, Date backDate, HttpServletResponse response) {
        if (id == null || backDate == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        Result result = leaveInfoService.reportBackForLeaveInfo(id, backDate);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 用户的请假列表
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @param userId   用户ID
     * @param response
     */
    @RequestMapping("back_api_findLeaveInfoListByUserId")
    public void back_api_findLeaveInfoListByUserId(Integer page, Integer pageSize, Integer userId,
                                                   HttpServletResponse response) {
        if (page == null || pageSize == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        Result result = leaveInfoService.findLeaveInfoList(userId);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * xx用户的待审批列表
     *
     * @param page     页码
     * @param pageSize 每页记录数
     * @param userId   用户ID
     * @param response
     */
    @RequestMapping("back_api_findLeaveAuditListByUserId")
    public void back_api_findLeaveAuditListByUserId(Integer page, Integer pageSize, Integer userId,
                                                    HttpServletResponse response) {
        if (page == null || pageSize == null || userId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = leaveInfoService.findLeaveAuditList(userId);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    /**
     * 审批请假
     *
     * @param leaveId   请假单ID
     * @param userId    审批人ID
     * @param isPass    是否通过(1是2否)
     * @param auditUser 审批人ID字符串列表
     * @param remark    审批意见
     * @param response
     */
    @RequestMapping("back_api_updateLeaveAudit")
    public void back_api_updateLeaveAudit(Integer leaveId, Integer userId, Short isPass,
                                          String auditUser, String remark, HttpServletResponse response) {
        if (leaveId == null || userId == null || isPass == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        LeaveAudit leaveAudit = new LeaveAudit();
        leaveAudit.setLeaveId(leaveId);
        leaveAudit.setUserId(userId);
        leaveAudit.setIsPass(isPass);
        leaveAudit.setRemark(remark);

        Result result = leaveInfoService.updateLeaveAudit(leaveAudit, auditUser, true);
        ServletUtilsEx.renderJson(response, result);
    }
}
