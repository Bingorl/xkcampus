package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.RepairCostNotice;
import com.biu.wifi.campus.dao.model.RepairCostNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairCostNoticeMapper {
    long countByExample(RepairCostNoticeExample example);

    int deleteByExample(RepairCostNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RepairCostNotice record);

    int insertSelective(RepairCostNotice record);

    List<RepairCostNotice> selectByExample(RepairCostNoticeExample example);

    RepairCostNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RepairCostNotice record, @Param("example") RepairCostNoticeExample example);

    int updateByExample(@Param("record") RepairCostNotice record, @Param("example") RepairCostNoticeExample example);

    int updateByPrimaryKeySelective(RepairCostNotice record);

    int updateByPrimaryKey(RepairCostNotice record);
}