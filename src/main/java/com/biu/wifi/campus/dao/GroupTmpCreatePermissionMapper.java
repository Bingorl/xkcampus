package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.GroupTmpCreatePermission;
import com.biu.wifi.campus.dao.model.GroupTmpCreatePermissionCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupTmpCreatePermissionMapper extends CoreDao {
    long countByExample(GroupTmpCreatePermissionCriteria example);

    int deleteByExample(GroupTmpCreatePermissionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(GroupTmpCreatePermission record);

    int insertSelective(GroupTmpCreatePermission record);

    List<GroupTmpCreatePermission> selectByExample(GroupTmpCreatePermissionCriteria example);

    GroupTmpCreatePermission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GroupTmpCreatePermission record, @Param("example") GroupTmpCreatePermissionCriteria example);

    int updateByExample(@Param("record") GroupTmpCreatePermission record, @Param("example") GroupTmpCreatePermissionCriteria example);

    int updateByPrimaryKeySelective(GroupTmpCreatePermission record);

    int updateByPrimaryKey(GroupTmpCreatePermission record);
}