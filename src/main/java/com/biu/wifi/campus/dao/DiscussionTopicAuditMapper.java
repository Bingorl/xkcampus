package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.DiscussionTopicAudit;
import com.biu.wifi.campus.dao.model.DiscussionTopicAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 张彬.
 * @date 2021/4/8 12:46.
 */
@Repository
public interface DiscussionTopicAuditMapper {
    long countByExample(DiscussionTopicAuditExample example);

    int deleteByExample(DiscussionTopicAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DiscussionTopicAudit record);

    int insertSelective(DiscussionTopicAudit record);

    List<DiscussionTopicAudit> selectByExample(DiscussionTopicAuditExample example);

    DiscussionTopicAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DiscussionTopicAudit record, @Param("example") DiscussionTopicAuditExample example);

    int updateByExample(@Param("record") DiscussionTopicAudit record, @Param("example") DiscussionTopicAuditExample example);

    int updateByPrimaryKeySelective(DiscussionTopicAudit record);

    int updateByPrimaryKey(DiscussionTopicAudit record);

    HashMap selectMap(@Param("applyId") Integer applyId,@Param("userId") String userId);
}