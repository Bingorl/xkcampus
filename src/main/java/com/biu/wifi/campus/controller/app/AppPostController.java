package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.CoreConstants;
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

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AppPostController extends AuthenticatorController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostLikeService postLikeService;
    @Autowired
    private PostSelectService postSelectService;
    @Autowired
    private FileSupportService fileService;
    @Autowired
    private UserService userService;
    @Autowired
    private FollowService followService;
    @Autowired
    private PostCommentLikeService postCommentLikeService;
    @Autowired
    private PostCommentService postCommentService;
    @Autowired
    private PostReplyService postReplyService;
    @Autowired
    private SayingService sayingService;
    @Autowired
    private AtMessageService atMessageService;
    @Autowired
    private CommentReplyMessageService commentReplyMessageService;
    @Autowired
    private LikeMessageService likeMessageService;
    @Autowired
    private PushSerivce pushSerivce;

    /**
     * 置顶公告贴列表
     *
     * @param response
     * @param page
     * @param pageSize
     * @param school_id
     * @param time
     */
    @RequestMapping("/app_topPostList")
    public void app_topPostList(HttpServletResponse response, Integer page, Integer pageSize, Integer school_id,
                                String time) {
        if (page == null || school_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }
        Integer pagesize = 6;
        PageLimitHolderFilter.setContext(page, pagesize, null);
        List<Map<String, Object>> topPostList = postService.findTopPostList(school_id, now);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", topPostList)));
    }

    /**
     * 论坛帖子列表
     *
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param school_id
     * @param type
     * @param search_type
     * @param essence_type
     * @param time
     */
    @RequestMapping("/app_postList")
    public void app_postList(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer page,
                             Integer pageSize, Integer school_id, Integer type, Integer search_type, Integer essence_type, String time) {
        if (page == null || school_id == null || search_type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
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
        List<Map<String, Object>> postList = postService.findLuntanPostList(school_id, type, search_type, essence_type,
                now);
        if (postList.size() > 0 && postList != null) {
            for (Map<String, Object> map : postList) {
                Object img = map.get("img");
                if (null != img) {
                    map.put("img_number", img.toString().split(",").length);
                } else {
                    map.put("img_number", 0);
                }
                Integer post_id = Integer.valueOf(map.get("id").toString());
                // 帖子点赞数,如果redis不为空,拿redis的点赞数,否则直接数据库
                if (null != RedisUtils.getValueByKey("post_like_" + post_id)) {
                    Integer like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + post_id).toString());
                    map.put("like_number", like_num);
                }

                // 帖子评论数,如果redis不为空,拿redis的点赞数,否则直接数据库
                if (null != RedisUtils.getValueByKey("post_comment_" + post_id)) {
                    Integer comment_number = Integer
                            .valueOf(RedisUtils.getValueByKey("post_comment_" + post_id).toString());
                    map.put("comment_number", comment_number);
                }

                // 如果登录,判断是否点赞;未登录默认未点赞
                if (null != user_id) {
                    PostLike lEntity = new PostLike();
                    lEntity.setPostId(post_id);
                    lEntity.setUserId(user_id);
                    PostLike postLike = postLikeService.getPostLike(lEntity);
                    if (null != postLike) {
                        map.put("is_like", 1);
                    } else {
                        map.put("is_like", 2);
                    }
                } else {
                    map.put("is_like", 2);
                }
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("postList", postList);
        result.put("time", now);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 精华分类
     *
     * @param response
     * @param school_id
     */
    @RequestMapping("/app_essenceTypeList")
    public void app_essenceTypeList(HttpServletResponse response, Integer school_id) {
        if (school_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        PostSelect sEntity = new PostSelect();
        sEntity.setIsDelete((short) 2);
        sEntity.setSchoolId(school_id);
        List<PostSelect> findPostSelectList = postSelectService.findPostSelectList(sEntity);

        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", findPostSelectList)));
    }

    /**
     * 发布帖子
     *
     * @param response
     * @param user_id
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/user_addPost")
    public void user_addPost(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        Object school_id = param.get("school_id");
        Object title = param.get("title");
        Object content = param.get("content");
        Object at_user = param.get("at_user");
        Object page_id = param.get("page_id");
        // 精选分类ID
        Object selectTypeId = param.get("selectTypeId");
        Integer userid = 0;
        if (null != page_id) {
            userid = Integer.parseInt(page_id.toString());
        } else {
            userid = user_id;
        }
        List<DiskFileItem> imgList = null;
        try {
            imgList = (List<DiskFileItem>) param.get("img");
        } catch (Exception e) {
            imgList = null;
        }
        if (school_id == null || title == null || content == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(userid);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户不存在!", null)));
            return;
        }
        /*if (title.toString().length() > 20) {
			ServletUtilsEx.renderText(response,
					JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子标题字数不满足要求!", null)));
			return;
		}*/
        if (content.toString().length() > 1000) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子内容字数不满足要求!", null)));
            return;
        }
        if (null != at_user && at_user.toString().split(",").length > 10) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "@人数超过限制!", null)));
            return;
        }
        if (null != imgList && imgList.size() > 9) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子图片超过限制!", null)));
            return;
        }
        Post pEntity = new Post();
        if (null != at_user) {
            pEntity.setAtUser(at_user.toString());
        }
        pEntity.setCommentNumber(0);
        pEntity.setContent(content.toString());
        pEntity.setCreateTime(new Date());
        pEntity.setNewestCmtTime(new Date());
        pEntity.setCreatorId(userid);
        pEntity.setHot(0);
        pEntity.setWatchCount(0);
        if (null != imgList) {
            String filePath = "";
            for (DiskFileItem diskFileItem : imgList) {
                byte[] fileContent = diskFileItem.get();
                String fileName = FileUtilsEx.getFileNameByPath(diskFileItem.getName());
                String fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
                filePath += fileid + ",";
            }
            filePath = filePath.substring(0, filePath.length() - 1);
            pEntity.setImg(filePath);
        }
        pEntity.setIsDelete((short) 2);
        pEntity.setIsSelect((short) 2);
        pEntity.setLikeNumber(0);
        pEntity.setPostType((short) 1);
        pEntity.setSchoolId(Integer.valueOf(school_id.toString()));
        pEntity.setTitle(title.toString());
        pEntity.setWeight(0);
        if (selectTypeId != null) {
            // 有传精选分类ID
            int typeId = Integer.valueOf(String.valueOf(selectTypeId));
            if (typeId != 0) {
                pEntity.setSelectTypeId(typeId);
            }
        }
        int i = postService.addPost(pEntity);
        if (i > 0) {
            // 用户的发布帖子数+1
            if (null != user.getPostNum() && user.getPostNum().intValue() > 0) {
                uEntity.setPostNum(user.getPostNum().intValue() + 1);
            } else {
                uEntity.setPostNum(1);
            }
            userService.updateUser(uEntity);

            // 发布成功,把帖子的热度和回复时间以及点赞数放到redis,默认0
//			RedisUtils.addValue("post_hot_"+i,"0", null);
//			RedisUtils.addValue("post_like_"+i, "0", null);
//			RedisUtils.addValue("post_cmt_"+i, null, null);
            if (null != at_user) {
                List<User> userList = new ArrayList<User>();
                for (int j = 0; j < at_user.toString().split(",").length; j++) {
                    Integer at_user_id = Integer.valueOf(at_user.toString().split(",")[j]);
                    User usEntity = new User();
                    usEntity.setId(at_user_id);
                    User atuser = userService.getUser(usEntity);
                    userList.add(atuser);
                    if (atuser.getIsPushAt().shortValue() == 1 && atuser.getType().shortValue() == 1) {
                        boolean flag = false;
                        HashMap<String, Object> hm = new HashMap<String, Object>();
                        hm.put("id", i);
                        hm.put("title", user.getName() + "@了你");
                        hm.put("type", 5);
                        // 个人的允许@推送开
                        try {
                            flag = PushTool.pushToUser(at_user_id, content.toString(), user.getName() + "@了你", hm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 入推送表
                        Push puEntity = new Push();
                        puEntity.setContent(content.toString());
                        puEntity.setDeviceType(atuser.getDevType());
                        puEntity.setMessageType((short) 5);
                        puEntity.setObjectId(i);
                        puEntity.setTitle(user.getName() + "@了你");
                        puEntity.setToken(atuser.getDevToken());
                        if (flag) {
                            puEntity.setType((short) 10);
                        } else {
                            puEntity.setType((short) 50);
                        }
                        puEntity.setUserId(at_user_id);
                        pushSerivce.addPush(puEntity);
                    }

                }
                // 入@消息表
                atMessageService.insertAll((short) 1, user_id, user.getName(), userList, i, null, title.toString());
            }

            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
        } else {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "失败", null)));
        }

    }

    /**
     * 帖子详情
     *
     * @param response
     * @param user_id
     * @param id
     */
    @RequestMapping("/app_postInfo")
    public void app_postInfo(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(10002, "帖子不存在!", null)));
            return;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", post.getId());
        result.put("create_time", post.getCreateTime());
        result.put("title", post.getTitle());
        result.put("content", post.getContent());

        String width = "";
        if (StringUtils.isNotBlank(post.getImg())) {
            for (int i = 0; i < post.getImg().split(",").length; i++) {
                String img_id = post.getImg().split(",")[i];
                FileIoEntity info = fileService.getInfo(img_id);
                width += img_id + "|" + info.getDataInfo().getDescription() + ",";
            }
            width = width.substring(0, width.length() - 1);
        }
        result.put("img_width", width);
        result.put("img", post.getImg());
        // redis拿点赞数,要判空,为空的话取数据库的,不为空取redis中的
        Integer like_num = null;
        if (null == RedisUtils.getValueByKey("post_like_" + id)) {
            like_num = post.getLikeNumber();
        } else {
            like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + id).toString());
        }
        result.put("like_number", like_num);
        result.put("user_id", post.getCreatorId());
        User uEntity = new User();
        uEntity.setId(post.getCreatorId());
        User user = userService.getUser(uEntity);
        if (null != user) {
            result.put("name", user.getName());
            result.put("headimg", user.getHeadimg());
            result.put("type", user.getType());
        } else {
            result.put("name", "");
            result.put("headimg", "");
            result.put("type", "");
        }
        if (null != user_id) {
            PostLike lEntity = new PostLike();
            lEntity.setPostId(id);
            lEntity.setUserId(user_id);
            PostLike postLike = postLikeService.getPostLike(lEntity);
            if (null != postLike) {
                result.put("is_like", 1);
            } else {
                result.put("is_like", 2);
            }
        } else {
            result.put("is_like", 2);
        }
        String atUser = post.getAtUser();
        if (StringUtils.isNotBlank(atUser)) {
            String[] ids = atUser.split(",");
            List<Map<String, Object>> atList = userService.findUserByIds(ids);
            result.put("atList", atList);
        } else {
            result.put("atList", null);
        }

        // 将浏览量存放进redis
        /*
         * if (null != RedisUtils.getValueByKey("post_watch_count_" + id)) { int
         * watch_count = Integer.parseInt(RedisUtils.getValueByKey("post_watch_count_" +
         * id).toString()); RedisUtils.addValue("post_watch_count_" + id,
         * String.valueOf(watch_count + 1), null); } else {
         * RedisUtils.addValue("post_watch_count_" + id,
         * String.valueOf(Integer.parseInt(result.get("watch_count").toString()) + 1),
         * null); } int watchCount =
         * Integer.parseInt(RedisUtils.getValueByKey("post_watch_count_" +
         * id).toString()); result.put("watch_count", watchCount);
         */

        // 浏览量+1
        post.setWatchCount(post.getWatchCount() + 1);
        postService.updatePost(post);
        result.put("watchCount", post.getWatchCount());

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 添加/取消关注
     *
     * @param response
     * @param user_id
     * @param be_followed_id
     * @param type
     */
    @RequestMapping("/user_addFollow")
    public void user_addFollow(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                               Integer be_followed_id, Integer type, Integer page_id) {
        if (be_followed_id == null || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        Integer userid = 0;
        if (null != page_id) {
            userid = page_id;
        } else {
            userid = user_id;
        }
        User uEntity1 = new User();
        uEntity1.setId(userid);
        User user1 = userService.getUser(uEntity1);
        if (user1 == null || user1.getIsDelete().shortValue() == 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
            return;
        }

        // 我关注对方
        Follow fEntity = new Follow();
        fEntity.setBeFollowedId(be_followed_id);
        fEntity.setFollowerId(userid);
        Follow follow = followService.getFollow(fEntity);
        // 对方关注我
        Follow entity = new Follow();
        entity.setFollowerId(be_followed_id);
        entity.setBeFollowedId(userid);
        Follow beFollow = followService.getFollow(entity);
        int result = 0;
        if (null != follow) {
            if (type.intValue() == 1) {
                // 关注
                follow.setIsCancel((short) 2);
                // 判断对方是否有没有关注过我,如果对方关注我了,我这次也关注对方了,把自己的和对方的互相关注字段修改成已互相关注
                if (null != beFollow && beFollow.getIsCancel().shortValue() == 2) {
                    follow.setIsTwoWay((short) 1);
                    beFollow.setIsTwoWay((short) 1);
                    followService.updateFollow(beFollow);
                }
                result = followService.updateFollow(follow);
            } else {
                // 取消关注
                follow.setIsCancel((short) 1);
                follow.setCancelTime(new Date());
                follow.setIsTwoWay((short) 2);
                result = followService.updateFollow(follow);
                // 如果对方是已互相关注状态,把对方互相关注改成未互相关注
                if (null != beFollow && beFollow.getIsTwoWay().shortValue() == 1) {
                    beFollow.setIsTwoWay((short) 2);
                    followService.updateFollow(beFollow);
                }
            }
        } else {
            // 为空的情况,直接关注
            Follow foEntity = new Follow();
            foEntity.setBeFollowedId(be_followed_id);
            foEntity.setCreateTime(new Date());
            foEntity.setFollowerId(userid);
            foEntity.setIsCancel((short) 2);
            // 涉及到是否互相关注的时候要看对方有没有关注过我
            if (beFollow != null) {
                // 修改为互相关注
                foEntity.setIsTwoWay((short) 1);
                beFollow.setIsTwoWay((short) 1);
                followService.updateFollow(beFollow);
            } else {
                foEntity.setIsTwoWay((short) 2);
            }
            result = followService.addFollow(foEntity);
        }
        // 我关注你,我的关注数+1,你的粉丝数+1,修改账号关注数和粉丝数
        if (result > 0 && type.intValue() == 1) {
            // 关注人关注数+1
            User uEntity = new User();
            uEntity.setId(userid);
            uEntity.setIsDelete((short) 2);
            User user = userService.getUser(uEntity);
            if (null != user && null != user.getFocusNum() && user.getFocusNum().intValue() > 0) {
                uEntity.setFocusNum(user.getFocusNum().intValue() + 1);
            } else {
                uEntity.setFocusNum(1);
            }
            userService.updateUser(uEntity);
            // 被关注人粉丝数+1
            User buEntity = new User();
            buEntity.setId(be_followed_id);
            buEntity.setIsDelete((short) 2);
            User buser = userService.getUser(buEntity);
            if (null != buser && null != buser.getFansNum() && buser.getFansNum().intValue() > 0) {
                buEntity.setFansNum(buser.getFansNum().intValue() + 1);
            } else {
                buEntity.setFansNum(1);
            }
            userService.updateUser(buEntity);
        } else if (result > 0 && type.intValue() == 2) {
            // 我取消关注你,我的关注数-1,你的粉丝数-1
            User uEntity = new User();
            uEntity.setId(userid);
            uEntity.setIsDelete((short) 2);
            User user = userService.getUser(uEntity);
            if (null != user && null != user.getFocusNum() && user.getFocusNum().intValue() > 0) {
                uEntity.setFocusNum(user.getFocusNum().intValue() - 1);
            } else {
                uEntity.setFocusNum(0);
            }
            userService.updateUser(uEntity);
            // 被关注人粉丝数+1
            User buEntity = new User();
            buEntity.setId(be_followed_id);
            buEntity.setIsDelete((short) 2);
            User buser = userService.getUser(buEntity);
            if (null != buser && null != buser.getFansNum() && buser.getFansNum().intValue() > 0) {
                buEntity.setFansNum(buser.getFansNum().intValue() - 1);
            } else {
                buEntity.setFansNum(0);
            }
            userService.updateUser(buEntity);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除帖子
     *
     * @param response
     * @param user_id
     * @param id
     */
    @RequestMapping("/user_deletePost")
    public void user_deletePost(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id) {
        if (id == null) {
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

        Post pEntity = new Post();
        pEntity.setId(id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }
        if (post.getCreatorId().intValue() != user_id) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不是你发布的帖子不能删除!", null)));
            return;
        }
        pEntity.setIsDelete((short) 1);
        pEntity.setDeleteTime(new Date());
        int i = postService.updatePost(pEntity);
        if (i > 0) {
            // 删除成功,我的帖子数-1
            if (null != user.getPostNum() && user.getPostNum().intValue() > 0) {
                uEntity.setPostNum(user.getPostNum().intValue() - 1);
            } else {
                uEntity.setPostNum(0);
            }
            userService.updateUser(uEntity);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 帖子点赞/取消点赞
     *
     * @param response
     * @param user_id
     * @param id
     * @param type
     */
    @RequestMapping("/user_likePost")
    public void user_likePost(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id,
                              Integer type) {
        if (id == null || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);

        Post pEntity = new Post();
        pEntity.setId(id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);

        PostLike lEntity = new PostLike();
        lEntity.setPostId(id);
        lEntity.setUserId(user_id);
        PostLike postLike = postLikeService.getPostLike(lEntity);
        // 用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }

        if (type.intValue() == 1) {
            // 点赞
            if (null != postLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已经点赞了,请先取消!", null)));
                return;
            }
            PostLike entity = new PostLike();
            entity.setCreateTime(new Date());
            entity.setPostId(id);
            entity.setUser(user.getName());
            entity.setUserId(user_id);
            postLikeService.addPostLike(entity);

            // 帖子发布人
            User tEntity = new User();
            tEntity.setId(post.getCreatorId());
            tEntity.setIsDelete((short) 2);
            User to_user = userService.getUser(uEntity);

            // 点赞完要先判断用户量>200更新redis中帖子的点赞数和热度数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getLikeNumber() != null && post.getHot() != null) {
                    pEntity.setLikeNumber(post.getLikeNumber().intValue() + 1);
                    pEntity.setHot(post.getHot().intValue() + 1);
                } else {
                    pEntity.setLikeNumber(1);
                    pEntity.setHot(1);
                }
                postService.updatePost(pEntity);
            } else {
                // 直接更新redis
                // 帖子点赞数
                if (null != RedisUtils.getValueByKey("post_like_" + id)) {
                    Integer like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + id).toString());
                    RedisUtils.addValue("post_like_" + id, String.valueOf(like_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_like_" + id, String.valueOf(post.getLikeNumber().intValue() + 1), null);
                }
                if (null != RedisUtils.getValueByKey("post_hot_" + id)) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + id).toString());
                    RedisUtils.addValue("post_hot_" + id, String.valueOf(hot_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_hot_" + id, String.valueOf(post.getHot().intValue() + 1), null);
                }
            }
            // 放到点赞消息表
            LikeMessage mEntity = new LikeMessage();
            mEntity.setContent(post.getTitle());
            mEntity.setCreateTime(new Date());
            mEntity.setIsDetele((short) 2);
            mEntity.setIsRead((short) 2);
            mEntity.setObjectId(id);
            mEntity.setToer(to_user.getName());
            mEntity.setToerId(post.getCreatorId());
            mEntity.setType((short) 1);
            mEntity.setUser(user.getName());
            mEntity.setUserId(user_id);
            likeMessageService.addLikeMessage(mEntity);
            // 推送
//			if(to_user.getIsPushLike().shortValue() == 1){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
//				hm.put("id", id);
//				hm.put("title", user.getName()+"赞了你的帖子");
//				//个人的允许点赞推送开
//				try {
//					flag = PushTool.pushToUser(post.getCreatorId(), "", user.getName()+"赞了你的帖子", hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//入推送表
//				Push puEntity = new Push();
//				puEntity.setContent("");
//				puEntity.setDeviceType(to_user.getDevType());
//				puEntity.setMessageType((short)5);
//				puEntity.setObjectId(id);
//				puEntity.setTitle(user.getName()+"赞了你的帖子");
//				puEntity.setToken(to_user.getDevToken());
//				if(flag){
//					puEntity.setType((short)10);
//				}else{
//					puEntity.setType((short)50);
//				}
//				puEntity.setUserId(post.getCreatorId());
//				pushSerivce.addPush(puEntity);
//			}
        } else {
            // 取消点赞
            if (null == postLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "暂无点赞取消,请先点赞!", null)));
                return;
            }
            postLikeService.deletePostLike(postLike);
            // 点赞完要先判断用户量>200更新redis中帖子的点赞数和热度数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getLikeNumber() > 0 && post.getHot() > 0) {
                    pEntity.setLikeNumber(post.getLikeNumber().intValue() - 1);
                    pEntity.setHot(post.getHot().intValue() - 1);
                } else {
                    pEntity.setLikeNumber(0);
                    pEntity.setHot(0);
                }
                postService.updatePost(pEntity);
            } else {
                // 直接更新redis
                // 帖子点赞数
                if (null != RedisUtils.getValueByKey("post_like_" + id)) {
                    Integer like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + id).toString());
                    if (like_num.intValue() > 0) {
                        RedisUtils.addValue("post_like_" + id, String.valueOf(like_num.intValue() - 1), null);
                    } else {
                        RedisUtils.addValue("post_like_" + id, String.valueOf(0), null);
                    }
                } else {
                    if (post.getLikeNumber().intValue() > 0) {
                        RedisUtils.addValue("post_like_" + id, String.valueOf(post.getLikeNumber().intValue() - 1),
                                null);
                    } else {
                        RedisUtils.addValue("post_like_" + id, String.valueOf(0), null);
                    }
                }
                // 帖子热度
                if (null != RedisUtils.getValueByKey("post_hot_" + id)) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + id).toString());
                    if (hot_num.intValue() > 0) {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(hot_num.intValue() - 1), null);
                    } else {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(0), null);
                    }
                } else {
                    if (post.getHot().intValue() > 0) {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(post.getHot().intValue() - 1), null);
                    } else {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(0), null);
                    }
                }
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 楼层列表
     *
     * @param response
     * @param id
     * @param type
     * @param page
     * @param pageSize
     * @param search_type
     * @param time
     */
    @RequestMapping("/app_floorList")
    public void app_floorList(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id,
                              Integer type, Integer page, Integer pageSize, Integer search_type, String time) {
        if (id == null || type == null || page == null || search_type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }
        // 楼层列表
        Integer pagesize = pageSize == null ? 10 : pageSize;
        Post pEntity = new Post();
        pEntity.setId(id);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }
        List<Map<String, Object>> postFloorList = null;
        if (search_type.intValue() == 1) {
            // 全部
            PageLimitHolderFilter.setContext(page, pagesize, null);
            postFloorList = postService.findPostFloorList(type, id, search_type, now, null);
        } else {
            // 只看楼主
            PageLimitHolderFilter.setContext(page, pagesize, null);
            postFloorList = postService.findPostFloorList(type, id, search_type, now, post.getCreatorId());
        }
        if (postFloorList.size() > 0) {
            for (Map<String, Object> map : postFloorList) {
                Object at_user = map.get("at_user");
                if (at_user != null && StringUtils.isNotBlank(at_user.toString())) {
                    String[] ids = at_user.toString().split(",");
                    List<Map<String, Object>> atList = userService.findUserByIds(ids);
                    map.put("atList", atList);
                } else {
                    map.put("atList", null);
                }

                // 楼层id
                Integer floor_id = Integer.valueOf(map.get("id").toString());
                String width = "";
                if (map.get("img") != null && StringUtils.isNotBlank(map.get("img").toString())) {
                    for (int i = 0; i < map.get("img").toString().split(",").length; i++) {
                        String img_id = map.get("img").toString().split(",")[i];
                        FileIoEntity info = fileService.getInfo(img_id);
                        width += img_id + "|" + info.getDataInfo().getDescription() + ",";
                    }
                    width = width.substring(0, width.length() - 1);
                }
                map.put("img_width", width);
                // 是否点赞 1:是 2:否
                if (null != user_id) {
                    PostCommentLike lEntity = new PostCommentLike();
                    lEntity.setUserId(user_id);
                    lEntity.setCommentId(floor_id);
                    PostCommentLike commentLike = postCommentLikeService.getPostCommentLike(lEntity);
                    if (null != commentLike) {
                        map.put("is_like", 1);
                    } else {
                        map.put("is_like", 2);
                    }
                } else {
                    map.put("is_like", 2);
                }

                // 根据楼层id查回复列表
                List<Map<String, Object>> postReplyList = postService.findPostReplyList(floor_id, null);
                map.put("replyNum", postReplyList.size());
                if (postReplyList.size() > 3) {
                    map.put("is_more", 1);
                } else {
                    map.put("is_more", 2);
                }
                pagesize = 3;
                PageLimitHolderFilter.setContext(page, pagesize, null);
                List<Map<String, Object>> replyList = postService.findPostReplyList(floor_id, now);
                map.put("replyList", replyList);
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("floorList", postFloorList);
        result.put("time", now);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", result)));
    }

    /**
     * 楼层详情
     *
     * @param response
     * @param id
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/app_floorInfo")
    public void app_floorInfo(HttpServletResponse response, Integer id, Integer page, Integer pageSize, String time) {
        if (id == null || page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        PostComment cEntity = new PostComment();
        cEntity.setId(id);
        cEntity.setIsDelete((short) 2);
        PostComment postComment = postCommentService.getPostComment(cEntity);
        if (null == postComment) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "楼层不存在!", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(postComment.getUserId());
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        String atUser = postComment.getAtUser();
        if (StringUtils.isNotBlank(atUser)) {
            String[] ids = atUser.split(",");
            List<Map<String, Object>> atList = userService.findUserByIds(ids);
            map.put("atList", atList);
        } else {
            map.put("atList", null);
        }
        map.put("user_id", postComment.getUserId());
        if (null != user) {
            map.put("user_name", user.getName());
            map.put("user_headimg", user.getHeadimg());
            map.put("type", user.getType());
        } else {
            map.put("user_name", "");
            map.put("user_headimg", "");
            map.put("type", "");
        }
        map.put("create_time", postComment.getCreateTime());
        map.put("content", postComment.getContent());
        map.put("img", postComment.getImg());
        String width = "";
        if (map.get("img") != null && StringUtils.isNotBlank(map.get("img").toString())) {
            for (int i = 0; i < map.get("img").toString().split(",").length; i++) {
                String img_id = map.get("img").toString().split(",")[i];
                FileIoEntity info = fileService.getInfo(img_id);
                width += img_id + "|" + info.getDataInfo().getDescription() + ",";
            }
            width = width.substring(0, width.length() - 1);
        }
        map.put("img_width", width);
        map.put("floor_num", postComment.getFloor());
        int pagesize = pageSize == null ? 10 : pageSize;
        PageLimitHolderFilter.setContext(page, pagesize, null);
        List<Map<String, Object>> replyList = postService.findPostReplyList(id, now);
        map.put("list", replyList);
        map.put("time", now);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 楼层点赞/取消点赞
     *
     * @param response
     * @param user_id
     * @param id
     * @param type
     */
    @RequestMapping("/user_floorLike")
    public void user_floorLike(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id,
                               Integer type) {
        if (id == null || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);

        PostComment pcEntity = new PostComment();
        pcEntity.setId(id);
        pcEntity.setIsDelete((short) 2);
        PostComment postComment = postCommentService.getPostComment(pcEntity);
        if (null == postComment) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "楼层不存在!", null)));
            return;
        }

        Post pEntity = new Post();
        pEntity.setId(postComment.getPostId());
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }

        PostCommentLike lEntity = new PostCommentLike();
        lEntity.setCommentId(id);
        lEntity.setUserId(user_id);
        PostCommentLike commentLike = postCommentLikeService.getPostCommentLike(lEntity);

        // 用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }

        if (type.intValue() == 1) {
            // 点赞
            if (null != commentLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已经点赞了,请先取消!", null)));
                return;
            }
            PostCommentLike entity = new PostCommentLike();
            entity.setCreateTime(new Date());
            entity.setCommentId(id);
            entity.setUser(user.getName());
            entity.setUserId(user_id);
            postCommentLikeService.addPostCommentLike(entity);
            // 点赞完要先判断用户量>200更新redis中帖子热度数和楼层的点赞数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getHot() != null) {
                    pEntity.setHot(post.getHot().intValue() + 1);
                } else {
                    pEntity.setHot(1);
                }
                postService.updatePost(pEntity);
                // 楼层点赞数
                if (null != postComment && postComment.getLikeCount() != null) {
                    pcEntity.setLikeCount(postComment.getLikeCount().intValue() + 1);
                } else {
                    pcEntity.setLikeCount(1);
                }
                postCommentService.updatePostComment(pcEntity);

                // 楼发布人
                User tEntity = new User();
                tEntity.setId(postComment.getUserId());
                tEntity.setIsDelete((short) 2);
                User to_user = userService.getUser(tEntity);

                // 放到点赞消息表
                LikeMessage mEntity = new LikeMessage();
                mEntity.setContent(postComment.getContent());
                mEntity.setCreateTime(new Date());
                mEntity.setIsDetele((short) 2);
                mEntity.setIsRead((short) 2);
                mEntity.setObjectId(id);
                mEntity.setToer(to_user.getName());
                mEntity.setToerId(postComment.getUserId());
                mEntity.setType((short) 2);
                mEntity.setUser(user.getName());
                mEntity.setUserId(user_id);
                likeMessageService.addLikeMessage(mEntity);

                // 推送
//				if(to_user.getIsPushLike().shortValue() == 1){
//					Boolean flag = false;
//					HashMap<String,Object> hm = new HashMap<String,Object>();
//					hm.put("id", id);
//					hm.put("title", user.getName()+"赞了你的帖子");
//					//个人的允许点赞推送开
//					try {
//						flag = PushTool.pushToUser(postComment.getUserId(), "", user.getName()+"赞了你", hm);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					//入推送表
//					Push puEntity = new Push();
//					puEntity.setContent("");
//					puEntity.setDeviceType(to_user.getDevType());
//					puEntity.setMessageType((short)6);
//					puEntity.setObjectId(id);
//					puEntity.setTitle(user.getName()+"赞了你");
//					puEntity.setToken(to_user.getDevToken());
//					if(flag){
//						puEntity.setType((short)10);
//					}else{
//						puEntity.setType((short)50);
//					}
//					puEntity.setUserId(postComment.getUserId());
//					pushSerivce.addPush(puEntity);
//				}

            } else {
                // 直接更新redis
                // 楼层点赞数
                if (null != RedisUtils.getValueByKey("post_like_" + post.getId() + "floor_like_" + id)) {
                    Integer like_num = Integer.valueOf(
                            RedisUtils.getValueByKey("post_like_" + post.getId() + "floor_like_" + id).toString());
                    RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id,
                            String.valueOf(like_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id,
                            String.valueOf(postComment.getLikeCount().intValue() + 1), null);
                }
                // 帖子热度
                if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() + 1), null);
                }
            }
        } else {
            // 取消点赞
            if (null == commentLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "暂无点赞取消,请先点赞!", null)));
                return;
            }
            postCommentLikeService.deletePostCommentLike(commentLike);
            // 点赞完要先判断用户量>200更新redis中帖子热度数和楼的点赞数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getHot() > 0) {
                    pEntity.setHot(post.getHot().intValue() - 1);
                } else {
                    pEntity.setHot(0);
                }
                postService.updatePost(pEntity);
                // 楼的点赞数
                if (null != postComment && postComment.getLikeCount().intValue() > 0) {
                    pcEntity.setLikeCount(postComment.getLikeCount().intValue() - 1);
                } else {
                    pcEntity.setLikeCount(0);
                }
                postCommentService.updatePostComment(pcEntity);
            } else {
                // 直接更新redis
                // 楼层点赞数
                if (null != RedisUtils.getValueByKey("post_like_" + post.getId() + "floor_like_" + id)) {
                    Integer like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + id).toString());
                    if (like_num.intValue() > 0) {
                        RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id,
                                String.valueOf(like_num.intValue() - 1), null);
                    } else {
                        RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id, String.valueOf(0), null);
                    }
                } else {
                    if (post.getLikeNumber().intValue() > 0) {
                        RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id,
                                String.valueOf(postComment.getLikeCount().intValue() - 1), null);
                    } else {
                        RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id, String.valueOf(0), null);
                    }
                }
                // 帖子热度
                if (null != RedisUtils.getValueByKey("post_hot_" + id)) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + id).toString());
                    if (hot_num.intValue() > 0) {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(hot_num.intValue() - 1), null);
                    } else {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(0), null);
                    }
                } else {
                    if (post.getHot().intValue() > 0) {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(post.getHot().intValue() - 1), null);
                    } else {
                        RedisUtils.addValue("post_hot_" + id, String.valueOf(0), null);
                    }
                }
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 帖子评论
     *
     * @param response
     * @param user_id
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/user_addPostComment")
    public void user_addPostComment(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        Object id = param.get("id");
        Object content = param.get("content");
        Object at_user = param.get("at_user");
        List<DiskFileItem> imgList = null;
        try {
            imgList = (List<DiskFileItem>) param.get("img");
        } catch (Exception e) {
            imgList = null;
        }
        if (id == null || content == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        if (null != imgList && imgList.size() > 3) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "图片数量超过3张!", null)));
            return;
        }
        if (content.toString().length() > 500) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "内容字数超过限制!", null)));
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
        Post pEntity = new Post();
        pEntity.setId(Integer.valueOf(id.toString()));
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }

        // 用redis自增来进行楼层+1
        Long num = RedisUtils.addNumIncrement("post_floor_" + id);
        PostComment cEntity = new PostComment();
        if (null != at_user) {
            cEntity.setAtUser(at_user.toString());
        }
        cEntity.setContent(content.toString());
        Date date = new Date();
        cEntity.setCreateTime(date);
        cEntity.setFloor(Integer.valueOf(String.valueOf(num)));
        if (null != imgList) {
            String filePath = "";
            for (DiskFileItem diskFileItem : imgList) {
                byte[] fileContent = diskFileItem.get();
                String fileName = FileUtilsEx.getFileNameByPath(diskFileItem.getName());
                String fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
                filePath += fileid + ",";
            }
            filePath = filePath.substring(0, filePath.length() - 1);
            cEntity.setImg(filePath);
        }
        cEntity.setIsDelete((short) 2);
        cEntity.setLikeCount(0);
        cEntity.setPostId(Integer.valueOf(id.toString()));
        cEntity.setUserId(user_id);
        int i = postCommentService.addPostComment(cEntity);
        if (i > 0) {
            // 评论入库成功之后,更新评论数,热度,最新回复时间
            // 用户量
            Integer user_num = null;
            if (null != RedisUtils.getValueByKey("user_num")) {
                user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
            } else {
                User uentity = new User();
                uentity.setIsDelete((short) 2);
                List<User> list = userService.findList(uentity);
                user_num = list.size();
            }
            // 评论完要先判断用户量>200更新redis中帖子热度数,评论数,最新回复时间,定时任务把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getHot() != null) {
                    pEntity.setHot(post.getHot().intValue() + 1);
                } else {
                    pEntity.setHot(1);
                }
                if (null != post && post.getCommentNumber() != null) {
                    pEntity.setCommentNumber(post.getCommentNumber().intValue() + 1);
                } else {
                    pEntity.setCommentNumber(1);
                }
                pEntity.setNewestCmtTime(date);
                postService.updatePost(pEntity);
            } else {
                // 直接更新redis
                // 帖子的评论数,热度数,最新回复时间
                if (null != RedisUtils.getValueByKey("post_comment_" + id)) {
                    Integer comment_num = Integer.valueOf(RedisUtils.getValueByKey("post_comment_" + id));
                    RedisUtils.addValue("post_comment_" + id, String.valueOf(comment_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_comment_" + id, String.valueOf(post.getCommentNumber().intValue() + 1),
                            null);
                }
                // 帖子热度
                if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() + 1), null);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 最新回复时间
                RedisUtils.addValue("post_reply_" + post.getId(), sdf.format(date), null);
            }
            if (null != at_user) {
                List<User> userList = new ArrayList<User>();
                for (int j = 0; j < at_user.toString().split(",").length; j++) {
                    Integer at_user_id = Integer.valueOf(at_user.toString().split(",")[j]);
                    User usEntity = new User();
                    usEntity.setId(at_user_id);
                    User atuser = userService.getUser(usEntity);
                    userList.add(atuser);

                    if (atuser.getIsPushAt().shortValue() == 1 && atuser.getType().shortValue() == 1) {
                        Boolean flag = false;
                        HashMap<String, Object> hm = new HashMap<String, Object>();
                        hm.put("id", i);
                        hm.put("title", user.getName() + "@了你");
                        hm.put("type", 5);
                        // 个人的允许@推送开
                        try {
                            flag = PushTool.pushToUser(at_user_id, content.toString(), user.getName() + "@了你", hm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 入推送表
                        Push puEntity = new Push();
                        puEntity.setContent(content.toString());
                        puEntity.setDeviceType(atuser.getDevType());
                        puEntity.setMessageType((short) 6);
                        puEntity.setObjectId(i);
                        puEntity.setTitle(user.getName() + "@了你");
                        puEntity.setToken(atuser.getDevToken());
                        if (flag) {
                            puEntity.setType((short) 10);
                        } else {
                            puEntity.setType((short) 50);
                        }
                        puEntity.setUserId(at_user_id);
                        pushSerivce.addPush(puEntity);
                    }

                }
                // 入@消息表
                atMessageService.insertAll((short) 2, user_id, user.getName(), userList, i, content.toString(),
                        post.getTitle());

            }
            // 帖子发布人
            User tEntity = new User();
            tEntity.setId(post.getCreatorId());
            tEntity.setIsDelete((short) 2);
            User to_user = userService.getUser(uEntity);
            // 入评论消息表
            CommentReplyMessage mEntity = new CommentReplyMessage();
            mEntity.setContent(content.toString());
            mEntity.setCreateTime(new Date());
            mEntity.setIsDetele((short) 2);
            mEntity.setIsRead((short) 2);
            mEntity.setObjectId(Integer.valueOf(id.toString()));
            mEntity.setOriginContent(post.getTitle());
            mEntity.setToer(to_user.getName());
            mEntity.setToerId(post.getCreatorId());
            mEntity.setType((short) 1);
            mEntity.setUser(user.getName());
            mEntity.setUserId(user_id);
            commentReplyMessageService.addCommentReplyMessage(mEntity);

            // 推送
            // 非自评的评论才会进行推送
            if (to_user.getIsPushCmt().shortValue() == 1 && to_user.getType().shortValue() == 1
                    && user_id != to_user.getId()) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", id);
                hm.put("title", user.getName() + "评论了你的帖子");
                hm.put("type", 5);
                // 个人的允许评论推送开
                try {
                    flag = PushTool.pushToUser(post.getCreatorId(), content.toString(), user.getName() + "评论了你的帖子", hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 入推送表
                Push puEntity = new Push();
                puEntity.setContent(content.toString());
                puEntity.setDeviceType(to_user.getDevType());
                puEntity.setMessageType((short) 5);
                puEntity.setObjectId(Integer.valueOf(id.toString()));
                puEntity.setTitle(user.getName() + "评论了你的帖子");
                puEntity.setToken(to_user.getDevToken());
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }
                puEntity.setUserId(post.getCreatorId());
                pushSerivce.addPush(puEntity);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", i);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 回复楼
     *
     * @param response
     * @param user_id
     * @param content
     * @param reply_id
     * @param to_user_id
     * @param floor_id
     * @param at_user
     * @param post_id
     */
    @RequestMapping("/user_addReplyComment")
    public void user_addReplyComment(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                     String content, Integer reply_id, Integer to_user_id, Integer floor_id, String at_user, Integer post_id) {
        if (StringUtils.isBlank(content) || floor_id == null || post_id == null) {
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

        Post pEntity = new Post();
        pEntity.setId(post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }

        PostReply rEntity = new PostReply();
        rEntity.setCommentId(floor_id);
        rEntity.setContent(content);
        Date date = new Date();
        rEntity.setCreateTime(date);
        rEntity.setIsDelete((short) 2);
        rEntity.setPostId(post_id);
        rEntity.setReplierId(user_id);
        rEntity.setToId(reply_id);
        rEntity.setToUserId(to_user_id);
        int id = postReplyService.addPostReply(rEntity);
        if (id > 0) {
            // 更新帖子的最新回复时间和热度
            // 用户量
            Integer user_num = null;
            if (null != RedisUtils.getValueByKey("user_num")) {
                user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
            } else {
                User uentity = new User();
                uentity.setIsDelete((short) 2);
                List<User> list = userService.findList(uentity);
                user_num = list.size();
            }
            // 评论完要先判断用户量>200更新redis中帖子热度数,最新回复时间,定时任务把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getHot() != null) {
                    pEntity.setHot(post.getHot().intValue() + 1);
                } else {
                    pEntity.setHot(1);
                }
                pEntity.setNewestCmtTime(date);
                postService.updatePost(pEntity);
            } else {
                // 直接更新redis
                // 帖子的热度数,最新回复时间
                // 帖子热度
                if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() + 1), null);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 最新回复时间
                RedisUtils.addValue("post_reply_" + post.getId(), sdf.format(date), null);
            }
            // 入@消息表
            PostComment cEntity = new PostComment();
            cEntity.setId(floor_id);
            PostComment comment = postCommentService.getPostComment(cEntity);
            if (null != at_user) {
                List<User> userList = new ArrayList<User>();
                for (int j = 0; j < at_user.toString().split(",").length; j++) {
                    Integer at_user_id = Integer.valueOf(at_user.toString().split(",")[j]);
                    User usEntity = new User();
                    usEntity.setId(at_user_id);
                    User atuser = userService.getUser(usEntity);
                    userList.add(atuser);

                    if (atuser.getIsPushAt().shortValue() == 1 && atuser.getType().shortValue() == 1) {
                        Boolean flag = false;
                        HashMap<String, Object> hm = new HashMap<String, Object>();
                        hm.put("id", post_id);
                        hm.put("floor_id", floor_id);
                        hm.put("title", user.getName() + "@了你");
                        hm.put("type", 6);
                        // 个人的允许@推送开
                        try {
                            flag = PushTool.pushToUser(at_user_id, content.toString(), user.getName() + "@了你", hm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 入推送表
                        Push puEntity = new Push();
                        puEntity.setContent(content.toString());
                        puEntity.setDeviceType(atuser.getDevType());
                        puEntity.setMessageType((short) 5);
                        puEntity.setObjectId(id);
                        puEntity.setTitle(user.getName() + "@了你");
                        puEntity.setToken(atuser.getDevToken());
                        if (flag) {
                            puEntity.setType((short) 10);
                        } else {
                            puEntity.setType((short) 50);
                        }
                        puEntity.setUserId(at_user_id);
                        pushSerivce.addPush(puEntity);
                    }

                }
                atMessageService.insertAll((short) 1, user_id, user.getName(), userList, floor_id, content.toString(),
                        comment.getContent());
            }

            // 上级楼层人或回复人
            if (null == to_user_id) {
                // 楼层创建者,否则就是传过来的回复人
                to_user_id = comment.getUserId();
            }
            User tEntity = new User();
            tEntity.setId(to_user_id);
            tEntity.setIsDelete((short) 2);
            User to_user = userService.getUser(uEntity);
            String og_content = "";
            if (null == reply_id) {
                // reply_id为空,回复的是楼层
                og_content = comment.getContent();
            } else {
                // reply_id不为空,回复的是回复
                PostReply prEntity = new PostReply();
                prEntity.setId(reply_id);
                PostReply reply = postReplyService.getPostReply(prEntity);
                og_content = reply.getContent();
            }
            // 入评论回复消息表
            CommentReplyMessage mEntity = new CommentReplyMessage();
            mEntity.setContent(content.toString());
            mEntity.setCreateTime(new Date());
            mEntity.setIsDetele((short) 2);
            mEntity.setIsRead((short) 2);
            mEntity.setObjectId(floor_id);
            mEntity.setOriginContent(og_content);
            mEntity.setToer(to_user.getName());
            mEntity.setToerId(to_user_id);
            mEntity.setType((short) 2);
            mEntity.setUser(user.getName());
            mEntity.setUserId(user_id);
            commentReplyMessageService.addCommentReplyMessage(mEntity);

            // 推送
            // 非自回的回复进行推送
            if (to_user.getIsPushLike().shortValue() == 1 && to_user.getType().shortValue() == 1
                    && to_user_id != user_id) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", post_id);
                hm.put("floor_id", floor_id);
                hm.put("title", user.getName() + "评论了你");
                hm.put("type", 6);
                // 个人的允许评论推送开
                try {
                    flag = PushTool.pushToUser(to_user_id, content.toString(), user.getName() + "评论了你", hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 入推送表
                Push puEntity = new Push();
                puEntity.setContent(content.toString());
                puEntity.setDeviceType(to_user.getDevType());
                puEntity.setMessageType((short) 6);
                puEntity.setObjectId(id);
                puEntity.setTitle(user.getName() + "评论了你");
                puEntity.setToken(to_user.getDevToken());
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }
                puEntity.setUserId(to_user_id);
                pushSerivce.addPush(puEntity);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("time", date);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 转发帖子
     *
     * @param response
     * @param user_id
     * @param content
     * @param privacy
     * @param at_user
     * @param origin_post_id
     */
    @RequestMapping("/user_forwardPost")
    public void user_forwardPost(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                 String content, Short privacy, String at_user, Integer origin_post_id) {
        if (StringUtils.isBlank(content) || privacy == null || origin_post_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(origin_post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }

        Saying sEntity = new Saying();
        sEntity.setCreatorId(user_id);
        sEntity.setAtUser(at_user);
        sEntity.setCommentNumber(0);
        sEntity.setContent(content);
        sEntity.setCreateTime(new Date());
        sEntity.setForwardNumber(0);
        sEntity.setIsDelete((short) 2);
        sEntity.setIsForward((short) 1);
        sEntity.setLikeNumber(0);
        sEntity.setOriginId(origin_post_id);
        sEntity.setOriginType((short) 1);
        sEntity.setPrivacy(privacy);
        sEntity.setWatchCount(0);
        int saying_id = sayingService.addSaying(sEntity);
        if (saying_id > 0) {
            Saying saEntity = new Saying();
            saEntity.setOriginSayingId(saying_id);
            saEntity.setId(saying_id);
            sayingService.update(saEntity);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除楼层评论
     *
     * @param response
     * @param user_id
     * @param id
     * @param post_id
     */
    @RequestMapping("/user_deleteComment")
    public void user_deleteComment(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id,
                                   Integer post_id) {
        if (id == null || post_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        PostComment cEntity = new PostComment();
        cEntity.setId(id);
        cEntity.setIsDelete((short) 2);
        PostComment postComment = postCommentService.getPostComment(cEntity);
        if (null == postComment) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "楼层不存在!", null)));
            return;
        }
        if (user_id != postComment.getUserId().intValue()) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不是你发布的楼层不能删除!", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }

        // 删除
        postComment.setIsDelete((short) 1);
        postComment.setDeleteTime(new Date());
        postCommentService.updatePostComment(postComment);
        // 用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }
        // 评论完要先判断用户量>200更新redis中帖子热度数,评论数,最新回复时间,定时任务把redis的值更新到数据库;<200正常更新数据库
        if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
            if (null != post && post.getHot() > 0) {
                pEntity.setHot(post.getHot().intValue() - 1);
            } else {
                pEntity.setHot(0);
            }
            if (null != post && post.getCommentNumber() > 0) {
                pEntity.setCommentNumber(post.getCommentNumber().intValue() - 1);
            } else {
                pEntity.setCommentNumber(0);
            }
            postService.updatePost(pEntity);
        } else {
            // 直接更新redis
            // 帖子的评论数,热度数,最新回复时间
            if (null != RedisUtils.getValueByKey("post_comment_" + id)) {
                Integer comment_num = Integer.valueOf(RedisUtils.getValueByKey("post_comment_" + id));
                RedisUtils.addValue("post_comment_" + id, String.valueOf(comment_num.intValue() - 1), null);
            } else {
                RedisUtils.addValue("post_comment_" + id, String.valueOf(post.getCommentNumber().intValue() - 1), null);
            }
            // 帖子热度
            if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() - 1), null);
            } else {
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() - 1), null);
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除回复
     *
     * @param response
     * @param user_id
     * @param id
     * @param post_id
     */
    @RequestMapping("/user_deleteFloorReply")
    public void user_deleteFloorReply(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                      Integer id, Integer post_id) {
        if (id == null || post_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        PostReply rEntity = new PostReply();
        rEntity.setId(id);
        rEntity.setIsDelete((short) 2);
        PostReply postReply = postReplyService.getPostReply(rEntity);
        if (null == postReply) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "回复不存在!", null)));
            return;
        }
        if (user_id != postReply.getReplierId().intValue()) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不是你发布的回复不能删除!", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "帖子不存在!", null)));
            return;
        }
        // 删除
        postReply.setIsDelete((short) 1);
        postReply.setDeleteTime(new Date());
        postReplyService.updatePostReply(postReply);
        // 用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }
        // 评论完要先判断用户量>200更新redis中帖子热度数,定时任务把redis的值更新到数据库;<200正常更新数据库
        if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
            if (null != post && post.getHot() > 0) {
                pEntity.setHot(post.getHot().intValue() - 1);
            } else {
                pEntity.setHot(0);
            }
            postService.updatePost(pEntity);
        } else {
            // 直接更新redis
            // 帖子的热度数
            if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() - 1), null);
            } else {
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() - 1), null);
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }
}
