package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.BiuTravelExpenseInfo;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseInfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BiuTravelExpenseInfoMapper {
    long countByExample(BiuTravelExpenseInfoExample example);

    int deleteByExample(BiuTravelExpenseInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BiuTravelExpenseInfo record);

    int insertSelective(BiuTravelExpenseInfo record);

    List<BiuTravelExpenseInfo> selectByExample(BiuTravelExpenseInfoExample example);

    BiuTravelExpenseInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BiuTravelExpenseInfo record, @Param("example") BiuTravelExpenseInfoExample example);

    int updateByExample(@Param("record") BiuTravelExpenseInfo record, @Param("example") BiuTravelExpenseInfoExample example);

    int updateByPrimaryKeySelective(BiuTravelExpenseInfo record);

    int updateByPrimaryKey(BiuTravelExpenseInfo record);

    List<HashMap> myExpenseInfoList(@Param("userId") Integer userId,
                                    @Param("startDate") String startDate,
                                    @Param("endDate") String endDate,
                                    @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditLeaveInfoList(@Param("userId") Integer userId,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("statusList") List<Short> statusList);
}