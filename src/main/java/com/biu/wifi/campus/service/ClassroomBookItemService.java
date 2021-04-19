package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ClassroomBookItemMapper;
import com.biu.wifi.campus.dao.model.ClassroomBookItem;
import com.biu.wifi.campus.dao.model.ClassroomBookItemCriteria;
import com.biu.wifi.campus.daoEx.ClassroomBookItemMapperEx;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/12/8.
 */
@Service
public class ClassroomBookItemService {

    @Autowired
    private ClassroomBookItemMapper classroomBookItemMapper;
    @Autowired
    private ClassroomBookItemMapperEx classroomBookItemMapperEx;

    public Result add(ClassroomBookItem classroomBookItem) {
        ClassroomBookItemCriteria example = new ClassroomBookItemCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andBookIdEqualTo(classroomBookItem.getBookId())
                .andClassroomNoEqualTo(classroomBookItem.getClassroomNo())
                .andStartTimeEqualTo(classroomBookItem.getStartTime());
        long count = classroomBookItemMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已存在");
        }

        classroomBookItem.setCreateTime(new Date());
        classroomBookItemMapper.insertSelective(classroomBookItem);

        return new Result(Result.SUCCESS, "成功");
    }

    public Result delete(Integer id) {
        ClassroomBookItem classroomBookItem = classroomBookItemMapper.selectByPrimaryKey(id);
        if (classroomBookItem == null || (classroomBookItem != null && classroomBookItem.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
        }

        classroomBookItem.setIsDelete((short) 1);
        classroomBookItem.setDeleteTime(new Date());
        int result = classroomBookItemMapper.updateByPrimaryKeySelective(classroomBookItem);
        if (result > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    public Result deleteByExample(ClassroomBookItemCriteria example) {
        ClassroomBookItem classroomBookItem = new ClassroomBookItem();
        classroomBookItem.setIsDelete((short) 1);
        classroomBookItem.setDeleteTime(new Date());
        int count = classroomBookItemMapper.updateByExampleSelective(classroomBookItem, example);
        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    public List<ClassroomBookItem> findList(ClassroomBookItemCriteria example) {
        return classroomBookItemMapper.selectByExample(example);
    }

    public boolean findBookedCount(Integer classroomBookId, Integer classroomBuildingId, String classroomNo, String startTime) {
        long count = classroomBookItemMapperEx.findBookedCount(classroomBookId, classroomBuildingId, classroomNo, startTime);
        return count > 0 ? true : false;
    }
}
