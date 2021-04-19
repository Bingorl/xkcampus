package com.biu.wifi.campus.service;

import com.biu.wifi.campus.constant.AudUserRole;
import com.biu.wifi.campus.constant.NoticeType;
import com.biu.wifi.campus.dao.*;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangbin.
 * @date 2018/12/13.
 */
@Service
public class ClassroomBookAuditService {

    private static Logger logger = LoggerFactory.getLogger(ClassroomBookAuditService.class);
    @Autowired
    private ClassroomBookAuditMapper classroomBookAuditMapper;
    @Autowired
    private ClassroomBookAuditNoticeMapper classroomBookAuditNoticeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PushSerivce pushSerivce;
    @Autowired
    private PushMapper pushMapper;
    @Autowired
    private ClassroomBookUseTypeMapper classroomBookUseTypeMapper;
    @Autowired
    private ClassroomBookMapper classroomBookMapper;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private AuditUserAuthService auditUserAuthService;
    @Autowired
    private ClassroomBookService classroomBookService;
    @Autowired
    private AuditInfoMapper auditInfoMapper;
    @Autowired
    private ClassroomBuildingMapper classroomBuildingMapper;
    @Autowired
    private AuditRolePermissionService auditRolePermissionService;
    @Autowired
    private NoticeInfoMapper noticeInfoMapper;
    @Autowired
    private AuditUserRoleService auditUserRoleService;

    /**
     * 添加审核
     *
     * @param classroomBookAudit 审核信息
     * @param noticeReceiverType 通知接收人类型(1申请人、2教学秘书、3教务、4审核教师）
     * @return
     */
    public Result addAudit(ClassroomBookAudit classroomBookAudit, Short noticeReceiverType) {
        // 添加审核记录
        ClassroomBookAuditCriteria example = new ClassroomBookAuditCriteria();
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andSchoolIdEqualTo(classroomBookAudit.getSchoolId())
                .andBookIdEqualTo(classroomBookAudit.getBookId())
                .andUserIdEqualTo(classroomBookAudit.getUserId());
        long count = classroomBookAuditMapper.countByExample(example);
        if (count > 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录已存在");
        }

        classroomBookAudit.setCreateTime(new Date());
        count = classroomBookAuditMapper.insertSelective(classroomBookAudit);
        // 创建汇总记录
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setUserId(classroomBookAudit.getUserId());
        auditInfo.setBizId(classroomBookAudit.getId());
        auditInfo.setType((short) 2);
        count = auditInfoMapper.insertSelective(auditInfo);

        ClassroomBook classroomBook = classroomBookMapper.selectByPrimaryKey(classroomBookAudit.getBookId());
        ClassroomBookUseType classroomBookUseType = classroomBookUseTypeMapper.selectByPrimaryKey(classroomBook.getUseTypeId());
        // 申请人的名字
        User user = userMapper.selectByPrimaryKey(classroomBook.getUserId());
        // 添加通知记录
        String title = "【教室预约】" + classroomBookUseType.getName() + " " + user.getName() + " " + user.getStuNumber() + "!";
        String classroomBookContent = classroomBook.getContent().length() > 20 ? classroomBook.getContent().substring(0, 20) + "..." : classroomBook.getContent();
        String classroomBookTime = classroomBook.getBookDate() +
                teachingWeekService.getCourseIndex(user.getSchoolId(), Arrays.asList(classroomBook.getBookPeriod().split(",")));
        String content = classroomBook.getTitle() + "\n" + classroomBookContent + "\n" + classroomBookTime;

        // 插入通知
        Result result = addNotice(classroomBook.getId(), title, content, classroomBookAudit.getRemark(),
                classroomBookAudit.getUserId(), noticeReceiverType, classroomBookAudit.getSchoolId(), (short) 1);
        if (result.getKey() != Result.SUCCESS) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }

        // 推送
        pushSerivce.pushNotice(title, classroomBook.getId(), classroomBookAudit.getUserId(), user.getDevToken(), user.getDevType(), (short) 13);

        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Result(Result.FAILURE, "失败");
        }
    }

    /**
     * 审批
     *
     * @param classroomBookAudit  审批
     * @param nextAuditUserId     下一个审核人ID,如果为null说明审批流结束
     * @param toTeachingSecretary 是否发送给教学秘书（是true，否false）
     *                            只有教务处人员审核过后，才可以发送给教学秘书
     * @return
     */
    public Result updateAudit(ClassroomBookAudit classroomBookAudit, Integer nextAuditUserId, boolean toTeachingSecretary) {
        return doUpdateAudit(classroomBookAudit, nextAuditUserId, toTeachingSecretary);
    }

    /**
     * 审批
     *
     * @param classroomBookAuditId 审核ID
     * @param currentAuditUserId   当前审核人ID
     * @param nextAuditUserId      下一个审核人ID,如果为null说明审批流结束
     * @param toTeachingSecretary  是否发送给教学秘书（是true，否false）
     *                             只有教务处人员审核过后，才可以发送给教学秘书
     * @param isPass               是否通过(1通过,2驳回)
     * @param remark               备注
     * @return
     */
    public Result updateAudit(Integer classroomBookAuditId, Integer currentAuditUserId,
                              Integer nextAuditUserId, boolean toTeachingSecretary, Short isPass, String remark) {
        ClassroomBookAudit classroomBookAudit = classroomBookAuditMapper.selectByPrimaryKey(classroomBookAuditId);
        if (classroomBookAudit == null || (classroomBookAudit != null && classroomBookAudit.getIsDelete() == 1)) {
            return new Result(Result.CUSTOM_MESSAGE, "该记录不存在");
        }

        if (currentAuditUserId.intValue() != classroomBookAudit.getUserId().intValue()) {
            return new Result(Result.CUSTOM_MESSAGE, "没有审核权限");
        }
        Date date = new Date();
        classroomBookAudit.setIsPass(isPass);
        classroomBookAudit.setAuditTime(date);
        classroomBookAudit.setRemark(remark);
        return doUpdateAudit(classroomBookAudit, nextAuditUserId, toTeachingSecretary);
    }

    /**
     * 审批
     *
     * @param classroomBookAudit  审批
     * @param nextAuditUserId     下一个审核人ID,如果为null说明审批流结束
     * @param toTeachingSecretary 是否发送给教学秘书（是true，否false）
     *                            只有教务处人员审核过后，才可以发送给教学秘书
     * @return
     */
    public Result doUpdateAudit(ClassroomBookAudit classroomBookAudit, Integer nextAuditUserId, boolean toTeachingSecretary) {
        long count = classroomBookAuditMapper.updateByPrimaryKeySelective(classroomBookAudit);

        ClassroomBook classroomBook = classroomBookMapper.selectByPrimaryKey(classroomBookAudit.getBookId());
        if (classroomBook.getStatus() != 1) {
            return new Result(Result.CUSTOM_MESSAGE, "该条预约申请已被审核");
        }
        ClassroomBookUseType classroomBookUseType = classroomBookUseTypeMapper.selectByPrimaryKey(classroomBook.getUseTypeId());
        // 教室预约申请人
        User user = userMapper.selectByPrimaryKey(classroomBook.getUserId());
        // 审核人
        User auditUser = userMapper.selectByPrimaryKey(classroomBookAudit.getUserId());
        if (classroomBookAudit.getIsPass().intValue() == 2) {
            // 驳回
            // 更新教室预约记录的状态
            Result result = classroomBookService.auditUpdateClassBookStatus(classroomBook.getId(), 3, classroomBook.getVersion(),
                    auditUser.getId(), auditUser.getName(), classroomBookAudit.getRemark());
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                logger.error("更新教室预约记录的状态失败");
                return result;
            }

            // 只要有一个教务人员审批通过，更新其他所有教务人员对该条预约的状态
            result = batchUpdateClassroomBookAuditStatusAfterOneAudit(classroomBookAudit, classroomBook, user);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }

            // 添加通知记录(给申请人的)
            String title = "【教室预约】" + classroomBookUseType.getName() + "  您的预约未通过审批！";
            String classroomBookContent;
            if (StringUtils.isBlank(classroomBook.getAdjustClassroomNo())) {
                classroomBookContent = "教室：" + classroomBook.getClassroomNo();
            } else {
                classroomBookContent = "原教室：" + classroomBook.getClassroomNo() +
                        "\n调整后的教室：" + classroomBook.getAdjustClassroomNo();
            }
            String classroomBookTime = classroomBook.getBookDate() +
                    teachingWeekService.getCourseIndex(user.getSchoolId(), Arrays.asList(classroomBook.getBookPeriod().split(",")));
            classroomBookContent += "\n" + classroomBookTime;

            // 插入通知
            result = addNotice(classroomBook.getId(), title, classroomBookContent, classroomBookAudit.getRemark(),
                    user.getId(), (short) 1, user.getSchoolId(), (short) 2);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result;
            }
            pushSerivce.pushNotice(title, classroomBook.getId(), classroomBookAudit.getUserId(), user.getDevToken(), user.getDevType(), (short) 13);

            if (count > 0) {
                return new Result(Result.SUCCESS, "成功");
            } else {
                return new Result(Result.FAILURE, "失败");
            }
        }

        if (nextAuditUserId != null) {
            // 通知下一个审批人
            classroomBookAudit = new ClassroomBookAudit();
            classroomBookAudit.setSchoolId(user.getSchoolId());
            classroomBookAudit.setBookId(classroomBook.getId());
            classroomBookAudit.setUserId(nextAuditUserId);
            classroomBookAudit.setCreateTime(new Date());

            Result result = addAudit(classroomBookAudit, (short) 4);
            if (result.getKey() != Result.SUCCESS) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return new Result(Result.FAILURE, "失败");
            }
        } else {
            if (toTeachingSecretary) {
                // 教务审批成功
                // 插入教室占用记录
                String classroomNo = StringUtils.deleteWhitespace(classroomBook.getClassroomNo());
                if (StringUtils.isNotBlank(classroomBook.getAdjustClassroomNo())) {
                    classroomNo = StringUtils.deleteWhitespace(classroomBook.getAdjustClassroomNo());
                }
                Result result = classroomBookService.batchOccupyClassroom(classroomBook.getId(), classroomNo);
                if (result.getKey() != Result.SUCCESS) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    logger.error("插入教室占用记录失败");
                    return result;
                }

                // 更新教室预约记录的状态
                result = classroomBookService.auditUpdateClassBookStatus(classroomBook.getId(), 2, classroomBook.getVersion(),
                        auditUser.getId(), auditUser.getName(), null);
                if (result.getKey() != Result.SUCCESS) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    logger.error("更新教室预约记录的状态失败");
                    return result;
                }

                // 只要有一个教务人员审批通过，更新其他所有教务人员对该条预约的状态
                result = batchUpdateClassroomBookAuditStatusAfterOneAudit(classroomBookAudit, classroomBook, user);
                if (result.getKey() != Result.SUCCESS) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }

                // 推送给教学秘书和申请人
                // 添加通知记录(给申请人的)
                String title = "【教室预约】" + classroomBookUseType.getName() + "  您的预约已通过审批！";
                String classroomBookContent;
                if (StringUtils.isBlank(classroomBook.getAdjustClassroomNo())) {
                    classroomBookContent = "教室：" + classroomBook.getClassroomNo();
                } else {
                    classroomBookContent = "原教室：" + classroomBook.getClassroomNo() +
                            "\n调整后的教室：" + classroomBook.getAdjustClassroomNo();
                }
                String classroomBookTime = classroomBook.getBookDate() +
                        teachingWeekService.getCourseIndex(user.getSchoolId(), Arrays.asList(classroomBook.getBookPeriod().split(",")));
                classroomBookContent += "\n" + classroomBookTime;

                result = addNotice(classroomBook.getId(), title, classroomBookContent, classroomBookAudit.getRemark(),
                        user.getId(), (short) 1, user.getSchoolId(), (short) 2);
                if (result.getKey() != Result.SUCCESS) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
                pushSerivce.pushNotice(title, classroomBook.getId(), classroomBookAudit.getUserId(), user.getDevToken(), user.getDevType(), (short) 13);

                // 添加通知记录(给教学秘书的)
                // 查询该学院下的教学秘书
                List<User> jwmsUserList = auditUserRoleService.getJxmsUserList(user.getSchoolId(), user.getInstituteId());
                title = "【教室预约】" + classroomBookUseType.getName() + " " + user.getName() + " " + user.getStuNumber() + "  的预约已通过审批！";
                if (StringUtils.isBlank(classroomBook.getAdjustClassroomNo())) {
                    classroomBookContent = "教室：" + classroomBook.getClassroomNo();
                } else {
                    classroomBookContent = "原教室：" + classroomBook.getClassroomNo() +
                            "\n调整后的教室：" + classroomBook.getAdjustClassroomNo();
                }
                classroomBookTime = classroomBook.getBookDate() +
                        teachingWeekService.getCourseIndex(user.getSchoolId(), Arrays.asList(classroomBook.getBookPeriod().split(",")));
                classroomBookContent += "\n" + classroomBookTime;
                for (User jwmsUser : jwmsUserList) {
                    result = addNotice(classroomBook.getId(), title, classroomBookContent, classroomBookAudit.getRemark(),
                            jwmsUser.getId(), (short) 2, jwmsUser.getSchoolId(), (short) 2);
                    if (result.getKey() != Result.SUCCESS) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result;
                    }
                    pushSerivce.pushNotice(title, classroomBook.getId(), classroomBookAudit.getUserId(), user.getDevToken(), user.getDevType(), (short) 13);
                }
//                AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(classroomBookAudit.getSchoolId(), AudUserRole.JXMS.getCode());
//                if (auditUserRole == null) {
//                    return new Result(Result.CUSTOM_MESSAGE, "未设置教务秘书类型的角色，无法推送通知给教学秘书，请联系教务处");
//                }
//                List<AuditUserAuth> auditUserAuthList = auditUserAuthService.findList(user.getSchoolId(), user.getInstituteId(), auditUserRole.getId());
//                for (AuditUserAuth auth : auditUserAuthList) {
//                    title = "【教室预约】" + classroomBookUseType.getName() + " " + user.getName() + " " + user.getStuNumber() + "  的预约已通过审批！";
//                    if (StringUtils.isBlank(classroomBook.getAdjustClassroomNo())) {
//                        classroomBookContent = "教室：" + classroomBook.getClassroomNo();
//                    } else {
//                        classroomBookContent = "原教室：" + classroomBook.getClassroomNo() +
//                                "\n调整后的教室：" + classroomBook.getAdjustClassroomNo();
//                    }
//                    classroomBookTime = classroomBook.getBookDate() +
//                            teachingWeekService.getCourseIndex(user.getSchoolId(), Arrays.asList(classroomBook.getBookPeriod().split(",")));
//                    classroomBookContent += "\n" + classroomBookTime;
//
//                    user = userMapper.selectByPrimaryKey(auth.getUserId());
//                    result = addNotice(classroomBook.getId(), title, classroomBookContent, classroomBookAudit.getRemark(),
//                            user.getId(), (short) 2, user.getSchoolId(), (short) 2);
//                    if (result.getKey() != Result.SUCCESS) {
//                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                        return result;
//                    }
//                    pushSerivce.pushNotice(title, classroomBook.getId(), classroomBookAudit.getUserId(), user.getDevToken(), user.getDevType(), (short) 13);
//                }
            } else {
                // 添加通知给教务
                // 查询该学院下的教务人员
                List<User> jwUserList = auditUserRoleService.getJwUserList(user.getSchoolId());
                for (User jwUser : jwUserList) {
                    classroomBookAudit = new ClassroomBookAudit();
                    classroomBookAudit.setSchoolId(jwUser.getSchoolId());
                    classroomBookAudit.setBookId(classroomBook.getId());
                    classroomBookAudit.setUserId(jwUser.getId());
                    classroomBookAudit.setCreateTime(new Date());

                    Result result = addAudit(classroomBookAudit, (short) 3);
                    if (result.getKey() != Result.SUCCESS) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return new Result(Result.FAILURE, "失败");
                    }
                }
//                AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(classroomBookAudit.getSchoolId(), AudUserRole.JWRY.getCode());
//                if (auditUserRole == null) {
//                    return new Result(Result.CUSTOM_MESSAGE, "未设置教务处类型的角色，无法推送通知给教务处，请联系教务处");
//                }
//                List<AuditUserAuth> auditUserAuthList = auditUserAuthService.findList(user.getSchoolId(), null, auditUserRole.getId());
//                for (AuditUserAuth auth : auditUserAuthList) {
//                    classroomBookAudit = new ClassroomBookAudit();
//                    classroomBookAudit.setSchoolId(auth.getSchoolId());
//                    classroomBookAudit.setBookId(classroomBook.getId());
//                    classroomBookAudit.setUserId(auth.getUserId());
//                    classroomBookAudit.setCreateTime(new Date());
//
//                    Result result = addAudit(classroomBookAudit, (short) 3);
//                    if (result.getKey() != Result.SUCCESS) {
//                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                        return new Result(Result.FAILURE, "失败");
//                    }
//                }
            }
        }

        if (count > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

    /**
     * 有一个教务人员对申请做出审批操作，则更新其他所有教务人员对该条预约的状态
     *
     * @param classroomBookAudit
     * @param classroomBook
     * @param user
     * @return
     */
    public Result batchUpdateClassroomBookAuditStatusAfterOneAudit(ClassroomBookAudit classroomBookAudit, ClassroomBook classroomBook, User user) {
        AuditUserRole auditUserRole = auditUserRoleService.findBySchoolIdAndCode(classroomBookAudit.getSchoolId(), AudUserRole.JWRY.getCode());
        if (auditUserRole == null) {
            return new Result(Result.CUSTOM_MESSAGE, "未设置教务处类型的角色，无法推送通知给教务处，请联系教务处");
        }
        List<AuditUserAuth> auditUserAuthList = auditUserAuthService.findList(user.getSchoolId(), null, auditUserRole.getId());
        for (AuditUserAuth auth : auditUserAuthList) {
            ClassroomBookAuditCriteria classroomBookAuditEx = new ClassroomBookAuditCriteria();
            classroomBookAuditEx.createCriteria()
                    .andBookIdEqualTo(classroomBook.getId())
                    .andUserIdEqualTo(auth.getUserId())
                    .andIsPassIsNull();
            List<ClassroomBookAudit> classroomBookAuditList = classroomBookAuditMapper.selectByExample(classroomBookAuditEx);
            for (ClassroomBookAudit classroomBookAudit1 : classroomBookAuditList) {
                classroomBookAudit1.setIsPass(classroomBookAudit.getIsPass());
                classroomBookAudit1.setRemark(classroomBookAudit.getRemark());
                classroomBookAudit1.setAuditTime(new Date());
                int count1 = classroomBookAuditMapper.updateByPrimaryKeySelective(classroomBookAudit1);

                AuditInfo auditInfo = new AuditInfo();
                auditInfo.setIsPass(classroomBookAudit.getIsPass());
                AuditInfoCriteria auditInfoEx = new AuditInfoCriteria();
                auditInfoEx.createCriteria()
                        .andBizIdEqualTo(classroomBookAudit1.getId())
                        .andUserIdEqualTo(auth.getUserId())
                        .andTypeEqualTo((short) 2);
                int count2 = auditInfoMapper.updateByExampleSelective(auditInfo, auditInfoEx);
                if (count1 <= 0 || count2 <= 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return new Result(Result.FAILURE, "失败");
                }
            }
        }
        return new Result(Result.SUCCESS, "成功");
    }

    /**
     * 添加通知
     *
     * @param bookId     教室预约ID
     * @param title      标题
     * @param content    内容
     * @param remark     备注
     * @param toUserId   被通知人ID
     * @param toUserType 被通知人类型(1申请人、2教学秘书、3教务、4审核教师）
     * @param schoolId   学校ID
     * @param isDelete   是否删除(1是，2否)
     * @return
     */
    public Result addNotice(Integer bookId, String title, String content, String remark, Integer toUserId,
                            Short toUserType, Integer schoolId, Short isDelete) {
        ClassroomBookAuditNotice classroomBookAuditNotice = new ClassroomBookAuditNotice();
        classroomBookAuditNotice.setTitle(title);
        classroomBookAuditNotice.setContent(content);
        classroomBookAuditNotice.setBookId(bookId);
        classroomBookAuditNotice.setRemark(remark);
        classroomBookAuditNotice.setSchoolId(schoolId);
        classroomBookAuditNotice.setToUserId(toUserId);
        classroomBookAuditNotice.setToUserType(toUserType);
        classroomBookAuditNotice.setCreateTime(new Date());
        classroomBookAuditNotice.setIsDelete(isDelete);
        if (isDelete.intValue() == 1) {
            classroomBookAuditNotice.setDeleteTime(new Date());
        }
        int count = classroomBookAuditNoticeMapper.insertSelective(classroomBookAuditNotice);

        NoticeInfo noticeInfo = new NoticeInfo();
        noticeInfo.setUserId(toUserId);
        noticeInfo.setType(NoticeType.CLASSROOM_BOOK_NOTICE.getCode().shortValue());
        noticeInfo.setBizId(classroomBookAuditNotice.getId());
        noticeInfo.setIsDelete(isDelete);
        int count2 = noticeInfoMapper.insertSelective(noticeInfo);

        if (count > 0 && count2 > 0) {
            return new Result(Result.SUCCESS, "成功");
        } else {
            return new Result(Result.FAILURE, "失败");
        }
    }

//    /**
//     * 推送通知
//     *
//     * @param title              通知标题
//     * @param classroomBookAudit 审核信息
//     * @param toUserId           通知接收人ID
//     * @param devToken           设备token
//     * @param devType            设备类型
//     * @param messageType        消息类型
//     * @param classroomBookId    预定ID
//     * @return
//     */
//    public Result pushNotice(String title, ClassroomBookAudit classroomBookAudit,
//                             Integer toUserId, String devToken, Short devType, Short messageType, Integer classroomBookId) {
//
//
//        // 推送
//        boolean flag = false;
//        HashMap<String, Object> hm = new HashMap<>();
//        hm.put("id", classroomBookId);
//        hm.put("title", title);
//        hm.put("type", messageType);
//
//        try {
//            flag = PushTool.pushToUser(classroomBookAudit.getUserId(), "", title, hm);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 入推送表
//        Push puEntity = new Push();
//        puEntity.setToken(devToken);
//        puEntity.setContent("");
//        puEntity.setUserId(toUserId);
//        puEntity.setMessageType(messageType);
//        puEntity.setDeviceType(devType);
//        puEntity.setTitle(title);
//        if (flag) {
//            puEntity.setType((short) 10);
//        } else {
//            puEntity.setType((short) 50);
//        }
//
//        int count = pushMapper.insertSelective(puEntity);
//
//        if (count > 0) {
//            return new Result(Result.SUCCESS, "成功");
//        } else {
//            logger.error("推送表记录插入失败");
//            return new Result(Result.FAILURE, "失败");
//        }
//    }

    public List<ClassroomBookAudit> findList(ClassroomBookAuditCriteria example) {
        return classroomBookAuditMapper.selectByExample(example);
    }

    /**
     * 查询xx用户的教室预约审批记录
     *
     * @param userId
     */
    public List<Map<String, Object>> findAuditList(Integer userId) {
        ClassroomBookAuditCriteria classroomBookAuditEx = new ClassroomBookAuditCriteria();
        classroomBookAuditEx.setOrderByClause("create_time desc");
        classroomBookAuditEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andUserIdEqualTo(userId);
        List<ClassroomBookAudit> classroomBookAuditList = classroomBookAuditMapper.selectByExample(classroomBookAuditEx);

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        User user = userMapper.selectByPrimaryKey(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ClassroomBookAudit audit : classroomBookAuditList) {
            map = formatClassroomBookAuditData(audit, user.getIsTeacher(), sdf);
            list.add(map);
        }
        return list;
    }

    public Map<String, Object> findAudit(Integer id) {
        ClassroomBookAudit classroomBookAudit = classroomBookAuditMapper.selectByPrimaryKey(id);
        User user = userMapper.selectByPrimaryKey(classroomBookAudit.getUserId());
        return formatClassroomBookAuditData(classroomBookAudit, user.getIsTeacher(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 格式化教室预约审核数据
     *
     * @param audit
     * @param isTeacher
     * @param sdf
     * @return
     */
    public Map<String, Object> formatClassroomBookAuditData(ClassroomBookAudit audit,
                                                            short isTeacher, SimpleDateFormat sdf) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", audit.getId());
        map.put("noticeType", "3");// 教室预约审批通知
        map.put("create_time", sdf.format(audit.getCreateTime()));
        map.put("name", "");

        ClassroomBookAuditNoticeCriteria classroomBookAuditNoticeEx = new ClassroomBookAuditNoticeCriteria();
        classroomBookAuditNoticeEx.setOrderByClause("create_time desc");
        classroomBookAuditNoticeEx.createCriteria()
                .andIsDeleteEqualTo((short) 1)// 教室待办审核的通知默认是删除的，便于查询待办审核列表的时候查询
                .andBookIdEqualTo(audit.getBookId())
                .andToUserIdEqualTo(audit.getUserId());
        List<ClassroomBookAuditNotice> auditNoticeList = classroomBookAuditNoticeMapper.selectByExample(classroomBookAuditNoticeEx);
        ClassroomBookAuditNotice classroomBookAuditNotice = auditNoticeList.get(0);
        map.put("title", classroomBookAuditNotice.getTitle());
        map.put("classroomBookId", audit.getBookId());
        map.put("content", classroomBookAuditNotice.getContent());
        // is_received
        // 是否已收到
        if (isTeacher == 1) {
            map.put("is_received", audit.getIsPass() == null ? 2 : 1);
        }

        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");

        ClassroomBook classroomBook = classroomBookMapper.selectByPrimaryKey(audit.getBookId());
        map.put("bookDate", classroomBook.getBookDate());
        ClassroomBuilding classroomBuilding = classroomBuildingMapper.selectByPrimaryKey(classroomBook.getClassroomBuildingId());
        map.put("buildingName", classroomBuilding.getName());
        map.put("classroomNo", classroomBook.getClassroomNo());
        map.put("bookSection", classroomBook.getBookSection());

        // 是否允许调整教室
        if (classroomBook.getStatus() == 1) {
            User user = userMapper.selectByPrimaryKey(audit.getUserId());
            AuditUserAuth auditUserAuth = auditUserAuthService.find(user.getSchoolId(), audit.getUserId());
            boolean allowAdjustClassroomNo = auditRolePermissionService.findPermissionByCodeAndRoleId(auditUserAuth.getRoleId(),
                    "classroomBook/adjustClassroomNo/update");
            map.put("allowAdjustClassroomNo", allowAdjustClassroomNo);
        } else {
            map.put("allowAdjustClassroomNo", false);
        }
        return map;
    }

    /**
     * xx用户收到的教室预约通知列表
     *
     * @param example
     */
    public List<ClassroomBookAuditNotice> findAuditNoticeList(ClassroomBookAuditNoticeCriteria example) {
        return classroomBookAuditNoticeMapper.selectByExample(example);
    }

    /**
     * xx用户收到的教室预约通知列表
     *
     * @param userId
     */
    public List<Map<String, Object>> findAuditNoticeList(Integer userId) {
        ClassroomBookAuditNoticeCriteria classroomBookAuditNoticeEx = new ClassroomBookAuditNoticeCriteria();
        classroomBookAuditNoticeEx.setOrderByClause("create_time desc");
        classroomBookAuditNoticeEx.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andToUserIdEqualTo(userId);
        List<ClassroomBookAuditNotice> auditNoticeList = classroomBookAuditNoticeMapper.selectByExample(classroomBookAuditNoticeEx);

        List<Map<String, Object>> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ClassroomBookAuditNotice auditNotice : auditNoticeList) {
            Map<String, Object> map = formatClassroomBookAuditNoticeData(auditNotice, sdf);
            list.add(map);
        }
        return list;
    }

    public Map<String, Object> findAuditNoticeById(Integer id) {
        ClassroomBookAuditNotice auditNotice = classroomBookAuditNoticeMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatClassroomBookAuditNoticeData(auditNotice, sdf);
    }

    /**
     * 格式化教室预约审核通知
     *
     * @param auditNotice
     * @param sdf
     * @return
     */
    public Map<String, Object> formatClassroomBookAuditNoticeData(ClassroomBookAuditNotice auditNotice, SimpleDateFormat sdf) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", auditNotice.getId());
        map.put("noticeType", NoticeType.CLASSROOM_BOOK_NOTICE.getCode().shortValue());// 教室预约审批通知
        map.put("create_time", sdf.format(auditNotice.getCreateTime()));
        map.put("name", "");
        map.put("title", auditNotice.getTitle());
        map.put("content", auditNotice.getContent());
        map.put("classroomBookId", auditNotice.getBookId());
        // is_received
        // 是否已读、是否已收到
        map.put("is_received", auditNotice.getIsConfirm() == 1 ? 1 : 2);

        map.put("question_number", 0);
        map.put("received_all", 0);
        map.put("received_number", 0);
        map.put("is_calendar", "2");
        map.put("remind_date", "");
        map.put("remind_time", "");
        map.put("event_id", "");
        return map;
    }

    /**
     * 是否有教室预约的审核操作权限
     *
     * @param userId
     * @param classroomBookId
     * @param status          状态（1审核中，2通过，3驳回）
     * @return
     */
    public boolean allowToDoClassroomBookAudit(Integer userId, Integer classroomBookId, String status) {
        if ("1".equals(status)) {
            ClassroomBookAuditCriteria example = new ClassroomBookAuditCriteria();
            example.createCriteria()
                    .andIsDeleteEqualTo((short) 2)
                    .andUserIdEqualTo(userId)
                    .andBookIdEqualTo(classroomBookId);
            List<ClassroomBookAudit> classroomBookAuditList = classroomBookAuditMapper.selectByExample(example);
            if (classroomBookAuditList.size() == 0) {
                // 没有审核权限
                return false;
            } else {
                if (classroomBookAuditList.get(0).getIsPass() == null) {
                    // 有审核权限，但未进行审核
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            // 教室预约已有审核结果
            return false;
        }
    }

    public Result confirmClassroomBookAuditNotice(Integer id, Integer userId) {
        ClassroomBookAuditNoticeCriteria example = new ClassroomBookAuditNoticeCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andIdEqualTo(id)
                .andToUserIdEqualTo(userId);
        List<ClassroomBookAuditNotice> auditNoticeList = classroomBookAuditNoticeMapper.selectByExample(example);
        if (auditNoticeList.size() == 0) {
            return new Result(Result.CUSTOM_MESSAGE, "该通知不存在");
        }

        ClassroomBookAuditNotice classroomBookAuditNotice = auditNoticeList.get(0);
        if (classroomBookAuditNotice.getIsConfirm() == 1) {
            return new Result(Result.CUSTOM_MESSAGE, "请勿重复操作", null);
        }
        if (classroomBookAuditNotice.getToUserId().intValue() != userId.intValue()) {
            return new Result(Result.CUSTOM_MESSAGE, "没有权限", null);
        }
        classroomBookAuditNotice.setIsConfirm((short) 1);
        classroomBookAuditNotice.setConfirmTime(new Date());
        classroomBookAuditNoticeMapper.updateByPrimaryKeySelective(classroomBookAuditNotice);
        return new Result(Result.SUCCESS, "成功");
    }

    public long countByExample(ClassroomBookAuditCriteria example) {
        return classroomBookAuditMapper.countByExample(example);
    }
}
