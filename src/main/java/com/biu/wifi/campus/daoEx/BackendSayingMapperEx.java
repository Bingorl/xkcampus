package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BackendSayingMapperEx {

    public List<Map<String, Object>> findSayingList(@Param("creatorId") Integer creatorId, @Param("content") String content, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("time") Long time, @Param("creatorName") String creatorName);

    public List<Map<String, Object>> findSayingComments(@Param("sayingId") Integer sayingId, @Param("time") Long time);

    public List<Map<String, Object>> findSayingReplys(@Param("commentId") Integer commentId);
}
