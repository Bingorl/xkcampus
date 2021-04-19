package com.biu.wifi.campus.service;

import com.biu.wifi.campus.dao.TemperatureReportMapper;
import com.biu.wifi.campus.dao.model.TemperatureReport;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.core.util.DateUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author 张彬.
 * @date 2021/4/9 16:04.
 */
@Service
public class TemperatureReportService {

    @Autowired
    private TemperatureReportMapper temperatureReportMapper;

    public void add(TemperatureReport req) {
        req.setCreateTime(new Date());
        if (req.getReportTime() == null) {
            req.setReportTime(new Date());
        }
        req.setIsDelete(2);
        boolean json = temperatureReportMapper.insertSelective(req) > 0;
        if (!json) {
            throw new BizException(Result.CUSTOM_MESSAGE, "上报失败");
        }
    }

    public List<TemperatureReport> myTemperatureReportList(Integer userId) {
        return temperatureReportMapper.search(userId);
    }

    public List<HashMap> myTemperatureReportMapList(Integer userId) {
        List<TemperatureReport> temperatureReports = myTemperatureReportList(userId);
        List<HashMap> list = new ArrayList<>();
        for (TemperatureReport report : temperatureReports) {
            HashMap hashMap = new HashMap();
            hashMap.put("id", report.getId());
            hashMap.put("temperature", report.getTemperature());
            hashMap.put("reportTime", DateUtilsEx.formatToString(report.getReportTime(), "yyyy-MM-dd HH:mm:ss"));
            list.add(hashMap);
        }
        return list;
    }
}
