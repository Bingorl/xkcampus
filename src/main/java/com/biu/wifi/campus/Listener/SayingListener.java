package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.Tool.SpringContextUtils;
import com.biu.wifi.campus.dao.model.Saying;
import com.biu.wifi.campus.dao.model.SayingComment;
import com.biu.wifi.campus.service.SayingCommentService;
import com.biu.wifi.campus.service.SayingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.*;

// @WebListener
public class SayingListener implements ServletContextListener {

    Log logger = LogFactory.getLog(this.getClass());

    public static ServletContext context;

    public Timer timer1;

    public SayingListener() {

    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if (timer1 != null) timer1.cancel();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        logger.info("-----进入监听----");
        //每天凌晨更新新鲜事浏览量、新鲜事点赞数、新鲜事评论点赞数、新鲜事评论数
        updateRedis();
    }

    public void updateRedis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点  
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //第一次执行定时任务的时间  
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }

        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                final SayingService sayingService = (SayingService) SpringContextUtils.getBean("SayingService");
                final SayingCommentService sayingCommentService = (SayingCommentService) SpringContextUtils.getBean("SayingCommentService");

                try {
                    logger.info("-----进入定时器----");

                    //获取新鲜事列表
                    Saying saying = new Saying();
                    saying.setIsDelete((short) 2);
                    List<Saying> sayingList = sayingService.findList(saying);

                    if (sayingList != null && sayingList.size() > 0) {
                        for (Saying ss : sayingList) {
                            int id = ss.getId().intValue();
                            //新鲜事浏览量
                            String watch_count = RedisUtils.getValueByKey("saying_watch_count_" + id);
                            //点赞数
                            String like_number = RedisUtils.getValueByKey("saying_like_" + id);
                            //评论数
                            String comment_number = RedisUtils.getValueByKey("saying_comment_" + id);

                            //更新到新鲜事表
                            Saying entity = new Saying();
                            entity.setId(id);
                            entity.setWatchCount(Integer.valueOf(watch_count));
                            entity.setLikeNumber(Integer.valueOf(like_number));
                            entity.setCommentNumber(Integer.valueOf(comment_number));
                            sayingService.update(entity);
                        }
                    }

                    //获取新鲜事评论列表
                    SayingComment sayingComment = new SayingComment();
                    sayingComment.setIsDelete((short) 2);
                    List<SayingComment> commentList = sayingCommentService.findList(sayingComment);

                    if (commentList != null && commentList.size() > 0) {
                        for (SayingComment comment : commentList) {
                            int id = comment.getId().intValue();
                            //新鲜事评论点赞数
                            String like_count = RedisUtils.getValueByKey("saying_comment_like_" + id);

                            //更新到新鲜事评论表
                            SayingComment entity = new SayingComment();
                            entity.setId(id);
                            entity.setLikeCount(Integer.valueOf(like_count));
                            sayingCommentService.update(entity);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, date, 3600000 * 24);
    }

    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

}
