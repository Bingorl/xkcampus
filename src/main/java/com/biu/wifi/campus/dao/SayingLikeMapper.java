package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SayingLike;
import com.biu.wifi.campus.dao.model.SayingLikeCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SayingLikeMapper extends CoreDao {
    int countByExample(SayingLikeCriteria example);

    int deleteByExample(SayingLikeCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SayingLike record);

    int insertSelective(SayingLike record);

    List<SayingLike> selectByExample(SayingLikeCriteria example);

    SayingLike selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SayingLike record, @Param("example") SayingLikeCriteria example);

    int updateByExample(@Param("record") SayingLike record, @Param("example") SayingLikeCriteria example);

    int updateByPrimaryKeySelective(SayingLike record);

    int updateByPrimaryKey(SayingLike record);
}