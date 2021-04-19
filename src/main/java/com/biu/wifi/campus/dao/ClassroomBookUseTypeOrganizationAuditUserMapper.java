package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUser;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUserCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBookUseTypeOrganizationAuditUserMapper extends CoreDao {
    long countByExample(ClassroomBookUseTypeOrganizationAuditUserCriteria example);

    int deleteByExample(ClassroomBookUseTypeOrganizationAuditUserCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBookUseTypeOrganizationAuditUser record);

    int insertSelective(ClassroomBookUseTypeOrganizationAuditUser record);

    List<ClassroomBookUseTypeOrganizationAuditUser> selectByExample(ClassroomBookUseTypeOrganizationAuditUserCriteria example);

    ClassroomBookUseTypeOrganizationAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBookUseTypeOrganizationAuditUser record, @Param("example") ClassroomBookUseTypeOrganizationAuditUserCriteria example);

    int updateByExample(@Param("record") ClassroomBookUseTypeOrganizationAuditUser record, @Param("example") ClassroomBookUseTypeOrganizationAuditUserCriteria example);

    int updateByPrimaryKeySelective(ClassroomBookUseTypeOrganizationAuditUser record);

    int updateByPrimaryKey(ClassroomBookUseTypeOrganizationAuditUser record);
}