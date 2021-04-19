package com.biu.wifi.campus.daoEx;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface XiaoXinMapperEx {

    //获取群分享界面管理员列表
    List<Map<String, Object>> app_findShareGroupUserList(@Param("groupId") Integer group_id);

    //选择@的人----获取粉丝列表
    List<Map<String, Object>> user_selectAtUserList(@Param("userId") Integer user_id);

    //选择@的人----获取全校用户
    List<Map<String, Object>> user_selectAtUserListForSchool(@Param("userId") Integer user_id, @Param("search") String search, @Param("schoolId") Integer school_id);

    //根据班级获取学生信息-----未加入群组的
    List<Map<String, Object>> user_findStudentList(@Param("userId") Integer user_id, @Param("classId") Integer class_id, @Param("groupId") Integer group_id,
                                                   @Param("instituteId") Integer institute_id, @Param("gradeId") Integer grade_id, @Param("majorId") Integer major_id);

    //校信搜索联系人列表(私信过的人)
    List<Map<String, Object>> findChatUserFromXiaoXin(@Param("userId") Integer userId, @Param("search") String search);

    //校信搜索群组列表
    List<Map<String, Object>> findGroupUserFromXiaoXin(@Param("userId") Integer userId, @Param("search") String search);

    //获取私信列表  isFollow 是否关注  1是2否
    List<Map<String, Object>> user_findChatList(@Param("userId") Integer userId, @Param("isFollow") Short isFollow, @Param("time") String time);

    //获取群组列表
    List<Map<String, Object>> user_findGroupList(@Param("userId") Integer userId, @Param("time") String time);

    //获取前9位加入的成员列表
    List<Map<String, Object>> findNineMemberList(@Param("groupId") Integer groupId);

    //获取群成员列表
    List<Map<String, Object>> user_findGroupMemberList(@Param("groupId") Integer groupId, @Param("search") String search);

    //搜索校内学生------邀请成员入群
    List<Map<String, Object>> user_findStudentBySchool(@Param("userId") Integer userId, @Param("groupId") Integer groupId, @Param("search") String search);

    //批量新增消息------申请加群
    int insertAllApplyAdd(@Param("groupId") Integer groupId, @Param("userId") Integer userId, @Param("user") String user, @Param("list") List<Map<String, Object>> groupManagerList);

    //批量新增消息------退出群组
    int insertAllOutGroup(@Param("groupId") Integer groupId, @Param("userId") Integer userId, @Param("user") String user, @Param("list") List<Map<String, Object>> groupManagerList);

    //获取群通知列表
    List<Map<String, Object>> user_findGroupMsgList(@Param("userId") Integer userId, @Param("time") String time);

    //批量新增群通知消息------解散群组
    void insertAllDeleteGroup(@Param("groupId") Integer groupId, @Param("userId") Integer userId, @Param("user") String user, @Param("list") List<Map<String, Object>> groupUserList);

    //根据多个id获取用户信息
    List<Map<String, Object>> findUserByIds(@Param("array") String[] ids);

}
