package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AudUserRole;
import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.BackendUserMapperEx;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/13.
 */
@Service
public class AuditUserRoleService {

    @Autowired
    private AuditUserRoleMapper auditUserRoleMapper;
    @Autowired
    private AuditUserAuthMapper auditUserAuthMapper;
    @Autowired
    private BackendUserMapperEx userMapperEx;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private DormBuildingMapper dormBuildingMapper;
    @Autowired
    private InstituteMapper instituteMapper;
    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private UserService userService;

    public List<AuditUserRole> findList(Integer schoolId, Integer pid) {
        AuditUserRoleCriteria example = new AuditUserRoleCriteria();
        AuditUserRoleCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId);
        if (pid != null) {
            criteria.andPidEqualTo(pid);
        } else {
            criteria.andPidIsNull();
        }
        return auditUserRoleMapper.selectByExample(example);
    }

    public AuditUserRole findBySchoolIdAndCode(Integer schoolId, String code) {
        AuditUserRoleCriteria example = new AuditUserRoleCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andCodeEqualTo(code)
                .andIsDeleteEqualTo((short) 2);
        List<AuditUserRole> auditUserRoleList = auditUserRoleMapper.selectByExample(example);
        if (auditUserRoleList.size() == 0) {
            return null;
        } else {
            return auditUserRoleList.get(0);
        }
    }

    public List<Map<String, Object>> findMapList(Integer schoolId, Integer pid) {
        List<AuditUserRole> roles = findList(schoolId, pid);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        AuditUserRole auditUserRole;
        for (AuditUserRole role : roles) {
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

    public AuditUserRole findById(Integer id) {
        AuditUserRole role = auditUserRoleMapper.selectByPrimaryKey(id);
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
        AuditUserAuthCriteria auditUserAuthCriteriaEx = new AuditUserAuthCriteria();
        auditUserAuthCriteriaEx.setOrderByClause("create_time desc");
        auditUserAuthCriteriaEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId)
                .andRoleIdEqualTo(roleId);
        List<AuditUserAuth> auditUserAuthList = auditUserAuthMapper.selectByExample(auditUserAuthCriteriaEx);
        if (CollectionUtils.isNotEmpty(auditUserAuthList)) {
            AuditUserAuth auditUserAuth = auditUserAuthList.get(0);

            if (auditUserAuth.getInstituteId() != null) {
                if (auditUserAuth.getGradeId() != null) {
                    //辅导员或教师
                    Grade grade = gradeMapper.selectByPrimaryKey(auditUserAuth.getGradeId());
                    if (grade != null) {
                        sb.append(grade.getName())
                                .append("级")
                                .append(roleName);
                        return sb.toString();
                    }
                } else {
                    //教学秘书
                    Institute institute = instituteMapper.selectByPrimaryKey(auditUserAuth.getInstituteId());
                    sb.append(institute.getName())
                            .append("的")
                            .append(roleName);
                    return sb.toString();
                }
            }

            //宿管
            if (auditUserAuth.getBuildingId() != null) {
                DormBuilding dormBuilding = dormBuildingMapper.selectByPrimaryKey(auditUserAuth.getBuildingId());
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
        AuditUserRoleCriteria example = new AuditUserRoleCriteria();
        example.createCriteria().andIsDeleteEqualTo((short) 2).andIdEqualTo(id);
        List<AuditUserRole> roles = auditUserRoleMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(roles)) {
            AuditUserRole role = roles.get(0);
            //子部门个数
            example = new AuditUserRoleCriteria();
            example.createCriteria().andIsDeleteEqualTo((short) 2).andPidEqualTo(id);
            long count = auditUserRoleMapper.countByExample(example);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "包含下级身份类型，不能被删除", null);
            }

            //判断是否有人认证此类型的身份
            AuditUserAuthCriteria auditUserAuthEx = new AuditUserAuthCriteria();
            auditUserAuthEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andRoleIdEqualTo(role.getId());
            count = auditUserAuthMapper.countByExample(auditUserAuthEx);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "有用户认证了该身份类型，不能被删除");
            }

            role.setIsDelete((short) 1);
            role.setDeleteTime(new Date());
            auditUserRoleMapper.updateByPrimaryKeySelective(role);
            return new Result(Result.SUCCESS, "成功", null);
        } else {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已被删除", null);
        }
    }

    public Result addOrUpdateAuditUserRole(Integer id, Integer schoolId, Integer pid, String name, Short allowCreateAuditUser) {
        AuditUserRoleCriteria example = new AuditUserRoleCriteria();
        AuditUserRoleCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId).andNameEqualTo(name);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        if (pid != null) {
            criteria.andPidEqualTo(pid);
        }

        long count = auditUserRoleMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已存在，请勿重复添加", null);
        }

        AuditUserRole role;
        if (id != null) {
            role = auditUserRoleMapper.selectByPrimaryKey(id);
            role.setPid(pid);
            role.setSchoolId(schoolId);
            role.setName(name);
            role.setAllowCreateAuditUser(allowCreateAuditUser);
            role.setUpdateTime(new Date());
            auditUserRoleMapper.updateByPrimaryKeySelective(role);
        } else {
            role = new AuditUserRole();
            role.setPid(pid);
            role.setSchoolId(schoolId);
            role.setName(name);
            role.setAllowCreateAuditUser(allowCreateAuditUser);
            role.setCreateTime(new Date());
            auditUserRoleMapper.insertSelective(role);
        }
        return new Result(Result.SUCCESS, "成功", null);
    }

    public List<Map<String, Object>> findUserListByAuditUserRoleId(Integer schoolId, Integer instituteId, Integer majorId,
                                                                   Integer classId, Integer gradeId, Integer roleId, String name) {
        //根据用户姓名搜索满足条件的用户
        List<Map<String, Object>> userList = userMapperEx.findUserListByCondition(schoolId, name, null, instituteId, majorId, classId, gradeId);
        //筛选出身份认证是宿管人员的用户集合
        AuditUserAuthCriteria example = new AuditUserAuthCriteria();
        AuditUserAuthCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(schoolId)
                .andBuildingIdIsNotNull();
        if (roleId != 0) {
            criteria.andRoleIdEqualTo(roleId);
        }
        List<AuditUserAuth> auditUserAuthList = auditUserAuthMapper.selectByExample(example);
        List<Integer> userAuthIdList = new ArrayList<>();
        for (AuditUserAuth auth : auditUserAuthList) {
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

    /**
     * 根据学校id查询所有的教学秘书用户列表
     *
     * @param schoolId
     * @return
     */
    public List<User> getJxmsUserList(Integer schoolId, Integer instituteId) {
        AuditUserRole auditUserRole = findBySchoolIdAndCode(schoolId, AudUserRole.JXMS.getCode());
        if (auditUserRole == null) {
            throw new BizException(Result.CUSTOM_MESSAGE, "未设置教务秘书类型的角色，无法推送通知给教学秘书，请联系教务处");
        }
        List<AuditUserAuth> auditUserAuthList = auditUserAuthService.findList(schoolId, instituteId, auditUserRole.getId());
        return getUserListByAuditUserAuthList(auditUserAuthList);
    }

    /**
     * 根据学校id查询所有的教务处用户列表
     *
     * @param schoolId
     * @return
     */
    public List<User> getJwUserList(Integer schoolId) {
        AuditUserRole auditUserRole = findBySchoolIdAndCode(schoolId, AudUserRole.JWRY.getCode());
        if (auditUserRole == null) {
            throw new BizException(Result.CUSTOM_MESSAGE, "未设置教务处类型的角色，无法推送通知给教务处，请联系教务处");
        }
        List<AuditUserAuth> auditUserAuthList = auditUserAuthService.findList(schoolId, null, auditUserRole.getId());
        return getUserListByAuditUserAuthList(auditUserAuthList);
    }


    private List<User> getUserListByAuditUserAuthList(List<AuditUserAuth> auditUserAuthList) {
        List<User> userList = new ArrayList<>();
        for (AuditUserAuth auth : auditUserAuthList) {
            User user = userService.getById(auth.getUserId());
            userList.add(user);
        }

        return userList;
    }
}
