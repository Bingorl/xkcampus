package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.RepairCostInfo;
import com.biu.wifi.campus.dao.model.RepairCostInfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairCostInfoMapper {
    long countByExample(RepairCostInfoExample example);

    int deleteByExample(RepairCostInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RepairCostInfo record);

    int insertSelective(RepairCostInfo record);

    List<RepairCostInfo> selectByExample(RepairCostInfoExample example);

    RepairCostInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RepairCostInfo record, @Param("example") RepairCostInfoExample example);

    int updateByExample(@Param("record") RepairCostInfo record, @Param("example") RepairCostInfoExample example);

    int updateByPrimaryKeySelective(RepairCostInfo record);

    int updateByPrimaryKey(RepairCostInfo record);

    List<HashMap> myRepairCostInfoList(@Param("userId") Integer userId,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditRepairCostInfoList(@Param("userId") Integer userId,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate,
                                            @Param("statusList") List<Short> statusList);
}