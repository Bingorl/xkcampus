package com.biu.wifi.campus.controller.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.dao.model.CommentReplyMessage;
import com.biu.wifi.campus.dao.model.LikeMessage;
import com.biu.wifi.campus.dao.model.Push;
import com.biu.wifi.campus.dao.model.Saying;
import com.biu.wifi.campus.dao.model.SayingComment;
import com.biu.wifi.campus.dao.model.SayingCommentLike;
import com.biu.wifi.campus.dao.model.SayingLike;
import com.biu.wifi.campus.dao.model.SayingReply;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.AtMessageService;
import com.biu.wifi.campus.service.CommentReplyMessageService;
import com.biu.wifi.campus.service.LikeMessageService;
import com.biu.wifi.campus.service.PushSerivce;
import com.biu.wifi.campus.service.SayingCommentLikeService;
import com.biu.wifi.campus.service.SayingCommentService;
import com.biu.wifi.campus.service.SayingLikeService;
import com.biu.wifi.campus.service.SayingReplyService;
import com.biu.wifi.campus.service.SayingService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.CoreConstants;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;

@Controller
public class AppSayingController extends AuthenticatorController {

    @Autowired
    private SayingService sayingService;

    @Autowired
    private SayingLikeService sayingLikeService;

    @Autowired
    private SayingCommentService sayingCommentService;

    @Autowired
    private SayingReplyService sayingReplyService;

    @Autowired
    private UserService userService;

    @Autowired
    private AtMessageService atMessageService;

    @Autowired
    private SayingCommentLikeService sayingCommentLikeService;

    @Autowired
    private LikeMessageService likeMessageService;

    @Autowired
    private CommentReplyMessageService commentReplyMessageService;

    @Autowired
    private PushSerivce pushSerivce;

    /**
     * 获取新鲜事列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param time
     */
    @RequestMapping("/user_findSayingList")
    public void user_findSayingList(HttpServletRequest request, HttpServletResponse response,
                                    @ModelAttribute("user_id") Integer user_id, Integer page, Integer pageSize, String time) {
        if (page == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = sayingService.user_findSayingList(user_id, time);

        if (list != null && list.size() > 0) {
            for (Map<String, Object> sayingMap : list) {
                //新鲜事id
                Integer sayingId = Integer.valueOf(sayingMap.get("id").toString());

                //浏览量watch_count  如果redis里面不为空
                if (null != RedisUtils.getValueByKey("saying_watch_count_" + sayingId)) {
                    int watch_count = Integer.parseInt(RedisUtils.getValueByKey("saying_watch_count_" + sayingId));
                    sayingMap.put("watch_count", watch_count);
                }

                //点赞数
                if (null != RedisUtils.getValueByKey("saying_like_" + sayingId)) {
                    int like_number = Integer.parseInt(RedisUtils.getValueByKey("saying_like_" + sayingId));
                    sayingMap.put("like_number", like_number);
                }
                //评论数
                if (null != RedisUtils.getValueByKey("saying_comment_" + sayingId)) {
                    int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + sayingId));
                    sayingMap.put("comment_number", comment_number);
                }

                //如果不是转发
                short is_forward = Short.parseShort(sayingMap.get("is_forward").toString());
                if (is_forward == 2) {
                    Object at_user = sayingMap.get("at_user");
                    if (at_user != null && StringUtils.isNotBlank(at_user.toString())) {
                        String[] ids = at_user.toString().split(",");
                        List<Map<String, Object>> atList = userService.findUserByIds(ids);
                        sayingMap.put("atList", atList);
                    } else {
                        sayingMap.put("atList", null);
                    }
                } else {
                    sayingMap.put("atList", null);
                }

                //获取点赞人列表
                SayingLike entity = new SayingLike();
                entity.setSayingId(sayingId);
                List<SayingLike> likeList = sayingLikeService.findList(entity);
                if (likeList != null && likeList.size() > 3) {
                    likeList = likeList.subList(0, 2);
                }
                sayingMap.put("likeList", likeList);

                //获取15条评论回复列表
                List<Map<String, Object>> commentList = sayingCommentService.findCommentAndReplyList(sayingId);
                sayingMap.put("commentList", commentList);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("time", time);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取新鲜事列表15条评论回复
     *
     * @param request
     * @param response
     * @param user_id
     * @param saying_id
     */
    @RequestMapping("/user_findSayingListCom")
    public void user_findSayingListCom(HttpServletRequest request, HttpServletResponse response,
                                       @ModelAttribute("user_id") Integer user_id, Integer saying_id) {
        if (saying_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        //评论数
        if (null != RedisUtils.getValueByKey("saying_comment_" + saying_id)) {
            int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + saying_id));
            map.put("comment_number", comment_number);
        } else {
            //获取新鲜事信息
            Saying saying = sayingService.getById(saying_id);
            map.put("comment_number", saying.getCommentNumber());
        }


        //获取15条评论回复列表
        List<Map<String, Object>> commentList = sayingCommentService.findCommentAndReplyList(saying_id);
        map.put("commentList", commentList);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 新鲜事详情
     *
     * @param request
     * @param response
     * @param user_id
     * @param saying_id
     */
    @RequestMapping("/user_getSayingInfo")
    public void user_getSayingInfo(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("user_id") Integer user_id, Integer saying_id) {
        if (saying_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        Map<String, Object> map = sayingService.user_getSayingInfo(user_id, saying_id);
        //是否删除
        int is_delete = Integer.parseInt(map.get("is_delete").toString());
        if (is_delete == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(10002, "新鲜事已不存在", null));
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

        //如果不是转发
        short is_forward = Short.parseShort(map.get("is_forward").toString());
        if (is_forward == 2) {
            Object at_user = map.get("at_user");
            if (at_user != null && StringUtils.isNotBlank(at_user.toString())) {
                String[] ids = at_user.toString().split(",");
                List<Map<String, Object>> atList = userService.findUserByIds(ids);
                map.put("atList", atList);
            } else {
                map.put("atList", null);
            }
        } else {
            map.put("atList", null);
        }

        //点赞数
        if (null != RedisUtils.getValueByKey("saying_like_" + saying_id)) {
            int like_number = Integer.parseInt(RedisUtils.getValueByKey("saying_like_" + saying_id));
            map.put("like_number", like_number);
        }
        //评论数
        if (null != RedisUtils.getValueByKey("saying_comment_" + saying_id)) {
            int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + saying_id));
            map.put("comment_number", comment_number);
        }

        //将浏览量存放进redis
        if (null != RedisUtils.getValueByKey("saying_watch_count_" + saying_id)) {
            int watch_count = Integer.parseInt(RedisUtils.getValueByKey("saying_watch_count_" + saying_id).toString());
            RedisUtils.addValue("saying_watch_count_" + saying_id, String.valueOf(watch_count + 1), null);
        } else {
            RedisUtils.addValue("saying_watch_count_" + saying_id, String.valueOf(Integer.parseInt(map.get("watch_count").toString()) + 1), null);
        }

        int watchCount = Integer.parseInt(RedisUtils.getValueByKey("saying_watch_count_" + saying_id).toString());
        map.put("watch_count", watchCount);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取新鲜事评论列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param time
     * @param saying_id
     */
    @RequestMapping("/user_findSayingCommentList")
    public void user_findSayingCommentList(HttpServletRequest request, HttpServletResponse response,
                                           @ModelAttribute("user_id") Integer user_id, Integer page, Integer pageSize, String time, Integer saying_id) {
        if (page == null || saying_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = sayingCommentService.user_findSayingCommentList(user_id, time, saying_id);
        if (list != null && list.size() > 0) {
            for (Map<String, Object> commentMap : list) {
                //评论id
                Integer commentId = Integer.valueOf(commentMap.get("id").toString());

                //获取回复列表
                List<Map<String, Object>> replyList = sayingReplyService.findReplyListFromCommentId(commentId);
                commentMap.put("replyList", replyList);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("time", time);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 发布新鲜事
     *
     * @param request
     * @param response
     * @param user_id
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/user_addSaying")
    public void user_addSaying(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("user_id") Integer user_id) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        //内容
        String content = null;
        try {
            content = param.get("content").toString();
        } catch (Exception e) {
            content = null;
        }
        //图片
        List<DiskFileItem> imgList = null;
        try {
            imgList = (List<DiskFileItem>) param.get("img");
        } catch (Exception e) {
            imgList = null;
        }
        //@的人
        String at_user = null;
        try {
            at_user = param.get("at_user").toString();
        } catch (Exception e) {
            at_user = null;
        }
        //帖子私密性 1仅自己可见2仅好友可见3公开
        Short privacy = null;
        try {
            privacy = Short.valueOf(param.get("privacy").toString());
        } catch (Exception e) {
            privacy = null;
        }

        if (StringUtils.isBlank(content) || privacy == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取发布人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //如果是仅自己可见，不可@
        if (privacy.shortValue() == 1) {
            if (StringUtils.isNotBlank(at_user)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不可@他人", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        String img = "";
        if (imgList != null && imgList.size() > 0) {
            for (DiskFileItem fileBean : imgList) {
                byte[] fileContent = fileBean.get();
                String fileName = FileUtilsEx.getFileNameByPath(fileBean.getName());
                String fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
                img = img + "," + fileid;
            }
            img = img.substring(1);
        }

        //新增新鲜事
        Saying saying = new Saying();
        saying.setCreatorId(user_id);
        saying.setAtUser(at_user);
        saying.setContent(content);
        saying.setImg(img);
        saying.setPrivacy(privacy);
        saying.setCreateTime(new Date());
        sayingService.addSaying(saying);

        //修改用户发布新鲜事的数量
        User uu = new User();
        uu.setId(user_id);
        uu.setNewsNum(user.getNewsNum().intValue() + 1);
        userService.updateUser(uu);

        //如果有@的人
        if (StringUtils.isNotBlank(at_user)) {
            if (content.length() > 50) {
                content = content.substring(0, 50);
            }

            List<User> toerList = new ArrayList<>();
            for (String id : at_user.split(",")) {
                User toer = userService.getById(Integer.valueOf(id));
                toerList.add(toer);

                //推送
                //is_push_at 是否推送@我的 1是2否
                if (toer.getIsPushAt().shortValue() == 1 && toer.getType().shortValue() == 1) {
                    Boolean flag = false;
                    HashMap<String, Object> hm = new HashMap<String, Object>();
                    hm.put("id", saying.getId());
                    hm.put("title", user.getName() + "@了你");
                    hm.put("type", 7);
                    try {
                        flag = PushTool.pushToUser(Integer.valueOf(id), content, user.getName() + "@了你", hm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //入推送表
                    Push puEntity = new Push();
                    puEntity.setToken(toer.getDevToken());
                    puEntity.setContent(content);
                    puEntity.setUserId(Integer.valueOf(id));
                    puEntity.setMessageType((short) 7);
                    puEntity.setDeviceType(toer.getDevType());
                    puEntity.setTitle(user.getName() + "@了你");
                    puEntity.setObjectId(saying.getId());
                    if (flag) {
                        puEntity.setType((short) 10);
                    } else {
                        puEntity.setType((short) 50);
                    }

                    pushSerivce.addPush(puEntity);
                }
            }

            //批量新增@信息
            atMessageService.insertAll((short) 4, user_id, user.getName(), toerList, saying.getId(), null, content);
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 转发新鲜事
     *
     * @param request
     * @param response
     * @param user_id
     * @param saying_id 被转发的新鲜事id
     * @param content   内容
     * @param at_user   @的人
     * @param privacy   帖子私密性 1仅自己可见2仅好友可见3公开
     */
    @RequestMapping("/user_forwardSaying")
    public void user_forwardSaying(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("user_id") Integer user_id, Integer saying_id, String content, String at_user, Short privacy) {
        if (saying_id == null || StringUtils.isBlank(content) || privacy == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取转发人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //如果是仅自己可见，不可@
        if (privacy.shortValue() == 1) {
            if (StringUtils.isNotBlank(at_user)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "不可@他人", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        //原始转发对象类型 1帖子2新鲜事
        Short origin_type = null;
        //原始转发对象id
        Integer origin_id = null;
        //第一次转发帖子的新鲜事id
        Integer origin_saying_id = null;
        //添加转发次数的新鲜事id
        Integer forward_saying_id = null;

        //获取原新鲜事信息
        Saying saying = sayingService.getById(saying_id);
        if (saying.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "新鲜事已被删除,不可被转发", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //判断原新鲜事是否属于转发
        //原新鲜事不属于转发的
        if (saying.getIsForward().shortValue() == 2) {
            origin_type = 2;
            origin_id = saying_id;
            forward_saying_id = saying_id;
        } else {
            //原新鲜事属于转发的
            origin_type = saying.getOriginType();
            origin_id = saying.getOriginId();

            //判断原新鲜事转发的对象类型
            //如果是帖子
            if (saying.getOriginType().shortValue() == 1) {
                origin_saying_id = saying.getOriginSayingId();
                forward_saying_id = saying.getOriginSayingId();
            } else {
                forward_saying_id = saying.getOriginId();
            }
        }

        //新增新鲜事
        Saying entity = new Saying();
        entity.setCreatorId(user_id);
        entity.setAtUser(at_user);
        entity.setContent(content);
        entity.setPrivacy(privacy);
        entity.setIsForward((short) 1);
        entity.setOriginType(origin_type);
        entity.setOriginId(origin_id);
        entity.setOriginSayingId(origin_saying_id);
        entity.setCreateTime(new Date());
        sayingService.addSaying(entity);

        //修改用户发布新鲜事的数量
        User uu = new User();
        uu.setId(user_id);
        uu.setNewsNum(user.getNewsNum().intValue() + 1);
        userService.updateUser(uu);

        //获取第一条新鲜事的信息
        Saying firstSaying = sayingService.getById(forward_saying_id);
        if (firstSaying != null && firstSaying.getIsDelete().shortValue() != 1) {
            //更新第一条新鲜事的转发次数
            Saying fs = new Saying();
            fs.setId(forward_saying_id);
            fs.setForwardNumber(firstSaying.getForwardNumber().intValue() + 1);
            sayingService.update(fs);
        }

//		//如果有@的人
//		if(StringUtils.isNotBlank(at_user)){
//			List<User> toerList = new ArrayList<>();
//			for(String id : at_user.split(",")){
//				User toer = userService.getById(Integer.valueOf(id));
//				toerList.add(toer);
//			}
//			
//			if(content.length() > 50){
//				content = content.substring(0, 50);
//			}
//			
//			//批量新增@信息
//			atMessageService.insertAll((short) 4, user_id, user.getName(), toerList, entity.getId(), null, content);
//		}

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 获取新鲜事点赞的人列表
     *
     * @param request
     * @param response
     * @param user_id
     * @param page
     * @param pageSize
     * @param time
     * @param saying_id
     */
    @RequestMapping("/user_findSayindLikeList")
    public void user_findSayindLikeList(HttpServletRequest request, HttpServletResponse response,
                                        @ModelAttribute("user_id") Integer user_id, Integer page, Integer pageSize, String time, Integer saying_id) {
        if (page == null || saying_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (page == 1) {
            time = sdf.format(new Date());
        } else {
            if (StringUtils.isBlank(time)) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map<String, Object>> list = sayingLikeService.user_findSayindLikeList(time, saying_id);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("time", time);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 点赞、取消点赞新鲜事
     *
     * @param request
     * @param response
     * @param user_id
     * @param saying_id 新鲜事id
     * @param type      1点赞 2取消点赞
     */
    @RequestMapping("/user_editSayingLike")
    public void user_editSayingLike(HttpServletRequest request, HttpServletResponse response,
                                    @ModelAttribute("user_id") Integer user_id, Integer saying_id, Short type) {
        if (saying_id == null || type == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取新鲜事信息
        Saying saying = sayingService.getById(saying_id);
        if (saying == null || saying.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "新鲜事不存在或已被删除", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }

        //获取点赞的信息
        SayingLike sayingLike = new SayingLike();
        sayingLike.setSayingId(saying_id);
        sayingLike.setUserId(user_id);
        sayingLike = sayingLikeService.get(sayingLike);

        //如果是点赞
        if (type.shortValue() == 1) {
            if (null != sayingLike) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已经点赞了,请先取消!", null)));
                return;
            }

            //新增点赞信息
            SayingLike like = new SayingLike();
            like.setUserId(user_id);
            like.setUser(user.getName());
            like.setSayingId(saying_id);
            like.setCreateTime(new Date());
            sayingLikeService.insert(like);

            //点赞完要先判断用户量>200更新redis中新鲜事的点赞数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                //直接修改新鲜事点赞数
                Saying saying2 = new Saying();
                saying2.setId(saying_id);
                saying2.setLikeNumber(saying.getLikeNumber().intValue() + 1);
                sayingService.update(saying2);
            } else {
                //直接更新redis
                //新鲜事点赞数
                if (null != RedisUtils.getValueByKey("saying_like_" + saying_id)) {
                    int like_num = Integer.parseInt(RedisUtils.getValueByKey("saying_like_" + saying_id).toString());
                    RedisUtils.addValue("saying_like_" + saying_id, String.valueOf(like_num + 1), null);
                } else {
                    RedisUtils.addValue("saying_like_" + saying_id, String.valueOf(saying.getLikeNumber().intValue() + 1), null);
                }
            }

            //获取新鲜事发布人信息
            User toer = userService.getById(saying.getCreatorId());

            //新增点赞消息
            LikeMessage likeMessage = new LikeMessage();
            likeMessage.setType((short) 3);
            likeMessage.setUserId(user_id);
            likeMessage.setUser(user.getName());
            likeMessage.setToerId(saying.getCreatorId());
            likeMessage.setToer(toer.getName());
            likeMessage.setObjectId(saying_id);
            String content = saying.getContent();
            if (content.length() > 50) {
                content = content.substring(0, 50);
            }
            likeMessage.setContent(content);
            likeMessage.setCreateTime(new Date());
            likeMessageService.addLikeMessage(likeMessage);

            //推送
            //是否推送点赞消息 1是2否
//			if(toer.getIsPushLike().shortValue() == 1){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
//				hm.put("id", saying_id);
//				hm.put("title", user.getName()+"赞了你的新鲜事");
//				try {
//					flag = PushTool.pushToUser(saying.getCreatorId(), "", user.getName()+"赞了你的新鲜事", hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//入推送表
//				Push puEntity = new Push();
//				puEntity.setToken(toer.getDevToken());
//				puEntity.setContent("");
//				puEntity.setUserId(saying.getCreatorId());
//				puEntity.setMessageType((short)7);
//				puEntity.setDeviceType(toer.getDevType());
//				puEntity.setTitle(user.getName()+"赞了你的新鲜事");
//				puEntity.setObjectId(saying_id);
//				if(flag){
//					puEntity.setType((short)10);
//				}else{
//					puEntity.setType((short)50);
//				}
//				
//				pushSerivce.addPush(puEntity);
//			}

        } else {
            //取消点赞
            if (null == sayingLike) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "暂无点赞,请先点赞!", null)));
                return;
            }

            //删除点赞信息
            int cnt = sayingLikeService.delete(sayingLike);

            if (cnt > 0) {
                //点赞完要先判断用户量>200更新redis中新鲜事的点赞数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
                if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                    //直接修改新鲜事点赞数
                    Saying saying2 = new Saying();
                    saying2.setId(saying_id);
                    saying2.setLikeNumber(saying.getLikeNumber().intValue() - 1);
                    sayingService.update(saying2);
                } else {
                    //直接更新redis
                    //新鲜事点赞数
                    if (null != RedisUtils.getValueByKey("saying_like_" + saying_id)) {
                        int like_num = Integer.parseInt(RedisUtils.getValueByKey("saying_like_" + saying_id).toString());
                        RedisUtils.addValue("saying_like_" + saying_id, String.valueOf(like_num - 1), null);
                    } else {
                        RedisUtils.addValue("saying_like_" + saying_id, String.valueOf(saying.getLikeNumber().intValue() - 1), null);
                    }
                }
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 点赞、取消点赞新鲜事评论
     *
     * @param request
     * @param response
     * @param user_id
     * @param comment_id 评论id
     * @param type       1点赞 2取消点赞
     */
    @RequestMapping("/user_editSayingCommentLike")
    public void user_editSayingCommentLike(HttpServletRequest request, HttpServletResponse response,
                                           @ModelAttribute("user_id") Integer user_id, Integer comment_id, Short type) {
        if (comment_id == null || type == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取评论信息
        SayingComment sayingComment = sayingCommentService.getById(comment_id);
        if (sayingComment.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "评论已被删除,不可操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }

        //获取点赞的信息
        SayingCommentLike commentLike = new SayingCommentLike();
        commentLike.setCommentId(comment_id);
        commentLike.setUserId(user_id);
        commentLike = sayingCommentLikeService.get(commentLike);

        //如果是点赞
        if (type.shortValue() == 1) {
            if (null != commentLike) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已经点赞了,请先取消!", null)));
                return;
            }

            //新增点赞信息
            SayingCommentLike like = new SayingCommentLike();
            like.setUserId(user_id);
            like.setUser(user.getName());
            like.setCommentId(comment_id);
            like.setCreateTime(new Date());
            sayingCommentLikeService.insert(like);

            //点赞完要先判断用户量>200更新redis中新鲜事评论点赞数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                //直接修改新鲜事评论点赞数
                SayingComment comment = new SayingComment();
                comment.setId(comment_id);
                comment.setLikeCount(sayingComment.getLikeCount().intValue() + 1);
                sayingCommentService.update(comment);
            } else {
                //直接更新redis
                //新鲜事评论点赞数
                if (null != RedisUtils.getValueByKey("saying_comment_like_" + comment_id)) {
                    int like_num = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_like_" + comment_id).toString());
                    RedisUtils.addValue("saying_comment_like_" + comment_id, String.valueOf(like_num + 1), null);
                } else {
                    RedisUtils.addValue("saying_comment_like_" + comment_id, String.valueOf(sayingComment.getLikeCount().intValue() + 1), null);
                }
            }

            //获取评论人信息
            User toer = userService.getById(sayingComment.getUserId());

            //新增点赞消息
            LikeMessage likeMessage = new LikeMessage();
            likeMessage.setType((short) 4);
            likeMessage.setUserId(user_id);
            likeMessage.setUser(user.getName());
            likeMessage.setToerId(sayingComment.getUserId());
            likeMessage.setToer(toer.getName());
            likeMessage.setObjectId(sayingComment.getSayingId());
            likeMessage.setContent(sayingComment.getContent());
            likeMessage.setCreateTime(new Date());
            likeMessageService.addLikeMessage(likeMessage);

            //推送
            //是否推送点赞消息 1是2否
//			if(toer.getIsPushLike().shortValue() == 1){
//				Boolean flag = false;
//				HashMap<String,Object> hm = new HashMap<String,Object>();
//				hm.put("id", sayingComment.getSayingId());
//				hm.put("title", user.getName()+"赞了你");
//				try {
//					flag = PushTool.pushToUser(sayingComment.getUserId(), "", user.getName()+"赞了你", hm);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//入推送表
//				Push puEntity = new Push();
//				puEntity.setToken(toer.getDevToken());
//				puEntity.setContent("");
//				puEntity.setUserId(sayingComment.getUserId());
//				puEntity.setMessageType((short)7);
//				puEntity.setDeviceType(toer.getDevType());
//				puEntity.setTitle(user.getName()+"赞了你");
//				puEntity.setObjectId(sayingComment.getSayingId());
//				if(flag){
//					puEntity.setType((short)10);
//				}else{
//					puEntity.setType((short)50);
//				}
//				
//				pushSerivce.addPush(puEntity);
//			}
        } else {
            //取消点赞
            if (null == commentLike) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "暂无点赞,请先点赞!", null)));
                return;
            }

            //删除点赞信息
            int cnt = sayingCommentLikeService.delete(commentLike);
            if (cnt > 0) {
                //点赞完要先判断用户量>200更新redis中新鲜事评论的点赞数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
                if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                    //直接修改新鲜事评论点赞数
                    SayingComment comment = new SayingComment();
                    comment.setId(comment_id);
                    comment.setLikeCount(sayingComment.getLikeCount().intValue() - 1);
                    sayingCommentService.update(comment);
                } else {
                    //直接更新redis
                    //新鲜事评论点赞数
                    if (null != RedisUtils.getValueByKey("saying_comment_like_" + comment_id)) {
                        int like_num = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_like_" + comment_id).toString());
                        RedisUtils.addValue("saying_comment_like_" + comment_id, String.valueOf(like_num - 1), null);
                    } else {
                        RedisUtils.addValue("saying_comment_like_" + comment_id, String.valueOf(sayingComment.getLikeCount().intValue() - 1), null);
                    }
                }
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 评论新鲜事
     *
     * @param request
     * @param response
     * @param user_id
     * @param saying_id 新鲜事id
     * @param content   评论内容
     */
    @RequestMapping("/user_commentSaying")
    public void user_commentSaying(HttpServletRequest request, HttpServletResponse response,
                                   @ModelAttribute("user_id") Integer user_id, Integer saying_id, String content) {
        if (saying_id == null || StringUtils.isBlank(content)) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取评论人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //获取新鲜事信息
        Saying saying = sayingService.getById(saying_id);
        if (saying == null || saying.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "新鲜事不存在或已被删除", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //新增评论
        SayingComment comment = new SayingComment();
        comment.setSayingId(saying_id);
        comment.setUserId(user_id);
        comment.setContent(content);
        comment.setCreateTime(new Date());
        int i = sayingCommentService.insert(comment);
        //如果新增成功
        if (i > 0) {
            //用户量
            Integer user_num = null;
            if (null != RedisUtils.getValueByKey("user_num")) {
                user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
            } else {
                User uentity = new User();
                uentity.setIsDelete((short) 2);
                List<User> list = userService.findList(uentity);
                user_num = list.size();
            }

            //评论完要先判断用户量>200更新redis中新鲜事的评论数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                //直接修改新鲜事的评论数
                Saying saying2 = new Saying();
                saying2.setId(saying_id);
                saying2.setCommentNumber(saying.getCommentNumber().intValue() + 1);
                sayingService.update(saying2);
            } else {
                //直接更新redis
                //新鲜事的评论数
                if (null != RedisUtils.getValueByKey("saying_comment_" + saying_id)) {
                    int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + saying_id).toString());
                    RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(comment_number + 1), null);
                } else {
                    RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(saying.getCommentNumber().intValue() + 1), null);
                }
            }

            //获取新鲜事发布人信息
            User toer = userService.getById(saying.getCreatorId());

            //评论回复消息表
            CommentReplyMessage message = new CommentReplyMessage();
            message.setType((short) 3);
            message.setUserId(user_id);
            message.setUser(user.getName());
            message.setToerId(saying.getCreatorId());
            message.setToer(toer.getName());
            message.setObjectId(saying_id);
            message.setContent(content);
            message.setCreateTime(new Date());
            message.setOriginContent(saying.getContent());
            commentReplyMessageService.addCommentReplyMessage(message);

            //推送
            //is_push_cmt 是否推送评论消息 1是2否
            if (toer.getIsPushCmt().shortValue() == 1 && toer.getType().shortValue() == 1) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", saying_id);
                hm.put("title", user.getName() + "评论了你的新鲜事");
                hm.put("type", 7);
                try {
                    flag = PushTool.pushToUser(saying.getCreatorId(), content, user.getName() + "评论了你的新鲜事", hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //入推送表
                Push puEntity = new Push();
                puEntity.setToken(toer.getDevToken());
                puEntity.setContent(content);
                puEntity.setUserId(saying.getCreatorId());
                puEntity.setMessageType((short) 7);
                puEntity.setDeviceType(toer.getDevType());
                puEntity.setTitle(user.getName() + "评论了你的新鲜事");
                puEntity.setObjectId(saying_id);
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }

                pushSerivce.addPush(puEntity);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", comment.getId());
        map.put("time", comment.getCreateTime());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 删除新鲜事评论
     *
     * @param request
     * @param response
     * @param user_id
     * @param comment_id
     */
    @RequestMapping("/user_deleteSayingComment")
    public void user_deleteSayingComment(HttpServletRequest request, HttpServletResponse response,
                                         @ModelAttribute("user_id") Integer user_id, Integer comment_id) {
        if (comment_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //获取评论信息
        SayingComment sayingComment = sayingCommentService.getById(comment_id);
        if (sayingComment == null || sayingComment.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "评论不存在或已被删除", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (sayingComment.getUserId().intValue() != user_id.intValue()) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "无权操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //新鲜事id
        Integer saying_id = sayingComment.getSayingId();
        //获取新鲜事详情
        Saying saying = sayingService.getById(saying_id);

        //删除新鲜事评论
        SayingComment comment = new SayingComment();
        comment.setId(comment_id);
        comment.setIsDelete((short) 1);
        comment.setCreateTime(new Date());
        sayingCommentService.update(comment);

        //用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }

        //评论完要先判断用户量>200更新redis中新鲜事的评论数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
        if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
            //直接修改新鲜事的评论数
            Saying saying2 = new Saying();
            saying2.setId(saying_id);
            saying2.setCommentNumber(saying.getCommentNumber().intValue() - 1);
            sayingService.update(saying2);
        } else {
            //直接更新redis
            //新鲜事的评论数
            if (null != RedisUtils.getValueByKey("saying_comment_" + saying_id)) {
                int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + saying_id).toString());
                RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(comment_number - 1), null);
            } else {
                RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(saying.getCommentNumber().intValue() - 1), null);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 回复新鲜事评论
     *
     * @param request
     * @param response
     * @param user_id
     * @param saying_id  新鲜事id
     * @param comment_id 评论id
     * @param content    回复内容
     * @param to_id      被回复的回复id  回复回复列表必传，回复主评论不传
     * @param to_user_id 被回复人员id  回复评论，传评论人id;回复回复，传回复人id
     */
    @RequestMapping("/user_replySaying")
    public void user_replySaying(HttpServletRequest request, HttpServletResponse response,
                                 @ModelAttribute("user_id") Integer user_id, Integer saying_id, Integer comment_id, String content, Integer to_id, Integer to_user_id) {
        if (saying_id == null || comment_id == null || StringUtils.isBlank(content) || to_user_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //被评论回复的内容
        String originContent = null;
        //如果被回复的回复id为空，说明回复的是主评论
        if (to_id == null) {
            //获取评论信息
            SayingComment sayingComment = sayingCommentService.getById(comment_id);
            if (sayingComment == null || sayingComment.getIsDelete().shortValue() == 1) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "评论不存在或已被删除", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            originContent = sayingComment.getContent();
        } else {
            //说明回复的是回复
            //查询上级回复的信息
            SayingReply sayingReply = sayingReplyService.getById(to_id);
            if (sayingReply == null || sayingReply.getIsDelete().shortValue() == 1) {
                String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "回复不存在或已被删除", null));
                ServletUtilsEx.renderText(response, strToMoblieJson);
                return;
            }

            originContent = sayingReply.getContent();
        }

        //获取新鲜事详情
        Saying saying = sayingService.getById(saying_id);

        //新增评论
        SayingReply reply = new SayingReply();
        reply.setSayingId(saying_id);
        reply.setCommentId(comment_id);
        reply.setReplierId(user_id);
        reply.setContent(content);
        reply.setToId(to_id);
        reply.setToUserId(to_user_id);
        reply.setCreateTime(new Date());
        int i = sayingReplyService.insert(reply);
        //如果新增成功
        if (i > 0) {
            //用户量
            Integer user_num = null;
            if (null != RedisUtils.getValueByKey("user_num")) {
                user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
            } else {
                User uentity = new User();
                uentity.setIsDelete((short) 2);
                List<User> list = userService.findList(uentity);
                user_num = list.size();
            }

            //回复完要先判断用户量>200更新redis中新鲜事的评论数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
            if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
                //直接修改新鲜事的评论数
                Saying saying2 = new Saying();
                saying2.setId(saying_id);
                saying2.setCommentNumber(saying.getCommentNumber().intValue() + 1);
                sayingService.update(saying2);
            } else {
                //直接更新redis
                //新鲜事的评论数
                if (null != RedisUtils.getValueByKey("saying_comment_" + saying_id)) {
                    int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + saying_id).toString());
                    RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(comment_number + 1), null);
                } else {
                    RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(saying.getCommentNumber().intValue() + 1), null);
                }
            }

            //获取回复对象信息
            User toer = userService.getById(to_user_id);

            //评论回复消息表
            CommentReplyMessage message = new CommentReplyMessage();
            message.setType((short) 4);
            message.setUserId(user_id);
            message.setUser(user.getName());
            message.setToerId(to_user_id);
            message.setToer(toer.getName());
            message.setObjectId(saying_id);
            message.setContent(content);
            message.setCreateTime(new Date());
            message.setOriginContent(originContent);
            commentReplyMessageService.addCommentReplyMessage(message);

            //推送
            //is_push_cmt 是否推送评论消息 1是2否
            if (toer.getIsPushCmt().shortValue() == 1 && toer.getType().shortValue() == 1) {
                Boolean flag = false;
                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("id", saying_id);
                hm.put("title", user.getName() + "评论了你");
                hm.put("type", 7);
                try {
                    flag = PushTool.pushToUser(to_user_id, content, user.getName() + "评论了你", hm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //入推送表
                Push puEntity = new Push();
                puEntity.setToken(toer.getDevToken());
                puEntity.setContent(content);
                puEntity.setUserId(to_user_id);
                puEntity.setMessageType((short) 7);
                puEntity.setDeviceType(toer.getDevType());
                puEntity.setTitle(user.getName() + "评论了你");
                puEntity.setObjectId(saying_id);
                if (flag) {
                    puEntity.setType((short) 10);
                } else {
                    puEntity.setType((short) 50);
                }

                pushSerivce.addPush(puEntity);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", reply.getId());
        map.put("time", reply.getCreateTime());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 删除新鲜事评论回复
     *
     * @param request
     * @param response
     * @param user_id
     * @param reply_id
     */
    @RequestMapping("/user_deleteSayingReply")
    public void user_deleteSayingReply(HttpServletRequest request, HttpServletResponse response,
                                       @ModelAttribute("user_id") Integer user_id, Integer reply_id) {
        if (reply_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //获取回复信息
        SayingReply sayingReply = sayingReplyService.getById(reply_id);
        if (sayingReply == null || sayingReply.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "回复不存在或已被删除", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (sayingReply.getReplierId().intValue() != user_id.intValue()) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "无权操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //新鲜事id
        Integer saying_id = sayingReply.getSayingId();
        //获取新鲜事详情
        Saying saying = sayingService.getById(saying_id);

        //删除新鲜事回复
        SayingReply reply = new SayingReply();
        reply.setId(reply_id);
        reply.setIsDelete((short) 1);
        reply.setCreateTime(new Date());
        sayingReplyService.update(reply);

        //用户量
        Integer user_num = null;
        if (null != RedisUtils.getValueByKey("user_num")) {
            user_num = Integer.valueOf(RedisUtils.getValueByKey("user_num").toString());
        } else {
            User uentity = new User();
            uentity.setIsDelete((short) 2);
            List<User> list = userService.findList(uentity);
            user_num = list.size();
        }

        //评论完要先判断用户量>200更新redis中新鲜事的评论数,定时任务隔5分钟把redis的值更新到数据库;<200正常更新数据库
        if (user_num.intValue() < Integer.valueOf(CoreConstants.getProperty("people_num"))) {
            //直接修改新鲜事的评论数
            Saying saying2 = new Saying();
            saying2.setId(saying_id);
            saying2.setCommentNumber(saying.getCommentNumber().intValue() - 1);
            sayingService.update(saying2);
        } else {
            //直接更新redis
            //新鲜事的评论数
            if (null != RedisUtils.getValueByKey("saying_comment_" + saying_id)) {
                int comment_number = Integer.parseInt(RedisUtils.getValueByKey("saying_comment_" + saying_id).toString());
                RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(comment_number - 1), null);
            } else {
                RedisUtils.addValue("saying_comment_" + saying_id, String.valueOf(saying.getCommentNumber().intValue() - 1), null);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 删除新鲜事
     *
     * @param request
     * @param response
     * @param user_id
     * @param saying_id
     */
    @RequestMapping("/user_deleteSaying")
    public void user_deleteSaying(HttpServletRequest request, HttpServletResponse response,
                                  @ModelAttribute("user_id") Integer user_id, Integer saying_id) {
        if (saying_id == null) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //获取操作人信息
        User user = userService.getById(user_id);
        if (user == null || user.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号不存在", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

//		if(user.getStatus().shortValue() == 2){
//			String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "账号被禁用",null));
//			ServletUtilsEx.renderText(response, strToMoblieJson);
//			return;
//		}

        //获取新鲜事信息
        Saying saying = sayingService.getById(saying_id);
        if (saying == null || saying.getIsDelete().shortValue() == 1) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "新鲜事不存在或已被删除", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        if (saying.getCreatorId().intValue() != user_id.intValue()) {
            String strToMoblieJson = JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "无权操作", null));
            ServletUtilsEx.renderText(response, strToMoblieJson);
            return;
        }

        //删除新鲜事
        Saying entity = new Saying();
        entity.setId(saying_id);
        entity.setIsDelete((short) 1);
        entity.setDeleteTime(new Date());
        sayingService.update(entity);

        //修改用户发布新鲜事的数量
        User uu = new User();
        uu.setId(user_id);
        uu.setNewsNum(user.getNewsNum().intValue() - 1);
        userService.updateUser(uu);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

}
