package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Push;
import com.biu.wifi.campus.dao.model.PushCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushMapper extends CoreDao {
    long countByExample(PushCriteria example);

    int deleteByExample(PushCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Push record);

    int insertSelective(Push record);

    List<Push> selectByExample(PushCriteria example);

    Push selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Push record, @Param("example") PushCriteria example);

    int updateByExample(@Param("record") Push record, @Param("example") PushCriteria example);

    int updateByPrimaryKeySelective(Push record);

    int updateByPrimaryKey(Push record);
}