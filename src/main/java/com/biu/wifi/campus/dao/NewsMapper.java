package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.News;
import com.biu.wifi.campus.dao.model.NewsCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsMapper {
    long countByExample(NewsCriteria example);

    int deleteByExample(NewsCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(News record);

    int insertSelective(News record);

    List<News> selectByExampleWithBLOBs(NewsCriteria example);

    List<News> selectByExample(NewsCriteria example);

    News selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") News record, @Param("example") NewsCriteria example);

    int updateByExampleWithBLOBs(@Param("record") News record, @Param("example") NewsCriteria example);

    int updateByExample(@Param("record") News record, @Param("example") NewsCriteria example);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKeyWithBLOBs(News record);

    int updateByPrimaryKey(News record);
}