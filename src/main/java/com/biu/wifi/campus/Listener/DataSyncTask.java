package com.biu.wifi.campus.Listener;

import com.biu.wifi.campus.dao.ClassroomBookUseTypeOrganizationMapper;
import com.biu.wifi.campus.dao.InstituteMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.service.JwDataTableService;
import com.biu.wifi.campus.service.SchoolService;
import com.biu.wifi.core.support.cache.CacheSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/11/27.
 */
// @Component
public class DataSyncTask {

    public static Logger logger = LoggerFactory.getLogger(DataSyncTask.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private JwDataTableService jwDataTableService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private InstituteMapper instituteMapper;
    @Autowired
    private ClassroomBookUseTypeOrganizationMapper classroomBookUseTypeOrganizationMapper;

    /**
     * 数据导入调度
     * <p>
     * 功能：半小时执行一次，如果检测到存在上传数据sql文件，则开始导入数据
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void syncData() {
        School school = new School();
        school.setIsDelete((short) 2);
        List<School> schoolList = schoolService.findSchoolList(school);
        for (School s : schoolList) {
            JwDataTableCriteria example = new JwDataTableCriteria();
            example.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andIsSyncEqualTo((short) 2)
                    .andSchoolIdEqualTo(s.getId());
            List<JwDataTable> tables = jwDataTableService.findList(example);
            for (JwDataTable table : tables) {
                String fileId = CacheSupport.get("jwDataTableSyncTask", "fileId_" + table.getFileId(), String.class);
                if (fileId == null) {
                    //根据fileId查询缓存，没有进行中的同步任务
                    jwDataTableService.syncData(table);
                }
            }
        }
    }

    /**
     * 将学院表同步更新到组织表
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void autoImportInstituteInToOrganization() {
        logger.info("开始执行学院表---->组织表的数据同步调度任务");
        School school = new School();
        school.setIsDelete((short) 2);
        List<School> schoolList = schoolService.findSchoolList(school);
        for (School s : schoolList) {
            InstituteCriteria instituteEx = new InstituteCriteria();
            instituteEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andSchoolIdEqualTo(s.getId());
            List<Institute> instituteList = instituteMapper.selectByExample(instituteEx);
            for (Institute institute : instituteList) {
                ClassroomBookUseTypeOrganizationCriteria organizationEx = new ClassroomBookUseTypeOrganizationCriteria();
                organizationEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andTypeEqualTo((short) 1)
                        .andSchoolIdEqualTo(s.getId())
                        .andInstituteIdEqualTo(institute.getId());

                long count = classroomBookUseTypeOrganizationMapper.countByExample(organizationEx);
                if (count == 0) {
                    ClassroomBookUseTypeOrganization organization = new ClassroomBookUseTypeOrganization();
                    organization.setType((short) 1);
                    organization.setName(institute.getName());
                    organization.setSchoolId(s.getId());
                    organization.setInstituteId(institute.getId());
                    organization.setCreateTime(new Date());
                    classroomBookUseTypeOrganizationMapper.insertSelective(organization);
                }
            }
        }
    }
}
