package com.biu.wifi.campus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.ReportMapper;
import com.biu.wifi.campus.dao.model.Report;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class ReportService {

    @Autowired
    private ReportMapper reportMapper;

    public void addReport(Report entity) {
        try {
            IbatisServiceUtils.insert(entity, reportMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Report getReport(Report entity) {
        return IbatisServiceUtils.get(entity, reportMapper);
    }

    public void updateReport(Report entity) {
        IbatisServiceUtils.updateByPk(entity, reportMapper);
    }

    public void deleteReport(Report entity) {
        try {
            IbatisServiceUtils.delete(entity, reportMapper);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public List<Report> findReportList(Report entity) {
        return IbatisServiceUtils.find(entity, reportMapper);
    }
}
