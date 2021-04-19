package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ActivityMapper;
import com.biu.wifi.campus.dao.ActivityVoteMapper;
import com.biu.wifi.campus.dao.model.Activity;
import com.biu.wifi.campus.dao.model.ActivityItem;
import com.biu.wifi.campus.dao.model.ActivityVote;
import com.biu.wifi.campus.dao.model.ActivityVoteCriteria;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.component.goeasy.GoEasyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/11/16.
 */
@Service
public class ActivityVoteService {

    @Autowired
    private ActivityVoteMapper activityVoteMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityItemService activityItemService;
    @Autowired
    private ActivityService activityService;

    /**
     * 活动投票
     *
     * @param itemId
     * @param version
     * @param userId
     * @return
     */
    @Transactional
    public Result add(Integer itemId, String version, Integer userId) {
        ActivityItem activityItem = activityItemService.find(itemId);
        if (activityItem == null || (activityItem != null && activityItem.getIsDelete().intValue() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该节目不存在");
        }

        Activity activity = activityMapper.selectByPrimaryKey(activityItem.getActId());
        if (activity == null || (activity != null && activity.getIsDelete().intValue() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该活动不存在");
        }

        if (activity.getStatus().intValue() == 2) {
            return new Result(Result.CUSTOM_MESSAGE, "该活动已停止，不能进行投票");
        }

        if (userId.intValue() != activity.getUserId().intValue() && !"0".equals(activity.getVotePermission())) {
            //判断用户是否具有投票权限
            String[] userIdList = activity.getVotePermission().split(",");
            if (!Arrays.asList(userIdList).contains(String.valueOf(userId))) {
                return new Result(Result.CUSTOM_MESSAGE, "没有投票的权限");
            }
        }

        //根据活动投票的单人最大可投票数，判断用户是否还可以再投
        boolean allowVoteAgain = allowVoteAgain(activity, userId);
        if (!allowVoteAgain) {
            return new Result(Result.CUSTOM_MESSAGE, "您的投票次数已用完");
        }

        ActivityVote activityVote = new ActivityVote();
        activityVote.setActId(activity.getId());
        activityVote.setItemId(itemId);
        activityVote.setUserId(userId);
        activityVote.setCreateTime(new Date());
        activityVoteMapper.insertSelective(activityVote);
        synchronized (this) {
            Result result = activityItemService.updateVotes(itemId, version, null);
            if (result.getKey() != 1) {
                //事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }

        //将票数图表数据推送给后台
        StringBuffer channel = new StringBuffer("the_result_of_activity_");
        channel.append(activity.getId());
        List<Map> data = activityService.getVoteChartData(activity.getId());
        GoEasyUtil.publish(channel.toString(), data);
        return new Result(Result.SUCCESS, "成功");
    }

    /**
     * 判断是否已经投票过某节目
     *
     * @param itemId 节目ID
     * @param userId
     * @return
     */
    public boolean isVote(Integer itemId, Integer userId) {
        ActivityVoteCriteria example = new ActivityVoteCriteria();
        example.createCriteria()
                .andItemIdEqualTo(itemId)
                .andUserIdEqualTo(userId);
        long count = activityVoteMapper.countByExample(example);
        return count > 0 ? true : false;
    }

    /**
     * 指定人允许再次对某活动进行投票
     *
     * @param activity
     * @param userId
     * @return
     */
    public boolean allowVoteAgain(Activity activity, Integer userId) {
        ActivityVoteCriteria activityVoteEx = new ActivityVoteCriteria();
        activityVoteEx.createCriteria()
                .andActIdEqualTo(activity.getId())
                .andUserIdEqualTo(userId);
        long count = activityVoteMapper.countByExample(activityVoteEx);
        return count < activity.getVoteMaxCount();
    }
}
