package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.CetScore;
import com.biu.wifi.campus.dao.model.CetScoreCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CetScoreMapper {
    long countByExample(CetScoreCriteria example);

    int deleteByExample(CetScoreCriteria example);

    int deleteByPrimaryKey(String exCardNum);

    int insert(CetScore record);

    int insertSelective(CetScore record);

    List<CetScore> selectByExample(CetScoreCriteria example);

    CetScore selectByPrimaryKey(String exCardNum);

    int updateByExampleSelective(@Param("record") CetScore record, @Param("example") CetScoreCriteria example);

    int updateByExample(@Param("record") CetScore record, @Param("example") CetScoreCriteria example);

    int updateByPrimaryKeySelective(CetScore record);

    int updateByPrimaryKey(CetScore record);
}