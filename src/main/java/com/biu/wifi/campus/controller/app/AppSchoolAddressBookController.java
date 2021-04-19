package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.dao.model.SchoolAddressBook;
import com.biu.wifi.campus.dao.model.SchoolAddressBookCriteria;
import com.biu.wifi.campus.dao.model.SchoolDepart;
import com.biu.wifi.campus.dao.model.SchoolDepartCriteria;
import com.biu.wifi.campus.dto.SchoolDepartDto;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.SchoolAddressBookService;
import com.biu.wifi.campus.service.SchoolDepartService;
import com.biu.wifi.campus.service.UserService;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbin
 */
@Controller
public class AppSchoolAddressBookController extends AuthenticatorController {

    @Autowired
    private SchoolDepartService schoolDepartService;
    @Autowired
    private SchoolAddressBookService schoolAddressBookService;
    @Autowired
    private UserService userService;

    /**
     * 通讯录列表
     *
     * @param response
     * @param schoolId 学校ID
     * @param pid      上级部门ID
     * @param userId   用户ID
     */
    @RequestMapping("app_getSchoolDepartList")
    public void getSchoolDepartList(HttpServletResponse response, Integer schoolId,
                                    @RequestParam(defaultValue = "0") Integer pid, @ModelAttribute("user_id") Integer userId) {
        if (schoolId == null || pid == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        // 通过pid查询分类列表
        SchoolDepartCriteria example = new SchoolDepartCriteria();
        SchoolDepartCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId).andPidEqualTo(pid).andIsDeleteEqualTo((short) 2);
        List<SchoolDepart> schoolDepartList = schoolDepartService.getSchoolDepartList(example);

        if (pid == 0 && schoolDepartList.size() == 0) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "请先设置通讯录部门", null)));
            return;
        }

        // 通过pid查询通讯录人员列表
        SchoolAddressBookCriteria example2 = new SchoolAddressBookCriteria();
        example2.createCriteria().andDepartIdEqualTo(pid).andIsDeleteEqualTo((short) 2);
        List<SchoolAddressBook> schoolAddressBookList = schoolAddressBookService.getSchoolAddressBookList(example2);

        List<SchoolDepartDto> schoolDepartDtoList = new ArrayList<>();
        if (schoolDepartList.size() > 0) {
            for (SchoolDepart depart : schoolDepartList) {
                SchoolDepartDto dto = new SchoolDepartDto();
                BeanUtils.copyProperties(depart, dto);

                // 子部门列表
                example = new SchoolDepartCriteria();
                criteria = example.createCriteria();
                criteria.andPidEqualTo(depart.getId()).andIsDeleteEqualTo((short) 2);
                List<SchoolDepart> children = schoolDepartService.getSchoolDepartList(example);
                dto.setSchoolDepartList(children);

                // 通讯录人员列表
                example2 = new SchoolAddressBookCriteria();
                example2.createCriteria().andDepartIdEqualTo(depart.getId()).andIsDeleteEqualTo((short) 2);
                schoolAddressBookList = schoolAddressBookService.getSchoolAddressBookList(example2);
                schoolAddressBookList = schoolAddressBookService.hideTel(schoolAddressBookList);
                dto.setSchoolAddressBookList(schoolAddressBookList);

                schoolDepartDtoList.add(dto);
            }
        } else {
            // 所传的pid是二级分类的ID，只有通讯录列表
            SchoolDepart depart = schoolDepartService.getSchoolDepart(pid);
            SchoolDepartDto dto = new SchoolDepartDto();
            BeanUtils.copyProperties(depart, dto);
            // 通讯录人员列表
            example2 = new SchoolAddressBookCriteria();
            example2.createCriteria().andDepartIdEqualTo(depart.getId()).andIsDeleteEqualTo((short) 2);
            schoolAddressBookList = schoolAddressBookService.getSchoolAddressBookList(example2);
            schoolAddressBookList = schoolAddressBookService.hideTel(schoolAddressBookList);
            dto.setSchoolAddressBookList(schoolAddressBookList);
            schoolDepartDtoList.add(dto);
        }

        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", schoolDepartDtoList)));
    }

    /**
     * 通讯录详情
     *
     * @param response
     * @param id
     * @param userId   用户ID
     */
    @RequestMapping("app_getSchoolAddressBook")
    public void getSchoolAddressBook(HttpServletResponse response, Integer id,
                                     @ModelAttribute("user_id") Integer userId) {
        if (id == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "缺少必要参数", null)));
            return;
        }

        SchoolAddressBook schoolAddressBook = schoolAddressBookService.getSchoolAddressBook(id);
        boolean isTeacher = userService.isTeacher(userId);
        if (!isTeacher) {
            schoolAddressBook = schoolAddressBookService.hideTel(schoolAddressBook);
        }

        if (schoolAddressBook == null || (schoolAddressBook != null && schoolAddressBook.getIsDelete() == 1)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "该记录不存在", null)));
            return;
        }

        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", schoolAddressBook)));
    }

    /**
     * 通讯录搜索
     *
     * @param response
     * @param name     姓名关键字
     * @param page     页码
     * @param userId   用户ID
     * @param schoolId 学校ID
     */
    @RequestMapping("app_getSchoolAddressBookList")
    public void getSchoolAddressBookList(HttpServletResponse response, String name, Integer page,
                                         @ModelAttribute("user_id") Integer userId, Integer schoolId) {
        if (page == null || StringUtils.isBlank(name)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "请输入姓名关键字", null)));
            return;
        }

        PageLimitHolderFilter.setContext(page, 10, null);

        SchoolAddressBookCriteria example = new SchoolAddressBookCriteria();
        example.createCriteria().andSchoolIdEqualTo(schoolId).andNameLike("%" + name + "%")
                .andIsDeleteEqualTo((short) 2);
        List<SchoolAddressBook> schoolAddressBookList = schoolAddressBookService.getSchoolAddressBookList(example);

        boolean isTeacher = userService.isTeacher(userId);
        if (!isTeacher) {
            schoolAddressBookList = schoolAddressBookService.hideTel(schoolAddressBookList);
        }
        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", schoolAddressBookList)));
    }
}
