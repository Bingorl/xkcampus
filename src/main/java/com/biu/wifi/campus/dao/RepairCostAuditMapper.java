package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.RepairCostAudit;
import com.biu.wifi.campus.dao.model.RepairCostAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairCostAuditMapper {
    long countByExample(RepairCostAuditExample example);

    int deleteByExample(RepairCostAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RepairCostAudit record);

    int insertSelective(RepairCostAudit record);

    List<RepairCostAudit> selectByExample(RepairCostAuditExample example);

    RepairCostAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RepairCostAudit record, @Param("example") RepairCostAuditExample example);

    int updateByExample(@Param("record") RepairCostAudit record, @Param("example") RepairCostAuditExample example);

    int updateByPrimaryKeySelective(RepairCostAudit record);

    int updateByPrimaryKey(RepairCostAudit record);

    HashMap selectMap(@Param("repairId") Integer id, @Param("userId") String userId);
}