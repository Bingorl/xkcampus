package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.DictInfoMapper;
import com.biu.wifi.campus.dao.model.DictInfo;
import com.biu.wifi.campus.dao.model.DictInfoExample;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 张彬.
 * @date 2021/4/6 9:25.
 */
@Service
public class DictInfoService {

    @Autowired
    private DictInfoMapper dictInfoMapper;

    /**
     * 新增字典
     *
     * @param req
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:29.
     */
    public void add(DictInfo req) {
        if (exists(req) > 0) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该字典已存在");
        }

        req.setIsDelete((short) 2);
        req.setCreateTime(new Date());
        boolean result = dictInfoMapper.insertSelective(req) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "新增失败");
        }
    }

    /**
     * 修改字典
     *
     * @param req
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:30.
     */
    public void update(DictInfo req) {
        if (exists(req) > 0) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该字典已存在");
        }

        req.setIsDelete((short) 2);
        req.setUpdateTime(new Date());
        boolean result = dictInfoMapper.updateByPrimaryKeySelective(req) > 0;

        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "修改失败");
        }
    }

    /**
     * 删除字典
     *
     * @param ids
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:33.
     */
    public void delete(List<Integer> ids) {
        DictInfoExample example = new DictInfoExample();
        example.createCriteria()
                .andIdIn(ids)
                .andIsDeleteEqualTo((short) 2);

        DictInfo dictInfo = new DictInfo();
        dictInfo.setIsDelete((short) 1);
        dictInfo.setDeleteTime(new Date());
        boolean result = dictInfoMapper.updateByExample(dictInfo, example) > 0;
        if (!result) {
            throw new BizException(Result.CUSTOM_MESSAGE, "删除失败");
        }
    }

    /**
     * 根据上级id查询子字典集合
     *
     * @param pid
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:37.
     */
    public List<DictInfo> selectByPid(Integer pid) {
        DictInfoExample example = new DictInfoExample();
        example.createCriteria()
                .andPidEqualTo(pid)
                .andIsDeleteEqualTo((short) 2);
        return dictInfoMapper.selectByExample(example);
    }

    /**
     * 根据上级id查询子字典集合(Map对象集合)
     *
     * @param pid
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:37.
     */
    public List<Map> selectMapByPid(Integer pid) {
        List<DictInfo> list = selectByPid(pid);
        List<Map> result = new ArrayList<>();
        for (DictInfo dictInfo : list) {
            HashMap hashMap = new HashMap();
            hashMap.put("name", dictInfo.getName());
            hashMap.put("value", dictInfo.getId());
            result.add(hashMap);
        }
        return result;
    }

    /**
     * 根据id查询字典
     *
     * @param id
     * @return
     * @author 张彬.
     * @date 2021/4/7 0:35.
     */
    public DictInfo selectById(Integer id) {
        return dictInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据code查询字典
     *
     * @param code
     * @return
     * @author 张彬.
     * @date 2021/4/6 9:43.
     */
    public DictInfo selectByCode(String code) {
        DictInfoExample example = new DictInfoExample();
        DictInfoExample.Criteria criteria = example.createCriteria()
                .andCodeEqualTo(code)
                .andIsDeleteEqualTo((short) 2);

        List<DictInfo> dictInfos = dictInfoMapper.selectByExample(example);
        return CollectionUtils.isNotEmpty(dictInfos) ? dictInfos.get(0) : null;
    }

    private long exists(DictInfo req) {
        DictInfoExample example = new DictInfoExample();
        DictInfoExample.Criteria criteria = example.createCriteria()
                .andCodeEqualTo(req.getCode())
                .andIsDeleteEqualTo((short) 2);
        if (req.getId() != null) {
            criteria.andIdNotEqualTo(req.getId());
        }
        return dictInfoMapper.countByExample(example);
    }
}
