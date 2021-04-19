package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.Post;
import com.biu.wifi.campus.dao.model.PostCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostMapper extends CoreDao {
    int countByExample(PostCriteria example);

    int deleteByExample(PostCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    List<Post> selectByExampleWithBLOBs(PostCriteria example);

    List<Post> selectByExample(PostCriteria example);

    Post selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Post record, @Param("example") PostCriteria example);

    int updateByExampleWithBLOBs(@Param("record") Post record, @Param("example") PostCriteria example);

    int updateByExample(@Param("record") Post record, @Param("example") PostCriteria example);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKeyWithBLOBs(Post record);

    int updateByPrimaryKey(Post record);
}