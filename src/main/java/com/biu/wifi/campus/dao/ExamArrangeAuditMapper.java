package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ExamArrangeAudit;
import com.biu.wifi.campus.dao.model.ExamArrangeAuditCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamArrangeAuditMapper extends CoreDao {
    long countByExample(ExamArrangeAuditCriteria example);

    int deleteByExample(ExamArrangeAuditCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ExamArrangeAudit record);

    int insertSelective(ExamArrangeAudit record);

    List<ExamArrangeAudit> selectByExample(ExamArrangeAuditCriteria example);

    ExamArrangeAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ExamArrangeAudit record, @Param("example") ExamArrangeAuditCriteria example);

    int updateByExample(@Param("record") ExamArrangeAudit record, @Param("example") ExamArrangeAuditCriteria example);

    int updateByPrimaryKeySelective(ExamArrangeAudit record);

    int updateByPrimaryKey(ExamArrangeAudit record);
}