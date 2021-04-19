package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.UserAuth;
import com.biu.wifi.campus.dao.model.UserAuthCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthMapper extends CoreDao {
    long countByExample(UserAuthCriteria example);

    int deleteByExample(UserAuthCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserAuth record);

    int insertSelective(UserAuth record);

    List<UserAuth> selectByExample(UserAuthCriteria example);

    UserAuth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserAuth record, @Param("example") UserAuthCriteria example);

    int updateByExample(@Param("record") UserAuth record, @Param("example") UserAuthCriteria example);

    int updateByPrimaryKeySelective(UserAuth record);

    int updateByPrimaryKey(UserAuth record);
}