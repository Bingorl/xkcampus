package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.TeachingCourseTimePlanMapper;
import com.biu.wifi.campus.dao.TeachingWeekMapper;
import com.biu.wifi.campus.dao.model.TeachingCourseTimePlan;
import com.biu.wifi.campus.dao.model.TeachingCourseTimePlanCriteria;
import com.biu.wifi.campus.dao.model.TeachingWeek;
import com.biu.wifi.campus.dao.model.TeachingWeekCriteria;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/11/8.
 */
@Service
public class TeachingWeekService {

    @Autowired
    private TeachingWeekMapper teachingWeekMapper;
    @Autowired
    private TeachingCourseTimePlanMapper teachingCourseTimePlanMapper;

    public Result delete(Integer id, Integer schoolId) {
        TeachingWeek teachingWeek = teachingWeekMapper.selectByPrimaryKey(id);
        if (teachingWeek == null) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
        }

        if (teachingWeek.getSchoolId().intValue() != schoolId.intValue()) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限");
        }

        teachingWeekMapper.deleteByPrimaryKey(id);
        return new Result(Result.SUCCESS, "成功");
    }

    public List<TeachingWeek> findList(Integer schoolId, String termName) {
        TeachingWeekCriteria example = new TeachingWeekCriteria();
        example.setOrderByClause("create_time desc");
        TeachingWeekCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        if (StringUtils.isNotBlank(termName)) {
            criteria.andTermNameLike("%" + termName + "%");
        }
        return findList(example);
    }

    public List<TeachingWeek> findList(TeachingWeekCriteria example) {
        return teachingWeekMapper.selectByExample(example);
    }

    public List<Map> findMapList(Integer schoolId, String termName) {
        List<TeachingWeek> list = findList(schoolId, termName);
        List<Map> maps = new ArrayList<>();
        for (TeachingWeek week : list) {
            BeanMap beanMap = BeanMap.create(week);
            Map tmp = new HashMap();
            tmp.putAll(beanMap);
            String termCount = week.getTermCode().substring(10, 11);
            tmp.put("termCount", termCount);
            maps.add(tmp);
        }
        return maps;
    }

    public void addOrUpdate(Integer id, Integer schoolId, String termCode, String termName, String termPeriod) {
        //重复校验
        TeachingWeekCriteria example = new TeachingWeekCriteria();
        TeachingWeekCriteria.Criteria criteria = example.createCriteria();
        criteria.andSchoolIdEqualTo(schoolId);
        if (id != null) {
            criteria.andIdNotEqualTo(id);
        }
        if (StringUtils.isNotBlank(termCode)) {
            criteria.andTermCodeEqualTo(termCode);
        }
        /*if (StringUtils.isNotBlank(termName)) {
            criteria.andTermNameEqualTo(termName);
        }
        if (StringUtils.isNotBlank(termPeriod)) {
            criteria.andTermPeriodEqualTo(termPeriod);
        }*/
        long count = teachingWeekMapper.countByExample(example);
        if (count > 0) {
            throw new BizException(Result.CUSTOM_MESSAGE, "该学期已存在");
        }

        //校验学期周期的合法性
        String[] termPeriods = termPeriod.split("~");
        Date date;
        try {
            //开始日期是否是周一
            date = DateUtils.parseDate(termPeriods[0], new String[]{"yyyy-MM-dd"});
            int day = TimeUtil.getDay(date);
            if (day != 1) {
                throw new BizException(Result.CUSTOM_MESSAGE, "课程教学周开始时间未选择周一");
            }
            //结束日期是否是周日
            date = DateUtils.parseDate(termPeriods[1], new String[]{"yyyy-MM-dd"});
            day = TimeUtil.getDay(date);
            if (day != 7) {
                throw new BizException(Result.CUSTOM_MESSAGE, "课程教学周结束时间未选择周日");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //计算周数
        int termTotalDays = TimeUtil.getDayDiff(termPeriods[0], termPeriods[1]) + 1;
        int weekCount = termTotalDays / 7;

        if (weekCount > 30) {
            throw new BizException(Result.CUSTOM_MESSAGE, "课程周期输入有误，超过最大值30周，请检查");
        }


        //获取该学期内的每周周一的日期拼成字符串(2018-09-03,2018-09-10)
        String mondayDate = termPeriods[0];
        StringBuffer sb = new StringBuffer(mondayDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(mondayDate);
            Date temp;
            for (int i = 1; i < termTotalDays; i++) {
                temp = DateUtils.addDays(d, i);
                if (TimeUtil.getDay(temp) == 1) {
                    mondayDate = TimeUtil.format(temp, "yyyy-MM-dd");
                    sb.append(",").append(mondayDate);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BizException(Result.CUSTOM_MESSAGE, "日期解析错误");
        }

        TeachingWeek teachingWeek = new TeachingWeek();
        teachingWeek.setId(id);
        teachingWeek.setSchoolId(schoolId);
        teachingWeek.setTermCode(termCode);
        teachingWeek.setTermName(termName);
        teachingWeek.setTermPeriod(termPeriod);
        teachingWeek.setWeekCount(weekCount);
        teachingWeek.setMondayDate(sb.toString());

        if (id == null) {
            teachingWeek.setCreateTime(new Date());
            teachingWeekMapper.insertSelective(teachingWeek);
        } else {
            teachingWeek.setUpdateTime(new Date());
            teachingWeekMapper.updateByPrimaryKeySelective(teachingWeek);
        }
    }

    /**
     * 返回指定学校的当前学年学期
     *
     * @param schoolId
     * @return
     */
    public TeachingWeek getTeachingWeekBySchoolId(Integer schoolId) {
        TeachingWeekCriteria example = new TeachingWeekCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        List<TeachingWeek> teachingWeekList = teachingWeekMapper.selectByExample(example);
        if (teachingWeekList.size() == 0) {
            return null;
        } else {
            return teachingWeekList.get(0);
        }
    }

    public TeachingWeek getTeachingWeekByExamTime(int schoolId,String examTime){
        TeachingWeekCriteria teachingWeekEx = new TeachingWeekCriteria();
        teachingWeekEx.createCriteria()
                .andSchoolIdEqualTo(schoolId);
        List<TeachingWeek> teachingWeekList = findList(teachingWeekEx);
        TeachingWeek teachingWeek = null;
        for (TeachingWeek t : teachingWeekList) {
            String[] termPeriodList = t.getTermPeriod().split("~");
            if (examTime.compareTo(termPeriodList[0]) >= 0 && examTime.compareTo(termPeriodList[1]) <= 0) {
                teachingWeek = t;
                break;
            }
        }
        return teachingWeek;
    }

    /**
     * @param schoolId     学校ID
     * @param courseCount  每个时间段的课的节次
     * @param startTime    上课开始时间
     * @param continueTime 每节课的时长
     * @param restTime     课间休息时长
     * @return
     */
    public List<TeachingCourseTimePlan> createTeachingCourseTimePlan(Short periodType, Integer courseCount, String startTime, Integer continueTime, Integer restTime, Integer schoolId) {
        List<TeachingCourseTimePlan> teachingCourseTimePlanList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date start, end;
        for (int i = 0; i < courseCount; i++) {
            try {
                start = sdf.parse(startTime);
                end = DateUtils.addMinutes(start, continueTime);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("添加上课时间表时，时间计算错误");
            }
            TeachingCourseTimePlan teachingCourseTimePlan = new TeachingCourseTimePlan();
            teachingCourseTimePlan.setPeriodType(periodType);
            teachingCourseTimePlan.setSchoolId(schoolId);
            String index = getCourseIndex(schoolId, periodType, i + 1);
            teachingCourseTimePlan.setCourseIndex(index);
            teachingCourseTimePlan.setName("第" + index + "节    " + sdf.format(start) + "-" + sdf.format(end));
            teachingCourseTimePlan.setStartTime(sdf.format(start));
            teachingCourseTimePlan.setEndTime(sdf.format(end));
            teachingCourseTimePlanList.add(teachingCourseTimePlan);

            //计算下节课的开始时间
            startTime = sdf.format(DateUtils.addMinutes(end, restTime));
        }

        return teachingCourseTimePlanList;
    }

    /**
     * 获取上课的节次索引
     *
     * @param schoolId
     * @param periodType 时间段类型
     * @param index      在各个时间段的索引
     * @return
     */
    public String getCourseIndex(Integer schoolId, Short periodType, Integer index) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.setOrderByClause("course_index desc");
        example.setOffset(0L);
        example.setLimit(1);
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andPeriodTypeEqualTo(periodType);
        List<TeachingCourseTimePlan> plans = teachingCourseTimePlanMapper.selectByExample(example);
        if (plans.isEmpty()) {
            return String.valueOf(index);
        } else {
            index += Integer.valueOf(plans.get(0).getCourseIndex());
            return index.toString();
        }
    }

    /**
     * 根据学校ID和上课开始时间查询对应的节次
     *
     * @param schoolId
     * @param startTimeList
     * @return
     */
    public String getCourseIndex(Integer schoolId, List<String> startTimeList) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andStartTimeIn(startTimeList);
        List<TeachingCourseTimePlan> plans = teachingCourseTimePlanMapper.selectByExample(example);
        String str = "第";
        if (plans.isEmpty()) {
            return null;
        } else {
            int m = 0;
            for (TeachingCourseTimePlan plan : plans) {
                str += plan.getCourseIndex();
                if (m != plans.size() - 1) {
                    str += ",";
                }
                m++;
            }
            str += "节";
            return str;
        }
    }

    /**
     * 根据学校ID和上课开始时间查询对应的节次
     *
     * @param schoolId
     * @param startTimeList
     * @return
     */
    public List<String> getCourseIndex(int schoolId, List<String> startTimeList) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andStartTimeIn(startTimeList);
        List<TeachingCourseTimePlan> plans = teachingCourseTimePlanMapper.selectByExample(example);

        List<String> list = new ArrayList<>();
        for (TeachingCourseTimePlan plan : plans)
            list.add(plan.getCourseIndex());
        return list;
    }


    /**
     * 新增或编辑上课时间计划
     *
     * @param teachingCourseTimePlan
     * @return
     */
    public Result addOrUpdateTeachingCourseTimePlan(TeachingCourseTimePlan teachingCourseTimePlan) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        TeachingCourseTimePlanCriteria.Criteria criteria = example.createCriteria()
                .andSchoolIdEqualTo(teachingCourseTimePlan.getSchoolId())
                .andCourseIndexEqualTo(teachingCourseTimePlan.getCourseIndex())
                .andStartTimeEqualTo(teachingCourseTimePlan.getStartTime())
                .andEndTimeEqualTo(teachingCourseTimePlan.getEndTime());

        if (teachingCourseTimePlan.getId() != null) {
            criteria.andIdEqualTo(teachingCourseTimePlan.getId());
        }

        List<TeachingCourseTimePlan> teachingCourseTimePlans = teachingCourseTimePlanMapper.selectByExample(example);
        if (teachingCourseTimePlans.size() == 0) {
            teachingCourseTimePlan.setCreateTime(new Date());
            teachingCourseTimePlanMapper.insertSelective(teachingCourseTimePlan);
        } else {
            teachingCourseTimePlan = teachingCourseTimePlans.get(0);
            teachingCourseTimePlan.setUpdateTime(new Date());
            teachingCourseTimePlanMapper.updateByPrimaryKeySelective(teachingCourseTimePlan);
        }

        return new Result(Result.SUCCESS, "成功");

    }

    /**
     * 批量新增或更新上课时间计划
     *
     * @param teachingCourseTimePlanList
     * @return
     */
    public Result batchOperate(List<TeachingCourseTimePlan> teachingCourseTimePlanList) {
        for (TeachingCourseTimePlan teachingCourseTimePlan : teachingCourseTimePlanList) {
            // 校验添加的上课时间是否与已添加的有重复
            TeachingCourseTimePlanCriteria example1 = new TeachingCourseTimePlanCriteria();
            example1.createCriteria()
                    .andStartTimeGreaterThanOrEqualTo(teachingCourseTimePlan.getStartTime())
                    .andStartTimeLessThanOrEqualTo(teachingCourseTimePlan.getEndTime());

            TeachingCourseTimePlanCriteria example2 = new TeachingCourseTimePlanCriteria();
            TeachingCourseTimePlanCriteria.Criteria criteria = example2.createCriteria()
                    .andEndTimeGreaterThanOrEqualTo(teachingCourseTimePlan.getStartTime())
                    .andEndTimeLessThanOrEqualTo(teachingCourseTimePlan.getEndTime());

            TeachingCourseTimePlanCriteria example3 = new TeachingCourseTimePlanCriteria();
            TeachingCourseTimePlanCriteria.Criteria criteria2 = example3.createCriteria()
                    .andStartTimeLessThanOrEqualTo(teachingCourseTimePlan.getStartTime())
                    .andEndTimeGreaterThanOrEqualTo(teachingCourseTimePlan.getEndTime());
            example1.or(criteria);
            example1.or(criteria2);
            long count = teachingCourseTimePlanMapper.countByExample(example1);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "上课时间与已添加的有冲突，请修改上课开始时间");
            }

            Result result = addOrUpdateTeachingCourseTimePlan(teachingCourseTimePlan);
            if (result.getKey() != Result.SUCCESS) {
                return result;
            }
        }
        return new Result(Result.SUCCESS, "成功");
    }

    /**
     * 清空指定时间段内的上课时间计划
     *
     * @param schoolId
     * @param periodType
     */
    public void deleteTeachingCourseTimePlanByExample(int schoolId, short periodType) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andPeriodTypeEqualTo(periodType);
        teachingCourseTimePlanMapper.deleteByExample(example);
    }

    public Result deleteTeachingCourseTimePlan(Integer id, Integer schoolId) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIdEqualTo(id);
        List<TeachingCourseTimePlan> plans = findTeachingCourseTimePlanList(example);
        if (plans.isEmpty()) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
        }

        TeachingCourseTimePlan teachingCourseTimePlan = plans.get(0);
        teachingCourseTimePlanMapper.deleteByPrimaryKey(teachingCourseTimePlan.getId());
        return new Result(Result.SUCCESS, "成功");
    }

    public List<TeachingCourseTimePlan> findTeachingCourseTimePlanList(TeachingCourseTimePlanCriteria example) {
        return teachingCourseTimePlanMapper.selectByExample(example);
    }

    public List<Map> findTeachingCourseTimePlanMapList(TeachingCourseTimePlanCriteria example, String... retainParams) {
        List<TeachingCourseTimePlan> list = teachingCourseTimePlanMapper.selectByExample(example);
        List<Map> mapList = new ArrayList<>();
        for (TeachingCourseTimePlan plan : list) {
            Map map = BeanUtil.retainKeys(BeanUtil.beanToMap(plan), retainParams);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 根据当前日期获取所在学期
     *
     * @param date
     * @return
     */
    public String getTermCode(Date date) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(now);


        // 根据当前日期判断属于哪一个学期  一学期：9.1-1.20,二学期：3.1-7.20
        String[] parsePatterns = {"yyyy-MM-dd"};
        try {
            Date _9_1 = DateUtils.parseDate(new StringBuffer(year).append("-09-01").toString(), parsePatterns);
            Date _1_20 = DateUtils.parseDate(new StringBuffer(String.valueOf(Integer.valueOf(year) + 1)).append("-01-20").toString(), parsePatterns);
            Date _3_1 = DateUtils.parseDate(new StringBuffer(year).append("-03-01").toString(), parsePatterns);
            Date _7_20 = DateUtils.parseDate(new StringBuffer(year).append("-07-20").toString(), parsePatterns);

            if (now.compareTo(_9_1) >= 0 && now.compareTo(_1_20) <= 0)
                return new StringBuffer(String.valueOf(Integer.valueOf(year) - 1))
                        .append("-")
                        .append(year)
                        .append("-")
                        .append("1-1")
                        .toString();
            else if (now.compareTo(_3_1) >= 0 && now.compareTo(_7_20) <= 0)
                return new StringBuffer(String.valueOf(Integer.valueOf(year) - 1))
                        .append("-")
                        .append(year)
                        .append("-")
                        .append("2-1")
                        .toString();
            else
                throw new BizException(Result.CUSTOM_MESSAGE, "当前处于假期,无法确定当前所在学期");
        } catch (ParseException e) {
            throw new BizException(Result.CUSTOM_MESSAGE, "无法确定当前所在学期");
        }
    }

    /**
     * 根据上课节次获取上课时间的字符串
     *
     * @param schoolId
     * @param courseIndexList
     * @return
     */
    public String getPeriodByCourseIndex(int schoolId, List<String> courseIndexList) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andCourseIndexIn(courseIndexList);
        List<TeachingCourseTimePlan> list = findTeachingCourseTimePlanList(example);
        if (list.isEmpty())
            return null;

        String period = "";
        for (int i = 0; i < list.size(); i++) {
            period += list.get(i).getStartTime();
            if (i != list.size() - 1)
                period += ",";
        }

        return period;
    }

    /**
     * 根据上课节次获取上课时间列表
     *
     * @param schoolId
     * @param courseIndexList
     * @return
     */
    public List<String> getPeriodByCourseIndex(String schoolId, List<String> courseIndexList) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(Integer.valueOf(schoolId))
                .andCourseIndexIn(courseIndexList);
        List<TeachingCourseTimePlan> list = findTeachingCourseTimePlanList(example);
        if (list.isEmpty())
            return null;

        List<String> periodList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            periodList.add(list.get(i).getStartTime());

        return periodList;
    }

    /**
     * 根据学校id和上课开始时间，获取上课结束时间
     *
     * @param schoolId
     * @param startTime
     * @return
     */
    public String getEndTimeBySchooldAndStartTime(int schoolId, String startTime) {
        TeachingCourseTimePlanCriteria example = new TeachingCourseTimePlanCriteria();
        example.createCriteria()
                .andSchoolIdEqualTo(Integer.valueOf(schoolId))
                .andStartTimeEqualTo(startTime);
        List<TeachingCourseTimePlan> list = findTeachingCourseTimePlanList(example);
        if (list.isEmpty())
            return null;
        else
            return list.get(0).getEndTime();
    }
}
