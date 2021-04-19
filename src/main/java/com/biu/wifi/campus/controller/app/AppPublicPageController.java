package com.biu.wifi.campus.controller.app;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.Tool.PushTool;
import com.biu.wifi.campus.Tool.RedisUtils;
import com.biu.wifi.campus.Tool.StringUtilEx;
import com.biu.wifi.campus.dao.model.AccountOnline;
import com.biu.wifi.campus.dao.model.LeaveMessage;
import com.biu.wifi.campus.dao.model.PageApply;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserPageRelationship;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.AccountOnlineService;
import com.biu.wifi.campus.service.AppPublicPageService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;


@Controller
public class AppPublicPageController extends AuthenticatorController {

    @Autowired
    private AppPublicPageService appPublicPageService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountOnlineService accountOnlineService;

    /**
     * 退出登录
     *
     * @param response
     * @param user_id
     */
    @RequestMapping("/user_public_handover")
    public void user_app_logout(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id,
                                @RequestParam(required = false) Integer type,
                                @RequestParam(required = false) Integer originUserId,
                                @RequestParam(required = false) String token) {
        if (type == null) {
            UserPageRelationship userPageRelationship = appPublicPageService.getUserPageRelationship(user_id);
            if (null != userPageRelationship) {
                //删除
                AccountOnline accountOnline = new AccountOnline();
                accountOnline.setAccountId(user_id);
                //获取信息
                List<AccountOnline> list = accountOnlineService.findList(accountOnline);
                if (list != null && list.size() > 0) {
                    for (AccountOnline online : list) {
                        //删除redis
                        RedisUtils.delKey(online.getOnlineKey());
                    }
                }
                accountOnlineService.delete(accountOnline);
                //得到主页id
                Integer pageId = userPageRelationship.getPageId();
                //生成token
                Date now = new Date();
                String loginkey = StringUtilEx.produceToken(now);
                long saveTime = StringUtilEx.save_time;
                //用户在线信息表
                AccountOnline accOnline = new AccountOnline();
                accOnline.setAccountId(pageId);
                accOnline.setOnlineKey(loginkey);
                accOnline.setEdatetime(now.getTime() + saveTime * 1000);
                accOnline.setSdatetime(new BigDecimal(now.getTime()).longValue());
                accOnline.setType((short) 1);
                accOnline.setLengthen((int) saveTime);
                accountOnlineService.insert(accOnline);

                //放入redis
                RedisUtils.addValue(loginkey, String.valueOf(pageId), 30 * 24 * 60 * 60);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("token", loginkey);
                map.put("pageId", pageId);
                map.put("originUserId", user_id);
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
                return;
            } else {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(8, "您还没有公共主页", null)));
                return;
            }
        } else if (type == 1) {
            //公共主页变个人，切换token
            if (originUserId == null) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "缺少必要参数！", null)));
                return;
            }
            User user = appPublicPageService.getUserInfoo(originUserId);//这是原来用户的id
            if (user != null) {
                Integer uId = user.getId();
                AccountOnline accountOnline = new AccountOnline();
                accountOnline.setOnlineKey(token);
                //获取信息
                List<AccountOnline> list = accountOnlineService.findList(accountOnline);
                if (list != null && list.size() > 0) {
                    for (AccountOnline online : list) {
                        //删除redis
                        RedisUtils.delKey(online.getOnlineKey());
                    }
                }
                accountOnlineService.delete(accountOnline);

                //生成token
                Date now = new Date();
                String loginkey = StringUtilEx.produceToken(now);
                long saveTime = StringUtilEx.save_time;
                //用户在线信息表
                AccountOnline accOnline = new AccountOnline();
                accOnline.setAccountId(uId);
                accOnline.setOnlineKey(loginkey);
                accOnline.setEdatetime(now.getTime() + saveTime * 1000);
                accOnline.setSdatetime(new BigDecimal(now.getTime()).longValue());
                accOnline.setType((short) 1);
                accOnline.setLengthen((int) saveTime);
                accountOnlineService.insert(accOnline);

                //放入redis
                RedisUtils.addValue(loginkey, String.valueOf(uId), 30 * 24 * 60 * 60);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("token", loginkey);
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
                return;

            } else {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "用户失效！！！", null)));
                return;
            }
        }
    }

    /**
     * 创建公共主页
     *
     * @param user_id
     * @param response
     * @param inroduction
     * @param name
     * @param signature
     * @throws Exception
     */
    @RequestMapping("/user_createPublicPage")
    public void getMyOwnOrPublicPage(@ModelAttribute("user_id") Integer user_id, HttpServletResponse response) throws Exception {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        Object inroduction = param.get("inroduction");
        Object name = param.get("name");

        String sig = null;
        if (param.get("sig") != null) {
            sig = param.get("sig").toString();
            Integer s = sig.length();
            if (s > 200) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "审核说明字数长度不符合要求!", null)));
                return;
            }
        }

        Integer i = inroduction.toString().length();
        Integer n = name.toString().length();
        if (n < 2 || n > 10) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "名字字数长度不符合要求!", null)));
            return;
        }
        if (i < 10 || i > 200) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "简介字数长度不符合要求!", null)));
            return;
        }

        //一个人只能创建一个公共主页
        User userr = new User();
        userr.setCreatorId(user_id);
        userr.setIsDelete((short) 2);
        List<User> uu = appPublicPageService.getUserList(userr);
        if (uu.size() > 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "你已经创建过了！", null)));
            return;
        }
		
		/*PageApply pageApp= new PageApply();
		pageApp.setApplicantId(user_id);
		pageApp.setStatus((short)1);
		List<PageApply>  PageA = appPublicPageService.getPageApply(pageApp);
		if(PageA != null && PageA.size() > 0) {
			PageApply page = PageA.get(0);
			page.setCreateTime(new Date());
			appPublicPageService.updatePageApply();
		}*/

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
        User user = new User();
        user.setId(user_id);
        User u = userService.getUser(user);
        PageApply pageApply = new PageApply();
        //学校id
        pageApply.setSchoolId(u.getSchoolId());
        //申请人id
        pageApply.setApplicantId(u.getId());
        //主页名称
        pageApply.setName(name.toString());
        //主页简介
        pageApply.setIntroduction(inroduction.toString());
        //申请说明
        if (sig != null) {
            pageApply.setDes(sig);
        }
        //头像
        pageApply.setHeadimg(fileid);
        //申请状态1申请中2通过3驳回
        pageApply.setStatus((short) 1);
        //申请时间
        pageApply.setCreateTime(new Date());
        appPublicPageService.addPageApply(pageApply);
        Integer id = pageApply.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("pageApplyId", id);
        map.put("status", pageApply.getStatus());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
        return;
    }

    /**
     * 得到公共主页的状态
     *
     * @param user_id
     * @param response
     */
    @RequestMapping("/user_auditPublicPage")
    public void getAuditPublicPage(@ModelAttribute("user_id") Integer user_id,//个人token
                                   HttpServletResponse response) {
        UserPageRelationship userPageRelationship = appPublicPageService.getUserPageRelationship(user_id);
        if (userPageRelationship == null) {

            Map<String, Object> map = appPublicPageService.getAudit(user_id);
            if (map != null && map.size() > 0) {
                if (map.get("status") != null) {
                    int status = Integer.parseInt(map.get("status").toString());
                    if (status == 3) {
                        Map<String, Object> back = appPublicPageService.getAuditBack(user_id);
                        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", back)));
                        return;
                    }
                }
            }

            if (map == null) {
                Map<String, Object> noResult = new HashMap<>();
                noResult.put("status", 4);
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", noResult)));
                return;
            }
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
            return;
        } else {
            Map<String, Object> mapPage = new HashMap<>();
            mapPage.put("status", 2);
            Integer pageId = userPageRelationship.getPageId();
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", mapPage)));
            return;
        }
    }

    /**
     * 编辑公共主页
     *
     * @param user_id
     * @param response
     * @param inroduction
     */
    @RequestMapping("user_editPublicPage")
    public void editPublicPage(@ModelAttribute("user_id") Integer user_id,
                               HttpServletResponse response) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        String inroduction = null;
        if (param.get("inroduction") != null) {
            inroduction = param.get("inroduction").toString();
            Integer i = inroduction.toString().length();
            if (i < 10 || i > 200) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "简介字数长度不符合要求!", null)));
                return;
            }
        }

        //去头像的参数
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

        //User user = appPublicPageService.getUserPublic(user_id);
        User u = new User();
        u.setId(user_id);
        u.setHeadimg(fileid);
        if (inroduction != null) {
            u.setIntroduction(inroduction);
        }
        int i = appPublicPageService.updateUserPage(u);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "编辑成功!", null)));
        return;
    }

    /**
     * 留言板列表
     *
     * @param user_id
     * @param response
     * @param page
     * @param pagesize
     */
    @RequestMapping("/user_messageBoardList")
    public void getMessageBoardList(@ModelAttribute("user_id") Integer user_id,
                                    HttpServletResponse response,
                                    Integer page,
                                    @RequestParam(required = false) Integer pagesize,
                                    @RequestParam(required = false) Integer Id) {   //pageId
        PageLimitHolderFilter.setContext(page, pagesize == null ? 10 : pagesize, null);
        List<Map<String, Object>> map = null;
        if (Id == null) {
            map = appPublicPageService.getMessageBoardList(user_id);
        } else if (Id != null) {
            map = appPublicPageService.getMessageBoardList(Id);
        }
        if (map != null && map.size() > 0) {
            for (Map<String, Object> mm : map) {
                if (mm.get("toer_id") != null) {
                    String to_reply_name = appPublicPageService.getToReplyName(Integer.parseInt(mm.get("toer_id").toString()));
                    mm.put("to_reply_name", to_reply_name);
                } else {
                    mm.put("to_reply_name", null);
                }

            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功!", map)));
        return;
    }

    /**
     * 关注公众主页的同校粉丝列表
     *
     * @param user_id
     * @param response
     * @param page
     * @param pagesize
     */
    @RequestMapping("/user_publicSameFansList")
    public void getPublicSameFansList(@ModelAttribute("user_id") Integer user_id,
                                      HttpServletResponse response,
                                      @RequestParam(required = false) String name) {
        //同校的，关注我的，没有公共主页的
        UserPageRelationship userPageRela = new UserPageRelationship();
        userPageRela.setIsDelete((short) 2);
        User u = new User();
        u.setId(user_id);
        User user = appPublicPageService.getUser(u);
        //已经是主页管理员的id
        List<Integer> adminId = appPublicPageService.getAdminId(user_id);

        List<Map<String, Object>> map = new ArrayList<>();

        List<Map<String, Object>> mmap = appPublicPageService.getPublicSameFansList(user_id, user.getSchoolId(), name);
        if (mmap != null && mmap.size() > 0) {
            for (Map<String, Object> filter : mmap) {
                if (filter.get("userId") != null) {
                    Integer userId = Integer.parseInt(filter.get("userId").toString());
                    if (adminId != null && adminId.size() > 0) {
                        for (Integer admId : adminId) {
                            if (userId == admId) {
                                filter.clear();
                            }
                        }
                    }
                    //把有公共主页的也过滤调
                    userPageRela.setUserId(userId);
                    List<UserPageRelationship> haveList = appPublicPageService.getNumOfManage(userPageRela);
                    if (haveList != null && haveList.size() > 0) {
                        filter.clear();
                    }

                }
                if (!filter.isEmpty()) {
                    map.add(filter);
                }
            }
        }
        UserPageRelationship userPageRelationship = new UserPageRelationship();
        userPageRelationship.setPageId(user_id);
        userPageRelationship.setType((short) 2);
        userPageRelationship.setIsDelete((short) 2);
        List<UserPageRelationship> list = appPublicPageService.getNumOfManage(userPageRelationship);
        Map<String, Object> num = new HashMap<>();
        if (list == null) {
            num.put("limitNum", 4);
        } else {
            Integer listnum = 4 - (list.size()) < 0 ? 0 : 4 - (list.size());
            num.put("limitNum", listnum);
        }

        num.put("list", map);

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功!", num)));
        return;
    }

    /**
     * 添加管理员
     *
     * @param user_id  token
     * @param userId   粉丝的用户id
     * @param response
     */
    @RequestMapping("/user_addPublicManage")
    public void getAddPublicManage(@ModelAttribute("user_id") Integer user_id,
                                   String userId,
                                   HttpServletResponse response) {
        UserPageRelationship userPageRela = new UserPageRelationship();
        userPageRela.setPageId(user_id);
        userPageRela.setType((short) 1);
        userPageRela.setIsDelete((short) 2);
        List<UserPageRelationship> userPageRelaList = appPublicPageService.getNumOfManage(userPageRela);

        if (userPageRelaList != null && userPageRelaList.size() == 1) {

            UserPageRelationship userPageRelationship = new UserPageRelationship();
            userPageRelationship.setPageId(user_id);
            userPageRelationship.setType((short) 2);
            userPageRelationship.setIsDelete((short) 2);
            List<UserPageRelationship> list = appPublicPageService.getNumOfManage(userPageRelationship);
            if (4 - list.size() > 0) {
                UserPageRelationship userPage = new UserPageRelationship();
                userPage.setPageId(user_id);
                userPage.setType((short) 2);
                userPage.setCreateTime(new Date());
                userPage.setIsDelete((short) 2);
                String[] str = userId.split(",");
                for (int i = 0; i < str.length; i++) {
                    int addId = Integer.parseInt(str[i]);
                    userPage.setUserId(addId);
                    appPublicPageService.addUserPageRelationship(userPage);
                }
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "新增成功!", null)));
                return;
            } else {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "管理员已满!", null)));
                return;
            }
        } else {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, " 无权限，新增失败！！！", null)));
            return;
        }
    }

    /**
     * 获取公共管理员
     *
     * @param user_id
     * @param response
     */
    @RequestMapping("/user_getPublicManage")
    public void getPublicManage(@ModelAttribute("user_id") Integer user_id,  //主页id
                                HttpServletResponse response,
                                Integer originUserId) {

        //
        UserPageRelationship userPageRela = new UserPageRelationship();
        userPageRela.setPageId(user_id);
        userPageRela.setUserId(originUserId);
        userPageRela.setIsDelete((short) 2);
        List<UserPageRelationship> userPageRelaList = appPublicPageService.getNumOfManage(userPageRela);
        List<Map<String, Object>> map = appPublicPageService.getPublicManage(user_id);
        if (userPageRelaList != null && userPageRelaList.size() > 0) {
            Short type = userPageRelaList.get(0).getType();
            Map<String, Object> last = new HashMap<>();
            last.put("result", map);
            last.put("role", type);
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功!", last)));
            return;
        } else {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "失败！！！", null)));
            return;
        }
    }

    /**
     * 删除管理员
     *
     * @param user_id
     * @param id
     * @param response
     */
    @RequestMapping("/user_cancelPubManage")
    public void cancelPubManage(@ModelAttribute("user_id") Integer user_id,
                                Integer id,
                                HttpServletResponse response) {
        UserPageRelationship userPage = new UserPageRelationship();
        userPage.setId(id);
        userPage.setIsDelete((short) 1);
        userPage.setDeleteTime(new Date());
        appPublicPageService.updateUserPageRelationship(userPage);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "删除成功!", null)));
        return;
    }

    /**
     * 发布留言
     *
     * @param user_id  身份认证token
     * @param pageId   公共主页Id
     * @param content  留言内容
     * @param response 返回
     * @throws Exception
     */
    @RequestMapping("/user_addLeaveMessage")
    public void AddLeaveMessage(@ModelAttribute("user_id") Integer user_id,
                                Integer pageId,
                                String content,
                                HttpServletResponse response) throws Exception {
        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setContent(content);
        leaveMessage.setCreateTime(new Date());
        if (pageId == null) {
            leaveMessage.setPageId(user_id);
        } else {
            leaveMessage.setPageId(pageId);
        }
        leaveMessage.setUserId(user_id);
        leaveMessage.setIsDelete((short) 2);
        appPublicPageService.AddLeaveMessage(leaveMessage);
        Map<String, Object> map = new HashMap<>();
        map.put("id", leaveMessage.getId());
        map.put("time", leaveMessage.getCreateTime());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "发布留言成功!", map)));
        return;
    }

    /**
     * 回复留言
     *
     * @param user_id
     * @param id       留言id
     * @param content
     * @param response
     * @throws Exception
     */
    @RequestMapping("/user_replyMessage")
    public void replyMessage(@ModelAttribute("user_id") Integer user_id,//留言的人  主页和管理员都可以
                             HttpServletResponse response,
                             Integer id,
                             String content) throws Exception {
        if (content != null) {
            if (content.length() > 200) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "回复内容过长", null)));
                return;
            }
        }

        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setId(id);
        LeaveMessage lm = appPublicPageService.getLeaveMessage(leaveMessage);


        LeaveMessage replyLeaveMessage = new LeaveMessage();
        replyLeaveMessage.setCreateTime(new Date());
        replyLeaveMessage.setPageId(lm.getPageId());
        replyLeaveMessage.setContent(lm.getContent());
        replyLeaveMessage.setUserId(user_id);
        replyLeaveMessage.setReplyContent(content);
        replyLeaveMessage.setToerId(lm.getUserId());
        replyLeaveMessage.setToId(id);
        replyLeaveMessage.setIsDelete((short) 2);
        appPublicPageService.AddLeaveMessage(replyLeaveMessage);

        User u = appPublicPageService.getUserInfoo(user_id);
        HashMap<String, Object> map = new HashMap<>();
        //公共主页留言板
        map.put("id", lm.getPageId());
        map.put("type", 8);

        User uu = appPublicPageService.getUserInfoo(lm.getUserId());
        if (uu != null) {
            if (uu.getIsPushCmt() == 1 && uu.getType().shortValue() == 1) {
                PushTool.pushToUser(lm.getUserId(), content, u.getName() + "回复了你的留言", map);
            }
        }

//		Integer userOrPageId = lm.getUserId();
//		UserPageRelationship userPageRelationship = new UserPageRelationship();
//		userPageRelationship.setIsDelete((short)2);
//		userPageRelationship.setPageId(userOrPageId);
//		List<UserPageRelationship> UserPageList = appPublicPageService.getNumOfManage(userPageRelationship);
//		if(UserPageList != null && UserPageList.size() > 0) {
//			for(UserPageRelationship UserPage : UserPageList) {
//				Integer tuiUser =  UserPage.getUserId();
//				User userCMT = appPublicPageService.getUserInfoo(tuiUser);
//				if(userCMT != null) {
//					if(userCMT.getIsPushCmt() == 1) {
//						PushTool.pushToUser(tuiUser, content, u.getName()+"回复了你的留言",map);	
//					}
//				}
//			}
//		}else {
//			User uu = appPublicPageService.getUserInfoo(lm.getUserId());
//			if(uu != null) {
//				if(uu.getIsPushCmt() == 1) {
//					PushTool.pushToUser(lm.getUserId(), content, u.getName()+"回复了你的留言",map);	
//				}
//			}
//		}
        Map<String, Object> result = new HashMap<>();
        result.put("id", replyLeaveMessage.getId());
        result.put("time", replyLeaveMessage.getCreateTime());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "回复留言成功!", result)));
        return;
    }

    /**
     * 删除留言或者回复
     *
     * @param user_id  身份认证 token
     * @param id       留言id
     * @param response
     */
    @RequestMapping("/user_deleteMessage")
    public void deleteMessage(@ModelAttribute("user_id") Integer user_id,
                              Integer id,
                              HttpServletResponse response) {
        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setId(id);
        leaveMessage.setUserId(user_id);
        leaveMessage.setIsDelete((short) 1);
        leaveMessage.setDeleteTime(new Date());
        appPublicPageService.updateLeaveMessage(leaveMessage);
        //把回复留言的也删掉,如果有的话
        List<LeaveMessage> lm = appPublicPageService.getReleaveMessage(id, user_id);
        if (lm.size() > 0) {
            for (LeaveMessage message : lm) {
                message.setIsDelete((short) 1);
                message.setDeleteTime(new Date());
                appPublicPageService.updateLeaveMessage(message);
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "删除留言成功!", null)));
        return;
    }

}
