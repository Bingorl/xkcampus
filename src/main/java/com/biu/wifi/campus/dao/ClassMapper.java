package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Class;
import com.biu.wifi.campus.dao.model.ClassCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassMapper extends CoreDao {
    int countByExample(ClassCriteria example);

    int deleteByExample(ClassCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Class record);

    int insertSelective(Class record);

    List<Class> selectByExample(ClassCriteria example);

    Class selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Class record, @Param("example") ClassCriteria example);

    int updateByExample(@Param("record") Class record, @Param("example") ClassCriteria example);

    int updateByPrimaryKeySelective(Class record);

    int updateByPrimaryKey(Class record);
}