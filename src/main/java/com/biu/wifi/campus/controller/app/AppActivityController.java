package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.dao.model.Activity;
import com.biu.wifi.campus.dao.model.ActivityCriteria;
import com.biu.wifi.campus.dao.model.ActivityItem;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.ActivityService;
import com.biu.wifi.campus.service.ActivityVoteService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/11/16.
 */
@Controller
public class AppActivityController extends AuthenticatorController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityVoteService activityVoteService;

    /**
     * 新增或编辑活动
     *
     * @param userId
     * @param response
     */
    /**
     * 活动数据
     * activity:{"name":"万圣节活动","profile":"万圣节活动火爆开启","votePermission":"1,2,3,4,5"}
     * 节目数据
     * activityItemList:[{"name":"林俊杰","title":"《小酒窝》"},{"name":"阿炳","title":"《二胡独奏》"}]
     * 图片数据为二进制，表单字段名用file加索引，活动图片为file0，节目图片为file1,file2...
     */
    @RequestMapping("app_addOrUpdateActivity")
    public void app_addOrUpdateActivity(@ModelAttribute("user_id") Integer userId, HttpServletResponse response) {

        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        //活动数据
        Object activityObj = param.get("activity");
        //节目数据
        Object activityItemObj = param.get("activityItemList");
        Activity activity;
        List<ActivityItem> activityItemList = new ArrayList<>();
        ActivityItem activityItem;
        try {
            activity = new ObjectMapper().readValue(activityObj.toString(), Activity.class);
            List list = new ObjectMapper().readValue(activityItemObj.toString(), List.class);
            int i = 1;
            for (Object o : list) {
                activityItem = new ActivityItem();
                Map m = (Map) o;
                activityItem.setItemNo(i + "号");
                activityItem.setUsername(m.get("name").toString());
                activityItem.setItemName(m.get("title").toString());
                activityItemList.add(activityItem);
                i++;
            }
        } catch (IOException e) {
            activity = null;
            activityItemList = null;
        }

        if (activity == null || activityItemList == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }


        //图片个数
        int imageCount = activityItemList == null ? 0 : activityItemList.size();
        imageCount += 1;

        //遍历上传的图片数
        if (imageCount != 0) {
            int j = Integer.valueOf(String.valueOf(imageCount));
            DiskFileItem diskFileItem;
            List<DiskFileItem> logoList;
            String fileName = null;

            for (int i = 0; i < j; i++) {
                //logo文件数据
                logoList = (List<DiskFileItem>) param.get("file" + i);
                if (CollectionUtils.isNotEmpty(logoList)) {
                    diskFileItem = logoList.get(0);
                    byte[] fileContent = diskFileItem.get();
                    fileName = FileUtilsEx.getFileNameByPath(diskFileItem.getName());
                    fileName = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
                }

                //第一张为活动logo
                if (i == 0) {
                    activity.setLogo(fileName);
                } else {
                    activityItem = activityItemList.get(i - 1);
                    //其余的为节目logo
                    activityItem.setLogo(fileName);
                }
            }
        }

        //app端活动添加后默认为进行中
        activity.setStatus((short) 1);
        activity.setUserId(userId);
        User user = userService.getById(userId);
        activity.setSchoolId(user.getSchoolId());
        activityService.addOrUpdate(activity, activityItemList, null);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }


    /**
     * 活动列表
     *
     * @param userId
     * @param page     页码
     * @param pageSize 每页记录数
     * @param isMine   0后台管理员查看所有活动，1查看自己发布的活动，2app端用户查看所有活动
     * @param keyword  活动名称的关键字
     * @param response
     */
    @RequestMapping("app_findActivityList")
    public void app_findActivityList(@ModelAttribute("user_id") Integer userId, Integer page,
                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                     @RequestParam(defaultValue = "2", required = false) Integer isMine,
                                     @RequestParam(defaultValue = "", required = false) String keyword,
                                     HttpServletResponse response) {

        if (page == null || userId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        User user = userService.getById(userId);

        PageLimitHolderFilter.setContext(page, pageSize, null);
        ActivityCriteria example = new ActivityCriteria();
        example.setOrderByClause("create_time desc");
        ActivityCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(user.getSchoolId())
                .andIsDeleteEqualTo((short) 2);

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }

        if (isMine.intValue() == 1) {
            //我发布的
            criteria.andUserIdEqualTo(userId).andStatusIsNotNull();
        } else if (isMine.intValue() == 2) {
            //所有进行中的活动
            criteria.andStatusEqualTo((short) 1);
        }
        List<Activity> activityList = activityService.findList(example, userId, isMine);

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", activityList));
    }

    /**
     * 活动详情
     *
     * @param userId
     * @param actId    活动ID
     * @param page     页码
     * @param pageSize 每页记录数
     * @param response
     */
    @RequestMapping("app_activityDetail")
    public void app_activityDetail(@ModelAttribute("user_id") Integer userId, Integer actId, Integer page,
                                   @RequestParam(defaultValue = "10", required = false) Integer pageSize, HttpServletResponse response) {
        if (page == null || userId == null || actId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        Map data = activityService.find(actId, userId, page, pageSize);
        if (data == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该活动不存在"));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
        }
    }

    /**
     * 更改活动状态
     *
     * @param toggle   1切换活动状态【开始或结束】，2将活动草稿发布成进行中的活动
     * @param actId    活动ID
     * @param response
     */
    @RequestMapping("app_changeStatus")
    public void app_changeStatus(@RequestParam(defaultValue = "2", required = false) Integer toggle,
                                 Integer actId, HttpServletResponse response) {
        if (actId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        activityService.changeStatus(actId, toggle == 1 ? true : false);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }

    /**
     * 投票
     *
     * @param userId
     * @param itemId      节目ID
     * @param itemVersion 版本号
     * @param response
     */
    @RequestMapping("app_addActivityVote")
    public void app_addActivityVote(@ModelAttribute("user_id") Integer userId, Integer itemId, String itemVersion, HttpServletResponse response) {
        if (userId == null || itemId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        Result result = activityVoteService.add(itemId, itemVersion, userId);
        ServletUtilsEx.renderJson(response, result);
    }
}
