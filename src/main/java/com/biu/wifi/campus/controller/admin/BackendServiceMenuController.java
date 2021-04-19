/**
 *
 */
package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.dao.ServiceMenuUserSchoolMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.ServiceMenuUserSchoolMapperEx;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author zhangbin
 * @date 2018年9月29日
 * @Company: biu
 */
@Controller
public class BackendServiceMenuController {

    @Autowired
    private ServiceMenuService serviceMenuService;

    @Autowired
    private ServiceMenuCategoryService serviceMenuCategoryService;

    @Autowired
    private ServiceMenuBannerService serviceMenuBannerService;

    @Autowired
    private ServiceMenuUserSchoolMapper serviceMenuUserSchoolMapper;

    @Autowired
    private ServiceMenuUserSchoolMapperEx serviceMenuUserSchoolMapperEx;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private JwCjcxService jwCjcxService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    /**
     * element ui upload 文件上传
     *
     * @param response
     */
    @RequestMapping("back_api_upload_icon")
    public void doUploadImg(HttpServletResponse response) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        List<DiskFileItem> iconImg = null;
        String fileid = null;
        try {
            iconImg = (List<DiskFileItem>) param.get("file");
        } catch (Exception e) {
            iconImg = null;
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
        if (iconImg != null) {
            DiskFileItem fileBean = iconImg.get(0);
            byte[] fileContent = fileBean.get();
            String fileName = FileUtilsEx.getFileNameByPath(fileBean.getName());
            fileid = NginxFileUtils.add(fileName, fileContent, "ds_upload", null);
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", fileid));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 添加服务菜单分类
     *
     * @param pid
     * @param name
     * @param response
     */
    @RequestMapping("/back_api_addServiceMenuCategory")
    public void addCategory(Integer pid, String name, HttpServletResponse response) {
        if (name == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        ServiceMenuCategory entity = new ServiceMenuCategory();
        entity.setPid(pid);
        entity.setName(name);

        ServiceMenuCategory query = serviceMenuCategoryService.getServiceMenuCategory(entity);
        if (query != null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该分类已存在", null));
            return;
        }

        int result = serviceMenuCategoryService.addServiceMenuCategory(entity);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 删除服务菜单分类
     *
     * @param id
     * @param response
     */
    @RequestMapping("/back_api_deleteServiceMenuCategory")
    public void deleteCategory(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }

        List<ServiceMenu> menuList = serviceMenuService.findList(id, "");
        if (menuList != null && menuList.size() > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该分类下有服务，不能被删除", null));
        }

        int result = serviceMenuCategoryService.deleteServiceMenuCategory(id);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 查询服务菜单分类列表
     *
     * @param page
     * @param name
     * @param response
     */
    @RequestMapping("/back_api_findMenuCategoryList")
    public void findMenuCategoryList(Integer page, Integer pageSize, String name, HttpServletResponse response) {
        if (page == null || name == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        if (pageSize != null && pageSize != 0) {
            // 每页记录数不为0时进行分页
            PageLimitHolderFilter.setContext(page, pageSize, null);
        }

        ServiceMenuCategoryCriteria example = null;
        if (!"".equals(name)) {
            new ServiceMenuCategoryCriteria();
            example.createCriteria().andNameLike(name);
        }

        List<ServiceMenuCategory> list = serviceMenuCategoryService.findServiceMenuCategoryList(example);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", map));
    }

    /**
     * 添加服务菜单
     *
     * @param icon
     * @param name
     * @param url
     * @param categoryId
     * @param response
     */
    @RequestMapping("/back_api_addServiceMenu")
    public void addMenu(String icon, String name, String url, Integer categoryId, HttpServletResponse response) {
        if (icon == null || name == null || categoryId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        ServiceMenuCategory category = serviceMenuCategoryService.getServiceMenuCategory(categoryId);
        if (category == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该分类已被删除", null));
            return;
        }

        ServiceMenu entity = new ServiceMenu();
        entity.setName(name);
        entity.setCategoryId(categoryId);

        ServiceMenu query = serviceMenuService.getServiceMenu(entity);
        if (query != null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该服务已存在", null));
            return;
        }

        entity.setUrl(url);
        entity.setIcon(icon);

        int result = serviceMenuService.addServiceMenu(entity);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 删除服务菜单
     *
     * @param id
     * @param response
     */
    @RequestMapping("/back_api_deleteServiceMenu")
    public void deleteMenu(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        ServiceMenu query = new ServiceMenu();
        query.setId(id);
        query = serviceMenuService.getServiceMenu(query);
        if (query == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该服务已被删除", null));
            return;
        }

        ServiceMenuUserSchoolCriteria example = new ServiceMenuUserSchoolCriteria();
        example.createCriteria().andServiceMenuIdEqualTo(id);
        List<ServiceMenuUserSchool> schools = serviceMenuUserSchoolMapper.selectByExample(example);
        if (schools != null && schools.size() > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该服务被校园使用中，不能被删除", null));
            return;
        }

        ServiceMenu entity = new ServiceMenu();
        entity.setId(id);
        int result = serviceMenuService.deleteServiceMenu(entity);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 编辑服务菜单
     *
     * @param icon
     * @param name
     * @param url
     * @param categoryId
     * @param response
     */
    @RequestMapping("/back_api_updateServiceMenu")
    public void updateMenu(Integer id, String name, String icon, String url, Integer categoryId,
                           HttpServletResponse response) {
        // 校验参数
        if (id == null || name == null || icon == null || categoryId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        // 防重校验
        ServiceMenu query = new ServiceMenu();
        query.setName(name);
        query.setCategoryId(categoryId);
        query = serviceMenuService.getServiceMenu(query);
        if (query != null && query.getId() != id) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该服务已存在", null));
            return;
        }

        // 分类校验
        ServiceMenuCategory category = serviceMenuCategoryService.getServiceMenuCategory(categoryId);
        if (category == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该分类已被删除", null));
            return;
        }

        // 根据ID查询
        ServiceMenu entity = new ServiceMenu();
        entity.setId(id);
        entity = serviceMenuService.getServiceMenu(entity);
        if (entity == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该服务已被删除", null));
            return;
        }

        // 更新
        entity.setName(name);
        if (StringUtils.isNotBlank(icon)) {
            // 上传了图标后才进行更新
            entity.setIcon(icon);
        }
        entity.setUrl(url);
        entity.setCategoryId(categoryId);
        int result = serviceMenuService.updateServiceMenu(entity);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 服务菜单列表
     *
     * @param page
     * @param pageSize
     * @param name
     * @param categoryId
     * @param response
     */
    @RequestMapping("/back_api_findServiceMenuList")
    public void findMenuList(Integer page, Integer pageSize, String name, Integer categoryId,
                             HttpServletResponse response) {
        if (page == null || pageSize == null || name == null || categoryId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);

        List<Map<String, Object>> list = serviceMenuService.findMapList(categoryId, name);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", map));
    }

    /**
     * 添加校园已开通服务
     *
     * @param schoolId
     * @param serviceMenuId
     * @param response
     */
    @RequestMapping("/back_api_addSchoolServiceMenuList")
    public void addSchoolMenu(Integer schoolId, Integer serviceMenuId, HttpServletResponse response) {
        if (schoolId == null || serviceMenuId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        ServiceMenuUserSchoolCriteria example = new ServiceMenuUserSchoolCriteria();
        ServiceMenuUserSchoolCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        // 查询当前校园开通服务总数
        long total = serviceMenuUserSchoolMapper.countByExample(example);
        // 校验是否已开通对应服务
        criteria.andServiceMenuIdEqualTo(serviceMenuId);
        long count = serviceMenuUserSchoolMapper.countByExample(example);
        if (count > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该服务已开通", null));
            return;
        }

        // 开通服务
        ServiceMenuUserSchool school = new ServiceMenuUserSchool();
        school.setSchoolId(schoolId);
        school.setServiceMenuId(serviceMenuId);
        school.setSort(Integer.valueOf(String.valueOf(total)) + 1);
        school.setSortTime(new Date());

        int result = serviceMenuUserSchoolMapper.insertSelective(school);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 删除校园已开通服务
     *
     * @param schoolId
     * @param serviceMenuId
     * @param response
     */
    @RequestMapping("/back_api_deleteSchoolServiceMenuList")
    public void deleteSchoolMenu(Integer schoolId, Integer serviceMenuId, HttpServletResponse response) {
        if (schoolId == null || serviceMenuId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        // 校验是否已开通对应服务
        ServiceMenuUserSchoolCriteria example = new ServiceMenuUserSchoolCriteria();
        example.createCriteria().andSchoolIdEqualTo(schoolId).andServiceMenuIdEqualTo(serviceMenuId);
        long count = serviceMenuUserSchoolMapper.countByExample(example);
        if (count == 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该服务已关闭", null));
            return;
        }

        // 删除开通的服务
        int result = serviceMenuUserSchoolMapper.deleteByExample(example);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 查询校园已开通服务列表
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param keyword
     * @param categoryId
     * @param status
     * @param response
     */
    @RequestMapping("/back_api_findSchoolServiceMenuList")
    public void findSchoolServiceMenuList(Integer page, Integer pageSize, Integer schoolId, String keyword,
                                          Integer categoryId, Integer status, HttpServletResponse response) {
        if (page == null || pageSize == null || schoolId == null || categoryId == null || status == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

//		PageLimitHolderFilter.setContext(page, pageSize, null);

        List<Map<String, Object>> list = new ArrayList<>();
        long totalOpenCout = 0;

        // 0全部 1已开通 2未开通
        if (status == 0 || status == 2) {
            ServiceMenuCriteria ex = new ServiceMenuCriteria();
            ServiceMenuCriteria.Criteria criteria = ex.createCriteria();
            if (StringUtils.isNotBlank(keyword)) {
                criteria.andNameLike("%" + keyword + "%");
            }
            if (categoryId != 0) {
                criteria.andCategoryIdEqualTo(categoryId);
            }
            List<ServiceMenu> menus = serviceMenuService.findList(ex);
            for (ServiceMenu menu : menus) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", menu.getId());
                map.put("name", menu.getName());
                map.put("icon", menu.getIcon());
                map.put("url", menu.getUrl());
                map.put("categoryId", menu.getCategoryId());
                ServiceMenuCategory category = serviceMenuCategoryService.getServiceMenuCategory(menu.getCategoryId());
                map.put("categoryName", category.getName());
                if (schoolId != null && schoolId != 0) {
                    School school = schoolService.getById(schoolId);
                    map.put("schoolName", school.getName());
                } else {
                    map.put("schoolName", "");
                }

                ServiceMenuUserSchoolCriteria example = new ServiceMenuUserSchoolCriteria();
                ServiceMenuUserSchoolCriteria.Criteria criteria2 = example.createCriteria();
                criteria2.andSchoolIdEqualTo(schoolId).andServiceMenuIdEqualTo(menu.getId());
                long count = serviceMenuUserSchoolMapper.countByExample(example);
                if (count > 0) {
                    map.put("openBtn", false);
                    map.put("closeBtn", true);
                } else {
                    map.put("openBtn", true);
                    map.put("closeBtn", false);
                }

                if (status == 0) {
                    // 全部
                    list.add(map);
                } else {
                    if (count == 0) {
                        // 未开通
                        list.add(map);
                    }
                }
            }
        } else {
            // 已开通
            list = serviceMenuUserSchoolMapperEx.selectMapByExample(schoolId, categoryId, keyword);
            for (Map<String, Object> schoolMenu : list) {
                ServiceMenuCategory category = serviceMenuCategoryService
                        .getServiceMenuCategory(Integer.valueOf(String.valueOf(schoolMenu.get("categoryId"))));
                schoolMenu.put("categoryName", category.getName());
                if (schoolId != null && schoolId != 0) {
                    School school = schoolService.getById(schoolId);
                    schoolMenu.put("schoolName", school.getName());
                } else {
                    schoolMenu.put("schoolName", "");
                }
                schoolMenu.put("openBtn", false);
                schoolMenu.put("closeBtn", true);
                schoolMenu.put("index", schoolMenu.get("sort"));
            }

            // 该校开通的服务总数
            totalOpenCout = serviceMenuUserSchoolMapperEx.selectMapCountByExample(schoolId);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("totalOpenCout", totalOpenCout);
        map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", map));
    }

    /**
     * 对校园已开通服务进行排序
     * <p>
     * 排序规则：按照sort asc排列，如果有sort相同，则再按sort_time desc排列
     *
     * @param menuId   菜单id
     * @param schoolId 校园id
     * @param sort     排序
     */
    @RequestMapping("/back_api_sortSchoolsServiceMenuList")
    public void sortSchoolsServiceMenuList(Integer menuId, Integer schoolId, Integer sort,
                                           HttpServletResponse response) {
        if (menuId == null || schoolId == null || sort == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        // 查询出该学校原来的服务集合
        List<ServiceMenuUserSchool> serviceMenuUserSchools = serviceMenuUserSchoolMapperEx
                .selectListByExample(schoolId);
        // 按照新排序组成新的集合
        ServiceMenuUserSchool temp = null;
        List<ServiceMenuUserSchool> newList = new ArrayList<>();
        for (ServiceMenuUserSchool school : serviceMenuUserSchools) {
            if (school.getServiceMenuId() == menuId) {
                temp = school;
            } else {
                newList.add(school);
            }
        }
        int i = sort - 1;
        newList.add(i, temp);

        // 更新数据库
        int index = 1;
        boolean hasError = false;
        for (ServiceMenuUserSchool school : newList) {
            school.setSort(index);
            school.setSortTime(new Date());
            try {
                serviceMenuUserSchoolMapper.updateByPrimaryKey(school);
                index++;
            } catch (Exception e) {
                e.printStackTrace();
                hasError = true;
                break;
            }
        }

        if (!hasError) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "失败", null));
        }
    }

    /**
     * 查询banner图链接的内容列表（帖子和活动）
     *
     * @param schoolId
     * @param type     跳转类型：vote活动投票、web外部网页、post帖子
     */
    @RequestMapping("/back_api_findBannerLinkBizId")
    public void back_api_findBannerLinkBizId(Integer schoolId, String type, HttpServletResponse response) {
        if (schoolId == null || type == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        List<Map> list = new ArrayList<>();
        if (StringUtils.equals("post", type)) {
            // TODO: 2019/2/12 该学校的用户最近一周的所有帖子
            UserCriteria userEx = new UserCriteria();
            userEx.createCriteria()
                    .andSchoolIdEqualTo(schoolId)
                    .andIsDeleteEqualTo((short) 2);
            List<User> userList = userService.findList(userEx);
            List<Integer> userIdList = new ArrayList<>();
            if (!userList.isEmpty()) {
                for (User user : userList)
                    userIdList.add(user.getId());

                Date time = DateUtils.addDays(new Date(), -7);
                PostCriteria postEx = new PostCriteria();
                postEx.setOrderByClause("create_time desc");
                postEx.createCriteria()
                        .andCreatorIdIn(userIdList)
                        .andCreateTimeGreaterThanOrEqualTo(time)
                        .andIsDeleteEqualTo((short) 2);
                List<Post> postList = postService.findList(postEx);
                for (Post post : postList) {
                    Map data = new HashMap();
                    data.put("id", post.getId());
                    data.put("title", post.getTitle());
                    list.add(data);
                }

                // TODO: 2019/2/12 开发测试完成删除如下代码
                Map data = new HashMap();
                data.put("id", 137);
                data.put("title", "54校园1.0版本即将开始公测");
                list.add(data);
            }
        } else if (StringUtils.equals("vote", type)) {
            // 查询学校内的活动列表
            ActivityCriteria example = new ActivityCriteria();
            example.setOrderByClause("create_time desc");
            List<Activity> activityList = activityService.findList(example, null, 0);
            for (Activity activity : activityList) {
                Map data = new HashMap();
                data.put("id", activity.getId());
                data.put("title", activity.getName());
                list.add(data);
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "参数错误"));
            return;
        }

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", list));
    }

    /**
     * 添加服务菜单banner
     *
     * @param schoolId
     * @param imgUrl
     * @param linkUrl
     * @param type
     * @param response
     */
    @RequestMapping("/back_api_addServiceMenuBanner")
    public void back_api_addServiceMenuBanner(Integer schoolId, String imgUrl, String linkUrl, String type, HttpServletResponse response) {
        if (schoolId == null || linkUrl == null || type == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        if (Arrays.asList("vote", "web", "post").contains(type)) {
            if (StringUtils.isBlank(linkUrl)) {
                if (StringUtils.equals("vote", type)) {
                    ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必须选择一个活动", null));
                    return;
                } else if (StringUtils.equals("post", type)) {
                    ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必须选择一个帖子", null));
                    return;
                }
            }
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "参数错误", null));
            return;
        }


        ServiceMenuBanner banner = new ServiceMenuBanner();
        banner.setSchoolId(schoolId);
        banner.setType(type);
        banner.setImgUrl(imgUrl);
        banner.setLinkUrl(linkUrl);

        int result = serviceMenuBannerService.addBanner(banner);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

    /**
     * 删除服务菜单banner
     *
     * @param id
     * @param response
     */
    @RequestMapping("/back_api_deleteServiceMenuBanner")
    public void back_api_deleteServiceMenuBanner(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        ServiceMenuBanner banner = new ServiceMenuBanner();
        banner.setId(id);
        ServiceMenuBanner query = serviceMenuBannerService.getBanner(banner);
        if (query == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该记录已被删除", null));
            return;
        }

        int result = serviceMenuBannerService.deleteBanner(banner);
        if (result > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
        }
    }

//    /**
//     * 修改服务菜单banner
//     *
//     * @param id
//     * @param imgUrl
//     * @param linkUrl
//     * @param type
//     * @param response
//     */
//    @RequestMapping("/back_api_updateServiceMenuBanner")
//    public void back_api_updateServiceMenuBanner(Integer id, String imgUrl, String linkUrl, String type, HttpServletResponse response) {
//        if (id == null) {
//            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
//            return;
//        }
//
//        ServiceMenuBanner banner = new ServiceMenuBanner();
//        banner.setId(id);
//        ServiceMenuBanner query = serviceMenuBannerService.getBanner(banner);
//        if (query == null) {
//            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该记录已被删除", null));
//            return;
//        }
//
//        banner.setType(type);
//        banner.setImgUrl(imgUrl);
//        banner.setLinkUrl(linkUrl);
//        int result = serviceMenuBannerService.updateBanner(banner);
//        if (result > 0) {
//            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", null));
//        } else {
//            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "请求失败", null));
//        }
//    }

    /**
     * 服务菜单banner列表
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param response
     */
    @RequestMapping("/back_api_serviceMenuBannerList")
    public void back_api_serviceMenuBannerList(Integer page, Integer pageSize, Integer schoolId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        if (page != null && pageSize != null) {
            PageLimitHolderFilter.setContext(page, pageSize, null);

            ServiceMenuBannerCriteria example = new ServiceMenuBannerCriteria();
            example.setOrderByClause("id desc");
            example.createCriteria()
                    .andSchoolIdEqualTo(schoolId);
            List<Map<String, Object>> list = serviceMenuBannerService.findList(example);
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
        }
    }

    /**
     * 服务菜单banner详情
     *
     * @param id
     * @param response
     */
    @RequestMapping("/back_api_serviceMenuBannerDetails")
    public void back_api_serviceMenuBannerDetails(Integer id, HttpServletResponse response) {

        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        ServiceMenuBanner serviceMenuBanner = new ServiceMenuBanner();
        serviceMenuBanner.setId(id);
        serviceMenuBanner = serviceMenuBannerService.getBanner(serviceMenuBanner);
        if (serviceMenuBanner == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该记录不存在", serviceMenuBanner));
        } else {
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "请求成功", serviceMenuBanner));
        }
    }

    /**
     * 导入教务学生成绩查询视图表
     *
     * @param response
     */
    @RequestMapping("back_api_importJwCjcx")
    public void back_api_importJwCjcx(HttpServletResponse response) {
        Map<String, Object> param = ServletHolderFilter.getContext().getParamMap();
        Object object = param.get("file");
        Object schoolId = param.get("schoolId");
        if (object == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "请上传dbf类型的文件", null));
            return;
        }

        List<DiskFileItem> diskFileItems = (List<DiskFileItem>) object;
        DiskFileItem item = diskFileItems.get(0);
        Result result = jwCjcxService.addByImportFromDbfFile(item, schoolId.toString());

        ServletUtilsEx.renderJson(response, result);
    }
}
