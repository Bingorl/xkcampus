package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.MessageMapperEx;
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

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AppPersonalityController extends AuthenticatorController {

    @Autowired
    private AppPersonalityService appPersonalityService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeQuestionService noticeQuestionService;
    @Autowired
    private GroupNoticeService noticeService;
    @Autowired
    private AppPublicPageService appPublicPageService;
    @Autowired
    private MessageMapperEx messageMapperEx;
    @Autowired
    private SayingLikeService sayingLikeService;
    @Autowired
    private SayingCommentService sayingCommentService;
    @Autowired
    private LeaveAuditUserAuthService leaveAuditUserAuthService;
    @Autowired
    private LeaveAuditUserRoleService leaveAuditUserRoleService;
    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private AuditUserRoleService auditUserRoleService;
    @Autowired
    private PushSerivce pushSerivce;

    /**
     * 我的或者公共主页
     *
     * @param user_id
     * @param type
     * @param response
     */
    @RequestMapping("/user_myOwnOrPublicPage")
    public void getMyOwnOrPublicPage(@ModelAttribute("user_id") Integer user_id,
                                     @ModelAttribute("version") String version, Integer type,
                                     HttpServletResponse response) {
        if (type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "缺少参数", null)));
            return;
        }
        Map<String, Object> map = null;
        // 发布帖子@未读数
        int postNotReadNum = messageMapperEx.getPostNotReadNum(user_id);
        // 帖子评论@未读数
        int postCommentNotReadNum = messageMapperEx.getPostCommentNotReadNum(user_id);
        // 新鲜事@未读数
        int sayingNotReadNum = messageMapperEx.getSayingNotReadNum(user_id);
        // 帖子点赞未读数
        int postLikeNotReadNum = messageMapperEx.getPostLikeNotReadNum(user_id);
        // 楼层点赞未读数
        int postComemntLikeNotReadNum = messageMapperEx.getPostComemntLikeNotReadNum(user_id);
        // 新鲜事和新鲜事评论点赞未读数
        int sayingLikeNotReadNum = messageMapperEx.getSayingLikeNotReadNum(user_id);
        // 帖子评论回复未读数
        int postCommetReplyNotReadNum = messageMapperEx.getPostCommetReplyNotReadNum(user_id);
        // 新鲜事评论回复未读数
        int sayingComementNotReadNum = messageMapperEx.getSayingComementNotReadNum(user_id);

        // 除提问之外的所有未读消息数,个人比公共主页多提问未读消息数
        int total = postNotReadNum + postCommentNotReadNum + sayingNotReadNum + postLikeNotReadNum
                + postComemntLikeNotReadNum + sayingLikeNotReadNum + postCommetReplyNotReadNum
                + sayingComementNotReadNum;
        // type:1用户2公共主页
        if (type == 1) {
            map = appPersonalityService.getMyOwnOrPublicPage(user_id);
            if (null == map || null == map.get("name")) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.NO_LOGIN, "账号不存在", null)));
                return;
            }
            // 提问未读数
            int questionNotReadNum = messageMapperEx.getQuestionNotReadNum(user_id);
            // 提问回复未读数
            int questionReplyNotReadNum = messageMapperEx.getQuestionReplyNotReadNum(user_id);

            Integer unread_num = questionNotReadNum + questionReplyNotReadNum + total;
            map.put("unread_num", unread_num);
            User u = appPublicPageService.getUserPublic(user_id);
            if (u != null) {
                map.put("pageId", u.getId());
            } else {
                map.put("pageId", null);
            }

        } else {
            map = appPersonalityService.getMyOwnOrPublicPagee(user_id);
            // 未读消息 = at我的+点赞+消息回复
            Integer unread_num = total;
            map.put("unread_num", unread_num);
        }
        List<Map<String, Object>> focuslist = appPersonalityService.getMyFocusOn(user_id);
        if (focuslist != null && focuslist.size() > 0) {
            map.put("focus_num", focuslist.size());
        } else {
            map.put("focus_num", 0);
        }
        List<Map<String, Object>> fanslist = appPersonalityService.getMyFans(user_id);
        if (fanslist != null && fanslist.size() > 0) {
            map.put("fans_num", fanslist.size());
        } else {
            map.put("fans_num", 0);
        }
        List<Map<String, Object>> releasePostList = appPersonalityService.getReleasePostList(user_id);
        if (releasePostList != null && releasePostList.size() > 0) {
            map.put("post_num", releasePostList.size());
        } else {
            map.put("post_num", 0);
        }
        // 新鲜事
        List<Map<String, Object>> sayingMapList = appPersonalityService.getMyOwnOrPublicDetails(user_id);
        if (sayingMapList != null && sayingMapList.size() > 0) {
            map.put("news_num", sayingMapList.size());
        } else {
            map.put("news_num", 0);
        }

        User user = userService.getById(user_id);
        map.put("isTeacher", user.getIsTeacher());
        if (convertVersionToDouble(version) <= 1.4) {
            //教职工身份细分：教导员、教师、教辅人员（宿管、安保、餐厅）
            LeaveAuditUserAuth leaveAuditUserAuth = leaveAuditUserAuthService.find(user.getSchoolId(), user_id);
            if (leaveAuditUserAuth != null) {
                LeaveAuditUserRole role = leaveAuditUserRoleService.findById(leaveAuditUserAuth.getRoleId());
                map.put("userAuthRoleId", role.getName());
                String showUserAuthRole = leaveAuditUserRoleService.showUserAuthRole(user_id, leaveAuditUserAuth.getRoleId(), role.getName());
                map.put("showUserAuthRole", showUserAuthRole);
                map.put("isAuth", true);
                map.put("allowCreateAuditUser", role.getAllowCreateAuditUser() == 1 ? true : false);
            } else {
                map.put("userAuthRoleId", null);
                map.put("showUserAuthRole", null);
                map.put("isAuth", false);
                map.put("allowCreateAuditUser", false);
            }
        } else {
            AuditUserAuth userAuth = auditUserAuthService.find(user.getSchoolId(), user_id);
            if (userAuth != null) {
                AuditUserRole role = auditUserRoleService.findById(userAuth.getRoleId());
                map.put("userAuthRoleId", role.getName());
                String showUserAuthRole = auditUserRoleService.showUserAuthRole(user_id, userAuth.getRoleId(), role.getName());
                map.put("showUserAuthRole", showUserAuthRole);
                map.put("isAuth", true);
                map.put("allowCreateAuditUser", role.getAllowCreateAuditUser() == 1 ? true : false);
            } else {
                map.put("userAuthRoleId", null);
                map.put("showUserAuthRole", null);
                map.put("isAuth", false);
                map.put("allowCreateAuditUser", false);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 编辑我的信息
     * @param user_id
     * @param response
     */
    @RequestMapping("/user_editOwnInfo")
    public void getMyOwnOrPublicPage(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        Object area = param.get("area");
        if (area != null) {
            if (area.toString().length() > 8) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "字数过多", null)));
                return;
            }
        }
        Object sig = param.get("sig");
        if (sig != null) {
            if (sig.toString().length() > 30) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "字数过多", null)));
                return;
            }
        }

        List<DiskFileItem> headimg = null;
        String fileid = null;
        try {
            headimg = (List<DiskFileItem>) param.get("headimg");
        } catch (Exception e) {
            headimg = null;
        }
        if (headimg != null) {
            DiskFileItem fileBean = headimg.get(0);
            byte[] fileContent = fileBean.get();
            String fileName = FileUtilsEx.getFileNameByPath(fileBean.getName());
            fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
        }
        User uEntity = new User();
        uEntity.setId(user_id);
        User user = userService.getUser(uEntity);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null)));
            return;
        }

        if (user.getStatus().shortValue() == 2) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用", null)));
            return;
        }
        if (null != area) {
            uEntity.setArea(area.toString());
        }
        if (null != sig) {
            uEntity.setSignature(sig.toString());
        }
        if (null != headimg) {
            uEntity.setHeadimg(fileid);
        }
        userService.updateUser(uEntity);
        // appPersonalityService.editInfo(user_id,fileid,area,sig);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
        return;
    }

    /**
     * 他人查看我个人信息或者自己
     * @param user_id
     * @param Id
     * @param response
     */
    @RequestMapping("/user_viewOtherPage")
    public void viewOtherPage(@ModelAttribute("user_id") Integer user_id, @RequestParam(required = false) Integer Id, // 用户id或者主页id
                              HttpServletResponse response) {
        Map<String, Object> map = null;
        if (Id == null) {
            // 查看自己的个人主页
            map = appPersonalityService.getMyOwnInfo(user_id);
            if (null == map || null == map.get("user_id")) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.NO_LOGIN, "账号不存在", null)));
                return;
            }
            // 关注数和粉丝数
            // List<Map<String, Object>> focuslist = appPersonalityService.getMyFocusOn(user_id);
            // if (focuslist != null && focuslist.size() > 0) {
            //     map.put("focus_num", focuslist.size());
            // } else {
            //     map.put("focus_num", 0);
            // }
            // List<Map<String, Object>> fanslist = appPersonalityService.getMyFans(user_id);
            // if (fanslist != null && fanslist.size() > 0) {
            //     map.put("fans_num", fanslist.size());
            // } else {
            //     map.put("fans_num", 0);
            // }

        } else if (Id != null) {
            // 他人查看我的主页
            map = appPersonalityService.getMyOwnInfo(Id);
            // 判断我有没有关注这个人
            Follow follow = new Follow();
            follow.setFollowerId(user_id);
            follow.setBeFollowedId(Id);
            follow.setIsCancel((short) 2);
            List<Follow> list = appPersonalityService.getFollow(follow);
            if (list.size() > 0 && list != null) {
                map.put("is_follow", 1);
            } else {
                map.put("is_follow", 2);
            }
            int myOwnId = user_id;
            map.put("myOwnId", myOwnId);
            // 关注数和粉丝数
            List<Map<String, Object>> focuslist = appPersonalityService.getMyFocusOn(Id);
            if (focuslist != null && focuslist.size() > 0) {
                map.put("focus_num", focuslist.size());
            } else {
                map.put("focus_num", 0);
            }
            List<Map<String, Object>> fanslist = appPersonalityService.getMyFans(Id);
            if (fanslist != null && fanslist.size() > 0) {
                map.put("fans_num", fanslist.size());
            } else {
                map.put("fans_num", 0);
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 我的公共主页
     * @param user_id
     * @param Id
     * @param response
     */
    @RequestMapping("/user_getMyPublicInfo")
    public void getMyPublicInfo(@ModelAttribute("user_id") Integer user_id, @RequestParam(required = false) Integer Id,
                                HttpServletResponse response) {
        // User u = appPersonalityService.getPublicPage(user_id);
        Map<String, Object> map = null;
        if (Id == null) {
            map = appPersonalityService.getMyOwnInfoo(user_id);
        } else {
            map = appPersonalityService.getMyOwnInfoo(Id);
            // 判断我有没有关注这个人
            Follow follow = new Follow();
            follow.setFollowerId(user_id);
            follow.setBeFollowedId(Id);
            follow.setIsCancel((short) 2);
            List<Follow> list = appPersonalityService.getFollow(follow);
            if (list.size() > 0 && list != null) {
                map.put("is_follow", 1);
            } else {
                map.put("is_follow", 2);
            }
            int myOwnId = user_id;
            map.put("myOwnId", myOwnId);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 他人或者自己查看我的新鲜事列表
     *
     * @param user_id
     * @param response
     * @param toUserId 他人的用户Id
     */
    @RequestMapping("/user_OtherOrPublicDetails")
    public void getOtherOrPublicDetails(@ModelAttribute("user_id") Integer user_id, // token
                                        HttpServletResponse response, Integer page, @RequestParam(required = false) Integer pagesize,
                                        @RequestParam(required = false) Integer toUserId, // 他人的用户Id
                                        String time) {
        if (page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
                return;
            }
        }
        // 1. 查看2个人是不是好友
        Follow follow = new Follow();
        follow.setFollowerId(user_id);
        follow.setBeFollowedId(toUserId);
        follow.setIsTwoWay((short) 1);
        follow.setIsCancel((short) 2);
        List<Follow> listFollow = appPersonalityService.getFollow(follow);
        Short is_friend = null;
        List<Map<String, Object>> sayingMap = null;
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        // 是同一个人，查看自己的
        if (toUserId == null) {
            sayingMap = appPersonalityService.getMyPublicDetails(user_id, time);
        } else {
            if (listFollow != null && listFollow.size() > 0) {
                // 是好友
                is_friend = 2;
                sayingMap = appPersonalityService.getOtherPublicDetails(toUserId, time, is_friend);
            } else {
                // 不是好友
                is_friend = 3;
                sayingMap = appPersonalityService.getOtherPublicDetails(toUserId, time, is_friend);
            }
        }
        if (sayingMap != null && sayingMap.size() > 0) {
            for (Map<String, Object> sayMap : sayingMap) {
                // 新鲜事id
                Integer id = Integer.parseInt(sayMap.get("id").toString());

                // 浏览量watch_count 如果redis里面不为空
                if (null != RedisUtils.getValueByKey("saying_watch_count_" + id)) {
                    int watch_count = Integer.parseInt(RedisUtils.getValueByKey("saying_watch_count_" + id).toString());
                    sayMap.put("watch_count", watch_count);
                }

                // 点赞数
                if (null != RedisUtils.getValueByKey("saying_like_" + id)) {
                    int like_number = Integer.parseInt(RedisUtils.getValueByKey("saying_like_" + id));
                    sayMap.put("like_number", like_number);
                }

                // 评论数
                if (null != RedisUtils.getValueByKey("saying_comment_" + id)) {
                    int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + id));
                    sayMap.put("comment_number", comment_number);
                }

                // 如果不是转发
                short is_forward = Short.parseShort(sayMap.get("is_forward").toString());
                if (is_forward == 2) {
                    Object at_user = sayMap.get("at_user");
                    if (at_user != null && StringUtils.isNotBlank(at_user.toString())) {
                        String[] ids = at_user.toString().split(",");
                        List<Map<String, Object>> atList = userService.findUserByIds(ids);
                        sayMap.put("atList", atList);
                    } else {
                        sayMap.put("atList", null);
                    }
                } else {
                    sayMap.put("atList", null);
                }

                // 获取点赞人列表
                SayingLike entity = new SayingLike();
                entity.setSayingId(id);
                List<SayingLike> likeList = sayingLikeService.findList(entity);
                if (likeList != null && likeList.size() > 3) {
                    likeList = likeList.subList(0, 2);
                }
                sayMap.put("likeList", likeList);

                // 获取回复列表
                List<Map<String, Object>> commentList = sayingCommentService.findCommentAndReplyList(id);
                sayMap.put("commentList", commentList);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", sayingMap);
        map.put("time", time);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 我发布过的帖子列表
     * @param user_id
     * @param response
     * @param page
     * @param pagesize
     */
    @RequestMapping("/user_releasePostList")
    public void getReleasePostList(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response,
                                   Integer page, @RequestParam(required = false) Integer pagesize) {

        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> map = appPersonalityService.getReleasePostList(user_id);

        PostLike postLike = new PostLike();
        postLike.setUserId(user_id);
        if (map != null && map.size() > 0) {
            for (Map<String, Object> mm : map) {
                int post_id = Integer.parseInt(mm.get("post_id").toString());
                // 帖子点赞数,如果redis不为空,拿redis的点赞数,否则直接数据库
                if (null != RedisUtils.getValueByKey("post_like_" + post_id)) {
                    Integer like_num = Integer.valueOf(RedisUtils.getValueByKey("post_like_" + post_id).toString());
                    mm.put("like_number", like_num);
                } else {
                    mm.put("like_number", null);
                }

                Integer comment_number = appPersonalityService
                        .getPostComment(Integer.parseInt(mm.get("post_id").toString()));
                mm.put("comment_number", comment_number);

                String img = null;
                if (mm.get("img") != null) {
                    img = mm.get("img").toString();
                }
                ;
                if (img != null && img != "") {
                    String[] imgnew = img.split(",");
                    mm.put("img_num", imgnew.length);
                } else {
                    mm.put("img_num", 0);
                }
                postLike.setPostId(post_id);

                List<PostLike> list = appPersonalityService.getPostLike(postLike);
                if (list.size() > 0) {
                    mm.put("own_like", 1);
                } else {
                    mm.put("own_like", 2);
                }
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 我的关注
     * @param user_id
     * @param response
     * @param page
     * @param pagesize
     */
    @RequestMapping("/user_myFocusOn")
    public void getMyFocusOn(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response, Integer page,
                             @RequestParam(required = false) Integer pagesize) {
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> map = appPersonalityService.getMyFocusOn(user_id);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 我的fans
     * @param user_id
     * @param response
     * @param page
     * @param pagesize
     */
    @RequestMapping("/user_myFans")
    public void getMyFans(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response, Integer page,
                          @RequestParam(required = false) Integer pagesize) {
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> map = appPersonalityService.getMyFans(user_id);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 我的收藏
     * @param user_id
     * @param response
     * @param page
     * @param pagesize
     */
    @RequestMapping("/user_myCollect")
    public void getMyCollect(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response, Integer page,
                             @RequestParam(required = false) Integer pagesize) {

        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);

        List<Map<String, Object>> map = appPersonalityService.getMyCollect(user_id);
        if (map != null && map.size() > 0) {

            for (Map<String, Object> mm : map) {
                Integer type = Integer.parseInt(mm.get("type").toString());
                Integer post_id = Integer.parseInt(mm.get("post_id").toString());
                // 如果是帖子
                if (type == 1) {
                    Map<String, Object> post = appPersonalityService.getPostInfo(post_id);
                    if (Integer.parseInt(post.get("is_delete").toString()) == 1) {
                        mm.put("is_detele", 1);
                    }
                    String img = null;
                    if (post.get("img") != null) {
                        img = post.get("img").toString();
                        String[] newimg = img.split(",");
                        if (newimg.length > 0) {
                            post.put("first_img", newimg[0]);
                        } else {
                            post.put("first_img", null);
                        }
                        mm.put("first_img", post.get("first_img"));
                    } else {
                        mm.put("first_img", null);
                    }
                    if (post.get("name") != null) {
                        mm.put("username", post.get("name"));
                    } else {
                        mm.put("username", null);
                    }
                    if (post.get("headimg") != null && post.get("headimg") != "") {
                        mm.put("headimg", post.get("headimg"));
                    } else {
                        mm.put("headimg", null);
                    }
                    if (post.get("title") != null) {
                        mm.put("title", post.get("title"));
                    } else {
                        mm.put("title", null);
                    }

                    // 如果是新鲜事
                } else {
                    Map<String, Object> saying = appPersonalityService.getSayingInfo(post_id);
                    if (Integer.parseInt(saying.get("is_delete").toString()) == 1) {
                        mm.put("is_detele", 1);
                    }
                    String img = null;
                    if (saying.get("img") != null) {
                        img = saying.get("img").toString();
                        String[] newimg = img.split(",");
                        if (newimg.length > 0) {
                            saying.put("first_img", newimg[0]);
                        } else {
                            saying.put("first_img", null);
                        }
                        mm.put("first_img", saying.get("first_img"));
                    } else {
                        mm.put("first_img", null);
                    }
                    if (saying.get("name") != null) {
                        mm.put("username", saying.get("name"));
                    } else {
                        mm.put("username", null);
                    }
                    if (saying.get("headimg") != null && saying.get("headimg") != "") {
                        mm.put("headimg", saying.get("headimg"));
                    } else {
                        mm.put("headimg", null);
                    }
                    if (saying.get("content") != null) {
                        mm.put("content", saying.get("content"));
                    } else {
                        mm.put("content", null);
                    }
                }
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 取消收藏
     * @param user_id
     * @param collectId
     * @param response
     */
    @RequestMapping("/user_cancelCollect")
    public void getMyCollect(@ModelAttribute("user_id") Integer user_id, Integer collectId,
                             HttpServletResponse response) {
        appPersonalityService.cancelMyCollect(user_id, collectId, new Date());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
        return;
    }

    /**
     * 已读评论
     *
     * @param user_id
     * @param response
     * @param id
     */
    @RequestMapping("user_isRead")
    public void isRead(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response, Integer id,
                       Integer type) {
        if (type == 1) {
            // 消息回复
            appPersonalityService.updateIsRead(id);
        } else if (type == 2) {
            // 艾特我的
            appPersonalityService.updateIsReadAT(id);
        } else if (type == 3) {
            // 点赞我的
            appPersonalityService.updateIsReadLike(id);
        } else if (type == 4) {
            // 提问的
            appPersonalityService.updateIsReadquestion(id);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
        return;
    }

    /**
     * 消息列表
     *
     * @param user_id
     * @param page
     * @param pagesize
     * @param response
     */
    @RequestMapping("user_messageList")
    public void getMyMessageList(@ModelAttribute("user_id") Integer user_id, Integer page,
                                 @RequestParam(required = false) Integer pagesize, String time, HttpServletResponse response) {
        if (page == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
                return;
            }
        }
        Map<String, Object> map = new HashMap<>();
        // 提问未读数
        int questionNotReadNum = messageMapperEx.getQuestionNotReadNum(user_id);
        // 提问回复未读数
        int questionReplyNotReadNum = messageMapperEx.getQuestionReplyNotReadNum(user_id);
        Integer questionNum = questionNotReadNum + questionReplyNotReadNum;

        if (questionNum > 0) {
            map.put("is_question", 1);
        } else {
            map.put("is_question", 2);
        }

        // 新艾特我的人数
        /*
         * //发布帖子@未读数 int postNotReadNum = messageMapperEx.getPostNotReadNum(user_id);
         * //帖子评论@未读数 int postCommentNotReadNum =
         * messageMapperEx.getPostCommentNotReadNum(user_id); //新鲜事@未读数 int
         * sayingNotReadNum = messageMapperEx.getSayingNotReadNum(user_id); Integer
         * at_num = postNotReadNum + postCommentNotReadNum + sayingNotReadNum;
         */
        AtMessage am = new AtMessage();
        am.setIsRead((short) 2);
        am.setToerId(user_id);
        am.setIsDetele((short) 2);
        List<AtMessage> atlist = appPersonalityService.getAtMessageList(am);
        Integer at_num = atlist.size();
        if (atlist != null && at_num > 0) {
            for (AtMessage atMe : atlist) {
                Short typeId = atMe.getType();
                Integer objectId = atMe.getObjectId();
                if (typeId == 1) {
                    // 帖子发布
                    Short postStatus = appPersonalityService.getPostStatus(objectId);
                    if (postStatus == 1) {
                        at_num = at_num - 1;
                    }
                } else if (typeId == 2) {
                    // 帖子盖楼
                    Short commentStatus = appPersonalityService.getCommentStatus(objectId);
                    if (commentStatus == 1) {
                        at_num = at_num - 1;
                    }
                } else if (typeId == 3) {
                    // 回复楼
                    Short commentReplyStatus = appPersonalityService.getCommentReplyStatus(objectId);
                    if (commentReplyStatus == 1) {
                        at_num = at_num - 1;
                    }
                } else if (typeId == 4) {
                    // 发布新鲜事
                    Short sayingStatus = appPersonalityService.getSayingStatus(objectId);
                    if (sayingStatus == 1) {
                        at_num = at_num - 1;
                    }
                }
            }
        }

        // 帖子点赞未读数
        int postLikeNotReadNum = messageMapperEx.getPostLikeNotReadNum(user_id);
        // 楼层点赞未读数
        int postComemntLikeNotReadNum = messageMapperEx.getPostComemntLikeNotReadNum(user_id);
        // 新鲜事和新鲜事评论点赞未读数
        int sayingLikeNotReadNum = messageMapperEx.getSayingLikeNotReadNum(user_id);
        Integer zan_num = postLikeNotReadNum + postComemntLikeNotReadNum + sayingLikeNotReadNum;
        /*
         * LikeMessage lm = new LikeMessage(); lm.setIsRead((short)2);
         * lm.setToerId(user_id); lm.setIsDetele((short)2); List<LikeMessage> lklist =
         * appPersonalityService.getLikeMessageList(lm); Integer zan_num =
         * lklist.size(); if(lklist != null && zan_num > 0) { for(LikeMessage likelist :
         * lklist) { Short typeId = likelist.getType(); Integer objectId =
         * likelist.getObjectId(); if(typeId == 1) { //帖子 Short postStatus =
         * appPersonalityService.getPostStatus(objectId); if(postStatus == 1) { zan_num
         * = zan_num - 1; } }else if(typeId == 2) { //帖子主评论 Short commentStatus =
         * appPersonalityService.getCommentStatus(objectId); if(commentStatus == 1) {
         * zan_num = zan_num - 1; } }else if(typeId == 3) { //新鲜事 Short sayingStatus =
         * appPersonalityService.getSayingStatus(objectId); if(sayingStatus == 1) {
         * zan_num = zan_num - 1; } }else if(typeId == 4) { //新鲜事主评论 Short
         * sayingCommentStatus = appPersonalityService.selectSayingComm(objectId);
         * if(sayingCommentStatus != null) { if(sayingCommentStatus == 1) { zan_num =
         * zan_num - 1; } } } } }
         */
        // 消息列表
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> messageList = appPersonalityService.getMessageList(user_id, time);
        if (messageList != null && messageList.size() > 0) {

            for (Map<String, Object> mList : messageList) {
                Integer id = Integer.parseInt(mList.get("object_id").toString());
                Integer type = Integer.parseInt(mList.get("type").toString());
                if (type == 1) {
                    // 帖子评论
                    Map<String, Object> postCommMap = appPersonalityService.selectPostCommen(id);
                    if (postCommMap != null) {
                        if (Integer.parseInt(postCommMap.get("is_delete").toString()) == 1) {
                            mList.put("is_detele", 1);
                        } else {
                            mList.put("post_id", Integer.parseInt(postCommMap.get("id").toString()));
                        }
                    }

                } else if (type == 2) {
                    // 帖子回复
                    Map<String, Object> postRe = appPersonalityService.selectPostRe(id);
                    if (postRe != null) {
                        if (Integer.parseInt(postRe.get("is_delete").toString()) == 1) {
                            mList.put("is_detele", 1);
                        } else {
                            mList.put("post_id", Integer.parseInt(postRe.get("post_id").toString()));
                        }
                    }

                } else if (type == 3) {
                    // 新鲜事评论
                    Map<String, Object> sayingCommen = appPersonalityService.selectSayingCommen(id);
                    if (sayingCommen != null) {
                        if (Integer.parseInt(sayingCommen.get("is_delete").toString()) == 1) {
                            mList.put("is_detele", 1);
                        } else {
                            mList.put("post_id", Integer.parseInt(sayingCommen.get("saying_id").toString()));
                        }
                    }
                } else if (type == 4) {
                    // 新鲜事回复
                    Map<String, Object> sayingRe = appPersonalityService.selectSayingRe(id);
                    if (sayingRe != null) {
                        if (Integer.parseInt(sayingRe.get("is_delete").toString()) == 1) {
                            mList.put("is_detele", 1);
                        } else {
                            mList.put("post_id", Integer.parseInt(sayingRe.get("saying_id").toString()));
                        }
                    }

                } else if (type == 5) {
                    // 留言
                    Map<String, Object> leaveMess = appPersonalityService.selectLeaveMess(id);
                    if (Integer.parseInt(leaveMess.get("is_delete").toString()) == 1) {
                        mList.put("is_detele", 1);
                    } else {
                        mList.put("post_id", Integer.parseInt(leaveMess.get("page_id").toString()));
                    }
                }
            }
        }
        map.put("at_num", at_num);
        map.put("zan_num", zan_num);
        map.put("messageList", messageList);
        map.put("time", time);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 艾特我的消息列表
     * @param user_id
     * @param page
     * @param pagesize
     * @param time
     * @param response
     */
    @RequestMapping("/user_atMessageList")
    public void getAtMessageList(@ModelAttribute("user_id") Integer user_id, Integer page,
                                 @RequestParam(required = false) Integer pagesize, String time, HttpServletResponse response) {
        if (page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
                return;
            }
        }
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> atMessageList = appPersonalityService.getAtMessageList(user_id, time);
        if (atMessageList != null && atMessageList.size() > 0) {

            for (Map<String, Object> atMessage : atMessageList) {
                Integer typeId = Integer.parseInt(atMessage.get("type").toString());
                Integer objectId = Integer.parseInt(atMessage.get("object_id").toString());
                if (typeId == 1) {
                    // 帖子发布
                    Map<String, Object> postSta = appPersonalityService.getPostSta(objectId);
                    if (Integer.parseInt(postSta.get("is_delete").toString()) == 1) {
                        atMessage.put("is_detele", 1);
                    } else {
                        atMessage.put("post_id", Integer.parseInt(postSta.get("id").toString()));
                    }
                } else if (typeId == 2) {
                    // 帖子盖楼
                    Map<String, Object> commentSta = appPersonalityService.getCommentSta(objectId);
                    if (Integer.parseInt(commentSta.get("is_delete").toString()) == 1) {
                        atMessage.put("is_detele", 1);
                    } else {
                        atMessage.put("post_id", Integer.parseInt(commentSta.get("post_id").toString()));
                    }
                } else if (typeId == 3) {
                    // 回复楼
                    Map<String, Object> commentReplySta = appPersonalityService.getCommentReplySta(objectId);
                    if (Integer.parseInt(commentReplySta.get("is_delete").toString()) == 1) {
                        atMessage.put("is_detele", 1);
                    } else {
                        atMessage.put("post_id", Integer.parseInt(commentReplySta.get("post_id").toString()));
                    }
                } else if (typeId == 4) {
                    // 发布新鲜事
                    Map<String, Object> sayingSta = appPersonalityService.getSayingSta(objectId);
                    if (Integer.parseInt(sayingSta.get("is_delete").toString()) == 1) {
                        atMessage.put("is_detele", 1);
                    } else {
                        atMessage.put("post_id", Integer.parseInt(sayingSta.get("id").toString()));
                    }
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("atMessageList", atMessageList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 点赞我的消息列表
     *
     * @param user_id
     * @param page
     * @param pagesize
     * @param response
     */
    @RequestMapping("/user_zanMessageList")
    public void getZanMessageList(@ModelAttribute("user_id") Integer user_id, Integer page,
                                  @RequestParam(required = false) Integer pagesize, String time, HttpServletResponse response) {

        if (page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
                return;
            }
        }
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> zanMessageList = appPersonalityService.getZanMessageList(user_id, time);
        if (zanMessageList != null && zanMessageList.size() > 0) {

            for (Map<String, Object> zanMessage : zanMessageList) {
                // 用户查看点赞列表时自动设置为已读
                appPersonalityService.updateIsReadLike(Integer.valueOf(String.valueOf(zanMessage.get("id"))));

                Integer typeId = Integer.parseInt(zanMessage.get("type").toString());
                Integer objectId = Integer.parseInt(zanMessage.get("object_id").toString());
                if (typeId == 1) {
                    // 帖子
                    Map<String, Object> postStatus = appPersonalityService.getPostSta(objectId);
                    if (Integer.parseInt(postStatus.get("is_delete").toString()) == 1) {
                        zanMessage.put("is_detele", 1);
                    } else {
                        zanMessage.put("post_id", Integer.parseInt(postStatus.get("id").toString()));
                    }
                } else if (typeId == 2) {
                    // 帖子主评论
                    Map<String, Object> commentSta = appPersonalityService.getCommentSta(objectId);
                    if (Integer.parseInt(commentSta.get("is_delete").toString()) == 1) {
                        zanMessage.put("is_detele", 1);
                    } else {
                        zanMessage.put("post_id", Integer.parseInt(commentSta.get("post_id").toString()));
                    }
                } else if (typeId == 3) {
                    // 新鲜事
                    Map<String, Object> sayingStatus = appPersonalityService.getSayingSta(objectId);
                    if (Integer.parseInt(sayingStatus.get("is_delete").toString()) == 1) {
                        zanMessage.put("is_detele", 1);
                    } else {
                        zanMessage.put("post_id", Integer.parseInt(sayingStatus.get("id").toString()));
                    }
                } else if (typeId == 4) {
                    // 新鲜事主评论
                    Map<String, Object> sayingCommentStatus = appPersonalityService.selectSayingCommen(objectId);
                    if (sayingCommentStatus != null) {
                        if (Integer.parseInt(sayingCommentStatus.get("is_delete").toString()) == 1) {
                            zanMessage.put("is_detele", 1);
                        } else {
                            zanMessage.put("post_id",
                                    Integer.parseInt(sayingCommentStatus.get("saying_id").toString()));
                        }
                    }

                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("zanMessageList", zanMessageList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 提问消息列表
     *
     * @param user_id
     * @param page
     * @param pagesize
     * @param response
     */
    @RequestMapping("/user_questionMessList")
    public void getQuestionMessList(@ModelAttribute("user_id") Integer user_id, Integer page, String time,
                                    @RequestParam(required = false) Integer pagesize, HttpServletResponse response) {
        if (page == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                ServletUtilsEx.renderText(response,
                        JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
                return;
            }
        }
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> questionMessList = appPersonalityService.getQuestionMessList(user_id, time);
        if (questionMessList != null && questionMessList.size() > 0) {
            for (Map<String, Object> questionMessage : questionMessList) {
                Integer typeId = Integer.parseInt(questionMessage.get("type").toString());
                Integer objectId = Integer.parseInt(questionMessage.get("object_id").toString());
                Short noticeStatus = null;
                if (typeId == 1) {
                    // 主提问
                    noticeStatus = appPersonalityService.getNoticeStatus(objectId);
                    if (noticeStatus != null) {
                        if (noticeStatus == 1) {
                            questionMessage.put("is_detele", 1);
                        }
                    }

                }
                if (typeId == 2) {
                    NoticeQuestion qEntity = new NoticeQuestion();
                    qEntity.setId(objectId);
                    NoticeQuestion noticeQuestion = noticeQuestionService.getNoticeQuestion(qEntity);
                    if (noticeQuestion != null) {
                        questionMessage.put("notice", noticeQuestion.getNoticeId());
                        GroupNotice nEntity = new GroupNotice();
                        nEntity.setId(noticeQuestion.getNoticeId());
                        GroupNotice groupNotice = noticeService.getGroupNotice(nEntity);
                        if (groupNotice != null && groupNotice.getIsDelete().shortValue() == 1) {
                            questionMessage.put("is_detele", 1);
                        }
                    }
                }
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("questionMessList", questionMessList);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 获取帮助列表
     *
     * @param user_id
     * @param page
     * @param pagesize
     * @param response
     */
    @RequestMapping("/user_helpList")
    public void getHelpList(@ModelAttribute("user_id") Integer user_id, Integer page,
                            @RequestParam(required = false) Integer pagesize, HttpServletResponse response) {
        if (user_id == null || user_id == 0) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请先登录!", null)));
            return;
        }
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> helpList = appPersonalityService.getHelpList();
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", helpList)));
        return;
    }

    /**
     * 问题详情
     *
     * @param user_id  token身份认证
     * @param helpId   列表的主键Id
     * @param response
     */
    @RequestMapping("/user_helpDetail")
    public void getHelpDetail(@ModelAttribute("user_id") Integer user_id, Integer helpId,
                              HttpServletResponse response) {
        if (user_id == null || user_id == 0) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请先登录!", null)));
            return;
        }
        Map<String, Object> helpDetail = appPersonalityService.getHelpDetail(helpId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", helpDetail)));
        return;
    }

    /**
     * 添加反馈
     *
     * @param user_id
     * @param content
     * @param response
     * @throws Exception
     */
    @RequestMapping("/user_addFeedback")
    public void addFeedback(@ModelAttribute("user_id") Integer user_id, String content, HttpServletResponse response)
            throws Exception {
        if (user_id == null || user_id == 0) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请先登录!", null)));
            return;
        }
        if (content == null || content == "" || content.length() < 10 || content.length() > 1000) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请输入超过10个字并且小于1000字!", null)));
            return;
        }
        FeedBack feedBack = new FeedBack();
        String con = EmojiUtil.resolveToByteFromEmoji(content);
        feedBack.setContent(con);
        feedBack.setCreateTime(new Date());
        feedBack.setUserId(user_id);
        feedBack.setIsRead((short) 2);
        appPersonalityService.addFeedback(feedBack);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
        return;
    }

    /**
     * 输入新密码
     *
     * @param user_id
     * @param response
     * @param newPass
     * @param verifyPass
     */
    @RequestMapping("/user_enterNewPass")
    public void enterNewPass(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response, String newPass,
                             String verifyPass) {
        if (!newPass.equals(verifyPass) || StringUtils.isBlank(verifyPass) || StringUtils.isBlank(newPass)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "两次密码不一致或没传值!", null)));
            return;
        }
        User user = new User();
        user.setId(user_id);
        User u = userService.getUser(user);
        user.setPassword(StringUtil.MD5Encode(newPass + u.getSalt()));
        userService.updateUser(user);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "修改密码成功", null)));
        return;
    }

    /**
     * 设置推送
     *
     * @param user_id
     * @param response
     * @param is_push_at
     * @param is_push_cmt
     * @param is_push_like
     * @param is_push_question
     */
    @RequestMapping("/user_setPush")
    public void setPush(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response,
                        @RequestParam(required = false) String is_push_at, @RequestParam(required = false) String is_push_cmt,
                        @RequestParam(required = false) String is_push_like,
                        @RequestParam(required = false) String is_push_question) {
        if (StringUtils.isNotBlank(is_push_at) && StringUtils.isNotBlank(is_push_cmt)
                && StringUtils.isNotBlank(is_push_like) && StringUtils.isNotBlank(is_push_question)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "必要参数为空!", null)));
            return;
        }
        User user = new User();
        user.setId(user_id);
        if (StringUtils.isNotBlank(is_push_at)) {
            user.setIsPushAt(Short.parseShort(is_push_at));
        }
        if (StringUtils.isNotBlank(is_push_cmt)) {
            user.setIsPushCmt(Short.parseShort(is_push_cmt));
        }
        if (StringUtils.isNotBlank(is_push_like)) {
            user.setIsPushLike(Short.parseShort(is_push_like));
        }
        if (StringUtils.isNotBlank(is_push_question)) {
            user.setIsPushQuestion(Short.parseShort(is_push_question));
        }
        userService.updateUser(user);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "设置推送成功", null)));
        return;
    }

    /**
     * 获取推送
     *
     * @param user_id
     * @param response
     */
    @RequestMapping("/user_getPush")
    public void getPush(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response) {
        Map<String, Object> map = appPersonalityService.selectPush(user_id);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 更新个人信息
     *
     * @param userId      用户ID
     * @param instituteId 学院ID
     * @param gradeId     年级ID
     * @param classId     班级ID
     * @param stuNumber   学号或工号
     * @param response
     */
    @RequestMapping("app_updateUserInfo")
    public void app_updateUserInfo(@ModelAttribute("user_id") Integer userId,
                                   @RequestParam(required = false) Integer instituteId,
                                   @RequestParam(required = false) Integer gradeId,
                                   @RequestParam(required = false) Integer classId,
                                   @RequestParam(required = false) String stuNumber,
                                   HttpServletResponse response) {
        if (userId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NO_LOGIN, "未登录", null)));
            return;
        }

        User user = userService.getById(userId);
        if (instituteId != null) {
            user.setInstituteId(instituteId);
        }
        if (gradeId != null) {
            user.setGradeId(gradeId);
        }
        if (classId != null) {
            user.setClassId(classId);
        }
        if (StringUtils.isNotBlank(stuNumber)) {
            user.setStuNumber(stuNumber);
        }

        user.setUpdateTime(new Date());
        userService.updateUser(user);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }


    /**
     * 系统通知列表
     *
     * @param page     页码
     * @param type     类型（不传，默认为14系统消息）
     * @param pageSize 每页记录数
     * @param response
     */
    @RequestMapping("app_findPushList")
    public void app_findPushList(Integer page,
                                 @ModelAttribute("user_id") Integer userId,
                                 @RequestParam(defaultValue = "14", required = false) Short type,
                                 @RequestParam(defaultValue = "10", required = false) Integer pageSize, HttpServletResponse response) {
        if (page == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        PushCriteria example = new PushCriteria();
        example.setOrderByClause("id desc");
        example.createCriteria()
                .andMessageTypeEqualTo(type)
                .andUserIdEqualTo(userId);

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Push> pushList = pushSerivce.findList(example);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", pushList)));
    }
}
