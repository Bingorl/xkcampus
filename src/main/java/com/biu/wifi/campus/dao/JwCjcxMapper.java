package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.JwCjcx;
import com.biu.wifi.campus.dao.model.JwCjcxCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JwCjcxMapper extends CoreDao {
    long countByExample(JwCjcxCriteria example);

    int deleteByExample(JwCjcxCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(JwCjcx record);

    int insertSelective(JwCjcx record);

    List<JwCjcx> selectByExample(JwCjcxCriteria example);

    JwCjcx selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") JwCjcx record, @Param("example") JwCjcxCriteria example);

    int updateByExample(@Param("record") JwCjcx record, @Param("example") JwCjcxCriteria example);

    int updateByPrimaryKeySelective(JwCjcx record);

    int updateByPrimaryKey(JwCjcx record);
}