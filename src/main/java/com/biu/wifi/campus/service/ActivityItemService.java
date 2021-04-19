package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ActivityItemMapper;
import com.biu.wifi.campus.dao.model.ActivityItem;
import com.biu.wifi.campus.dao.model.ActivityItemCriteria;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/11/16.
 */
@Service
public class ActivityItemService {

    @Autowired
    private ActivityItemMapper activityItemMapper;

    /**
     * 新增活动节目
     *
     * @param actId
     * @param activityItemList
     * @return
     */
    public void add(Integer actId, List<ActivityItem> activityItemList) {
        //删除旧的
        ActivityItemCriteria example = new ActivityItemCriteria();
        ActivityItemCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andActIdEqualTo(actId);
        ActivityItem activityItem = new ActivityItem();
        activityItem.setIsDelete((short) 1);
        activityItem.setDeleteTime(new Date());
        activityItemMapper.updateByExampleSelective(activityItem, example);

        //添加新的
        Date date;
        for (ActivityItem item : activityItemList) {
            date = new Date();
            item.setActId(actId);
            item.setCreateTime(date);
            item.setVersion(String.valueOf(date.getTime()));
            activityItemMapper.insertSelective(item);
        }
    }

    /**
     * 删除活动节目
     *
     * @param itemIds
     * @return
     */
    public void delete(String itemIds) {
        String[] itemIdList = itemIds.split(",");
        ActivityItem activityItem;
        for (String id : itemIdList) {
            if (StringUtils.isNotBlank(id)) {
                activityItem = activityItemMapper.selectByPrimaryKey(Integer.valueOf(id));
                activityItem.setDeleteTime(new Date());
                activityItem.setIsDelete((short) 1);
                activityItemMapper.updateByPrimaryKeySelective(activityItem);
            }
        }
    }

    /**
     * 更新票数
     *
     * @param id                       节目ID
     * @param version                  版本号
     * @param blackBoxOperateVoteCount 暗箱操作票数
     * @return
     */
    public Result updateVotes(Integer id, String version, Integer blackBoxOperateVoteCount) {
        ActivityItem item = activityItemMapper.selectByPrimaryKey(id);
        if (item == null || (item != null && item.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该节目不存在");
        }

        if (!version.equals(item.getVersion())) {
            return new Result(Result.CUSTOM_MESSAGE, "系统忙，请稍后再试");
        }

        Date date = new Date();
        item.setUpdateTime(date);
        item.setVersion(String.valueOf(date.getTime()));
        if (blackBoxOperateVoteCount != null) {
            //暗箱操作更改票数
            int diffCount = blackBoxOperateVoteCount - item.getVoteCount();
            item.setVoteCount(blackBoxOperateVoteCount);
            item.setDiffCount(item.getDiffCount() + diffCount);
        } else {
            //正常投票
            item.setVoteCount(item.getVoteCount() + 1);
        }
        activityItemMapper.updateByPrimaryKeySelective(item);
        return new Result(Result.SUCCESS, "成功");
    }

    public List<ActivityItem> findList(ActivityItemCriteria example) {
        return activityItemMapper.selectByExample(example);
    }

    public ActivityItem find(Integer id) {
        return activityItemMapper.selectByPrimaryKey(id);
    }
}
