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
     * ??????@????????????
     *
     * @param request
     * @param response
     * @param user_id
     * @param search
     * @param school_id ??????????????? ps:??????????????????????????????????????????????????????
     */
    @RequestMapping("/user_selectAtUserList")
    public void user_selectAtUserList(HttpServletRequest request, HttpServletResponse response,
                                      @ModelAttribute("user_id") Integer user_id, String search, Integer school_id) {
        List<Map<String, Object>> list = new ArrayList<>();

        // ???????????????
        if (StringUtils.isBlank(search)) {
            // ??????@??????----??????????????????
            list = followService.user_selectAtUserList(user_id);

            // ????????????????????????????????????????????????
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
             * JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????",null));
             * ServletUtilsEx.renderText(response, strToMoblieJson); return; }
             */
            // ??????@??????----??????????????????+??????????????????
            list = userService.user_selectAtUserListForSchool(user_id, search, school_id);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     */
    @RequestMapping("/app_findGradeList")
    public void app_findGradeList(HttpServletRequest request, HttpServletResponse response) {
        Grade entity = new Grade();
        entity.setIsDelete((short) 2);
        List<Grade> list = gradeService.findList(entity);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @param school_id
     */
    @RequestMapping("/app_findInstituteList")
    public void app_findInstituteList(HttpServletRequest request, HttpServletResponse response, Integer school_id,
                                      @ModelAttribute("version") String version) {
        if (school_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Institute entity = new Institute();
        entity.setSchoolId(school_id);
        entity.setIsDelete((short) 2);
        List<Institute> list = instituteService.findList(entity);

        // 1.2??????????????????????????????????????????????????????
        if (convertVersionToDouble(version) >= 1.2) {
            Institute institute = new Institute();
            institute.setId(-1);
            institute.setName("?????????");
            institute.setIsDelete((short) 2);
            list.add(institute);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @param institute_id ??????id
     * @param grade_id     ??????id
     * @param group_id     ??????id
     */
    @RequestMapping("/app_findMajorList")
    public void app_findMajorList(HttpServletRequest request, HttpServletResponse response, Integer institute_id,
                                  Integer grade_id, Integer group_id, @ModelAttribute("version") String version) {
        if (institute_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Major> list;
        if (institute_id == -1 && convertVersionToDouble(version) >= 1.2) {
            // 1.2?????????????????????????????????
            list = new ArrayList<>();
            UserCriteria example = new UserCriteria();
            UserCriteria.Criteria criteria = example.createCriteria();
            criteria.andTypeEqualTo((short) 1);// ??????
            criteria.andIsDeleteEqualTo((short) 2);// ?????????
            criteria.andIsAuthEqualTo((short) 1);// ????????????
            criteria.andStatusEqualTo((short) 1);// ??????
            criteria.andIsTeacherEqualTo((short) 1);// ?????????
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
            // ??????????????????
            Major entity = new Major();
            entity.setInstituteId(institute_id);
            entity.setIsDelete((short) 2);
            list = majorService.findList(entity);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);

        if (group_id != null && grade_id != null) {
            // ?????????????????????????????????
            List<Map<String, Object>> stuList = userService.user_findStudentList(null, null, group_id, institute_id,
                    grade_id, null);

            map.put("stuList", stuList);
            map.put("stuNum", stuList.size());
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ???????????????????????????????????????
     *
     * @param request
     * @param response
     * @param grade_id ??????id
     * @param major_id ??????id
     * @param group_id ??????id
     */
    @RequestMapping("/app_findClassList")
    public void app_findClassList(HttpServletRequest request, HttpServletResponse response, Integer grade_id,
                                  Integer major_id, Integer group_id) {
        if (grade_id == null || major_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????
        Class entity = new Class();
        entity.setMajorId(major_id);
        entity.setGradeId(grade_id);
        entity.setIsDelete((short) 2);
        List<Class> list = classService.findList(entity);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);

        if (group_id != null) {
            // ?????????????????????????????????
            List<Map<String, Object>> stuList = userService.user_findStudentList(null, null, group_id, null, grade_id,
                    major_id);

            map.put("stuList", stuList);
            map.put("stuNum", stuList.size());
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ??????????????????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Map<String, Object>> list = userService.user_findStudentList(user_id, class_id, group_id, null, null,
                null);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("stuNum", list.size());

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ??????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ???????????????????????????(???????????????)
        List<Map<String, Object>> chatList = chatService.findChatUserFromXiaoXin(user_id, search);

        // ????????????????????????
        List<Map<String, Object>> groupList = groupService.findGroupUserFromXiaoXin(user_id, search);

        Map<String, Object> map = new HashMap<>();
        map.put("userList", chatList);
        map.put("groupList", groupList);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ???????????????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
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

        // ????????????????????????????????????
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

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ??????????????????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
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

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ??????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = new HashMap<>();

        // ?????????????????????
        GroupMessageCriteria gc = new GroupMessageCriteria();
        Criteria cc = gc.createCriteria();
        cc.andReceiverIdEqualTo(user_id);
        cc.andIsReadEqualTo((short) 2);
        cc.andIsDeteleEqualTo((short) 2);
        long notReadMsgNum = groupMessageService.findCount(gc);
        map.put("notReadMsgNum", notReadMsgNum);

        // ????????????????????????
        GroupMessage groupMessage2 = new GroupMessage();
        groupMessage2.setReceiverId(user_id);
        groupMessage2.setIsDetele((short) 2);
        List<GroupMessage> msgList = groupMessageService.findList(groupMessage2);

        if (msgList != null && msgList.size() > 0) {
            GroupMessage groupMessage = msgList.get(0);

            // ????????????????????????
            map.put("lastMsgTime", groupMessage.getCreateTime());

            // ??????1????????????2????????????3??????4??????5??????????????? 6??????????????? 7????????????
            short type = groupMessage.getType().shortValue();
            // ?????????
            String user = groupMessage.getUser();
            // ??????????????????
            Group group = groupService.getById(groupMessage.getGroupId());

            if (type == 1) {
                map.put("lastMsgContent", user + " ???????????? " + group.getName());
            } else if (type == 2) {
                map.put("lastMsgContent", user + " ??????????????? " + group.getName());
            } else if (type == 3) {
                map.put("lastMsgContent", user + " ?????? " + group.getName());
            } else if (type == 4) {
                map.put("lastMsgContent", group.getName() + " ??????????????????????????????");
            } else if (type == 5) {
                map.put("lastMsgContent", group.getName() + " ???????????????????????????");
            } else if (type == 6) {
                map.put("lastMsgContent", group.getName() + " ????????????????????????????????????");
            } else if (type == 7) {
                map.put("lastMsgContent", group.getName() + " ???????????????");
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
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        // ??????????????????
        List<Map<String, Object>> list = groupService.user_findGroupList(user_id, time);
        map.put("list", list);
        map.put("time", time);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ?????????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = new HashMap<>();

        // ???????????????
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
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????????????????",null));
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

        // ?????????9????????????????????????
        List<Map<String, Object>> list = groupUserService.findNineMemberList(group_id);
        map.put("list", list);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ?????????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Map<String, Object>> list = groupUserService.user_findGroupMemberList(group_id, search);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id ??????id
     * @param user_ids ??????id?????????????????????
     * @param type     1??????????????? 2???????????????
     */
    @RequestMapping("/user_editGroupManage")
    public void user_editGroupManage(HttpServletRequest request, HttpServletResponse response,
                                     @ModelAttribute("user_id") Integer user_id, Integer group_id, String user_ids, Short type) {
        if (group_id == null || StringUtils.isBlank(user_ids) || type == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????????????????
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() != 1) {
            String strToMoblieJson = JsonUtilEx
                    .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        String[] userIds = user_ids.split(",");
        // ??????????????????
//		Group group = groupService.getById(group_id);

        // ???????????????
        if (type.shortValue() == 1) {
            // ??????????????????????????????
            GroupUserCriteria gc = new GroupUserCriteria();
            GroupUserCriteria.Criteria cc = gc.createCriteria();
            cc.andGroupIdEqualTo(group_id);
            cc.andTypeEqualTo((short) 2);
            cc.andIsDeleteEqualTo((short) 2);
            int count = groupUserService.findCount(gc);
            if (count == 4) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            if ((count + userIds.length) > 4) {
                String strToMoblieJson = JsonUtilEx
                        .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????,???????????????", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            for (String uId : userIds) {
                Integer receiverId = Integer.valueOf(uId);
                // ??????????????????
                User recceiver = userService.getById(receiverId);

                // ????????????????????????
                GroupUser gu = new GroupUser();
                gu.setGroupId(group_id);
                gu.setUserId(receiverId);
                gu.setIsDelete((short) 2);
                gu = groupUserService.getGroupUser(gu);

                // ?????????????????????
                if (gu.getType().shortValue() == 3) {
                    // ??????????????????
                    GroupUser gUser = new GroupUser();
                    gUser.setId(gu.getId());
                    gUser.setType((short) 2);
                    gUser.setBecomeAdminTime(new Date());
                    groupUserService.update(gUser);

                    // ????????????
                    GroupMessage groupMessage = new GroupMessage();
                    groupMessage.setGroupId(group_id);
                    groupMessage.setType((short) 5);
                    groupMessage.setUserId(user_id);
                    groupMessage.setUser(user.getName());
                    groupMessage.setReceiverId(receiverId);
                    groupMessage.setRecceiver(recceiver.getName());
                    groupMessage.setCreateTime(new Date());
                    groupMessageService.insert(groupMessage);

//					//??????
//					//????????????????????? 1???2???
//					if(gu.getIsNotify().shortValue() == 2){
//						Boolean flag = false;
//						HashMap<String,Object> hm = new HashMap<String,Object>();
////						hm.put("id", saying.getId());
//						hm.put("title", "??????????????? "+group.getName()+" ????????????");
//						try {
//							flag = PushTool.pushToUser(receiverId, "", "??????????????? "+group.getName()+" ????????????", hm);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						//????????????
//						Push puEntity = new Push();
//						puEntity.setToken(recceiver.getDevToken());
//						puEntity.setContent("");
//						puEntity.setUserId(receiverId);
//						puEntity.setMessageType((short)4);
//						puEntity.setDeviceType(recceiver.getDevType());
//						puEntity.setTitle("??????????????? "+group.getName()+" ????????????");
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
                // ??????????????????
                User recceiver = userService.getById(receiverId);

                // ????????????????????????
                GroupUser gu = new GroupUser();
                gu.setGroupId(group_id);
                gu.setUserId(Integer.valueOf(uId));
                gu.setIsDelete((short) 2);
                gu = groupUserService.getGroupUser(gu);

                // ??????????????????
                if (gu.getType().shortValue() == 2) {
                    // ?????????????????????
                    GroupUser gUser = new GroupUser();
                    gUser.setId(gu.getId());
                    gUser.setType((short) 3);
                    groupUserService.update(gUser);

                    // ????????????
                    GroupMessage groupMessage = new GroupMessage();
                    groupMessage.setGroupId(group_id);
                    groupMessage.setType((short) 6);
                    groupMessage.setUserId(user_id);
                    groupMessage.setUser(user.getName());
                    groupMessage.setReceiverId(receiverId);
                    groupMessage.setRecceiver(recceiver.getName());
                    groupMessage.setCreateTime(new Date());
                    groupMessageService.insert(groupMessage);

//					//??????
//					//????????????????????? 1???2???
//					if(gu.getIsNotify().shortValue() == 2){
//						Boolean flag = false;
//						HashMap<String,Object> hm = new HashMap<String,Object>();
////						hm.put("id", saying.getId());
//						hm.put("title", "?????????????????????????????? "+group.getName());
//						try {
//							flag = PushTool.pushToUser(receiverId, "", "?????????????????????????????? "+group.getName(), hm);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						//????????????
//						Push puEntity = new Push();
//						puEntity.setToken(recceiver.getDevToken());
//						puEntity.setContent("");
//						puEntity.setUserId(receiverId);
//						puEntity.setMessageType((short)4);
//						puEntity.setDeviceType(recceiver.getDevType());
//						puEntity.setTitle("?????????????????????????????? "+group.getName());
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

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @param user_id
     * @param school_id ??????id
     * @param group_id  ??????id
     * @param search    ????????????
     */
    @RequestMapping("/user_findStudentBySchool")
    public void user_findStudentBySchool(HttpServletRequest request, HttpServletResponse response,
                                         @ModelAttribute("user_id") Integer user_id, Integer group_id, String search) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        List<Map<String, Object>> list = userService.user_findStudentBySchool(user_id, group_id, search);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id ??????id
     * @param user_ids ??????id?????????????????????
     */
    @RequestMapping("/user_addGroupMember")
    public void user_addGroupMember(HttpServletRequest request, HttpServletResponse response,
                                    @ModelAttribute("user_id") Integer user_id, Integer group_id, String user_ids) {
        if (group_id == null || StringUtils.isBlank(user_ids)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        String[] userIds = user_ids.split(",");
        // ??????????????????
        Group group = groupService.getById(group_id);

        for (String uId : userIds) {
            Integer receiverId = Integer.valueOf(uId);
            // ??????????????????
            User recceiver = userService.getById(receiverId);

            // ????????????????????????
            GroupUser gu = new GroupUser();
            gu.setGroupId(group_id);
            gu.setUserId(Integer.valueOf(uId));
            gu.setIsDelete((short) 2);
            gu = groupUserService.getGroupUser(gu);

            // ???????????????
            if (gu == null) {
                boolean insertFlag = groupMessageService.allowedSendGroupInviteMsg(Integer.valueOf(uId), group_id);
                if (!insertFlag) {
                    //????????????????????????????????????????????????????????????????????????
                    continue;
                }

                // ????????????
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

                // ??????
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
//				hm.put("id", saying.getId());
                hm.put("title", user.getName() + " ????????????????????? " + group.getName());
                hm.put("type", 4);
                try {
                    flag = PushTool.pushToUser(receiverId, "", user.getName() + " ????????????????????? " + group.getName(), hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ????????????
                Push puEntity = new Push();
                puEntity.setToken(recceiver.getDevToken());
                puEntity.setContent("");
                puEntity.setUserId(receiverId);
                puEntity.setMessageType((short) 4);
                puEntity.setDeviceType(recceiver.getDevType());
                puEntity.setTitle(user.getName() + " ????????????????????? " + group.getName());
//				puEntity.setObjectId(saying.getId());
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }

                pushSerivce.addPush(puEntity);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ????????????????????????
        GroupUser gu = new GroupUser();
        gu.setGroupId(group_id);
        gu.setUserId(user_id);
        gu.setIsDelete((short) 2);
        gu = groupUserService.getGroupUser(gu);
        if (gu != null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ????????????????????????
        GroupMessage gm = new GroupMessage();
        gm.setGroupId(group_id);
        gm.setType((short) 1);
        gm.setUserId(user_id);
        gm.setStatus((short) 1);
        gm.setIsDetele((short) 2);
        List<GroupMessage> groupMessagesList = groupMessageService.findList(gm);
        if (groupMessagesList != null && groupMessagesList.size() > 0) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ????????????????????????
        List<Map<String, Object>> groupManagerList = groupUserService.app_findShareGroupUserList(group_id);
        // ???????????????????????????
        groupMessageService.insertAllApplyAdd(group_id, user_id, user.getName(), groupManagerList);

        // ??????????????????
        Group group = groupService.getById(group_id);

        // ??????
        for (Map<String, Object> map : groupManagerList) {
            // ????????????????????? 1???2???
            short is_notify = Short.parseShort(map.get("is_notify").toString());
            // ???????????????id
            Integer toerId = Integer.valueOf(map.get("user_id").toString());
            // ?????????????????????
            User toer = userService.getById(toerId);

            if (is_notify == 2) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
//				hm.put("id", saying.getId());
                hm.put("title", user.getName() + " ?????????????????? " + group.getName());
                hm.put("type", 4);
                try {
                    flag = PushTool.pushToUser(toerId, "", user.getName() + " ?????????????????? " + group.getName(), hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ????????????
                Push puEntity = new Push();
                puEntity.setToken(toer.getDevToken());
                puEntity.setContent("");
                puEntity.setUserId(toerId);
                puEntity.setMessageType((short) 4);
                puEntity.setDeviceType(toer.getDevType());
                puEntity.setTitle(user.getName() + " ?????????????????? " + group.getName());
//				puEntity.setObjectId(saying.getId());
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }

                pushSerivce.addPush(puEntity);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id ??????id
     * @param user_ids ??????id?????????????????????
     */
    @RequestMapping("/user_deleteGroupMember")
    public void user_deleteGroupMember(HttpServletRequest request, HttpServletResponse response,
                                       @ModelAttribute("user_id") Integer user_id, Integer group_id, String user_ids) {
        if (group_id == null || StringUtils.isBlank(user_ids)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????????????????
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() == 3) {
            String strToMoblieJson = JsonUtilEx
                    .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????????????????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        String[] userIds = user_ids.split(",");
        // ??????????????????
        Group group = groupService.getById(group_id);

        for (String uId : userIds) {
            // ??????????????????
            GroupUser gu = new GroupUser();
            gu.setGroupId(group_id);
            gu.setUserId(Integer.valueOf(uId));
            gu.setIsDelete((short) 2);
            gu = groupUserService.getGroupUser(gu);

            // ????????????
            if (gu != null) {
                // ??????????????????
                User recceiver = userService.getById(Integer.valueOf(uId));

                // ????????????
                GroupUser gUser = new GroupUser();
                gUser.setId(gu.getId());
                gUser.setIsDelete((short) 1);
                gUser.setDeleteTime(new Date());
                groupUserService.update(gUser);

                // ??????????????????-1
                Group gp = new Group();
                gp.setId(group_id);
                gp.setMemberCount(group.getMemberCount().intValue() - 1);
                groupService.update(gp);

                // ??????????????????
                GroupMessage groupMessage = new GroupMessage();
                groupMessage.setGroupId(group_id);
                groupMessage.setType((short) 4);
                groupMessage.setUserId(user_id);
                groupMessage.setUser(user.getName());
                groupMessage.setReceiverId(Integer.valueOf(uId));
                groupMessage.setRecceiver(recceiver.getName());
                groupMessage.setCreateTime(new Date());
                groupMessageService.insert(groupMessage);

                // ?????????????????????????????????????????????
                noticeReceiveService.deleteAllByUser(Integer.valueOf(uId), group_id);

//				//??????
//				//????????????????????? 1???2???
//				if(gu.getIsNotify().shortValue() == 2){
//					Boolean flag = false;
//					HashMap<String,Object> hm = new HashMap<String,Object>();
////					hm.put("id", saying.getId());
//					hm.put("title", "????????????????????? "+group.getName());
//					try {
//						flag = PushTool.pushToUser(Integer.valueOf(uId), "", "????????????????????? "+group.getName(), hm);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					//????????????
//					Push puEntity = new Push();
//					puEntity.setToken(recceiver.getDevToken());
//					puEntity.setContent("");
//					puEntity.setUserId(Integer.valueOf(uId));
//					puEntity.setMessageType((short)4);
//					puEntity.setDeviceType(recceiver.getDevType());
//					puEntity.setTitle("????????????????????? "+group.getName());
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

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ???????????????
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
        // ??????id
        Integer group_id = null;
        try {
            group_id = Integer.valueOf(param.get("group_id").toString());
        } catch (Exception e) {
            group_id = null;
        }
        // ?????????
        String name = null;
        try {
            name = param.get("name").toString();
        } catch (Exception e) {
            name = null;
        }
        // ?????????
        List<DiskFileItem> headimg = null;
        try {
            headimg = (List<DiskFileItem>) param.get("headimg");
        } catch (Exception e) {
            headimg = null;
        }

        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????????????????
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() == 3) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "??????????????????????????????", null));
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

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????????????????
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "??????????????????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		//??????????????????
        Group group = groupService.getById(group_id);

        // ????????????
        GroupUser gUser = new GroupUser();
        gUser.setId(groupUser.getId());
        gUser.setIsDelete((short) 1);
        gUser.setDeleteTime(new Date());
        groupUserService.update(gUser);

        // ??????????????????-1
        Group gp = new Group();
        gp.setId(group_id);
        gp.setMemberCount(group.getMemberCount().intValue() - 1);
        groupService.update(gp);

        // ????????????????????????
        List<Map<String, Object>> groupManagerList = groupUserService.app_findShareGroupUserList(group_id);
        // ???????????????????????????
        groupMessageService.insertAllOutGroup(group_id, user_id, user.getName(), groupManagerList);

        // ?????????????????????????????????????????????
        noticeReceiveService.deleteAllByUser(user_id, group_id);

//		
//		//??????
//		for(Map<String, Object> map : groupManagerList){
//			//????????????????????? 1???2???
//			short is_notify = Short.parseShort(map.get("is_notify").toString());
//			//???????????????id
//			Integer toerId = Integer.valueOf(map.get("user_id").toString());
//			//?????????????????????
//			User toer = userService.getById(toerId);
//			
//			if(is_notify == 2){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
//				hm.put("title", user.getName()+" ??????????????? "+group.getName());
//				try {
//					flag = PushTool.pushToUser(toerId, "", user.getName()+" ??????????????? "+group.getName(), hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//????????????
//				Push puEntity = new Push();
//				puEntity.setToken(toer.getDevToken());
//				puEntity.setContent("");
//				puEntity.setUserId(toerId);
//				puEntity.setMessageType((short)4);
//				puEntity.setDeviceType(toer.getDevType());
//				puEntity.setTitle(user.getName()+" ??????????????? "+group.getName());
//				if(flag){
//					puEntity.setType((short)10);
//				}else{
//					puEntity.setType((short)50);
//				}
//				
//				pushSerivce.addPush(puEntity);
//			}
//		}

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????
        Group group = groupService.getById(group_id);
        if (group.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????????????????
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null || groupUser.getType().shortValue() != 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ????????????
        Group group2 = new Group();
        group2.setId(group_id);
        group2.setIsDelete((short) 1);
        group2.setDeleteTime(new Date());
        groupService.update(group2);

        // ?????????????????????
        List<Map<String, Object>> groupUserList = groupUserService.user_findGroupMemberList(group_id, null);
        // ???????????????????????????
        groupMessageService.insertAllDeleteGroup(group_id, user_id, user.getName(), groupUserList);

        // ?????????????????????????????????????????????
        noticeReceiveService.deleteAllByUser(null, group_id);

//		//??????
//		for(Map<String, Object> map : groupUserList){
//			//????????????????????? 1???2???
//			short is_notify = Short.parseShort(map.get("is_notify").toString());
//			//???????????????id
//			Integer toerId = Integer.valueOf(map.get("user_id").toString());
//			//?????????????????????
//			User toer = userService.getById(toerId);
//			
//			if(is_notify == 2){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
////				hm.put("id", saying.getId());
//				hm.put("title", "??????????????? "+group.getName());
//				try {
//					flag = PushTool.pushToUser(toerId, "", "??????????????? "+group.getName(), hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//????????????
//				Push puEntity = new Push();
//				puEntity.setToken(toer.getDevToken());
//				puEntity.setContent("");
//				puEntity.setUserId(toerId);
//				puEntity.setMessageType((short)4);
//				puEntity.setDeviceType(toer.getDevType());
//				puEntity.setTitle("??????????????? "+group.getName());
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

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ???????????????????????????
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_id
     * @param is_notify ????????????????????? 1???2???
     */
    @RequestMapping("/user_editGroup")
    public void user_editGroup(HttpServletRequest request, HttpServletResponse response,
                               @ModelAttribute("user_id") Integer user_id, Integer group_id, Short is_notify) {
        if (group_id == null || is_notify == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ???????????????????????????
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group_id);
        groupUser.setUserId(user_id);
        groupUser.setIsDelete((short) 2);
        groupUser = groupUserService.getGroupUser(groupUser);

        if (groupUser == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        GroupUser gu = new GroupUser();
        gu.setId(groupUser.getId());
        gu.setIsNotify(is_notify);
        groupUserService.update(gu);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
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
        // ??????
        Integer school_id = null;
        try {
            school_id = Integer.valueOf(param.get("school_id").toString());
        } catch (Exception e) {
            school_id = null;
        }
        // ?????????
        String name = null;
        try {
            name = param.get("name").toString();
        } catch (Exception e) {
            name = null;
        }
        // ?????????
        List<DiskFileItem> headimg = null;
        try {
            headimg = (List<DiskFileItem>) param.get("headimg");
        } catch (Exception e) {
            headimg = null;
        }

        if (school_id == null || StringUtils.isBlank(name) || headimg == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Group gp = new Group();
        gp.setName(name);
        gp.setIsDelete((short) 2);
        gp = groupService.getGroup(gp);
        if (gp != null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????
        School school = schoolService.getById(school_id);

        // ?????????
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

        // ????????????
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getId());
        groupUser.setUserId(user_id);
        groupUser.setType((short) 1);
        groupUser.setCreateTime(new Date());
        groupUser.setBecomeAdminTime(new Date());
        groupUserService.insert(groupUser);

        Map<String, Object> map = new HashMap<>();
        map.put("group_id", group.getId());

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ?????????????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = groupMessageService.user_findGroupMsgList(user_id, time);

        // ???????????????????????????
        groupMessageService.updateIsRead(user_id);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("time", time);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @param user_id
     * @param group_msg_id ?????????id
     * @param type         1?????? 2??????
     */
    @RequestMapping("/user_editGroupMsg")
    public void user_editGroupMsg(HttpServletRequest request, HttpServletResponse response,
                                  @ModelAttribute("user_id") Integer user_id, Integer group_msg_id, Short type) {
        if (group_msg_id == null || type == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        GroupMessage groupMessage = groupMessageService.getById(group_msg_id);
        if (groupMessage.getStatus().shortValue() != 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ???????????????
        Integer groupId = groupMessage.getGroupId();
        Group group = groupService.getById(groupId);

        // 2??????/?????? 3??????/??????
        Short status = null;
        if (type.shortValue() == 1) {
            status = 2;
        } else {
            status = 3;
        }

        // ?????????????????????
        if (groupMessage.getType().shortValue() == 1) {
            // ??????????????????????????????
            GroupUser groupUser = new GroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(groupMessage.getUserId());
            groupUser.setIsDelete((short) 2);
            groupUser = groupUserService.getGroupUser(groupUser);
            if (groupUser != null) {
                String strToMoblieJson = JsonUtilEx
                        .strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????????????????", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            // ????????????????????????????????????????????????????????????????????????????????????
            // ??????????????????????????????
            GroupMessage gm = new GroupMessage();
            gm.setGroupId(groupId);
            gm.setType((short) 1);
            gm.setUserId(groupMessage.getUserId());
            gm.setStatus((short) 1);
            gm.setIsDetele((short) 2);
            List<GroupMessage> gmList = groupMessageService.findList(gm);
            if (gmList != null && gmList.size() > 0) {
                for (GroupMessage message : gmList) {
                    // ????????????
                    GroupMessage gMessage = new GroupMessage();
                    gMessage.setId(message.getId());
                    gMessage.setStatus(status);
                    gMessage.setOperationTime(new Date());
                    gMessage.setOperator(user.getName());
                    gMessage.setOperatorId(user_id);
                    groupMessageService.update(gMessage);
                }
            }

            // ????????????
            if (status.shortValue() == 2) {
                // ????????????????????????
                GroupUser gUser = new GroupUser();
                gUser.setGroupId(groupId);
                gUser.setUserId(groupMessage.getUserId());
                gUser.setType((short) 3);
                gUser.setCreateTime(new Date());
                groupUserService.insert(gUser);

                // ????????????+1
                Group gp = new Group();
                gp.setId(groupId);
                gp.setMemberCount(group.getMemberCount().intValue() + 1);
                groupService.update(gp);
            }
        } else {
            // ?????????????????????

            // ??????????????????????????????
            GroupUser groupUser = new GroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(groupMessage.getReceiverId());
            groupUser.setIsDelete((short) 2);
            groupUser = groupUserService.getGroupUser(groupUser);
            if (groupUser != null) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            // ??????????????????
            GroupMessage gMessage = new GroupMessage();
            gMessage.setId(group_msg_id);
            gMessage.setStatus(status);
            gMessage.setOperationTime(new Date());
            gMessage.setOperator(user.getName());
            gMessage.setOperatorId(user_id);
            groupMessageService.update(gMessage);

            // ????????????
            if (status.shortValue() == 2) {
                // ????????????????????????
                GroupUser gUser = new GroupUser();
                gUser.setGroupId(groupId);
                gUser.setUserId(groupMessage.getReceiverId());
                gUser.setType((short) 3);
                gUser.setCreateTime(new Date());
                groupUserService.insert(gUser);

                // ????????????+1
                Group gp = new Group();
                gp.setId(groupId);
                gp.setMemberCount(group.getMemberCount().intValue() + 1);
                groupService.update(gp);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ???????????????
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
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        GroupMessage groupMessage = groupMessageService.getById(group_msg_id);
        if (groupMessage.getIsDetele().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        GroupMessage message = new GroupMessage();
        message.setId(group_msg_id);
        message.setIsDetele((short) 1);
        message.setDeleteTime(new Date());
        groupMessageService.update(message);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ???????????????
     *
     * @param request
     * @param response
     * @param user_id
     */
    @RequestMapping("/user_cleanGroupMsg")
    public void user_cleanGroupMsg(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("user_id") Integer user_id) {
        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        groupMessageService.user_cleanGroupMsg(user_id);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @param user_id     ????????? a
     * @param otherUserId ????????? b
     */
    @RequestMapping("/user_chatOther")
    public void user_chatOther(HttpServletRequest request, HttpServletResponse response,
                               @ModelAttribute("user_id") Integer user_id, String otherHuanxinId) {
        if (StringUtils.isBlank(otherHuanxinId)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????id????????????user_id
        Integer otherUserId = 0;
        User uEntity = new User();
        uEntity.setHuanxinId(otherHuanxinId);
        uEntity.setIsDelete((short) 2);
        User user2 = userService.getUser(uEntity);
        if (null != user2) {
            otherUserId = user2.getId();
        }

        // a ?????????????????????b
        // a???b?????????????????????????????????????????????a???????????????b????????????????????????????????????????????????b???????????????a?????????

        // ??????????????????a???????????????b???????????????
        Chat fChat = new Chat();
        fChat.setUserId(user_id);
        fChat.setToUserId(otherUserId);
        fChat.setIsDelete((short) 2);
        fChat = chatService.get(fChat);

        // ??????????????????b???????????????a???????????????
        Chat jChat = new Chat();
        jChat.setUserId(otherUserId);
        jChat.setToUserId(user_id);
        jChat.setIsDelete((short) 2);
        jChat = chatService.get(jChat);

        // ????????????
        Short isFollow = null;
        // ???????????????b?????????????????????a 1???2???
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

        // ??????a?????????b??????????????????
        if (fChat == null) {
            // ??????b?????????a??????????????????????????????????????????
            if (jChat == null) {
                // ????????????a?????????b???????????????
                Chat chata = new Chat();
                chata.setUserId(user_id);
                chata.setToUserId(otherUserId);
                chata.setCreateTime(new Date());
                chata.setIsFollow(isFollow);
                chatService.insert(chata);

                // ????????????b?????????a???????????????
                Chat chatb = new Chat();
                chatb.setUserId(otherUserId);
                chatb.setToUserId(user_id);
                chatb.setCreateTime(new Date());
                chatb.setIsFollow((short) 1);
                chatService.insert(chatb);
            } else {
                // ??????b?????????a????????????????????????b?????????????????????
                // ??????b?????????a????????????a???????????????????????????a???b?????????????????????????????????
                if (jChat.getIsFollow().shortValue() == 2) {
                    // ????????????a?????????b???????????????
                    Chat chata = new Chat();
                    chata.setUserId(user_id);
                    chata.setToUserId(otherUserId);
                    chata.setCreateTime(new Date());
                    chata.setIsFollow((short) 1);
                    chatService.insert(chata);

                    // ??????b?????????a?????????
                    Chat chatb = new Chat();
                    chatb.setId(jChat.getId());
                    chatb.setCreateTime(new Date());
                    chatb.setIsFollow((short) 1);
                    chatService.update(chatb);
                } else {
                    // ????????????a?????????b???????????????
                    Chat chata = new Chat();
                    chata.setUserId(user_id);
                    chata.setToUserId(otherUserId);
                    chata.setCreateTime(new Date());
                    chata.setIsFollow(isFollow);
                    chatService.insert(chata);
                }
            }
        } else {// ??????a?????????b???????????????
            // ??????a?????????b??????????????????
            Chat chata = new Chat();
            chata.setId(fChat.getId());
            chata.setCreateTime(new Date());
            chatService.update(chata);

            // ??????a?????????b????????????b??????????????????
            if (fChat.getIsFollow().shortValue() == 2) {
                // ??????b?????????a??????????????????
                if (jChat == null) {
                    // ????????????b?????????a???????????????
                    Chat chatb = new Chat();
                    chatb.setUserId(otherUserId);
                    chatb.setToUserId(user_id);
                    chatb.setCreateTime(new Date());
                    chatb.setIsFollow((short) 1);
                    chatService.insert(chatb);
                }
            } else {
                // ??????b?????????a???????????????(?????????????????????)
                if (jChat != null) {
                    // ??????b?????????a????????????a???????????????????????????a???b?????????????????????????????????
                    if (jChat.getIsFollow().shortValue() == 2) {
                        // ??????b?????????a?????????
                        Chat chatb = new Chat();
                        chatb.setId(jChat.getId());
                        chatb.setCreateTime(new Date());
                        chatb.setIsFollow((short) 1);
                        chatService.update(chatb);
                    }
                } else {
                    // ??????b?????????a??????????????????
                    // ????????????
                    Short isFollowb = null;
                    // ???????????????a?????????????????????b 1???2???
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

                    // ????????????b?????????a???????????????
                    Chat chatb = new Chat();
                    chatb.setUserId(otherUserId);
                    chatb.setToUserId(user_id);
                    chatb.setCreateTime(new Date());
                    chatb.setIsFollow(isFollowb);
                    chatService.insert(chatb);
                }
            }
        }

        // ??????
        Boolean flag = false;
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("title", "?????????????????????");
        hm.put("type", 3);
        try {
            flag = PushTool.pushToUser(otherUserId, "", "?????????????????????", hm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ????????????
        Push puEntity = new Push();
        puEntity.setToken(user2.getDevToken());
        puEntity.setContent("");
        puEntity.setUserId(otherUserId);
        puEntity.setMessageType((short) 11);
        puEntity.setDeviceType(user2.getDevType());
        puEntity.setTitle("?????????????????????");
        if (flag) {
            puEntity.setType((short) 10);
        } else {
            puEntity.setType((short) 50);
        }

        pushSerivce.addPush(puEntity);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @param user_id     ??????id(?????????id)
     * @param otherUserId ??????id(?????????id)
     */
    @RequestMapping("/user_deleteChar")
    public void user_deleteChar(HttpServletRequest request, HttpServletResponse response,
                                @ModelAttribute("user_id") Integer user_id, Integer otherUserId) {
        if (otherUserId == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ?????????????????????
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        // ??????????????????
        Chat chat = new Chat();
        chat.setUserId(otherUserId);
        chat.setToUserId(user_id);
        chat.setIsDelete((short) 2);
        chat = chatService.get(chat);

        if (chat != null) {
            // ??????
            Chat entity = new Chat();
            entity.setId(chat.getId());
            entity.setIsDelete((short) 1);
            entity.setDeleteTime(new Date());
            chatService.update(entity);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ?????????????????????
     *
     * @param schoolId ??????ID
     * @param name     ????????????????????????
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", resultList)));
    }
}
