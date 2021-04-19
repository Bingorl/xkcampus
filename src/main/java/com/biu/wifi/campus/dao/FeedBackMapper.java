package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.FeedBack;
import com.biu.wifi.campus.dao.model.FeedBackCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackMapper extends CoreDao {
    long countByExample(FeedBackCriteria example);

    int deleteByExample(FeedBackCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(FeedBack record);

    int insertSelective(FeedBack record);

    List<FeedBack> selectByExample(FeedBackCriteria example);

    FeedBack selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FeedBack record, @Param("example") FeedBackCriteria example);

    int updateByExample(@Param("record") FeedBack record, @Param("example") FeedBackCriteria example);

    int updateByPrimaryKeySelective(FeedBack record);

    int updateByPrimaryKey(FeedBack record);
}