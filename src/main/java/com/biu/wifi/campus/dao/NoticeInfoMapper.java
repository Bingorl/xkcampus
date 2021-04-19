package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.NoticeInfo;
import com.biu.wifi.campus.dao.model.NoticeInfoCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeInfoMapper {
    long countByExample(NoticeInfoCriteria example);

    int deleteByExample(NoticeInfoCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(NoticeInfo record);

    int insertSelective(NoticeInfo record);

    List<NoticeInfo> selectByExample(NoticeInfoCriteria example);

    NoticeInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NoticeInfo record, @Param("example") NoticeInfoCriteria example);

    int updateByExample(@Param("record") NoticeInfo record, @Param("example") NoticeInfoCriteria example);

    int updateByPrimaryKeySelective(NoticeInfo record);

    int updateByPrimaryKey(NoticeInfo record);
}