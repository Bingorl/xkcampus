package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.dao.ClassroomBookUseTypeMapper;
import com.biu.wifi.campus.dao.model.ClassroomBookUseType;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeCriteria;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUser;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUserCriteria;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/12/6.
 */
@Service
public class ClassroomBookUseTypeService {

    @Autowired
    private ClassroomBookUseTypeMapper classroomBookUseTypeMapper;
    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserService classroomBookUseTypeOrganizationAuditUserService;


    public ClassroomBookUseType findById(Integer id) {
        return classroomBookUseTypeMapper.selectByPrimaryKey(id);
    }
    /**
     * 教室预约使用类型列表
     *
     * @param example
     * @return
     */
    public List<ClassroomBookUseType> findList(ClassroomBookUseTypeCriteria example) {
        return classroomBookUseTypeMapper.selectByExample(example);
    }

    /**
     * 教室预约使用类型列表
     *
     * @param organizationId
     * @return
     */
    public List<ClassroomBookUseType> findList(Integer organizationId) {
        ClassroomBookUseTypeOrganizationAuditUserCriteria example = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andOrganizationIdEqualTo(organizationId);
        List<ClassroomBookUseTypeOrganizationAuditUser> auditUserList = classroomBookUseTypeOrganizationAuditUserService.findList(example);
        List<ClassroomBookUseType> classroomBookUseTypeList = new ArrayList<>();
        for (ClassroomBookUseTypeOrganizationAuditUser auditUser : auditUserList) {
            ClassroomBookUseType classroomBookUseType = classroomBookUseTypeMapper.selectByPrimaryKey(auditUser.getUseTypeId());
            if (classroomBookUseType.getIsDelete().intValue() == 2)
                classroomBookUseTypeList.add(classroomBookUseType);
        }
        return classroomBookUseTypeList;
    }

    /**
     * 教室预约使用类型列表
     *
     * @param example
     * @param extendParams 额外的参数
     * @return
     */
    public List<Map> findList(ClassroomBookUseTypeCriteria example, Map extendParams) {
        List<ClassroomBookUseType> classroomBookUseTypeList = findList(example);
        List<Map> mapList = new ArrayList<>();
        for (ClassroomBookUseType classroomBookUseType : classroomBookUseTypeList) {
            Map temp = BeanUtil.beanToMap(classroomBookUseType, extendParams);
            mapList.add(temp);
        }
        return mapList;
    }

    /**
     * 新增或编辑
     *
     * @param classroomBookUseType
     * @param isImport             是否是导入
     * @return
     */
    public Result addOrUpdate(ClassroomBookUseType classroomBookUseType, boolean isImport, List<Map> auditUserMapList) {
        ClassroomBookUseTypeCriteria example = new ClassroomBookUseTypeCriteria();
        ClassroomBookUseTypeCriteria.Criteria criteria = example.createCriteria()
                .andSchoolIdEqualTo(classroomBookUseType.getSchoolId())
                .andIsDeleteEqualTo((short) 2)
                .andNameEqualTo(classroomBookUseType.getName());

        if (classroomBookUseType.getId() != null) {
            criteria.andIdNotEqualTo(classroomBookUseType.getId());
        }

        long count = classroomBookUseTypeMapper.countByExample(example);
        if (!isImport && count > 0) {
            //批量导入时不会进入此判断
            return new Result(Result.CUSTOM_MESSAGE, "该教室预约使用类型已存在");
        }

        if (classroomBookUseType.getId() == null) {
            classroomBookUseType.setCreateTime(new Date());
            classroomBookUseTypeMapper.insertSelective(classroomBookUseType);
        } else {
            classroomBookUseType.setUpdateTime(new Date());
            classroomBookUseTypeMapper.updateByPrimaryKeySelective(classroomBookUseType);
        }

        //新增或编辑审批人员
        List<ClassroomBookUseTypeOrganizationAuditUser> users = new ArrayList<>();
        if (auditUserMapList != null) {
            for (Map map : auditUserMapList) {
                ClassroomBookUseTypeOrganizationAuditUser useTypeOrganizationAuditUser = new ClassroomBookUseTypeOrganizationAuditUser();
                useTypeOrganizationAuditUser.setOrganizationId(Integer.valueOf(map.get("organizationId").toString()));
                useTypeOrganizationAuditUser.setAuditUser(map.get("auditUser").toString());
                useTypeOrganizationAuditUser.setUseTypeId(classroomBookUseType.getId());
                users.add(useTypeOrganizationAuditUser);
            }

            Result result = classroomBookUseTypeOrganizationAuditUserService.addOrUpdate(users);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(Result.FAILURE, "失败");
            }
        }

        return new Result(Result.SUCCESS, "成功", classroomBookUseType);
    }

    /**
     * 批量导入教室预约使用类型数据
     *
     * @param schoolId
     * @param userId
     * @param list     教室预约使用类型原数据
     * @return
     */
    public Result batchAdd(Integer schoolId, Integer userId, List<Map<String, Object>> list) {
        Result result;
        Map<String, Object> map;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            ClassroomBookUseType ClassroomBookUseType = new ClassroomBookUseType();
            ClassroomBookUseType.setSchoolId(schoolId);
            ClassroomBookUseType.setName(map.get("name").toString());
            ClassroomBookUseType.setCreateBy(userId);
            result = addOrUpdate(ClassroomBookUseType, true, null);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(Result.FAILURE, "失败");
            }
        }
        return new Result(Result.SUCCESS, "成功");
    }

    public Result delete(Integer id) {
        ClassroomBookUseTypeCriteria example = new ClassroomBookUseTypeCriteria();
        example.createCriteria()
                .andIdEqualTo(id)
                .andIsDeleteEqualTo((short) 2);
        List<ClassroomBookUseType> list = classroomBookUseTypeMapper.selectByExample(example);
        if (list.size() == 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
        }

        ClassroomBookUseType classroomBookUseType = list.get(0);
        classroomBookUseType.setIsDelete((short) 1);
        classroomBookUseType.setDeleteTime(new Date());
        classroomBookUseTypeMapper.updateByPrimaryKeySelective(classroomBookUseType);
        return new Result(Result.SUCCESS, "成功");
    }
}
