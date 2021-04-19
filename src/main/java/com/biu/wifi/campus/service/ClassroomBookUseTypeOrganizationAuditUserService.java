package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.dao.ClassroomBookUseTypeOrganizationAuditUserMapper;
import com.biu.wifi.campus.dao.ClassroomBookUseTypeOrganizationMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/6.
 */
@Service
public class ClassroomBookUseTypeOrganizationAuditUserService {

    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserMapper classroomBookUseTypeOrganizationAuditUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ClassroomBookUseTypeOrganizationMapper classroomBookUseTypeOrganizationMapper;

    /**
     * 用户是否具有教室审核权限
     *
     * @param schoolId
     * @param userId
     * @return
     */
    public boolean allowToAuditClassroomBook(Integer schoolId, Integer userId) {
        ClassroomBookUseTypeOrganizationCriteria ex = new ClassroomBookUseTypeOrganizationCriteria();
        ex.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(schoolId);
        List<ClassroomBookUseTypeOrganization> classroomBookUseTypeOrganizations = classroomBookUseTypeOrganizationMapper.selectByExample(ex);
        if (classroomBookUseTypeOrganizations.isEmpty()) {
            return false;
        }
        List<Integer> organizationIdList = new ArrayList<>();
        for (ClassroomBookUseTypeOrganization organization : classroomBookUseTypeOrganizations) {
            organizationIdList.add(organization.getId());
        }
        ClassroomBookUseTypeOrganizationAuditUserCriteria ex2 = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
        ex2.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andOrganizationIdIn(organizationIdList);
        List<ClassroomBookUseTypeOrganizationAuditUser> classroomBookUseTypeOrganizationAuditUsers = classroomBookUseTypeOrganizationAuditUserMapper.selectByExample(ex2);
        boolean allowToAudit = false;
        for (ClassroomBookUseTypeOrganizationAuditUser user : classroomBookUseTypeOrganizationAuditUsers) {
            List<String> userIdList = Arrays.asList(user.getAuditUser().split(","));
            if (userIdList.contains(userId.toString())) {
                allowToAudit = true;
            }
        }
        return allowToAudit;
    }

    /**
     * 组织审批人员
     *
     * @param useTypeId      使用类型ID
     * @param organizationId 组织ID
     * @param isDelete       是否删除（1是，2否）
     * @return
     */
    public ClassroomBookUseTypeOrganizationAuditUser find(Integer useTypeId, Integer organizationId, Short isDelete) {
        ClassroomBookUseTypeOrganizationAuditUserCriteria example = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
        example.setOrderByClause("create_time desc");
        ClassroomBookUseTypeOrganizationAuditUserCriteria.Criteria criteria = example.createCriteria()
                .andOrganizationIdEqualTo(organizationId)
                .andUseTypeIdEqualTo(useTypeId);
        if (isDelete != null) {
            criteria.andIsDeleteEqualTo(isDelete);
        }
        List<ClassroomBookUseTypeOrganizationAuditUser> list = findList(example);
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }


    /**
     * 组织审批人员列表
     *
     * @param example
     * @return
     */
    public List<ClassroomBookUseTypeOrganizationAuditUser> findList(ClassroomBookUseTypeOrganizationAuditUserCriteria example) {
        return classroomBookUseTypeOrganizationAuditUserMapper.selectByExample(example);
    }

    /**
     * 组织审批人员列表
     *
     * @param example
     * @param extendParams 额外的参数
     * @return
     */
    public List<Map> findList(ClassroomBookUseTypeOrganizationAuditUserCriteria example, Map extendParams) {
        List<ClassroomBookUseTypeOrganizationAuditUser> classroomBookUseTypeOrganizationAuditUsercList = findList(example);
        List<Map> mapList = new ArrayList<>();
        for (ClassroomBookUseTypeOrganizationAuditUser auditUser : classroomBookUseTypeOrganizationAuditUsercList) {
            Map temp = BeanUtil.beanToMap(auditUser, extendParams);
            mapList.add(temp);
        }
        return mapList;
    }

    /**
     * 组织审批人员列表
     *
     * @param useTypeId
     * @param organizationIdList 指定组织ID集合
     * @return
     */
    public List<Map> findListWithUserName(Integer useTypeId, List<Integer> organizationIdList) {
        List<Map> organizationMapList = new ArrayList<>();
        for (Integer organizationId : organizationIdList) {
            ClassroomBookUseTypeOrganization organization = classroomBookUseTypeOrganizationMapper.selectByPrimaryKey(organizationId);
            Map organizationMap = new HashMap();
            organizationMap.put("id", organization.getId());
            organizationMap.put("name", organization.getName());
            List<Map> userList = new ArrayList<>();
            organizationMap.put("auditUserMapList", userList);

            //根据使用类型ID和组织ID查询教室预约审批人员列表
            ClassroomBookUseTypeOrganizationAuditUserCriteria example = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
            example.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andUseTypeIdEqualTo(useTypeId)
                    .andOrganizationIdEqualTo(organizationId);
            List<Map> classroomBookUseTypeOrganizationAuditUserList = findList(example, new HashMap());
            if (classroomBookUseTypeOrganizationAuditUserList.size() > 0) {
                Map classroomBookUseTypeOrganizationAuditUserMap = classroomBookUseTypeOrganizationAuditUserList.get(0);
                for (String userId : classroomBookUseTypeOrganizationAuditUserMap.get("auditUser").toString().split(",")) {
                    User user = userMapper.selectByPrimaryKey(Integer.valueOf(userId));
                    Map userMap = new HashMap();
                    userMap.put("id", user.getId());
                    userMap.put("name", user.getName());
                    userList.add(userMap);
                }
            }

            organizationMapList.add(organizationMap);
        }
        return organizationMapList;
    }

    /**
     * 新增或编辑
     *
     * @param list
     * @return
     */

    public Result addOrUpdate(List<ClassroomBookUseTypeOrganizationAuditUser> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new Result(Result.CUSTOM_MESSAGE, "审批人员列表不能为空");
        }

        Integer useTypeId = list.get(0).getUseTypeId();
        if (useTypeId == null) {
            return new Result(Result.CUSTOM_MESSAGE, "审批人员列表不能为空");
        }

        //删除该类型中旧的审批人
        ClassroomBookUseTypeOrganizationAuditUserCriteria example = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUseTypeIdEqualTo(useTypeId);
        ClassroomBookUseTypeOrganizationAuditUser record = new ClassroomBookUseTypeOrganizationAuditUser();
        record.setIsDelete((short) 1);
        record.setDeleteTime(new Date());
        classroomBookUseTypeOrganizationAuditUserMapper.updateByExampleSelective(record, example);

        //新增新的审批人
        for (ClassroomBookUseTypeOrganizationAuditUser classroomBookUseTypeOrganizationAuditUser : list) {
            classroomBookUseTypeOrganizationAuditUser.setCreateTime(new Date());
            classroomBookUseTypeOrganizationAuditUserMapper.insertSelective(classroomBookUseTypeOrganizationAuditUser);
        }
        return new Result(Result.SUCCESS, "成功");
    }
}
