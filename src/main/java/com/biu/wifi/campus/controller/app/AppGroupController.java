package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.Tool.RandomUtil;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.dao.model.Class;
import com.biu.wifi.campus.dao.model.GroupMessageCriteria.Criteria;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AppGroupController extends AuthenticatorController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private InstituteService instituteService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private ClassService classService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMessageService groupMessageService;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private PushSerivce pushSerivce;

    @Autowired
    private NoticeReceiveService noticeReceiveService;

    /**
     * 选择@的人列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param search
     * @param school_id 废弃，不要 ps:默认展示粉丝列表，搜索是搜索全部学生
     */
    @RequestMapping("/user_selectAtUserList")
    public void user_selectAtUserList(HttpServletRequest request, HttpServletResponse response,
                                      @ModelAttribute("user_id") Integer user_id, String search, Integer school_id) {
        List<Map<String, Object>> list = new ArrayList<>();

        // 如果是默认
        if (StringUtils.isBlank(search)) {
            // 选择@的人----获取粉丝列表
            list = followService.user_selectAtUserList(user_id);

            // 默认列表中也需要添加公共主页账号
            UserCriteria example = new UserCriteria();
            UserCriteria.Criteria criteria = example.createCriteria();
            criteria.andIsAuthEqualTo((short) 1).andStatusEqualTo((short) 1).andIsDeleteEqualTo((short) 2)
                    .andIdNotEqualTo(user_id).andTypeEqualTo((short) 2);
            if (school_id != null) {
                criteria.andSchoolIdEqualTo(school_id);
            }
            if (StringUtils.isNotBlank(search)) {
                criteria.andNameLike("%" + search + "%");
            }
            List<User> userList = userService.findList(example);
            for (User user : userList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("headimg", user.getHeadimg());
                map.put("name", user.getName());
                list.add(map);
            }
        } else {
            /*
             * if(school_id == null){ String strToMoblieJson =
             * JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空",null));
             * ServletUtilsEx.renderText(response, strToMoblieJson); return; }
             */
            // 选择@的人----获取全校用户+公共主页账号
            list = userService.user_selectAtUserListForSchool(user_id, search, school_id);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 获取年级列表
     *
     * @param request
     * @param response
     */
    @RequestMapping("/app_findGradeList")
    public void app_findGradeList(HttpServletRequest request, HttpServletResponse response) {
        Grade entity = new Grade();
        entity.setIsDelete((short) 2);
        List<Grade> list = gradeService.findList(entity);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 根据学校获取学院列表
     *
     * @param request
     * @param response
     * @param school_id
     */
    @RequestMapping("/app_findInstituteList")
    public void app_findInstituteList(HttpServletRequest request, HttpServletResponse response, Integer school_id,
                                      @ModelAttribute("version") String version) {
        if (school_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Institute entity = new Institute();
        entity.setSchoolId(school_id);
        entity.setIsDelete((short) 2);
        List<Institute> list = instituteService.findList(entity);

        // 1.2版本以上在学院列表最后添加教职工栏位
        if (convertVersionToDouble(version) >= 1.2) {
            Institute institute = new Institute();
            institute.setId(-1);
            institute.setName("教职工");
            institute.setIsDelete((short) 2);
            list.add(institute);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 根据学院获取专业列表
     *
     * @param request
     * @param response
     * @param institute_id 学院id
     * @param grade_id     年级id
     * @param group_id     群组id
     */
    @RequestMapping("/app_findMajorList")
    public void app_findMajorList(HttpServletRequest request, HttpServletResponse response, Integer institute_id,
                                  Integer grade_id, Integer group_id, @ModelAttribute("version") String version) {
        if (institute_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Major> list;
        if (institute_id == -1 && convertVersionToDouble(version) >= 1.2) {
            // 1.2版本以上显示教职工列表
            list = new ArrayList<>();
            UserCriteria example = new UserCriteria();
            UserCriteria.Criteria criteria = example.createCriteria();
            criteria.andTypeEqualTo((short) 1);// 用户
            criteria.andIsDeleteEqualTo((short) 2);// 未删除
            criteria.andIsAuthEqualTo((short) 1);// 认证通过
            criteria.andStatusEqualTo((short) 1);// 正常
            criteria.andIsTeacherEqualTo((short) 1);// 教职工
            List<User> users = userService.findList(example);
            for (User user : users) {
                Major major = new Major();
                major.setId(user.getId());
                major.setName(user.getName());
                major.setIsDelete(user.getIsDelete());
                major.setInstituteId(-5);
                if (StringUtils.isNotBlank(user.getHeadimg())) {
                    major.setCode(user.getHeadimg());
                } else {
                    major.setCode("has no headimg");
                }
                list.add(major);
            }
        } else {
            // 获取专业列表
            Major entity = new Major();
            entity.setInstituteId(institute_id);
            entity.setIsDelete((short) 2);
            list = majorService.findList(entity);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);

        if (group_id != null && grade_id != null) {
            // 获取未加入群的学生列表
            List<Map<String, Object>> stuList = userService.user_findStudentList(null, null, group_id, institute_id,
                    grade_id, null);

            map.put("stuList", stuList);
            map.put("stuNum", stuList.size());
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 根据年级和专业获取班级列表
     *
     * @param request
     * @param response
     * @param grade_id 年级id
     * @param major_id 专业id
     * @param group_id 群组id
     */
    @RequestMapping("/app_findClassList")
    public void app_findClassList(HttpServletRequest request, HttpServletResponse response, Integer grade_id,
                                  Integer major_id, Integer group_id) {
        if (grade_id == null || major_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取班级列表
        Class entity = new Class();
        entity.setMajorId(major_id);
        entity.setGradeId(grade_id);
        entity.setIsDelete((short) 2);
        List<Class> list = classService.findList(entity);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);

        if (group_id != null) {
            // 获取未加入群的学生列表
            List<Map<String, Object>> stuList = userService.user_findStudentList(null, null, group_id, null, grade_id,
                    major_id);

            map.put("stuList", stuList);
            map.put("stuNum", stuList.size());
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 根据班级获取学生信息
     *
     * @param request
     * @param response
     * @param user_id
     * @param class_id
     * @param group_id
     */
    @RequestMapping("/user_findStudentListByTwo")
    public void user_findStudentListByTwo(HttpServletRequest request, HttpServletResponse response,
                                          @ModelAttribute("user_id") Integer user_id, Integer class_id, Integer group_id) {
        if (class_id == null && group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Map<String, Object>> list = userService.user_findStudentList(user_id, class_id, group_id, null, null,
                null);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("stuNum", list.size());

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 搜索校信信息
     *
     * @param request
     * @param response
     * @param user_id
     * @param search
     */
    @RequestMapping("/user_findUserAndGroupList")
    public void user_findUserAndGroupList(HttpServletRequest request, HttpServletResponse response,
                                          @ModelAttribute("user_id") Integer user_id, String search) {
        if (StringUtils.isBlank(search)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 校信搜索联系人列表(私信过的人)
        List<Map<String, Object>> chatList = chatService.findChatUserFromXiaoXin(user_id, search);

        // 校信搜索群组列表
        List<Map<String, Object>> groupList = groupService.findGroupUserFromXiaoXin(user_id, search);

        Map<String, Object> map = new HashMap<>();
        map.put("userList", chatList);
        map.put("groupList", groupList);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取关注人私信列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/user_findFocusChatList")
    public void user_findFocusChatList(HttpServletRequest request, HttpServletResponse response,
                                       @ModelAttribute("user_id") Integer user_id, Integer page, Integer pageSize, String time) {
        if (page == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = chatService.user_findChatList(user_id, (short) 1, time);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("time", time);

        // 获取最新一条未关注人私信
        List<Map<String, Object>> noFocuslist = chatService.user_findChatList(user_id, (short) 2, time);
        if (noFocuslist == null || noFocuslist.size() == 0) {
            map.put("user_id", 0);
            map.put("headimg", "");
            map.put("name", "");
            map.put("huanxin_id", "");
        } else {
            map.put("user_id", noFocuslist.get(0).get("user_id"));
            map.put("headimg", noFocuslist.get(0).get("headimg"));
            map.put("name", noFocuslist.get(0).get("name"));
            map.put("huanxin_id", noFocuslist.get(0).get("huanxin_id"));
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取未关注人私信列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/user_findNoFocusChatList")
    public void user_findNoFocusChatList(HttpServletRequest request, HttpServletResponse response,
                                         @ModelAttribute("user_id") Integer user_id, Integer page, Integer pageSize, String time) {
        if (page == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = chatService.user_findChatList(user_id, (short) 2, time);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("time", time);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取群组列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/user_findGroupList")
    public void user_findGroupList(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("user_id") Integer user_id, Integer page, Integer pageSize, String time) {
        if (page == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = new HashMap<>();

        // 未读群组消息数
        GroupMessageCriteria gc = new GroupMessageCriteria();
        Criteria cc = gc.createCriteria();
        cc.andReceiverIdEqualTo(user_id);
        cc.andIsReadEqualTo((short) 2);
        cc.andIsDeteleEqualTo((short) 2);
        long notReadMsgNum = groupMessageService.findCount(gc);
        map.put("notReadMsgNum", notReadMsgNum);

        // 获取最新一条消息
        GroupMessage groupMessage2 = new GroupMessage();
        groupMessage2.setReceiverId(user_id);
        groupMessage2.setIsDetele((short) 2);
        List<GroupMessage> msgList = groupMessageService.findList(groupMessage2);

        if (msgList != null && msgList.size() > 0) {
            GroupMessage groupMessage = msgList.get(0);

            // 最后一条消息时间
            map.put("lastMsgTime", groupMessage.getCreateTime());

            // 类型1申请加入2邀请加入3退群4踢出5设为管理员 6取消管理员 7解散群组
            short type = groupMessage.getType().shortValue();
            // 发起人
            String user = groupMessage.getUser();
            // 获取群组信息
            Group group = groupService.getById(groupMessage.getGroupId());

            if (type == 1) {
                map.put("lastMsgContent", user + " 申请加入 " + group.getName());
            } else if (type == 2) {
                map.put("lastMsgContent", user + " 邀请你加入 " + group.getName());
            } else if (type == 3) {
                map.put("lastMsgContent", user + " 退出 " + group.getName());
            } else if (type == 4) {
                map.put("lastMsgContent", group.getName() + " 你被管理员移出了群组");
            } else if (type == 5) {
                map.put("lastMsgContent", group.getName() + " 你成为了群组管理员");
            } else if (type == 6) {
                map.put("lastMsgContent", group.getName() + " 你被取消了群组管理员身份");
            } else if (type == 7) {
                map.put("lastMsgContent", group.getName() + " 群组已解散");
            }
        } else {
            map.put("lastMsgTime", null);
            map.put("lastMsgContent", null);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        // 获取群组列表
        List<Map<String, Object>> list = groupService.user_findGroupList(user_id, time);
        map.put("list", list);
        map.put("time", time);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取群资料信息
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id
     */
    @RequestMapping("/user_getGroupInfo")
    public void user_getGroupInfo(HttpServletRequest request, HttpServletResponse response,
                                  @ModelAttribute("user_id") Integer user_id, Integer group_id) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = new HashMap<>();

        // 获取群信息
        Group group = groupService.getById(group_id);
        map.put("name", group.getName());
        map.put("headimg", group.getHeadimg());
        map.put("memberNum", group.getMemberCount());
        map.put("number", group.getNumber());

        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null) {
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您已经不是群成员了",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
            map.put("status", 2);
            map.put("is_notify", null);
            map.put("type", null);
        } else {
            map.put("status", 1);
            map.put("is_notify", groupUser.getIsNotify());
            map.put("type", groupUser.getType());
        }

        // 获取前9位加入的成员列表
        List<Map<String, Object>> list = groupUserService.findNineMemberList(group_id);
        map.put("list", list);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取群成员列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id
     * @param search
     */
    @RequestMapping("/user_findGroupMemberList")
    public void user_findGroupMemberList(HttpServletRequest request, HttpServletResponse response,
                                         @ModelAttribute("user_id") Integer user_id, Integer group_id, String search) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Map<String, Object>> list = groupUserService.user_findGroupMemberList(group_id, search);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 设置群管理员
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id 群组id
     * @param user_ids 成员id，多个逗号相隔
     * @param type     1设置管理员 2取消管理员
     */
    @RequestMapping("/user_editGroupManage")
    public void user_editGroupManage(HttpServletRequest request, HttpServletResponse response,
                                     @ModelAttribute("user_id") Integer user_id, Integer group_id, String user_ids, Short type) {
        if (group_id == null || StringUtils.isBlank(user_ids) || type == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取本人在群组的角色
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() != 1) {
            String strToMoblieJson = JsonUtilEx
                    .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您不是群主，无权做此操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        String[] userIds = user_ids.split(",");
        // 获取群组信息
//		Group group = groupService.getById(group_id);

        // 设置管理员
        if (type.shortValue() == 1) {
            // 获取现有的管理员数量
            GroupUserCriteria gc = new GroupUserCriteria();
            GroupUserCriteria.Criteria cc = gc.createCriteria();
            cc.andGroupIdEqualTo(group_id);
            cc.andTypeEqualTo((short) 2);
            cc.andIsDeleteEqualTo((short) 2);
            int count = groupUserService.findCount(gc);
            if (count == 4) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "管理员数量已满", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            if ((count + userIds.length) > 4) {
                String strToMoblieJson = JsonUtilEx
                        .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "管理员人数超出,请重新选择", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            for (String uId : userIds) {
                Integer receiverId = Integer.valueOf(uId);
                // 获取成员信息
                User recceiver = userService.getById(receiverId);

                // 获取成员群组信息
                GroupUser gu = new GroupUser();
                gu.setGroupId(group_id);
                gu.setUserId(receiverId);
                gu.setIsDelete((short) 2);
                gu = groupUserService.getGroupUser(gu);

                // 如果是普通成员
                if (gu.getType().shortValue() == 3) {
                    // 修改为管理员
                    GroupUser gUser = new GroupUser();
                    gUser.setId(gu.getId());
                    gUser.setType((short) 2);
                    gUser.setBecomeAdminTime(new Date());
                    groupUserService.update(gUser);

                    // 群通知表
                    GroupMessage groupMessage = new GroupMessage();
                    groupMessage.setGroupId(group_id);
                    groupMessage.setType((short) 5);
                    groupMessage.setUserId(user_id);
                    groupMessage.setUser(user.getName());
                    groupMessage.setReceiverId(receiverId);
                    groupMessage.setRecceiver(recceiver.getName());
                    groupMessage.setCreateTime(new Date());
                    groupMessageService.insert(groupMessage);

//					//推送
//					//是否消息免打扰 1是2否
//					if(gu.getIsNotify().shortValue() == 2){
//						Boolean flag = false;
//						HashMap<String,Object> hm = new HashMap<String,Object>();
////						hm.put("id", saying.getId());
//						hm.put("title", "你被设置为 "+group.getName()+" 的管理员");
//						try {
//							flag = PushTool.pushToUser(receiverId, "", "你被设置为 "+group.getName()+" 的管理员", hm);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						//入推送表
//						Push puEntity = new Push();
//						puEntity.setToken(recceiver.getDevToken());
//						puEntity.setContent("");
//						puEntity.setUserId(receiverId);
//						puEntity.setMessageType((short)4);
//						puEntity.setDeviceType(recceiver.getDevType());
//						puEntity.setTitle("你被设置为 "+group.getName()+" 的管理员");
////						puEntity.setObjectId(saying.getId());
//						if(flag){
//							puEntity.setType((short)10);
//						}else{
//							puEntity.setType((short)50);
//						}
//						
//						pushSerivce.addPush(puEntity);
//					}
                }
            }
        } else {
            for (String uId : userIds) {
                Integer receiverId = Integer.valueOf(uId);
                // 获取成员信息
                User recceiver = userService.getById(receiverId);

                // 获取成员群组信息
                GroupUser gu = new GroupUser();
                gu.setGroupId(group_id);
                gu.setUserId(Integer.valueOf(uId));
                gu.setIsDelete((short) 2);
                gu = groupUserService.getGroupUser(gu);

                // 如果是管理员
                if (gu.getType().shortValue() == 2) {
                    // 修改为普通成员
                    GroupUser gUser = new GroupUser();
                    gUser.setId(gu.getId());
                    gUser.setType((short) 3);
                    groupUserService.update(gUser);

                    // 群通知表
                    GroupMessage groupMessage = new GroupMessage();
                    groupMessage.setGroupId(group_id);
                    groupMessage.setType((short) 6);
                    groupMessage.setUserId(user_id);
                    groupMessage.setUser(user.getName());
                    groupMessage.setReceiverId(receiverId);
                    groupMessage.setRecceiver(recceiver.getName());
                    groupMessage.setCreateTime(new Date());
                    groupMessageService.insert(groupMessage);

//					//推送
//					//是否消息免打扰 1是2否
//					if(gu.getIsNotify().shortValue() == 2){
//						Boolean flag = false;
//						HashMap<String,Object> hm = new HashMap<String,Object>();
////						hm.put("id", saying.getId());
//						hm.put("title", "你被移除了管理员身份 "+group.getName());
//						try {
//							flag = PushTool.pushToUser(receiverId, "", "你被移除了管理员身份 "+group.getName(), hm);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						//入推送表
//						Push puEntity = new Push();
//						puEntity.setToken(recceiver.getDevToken());
//						puEntity.setContent("");
//						puEntity.setUserId(receiverId);
//						puEntity.setMessageType((short)4);
//						puEntity.setDeviceType(recceiver.getDevType());
//						puEntity.setTitle("你被移除了管理员身份 "+group.getName());
////						puEntity.setObjectId(saying.getId());
//						if(flag){
//							puEntity.setType((short)10);
//						}else{
//							puEntity.setType((short)50);
//						}
//						
//						pushSerivce.addPush(puEntity);
//					}
                }
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 搜索全部实名认证用户
     *
     * @param request
     * @param response
     * @param user_id
     * @param school_id 学校id
     * @param group_id  群组id
     * @param search    搜索内容
     */
    @RequestMapping("/user_findStudentBySchool")
    public void user_findStudentBySchool(HttpServletRequest request, HttpServletResponse response,
                                         @ModelAttribute("user_id") Integer user_id, Integer group_id, String search) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Map<String, Object>> list = userService.user_findStudentBySchool(user_id, group_id, search);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 添加群成员
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id 群组id
     * @param user_ids 用户id，多个逗号相隔
     */
    @RequestMapping("/user_addGroupMember")
    public void user_addGroupMember(HttpServletRequest request, HttpServletResponse response,
                                    @ModelAttribute("user_id") Integer user_id, Integer group_id, String user_ids) {
        if (group_id == null || StringUtils.isBlank(user_ids)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        String[] userIds = user_ids.split(",");
        // 获取群组信息
        Group group = groupService.getById(group_id);

        for (String uId : userIds) {
            Integer receiverId = Integer.valueOf(uId);
            // 获取成员信息
            User recceiver = userService.getById(receiverId);

            // 获取成员群组信息
            GroupUser gu = new GroupUser();
            gu.setGroupId(group_id);
            gu.setUserId(Integer.valueOf(uId));
            gu.setIsDelete((short) 2);
            gu = groupUserService.getGroupUser(gu);

            // 如果不存在
            if (gu == null) {
                boolean insertFlag = groupMessageService.allowedSendGroupInviteMsg(Integer.valueOf(uId), group_id);
                if (!insertFlag) {
                    //已经被邀请的群成员，还没被确认，不能重复发送通知
                    continue;
                }

                // 群通知表
                GroupMessage groupMessage = new GroupMessage();
                groupMessage.setGroupId(group_id);
                groupMessage.setType((short) 2);
                groupMessage.setUserId(user_id);
                groupMessage.setUser(user.getName());
                groupMessage.setReceiverId(receiverId);
                groupMessage.setRecceiver(recceiver.getName());
                groupMessage.setCreateTime(new Date());
                groupMessage.setStatus((short) 1);
                groupMessageService.insert(groupMessage);

                // 推送
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
//				hm.put("id", saying.getId());
                hm.put("title", user.getName() + " 邀请您加入群组 " + group.getName());
                hm.put("type", 4);
                try {
                    flag = PushTool.pushToUser(receiverId, "", user.getName() + " 邀请您加入群组 " + group.getName(), hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 入推送表
                Push puEntity = new Push();
                puEntity.setToken(recceiver.getDevToken());
                puEntity.setContent("");
                puEntity.setUserId(receiverId);
                puEntity.setMessageType((short) 4);
                puEntity.setDeviceType(recceiver.getDevType());
                puEntity.setTitle(user.getName() + " 邀请您加入群组 " + group.getName());
//				puEntity.setObjectId(saying.getId());
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }

                pushSerivce.addPush(puEntity);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 申请加群
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id
     */
    @RequestMapping("/user_applyAddGroup")
    public void user_applyAddGroup(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("user_id") Integer user_id, Integer group_id) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取申请人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取成员群组信息
        GroupUser gu = new GroupUser();
        gu.setGroupId(group_id);
        gu.setUserId(user_id);
        gu.setIsDelete((short) 2);
        gu = groupUserService.getGroupUser(gu);
        if (gu != null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您已经加入此群了", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 判断是否发过申请
        GroupMessage gm = new GroupMessage();
        gm.setGroupId(group_id);
        gm.setType((short) 1);
        gm.setUserId(user_id);
        gm.setStatus((short) 1);
        gm.setIsDetele((short) 2);
        List<GroupMessage> groupMessagesList = groupMessageService.findList(gm);
        if (groupMessagesList != null && groupMessagesList.size() > 0) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您已经申请过了", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取群管理员列表
        List<Map<String, Object>> groupManagerList = groupUserService.app_findShareGroupUserList(group_id);
        // 批量新增群通知消息
        groupMessageService.insertAllApplyAdd(group_id, user_id, user.getName(), groupManagerList);

        // 获取群组信息
        Group group = groupService.getById(group_id);

        // 推送
        for (Map<String, Object> map : groupManagerList) {
            // 是否消息免打扰 1是2否
            short is_notify = Short.parseShort(map.get("is_notify").toString());
            // 消息接收人id
            Integer toerId = Integer.valueOf(map.get("user_id").toString());
            // 消息接收人信息
            User toer = userService.getById(toerId);

            if (is_notify == 2) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
//				hm.put("id", saying.getId());
                hm.put("title", user.getName() + " 请求加入群组 " + group.getName());
                hm.put("type", 4);
                try {
                    flag = PushTool.pushToUser(toerId, "", user.getName() + " 请求加入群组 " + group.getName(), hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 入推送表
                Push puEntity = new Push();
                puEntity.setToken(toer.getDevToken());
                puEntity.setContent("");
                puEntity.setUserId(toerId);
                puEntity.setMessageType((short) 4);
                puEntity.setDeviceType(toer.getDevType());
                puEntity.setTitle(user.getName() + " 请求加入群组 " + group.getName());
//				puEntity.setObjectId(saying.getId());
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }

                pushSerivce.addPush(puEntity);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除群成员
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id 群组id
     * @param user_ids 成员id，多个逗号相隔
     */
    @RequestMapping("/user_deleteGroupMember")
    public void user_deleteGroupMember(HttpServletRequest request, HttpServletResponse response,
                                       @ModelAttribute("user_id") Integer user_id, Integer group_id, String user_ids) {
        if (group_id == null || StringUtils.isBlank(user_ids)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取本人在群组的角色
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() == 3) {
            String strToMoblieJson = JsonUtilEx
                    .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您不是管理员，无权做此操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        String[] userIds = user_ids.split(",");
        // 获取群组信息
        Group group = groupService.getById(group_id);

        for (String uId : userIds) {
            // 获取成员信息
            GroupUser gu = new GroupUser();
            gu.setGroupId(group_id);
            gu.setUserId(Integer.valueOf(uId));
            gu.setIsDelete((short) 2);
            gu = groupUserService.getGroupUser(gu);

            // 如果存在
            if (gu != null) {
                // 获取用户信息
                User recceiver = userService.getById(Integer.valueOf(uId));

                // 删除成员
                GroupUser gUser = new GroupUser();
                gUser.setId(gu.getId());
                gUser.setIsDelete((short) 1);
                gUser.setDeleteTime(new Date());
                groupUserService.update(gUser);

                // 修改群组人数-1
                Group gp = new Group();
                gp.setId(group_id);
                gp.setMemberCount(group.getMemberCount().intValue() - 1);
                groupService.update(gp);

                // 新增群通知表
                GroupMessage groupMessage = new GroupMessage();
                groupMessage.setGroupId(group_id);
                groupMessage.setType((short) 4);
                groupMessage.setUserId(user_id);
                groupMessage.setUser(user.getName());
                groupMessage.setReceiverId(Integer.valueOf(uId));
                groupMessage.setRecceiver(recceiver.getName());
                groupMessage.setCreateTime(new Date());
                groupMessageService.insert(groupMessage);

                // 删除用户通知收到表相关群组信息
                noticeReceiveService.deleteAllByUser(Integer.valueOf(uId), group_id);

//				//推送
//				//是否消息免打扰 1是2否
//				if(gu.getIsNotify().shortValue() == 2){
//					Boolean flag = false;
//					HashMap<String,Object> hm = new HashMap<String,Object>();
////					hm.put("id", saying.getId());
//					hm.put("title", "你被踢出了群组 "+group.getName());
//					try {
//						flag = PushTool.pushToUser(Integer.valueOf(uId), "", "你被踢出了群组 "+group.getName(), hm);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					//入推送表
//					Push puEntity = new Push();
//					puEntity.setToken(recceiver.getDevToken());
//					puEntity.setContent("");
//					puEntity.setUserId(Integer.valueOf(uId));
//					puEntity.setMessageType((short)4);
//					puEntity.setDeviceType(recceiver.getDevType());
//					puEntity.setTitle("你被踢出了群组 "+group.getName());
////					puEntity.setObjectId(saying.getId());
//					if(flag){
//						puEntity.setType((short)10);
//					}else{
//						puEntity.setType((short)50);
//					}
//					
//					pushSerivce.addPush(puEntity);
//				}
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 修改群资料
     *
     * @param request
     * @param response
     * @param user_id
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/user_updateGroup")
    public void user_updateGroup(HttpServletRequest request, HttpServletResponse response,
                                 @ModelAttribute("user_id") Integer user_id) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        // 群组id
        Integer group_id = null;
        try {
            group_id = Integer.valueOf(param.get("group_id").toString());
        } catch (Exception e) {
            group_id = null;
        }
        // 群名称
        String name = null;
        try {
            name = param.get("name").toString();
        } catch (Exception e) {
            name = null;
        }
        // 群头像
        List<DiskFileItem> headimg = null;
        try {
            headimg = (List<DiskFileItem>) param.get("headimg");
        } catch (Exception e) {
            headimg = null;
        }

        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取本人在群组的角色
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() == 3) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已删除或无权做此操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Group group = new Group();
        group.setId(group_id);
        if (StringUtils.isNotBlank(name)) {
            group.setName(name);
        }
        if (headimg != null && headimg.size() > 0) {
            DiskFileItem fileBean = headimg.get(0);
            byte[] fileContent = fileBean.get();
            String fileName = FileUtilsEx.getFileNameByPath(fileBean.getName());
            String fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
            group.setHeadimg(fileid);
        }
        group.setModifyTime(new Date());
        groupService.update(group);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 退出群组
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id
     */
    @RequestMapping("/user_outGroup")
    public void user_outGroup(HttpServletRequest request, HttpServletResponse response,
                              @ModelAttribute("user_id") Integer user_id, Integer group_id) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取本人在群组的角色
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已退出或无权做此操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		//获取群组信息
        Group group = groupService.getById(group_id);

        // 删除成员
        GroupUser gUser = new GroupUser();
        gUser.setId(groupUser.getId());
        gUser.setIsDelete((short) 1);
        gUser.setDeleteTime(new Date());
        groupUserService.update(gUser);

        // 修改群组人数-1
        Group gp = new Group();
        gp.setId(group_id);
        gp.setMemberCount(group.getMemberCount().intValue() - 1);
        groupService.update(gp);

        // 获取群管理员列表
        List<Map<String, Object>> groupManagerList = groupUserService.app_findShareGroupUserList(group_id);
        // 批量新增群通知消息
        groupMessageService.insertAllOutGroup(group_id, user_id, user.getName(), groupManagerList);

        // 删除用户通知收到表相关群组信息
        noticeReceiveService.deleteAllByUser(user_id, group_id);

//		
//		//推送
//		for(Map<String, Object> map : groupManagerList){
//			//是否消息免打扰 1是2否
//			short is_notify = Short.parseShort(map.get("is_notify").toString());
//			//消息接收人id
//			Integer toerId = Integer.valueOf(map.get("user_id").toString());
//			//消息接收人信息
//			User toer = userService.getById(toerId);
//			
//			if(is_notify == 2){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
//				hm.put("title", user.getName()+" 退出了群组 "+group.getName());
//				try {
//					flag = PushTool.pushToUser(toerId, "", user.getName()+" 退出了群组 "+group.getName(), hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//入推送表
//				Push puEntity = new Push();
//				puEntity.setToken(toer.getDevToken());
//				puEntity.setContent("");
//				puEntity.setUserId(toerId);
//				puEntity.setMessageType((short)4);
//				puEntity.setDeviceType(toer.getDevType());
//				puEntity.setTitle(user.getName()+" 退出了群组 "+group.getName());
//				if(flag){
//					puEntity.setType((short)10);
//				}else{
//					puEntity.setType((short)50);
//				}
//				
//				pushSerivce.addPush(puEntity);
//			}
//		}

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 解散群组
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id
     */
    @RequestMapping("/user_deleteGroup")
    public void user_deleteGroup(HttpServletRequest request, HttpServletResponse response,
                                 @ModelAttribute("user_id") Integer user_id, Integer group_id) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取群组信息
        Group group = groupService.getById(group_id);
        if (group.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "群组已解散", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取本人在群组的角色
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() != 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不是群主，无权操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 删除群组
        Group group2 = new Group();
        group2.setId(group_id);
        group2.setIsDelete((short) 1);
        group2.setDeleteTime(new Date());
        groupService.update(group2);

        // 获取群成员列表
        List<Map<String, Object>> groupUserList = groupUserService.user_findGroupMemberList(group_id, null);
        // 批量新增群通知消息
        groupMessageService.insertAllDeleteGroup(group_id, user_id, user.getName(), groupUserList);

        // 删除用户通知收到表相关群组信息
        noticeReceiveService.deleteAllByUser(null, group_id);

//		//推送
//		for(Map<String, Object> map : groupUserList){
//			//是否消息免打扰 1是2否
//			short is_notify = Short.parseShort(map.get("is_notify").toString());
//			//消息接收人id
//			Integer toerId = Integer.valueOf(map.get("user_id").toString());
//			//消息接收人信息
//			User toer = userService.getById(toerId);
//			
//			if(is_notify == 2){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
////				hm.put("id", saying.getId());
//				hm.put("title", "群组已解散 "+group.getName());
//				try {
//					flag = PushTool.pushToUser(toerId, "", "群组已解散 "+group.getName(), hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//入推送表
//				Push puEntity = new Push();
//				puEntity.setToken(toer.getDevToken());
//				puEntity.setContent("");
//				puEntity.setUserId(toerId);
//				puEntity.setMessageType((short)4);
//				puEntity.setDeviceType(toer.getDevType());
//				puEntity.setTitle("群组已解散 "+group.getName());
////				puEntity.setObjectId(saying.getId());
//				if(flag){
//					puEntity.setType((short)10);
//				}else{
//					puEntity.setType((short)50);
//				}
//				
//				pushSerivce.addPush(puEntity);
//			}
//		}

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 设置群内消息免打扰
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id
     * @param is_notify 是否消息免打扰 1是2否
     */
    @RequestMapping("/user_editGroup")
    public void user_editGroup(HttpServletRequest request, HttpServletResponse response,
                               @ModelAttribute("user_id") Integer user_id, Integer group_id, Short is_notify) {
        if (group_id == null || is_notify == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取本人在群的信息
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您不是群成员，不能操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        GroupUser gu = new GroupUser();
        gu.setId(groupUser.getId());
        gu.setIsNotify(is_notify);
        groupUserService.update(gu);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 创建群组
     *
     * @param request
     * @param response
     * @param user_id
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/user_addGroup")
    public void user_addGroup(HttpServletRequest request, HttpServletResponse response,
                              @ModelAttribute("user_id") Integer user_id) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        // 学校
        Integer school_id = null;
        try {
            school_id = Integer.valueOf(param.get("school_id").toString());
        } catch (Exception e) {
            school_id = null;
        }
        // 群名称
        String name = null;
        try {
            name = param.get("name").toString();
        } catch (Exception e) {
            name = null;
        }
        // 群头像
        List<DiskFileItem> headimg = null;
        try {
            headimg = (List<DiskFileItem>) param.get("headimg");
        } catch (Exception e) {
            headimg = null;
        }

        if (school_id == null || StringUtils.isBlank(name) || headimg == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取创建人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Group gp = new Group();
        gp.setName(name);
        gp.setIsDelete((short) 2);
        gp = groupService.getGroup(gp);
        if (gp != null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "群组已存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取学校信息
        School school = schoolService.getById(school_id);

        // 群组表
        Group group = new Group();
        group.setSchoolId(school_id);
        group.setSchool(school.getName());
        group.setNumber(school_id.toString() + RandomUtil.randomNumber(0, 6));
        group.setName(name);
        DiskFileItem fileBean = headimg.get(0);
        byte[] fileContent = fileBean.get();
        String fileName = FileUtilsEx.getFileNameByPath(fileBean.getName());
        String fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
        group.setHeadimg(fileid);
        group.setCreatorId(user_id);
        group.setCreateTime(new Date());
        group.setMemberCount(1);
        groupService.insert(group);

        // 群成员表
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getId());
        groupUser.setUserId(user_id);
        groupUser.setType((short) 1);
        groupUser.setCreateTime(new Date());
        groupUser.setBecomeAdminTime(new Date());
        groupUserService.insert(groupUser);

        Map<String, Object> map = new HashMap<>();
        map.put("group_id", group.getId());

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取群通知列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/user_findGroupMsgList")
    public void user_findGroupMsgList(HttpServletRequest request, HttpServletResponse response,
                                      @ModelAttribute("user_id") Integer user_id, Integer page, Integer pageSize, String time) {
        if (page == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = groupMessageService.user_findGroupMsgList(user_id, time);

        // 将未读消息设为已读
        groupMessageService.updateIsRead(user_id);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("time", time);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 操作群通知
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_msg_id 群通知id
     * @param type         1同意 2拒绝
     */
    @RequestMapping("/user_editGroupMsg")
    public void user_editGroupMsg(HttpServletRequest request, HttpServletResponse response,
                                  @ModelAttribute("user_id") Integer user_id, Integer group_msg_id, Short type) {
        if (group_msg_id == null || type == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取本人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取群通知信息
        GroupMessage groupMessage = groupMessageService.getById(group_msg_id);
        if (groupMessage.getStatus().shortValue() != 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已操作过", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取群信息
        Integer groupId = groupMessage.getGroupId();
        Group group = groupService.getById(groupId);

        // 2通过/同意 3驳回/拒绝
        Short status = null;
        if (type.shortValue() == 1) {
            status = 2;
        } else {
            status = 3;
        }

        // 如果是申请加群
        if (groupMessage.getType().shortValue() == 1) {
            // 获取申请人在群的信息
            GroupUser groupUser = new GroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(groupMessage.getUserId());
            groupUser.setIsDelete((short) 2);
            groupUser = groupUserService.getGroupUser(groupUser);
            if (groupUser != null) {
                String strToMoblieJson = JsonUtilEx
                        .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "申请人已经在群组里", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            // 如果有多个管理员，则要把所有管理员收到的消息的状态都修改
            // 获取同一人的申请信息
            GroupMessage gm = new GroupMessage();
            gm.setGroupId(groupId);
            gm.setType((short) 1);
            gm.setUserId(groupMessage.getUserId());
            gm.setStatus((short) 1);
            gm.setIsDetele((short) 2);
            List<GroupMessage> gmList = groupMessageService.findList(gm);
            if (gmList != null && gmList.size() > 0) {
                for (GroupMessage message : gmList) {
                    // 修改状态
                    GroupMessage gMessage = new GroupMessage();
                    gMessage.setId(message.getId());
                    gMessage.setStatus(status);
                    gMessage.setOperationTime(new Date());
                    gMessage.setOperator(user.getName());
                    gMessage.setOperatorId(user_id);
                    groupMessageService.update(gMessage);
                }
            }

            // 如果同意
            if (status.shortValue() == 2) {
                // 新增群成员关系表
                GroupUser gUser = new GroupUser();
                gUser.setGroupId(groupId);
                gUser.setUserId(groupMessage.getUserId());
                gUser.setType((short) 3);
                gUser.setCreateTime(new Date());
                groupUserService.insert(gUser);

                // 群成员数+1
                Group gp = new Group();
                gp.setId(groupId);
                gp.setMemberCount(group.getMemberCount().intValue() + 1);
                groupService.update(gp);
            }
        } else {
            // 如果是邀请加群

            // 获取受邀人在群的信息
            GroupUser groupUser = new GroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(groupMessage.getReceiverId());
            groupUser.setIsDelete((short) 2);
            groupUser = groupUserService.getGroupUser(groupUser);
            if (groupUser != null) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您已经在群组里", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            // 直接修改状态
            GroupMessage gMessage = new GroupMessage();
            gMessage.setId(group_msg_id);
            gMessage.setStatus(status);
            gMessage.setOperationTime(new Date());
            gMessage.setOperator(user.getName());
            gMessage.setOperatorId(user_id);
            groupMessageService.update(gMessage);

            // 如果同意
            if (status.shortValue() == 2) {
                // 新增群成员关系表
                GroupUser gUser = new GroupUser();
                gUser.setGroupId(groupId);
                gUser.setUserId(groupMessage.getReceiverId());
                gUser.setType((short) 3);
                gUser.setCreateTime(new Date());
                groupUserService.insert(gUser);

                // 群成员数+1
                Group gp = new Group();
                gp.setId(groupId);
                gp.setMemberCount(group.getMemberCount().intValue() + 1);
                groupService.update(gp);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除群通知
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_msg_id
     */
    @RequestMapping("/user_deleteGroupMsg")
    public void user_deleteGroupMsg(HttpServletRequest request, HttpServletResponse response,
                                    @ModelAttribute("user_id") Integer user_id, Integer group_msg_id) {
        if (group_msg_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取群通知信息
        GroupMessage groupMessage = groupMessageService.getById(group_msg_id);
        if (groupMessage.getIsDetele().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已被删除", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 删除群通知消息
        GroupMessage message = new GroupMessage();
        message.setId(group_msg_id);
        message.setIsDetele((short) 1);
        message.setDeleteTime(new Date());
        groupMessageService.update(message);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 清空群通知
     *
     * @param request
     * @param response
     * @param user_id
     */
    @RequestMapping("/user_cleanGroupMsg")
    public void user_cleanGroupMsg(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("user_id") Integer user_id) {
        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        groupMessageService.user_cleanGroupMsg(user_id);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 私信他人
     *
     * @param request
     * @param response
     * @param user_id     发信人 a
     * @param otherUserId 收信人 b
     */
    @RequestMapping("/user_chatOther")
    public void user_chatOther(HttpServletRequest request, HttpServletResponse response,
                               @ModelAttribute("user_id") Integer user_id, String otherHuanxinId) {
        if (StringUtils.isBlank(otherHuanxinId)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取发起人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 根据对方环信id获取对方user_id
        Integer otherUserId = 0;
        User uEntity = new User();
        uEntity.setHuanxinId(otherHuanxinId);
        uEntity.setIsDelete((short) 2);
        User user2 = userService.getUser(uEntity);
        if (null != user2) {
            otherUserId = user2.getId();
        }

        // a 发送一条私信给b
        // a给b发送消息，除了新增一条发送人为a，接收人为b的消息以外，还需新增一条发送人为b，接收人为a的消息

        // 获取发送人为a，接收人为b的私信记录
        Chat fChat = new Chat();
        fChat.setUserId(user_id);
        fChat.setToUserId(otherUserId);
        fChat.setIsDelete((short) 2);
        fChat = chatService.get(fChat);

        // 获取发送人为b，接收人为a的私信记录
        Chat jChat = new Chat();
        jChat.setUserId(otherUserId);
        jChat.setToUserId(user_id);
        jChat.setIsDelete((short) 2);
        jChat = chatService.get(jChat);

        // 是否关注
        Short isFollow = null;
        // 获取收信人b是否关注发信人a 1是2否
        Follow follow = new Follow();
        follow.setFollowerId(otherUserId);
        follow.setBeFollowedId(user_id);
        follow.setIsCancel((short) 2);
        follow = followService.getFollow(follow);
        if (follow != null) {
            isFollow = 1;
        } else {
            isFollow = 2;
        }

        // 如果a发送给b的记录不存在
        if (fChat == null) {
            // 如果b发送给a的记录也不存在，说明是第一次
            if (jChat == null) {
                // 新增一条a发送给b的私信记录
                Chat chata = new Chat();
                chata.setUserId(user_id);
                chata.setToUserId(otherUserId);
                chata.setCreateTime(new Date());
                chata.setIsFollow(isFollow);
                chatService.insert(chata);

                // 新增一条b发送给a的私信记录
                Chat chatb = new Chat();
                chatb.setUserId(otherUserId);
                chatb.setToUserId(user_id);
                chatb.setCreateTime(new Date());
                chatb.setIsFollow((short) 1);
                chatService.insert(chatb);
            } else {
                // 如果b发送给a的记录存在，说明b将聊天框删除了
                // 如果b发送给a的记录，a不显示在外面，说明a给b发送的这条私信属于回复
                if (jChat.getIsFollow().shortValue() == 2) {
                    // 新增一条a发送给b的私信记录
                    Chat chata = new Chat();
                    chata.setUserId(user_id);
                    chata.setToUserId(otherUserId);
                    chata.setCreateTime(new Date());
                    chata.setIsFollow((short) 1);
                    chatService.insert(chata);

                    // 修改b发送给a的显示
                    Chat chatb = new Chat();
                    chatb.setId(jChat.getId());
                    chatb.setCreateTime(new Date());
                    chatb.setIsFollow((short) 1);
                    chatService.update(chatb);
                } else {
                    // 新增一条a发送给b的私信记录
                    Chat chata = new Chat();
                    chata.setUserId(user_id);
                    chata.setToUserId(otherUserId);
                    chata.setCreateTime(new Date());
                    chata.setIsFollow(isFollow);
                    chatService.insert(chata);
                }
            }
        } else {// 如果a发送给b的记录存在
            // 修改a发送给b的记录的时间
            Chat chata = new Chat();
            chata.setId(fChat.getId());
            chata.setCreateTime(new Date());
            chatService.update(chata);

            // 如果a发送给b的记录，b不显示在外面
            if (fChat.getIsFollow().shortValue() == 2) {
                // 如果b发送给a的记录不存在
                if (jChat == null) {
                    // 新增一条b发送给a的私信记录
                    Chat chatb = new Chat();
                    chatb.setUserId(otherUserId);
                    chatb.setToUserId(user_id);
                    chatb.setCreateTime(new Date());
                    chatb.setIsFollow((short) 1);
                    chatService.insert(chatb);
                }
            } else {
                // 如果b发送给a的记录存在(两条记录均存在)
                if (jChat != null) {
                    // 如果b发送给a的记录，a不显示在外面，说明a给b发送的这条私信属于回复
                    if (jChat.getIsFollow().shortValue() == 2) {
                        // 修改b发送给a的显示
                        Chat chatb = new Chat();
                        chatb.setId(jChat.getId());
                        chatb.setCreateTime(new Date());
                        chatb.setIsFollow((short) 1);
                        chatService.update(chatb);
                    }
                } else {
                    // 如果b发送给a的记录不存在
                    // 是否关注
                    Short isFollowb = null;
                    // 获取收信人a是否关注发信人b 1是2否
                    Follow followb = new Follow();
                    followb.setFollowerId(user_id);
                    followb.setBeFollowedId(otherUserId);
                    followb.setIsCancel((short) 2);
                    followb = followService.getFollow(followb);
                    if (followb != null) {
                        isFollowb = 1;
                    } else {
                        isFollowb = 2;
                    }

                    // 新增一条b发送给a的私信记录
                    Chat chatb = new Chat();
                    chatb.setUserId(otherUserId);
                    chatb.setToUserId(user_id);
                    chatb.setCreateTime(new Date());
                    chatb.setIsFollow(isFollowb);
                    chatService.insert(chatb);
                }
            }
        }

        // 推送
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("title", "您有一条新私信");
        hm.put("type", 3);
        try {
            flag = PushTool.pushToUser(otherUserId, "", "您有一条新私信", hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 入推送表
        Push puEntity = new Push();
        puEntity.setToken(user2.getDevToken());
        puEntity.setContent("");
        puEntity.setUserId(otherUserId);
        puEntity.setMessageType((short) 11);
        puEntity.setDeviceType(user2.getDevType());
        puEntity.setTitle("您有一条新私信");
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushSerivce.addPush(puEntity);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除聊天
     *
     * @param request
     * @param response
     * @param user_id     本人id(收信人id)
     * @param otherUserId 对方id(发信人id)
     */
    @RequestMapping("/user_deleteChar")
    public void user_deleteChar(HttpServletRequest request, HttpServletResponse response,
                                @ModelAttribute("user_id") Integer user_id, Integer otherUserId) {
        if (otherUserId == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // 获取私信信息
        Chat chat = new Chat();
        chat.setUserId(otherUserId);
        chat.setToUserId(user_id);
        chat.setIsDelete((short) 2);
        chat = chatService.get(chat);

        if (chat != null) {
            // 删除
            Chat entity = new Chat();
            entity.setId(chat.getId());
            entity.setIsDelete((short) 1);
            entity.setDeleteTime(new Date());
            chatService.update(entity);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 获取教职工列表
     *
     * @param schoolId 学校ID
     * @param name     教职工姓名关键字
     */
    @RequestMapping(value = "/user_findTeacherList")
    public void user_findTeacherList(@RequestParam(value = "schoolId") int schoolId,
                                     @RequestParam(value = "name") String name, HttpServletResponse response) {
        List<Map<String, Object>> list = userService.findUserTeacherLists(schoolId, name);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", map.get("id"));
            result.put("name", map.get("name"));
            result.put("headimg", map.get("headimg"));
            result.put("phone", map.get("phone"));
            resultList.add(result);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", resultList)));
    }
}
