package com.biu.wifi.campus.dao.model;

import com.biu.wifi.campus.dao.model.StampToApplyInfo;
import com.biu.wifi.campus.dao.model.StampToApplyInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StampToApplyInfoMapper {
    long countByExample(StampToApplyInfoExample example);

    int deleteByExample(StampToApplyInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StampToApplyInfo record);

    int insertSelective(StampToApplyInfo record);

    List<StampToApplyInfo> selectByExample(StampToApplyInfoExample example);

    StampToApplyInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StampToApplyInfo record, @Param("example") StampToApplyInfoExample example);

    int updateByExample(@Param("record") StampToApplyInfo record, @Param("example") StampToApplyInfoExample example);

    int updateByPrimaryKeySelective(StampToApplyInfo record);

    int updateByPrimaryKey(StampToApplyInfo record);
}