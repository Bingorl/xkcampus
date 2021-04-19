package com.biu.wifi.campus.controller.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.dao.model.Group;
import com.biu.wifi.campus.dao.model.Post;
import com.biu.wifi.campus.dao.model.SayingLike;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.GroupService;
import com.biu.wifi.campus.service.GroupUserService;
import com.biu.wifi.campus.service.PostService;
import com.biu.wifi.campus.service.SayingCommentService;
import com.biu.wifi.campus.service.SayingLikeService;
import com.biu.wifi.campus.service.SayingReplyService;
import com.biu.wifi.campus.service.SayingService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;

@Controller
public class AppShareController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private PostService postService;

    @Autowired
    private FileSupportService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private SayingService sayingService;

    @Autowired
    private SayingLikeService sayingLikeService;

    @Autowired
    private SayingCommentService sayingCommentService;

    @Autowired
    private SayingReplyService sayingReplyService;

    /**
     * 分享群聊页面信息
     *
     * @param request
     * @param response
     * @param group_id
     */
    @RequestMapping("/app_getGroupShare")
    public void app_getGroupShare(HttpServletRequest request, HttpServletResponse response, Integer group_id) {
        if (group_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = new HashMap<>();

        //获取群信息
        Group group = groupService.getById(group_id);

        if (group == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该群已不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (group.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该群已不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        map.put("headimg", group.getHeadimg());
        map.put("name", group.getName());
        map.put("number", group.getNumber());
        map.put("member_count", group.getMemberCount());

        //获取群管理员列表
        List<Map<String, Object>> list = groupUserService.app_findShareGroupUserList(group_id);
        map.put("list", list);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 分享新鲜事页面信息
     *
     * @param request
     * @param response
     * @param saying_id
     */
    @RequestMapping("/app_getSayingShare")
    public void app_getSayingShare(HttpServletRequest request, HttpServletResponse response, Integer saying_id) {
        if (saying_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = sayingService.user_getSayingInfo(null, saying_id);
        if (map == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "新鲜事已不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //是否删除
        int is_delete = Integer.parseInt(map.get("is_delete").toString());
        if (is_delete == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "新鲜事已不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取点赞人列表
        SayingLike entity = new SayingLike();
        entity.setSayingId(saying_id);
        List<SayingLike> likeList = sayingLikeService.findList(entity);
        if (likeList != null && likeList.size() > 3) {
            likeList = likeList.subList(0, 2);
        }
        map.put("likeList", likeList);

        //将浏览量存放进redis
        if (null != RedisUtils.getValueByKey("saying_watch_count_" + saying_id)) {
            int watch_count = Integer.parseInt(RedisUtils.getValueByKey("saying_watch_count_" + saying_id).toString());
            RedisUtils.addValue("saying_watch_count_" + saying_id, String.valueOf(watch_count + 1), null);
        } else {
            RedisUtils.addValue("saying_watch_count_" + saying_id, String.valueOf(Integer.parseInt(map.get("watch_count").toString()) + 1), null);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());

        PageLimitHolderFilter.setContext(1, 10, null);
        List<Map<String, Object>> list = sayingCommentService.user_findSayingCommentList(null, time, saying_id);
        if (list != null && list.size() > 0) {
            for (Map<String, Object> commentMap : list) {
                //评论id
                Integer commentId = Integer.valueOf(commentMap.get("id").toString());

                //获取回复列表
                List<Map<String, Object>> replyList = sayingReplyService.findReplyListFromCommentId(commentId);
                commentMap.put("replyList", replyList);
            }
        }

        map.put("commentList", list);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    @RequestMapping("/app_getPostShare")
    public void app_getPostShare(HttpServletResponse response, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPostAll(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }
        Map<String, Object> postDetail = new HashMap<String, Object>();
        postDetail.put("id", post.getId());
        postDetail.put("create_time", post.getCreateTime());
        postDetail.put("title", post.getTitle());
        postDetail.put("content", post.getContent());
        String width = "";
        if (StringUtils.isNotBlank(post.getImg())) {
            for (int i = 0; i < post.getImg().split(",").length; i++) {
                String img_id = post.getImg().split(",")[i];
                FileIoEntity info = fileService.getInfo(img_id);
                width = img_id + "|" + info.getDataInfo().getDescription() + ",";
            }
            width = width.substring(0, width.length() - 1);
        }
        postDetail.put("img_width", width);
        postDetail.put("img", post.getImg());
        //redis拿点赞数,要判空,为空的话取数据库的,不为空取redis中的
        Integer like_num = null;
        if (StringUtils.isBlank(RedisUtils.getValueByKey("post_like_" + id))) {
            like_num = post.getLikeNumber();
        } else {
            like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + id).toString());
        }
        postDetail.put("like_number", like_num);
        postDetail.put("user_id", post.getCreatorId());
        User uEntity = new User();
        uEntity.setId(post.getCreatorId());
        User user = userService.getUser(uEntity);
        if (null != user) {
            postDetail.put("name", user.getName());
            postDetail.put("headimg", user.getHeadimg());
        } else {
            postDetail.put("name", "");
            postDetail.put("headimg", "");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());
        //楼层列表
        List<Map<String, Object>> postFloorList = null;

        PageLimitHolderFilter.setContext(1, 20, null);
        postFloorList = postService.findPostFloorList(2, id, 1, now, null);

        for (Map<String, Object> map : postFloorList) {
            //楼层id
            Integer floor_id = Integer.valueOf(map.get("id").toString());
            String width2 = "";
            if (map.get("img") != null && StringUtils.isNotBlank(map.get("img").toString())) {
                for (int i = 0; i < map.get("img").toString().split(",").length; i++) {
                    String img_id = map.get("img").toString().split(",")[i];
                    FileIoEntity info = fileService.getInfo(img_id);
                    width2 = img_id + "|" + info.getDataInfo().getDescription() + ",";
                }
                width2 = width2.substring(0, width2.length() - 1);
            }
            map.put("img_width", width2);

            //根据楼层id查回复列表
            List<Map<String, Object>> postReplyList = postService.findPostReplyList(floor_id, now);
            map.put("replyNum", postReplyList.size());
            PageLimitHolderFilter.setContext(1, 20, null);
            List<Map<String, Object>> replyList = postService.findPostReplyList(floor_id, now);
            map.put("replyList", replyList);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("postDetail", postDetail);
        result.put("floorList", postFloorList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 获取固定的url
     *
     * @param response
     */
    @RequestMapping("/app_getUrl")
    public void app_getUrl(HttpServletResponse response) {
        String serUrl = "https://app.54xy.com/";

        Map<String, Object> map = new HashMap<String, Object>();
        //关于我们
        map.put("aboutUs", serUrl + "admin/aboutUs");
        //用户协议
        map.put("agreement", serUrl + "admin/agreement");

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }
}
