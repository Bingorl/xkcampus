package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.OfficialWebsiteNotice;
import com.biu.wifi.campus.dao.model.OfficialWebsiteNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialWebsiteNoticeMapper {
    long countByExample(OfficialWebsiteNoticeExample example);

    int deleteByExample(OfficialWebsiteNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OfficialWebsiteNotice record);

    int insertSelective(OfficialWebsiteNotice record);

    List<OfficialWebsiteNotice> selectByExample(OfficialWebsiteNoticeExample example);

    OfficialWebsiteNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OfficialWebsiteNotice record, @Param("example") OfficialWebsiteNoticeExample example);

    int updateByExample(@Param("record") OfficialWebsiteNotice record, @Param("example") OfficialWebsiteNoticeExample example);

    int updateByPrimaryKeySelective(OfficialWebsiteNotice record);

    int updateByPrimaryKey(OfficialWebsiteNotice record);
}