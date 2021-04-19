/**
 *
 */
package com.biu.wifi.campus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.ServiceMenuCategoryMapper;
import com.biu.wifi.campus.dao.ServiceMenuMapper;
import com.biu.wifi.campus.dao.model.ServiceMenu;
import com.biu.wifi.campus.dao.model.ServiceMenuCategory;
import com.biu.wifi.campus.dao.model.ServiceMenuCriteria;
import com.biu.wifi.campus.dao.model.ServiceMenuUserSchool;
import com.biu.wifi.campus.daoEx.ServiceMenuMapperEx;
import com.biu.wifi.campus.daoEx.ServiceMenuUserSchoolMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

/**
 *
 * @author zhangbin
 * @date 2018年9月29日
 * @Company: biu
 */
@Service
public class ServiceMenuService {

    @Autowired
    private ServiceMenuMapper serviceMenuMapper;

    @Autowired
    private ServiceMenuMapperEx serviceMenuMapperEx;

    @Autowired
    private ServiceMenuCategoryMapper serviceMenuCategoryMapper;

    @Autowired
    private ServiceMenuUserSchoolMapperEx serviceMenuUserSchoolMapperEx;

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    public int addServiceMenu(ServiceMenu entity) {
        try {
            IbatisServiceUtils.insert(entity, serviceMenuMapper);
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
    public ServiceMenu getServiceMenu(ServiceMenu entity) {
        return IbatisServiceUtils.get(entity, serviceMenuMapper);
    }

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    public int updateServiceMenu(ServiceMenu entity) {
        return IbatisServiceUtils.updateByPk(entity, serviceMenuMapper);
    }

    /**
     * 删除
     *
     * @param entity
     * @return
     */
    public int deleteServiceMenu(ServiceMenu entity) {
        try {
            return IbatisServiceUtils.delete(entity, serviceMenuMapper);
        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 查询集合
     *
     * @param entity
     * @return
     */
    public List<ServiceMenu> findServiceMenuList(ServiceMenu entity) {
        return IbatisServiceUtils.find(entity, serviceMenuMapper);
    }

    /**
     * 查询集合
     *
     * @param entity
     * @return
     */
    public List<ServiceMenu> findList(ServiceMenuCriteria example) {
        return serviceMenuMapper.selectByExample(example);
    }

    public List<ServiceMenu> findList(Integer categoryId, String keyword) {
        return serviceMenuMapperEx.findListByKeyword(keyword, categoryId);
    }

    public List<Map<String, Object>> findMapList(Integer categoryId, String keyword) {
        ServiceMenuCriteria example = new ServiceMenuCriteria();
        ServiceMenuCriteria.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }
        if (categoryId != null && categoryId != 0) {
            criteria.andCategoryIdEqualTo(categoryId);
        }
        List<ServiceMenu> menus = serviceMenuMapper.selectByExample(example);

        List<Map<String, Object>> maps = new ArrayList<>();
        for (ServiceMenu menu : menus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", menu.getId());
            map.put("name", menu.getName());
            map.put("icon", menu.getIcon());
            map.put("url", menu.getUrl());
            map.put("categoryId", menu.getCategoryId());
            ServiceMenuCategory category = serviceMenuCategoryMapper.selectByPrimaryKey(menu.getCategoryId());
            map.put("categoryName", category.getName());
            maps.add(map);
        }
        return maps;
    }

    /**
     * 根据关键字查询服务菜单集合
     *
     * @param entity
     * @return
     */
    public List<Map<String, Object>> findListByKeyword(String keyword, Integer schoolId, Integer userId) {
        // 根据关键字查询所有满足条件的服务菜单分类列表
        List<ServiceMenuCategory> categoryList;
        if (StringUtils.isBlank(keyword)) {
            categoryList = IbatisServiceUtils.find(new ServiceMenuCategory(), serviceMenuCategoryMapper);
        } else {
            categoryList = serviceMenuMapperEx.findCategoryListByKeyword(keyword);
        }

        List<Map<String, Object>> categoryMapList = new ArrayList<>();

        if (schoolId != null) {
            // 查询校园已开通的服务
            List<ServiceMenuUserSchool> menuUserSchools = serviceMenuUserSchoolMapperEx.selectListByExample(schoolId);
            if (menuUserSchools != null && menuUserSchools.size() > 0) {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("id", 0);
                categoryMap.put("categoryName", "校园已开通");
                List<Map<String, Object>> menuMapList = new ArrayList<>();
                for (ServiceMenuUserSchool school : menuUserSchools) {
                    ServiceMenu menu = serviceMenuMapper.selectByPrimaryKey(school.getServiceMenuId());
                    Map<String, Object> menuMap = new HashMap<>();
                    menuMap.put("id", menu.getId());
                    menuMap.put("name", menu.getName());
                    menuMap.put("icon", menu.getIcon());
                    menuMap.put("url", menu.getUrl());

                    if (StringUtils.isBlank(keyword) || menu.getName().contains(keyword)
                            || menu.getName().equals(keyword)) {
                        // 关键字为空
                        // 关键字是菜单名中的一些字符
                        menuMapList.add(menuMap);
                    }
                }
                categoryMap.put("menuMapList", menuMapList);
                if (menuMapList != null && menuMapList.size() > 0) {
                    categoryMapList.add(categoryMap);
                }
            }
        }

        // 所有服务分类下有子类的服务列表
        for (ServiceMenuCategory category : categoryList) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", category.getId());
            categoryMap.put("categoryName", category.getName());
            // 根据分类ID和关键字查询服务菜单列表
            keyword = keyword == null ? "" : keyword;
            List<ServiceMenu> menuList = serviceMenuMapperEx.findListByKeyword(keyword, category.getId());
            List<Map<String, Object>> menuMapList = new ArrayList<>();
            for (ServiceMenu menu : menuList) {
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("id", menu.getId());
                menuMap.put("name", menu.getName());
                menuMap.put("icon", menu.getIcon());
                menuMap.put("url", menu.getUrl());
                menuMapList.add(menuMap);
            }
            categoryMap.put("menuMapList", menuMapList);
            if (menuMapList.size() > 0) {
                // 返回子项的分类列表
                categoryMapList.add(categoryMap);
            }
        }

        return categoryMapList;
    }
}
