package com.biu.wifi.campus.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.GroupTmpCreatePermissionMapper;
import com.biu.wifi.campus.dao.model.GroupTmpCreatePermission;
import com.biu.wifi.campus.dao.model.GroupTmpCreatePermissionCriteria;
import com.biu.wifi.campus.result.Result;

/**
 * 临时群组创建权限接口
 *
 * @author zhangbin
 */
@Service
public class GroupTmpCreatePermissionService {

    @Autowired
    private GroupTmpCreatePermissionMapper groupTmpCreatePermissionMapper;

    public Result addGroupTmpCreatePermission(GroupTmpCreatePermission record) {
        GroupTmpCreatePermissionCriteria example = new GroupTmpCreatePermissionCriteria();
        GroupTmpCreatePermissionCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andSchoolIdEqualTo(record.getSchoolId());
        criteria.andUserTypeEqualTo(record.getUserType());
        criteria.andUserIdEqualTo(record.getUserId());

        long count = groupTmpCreatePermissionMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该用户已经拥有该权限", null);
        }

        record.setCreateTime(new Date());
        int result = groupTmpCreatePermissionMapper.insertSelective(record);
        if (result > 0) {
            return new Result(Result.SUCCESS, "成功", null);
        } else {
            return new Result(Result.FAILURE, "失败", null);
        }
    }

    public Result deleteGroupTmpCreatePermission(Integer schoolId, Integer userType, Integer userId) {
        GroupTmpCreatePermissionCriteria example = new GroupTmpCreatePermissionCriteria();
        GroupTmpCreatePermissionCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andUserTypeEqualTo(userType);
        criteria.andUserIdEqualTo(userId);
        List<GroupTmpCreatePermission> list = groupTmpCreatePermissionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return new Result(Result.CUSTOM_MESSAGE, "该用户没有该权限", null);
        }

        boolean success = true;
        for (GroupTmpCreatePermission permission : list) {
            permission.setIsDelete((short) 1);
            permission.setDeleteTime(new Date());
            try {
                groupTmpCreatePermissionMapper.updateByPrimaryKeySelective(permission);
            } catch (Exception e) {
                success = false;
                break;
            }
        }
        if (success) {
            return new Result(Result.SUCCESS, "成功", null);
        } else {
            return new Result(Result.FAILURE, "失败", null);
        }
    }

    public boolean getGroupTmpCreatePermission(Integer schoolId, Integer userType, Integer userId) {
        GroupTmpCreatePermissionCriteria example = new GroupTmpCreatePermissionCriteria();
        GroupTmpCreatePermissionCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andSchoolIdEqualTo(schoolId);
        criteria.andUserTypeEqualTo(userType);
        criteria.andUserIdEqualTo(userId);
        long count = groupTmpCreatePermissionMapper.countByExample(example);
        return count > 0 ? true : false;
    }

}
