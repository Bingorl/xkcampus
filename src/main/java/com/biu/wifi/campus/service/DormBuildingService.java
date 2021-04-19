package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.DormBuildingMapper;
import com.biu.wifi.campus.dao.SchoolMapper;
import com.biu.wifi.campus.dao.model.DormBuilding;
import com.biu.wifi.campus.dao.model.DormBuildingCriteria;
import com.biu.wifi.campus.dao.model.School;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/10/30.
 */
@Service
public class DormBuildingService {

    @Autowired
    private DormBuildingMapper dormBuildingMapper;
    @Autowired
    private SchoolMapper schoolMapper;

    public List<DormBuilding> findList(DormBuildingCriteria example) {
        return dormBuildingMapper.selectByExample(example);
    }

    public List<Map<String, Object>> findMapList(DormBuildingCriteria example) {
        List<DormBuilding> buildings = dormBuildingMapper.selectByExample(example);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (DormBuilding building : buildings) {
            map = new HashedMap();
            map.put("id", building.getId());
            map.put("schoolId", building.getSchoolId());
            School school = schoolMapper.selectByPrimaryKey(building.getSchoolId());
            map.put("schoolName", building.getNo());//school.getName()
            map.put("areaPosition", building.getAreaPosition());
            map.put("no", building.getNo());
            map.put("unitNo", building.getUnitNo());
            map.put("phone", building.getPhone());
            list.add(map);
        }
        return list;
    }

    public void delete(Integer id, Integer schoolId) {
        DormBuildingCriteria example = new DormBuildingCriteria();
        example.createCriteria().andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId)
                .andIdEqualTo(id);
        List<DormBuilding> buildings = dormBuildingMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(buildings)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该宿舍楼信息不存在");
        }

        DormBuilding building = buildings.get(0);
        building.setIsDelete((short) 1);
        building.setDeleteTime(new Date());
        dormBuildingMapper.updateByPrimaryKeySelective(building);
    }

    public void addOrUpdateDormBuilding(DormBuilding dormBuilding) {
        DormBuildingCriteria example = new DormBuildingCriteria();
        DormBuildingCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(dormBuilding.getSchoolId())
                /*.andAreaPositionEqualTo(dormBuilding.getAreaPosition())*/
                .andNoEqualTo(dormBuilding.getNo())
                .andUnitNoEqualTo(dormBuilding.getUnitNo());
        if (dormBuilding.getId() != null) {
            criteria.andIdNotEqualTo(dormBuilding.getId());
        }

        long count = dormBuildingMapper.countByExample(example);
        if (count > 0) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该宿舍楼信息已存在，请勿重复添加");
        }

        if (dormBuilding.getId() == null) {
            dormBuilding.setCreateTime(new Date());
            dormBuildingMapper.insertSelective(dormBuilding);
        } else {
            dormBuilding.setUpdateTime(new Date());
            dormBuildingMapper.updateByPrimaryKeySelective(dormBuilding);
        }
    }

    public DormBuilding getById(Integer id) {
        return dormBuildingMapper.selectByPrimaryKey(id);
    }
}
