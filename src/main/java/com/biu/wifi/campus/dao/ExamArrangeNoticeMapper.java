package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ExamArrangeNotice;
import com.biu.wifi.campus.dao.model.ExamArrangeNoticeCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamArrangeNoticeMapper {
    long countByExample(ExamArrangeNoticeCriteria example);

    int deleteByExample(ExamArrangeNoticeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExamArrangeNotice record);

    int insertSelective(ExamArrangeNotice record);

    List<ExamArrangeNotice> selectByExample(ExamArrangeNoticeCriteria example);

    ExamArrangeNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExamArrangeNotice record, @Param("example") ExamArrangeNoticeCriteria example);

    int updateByExample(@Param("record") ExamArrangeNotice record, @Param("example") ExamArrangeNoticeCriteria example);

    int updateByPrimaryKeySelective(ExamArrangeNotice record);

    int updateByPrimaryKey(ExamArrangeNotice record);
}