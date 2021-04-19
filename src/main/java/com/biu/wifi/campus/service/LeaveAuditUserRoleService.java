package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.DormBuildingMapper;
import com.biu.wifi.campus.dao.GradeMapper;
import com.biu.wifi.campus.dao.LeaveAuditUserAuthMapper;
import com.biu.wifi.campus.dao.LeaveAuditUserRoleMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.BackendUserMapperEx;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/10/30.
 */
@Service
public class LeaveAuditUserRoleService {

    @Autowired
    private LeaveAuditUserRoleMapper leaveAuditUserRoleMapper;
    @Autowired
    private BackendUserMapperEx userMapperEx;
    @Autowired
    private LeaveAuditUserAuthMapper leaveAuditUserAuthMapper;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private DormBuildingMapper dormBuildingMapper;

    public List<LeaveAuditUserRole> findList(Integer schoolId, Integer pid) {
        LeaveAuditUserRoleCriteria example = new LeaveAuditUserRoleCriteria();
        LeaveAuditUserRoleCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId);
        if (pid != null) {
            criteria.andPidEqualTo(pid);
        } else {
            criteria.andPidIsNull();
        }
        return leaveAuditUserRoleMapper.selectByExample(example);
    }

    public List<Map<String, Object>> findMapList(Integer schoolId, Integer pid) {
        List<LeaveAuditUserRole> roles = findList(schoolId, pid);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        LeaveAuditUserRole auditUserRole;
        for (LeaveAuditUserRole role : roles) {
            map = new HashedMap();
            map.put("id", role.getId());
            map.put("pid", role.getPid());
            auditUserRole = findById(role.getPid());
            map.put("pName", auditUserRole != null ? auditUserRole.getName() : "");
            map.put("schoolId", role.getSchoolId());
            map.put("name", role.getName());
            map.put("allowCreateAuditUser", role.getAllowCreateAuditUser());
            list.add(map);
        }
        return list;
    }

    public LeaveAuditUserRole findById(Integer id) {
        LeaveAuditUserRole role = leaveAuditUserRoleMapper.selectByPrimaryKey(id);
        if (role == null || (role != null && role.getIsDelete() == 1)) {
            return null;
        } else {
            return role;
        }
    }

    /**
     * 身份认证后的具体身份名称
     *
     * @param userId
     * @param roleId
     * @param roleName
     * @return
     */
    public String showUserAuthRole(Integer userId, Integer roleId, String roleName) {
        StringBuffer sb = new StringBuffer();
        LeaveAuditUserAuthCriteria leaveAuditUserAuthCriteriaEx = new LeaveAuditUserAuthCriteria();
        leaveAuditUserAuthCriteriaEx.setOrderByClause("create_time desc");
        leaveAuditUserAuthCriteriaEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId)
                .andRoleIdEqualTo(roleId);
        List<LeaveAuditUserAuth> leaveAuditUserAuthList = leaveAuditUserAuthMapper.selectByExample(leaveAuditUserAuthCriteriaEx);
        if (CollectionUtils.isNotEmpty(leaveAuditUserAuthList)) {
            LeaveAuditUserAuth leaveAuditUserAuth = leaveAuditUserAuthList.get(0);
            //辅导员或教师
            if (leaveAuditUserAuth.getGradeId() != null) {
                Grade grade = gradeMapper.selectByPrimaryKey(leaveAuditUserAuth.getGradeId());
                if (grade != null) {
                    sb.append(grade.getName())
                            .append("级")
                            .append(roleName);
                    return sb.toString();
                }
            }

            //宿管
            if (leaveAuditUserAuth.getBuildingId() != null) {
                DormBuilding dormBuilding = dormBuildingMapper.selectByPrimaryKey(leaveAuditUserAuth.getBuildingId());
                if (dormBuilding != null) {
                    sb.append(dormBuilding.getAreaPosition())
                            .append(dormBuilding.getNo())
                            .append(dormBuilding.getUnitNo())
                            .append("的")
                            .append(roleName);
                    return sb.toString();
                }
            }

            return roleName;
        } else {
            return null;
        }
    }

    public Result delete(Integer id) {
        LeaveAuditUserRoleCriteria example = new LeaveAuditUserRoleCriteria();
        example.createCriteria().andIsDeleteEqualTo((short) 2).andIdEqualTo(id);
        List<LeaveAuditUserRole> roles = leaveAuditUserRoleMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(roles)) {
            LeaveAuditUserRole role = roles.get(0);
            //子部门个数
            example = new LeaveAuditUserRoleCriteria();
            example.createCriteria().andIsDeleteEqualTo((short) 2).andPidEqualTo(id);
            long count = leaveAuditUserRoleMapper.countByExample(example);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "包含下级身份类型，不能被删除", null);
            }

            //判断是否有人认证此类型的身份
            LeaveAuditUserAuthCriteria auditUserAuthEx = new LeaveAuditUserAuthCriteria();
            auditUserAuthEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andRoleIdEqualTo(role.getId());
            count = leaveAuditUserAuthMapper.countByExample(auditUserAuthEx);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "有用户认证了该身份类型，不能被删除");
            }

            role.setIsDelete((short) 1);
            role.setDeleteTime(new Date());
            leaveAuditUserRoleMapper.updateByPrimaryKeySelective(role);
            return new Result(Result.SUCCESS, "成功", null);
        } else {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已被删除", null);
        }
    }

    public Result addOrUpdateLeaveAuditUserRole(Integer id, Integer schoolId, Integer pid, String name, Short allowCreateAuditUser) {
        LeaveAuditUserRoleCriteria example = new LeaveAuditUserRoleCriteria();
        LeaveAuditUserRoleCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId).andNameEqualTo(name);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        if (pid != null) {
            criteria.andPidEqualTo(pid);
        }

        long count = leaveAuditUserRoleMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已存在，请勿重复添加", null);
        }

        LeaveAuditUserRole role;
        if (id != null) {
            role = leaveAuditUserRoleMapper.selectByPrimaryKey(id);
            role.setPid(pid);
            role.setSchoolId(schoolId);
            role.setName(name);
            role.setAllowCreateAuditUser(allowCreateAuditUser);
            role.setUpdateTime(new Date());
            leaveAuditUserRoleMapper.updateByPrimaryKeySelective(role);
        } else {
            role = new LeaveAuditUserRole();
            role.setPid(pid);
            role.setSchoolId(schoolId);
            role.setName(name);
            role.setAllowCreateAuditUser(allowCreateAuditUser);
            role.setCreateTime(new Date());
            leaveAuditUserRoleMapper.insertSelective(role);
        }
        return new Result(Result.SUCCESS, "成功", null);
    }

    public List<Map<String, Object>> findUserListByAuditUserRoleId(Integer schoolId, Integer instituteId, Integer majorId,
                                                                   Integer classId, Integer gradeId, Integer roleId, String name) {
        //根据用户姓名搜索满足条件的用户
        List<Map<String, Object>> userList = userMapperEx.findUserListByCondition(schoolId, name, null, instituteId, majorId, classId, gradeId);
        //筛选出身份认证是宿管人员的用户集合
        LeaveAuditUserAuthCriteria example = new LeaveAuditUserAuthCriteria();
        LeaveAuditUserAuthCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(schoolId)
                .andBuildingIdIsNotNull();
        if (roleId != 0) {
            criteria.andRoleIdEqualTo(roleId);
        }
        List<LeaveAuditUserAuth> leaveAuditUserAuthList = leaveAuditUserAuthMapper.selectByExample(example);
        List<Integer> userAuthIdList = new ArrayList<>();
        for (LeaveAuditUserAuth auth : leaveAuditUserAuthList) {
            userAuthIdList.add(auth.getUserId());
        }
        Iterator<Map<String, Object>> iterator = userList.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            if (!userAuthIdList.contains(Integer.valueOf(map.get("id").toString()))) {
                iterator.remove();
            }
        }
        return userList;
    }
}
