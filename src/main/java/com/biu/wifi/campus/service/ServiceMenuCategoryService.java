/**
 *
 */
package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.ServiceMenuCategoryMapper;
import com.biu.wifi.campus.dao.model.ServiceMenuCategory;
import com.biu.wifi.campus.dao.model.ServiceMenuCategoryCriteria;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

/**
 *
 * @author zhangbin
 * @date 2018年9月29日
 * @Company: biu
 */
@Service
public class ServiceMenuCategoryService {

    @Autowired
    private ServiceMenuCategoryMapper serviceMenuCategoryMapper;

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    public int addServiceMenuCategory(ServiceMenuCategory entity) {
        try {
            IbatisServiceUtils.insert(entity, serviceMenuCategoryMapper);
            return entity.getId();
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取
     *
     * @param entity
     * @return
     */
    public ServiceMenuCategory getServiceMenuCategory(Integer id) {
        return serviceMenuCategoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取
     *
     * @param entity
     * @return
     */
    public ServiceMenuCategory getServiceMenuCategory(ServiceMenuCategory entity) {
        return IbatisServiceUtils.get(entity, serviceMenuCategoryMapper);
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    public int updateServiceMenuCategory(ServiceMenuCategory entity) {
        return IbatisServiceUtils.updateByPk(entity, serviceMenuCategoryMapper);
    }

    /**
     * 查询集合
     *
     * @param entity
     * @return
     */
    public List<ServiceMenuCategory> findServiceMenuCategoryList(ServiceMenuCategoryCriteria example) {
        return serviceMenuCategoryMapper.selectByExample(example);
    }

    /**
     *
     * @param id
     */
    public int deleteServiceMenuCategory(Integer id) {
        try {
            IbatisServiceUtils.deleteByPk(id, serviceMenuCategoryMapper);
            return 1;
        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }
}
