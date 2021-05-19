package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.OfficialWebsiteAudit;
import com.biu.wifi.campus.dao.model.OfficialWebsiteAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialWebsiteAuditMapper {
    long countByExample(OfficialWebsiteAuditExample example);

    int deleteByExample(OfficialWebsiteAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OfficialWebsiteAudit record);

    int insertSelective(OfficialWebsiteAudit record);

    List<OfficialWebsiteAudit> selectByExample(OfficialWebsiteAuditExample example);

    OfficialWebsiteAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OfficialWebsiteAudit record, @Param("example") OfficialWebsiteAuditExample example);

    int updateByExample(@Param("record") OfficialWebsiteAudit record, @Param("example") OfficialWebsiteAuditExample example);

    int updateByPrimaryKeySelective(OfficialWebsiteAudit record);

    int updateByPrimaryKey(OfficialWebsiteAudit record);

    HashMap selectMap(@Param("websiteId") Integer id,@Param("userId") String userId);

}