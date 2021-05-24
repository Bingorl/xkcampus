package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.AssertsUseNotice;
import com.biu.wifi.campus.dao.model.AssertsUseNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssertsUseNoticeMapper {
    long countByExample(AssertsUseNoticeExample example);

    int deleteByExample(AssertsUseNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AssertsUseNotice record);

    int insertSelective(AssertsUseNotice record);

    List<AssertsUseNotice> selectByExample(AssertsUseNoticeExample example);

    AssertsUseNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AssertsUseNotice record, @Param("example") AssertsUseNoticeExample example);

    int updateByExample(@Param("record") AssertsUseNotice record, @Param("example") AssertsUseNoticeExample example);

    int updateByPrimaryKeySelective(AssertsUseNotice record);

    int updateByPrimaryKey(AssertsUseNotice record);
}