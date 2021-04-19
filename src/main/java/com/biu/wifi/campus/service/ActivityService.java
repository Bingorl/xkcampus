package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ActivityMapper;
import com.biu.wifi.campus.dao.model.Activity;
import com.biu.wifi.campus.dao.model.ActivityCriteria;
import com.biu.wifi.campus.dao.model.ActivityItem;
import com.biu.wifi.campus.dao.model.ActivityItemCriteria;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/11/16.
 */
@Service
public class ActivityService {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityItemService activityItemService;
    @Autowired
    private ActivityVoteService activityVoteService;
    @Autowired
    private FileSupportService fileSupportService;

    /**
     * 新增或编辑活动
     *
     * @param activity
     * @return
     */
    @Transactional
    public Result addOrUpdate(Activity activity, List<ActivityItem> activityItemList, String[] delFileIds) {
        ActivityCriteria example = new ActivityCriteria();
        ActivityCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(activity.getSchoolId())
                .andNameEqualTo(activity.getName())
                .andProfileEqualTo(activity.getProfile());
        if (activity.getId() != null) {
            criteria.andIdNotEqualTo(activity.getId());
        }
        long count = activityMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "请勿重复添加");
        }

        if (activity.getId() == null) {
            activity.setCreateTime(new Date());
            activityMapper.insertSelective(activity);
        } else {
            activity.setUpdateTime(new Date());
            activityMapper.updateByPrimaryKeySelective(activity);
        }
        activityItemService.add(activity.getId(), activityItemList);

        /*if (delFileIds != null) {
            for (String fileId : delFileIds)
                fileSupportService.remove(fileId);
        }*/
        return new Result(Result.SUCCESS, "成功");
    }

    /**
     * 删除活动
     *
     * @param actIds
     * @param userId
     * @return
     */
    public Result delete(String actIds, Integer userId) {
        String[] idList = actIds.split(",");
        Activity activity;
        for (String id : idList) {
            if (StringUtils.isNotBlank(id)) {
                activity = activityMapper.selectByPrimaryKey(Integer.valueOf(id));
                if (activity == null || (activity != null && activity.getIsDelete().shortValue() == 1)) {
                    return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
                }

                if (userId.intValue() != activity.getUserId().intValue()) {
                    return new Result(Result.CUSTOM_MESSAGE, "没有权限");
                }

                activity.setIsDelete((short) 1);
                activity.setDeleteTime(new Date());
                activityMapper.updateByPrimaryKeySelective(activity);
            }
        }
        return new Result(Result.SUCCESS, "成功");
    }

    /**
     * 更改活动状态
     *
     * @param id
     * @param toggle 是否为切换操作，发布为false
     */
    public void changeStatus(Integer id, boolean toggle) {
        Activity activity = activityMapper.selectByPrimaryKey(id);
        if (!toggle) {
            //发布活动
            activity.setStatus((short) 1);
        } else {
            if (activity.getStatus().intValue() == 1) {
                //暂停
                activity.setStatus((short) 2);
            } else {
                //开始
                activity.setStatus((short) 1);
            }
        }
        activity.setUpdateTime(new Date());
        activityMapper.updateByPrimaryKeySelective(activity);
    }

    /**
     * 更改票数
     *
     * @param actId
     * @param itemId
     * @param userId
     * @param newVoteCount
     */
    public void updateActivityVote(Integer actId, Integer itemId, String version, Integer userId, Integer newVoteCount) {
        Activity activity = activityMapper.selectByPrimaryKey(actId);
        if (activity == null || (activity != null && activity.getIsDelete().intValue() == 1)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该活动不存在");
        }

        if (activity.getUserId().intValue() != userId.intValue()) {
            throw new BizException(Result.CUSTOM_MESSAGE, "没有权限");
        }

        ActivityItem activityItem = activityItemService.find(itemId);
        if (activityItem == null || (activityItem != null && activityItem.getIsDelete().intValue() == 1)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该活动项目不存在");
        }

        if (newVoteCount < 0) {
            throw new BizException(Result.CUSTOM_MESSAGE, "票数不能为负数");
        }

        activityItemService.updateVotes(itemId, version, newVoteCount);
    }

    /**
     * 活动详情
     *
     * @param id
     * @return
     */
    public Activity find(Integer id) {
        return activityMapper.selectByPrimaryKey(id);
    }

    /**
     * 活动详情
     *
     * @param id
     * @param userId
     * @return
     */
    public Map find(Integer id, Integer userId, Integer page, Integer pageSize) {
        Map data = new HashMap();
        Activity activity = find(id);
        //活动数据
        data.put("actId", activity.getId());
        data.put("logo", activity.getLogo());
        data.put("status", activity.getStatus());
        data.put("title", activity.getName());
        data.put("profile", activity.getProfile());
        data.put("allowVoteAgain", activityVoteService.allowVoteAgain(activity, userId));

        //节目数据
        List<Map> itemMapList = new ArrayList<>();
        ActivityItemCriteria activityItemEx = new ActivityItemCriteria();
        activityItemEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andActIdEqualTo(id);
        if (page != null && pageSize != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);
        }
        List<ActivityItem> activityItemList = activityItemService.findList(activityItemEx);
        Map itemMap;
        for (ActivityItem item : activityItemList) {
            itemMap = new HashMap();
            itemMap.put("itemId", item.getId());
            itemMap.put("itemNo", item.getItemNo());
            itemMap.put("logo", item.getLogo());
            itemMap.put("username", item.getUsername());
            itemMap.put("itemName", item.getItemName());
            itemMap.put("voteCount", item.getVoteCount());
            itemMap.put("version", item.getVersion());
            itemMap.put("isVote", activityVoteService.isVote(item.getId(), userId));
            itemMapList.add(itemMap);
        }

        //投票排名数据
        Collections.sort(activityItemList, new Comparator<ActivityItem>() {
            @Override
            public int compare(ActivityItem o1, ActivityItem o2) {
                return o2.getVoteCount().compareTo(o1.getVoteCount());
            }
        });
        List<Map> rankMapList = new ArrayList<>();
        for (int i = 0; i < activityItemList.size(); i++) {
            if (i == 3) {
                break;
            }
            ActivityItem item = activityItemList.get(i);
            itemMap = new HashMap();
            itemMap.put("itemId", item.getId());
            itemMap.put("itemNo", item.getItemNo());
            itemMap.put("logo", item.getLogo());
            itemMap.put("username", item.getUsername());
            itemMap.put("itemName", item.getItemName());
            itemMap.put("voteCount", item.getVoteCount());
            itemMap.put("version", item.getVersion());
            itemMap.put("isVote", activityVoteService.isVote(item.getId(), userId));
            rankMapList.add(itemMap);
        }
        data.put("itemMapList", itemMapList);
        data.put("rankMapList", rankMapList);
        return data;
    }

    /**
     * 活动列表
     *
     * @param example
     * @param userId  用户ID
     * @param isMine  0后台管理员查看所有活动，1查看自己发布的活动，2app端用户查看所有活动
     * @return
     */
    public List<Activity> findList(ActivityCriteria example, Integer userId, Integer isMine) {
        List<Activity> activityList = activityMapper.selectByExample(example);

        if (isMine.intValue() == 2) {
            //查询所有活动，需要去除没有投票权限的活动
            Iterator<Activity> iterator = activityList.iterator();
            String[] array;
            String userIdStr = String.valueOf(userId);
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (!"0".equals(activity.getVotePermission())) {
                    array = activity.getVotePermission().split(",");
                    if (!Arrays.asList(array).contains(userIdStr)) {
                        iterator.remove();
                    }
                }
            }
        }

        return activityList;
    }

    /**
     * 获取活动投票数据(echarts图表格式的数据)
     * <p>
     * var option = {
     * title: {
     * text: '活动投票数据'
     * },
     * tooltip: {},
     * legend: {
     * data: ['票数']
     * },
     * xAxis: {
     * data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
     * },
     * yAxis: {},
     * series: [{
     * name: '票数',
     * type: 'bar',
     * data: [5, 20, 36, 10, 10, 60]
     * }]
     * };
     */
    public Map getEchartsVoteData(Integer actId) {
        Activity activity = activityMapper.selectByPrimaryKey(actId);
        ActivityItemCriteria example = new ActivityItemCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andActIdEqualTo(actId);
        List<ActivityItem> activityItemList = activityItemService.findList(example);
        List<String> xAxisData = new ArrayList<>();
        List<Integer> seriesData = new ArrayList<>();
        for (ActivityItem item : activityItemList) {
            String itemName = item.getItemNo() + "  " + item.getItemName();
            xAxisData.add(itemName);
            seriesData.add(item.getVoteCount());
        }
        Map data = new HashMap();
        Map title = new HashMap();
        title.put("text", activity.getName());
        data.put("title", title);

        Map legend = new HashMap();
        legend.put("data", Arrays.asList("票数"));
        data.put("legend", legend);

        Map xAxis = new HashMap();
        xAxis.put("data", xAxisData);
        data.put("xAxis", xAxis);

        Map seriesMap = new HashMap();
        seriesMap.put("name", "票数");
        seriesMap.put("type", "bar");
        seriesMap.put("data", seriesData);
        data.put("series", Arrays.asList(seriesMap));
        return data;
    }

    /**
     * 获取活动投票数据(自定义DIV布局的样式)
     *
     * @param actId
     * @return
     */
    public List<Map> getVoteChartData(Integer actId) {
        ActivityItemCriteria activityItemEx = new ActivityItemCriteria();
        activityItemEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andActIdEqualTo(actId);
        List<ActivityItem> activityItemList = activityItemService.findList(activityItemEx);

        //按票数倒序排
        Collections.sort(activityItemList, new Comparator<ActivityItem>() {
            @Override
            public int compare(ActivityItem o1, ActivityItem o2) {
                return o2.getVoteCount().compareTo(o1.getVoteCount());
            }
        });

        List<Map> maps = new ArrayList<>();
        int total = 1;//避免分母为0
        for (int i = 0; i < activityItemList.size(); i++) {
            total += activityItemList.get(i).getVoteCount();
        }
        for (int i = 0; i < activityItemList.size(); i++) {
            ActivityItem item = activityItemList.get(i);
            BeanMap beanMap = BeanMap.create(item);
            Map map = new HashMap();
            map.putAll(beanMap);
            map.put("rankingSort", (i + 1) + "名");
            double rate = ((double) item.getVoteCount()) / total;
            BigDecimal bg = new BigDecimal(rate);
            rate = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
            map.put("bottom", (rate + 5) + "%");
            map.put("height", rate + "%");
            maps.add(map);
        }

        //按编号升序排
        Collections.sort(maps, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                String no1 = o1.get("itemNo").toString();
                String no2 = o2.get("itemNo").toString();
                return no1.compareTo(no2);
            }
        });

        return maps;
    }
}
