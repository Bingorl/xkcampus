package com.biu.wifi.campus.daoEx;

import com.biu.wifi.campus.dao.model.StudentGpa;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/11/15.
 */
@Repository
public interface StudentGpaMapperEx {

    int batchInsert(@Param("list") List<StudentGpa> list);
}
