package com.biu.wifi.campus.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biu.wifi.campus.dao.model.SchoolAddressBook;
import com.biu.wifi.campus.dao.model.SchoolAddressBookCriteria;
import com.biu.wifi.campus.dao.model.SchoolDepart;
import com.biu.wifi.campus.dao.model.SchoolDepartCriteria;
import com.biu.wifi.campus.dao.model.SchoolPosition;
import com.biu.wifi.campus.dao.model.SchoolPositionCriteria;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.SchoolAddressBookService;
import com.biu.wifi.campus.service.SchoolDepartService;
import com.biu.wifi.campus.service.SchoolPositionService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * 校园通讯录控制器
 *
 * @author zhangbin
 */
@Controller
public class BackendSchoolAddressBookController {

    @Autowired
    private SchoolDepartService schoolDepartService;
    @Autowired
    private SchoolAddressBookService schoolAddressBookService;
    @Autowired
    private SchoolPositionService schoolPositionService;

    /**
     * 添加或编辑部门
     *
     * @param id
     * @param pid      父级ID
     * @param name     部门名称
     * @param schoolId 学校ID
     * @param flag     edit为编辑
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateSchoolDepart")
    public void addOrUpdateSchoolDepart(Integer id, @RequestParam(defaultValue = "0") Integer pid, String name,
                                        Integer schoolId, String flag, HttpServletResponse response) {
        if (name == null || schoolId == null || ("edit".equals(flag) && id == null)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        SchoolDepart schoolDepart = new SchoolDepart();
        if (id == 0) {
            id = null;
        }
        schoolDepart.setId(id);
        schoolDepart.setSchoolId(schoolId);
        schoolDepart.setName(name);
        schoolDepart.setPid(pid);
        Result result = schoolDepartService.addOrUpdate(schoolDepart);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 删除部门
     *
     * @param id
     * @param response
     */
    @RequestMapping("back_api_deleteSchoolDepart")
    public void deleteSchoolDepart(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Result result = schoolDepartService.delete(id);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 根据ID查看部门详情
     *
     * @param response
     * @param id
     */
    @RequestMapping("back_api_schoolDepart")
    public void getSchoolDepart(HttpServletResponse response, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Map<String, Object> schoolDepart = schoolDepartService.getSchoolDepartMap(id);
        if (schoolDepart == null
                || (schoolDepart != null && Integer.valueOf("1").equals(schoolDepart.get("isDelete")))) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null));
            return;
        }

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", schoolDepart));
    }

    /**
     * 查看部门列表
     *
     * @param response
     * @param name     部门名称
     * @param pid      上级部门ID
     * @param schoolId 学校ID
     */
    @RequestMapping("back_api_schoolDepartList")
    public void getSchoolDepartList(HttpServletResponse response, String name, Integer pid, Integer schoolId) {
        if (schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        SchoolDepartCriteria example = new SchoolDepartCriteria();
        example.setOrderByClause("create_time desc");
        SchoolDepartCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId).andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(name)) {
            criteria.andNameLike(name);
        }
        if (pid != null) {
            criteria.andPidEqualTo(pid);
        }

        List<Map<String, Object>> schoolDepartList = schoolDepartService.getSchoolDeparMaptList(example);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", schoolDepartList));
    }

    /**
     * 添加或编辑职位
     *
     * @param request
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateSchoolPosition")
    public void addOrUpdateSchoolPosition(HttpServletRequest request, HttpServletResponse response) {
        Object idObj = request.getParameter("id");
        Object nameObj = request.getParameter("name");
        Object departIdObj = request.getParameter("departId");
        Object flag = request.getParameter("flag");

        if (nameObj == null || departIdObj == null || ("edit".equals(flag)) && idObj == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Integer id = idObj == null ? null : Integer.valueOf(String.valueOf(idObj));
        String name = String.valueOf(nameObj);
        Integer departId = departIdObj == null ? null : Integer.valueOf(String.valueOf(departIdObj));

        SchoolPosition record = new SchoolPosition();
        record.setId(id);
        record.setName(name);
        record.setDepartId(departId);

        Result result = schoolPositionService.addOrUpdate(record);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 删除职位
     *
     * @param id
     * @param response
     */
    @RequestMapping("back_api_deleteSchoolPosition")
    public void deleteSchoolPosition(Integer id, HttpServletResponse response) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Result result = schoolPositionService.delete(id);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 根据ID查看职位详情
     *
     * @param response
     * @param id
     */
    @RequestMapping("back_api_schoolPosition")
    public void getSchoolPosition(HttpServletResponse response, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        SchoolPosition schoolPosition = schoolPositionService.getSchoolPosition(id);
        if (schoolPosition == null || (schoolPosition != null && schoolPosition.getIsDelete() == 1)) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null));
        }

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", schoolPosition));
    }

    /**
     * 查看职位列表
     *
     * @param response
     * @param page     页码
     * @param pageSize 每页记录数
     * @param name     职位名称
     * @param departId 部门ID
     */
    @RequestMapping("back_api_schoolPositionList")
    public void getSchoolPositionList(HttpServletResponse response, Integer page, Integer pageSize, String name,
                                      Integer departId) {
        if (page == null || pageSize == null || departId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);

        SchoolPositionCriteria example = new SchoolPositionCriteria();
        SchoolPositionCriteria.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            criteria.andNameLike(name);
        }
        if (departId != null) {
            criteria.andDepartIdEqualTo(departId);
        }

        List<SchoolPosition> schoolPositionList = schoolPositionService.getSchoolPositionList(example);

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", schoolPositionList));
    }

    /**
     * 添加或编辑通讯录
     *
     * @param request
     * @param response
     */
    @RequestMapping("back_api_addOrUpdateSchoolAddressBook")
    public void addOrUpdateSchoolAddressBook(HttpServletRequest request, HttpServletResponse response) {
        Object idObject = request.getParameter("id");
        Object nameObj = request.getParameter("name");
        Object departIdObj = request.getParameter("departId");
        Object positionIdObj = request.getParameter("positionName");
        Object officePositionObj = request.getParameter("officePosition");
        Object phoneObj = request.getParameter("phone");
        Object telObj = request.getParameter("tel");
        Object flag = request.getParameter("flag");
        Object headImgObj = request.getParameter("headImg");

        if (nameObj == null || departIdObj == null || positionIdObj == null || (phoneObj == null && telObj == null)
                || ("edit".equals(flag) && idObject == null)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Integer id = idObject == null || "".equals(idObject) ? null : Integer.valueOf(String.valueOf(idObject));
        String name = String.valueOf(nameObj);
        String phone = phoneObj == null ? null : String.valueOf(phoneObj);
        String tel = telObj == null ? null : String.valueOf(telObj);
        Integer departId = Integer.valueOf(String.valueOf(departIdObj));
        String positionName = String.valueOf(positionIdObj);
        String officePosition = String.valueOf(officePositionObj);
        String headImg = headImgObj == null || "".equals(headImgObj) ? null : String.valueOf(headImgObj);

        SchoolAddressBook schoolAddressBook = new SchoolAddressBook();
        schoolAddressBook.setId(id);
        schoolAddressBook.setName(name);
        schoolAddressBook.setPhone(phone);
        schoolAddressBook.setTel(tel);
        schoolAddressBook.setDepartId(departId);
        schoolAddressBook.setPositionName(positionName);
        schoolAddressBook.setOfficePosition(officePosition);
        schoolAddressBook.setHeadImg(headImg);

        Result result = schoolAddressBookService.addOrUpdate(schoolAddressBook);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 删除通讯录
     *
     * @param response
     * @param id
     */
    @RequestMapping("back_api_deleteSchoolAddressBook")
    public void deleteSchoolAddressBook(HttpServletResponse response, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Result result = schoolAddressBookService.delete(id);
        ServletUtilsEx.renderJson(response, result);
    }

    /**
     * 根据ID查看通讯录详情
     *
     * @param response
     * @param id
     */
    @RequestMapping("back_api_schoolAddressBook")
    public void getSchoolAddressBook(HttpServletResponse response, Integer id) {
        if (id == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        SchoolAddressBook schoolAddressBook = schoolAddressBookService.getSchoolAddressBook(id);
        if (schoolAddressBook == null || (schoolAddressBook != null && schoolAddressBook.getIsDelete() == 1)) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null));
        }

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", schoolAddressBook));
    }

    /**
     * 查看通讯录列表
     *
     * @param response
     * @param page       页码
     * @param pageSize   每页记录数
     * @param name       姓名
     * @param departName 部门名称
     * @param phone      办公室电话或手机号
     * @param schoolId   学校ID
     */
    @RequestMapping("back_api_schoolAddressBookList")
    public void getSchoolAddressBookList(HttpServletResponse response, Integer page, Integer pageSize, String name,
                                         String departName, String phone, Integer schoolId) {
        if (page == null || pageSize == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);

        // name like ? and departName like ? and (phone like ? or tel like ?)
        SchoolAddressBookCriteria example = new SchoolAddressBookCriteria();
        example.setOrderByClause("create_time desc");
        SchoolAddressBookCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId).andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (StringUtils.isNotBlank(departName)) {
            criteria.andDepartNameLike("%" + departName + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            criteria.andPhoneLike("%" + phone + "%");
        }

        SchoolAddressBookCriteria.Criteria criteria2 = example.createCriteria();
        criteria2.andSchoolIdEqualTo(schoolId).andIsDeleteEqualTo((short) 2);
        if (StringUtils.isNotBlank(name)) {
            criteria2.andNameLike("%" + name + "%");
        }
        if (StringUtils.isNotBlank(departName)) {
            criteria2.andDepartNameLike("%" + departName + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            criteria2.andTelLike("%" + phone + "%");
        }
        example.or(criteria2);

        List<Map<String, Object>> schoolAddressBookList = schoolAddressBookService.getSchoolAddressBookMapList(example);

        Map<String, Object> map = new HashMap<>();
        map.put("list", schoolAddressBookList);
        map.put("totalCount", PageLimitHolderFilter.getContext().getTotalCount());

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", map));
    }

}
