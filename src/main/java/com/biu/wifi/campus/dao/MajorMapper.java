package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Major;
import com.biu.wifi.campus.dao.model.MajorCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorMapper extends CoreDao {
    int countByExample(MajorCriteria example);

    int deleteByExample(MajorCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Major record);

    int insertSelective(Major record);

    List<Major> selectByExample(MajorCriteria example);

    Major selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Major record, @Param("example") MajorCriteria example);

    int updateByExample(@Param("record") Major record, @Param("example") MajorCriteria example);

    int updateByPrimaryKeySelective(Major record);

    int updateByPrimaryKey(Major record);
}