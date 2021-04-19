package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBookAuditNotice;
import com.biu.wifi.campus.dao.model.ClassroomBookAuditNoticeCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBookAuditNoticeMapper {
    long countByExample(ClassroomBookAuditNoticeCriteria example);

    int deleteByExample(ClassroomBookAuditNoticeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBookAuditNotice record);

    int insertSelective(ClassroomBookAuditNotice record);

    List<ClassroomBookAuditNotice> selectByExample(ClassroomBookAuditNoticeCriteria example);

    ClassroomBookAuditNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBookAuditNotice record, @Param("example") ClassroomBookAuditNoticeCriteria example);

    int updateByExample(@Param("record") ClassroomBookAuditNotice record, @Param("example") ClassroomBookAuditNoticeCriteria example);

    int updateByPrimaryKeySelective(ClassroomBookAuditNotice record);

    int updateByPrimaryKey(ClassroomBookAuditNotice record);
}