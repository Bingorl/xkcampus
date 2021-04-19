package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.PostLike;
import com.biu.wifi.campus.dao.model.PostLikeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeMapper extends CoreDao {
    int countByExample(PostLikeCriteria example);

    int deleteByExample(PostLikeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(PostLike record);

    int insertSelective(PostLike record);

    List<PostLike> selectByExample(PostLikeCriteria example);

    PostLike selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PostLike record, @Param("example") PostLikeCriteria example);

    int updateByExample(@Param("record") PostLike record, @Param("example") PostLikeCriteria example);

    int updateByPrimaryKeySelective(PostLike record);

    int updateByPrimaryKey(PostLike record);
}