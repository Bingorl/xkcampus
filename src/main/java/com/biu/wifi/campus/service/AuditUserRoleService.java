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
     * ????????????????????????????????????
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
                    //??????????????????
                    Grade grade = gradeMapper.selectByPrimaryKey(auditUserAuth.getGradeId());
                    if (grade != null) {
                        sb.append(grade.getName())
                                .append("???")
                                .append(roleName);
                        return sb.toString();
                    }
                } else {
                    //????????????
                    Institute institute = instituteMapper.selectByPrimaryKey(auditUserAuth.getInstituteId());
                    sb.append(institute.getName())
                            .append("???")
                            .append(roleName);
                    return sb.toString();
                }
            }

            //??????
            if (auditUserAuth.getBuildingId() != null) {
                DormBuilding dormBuilding = dormBuildingMapper.selectByPrimaryKey(auditUserAuth.getBuildingId());
                if (dormBuilding != null) {
                    sb.append(dormBuilding.getAreaPosition())
                            .append(dormBuilding.getNo())
                            .append(dormBuilding.getUnitNo())
                            .append("???")
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
            //???????????????
            example = new AuditUserRoleCriteria();
            example.createCriteria().andIsDeleteEqualTo((short) 2).andPidEqualTo(id);
            long count = auditUserRoleMapper.countByExample(example);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "??????????????????????????????????????????", null);
            }

            //??????????????????????????????????????????
            AuditUserAuthCriteria auditUserAuthEx = new AuditUserAuthCriteria();
            auditUserAuthEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andRoleIdEqualTo(role.getId());
            count = auditUserAuthMapper.countByExample(auditUserAuthEx);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "???????????????????????????????????????????????????");
            }

            role.setIsDelete((short) 1);
            role.setDeleteTime(new Date());
            auditUserRoleMapper.updateByPrimaryKeySelective(role);
            return new Result(Result.SUCCESS, "??????", null);
        } else {
            return new Result(Result.CUSTOM_MESSAGE, "?????????????????????", null);
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
            return new Result(Result.CUSTOM_MESSAGE, "???????????????????????????????????????", null);
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
        return new Result(Result.SUCCESS, "??????", null);
    }

    public List<Map<String, Object>> findUserListByAuditUserRoleId(Integer schoolId, Integer instituteId, Integer majorId,
                                                                   Integer classId, Integer gradeId, Integer roleId, String name) {
        //?????????????????????????????????????????????
        List<Map<String, Object>> userList = userMapperEx.findUserListByCondition(schoolId, name, null, instituteId, majorId, classId, gradeId);
        //???????????????????????????????????????????????????
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
     * ????????????id???????????????????????????????????????
     *
     * @param schoolId
     * @return
     */
    public List<User> getJxmsUserList(Integer schoolId, Integer instituteId) {
        AuditUserRole auditUserRole = findBySchoolIdAndCode(schoolId, AudUserRole.JXMS.getCode());
        if (auditUserRole == null) {
            throw new BizException(Result.CUSTOM_MESSAGE, "?????????????????????????????????????????????????????????????????????????????????????????????");
        }
        List<AuditUserAuth> auditUserAuthList = auditUserAuthService.findList(schoolId, instituteId, auditUserRole.getId());
        return getUserListByAuditUserAuthList(auditUserAuthList);
    }

    /**
     * ????????????id????????????????????????????????????
     *
     * @param schoolId
     * @return
     */
    public List<User> getJwUserList(Integer schoolId) {
        AuditUserRole auditUserRole = findBySchoolIdAndCode(schoolId, AudUserRole.JWRY.getCode());
        if (auditUserRole == null) {
            throw new BizException(Result.CUSTOM_MESSAGE, "???????????????????????????????????????????????????????????????????????????????????????");
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
