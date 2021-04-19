package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.UserStuNoInvalid;
import com.biu.wifi.campus.dao.model.UserStuNoInvalidCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStuNoInvalidMapper {
    long countByExample(UserStuNoInvalidCriteria example);

    int deleteByExample(UserStuNoInvalidCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserStuNoInvalid record);

    int insertSelective(UserStuNoInvalid record);

    List<UserStuNoInvalid> selectByExample(UserStuNoInvalidCriteria example);

    UserStuNoInvalid selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserStuNoInvalid record, @Param("example") UserStuNoInvalidCriteria example);

    int updateByExample(@Param("record") UserStuNoInvalid record, @Param("example") UserStuNoInvalidCriteria example);

    int updateByPrimaryKeySelective(UserStuNoInvalid record);

    int updateByPrimaryKey(UserStuNoInvalid record);
}