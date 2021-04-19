package com.biu.wifi.campus.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.biu.wifi.campus.Tool.EmojiUtil;
import com.biu.wifi.campus.Tool.PushUtils;
import com.biu.wifi.campus.dao.model.FeedBack;
import com.biu.wifi.campus.dao.model.Grade;
import com.biu.wifi.campus.dao.model.Help;
import com.biu.wifi.campus.dao.model.Institute;
import com.biu.wifi.campus.dao.model.Major;
import com.biu.wifi.campus.dao.model.Message;
import com.biu.wifi.campus.dao.model.PostComment;
import com.biu.wifi.campus.dao.model.Report;
import com.biu.wifi.campus.dao.model.School;
import com.biu.wifi.campus.dao.model.SystemMessage;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.BackendOtherManageService;
import com.biu.wifi.campus.service.BackendSchoolService;
import com.biu.wifi.campus.service.BackendUserService;
import com.biu.wifi.campus.service.PostCommentService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;

@Controller
public class BackendOtherManageController {

    @Autowired
    private BackendOtherManageService otherManageService;

    @Autowired
    private BackendUserService userService;

    @Autowired
    private BackendSchoolService schoolService;

    @Autowired
    private PostCommentService postCommentService;

    @RequestMapping("/back_api_findReportList")
    public void back_api_findReportList(Integer page, Integer pageSize, Integer schoolId,
                                        HttpServletResponse response) {
        if (page != null && pageSize != null && schoolId != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = otherManageService.findReportList(schoolId);

            for (Map<String, Object> map : list) {
                if (Short.parseShort(map.get("type").toString()) == 4) {
                    PostComment postComment = new PostComment();
                    postComment.setId(Integer.parseInt(map.get("otherId").toString()));
                    postComment = postCommentService.getPostComment(postComment);

                    if (null != postComment) {
                        map.put("postId", postComment.getPostId());
                    } else {
                        map.put("postId", null);
                    }

                    School school = schoolService.getSchoolById(Integer.parseInt(map.get("school_id").toString()));

                    if (null != school) {
                        map.put("schoolName", school.getName());
                    } else {
                        map.put("schoolName", null);
                    }
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_markFeedbackAsRead")
    public void back_api_markFeedbackAsRead(Integer id, HttpServletResponse response) {
        if (id != null) {

            FeedBack feedBack = new FeedBack();
            feedBack.setIsRead((short) 1);
            feedBack.setId(id);
            otherManageService.updateFeedback(feedBack);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_markReportAsRead")
    public void back_api_markReportAsRead(Integer id, HttpServletResponse response) {
        if (id != null) {
            Report report = new Report();
            report.setHasHandle((short) 1);
            report.setId(id);
            otherManageService.updateReport(report);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findFeedbackList")
    public void back_api_findFeedbackList(Integer page, Integer pageSize, HttpServletResponse response) {
        if (page != null && pageSize != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<Map<String, Object>> list = otherManageService.findFeedbackList();

            for (Map<String, Object> map : list) {
                map.put("content", EmojiUtil.resolveToEmojiFromByte(map.get("content").toString()));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findHelpList")
    public void back_api_findHelpList(Integer page, Integer pageSize, HttpServletResponse response) {
        if (page != null && pageSize != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            Help help = new Help();
            help.setIsDelete((short) 2);
            List<Help> list = otherManageService.findHelpList(help);

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_addHelpRecord")
    public void back_api_addHelpRecord(String title, String content, HttpServletResponse response) {
        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content)) {

            Help help = new Help();
            help.setContent(content);
            help.setCreateTime(new Date());
            help.setUpdateTime(new Date());
            help.setTitle(title);
            help.setIsDelete((short) 2);
            otherManageService.addHelp(help);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_updateHelpRecord")
    public void back_api_updateHelpRecord(Integer id, String title, String content, HttpServletResponse response) {
        if (id != null && StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content)) {

            Help help = new Help();
            help.setId(id);
            help.setContent(content);
            help.setTitle(title);
            help.setUpdateTime(new Date());
            otherManageService.updateHelp(help);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteHelpRecord")
    public void back_api_deleteHelpRecord(Integer id, HttpServletResponse response) {
        if (id != null) {

            Help help = new Help();
            help.setId(id);
            help.setIsDelete((short) 1);
            help.setDeleteTime(new Date());
            otherManageService.updateHelp(help);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findSystemMessageList")
    public void back_api_findSystemMessageList(Integer page, Integer pageSize, HttpServletResponse response) {
        if (page != null && pageSize != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            List<SystemMessage> list = otherManageService.findSystemMessageList();

            for (SystemMessage systemMessage : list) {
                String receiverIds = systemMessage.getReceiverIds();
                String nameStr = "";

                if (StringUtils.isNotBlank(receiverIds)) {
                    String[] idArray = receiverIds.split(",");

                    for (String id : idArray) {
                        User user = userService.getUserById(Integer.parseInt(id));

                        if (user != null) {
                            nameStr = nameStr + user.getName() + ",";
                        }
                    }
                }

                systemMessage.setReceiverIds(nameStr.equals("") ? "" : nameStr.substring(0, nameStr.length() - 1));

                String receiverUnits = systemMessage.getReceiveUnitIds();
                String unitStr = "";

                if (StringUtils.isNotBlank(receiverUnits)) {
                    String[] idArray = receiverUnits.split(",");

                    for (int i = 0; i < idArray.length; i++) {

                        // school
                        if (i == 0) {
                            School school = schoolService.getSchoolById(Integer.parseInt(idArray[i]));

                            if (school != null) {
                                unitStr = unitStr + school.getName() + "-";
                            }
                        }

                        // institute
                        if (i == 1) {
                            Institute institute = schoolService.getInstituteById(Integer.parseInt(idArray[i]));

                            if (institute != null) {
                                unitStr = unitStr + institute.getName() + "-";
                            }
                        }

                        // major
                        if (i == 2) {
                            Major major = schoolService.getMajorById(Integer.parseInt(idArray[i]));

                            if (major != null) {
                                unitStr = unitStr + major.getName() + "-";
                            }
                        }

                        // grade
                        if (i == 3) {
                            Grade grade = schoolService.getGradeById(Integer.parseInt(idArray[i]));

                            if (grade != null) {
                                unitStr = unitStr + grade.getName() + "-";
                            }
                        }

                        // class
                        if (i == 4) {
                            com.biu.wifi.campus.dao.model.Class clazz = schoolService
                                    .getClassById(Integer.parseInt(idArray[i]));

                            if (clazz != null) {
                                unitStr = unitStr + clazz.getName() + "-";
                            }
                        }
                    }
                }

                systemMessage.setReceiveUnitIds(unitStr.equals("") ? "" : unitStr.substring(0, unitStr.length() - 1));
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("total", PageLimitHolderFilter.getContext().getTotalCount());
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 新增消息
     */
    @RequestMapping("/back_api_addSystemMessage")
    public void back_api_addSystemMessage(String title, Short sendType, String receiveIds, String content,
                                          String receiveUnit, HttpServletResponse response) {
        if (sendType != null && StringUtils.isNotBlank(title) && StringUtils.isNotBlank(content)) {

            if (sendType == 2 && StringUtils.isBlank(receiveUnit) || sendType == 3 && StringUtils.isBlank(receiveIds)) {
                ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
                return;
            }

            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setContent(content);
            systemMessage.setSendTime(new Date());
            systemMessage.setSendType(sendType);
            systemMessage.setIsDetele((short) 2);
            systemMessage.setReceiverIds(receiveIds);
            systemMessage.setReceiveUnitIds(receiveUnit);
            systemMessage.setTitle(title);
            otherManageService.addSystemMessage(systemMessage);

            if (sendType == 1 || sendType == 2) {

                List<User> users = null;

                if (sendType == 1) {
                    users = userService.getAllAvailableUsers();
                } else {
                    String[] idArray = receiveUnit.split(",");

                    Integer schoolId = 0;
                    Integer instituteId = 0;
                    Integer majorId = 0;
                    Integer gradeId = 0;
                    Integer classId = 0;

                    for (int i = 0; i < idArray.length; i++) {

                        // school
                        if (i == 0) {
                            schoolId = Integer.parseInt(idArray[i]);
                        }

                        // institute
                        if (i == 1) {
                            instituteId = Integer.parseInt(idArray[i]);
                        }

                        // major
                        if (i == 2) {
                            majorId = Integer.parseInt(idArray[i]);
                        }

                        // grade
                        if (i == 3) {
                            gradeId = Integer.parseInt(idArray[i]);
                        }

                        // class
                        if (i == 4) {
                            classId = Integer.parseInt(idArray[i]);
                        }
                    }

                    users = userService.findUserListByLevel(schoolId, instituteId, majorId, classId, gradeId);
                }

                if (users != null) {
                    for (User user : users) {
                        int id = user.getId();

                        Message message = new Message();
                        message.setTitle(title);
                        message.setContent(content);
                        message.setUserId(id);
                        message.setOtherId(systemMessage.getId());
                        message.setCreateTime(new Date());
                        message.setIsDetele((short) 2);
                        message.setIsRead((short) 2);
                        otherManageService.addMessage(message);

                        String deviceToken = user.getDevToken();
                        String deviceId = user.getDevId();
                        Short deviceType = user.getDevType();

                        if (StringUtils.isNotBlank(deviceToken) && StringUtils.isNotBlank(deviceId)
                                && deviceType != null) {
                            PushUtils.addToPushTable(title, content, deviceType, (short) 1, deviceToken, id,
                                    systemMessage.getId());
                        }
                    }
                }
            }

            if (sendType == 3) {
                String[] idArray = receiveIds.split(",");

                for (String id : idArray) {
                    Message message = new Message();
                    message.setTitle(title);
                    message.setContent(content);
                    message.setUserId(Integer.parseInt(id));
                    message.setOtherId(systemMessage.getId());
                    message.setCreateTime(new Date());
                    message.setIsDetele((short) 2);
                    message.setIsRead((short) 2);
                    otherManageService.addMessage(message);

                    User publisher = userService.getUserById(Integer.parseInt(id));

                    if (publisher != null) {
                        String deviceToken = publisher.getDevToken();
                        String deviceId = publisher.getDevId();
                        Short deviceType = publisher.getDevType();

                        if (StringUtils.isNotBlank(deviceToken) && StringUtils.isNotBlank(deviceId)
                                && deviceType != null) {
                            PushUtils.addToPushTable(title, content, deviceType, (short) 1, deviceToken,
                                    Integer.parseInt(id), systemMessage.getId());
                        }
                    }
                }
            }

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 根据昵称关键字搜索用户信息
     */
    @RequestMapping("/back_api_getUserInfoByName")
    public void back_api_getUserInfoByName(String name, HttpServletResponse response) {

        List<Map<String, Object>> list = otherManageService.findUserListByName(name);

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

}
