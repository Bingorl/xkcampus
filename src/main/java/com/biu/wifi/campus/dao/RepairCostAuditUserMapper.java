package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.RepairCostAuditUser;
import com.biu.wifi.campus.dao.model.RepairCostAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairCostAuditUserMapper {
    long countByExample(RepairCostAuditUserExample example);

    int deleteByExample(RepairCostAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RepairCostAuditUser record);

    int insertSelective(RepairCostAuditUser record);

    List<RepairCostAuditUser> selectByExample(RepairCostAuditUserExample example);

    RepairCostAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RepairCostAuditUser record, @Param("example") RepairCostAuditUserExample example);

    int updateByExample(@Param("record") RepairCostAuditUser record, @Param("example") RepairCostAuditUserExample example);

    int updateByPrimaryKeySelective(RepairCostAuditUser record);

    int updateByPrimaryKey(RepairCostAuditUser record);
}