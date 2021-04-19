package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.dao.ClassroomBookUseTypeOrganizationAuditUserMapper;
import com.biu.wifi.campus.dao.ClassroomBookUseTypeOrganizationMapper;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganization;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUser;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUserCriteria;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationCriteria;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/6.
 */
@Service
public class ClassroomBookUseTypeOrganizationService {

    @Autowired
    private ClassroomBookUseTypeOrganizationMapper classroomBookUseTypeOrganizationMapper;
    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserMapper classroomBookUseTypeOrganizationAuditUserMapper;

    public ClassroomBookUseTypeOrganization findById(Integer id) {
        return classroomBookUseTypeOrganizationMapper.selectByPrimaryKey(id);
    }

    /**
     * 组织列表
     *
     * @param example
     * @return
     */
    public List<ClassroomBookUseTypeOrganization> findList(ClassroomBookUseTypeOrganizationCriteria example) {
        return classroomBookUseTypeOrganizationMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 组织列表
     *
     * @param useTypeId
     * @param updateFlag
     * @return
     */
    public List<Map> findList(Integer schoolId, Integer useTypeId, Integer updateFlag) {
        ClassroomBookUseTypeOrganizationCriteria example = new ClassroomBookUseTypeOrganizationCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        List<Map> classroomBookUseTypeOrganizationList = findList(example, new HashMap());
        if (updateFlag.intValue() != 1) {
            //指定流程下的所有的组织列表，没有选中标识
            return classroomBookUseTypeOrganizationList;
        } else {
            //指定流程下的组织列表，有选中标识
            for (Map map : classroomBookUseTypeOrganizationList) {
                ClassroomBookUseTypeOrganizationAuditUserCriteria useEx = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
                useEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andOrganizationIdEqualTo(Integer.valueOf(map.get("id").toString()))
                        .andUseTypeIdEqualTo(useTypeId);
                long count = classroomBookUseTypeOrganizationAuditUserMapper.countByExample(useEx);
                map.put("selected", count > 0 ? true : false);
            }
            return classroomBookUseTypeOrganizationList;
        }
    }

    /**
     * 组织列表
     *
     * @param example
     * @param extendParams  额外的参数
     * @param excludeParams 需要排除的参数
     * @return
     */
    public List<Map> findList(ClassroomBookUseTypeOrganizationCriteria example, Map extendParams, String... excludeParams) {
        List<ClassroomBookUseTypeOrganization> classroomBookUseTypeOrganizationList = findList(example);
        List<Map> mapList = new ArrayList<>();
        for (ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization : classroomBookUseTypeOrganizationList) {
            Map temp = BeanUtil.beanToMap(classroomBookUseTypeOrganization, extendParams, excludeParams);
            switch (classroomBookUseTypeOrganization.getType()) {
                case 1:
                    temp.put("typeName", "学院");
                    break;
                case 2:
                    temp.put("typeName", "部门");
                    break;
                case 3:
                    temp.put("typeName", "活动组织单位");
                    break;
            }

            mapList.add(temp);
        }
        return mapList;
    }

    /**
     * 新增或编辑
     *
     * @param classroomBookUseTypeOrganization
     * @param isImport                         是否是导入
     * @return
     */
    public Result addOrUpdate(ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization, boolean isImport) {
        ClassroomBookUseTypeOrganizationCriteria example = new ClassroomBookUseTypeOrganizationCriteria();
        ClassroomBookUseTypeOrganizationCriteria.Criteria criteria = example.createCriteria()
                .andSchoolIdEqualTo(classroomBookUseTypeOrganization.getSchoolId())
                .andIsDeleteEqualTo((short) 2)
                .andTypeEqualTo(classroomBookUseTypeOrganization.getType())
                .andNameEqualTo(classroomBookUseTypeOrganization.getName());

        if (classroomBookUseTypeOrganization.getInstituteId() != null) {
            criteria.andInstituteIdEqualTo(classroomBookUseTypeOrganization.getInstituteId());
        }

        if (classroomBookUseTypeOrganization.getId() != null) {
            criteria.andIdNotEqualTo(classroomBookUseTypeOrganization.getId());
        }

        long count = classroomBookUseTypeOrganizationMapper.countByExample(example);
        if (!isImport && count > 0) {
            //批量导入时不会进入此判断
            return new Result(Result.CUSTOM_MESSAGE, "该组织已存在");
        }

        if (classroomBookUseTypeOrganization.getId() == null) {
            classroomBookUseTypeOrganization.setCreateTime(new Date());
            classroomBookUseTypeOrganizationMapper.insertSelective(classroomBookUseTypeOrganization);
        } else {
            classroomBookUseTypeOrganization.setUpdateTime(new Date());
            classroomBookUseTypeOrganizationMapper.updateByPrimaryKeySelective(classroomBookUseTypeOrganization);
        }

        return new Result(Result.SUCCESS, "成功", classroomBookUseTypeOrganization);
    }

    /**
     * 批量导入组织数据
     *
     * @param schoolId
     * @param userId
     * @param list     组织原数据
     * @return
     */
    public Result batchAdd(Integer schoolId, Integer userId, List<Map<String, Object>> list) {
        Result result;
        Map<String, Object> map;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = new ClassroomBookUseTypeOrganization();
            classroomBookUseTypeOrganization.setSchoolId(schoolId);
            classroomBookUseTypeOrganization.setType(Short.valueOf(map.get("type").toString()));
            classroomBookUseTypeOrganization.setInstituteId(Integer.valueOf(map.get("instituteId").toString()));
            classroomBookUseTypeOrganization.setName(map.get("name").toString());
            classroomBookUseTypeOrganization.setRemark(map.get("remark").toString());
            result = addOrUpdate(classroomBookUseTypeOrganization, true);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(Result.FAILURE, "失败");
            }
        }
        return new Result(Result.SUCCESS, "成功");
    }

    public Result delete(Integer id) {
        //删除组织
        ClassroomBookUseTypeOrganizationCriteria example = new ClassroomBookUseTypeOrganizationCriteria();
        example.createCriteria()
                .andIdEqualTo(id)
                .andIsDeleteEqualTo((short) 2);
        List<ClassroomBookUseTypeOrganization> list = classroomBookUseTypeOrganizationMapper.selectByExample(example);
        if (list.size() == 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
        }

        ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = list.get(0);
        classroomBookUseTypeOrganization.setIsDelete((short) 1);
        classroomBookUseTypeOrganization.setDeleteTime(new Date());
        classroomBookUseTypeOrganizationMapper.updateByPrimaryKeySelective(classroomBookUseTypeOrganization);

        //删除组织审批人员
        ClassroomBookUseTypeOrganizationAuditUserCriteria auditUserEx = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
        auditUserEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andOrganizationIdEqualTo(id);
        ClassroomBookUseTypeOrganizationAuditUser classroomBookUseTypeOrganizationAuditUser = new ClassroomBookUseTypeOrganizationAuditUser();
        classroomBookUseTypeOrganizationAuditUser.setIsDelete((short) 1);
        classroomBookUseTypeOrganizationAuditUser.setDeleteTime(new Date());
        classroomBookUseTypeOrganizationAuditUserMapper.updateByExample(classroomBookUseTypeOrganizationAuditUser, auditUserEx);
        return new Result(Result.SUCCESS, "成功");
    }
}
