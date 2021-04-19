/**
 *
 */
package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.ServiceMenuBannerMapper;
import com.biu.wifi.campus.dao.model.ServiceMenuBanner;
import com.biu.wifi.campus.dao.model.ServiceMenuBannerCriteria;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin
 * @date 2018年9月30日
 * @Company: biu
 */
@Service
public class ServiceMenuBannerService {

    @Autowired
    private ServiceMenuBannerMapper serviceMenuBannerMapper;

    public int addBanner(ServiceMenuBanner entity) {
        try {
            return IbatisServiceUtils.insert(entity, serviceMenuBannerMapper);
        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public int deleteBanner(ServiceMenuBanner entity) {
        try {
            return IbatisServiceUtils.delete(entity, serviceMenuBannerMapper);
        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public int updateBanner(ServiceMenuBanner entity) {
        try {
            return IbatisServiceUtils.updateByPk(entity, serviceMenuBannerMapper);
        } catch (Exception e) {

            e.printStackTrace();
            return -1;
        }
    }

    public List<Map<String, Object>> findList() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<ServiceMenuBanner> list = serviceMenuBannerMapper.selectByExample(new ServiceMenuBannerCriteria());
        for (ServiceMenuBanner banner : list) {
            Map<String, Object> map = wrapper(banner);
            mapList.add(map);
        }
        return mapList;
    }

    public List<Map<String, Object>> findList(ServiceMenuBannerCriteria example) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<ServiceMenuBanner> list = serviceMenuBannerMapper.selectByExample(example);
        for (ServiceMenuBanner banner : list) {
            Map<String, Object> map = wrapper(banner);
            String type = MapUtils.getString(map, "type");
            if (StringUtils.equals("vote", type)) {
                map.put("typeText", "活动");
            } else if (StringUtils.equals("post", type)) {
                map.put("typeText", "帖子");
            } else {
                map.put("typeText", "外链网页");
            }
            mapList.add(map);
        }
        return mapList;
    }

    public Map<String, Object> wrapper(ServiceMenuBanner record) {
        BeanMap beanMap = BeanMap.create(record);
        Map<String, Object> map = new HashedMap(beanMap);
        return map;
    }

    public List<ServiceMenuBanner> findList(ServiceMenuBanner entity) {
        return IbatisServiceUtils.find(entity, serviceMenuBannerMapper);
    }

    public ServiceMenuBanner getBanner(ServiceMenuBanner entity) {
        return IbatisServiceUtils.get(entity, serviceMenuBannerMapper);
    }
}
