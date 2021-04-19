package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ActivityVote;
import com.biu.wifi.campus.dao.model.ActivityVoteCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityVoteMapper {
    long countByExample(ActivityVoteCriteria example);

    int deleteByExample(ActivityVoteCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActivityVote record);

    int insertSelective(ActivityVote record);

    List<ActivityVote> selectByExample(ActivityVoteCriteria example);

    ActivityVote selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActivityVote record, @Param("example") ActivityVoteCriteria example);

    int updateByExample(@Param("record") ActivityVote record, @Param("example") ActivityVoteCriteria example);

    int updateByPrimaryKeySelective(ActivityVote record);

    int updateByPrimaryKey(ActivityVote record);
}