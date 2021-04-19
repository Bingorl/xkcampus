package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.dao.model.AuditUserAuth;
import com.biu.wifi.campus.dao.model.AuditUserRole;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.AuditUserAuthService;
import com.biu.wifi.campus.service.AuditUserRoleService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 教职工身份认证控制器
 *
 * @author zhangbin.
 * @date 2018/12/13.
 */
@Controller
public class AppAuditUserAuthController extends AuthenticatorController {

    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private AuditUserRoleService auditUserRoleService;
    @Autowired
    private UserService userService;

    /**
     * 身份认证类型列表
     *
     * @param userId   用户ID
     * @param pid      父级ID(一级类型pid=0，二级以下类型的pid为上级类型的ID)
     * @param response
     */
    @RequestMapping("app_findAuditUserRoleList")
    public void app_findAuditUserRoleList(@ModelAttribute("user_id") Integer userId,
                                          @RequestParam(defaultValue = "0") Integer pid, HttpServletResponse response) {
        User user = userService.getById(userId);
        if (pid == 0) {
            pid = null;
        }
        List<Map<String, Object>> list = auditUserRoleService.findMapList(user.getSchoolId(), pid);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 教职工身份认证
     *
     * @param userId      用户ID
     * @param instituteId 学院ID
     * @param gradeId     年级ID
     * @param buildingId  宿舍楼ID
     * @param roleId      身份认证类型ID
     * @param reOperate   是否是重新认证，默认为false
     * @param reOperate2  是否是重新认证，默认为 0
     * @param response
     */
    @RequestMapping("app_addAuditUserAuth")
    public void app_addAuditUserAuth(@ModelAttribute("user_id") Integer userId,
                                     @RequestParam(required = false) Integer instituteId,
                                     @RequestParam(required = false) Integer gradeId,
                                     @RequestParam(required = false) Integer buildingId,
                                     Integer roleId,
                                     @RequestParam(defaultValue = "false") Boolean reOperate,
                                     @RequestParam(defaultValue = "0") Integer reOperate2,
                                     HttpServletResponse response) {

        if (userId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.NO_LOGIN, "未登录")));
            return;
        }
        if (roleId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请选择要认证的身份类型")));
            return;
        }

        AuditUserRole authUserRole = auditUserRoleService.findById(roleId);
        User user = userService.getById(userId);
        instituteId = user.getInstituteId();
        if (authUserRole.getAuthParam() != null) {
            List<String> params = Arrays.asList(authUserRole.getAuthParam().split(","));
            if (params.contains("instituteId") && instituteId == null) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.EDIT_PERSON_INFO, "您当前未选择学院\n请去个人中心->个人主页->编辑 进行设置")));
                return;
            }

            if (params.contains("gradeId") && gradeId == null) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请选择所在年级")));
                return;
            }

            if (params.contains("buildingId") && buildingId == null) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请选择所在宿舍楼")));
                return;
            }
        }

        if (user.getIsTeacher() == 2) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "学生不能进行审批流的身份认证", null)));
            return;
        }

        AuditUserRole auditUserRole = auditUserRoleService.findById(roleId);
        if (auditUserRole == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "您认证的角色不存在", null)));
            return;
        }

        AuditUserAuth auditUserAuth = new AuditUserAuth();
        auditUserAuth.setSchoolId(user.getSchoolId());
        auditUserAuth.setInstituteId(instituteId);
        auditUserAuth.setGradeId(gradeId);
        auditUserAuth.setBuildingId(buildingId);
        auditUserAuth.setRoleId(roleId);
        auditUserAuth.setUserId(userId);
        Result result = auditUserAuthService.addAuditUserAuth(auditUserAuth, reOperate, reOperate2);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

}
