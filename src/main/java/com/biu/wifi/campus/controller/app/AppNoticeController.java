package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.*;
import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.biu.wifi.dao.TCptDataInfoMapper;
import com.biu.wifi.model.TCptDataInfo;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AppNoticeController extends AuthenticatorController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GroupNoticeService groupNoticeService;
    @Autowired
    private NoticeReceiveService noticeReceiveService;
    @Autowired
    private NoticeCalendarService noticeCalendarService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeAttachmentService noticeAttachmentService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private PostService postService;
    @Autowired
    private FollowService followService;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private FileSupportService fileService;
    @Autowired
    private TCptDataInfoMapper dataInfoDao;
    @Autowired
    private NoticeQuestionService noticeQuestionService;
    @Autowired
    private QuestionReplyService questionReplyService;
    @Autowired
    private QuestionMessageService questionMessageService;
    @Autowired
    private PushSerivce pushSerivce;
    @Autowired
    private LeaveInfoService leaveInfoService;
    @Autowired
    private NoticeInfoService noticeInfoService;
    @Autowired
    private ClassroomBookAuditService classroomBookAuditService;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;
    @Autowired
    private ExamArrangeNoticeService examArrangeNoticeService;
    @Autowired
    private TeacherLeaveNoticeService teacherLeaveNoticeService;
    @Autowired
    private DiscussionTopicNoticeService discussionTopicNoticeService;

    public static void main(String[] args) {
        Short a = 1;
        int b = 1;
        System.out.println(a == b);
    }

    /**
     * 通知列表
     *
     * @param response
     * @param user_id
     * @param currentVersion
     * @param search_word
     * @param page
     * @param pageSize
     * @param type
     * @param date
     * @param group_id
     * @param time
     */
    @RequestMapping("/user_noticeList")
    public void user_noticeList(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                @ModelAttribute("version") String version,
                                String search_word, Integer page, Integer pageSize, Integer type, String date, Integer group_id,
                                String time) {
        if (page == null || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }
        Integer pagesize = pageSize == null ? 10 : pageSize;

        List<Map<String, Object>> noticeList;
        if (convertVersionToDouble(version) <= 1.4) {
           /*
            *由于涉及到的群消息通知和请假审批通知不在同一张表中
            * 因此分页操作放在业务层进行
            PageLimitHolderFilter.setContext(page, pagesize, null);*/
            noticeList = groupNoticeService.app_noticeList(search_word, type, date, group_id, now,
                    user_id);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            if (noticeList.size() > 0) {
                for (Map<String, Object> map : noticeList) {
                    // 通知id
                    int id = Integer.parseInt(map.get("id").toString());
                    map.put("noticeType", 1);//消息通知
                    // 根据是否登录,判断是否收到,是否添加日历事件,未登录默认未收到
                    if (null != user_id) {
                        NoticeReceive rEntity = new NoticeReceive();
                        rEntity.setNoticeId(id);
                        rEntity.setUserId(user_id);
                        NoticeReceive noticeReceive = noticeReceiveService.getNoticeReceive(rEntity);
                        // 1:已收到 2:未收到
                        if (null != noticeReceive && noticeReceive.getIsReceived().shortValue() == 1) {
                            map.put("is_received", 1);
                        } else {
                            map.put("is_received", 2);
                        }
                        NoticeCalendar cEntity = new NoticeCalendar();
                        cEntity.setIsDelete((short) 2);
                        cEntity.setUserId(user_id);
                        cEntity.setNoticeId(id);
                        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
                        if (null != noticeCalendar) {
                            map.put("is_calendar", 1);
                            if (null != noticeCalendar.getRemindDate()) {
                                map.put("remind_date", sdf1.format(noticeCalendar.getRemindDate()));
                            } else {
                                map.put("remind_date", "");
                            }
                            if (null != noticeCalendar.getRemindTime()) {
                                map.put("remind_time", sdf2.format(noticeCalendar.getRemindTime()));
                            } else {
                                map.put("remind_time", "");
                            }
                            map.put("event_id", noticeCalendar.getId());
                        } else {
                            map.put("is_calendar", 2);
                            map.put("remind_date", "");
                            map.put("remind_time", "");
                            map.put("event_id", "");
                        }
                    } else {
                        map.put("is_received", 2);
                        map.put("is_calendar", 2);
                        map.put("remind_date", "");
                        map.put("remind_time", "");
                        map.put("event_id", "");
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("time", now);
            int notReadNum = groupNoticeService.getNotReadNotice(user_id);

            if (convertVersionToDouble(version) == 1.3) {
                //请假审批通知
                List<Map<String, Object>> leaveAuditNoticeMapList = leaveInfoService.findLeaveNoticeMapList(user_id);
                for (Map<String, Object> map : leaveAuditNoticeMapList) {
                    //统计未读请假审批通知的条数
                    if (map.get("is_received").toString().equals("2")) {
                        notReadNum++;
                    }
                }
                noticeList.addAll(leaveAuditNoticeMapList);
                //按照时间倒叙排列
                Collections.sort(noticeList, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        return o2.get("create_time").toString().compareTo(o1.get("create_time").toString());
                    }
                });
                //判断用户是否有待办审核
                boolean toDoAudit = user.getIsTeacher() == 1 ? true : false;
                result.put("toDoAudit", toDoAudit);
            }

            //业务层进行分页
            noticeList = PageHelperUtil.getPageList(page, pagesize, noticeList);
            result.put("notice_list", noticeList);
            result.put("notRead_num", notReadNum);
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
        } else {
            //1.4版本后将各类通知汇总到biu_notice_info表中
            int notReadNum = 0;
            noticeList = new ArrayList<>();
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<NoticeInfo> auditInfoList = noticeInfoService.findList(user_id);
            for (NoticeInfo noticeInfo : auditInfoList) {
                //统计未读请假审批通知的条数
                Map<String, Object> map;
                if (NoticeType.GROUP_NOTICE.getCode().shortValue() == noticeInfo.getType()) {
                    //群组通知
                    map = groupNoticeService.findGroupNoticeMapById(noticeInfo.getBizId(), user_id);
                } else if (NoticeType.CHAT_NOTICE.getCode().shortValue() == noticeInfo.getType()) {
                    //私信通知
                    map = null;
                } else if (NoticeType.STUDENT_LEAVE_NOTICE.getCode().shortValue() == noticeInfo.getType()) {
                    // 学生请假通知
                    map = leaveInfoService.findLeaveNoticeMapById(noticeInfo.getBizId());
                } else if (NoticeType.CLASSROOM_BOOK_NOTICE.getCode().shortValue() == noticeInfo.getType()) {
                    //教室预约审批通知
                    map = classroomBookAuditService.findAuditNoticeById(noticeInfo.getBizId());
                } else if (NoticeType.EXAM_PLAN_NOTICE.getCode().shortValue() == noticeInfo.getType()) {
                    //智能排考审批通知
                    map = examArrangeNoticeService.findExamArrangeNoticeMapById(noticeInfo.getBizId());
                }else if (NoticeType.TEACHER_LEAVE_NOTICE.getCode().shortValue() == noticeInfo.getType()) {
                    // TODO 教师请假
                    map = teacherLeaveNoticeService.findLeaveNoticeMapById(noticeInfo.getBizId());
                } else if (NoticeType.DISCUSSION_TOPIC_APPLY_NOTICE.getCode().shortValue() == noticeInfo.getType()) {
                    // TODO 会议议题申请
                    map = discussionTopicNoticeService.findNoticeMapById(noticeInfo.getBizId());
                } else {
                    //其他审批类型
                    map = null;
                }

                if (map.get("is_received").toString().equals("2")) {
                    notReadNum++;
                }
                noticeList.add(map);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("time", now);
            result.put("notice_list", noticeList);
            result.put("notRead_num", notReadNum);
            //判断用户是否有待办审核
            boolean toDoAudit = user.getIsTeacher() == 1 ? true : false;
            result.put("toDoAudit", toDoAudit);
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
        }
    }

    /**
     * 通知详情
     *
     * @param response
     * @param id
     */
    @RequestMapping("/user_noticeInfo")
    public void user_noticeInfo(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer
            id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity1 = new User();
        uEntity1.setId(user_id);
        User user1 = userService.getUser(uEntity1);
        if (null != user1 && user1.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        GroupNotice nEntity = new GroupNotice();
        nEntity.setId(id);
        nEntity.setIsDelete((short) 2);
        GroupNotice groupNotice = groupNoticeService.getGroupNotice(nEntity);
        if (null == groupNotice) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(10001, "通知不存在!", null)));
            return;
        }
        result.put("create_time", groupNotice.getCreateTime());
        User uEntity = new User();
        uEntity.setId(groupNotice.getUserId());
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        result.put("user_id", groupNotice.getUserId());
        if (null != user) {
            result.put("name", user.getName());
            result.put("headimg", user.getHeadimg());
            result.put("huanxin_id", user.getHuanxinId());
        } else {
            result.put("name", "");
            result.put("headimg", "");
            result.put("huanxin_id", "");
        }
        result.put("title", groupNotice.getTitle());
        result.put("content", groupNotice.getContent());
        result.put("img", groupNotice.getImg());
        String width = "";
        if (StringUtils.isNotBlank(groupNotice.getImg())) {
            for (int i = 0; i < groupNotice.getImg().split(",").length; i++) {
                String img_id = groupNotice.getImg().split(",")[i];
                FileIoEntity info = fileService.getInfo(img_id);
                if (null != info && info.getDataInfo() != null) {
                    width += img_id + "|" + info.getDataInfo().getDescription() + ",";
                } else {
                    width += img_id + ",";
                }
            }
            width = width.substring(0, width.length() - 1);
        }
        result.put("img_width", width);
        result.put("question_number", groupNotice.getQuestionCount());
        NoticeAttachment aEntity = new NoticeAttachment();
        aEntity.setNoticeId(id);
        aEntity.setIsDelete((short) 2);
        List<NoticeAttachment> attachmentList = noticeAttachmentService.findNoticeAttachmentList(aEntity);
        result.put("attachmentList", attachmentList);
        result.put("group_id", groupNotice.getGroupId());
        Group gEntity = new Group();
        gEntity.setId(groupNotice.getGroupId());
        gEntity.setIsDelete((short) 2);
        Group group = groupService.getGroup(gEntity);
        if (null != group) {
            result.put("group_img", group.getHeadimg());
            result.put("group_name", group.getName());
        } else {
            result.put("group_img", "");
            result.put("group_name", "");
        }
        // 未收到个数和未收到的人列表
        NoticeReceive rEntity = new NoticeReceive();
        rEntity.setIsReceived((short) 2);
        rEntity.setNoticeId(id);
        List<NoticeReceive> notReceiveList = noticeReceiveService.findNoticeReceiveList(rEntity);
        result.put("not_received_num", notReceiveList.size());
        result.put("not_received_list", notReceiveList);
        // 收到总数
        NoticeReceive nrEntity = new NoticeReceive();
        nrEntity.setNoticeId(id);
        List<NoticeReceive> noticeReceiveList = noticeReceiveService.findNoticeReceiveList(nrEntity);
        result.put("received_all", noticeReceiveList.size());
        // 根据是否登录,判断是否收到,是否添加日历事件,未登录默认未收到
        if (null != user_id) {
            NoticeReceive entity = new NoticeReceive();
            entity.setNoticeId(id);
            entity.setUserId(user_id);
            NoticeReceive noticeReceive = noticeReceiveService.getNoticeReceive(entity);
            // 1:已收到 2:未收到
            if (null != noticeReceive && noticeReceive.getIsReceived().shortValue() == 1) {
                result.put("is_received", 1);
            } else {
                result.put("is_received", 2);
            }
            NoticeCalendar cEntity = new NoticeCalendar();
            cEntity.setIsDelete((short) 2);
            cEntity.setUserId(user_id);
            cEntity.setNoticeId(id);
            NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
            if (null != noticeCalendar) {
                result.put("is_calendar", 1);
                if (null != noticeCalendar.getRemindDate()) {
                    result.put("remind_date", sdf1.format(noticeCalendar.getRemindDate()));
                } else {
                    result.put("remind_date", "");
                }
                if (null != noticeCalendar.getRemindTime()) {
                    result.put("remind_time", sdf2.format(noticeCalendar.getRemindTime()));
                } else {
                    result.put("remind_time", "");
                }
                result.put("event_id", noticeCalendar.getId());
            } else {
                result.put("is_calendar", 2);
                result.put("remind_date", "");
                result.put("remind_time", "");
                result.put("event_id", "");
            }
        } else {
            result.put("is_received", 2);
            result.put("is_calendar", 2);
            result.put("remind_date", "");
            result.put("remind_time", "");
            result.put("event_id", "");
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 通知提问列表
     *
     * @param response
     * @param id
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/user_noticeCommentList")
    public void user_noticeCommentList(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                       Integer id, Integer page, Integer pageSize, String time) {
        if (id == null || page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }
        // 提问列表
        Integer pagesize = pageSize == null ? 10 : pageSize;
        PageLimitHolderFilter.setContext(page, pagesize, null);
        List<Map<String, Object>> noticeQuestionList = groupNoticeService.findNoticeQuestionList(id, now);
        if (noticeQuestionList.size() > 0) {
            for (Map<String, Object> map : noticeQuestionList) {
                // 提问id
                Integer question_id = Integer.parseInt(map.get("id").toString());
                // 根据提问id查回复列表
                List<Map<String, Object>> questionReplyList = groupNoticeService.findQuestionReplyList(question_id,
                        null);
                map.put("replyNum", questionReplyList.size());
                if (questionReplyList.size() > 5) {
                    map.put("is_more", 1);
                } else {
                    map.put("is_more", 2);
                }
                pagesize = 5;
                PageLimitHolderFilter.setContext(page, pagesize, null);
                List<Map<String, Object>> replyList = groupNoticeService.findQuestionReplyList(question_id, now);
                map.put("replyList", replyList);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("commentList", noticeQuestionList);
        result.put("time", now);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 提问回复列表,产品说不要分页,展开折叠全展示
     *
     * @param response
     * @param id
     * @param time
     */
    @RequestMapping("/user_noticeReplyList")
    public void user_noticeReplyList(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                     Integer id, String time, Integer page, Integer pageSize) {
        if (id == null || page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }

        Integer pagesize = pageSize == null ? 10 : pageSize;
        PageLimitHolderFilter.setContext(page, pagesize, null);
        List<Map<String, Object>> questionReplyList = groupNoticeService.findQuestionReplyList(id, now);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", questionReplyList);
        result.put("time", now);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 收到详情
     *
     * @param response
     * @param id
     */
    @RequestMapping("/user_receivedInfo")
    public void user_receivedInfo(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                  Integer id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        // 收到列表
        List<Map<String, Object>> receivedList = groupNoticeService.findReceivedList(id, (short) 1);
        // 未收到列表
        List<Map<String, Object>> notReceivedList = groupNoticeService.findReceivedList(id, (short) 2);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("receivedList", receivedList);
        result.put("notReceivedList", notReceivedList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 确认收到
     *
     * @param response
     * @param id
     * @param user_id
     */
    @RequestMapping("/user_confirmReceived")
    public void user_confirmReceived(HttpServletResponse response, Integer id,
                                     @ModelAttribute("user_id") Integer user_id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        NoticeReceive rEntity = new NoticeReceive();
        rEntity.setNoticeId(id);
        rEntity.setUserId(user_id);
        NoticeReceive noticeReceive = noticeReceiveService.getNoticeReceive(rEntity);
        if (null == noticeReceive) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "收到不存在!", null)));
            return;
        }
        NoticeReceive nrEntity = new NoticeReceive();
        nrEntity.setNoticeId(id);
        nrEntity.setUserId(user_id);
        nrEntity.setIsReceived((short) 1);
        NoticeReceive receive = noticeReceiveService.getNoticeReceive(nrEntity);
        if (null != receive) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您已确认收到,不能再次确认!", null)));
            return;
        }
        NoticeReceive entity = new NoticeReceive();
        entity.setId(noticeReceive.getId());
        entity.setIsReceived((short) 1);// 1:已收到
        entity.setReceiveTime(new Date());
        noticeReceiveService.updateNoticeReceive(entity);
        // 修改通知的收到个数
        GroupNotice nEntity = new GroupNotice();
        nEntity.setId(id);
        nEntity.setIsDelete((short) 2);
        GroupNotice notice = groupNoticeService.getGroupNotice(nEntity);
        if (null != notice && null != notice.getReceiveCount()) {
            nEntity.setReceiveCount(notice.getReceiveCount().intValue() + 1);
        } else {
            nEntity.setReceiveCount(1);
        }
        groupNoticeService.updateGroupNotice(nEntity);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 获取日历事件
     *
     * @param response
     * @param id
     * @param remind_date
     * @param user_id
     */
    @RequestMapping("/user_getCalendarInfo")
    public void user_getCalendarInfo(HttpServletResponse response, Integer id, String remind_date,
                                     @ModelAttribute("user_id") Integer user_id) {
        if (id == null || StringUtils.isBlank(remind_date)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        NoticeCalendar cEntity = new NoticeCalendar();
        cEntity.setIsDelete((short) 2);
        cEntity.setNoticeId(id);
        cEntity.setUserId(user_id);
        try {
            cEntity.setRemindDate(sdf.parse(remind_date));
        } catch (ParseException e) {

            e.printStackTrace();
        }
        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
        if (null != noticeCalendar) {
            result.put("id", noticeCalendar.getId());
            result.put("remind_time", noticeCalendar.getRemindTime());
            result.put("remark", noticeCalendar.getRemark());
            result.put("is_open", noticeCalendar.getIsOpen());
        } else {
            result.put("id", "");
            result.put("remind_time", "");
            result.put("remark", "");
            result.put("is_open", "");
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 添加日历事件
     *
     * @param response
     * @param id
     * @param remind_date
     * @param remind_time
     * @param remark
     * @param is_open
     * @param user_id
     */
    @RequestMapping("/user_addCalendar")
    public void user_addCalendar(HttpServletResponse response, Integer id, String remind_date, String remind_time,
                                 String remark, Short is_open, @ModelAttribute("user_id") Integer user_id) {
        if (id == null || StringUtils.isBlank(remind_date) || StringUtils.isBlank(remind_time) || is_open == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        NoticeCalendar cEntity = new NoticeCalendar();
        cEntity.setNoticeId(id);
        cEntity.setUserId(user_id);
        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
        // 通知,人,日期,存在就更新,不存在就新增
        if (null != noticeCalendar) {
            noticeCalendar.setIsDelete((short) 2);
            noticeCalendar.setIsOpen(is_open);
            noticeCalendar.setRemark(remark);
            try {
                noticeCalendar.setRemindTime(sdf1.parse(remind_time));
            } catch (ParseException e) {

                e.printStackTrace();
            }
            try {
                cEntity.setRemindDate(sdf.parse(remind_date));
            } catch (ParseException e) {

                e.printStackTrace();
            }
            noticeCalendarService.updateNoticeCalendar(noticeCalendar);
        } else {
            cEntity.setIsDelete((short) 2);
            cEntity.setIsOpen(is_open);
            cEntity.setRemark(remark);
            try {
                cEntity.setRemindTime(sdf1.parse(remind_time));
            } catch (ParseException e) {

                e.printStackTrace();
            }
            try {
                cEntity.setRemindDate(sdf.parse(remind_date));
            } catch (ParseException e) {

                e.printStackTrace();
            }
            cEntity.setCreateTime(new Date());
            noticeCalendarService.addNoticeCalendar(cEntity);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 事件详情
     *
     * @param response
     * @param id
     * @param user_id
     */
    @RequestMapping("/user_getEventInfo")
    public void user_getEventInfo(HttpServletResponse response, Integer id,
                                  @ModelAttribute("user_id") Integer user_id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity1 = new User();
        uEntity1.setId(user_id);
        User user1 = userService.getUser(uEntity1);
        if (null != user1 && user1.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        NoticeCalendar cEntity = new NoticeCalendar();
        cEntity.setId(id);
        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
        if (null == noticeCalendar) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "日历不存在!", null)));
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", noticeCalendar.getId());
        if (null != noticeCalendar.getRemindTime()) {
            result.put("remind_time", sdf1.format(noticeCalendar.getRemindTime()));
        } else {
            result.put("remind_time", "");
        }
        if (null != noticeCalendar.getRemindDate()) {
            result.put("remind_date", sdf.format(noticeCalendar.getRemindDate()));
        } else {
            result.put("remind_date", "");
        }
        result.put("remark", noticeCalendar.getRemark());
        result.put("is_open", noticeCalendar.getIsOpen());
        result.put("notice_id", noticeCalendar.getNoticeId());
        User uEntity = new User();
        uEntity.setId(noticeCalendar.getUserId());
        User user = userService.getUser(uEntity);
        if (user != null && user.getIsDelete().shortValue() == 2 && user.getName() != null) {
            result.put("user_name", user.getName());
        } else {
            result.put("user_name", "");
        }
        GroupNotice nEntity = new GroupNotice();
        nEntity.setId(noticeCalendar.getNoticeId());
        nEntity.setIsDelete((short) 2);
        GroupNotice notice = groupNoticeService.getGroupNotice(nEntity);
        if (null != notice) {
            result.put("title", notice.getTitle());
            result.put("create_time", notice.getCreateTime());
            result.put("content", notice.getContent());
        } else {
            result.put("title", "");
            result.put("create_time", "");
            result.put("content", "");
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 修改事件
     *
     * @param response
     * @param id
     * @param remind_date
     * @param remind_time
     * @param remark
     * @param is_open
     * @param user_id
     */
    @RequestMapping("/user_updateEvent")
    public void user_updateEvent(HttpServletResponse response, Integer id, String remind_date, String remind_time,
                                 String remark, Short is_open, @ModelAttribute("user_id") Integer user_id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        // 传哪个修改哪个
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        NoticeCalendar cEntity = new NoticeCalendar();
        cEntity.setId(id);
        cEntity.setIsDelete((short) 2);
        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
        if (null == noticeCalendar) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "日历不存在!", null)));
            return;
        }
        noticeCalendar.setIsOpen(is_open);
        noticeCalendar.setRemark(remark);
        if (StringUtils.isNotBlank(remind_time)) {
            try {
                noticeCalendar.setRemindTime(sdf1.parse(remind_time));
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }
        noticeCalendarService.updateNoticeCalendar(noticeCalendar);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 取消事件
     *
     * @param response
     * @param id
     * @param user_id
     */
    @RequestMapping("/user_cancleEvent")
    public void user_cancleEvent(HttpServletResponse response, Integer id, @ModelAttribute("user_id") Integer
            user_id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        NoticeCalendar cEntity = new NoticeCalendar();
        cEntity.setId(id);
        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
        if (null == noticeCalendar) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "日历不存在!", null)));
            return;
        }
        if (user_id.intValue() != noticeCalendar.getUserId()) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不是你的事件,不能取消!", null)));
            return;
        }
        cEntity.setIsDelete((short) 1);
        cEntity.setDeleteTime(new Date());
        noticeCalendarService.updateNoticeCalendar(cEntity);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 首页搜索
     *
     * @param response
     * @param type
     * @param user_id
     * @param search_word
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/app_search")
    public void app_search(HttpServletResponse response, Integer type, @ModelAttribute("user_id") Integer user_id,
                           String search_word, Integer page, Integer pageSize, String time) {
        if (type == null || StringUtils.isBlank(search_word) || page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        if (type.intValue() != 2) {
            // 除了帖子,其他的搜索都必须登录才能搜
            if (null == user_id) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
                return;
            }
            // 所有通知模块是必须登录,并且是实名认证之后才能看的
            User uEntity = new User();
            uEntity.setId(user_id);
            User user = userService.getUser(uEntity);
            if (null != user && user.getIsAuth().shortValue() != 1) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
                return;
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }
        Integer pagesize = pageSize == null ? 10 : pageSize;
        if (type.intValue() == 1) {
            // 通知
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<Map<String, Object>> noticeList = groupNoticeService.app_noticeList(search_word, 0, null, null, now,
                    user_id);
            if (noticeList.size() > 0) {
                for (Map<String, Object> map : noticeList) {
                    // 通知id
                    int id = Integer.parseInt(map.get("id").toString());
                    // 根据是否登录,判断是否收到,是否添加日历事件,未登录默认未收到
                    if (null != user_id) {
                        NoticeReceive rEntity = new NoticeReceive();
                        rEntity.setNoticeId(id);
                        rEntity.setUserId(user_id);
                        NoticeReceive noticeReceive = noticeReceiveService.getNoticeReceive(rEntity);
                        // 1:已收到 2:未收到
                        if (null != noticeReceive && noticeReceive.getIsReceived().shortValue() == 1) {
                            map.put("is_received", 1);
                        } else {
                            map.put("is_received", 2);
                        }
                        NoticeCalendar cEntity = new NoticeCalendar();
                        cEntity.setIsDelete((short) 2);
                        cEntity.setUserId(user_id);
                        cEntity.setNoticeId(id);
                        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
                        if (null != noticeCalendar) {
                            map.put("is_calendar", 1);
                            map.put("remind_date", sdf1.format(noticeCalendar.getRemindDate()));
                            map.put("remind_time", sdf2.format(noticeCalendar.getRemindTime()));
                            map.put("event_id", noticeCalendar.getId());
                        } else {
                            map.put("is_calendar", 2);
                            map.put("remind_date", "");
                            map.put("remind_time", "");
                            map.put("event_id", "");
                        }
                    } else {
                        map.put("is_received", 2);
                        map.put("is_calendar", 2);
                        map.put("remind_date", "");
                        map.put("remind_time", "");
                        map.put("event_id", "");
                    }
                }
            }
            result.put("noticeList", noticeList);
        } else if (type.intValue() == 2) {
            // 帖子标题,内容,楼搜索
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<Map<String, Object>> postListByName = postService.findPostListByName(search_word, now);
            result.put("postList", postListByName);
        } else if (type.intValue() == 3) {
            // 动态,新鲜事
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<Map<String, Object>> sayingList = postService.findSayingListByName(search_word, now, user_id);
            result.put("sayingList", sayingList);
        } else if (type.intValue() == 4) {
            // 用户
            User uEntity = new User();
            uEntity.setName(search_word);
            uEntity.setType((short) 1);
            uEntity.setIsDelete((short) 2);
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<User> userList = userService.findList(uEntity);
            List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
            if (userList.size() > 0) {
                for (User user : userList) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", user.getId());
                    map.put("name", user.getName());
                    map.put("headimg", user.getHeadimg());
                    map.put("signature", user.getSignature());
                    // 登录状态下判断我是否关注这个人
                    if (null != user_id) {
                        Follow fEntity = new Follow();
                        fEntity.setFollowerId(user_id);
                        fEntity.setBeFollowedId(user.getId());
                        fEntity.setIsCancel((short) 2);
                        Follow follow = followService.getFollow(fEntity);
                        if (null != follow) {
                            map.put("is_follow", 1);
                        } else {
                            map.put("is_follow", 2);
                        }
                    } else {
                        map.put("is_follow", 2);
                    }
                    rList.add(map);
                }
            }
            result.put("userList", rList);
        } else if (type.intValue() == 5) {
            // 群组
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<Map<String, Object>> groupList = postService.findGroupListByname(search_word, now);
            if (groupList.size() > 0) {
                for (Map<String, Object> map : groupList) {
                    Integer group_id = Integer.valueOf(map.get("id").toString());
                    // 登录状态下判断我是否加入过这个群组
                    if (null != user_id) {
                        GroupUser guEntity = new GroupUser();
                        guEntity.setGroupId(group_id);
                        guEntity.setIsDelete((short) 2);
                        guEntity.setUserId(user_id);
                        GroupUser groupUser = groupUserService.getGroupUser(guEntity);
                        if (null != groupUser) {
                            map.put("is_join", 1);
                        } else {
                            map.put("is_join", 2);
                        }
                    }
                }
            }
            result.put("groupList", groupList);
        } else if (type.intValue() == 6) {
            // 公共主页
            User uEntity = new User();
            uEntity.setName(search_word);
            uEntity.setType((short) 2);
            uEntity.setIsDelete((short) 2);
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<User> userList = userService.findList(uEntity);
            List<Map<String, Object>> rList = new ArrayList<Map<String, Object>>();
            if (userList.size() > 0) {
                for (User user : userList) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", user.getId());
                    map.put("name", user.getName());
                    map.put("headimg", user.getHeadimg());
                    map.put("inroduction", user.getIntroduction());
                    // 登录状态下判断我是否关注这个人
                    if (null != user_id) {
                        Follow fEntity = new Follow();
                        fEntity.setFollowerId(user_id);
                        fEntity.setBeFollowedId(user.getId());
                        fEntity.setIsCancel((short) 2);
                        Follow follow = followService.getFollow(fEntity);
                        if (null != follow) {
                            map.put("is_follow", 1);
                        } else {
                            map.put("is_follow", 2);
                        }
                    } else {
                        map.put("is_follow", 2);
                    }
                    rList.add(map);
                }
            }
            result.put("pageList", rList);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 群发通知
     *
     * @param response
     * @param user_id
     */
    @RequestMapping("/user_addNotice")
    public void user_addNotice(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, @ModelAttribute("version") String version) {
        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity1 = new User();
        uEntity1.setId(user_id);
        User user1 = userService.getUser(uEntity1);
        if (null != user1 && user1.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        Object title = param.get("title");
        Object content = param.get("content");
        Object group_id = param.get("group_id");
        List<DiskFileItem> imgList = null;
        List<DiskFileItem> attachList = null;
        try {
            imgList = (List<DiskFileItem>) param.get("img");
            attachList = (List<DiskFileItem>) param.get("attachment");
        } catch (Exception e) {
            imgList = null;
            attachList = null;
        }
        if (null == title || null == content || null == group_id) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        if (null != imgList && imgList.size() > 3) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "图片数量超过3张!", null)));
            return;
        }
        if (null != attachList && attachList.size() > 3) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "附件数量超过3张!", null)));
            return;
        }
        if (title.toString().length() < 4 || title.toString().length() > 40) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "通知标题字数不满足要求!", null)));
            return;
        }
        if (content.toString().length() < 5 || content.toString().length() > 500) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "通知内容字数不满足要求!", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在!", null)));
            return;
        }
        Group gEntity = new Group();
        gEntity.setId(Integer.valueOf(group_id.toString()));
        gEntity.setIsDelete((short) 2);
        Group group = groupService.getGroup(gEntity);
        if (null == group) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "群组不存在!", null)));
            return;
        }

        GroupUser gu = new GroupUser();
        gu.setGroupId(Integer.valueOf(group_id.toString()));
        gu.setUserId(user_id);
        gu.setIsDelete((short) 2);
        GroupUser groupUser1 = groupUserService.getGroupUser(gu);
        if (null != groupUser1 && groupUser1.getType().shortValue() == 3) {
            // 普通成员不能发通知
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不是管理员或群主,不能群发通知!", null)));
            return;
        }

        // 入通知表
        GroupNotice nEntity = new GroupNotice();
        nEntity.setContent(content.toString());
        nEntity.setCreateTime(new Date());
        nEntity.setGroupId(Integer.valueOf(group_id.toString()));
        nEntity.setIsDelete((short) 2);
        nEntity.setQuestionCount(0);
        nEntity.setReceiveCount(1);
        nEntity.setTitle(title.toString());
        nEntity.setUser(user.getName());
        nEntity.setUserId(user_id);
        if (null != imgList) {
            String filePath = "";
            for (DiskFileItem diskFileItem : imgList) {
                byte[] fileContent = diskFileItem.get();
                String fileName = FileUtilsEx.getFileNameByPath(diskFileItem.getName());
                String fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
                filePath += fileid + ",";
            }
            filePath = filePath.substring(0, filePath.length() - 1);
            nEntity.setImg(filePath);
        }
        // 返回通知id
        int i = groupNoticeService.addGroupNotice(nEntity);
        // 通知表入库成功之后再入附件表
        if (i > 0 && null != attachList) {
            for (DiskFileItem diskFileItem : attachList) {
                byte[] fileContent = diskFileItem.get();
                String fileName = FileUtilsEx.getFileNameByPath(diskFileItem.getName());
                String fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
                TCptDataInfo dataInfo = dataInfoDao.selectByPrimaryKey(fileid);
                NoticeAttachment aEntity = new NoticeAttachment();
                aEntity.setCreateTime(new Date());
                aEntity.setGroupId(Integer.valueOf(group_id.toString()));
                aEntity.setIsDelete((short) 2);
                aEntity.setName(fileName);
                aEntity.setNoticeId(i);
                aEntity.setSize(dataInfo.getFileSize().intValue());
                aEntity.setUrl(fileid);
                File byte2File = NginxFileUtils.byte2File(fileContent, "", fileName);
                try {
                    String md5 = Md5.md5(byte2File);
                    aEntity.setMd5File(md5);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                noticeAttachmentService.addNoticeAttachment(aEntity);
            }
        }
        // 查询群里的每个人,入到通知表
        GroupUser guEntity = new GroupUser();
        guEntity.setGroupId(Integer.valueOf(group_id.toString()));
        guEntity.setIsDelete((short) 2);
        List<GroupUser> userList = groupUserService.findGroupUserList(guEntity);
        if (userList.size() > 0) {
            for (GroupUser groupUser : userList) {
                // 收到表
                NoticeReceive rEntity = new NoticeReceive();
                rEntity.setCreateTime(new Date());
                rEntity.setGroupId(Integer.valueOf(group_id.toString()));
                // 发通知的人默认收到
                if (groupUser.getUserId().intValue() == user_id.intValue()) {
                    rEntity.setIsReceived((short) 1);
                    rEntity.setReceiveTime(new Date());
                } else {
                    rEntity.setIsReceived((short) 2);
                }
                rEntity.setIsDelete((short) 2);
                rEntity.setNoticeId(i);
                User usEntity = new User();
                usEntity.setId(groupUser.getUserId());
                usEntity.setIsDelete((short) 2);
                User groupuser = userService.getUser(usEntity);
                if (null != groupuser) {
                    rEntity.setUser(groupuser.getName());
                    rEntity.setUserId(groupUser.getUserId());
                } else {
                    rEntity.setUser("");
                    rEntity.setUserId(groupUser.getUserId());
                }

                if (groupuser != null) {
                    // 该用户没有被删除的时候才会进行推送和收到通知
                    noticeReceiveService.addNoticeReceive(rEntity);

                    // 推送
                    Boolean flag = false;
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("id", i);
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
                    pEntity.setObjectId(i);
                    pEntity.setTitle(group.getName());
                    pEntity.setToken(groupuser.getDevToken());
                    if (flag) {
                        pEntity.setType((short) 10);
                    } else {
                        pEntity.setType((short) 50);
                    }
                    pEntity.setUserId(groupUser.getUserId());
                    pushSerivce.addPush(pEntity);


                    if (convertVersionToDouble(version) > 1.4) {
                        // 1.5版本引入通知汇总表
                        NoticeInfo noticeInfo = new NoticeInfo();
                        noticeInfo.setType((short) 1);
                        noticeInfo.setBizId(i);
                        noticeInfo.setUserId(groupUser.getUserId());
                        noticeInfo.setIsDelete((short) 2);
                        noticeInfoMapper.insertSelective(noticeInfo);
                    }
                }
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 群搜索通知,附件
     *
     * @param response
     * @param user_id
     * @param search_word
     * @param page
     * @param pageSize
     * @param type
     * @param group_id
     * @param time
     */
    @RequestMapping("/user_searchByGroup")
    public void user_searchByGroup(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                   String search_word, Integer page, Integer pageSize, Integer type, Integer group_id, String time) {
        if (page == null || type == null || group_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        if (type.intValue() == 0) {
            // 通知的时候,搜索内容必填
            if (StringUtils.isBlank(search_word)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
                return;
            }
        }

        // 所有通知模块是必须登录,并且是实名认证之后才能看的
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (null != user && user.getIsAuth().shortValue() != 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在或者未通过实名认证!", null)));
            return;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }
        Integer pagesize = pageSize == null ? 10 : pageSize;

        if (type.intValue() == 0) {
            // 通知
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<Map<String, Object>> noticeList = groupNoticeService.app_noticeList(search_word, 0, null, group_id,
                    now, user_id);
            if (noticeList.size() > 0) {
                for (Map<String, Object> map : noticeList) {
                    // 通知id
                    int id = Integer.parseInt(map.get("id").toString());
                    // 根据是否登录,判断是否收到,是否添加日历事件,未登录默认未收到
                    if (null != user_id) {
                        NoticeReceive rEntity = new NoticeReceive();
                        rEntity.setNoticeId(id);
                        rEntity.setUserId(user_id);
                        NoticeReceive noticeReceive = noticeReceiveService.getNoticeReceive(rEntity);
                        // 1:已收到 2:未收到
                        if (null != noticeReceive && noticeReceive.getIsReceived().shortValue() == 1) {
                            map.put("is_received", 1);
                        } else {
                            map.put("is_received", 2);
                        }
                        NoticeCalendar cEntity = new NoticeCalendar();
                        cEntity.setIsDelete((short) 2);
                        cEntity.setUserId(user_id);
                        cEntity.setNoticeId(id);
                        NoticeCalendar noticeCalendar = noticeCalendarService.getNoticeCalendar(cEntity);
                        if (null != noticeCalendar) {
                            map.put("is_calendar", 1);
                            map.put("remind_date", sdf1.format(noticeCalendar.getRemindDate()));
                            map.put("remind_time", sdf2.format(noticeCalendar.getRemindTime()));
                            map.put("event_id", noticeCalendar.getId());
                        } else {
                            map.put("is_calendar", 2);
                            map.put("remind_date", "");
                            map.put("remind_time", "");
                            map.put("event_id", "");
                        }
                    } else {
                        map.put("is_received", 2);
                        map.put("is_calendar", 2);
                        map.put("remind_date", "");
                        map.put("remind_time", "");
                        map.put("event_id", "");
                    }
                }
            }
            result.put("notice_list", noticeList);
        } else {
            // 附件搜索
            PageLimitHolderFilter.setContext(page, pagesize, null);
            List<Map<String, Object>> attachList = groupNoticeService.findNoticeAttachList(search_word, group_id, now,
                    user_id);
            result.put("attachment_list", attachList);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 提问
     *
     * @param response
     * @param user_id
     * @param content
     * @param notice_id
     */
    @RequestMapping("/user_addQuestion")
    public void user_addQuestion(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                 String content, Integer notice_id) {
        if (StringUtils.isBlank(content) || notice_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        // 提问人
        User uEntity = new User();
        uEntity.setId(user_id);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在!", null)));
            return;
        }
        if (content.length() > 500) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "评论字数超过限制!", null)));
            return;
        }
        // 通知发布人
        GroupNotice nEntity = new GroupNotice();
        nEntity.setId(notice_id);
        nEntity.setIsDelete((short) 2);
        GroupNotice groupNotice = groupNoticeService.getGroupNotice(nEntity);

        User tEntity = new User();
        tEntity.setId(groupNotice.getUserId());
        tEntity.setIsDelete((short) 2);
        User to_user = userService.getUser(tEntity);
        if (null == to_user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "通知发布人不存在,暂不能对改通知提问!", null)));
            return;
        }
        NoticeQuestion qEntity = new NoticeQuestion();
        qEntity.setContent(content);
        qEntity.setCreateTime(new Date());
        qEntity.setIsDelete((short) 2);
        qEntity.setNoticeId(notice_id);
        qEntity.setUserId(user_id);
        int question_id = noticeQuestionService.addNoticeQuestion(qEntity);
        // 提问id,新增成功之后更新通知表的提问数
        if (question_id > 0) {

            if (groupNotice != null && groupNotice.getQuestionCount() != null) {
                nEntity.setQuestionCount(groupNotice.getQuestionCount().intValue() + 1);
            } else {
                nEntity.setQuestionCount(1);
            }
            groupNoticeService.updateGroupNotice(nEntity);

            // 入提问消息表
            QuestionMessage mEntity = new QuestionMessage();
            mEntity.setContent(content);
            mEntity.setCreateTime(new Date());
            mEntity.setIsDetele((short) 2);
            mEntity.setIsRead((short) 2);
            mEntity.setObjectId(notice_id);
            mEntity.setOriginContent(groupNotice.getTitle());
            mEntity.setToer(to_user.getName());
            mEntity.setToerId(groupNotice.getUserId());
            mEntity.setType((short) 1);// 主提问
            mEntity.setUser(user.getName());
            mEntity.setUserId(user_id);
            questionMessageService.addQuestionMessage(mEntity);
            // 推送
            GroupUser guEntity = new GroupUser();
            guEntity.setGroupId(groupNotice.getGroupId());
            guEntity.setUserId(groupNotice.getUserId());
            guEntity.setIsDelete((short) 2);
            GroupUser groupUser = groupUserService.getGroupUser(guEntity);
            if (null != groupUser && groupUser.getIsNotify().shortValue() == 2
                    && to_user.getIsPushQuestion().shortValue() == 1) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", groupNotice.getId());
                hm.put("type", 1);
                // 群免打扰关闭并且个人的允许提问推送开
                try {
                    flag = PushTool.pushToUser(groupNotice.getUserId(), content, "您收到一条提问", hm);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                // 入推送表
                Push pEntity = new Push();
                pEntity.setContent(content);
                pEntity.setDeviceType(to_user.getDevType());
                pEntity.setMessageType((short) 5);
                pEntity.setObjectId(notice_id);
                pEntity.setTitle("您收到一条提问");
                pEntity.setToken(to_user.getDevToken());
                if (flag) {
                    pEntity.setType((short) 10);
                } else {
                    pEntity.setType((short) 50);
                }
                pEntity.setUserId(groupUser.getUserId());
                pushSerivce.addPush(pEntity);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", question_id);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 回复提问
     *
     * @param response
     * @param user_id
     * @param content
     * @param reply_id
     * @param to_user_id
     * @param question_id
     * @param notice_id
     */
    @RequestMapping("/user_replyQuestion")
    public void user_replyQuestion(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                   String content, Integer reply_id, Integer to_user_id, Integer question_id, Integer notice_id) {
        if (StringUtils.isBlank(content) || question_id == null || notice_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在!", null)));
            return;
        }
        if (content.length() > 500) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "回复内容字数超过限制!", null)));
            return;
        }
        Date date = new Date();
        QuestionReply rEntity = new QuestionReply();
        rEntity.setContent(content);
        rEntity.setCreateTime(date);
        rEntity.setIsDelete((short) 2);
        rEntity.setNoticeId(notice_id);
        rEntity.setQuestionId(question_id);
        rEntity.setToId(reply_id);
        rEntity.setToUserId(to_user_id);
        rEntity.setUserId(user_id);
        int id = questionReplyService.addQuestionReply(rEntity);

        // 提问
        NoticeQuestion qEntity = new NoticeQuestion();
        qEntity.setId(question_id);
        NoticeQuestion question = noticeQuestionService.getNoticeQuestion(qEntity);
        // 上级提问人或回复人,否则就是传过来的回复人
        if (null == to_user_id) {
            // 提问人
            to_user_id = question.getUserId();
        }

        if (id > 0) {
            User tEntity = new User();
            tEntity.setId(to_user_id);
            tEntity.setIsDelete((short) 2);
            User to_user = userService.getUser(uEntity);
            String og_content = "";
            if (null == reply_id) {
                // reply_id为空,回复的是提问
                og_content = question.getContent();
            } else {
                // reply_id不为空,回复的是回复
                QuestionReply qrEntity = new QuestionReply();
                qrEntity.setId(reply_id);
                QuestionReply reply = questionReplyService.getQuestionReply(qrEntity);
                og_content = reply.getContent();
            }
            // 入提问消息表
            QuestionMessage mEntity = new QuestionMessage();
            mEntity.setContent(content);
            mEntity.setCreateTime(new Date());
            mEntity.setIsDetele((short) 2);
            mEntity.setIsRead((short) 2);
            mEntity.setObjectId(question_id);
            mEntity.setOriginContent(og_content);
            mEntity.setToer(to_user.getName());
            mEntity.setToerId(to_user_id);
            mEntity.setType((short) 2);// 主提问
            mEntity.setUser(user.getName());
            mEntity.setUserId(user_id);
            questionMessageService.addQuestionMessage(mEntity);
            // 推送
            GroupNotice nEntity = new GroupNotice();
            nEntity.setId(notice_id);
            nEntity.setIsDelete((short) 2);
            GroupNotice groupNotice = groupNoticeService.getGroupNotice(nEntity);

            GroupUser guEntity = new GroupUser();
            guEntity.setGroupId(groupNotice.getGroupId());
            guEntity.setUserId(to_user_id);
            guEntity.setIsDelete((short) 2);
            GroupUser groupUser = groupUserService.getGroupUser(guEntity);
            if (null != groupUser && groupUser.getIsNotify().shortValue() == 2
                    && to_user.getIsPushQuestion().shortValue() == 1) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", groupNotice.getId());
                hm.put("title", og_content);
                hm.put("type", 1);
                // 群免打扰关闭并且个人的允许提问推送开
                try {
                    flag = PushTool.pushToUser(to_user_id, content, og_content, hm);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                // 入推送表
                Push pEntity = new Push();
                pEntity.setContent(content);
                pEntity.setDeviceType(to_user.getDevType());
                pEntity.setMessageType((short) 5);
                pEntity.setObjectId(notice_id);
                pEntity.setTitle(og_content);
                pEntity.setToken(to_user.getDevToken());
                if (flag) {
                    pEntity.setType((short) 10);
                } else {
                    pEntity.setType((short) 50);
                }
                pEntity.setUserId(to_user_id);
                pushSerivce.addPush(pEntity);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("time", date);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 删除提问
     *
     * @param response
     * @param user_id
     * @param id
     * @param notice_id
     */
    @RequestMapping("/user_deleteQuestion")
    public void user_deleteQuestion(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                    Integer id, Integer notice_id) {
        if (id == null || notice_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        NoticeQuestion qEntity = new NoticeQuestion();
        qEntity.setId(id);
        qEntity.setUserId(user_id);
        qEntity.setIsDelete((short) 2);
        NoticeQuestion question = noticeQuestionService.getNoticeQuestion(qEntity);
        if (null == question) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "提问不存在!", null)));
            return;
        }
        qEntity.setIsDelete((short) 1);
        qEntity.setDeleteTime(new Date());
        int i = noticeQuestionService.updateNoticeQuestion(qEntity);
        if (i > 0) {
            // 提问删除成功,把通知的提问数-1
            GroupNotice nEntity = new GroupNotice();
            nEntity.setId(notice_id);
            nEntity.setIsDelete((short) 2);
            GroupNotice groupNotice = groupNoticeService.getGroupNotice(nEntity);
            if (groupNotice != null && groupNotice.getQuestionCount() != null
                    && groupNotice.getQuestionCount().intValue() > 0) {
                nEntity.setQuestionCount(groupNotice.getQuestionCount().intValue() - 1);
            } else {
                nEntity.setQuestionCount(0);
            }
            groupNoticeService.updateGroupNotice(nEntity);

            // 提问删除成功,把这个提问下的回复也删了
            QuestionReply rEntity = new QuestionReply();
            rEntity.setQuestionId(id);
            rEntity.setIsDelete((short) 2);
            List<QuestionReply> replyList = questionReplyService.findQuestionReplyList(rEntity);
            if (replyList.size() > 0) {
                for (QuestionReply reply : replyList) {
                    reply.setIsDelete((short) 1);
                    reply.setDeleteTime(new Date());
                    questionReplyService.updateQuestionReply(reply);
                }
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除提问回复
     *
     * @param response
     * @param user_id
     * @param id
     */
    @RequestMapping("/user_deleteReply")
    public void user_deleteReply(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer
            id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        QuestionReply rEntity = new QuestionReply();
        rEntity.setId(id);
        rEntity.setUserId(user_id);
        rEntity.setIsDelete((short) 2);
        QuestionReply reply = questionReplyService.getQuestionReply(rEntity);
        if (null == reply) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "回复不存在!", null)));
            return;
        }
        rEntity.setIsDelete((short) 1);
        rEntity.setDeleteTime(new Date());
        questionReplyService.updateQuestionReply(rEntity);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 获取日历事件列表
     *
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param type
     * @param search_date
     * @param order_type
     */
    @RequestMapping("/user_findCalderNotice")
    public void user_findCalderNotice(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                      Integer page, Integer pageSize, Integer type, String search_date, Integer order_type) {
        if (type == null || StringUtils.isBlank(search_date) || order_type == null || page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        Integer pagesize = pageSize == null ? 10 : pageSize;
        PageLimitHolderFilter.setContext(page, pagesize, null);
        List<Map<String, Object>> calderNoticeList = groupNoticeService.findCalderNoticeList(user_id, type, search_date,
                order_type);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventList", calderNoticeList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 按月获取事件日历
     *
     * @param response
     * @param user_id
     * @param search_date
     */
    @RequestMapping("/user_findCalderIsEvent")
    public void user_findCalderIsEvent(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                       String search_date) {
        if (StringUtils.isBlank(search_date)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        List<Map<String, Object>> isCalderList = groupNoticeService.findIsCalderList(user_id, search_date);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", isCalderList)));
    }
}
