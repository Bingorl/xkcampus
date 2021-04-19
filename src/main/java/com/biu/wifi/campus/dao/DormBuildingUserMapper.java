package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.DormBuildingUser;
import com.biu.wifi.campus.dao.model.DormBuildingUserCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormBuildingUserMapper extends CoreDao {
    long countByExample(DormBuildingUserCriteria example);

    int deleteByExample(DormBuildingUserCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(DormBuildingUser record);

    int insertSelective(DormBuildingUser record);

    List<DormBuildingUser> selectByExample(DormBuildingUserCriteria example);

    DormBuildingUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DormBuildingUser record, @Param("example") DormBuildingUserCriteria example);

    int updateByExample(@Param("record") DormBuildingUser record, @Param("example") DormBuildingUserCriteria example);

    int updateByPrimaryKeySelective(DormBuildingUser record);

    int updateByPrimaryKey(DormBuildingUser record);
}