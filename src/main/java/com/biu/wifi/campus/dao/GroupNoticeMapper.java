package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.GroupNotice;
import com.biu.wifi.campus.dao.model.GroupNoticeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupNoticeMapper extends CoreDao {
    long countByExample(GroupNoticeCriteria example);

    int deleteByExample(GroupNoticeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(GroupNotice record);

    int insertSelective(GroupNotice record);

    List<GroupNotice> selectByExample(GroupNoticeCriteria example);

    GroupNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GroupNotice record, @Param("example") GroupNoticeCriteria example);

    int updateByExample(@Param("record") GroupNotice record, @Param("example") GroupNoticeCriteria example);

    int updateByPrimaryKeySelective(GroupNotice record);

    int updateByPrimaryKey(GroupNotice record);
}