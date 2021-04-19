package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.DiscussionTopicApply;
import com.biu.wifi.campus.dao.model.DiscussionTopicApplyExample;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/4 21:56.
 */
public interface DiscussionTopicApplyMapper {
    long countByExample(DiscussionTopicApplyExample example);

    int deleteByExample(DiscussionTopicApplyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DiscussionTopicApply record);

    int insertSelective(DiscussionTopicApply record);

    List<DiscussionTopicApply> selectByExample(DiscussionTopicApplyExample example);

    DiscussionTopicApply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DiscussionTopicApply record, @Param("example") DiscussionTopicApplyExample example);

    int updateByExample(@Param("record") DiscussionTopicApply record, @Param("example") DiscussionTopicApplyExample example);

    int updateByPrimaryKeySelective(DiscussionTopicApply record);

    int updateByPrimaryKey(DiscussionTopicApply record);

    List<HashMap> myDiscussionTopicApplyList(@Param("userId") Integer userId,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditDiscussionTopicApplyList(@Param("userId") Integer userId,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("statusList") List<Short> statusList);
}