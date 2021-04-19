package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.dao.model.Collection;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class AppBaseController extends AuthenticatorController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private SayingService sayingService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PostService postService;
    @Autowired
    private AuditUserRoleService auditUserRoleService;
    @Autowired
    private AuditRolePermissionService auditRolePermissionService;
    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private UserService userService;

    /**
     * 学校列表
     *
     * @param response
     * @param name
     */
    @RequestMapping("/app_schoolList")
    public void app_schoolList(HttpServletResponse response, String name) {
        School sEntity = new School();
        sEntity.setName(name);
        sEntity.setIsDelete((short) 2);
        List<School> schoolList = schoolService.findList(sEntity);
        if (schoolList.size() == 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NODATA, "没有相关数据!", null)));
            return;
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", schoolList)));
    }

    /**
     * 举报
     *
     * @param response
     * @param user_id
     * @param content
     * @param other_id
     * @param type
     */
    @RequestMapping("/user_report")
    public void user_report(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, String content,
                            Integer other_id, Short type, Integer school_id) {
        if (StringUtils.isBlank(content) || other_id == null || type == null || school_id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        Report rEntity = new Report();
        rEntity.setContent(content);
        rEntity.setCreateTime(new Date());
        rEntity.setIsDelete((short) 2);
        rEntity.setOtherId(other_id);
        rEntity.setType(type);
        rEntity.setUserId(user_id);
        rEntity.setHasHandle((short) 2);
        rEntity.setSchoolId(school_id);
        reportService.addReport(rEntity);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 收藏
     *
     * @param response
     * @param user_id
     * @param id
     * @param type
     */
    @RequestMapping("/user_collect")
    public void user_collect(HttpServletResponse response, @ModelAttribute("user_id") Integer user_id, Integer id,
                             Short type) {
        if (id == null || type == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        Collection collection = new Collection();
        collection.setUserId(user_id);
        collection.setType(type);
        collection.setPostId(id);
        collection.setIsDetele((short) 2);
        List<Collection> cList = collectionService.findList(collection);
        if (cList != null && cList.size() > 0) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "您已收藏", null)));
            return;
        }

        Collection cEntity = new Collection();
        cEntity.setCreateTime(new Date());
        cEntity.setIsDetele((short) 2);
        cEntity.setPostId(id);
        cEntity.setType(type);
        cEntity.setUserId(user_id);
        collectionService.addCollection(cEntity);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", null)));
    }

    /**
     * 分享
     *
     * @param response
     * @param type
     * @param id
     */
    @RequestMapping("/app_shareInfo")
    public void app_shareInfo(HttpServletResponse response, Integer type, Integer id) {
        if (type == null || id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }
        String appPath = "https://app.54xy.com/";
        HashMap<String, Object> map = new HashMap<String, Object>();
        String url = "";
        String title = "";
        String content = "";
        String pic = "";

        // 帖子
        if (type.intValue() == 1) {
            url = appPath + "admin/post/" + id;
            Post pEntity = new Post();
            pEntity.setId(id);
            Post post = postService.getPost(pEntity);
            title = post.getTitle();
            content = post.getContent();
            String pics = post.getImg();
            if (StringUtils.isNotBlank(pics)) {
                pic = appPath + "campus/" + pics.split(",")[0];
            }
        } else if (type.intValue() == 2) {// 新鲜事
            url = appPath + "admin/novelty/" + id;
            title = "分享新鲜事";
            // 获取新鲜事
            Saying saying = sayingService.getById(id);
            content = saying.getContent();
            String pics = saying.getImg();
            // 如果图片不为空
            if (StringUtils.isNotBlank(pics)) {
                pic = appPath + "campus/" + pics.split(",")[0];
            }
        } else if (type.intValue() == 3) {// 群聊
            url = appPath + "admin/group/" + id;
            title = "分享群聊";
            // 获取群组信息
            Group group = groupService.getById(id);
            content = group.getName();
            pic = group.getHeadimg();
        } else {
            // 分享应用
            url = "https://a.app.qq.com/o/simple.jsp?pkgname=com.biu.i54school.android";
            title = "分享应用";
            content = "54校园,更多精彩等你来!";
        }
        if (StringUtils.isBlank(pic)) {
            pic = appPath + "resources/images/120.png";
        }
        map.put("title", title);
        map.put("content", content);
        map.put("pic", pic);
        map.put("url", url);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", map)));
    }

    /**
     * 获取设置审批流程类型列表
     *
     * @param response
     */
    @RequestMapping("app_findAuditTypeList")
    public void app_findAuditTypeList(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {
        List<Map> mapList = new ArrayList<>();
        User user = userService.getById(userId);
        AuditUserAuth userAuth = auditUserAuthService.find(user.getSchoolId(), userId);
        if (userAuth == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请先进行身份认证")));
            return;
        }
        List<AuditPermission> permissionList = auditRolePermissionService.findPermissionListByRoleId(userAuth.getRoleId());
        for (AuditPermission permission : permissionList) {
            if (permission.getType().equals("m")) {
                Map auditTypeMap = new HashMap<>();
                auditTypeMap.put("code", permission.getCode());
                auditTypeMap.put("name", permission.getName());
                mapList.add(auditTypeMap);
            }
        }

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", mapList)));
    }
}
