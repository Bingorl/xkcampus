package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.OfficialWebsiteAuditUser;
import com.biu.wifi.campus.dao.model.OfficialWebsiteAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialWebsiteAuditUserMapper {
    long countByExample(OfficialWebsiteAuditUserExample example);

    int deleteByExample(OfficialWebsiteAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OfficialWebsiteAuditUser record);

    int insertSelective(OfficialWebsiteAuditUser record);

    List<OfficialWebsiteAuditUser> selectByExample(OfficialWebsiteAuditUserExample example);

    OfficialWebsiteAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OfficialWebsiteAuditUser record, @Param("example") OfficialWebsiteAuditUserExample example);

    int updateByExample(@Param("record") OfficialWebsiteAuditUser record, @Param("example") OfficialWebsiteAuditUserExample example);

    int updateByPrimaryKeySelective(OfficialWebsiteAuditUser record);

    int updateByPrimaryKey(OfficialWebsiteAuditUser record);
}