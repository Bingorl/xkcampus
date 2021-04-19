package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganization;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBookUseTypeOrganizationMapper extends CoreDao {
    long countByExample(ClassroomBookUseTypeOrganizationCriteria example);

    int deleteByExample(ClassroomBookUseTypeOrganizationCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBookUseTypeOrganization record);

    int insertSelective(ClassroomBookUseTypeOrganization record);

    List<ClassroomBookUseTypeOrganization> selectByExampleWithBLOBs(ClassroomBookUseTypeOrganizationCriteria example);

    List<ClassroomBookUseTypeOrganization> selectByExample(ClassroomBookUseTypeOrganizationCriteria example);

    ClassroomBookUseTypeOrganization selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBookUseTypeOrganization record, @Param("example") ClassroomBookUseTypeOrganizationCriteria example);

    int updateByExampleWithBLOBs(@Param("record") ClassroomBookUseTypeOrganization record, @Param("example") ClassroomBookUseTypeOrganizationCriteria example);

    int updateByExample(@Param("record") ClassroomBookUseTypeOrganization record, @Param("example") ClassroomBookUseTypeOrganizationCriteria example);

    int updateByPrimaryKeySelective(ClassroomBookUseTypeOrganization record);

    int updateByPrimaryKeyWithBLOBs(ClassroomBookUseTypeOrganization record);

    int updateByPrimaryKey(ClassroomBookUseTypeOrganization record);
}