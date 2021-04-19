package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Collection;
import com.biu.wifi.campus.dao.model.CollectionCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionMapper extends CoreDao {
    int countByExample(CollectionCriteria example);

    int deleteByExample(CollectionCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Collection record);

    int insertSelective(Collection record);

    List<Collection> selectByExample(CollectionCriteria example);

    Collection selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Collection record, @Param("example") CollectionCriteria example);

    int updateByExample(@Param("record") Collection record, @Param("example") CollectionCriteria example);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);
}