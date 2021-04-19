package com.biu.wifi.campus.dao;

import com.biu.wifi.campus.dao.model.SchoolAddressBook;
import com.biu.wifi.campus.dao.model.SchoolAddressBookCriteria;
import com.biu.wifi.core.base.CoreDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolAddressBookMapper extends CoreDao {
    long countByExample(SchoolAddressBookCriteria example);

    int deleteByExample(SchoolAddressBookCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SchoolAddressBook record);

    int insertSelective(SchoolAddressBook record);

    List<SchoolAddressBook> selectByExample(SchoolAddressBookCriteria example);

    SchoolAddressBook selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SchoolAddressBook record,
                                 @Param("example") SchoolAddressBookCriteria example);

    int updateByExample(@Param("record") SchoolAddressBook record, @Param("example") SchoolAddressBookCriteria example);

    int updateByPrimaryKeySelective(SchoolAddressBook record);

    int updateByPrimaryKey(SchoolAddressBook record);
}