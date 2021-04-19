package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.ActivityItemService;
import com.biu.wifi.campus.service.ActivityService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/11/17.
 */
@Controller
public class BackendActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityItemService activityItemService;

    /**
     * 新增或编辑活动
     *
     * @param userId
     * @param act
     * @param itemList
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateActivity")
    public void back_api_addOrUpdateActivity(Integer userId, String act, String itemList, String deleteFileId, HttpServletResponse response) {
        if (StringUtils.isBlank(act) || StringUtils.isBlank(itemList)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }
        List<ActivityItem> activityItemList = null;
        Activity activity = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            activity = objectMapper.readValue(act, Activity.class);
            List list = objectMapper.readValue(itemList, List.class);
            int i = 1;
            if (CollectionUtils.isNotEmpty(list)) {
                activityItemList = new ArrayList<>();
            } else {
                ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "请添加活动项目"));
                return;
            }
            for (Object o : list) {
                ActivityItem activityItem = new ActivityItem();
                Map m = (Map) o;
                activityItem.setItemNo(i + "号");
                activityItem.setUsername(m.get("name").toString());
                activityItem.setItemName(m.get("title").toString());
                if (m.get("logo") != null) {
                    activityItem.setLogo(m.get("logo").toString());
                }
                activityItemList.add(activityItem);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (activity == null || activityItemList == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "提交的数据不正确"));
            return;
        }

        activity.setUserId(userId);
        User user = userService.getById(userId);
        activity.setSchoolId(user.getSchoolId());
        activityService.addOrUpdate(activity, activityItemList, StringUtils.isBlank(deleteFileId) ? null : deleteFileId.split(","));
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }

    /**
     * 活动列表
     *
     * @param userId
     * @param page     页码
     * @param pageSize 每页记录数
     * @param status   状态(NULL未发布，1进行中，2暂停)
     * @param isMine   是否是我发布的(1是，2否)，0为超级管理员查看所有的活动
     * @param keyword  活动名称的关键字
     * @param response
     */
    @RequestMapping("back_api_findActivityList")
    public void back_api_findActivityList(Integer userId, Integer page,
                                          @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                          @RequestParam(defaultValue = "0", required = false) Short status,
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

        if (status != 0) {
            criteria.andStatusEqualTo(status);
        }

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }

        if (isMine.intValue() == 1) {
            //我发布的
            criteria.andUserIdEqualTo(userId);
        }
        List<Activity> activityList = activityService.findList(example, userId, isMine);

        //操作权限控制
        List<Map> maps = new ArrayList<>();
        for (Activity activity : activityList) {
            BeanMap beanMap = BeanMap.create(activity);
            Map map = new HashMap();
            map.putAll(beanMap);
            map.put("isMine", userId.intValue() == activity.getUserId().intValue() ? true : false);
            maps.add(map);
        }

        Map data = new HashMap();
        data.put("list", maps);
        data.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 活动详情
     *
     * @param userId
     * @param actId    活动ID
     * @param response
     */
    @RequestMapping("back_api_activityDetail")
    public void back_api_activityDetail(Integer userId, Integer actId, HttpServletResponse response) {
        if (userId == null || actId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        Activity activity = activityService.find(actId);
        if (activity == null || activity.getIsDelete().intValue() == 1) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该活动不存在"));
            return;
        }

        List<Integer> userIdList = new ArrayList<>();
        if (!"0".equals(activity.getVotePermission())) {
            for (String id : activity.getVotePermission().split(",")) {
                userIdList.add(Integer.valueOf(id));
            }
        }

        List<User> userList;
        if (userIdList.size() == 0) {
            userList = new ArrayList<>();
        } else {
            UserCriteria userEx = new UserCriteria();
            userEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andIdIn(userIdList);
            userList = userService.findList(userEx);
        }

        ActivityItemCriteria actItemEx = new ActivityItemCriteria();
        actItemEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andActIdEqualTo(actId);
        List<ActivityItem> activityItemList = activityItemService.findList(actItemEx);

        Map data = new HashMap();
        data.put("activity", activity);
        data.put("itemList", activityItemList);
        data.put("userList", userList);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 活动投票数据
     *
     * @param actId    活动ID
     * @param response
     */
    @RequestMapping("back_api_activityVoteData")
    public void back_api_activityVoteData(Integer actId, HttpServletResponse response) {
        if (actId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        List<Map> data = activityService.getVoteChartData(actId);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 更改票数
     *
     * @param actId
     * @param itemId
     * @param userId
     * @param newVoteCount
     * @param response
     */
    @RequestMapping("back_api_updateActivityVote")
    public void back_api_updateActivityVote(Integer actId, Integer itemId, String version, Integer userId, Integer newVoteCount, HttpServletResponse response) {
        if (actId == null || itemId == null || version == null || userId == null || newVoteCount == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        activityService.updateActivityVote(actId, itemId, version, userId, newVoteCount);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }

    /**
     * 更改活动状态
     *
     * @param actId
     * @param toggle   1切换活动状态，2发布
     * @param response
     */
    @RequestMapping("back_api_changeStatus")
    public void back_api_changeStatus(Integer actId, @RequestParam(defaultValue = "2", required = false) Integer toggle, HttpServletResponse response) {
        if (actId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        activityService.changeStatus(actId, toggle == 1 ? true : false);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }

    /**
     * 删除活动草稿
     *
     * @param actIds
     * @param userId
     * @param response
     */
    @RequestMapping("back_api_deleteActivity")
    public void back_api_deleteActivity(String actIds, Integer userId, HttpServletResponse response) {
        if (StringUtils.isBlank(actIds)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数"));
            return;
        }

        activityService.delete(actIds, userId);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功"));
    }
}
