package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.*;
import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.biu.wifi.model.TCptDataInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BackendGroupController {

    @Autowired
    private BackendGroupService groupService;

    @Autowired
    private BackendUserService userService;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private NoticeReceiveService noticeReceiveService;

    @Autowired
    private PushSerivce pushSerivce;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private GroupTmpCreatePermissionService groupTmpCreatePermissionService;

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    @RequestMapping("/back_api_findGroupList_g")
    public void back_api_findGroupList_g(Integer userId, String groupName, HttpServletResponse response) {
        if (userId != null) {

            List<Map<String, Object>> list = groupService.findManageGroupList(userId, groupName);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findGroupList_a")
    public void back_api_findGroupList_a(Integer page, Integer pageSize, Integer schoolId, String groupName, Long time,
                                         HttpServletResponse response) {
        if (page != null && pageSize != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            if (page == 1) {
                time = TimeUtil.getNowTime();
            }

            List<Map<String, Object>> list = groupService.findGroupList(schoolId, groupName, time);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
            map.put("time", time);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findGroupMember_g")
    public void back_api_findGroupMember_g(Integer page, Integer pageSize, Integer groupId,
                                           HttpServletResponse response) {
        if (page != null && pageSize != null && groupId != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = groupService.findGroupMembers(groupId);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findGroupNoticeAll")
    public void back_api_findGroupNoticeAll(Integer groupId, HttpServletResponse response) {
        if (groupId != null) {

            List<Map<String, Object>> noticeList = groupService.findGroupNotices(groupId, null, null);

            for (Map<String, Object> noticeMap : noticeList) {

                List<Map<String, Object>> questionList = groupService
                        .findNoticeQuestions(Integer.parseInt(noticeMap.get("id").toString()), null);

                for (Map<String, Object> questionMap : questionList) {
                    List<Map<String, Object>> replyList = groupService
                            .findQuestionReplys(Integer.parseInt(questionMap.get("id").toString()));
                    questionMap.put("replyList", replyList);
                }

                noticeMap.put("questionList", questionList);

                List<Map<String, Object>> receivelist = groupService
                        .findNoticeReceives(Integer.parseInt(noticeMap.get("id").toString()), (short) 1);
                List<Map<String, Object>> noreceivelist = groupService
                        .findNoticeReceives(Integer.parseInt(noticeMap.get("id").toString()), (short) 2);

                noticeMap.put("receivelist", receivelist);
                noticeMap.put("noreceivelist", noreceivelist);
            }

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", noticeList));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findGroupNotice_g")
    public void back_api_findGroupNotice_g(Integer groupId, String title, String content,
                                           HttpServletResponse response) {
        if (groupId != null) {

            List<Map<String, Object>> list = groupService.findGroupNotices(groupId, title, content);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findNoticeQuestion_g")
    public void back_api_findNoticeQuestion_g(Integer page, Integer pageSize, Integer noticeId, Long time,
                                              HttpServletResponse response) {
        if (page != null && pageSize != null && noticeId != null && time != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            if (page == 1) {
                time = TimeUtil.getNowTime();
            }

            List<Map<String, Object>> list = groupService.findNoticeQuestions(noticeId, time);

            for (Map<String, Object> map : list) {
                List<Map<String, Object>> replyList = groupService
                        .findQuestionReplys(Integer.parseInt(map.get("id").toString()));
                map.put("replyList", replyList);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
            map.put("time", time);
            map.put("noticeId", noticeId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findQuestionReply_g")
    public void back_api_findQuestionReply_g(Integer questionId, HttpServletResponse response) {
        if (questionId != null) {
            List<Map<String, Object>> list = groupService.findQuestionReplys(questionId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findNoticeReceive_g")
    public void back_api_findNoticeReceive_g(Integer noticeId, HttpServletResponse response) {
        if (noticeId != null) {

            List<Map<String, Object>> receivelist = groupService.findNoticeReceives(noticeId, (short) 1);
            List<Map<String, Object>> noreceivelist = groupService.findNoticeReceives(noticeId, (short) 2);

            Map<String, Object> map = new HashMap<>();
            map.put("noticeId", noticeId);
            map.put("receivelist", receivelist);
            map.put("noreceivelist", noreceivelist);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addGroupNotice_g")
    public void back_api_addGroupNotice_g(Integer groupId, Integer userId, String user, String title, String content,
                                          String attachment, String img, HttpServletResponse response) {
        if (groupId != null && userId != null && StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content)
                && StringUtils.isNotBlank(user)) {

            Group group = new Group();
            group = groupService.getGroupById(groupId);

            if (group.getIsDelete() == 1) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "群组已经解散", null));
                return;
            }

            GroupNotice groupNotice = new GroupNotice();
            Date now = new Date();
            groupNotice.setContent(content);
            groupNotice.setCreateTime(now);
            groupNotice.setGroupId(groupId);
            groupNotice.setImg(img);
            groupNotice.setIsDelete((short) 2);
            groupNotice.setQuestionCount(0);
            groupNotice.setReceiveCount(0);
            groupNotice.setTitle(title);
            groupNotice.setUser(user);
            groupNotice.setUserId(userId);
            groupNotice.setReceiveCount(1);
            groupService.addGroupNotice(groupNotice);

            GroupUser guEntity = new GroupUser();
            guEntity.setGroupId(groupId);
            guEntity.setIsDelete((short) 2);

            List<GroupUser> userList = groupUserService.findGroupUserList(guEntity);

            if (userList.size() > 0) {
                for (GroupUser groupUser : userList) {
                    // 收到表
                    NoticeReceive rEntity = new NoticeReceive();
                    rEntity.setCreateTime(now);
                    rEntity.setGroupId(groupId);
                    rEntity.setNoticeId(groupNotice.getId());

                    User usEntity = new User();
                    usEntity.setId(groupUser.getUserId());
                    usEntity.setIsDelete((short) 2);
                    User groupuser = userService.getUser(usEntity);

                    rEntity.setUser(groupuser.getName());
                    if (groupUser.getUserId() == userId) {
                        rEntity.setIsReceived((short) 1);
                        rEntity.setReceiveTime(now);
                    } else {
                        rEntity.setIsReceived((short) 2);
                    }
                    rEntity.setUserId(groupUser.getUserId());
                    rEntity.setIsDelete((short) 2);
                    noticeReceiveService.addNoticeReceive(rEntity);
                    // 推送
                    Boolean flag = false;
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("id", groupNotice.getId());
                    hm.put("title", group.getName());
                    hm.put("type", 1);
                    if (groupUser.getIsNotify().shortValue() == 2) {
                        // 群组用户的消息免打扰关了收到推送
                        try {
                            flag = PushTool.pushToUser(groupUser.getUserId(), title.toString(), group.getName(), hm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 入推送表
                    Push pEntity = new Push();
                    pEntity.setContent(title.toString());
                    pEntity.setDeviceType(groupuser.getDevType());
                    pEntity.setMessageType((short) 5);
                    pEntity.setObjectId(groupNotice.getId());
                    pEntity.setTitle(group.getName());
                    pEntity.setToken(groupuser.getDevToken());

                    if (flag) {
                        pEntity.setType((short) 10);
                    } else {
                        pEntity.setType((short) 50);
                    }

                    pEntity.setUserId(groupUser.getUserId());
                    pushSerivce.addPush(pEntity);

                    // 1.5版本引入通知汇总表
                    NoticeInfo noticeInfo = new NoticeInfo();
                    noticeInfo.setType(NoticeType.GROUP_NOTICE.getCode().shortValue());
                    noticeInfo.setBizId(groupNotice.getId());
                    noticeInfo.setUserId(groupUser.getUserId());
                    noticeInfo.setIsDelete((short) 2);
                    noticeInfoMapper.insertSelective(noticeInfo);
                }
            }

            if (StringUtils.isNotBlank(attachment)) {
                String[] urls = attachment.split(",");

                for (String url : urls) {
                    TCptDataInfo fileInfo = new TCptDataInfo();
                    fileInfo.setId(url);
                    List<TCptDataInfo> fs = groupService.findTcpList(fileInfo);

                    if (fs != null && fs.size() == 1) {
                        fileInfo = fs.get(0);
                        NoticeAttachment noticeAttachment = new NoticeAttachment();
                        noticeAttachment.setCreateTime(now);
                        noticeAttachment.setGroupId(groupId);
                        noticeAttachment.setIsDelete((short) 2);
                        noticeAttachment.setName(fileInfo.getFileName());
                        noticeAttachment.setNoticeId(groupNotice.getId());
                        noticeAttachment.setSize(fileInfo.getFileSize().intValue());
                        noticeAttachment.setUrl(url);

                        File byte2File = NginxFileUtils.get(url);
                        try {
                            String md5 = Md5.md5(byte2File);
                            noticeAttachment.setMd5File(md5);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        groupService.addNoticeAttachment(noticeAttachment);
                    }
                }
            }

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 创建临时群组（一次性通知的群组） 群成员自动加入群
     *
     * @param userId     创建人ID
     * @param userIdList 被邀请成员ID
     * @param response
     */
    @RequestMapping("back_api_addTempGroup")
    public void back_api_addTempGroup(Integer userId, String userIdList, HttpServletResponse response) {
        if (userId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        if (StringUtils.isBlank(userIdList)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "请选择被被通知人", null));
            return;
        }

        // 获取创建人信息
        User user = userService.getById(userId);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderJson(response, strToMoblieJson);
            return;
        }

        // 校验权限
        boolean permission = groupTmpCreatePermissionService.getGroupTmpCreatePermission(user.getSchoolId(), 2, userId);
        if (!permission) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "没有创建临时群组的权限", null));
            return;
        }

        // 获取学校信息
        School school = schoolService.getById(user.getSchoolId());

        String groupName = "临时群组[" + RandomUtil.randomNumber(0, 6) + "]";
        groupName = groupService.checkGroupName(groupName);
        // 创建群
        Group group = new Group();
        group.setHeadimg("/default/group_default_avator.png");
        group.setSchoolId(school.getId());
        group.setSchool(school.getName());
        group.setNumber(school.getId() + RandomUtil.randomNumber(0, 6));
        group.setName(groupName);
        group.setCreatorId(userId);
        group.setCreateTime(new Date());
        group.setMemberCount(1);
        int result = groupService.addGroup(group);

        if (result > 0) {
            boolean success = true;
            // 成员自动加入群
            // 创建者
            GroupUser groupUser = new GroupUser();
            groupUser.setGroupId(group.getId());
            groupUser.setUserId(userId);
            groupUser.setType((short) 1);
            groupUser.setCreateTime(new Date());
            groupUser.setBecomeAdminTime(new Date());

            try {
                groupUserService.insert(groupUser);
            } catch (Exception e) {
                success = false;
            }

            String[] ids = userIdList.split(",");
            for (String id : ids) {
                // 邀请入群的成员
                int ID = Integer.valueOf(id);
                if (ID != userId) {
                    // 避免创建人被多次加入
                    groupUser = new GroupUser();
                    groupUser.setGroupId(group.getId());
                    groupUser.setUserId(Integer.valueOf(id));
                    groupUser.setType((short) 3);
                    groupUser.setCreateTime(new Date());
                    try {
                        groupUserService.insert(groupUser);
                    } catch (Exception e) {
                        success = false;
                    }
                }
            }

            if (success) {
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", group));
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "失败", null));
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "失败", null));
        }
    }

    @RequestMapping("/back_api_deleteGroupNotice")
    public void back_api_deleteGroupNotice(Integer noticeId, HttpServletResponse response) {

        if (noticeId != null) {
            GroupNotice getGroupNotice = groupService.getGroupNotice(noticeId);

            if (getGroupNotice == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "通知不存在", null));
                return;
            }

            Group group = new Group();
            group = groupService.getGroupById(getGroupNotice.getGroupId());

            if (group.getIsDelete() == 1) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "群组已经解散", null));
                return;
            }

            GroupNotice groupNotice = new GroupNotice();
            groupNotice.setIsDelete((short) 1);
            groupNotice.setId(noticeId);
            groupNotice.setDeleteTime(new Date());
            groupService.deleteGroupNotice(groupNotice);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_reply")
    public void back_api_reply(Integer noticeId, Integer questionId, Integer userId, String content, Integer toId,
                               Integer toUserId, HttpServletResponse response) {
        if (noticeId != null && questionId != null && userId != null && StringUtils.isNotBlank(content) && toId != null
                && toUserId != null) {

            GroupNotice getGroupNotice = groupService.getGroupNotice(noticeId);

            if (getGroupNotice == null) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "通知不存在", null));
                return;
            }

            Group group = new Group();
            group = groupService.getGroupById(getGroupNotice.getGroupId());

            if (group.getIsDelete() == 1) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "群组已经解散", null));
                return;
            }

            QuestionReply questionReply = new QuestionReply();
            questionReply.setContent(content);
            questionReply.setCreateTime(new Date());
            questionReply.setIsDelete((short) 2);
            questionReply.setNoticeId(noticeId);
            questionReply.setQuestionId(questionId);
            questionReply.setToId(toId);
            questionReply.setToUserId(toUserId);
            questionReply.setUserId(userId);
            groupService.addQuestionReply(questionReply);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_inviteJoinGroup")
    public void back_api_inviteJoinGroup(Integer userId, String receiveIds, Integer groupId,
                                         HttpServletResponse response) {
        if (userId != null && groupId != null && StringUtils.isNotBlank(receiveIds)) {

            Group group = new Group();
            group = groupService.getGroupById(groupId);

            if (group.getIsDelete() == 1) {
                ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "群组已经解散", null));
                return;
            }

            String[] ids = receiveIds.split(",");

            Date now = new Date();

            for (String id : ids) {
                GroupMessage groupMessage = new GroupMessage();
                groupMessage.setGroupId(groupId);
                groupMessage.setIsDetele((short) 2);
                groupMessage.setIsRead((short) 2);
                groupMessage.setStatus((short) 1);
                groupMessage.setType((short) 2);
                groupMessage.setUserId(userId);
                groupMessage.setReceiverId(Integer.parseInt(id));

                if (groupService.getGroupMessageCount(groupMessage) > 0) {
                    continue;
                }

                groupMessage.setCreateTime(now);

                User receiveUser = userService.getUserById(Integer.parseInt(id));

                if (receiveUser != null) {
                    groupMessage.setRecceiver(receiveUser.getName());
                }

                User invitorUser = userService.getUserById(userId);
                if (invitorUser != null) {
                    groupMessage.setUser(invitorUser.getName());
                }
                groupService.addGroupMessage(groupMessage);

                PushUtils.addToPushTable(invitorUser.getName() + "邀请您加入群组" + group == null ? "" : group.getName(), null,
                        receiveUser.getDevType(), (short) 4, receiveUser.getDevToken(), receiveUser.getId(), null);
            }

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_getGroupInfo")
    public void back_api_getGroupInfo(Integer id, HttpServletResponse response) {
        if (id != null) {

            Group group = groupService.getGroupById(id);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", group));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }
}
