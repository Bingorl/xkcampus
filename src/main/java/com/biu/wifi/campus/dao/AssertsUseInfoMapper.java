package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AssertsUseInfo;
import com.biu.wifi.campus.dao.model.AssertsUseInfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssertsUseInfoMapper {
    long countByExample(AssertsUseInfoExample example);

    int deleteByExample(AssertsUseInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AssertsUseInfo record);

    int insertSelective(AssertsUseInfo record);

    List<AssertsUseInfo> selectByExample(AssertsUseInfoExample example);

    AssertsUseInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AssertsUseInfo record, @Param("example") AssertsUseInfoExample example);

    int updateByExample(@Param("record") AssertsUseInfo record, @Param("example") AssertsUseInfoExample example);

    int updateByPrimaryKeySelective(AssertsUseInfo record);

    int updateByPrimaryKey(AssertsUseInfo record);

    List<HashMap> myUseInfoList(@Param("userId") Integer userId,
                                @Param("startDate") String startDate,
                                @Param("endDate") String endDate,
                                @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditUseInfoList(@Param("userId") Integer userId,
                                     @Param("startDate") String startDate,
                                     @Param("endDate") String endDate,
                                     @Param("statusList") List<Short> statusList);
}