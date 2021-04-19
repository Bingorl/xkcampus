package com.biu.wifi.campus.service;

import com.biu.wifi.campus.BaseJunit;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUser;
import com.biu.wifi.campus.dao.model.ClassroomBookUseTypeOrganizationAuditUserCriteria;
import com.biu.wifi.campus.result.Result;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/12/6.
 */
public class ClassroomBookUseTypeOrganizationAuditUserServiceTest extends BaseJunit {

    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserService classroomBookUseTypeOrganizationAuditUserService;

    @Test
    public void findList() throws Exception {
        ClassroomBookUseTypeOrganizationAuditUserCriteria example = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUseTypeIdEqualTo(1);
        List<ClassroomBookUseTypeOrganizationAuditUser> list = classroomBookUseTypeOrganizationAuditUserService.findList(example);
        logger.info("接口返回数据：{}", list);
    }

    @Test
    public void findList1() throws Exception {
        ClassroomBookUseTypeOrganizationAuditUserCriteria example = new ClassroomBookUseTypeOrganizationAuditUserCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUseTypeIdEqualTo(1);
        List<Map> list = classroomBookUseTypeOrganizationAuditUserService.findList(example, new HashMap());
        logger.info("接口返回数据：{}", list);
    }

    @Test
    public void addOrUpdate() throws Exception {
        Integer useTypeId = 1;
        List<ClassroomBookUseTypeOrganizationAuditUser> list = new ArrayList<>();
        ClassroomBookUseTypeOrganizationAuditUser user = new ClassroomBookUseTypeOrganizationAuditUser();
        user.setOrganizationId(1);
        user.setUseTypeId(useTypeId);
        user.setAuditUser("1,2,3");
        list.add(user);

        user = new ClassroomBookUseTypeOrganizationAuditUser();
        user.setOrganizationId(2);
        user.setUseTypeId(useTypeId);
        user.setAuditUser("3,4,5");
        list.add(user);

        user = new ClassroomBookUseTypeOrganizationAuditUser();
        user.setOrganizationId(3);
        user.setUseTypeId(useTypeId);
        user.setAuditUser("5,6,7");
        list.add(user);

        Result result = classroomBookUseTypeOrganizationAuditUserService.addOrUpdate(list);
        logger.info("接口返回数据：{}", new Gson().toJson(result));
    }

}