package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Institute;
import com.biu.wifi.campus.dao.model.InstituteCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteMapper extends CoreDao {
    int countByExample(InstituteCriteria example);

    int deleteByExample(InstituteCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Institute record);

    int insertSelective(Institute record);

    List<Institute> selectByExample(InstituteCriteria example);

    Institute selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Institute record, @Param("example") InstituteCriteria example);

    int updateByExample(@Param("record") Institute record, @Param("example") InstituteCriteria example);

    int updateByPrimaryKeySelective(Institute record);

    int updateByPrimaryKey(Institute record);
}