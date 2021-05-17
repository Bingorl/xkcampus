package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.dao.model.BiuTravelExpenseAudit;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseAuditUser;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseDetail;
import com.biu.wifi.campus.dao.model.BiuTravelExpenseInfo;
import com.biu.wifi.campus.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author MCX
 * @Version 1.0
 **/
@Controller
@RequestMapping
public class AppTravelExpenseController {
    @Autowired
    private BiuTravelExpenseAuditService biuTravelExpenseAuditService;
    @Autowired
    private BiuTravelExpenseNoticeService biuTravelExpenseNoticeService;
    @Autowired
    private BiuTravelExpenseAuditUserService biuTravelExpenseAuditUserService;
    @Autowired
    private BiuTravelExpenseInfoService biuTravelExpenseInfoService;
    @Autowired
    private BiuTravelExpenseDetailService biuTravelExpenseDetailService;


}
