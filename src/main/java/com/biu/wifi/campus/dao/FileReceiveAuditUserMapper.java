package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.FileReceiveAuditUser;
import com.biu.wifi.campus.dao.model.FileReceiveAuditUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileReceiveAuditUserMapper {
    long countByExample(FileReceiveAuditUserExample example);

    int deleteByExample(FileReceiveAuditUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FileReceiveAuditUser record);

    int insertSelective(FileReceiveAuditUser record);

    List<FileReceiveAuditUser> selectByExample(FileReceiveAuditUserExample example);

    FileReceiveAuditUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FileReceiveAuditUser record, @Param("example") FileReceiveAuditUserExample example);

    int updateByExample(@Param("record") FileReceiveAuditUser record, @Param("example") FileReceiveAuditUserExample example);

    int updateByPrimaryKeySelective(FileReceiveAuditUser record);

    int updateByPrimaryKey(FileReceiveAuditUser record);
}