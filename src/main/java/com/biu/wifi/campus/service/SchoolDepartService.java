package com.biu.wifi.campus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.SchoolAddressBookMapper;
import com.biu.wifi.campus.dao.SchoolDepartMapper;
import com.biu.wifi.campus.dao.SchoolPositionMapper;
import com.biu.wifi.campus.dao.model.SchoolAddressBookCriteria;
import com.biu.wifi.campus.dao.model.SchoolDepart;
import com.biu.wifi.campus.dao.model.SchoolDepartCriteria;
import com.biu.wifi.campus.result.Result;

/**
 * @author zhangbin
 */
@Service
public class SchoolDepartService {

    @Autowired
    private SchoolAddressBookMapper schoolAddressBookMapper;
    @Autowired
    private SchoolDepartMapper schoolDepartMapper;
    @Autowired
    private SchoolPositionMapper schoolPositionMapper;

    public SchoolDepart getSchoolDepart(Integer id) {
        SchoolDepart depart = schoolDepartMapper.selectByPrimaryKey(id);
        depart = setPersonNum(depart);
        return depart;
    }

    public Map<String, Object> getSchoolDepartMap(Integer id) {
        SchoolDepart depart = schoolDepartMapper.selectByPrimaryKey(id);
        depart = setPersonNum(depart);
        Map<String, Object> map = new HashMap<>();
        map.put("id", depart.getId());
        map.put("pid", depart.getPid());
        SchoolDepart parent = getSchoolDepart(depart.getPid());
        if (parent != null) {
            map.put("pName", parent.getName());
        } else {
            map.put("pName", "");
        }
        map.put("name", depart.getName());
        map.put("level", depart.getLevel());
        map.put("personNum", depart.getPersonNum());
        map.put("schoolId", depart.getSchoolId());
        map.put("createTime", depart.getCreateTime());
        map.put("updateTime", depart.getUpdateTime());
        map.put("isDelete", depart.getIsDelete());
        return map;
    }

    public List<SchoolDepart> getSchoolDepartList(SchoolDepartCriteria example) {
        List<SchoolDepart> schoolDeparts = schoolDepartMapper.selectByExample(example);
        for (SchoolDepart depart : schoolDeparts) {
            depart = setPersonNum(depart);
        }
        return schoolDeparts;
    }

    public List<Map<String, Object>> getSchoolDeparMaptList(SchoolDepartCriteria example) {
        List<SchoolDepart> schoolDeparts = schoolDepartMapper.selectByExample(example);
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (SchoolDepart depart : schoolDeparts) {
            depart = setPersonNum(depart);
            if (depart == null) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", depart.getId());
            map.put("pid", depart.getPid());
            SchoolDepart parent = getSchoolDepart(depart.getPid());
            if (parent != null) {
                map.put("pName", parent.getName());
            } else {
                map.put("pName", "");
            }
            map.put("name", depart.getName());
            map.put("level", depart.getLevel());
            map.put("personNum", depart.getPersonNum());
            map.put("schoolId", depart.getSchoolId());
            map.put("createTime", depart.getCreateTime());
            map.put("updateTime", depart.getUpdateTime());
            maps.add(map);
        }
        return maps;
    }

    public Result addOrUpdate(SchoolDepart record) {
        // ??????level
        record = setLevel(record);
        // ??????????????????????????????
        if (record.getPid() != 0) {
            SchoolDepart pDepart = schoolDepartMapper.selectByPrimaryKey(record.getPid());
            if (pDepart.getPid() != 0) {
                return new Result(Result.CUSTOM_MESSAGE, "????????????????????????2???", null);
            }
        }

        // ?????????????????????????????????
        if (record.getPid() != 0) {
            SchoolDepart parent = schoolDepartMapper.selectByPrimaryKey(record.getPid());
            if (parent.getIsMajor() == 1) {
                return new Result(Result.CUSTOM_MESSAGE, "??????????????????????????????", null);
            }
        }

        // ????????????
        SchoolDepartCriteria example = new SchoolDepartCriteria();
        example.createCriteria().andSchoolIdEqualTo(record.getSchoolId()).andNameEqualTo(record.getName())
                .andPidEqualTo(record.getPid()).andLevelEqualTo(record.getLevel()).andIsDeleteEqualTo((short) 2);
        List<SchoolDepart> list = getSchoolDepartList(example);
        SchoolDepart query = null;
        if (CollectionUtils.isNotEmpty(list)) {
            query = list.get(0);
        }
        if (query != null && (record.getId() == null
                || (record.getId() != null && query.getId().intValue() == record.getId().intValue()))) {
            return new Result(Result.CUSTOM_MESSAGE, "??????????????????", null);
        }

        // ???????????????
        int result = 0;
        Date now = new Date();
        if (record.getId() == null) {
            record.setCreateTime(now);
            result = schoolDepartMapper.insertSelective(record);
        } else {
            record.setUpdateTime(now);
            result = schoolDepartMapper.updateByPrimaryKeySelective(record);
        }

        if (result > 0) {
            return new Result(Result.SUCCESS, "??????", record);
        } else {
            return new Result(Result.FAILURE, "??????", null);
        }
    }

    public Result delete(Integer id) {
        SchoolDepart query = getSchoolDepart(id);
        if (query == null || (query != null && query.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "??????????????????", null);
        }

        // ?????????????????????????????????
        SchoolAddressBookCriteria example = new SchoolAddressBookCriteria();
        example.createCriteria().andDepartIdEqualTo(id).andIsDeleteEqualTo((short) 2);
        long count = schoolAddressBookMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????????????????", null);
        }

        // ???????????????????????????????????????
        SchoolDepartCriteria schoolDepartEx = new SchoolDepartCriteria();
        schoolDepartEx.createCriteria().andPidEqualTo(id).andIsDeleteEqualTo((short) 2);
        List<SchoolDepart> departs = schoolDepartMapper.selectByExample(schoolDepartEx);
        for (SchoolDepart depart : departs) {
            example = new SchoolAddressBookCriteria();
            example.createCriteria().andDepartIdEqualTo(depart.getId()).andIsDeleteEqualTo((short) 2);
            long temp = schoolAddressBookMapper.countByExample(example);
            count += temp;
        }
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????????????????", null);
        }

        // ????????????????????????
        // ?????????????????????
        /*
         * SchoolPositionCriteria positionExample = new SchoolPositionCriteria();
         * positionExample.createCriteria().andDepartIdEqualTo(id).andIsDeleteNotEqualTo
         * ((short) 2); long positonCount =
         * schoolPositionMapper.countByExample(positionExample); if (positonCount > 0) {
         * return new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????", null); }
         */
        // ????????????????????????
        /*
         * SchoolAddressBookCriteria schoolAddressBookCriteria = new
         * SchoolAddressBookCriteria();
         * schoolAddressBookCriteria.createCriteria().andDepartIdEqualTo(id).
         * andIsDeleteNotEqualTo((short) 2); long schoolAddressBookCount =
         * schoolAddressBookMapper.countByExample(schoolAddressBookCriteria); if
         * (schoolAddressBookCount > 0) { return new Result(Result.CUSTOM_MESSAGE,
         * "????????????????????????????????????????????????", null); }
         */

        // ???????????????
        query.setIsDelete((short) 1);
        query.setUpdateTime(new Date());
        int result = schoolDepartMapper.updateByPrimaryKeySelective(query);
        if (result > 0) {
            SchoolDepart child = new SchoolDepart();
            child.setIsDelete((short) 1);
            child.setUpdateTime(new Date());

            SchoolDepartCriteria childExample = new SchoolDepartCriteria();
            childExample.createCriteria().andPidEqualTo(query.getId()).andIsDeleteEqualTo((short) 2);
            result = schoolDepartMapper.updateByExampleSelective(child, childExample);
            if (result > 0) {
                return new Result(Result.SUCCESS, "??????", null);
            } else {
                return new Result(Result.FAILURE, "??????", null);
            }
        } else {
            return new Result(Result.FAILURE, "??????", null);
        }
    }

    private SchoolDepart setLevel(SchoolDepart record) {
        record.setLevel((short) 1);
        if (record.getPid() != null) {
            SchoolDepartCriteria example = new SchoolDepartCriteria();
            example.createCriteria().andIdEqualTo(record.getPid()).andIsDeleteEqualTo((short) 2);
            List<SchoolDepart> list = getSchoolDepartList(example);
            if (CollectionUtils.isNotEmpty(list)) {
                SchoolDepart parent = list.get(0);
                short parentLevel = parent.getLevel();
                parentLevel++;
                record.setLevel(parentLevel);
            }
        }
        return record;
    }

    private SchoolDepart setPersonNum(SchoolDepart record) {
        if (record == null) {
            return null;
        }

        // ???????????????????????????????????????
        SchoolAddressBookCriteria example = new SchoolAddressBookCriteria();
        example.createCriteria().andDepartIdEqualTo(record.getId()).andIsDeleteEqualTo((short) 2);
        long count = schoolAddressBookMapper.countByExample(example);

        // ???????????????????????????????????????????????????
        if (record.getPid() == 0) {
            // ??????????????????????????????
            SchoolDepartCriteria departExample = new SchoolDepartCriteria();
            departExample.createCriteria().andPidEqualTo(record.getId()).andIsDeleteEqualTo((short) 2);
            List<SchoolDepart> departs = schoolDepartMapper.selectByExample(departExample);
            // ???????????????????????????????????????
            for (SchoolDepart depart : departs) {
                example = new SchoolAddressBookCriteria();
                example.createCriteria().andDepartIdEqualTo(depart.getId()).andIsDeleteEqualTo((short) 2);
                long tempCount = schoolAddressBookMapper.countByExample(example);
                count += tempCount;
            }
        }

        record.setPersonNum(Integer.valueOf(String.valueOf(count)));
        return record;
    }
}
