package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Report;
import com.biu.wifi.campus.dao.model.ReportCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMapper extends CoreDao {
    long countByExample(ReportCriteria example);

    int deleteByExample(ReportCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Report record);

    int insertSelective(Report record);

    List<Report> selectByExample(ReportCriteria example);

    Report selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Report record, @Param("example") ReportCriteria example);

    int updateByExample(@Param("record") Report record, @Param("example") ReportCriteria example);

    int updateByPrimaryKeySelective(Report record);

    int updateByPrimaryKey(Report record);
}