package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.StampToApplyNotice;
import com.biu.wifi.campus.dao.model.StampToApplyNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StampToApplyNoticeMapper {
    long countByExample(StampToApplyNoticeExample example);

    int deleteByExample(StampToApplyNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StampToApplyNotice record);

    int insertSelective(StampToApplyNotice record);

    List<StampToApplyNotice> selectByExample(StampToApplyNoticeExample example);

    StampToApplyNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StampToApplyNotice record, @Param("example") StampToApplyNoticeExample example);

    int updateByExample(@Param("record") StampToApplyNotice record, @Param("example") StampToApplyNoticeExample example);

    int updateByPrimaryKeySelective(StampToApplyNotice record);

    int updateByPrimaryKey(StampToApplyNotice record);
}