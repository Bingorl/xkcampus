package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.StampToApplyUser;
import com.biu.wifi.campus.dao.model.StampToApplyUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StampToApplyUserMapper {
    long countByExample(StampToApplyUserExample example);

    int deleteByExample(StampToApplyUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StampToApplyUser record);

    int insertSelective(StampToApplyUser record);

    List<StampToApplyUser> selectByExample(StampToApplyUserExample example);

    StampToApplyUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StampToApplyUser record, @Param("example") StampToApplyUserExample example);

    int updateByExample(@Param("record") StampToApplyUser record, @Param("example") StampToApplyUserExample example);

    int updateByPrimaryKeySelective(StampToApplyUser record);

    int updateByPrimaryKey(StampToApplyUser record);
}