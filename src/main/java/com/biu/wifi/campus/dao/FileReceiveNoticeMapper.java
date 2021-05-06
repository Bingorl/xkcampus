package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.FileReceiveNotice;
import com.biu.wifi.campus.dao.model.FileReceiveNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileReceiveNoticeMapper {
    long countByExample(FileReceiveNoticeExample example);

    int deleteByExample(FileReceiveNoticeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FileReceiveNotice record);

    int insertSelective(FileReceiveNotice record);

    List<FileReceiveNotice> selectByExample(FileReceiveNoticeExample example);

    FileReceiveNotice selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FileReceiveNotice record, @Param("example") FileReceiveNoticeExample example);

    int updateByExample(@Param("record") FileReceiveNotice record, @Param("example") FileReceiveNoticeExample example);

    int updateByPrimaryKeySelective(FileReceiveNotice record);

    int updateByPrimaryKey(FileReceiveNotice record);
}