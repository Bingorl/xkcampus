package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SuppliesPurchaseInfo;
import com.biu.wifi.campus.dao.model.SuppliesPurchaseInfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SuppliesPurchaseInfoMapper {
    long countByExample(SuppliesPurchaseInfoExample example);

    int deleteByExample(SuppliesPurchaseInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SuppliesPurchaseInfo record);

    int insertSelective(SuppliesPurchaseInfo record);

    List<SuppliesPurchaseInfo> selectByExample(SuppliesPurchaseInfoExample example);

    SuppliesPurchaseInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SuppliesPurchaseInfo record, @Param("example") SuppliesPurchaseInfoExample example);

    int updateByExample(@Param("record") SuppliesPurchaseInfo record, @Param("example") SuppliesPurchaseInfoExample example);

    int updateByPrimaryKeySelective(SuppliesPurchaseInfo record);

    int updateByPrimaryKey(SuppliesPurchaseInfo record);

    List<HashMap> mySuppliesPurchaseInfoList(@Param("userId") Integer userId,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditpurchaseInfoList(@Param("userId") Integer userId,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("statusList") List<Short> statusList);
}