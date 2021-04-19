package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SignIn;
import com.biu.wifi.campus.dao.model.SignInExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author 张彬.
 * @date 2021/4/9 16:36.
 */
@Repository
public interface SignInMapper {
    long countByExample(SignInExample example);

    int deleteByExample(SignInExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SignIn record);

    int insertSelective(SignIn record);

    List<SignIn> selectByExample(SignInExample example);

    SignIn selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SignIn record, @Param("example") SignInExample example);

    int updateByExample(@Param("record") SignIn record, @Param("example") SignInExample example);

    int updateByPrimaryKeySelective(SignIn record);

    int updateByPrimaryKey(SignIn record);

    List<SignIn> mySignInList(@Param("userId") Integer userId, @Param("type") Integer type);

    List<HashMap> search(@Param("schoolId") Integer schoolId,
                         @Param("type") Integer type,
                         @Param("startTime") String startTime,
                         @Param("endTime") String endTime,
                         @Param("keyword") String keyword);
}