package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.PageApplyMapper;
import com.biu.wifi.campus.dao.PushMapper;
import com.biu.wifi.campus.dao.UserAuthMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.UserPageRelationshipMapper;
import com.biu.wifi.campus.dao.model.PageApply;
import com.biu.wifi.campus.dao.model.Push;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserAuth;
import com.biu.wifi.campus.dao.model.UserCriteria;
import com.biu.wifi.campus.dao.model.UserCriteria.Criteria;
import com.biu.wifi.campus.dao.model.UserPageRelationship;
import com.biu.wifi.campus.daoEx.BackendUserMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class BackendUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BackendUserMapperEx userMapperEx;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private UserPageRelationshipMapper userPageRelationshipMapper;

    @Autowired
    private PageApplyMapper pageApplyMapper;

    @Autowired
    private PushMapper pushMapper;

    public List<User> getUserListForLogin(User entity) {
        return IbatisServiceUtils.find(entity, userMapper);
    }

    public List<Map<String, Object>> findUserLists(Integer schoolId, String startTime, String endTime, String name,
                                                   Integer instituteId, Integer majorId, Integer classId, Integer gradeId, Integer id, String studentNumber,
                                                   Short status, Short isTeacher) {
        return userMapperEx.findUserLists(schoolId, startTime, endTime, name, instituteId, majorId, classId, gradeId,
                id, studentNumber, status, isTeacher);
    }

    public List<Map<String, Object>> findUserTeacherLists(Integer schoolId, String startTime, String endTime,
                                                          String name, Integer id, String studentNumber, Short status) {
        return userMapperEx.findUserTeacherLists(schoolId, startTime, endTime, name, id, studentNumber, status);
    }

    public List<Map<String, Object>> findUserListByCondition(Integer schoolId, String name, String stuNumber, Integer instituteId,
                                                             Integer majorId, Integer classId, Integer gradeId) {
        return userMapperEx.findUserListByCondition(schoolId, name, stuNumber, instituteId, majorId, classId, gradeId);
    }

    public Map<String, Object> findUserById(Integer id) {
        return userMapperEx.findUserById(id);
    }

    public void updateUser(User entity) {
        userMapper.updateByPrimaryKeySelective(entity);
    }

    public Map<String, Object> querySchoolIndexPageData(Integer schoolId) {
        return userMapperEx.querySchoolIndexPageData(schoolId);
    }

    public List<Map<String, Object>> findStudentAuthLists(Integer schoolId, String name, Integer instituteId,
                                                          Integer majorId, Integer classId, Integer gradeId, String studentNumber) {
        return userMapperEx.findStudentAuthLists(schoolId, name, instituteId, majorId, classId, gradeId, studentNumber);
    }

    public List<Map<String, Object>> findTeacherAuthLists(Integer schoolId, String name, String studentNumber) {
        return userMapperEx.findTeacherAuthLists(schoolId, name, studentNumber);
    }

    public void updateUserAuth(UserAuth entity) {
        userAuthMapper.updateByPrimaryKeySelective(entity);
    }

    public UserAuth getUserAuth(UserAuth entity) {
        return IbatisServiceUtils.get(entity, userAuthMapper);
    }

    public User getUser(User entity) {
        return IbatisServiceUtils.get(entity, userMapper);
    }

    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public List<Map<String, Object>> findPublicPages(Integer schoolId, String startTime, String endTime, String name) {
        return userMapperEx.findPublicPages(schoolId, startTime, endTime, name);
    }

    public List<Map<String, Object>> findPublicPageManagers(Integer pageId) {
        return userMapperEx.findPublicPageManagers(pageId);
    }

    public void updatePublicPageUserRelationship(UserPageRelationship entity) {
        userPageRelationshipMapper.updateByPrimaryKeySelective(entity);
    }

    public UserPageRelationship getPublicPageUserRelationshipById(Integer id) {
        return userPageRelationshipMapper.selectByPrimaryKey(id);
    }

    public List<UserPageRelationship> getPublicPageUserRelationship(UserPageRelationship entity) {
        return IbatisServiceUtils.find(entity, userPageRelationshipMapper);
    }

    public List<Map<String, Object>> findPublicPageAuthLists(Integer schoolId) {
        return userMapperEx.findPublicPageAuthLists(schoolId);
    }

    public PageApply getPageApply(PageApply entity) {
        return IbatisServiceUtils.get(entity, pageApplyMapper);
    }

    public void updatePageApply(PageApply entity) {
        pageApplyMapper.updateByPrimaryKeySelective(entity);
    }

    public void addPublicPage(User user) {
        userMapper.insertSelective(user);
    }

    public void addUserPageRelationship(UserPageRelationship user) {
        userPageRelationshipMapper.insertSelective(user);
    }

    public void addPush(Push entity) {
        pushMapper.insertSelective(entity);
    }

    public List<User> getAllAvailableUsers() {
        UserCriteria userCriteria = new UserCriteria();
        Criteria criteria = userCriteria.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andStatusEqualTo((short) 1);
        criteria.andTypeEqualTo((short) 1);
        return userMapper.selectByExample(userCriteria);
    }

    public List<User> findUserListByLevel(Integer schoolId, Integer instituteId, Integer majorId, Integer classId,
                                          Integer gradeId) {
        UserCriteria userCriteria = new UserCriteria();
        Criteria criteria = userCriteria.createCriteria();

        criteria.andSchoolIdEqualTo(schoolId);
        if (instituteId != 0) {
            criteria.andInstituteIdEqualTo(instituteId);
        }
        if (majorId != 0) {
            criteria.andMajorIdEqualTo(majorId);
        }
        if (gradeId != 0) {
            criteria.andGradeIdEqualTo(gradeId);
        }
        if (classId != 0) {
            criteria.andClassIdEqualTo(classId);
        }

        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andStatusEqualTo((short) 1);
        criteria.andTypeEqualTo((short) 1);
        return userMapper.selectByExample(userCriteria);
    }

    public List<User> findUserNameListByUserIdList(List<Integer> userIdList) {
        UserCriteria example = new UserCriteria();
        example.createCriteria().andIdIn(userIdList).andIsDeleteEqualTo((short) 2);
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

    public User getById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
