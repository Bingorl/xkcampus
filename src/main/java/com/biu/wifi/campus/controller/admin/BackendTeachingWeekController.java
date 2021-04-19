package com.biu.wifi.campus.controller.admin;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.Tool.NginxFileUtils;
import com.biu.wifi.campus.Tool.SqlUtils;
import com.biu.wifi.campus.dao.model.JwDataTable;
import com.biu.wifi.campus.dao.model.JwDataTableCriteria;
import com.biu.wifi.campus.dao.model.TeachingCourseTimePlan;
import com.biu.wifi.campus.dao.model.TeachingCourseTimePlanCriteria;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.JwDataTableService;
import com.biu.wifi.campus.service.TeacherCoursePlanService;
import com.biu.wifi.campus.service.TeachingWeekService;
import com.biu.wifi.core.support.cache.CacheSupport;
import com.biu.wifi.core.support.pageLimit.PageLimitHolderFilter;
import com.biu.wifi.core.support.servlet.ServletHolderFilter;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/11/8.
 */
@Controller
public class BackendTeachingWeekController {

    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private JwDataTableService jwDataTableService;
    @Autowired
    private TeacherCoursePlanService teacherCoursePlanService;


    /**
     * 新增或编辑学期
     *
     * @param id
     * @param schoolId
     * @param termPeriod 学期周期
     * @param termCount  学期数：默认为第一学期
     * @param response
     */
    @RequestMapping("back_api_addOrUpdate")
    public void back_api_addOrUpdate(Integer id, Integer schoolId, String termPeriod,
                                     @RequestParam(defaultValue = "1") Integer termCount, HttpServletResponse response) {
        if (schoolId == null || StringUtils.isBlank(termPeriod)) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        String[] termPeriods = termPeriod.split("~");
        String startYear = termPeriods[0].substring(0, 4);
        String endYear = termPeriods[1].substring(0, 4);

        if (startYear.compareTo(endYear) > 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.FAILURE, "课程周期不正确，请重新选择", null));
            return;
        }

        StringBuffer termCode = new StringBuffer();
        termCode.append(startYear)
                .append("-")
                .append(endYear)
                .append("-")
                .append(termCount)
                .append("-1");
        StringBuffer termName = new StringBuffer();
        termName.append(startYear)
                .append("-")
                .append(endYear)
                .append("学年,第")
                .append(termCount)
                .append("学期");
        teachingWeekService.addOrUpdate(id, schoolId, termCode.toString(), termName.toString(), termPeriod);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", null));
    }

    /**
     * 学期列表
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param termName 学期名关键字
     * @param response
     */
    @RequestMapping("back_api_findList")
    public void back_api_findList(Integer page, Integer pageSize, Integer schoolId, String termName, HttpServletResponse response) {
        if (page == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }


        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<Map> teachingWeekList = teachingWeekService.findMapList(schoolId, termName);
        Map data = new HashMap();
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        data.put("list", teachingWeekList);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 删除学年学期
     *
     * @param id
     * @param schoolId
     * @param response
     */
    @RequestMapping("back_api_deleteTerm")
    public void back_api_deleteTerm(Integer id, Integer schoolId, HttpServletResponse response) {
        if (id == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        Result result = teachingWeekService.delete(id, schoolId);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", result));
    }

    /**
     * 同步数据表列表
     * <p>
     * 展现已经同步的数据表和未同步的数据表
     *
     * @param page
     * @param pageSize
     * @param schoolId
     * @param response
     */
    @RequestMapping("back_api_findDataTableSyncList")
    public void back_api_findDataTableSyncList(Integer page, Integer pageSize, Integer schoolId, String keyword, HttpServletResponse response) {
        if (page == null || pageSize == null || schoolId == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空", null));
            return;
        }

        JwDataTableCriteria example = new JwDataTableCriteria();
        example.setOrderByClause("create_time desc");
        JwDataTableCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo((short) 2);
        criteria.andSchoolIdEqualTo(schoolId);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andTableNameLike("%" + keyword + "%");
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<JwDataTable> tables = jwDataTableService.findList(example);
        List<Map> tablesMapList = new ArrayList<>();
        for (JwDataTable jwDataTable : tables) {
            Map map = new HashedMap();
            map.put("id", jwDataTable.getId());
            map.put("tableName", jwDataTable.getTableName());
            map.put("tableDdl", jwDataTable.getTableDdl());
            String fileId = CacheSupport.get("jwDataTableSyncTask", "fileId_" + jwDataTable.getFileId(), String.class);
            map.put("isSync", jwDataTable.getIsSync().intValue() == 1 ? "是" : (fileId == null ? "否" : "进行中"));
            map.put("createTime", jwDataTable.getCreateTime());
            if (StringUtils.equals("否", map.get("isSync").toString())) {
                map.put("showSyncFlag", true);
            } else {
                map.put("showSyncFlag", false);
            }
            tablesMapList.add(map);
        }

        Map data = new HashMap();
        data.put("list", tablesMapList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 上传sql文件，并保存sql文件记录
     *
     * @param response
     */
    @RequestMapping("back_api_uploadSqlFile")
    public void back_api_uploadSqlFile(HttpServletResponse response) {
        Map<String, Object> params = ServletHolderFilter.getContext().getParamMap();
        Object file = params.get("file");
        Object schoolId = params.get("schoolId");
        if (file == null || schoolId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "请上传sql文件")));
            return;
        }

        try {
            List<DiskFileItem> diskFileItemList = (List<DiskFileItem>) params.get("file");
            DiskFileItem item = diskFileItemList.get(0);
            byte[] content = item.get();
            String fileId = NginxFileUtils.add(item.getName(), content, "ds_upload", null);
            List<String> list = SqlUtils.readSqlFile(new ByteArrayInputStream(content));
            //生成insert语句
            List<String> sqlList = SqlUtils.generateInsertSql(list);
            //获取表名
            String tableName = SqlUtils.getTableName(sqlList.get(0));
            //生成创建表的语句
            String tableDDL = SqlUtils.generateTableDDL(tableName, sqlList.get(0));
            jwDataTableService.add(Integer.valueOf(schoolId.toString()), fileId, tableName, tableDDL);
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
        } catch (Exception e) {
            e.printStackTrace();
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.FAILURE, "失败")));
        }
    }


    /**
     * 开始执行数据同步
     * <p>
     * 从sql文件导入数据到数据库
     *
     * @param dataTableId
     * @param toggle
     * @param response
     */
    @RequestMapping("back_api_sync")
    public void back_api_sync(Integer dataTableId, String toggle, HttpServletResponse response) {
        if (dataTableId == null || toggle == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }


        //每次只允许一个任务进行
        List cacheList = CacheSupport.get("jwDataTableSyncTask", String.class);
        if (CollectionUtils.isEmpty(cacheList)) {
            //同步数据
            jwDataTableService.syncData(dataTableId);
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
        } else {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "已有一个数据同步任务正在进行，请等待完成...")));
        }
    }

    /**
     * 上课时间计划列表
     *
     * @param schoolId
     * @param page
     * @param pageSize
     * @param periodType 时间段（1上午，2中午，3下午，4晚上）
     * @param keyword    节次名称关键字
     * @param response
     */
    @RequestMapping("back_api_findTeachingCourseTimePlanList")
    public void back_api_findTeachingCourseTimePlanList(Integer schoolId, Integer page, Integer pageSize,
                                                        @RequestParam(defaultValue = "0") Short periodType,
                                                        String keyword, HttpServletResponse response) {
        if (schoolId == null || page == null || pageSize == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.setOrderByClause("start_time asc");
        TeachingCourseTimePlanCriteria.Criteria criteria = example.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        if (periodType != 0) {
            criteria.andPeriodTypeEqualTo(periodType);
        }
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }

        PageLimitHolderFilter.setContext(page, pageSize, null);
        List<TeachingCourseTimePlan> list = teachingWeekService.findTeachingCourseTimePlanList(example);
        List<Map> mapList = new ArrayList<>();
        for (TeachingCourseTimePlan plan : list) {
            Map map = BeanUtil.beanToMap(plan);
            switch (plan.getPeriodType()) {
                case 1:
                    map.put("periodTypeText", "上午");
                    break;
                case 2:
                    map.put("periodTypeText", "中午");
                    break;
                case 3:
                    map.put("periodTypeText", "下午");
                    break;
                case 4:
                    map.put("periodTypeText", "晚上");
                    break;
            }
            mapList.add(map);
        }

        Map data = new HashMap();
        data.put("list", mapList);
        data.put("totalNum", PageLimitHolderFilter.getContext().getTotalCount());
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 新增上课时间计划
     *
     * @param schoolId     学校ID
     * @param periodType   时间段类型
     * @param courseCount  每个时间段的课的节次
     * @param startTime    上课开始时间
     * @param continueTime 每节课的时长
     * @param restTime     课间休息时长
     * @param response
     */
    @RequestMapping("back_api_addTeachingCourseTimePlan")
    public void back_api_addTeachingCourseTimePlan(Integer schoolId, Short periodType, Integer courseCount,
                                                   String startTime, Integer continueTime, Integer restTime, HttpServletResponse response) {
        if (schoolId == null || periodType == null || startTime == null || continueTime == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        /*时间段（1上午，2中午，3下午，4晚上）*/
        switch (periodType) {
            case 1:
                if (courseCount > 5) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "上午最多只能设置5节课")));
                    return;
                }
                if (startTime.compareTo("12:00") > 0) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "上课时间：" + startTime + "不属于上午时间段")));
                    return;
                }
                break;
            case 2:
                if (courseCount > 1) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "中午只能设置一节课")));
                    return;
                }
                if (startTime.compareTo("13:00") > 0 || startTime.compareTo("12:00") < 0) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "上课时间：" + startTime + "不属于中午时间段")));
                    return;
                }
                break;
            case 3:
                if (courseCount > 5) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "下午最多只能设置5节课")));
                    return;
                }
                if (startTime.compareTo("18:00") > 0 || startTime.compareTo("13:00") < 0) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "上课时间：" + startTime + "不属于下午时间段")));
                    return;
                }
                break;
            case 4:
                if (courseCount > 3) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "晚上最多只能设置3节课")));
                    return;
                }
                if (startTime.compareTo("18:00") < 0) {
                    ServletUtilsEx.renderText(response,
                            JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "上课时间：" + startTime + "不属于下午时间段")));
                    return;
                }
                break;
        }
        List<TeachingCourseTimePlan> teachingCourseTimePlanList = teachingWeekService.createTeachingCourseTimePlan(periodType, courseCount, startTime, continueTime, restTime, schoolId);
        Result result = teachingWeekService.batchOperate(teachingCourseTimePlanList);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 修改上课时间计划
     *
     * @param id
     * @param startTime    上课开始时间
     * @param continueTime 每节课的时长
     * @param courseIndex  节次
     * @param response
     */
    @RequestMapping("back_api_updateTeachingCourseTimePlan")
    public void back_api_updateTeachingCourseTimePlan(Integer id, String startTime, Integer continueTime, String courseIndex, HttpServletResponse response) {
        if (id == null || startTime == null || continueTime == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        TeachingCourseTimePlan teachingCourseTimePlan = new TeachingCourseTimePlan();
        teachingCourseTimePlan.setId(id);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date start = null, end = null;
        try {
            start = sdf.parse(startTime);
            end = DateUtils.addMinutes(start, continueTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        teachingCourseTimePlan.setStartTime(sdf.format(start));
        teachingCourseTimePlan.setEndTime(sdf.format(end));
        teachingCourseTimePlan.setCourseIndex(courseIndex);

        Result result = teachingWeekService.addOrUpdateTeachingCourseTimePlan(teachingCourseTimePlan);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 删除上课时间计划
     *
     * @param id
     * @param response
     */
    @RequestMapping("back_api_deleteTeachingCourseTimePlan")
    public void back_api_deleteTeachingCourseTimePlan(Integer id, Integer schoolId, HttpServletResponse response) {
        if (id == null || schoolId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        Result result = teachingWeekService.deleteTeachingCourseTimePlan(id, schoolId);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 清空上课时间计划
     *
     * @param schoolId 学校ID
     * @param response
     */
    @RequestMapping("back_api_clearTeachingCourseTimePlan")
    public void back_api_clearTeachingCourseTimePlan(Integer schoolId, Short periodType, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        teachingWeekService.deleteTeachingCourseTimePlanByExample(schoolId, periodType);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功")));
    }

    /**
     * 导入教务系统的excel格式的教师排课表
     *
     * @param schoolId
     * @param fileId
     * @param response
     */
    @RequestMapping("back_api_importTeacherCoursePlan")
    public void back_api_importTeacherCoursePlan(Integer schoolId, String fileId, HttpServletResponse response) {
        if (schoolId == null) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空")));
            return;
        }

        Result result = teacherCoursePlanService.importTeacherCoursePlan(schoolId, fileId);
        ServletUtilsEx.renderJson(response, result);
    }
}
