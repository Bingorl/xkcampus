package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ExamArrange;
import com.biu.wifi.campus.dao.model.ExamArrangeCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamArrangeMapper {
    long countByExample(ExamArrangeCriteria example);

    int deleteByExample(ExamArrangeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExamArrange record);

    int insertSelective(ExamArrange record);

    List<ExamArrange> selectByExample(ExamArrangeCriteria example);

    ExamArrange selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExamArrange record, @Param("example") ExamArrangeCriteria example);

    int updateByExample(@Param("record") ExamArrange record, @Param("example") ExamArrangeCriteria example);

    int updateByPrimaryKeySelective(ExamArrange record);

    int updateByPrimaryKey(ExamArrange record);
}