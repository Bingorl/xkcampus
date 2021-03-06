package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.constant.AudUserRole;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/5.
 */
@Controller
public class BackendClassRoomBookController {

    private static Logger logger = LoggerFactory.getLogger(BackendClassRoomBookController.class);
    @Autowired
    private ClassroomBuildingService classroomBuildingService;
    @Autowired
    private ClassroomBookUseTypeService classroomBookUseTypeService;
    @Autowired
    private ClassroomBookUseTypeOrganizationService classroomBookUseTypeOrganizationService;
    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserService classroomBookUseTypeOrganizationAuditUserService;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ClassroomBookAuditService classroomBookAuditService;
    @Autowired
    private ClassroomBookService classroomBookService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private AuditRolePermissionService auditRolePermissionService;
    @Autowired
    private ClassroomTypeService classroomTypeService;
    @Autowired
    private AuditUserRoleService auditUserRoleService;

    /**
     * ????????????????????????
     *
     * @param id           ?????????ID?????????????????????
     * @param schoolId     ??????ID????????????
     * @param areaPosition ????????????
     * @param no           ??????????????????????????????????????????????????????
     * @param name         ??????????????????????????????????????????????????????
     * @param floor        ??????????????????
     * @param remark       ??????
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateClassroomBuilding")
    public void back_api_addOrUpdateClassroomBuilding(Integer id, Integer schoolId, String areaPosition,
                                                      String no, String name, Integer floor, String remark, HttpServletResponse response) {
        if (schoolId == null || (StringUtils.isBlank(no) && StringUtils.isBlank(name))) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomBuilding classroomBuilding = new ClassroomBuilding();
        classroomBuilding.setId(id);
        classroomBuilding.setSchoolId(schoolId);
        classroomBuilding.setAreaPosition(areaPosition);
        classroomBuilding.setNo(no);
        classroomBuilding.setName(name);
        classroomBuilding.setFloor(floor);
        classroomBuilding.setRemark(remark);
        Result result = classroomBuildingService.addOrUpdate(classroomBuilding, false);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ???????????????
     *
     * @param page
     * @param pageSize
     * @param schoolId ??????ID
     * @param keyword  ?????????????????????????????????
     * @param response
     */
    @RequestMapping("back_api_findClassroomBuildingList")
    public void back_api_findClassroomBuildingList(Integer page,
                                                   @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                   Integer schoolId,
                                                   @RequestParam(required = false) String keyword, HttpServletResponse response) {
        if (page == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        ClassroomBuildingCriteria example1 = new ClassroomBuildingCriteria();
        example1.setOrderByClause("create_time desc");
        ClassroomBuildingCriteria.Criteria criteria1 = example1.createCriteria();
        criteria1.andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);

        ClassroomBuildingCriteria example2 = new ClassroomBuildingCriteria();
        example2.setOrderByClause("create_time desc");
        ClassroomBuildingCriteria.Criteria criteria2 = example2.createCriteria();
        criteria2.andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(keyword)) {
            criteria1.andNoLike("%" + keyword + "%");
            criteria2.andNameLike("%" + keyword + "%");
            example1.or(criteria2);
        }

        List<ClassroomBuilding> classroomBuildingList = classroomBuildingService.findList(example1);

        Map data = new HashMap();
        data.put("list", classroomBuildingList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", data));
    }

    /**
     * ?????????????????????
     *
     * @param schoolId ??????ID
     * @param keyword  ?????????????????????????????????
     * @param response
     */
    @RequestMapping("back_api_findClassroomBuildingSelectorList")
    public void back_api_findClassroomBuildingSelectorList(Integer schoolId,
                                                           @RequestParam(required = false) String keyword, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomBuildingCriteria example1 = new ClassroomBuildingCriteria();
        example1.setOrderByClause("create_time desc");
        ClassroomBuildingCriteria.Criteria criteria1 = example1.createCriteria();
        criteria1.andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);

        ClassroomBuildingCriteria example2 = new ClassroomBuildingCriteria();
        example2.setOrderByClause("create_time desc");
        ClassroomBuildingCriteria.Criteria criteria2 = example2.createCriteria();
        criteria2.andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(keyword)) {
            criteria1.andNoLike("%" + keyword + "%");
            criteria2.andNameLike("%" + keyword + "%");
            example1.or(criteria2);
        }

        List<ClassroomBuilding> classroomBuildingList = classroomBuildingService.findList(example1);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", classroomBuildingList));
    }

    /**
     * ???????????????
     *
     * @param id
     * @param schoolId
     * @param response
     */
    @RequestMapping("back_api_deleteClassroomBuilding")
    public void back_api_deleteClassroomBuilding(Integer id, Integer schoolId, HttpServletResponse response) {
        if (id == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        Result result = classroomBuildingService.delete(id, schoolId);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ???????????????????????????
     *
     * @param id
     * @param schoolId
     * @param isMedia
     * @param code
     * @param name
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateClassroomType")
    public void back_api_addOrUpdateClassroomType(Integer id, Integer schoolId,
                                                  @RequestParam(defaultValue = "1") String isMedia,
                                                  String code, String name, HttpServletResponse response) {
        if (schoolId == null || code == null || name == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomType classroomType = new ClassroomType();
        classroomType.setId(id);
        classroomType.setSchoolId(schoolId);
        classroomType.setIsMedia(isMedia);
        classroomType.setCode(code);
        classroomType.setName(name);
        Result result = classroomTypeService.addOrUpdate(classroomType);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ??????????????????
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param keyword
     * @param response
     */
    @RequestMapping("back_api_findClassroomTypeList")
    public void back_api_classroomTypeList(Integer page, Integer pageSize, Integer schoolId, String keyword, HttpServletResponse response) {
        if (page == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomTypeCriteria example1 = new ClassroomTypeCriteria();
        example1.setOrderByClause("create_time desc");
        ClassroomTypeCriteria.Criteria criteria1 = example1.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);

        ClassroomTypeCriteria example2 = new ClassroomTypeCriteria();
        example1.setOrderByClause("create_time desc");
        ClassroomTypeCriteria.Criteria criteria2 = example2.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);

        if (StringUtils.isNotBlank(keyword)) {
            criteria1.andNameLike("%" + keyword + "%");
            criteria2.andCodeLike("%" + keyword + "%");
        }
        example1.or(criteria2);

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<ClassroomType> classroomTypeList = classroomTypeService.findList(example1);
        List<Map> list = BeanUtil.beanListToMapList(classroomTypeList);
        for (Map map : list) {
            if ("1".equals(map.get("isMedia").toString())) {
                map.put("isMediaText", "???");
            } else {
                map.put("isMediaText", "???");
            }
        }

        Map data = new HashMap();
        data.put("list", list);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", data));
    }

    /**
     * ????????????????????????
     *
     * @param schoolId
     * @param keyword
     * @param response
     */
    @RequestMapping("back_api_findClassroomTypeSelectorList")
    public void back_api_classroomTypeSelectorList(Integer schoolId, String keyword, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomTypeCriteria example1 = new ClassroomTypeCriteria();
        example1.setOrderByClause("create_time desc");
        ClassroomTypeCriteria.Criteria criteria1 = example1.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);

        ClassroomTypeCriteria example2 = new ClassroomTypeCriteria();
        example1.setOrderByClause("create_time desc");
        ClassroomTypeCriteria.Criteria criteria2 = example2.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);

        if (StringUtils.isNotBlank(keyword)) {
            criteria1.andNameLike("%" + keyword + "%");
            criteria2.andCodeLike("%" + keyword + "%");
        }
        example1.or(criteria2);

        List<ClassroomType> classroomTypeList = classroomTypeService.findList(example1);
        List<Map> list = BeanUtil.beanListToMapList(classroomTypeList);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", list));
    }

    /**
     * ??????????????????
     *
     * @param id
     * @param schoolId
     * @param response
     */
    @RequestMapping("back_api_deleteClassroomType")
    public void back_api_deleteClassroomType(Integer id, Integer schoolId, HttpServletResponse response) {
        if (id == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        Result result = classroomTypeService.delete(id, schoolId);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ?????????????????????
     *
     * @param id
     * @param buildingId
     * @param no
     * @param name
     * @param seatCount
     * @param exSeatCount
     * @param typeId
     * @param status
     * @param remark
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateClassroom")
    public void back_api_addOrUpdateClassroom(Integer id, Integer buildingId, String no, String name, Integer seatCount,
                                              Integer exSeatCount, Integer typeId,
                                              @RequestParam(defaultValue = "1") Short status, String remark, HttpServletResponse response) {
        if (buildingId == null || typeId == null || (StringUtils.isBlank(no) && StringUtils.isBlank(name))) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        Classroom classroom = new Classroom();
        classroom.setId(id);
        classroom.setBuildingId(buildingId);
        classroom.setNo(no);
        classroom.setName(name);
        classroom.setSeatCount(seatCount);
        classroom.setExSeatCount(exSeatCount);
        classroom.setTypeId(typeId);
        classroom.setStatus(status);
        classroom.setRemark(remark);
        Result result = classroomService.addOrUpdate(classroom);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ????????????
     *
     * @param page
     * @param pageSize
     * @param buildingId
     * @param keyword
     * @param response
     */
    @RequestMapping("back_api_findClassroomList")
    public void back_api_findClassroomList(Integer page, Integer pageSize,
                                           @RequestParam(required = false) String buildingId, String keyword, HttpServletResponse response) {
        if (page == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomCriteria example1 = new ClassroomCriteria();
        example1.setOrderByClause("create_time desc");
        ClassroomCriteria.Criteria criteria1 = example1.createCriteria()
                .andIsDeleteEqualTo((short) 2);

        ClassroomCriteria example2 = new ClassroomCriteria();
        example2.setOrderByClause("create_time desc");
        ClassroomCriteria.Criteria criteria2 = example2.createCriteria()
                .andIsDeleteEqualTo((short) 2);

        if (StringUtils.isNotBlank(keyword)) {
            criteria1.andNoLike("%" + keyword + "%");
            criteria2.andNameLike("%" + keyword + "%");
        }

        if (StringUtils.isNotBlank(buildingId)) {
            criteria1.andBuildingIdEqualTo(Integer.valueOf(buildingId));
            criteria2.andBuildingIdEqualTo(Integer.valueOf(buildingId));
        }
        example1.or(criteria2);

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Classroom> classroomList = classroomService.findList(example1);

        List<Map> classroomMapList = new ArrayList<>();
        for (Classroom classroom : classroomList) {
            Map map = BeanUtil.beanToMap(classroom);
            ClassroomBuilding classroomBuilding = classroomBuildingService.findById(classroom.getBuildingId());
            ClassroomType classroomType = classroomTypeService.find(classroom.getTypeId());
            String statusText = "";
            switch (classroom.getStatus()) {
                case 1:
                    statusText = "??????";
                    break;
                case 2:
                    statusText = "??????";
                    break;
                case 3:
                    statusText = "??????";
                    break;
            }

            map.put("buildingName", classroomBuilding.getName());
            map.put("typeName", classroomType.getName());
            map.put("statusText", statusText);
            classroomMapList.add(map);
        }

        Map data = new HashMap();
        data.put("list", classroomMapList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", data));
    }

    /**
     * ????????????
     *
     * @param id
     * @param classroomNo
     * @param buildingId
     * @param schoolId
     * @param response
     */
    @RequestMapping("back_api_deleteClassroom")
    public void back_api_deleteClassroom(Integer id, String classroomNo, Integer buildingId, Integer schoolId, HttpServletResponse response) {
        if (id == null || buildingId == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomBuilding classroomBuilding = classroomBuildingService.findById(buildingId);
        if (classroomBuilding == null || (classroomBuilding != null && classroomBuilding.getSchoolId().intValue() != schoolId)) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "??????????????????", null));
            return;
        }

        String now = TimeUtil.format(new Date(), "yyyy-MM-dd");
        ClassroomBookCriteria example1 = new ClassroomBookCriteria();
        example1.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andStatusEqualTo(1)
                .andClassroomNoLike("%" + classroomNo + "%")
                .andBookDateGreaterThanOrEqualTo(now);

        ClassroomBookCriteria example2 = new ClassroomBookCriteria();
        ClassroomBookCriteria.Criteria criteria2 = example2.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andStatusEqualTo(1)
                .andAdjustClassroomNoLike("%" + classroomNo + "%")
                .andBookDateGreaterThanOrEqualTo(now);
        example1.or(criteria2);

        long count = classroomBookService.countByExample(example1);
        if (count > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "???????????????????????????????????????", null));
            return;
        }

        Result result = classroomService.delete(id);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ???excel??????????????????
     * <p>
     * global.UP:uploadFile.do
     *
     * @param schoolId
     * @param fileId
     * @param response
     */
    @RequestMapping("back_api_importClassroomData")
    public void back_api_importClassroomData(Integer schoolId, String fileId, HttpServletResponse response) {
        if (fileId == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        Result result = classroomService.importClassroomData(schoolId, fileId);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param id            ????????????ID
     * @param userId        ??????ID
     * @param schoolId      ??????ID
     * @param name          ??????????????????
     * @param organizations ?????????????????????????????????????????????ID
     *                      <p>
     *                      ?????????[{"organizationId":1,"auditUser":"1,2,3"},{"organizationId":2,"auditUser":"5,9,13"}]
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateClassroomBookUseType")
    public void back_api_addOrUpdateClassroomBookUseType(@RequestParam(required = false) Integer id,
                                                         @RequestParam(required = false) Integer userId,
                                                         Integer schoolId, String name, String organizations, HttpServletResponse response) {
        if (schoolId == null || StringUtils.isBlank(name) || organizations == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        List<Map> auditUserMapList = new ArrayList<>();
        try {
            List auditUserList = new ObjectMapper().readValue(organizations, List.class);
            for (Object o : auditUserList) {
                Map map = (Map) o;
                if (!map.containsKey("organizationId") || !map.containsKey("auditUser")) {
                    continue;
                }
                map.put("organizationId", map.get("organizationId").toString());
                map.put("auditUser", map.get("auditUser").toString());
                auditUserMapList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("?????????????????????????????????????????????{}", e.getMessage());
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "??????????????????????????????")));
            return;
        }

        ClassroomBookUseType classroomBookUseType = new ClassroomBookUseType();
        classroomBookUseType.setId(id);
        classroomBookUseType.setSchoolId(schoolId);
        classroomBookUseType.setCreateBy(userId);
        classroomBookUseType.setName(name);

        Result result = classroomBookUseTypeService.addOrUpdate(classroomBookUseType, false, auditUserMapList);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ??????????????????????????????
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param keyword
     * @param response
     */
    @RequestMapping("back_api_findClassroomBookUseTypeList")
    public void back_api_findClassroomBookUseTypeList(Integer page,
                                                      @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                      Integer schoolId,
                                                      @RequestParam(required = false) String keyword, HttpServletResponse response) {
        if (page == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        ClassroomBookUseTypeCriteria example = new ClassroomBookUseTypeCriteria();
        example.setOrderByClause("create_time desc");
        ClassroomBookUseTypeCriteria.Criteria criteria = example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(schoolId);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }
        List<ClassroomBookUseType> classroomBookUseTypeList = classroomBookUseTypeService.findList(example);
        List<Map> mapList = BeanUtil.beanListToMapList(classroomBookUseTypeList, "retainKeys", "id", "name");
        for (Map map : mapList) {
            ClassroomBookUseTypeOrganizationAuditUserCriteria classroomBookUseTypeOrganizationAuditUserEx = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
            classroomBookUseTypeOrganizationAuditUserEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andUseTypeIdEqualTo(Integer.valueOf(map.get("id").toString()));
            List<ClassroomBookUseTypeOrganizationAuditUser> classroomBookUseTypeOrganizationAuditUsers = classroomBookUseTypeOrganizationAuditUserService.findList(classroomBookUseTypeOrganizationAuditUserEx);
            List<Integer> organizationId = new ArrayList<>();
            map.put("organizationId", organizationId);
            List<Map> organizationUserList = new ArrayList<>();
            map.put("organizationUserList", organizationUserList);
            for (ClassroomBookUseTypeOrganizationAuditUser classroomBookUseTypeOrganizationAuditUser : classroomBookUseTypeOrganizationAuditUsers) {
                organizationId.add(classroomBookUseTypeOrganizationAuditUser.getOrganizationId());
                ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = classroomBookUseTypeOrganizationService.findById(classroomBookUseTypeOrganizationAuditUser.getOrganizationId());
                Map organizationUserMap = new HashMap();
                organizationUserMap.put("id", classroomBookUseTypeOrganizationAuditUser.getOrganizationId());
                organizationUserMap.put("name", classroomBookUseTypeOrganization.getName());
                List<Map> auditUser = new ArrayList<>();
                organizationUserMap.put("auditUser", auditUser);
                List<Integer> userIds = new ArrayList<>();
                for (String id : classroomBookUseTypeOrganizationAuditUser.getAuditUser().split(",")) {
                    userIds.add(Integer.valueOf(id));
                }
                UserCriteria userEx = new UserCriteria();
                userEx.createCriteria()
                        .andIdIn(userIds);
                List<User> userList = userService.findList(userEx);
                for (User user : userList) {
                    Map userMap = new HashMap();
                    userMap.put("id", user.getId());
                    userMap.put("headimg", user.getHeadimg());
                    userMap.put("name", user.getName());
                    auditUser.add(userMap);
                }
                organizationUserList.add(organizationUserMap);
            }
        }

        //???????????????????????????????????????
        ClassroomBookUseTypeOrganizationCriteria classroomBookUseTypeOrganizationEx2 = new ClassroomBookUseTypeOrganizationCriteria();
        classroomBookUseTypeOrganizationEx2.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(schoolId);
        List<ClassroomBookUseTypeOrganization> classroomBookUseTypeOrganizations = classroomBookUseTypeOrganizationService.findList(classroomBookUseTypeOrganizationEx2);

        Map data = new HashMap();
        data.put("list", mapList);
        data.put("organizationList", classroomBookUseTypeOrganizations);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", data));
    }

    /**
     * ??????????????????????????????
     *
     * @param id
     * @param response
     */
    @RequestMapping("back_api_deleteClassroomBookUseType")
    public void back_api_deleteClassroomBookUseType(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        Result result = classroomBookUseTypeService.delete(id);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ?????????????????????
     *
     * @param id
     * @param schoolId
     * @param type
     * @param instituteId
     * @param name
     * @param remark
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateClassroomBookUseTypeOrganization")
    public void back_api_addOrUpdateClassroomBookUseTypeOrganization(@RequestParam(required = false) Integer id,
                                                                     Integer schoolId, Short type, Integer instituteId, String name, String remark, HttpServletResponse response) {
        if (schoolId == null || StringUtils.isBlank(name)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = new ClassroomBookUseTypeOrganization();
        classroomBookUseTypeOrganization.setId(id);
        classroomBookUseTypeOrganization.setSchoolId(schoolId);
        classroomBookUseTypeOrganization.setType(type);
        classroomBookUseTypeOrganization.setInstituteId(instituteId);
        classroomBookUseTypeOrganization.setName(name);
        classroomBookUseTypeOrganization.setRemark(remark);

        Result result = classroomBookUseTypeOrganizationService.addOrUpdate(classroomBookUseTypeOrganization, false);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ????????????
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param type
     * @param keyword
     * @param response
     */
    @RequestMapping("back_api_findClassroomBookUseTypeOrganizationList")
    public void back_api_findClassroomBookUseTypeOrganizationList(Integer page,
                                                                  @RequestParam(defaultValue = "10", required = false) Integer pageSize,
                                                                  Integer schoolId,
                                                                  @RequestParam(required = false, defaultValue = "0") Short type,
                                                                  @RequestParam(required = false) String keyword, HttpServletResponse response) {
        if (page == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomBookUseTypeOrganizationCriteria example = new ClassroomBookUseTypeOrganizationCriteria();
        example.setOrderByClause("create_time desc");
        ClassroomBookUseTypeOrganizationCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId)
                .andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }

        if (type != 0) {
            criteria.andTypeEqualTo(type);
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map> classroomBookUseTypeOrganizationMapList = classroomBookUseTypeOrganizationService.findList(example, new HashMap());

        Map data = new HashMap();
        data.put("list", classroomBookUseTypeOrganizationMapList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", data));
    }

    /**
     * ????????????
     *
     * @param id
     * @param response
     */
    @RequestMapping("back_api_deleteClassroomBookUseTypeOrganization")
    public void back_api_deleteClassroomBookUseTypeOrganization(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        Result result = classroomBookUseTypeOrganizationService.delete(id);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * xx?????????????????????????????????
     *
     * @param userId
     * @param isPass
     * @param page
     * @param pageSize
     * @param response
     */
    @RequestMapping("back_api_findClassroomBookAuditList")
    public void back_api_findClassroomBookAuditList(@RequestParam(required = false, defaultValue = "0") Integer id, Integer userId,
                                                    @RequestParam(required = false, defaultValue = "") String isPass,
                                                    Integer page, Integer pageSize, HttpServletResponse response) {
        if (userId == null || page == null || pageSize == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????", null));
            return;
        }

        ClassroomBookAuditCriteria example = new ClassroomBookAuditCriteria();
        example.setOrderByClause("create_time desc");
        ClassroomBookAuditCriteria.Criteria criteria = example.createCriteria()
                .andIsDeleteEqualTo((short) 2);
        if (id == 0) {
            criteria.andUserIdEqualTo(userId);
        } else {
            criteria.andIdEqualTo(id);
        }
        if (StringUtils.isNotBlank(isPass)) {
            criteria.andIsPassEqualTo(Short.valueOf(isPass));
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<ClassroomBookAudit> classroomBookAuditList = classroomBookAuditService.findList(example);

        List<Map> classroomBookAuditMapList = new ArrayList<>();
        User user = userService.getById(userId);
        AuditUserAuth auditUserAuth = auditUserAuthService.find(user.getSchoolId(), userId);
        boolean allowAdjustClassroomNo = false;
        if (auditUserAuth != null) {
            allowAdjustClassroomNo = auditRolePermissionService.findPermissionByCodeAndRoleId(auditUserAuth.getRoleId(),
                    "classroomBook/adjustClassroomNo/update");
        }
        boolean allowAudit = false;
        for (ClassroomBookAudit classroomBookAudit : classroomBookAuditList) {
            String no = classroomBookService.getBookNo("JSSH", classroomBookAudit.getId().toString());
            ClassroomBook classroomBook = classroomBookService.findById(classroomBookAudit.getBookId());
            ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = classroomBookUseTypeOrganizationService.findById(classroomBook.getOrganizationId());
            String bookDate = classroomBook.getBookDate() + " "
                    + teachingWeekService.getCourseIndex(user.getSchoolId(),
                    Arrays.asList(classroomBook.getBookPeriod().split(",")));
            ClassroomBookUseType classroomBookUseType = classroomBookUseTypeService.findById(classroomBook.getUseTypeId());
            String remark = "";
            if (classroomBook.getStatus() == 3) {
                remark = classroomBook.getAuditRemark();
            }
            if (classroomBook.getStatus().intValue() == 1 && classroomBookAudit.getIsPass() == null) {
                allowAudit = true;
            }
            String statusText;
            switch (classroomBook.getStatus().intValue()) {
                case 1:
                    statusText = "?????????";
                    break;
                case 2:
                    statusText = "????????????";
                    break;
                case 3:
                    statusText = "???????????????";
                    break;
                default:
                    statusText = "";
                    break;
            }


            /*List<Map> auditProcessList = new ArrayList<>();
            //??????????????????????????????????????????
            ClassroomBookAuditCriteria classroomBookAuditCriteria = new ClassroomBookAuditCriteria();
            classroomBookAuditCriteria.createCriteria()
                    .andBookIdEqualTo(classroomBook.getId())
                    .andIsDeleteEqualTo((short) 2);
            List<ClassroomBookAudit> classroomBookAudits = classroomBookAuditService.findList(classroomBookAuditCriteria);
            int auditProcessActive = 0;
            for (int i = 0; i < classroomBookAudits.size(); i++) {
                Map map = new HashMap();
                ClassroomBookAudit c = classroomBookAudits.get(i);
                map.put("index", i + 1);
                String status;
                if (c.getIsPass() == null) {
                    status = "(?????????)";
                } else {
                    if (c.getIsPass().intValue() == 1) {
                        status = "(?????????)";
                    } else {
                        status = "(?????????)";
                    }
                    auditProcessActive = i + 1;
                }
                String name;
                if (classroomBookAudit.getUserId().intValue() == c.getUserId().intValue()) {
                    name = "???";
                } else {
                    User user1 = userService.getById(c.getUserId());
                    name = user1.getName();
                }
                map.put("name", name + status);
                auditProcessList.add(map);
            }*/

            Map audit = BeanUtil.beanToMap(classroomBookAudit);
            audit.put("no", no);
            audit.put("organizationName", classroomBookUseTypeOrganization.getName());
            audit.put("useTypeText", classroomBookUseType.getName());
            audit.put("bookDate", bookDate);
            audit.put("stuNumber", classroomBook.getLinkManNo());
            audit.put("phone", classroomBook.getLinkPhone());
            audit.put("title", classroomBook.getTitle());
            audit.put("content", classroomBook.getContent());
            audit.put("classroomNo", classroomBook.getClassroomNo());
            audit.put("adjustClassroomNo", classroomBook.getAdjustClassroomNo());
            audit.put("personCount", classroomBook.getPersonCount());
            audit.put("status", classroomBook.getStatus());
            audit.put("statusText", statusText);
            audit.put("remark", remark);
            audit.put("allowAdjustClassroomNo", allowAdjustClassroomNo);
            audit.put("allowAudit", allowAudit);
            audit.put("classroomBookId", classroomBook.getId());
            /*audit.put("auditProcessList", auditProcessList);
            audit.put("auditProcessActive", auditProcessActive);*/
            classroomBookAuditMapList.add(audit);
        }
        Map data = new HashMap();
        data.put("list", classroomBookAuditMapList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "??????", data));
    }

    /**
     * ????????????????????????
     *
     * @param userId
     * @param classroomBookId
     * @param isPass
     * @param remark
     * @param response
     */
    @RequestMapping("back_api_updateClassroomBookAudit")
    public void back_api_updateClassroomBookAudit(Integer userId, Integer classroomBookId,
                                                  @RequestParam(defaultValue = "1", required = false) Short isPass, String remark, HttpServletResponse response) {
        if (userId == null || classroomBookId == null || isPass == null || (isPass.intValue() == 2 && remark == null)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "??????????????????"));
            return;
        }

        User user = userService.getById(userId);

        //  ?????????????????????????????????????????????????????????????????????????????????
        boolean toTeachingSecretary = false;
        AuditUserAuth auditUserAuth = auditUserAuthService.find(user.getSchoolId(), userId);
        boolean hasPermission = auditRolePermissionService.findPermissionByCodeAndRoleId(auditUserAuth.getRoleId(), "classroomBook/audit");
        if (hasPermission) {
            toTeachingSecretary = true;
        } else {
            //??????????????????????????????????????????
            ClassroomBook classroomBook = classroomBookService.findById(classroomBookId);
            ClassroomBookUseTypeOrganizationAuditUser auditUser = classroomBookUseTypeOrganizationAuditUserService.find(classroomBook.getUseTypeId(), classroomBook.getOrganizationId(), (short) 2);
            List<String> auditUserIdList = Arrays.asList(auditUser.getAuditUser().split(","));
            if (!auditUserIdList.contains(String.valueOf(userId))) {
                ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "??????????????????"));
                return;
            }
        }

        ClassroomBookAuditCriteria example = new ClassroomBookAuditCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId)
                .andBookIdEqualTo(classroomBookId);
        List<ClassroomBookAudit> classroomBookAuditList = classroomBookAuditService.findList(example);
        if (classroomBookAuditList.isEmpty()) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "????????????????????????"));
            return;
        }
        ClassroomBookAudit classroomBookAudit = classroomBookAuditList.get(0);

        Map classroomBookMap = classroomBookService.find(user.getSchoolId(), classroomBookId);
        Integer useTypeId = (Integer) classroomBookMap.get("useTypeId");
        Integer organizationId = (Integer) classroomBookMap.get("organizationId");
        ClassroomBookUseTypeOrganizationAuditUser auditUser = classroomBookUseTypeOrganizationAuditUserService.find(useTypeId, organizationId, (short) 2);
        List<String> users = Arrays.asList(auditUser.getAuditUser().split(","));
        Integer nextAuditUserId = null;
        int index = users.indexOf(String.valueOf(userId));
        if (index != -1 && index != users.size() - 1) {
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????
            nextAuditUserId = Integer.valueOf(users.get(index + 1));
        }

        //????????????????????????????????????????????????
        ClassroomBookAuditCriteria classroomBookAuditCriteria = new ClassroomBookAuditCriteria();
        classroomBookAuditCriteria.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId)
                .andBookIdEqualTo(classroomBookId)
                .andIsPassIsNotNull();
        long count = classroomBookAuditService.countByExample(classroomBookAuditCriteria);
        if (count > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "????????????????????????"));
            return;
        }

        Result result = classroomBookAuditService.updateAudit(classroomBookAudit.getId(), userId, nextAuditUserId, toTeachingSecretary, isPass, remark);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * ???????????????
     * <p>
     * ???????????????????????????classroomBookId?????????????????????
     *
     * @param classroomBookId ????????????ID(???????????????????????????????????????)
     * @param schoolId        ??????ID
     * @param buildingId      ?????????ID
     * @param seatRange       ???????????????0-50???
     * @param isMedia         ??????????????????????????????1??????2??????
     * @param bookDateStr     ??????????????????
     * @param bookPeriod      ?????????????????????(08:00,08:50)
     * @param response
     */
    @RequestMapping("back_api_findUseAbleClassroomList")
    public void back_api_findUseAbleClassroomList(Integer userId,
                                                  @RequestParam(required = false) Integer classroomBookId,
                                                  Integer schoolId,
                                                  @RequestParam(defaultValue = "0", required = false) Integer buildingId,
                                                  @RequestParam(defaultValue = "0-50", required = false) String seatRange,
                                                  @RequestParam(defaultValue = "2", required = false) Integer isMedia,
                                                  String bookDateStr, String bookPeriod, HttpServletResponse response) {
        ClassroomBook classroomBook = null;
        if (classroomBookId != null) {
            User user = userService.getById(userId);
            AuditUserAuth userAuth = auditUserAuthService.find(user.getSchoolId(), userId);
            boolean hasPermission = auditRolePermissionService.findPermissionByCodeAndRoleId(userAuth.getRoleId(),
                    "classroomBook/adjustClassroomNo/update");
            if (!hasPermission) {
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????")));
                return;
            }

            classroomBook = classroomBookService.findById(classroomBookId);
            try {
                Map condition = new ObjectMapper().readValue(classroomBook.getBookCondition(), Map.class);
                buildingId = (Integer) condition.get("buildingId");
                seatRange = (String) condition.get("seatRange");
                isMedia = (Integer) condition.get("isMedia");
                bookDateStr = (String) condition.get("bookDateStr");
                bookPeriod = (String) condition.get("bookPeriod");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("?????????????????????????????????????????????????????????");
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
                return;
            }
        }

        if (buildingId.equals("0") || StringUtils.isBlank(bookDateStr) || StringUtils.isBlank(bookPeriod)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        //??????????????????
        List<String> startTimeList = new ArrayList<>();
        Date now = new Date();
        for (String time : bookPeriod.split(",")) {
            try {
                String str = bookDateStr + " " + time;
                Date startTime = DateUtils.parseDate(str, new String[]{"yyyy-MM-dd HH:mm"});
                if (startTime.compareTo(now) <= 0) {
                    ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "????????????????????????????????????")));
                    return;
                }
                startTimeList.add(str);
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("???????????????????????????{}", e.getMessage());
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "??????")));
                return;
            }
        }

        //??????????????????????????????
        TeachingWeek teachingWeek = teachingWeekService.getTeachingWeekBySchoolId(schoolId);
        if (teachingWeek == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "??????????????????????????????????????????")));
            return;
        }

        List<String> mondayList = new ArrayList<>();
        if (StringUtils.isNotBlank(teachingWeek.getMondayDate())) {
            mondayList = Arrays.asList(teachingWeek.getMondayDate().split(","));
        }
        int courseWeek = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date bookDate = null;
        for (int i = 0; i < mondayList.size(); i++) {
            String thisMonday = mondayList.get(i).toString();
            Date m, f;
            try {
                bookDate = sdf.parse(bookDateStr);
                m = sdf.parse(thisMonday);
                f = DateUtils.addDays(m, 6);
                if (bookDate.compareTo(m) >= 0 && bookDate.compareTo(f) <= 0) {
                    courseWeek = i + 1;
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("?????????????????????{}", e.getMessage());
            }
        }

        if (courseWeek == 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "????????????????????????????????????????????????")));
            return;
        }

        int courseWeekDay = TimeUtil.getDay(bookDate);
        List<Classroom> classroomList = classroomService.findReleaseList(schoolId, teachingWeek.getTermCode(), courseWeek,
                String.valueOf(courseWeekDay), buildingId, seatRange, isMedia, startTimeList, "seatCount", true);
        if (classroomList.size() == 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.NO_DATA_RETURN_PAGE, "???????????????????????????")));
        } else {
            //??????buildingId???createTime???name???no???status????????????
            List<Map> mapList = BeanUtil.beanListToMapList(classroomList, "retainKeys",
                    new String[]{"buildingId", "createTime", "name", "no", "status", "seatCount"});

            if (classroomBookId != null) {
                //??????????????????????????????????????????????????????
                for (Map map : mapList) {
                    map.put("selected", false);
                }


                List<String> selectedClassroomNoList;
                if (StringUtils.isBlank(classroomBook.getAdjustClassroomNo())) {
                    selectedClassroomNoList = Arrays.asList(classroomBook.getClassroomNo().split(","));
                } else {
                    selectedClassroomNoList = Arrays.asList(classroomBook.getAdjustClassroomNo().split(","));
                }

                ClassroomCriteria classroomEx = new ClassroomCriteria();
                classroomEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andBuildingIdEqualTo(buildingId)
                        .andNoIn(selectedClassroomNoList);
                classroomList = classroomService.findList(classroomEx);
                for (Classroom classroom : classroomList) {
                    Map map = new HashMap();
                    map.put("buildingId", classroom.getBuildingId());
                    map.put("createTime", classroom.getCreateTime());
                    map.put("name", classroom.getName() + "  (????????????" + classroom.getSeatCount() + ")");
                    map.put("no", classroom.getNo());
                    map.put("status", classroom.getStatus());
                    map.put("seatCount", classroom.getSeatCount());
                    map.put("selected", true);
                    mapList.add(map);
                }

                Collections.sort(mapList, new Comparator<Map>() {
                    @Override
                    public int compare(Map o1, Map o2) {
                        Integer c1 = Integer.valueOf(o1.get("seatCount").toString());
                        Integer c2 = Integer.valueOf(o2.get("seatCount").toString());
                        return c1.compareTo(c2);
                    }
                });
            }
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", mapList)));
        }
    }

    /**
     * ??????????????????
     *
     * @param schoolId          ??????ID
     * @param classroomBookId   ??????ID
     * @param adjustClassroomNo ????????????????????????A104,A106???
     * @param response
     */
    @RequestMapping("back_api_updateClassroomBook")
    public void app_updateClassroomBook(Integer schoolId, Integer classroomBookId, String adjustClassroomNo, HttpServletResponse response) {
        if (schoolId == null || classroomBookId == null || adjustClassroomNo == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        ClassroomBookCriteria example = new ClassroomBookCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdEqualTo(classroomBookId);
        List<ClassroomBook> classroomBookList = classroomBookService.findList(example);
        if (classroomBookList.isEmpty()) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        ClassroomBook classroomBook = classroomBookList.get(0);
        Date date = new Date();
        classroomBook.setUpdateTime(date);
        classroomBook.setAdjustClassroomNo(adjustClassroomNo);
        List<Date> startTimeList = new ArrayList<>();
        for (String time : classroomBook.getBookPeriod().split(",")) {
            try {
                Date startTime = DateUtils.parseDate(classroomBook.getBookDate() + " " + time, new String[]{"yyyy-MM-dd HH:mm"});
                startTimeList.add(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("???????????????????????????{}", e.getMessage());
                ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "??????")));
                return;
            }
        }

        Result result = classroomBookService.addOrUpdate(classroomBook, classroomBook.getBookDate(), startTimeList, schoolId, true, true);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * ??????????????????
     * <p>
     * ??????????????????adjustClassroomNo,isUseMedia,remark
     *
     * @param buildingId     ?????????ID
     * @param seatRange      ????????????
     * @param bookDate       ??????????????????
     * @param bookPeriod     ?????????????????????(08:00,08:50)
     * @param classroomNo    ???????????????????????????
     * @param useTypeId      ????????????ID
     * @param organizationId ??????ID
     * @param title          ??????
     * @param content        ??????
     * @param isMedia        ??????????????????????????????1??????2??????
     * @param isUseMedia     ????????????????????????(1??????2???)
     * @param userId         ?????????ID
     * @param linkMan        ?????????
     * @param linkPhone      ????????????
     * @param linkManNo      ???????????????
     * @param remark
     * @param response
     */
    @RequestMapping("back_api_addClassroomBook")
    public void back_api_addClassroomBook(Integer buildingId, String seatRange, String bookDate, String bookPeriod, String classroomNo,
                                          Integer useTypeId, Integer organizationId, String title, String content, Short isMedia, Short isUseMedia,
                                          Integer userId, String linkMan, String linkPhone, String linkManNo, String remark, HttpServletResponse response) {
        if (buildingId == null || bookDate == null || bookPeriod == null || classroomNo == null || useTypeId == null || organizationId == null
                || title == null || content == null || userId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        //??????????????????????????????map?????????json?????????
        Map conditionMap = new HashMap();
        conditionMap.put("userId", userId);
        conditionMap.put("buildingId", buildingId);
        conditionMap.put("seatRange", seatRange);
        conditionMap.put("isMedia", isMedia);
        conditionMap.put("bookDateStr", bookDate);
        conditionMap.put("bookPeriod", bookPeriod);
        String bookCondition;
        try {
            bookCondition = new ObjectMapper().writeValueAsString(conditionMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("????????????map??????json?????????????????????json?????????{}", e.getMessage());
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "??????????????????????????????????????????")));
            return;
        }

        User user = userService.getById(userId);

        ClassroomBook classroomBook = new ClassroomBook();
        classroomBook.setClassroomBuildingId(buildingId);
        classroomBook.setPersonCount(seatRange);
        classroomBook.setBookDate(bookDate);
        classroomBook.setBookPeriod(bookPeriod);
        classroomBook.setClassroomNo(classroomNo);
        classroomBook.setUseTypeId(useTypeId);
        classroomBook.setOrganizationId(organizationId);
        classroomBook.setTitle(title);
        classroomBook.setContent(content);
        classroomBook.setIsUseMedia(isUseMedia);
        classroomBook.setUserId(userId);
        if (StringUtils.isBlank(linkMan)) {
            classroomBook.setLinkMan(linkMan);
        } else {
            classroomBook.setLinkMan(user.getName());
        }
        if (StringUtils.isBlank(linkPhone)) {
            classroomBook.setLinkPhone(linkPhone);
        } else {
            classroomBook.setLinkPhone(user.getPhone());
        }
        if (StringUtils.isBlank(linkManNo)) {
            classroomBook.setLinkManNo(linkManNo);
        } else {
            classroomBook.setLinkManNo(user.getStuNumber());
        }

        classroomBook.setRemark(remark);
        //??????????????????????????????????????????????????????????????????????????????
        classroomBook.setBookCondition(bookCondition);

        List<Date> startTimeList = TimeUtil.getDateList(bookDate, bookPeriod);

        if (CollectionUtils.isEmpty(startTimeList)) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "???????????????????????????")));
            return;
        }

        ClassroomBookCriteria classroomBookEx = new ClassroomBookCriteria();
        ClassroomBookCriteria.Criteria criteria = classroomBookEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andStatusEqualTo(1)
                .andUseTypeIdEqualTo(classroomBook.getUseTypeId())
                .andOrganizationIdEqualTo(classroomBook.getOrganizationId())
                .andBookDateEqualTo(bookDate)
                .andBookPeriodEqualTo(bookPeriod);

        if (classroomBook.getId() != null) {
            criteria.andIdNotEqualTo(classroomBook.getId());
        }

        if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
            criteria.andAdjustClassroomNoEqualTo(classroomBook.getAdjustClassroomNo());
        } else {
            criteria.andClassroomNoEqualTo(classroomBook.getClassroomNo());
        }

        long count = classroomBookService.countByExample(classroomBookEx);
        if (count > 0) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "?????????????????????????????????????????????")));
            return;
        }

        AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(user.getSchoolId(), AudUserRole.JWRY.getCode());
        boolean isJw = false;
        if (auditUserRole != null) {
            AuditUserAuthCriteria auditUserAuthEx = new AuditUserAuthCriteria();
            auditUserAuthEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andUserIdEqualTo(userId)
                    .andRoleIdEqualTo(auditUserRole.getId());
            long c = auditUserAuthService.countByExample(auditUserAuthEx);
            isJw = c > 0 ? true : false;
        }
        Result result = classroomBookService.addOrUpdate(classroomBook, bookDate, startTimeList, user.getSchoolId(), false, isJw);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * xx???????????????????????????
     *
     * @param id
     * @param userId   ??????ID
     * @param status   ?????????1????????????2?????????3?????????
     * @param page     ??????
     * @param pageSize ?????????????????????????????????10
     * @param keyword  ?????????????????????
     * @param response
     */
    @RequestMapping("back_api_findClassroomBookList")
    public void back_api_findClassroomBookList(@RequestParam(defaultValue = "0", required = false) Integer id, Integer userId,
                                               @RequestParam(defaultValue = "0", required = false) Integer status, Integer page,
                                               @RequestParam(defaultValue = "10", required = false) Integer pageSize, String keyword, HttpServletResponse response) {
        if (page == null || userId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        User user = userService.getById(userId);

        PageLimitHolderFilter.setContext(page, pageSize, null);
        ClassroomBookCriteria example = new ClassroomBookCriteria();
        example.setOrderByClause("create_time desc");
        ClassroomBookCriteria.Criteria criteria = example.createCriteria()
                .andIsDeleteEqualTo((short) 2);

        if (id == 0) {
            criteria.andUserIdEqualTo(userId);
        } else {
            criteria.andIdEqualTo(id);
        }

        if (status != 0) {
            criteria.andStatusEqualTo(status);
        }
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andTitleLike("%" + keyword + "%");
        }

        List<ClassroomBook> classroomBookList = classroomBookService.findList(example);
        List<Map> mapList = new ArrayList<>();
        for (ClassroomBook classroomBook : classroomBookList) {
            String no = classroomBookService.getBookNo("JSYY", classroomBook.getId().toString());
            ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = classroomBookUseTypeOrganizationService.findById(classroomBook.getOrganizationId());
            String bookDate = classroomBook.getBookDate() + " "
                    + teachingWeekService.getCourseIndex(user.getSchoolId(),
                    Arrays.asList(classroomBook.getBookPeriod().split(",")));
            ClassroomBookUseType classroomBookUseType = classroomBookUseTypeService.findById(classroomBook.getUseTypeId());
            String remark = "";
            if (classroomBook.getStatus() == 3) {
                remark = classroomBook.getAuditRemark();
            }
            String statusText;
            switch (classroomBook.getStatus().intValue()) {
                case 1:
                    statusText = "?????????";
                    break;
                case 2:
                    statusText = "????????????";
                    break;
                case 3:
                    statusText = "???????????????";
                    break;
                default:
                    statusText = "";
                    break;
            }

            Map map = BeanUtil.beanToMap(classroomBook);
            map.put("no", no);
            map.put("organizationName", classroomBookUseTypeOrganization.getName());
            map.put("useTypeText", classroomBookUseType.getName());
            map.put("bookDate", bookDate);
            map.put("stuNumber", classroomBook.getLinkManNo());
            map.put("phone", classroomBook.getLinkPhone());
            map.put("title", classroomBook.getTitle());
            map.put("content", classroomBook.getContent());
            map.put("classroomNo", classroomBook.getClassroomNo());
            map.put("adjustClassroomNo", classroomBook.getAdjustClassroomNo());
            map.put("personCount", classroomBook.getPersonCount());
            map.put("status", classroomBook.getStatus());
            map.put("statusText", statusText);
            map.put("remark", remark);
            map.put("classroomBookId", classroomBook.getId());
            mapList.add(map);
        }

        Map data = new HashMap();
        data.put("list", mapList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", data)));
    }

    /**
     * ????????????????????????
     *
     * @param schoolId
     * @param response
     */
    @RequestMapping("back_api_findTeachingCourseTimePlanSelectorList")
    public void back_api_findTeachingCourseTimePlanSelectorList(Integer schoolId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.setOrderByClause("start_time asc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        List<Map> list = teachingWeekService.findTeachingCourseTimePlanMapList(example, "id", "name", "startTime", "periodType", "courseIndex");
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param schoolId
     * @param useTypeId  ????????????ID??????????????????
     * @param updateFlag ????????????????????????????????????1??????2??????
     * @param response
     */
    @RequestMapping("back_api_findClassroomBookUseTypeOrganizationSelectorList")
    public void back_api_findClassroomBookUseTypeOrganizationSelectorList(Integer schoolId,
                                                                          @RequestParam(required = false) Integer useTypeId,
                                                                          @RequestParam(defaultValue = "1", required = false) Integer updateFlag,
                                                                          HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        List<Map> list = classroomBookUseTypeOrganizationService.findList(schoolId, useTypeId, updateFlag);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * ????????????ID????????????????????????
     *
     * @param organizationId ??????ID
     * @param response
     */
    @RequestMapping("back_api_findClassroomBookUseTypeListByOrganizationId")
    public void back_api_findClassroomBookUseTypeListByOrganizationId(Integer organizationId, HttpServletResponse response) {
        if (organizationId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        List<ClassroomBookUseType> list = classroomBookUseTypeService.findList(organizationId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", list)));
    }

    /**
     * xx???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param userId
     * @param type     ?????????1?????????2?????????
     * @param page
     * @param pageSize
     * @param response
     */
    @RequestMapping("back_api_findClassroomBookAuditNoticeAndAuditList")
    public void back_api_findClassroomBookAuditNoticeAndAuditList(Integer userId, Integer type,
                                                                  Integer page, Integer pageSize, HttpServletResponse response) {
        if (userId == null || type == null || page == null || pageSize == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        List<Map> mapList = new ArrayList<>();
        PageLimitHolderFilter.setContext(page, pageSize, null);
        if (type == 1) {
            ClassroomBookAuditNoticeCriteria example = new ClassroomBookAuditNoticeCriteria();
            example.setOrderByClause("create_time desc");
            example.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andIsConfirmEqualTo((short) 2)
                    .andToUserIdEqualTo(userId);
            List<ClassroomBookAuditNotice> classroomBookAuditNoticeList = classroomBookAuditService.findAuditNoticeList(example);
            for (ClassroomBookAuditNotice classroomBookAuditNotice : classroomBookAuditNoticeList) {
                ClassroomBook classroomBook = classroomBookService.findById(classroomBookAuditNotice.getBookId());
                ClassroomBookUseType classroomBookUseType = classroomBookUseTypeService.findById(classroomBook.getUseTypeId());
                ClassroomBookUseTypeOrganization organization = classroomBookUseTypeOrganizationService.findById(classroomBook.getOrganizationId());

                String classroomNo = classroomBook.getClassroomNo();
                if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
                    classroomNo = classroomBook.getAdjustClassroomNo();
                }

                StringBuffer sb = new StringBuffer();
                sb.append(organization.getName())
                        .append("  ")
                        .append(classroomBook.getLinkMan())
                        .append("  ")
                        .append(classroomBook.getLinkManNo())
                        .append("  ")
                        .append(classroomBook.getTitle())
                        .append("  ")
                        .append(classroomBook.getContent())
                        .append("  ")
                        .append(classroomNo)
                        .append("  ")
                        .append(classroomBook.getBookDate())
                        .append("  ")
                        .append(teachingWeekService.getCourseIndex(organization.getSchoolId(), Arrays.asList(classroomBook.getBookPeriod().split(","))));

                String content = sb.toString();
                if (content.length() > 100) {
                    content = content.substring(0, 100) + "...";
                }

                Map map = new HashMap();
                map.put("id", classroomBookAuditNotice.getId());
                map.put("useTypeName", classroomBookUseType.getName());
                map.put("content", content);
                map.put("classroomBookId", classroomBookAuditNotice.getBookId());
                mapList.add(map);
            }
        } else {
            ClassroomBookAuditCriteria example = new ClassroomBookAuditCriteria();
            example.setOrderByClause("create_time desc");
            example.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andIsPassIsNull()
                    .andUserIdEqualTo(userId);
            List<ClassroomBookAudit> classroomBookAuditList = classroomBookAuditService.findList(example);
            for (ClassroomBookAudit classroomBookAudit : classroomBookAuditList) {
                ClassroomBook classroomBook = classroomBookService.findById(classroomBookAudit.getBookId());
                ClassroomBookUseType classroomBookUseType = classroomBookUseTypeService.findById(classroomBook.getUseTypeId());
                ClassroomBookUseTypeOrganization organization = classroomBookUseTypeOrganizationService.findById(classroomBook.getOrganizationId());

                String classroomNo = classroomBook.getClassroomNo();
                if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
                    classroomNo = classroomBook.getAdjustClassroomNo();
                }

                StringBuffer sb = new StringBuffer();
                sb.append(organization.getName())
                        .append("  ")
                        .append(classroomBook.getLinkMan())
                        .append("  ")
                        .append(classroomBook.getLinkManNo())
                        .append("  ")
                        .append(classroomBook.getTitle())
                        .append("  ")
                        .append(classroomBook.getContent())
                        .append("  ")
                        .append(classroomNo)
                        .append("  ")
                        .append(classroomBook.getBookDate())
                        .append("  ")
                        .append(teachingWeekService.getCourseIndex(organization.getSchoolId(), Arrays.asList(classroomBook.getBookPeriod().split(","))));

                String content = sb.toString();
                if (content.length() > 30) {
                    content = content.substring(0, 30) + "...";
                }

                Map map = new HashMap();
                map.put("id", classroomBookAudit.getId());
                map.put("useTypeName", classroomBookUseType.getName());
                map.put("content", content);
                map.put("classroomBookId", classroomBookAudit.getBookId());
                mapList.add(map);
            }
        }

        Map data = new HashMap();
        data.put("list", mapList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", data)));
    }

    /**
     * ??????xx????????????????????????
     *
     * @param schoolId
     * @param selectedUserId
     * @param response
     */
    @RequestMapping("back_api_findTeacherUserList")
    public void back_api_findTeacherUserList(Integer schoolId, String selectedUserId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "??????????????????")));
            return;
        }

        User user = new User();
        user.setSchoolId(schoolId);
        user.setIsDelete((short) 2);
        user.setIsAuth((short) 1);
        user.setIsTeacher((short) 1);
        user.setStatus((short) 1);
        List<User> userList = userService.findUserList(user);
        List<Map> userMapList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(selectedUserId)) {
            userIdList = Arrays.asList(selectedUserId.split(","));
        }
        for (User u : userList) {
            Map map = new HashMap();
            map.put("id", u.getId());
            map.put("name", u.getName());
            map.put("headimg", u.getHeadimg());
            map.put("stuNumber", u.getStuNumber());
            if (userIdList.contains(u.getId().toString())) {
                map.put("selected", true);
            } else {
                map.put("selected", false);
            }
            userMapList.add(map);
        }
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "??????", userMapList)));
    }
}
