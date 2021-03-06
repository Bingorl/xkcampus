package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.StampToApplyInfo;
import com.biu.wifi.campus.dao.model.StampToApplyInfoExample;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StampToApplyInfoMapper {
    long countByExample(StampToApplyInfoExample example);

    int deleteByExample(StampToApplyInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StampToApplyInfo record);

    int insertSelective(StampToApplyInfo record);

    List<StampToApplyInfo> selectByExample(StampToApplyInfoExample example);

    StampToApplyInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StampToApplyInfo record, @Param("example") StampToApplyInfoExample example);

    int updateByExample(@Param("record") StampToApplyInfo record, @Param("example") StampToApplyInfoExample example);

    int updateByPrimaryKeySelective(StampToApplyInfo record);

    int updateByPrimaryKey(StampToApplyInfo record);

    List<HashMap> myAuditStampApplyInfoList(@Param("userId") Integer userId,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate,
                                            @Param("statusList") List<Short> statusList);


    List<HashMap> myStampApplyInfoList(@Param("userId") Integer userId,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("statusList") List<Short> statusList);
}