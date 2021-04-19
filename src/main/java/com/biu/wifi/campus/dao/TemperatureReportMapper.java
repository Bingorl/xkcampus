package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.TemperatureReport;
import com.biu.wifi.campus.dao.model.TemperatureReportExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 张彬.
 * @date 2021/4/9 16:03.
 */
@Repository
public interface TemperatureReportMapper {
    long countByExample(TemperatureReportExample example);

    int deleteByExample(TemperatureReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TemperatureReport record);

    int insertSelective(TemperatureReport record);

    List<TemperatureReport> selectByExample(TemperatureReportExample example);

    TemperatureReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TemperatureReport record, @Param("example") TemperatureReportExample example);

    int updateByExample(@Param("record") TemperatureReport record, @Param("example") TemperatureReportExample example);

    int updateByPrimaryKeySelective(TemperatureReport record);

    int updateByPrimaryKey(TemperatureReport record);

    List<TemperatureReport> search(@Param("userId") Integer userId);
}