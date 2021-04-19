package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.CommentReplyMessage;
import com.biu.wifi.campus.dao.model.CommentReplyMessageCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentReplyMessageMapper extends CoreDao {
    long countByExample(CommentReplyMessageCriteria example);

    int deleteByExample(CommentReplyMessageCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(CommentReplyMessage record);

    int insertSelective(CommentReplyMessage record);

    List<CommentReplyMessage> selectByExample(CommentReplyMessageCriteria example);

    CommentReplyMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CommentReplyMessage record, @Param("example") CommentReplyMessageCriteria example);

    int updateByExample(@Param("record") CommentReplyMessage record, @Param("example") CommentReplyMessageCriteria example);

    int updateByPrimaryKeySelective(CommentReplyMessage record);

    int updateByPrimaryKey(CommentReplyMessage record);
}