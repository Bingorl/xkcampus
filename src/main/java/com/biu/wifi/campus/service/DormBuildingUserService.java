package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.DormBuildingUserMapper;
import com.biu.wifi.campus.dao.UserMapper;
import com.biu.wifi.campus.dao.model.DormBuildingUser;
import com.biu.wifi.campus.dao.model.DormBuildingUserCriteria;
import com.biu.wifi.campus.dao.model.User;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/10/31.
 */
@Service
public class DormBuildingUserService {

    @Autowired
    private DormBuildingUserMapper dormBuildingUserMapper;

    @Autowired
    private UserMapper userMapper;

    public void addOrUpdate(DormBuildingUser buildingUser) {
        DormBuildingUserCriteria example = new DormBuildingUserCriteria();
        DormBuildingUserCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andBuildingIdEqualTo(buildingUser.getBuildingId())
                .andUserIdEqualTo(buildingUser.getUserId());
        if (buildingUser.getId() != null) {
            criteria.andIdNotEqualTo(buildingUser.getId());
        }

        long count = dormBuildingUserMapper.countByExample(example);
        if (count > 0) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该宿管人员已存在，请勿重复添加");
        }

        User user = userMapper.selectByPrimaryKey(buildingUser.getUserId());
        buildingUser.setUsername(user.getName());
        buildingUser.setPhone(user.getPhone());

        if (buildingUser.getId() == null) {
            buildingUser.setCreateTime(new Date());
            dormBuildingUserMapper.insertSelective(buildingUser);
        } else {
            buildingUser.setUpdateTime(new Date());
            dormBuildingUserMapper.updateByPrimaryKeySelective(buildingUser);
        }
    }

    @Transactional
    public void addOrUpdate(Integer buildingId, String[] userIdList) {
        //删除该楼所有的宿管
        DormBuildingUserCriteria example = new DormBuildingUserCriteria();
        DormBuildingUserCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2)
                .andBuildingIdEqualTo(buildingId);
        DormBuildingUser delete = new DormBuildingUser();
        delete.setIsDelete((short) 1);
        delete.setDeleteTime(new Date());

        //删除
        dormBuildingUserMapper.updateByExampleSelective(delete, example);
        //添加新的
        DormBuildingUser buildingUser;
        for (String id : userIdList) {
            buildingUser = new DormBuildingUser();
            buildingUser.setBuildingId(buildingId);
            int userId = Integer.valueOf(id);
            buildingUser.setUserId(userId);
            User user = userMapper.selectByPrimaryKey(userId);
            buildingUser.setUsername(user.getName());
            buildingUser.setPhone(user.getPhone());
            addOrUpdate(buildingUser);
        }
    }

    public List<DormBuildingUser> findList(DormBuildingUserCriteria example) {
        return dormBuildingUserMapper.selectByExample(example);
    }

    public DormBuildingUser findById(Integer id) {
        return dormBuildingUserMapper.selectByPrimaryKey(id);
    }

    public void delete(Integer id) {
        DormBuildingUser user = dormBuildingUserMapper.selectByPrimaryKey(id);
        if (user == null || (user != null && user.getIsDelete() == 2)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该记录已被删除，请勿重复操作");
        }

        user.setIsDelete((short) 1);
        user.setDeleteTime(new Date());
        dormBuildingUserMapper.updateByPrimaryKeySelective(user);
    }
}
