package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.FileReceiveAudit;
import com.biu.wifi.campus.dao.model.FileReceiveAuditExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileReceiveAuditMapper {
    long countByExample(FileReceiveAuditExample example);

    int deleteByExample(FileReceiveAuditExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FileReceiveAudit record);

    int insertSelective(FileReceiveAudit record);

    List<FileReceiveAudit> selectByExample(FileReceiveAuditExample example);

    FileReceiveAudit selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FileReceiveAudit record, @Param("example") FileReceiveAuditExample example);

    int updateByExample(@Param("record") FileReceiveAudit record, @Param("example") FileReceiveAuditExample example);

    int updateByPrimaryKeySelective(FileReceiveAudit record);

    int updateByPrimaryKey(FileReceiveAudit record);

    HashMap selectMap(@Param("receiveId")Integer id,@Param("userId") String userId);
}