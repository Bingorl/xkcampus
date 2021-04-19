package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Help;
import com.biu.wifi.campus.dao.model.HelpCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpMapper extends CoreDao {
    long countByExample(HelpCriteria example);

    int deleteByExample(HelpCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Help record);

    int insertSelective(Help record);

    List<Help> selectByExample(HelpCriteria example);

    Help selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Help record, @Param("example") HelpCriteria example);

    int updateByExample(@Param("record") Help record, @Param("example") HelpCriteria example);

    int updateByPrimaryKeySelective(Help record);

    int updateByPrimaryKey(Help record);
}