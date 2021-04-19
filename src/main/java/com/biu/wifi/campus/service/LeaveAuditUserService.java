package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.LeaveAuditUserMapper;
import com.biu.wifi.campus.dao.LeaveTypeMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class LeaveAuditUserService {

    @Autowired
    private LeaveAuditUserMapper leaveAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    /**
     * 添加或者编辑审批人队列
     *
     * @param id
     * @param userId    用户ID
     * @param type      请假类型(1-3天以下，2-3~7天，3-7天以上，4-大四实习请假，5-参加重大赛事请假)
     * @param auditUser 审批人ID字符串
     * @return
     */
    public Result addOrUpdateLeaveAuditUser(Integer id, Integer userId, short type, String auditUser) {
        //校验审批人。自己不能再审批队列中
        String[] userIdList = auditUser.split(",");
        for (String str : userIdList) {
            if (str.equals(String.valueOf(userId))) {
                return new Result(Result.CUSTOM_MESSAGE, "不能将自己设置到审批人中", null);
            }
        }

        LeaveAuditUser leaveAuditUser = new LeaveAuditUser();
        LeaveAuditUserCriteria example = new LeaveAuditUserCriteria();
        example.setOrderByClause("create_time desc");
        LeaveAuditUserCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andUserIdEqualTo(userId).andTypeEqualTo(type).andAuditUserEqualTo(auditUser);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        long count = leaveAuditUserMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该审批队列已存在", null);
        }

        if (id != null) {
            leaveAuditUser = leaveAuditUserMapper.selectByPrimaryKey(id);
        }

        leaveAuditUser.setUserId(userId);
        User user = userMapper.selectByPrimaryKey(userId);
        leaveAuditUser.setSchoolId(user.getSchoolId());
        leaveAuditUser.setType(type);
        leaveAuditUser.setAuditUser(auditUser);

        if (id == null) {
            leaveAuditUser.setCreateTime(new Date());
            leaveAuditUser.setIsDelete((short) 2);
            leaveAuditUserMapper.insertSelective(leaveAuditUser);
            return new Result(Result.SUCCESS, "成功", null);
        } else {
            leaveAuditUser.setUpdateTime(new Date());
            leaveAuditUserMapper.updateByPrimaryKeySelective(leaveAuditUser);
            return new Result(Result.SUCCESS, "成功", null);
        }
    }

    /**
     * 添加或者编辑审批人队列
     *
     * @param userId 用户ID
     * @param data   审批人数据包
     * @return
     */
    public Result addOrUpdateLeaveAuditUser(Integer userId, Map<String, String> data) {
        Result result = null;
        //解析审批人数据包(key为请假类型ID,value为审批人ID字符串)
        for (String key : data.keySet()) {
            if (StringUtils.isBlank(key)) {
                continue;
            }

            short type = Short.valueOf(key);
            LeaveAuditUser leaveAuditUser = getLeaveAuditUser(userId, type);
            Integer id = null;
            if (leaveAuditUser != null) {
                id = leaveAuditUser.getId();
            }

            String auditUser = data.get(key).toString();
            result = addOrUpdateLeaveAuditUser(id, userId, type, auditUser);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
        }
        return result;
    }

    /**
     * 获取请假审批人队列
     *
     * @param userId
     * @param type
     * @return
     */
    public LeaveAuditUser getLeaveAuditUser(Integer userId, short type) {
        List<LeaveAuditUser> list = getLeaveAuditUserList(userId, type);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 删除xx用户的审批人队列
     *
     * @param id
     * @param userId
     * @return
     */
    public Result deleteLeaveAuditUser(Integer id, Integer userId) {
        LeaveAuditUser leaveAuditUser = leaveAuditUserMapper.selectByPrimaryKey(id);
        if (leaveAuditUser == null || (leaveAuditUser != null && leaveAuditUser.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已被删除", null);
        }

        if (leaveAuditUser.getUserId() != userId) {
            return new Result(Result.CUSTOM_MESSAGE, "没有操作权限", null);
        }

        leaveAuditUser.setIsDelete((short) 1);
        leaveAuditUser.setDeleteTime(new Date());
        leaveAuditUser.setUpdateTime(new Date());
        leaveAuditUserMapper.updateByPrimaryKeySelective(leaveAuditUser);
        return new Result(Result.SUCCESS, "成功", null);
    }

    /**
     * 获取请假审批人列表
     *
     * @param userId
     * @param type
     * @return
     */
    public List<LeaveAuditUser> getLeaveAuditUserList(Integer userId, short type) {
        LeaveAuditUserCriteria example = new LeaveAuditUserCriteria();
        LeaveAuditUserCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andUserIdEqualTo(userId);
        if (type != 0) {
            criteria.andTypeEqualTo(type);
        }
        List<LeaveAuditUser> list = leaveAuditUserMapper.selectByExample(example);
        return list;
    }

    /**
     * 获取请假审批人列表
     *
     * @param userId
     * @param type
     * @return
     */
    public List<Map<String, Object>> getLeaveAuditUserMapList(Integer userId, short type) {
        List<LeaveAuditUser> leaveAuditUserList = getLeaveAuditUserList(userId, type);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (LeaveAuditUser leaveAuditUser : leaveAuditUserList) {
            map = new HashedMap();
            map.put("id", leaveAuditUser.getId());
            map.put("userId", leaveAuditUser.getUserId());
            map.put("auditUser", leaveAuditUser.getAuditUser());
            list.add(map);
        }
        return list;
    }

    /**
     * 获取请假审批人列表
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> getLeaveAuditUserMapList(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        LeaveTypeCriteria example = new LeaveTypeCriteria();
        example.createCriteria().andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(user.getSchoolId());
        List<LeaveType> leaveTypeList = leaveTypeMapper.selectByExample(example);

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map, userMap;
        for (LeaveType leaveType : leaveTypeList) {
            map = new HashedMap();
            map.put("id", leaveType.getId());
            map.put("name", leaveType.getName());
            List<Map<String, Object>> leaveAuditUserMapList = getLeaveAuditUserMapList(userId, leaveType.getId().shortValue());
            List<Map<String, Object>> auditUserMapList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(leaveAuditUserMapList)) {
                String auditUser = leaveAuditUserMapList.get(0).get("auditUser").toString();
                String[] userIdList = auditUser.split(",");
                for (String id : userIdList) {
                    if (StringUtils.isBlank(id)) {
                        continue;
                    }
                    user = userMapper.selectByPrimaryKey(Integer.valueOf(id));
                    if (user != null) {
                        userMap = new HashedMap();
                        userMap.put("id", user.getId());
                        userMap.put("name", user.getName());
                        auditUserMapList.add(userMap);
                    }
                }
            }
            map.put("auditUserMapList", auditUserMapList);
            list.add(map);
        }
        return list;
    }
}