package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.FileReceiveAuditInfo;
import com.biu.wifi.campus.dao.model.FileReceiveAuditInfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileReceiveAuditInfoMapper {
    long countByExample(FileReceiveAuditInfoExample example);

    int deleteByExample(FileReceiveAuditInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FileReceiveAuditInfo record);

    int insertSelective(FileReceiveAuditInfo record);

    List<FileReceiveAuditInfo> selectByExample(FileReceiveAuditInfoExample example);

    FileReceiveAuditInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FileReceiveAuditInfo record, @Param("example") FileReceiveAuditInfoExample example);

    int updateByExample(@Param("record") FileReceiveAuditInfo record, @Param("example") FileReceiveAuditInfoExample example);

    int updateByPrimaryKeySelective(FileReceiveAuditInfo record);

    int updateByPrimaryKey(FileReceiveAuditInfo record);

    List<HashMap> myStampApplyInfoList(
            @Param("userId") Integer userId,
            @Param("referenceNum") String referenceNum,
            @Param("title") String title,
            @Param("statusList") List<Short> statusList);

    List<HashMap> myAuditLeaveInfoList(@Param("userId") Integer userId,
                                       @Param("referenceNum") String referenceNum,
                                       @Param("title") String title,
                                       @Param("statusList") List<Short> statusList);
}