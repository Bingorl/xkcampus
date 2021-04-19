package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.NoticeInfoMapper;
import com.biu.wifi.campus.dao.model.NoticeInfo;
import com.biu.wifi.campus.dao.model.NoticeInfoCriteria;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/12/15.
 */
@Service
public class NoticeInfoService {

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;

    public List<NoticeInfo> findList(Integer userId) {
        NoticeInfoCriteria example = new NoticeInfoCriteria();
        example.setOrderByClause("id desc");
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andIsDeleteEqualTo((short) 2);
        return noticeInfoMapper.selectByExample(example);
    }

    /**
     * 关联的业务记录被删除后，删除此表的相关记录
     *
     * @param type  业务类型
     * @param bizId 业务ID
     * @return
     */
    public Result delete(Short type, Integer bizId) {
        NoticeInfoCriteria example = new NoticeInfoCriteria();
        example.createCriteria()
                .andTypeEqualTo(type)
                .andBizIdEqualTo(bizId);
        int count = noticeInfoMapper.deleteByExample(example);
        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }
}
