package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.ClassroomBuilding;
import com.biu.wifi.campus.dao.model.ClassroomBuildingCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomBuildingMapper extends CoreDao {
    long countByExample(ClassroomBuildingCriteria example);

    int deleteByExample(ClassroomBuildingCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomBuilding record);

    int insertSelective(ClassroomBuilding record);

    List<ClassroomBuilding> selectByExampleWithBLOBs(ClassroomBuildingCriteria example);

    List<ClassroomBuilding> selectByExample(ClassroomBuildingCriteria example);

    ClassroomBuilding selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ClassroomBuilding record, @Param("example") ClassroomBuildingCriteria example);

    int updateByExampleWithBLOBs(@Param("record") ClassroomBuilding record, @Param("example") ClassroomBuildingCriteria example);

    int updateByExample(@Param("record") ClassroomBuilding record, @Param("example") ClassroomBuildingCriteria example);

    int updateByPrimaryKeySelective(ClassroomBuilding record);

    int updateByPrimaryKeyWithBLOBs(ClassroomBuilding record);

    int updateByPrimaryKey(ClassroomBuilding record);
}