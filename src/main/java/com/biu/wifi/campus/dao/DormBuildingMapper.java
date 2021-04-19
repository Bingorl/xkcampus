package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.DormBuilding;
import com.biu.wifi.campus.dao.model.DormBuildingCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormBuildingMapper {
    long countByExample(DormBuildingCriteria example);

    int deleteByExample(DormBuildingCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(DormBuilding record);

    int insertSelective(DormBuilding record);

    List<DormBuilding> selectByExample(DormBuildingCriteria example);

    DormBuilding selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DormBuilding record, @Param("example") DormBuildingCriteria example);

    int updateByExample(@Param("record") DormBuilding record, @Param("example") DormBuildingCriteria example);

    int updateByPrimaryKeySelective(DormBuilding record);

    int updateByPrimaryKey(DormBuilding record);
}