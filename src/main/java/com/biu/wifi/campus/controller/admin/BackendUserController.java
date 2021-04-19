package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.PushUtils;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.component.huanxin.HuanXinService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class BackendUserController {

    @Autowired
    private BackendUserService userService;

    @Autowired
    private BackendSchoolService schoolService;

    @Autowired
    private HuanXinService huanXinService;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private AccountOnlineService accountOnlineService;

    @Autowired
    private GroupTmpCreatePermissionService groupTmpCreatePermissionService;

    @Autowired
    private BackendAccountService accountService;

    /**
     * 给用户设置创建临时群组（发一次性通知）的权限
     *
     * @param userId
     * @param response
     */
    @RequestMapping("back_api_addGroupTmpCreatePermission")
    public void back_api_addGroupTmpCreatePermission(Integer userId, Integer userType, HttpServletResponse response) {
        if (userId == null || userType == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Integer schoolId = null;
        if (userType == 1) {
            // 学校用户
            Account account = accountService.getById(userId);
            if (account == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该学校用户不存在", null));
                return;
            }
            schoolId = account.getSchoolId();
        } else {
            // 普通用户
            User user = userService.getById(userId);
            if (user == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该普通用户不存在", null));
                return;
            }
            schoolId = user.getSchoolId();
        }

        // 学校用户拥有创建临时群组的权限(发送一次性通知)
        GroupTmpCreatePermission permission = new GroupTmpCreatePermission();
        permission.setSchoolId(schoolId);
        permission.setUserId(userId);
        permission.setUserType(userType);
        permission.setIsDelete((short) 2);
        Result result = groupTmpCreatePermissionService.addGroupTmpCreatePermission(permission);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 取消用户的创建临时群组（发一次性通知）的权限
     *
     * @param userId
     * @param response
     */
    @RequestMapping("back_api_deleteGroupTmpCreatePermission")
    public void back_api_deleteGroupTmpCreatePermission(Integer userId, Integer userType,
                                                        HttpServletResponse response) {
        if (userId == null || userType == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        User user = userService.getById(userId);
        Result result = groupTmpCreatePermissionService.deleteGroupTmpCreatePermission(user.getSchoolId(), userType,
                userId);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 用户列表（学生用户、教职工用户）
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param startTime
     * @param endTime
     * @param name
     * @param instituteId
     * @param majorId
     * @param classId
     * @param gradeId
     * @param id
     * @param studentNumber
     * @param status
     * @param isTeacher     是否是教职工(1-是，2否)
     * @param response
     */
    @RequestMapping("/back_api_findUserList_s")
    public void back_api_findUserList_s(Integer page, Integer pageSize, Integer schoolId, String startTime,
                                        String endTime, String name, Integer instituteId, Integer majorId, Integer classId, Integer gradeId,
                                        Integer id, String studentNumber, Short status, Short isTeacher, HttpServletResponse response) {

        if (page == null || pageSize == null || schoolId == null || status == null
                || (isTeacher == 2 && (instituteId == null || majorId == null || classId == null || gradeId == null))) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);

        List<Map<String, Object>> list;
        if (isTeacher == 1) {
            list = userService.findUserTeacherLists(schoolId, startTime, endTime, name, id, studentNumber, status);
        } else {
            // 学生
            list = userService.findUserLists(schoolId, startTime, endTime, name, instituteId, majorId, classId, gradeId,
                    id, studentNumber, status, isTeacher);
        }

        // 查看发送一次性通知的权限
        boolean permission = false;
        for (Map<String, Object> map : list) {
            permission = groupTmpCreatePermissionService.getGroupTmpCreatePermission(schoolId, 2,
                    Integer.valueOf(String.valueOf(map.get("id"))));
            map.put("groupTmpCreatePermission", permission);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
    }

    @RequestMapping("/back_api_findUserListByCondition")
    public void back_api_findUserListByCondition(Integer schoolId, String name, String stuNumber, Integer instituteId, Integer majorId,
                                                 Integer classId, Integer gradeId, @RequestParam(defaultValue = "0") Integer groupId,
                                                 HttpServletResponse response) {

        if (schoolId != null && instituteId != null && majorId != null && classId != null && gradeId != null) {

            List<Map<String, Object>> list = new ArrayList<>();
            List<Map<String, Object>> itemList;
            if (StringUtils.isNotBlank(stuNumber)) {
                String[] stuNumberArray = stuNumber.split(",");
                for (String number : stuNumberArray) {
                    itemList = userService.findUserListByCondition(schoolId, name, number, instituteId, majorId,
                            classId, gradeId);
                    list.addAll(itemList);
                }
            } else {
                list = userService.findUserListByCondition(schoolId, name, stuNumber, instituteId, majorId,
                        classId, gradeId);
            }

            if (groupId != 0) {
                GroupUser groupUser = new GroupUser();
                groupUser.setGroupId(groupId);
                groupUser.setIsDelete((short) 2);
                List<GroupUser> ls = groupUserService.findGroupUserList(groupUser);

                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);

                    for (int j = 0; j < ls.size(); j++) {
                        GroupUser user = ls.get(j);

                        if (Integer.parseInt(map.get("id").toString()) == user.getUserId()) {
                            list.remove(i);
                            break;
                        }
                    }
                }
            }

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findUserById")
    public void back_api_findUserById(Integer id, HttpServletResponse response) {

        if (id != null) {

            Map<String, Object> map = userService.findUserById(id);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_operateUserAccountStatus_s")
    public void back_api_operateUserAccountStatus_s(Integer userId, Short type, HttpServletResponse response) {
        if (userId != null && type != null) {

            if (type == 1 || type == 2) {
                User user = new User();
                user.setId(userId);
                user.setStatus(type);
                userService.updateUser(user);
                if (type == 2) {
                    // 用户在线信息表
                    AccountOnline aoEntity = new AccountOnline();
                    aoEntity.setAccountId(user.getId());
                    // 获取信息
                    List<AccountOnline> list = accountOnlineService.findList(aoEntity);
                    if (list != null && list.size() > 0) {
                        for (AccountOnline online : list) {
                            // 删除redis
                            RedisUtils.delKey(online.getOnlineKey());
                        }
                    }
                    // 删除存在的用户登录token记录
                    accountOnlineService.delete(aoEntity);
                }
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "参数值不正确", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_schoolIndexData")
    public void back_api_schoolIndexData(Integer schoolId, HttpServletResponse response) {
        if (schoolId != null) {

            Map<String, Object> maps = userService.querySchoolIndexPageData(schoolId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", maps));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findStudentAuthList_s")
    public void back_api_findStudentAuthList_s(Integer page, Integer pageSize, Integer schoolId, String name,
                                               Integer instituteId, Integer majorId, Integer classId, Integer gradeId, String studentNumber,
                                               HttpServletResponse response) {

        if (page != null && pageSize != null && schoolId != null && instituteId != null && majorId != null
                && classId != null && gradeId != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = userService.findStudentAuthLists(schoolId, name, instituteId, majorId,
                    classId, gradeId, studentNumber);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 教职工待审核列表
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param name
     * @param studentNumber
     * @param response
     */
    @RequestMapping("/back_api_findTeacherAuthList_s")
    public void back_api_findTeacherAuthList_s(Integer page, Integer pageSize, Integer schoolId, String name,
                                               String studentNumber, HttpServletResponse response) {

        if (page != null && pageSize != null && schoolId != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = userService.findTeacherAuthLists(schoolId, name, studentNumber);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_operateStudentAuthStatus_s")
    public void back_api_operateStudentAuthStatus_s(Integer id, Short type, String reason,
                                                    HttpServletResponse response) {
        if (id != null && type != null) {

            // pass
            if (type == 1) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(id);
                userAuth.setStatus((short) 1);

                userAuth = userService.getUserAuth(userAuth);

                if (userAuth == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证记录不存在或者已经认证", null));
                    return;
                }

                School getSchool = schoolService.getSchoolById(userAuth.getSchoolId());

                if (getSchool != null && getSchool.getIsDelete() == 1) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学校已经删除", null));
                    return;
                }

                Institute getInstitute = schoolService.getInstituteById(userAuth.getInstituteId());

                if (getInstitute != null && getInstitute.getIsDelete() == 1) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学院已经删除", null));
                    return;
                }

                Major getMajor = schoolService.getMajorById(userAuth.getMajorId());

                if (getMajor != null && getMajor.getIsDelete() == 1) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "专业已经删除", null));
                    return;
                }

                com.biu.wifi.campus.dao.model.Class getClass = schoolService.getClassById(userAuth.getClassId());

                if (getClass != null && getClass.getIsDelete() == 1) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "班级已经删除", null));
                    return;
                }

                User us = new User();
                us.setStuNumber(userAuth.getStuNumber());
                us.setSchoolId(userAuth.getSchoolId());
                us.setGradeId(userAuth.getGradeId());
                us.setInstituteId(userAuth.getInstituteId());
                us.setMajorId(userAuth.getMajorId());
                us.setClassId(userAuth.getClassId());
                us.setIsDelete((short) 2);
                us.setIsAuth((short) 1);
                us = userService.getUser(us);

                if (us != null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学号已被认证", null));
                    return;
                }

                // modify status
                userAuth.setStatus((short) 2);
                userAuth.setReason(reason);
                userAuth.setAuditTime(new Date());
                userService.updateUserAuth(userAuth);

                // update user
                User user = new User();
                user.setId(userAuth.getUserId());
                user = userService.getUser(user);

                if (user == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证用户不存在", null));
                    return;
                }

                user.setPhone(userAuth.getPhone());
                user.setName(userAuth.getName());
                user.setStuNumber(userAuth.getStuNumber());
                user.setSex(userAuth.getSex());
                user.setSchoolId(userAuth.getSchoolId());
                if (user.getIsTeacher() == 2) {
                    // 学生
                    Grade grade = new Grade();
                    grade.setId(userAuth.getGradeId());
                    grade = schoolService.getGrade(grade);
                    user.setGrade(grade.getName());
                    user.setGradeId(userAuth.getGradeId());
                    user.setInstituteId(userAuth.getInstituteId());
                    user.setMajorId(userAuth.getMajorId());
                    user.setClassId(userAuth.getClassId());
                }
                user.setIsAuth((short) 1);
                userService.updateUser(user);

                // 注册环信
                String huanxin = "";
                try {
                    huanxin = huanXinService.addImMembers(userAuth.getPhone(), "123456", userAuth.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (huanxin == "") {
                    User query = new User();
                    query.setHuanxinId(userAuth.getPhone());
                    query = userService.getUser(query);
                    if (query.getId() != null && userAuth.getUserId().intValue() != query.getId().intValue()) {
                        ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "您的手机号已经被注册", null));
                        return;
                    }
                } else {
                    // 修改环信id
                    User ushx = new User();
                    ushx.setId(userAuth.getUserId());
                    ushx.setHuanxinId(huanxin);
                    userService.updateUser(ushx);
                }

                // modify huanxin nickname
//				huanXinService.updateImMemberName(userAuth.getPhone(), userAuth.getName());

                PushUtils.addToPushTable("您提交的个人审核已通过", null, user.getDevType(), (short) 11, user.getDevToken(),
                        user.getId(), null);

                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
            } else if (type == 2) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(id);

                userAuth = userService.getUserAuth(userAuth);

                if (userAuth == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证记录不存在或者已经认证", null));
                    return;
                }

                userAuth.setStatus((short) 3);
                userAuth.setReason(reason);
                userAuth.setAuditTime(new Date());
                userService.updateUserAuth(userAuth);

                User user = new User();
                user.setId(userAuth.getUserId());
                user = userService.getUser(user);

                if (user == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证用户不存在", null));
                    return;
                }

                PushUtils.addToPushTable("您提交的个人审核失败，请重新提交", null, user.getDevType(), (short) 12, user.getDevToken(),
                        user.getId(), null);

                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "参数值不正确", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 教职工状态审核操作
     *
     * @param id
     * @param type
     * @param reason
     * @param response
     */
    @RequestMapping("/back_api_operateTeacherAuthStatus_s")
    public void back_api_operateTeacherAuthStatus_s(Integer id, Short type, String reason,
                                                    HttpServletResponse response) {
        if (id != null && type != null) {

            // pass
            if (type == 1) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(id);
                userAuth.setStatus((short) 1);

                userAuth = userService.getUserAuth(userAuth);

                if (userAuth == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证记录不存在或者已经认证", null));
                    return;
                }

                School getSchool = schoolService.getSchoolById(userAuth.getSchoolId());

                if (getSchool != null && getSchool.getIsDelete() == 1) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "学校已经删除", null));
                    return;
                }

                User us = new User();
                us.setStuNumber(userAuth.getStuNumber());
                us.setSchoolId(userAuth.getSchoolId());
                us.setIsDelete((short) 2);
                us.setIsAuth((short) 1);
                us = userService.getUser(us);

                if (us != null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "工号已被认证", null));
                    return;
                }

                // modify status
                userAuth.setStatus((short) 2);
                userAuth.setReason(reason);
                userAuth.setAuditTime(new Date());
                userService.updateUserAuth(userAuth);

                // update user
                User user = new User();
                user.setId(userAuth.getUserId());
                user = userService.getUser(user);

                if (user == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证用户不存在", null));
                    return;
                }

                user.setPhone(userAuth.getPhone());
                user.setName(userAuth.getName());
                user.setStuNumber(userAuth.getStuNumber());
                user.setSex(userAuth.getSex());
                user.setSchoolId(userAuth.getSchoolId());
                user.setIsAuth((short) 1);
                userService.updateUser(user);

                // 注册环信
                String huanxin = "";
                try {
                    huanxin = huanXinService.addImMembers(userAuth.getPhone(), "123456", userAuth.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (huanxin == "") {
                    User query = new User();
                    query.setHuanxinId(userAuth.getPhone());
                    query = userService.getUser(query);
                    if (query.getId() != null && userAuth.getUserId().intValue() != query.getId().intValue()) {
                        ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "您的手机号已经被注册", null));
                        return;
                    }
                } else {
                    // 修改环信id
                    User ushx = new User();
                    ushx.setId(userAuth.getUserId());
                    ushx.setHuanxinId(huanxin);
                    userService.updateUser(ushx);
                }

                // modify huanxin nickname
//				huanXinService.updateImMemberName(userAuth.getPhone(), userAuth.getName());

                PushUtils.addToPushTable("您提交的个人审核已通过", null, user.getDevType(), (short) 11, user.getDevToken(),
                        user.getId(), null);

                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
            } else if (type == 2) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(id);

                userAuth = userService.getUserAuth(userAuth);

                if (userAuth == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证记录不存在或者已经认证", null));
                    return;
                }

                userAuth.setStatus((short) 3);
                userAuth.setReason(reason);
                userAuth.setAuditTime(new Date());
                userService.updateUserAuth(userAuth);

                User user = new User();
                user.setId(userAuth.getUserId());
                user = userService.getUser(user);

                if (user == null) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证用户不存在", null));
                    return;
                }

                PushUtils.addToPushTable("您提交的个人审核失败，请重新提交", null, user.getDevType(), (short) 12, user.getDevToken(),
                        user.getId(), null);

                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "参数值不正确", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findPublicPageList")
    public void back_api_findPublicPageList(Integer page, Integer pageSize, Integer schoolId, String startTime,
                                            String endTime, String name, HttpServletResponse response) {

        if (page != null && pageSize != null && schoolId != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = userService.findPublicPages(schoolId, startTime, endTime, name);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findPageManager_s")
    public void back_api_findPageManager_s(Integer pageId, HttpServletResponse response) {

        if (pageId != null) {

            List<Map<String, Object>> list = userService.findPublicPageManagers(pageId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_setAsMainManager_s")
    public void back_api_setAsMainManager_s(Integer id, Short type, HttpServletResponse response) throws Exception {

        if (id != null && type != null) {

            UserPageRelationship queryRes = userService.getPublicPageUserRelationshipById(id);

            if (queryRes == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "记录不存在", null));
                return;
            }

            UserPageRelationship upr = new UserPageRelationship();
            upr.setIsDelete((short) 2);
            upr.setPageId(queryRes.getPageId());
            upr.setType((short) 1);
            List<UserPageRelationship> ls = userService.getPublicPageUserRelationship(upr);

            // 将原来的主管理员置为一般管理员
            if (ls.size() == 1) {
                UserPageRelationship updateUpr = new UserPageRelationship();
                updateUpr.setId(ls.get(0).getId());
                updateUpr.setType((short) 2);
                userService.updatePublicPageUserRelationship(updateUpr);
            }

            UserPageRelationship userPageRelationship = new UserPageRelationship();
            userPageRelationship.setId(id);
            userPageRelationship.setType((short) 1);
            userService.updatePublicPageUserRelationship(userPageRelationship);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deletePageManager_s")
    public void back_api_deletePageManager_s(Integer id, HttpServletResponse response) {

        if (id != null) {
            UserPageRelationship userPageRelationship = new UserPageRelationship();
            userPageRelationship.setId(id);
            userPageRelationship.setIsDelete((short) 1);
            userPageRelationship.setDeleteTime(new Date());
            userService.updatePublicPageUserRelationship(userPageRelationship);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findPageAuthList_s")
    public void back_api_findPageAuthList_s(Integer page, Integer pageSize, Integer schoolId,
                                            HttpServletResponse response) {

        if (page != null && pageSize != null && schoolId != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = userService.findPublicPageAuthLists(schoolId);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_operatePageAuthStatus_s")
    public void back_api_operatePageAuthStatus_s(Integer id, Short type, String reason, HttpServletResponse response) {
        if (id != null && type != null) {

            PageApply pageApply = new PageApply();
            pageApply.setId(id);
            pageApply.setStatus((short) 1);
            pageApply = userService.getPageApply(pageApply);

            if (pageApply == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "认证记录不存在或者已经审核", null));
                return;
            }

            // pass
            if (type == 1) {
                UserPageRelationship uspr = new UserPageRelationship();
                uspr.setIsDelete((short) 2);
                uspr.setUserId(pageApply.getApplicantId());
                List<UserPageRelationship> ls = userService.getPublicPageUserRelationship(uspr);

                if (ls.size() > 0) {
                    ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "该用户已经关联公共主页，无法再进行关联", null));
                    return;
                }

                // modify status
                pageApply.setStatus((short) 2);
                pageApply.setReason(reason);
                pageApply.setAuditTime(new Date());
                userService.updatePageApply(pageApply);

                // add page
                User user = new User();
                user.setSchoolId(pageApply.getSchoolId());
                user.setCreatorId(pageApply.getApplicantId());
                user.setName(pageApply.getName());
                user.setIntroduction(pageApply.getIntroduction());
                user.setHeadimg(pageApply.getHeadimg());
                user.setCreateTime(pageApply.getCreateTime());
                user.setType((short) 2);
                user.setIsDelete((short) 2);
                user.setStatus((short) 1);
                user.setIsAuth((short) 1);
                userService.addPublicPage(user);

                // add relationship
                UserPageRelationship userPageRelationship = new UserPageRelationship();
                userPageRelationship.setCreateTime(new Date());
                userPageRelationship.setIsDelete((short) 2);
                userPageRelationship.setPageId(user.getId());
                userPageRelationship.setType((short) 1);
                userPageRelationship.setUserId(pageApply.getApplicantId());
                userService.addUserPageRelationship(userPageRelationship);

                // push
                User applicant = new User();
                applicant.setId(pageApply.getApplicantId());
                applicant = userService.getUser(applicant);

                if (applicant != null) {
                    PushUtils.addToPushTable("您提交的公共主页审核已通过", null, applicant.getDevType(), (short) 10,
                            applicant.getDevToken(), applicant.getId(), null);
                }

                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
            } else if (type == 2) {
                pageApply.setStatus((short) 3);
                pageApply.setReason(reason);
                pageApply.setAuditTime(new Date());
                userService.updatePageApply(pageApply);

                User applicant = new User();
                applicant.setId(pageApply.getApplicantId());
                applicant = userService.getUser(applicant);

                if (applicant != null) {
                    PushUtils.addToPushTable("您提交的公共主页审核失败，请重新提交", null, applicant.getDevType(), (short) 9,
                            applicant.getDevToken(), applicant.getId(), null);
                }

                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "参数值不正确", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deletePage")
    public void back_api_deletePage(Integer id, HttpServletResponse response) {
        if (id != null) {
            User page = new User();
            page.setId(id);
            page.setIsDelete((short) 1);
            page.setDeleteTime(new Date());
            userService.updateUser(page);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteUser")
    public void back_api_deleteUser(Integer id, HttpServletResponse response) {
        if (id != null) {
            User user = new User();
            user.setId(id);

            User getUser = userService.getUser(user);

            if (getUser == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "用户不存在", null));
                return;
            }

            user.setIsDelete((short) 1);
            user.setDeleteTime(new Date());
            userService.updateUser(user);

            huanXinService.delImMembers(getUser.getPhone());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 根据用户ID集合查询用户姓名字符串
     *
     * @param userIdList
     * @param response
     */
    @RequestMapping("back_api_findUserNameListByUserIdList")
    public void back_api_findUserNameListByUserIdList(String userIdList, HttpServletResponse response) {
        if (userIdList == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }
        List<String> ids = Arrays.asList(userIdList.split(","));
        List<Integer> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(Integer.valueOf(id));
        }
        List<User> users = userService.findUserNameListByUserIdList(idList);
        StringBuffer sb = new StringBuffer();
        int total = users.size();
        // 最多只显示10个名字
        boolean flag = false;
        if (total > 10) {
            flag = true;
            total = 10;
        }
        for (int i = 0; i < total; i++) {
            String name = users.get(i).getName();
            sb.append(name);
            if (i != total - 1) {
                sb.append(",");
            } else {
                if (flag) {
                    sb.append("...");
                }
            }
        }
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", sb.toString()));
    }
}
