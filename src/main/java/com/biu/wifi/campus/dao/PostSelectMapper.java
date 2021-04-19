package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.PostSelect;
import com.biu.wifi.campus.dao.model.PostSelectCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostSelectMapper extends CoreDao {
    long countByExample(PostSelectCriteria example);

    int deleteByExample(PostSelectCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(PostSelect record);

    int insertSelective(PostSelect record);

    List<PostSelect> selectByExample(PostSelectCriteria example);

    PostSelect selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PostSelect record, @Param("example") PostSelectCriteria example);

    int updateByExample(@Param("record") PostSelect record, @Param("example") PostSelectCriteria example);

    int updateByPrimaryKeySelective(PostSelect record);

    int updateByPrimaryKey(PostSelect record);
}