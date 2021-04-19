package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.dao.model.UserCriteria;
import com.biu.wifi.campus.dao.model.UserCriteria.Criteria;
import com.biu.wifi.campus.daoEx.BackendUserMapperEx;
import com.biu.wifi.campus.daoEx.XiaoXinMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BackendUserMapperEx userMapperEx;

    @Autowired
    private XiaoXinMapperEx xiaoXinMapperEx;

    public User getUser(User entity) {
        return IbatisServiceUtils.get(entity, userMapper);
    }

    public User getById(Integer id) {
//        String key = "getById_" + id;
//        User user = CacheSupport.get("user", key, User.class);
//        if (user == null) {
        User user = userMapper.selectByPrimaryKey(id);
//            CacheSupport.put("user", key, user);
//        }
        return user;
    }

    public Integer addUser(User entity) {
        try {
            IbatisServiceUtils.insert(entity, userMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return entity.getId();
    }

    public void updateUser(User entity) {
        IbatisServiceUtils.updateByPk(entity, userMapper);
    }

    public void deleteUser(User entity) throws Exception {
        IbatisServiceUtils.delete(entity, userMapper);
    }

    public List<User> findUserList(User entity) {
        return IbatisServiceUtils.find(entity, userMapper);
    }

    // 选择@的人----获取全校用户
    public List<Map<String, Object>> user_selectAtUserListForSchool(Integer user_id, String search, Integer school_id) {
        return xiaoXinMapperEx.user_selectAtUserListForSchool(user_id, search, school_id);
    }

    // 根据班级获取学生信息-----未加入群组的
    public List<Map<String, Object>> user_findStudentList(Integer user_id, Integer class_id, Integer group_id,
                                                          Integer institute_id, Integer grade_id, Integer major_id) {
        return xiaoXinMapperEx.user_findStudentList(user_id, class_id, group_id, institute_id, grade_id, major_id);
    }

    public List<User> findList(User entity) {
        UserCriteria criteria = new UserCriteria();
        criteria.setOrderByClause("create_time");
        Criteria createCriteria = criteria.createCriteria();
        if (StringUtils.isNotBlank(entity.getName())) {
            createCriteria.andNameLike("%" + entity.getName() + "%");
        }
        if (entity.getType() != null) {
            createCriteria.andTypeEqualTo(entity.getType());
        }
        if (entity.getIsDelete() != null) {
            createCriteria.andIsDeleteEqualTo(entity.getIsDelete());
        }
        return userMapper.selectByExample(criteria);
    }

    public List<User> findList(UserCriteria example) {
        return userMapper.selectByExample(example);
    }

    // 搜索校内学生------邀请成员入群
    public List<Map<String, Object>> user_findStudentBySchool(Integer userId, Integer groupId, String search) {
        return xiaoXinMapperEx.user_findStudentBySchool(userId, groupId, search);
    }

    // 根据多个id获取用户信息
    public List<Map<String, Object>> findUserByIds(String[] ids) {
        return xiaoXinMapperEx.findUserByIds(ids);
    }

    public List<Map<String, Object>> findUserTeacherLists(Integer schoolId, String name) {
        return userMapperEx.findUserTeacherLists(schoolId, null, null, name, 0, null, (short) 1);
    }

    /**
     * 根据用户ID判断用户是否是教职工
     *
     * @param userId
     * @return
     */
    public boolean isTeacher(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return false;
        }

        if (user.getIsTeacher() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public User getUserByStuNumber(String stuNumber) {
        User user = new User();
        user.setStuNumber(stuNumber);
        user.setIsTeacher((short) 1);
        user.setIsDelete((short) 2);
        return getUser(user);
    }
}
