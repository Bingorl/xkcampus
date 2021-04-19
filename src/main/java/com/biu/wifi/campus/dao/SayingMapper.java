package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Saying;
import com.biu.wifi.campus.dao.model.SayingCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SayingMapper extends CoreDao {
    long countByExample(SayingCriteria example);

    int deleteByExample(SayingCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Saying record);

    int insertSelective(Saying record);

    List<Saying> selectByExampleWithBLOBs(SayingCriteria example);

    List<Saying> selectByExample(SayingCriteria example);

    Saying selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Saying record, @Param("example") SayingCriteria example);

    int updateByExampleWithBLOBs(@Param("record") Saying record, @Param("example") SayingCriteria example);

    int updateByExample(@Param("record") Saying record, @Param("example") SayingCriteria example);

    int updateByPrimaryKeySelective(Saying record);

    int updateByPrimaryKeyWithBLOBs(Saying record);

    int updateByPrimaryKey(Saying record);
}