package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.AuditUserAuthMapper;
import com.biu.wifi.campus.dao.DormBuildingUserMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/12/13.
 */
@Service
public class AuditUserAuthService {

    @Autowired
    private AuditUserAuthMapper auditUserAuthMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DormBuildingUserMapper dormBuildingUserMapper;

    /**
     * 教职工身份认证
     *
     * @param auditUserAuth 认证信息
     * @param reOperate     是否是重新认证，默认为false
     * @return
     */
    @Transactional
    public Result addAuditUserAuth(AuditUserAuth auditUserAuth, boolean reOperate, Integer reOperate2) {
        AuditUserAuthCriteria example;
        if (reOperate || reOperate2 == 1) {
            //删除之前的认证
            example = new AuditUserAuthCriteria();
            example.createCriteria().andIsDeleteEqualTo((short) 2)
                    .andSchoolIdEqualTo(auditUserAuth.getSchoolId())
                    .andUserIdEqualTo(auditUserAuth.getUserId());
            List<AuditUserAuth> auditUserAuthList = auditUserAuthMapper.selectByExample(example);
            for (AuditUserAuth auth : auditUserAuthList) {
                auth.setIsDelete((short) 1);
                auth.setDeleteTime(new Date());
                auditUserAuthMapper.updateByPrimaryKeySelective(auth);
            }
        }

        example = new AuditUserAuthCriteria();
        AuditUserAuthCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(auditUserAuth.getSchoolId())
                .andUserIdEqualTo(auditUserAuth.getUserId());
        if (auditUserAuth.getInstituteId() != null) {
            criteria.andInstituteIdEqualTo(auditUserAuth.getInstituteId());
        }
        if (auditUserAuth.getGradeId() != null) {
            criteria.andGradeIdEqualTo(auditUserAuth.getGradeId());
        }
        if (auditUserAuth.getBuildingId() != null) {
            criteria.andBuildingIdEqualTo(auditUserAuth.getBuildingId());
        }
        long count = auditUserAuthMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "您已经认证完成，请勿重复操作", null);
        }

        User user = userMapper.selectByPrimaryKey(auditUserAuth.getUserId());
        auditUserAuth.setUsername(user.getName());
        auditUserAuth.setCreateTime(new Date());
        if (auditUserAuth.getInstituteId() == null) {
            auditUserAuth.setInstituteId(user.getInstituteId());
        }
        auditUserAuthMapper.insertSelective(auditUserAuth);

        if (auditUserAuth.getBuildingId() != null) {
            //宿管认证，绑定宿舍楼
            DormBuildingUser dormBuildingUser = new DormBuildingUser();
            dormBuildingUser.setUserId(auditUserAuth.getUserId());
            dormBuildingUser.setUsername(auditUserAuth.getUsername());
            dormBuildingUser.setBuildingId(auditUserAuth.getBuildingId());
            dormBuildingUser.setPhone(user.getPhone());
            dormBuildingUser.setIsDelete((short) 2);
            dormBuildingUser.setCreateTime(new Date());
            dormBuildingUserMapper.insertSelective(dormBuildingUser);
        } else if (auditUserAuth.getInstituteId() != null) {
            //教导员认证，绑定学院和年级
            user.setInstituteId(auditUserAuth.getInstituteId());
            user.setGradeId(auditUserAuth.getGradeId());
            user.setUpdateTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        } else {
            //教务处认证

        }
        return new Result(Result.SUCCESS, "成功", null);
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
        AuditUserAuthCriteria example = new AuditUserAuthCriteria();
        AuditUserAuthCriteria.Criteria criteria = example.createCriteria();
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

        long count = auditUserAuthMapper.countByExample(example);
        return count > 0 ? true : false;
    }

    /**
     * 根据用户ID和学校ID查询用户的身份认证信息
     *
     * @param schoolId
     * @param userId
     * @return
     */
    public AuditUserAuth find(Integer schoolId, Integer userId) {
        AuditUserAuthCriteria example = new AuditUserAuthCriteria();
        example.setOrderByClause("create_time desc");
        AuditUserAuthCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId).andUserIdEqualTo(userId);
        List<AuditUserAuth> userAuthList = auditUserAuthMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userAuthList)) {
            return userAuthList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询用户的身份认证信息
     *
     * @param schoolId    学校ID
     * @param instituteId 学院ID
     * @param gradeId     年级ID
     * @param buildingId  宿舍楼ID
     * @param roleId      身份认证类型ID
     * @param userId      用户ID
     * @return
     */
    public List<AuditUserAuth> findList(Integer schoolId, Integer instituteId,
                                        Integer gradeId, Integer buildingId, Integer roleId, Integer userId) {
        AuditUserAuthCriteria example = new AuditUserAuthCriteria();
        example.setOrderByClause("create_time desc");
        AuditUserAuthCriteria.Criteria criteria = example.createCriteria()
                .andIsDeleteEqualTo((short) 2);
        if (schoolId != null) {
            criteria.andSchoolIdEqualTo(schoolId);
        }
        if (instituteId != null) {
            criteria.andInstituteIdEqualTo(instituteId);
        }
        if (gradeId != null) {
            criteria.andGradeIdEqualTo(gradeId);
        }
        if (buildingId != null) {
            criteria.andBuildingIdEqualTo(buildingId);
        }
        if (roleId != null) {
            criteria.andRoleIdEqualTo(roleId);
        }
        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        List<AuditUserAuth> userAuthList = auditUserAuthMapper.selectByExample(example);
        return userAuthList;
    }

    public List<AuditUserAuth> findList(Integer schoolId, Integer userId) {
        return findList(schoolId, null, null, null, null, userId);
    }

    public List<AuditUserAuth> findList(Integer schoolId, Integer instituteId, Integer roleId) {
        return findList(schoolId, instituteId, null, null, roleId, null);
    }

    public long countByExample(AuditUserAuthCriteria example) {
        return auditUserAuthMapper.countByExample(example);
    }
}
