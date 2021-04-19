package com.biu.wifi.campus.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SchoolAddressBookMapper;
import com.biu.wifi.campus.dao.SchoolDepartMapper;
import com.biu.wifi.campus.dao.SchoolPositionMapper;
import com.biu.wifi.campus.dao.model.SchoolAddressBookCriteria;
import com.biu.wifi.campus.dao.model.SchoolPosition;
import com.biu.wifi.campus.dao.model.SchoolDepart;
import com.biu.wifi.campus.dao.model.SchoolPositionCriteria;
import com.biu.wifi.campus.result.Result;

/**
 * @author zhangbin
 */
@Service
public class SchoolPositionService {

    @Autowired
    private SchoolAddressBookMapper schoolAddressBookMapper;
    @Autowired
    private SchoolDepartMapper schoolDepartMapper;
    @Autowired
    private SchoolPositionMapper schoolPositionMapper;

    public SchoolPosition getSchoolPosition(Integer id) {
        return schoolPositionMapper.selectByPrimaryKey(id);
    }

    public List<SchoolPosition> getSchoolPositionList(SchoolPositionCriteria example) {
        return schoolPositionMapper.selectByExample(example);
    }

    public Result addOrUpdate(SchoolPosition record) {
        SchoolDepart schoolDepart = schoolDepartMapper.selectByPrimaryKey(record.getDepartId());
        if (schoolDepart == null || (schoolDepart != null && schoolDepart.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "所选部门不存在", null);
        }

        // 防重校验
        SchoolPositionCriteria example = new SchoolPositionCriteria();
        example.createCriteria().andNameEqualTo(record.getName()).andDepartIdEqualTo(record.getDepartId())
                .andIsDeleteEqualTo((short) 2);
        List<SchoolPosition> list = getSchoolPositionList(example);
        SchoolPosition query = null;
        if (CollectionUtils.isNotEmpty(list)) {
            query = list.get(0);
        }
        if (query != null && (record.getId() == null
                || (record.getId() != null && query.getId().intValue() == record.getId().intValue()))) {
            return new Result(Result.CUSTOM_MESSAGE, "该职位已存在", null);
        }

        int result = 0;
        Date now = new Date();
        if (record.getId() == null) {
            record.setCreateTime(now);
            result = schoolPositionMapper.insertSelective(record);
        } else {
            record.setUpdateTime(now);
            result = schoolPositionMapper.updateByPrimaryKeySelective(record);
        }

        if (result > 0) {
            return new Result(Result.SUCCESS, "成功", record);
        } else {
            return new Result(Result.FAILURE, "失败", null);
        }
    }

    public Result delete(Integer id) {
        SchoolPosition query = getSchoolPosition(id);
        if (query == null || (query != null && query.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已删除", null);
        }

        // 以下情况不能删除
        // 职位下有通讯录的
        SchoolAddressBookCriteria schoolAddressBookCriteria = new SchoolAddressBookCriteria();
        schoolAddressBookCriteria.createCriteria().andPositionIdEqualTo(id).andIsDeleteNotEqualTo((short) 2);
        long schoolAddressBookCount = schoolAddressBookMapper.countByExample(schoolAddressBookCriteria);
        if (schoolAddressBookCount > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该部门下有通讯录人员，不能被删除", null);
        }

        SchoolPosition record = new SchoolPosition();
        record.setId(id);
        record.setIsDelete((short) 1);
        return addOrUpdate(record);
    }
}
