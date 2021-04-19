package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.AuditInfoMapper;
import com.biu.wifi.campus.dao.model.AuditInfo;
import com.biu.wifi.campus.dao.model.AuditInfoCriteria;
import com.biu.wifi.campus.daoEx.AuditInfoMapperEx;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/12/14.
 */
@Service
public class AuditInfoService {

    @Autowired
    private AuditInfoMapper auditInfoMapper;
    @Autowired
    private AuditInfoMapperEx auditInfoMapperEx;

    public List<AuditInfo> findList(Short type, Integer userId, Integer bizId, String orderByField, boolean asc) {
        AuditInfoCriteria example = new AuditInfoCriteria();
        String orderByClause = orderByField;
        if (!asc) {
            orderByClause += " desc";
        }
        example.setOrderByClause(orderByClause);
        AuditInfoCriteria.Criteria criteria = example.createCriteria()
                .andTypeEqualTo(type)
                .andUserIdEqualTo(userId);
        if (bizId != null) {
            criteria.andBizIdEqualTo(bizId);
        }
        return auditInfoMapper.selectByExample(example);
    }

    public List<AuditInfo> findList(Short type, Integer userId, String orderByField, boolean asc) {
        return findList(type, userId, null, orderByField, asc);
    }

    /**
     * 根据业务类型和业务id查询审批记录列表
     *
     * @param type
     * @param bizId
     * @return
     * @author 张彬.
     * @date 2021/4/3 20:27.
     */
    public List<AuditInfo> findList(Short type, Integer bizId) {
        AuditInfoCriteria example = new AuditInfoCriteria();
        example.createCriteria()
                .andBizIdEqualTo(bizId)
                .andTypeEqualTo(type);
        return auditInfoMapper.selectByExample(example);
    }

    /**
     * 根据业务类型和业务id查询审批记录列表（Map对象列表）
     *
     * @param type
     * @param bizId
     * @return
     * @author 张彬.
     * @date 2021/4/3 20:27.
     */
    public List<HashMap> findMapList(Short type, Integer bizId) {
        return auditInfoMapperEx.findMapList(type, bizId);
    }

    public List<Map> findList(Integer userId) {
        return auditInfoMapperEx.findList(userId);
    }


    /**
     * 关联的业务记录被删除后，删除此表的相关记录
     *
     * @param type  业务类型
     * @param bizId 业务ID
     * @return
     */
    public Result delete(Short type, Integer bizId) {
        AuditInfoCriteria example = new AuditInfoCriteria();
        example.createCriteria()
                .andTypeEqualTo(type)
                .andBizIdEqualTo(bizId);
        int count = auditInfoMapper.deleteByExample(example);
        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    /**
     * 新增审核记录
     *
     * @param auditInfo
     * @return
     * @author 张彬.
     * @date 2021/4/3 20:05.
     */
    public int add(AuditInfo auditInfo) {
        return auditInfoMapper.insertSelective(auditInfo);
    }

    /**
     * 根据审批类型、审批业务id和审核人id更新审批状态
     *
     * @param bizId
     * @param userId
     * @param type
     * @param isPass
     * @return
     * @author 张彬.
     * @date 2021/4/4 19:27.
     */
    public int update(Integer bizId, Integer userId, short type, Short isPass) {
        return auditInfoMapper.update(bizId, userId, type, isPass);
    }
}
