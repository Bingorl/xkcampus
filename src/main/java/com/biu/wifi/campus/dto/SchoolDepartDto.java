package com.biu.wifi.campus.dto;

import java.util.ArrayList;
import java.util.List;

import com.biu.wifi.campus.dao.model.SchoolAddressBook;
import com.biu.wifi.campus.dao.model.SchoolDepart;

/**
 * @author zhangbin
 */
public class SchoolDepartDto extends SchoolDepart {

    /**
     * 部门列表
     */
    private List<SchoolDepart> schoolDepartList = new ArrayList<>();
    /**
     * 通讯录人员列表
     */
    private List<SchoolAddressBook> schoolAddressBookList = new ArrayList<>();

    public List<SchoolDepart> getSchoolDepartList() {
        return schoolDepartList;
    }

    public void setSchoolDepartList(List<SchoolDepart> schoolDepartList) {
        this.schoolDepartList = schoolDepartList;
    }

    public List<SchoolAddressBook> getSchoolAddressBookList() {
        return schoolAddressBookList;
    }

    public void setSchoolAddressBookList(List<SchoolAddressBook> schoolAddressBookList) {
        this.schoolAddressBookList = schoolAddressBookList;
    }
}
