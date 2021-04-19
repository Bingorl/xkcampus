package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.LeaveTypeMapper;
import com.biu.wifi.campus.dao.model.LeaveType;
import com.biu.wifi.campus.dao.model.LeaveTypeCriteria;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/10/30.
 */
@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    public LeaveType find(Integer id) {
        LeaveType leaveType = leaveTypeMapper.selectByPrimaryKey(id);
        return leaveType;
    }

    /**
     * 获取请假类型列表
     *
     * @param schoolId 学校ID
     * @return
     */
    public List<LeaveType> findList(Integer schoolId) {
        return findList(schoolId, null);
    }

    /**
     * 获取请假类型列表
     *
     * @param schoolId 学校ID
     * @param name     类型名称关键字
     * @return
     */
    public List<LeaveType> findList(Integer schoolId, String name) {
        LeaveTypeCriteria example = new LeaveTypeCriteria();
        LeaveTypeCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(name)) {
            criteria.andNameLike("%" + name + "%");
        }

        if (schoolId == null) {
            //平台查询
            criteria.andSchoolIdIsNull();
        } else {
            //系统查询
            criteria.andSchoolIdEqualTo(schoolId);
        }
        return leaveTypeMapper.selectByExample(example);
    }

    /**
     * 删除请假类型
     *
     * @param id 请假类型ID
     */
    public void delete(Integer id) {
        LeaveType leaveType = leaveTypeMapper.selectByPrimaryKey(id);
        if (leaveType == null || (leaveType != null && leaveType.getIsDelete() == 1)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该记录已被删除，请勿重复操作");
        }

        leaveType.setIsDelete((short) 1);
        leaveType.setDeleteTime(new Date());
        leaveTypeMapper.updateByPrimaryKeySelective(leaveType);
    }

    /**
     * 新增或编辑
     *
     * @param record
     */
    public void addOrUpdate(LeaveType record) {
        LeaveTypeCriteria example = new LeaveTypeCriteria();
        LeaveTypeCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andNameEqualTo(record.getName());
        if (record.getId() != null) {
            criteria.andIdNotEqualTo(record.getId());
        }

        if (record.getSchoolId() == null) {
            criteria.andSchoolIdIsNull();
        } else {
            criteria.andSchoolIdEqualTo(record.getSchoolId());
        }

        long count = leaveTypeMapper.countByExample(example);
        if (count > 0) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该记录已存在");
        }

        if (record.getId() == null) {
            leaveTypeMapper.insertSelective(record);
        } else {
            leaveTypeMapper.updateByPrimaryKeySelective(record);
        }
    }
}
