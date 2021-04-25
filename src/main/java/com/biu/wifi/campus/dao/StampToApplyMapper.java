package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.StampToApply;
import com.biu.wifi.campus.dao.model.StampToApplyExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StampToApplyMapper {
    long countByExample(StampToApplyExample example);

    int deleteByExample(StampToApplyExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StampToApply record);

    int insertSelective(StampToApply record);

    List<StampToApply> selectByExample(StampToApplyExample example);

    StampToApply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StampToApply record, @Param("example") StampToApplyExample example);

    int updateByExample(@Param("record") StampToApply record, @Param("example") StampToApplyExample example);

    int updateByPrimaryKeySelective(StampToApply record);

    int updateByPrimaryKey(StampToApply record);

    HashMap findMap(@Param("applyId") Integer id, @Param("userId") String userId);
}