package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ContractApproveInfo;
import com.biu.wifi.campus.dao.model.ContractApproveInfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractApproveInfoMapper {
    long countByExample(ContractApproveInfoExample example);

    int deleteByExample(ContractApproveInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContractApproveInfo record);

    int insertSelective(ContractApproveInfo record);

    List<ContractApproveInfo> selectByExample(ContractApproveInfoExample example);

    ContractApproveInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContractApproveInfo record, @Param("example") ContractApproveInfoExample example);

    int updateByExample(@Param("record") ContractApproveInfo record, @Param("example") ContractApproveInfoExample example);

    int updateByPrimaryKeySelective(ContractApproveInfo record);

    int updateByPrimaryKey(ContractApproveInfo record);


    List<HashMap> myApproveInfoList(@Param("userId") Integer userId,
                                    @Param("title") String title,
                                    @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditapproveInfoList(@Param("userId") Integer userId,
                                         @Param("title") String title,
                                         @Param("statusList") List<Short> statusList);
}