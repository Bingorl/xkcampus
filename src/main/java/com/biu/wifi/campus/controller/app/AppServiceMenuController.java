/**
 *
 */
package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.dao.ServiceMenuUserSchoolMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.ServiceMenuBannerService;
import com.biu.wifi.campus.service.ServiceMenuService;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author zhangbin
 * @date 2018年9月30日
 * @Company: biu
 */
@Controller
public class AppServiceMenuController extends AuthenticatorController {

    @Autowired
    private ServiceMenuService serviceMenuService;

    @Autowired
    private ServiceMenuBannerService serviceMenuBannerService;

    @Autowired
    private ServiceMenuUserSchoolMapper serviceMenuUserSchoolMapper;

    /**
     * 用户添加或者编辑我的服务
     *
     * @param userId
     * @param serviceMenuIdStr
     */
    @RequestMapping(value = "/app_addMyServiceMenu")
    public void app_addMyServiceMenu(@ModelAttribute("user_id") Integer userId, String serviceMenuIdStr,
                                     HttpServletResponse response) {
        if (userId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        // 删除旧的
        ServiceMenuUserSchoolCriteria example = new ServiceMenuUserSchoolCriteria();
        example.createCriteria().andUserIdEqualTo(userId);
        serviceMenuUserSchoolMapper.deleteByExample(example);

        if (StringUtils.isNotBlank(serviceMenuIdStr)) {
            // 添加新的
            String[] serviceMenuIdList = serviceMenuIdStr.split(",");
            for (String id : serviceMenuIdList) {
                ServiceMenuUserSchool record = new ServiceMenuUserSchool();
                record.setUserId(userId);
                record.setServiceMenuId(Integer.valueOf(id));
                serviceMenuUserSchoolMapper.insertSelective(record);
            }
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", null)));
    }

    /**
     * 服务菜单列表
     *
     * @param response
     * @param keyword  服务菜单名关键字
     * @param schoolId 学校ID
     */
    @RequestMapping("/app_serviceMenu")
    public void app_serviceMenu(@ModelAttribute("user_id") Integer userId,
                                @ModelAttribute("version") String version,
                                String keyword, Integer schoolId,
                                HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        Map<String, Object> result = new HashMap<>();
        // banner图列表
        ServiceMenuBannerCriteria bannerEx = new ServiceMenuBannerCriteria();
        bannerEx.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        List<Map<String, Object>> banners = serviceMenuBannerService.findList(bannerEx);
        result.put("bannerList", banners);
        result.put("banner", banners);
        // 用户已添加的服务列表
        List<Map<String, Object>> myServiceMenuList = new ArrayList<>();
        result.put("myServiceMenuList", myServiceMenuList);
        if (userId != null) {
            ServiceMenuUserSchoolCriteria example = new ServiceMenuUserSchoolCriteria();
            example.createCriteria().andUserIdEqualTo(userId);
            List<ServiceMenuUserSchool> menuUserSchools = serviceMenuUserSchoolMapper.selectByExample(example);
            for (ServiceMenuUserSchool school : menuUserSchools) {
                ServiceMenu menu = new ServiceMenu();
                menu.setId(school.getServiceMenuId());
                menu = serviceMenuService.getServiceMenu(menu);
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("id", menu.getId());
                menuMap.put("name", menu.getName());
                menuMap.put("icon", menu.getIcon());
                menuMap.put("url", menu.getUrl());
                menuMap.put("categoryId", menu.getCategoryId());

                if (StringUtils.isBlank(keyword) || menu.getName().contains(keyword)
                        || menu.getName().equals(keyword)) {
                    // 关键字为空
                    // 关键字是菜单名中的一些字符
                    myServiceMenuList.add(menuMap);
                }
            }
        }

        // 服务菜单选项列表
        List<Map<String, Object>> categoryMapList = serviceMenuService.findListByKeyword(keyword, schoolId, userId);
        result.put("menuCategoryList", categoryMapList);


        /*//todo IOS更新上架审核，屏蔽没有的服务功能(以河海和苏州两所大学为例)
        if (version.substring(0, 1).equals("V")) {
            result = iosUpSetting(result, schoolId);
        }*/

        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "请求成功", result)));
    }

    //todo IOS更新上架审核，屏蔽没有的服务功能(以河海和苏州两所大学为例)
    public Map<String, Object> iosUpSetting(Map<String, Object> result, Integer schoolId) {
        if (schoolId == 25) {
            //设置苏州大学的服务列表
            List<Map<String, Object>> myServiceMenuList = new ArrayList<>();
            result.put("myServiceMenuList", myServiceMenuList);

            //查询四六级查询、活动投票服务
            ServiceMenu menu = new ServiceMenu();
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", 0);
            categoryMap.put("categoryName", "校园已开通");


            Map<String, Object> menuMap, menuMap2;
            {
                menu.setId(61);
                menu = serviceMenuService.getServiceMenu(menu);
                menuMap = new HashMap<>();
                menuMap.put("id", menu.getId());
                menuMap.put("name", menu.getName());
                menuMap.put("icon", menu.getIcon());
                menuMap.put("url", menu.getUrl());
            }

            {
                menu = new ServiceMenu();
                menu.setId(62);
                menu = serviceMenuService.getServiceMenu(menu);
                menuMap2 = new HashMap<>();
                menuMap2.put("id", menu.getId());
                menuMap2.put("name", menu.getName());
                menuMap2.put("icon", menu.getIcon());
                menuMap2.put("url", menu.getUrl());
            }
            categoryMap.put("menuMapList", Arrays.asList(menuMap, menuMap2));
            result.put("menuCategoryList", Arrays.asList(categoryMap));
        } else if (schoolId == 26) {
            //河海大学,去除没有实现的服务功能
            List<String> contains = Arrays.asList(new String[]{
                    "通讯录", "成绩查询", "校历", "有道词典", "快递查询", "我的课表", "请假审批", "四六级查询", "活动投票", "教室预约"
            });
            List<Map<String, Object>> categoryMapList = (List<Map<String, Object>>) result.get("menuCategoryList");

            Iterator<Map<String, Object>> iterator = categoryMapList.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> categoryMap = iterator.next();
                if (!categoryMap.get("id").toString().equals("0")) {
                    iterator.remove();
                }

                List<Map<String, Object>> menuMapList = (List<Map<String, Object>>) categoryMap.get("menuMapList");
                Iterator<Map<String, Object>> iterator2 = menuMapList.iterator();
                while (iterator2.hasNext()) {
                    Map<String, Object> menuMap = iterator2.next();
                    if (!contains.contains(menuMap.get("name").toString())) {
                        iterator2.remove();
                    }
                }
            }

            iterator = categoryMapList.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> categoryMap = iterator.next();
                if (((List<Map<String, Object>>) categoryMap.get("menuMapList")).size() == 0) {
                    iterator.remove();
                }
            }
        }
        return result;
    }
}
