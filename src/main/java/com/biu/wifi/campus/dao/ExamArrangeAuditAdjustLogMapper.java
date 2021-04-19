package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ExamArrangeAuditAdjustLog;
import com.biu.wifi.campus.dao.model.ExamArrangeAuditAdjustLogCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamArrangeAuditAdjustLogMapper {
    long countByExample(ExamArrangeAuditAdjustLogCriteria example);

    int deleteByExample(ExamArrangeAuditAdjustLogCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExamArrangeAuditAdjustLog record);

    int insertSelective(ExamArrangeAuditAdjustLog record);

    List<ExamArrangeAuditAdjustLog> selectByExample(ExamArrangeAuditAdjustLogCriteria example);

    ExamArrangeAuditAdjustLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExamArrangeAuditAdjustLog record, @Param("example") ExamArrangeAuditAdjustLogCriteria example);

    int updateByExample(@Param("record") ExamArrangeAuditAdjustLog record, @Param("example") ExamArrangeAuditAdjustLogCriteria example);

    int updateByPrimaryKeySelective(ExamArrangeAuditAdjustLog record);

    int updateByPrimaryKey(ExamArrangeAuditAdjustLog record);
}