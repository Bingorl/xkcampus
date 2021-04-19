package com.biu.wifi.campus.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.model.Saying;
import com.biu.wifi.campus.dao.model.SayingComment;
import com.biu.wifi.campus.dao.model.SayingReply;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.BackendSayingService;
import com.biu.wifi.campus.service.BackendUserService;
import com.biu.wifi.campus.service.SayingCommentService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;

@Controller
public class BackendSayingController {

    @Autowired
    private BackendSayingService sayingService;

    @Autowired
    private SayingCommentService sayingCommentService;

    @Autowired
    private BackendUserService userService;

    @RequestMapping("/back_api_findSayingList")
    public void back_api_findSayingList(Integer page, Integer pageSize, String creatorName, Integer creatorId, Long time, String content, String startTime, String endTime, HttpServletResponse response) {
        if (page != null && pageSize != null && time != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            if (page == 1) {
                time = TimeUtil.getNowTime();
            }

            List<Map<String, Object>> list = sayingService.findSayingList(creatorId, content, startTime, endTime, time, creatorName);

            if (list != null && list.size() > 0) {
                for (Map<String, Object> sayingMap : list) {
                    //新鲜事id
                    Integer sayingId = Integer.valueOf(sayingMap.get("id").toString());

                    //浏览量watch_count  如果redis里面不为空
                    if (null != RedisUtils.getValueByKey("saying_watch_count_" + sayingId)) {
                        int watch_count = Integer.parseInt(RedisUtils.getValueByKey("saying_watch_count_" + sayingId).toString());
                        sayingMap.put("watchCount", watch_count);
                    }
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
            map.put("time", time);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findSayingComment")
    public void back_api_findSayingComment(Integer page, Integer pageSize, Integer sayingId, Long time, HttpServletResponse response) {
        if (page != null && pageSize != null && sayingId != null && time != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            if (page == 1) {
                time = TimeUtil.getNowTime();
            }

            List<Map<String, Object>> list = sayingService.findSayingComments(sayingId, time);

            for (Map<String, Object> map : list) {
                List<Map<String, Object>> replyList = sayingService.findSayingReplys(Integer.parseInt(map.get("id").toString()));
                map.put("replyList", replyList);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
            map.put("time", time);
            map.put("sayingId", sayingId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_findSayingReply")
    public void back_api_findSayingReply(Integer commentId, HttpServletResponse response) {
        if (commentId != null) {
            List<Map<String, Object>> list = sayingService.findSayingReplys(commentId);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteSayingComment")
    public void back_api_deleteSayingComment(Integer commentId, HttpServletResponse response) {
        if (commentId != null) {
            SayingComment sayingComment = new SayingComment();
            sayingComment.setId(commentId);

            SayingComment getSayingComment = sayingCommentService.get(sayingComment);

            if (getSayingComment != null) {
                Saying saying = new Saying();
                saying.setId(getSayingComment.getSayingId());
                saying = sayingService.getSaying(saying);
                saying.setCommentNumber(saying.getCommentNumber() - 1);
                sayingService.updateSaying(saying);
            }

            sayingComment.setIsDelete((short) 1);
            sayingComment.setDeleteTime(new Date());
            sayingService.deleteSayingComment(sayingComment);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteSayingReply")
    public void back_api_deleteSayingReply(Integer replyId, HttpServletResponse response) {
        if (replyId != null) {
            SayingReply sayingReply = new SayingReply();
            sayingReply.setId(replyId);

            SayingReply getSayingReply = sayingService.getSayingReply(sayingReply);

            if (getSayingReply != null) {
                Saying saying = new Saying();
                saying.setId(getSayingReply.getSayingId());
                saying = sayingService.getSaying(saying);
                saying.setCommentNumber(saying.getCommentNumber() - 1);
                sayingService.updateSaying(saying);
            }

            sayingReply.setIsDelete((short) 1);
            sayingReply.setDeleteTime(new Date());
            sayingService.deleteSayingCommentReply(sayingReply);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    @RequestMapping("/back_api_deleteSaying")
    public void back_api_deleteSaying(Integer sayingId, HttpServletResponse response) {
        if (sayingId != null) {

            Saying saying = new Saying();
            saying.setId(sayingId);

            Saying getSaying = sayingService.getSaying(saying);

            if (getSaying != null) {
                User user = new User();
                user.setId(getSaying.getCreatorId());
                User getUser = userService.getUser(user);
                user.setNewsNum(getUser.getNewsNum() - 1);
                userService.updateUser(user);
            }

            saying.setIsDelete((short) 1);
            saying.setDeleteTime(new Date());
            sayingService.updateSaying(saying);

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }
}
