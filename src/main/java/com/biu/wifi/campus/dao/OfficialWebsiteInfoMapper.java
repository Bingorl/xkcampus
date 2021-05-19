package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.OfficialWebsiteInfo;
import com.biu.wifi.campus.dao.model.OfficialWebsiteInfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialWebsiteInfoMapper {
    long countByExample(OfficialWebsiteInfoExample example);

    int deleteByExample(OfficialWebsiteInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OfficialWebsiteInfo record);

    int insertSelective(OfficialWebsiteInfo record);

    List<OfficialWebsiteInfo> selectByExample(OfficialWebsiteInfoExample example);

    OfficialWebsiteInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OfficialWebsiteInfo record, @Param("example") OfficialWebsiteInfoExample example);

    int updateByExample(@Param("record") OfficialWebsiteInfo record, @Param("example") OfficialWebsiteInfoExample example);

    int updateByPrimaryKeySelective(OfficialWebsiteInfo record);

    int updateByPrimaryKey(OfficialWebsiteInfo record);

    List<HashMap> myOfficialWebsiteInfoList(@Param("userId") Integer userId,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate,
                                            @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditWebsiteInfoList(@Param("userId") Integer userId,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         @Param("statusList") List<Short> statusList);

}