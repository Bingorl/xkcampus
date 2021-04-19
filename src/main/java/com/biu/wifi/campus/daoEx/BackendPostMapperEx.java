package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.Post;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BackendPostMapperEx {

    public List<Map<String, Object>> findPostList(@Param("schoolId") Integer schoolId, @Param("time") Long time,
                                                  @Param("title") String title,
                                                  @Param("content") String content,
                                                  @Param("studentNumber") String studentNumber,
                                                  @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime,
                                                  @Param("isSelect") Short isSelect,
                                                  @Param("isSpecial") Short isSpecial);

    public List<Map<String, Object>> findPostComments(@Param("postId") Integer postId, @Param("time") Long time);

    public List<Map<String, Object>> findPostReplys(@Param("commentId") Integer commentId);

    public List<Map<String, Object>> findPostSelectListInfo(@Param("schoolId") Integer schoolId);

    int cancelNoticeTopPost(Post record);

    int cancelSelectPost(Post record);
}
