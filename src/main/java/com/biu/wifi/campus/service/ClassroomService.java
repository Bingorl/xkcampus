package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.BeanUtil;
import com.biu.wifi.campus.Tool.ExcelUtils;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.ClassroomBookItemMapper;
import com.biu.wifi.campus.dao.ClassroomBookMapper;
import com.biu.wifi.campus.dao.ClassroomMapper;
import com.biu.wifi.campus.dao.ClassroomOccupyMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.daoEx.ClassroomBookItemMapperEx;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/5.
 */
@Service
public class ClassroomService {

    private static Logger logger = LoggerFactory.getLogger(ClassroomService.class);
    @Autowired
    private ClassroomMapper classroomMapper;
    @Autowired
    private ClassroomOccupyMapper classroomOccupyMapper;
    @Autowired
    private ClassroomBookMapper classroomBookMapper;
    @Autowired
    private ClassroomBookItemMapper classroomBookItemMapper;
    @Autowired
    private ClassroomBookItemMapperEx classroomBookItemMapperEx;
    @Autowired
    private TeacherCoursePlanService teacherCoursePlanService;
    @Autowired
    private FileSupportService fileSupportService;
    @Autowired
    private ClassroomBuildingService classroomBuildingService;
    @Autowired
    private ClassroomTypeService classroomTypeService;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private ExamArrangeService examArrangeService;

    /**
     * 将教务的教室数据导入到数据库
     *
     * @param schoolId
     * @return
     */
    public Result importClassroomData(Integer schoolId, String fileId) {
        FileIoEntity fileIoEntity = fileSupportService.get(fileId);

        if (fileIoEntity == null) {
            return new Result(Result.CUSTOM_MESSAGE, "请上传excel格式的数据文件");
        }

        try {
            ExcelUtils excel = ExcelUtils.getInstance(fileIoEntity.getContent(), fileId);

            int rowCount = excel.getSheetRow(0);

            for (int i = 1; i < rowCount; i++) {
                Date date = new Date();
                String buildingNo = excel.read(0, 0, i);
                String classroomNo = excel.read(0, 1, i);
                String classroomName = excel.read(0, 2, i);
                String typeName = excel.read(0, 3, i);
                String typeCode = excel.read(0, 4, i);
                String isMedia = excel.read(0, 5, i);
                String seatCount = excel.read(0, 6, i);
                String exSeatCount = excel.read(0, 7, i);
                String remark = excel.read(0, 8, i);
                String status = excel.read(0, 9, i);
                Long version = date.getTime();


                {
                    //格式化数据
                    if (buildingNo != null && buildingNo.contains("."))
                        buildingNo = buildingNo.substring(0, buildingNo.indexOf("."));
                    if (typeCode != null && typeCode.contains(".")) {
                        typeCode = typeCode.substring(0, typeCode.indexOf("."));
                    } else if (typeCode.equals("")) {
                        continue;
                    }
                    if (Arrays.asList("是", "否").contains(isMedia)) {
                        if ("是".equals(isMedia)) {
                            isMedia = "1";
                        } else {
                            isMedia = "2";
                        }
                    } else {
                        return new Result(Result.CUSTOM_MESSAGE, "第" + (1 + i) + "行数据，是否带有多媒体设备，只能填是或否");
                    }
                    if (StringUtils.isBlank(seatCount)) {
                        seatCount = "0";
                    } else if (seatCount.contains(".")) {
                        seatCount = seatCount.substring(0, seatCount.indexOf("."));
                    }
                    if (StringUtils.isBlank(exSeatCount)) {
                        exSeatCount = "0";
                    } else if (exSeatCount.contains(".")) {
                        exSeatCount = exSeatCount.substring(0, exSeatCount.indexOf("."));
                    }
                    if (status != null && status.contains("."))
                        status = status.substring(0, status.indexOf("."));
                }

                //根据buildingNo和schoolId查询教学楼ID
                ClassroomBuildingCriteria buildingEx = new ClassroomBuildingCriteria();
                buildingEx.setOrderByClause("create_time desc");
                buildingEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andSchoolIdEqualTo(schoolId)
                        .andNoEqualTo(buildingNo);
                List<ClassroomBuilding> classroomBuildings = classroomBuildingService.findList(buildingEx);
                if (classroomBuildings.size() == 0) {
                    return new Result(Result.CUSTOM_MESSAGE, "暂时没有找到编号为【" + buildingNo + "】的教学楼，请修改");
                }
                ClassroomBuilding classroomBuilding = classroomBuildings.get(0);

                //根据类型代码查询类型ID
                ClassroomTypeCriteria classroomTypeEx = new ClassroomTypeCriteria();
                classroomTypeEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andCodeEqualTo(typeCode)
                        .andIsMediaEqualTo(isMedia);
                List<ClassroomType> classroomTypeList = classroomTypeService.findList(classroomTypeEx);
                if (classroomTypeList.isEmpty()) {
                    return new Result(Result.CUSTOM_MESSAGE, "暂时没有找到编号为【" + typeCode + "】的教室类型，请修改");
                }

                Classroom classroom = new Classroom();
                classroom.setName(classroomName);
                classroom.setStatus(status == null ? null : Short.valueOf(status));
                classroom.setVersion(version.toString());
                classroom.setBuildingId(classroomBuildings.get(0).getId());
                classroom.setNo(classroomNo);
                classroom.setSeatCount(Integer.valueOf(seatCount));
                classroom.setExSeatCount(Integer.valueOf(exSeatCount));
                classroom.setRemark(remark);
                classroom.setTypeId(classroomTypeList.get(0).getId());

                //删除旧教室
                ClassroomCriteria classroomEx = new ClassroomCriteria();
                classroomEx.createCriteria()
                        .andIsDeleteEqualTo((short) 2)
                        .andNoEqualTo(classroomNo)
                        .andBuildingIdEqualTo(classroomBuilding.getId());
                Classroom toDelete = new Classroom();
                toDelete.setIsDelete((short) 1);
                toDelete.setDeleteTime(date);
                classroomMapper.updateByExampleSelective(toDelete, classroomEx);
                //添加新教室
                addOrUpdate(classroom);
            }

            return new Result(Result.SUCCESS, "成功");
        } catch (Exception e) {
            logger.error("导入教室数据错误：{}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(Result.FAILURE, "失败");
        } finally {
            try {
                fileSupportService.remove(fileId);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(Result.FAILURE, "请上传excel格式的数据文件");
            }
        }
    }

    public Classroom findById(Integer classroomId) {
        return classroomMapper.selectByPrimaryKey(classroomId);
    }

    /**
     * 教室列表
     *
     * @param example
     * @return
     */
    public List<Classroom> findList(ClassroomCriteria example) {
        return classroomMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 教室列表
     *
     * @param example
     * @param extendParams 额外的参数
     * @return
     */
    public List<Map> findList(ClassroomCriteria example, Map extendParams) {
        List<Classroom> classroomList = findList(example);
        List<Map> mapList = new ArrayList<>();
        for (Classroom classroom : classroomList) {
            Map temp = BeanUtil.beanToMap(classroom, extendParams);
            mapList.add(temp);
        }
        return mapList;
    }

    /**
     * 查询满足条件的空教室
     *
     * @param schoolId      学校ID
     * @param termCode      学期代码
     * @param courseWeek    周次
     * @param courseWeekDay 星期
     * @param buildingId    教学楼ID
     * @param seatRange     人数
     * @param isMedia       是否包含多媒体教室（1是，2否）
     * @param startTimeList 开始使用时间段集合
     * @param orderByFiled  排序字段
     * @param asc           是否是按升序排
     * @return
     */
    public List<Classroom> findReleaseList(Integer schoolId, String termCode, Integer courseWeek, String courseWeekDay,
                                           Integer buildingId, String seatRange, Integer isMedia, List<String> startTimeList, String orderByFiled, boolean asc) {
        ClassroomTypeCriteria classroomTypeEx = new ClassroomTypeCriteria();
        classroomTypeEx.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andIsMediaEqualTo(isMedia + "")
                .andIsDeleteEqualTo((short) 2);
        //查询带有多媒体设备的教室类型
        List<ClassroomType> classroomTypeList = classroomTypeService.findList(classroomTypeEx);
        List<Integer> typeIdList = new ArrayList<>();
        for (ClassroomType classroomType : classroomTypeList) {
            typeIdList.add(classroomType.getId());
        }

        //查询教室表，筛选xx教学楼下的xx类型的，xx座位数的可用的教室
        ClassroomCriteria example = new ClassroomCriteria();
        ClassroomCriteria.Criteria criteria = example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andStatusEqualTo((short) 1)
                .andTypeIdIn(typeIdList);

        //根据教学楼来筛选
        if (buildingId != null)
            criteria.andBuildingIdEqualTo(buildingId);

        if (StringUtils.isNotBlank(seatRange)) {
            //根据人数规模来筛选
            seatRange = StringUtils.deleteWhitespace(seatRange);
            String[] array;
            if (seatRange.contains("-")) {
                array = seatRange.split("-");
            } else {
                array = seatRange.split("~");
            }
            criteria.andSeatCountGreaterThanOrEqualTo(Integer.valueOf(array[1]));
        }
        List<Classroom> classroomList = classroomMapper.selectByExample(example);


        //查询教室预约表，筛选出xx时间段被预约的教室集合
        List<ClassroomBookItem> classroomBookItemList = classroomBookItemMapperEx.findClassroomBookList(buildingId, startTimeList);

        //指定日期当天的课程表
        /*String teacherCoursePlanCacheKey = "teacherCoursePlan_" + TimeUtil.format(bookDate, "yyyy-MM-dd");
        List<TeacherCoursePlan> teacherCoursePlanList = CacheSupport.get(teacherCoursePlanCacheKey, teacherCoursePlanCacheKey, List.class);
        if (teacherCoursePlanList == null) {*/
        List<TeacherCoursePlan> teacherCoursePlanList = teacherCoursePlanService.findList(schoolId, courseWeek, courseWeekDay, startTimeList);
        /*CacheSupport.put("teacherCoursePlanCache", teacherCoursePlanCacheKey, teacherCoursePlanList);
        }*/

        //指定日期，指定时间段当天的排考列表
        List<ExamArrange> examArrangeList = examArrangeService.findExamArrangeListByExamTime(schoolId, startTimeList);

        //过滤数据
        classroomList = filterList(schoolId, (short) 1, classroomList, startTimeList);
        classroomList = filterList(schoolId, (short) 3, classroomList, startTimeList);
        classroomList = filterList(classroomList, classroomBookItemList, teacherCoursePlanList, examArrangeList);
        return orderBy(classroomList, orderByFiled, asc);
    }

    /**
     * 过滤教室数据
     * <p>
     * 1、去除已经被预约占用的教室（审批中、已通过的）
     * 2、去除日常上课占用的教室
     *
     * @param classroomList
     * @param classroomBookItemList
     * @param teacherCoursePlanList
     * @param examArrangeList
     * @return
     */
    public List<Classroom> filterList(List<Classroom> classroomList, List<ClassroomBookItem> classroomBookItemList,
                                      List<TeacherCoursePlan> teacherCoursePlanList, List<ExamArrange> examArrangeList) {
        Set<String> classroomNoSet = new HashSet<>();
        //所有上课需要占用的教室
        for (TeacherCoursePlan teacherCoursePlan : teacherCoursePlanList) {
            classroomNoSet.add(teacherCoursePlan.getClassroomNo());
        }
        //所有已经被预约的教室（包括待审核和审核通过的）
        for (ClassroomBookItem classroomBookItem : classroomBookItemList) {
            classroomNoSet.add(classroomBookItem.getClassroomNo());
        }
        //所有已经被考试占用的教室
        for (ExamArrange examArrange : examArrangeList) {
            classroomNoSet.addAll(Arrays.asList(examArrange.getClassroomNo().split(",")));
        }

        //迭代筛选出最终的教室列表
        Iterator<Classroom> iterator = classroomList.iterator();
        while (iterator.hasNext()) {
            Classroom classroom = iterator.next();
            classroom.setName(classroom.getName() + "  (座位数：" + classroom.getSeatCount() + ")");
            if (classroomNoSet.contains(classroom.getNo())) {
                iterator.remove();
            }
        }
        return classroomList;
    }

    /**
     * 过滤教室数据
     * <p>
     * 如果预约的时间为第三、第四节课，要是该教室存在第一、第二节被预约的记录，则该教室不能被预约
     *
     * @param schoolId
     * @param periodType    时间段（1上午，2中午，3下午，4晚上）
     * @param classroomList 教室集合
     * @param startTimeList 开始使用时间集合
     * @return
     */
    public List<Classroom> filterList(Integer schoolId, Short periodType, List<Classroom> classroomList, List<String> startTimeList) {
        TeachingCourseTimePlanCriteria teachingCourseTimePlanEx = new TeachingCourseTimePlanCriteria();
        teachingCourseTimePlanEx.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andPeriodTypeEqualTo(periodType);
        List<TeachingCourseTimePlan> teachingCourseTimePlantList = teachingWeekService.findTeachingCourseTimePlanList(teachingCourseTimePlanEx);
        int courseIndex = 1;
        for (TeachingCourseTimePlan teachingCourseTimePlan : teachingCourseTimePlantList) {
            for (String startTime : startTimeList) {
                if (startTime.split(" ")[1].equals(teachingCourseTimePlan.getStartTime()) && (courseIndex == 3 || courseIndex == 4)) {
                    // 判断一二节课是否有预约
                    List<String> tempTimeList = Arrays.asList(
                            startTime.split(" ")[0] + " " + teachingCourseTimePlantList.get(0).getStartTime(),
                            startTime.split(" ")[0] + " " + teachingCourseTimePlantList.get(1).getStartTime());
                    Iterator<Classroom> iterator = classroomList.iterator();
                    while (iterator.hasNext()) {
                        Classroom classroom = iterator.next();
                        ClassroomBookItemCriteria classroomBookItemEx = new ClassroomBookItemCriteria();
                        classroomBookItemEx.createCriteria()
                                .andClassroomIdEqualTo(classroom.getId())
                                .andIsDeleteEqualTo((short) 2)
                                .andStartTimeIn(tempTimeList);
                        long count = classroomBookItemMapper.countByExample(classroomBookItemEx);
                        if (count > 0) {
                            iterator.remove();
                        }
                    }
                }
            }
            courseIndex++;
        }
        return classroomList;
    }

    /**
     * 如果教室预约的时间段是第三、四节课，要是第一二节课被预约了，则返回对应的教室编号，否则返回null
     *
     * @param schoolId
     * @param periodType
     * @param classroomList
     * @param bookDate
     * @param startTimeList
     * @return
     */
    public String isCanBook(Integer schoolId, Short periodType, List<Classroom> classroomList, String bookDate, List<String> startTimeList) {
        TeachingCourseTimePlanCriteria teachingCourseTimePlanEx = new TeachingCourseTimePlanCriteria();
        teachingCourseTimePlanEx.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andPeriodTypeEqualTo(periodType);
        List<TeachingCourseTimePlan> teachingCourseTimePlantList = teachingWeekService.findTeachingCourseTimePlanList(teachingCourseTimePlanEx);
        int courseIndex = 1;
        for (TeachingCourseTimePlan teachingCourseTimePlan : teachingCourseTimePlantList) {
            for (String startTime : startTimeList) {
                if (startTime.equals(teachingCourseTimePlan.getStartTime()) && (courseIndex == 3 || courseIndex == 4)) {
                    // 判断一二节课是否有预约
                    List<String> tempTimeList = Arrays.asList(
                            bookDate + " " + teachingCourseTimePlantList.get(0).getStartTime(),
                            bookDate + " " + teachingCourseTimePlantList.get(1).getStartTime());
                    for (Classroom classroom : classroomList) {
                        ClassroomBookItemCriteria classroomBookItemEx = new ClassroomBookItemCriteria();
                        classroomBookItemEx.createCriteria()
                                .andClassroomIdEqualTo(classroom.getId())
                                .andIsDeleteEqualTo((short) 2)
                                .andStartTimeIn(tempTimeList);
                        long count = classroomBookItemMapper.countByExample(classroomBookItemEx);
                        if (count > 0) {
                            return classroom.getNo();
                        }
                    }
                }
            }
            courseIndex++;
        }
        return null;
    }

    /**
     * 按给定条件排序
     *
     * @param classroomList
     * @param orderByFiled
     * @param asc
     * @return
     */
    public List<Classroom> orderBy(List<Classroom> classroomList, String orderByFiled, final boolean asc) {

        if ("classroomNo".equals(orderByFiled)) {
            Collections.sort(classroomList, new Comparator<Classroom>() {
                @Override
                public int compare(Classroom o1, Classroom o2) {
                    if (asc) {
                        return o1.getNo().compareTo(o2.getNo());
                    } else {
                        return o2.getNo().compareTo(o1.getNo());
                    }
                }
            });
        } else if ("create_time".equals(orderByFiled)) {
            Collections.sort(classroomList, new Comparator<Classroom>() {
                @Override
                public int compare(Classroom o1, Classroom o2) {
                    if (asc) {
                        return o1.getCreateTime().compareTo(o2.getCreateTime());
                    } else {
                        return o2.getCreateTime().compareTo(o1.getCreateTime());
                    }
                }
            });
        } else if ("seatCount".equals(orderByFiled)) {
            Collections.sort(classroomList, new Comparator<Classroom>() {
                @Override
                public int compare(Classroom o1, Classroom o2) {
                    if (asc) {
                        return o1.getSeatCount().compareTo(o2.getSeatCount());
                    } else {
                        return o2.getSeatCount().compareTo(o1.getSeatCount());
                    }
                }
            });
        }

        return classroomList;
    }

    /**
     * 新增或编辑
     *
     * @param classroom
     * @return
     */
    public Result addOrUpdate(Classroom classroom) {
        if (classroom.getBuildingId() == null) {
            return new Result(Result.CUSTOM_MESSAGE, "请选择教学楼");
        }

        if (StringUtils.isBlank(classroom.getNo())) {
            return new Result(Result.CUSTOM_MESSAGE, "教室号不能为空");
        }

        if (classroom.getTypeId() == null) {
            return new Result(Result.CUSTOM_MESSAGE, "请选择教室类型");
        }

        ClassroomCriteria example = new ClassroomCriteria();
        ClassroomCriteria.Criteria criteria = example.createCriteria();
        criteria.andBuildingIdEqualTo(classroom.getBuildingId())
                .andNoEqualTo(classroom.getNo())
                .andIsDeleteEqualTo((short) 2);
        if (classroom.getId() != null) {
            criteria.andIdNotEqualTo(classroom.getId());
        }

        long count = classroomMapper.countByExample(example);
        if (count > 0) {
            //批量导入时不会进入此判断
            return new Result(Result.CUSTOM_MESSAGE, "该教室已存在");
        }

        classroom.setName(classroom.getNo());
        if (classroom.getId() == null) {
            classroom.setCreateTime(new Date());
            classroomMapper.insertSelective(classroom);
        } else {
            classroom.setUpdateTime(new Date());
            classroomMapper.updateByPrimaryKeySelective(classroom);
        }
        return new Result(Result.SUCCESS, "成功", classroom);
    }

    public Result delete(Integer id) {
        ClassroomCriteria example = new ClassroomCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdEqualTo(id);
        List<Classroom> classroomList = classroomMapper.selectByExample(example);
        if (classroomList.isEmpty()) {
            return new Result(Result.CUSTOM_MESSAGE, "该教室不存在");
        }

        Classroom classroom = classroomList.get(0);
        classroom.setIsDelete((short) 1);
        classroom.setDeleteTime(new Date());
        classroomMapper.updateByPrimaryKeySelective(classroom);
        return new Result(Result.SUCCESS, "成功");
    }

    /**
     * 更新教室状态(启用，停用)
     *
     * @param id
     * @param status
     * @return
     */
    public synchronized Result changeStatus(Integer id, Short status) {
        Classroom classroom = classroomMapper.selectByPrimaryKey(id);
        if (classroom == null || (classroom != null && classroom.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该教室不存在");
        }

        if (classroom.getStatus().intValue() == 3) {
            return new Result(Result.CUSTOM_MESSAGE, "该教室目前正在被使用，不能停用");
        }

        if (status.intValue() == 2) {
            //停用教室时查询是否已经被预约，被预约了则无法停用
            ClassroomBookCriteria bookEx = new ClassroomBookCriteria();
            bookEx.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andStatusEqualTo(2)
                    .andBookDateGreaterThanOrEqualTo(TimeUtil.format(new Date(), "yyyy-MM-dd"));
            long count = classroomBookMapper.countByExample(bookEx);
            if (count > 0) {
                return new Result(Result.CUSTOM_MESSAGE, "该教室已经被预约，不能停用");
            }
        }

        ClassroomCriteria example = new ClassroomCriteria();
        example.createCriteria()
                .andIdEqualTo(id)
                .andVersionEqualTo(classroom.getVersion());

        classroom.setStatus(status);
        Date date = new Date();
        classroom.setStatus(status);
        classroom.setUpdateTime(date);
        classroom.setVersion(date.getTime() + "");

        int count = classroomMapper.updateByExampleSelective(classroom, example);
        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    /**
     * 占用教室
     *
     * @param classroomId
     * @param classroomBuildingId
     * @param classroomBookId
     * @return
     */
    public synchronized Result occupy(Integer classroomId, Integer classroomBuildingId, Integer classroomBookId) {
        Classroom classroom = classroomMapper.selectByPrimaryKey(classroomId);
        if (classroom == null) {
            return new Result(Result.CUSTOM_MESSAGE, "教室【" + classroom.getNo() + "】不存在，请联系教务");
        } else if (classroom.getIsDelete() == 1) {
            return new Result(Result.CUSTOM_MESSAGE, "教室【" + classroom.getNo() + "】不存在，请联系教务");
        } else if (classroom.getStatus().intValue() == 2) {
            return new Result(Result.CUSTOM_MESSAGE, "教室【" + classroom.getNo() + "】已经被停用");
        } else if (classroom.getStatus().intValue() == 3) {
            return new Result(Result.CUSTOM_MESSAGE, "教室【" + classroom.getNo() + "】已经被占用");
        }

        ClassroomBookItemCriteria itemEx = new ClassroomBookItemCriteria();
        itemEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andClassroomIdEqualTo(classroomId)
                .andBookIdEqualTo(classroomBookId);
        List<ClassroomBookItem> classroomBookItemList = classroomBookItemMapper.selectByExample(itemEx);
        if (classroomBookItemList.size() == 0) {
            return new Result(Result.CUSTOM_MESSAGE, "没有找到符合条件的教室预约记录");
        }

        ClassroomBookItem item = classroomBookItemList.get(0);
        //教室在相同时间被预定
        long bookedCount = classroomBookItemMapperEx.findBookedCount(classroomBookId, classroomBuildingId, classroom.getNo(), item.getStartTime());
        if (bookedCount > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "教室【" + classroom.getNo() + "】已经被占用");
        }

        // 教室在相同时间被排考占用
        ClassroomBook classroomBook = classroomBookMapper.selectByPrimaryKey(classroomBookId);
        ClassroomBuilding classroomBuilding = classroomBuildingService.findById(classroomBook.getClassroomBuildingId());
        int schoolId = classroomBuilding.getSchoolId();
        Date bookDate = null;
        try {
            bookDate = DateUtils.parseDate(classroomBook.getBookDate(), new String[]{"yyyy-MM-dd"});
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 教室在相同时间被排考占用
        List<ExamArrange> queryExamArrangeList = examArrangeService.findExamArrangeList(schoolId, classroom.getNo(),
                bookDate, Arrays.asList(item.getStartTime()));
        if (!queryExamArrangeList.isEmpty()) {
            return new Result(Result.CUSTOM_MESSAGE, "教室【" + classroom.getNo() + "】已经被占用");
        }

        ClassroomOccupy classroomOccupy = new ClassroomOccupy();
        classroomOccupy.setClassroomId(classroomId);
        classroomOccupy.setStartTime(item.getStartTime());
        classroomOccupy.setEndTime(item.getEndTime());
        int count1 = classroomOccupyMapper.insertSelective(classroomOccupy);

        ClassroomCriteria example = new ClassroomCriteria();
        example.createCriteria()
                .andIdEqualTo(classroomId)
                .andVersionEqualTo(classroom.getVersion());

        classroom.setStatus((short) 3);
        Date date = new Date();
        classroom.setUpdateTime(date);
        classroom.setVersion(date.getTime() + "");

        int count2 = classroomMapper.updateByExampleSelective(classroom, example);
        if (count1 <= 0 && count2 <= 0) {
            return new Result(Result.FAILURE, "失败");
        } else {
            return new Result(Result.SUCCESS, "成功");
        }
    }

    /**
     * 释放教室
     *
     * @param classroomId
     * @return
     */
    public synchronized Result release(Integer classroomId) {
        ClassroomOccupyCriteria classroomOccupyEx = new ClassroomOccupyCriteria();
        classroomOccupyEx.createCriteria()
                .andClassroomIdEqualTo(classroomId);
        int count1 = classroomOccupyMapper.deleteByExample(classroomOccupyEx);

        Classroom classroom = classroomMapper.selectByPrimaryKey(classroomId);
        ClassroomCriteria example = new ClassroomCriteria();
        example.createCriteria()
                .andIdEqualTo(classroomId)
                .andVersionEqualTo(classroom.getVersion());

        classroom.setStatus((short) 1);
        Date date = new Date();
        classroom.setUpdateTime(date);
        classroom.setVersion(date.getTime() + "");

        int count2 = classroomMapper.updateByExample(classroom, example);
        if (count1 <= 0 && count2 <= 0) {
            return new Result(Result.FAILURE, "失败");
        } else {
            return new Result(Result.SUCCESS, "成功");
        }
    }

    public Classroom findOne(Integer schoolId, String classroomNo) {
        List<Integer> classroomBuildingIdList = new ArrayList<>();
        ClassroomBuildingCriteria buildingEx = new ClassroomBuildingCriteria();
        buildingEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(schoolId);
        List<ClassroomBuilding> buildingList = classroomBuildingService.findList(buildingEx);
        for (ClassroomBuilding building : buildingList)
            classroomBuildingIdList.add(building.getId());

        ClassroomCriteria ex = new ClassroomCriteria();
        ex.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andNoEqualTo(classroomNo)
                .andBuildingIdIn(classroomBuildingIdList);
        List<Classroom> list = classroomMapper.selectByExample(ex);
        return list.isEmpty() ? null : list.get(0);
    }
}
