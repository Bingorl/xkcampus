package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.DiscussionTopicNotice;
import com.biu.wifi.campus.dao.model.DiscussionTopicNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 张彬.
 * @date 2021/4/4 22:32.
 */
@Repository
public interface DiscussionTopicNoticeMapper {
    long countByExample(DiscussionTopicNoticeExample example);

    int deleteByExample(DiscussionTopicNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DiscussionTopicNotice record);

    int insertSelective(DiscussionTopicNotice record);

    List<DiscussionTopicNotice> selectByExample(DiscussionTopicNoticeExample example);

    DiscussionTopicNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DiscussionTopicNotice record, @Param("example") DiscussionTopicNoticeExample example);

    int updateByExample(@Param("record") DiscussionTopicNotice record, @Param("example") DiscussionTopicNoticeExample example);

    int updateByPrimaryKeySelective(DiscussionTopicNotice record);

    int updateByPrimaryKey(DiscussionTopicNotice record);
}