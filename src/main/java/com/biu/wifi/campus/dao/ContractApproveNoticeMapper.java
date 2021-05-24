package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ContractApproveNotice;
import com.biu.wifi.campus.dao.model.ContractApproveNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractApproveNoticeMapper {
    long countByExample(ContractApproveNoticeExample example);

    int deleteByExample(ContractApproveNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ContractApproveNotice record);

    int insertSelective(ContractApproveNotice record);

    List<ContractApproveNotice> selectByExample(ContractApproveNoticeExample example);

    ContractApproveNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ContractApproveNotice record, @Param("example") ContractApproveNoticeExample example);

    int updateByExample(@Param("record") ContractApproveNotice record, @Param("example") ContractApproveNoticeExample example);

    int updateByPrimaryKeySelective(ContractApproveNotice record);

    int updateByPrimaryKey(ContractApproveNotice record);
}