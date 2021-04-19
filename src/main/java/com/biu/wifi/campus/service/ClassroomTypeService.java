package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ClassroomMapper;
import com.biu.wifi.campus.dao.ClassroomTypeMapper;
import com.biu.wifi.campus.dao.model.ClassroomCriteria;
import com.biu.wifi.campus.dao.model.ClassroomType;
import com.biu.wifi.campus.dao.model.ClassroomTypeCriteria;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/12/11.
 */
@Service
public class ClassroomTypeService {

    @Autowired
    private ClassroomTypeMapper classroomTypeMapper;
    @Autowired
    private ClassroomMapper classroomMapper;

    public Result delete(Integer id, Integer schoolId) {
        ClassroomTypeCriteria example = new ClassroomTypeCriteria();
        example.createCriteria()
                .andIdEqualTo(id)
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        List<ClassroomType> classroomTypeList = findList(example);
        if (classroomTypeList.isEmpty()) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
        }

        ClassroomCriteria classroomEx = new ClassroomCriteria();
        classroomEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andTypeIdEqualTo(id);
        long count = classroomMapper.countByExample(classroomEx);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该教师类型下有教室，不能删除");
        }

        ClassroomType classroomType = classroomTypeList.get(0);

        classroomType.setIsDelete((short) 1);
        classroomType.setDeleteTime(new Date());
        int result = classroomTypeMapper.updateByPrimaryKeySelective(classroomType);
        if (result > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    public Result addOrUpdate(ClassroomType classroomType) {
        ClassroomTypeCriteria example = new ClassroomTypeCriteria();
        ClassroomTypeCriteria.Criteria criteria = example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andNameEqualTo(classroomType.getName())
                .andCodeEqualTo(classroomType.getCode());

        if (classroomType.getId() != null) {
            criteria.andIdNotEqualTo(classroomType.getId());
        }
        if (classroomType.getSchoolId() != null) {
            //平台的schoolId为null
            criteria.andSchoolIdEqualTo(classroomType.getSchoolId());
        }


        long count = classroomTypeMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已存在");
        }

        if (classroomType.getId() != null) {
            classroomType.setUpdateTime(new Date());
            count = classroomTypeMapper.updateByPrimaryKeySelective(classroomType);
        } else {
            classroomType.setCreateTime(new Date());
            count = classroomTypeMapper.insertSelective(classroomType);
        }

        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    public ClassroomType find(Integer typeId) {
        ClassroomTypeCriteria example = new ClassroomTypeCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIdEqualTo(typeId)
                .andIsDeleteEqualTo((short) 2);
        List<ClassroomType> classroomTypeList = classroomTypeMapper.selectByExample(example);
        if (classroomTypeList.size() == 0) {
            return null;
        } else {
            return classroomTypeList.get(0);
        }
    }

    public List<ClassroomType> findList(ClassroomTypeCriteria example) {
        return classroomTypeMapper.selectByExample(example);
    }
}
