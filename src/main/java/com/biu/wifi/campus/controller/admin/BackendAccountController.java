package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.StringUtil;
import com.biu.wifi.campus.constant.AudUserRole;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.component.huanxin.HuanXinService;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class BackendAccountController {

    @Autowired
    private BackendAccountService accountService;

    @Autowired
    private BackendUserService userService;

    @Autowired
    private BackendGroupService groupService;

    @Autowired
    private BackendSchoolService schoolService;

    @Autowired
    private HuanXinService huanXinService;

    @Autowired
    private GroupTmpCreatePermissionService groupTmpCreatePermissionService;

    @Autowired
    private AuditUserAuthService auditUserAuthService;

    @Autowired
    private AuditUserRoleService auditUserRoleService;

    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserService classroomBookUseTypeOrganizationAuditUserService;

    protected Log logger = LogFactory.getLog(this.getClass());

    /**
     * 后台登录
     */
    @RequestMapping("/back_api_login")
    public void back_api_login(String password, String username, Short type, String verificationCode,
                               HttpServletRequest request, HttpServletResponse response) {

        if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(username) && type != null
                && StringUtils.isNotBlank(verificationCode)) {
            // todo 打包时需要将验证码放开，本地前后端开发时session不一致，导致无法校验
            // if (!verificationCode.equals(request.getSession().getAttribute("biu.jcaptcha"))) {
            //     ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "验证码错误", null));
            //     return;
            // }
            // 学校后台帐号或者总账号
            if (type == 1 || type == 2) {
                Account account = new Account();
                account.setIsDetele((short) 2);
                account.setUsername(username);
                account.setType(type);
                List<Account> accounts = accountService.getAccountListForLogin(account);

                if (accounts != null && accounts.size() == 1) {
                    account = accounts.get(0);

                    if (account != null && account.getStatus() == 2) {
                        ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "帐号被禁用", null));
                        return;
                    }

                    if (!StringUtil.MD5Encode(password + account.getSalt()).equals(account.getPassword())) {
                        ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "账号或密码错误", null));
                        return;
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("accountId", account.getId());
                    map.put("type", account.getType());
                    map.put("schoolId", account.getSchoolId());

                    School school = schoolService.getSchoolById(account.getSchoolId());
                    if (school != null) {
                        map.put("school", school.getName());
                    }
                    map.put("username", account.getUsername());

                    ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
                } else {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "用户不存在", null));
                }
            } else if (type == 3) {
                User user = new User();
                user.setIsDelete((short) 2);
                user.setPhone(username);
                List<User> users = userService.getUserListForLogin(user);

                if (users != null && users.size() == 1) {
                    user = users.get(0);

                    if (user != null && user.getStatus() == 2) {
                        ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "帐号被禁用", null));
                        return;
                    }

                    if (!StringUtil.MD5Encode(password + user.getSalt()).equals(user.getPassword())) {
                        ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "账号或密码错误", null));
                        return;
                    }

                    // 验证用户是否是群组管理员
                    List<Short> types = new ArrayList<Short>();
                    types.add((short) 1);
                    types.add((short) 2);
                    int groupCount = groupService.getManageGroupCount(user.getId(), types);


                    // 校验用户是否拥有发送一次性通知的权限
                    boolean hasGroupTmpCreatePermission = groupTmpCreatePermissionService
                            .getGroupTmpCreatePermission(user.getSchoolId(), 2, user.getId());

                    // 校验用户是否在教室预约审核人队列
                    boolean isAuditUser = classroomBookUseTypeOrganizationAuditUserService.allowToAuditClassroomBook(user.getSchoolId(), user.getId());

                    // 校验用户是否是教务处人员
                    boolean isJw = false;
                    AuditUserAuth auditUserAuth = auditUserAuthService.find(user.getSchoolId(), user.getId());
                    if (auditUserAuth != null) {
                        AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(user.getSchoolId(), AudUserRole.JWRY.getCode());
                        if (auditUserRole != null && auditUserAuth.getRoleId().intValue() == auditUserRole.getId()) {
                            isJw = true;
                        }
                    }

                    // 校验用户是否是教教学秘书
                    boolean isJxms = false;
                    auditUserAuth = auditUserAuthService.find(user.getSchoolId(), user.getId());
                    if (auditUserAuth != null) {
                        AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(user.getSchoolId(), AudUserRole.JXMS.getCode());
                        if (auditUserRole != null && auditUserAuth.getRoleId().intValue() == auditUserRole.getId()) {
                            isJxms = true;
                        }
                    }

                    if (groupCount == 0) {
                        if (!(hasGroupTmpCreatePermission || isJw || isAuditUser || isJxms)) {
                            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "您没有登陆权限", null));
                            return;
                        }
                    }

                    //是否拥有通知和待办的菜单权限
                    boolean noticeAndAuditPermission = false;
                    boolean classroomBookPermission = false;
                    if (isJw || isAuditUser || isJxms) {
                        noticeAndAuditPermission = true;
                        classroomBookPermission = true;
                    }

                    //是否拥有设置审批流程的权限
                    boolean setClassroomAuditUser = false;
                    if (isJw) {
                        setClassroomAuditUser = true;
                    }

                    //是否拥有审批权限
                    boolean classroomAuditPermission = false;
                    if (isJw || isAuditUser) {
                        classroomAuditPermission = true;
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("accountId", user.getId());
                    map.put("type", type);
                    map.put("user", user.getName());
                    map.put("schoolId", user.getSchoolId());
                    map.put("groupTmpCreatePermission", hasGroupTmpCreatePermission ? "1" : "0");
                    map.put("noticeAndAuditPermission", noticeAndAuditPermission ? "1" : "0");
                    map.put("classroomBookPermission", classroomBookPermission ? "1" : "0");
                    map.put("classroomAuditPermission", classroomAuditPermission ? "1" : "0");
                    map.put("setClassroomAuditUser", setClassroomAuditUser ? "1" : "0");

                    ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));

                } else {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "用户不存在", null));
                }
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "参数值不正确", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /*
     * 新增总后台账号
     */
    @RequestMapping("/back_api_addAdmin")
    public void back_api_addAdmin(String username, String password, HttpServletResponse response) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            Account account = new Account();
            account.setIsDetele((short) 2);
            account.setUsername(username);
            List<Account> accounts = accountService.getAccountListForLogin(account);

            if (accounts != null && accounts.size() > 0) {
                ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "用户名已经存在", null));
                return;
            }

            String random = StringUtil.getStringRandom(6);
            account.setType((short) 1);
            account.setSalt(random);
            account.setPassword(StringUtil.MD5Encode(password + random));
            account.setStatus((short) 1);
            account.setCreateTime(new Date());
            accountService.addAccount(account);
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteHXAccount")
    public void back_api_deleteHXAccount(String phone, HttpServletResponse response) {
        huanXinService.delImMembers(phone);
    }
}
