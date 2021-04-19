package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.PageApply;
import com.biu.wifi.campus.dao.model.PageApplyCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageApplyMapper extends CoreDao {
    long countByExample(PageApplyCriteria example);

    int deleteByExample(PageApplyCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(PageApply record);

    int insertSelective(PageApply record);

    List<PageApply> selectByExample(PageApplyCriteria example);

    PageApply selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PageApply record, @Param("example") PageApplyCriteria example);

    int updateByExample(@Param("record") PageApply record, @Param("example") PageApplyCriteria example);

    int updateByPrimaryKeySelective(PageApply record);

    int updateByPrimaryKey(PageApply record);
}