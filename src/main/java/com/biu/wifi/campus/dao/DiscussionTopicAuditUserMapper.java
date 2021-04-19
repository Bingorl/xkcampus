package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.DiscussionTopicAuditUser;
import com.biu.wifi.campus.dao.model.DiscussionTopicAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author 张彬.
 * @date 2021/4/8 12:18.
 */
@Repository
public interface DiscussionTopicAuditUserMapper {
    long countByExample(DiscussionTopicAuditUserExample example);

    int deleteByExample(DiscussionTopicAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DiscussionTopicAuditUser record);

    int insertSelective(DiscussionTopicAuditUser record);

    List<DiscussionTopicAuditUser> selectByExample(DiscussionTopicAuditUserExample example);

    DiscussionTopicAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DiscussionTopicAuditUser record, @Param("example") DiscussionTopicAuditUserExample example);

    int updateByExample(@Param("record") DiscussionTopicAuditUser record, @Param("example") DiscussionTopicAuditUserExample example);

    int updateByPrimaryKeySelective(DiscussionTopicAuditUser record);

    int updateByPrimaryKey(DiscussionTopicAuditUser record);
}