package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.constant.AuditBusinessType;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/7 21:59.
 */
@Controller
public class AppAuditTypeController {

    /**
     * 审批类型列表
     *
     * @param response
     * @return
     * @author 张彬.
     * @date 2021/4/7 22:02.
     */
    @RequestMapping("app_auditTypeList")
    public void auditTypeList(HttpServletResponse response) {
        List<HashMap> list = new ArrayList<>();
        for (AuditBusinessType auditBusinessType : AuditBusinessType.values()) {
            HashMap hashMap = new HashMap();
            hashMap.put("name", auditBusinessType.getDescription());
            hashMap.put("value", auditBusinessType.getCode());
            list.add(hashMap);
        }

        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", list));
    }
}
