package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.dao.ClassroomBuildingMapper;
import com.biu.wifi.campus.dao.ClassroomMapper;
import com.biu.wifi.campus.dao.model.ClassroomBuilding;
import com.biu.wifi.campus.dao.model.ClassroomBuildingCriteria;
import com.biu.wifi.campus.dao.model.ClassroomCriteria;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/12/5.
 */
@Service
public class ClassroomBuildingService {

    @Autowired
    private ClassroomBuildingMapper classroomBuildingMapper;
    @Autowired
    private ClassroomMapper classroomMapper;

    public ClassroomBuilding findById(Integer id) {
        return classroomBuildingMapper.selectByPrimaryKey(id);
    }

    /**
     * 教学楼列表
     *
     * @param example
     * @return
     */
    public List<ClassroomBuilding> findList(ClassroomBuildingCriteria example) {
        return classroomBuildingMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 教学楼列表
     *
     * @param example
     * @param extendParams 额外的参数
     * @return
     */
    public List<Map> findList(ClassroomBuildingCriteria example, Map extendParams) {
        List<ClassroomBuilding> classroomBuildingList = findList(example);
        List<Map> mapList = new ArrayList<>();
        for (ClassroomBuilding classroomBuilding : classroomBuildingList) {
            Map temp = BeanUtil.beanToMap(classroomBuilding, extendParams);
            mapList.add(temp);
        }
        return mapList;
    }

    /**
     * 新增或编辑
     *
     * @param classroomBuilding
     * @param isImport          是否是导入
     * @return
     */
    public Result addOrUpdate(ClassroomBuilding classroomBuilding, boolean isImport) {
        ClassroomBuildingCriteria example = new ClassroomBuildingCriteria();
        ClassroomBuildingCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(classroomBuilding.getSchoolId())
                .andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(classroomBuilding.getNo())) {
            criteria.andNoEqualTo(classroomBuilding.getNo());
        }

        if (StringUtils.isNotBlank(classroomBuilding.getName())) {
            criteria.andNameEqualTo(classroomBuilding.getName());
        }

        if (classroomBuilding.getId() != null) {
            criteria.andIdNotEqualTo(classroomBuilding.getId());
        }

        long count = classroomBuildingMapper.countByExample(example);
        if (!isImport && count > 0) {
            //批量导入时不会进入此判断
            return new Result(Result.CUSTOM_MESSAGE, "该教学楼已存在");
        }

        if (classroomBuilding.getId() == null) {
            classroomBuilding.setCreateTime(new Date());
            classroomBuildingMapper.insertSelective(classroomBuilding);
        } else {
            classroomBuilding.setUpdateTime(new Date());
            classroomBuildingMapper.updateByPrimaryKeySelective(classroomBuilding);
        }

        return new Result(Result.SUCCESS, "成功", classroomBuilding);
    }

    /**
     * 批量导入教学楼数据
     *
     * @param schoolId
     * @param list     教学楼原数据
     * @return
     */
    public Result batchAdd(Integer schoolId, List<Map<String, Object>> list) {
        //查询教务系统的教学楼数据
        Result result;
        Map<String, Object> map;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            ClassroomBuilding classroomBuilding = new ClassroomBuilding();
            classroomBuilding.setSchoolId(schoolId);
            classroomBuilding.setNo(map.get("no").toString());
            classroomBuilding.setName(map.get("name").toString());
            result = addOrUpdate(classroomBuilding, true);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(Result.FAILURE, "失败");
            }
        }
        return new Result(Result.SUCCESS, "成功");
    }

    public Result delete(Integer id, Integer schoolId) {
        ClassroomBuildingCriteria buildingEx = new ClassroomBuildingCriteria();
        buildingEx.createCriteria()
                .andIdEqualTo(id)
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        buildingEx.setOffset(0L);
        buildingEx.setLimit(1);
        List<ClassroomBuilding> classroomBuildingList = findList(buildingEx);
        if (classroomBuildingList.isEmpty()) {
            return new Result(Result.CUSTOM_MESSAGE, "该教学楼不存在");
        }

        ClassroomBuilding classroomBuilding = classroomBuildingList.get(0);

        ClassroomCriteria example = new ClassroomCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andBuildingIdEqualTo(id);
        long count = classroomMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该教学楼下有教室，不能删除");
        }

        classroomBuilding.setIsDelete((short) 1);
        classroomBuilding.setDeleteTime(new Date());
        classroomBuildingMapper.updateByPrimaryKeySelective(classroomBuilding);
        return new Result(Result.SUCCESS, "成功");
    }
}
