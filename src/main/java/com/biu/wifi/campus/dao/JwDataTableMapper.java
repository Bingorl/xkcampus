package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.JwDataTable;
import com.biu.wifi.campus.dao.model.JwDataTableCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JwDataTableMapper {
    long countByExample(JwDataTableCriteria example);

    int deleteByExample(JwDataTableCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(JwDataTable record);

    int insertSelective(JwDataTable record);

    List<JwDataTable> selectByExample(JwDataTableCriteria example);

    JwDataTable selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") JwDataTable record, @Param("example") JwDataTableCriteria example);

    int updateByExample(@Param("record") JwDataTable record, @Param("example") JwDataTableCriteria example);

    int updateByPrimaryKeySelective(JwDataTable record);

    int updateByPrimaryKey(JwDataTable record);
}