package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.Tool.SpringContextUtils;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.text.SimpleDateFormat;
import java.util.*;

// @WebListener
public class NoticeAndPostListener implements ServletContextListener {

    Log logger = LogFactory.getLog(this.getClass());
    public static ServletContext context;
    public Timer timer1;
    public Timer timer2;
    public Timer timer3;

    public NoticeAndPostListener() {
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        if (timer1 != null) timer1.cancel();
        if (timer2 != null) timer2.cancel();
        if (timer3 != null) timer3.cancel();
    }

    public void contextInitialized(ServletContextEvent sce) {
        logger.info("-----进入监听----");
        //根据数据库时间进行推送
        initTimerTask();
        //5分钟更新一次帖子热度,和最新回复时间到数据库
        updateHotAndCmt();
        //一天更新一次帖子评论数点赞数
        updateCmtAndLike();
    }

    public void initTimerTask() {
        timer1 = new Timer(true);
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                final JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtils.getBean("JdbcTemplate");
                final UserService userService = (UserService) SpringContextUtils.getBean("UserService");
                final GroupUserService groupUserService = (GroupUserService) SpringContextUtils.getBean("GroupUserService");
                final GroupNoticeService groupNoticeService = (GroupNoticeService) SpringContextUtils.getBean("GroupNoticeService");
                final PushSerivce pushSerivce = (PushSerivce) SpringContextUtils.getBean("PushSerivce");
                while (true) {
                    try {
                        logger.info("-----进入推送定时器----");
                        Thread.sleep(60000);
                        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("SELECT bnc.id,bnc.is_open,bnc.user_id,bnc.notice_id,bgn.title FROM biu_notice_calendar bnc LEFT JOIN biu_group_notice bgn ON bnc.notice_id =bgn.id WHERE bnc.remind_time = DATE_FORMAT(NOW(), '%H:%i') and DATE_FORMAT(bnc.remind_date,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') and bnc.is_delete = 2");
                        if (queryForList.size() > 0) {
                            for (Map<String, Object> map : queryForList) {
                                int user_id = Integer.parseInt(map.get("user_id").toString());
                                int notice_id = Integer.parseInt(map.get("notice_id").toString());
                                int is_open = Integer.parseInt(map.get("is_open").toString());

                                User uEntity = new User();
                                uEntity.setId(user_id);
                                uEntity.setIsDelete((short) 2);
                                User user = userService.getUser(uEntity);

                                GroupNotice nEntity = new GroupNotice();
                                nEntity.setId(notice_id);
                                nEntity.setIsDelete((short) 2);
                                GroupNotice groupNotice = groupNoticeService.getGroupNotice(nEntity);

                                GroupUser guEntity = new GroupUser();
                                guEntity.setGroupId(groupNotice.getGroupId());
                                guEntity.setUserId(user_id);
                                GroupUser groupUser = groupUserService.getGroupUser(guEntity);

                                if (groupUser != null && groupUser.getIsNotify().shortValue() == 2 && is_open == 1) {
                                    //推送,入推送表
                                    Boolean flag = false;
                                    HashMap<String, Object> hm = new HashMap<String, Object>();
                                    hm.put("id", map.get("id"));
                                    hm.put("title", map.get("title").toString());
                                    hm.put("type", 2);
                                    //群免打扰关闭并且个人的允许提问推送开
                                    try {
                                        flag = PushTool.pushToUser(user_id, map.get("title").toString(), "日历事件", hm);
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    //入推送表
                                    Push pEntity = new Push();
                                    pEntity.setContent(map.get("title").toString());
                                    pEntity.setDeviceType(user.getDevType());
                                    pEntity.setMessageType((short) 3);
                                    pEntity.setObjectId(Integer.valueOf(map.get("id").toString()));
                                    pEntity.setTitle("日历事件");
                                    pEntity.setToken(user.getDevToken());
                                    if (flag) {
                                        pEntity.setType((short) 10);
                                    } else {
                                        pEntity.setType((short) 50);
                                    }
                                    pEntity.setUserId(user_id);
                                    pushSerivce.addPush(pEntity);
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }, 60000);
    }

    public void updateCmtAndLike() {
        timer3 = new Timer(true);
        timer3.schedule(new TimerTask() {

            @Override
            public void run() {
                final JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtils.getBean("JdbcTemplate");
                final PostService postService = (PostService) SpringContextUtils.getBean("PostService");
                final PostCommentService postCommentService = (PostCommentService) SpringContextUtils.getBean("PostCommentService");
                while (true) {
                    try {
                        logger.info("-----进入帖子推送定时器----");
                        Thread.sleep(60000);
                        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("SELECT id FROM biu_post WHERE is_delete = 2");
                        if (queryForList.size() > 0) {
                            for (Map<String, Object> map : queryForList) {
                                //帖子评论点赞数
                                int id = Integer.parseInt(map.get("id").toString());
                                String comment_num = RedisUtils.getValueByKey("post_comment_" + id);
                                String like_num = RedisUtils.getValueByKey("post_like_" + id);
                                Post pEntity = new Post();
                                pEntity.setId(id);
                                if (StringUtils.isNotBlank(comment_num)) {
                                    pEntity.setCommentNumber(Integer.parseInt(comment_num));
                                }
                                if (StringUtils.isNotBlank(like_num)) {
                                    pEntity.setLikeNumber(Integer.parseInt(like_num));
                                }
                                pEntity.setModifyTime(new Date());
                                postService.updatePost(pEntity);

                                //楼层帖子点赞
                                List<Map<String, Object>> likeForList = jdbcTemplate.queryForList("SELECT id AS floor_id FROM biu_post_comment WHERE post_id = " + id + " AND is_delete = 2");
                                for (Map<String, Object> likeMap : likeForList) {
                                    int floor_id = Integer.parseInt(likeMap.get("floor_id").toString());
                                    String comment_like_num = RedisUtils.getValueByKey("post_like_" + id + "floor_like_" + floor_id);
                                    PostComment pcEntity = new PostComment();
                                    pcEntity.setId(floor_id);
                                    if (StringUtils.isNotBlank(comment_like_num)) {
                                        pcEntity.setLikeCount(Integer.valueOf(comment_like_num));
                                        postCommentService.updatePostComment(pcEntity);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }, 60000 * 5);
    }

    public void updateHotAndCmt() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); //凌晨1点  
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        timer2 = new Timer(true);
        timer2.schedule(new TimerTask() {

            @Override
            public void run() {
                final JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtils.getBean("JdbcTemplate");
                final PostService postService = (PostService) SpringContextUtils.getBean("PostService");
                while (true) {
                    try {
                        logger.info("-----进入帖子推送定时器----");
                        Thread.sleep(60000);
                        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("SELECT id FROM biu_post WHERE is_delete = 2");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (queryForList.size() > 0) {
                            for (Map<String, Object> map : queryForList) {
                                int id = Integer.parseInt(map.get("id").toString());
                                String hot_num = RedisUtils.getValueByKey("post_hot_" + id);
                                String cmt_time = RedisUtils.getValueByKey("post_reply_" + id);
                                Post pEntity = new Post();
                                pEntity.setId(id);
                                pEntity.setHot(Integer.parseInt(hot_num));
                                pEntity.setNewestCmtTime(sdf.parse(cmt_time));
                                postService.updatePost(pEntity);
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }, 60000 * 60 * 24);
    }

    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
