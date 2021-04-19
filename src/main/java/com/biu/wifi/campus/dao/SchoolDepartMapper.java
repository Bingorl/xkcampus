package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SchoolDepart;
import com.biu.wifi.campus.dao.model.SchoolDepartCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolDepartMapper extends CoreDao {
    long countByExample(SchoolDepartCriteria example);

    int deleteByExample(SchoolDepartCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SchoolDepart record);

    int insertSelective(SchoolDepart record);

    List<SchoolDepart> selectByExample(SchoolDepartCriteria example);

    SchoolDepart selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SchoolDepart record, @Param("example") SchoolDepartCriteria example);

    int updateByExample(@Param("record") SchoolDepart record, @Param("example") SchoolDepartCriteria example);

    int updateByPrimaryKeySelective(SchoolDepart record);

    int updateByPrimaryKey(SchoolDepart record);
}