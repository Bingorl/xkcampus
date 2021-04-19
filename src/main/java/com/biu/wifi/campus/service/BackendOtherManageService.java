package com.biu.wifi.campus.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.campus.dao.FeedBackMapper;
import com.biu.wifi.campus.dao.HelpMapper;
import com.biu.wifi.campus.dao.MessageMapper;
import com.biu.wifi.campus.dao.ReportMapper;
import com.biu.wifi.campus.dao.SystemMessageMapper;
import com.biu.wifi.campus.dao.model.FeedBack;
import com.biu.wifi.campus.dao.model.Help;
import com.biu.wifi.campus.dao.model.Message;
import com.biu.wifi.campus.dao.model.Report;
import com.biu.wifi.campus.dao.model.SystemMessage;
import com.biu.wifi.campus.dao.model.SystemMessageCriteria;
import com.biu.wifi.campus.dao.model.SystemMessageCriteria.Criteria;
import com.biu.wifi.campus.daoEx.BackendOtherManageMapperEx;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;

@Service
public class BackendOtherManageService {

    @Autowired
    private BackendOtherManageMapperEx otherManageMapperEx;

    @Autowired
    private HelpMapper helpMapper;

    @Autowired
    private SystemMessageMapper systemMessageMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private FeedBackMapper feedBackMapper;

    @Autowired
    private ReportMapper reportMapper;

    public List<Map<String, Object>> findReportList(Integer schoolId) {
        return otherManageMapperEx.findReportList(schoolId);
    }

    public List<Map<String, Object>> findFeedbackList() {
        return otherManageMapperEx.findFeedbackList();
    }

    public List<Help> findHelpList(Help entity) {
        return IbatisServiceUtils.find(entity, helpMapper, "id asc");
    }

    public void addHelp(Help entity) {
        helpMapper.insertSelective(entity);
    }

    public Help getHelp(Help entity) {
        return IbatisServiceUtils.get(entity, helpMapper);
    }

    public void updateHelp(Help entity) {
        helpMapper.updateByPrimaryKeySelective(entity);
    }

    public List<SystemMessage> findSystemMessageList() {
        SystemMessageCriteria systemMessageCriteria = new SystemMessageCriteria();
        systemMessageCriteria.setOrderByClause("send_time desc");
        Criteria criteria = systemMessageCriteria.createCriteria();
        criteria.andIsDeteleEqualTo((short) 2);
        return systemMessageMapper.selectByExample(systemMessageCriteria);
    }

    public List<Map<String, Object>> findUserListByName(String name) {
        return otherManageMapperEx.findUserListByName(name);
    }

    public void addSystemMessage(SystemMessage entity) {
        systemMessageMapper.insertSelective(entity);
    }

    public void addMessage(Message entity) {
        messageMapper.insertSelective(entity);
    }

    public void updateFeedback(FeedBack entity) {
        feedBackMapper.updateByPrimaryKeySelective(entity);
    }

    public void updateReport(Report entity) {
        reportMapper.updateByPrimaryKeySelective(entity);
    }
}
