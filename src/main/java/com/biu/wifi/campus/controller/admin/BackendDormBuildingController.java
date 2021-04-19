package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/10/30.
 */
@Controller
public class BackendDormBuildingController {

    @Autowired
    private DormBuildingService dormBuildingService;

    @Autowired
    private DormBuildingUserService dormBuildingUserService;

    @Autowired
    private BackendUserService userService;

    @Autowired
    private LeaveAuditUserRoleService leaveAuditUserRoleService;

    /**
     * xx宿舍楼的宿管人员列表
     *
     * @param buildingId 宿舍楼ID
     * @param response
     */
    @RequestMapping("back_api_findDormBuildingUserListByBuildingId")
    public void back_api_findDormBuildingUserListByBuildingId(Integer buildingId, HttpServletResponse response) {
        if (buildingId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        DormBuildingUserCriteria example = new DormBuildingUserCriteria();
        example.createCriteria().andIsDeleteEqualTo((short) 2)
                .andBuildingIdEqualTo(buildingId);
        List<DormBuildingUser> users = dormBuildingUserService.findList(example);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (DormBuildingUser buildingUser : users) {
            map = new HashedMap();
            map.put("id", buildingUser.getUserId());
            map.put("name", buildingUser.getUsername());
            User user = userService.getById(buildingUser.getUserId());
            map.put("headimg", user.getHeadimg());
            map.put("stuNumber", user.getStuNumber());
            list.add(map);
        }
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    /**
     * 添加宿管
     *
     * @param buildingId 宿舍楼ID
     * @param userId     宿管人员ID字符串，逗号分隔
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateDormBuildingUser")
    public void back_api_addOrUpdateDormBuildingUser(Integer buildingId, String userId, HttpServletResponse response) {
        if (buildingId == null || StringUtils.isBlank(userId)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        String[] userIdList = userId.split(",");
        dormBuildingUserService.addOrUpdate(buildingId, userIdList);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 获取xx学校的请假审批身份认证类型列表
     *
     * @param schoolId 学校ID
     * @param response
     */
    @RequestMapping("back_api_findLeaveAuditUserRoleSelectList")
    public void back_api_findLeaveAuditUserRoleSelectList(Integer schoolId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        //身份认证只有两级，先查询一级ID
        List<LeaveAuditUserRole> list = leaveAuditUserRoleService.findList(schoolId, null);
        Iterator<LeaveAuditUserRole> iterator = list.iterator();
        List<LeaveAuditUserRole> children = new ArrayList<>();
        while (iterator.hasNext()) {
            LeaveAuditUserRole role = iterator.next();
            List<LeaveAuditUserRole> tempChildren = leaveAuditUserRoleService.findList(schoolId, role.getId());
            if (tempChildren.size() > 0) {
                String pName = role.getName();
                //将一级替换成子集
                iterator.remove();
                for (LeaveAuditUserRole child : tempChildren) {
                    child.setName(pName + "-" + child.getName());
                    children.add(child);
                }
            }
        }
        list.addAll(children);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }

    /**
     * 新增或编辑宿舍楼
     *
     * @param id
     * @param schoolId     学校ID
     * @param instituteId  学院ID
     * @param areaPosition 区域位置
     * @param no           宿舍楼编号
     * @param unitNo       单元编号
     * @param floorCount   楼层数
     * @param roomCount    房间数
     * @param phone        联系电话
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateDormBuilding")
    public void back_api_addOrUpdateDormBuilding(String id, Integer schoolId,
                                                 @RequestParam(required = false, defaultValue = "0") Integer instituteId,
                                                 String areaPosition, String no, String unitNo,
                                                 String floorCount, String roomCount, String phone, HttpServletResponse response) {

        if (schoolId == null || /*StringUtils.isBlank(areaPosition) ||*/ no == null /*|| StringUtils.isBlank(unitNo)*/) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }
        if (instituteId == 0) {
            instituteId = null;
        }

        DormBuilding building = new DormBuilding();
        if (StringUtils.isNotBlank(id)) {
            building.setId(Integer.valueOf(id));
        }
        building.setSchoolId(schoolId);
        building.setInstituteId(instituteId);
        building.setAreaPosition(areaPosition);
        building.setNo(no);
        building.setUnitNo(unitNo);
        if (StringUtils.isNotBlank(floorCount)) {
            building.setFloorCount(Integer.valueOf(floorCount));
        }
        building.setRoomCount(roomCount);
        building.setPhone(phone);
        dormBuildingService.addOrUpdateDormBuilding(building);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 宿舍楼列表
     *
     * @param areaPosition 区域位置
     * @param no           宿舍楼编号
     * @param unitNo       单元编号
     * @param page         页码
     * @param pageSize     每页记录数
     * @param schoolId     学校ID
     * @param response
     */
    @RequestMapping("back_api_findDormBuildingList")
    public void back_api_findDormBuildingList(String areaPosition, String no, String unitNo, Integer page,
                                              Integer pageSize, Integer schoolId, HttpServletResponse response) {

        if (page == null || pageSize == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        DormBuildingCriteria example = new DormBuildingCriteria();
        DormBuildingCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId);

        if (StringUtils.isNotBlank(areaPosition)) {
            criteria.andAreaPositionLike("%" + areaPosition + "%");
        }
        if (StringUtils.isNotBlank(no)) {
            try {
                criteria.andNoLike("%" + no + "%");
            } catch (Exception e) {
                e.printStackTrace();
                ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "请输入数字编号", null));
                return;
            }
        }
        if (StringUtils.isNotBlank(unitNo)) {
            criteria.andUnitNoLike("%" + unitNo + "%");
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<DormBuilding> list = dormBuildingService.findList(example);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
    }

    /**
     * 查看宿舍楼详情
     *
     * @param id
     * @param schoolId
     * @param response
     */
    @RequestMapping("back_api_findDormBuildingDetail")
    public void back_api_findDormBuildingDetail(Integer id, Integer schoolId, HttpServletResponse response) {

        if (id == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        DormBuildingCriteria example = new DormBuildingCriteria();
        DormBuildingCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2).andSchoolIdEqualTo(schoolId).andIdEqualTo(id);

        List<DormBuilding> list = dormBuildingService.findList(example);

        if (list.size() == 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "该宿舍楼不存在", null));
            return;
        }

        DormBuilding building = list.get(0);
        Map<String, Object> map = new HashMap<>();
        map.put("id", building.getId());
        map.put("areaPosition", building.getAreaPosition());
        map.put("no", building.getNo());
        map.put("unitNo", building.getUnitNo());
        map.put("floorCount", building.getFloorCount());
        map.put("roomCount", building.getRoomCount());
        map.put("phone", building.getPhone());

        DormBuildingUserCriteria userEx = new DormBuildingUserCriteria();
        userEx.createCriteria().andIsDeleteEqualTo((short) 2).andBuildingIdEqualTo(building.getId());
        List<DormBuildingUser> dormBuildingUsers = dormBuildingUserService.findList(userEx);
        map.put("dormBuildingUserList", dormBuildingUsers);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
    }

    /**
     * 删除宿舍楼信息
     *
     * @param id       宿舍楼ID
     * @param schoolId 学校ID
     * @param response
     */
    @RequestMapping("back_api_deleteDormBuilding")
    public void back_api_deleteDormBuilding(Integer id, Integer schoolId, HttpServletResponse response) {
        if (id == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "缺少必要参数", null));
            return;
        }

        dormBuildingService.delete(id, schoolId);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }
}
