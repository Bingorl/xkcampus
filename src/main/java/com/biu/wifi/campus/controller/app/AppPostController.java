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
     * ?????????????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", topPostList)));
    }

    /**
     * ??????????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
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
                // ???????????????,??????redis?????????,???redis????????????,?????????????????????
                if (null != RedisUtils.getValueByKey("post_like_" + post_id)) {
                    Integer like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + post_id).toString());
                    map.put("like_number", like_num);
                }

                // ???????????????,??????redis?????????,???redis????????????,?????????????????????
                if (null != RedisUtils.getValueByKey("post_comment_" + post_id)) {
                    Integer comment_number = Integer
                            .valueOf(RedisUtils.getValueByKey("post_comment_" + post_id).toString());
                    map.put("comment_number", comment_number);
                }

                // ????????????,??????????????????;????????????????????????
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", result)));
    }

    /**
     * ????????????
     *
     * @param response
     * @param school_id
     */
    @RequestMapping("/app_essenceTypeList")
    public void app_essenceTypeList(HttpServletResponse response, Integer school_id) {
        if (school_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        PostSelect sEntity = new PostSelect();
        sEntity.setIsDelete((short) 2);
        sEntity.setSchoolId(school_id);
        List<PostSelect> findPostSelectList = postSelectService.findPostSelectList(sEntity);

        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", findPostSelectList)));
    }

    /**
     * ????????????
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
        // ????????????ID
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(userid);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        /*if (title.toString().length() > 20) {
			ServletUtilsEx.renderText(response,
					JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????????????????!", null)));
			return;
		}*/
        if (content.toString().length() > 1000) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????????????????!", null)));
            return;
        }
        if (null != at_user && at_user.toString().split(",").length > 10) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "@??????????????????!", null)));
            return;
        }
        if (null != imgList && imgList.size() > 9) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????!", null)));
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
            // ??????????????????ID
            int typeId = Integer.valueOf(String.valueOf(selectTypeId));
            if (typeId != 0) {
                pEntity.setSelectTypeId(typeId);
            }
        }
        int i = postService.addPost(pEntity);
        if (i > 0) {
            // ????????????????????????+1
            if (null != user.getPostNum() && user.getPostNum().intValue() > 0) {
                uEntity.setPostNum(user.getPostNum().intValue() + 1);
            } else {
                uEntity.setPostNum(1);
            }
            userService.updateUser(uEntity);

            // ????????????,??????????????????????????????????????????????????????redis,??????0
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
                        hm.put("title", user.getName() + "@??????");
                        hm.put("type", 5);
                        // ???????????????@?????????
                        try {
                            flag = PushTool.pushToUser(at_user_id, content.toString(), user.getName() + "@??????", hm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // ????????????
                        Push puEntity = new Push();
                        puEntity.setContent(content.toString());
                        puEntity.setDeviceType(atuser.getDevType());
                        puEntity.setMessageType((short) 5);
                        puEntity.setObjectId(i);
                        puEntity.setTitle(user.getName() + "@??????");
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
                // ???@?????????
                atMessageService.insertAll((short) 1, user_id, user.getName(), userList, i, null, title.toString());
            }

            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
        } else {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "??????", null)));
        }

    }

    /**
     * ????????????
     *
     * @param response
     * @param user_id
     * @param id
     */
    @RequestMapping("/app_postInfo")
    public void app_postInfo(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(10002, "???????????????!", null)));
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
        // redis????????????,?????????,???????????????????????????,????????????redis??????
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

        // ?????????????????????redis
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

        // ?????????+1
        post.setWatchCount(post.getWatchCount() + 1);
        postService.updatePost(post);
        result.put("watchCount", post.getWatchCount());

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", result)));
    }

    /**
     * ??????/????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????", null)));
            return;
        }

        // ???????????????
        Follow fEntity = new Follow();
        fEntity.setBeFollowedId(be_followed_id);
        fEntity.setFollowerId(userid);
        Follow follow = followService.getFollow(fEntity);
        // ???????????????
        Follow entity = new Follow();
        entity.setFollowerId(be_followed_id);
        entity.setBeFollowedId(userid);
        Follow beFollow = followService.getFollow(entity);
        int result = 0;
        if (null != follow) {
            if (type.intValue() == 1) {
                // ??????
                follow.setIsCancel((short) 2);
                // ???????????????????????????????????????,????????????????????????,???????????????????????????,??????????????????????????????????????????????????????????????????
                if (null != beFollow && beFollow.getIsCancel().shortValue() == 2) {
                    follow.setIsTwoWay((short) 1);
                    beFollow.setIsTwoWay((short) 1);
                    followService.updateFollow(beFollow);
                }
                result = followService.updateFollow(follow);
            } else {
                // ????????????
                follow.setIsCancel((short) 1);
                follow.setCancelTime(new Date());
                follow.setIsTwoWay((short) 2);
                result = followService.updateFollow(follow);
                // ????????????????????????????????????,??????????????????????????????????????????
                if (null != beFollow && beFollow.getIsTwoWay().shortValue() == 1) {
                    beFollow.setIsTwoWay((short) 2);
                    followService.updateFollow(beFollow);
                }
            }
        } else {
            // ???????????????,????????????
            Follow foEntity = new Follow();
            foEntity.setBeFollowedId(be_followed_id);
            foEntity.setCreateTime(new Date());
            foEntity.setFollowerId(userid);
            foEntity.setIsCancel((short) 2);
            // ?????????????????????????????????????????????????????????????????????
            if (beFollow != null) {
                // ?????????????????????
                foEntity.setIsTwoWay((short) 1);
                beFollow.setIsTwoWay((short) 1);
                followService.updateFollow(beFollow);
            } else {
                foEntity.setIsTwoWay((short) 2);
            }
            result = followService.addFollow(foEntity);
        }
        // ????????????,???????????????+1,???????????????+1,?????????????????????????????????
        if (result > 0 && type.intValue() == 1) {
            // ??????????????????+1
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
            // ?????????????????????+1
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
            // ??????????????????,???????????????-1,???????????????-1
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
            // ?????????????????????+1
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

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
     *
     * @param response
     * @param user_id
     * @param id
     */
    @RequestMapping("/user_deletePost")
    public void user_deletePost(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }

        Post pEntity = new Post();
        pEntity.setId(id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        if (post.getCreatorId().intValue() != user_id) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????!", null)));
            return;
        }
        pEntity.setIsDelete((short) 1);
        pEntity.setDeleteTime(new Date());
        int i = postService.updatePost(pEntity);
        if (i > 0) {
            // ????????????,???????????????-1
            if (null != user.getPostNum() && user.getPostNum().intValue() > 0) {
                uEntity.setPostNum(user.getPostNum().intValue() - 1);
            } else {
                uEntity.setPostNum(0);
            }
            userService.updateUser(uEntity);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????/????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
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
        // ?????????
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
            // ??????
            if (null != postLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????,????????????!", null)));
                return;
            }
            PostLike entity = new PostLike();
            entity.setCreateTime(new Date());
            entity.setPostId(id);
            entity.setUser(user.getName());
            entity.setUserId(user_id);
            postLikeService.addPostLike(entity);

            // ???????????????
            User tEntity = new User();
            tEntity.setId(post.getCreatorId());
            tEntity.setIsDelete((short) 2);
            User to_user = userService.getUser(uEntity);

            // ??????????????????????????????>200??????redis?????????????????????????????????,???????????????5?????????redis????????????????????????;<200?????????????????????
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
                // ????????????redis
                // ???????????????
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
            // ?????????????????????
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
            // ??????
//			if(to_user.getIsPushLike().shortValue() == 1){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
//				hm.put("id", id);
//				hm.put("title", user.getName()+"??????????????????");
//				//??????????????????????????????
//				try {
//					flag = PushTool.pushToUser(post.getCreatorId(), "", user.getName()+"??????????????????", hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//????????????
//				Push puEntity = new Push();
//				puEntity.setContent("");
//				puEntity.setDeviceType(to_user.getDevType());
//				puEntity.setMessageType((short)5);
//				puEntity.setObjectId(id);
//				puEntity.setTitle(user.getName()+"??????????????????");
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
            // ????????????
            if (null == postLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "??????????????????,????????????!", null)));
                return;
            }
            postLikeService.deletePostLike(postLike);
            // ??????????????????????????????>200??????redis?????????????????????????????????,???????????????5?????????redis????????????????????????;<200?????????????????????
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
                // ????????????redis
                // ???????????????
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
                // ????????????
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        String now = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isBlank(time)) {
            now = sdf.format(new Date());
        } else {
            now = time;
        }
        // ????????????
        Integer pagesize = pageSize == null ? 10 : pageSize;
        Post pEntity = new Post();
        pEntity.setId(id);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        List<Map<String, Object>> postFloorList = null;
        if (search_type.intValue() == 1) {
            // ??????
            PageLimitHolderFilter.setContext(page, pagesize, null);
            postFloorList = postService.findPostFloorList(type, id, search_type, now, null);
        } else {
            // ????????????
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

                // ??????id
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
                // ???????????? 1:??? 2:???
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

                // ????????????id???????????????
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", result)));
    }

    /**
     * ????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ????????????/????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }

        Post pEntity = new Post();
        pEntity.setId(postComment.getPostId());
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }

        PostCommentLike lEntity = new PostCommentLike();
        lEntity.setCommentId(id);
        lEntity.setUserId(user_id);
        PostCommentLike commentLike = postCommentLikeService.getPostCommentLike(lEntity);

        // ?????????
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
            // ??????
            if (null != commentLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????,????????????!", null)));
                return;
            }
            PostCommentLike entity = new PostCommentLike();
            entity.setCreateTime(new Date());
            entity.setCommentId(id);
            entity.setUser(user.getName());
            entity.setUserId(user_id);
            postCommentLikeService.addPostCommentLike(entity);
            // ??????????????????????????????>200??????redis???????????????????????????????????????,???????????????5?????????redis????????????????????????;<200?????????????????????
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getHot() != null) {
                    pEntity.setHot(post.getHot().intValue() + 1);
                } else {
                    pEntity.setHot(1);
                }
                postService.updatePost(pEntity);
                // ???????????????
                if (null != postComment && postComment.getLikeCount() != null) {
                    pcEntity.setLikeCount(postComment.getLikeCount().intValue() + 1);
                } else {
                    pcEntity.setLikeCount(1);
                }
                postCommentService.updatePostComment(pcEntity);

                // ????????????
                User tEntity = new User();
                tEntity.setId(postComment.getUserId());
                tEntity.setIsDelete((short) 2);
                User to_user = userService.getUser(tEntity);

                // ?????????????????????
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

                // ??????
//				if(to_user.getIsPushLike().shortValue() == 1){
//					Boolean flag = false;
//					HashMap<String,Object> hm = new HashMap<String,Object>();
//					hm.put("id", id);
//					hm.put("title", user.getName()+"??????????????????");
//					//??????????????????????????????
//					try {
//						flag = PushTool.pushToUser(postComment.getUserId(), "", user.getName()+"?????????", hm);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					//????????????
//					Push puEntity = new Push();
//					puEntity.setContent("");
//					puEntity.setDeviceType(to_user.getDevType());
//					puEntity.setMessageType((short)6);
//					puEntity.setObjectId(id);
//					puEntity.setTitle(user.getName()+"?????????");
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
                // ????????????redis
                // ???????????????
                if (null != RedisUtils.getValueByKey("post_like_" + post.getId() + "floor_like_" + id)) {
                    Integer like_num = Integer.valueOf(
                            RedisUtils.getValueByKey("post_like_" + post.getId() + "floor_like_" + id).toString());
                    RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id,
                            String.valueOf(like_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_like_" + post.getId() + "floor_like_" + id,
                            String.valueOf(postComment.getLikeCount().intValue() + 1), null);
                }
                // ????????????
                if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() + 1), null);
                }
            }
        } else {
            // ????????????
            if (null == commentLike) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "??????????????????,????????????!", null)));
                return;
            }
            postCommentLikeService.deletePostCommentLike(commentLike);
            // ??????????????????????????????>200??????redis????????????????????????????????????,???????????????5?????????redis????????????????????????;<200?????????????????????
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getHot() > 0) {
                    pEntity.setHot(post.getHot().intValue() - 1);
                } else {
                    pEntity.setHot(0);
                }
                postService.updatePost(pEntity);
                // ???????????????
                if (null != postComment && postComment.getLikeCount().intValue() > 0) {
                    pcEntity.setLikeCount(postComment.getLikeCount().intValue() - 1);
                } else {
                    pcEntity.setLikeCount(0);
                }
                postCommentService.updatePostComment(pcEntity);
            } else {
                // ????????????redis
                // ???????????????
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
                // ????????????
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        if (null != imgList && imgList.size() > 3) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "??????????????????3???!", null)));
            return;
        }
        if (content.toString().length() > 500) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????!", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(Integer.valueOf(id.toString()));
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }

        // ???redis?????????????????????+1
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
            // ????????????????????????,???????????????,??????,??????????????????
            // ?????????
            Integer user_num = null;
            if (null != RedisUtils.getValueByKey("user_num")) {
                user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
            } else {
                User uentity = new User();
                uentity.setIsDelete((short) 2);
                List<User> list = userService.findList(uentity);
                user_num = list.size();
            }
            // ??????????????????????????????>200??????redis??????????????????,?????????,??????????????????,???????????????redis????????????????????????;<200?????????????????????
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
                // ????????????redis
                // ??????????????????,?????????,??????????????????
                if (null != RedisUtils.getValueByKey("post_comment_" + id)) {
                    Integer comment_num = Integer.valueOf(RedisUtils.getValueByKey("post_comment_" + id));
                    RedisUtils.addValue("post_comment_" + id, String.valueOf(comment_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_comment_" + id, String.valueOf(post.getCommentNumber().intValue() + 1),
                            null);
                }
                // ????????????
                if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() + 1), null);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // ??????????????????
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
                        hm.put("title", user.getName() + "@??????");
                        hm.put("type", 5);
                        // ???????????????@?????????
                        try {
                            flag = PushTool.pushToUser(at_user_id, content.toString(), user.getName() + "@??????", hm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // ????????????
                        Push puEntity = new Push();
                        puEntity.setContent(content.toString());
                        puEntity.setDeviceType(atuser.getDevType());
                        puEntity.setMessageType((short) 6);
                        puEntity.setObjectId(i);
                        puEntity.setTitle(user.getName() + "@??????");
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
                // ???@?????????
                atMessageService.insertAll((short) 2, user_id, user.getName(), userList, i, content.toString(),
                        post.getTitle());

            }
            // ???????????????
            User tEntity = new User();
            tEntity.setId(post.getCreatorId());
            tEntity.setIsDelete((short) 2);
            User to_user = userService.getUser(uEntity);
            // ??????????????????
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

            // ??????
            // ????????????????????????????????????
            if (to_user.getIsPushCmt().shortValue() == 1 && to_user.getType().shortValue() == 1
                    && user_id != to_user.getId()) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", id);
                hm.put("title", user.getName() + "?????????????????????");
                hm.put("type", 5);
                // ??????????????????????????????
                try {
                    flag = PushTool.pushToUser(post.getCreatorId(), content.toString(), user.getName() + "?????????????????????", hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ????????????
                Push puEntity = new Push();
                puEntity.setContent(content.toString());
                puEntity.setDeviceType(to_user.getDevType());
                puEntity.setMessageType((short) 5);
                puEntity.setObjectId(Integer.valueOf(id.toString()));
                puEntity.setTitle(user.getName() + "?????????????????????");
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ?????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        uEntity.setIsDelete((short) 2);
        User user = userService.getUser(uEntity);
        if (null == user) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        if (content.length() > 500) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "??????????????????????????????!", null)));
            return;
        }

        Post pEntity = new Post();
        pEntity.setId(post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
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
            // ??????????????????????????????????????????
            // ?????????
            Integer user_num = null;
            if (null != RedisUtils.getValueByKey("user_num")) {
                user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
            } else {
                User uentity = new User();
                uentity.setIsDelete((short) 2);
                List<User> list = userService.findList(uentity);
                user_num = list.size();
            }
            // ??????????????????????????????>200??????redis??????????????????,??????????????????,???????????????redis????????????????????????;<200?????????????????????
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                if (null != post && post.getHot() != null) {
                    pEntity.setHot(post.getHot().intValue() + 1);
                } else {
                    pEntity.setHot(1);
                }
                pEntity.setNewestCmtTime(date);
                postService.updatePost(pEntity);
            } else {
                // ????????????redis
                // ??????????????????,??????????????????
                // ????????????
                if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                    Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() + 1), null);
                } else {
                    RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() + 1), null);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // ??????????????????
                RedisUtils.addValue("post_reply_" + post.getId(), sdf.format(date), null);
            }
            // ???@?????????
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
                        hm.put("title", user.getName() + "@??????");
                        hm.put("type", 6);
                        // ???????????????@?????????
                        try {
                            flag = PushTool.pushToUser(at_user_id, content.toString(), user.getName() + "@??????", hm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // ????????????
                        Push puEntity = new Push();
                        puEntity.setContent(content.toString());
                        puEntity.setDeviceType(atuser.getDevType());
                        puEntity.setMessageType((short) 5);
                        puEntity.setObjectId(id);
                        puEntity.setTitle(user.getName() + "@??????");
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

            // ???????????????????????????
            if (null == to_user_id) {
                // ???????????????,?????????????????????????????????
                to_user_id = comment.getUserId();
            }
            User tEntity = new User();
            tEntity.setId(to_user_id);
            tEntity.setIsDelete((short) 2);
            User to_user = userService.getUser(uEntity);
            String og_content = "";
            if (null == reply_id) {
                // reply_id??????,??????????????????
                og_content = comment.getContent();
            } else {
                // reply_id?????????,??????????????????
                PostReply prEntity = new PostReply();
                prEntity.setId(reply_id);
                PostReply reply = postReplyService.getPostReply(prEntity);
                og_content = reply.getContent();
            }
            // ????????????????????????
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

            // ??????
            // ??????????????????????????????
            if (to_user.getIsPushLike().shortValue() == 1 && to_user.getType().shortValue() == 1
                    && to_user_id != user_id) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", post_id);
                hm.put("floor_id", floor_id);
                hm.put("title", user.getName() + "????????????");
                hm.put("type", 6);
                // ??????????????????????????????
                try {
                    flag = PushTool.pushToUser(to_user_id, content.toString(), user.getName() + "????????????", hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // ????????????
                Push puEntity = new Push();
                puEntity.setContent(content.toString());
                puEntity.setDeviceType(to_user.getDevType());
                puEntity.setMessageType((short) 6);
                puEntity.setObjectId(id);
                puEntity.setTitle(user.getName() + "????????????");
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", map)));
    }

    /**
     * ????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(origin_post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
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
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ??????????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        PostComment cEntity = new PostComment();
        cEntity.setId(id);
        cEntity.setIsDelete((short) 2);
        PostComment postComment = postCommentService.getPostComment(cEntity);
        if (null == postComment) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        if (user_id != postComment.getUserId().intValue()) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????!", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }

        // ??????
        postComment.setIsDelete((short) 1);
        postComment.setDeleteTime(new Date());
        postCommentService.updatePostComment(postComment);
        // ?????????
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }
        // ??????????????????????????????>200??????redis??????????????????,?????????,??????????????????,???????????????redis????????????????????????;<200?????????????????????
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
            // ????????????redis
            // ??????????????????,?????????,??????????????????
            if (null != RedisUtils.getValueByKey("post_comment_" + id)) {
                Integer comment_num = Integer.valueOf(RedisUtils.getValueByKey("post_comment_" + id));
                RedisUtils.addValue("post_comment_" + id, String.valueOf(comment_num.intValue() - 1), null);
            } else {
                RedisUtils.addValue("post_comment_" + id, String.valueOf(post.getCommentNumber().intValue() - 1), null);
            }
            // ????????????
            if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() - 1), null);
            } else {
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() - 1), null);
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }

    /**
     * ????????????
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
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????", null)));
            return;
        }
        PostReply rEntity = new PostReply();
        rEntity.setId(id);
        rEntity.setIsDelete((short) 2);
        PostReply postReply = postReplyService.getPostReply(rEntity);
        if (null == postReply) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        if (user_id != postReply.getReplierId().intValue()) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????!", null)));
            return;
        }
        Post pEntity = new Post();
        pEntity.setId(post_id);
        pEntity.setIsDelete((short) 2);
        Post post = postService.getPost(pEntity);
        if (null == post) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????!", null)));
            return;
        }
        // ??????
        postReply.setIsDelete((short) 1);
        postReply.setDeleteTime(new Date());
        postReplyService.updatePostReply(postReply);
        // ?????????
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }
        // ??????????????????????????????>200??????redis??????????????????,???????????????redis????????????????????????;<200?????????????????????
        if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
            if (null != post && post.getHot() > 0) {
                pEntity.setHot(post.getHot().intValue() - 1);
            } else {
                pEntity.setHot(0);
            }
            postService.updatePost(pEntity);
        } else {
            // ????????????redis
            // ??????????????????
            if (null != RedisUtils.getValueByKey("post_hot_" + post.getId())) {
                Integer hot_num = Integer.valueOf(RedisUtils.getValueByKey("post_hot_" + post.getId()).toString());
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(hot_num.intValue() - 1), null);
            } else {
                RedisUtils.addValue("post_hot_" + post.getId(), String.valueOf(post.getHot().intValue() - 1), null);
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", null)));
    }
}
