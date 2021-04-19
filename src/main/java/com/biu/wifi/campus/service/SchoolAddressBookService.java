package com.biu.wifi.campus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SchoolAddressBookMapper;
import com.biu.wifi.campus.dao.SchoolDepartMapper;
import com.biu.wifi.campus.dao.SchoolPositionMapper;
import com.biu.wifi.campus.dao.model.SchoolAddressBook;
import com.biu.wifi.campus.dao.model.SchoolAddressBookCriteria;
import com.biu.wifi.campus.dao.model.SchoolDepart;
import com.biu.wifi.campus.result.Result;

/**
 * @author zhangbin
 */
@Service
public class SchoolAddressBookService {

    @Autowired
    private SchoolAddressBookMapper schoolAddressBookMapper;
    @Autowired
    private SchoolDepartMapper schoolDepartMapper;
    @Autowired
    private SchoolPositionMapper schoolPositionMapper;

    public SchoolAddressBook getSchoolAddressBook(Integer id) {
        return schoolAddressBookMapper.selectByPrimaryKey(id);
    }

    public List<SchoolAddressBook> getSchoolAddressBookList(SchoolAddressBookCriteria example) {
        return schoolAddressBookMapper.selectByExample(example);
    }

    public List<Map<String, Object>> getSchoolAddressBookMapList(SchoolAddressBookCriteria example) {
        List<Map<String, Object>> maps = new ArrayList<>();
        List<SchoolAddressBook> schoolAddressBooks = schoolAddressBookMapper.selectByExample(example);
        for (SchoolAddressBook book : schoolAddressBooks) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", book.getId());
            map.put("name", book.getName());
            map.put("phone", book.getPhone());
            map.put("tel", book.getTel());
            map.put("headImg", book.getHeadImg());
            map.put("schoolId", book.getSchoolId());
            map.put("positionName", book.getPositionName());
            map.put("officePosition", book.getOfficePosition());

            SchoolDepart depart = schoolDepartMapper.selectByPrimaryKey(book.getDepartId());
            if (depart.getPid() == 0) {
                map.put("departId", depart.getId());
                map.put("departName", depart.getName());
                map.put("childDepartId", "");
                map.put("childDepartName", "");
            } else {
                SchoolDepart parent = schoolDepartMapper.selectByPrimaryKey(depart.getPid());
                map.put("departId", parent.getId());
                map.put("departName", parent.getName());
                map.put("childDepartId", depart.getId());
                map.put("childDepartName", depart.getName());
            }

            maps.add(map);
        }
        return maps;
    }

    public Result addOrUpdate(SchoolAddressBook record) {
        SchoolDepart schoolDepart = schoolDepartMapper.selectByPrimaryKey(record.getDepartId());
        if (schoolDepart == null || (schoolDepart != null && schoolDepart.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "所选部门不存在", null);
        }

        // 设置学校
        record.setSchoolId(schoolDepart.getSchoolId());
        if (schoolDepart.getPid() == 0) {
            record.setDepartName(schoolDepart.getName());
        } else {
            SchoolDepart parent = schoolDepartMapper.selectByPrimaryKey(record.getDepartId());
            if (parent != null) {
                record.setDepartName(parent.getName() + "-" + schoolDepart.getName());
            }
        }

        /*
         * SchoolPosition schoolPosition =
         * schoolPositionMapper.selectByPrimaryKey(record.getPositionId()); if
         * (schoolPosition == null || (schoolPosition != null &&
         * schoolPosition.getIsDelete() == 1)) { return new
         * Result(Result.CUSTOM_MESSAGE, "所选职位不存在", null); }
         */

        // 防重校验
        SchoolAddressBookCriteria example = new SchoolAddressBookCriteria();
        example.createCriteria().andNameEqualTo(record.getName()).andDepartIdEqualTo(record.getDepartId())
                .andIsDeleteEqualTo((short) 2);
        List<SchoolAddressBook> list = getSchoolAddressBookList(example);
        SchoolAddressBook query = null;
        if (CollectionUtils.isNotEmpty(list)) {
            query = list.get(0);
        }
        if (query != null && (record.getId() == null
                || (record.getId() != null && query.getId().intValue() != record.getId().intValue()))) {
            return new Result(Result.CUSTOM_MESSAGE, "该人员的通讯录已存在", null);
        }

        int result;
        Date now = new Date();
        if (record.getId() == null) {
            record.setCreateTime(now);
            result = schoolAddressBookMapper.insertSelective(record);
        } else {
            record.setUpdateTime(now);
            result = schoolAddressBookMapper.updateByPrimaryKeySelective(record);
        }

        if (result > 0) {
            return new Result(Result.SUCCESS, "成功", record);
        } else {
            return new Result(Result.FAILURE, "失败", null);
        }
    }

    public Result delete(Integer id) {
        SchoolAddressBook query = getSchoolAddressBook(id);
        if (query == null || (query != null && query.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已删除", null);
        }

        SchoolAddressBook record = new SchoolAddressBook();
        record.setId(id);
        record.setUpdateTime(new Date());
        record.setIsDelete((short) 1);

        int count = schoolAddressBookMapper.updateByPrimaryKeySelective(record);
        if (count > 0) {
            return new Result(Result.SUCCESS, "成功", null);
        } else {
            return new Result(Result.FAILURE, "失败", null);
        }
    }

    public List<SchoolAddressBook> hideTel(List<SchoolAddressBook> list) {
        for (SchoolAddressBook addressBook : list) {
            addressBook = hideTel(addressBook);
        }
        return list;
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param list
     * @return
     */
    public SchoolAddressBook hideTel(SchoolAddressBook addressBook) {
        StringBuffer sb = new StringBuffer();
        String tel = "";
        int len = 0;

        tel = addressBook.getTel();
        if (StringUtils.isNotBlank(tel)) {
            len = tel.length();
            sb.append(tel.substring(0, 3));
            sb.append("****");
            sb.append(tel.substring(len - 4, len));
            addressBook.setTel(sb.toString());
        }
        return addressBook;
    }
}
