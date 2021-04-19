package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.NoticeAttachment;
import com.biu.wifi.campus.dao.model.NoticeAttachmentCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeAttachmentMapper extends CoreDao {
    long countByExample(NoticeAttachmentCriteria example);

    int deleteByExample(NoticeAttachmentCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(NoticeAttachment record);

    int insertSelective(NoticeAttachment record);

    List<NoticeAttachment> selectByExample(NoticeAttachmentCriteria example);

    NoticeAttachment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") NoticeAttachment record, @Param("example") NoticeAttachmentCriteria example);

    int updateByExample(@Param("record") NoticeAttachment record, @Param("example") NoticeAttachmentCriteria example);

    int updateByPrimaryKeySelective(NoticeAttachment record);

    int updateByPrimaryKey(NoticeAttachment record);
}