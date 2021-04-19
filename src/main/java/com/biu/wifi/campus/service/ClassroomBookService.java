package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.ParseException;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/7.
 */
@Service
public class ClassroomBookService {

    private static Logger logger = LoggerFactory.getLogger(ClassroomBookService.class);
    @Autowired
    private ClassroomBookMapper classroomBookMapper;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ClassroomBookItemService classroomBookItemService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private ClassroomBookUseTypeOrganizationMapper classroomBookUseTypeOrganizationMapper;
    @Autowired
    private ClassroomBookUseTypeMapper classroomBookUseTypeMapper;
    @Autowired
    private TeachingCourseTimePlanMapper teachingCourseTimePlanMapper;
    @Autowired
    private ClassroomBuildingMapper classroomBuildingMapper;
    @Autowired
    private ClassroomBookAuditService classroomBookAuditService;
    @Autowired
    private ClassroomBookUseTypeOrganizationAuditUserService classroomBookUseTypeOrganizationAuditUserService;
    @Autowired
    private UserMapper userMapper;

    public long countByExample(ClassroomBookCriteria example) {
        return classroomBookMapper.countByExample(example);
    }

    /**
     * 新增或编辑教室预约
     *
     * @param classroomBook 教室预约ID
     * @param bookDate      预约时间
     * @param startTimeList 预约时间段
     * @param schoolId      学校ID
     * @param updateFlag    调整教室标识
     * @param isJw          是否是教务处人员
     * @return
     */
    public Result addOrUpdate(ClassroomBook classroomBook, String bookDate, List<Date> startTimeList, Integer schoolId,
                              boolean updateFlag, boolean isJw) {
        // 教室预约更新入库操作
        classroomBook = doAddOrUpdateClassroomBook(classroomBook, bookDate, startTimeList, schoolId);

        if (isJw && !updateFlag) {
            // 教务处预约教室，直接通过审批;教务处调整教室则不更新教室状态
            Result result = batchOccupyClassroom(classroomBook.getId(), classroomBook.getClassroomNo());
            if (result.getKey() == Result.SUCCESS) {
                User user = userMapper.selectByPrimaryKey(classroomBook.getUserId());
                result = auditUpdateClassBookStatus(classroomBook.getId(), 2, classroomBook.getVersion(),
                        user.getId(), user.getName(), null);
                if (result.getKey() == Result.SUCCESS) {
                    return new Result(Result.SUCCESS, "成功");
                } else {
                    logger.error("教务处预约教室，批量更新教室状态失败");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
            } else {
                logger.error("教务处预约教室，批量占座失败");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
        } else {
            // 创建第一个审批
            if (!updateFlag) {
                ClassroomBookUseTypeOrganizationAuditUser auditUser = classroomBookUseTypeOrganizationAuditUserService.find(classroomBook.getUseTypeId(), classroomBook.getOrganizationId(), null);
                String[] users = auditUser.getAuditUser().split(",");
                ClassroomBookAudit classroomBookAudit = new ClassroomBookAudit();
                classroomBookAudit.setSchoolId(schoolId);
                classroomBookAudit.setUserId(Integer.valueOf(users[0]));
                classroomBookAudit.setBookId(classroomBook.getId());
                Result result = classroomBookAuditService.addAudit(classroomBookAudit, (short) 4);
                if (result.getKey() == Result.SUCCESS) {
                    return new Result(Result.SUCCESS, "成功");
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
            } else {
                return new Result(Result.SUCCESS, "成功");
            }
        }
    }

    /**
     * 教室预约更新入库操作
     *
     * @param classroomBook
     * @param bookDate
     * @param startTimeList
     * @param schoolId
     * @return
     */
    public ClassroomBook doAddOrUpdateClassroomBook(ClassroomBook classroomBook, String bookDate, List<Date> startTimeList, Integer schoolId) {
        if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
            //有调整
            //教务处有调整,删除之前的明细
            ClassroomBookItemCriteria classroomBookItemEx = new ClassroomBookItemCriteria();
            classroomBookItemEx.createCriteria()
                    .andBookIdEqualTo(classroomBook.getId())
                    .andIsDeleteEqualTo((short) 2);
            classroomBookItemService.deleteByExample(classroomBookItemEx);
        }

        if (classroomBook.getId() == null) {
            classroomBook.setCreateTime(new Date());
            classroomBook.setVersion(String.valueOf(new Date().getTime()));
            classroomBookMapper.insertSelective(classroomBook);
        } else {
            classroomBook.setUpdateTime(new Date());
            classroomBookMapper.updateByPrimaryKeySelective(classroomBook);
        }

        String[] classroomNoList = classroomBook.getClassroomNo().split(",");
        if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
            classroomNoList = classroomBook.getAdjustClassroomNo().split(",");
        }

        int m = 0;
        String bookSection = "第";
        for (Date start : startTimeList) {
            TeachingCourseTimePlanCriteria teachingCourseTimePlanEx = new TeachingCourseTimePlanCriteria();
            teachingCourseTimePlanEx.setOrderByClause("create_time desc");
            teachingCourseTimePlanEx.createCriteria()
                    .andSchoolIdEqualTo(schoolId)
                    .andStartTimeEqualTo(TimeUtil.format(start, "HH:mm"));

            Date end;
            try {
                List<TeachingCourseTimePlan> teachingCourseTimePlans = teachingWeekService.findTeachingCourseTimePlanList(teachingCourseTimePlanEx);
                if (teachingCourseTimePlans.size() == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    //return new Result(Result.CUSTOM_MESSAGE, "没有设置上课时间，请联系教务");
                    throw new BizException(Result.CUSTOM_MESSAGE, "没有设置上课时间，请联系教务");
                }
                end = DateUtils.parseDate(bookDate + " " + teachingCourseTimePlans.get(0).getEndTime(), new String[]{"yyyy-MM-dd HH:mm"});

                bookSection += teachingCourseTimePlans.get(0).getCourseIndex();
                if (m != startTimeList.size() - 1) {
                    bookSection += "、";
                }
                m++;
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("日期解析异常：{}", e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                //return new Result(Result.FAILURE, "失败");
                throw new BizException(Result.FAILURE, "失败");
            }

            for (String classroomNo : classroomNoList) {
                ClassroomBookItem classroomBookItem = new ClassroomBookItem();
                classroomBookItem.setBookId(classroomBook.getId());
                ClassroomCriteria classroomEx = new ClassroomCriteria();
                classroomEx.setOrderByClause("create_time desc");
                classroomEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andBuildingIdEqualTo(classroomBook.getClassroomBuildingId())
                        .andNoEqualTo(classroomNo);

                List<Classroom> classroomList = classroomService.findList(classroomEx);
                if (classroomList.size() == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    //return new Result(Result.CUSTOM_MESSAGE, "编号为【" + classroomNo + "】的教室不存在");
                    throw new BizException(Result.CUSTOM_MESSAGE, "编号为【" + classroomNo + "】的教室不存在");
                }
                classroomBookItem.setClassroomId(classroomList.get(0).getId());
                classroomBookItem.setClassroomNo(classroomNo);
                classroomBookItem.setStartTime(TimeUtil.format(start, "yyyy-MM-dd HH:mm"));
                classroomBookItem.setEndTime(TimeUtil.format(end, "yyyy-MM-dd HH:mm"));

                //检查教室是否被占用
                if (classroomBookItemService.findBookedCount(classroomBook.getId(), classroomBook.getClassroomBuildingId(),
                        classroomNo, TimeUtil.format(start, "yyyy-MM-dd HH:mm"))) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    //return new Result(Result.CUSTOM_MESSAGE, "编号为【" + classroomNo + "】的教室已经被占用，请调整");
                    throw new BizException(Result.CUSTOM_MESSAGE, "编号为【" + classroomNo + "】的教室已经被占用，请调整");
                }
                classroomBookItemService.add(classroomBookItem);
            }
        }
        bookSection += "节";
        classroomBook.setBookSection(bookSection);
        classroomBook.setUpdateTime(new Date());
        int count = classroomBookMapper.updateByPrimaryKeySelective(classroomBook);
        if (count <= 0) {
            logger.error("更新教室预定的使用时间节次出错");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //return new Result(Result.FAILURE, "失败");
            throw new BizException(Result.FAILURE, "失败");
        }
        return classroomBook;
    }

    /**
     * 审批时更改教室预约的状态
     *
     * @param bookId        预约ID
     * @param status        状态
     * @param version       版本号
     * @param auditUserId   审核人ID
     * @param auditUserName 审核人姓名
     * @param auditRemark   审批意见
     * @return
     */
    public Result auditUpdateClassBookStatus(Integer bookId, Integer status, String version,
                                             Integer auditUserId, String auditUserName, String auditRemark) {
        ClassroomBookCriteria example = new ClassroomBookCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdEqualTo(bookId)
                .andVersionEqualTo(version);
        List<ClassroomBook> classroomBookList = classroomBookMapper.selectByExample(example);
        if (classroomBookList.isEmpty()) {
            return new Result(Result.CUSTOM_MESSAGE, "当前预约记录已被修改");
        }

        ClassroomBook classroomBook = new ClassroomBook();
        classroomBook.setStatus(status);
        Date date = new Date();
        classroomBook.setAuditUserId(auditUserId);
        classroomBook.setAuditUserName(auditUserName);
        classroomBook.setAuditRemark(auditRemark);
        classroomBook.setAuditTime(date);
        classroomBook.setUpdateTime(date);
        int count = classroomBookMapper.updateByExampleSelective(classroomBook, example);
        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    /**
     * 审批通过批量占用教室
     *
     * @param classroomBookId 预约ID
     * @param classroomNo     教室号
     * @return
     */
    public Result batchOccupyClassroom(Integer classroomBookId, String classroomNo) {
        //审批通过，更改教室占用状态
        classroomNo = StringUtils.deleteWhitespace(classroomNo);
        //根据教室号查询教室
        ClassroomCriteria classroomEx = new ClassroomCriteria();
        classroomEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andStatusEqualTo((short) 1)
                .andNoIn(Arrays.asList(classroomNo.split(",")));
        List<Classroom> classroomList = classroomService.findList(classroomEx);
        ClassroomBook classroomBook = classroomBookMapper.selectByPrimaryKey(classroomBookId);
        ClassroomBuilding classroomBuilding = classroomBuildingMapper.selectByPrimaryKey(classroomBook.getClassroomBuildingId());
        // 判断上午第一二节课是否被预约
        String checkClassroomNo = classroomService.isCanBook(classroomBuilding.getSchoolId(), (short) 1, classroomList, classroomBook.getBookDate()
                , Arrays.asList(classroomBook.getBookPeriod().split(",")));
        if (checkClassroomNo != null) {
            return new Result(Result.CUSTOM_MESSAGE, "教室" + checkClassroomNo + "第一二节课已经被预约，不能被预约");
        }
        // 判断下午第一二节课是否被预约
        checkClassroomNo = classroomService.isCanBook(classroomBuilding.getSchoolId(), (short) 3, classroomList, classroomBook.getBookDate()
                , Arrays.asList(classroomBook.getBookPeriod().split(",")));
        if (checkClassroomNo != null) {
            return new Result(Result.CUSTOM_MESSAGE, "教室" + checkClassroomNo + "第一二节课已经被预约，不能被预约");
        }
        for (Classroom classroom : classroomList) {
            Result result = classroomService.occupy(classroom.getId(), classroom.getBuildingId(), classroomBookId);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
        }
        return new Result(Result.SUCCESS, "成功");
    }

    public List<ClassroomBook> findList(ClassroomBookCriteria example) {
        return classroomBookMapper.selectByExample(example);
    }

    /**
     * xx用户的教室预约记录
     *
     * @param userId
     * @param schoolId
     * @return
     */
    public List<Map> findList(Integer userId, Integer schoolId) {
        List<Map> mapList = new ArrayList<>();
        ClassroomBookCriteria example = new ClassroomBookCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId);
        List<ClassroomBook> classroomBookList = classroomBookMapper.selectByExample(example);
        for (ClassroomBook classroomBook : classroomBookList) {
            Map map = new HashMap();
            map.put("id", classroomBook.getId());
            ClassroomBookUseType classroomBookUseType = classroomBookUseTypeMapper.selectByPrimaryKey(classroomBook.getUseTypeId());
            map.put("useType", "【教室预约】【" + classroomBookUseType.getName() + "】");
            map.put("title", classroomBook.getTitle());
            String classroomNo = classroomBook.getClassroomNo();
            if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
                classroomNo += "\n调整后的：" + classroomBook.getAdjustClassroomNo();
            }
            map.put("classroomNo", classroomNo);
            map.put("useTime", getUseTimeText(classroomBook, schoolId));
            map.put("status", classroomBook.getStatus());
            map.put("statusText", getStatusText(classroomBook.getStatus().intValue()));
            map.put("createTime", classroomBook.getCreateTime());
            mapList.add(map);
        }
        return mapList;
    }

    public ClassroomBook findById(Integer id) {
        return classroomBookMapper.selectByPrimaryKey(id);
    }

    /**
     * 教室预约详情
     *
     * @param classroomBookId
     * @return
     */
    public Map find(Integer schoolId, Integer classroomBookId) {
        ClassroomBookCriteria example = new ClassroomBookCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdEqualTo(classroomBookId);
        List<ClassroomBook> classroomBookList = classroomBookMapper.selectByExample(example);
        if (classroomBookList.isEmpty()) {
            return null;
        }

        ClassroomBook classroomBook = classroomBookList.get(0);
        Map map = BeanUtil.beanToMap(classroomBook);
        map.put("bookNo", getBookNo("JSYD", classroomBook.getId().toString()));
        ClassroomBookUseTypeOrganization classroomBookUseTypeOrganization = classroomBookUseTypeOrganizationMapper.selectByPrimaryKey(classroomBook.getOrganizationId());
        map.put("organizationName", classroomBookUseTypeOrganization.getName());
        ClassroomBookUseType classroomBookUseType = classroomBookUseTypeMapper.selectByPrimaryKey(classroomBook.getUseTypeId());
        map.put("useTypeName", classroomBookUseType.getName());
        ClassroomBuilding classroomBuilding = classroomBuildingMapper.selectByPrimaryKey(classroomBook.getClassroomBuildingId());
        map.put("classroomBuildingName", classroomBuilding.getName());
        map.put("useTime", getUseTimeText(classroomBook, schoolId));
        map.put("statusText", getStatusText(classroomBook.getStatus().intValue()));
        if ("3".equals(classroomBook.getStatus().toString())) {
            //驳回
            map.put("remark", map.get("auditRemark").toString());
        } else {
            map.put("remark", "");
        }
        return map;
    }

    /**
     * 查询教室使用时间
     *
     * @param classroomBook
     * @param schoolId
     * @return
     */
    public String getUseTimeText(ClassroomBook classroomBook, Integer schoolId) {
        StringBuffer useTime = new StringBuffer(classroomBook.getBookDate());
        String[] startTime = classroomBook.getBookPeriod().split(",");
        TeachingCourseTimePlanCriteria teachingCourseTimePlanEx = new TeachingCourseTimePlanCriteria();
        teachingCourseTimePlanEx.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andStartTimeIn(Arrays.asList(startTime));
        List<TeachingCourseTimePlan> plans = teachingCourseTimePlanMapper.selectByExample(teachingCourseTimePlanEx);
        useTime.append(" 第");
        for (int i = 0; i < plans.size(); i++) {
            TeachingCourseTimePlan plan = plans.get(i);
            useTime.append(plan.getCourseIndex());
            if (i != plans.size() - 1) {
                useTime.append("，");
            }
        }
        useTime.append("节");
        return useTime.toString();
    }

    /**
     * 根据状态查询对应中文
     *
     * @param status
     * @return
     */
    public String getStatusText(int status) {
        String statusText = "";
        switch (status) {
            case 1:
                statusText = "审核中";
                break;
            case 2:
                statusText = "已通过";
                break;
            case 3:
                statusText = "已退回";
                break;
        }
        return statusText;
    }

    /**
     * 生成8位的预定编号
     *
     * @param prefix
     * @param classroomBookId
     * @return
     */
    public String getBookNo(String prefix, String classroomBookId) {
        int len = 8 - classroomBookId.length();
        StringBuffer sb = new StringBuffer(prefix);
        for (int i = 0; i < len; i++) {
            sb.append("0");
        }
        sb.append(classroomBookId);
        return sb.toString();
    }

    /**
     * 根据预定日期和预定时间段获取预定时间的集合,格式为:yyyy-MM-dd HH:mm
     * @param bookDateStr
     * @param bookPeriod
     * @return
     */
    public List<String> getStartTimeListByBookPeriod(String bookDateStr, String bookPeriod) {
        //筛选日期时间
        List<String> startTimeList = new ArrayList<>();
        Date now = new Date();
        for (String time : bookPeriod.split(",")) {
            try {
                String str = bookDateStr + " " + time;
                Date startTime = DateUtils.parseDate(str, new String[]{"yyyy-MM-dd HH:mm"});
                if (startTime.compareTo(now) <= 0) {
                    throw new BizException(Result.CUSTOM_MESSAGE, "预定时间必须大于当前时间");
                }
                startTimeList.add(str);
            } catch (ParseException e) {
                logger.error("日期格式解析错误：{}", e.getMessage());
                throw new BizException(Result.FAILURE, "服务器异常");
            }
        }
        return startTimeList;
    }
}
