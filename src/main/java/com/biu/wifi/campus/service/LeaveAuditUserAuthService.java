package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/10/30.
 */
@Service
public class LeaveAuditUserAuthService {

    private static Logger logger = LoggerFactory.getLogger(LeaveAuditUserAuthService.class);
    @Autowired
    private LeaveAuditUserAuthMapper leaveAuditUserAuthMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LeaveAuditUserRoleMapper leaveAuditUserRoleMapper;
    @Autowired
    private DormBuildingUserMapper dormBuildingUserMapper;
    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private GradeMapper gradeMapper;

    /**
     * 教导员、教辅人员、宿管人员身份认证
     *
     * @param leaveAuditUserAuth 认证信息
     * @param reOperate          是否是重新认证，默认为false
     * @return
     */
    @Transactional
    public Result addLeaveAuditUserAuth(LeaveAuditUserAuth leaveAuditUserAuth, boolean reOperate, Integer reOperate2) {
        LeaveAuditUserAuthCriteria example;
        if (reOperate || reOperate2 == 1) {
            //删除之前的认证
            example = new LeaveAuditUserAuthCriteria();
            example.createCriteria().andIsDeleteEqualTo((short) 2)
                    .andSchoolIdEqualTo(leaveAuditUserAuth.getSchoolId())
                    .andUserIdEqualTo(leaveAuditUserAuth.getUserId());
            List<LeaveAuditUserAuth> leaveAuditUserAuthList = leaveAuditUserAuthMapper.selectByExample(example);
            for (LeaveAuditUserAuth auth : leaveAuditUserAuthList) {
                auth.setIsDelete((short) 1);
                auth.setDeleteTime(new Date());
                leaveAuditUserAuthMapper.updateByPrimaryKeySelective(auth);
            }
        }

        example = new LeaveAuditUserAuthCriteria();
        LeaveAuditUserAuthCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(leaveAuditUserAuth.getSchoolId())
                .andUserIdEqualTo(leaveAuditUserAuth.getUserId());
        if (leaveAuditUserAuth.getInstituteId() != null) {
            criteria.andInstituteIdEqualTo(leaveAuditUserAuth.getInstituteId());
        }
        if (leaveAuditUserAuth.getGradeId() != null) {
            criteria.andGradeIdEqualTo(leaveAuditUserAuth.getGradeId());
        }
        if (leaveAuditUserAuth.getBuildingId() != null) {
            criteria.andBuildingIdEqualTo(leaveAuditUserAuth.getBuildingId());
        }
        long count = leaveAuditUserAuthMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "您已经认证完成，请勿重复操作", null);
        }

        User user = userMapper.selectByPrimaryKey(leaveAuditUserAuth.getUserId());
        leaveAuditUserAuth.setUsername(user.getName());
        leaveAuditUserAuth.setCreateTime(new Date());
        if (leaveAuditUserAuth.getInstituteId() == null) {
            leaveAuditUserAuth.setInstituteId(user.getInstituteId());
        }
        leaveAuditUserAuthMapper.insertSelective(leaveAuditUserAuth);

        //宿管认证，绑定宿舍楼
        if (leaveAuditUserAuth.getBuildingId() != null) {
            DormBuildingUser dormBuildingUser = new DormBuildingUser();
            dormBuildingUser.setUserId(leaveAuditUserAuth.getUserId());
            dormBuildingUser.setUsername(leaveAuditUserAuth.getUsername());
            dormBuildingUser.setBuildingId(leaveAuditUserAuth.getBuildingId());
            dormBuildingUser.setPhone(user.getPhone());
            dormBuildingUser.setIsDelete((short) 2);
            dormBuildingUser.setCreateTime(new Date());
            dormBuildingUserMapper.insertSelective(dormBuildingUser);
        }

        //教导员认证，绑定学院和年级
        if (leaveAuditUserAuth.getInstituteId() != null) {
            user.setInstituteId(leaveAuditUserAuth.getInstituteId());
            user.setGradeId(leaveAuditUserAuth.getGradeId());
            Grade grade = gradeMapper.selectByPrimaryKey(leaveAuditUserAuth.getGradeId());
            user.setGrade(grade.getName());
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        }

        //将leave_audit_user_auth的数据同步到audit_user_auth中
        AuditUserAuth auth = new AuditUserAuth();
        auth.setSchoolId(leaveAuditUserAuth.getSchoolId());
        auth.setInstituteId(leaveAuditUserAuth.getInstituteId());
        auth.setGradeId(leaveAuditUserAuth.getGradeId());
        auth.setBuildingId(leaveAuditUserAuth.getBuildingId());
        auth.setRoleId(leaveAuditUserAuth.getRoleId());
        auth.setUserId(leaveAuditUserAuth.getUserId());
        auth.setUsername(user.getName());
        auth.setIsAuth(leaveAuditUserAuth.getIsAuth());
        return auditUserAuthService.addAuditUserAuth(auth, reOperate, reOperate2);
    }

    /**
     * 校验是否认证
     *
     * @param schoolId 学校ID
     * @param userId   用户ID
     * @return
     */
    public boolean isAuth(Integer schoolId, Integer userId) {
        return isAuth(schoolId, null, null, null, userId);
    }

    /**
     * 校验是否认证
     *
     * @param schoolId    学校ID
     * @param instituteId 学院ID
     * @param gradeId     年级ID
     * @param buildingId  宿舍楼ID
     * @param userId      用户ID
     * @return
     */
    public boolean isAuth(Integer schoolId, Integer instituteId, Integer gradeId, Integer buildingId, Integer userId) {
        LeaveAuditUserAuthCriteria example = new LeaveAuditUserAuthCriteria();
        LeaveAuditUserAuthCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId).andUserIdEqualTo(userId);
        if (instituteId != null) {
            criteria.andInstituteIdEqualTo(instituteId);
        }
        if (gradeId != null) {
            criteria.andGradeIdEqualTo(gradeId);
        }
        if (buildingId != null) {
            criteria.andBuildingIdEqualTo(buildingId);
        }

        long count = leaveAuditUserAuthMapper.countByExample(example);
        return count > 0 ? true : false;
    }

    public LeaveAuditUserAuth find(Integer schoolId, Integer userId) {
        LeaveAuditUserAuthCriteria example = new LeaveAuditUserAuthCriteria();
        example.setOrderByClause("create_time desc");
        LeaveAuditUserAuthCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId).andUserIdEqualTo(userId);
        List<LeaveAuditUserAuth> userAuthList = leaveAuditUserAuthMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userAuthList)) {
            return userAuthList.get(0);
        } else {
            return null;
        }
    }

    public Integer LeaveAuditUserAuthRoleId(Integer schoolId, Integer userId) {
        LeaveAuditUserAuth auditUserAuth = find(schoolId, userId);
        if (auditUserAuth == null) {
            return null;
        } else {
            return auditUserAuth.getRoleId();
        }
    }

    public String findLeaveAuditUserRoleName(Integer schoolId, Integer userId) {
        LeaveAuditUserAuth auditUserAuth = find(schoolId, userId);
        if (auditUserAuth == null) {
            return "";
        } else {
            LeaveAuditUserRole role = leaveAuditUserRoleMapper.selectByPrimaryKey(auditUserAuth.getRoleId());
            return role != null ? role.getName() : "";
        }
    }

    public List<LeaveAuditUserAuth> findList(LeaveAuditUserAuthCriteria example) {
        return leaveAuditUserAuthMapper.selectByExample(example);
    }
}
