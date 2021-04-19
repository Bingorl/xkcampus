package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.JsonUtilEx;
import com.biu.wifi.campus.dao.CetScoreMapper;
import com.biu.wifi.campus.dao.StudentGpaMapper;
import com.biu.wifi.campus.dao.model.*;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.campus.service.*;
import com.biu.wifi.campus.vo.Term;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.core.support.cache.CacheSupport;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 教务控制器
 *
 * @author zhangbin.
 * @date 2018/11/5.
 */
@Controller
public class AppJwController extends AuthenticatorController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JwCjcxService jwCjcxService;
    @Autowired
    private CetService cetService;
    @Autowired
    private UserService userService;
    @Autowired
    private CetScoreMapper cetScoreMapper;
    @Autowired
    private FileSupportService fileSupportService;
    @Autowired
    private StudentGpaMapper studentGpaMapper;
    @Autowired
    private TeachingWeekService teachingWeekService;
    @Autowired
    private GradeService gradeService;

    /**
     * 老师查询空课时间
     *
     * @param userId
     * @param skzc     上课周次
     * @param skxq     上课星期
     * @param response
     */
    @RequestMapping("app_findKKPlan")
    public void app_findKKPlan(@ModelAttribute("user_id") Integer userId,
                               String skzc, String skxq, HttpServletResponse response) {
        if (skzc == null || skxq == null) {
            ServletUtilsEx.renderJson(response, new Result(Result.REQUIRED, "必要参数为空"));
            return;
        }
        User user = userService.getById(userId);

        if (user.getIsTeacher().intValue() == 2) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "学生无法使用此项功能"));
            return;
        }

        if (jwCjcxService.validStuNo(user.getSchoolId(), user.getStuNumber())) {
            if (!Arrays.asList("1662410118", "1562410214", "1762910220", "1762310127", "187876724").contains(user.getStuNumber())) {
                // TODO: 2019/2/27 测试账号不进行校验
                ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "工号有误，无法进行空课时间查询，请检查更正"));
                return;
            }
        }

        //查询学年学期
        List<TeachingWeek> teachingWeekList = teachingWeekService.findList(user.getSchoolId(), null);
        if (CollectionUtils.isEmpty(teachingWeekList)) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "学校暂未设置学年学期信息"));
            return;
        }

        String zxjxjhh = teachingWeekList.get(0).getTermCode();
        Map data = jwCjcxService.findKKPlan(zxjxjhh, user.getStuNumber(), Arrays.asList(Integer.valueOf(skzc)), skxq);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 课程表
     *
     * @param userId
     * @param currentWeek 当前星期
     * @param response
     */
    @RequestMapping("app_getKcb")
    public void app_getKcb(@ModelAttribute("user_id") Integer userId,
                           @RequestParam(defaultValue = "", required = false) String currentWeek, HttpServletResponse response) {
        User user = userService.getById(userId);

        //查询缓存
        Map data = null;//CacheSupport.get("kcbCache", user.getStuNumber(), Map.class);
        if (data != null) {
            String cacheKey = user.getStuNumber() + currentWeek;
            data = (Map) data.get(cacheKey);
            if (data != null) {
                logger.info("课程表查询，命中缓存，学号为{}", user.getStuNumber());
                ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
                return;
            }
        }

        //查询学年学期
        List<TeachingWeek> teachingWeekList = teachingWeekService.findList(user.getSchoolId(), null);
        if (CollectionUtils.isEmpty(teachingWeekList)) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "学校暂未设置学年学期信息"));
            return;
        }

        //执行教学计划号
        TeachingWeek teachingWeek = teachingWeekList.get(0);
        String zxjxjhh = teachingWeek.getTermCode();
        //解析学年学期
        //学年
        Grade grade = gradeService.find(user.getGradeId());
        String xn = zxjxjhh.substring(0, 9).split("-")[0];
        int xnxq = Integer.valueOf(xn) - Integer.valueOf(grade.getName());
        if (xnxq < 0) {
            ServletUtilsEx.renderJson(response, new Result(Result.CUSTOM_MESSAGE, "您的输入的年级不正确，请先更改年级"));
            return;
        } else if (xnxq < 1) {
            xn = "大一";
        } else if (xnxq < 2) {
            xn = "大二";
        } else if (xnxq < 3) {
            xn = "大三";
        } else {
            xn = "大四";
        }

        //学期
        String xq = zxjxjhh.substring(zxjxjhh.length() - 3, zxjxjhh.length() - 2);
        List<String> mondayList = new ArrayList<>();
        if (StringUtils.isNotBlank(teachingWeek.getMondayDate())) {
            mondayList = Arrays.asList(teachingWeek.getMondayDate().split(","));
        }

        //计算当前日期所在周次
        int skzc = 0;
        String thisMonday = "";
        String thisMonth = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(currentWeek)) {
            Date today = new Date();
            try {
                today = sdf.parse(sdf.format(today));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            thisMonth = today.getMonth() + 1 + "";
            for (int i = 0; i < mondayList.size(); i++) {
                thisMonday = mondayList.get(i).toString();
                Date m, f;
                try {
                    m = sdf.parse(thisMonday);
                    f = DateUtils.addDays(m, 6);
                    if (today.compareTo(m) >= 0 && today.compareTo(f) <= 0) {
                        skzc = i + 1;
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            skzc = Integer.valueOf(currentWeek);
            thisMonday = mondayList.get(skzc - 1);
            try {
                thisMonth = sdf.parse(thisMonday).getMonth() + 1 + "";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //上课安排表
        List<Map<String, Object>> classPKList;
        if (user.getIsTeacher().intValue() == 1) {
            //教师的
            classPKList = jwCjcxService.findJSPKBList(zxjxjhh, user.getStuNumber(), Arrays.asList(skzc), "1,2,3,4,5,6,7");
        } else {
            //学生的
            classPKList = jwCjcxService.findXSKCBList(zxjxjhh, user.getStuNumber(), Arrays.asList(skzc), "1,2,3,4,5,6,7");
        }

        data = new HashMap();
        data.put("week", skzc);
        data.put("totalWeek", teachingWeek.getWeekCount());
        data.put("xn", xn);
        data.put("xq", "第" + xq + "学期");
        data.put("month", thisMonth);

        List<Map<String, Object>> kcMapList;

        if (CollectionUtils.isEmpty(classPKList)) {
            kcMapList = jwCjcxService.formatKcbData(false, thisMonday, new ArrayList<Map<String, Object>>());
            data.put("kcList", kcMapList);
            ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "没有课程信息", data));
            return;
        }

        if (user.getIsTeacher().intValue() == 1) {
            kcMapList = jwCjcxService.formatKcbData(true, thisMonday, classPKList);
        } else {
            kcMapList = jwCjcxService.formatKcbData(false, thisMonday, classPKList);
        }
        data.put("kcList", kcMapList);
        //加入缓存
        Map cacheMap = new HashedMap();
        cacheMap.put(user.getStuNumber() + currentWeek, data);
        CacheSupport.put("kcbCache", user.getStuNumber(), cacheMap);
        ServletUtilsEx.renderJson(response, new Result(Result.SUCCESS, "成功", data));
    }

    /**
     * 四六级成绩查询验证码
     *
     * @param exCardNum 15位准考证号
     * @param response
     */
    @RequestMapping("app_getAuthCode")
    public void app_getAuthCode(String exCardNum, HttpServletResponse response) {
        Map data = new HashMap();
        //根据准考证号查询数据库
        CetScore cetScore = cetScoreMapper.selectByPrimaryKey(exCardNum);
        if (cetScore != null) {
            data.put("type", "1");
            data.put("code", null);
            BeanMap beanMap = BeanMap.create(cetScore);
            Map grade = new HashMap();
            for (Object key : beanMap.keySet()) {
                grade.put(key, beanMap.get(key));
            }
            data.put("grade", grade);
        } else {
            //获取验证码
            Map code = cetService.getAuthCodeUrl();
            data.put("type", "2");
            data.put("grade", null);
            data.put("code", code);
        }

        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", data)));
    }

    /**
     * 四六级成绩查询
     *
     * @param schoolId  学校ID
     * @param exCardNum 15位准考证号
     * @param realName  姓名
     * @param authCode  验证码
     * @param sessionId 获取验证码时获得的sessionId
     * @param fileId    文件名称
     * @param response
     */
    @RequestMapping("app_cetQuery")
    public void app_cetQuery(Integer schoolId, String exCardNum, String realName, String authCode, String sessionId,
                             String fileId, HttpServletResponse response) {
        if (StringUtils.isBlank(exCardNum) || StringUtils.isBlank(realName) || StringUtils.isBlank(authCode) || StringUtils.isBlank(sessionId)
                || StringUtils.isBlank(fileId)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        if (exCardNum.length() != 15) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "请输入15位的准考证号", null)));
            return;
        }

        //返回HTML页面
        String page = cetService.getCetQueryHtml(exCardNum, realName, authCode, sessionId);
        //删除验证码文件
        fileSupportService.remove(fileId);

        /*
            通过返回的HTML页面，渲染出网页
        ServletUtilsEx.render(response, page, "text/html;charset=utf-8");*/
        //返回解析后的结果
        //查询到的成绩或者错误提示信息
        Result result = cetService.getCetResult(page, schoolId, exCardNum);
        ServletUtilsEx.renderText(response, JsonUtilEx.strToMoblieJson(result));
    }

    /**
     * 课程成绩查询
     *
     * @param user_id  用户ID
     * @param zxjxjhh  学年学期
     * @param response
     */
    @RequestMapping(value = "app_findKCCJList")
    public void app_findKCCJList(@ModelAttribute("user_id") Integer user_id, String zxjxjhh, HttpServletResponse response) {
        User user = userService.getById(user_id);
        if (user.getIsTeacher().intValue() == 1) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "教职工无法使用此功能", null)));
            return;
        }

        boolean flag = jwCjcxService.validStuNo(user.getSchoolId(), user.getStuNumber());

        if (Arrays.asList("1662410118", "1562410214", "1762910220", "1762310127", "187876724").contains(user.getStuNumber())) {
            // TODO: 2019/2/27 测试账号不进行校验
            flag = true;
        }
        if (!flag) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "学号有误，无法进行成绩查询，请检查更正", null)));
            return;
        }
        String xh = user.getStuNumber();

        //ios解析数据包
        List<Map<String, String>> zxjxjhhMapList = new ArrayList<>();
        //安卓解析数据包
        List<Term> termList = new ArrayList<>();

        if (StringUtils.isBlank(zxjxjhh)) {
            //获取学年学期列表
            zxjxjhhMapList = jwCjcxService.findZXJXJHHList(xh);
            //取最近的一个学年学期
            Map<String, String> zxjxjhhMap = zxjxjhhMapList.get(zxjxjhhMapList.size() - 1);
            zxjxjhh = new ArrayList<>(zxjxjhhMap.keySet()).get(0).toString();
        }

        for (Map<String, String> map : zxjxjhhMapList) {
            for (String key : map.keySet()) {
                termList.add(new Term(key, map.get(key)));
            }
        }

        List<Map<String, Object>> list = jwCjcxService.findKCCJList(zxjxjhh, xh);
        Map<String, Object> data = new HashMap<>();
        data.put("kccjList", list);
        data.put("zxjxjhhList", zxjxjhhMapList);
        data.put("zxjxjhhList2", termList);
        data.put("gpa", "--");
        data.put("majorRanking", "--");
        if (CollectionUtils.isEmpty(list)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "暂时没有成绩记录", data)));
            return;
        }

        //推优绩点和专业排名
        StudentGpaCriteria studentGpaEx = new StudentGpaCriteria();
        studentGpaEx.createCriteria()
                .andSchoolIdEqualTo(user.getSchoolId())
                .andStuNoEqualTo(user.getStuNumber());
        List<StudentGpa> studentGpaList = studentGpaMapper.selectByExample(studentGpaEx);
        if (CollectionUtils.isEmpty(studentGpaList)) {
            //计算推优绩点
            double gpa = jwCjcxService.getTYJD(jwCjcxService.queryKCCJList(zxjxjhh, xh));
            StudentGpa studentGpa = new StudentGpa();
            studentGpa.setStuNo(xh);
            studentGpa.setSchoolId(user.getSchoolId());
            studentGpa.setGpa(gpa);
            studentGpa.setCreateTime(new Date());
            studentGpaMapper.insertSelective(studentGpa);
            data.put("gpa", String.valueOf(gpa));
            data.put("majorRanking", "--");
        } else {
            data.put("gpa", studentGpaList.get(0).getGpa().toString());
            data.put("majorRanking", studentGpaList.get(0).getMajorRanking() != null ? studentGpaList.get(0).getMajorRanking().toString() : "--");
        }

        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", data)));
    }

//    /**
//     * 课程成绩查询
//     *
//     * @param user_id  用户ID
//     * @param zxjxjhh  学年学期
//     * @param xh       学号
//     * @param response
//     */
//    @RequestMapping(value = "app_findKCCJList")
//    public void app_findKCCJList(@ModelAttribute("user_id") Integer user_id, String zxjxjhh, String xh, HttpServletResponse response) {
//        if (StringUtils.isBlank(xh)) {
//            ServletUtilsEx.renderText(response,
//                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
//            return;
//        }
//
//        //TODO 注册的时候未接入教务系统，因此需要校验学生的学号是否正确，学号错误无法进行成绩查询
//        User user = userService.getById(user_id);
////        boolean flag = jwCjcxService.validStuNo(user.getSchoolId(), user.getStuNumber());
////        if (!flag) {
////            ServletUtilsEx.renderText(response,
////                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "学号有误，无法进行成绩查询，请检查更正", null)));
////            return;
////        }
//
//        List<Map<String, String>> zxjxjhhMapList = new ArrayList<>();
//        List<Term> termList = new ArrayList<>();
//        //ios解析数据包
//        Map<String, String> zxjxjhhMap = new HashMap<>();
//        zxjxjhhMap.put("2016-2017-1-1", "2016-2017学年  第一学期");
//        zxjxjhhMapList.add(zxjxjhhMap);
//        zxjxjhhMap = new HashMap<>();
//        zxjxjhhMap.put("2016-2017-2-1", "2016-2017学年  第二学期");
//        zxjxjhhMapList.add(zxjxjhhMap);
//        zxjxjhhMap = new HashMap<>();
//        zxjxjhhMap.put("2017-2018-1-1", "2017-2018学年  第一学期");
//        zxjxjhhMapList.add(zxjxjhhMap);
//        zxjxjhhMap = new HashMap<>();
//        zxjxjhhMap.put("2017-2018-2-1", "2017-2018学年  第二学期");
//        zxjxjhhMapList.add(zxjxjhhMap);
//
//        //安卓解析数据包
//        termList.add(new Term("2016-2017-1-1", "2016-2017学年  第一学期"));
//        termList.add(new Term("2016-2017-2-1", "2016-2017学年  第二学期"));
//        termList.add(new Term("2017-2018-1-1", "2017-2018学年  第一学期"));
//        termList.add(new Term("2017-2018-2-1", "2017-2018学年  第二学期"));
//
//
//        if (StringUtils.isBlank(zxjxjhh)) {
//            /*
//             todo 接入到教务系统后开放此代码，根据入学日期计算学年学期
//            //获取学年学期列表
//            zxjxjhhMap = jwCjcxService.findZXJXJHHList(xh);
//            List<String> keyList = new ArrayList<>(zxjxjhhMap.keySet());
//            //取最近的一个学年学期
//            Collections.sort(keyList, new Comparator<String>() {
//                @Override
//                public int compare(String o1, String o2) {
//                    return o2.compareTo(o1);
//                }
//            });
//            zxjxjhh = keyList.get(0);
//            */
//
//            zxjxjhh = "2017-2018-1-1";
//        }
//
//        //TODO 正式上线时，学号根据用户来确定，测试的时候用接口传参获取
//        List<Map<String, Object>> list = jwCjcxService.findKCCJList(zxjxjhh, xh);
//        if (CollectionUtils.isEmpty(list)) {
//            ServletUtilsEx.renderText(response,
//                    JsonUtilEx.strToMoblieJson(new Result(Result.CUSTOM_MESSAGE, "暂时没有成绩记录", null)));
//            return;
//        }
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("kccjList", list);
//        data.put("zxjxjhhList", zxjxjhhMapList);
//        data.put("zxjxjhhList2", termList);
//
//        //推优绩点和专业排名
//        StudentGpaCriteria studentGpaEx = new StudentGpaCriteria();
//        studentGpaEx.createCriteria()
//                .andSchoolIdEqualTo(user.getSchoolId())
//                .andStuNoEqualTo(user.getStuNumber());
//        List<StudentGpa> studentGpaList = studentGpaMapper.selectByExample(studentGpaEx);
//        if (CollectionUtils.isEmpty(studentGpaList)) {
//            //计算推优绩点
//            double gpa = jwCjcxService.getTYJD(jwCjcxService.queryKCCJList(zxjxjhh, xh));
//            StudentGpa studentGpa = new StudentGpa();
//            studentGpa.setStuNo(xh);
//            studentGpa.setSchoolId(user.getSchoolId());
//            studentGpa.setGpa(gpa);
//            studentGpa.setCreateTime(new Date());
//            studentGpaMapper.insertSelective(studentGpa);
//            data.put("gpa", String.valueOf(gpa));
//            data.put("majorRanking", "--");
//        } else {
//            data.put("gpa", studentGpaList.get(0).getGpa().toString());
//            data.put("majorRanking", studentGpaList.get(0).getMajorRanking() != null ? studentGpaList.get(0).getMajorRanking().toString() : "--");
//        }
//
//        ServletUtilsEx.renderText(response,
//                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", data)));
//    }

    /**
     * 学生学籍查询
     *
     * @param xm       姓名
     * @param xh       学号
     * @param response
     */
    @RequestMapping(value = "app_findXSXJList")
    public void app_findXSXJList(String xm, String xh, HttpServletResponse response) {
        if (StringUtils.isBlank(xh)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        List<Map<String, Object>> list = jwCjcxService.findXSXJList(xm, xh);
        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }

    /**
     * 教室列表
     *
     * @param xqh      校区号
     * @param jxlh     教学楼号
     * @param jash     教室号
     * @param response
     */
    @RequestMapping(value = "app_findJASBList")
    public void app_findJASBList(String xqh, String jxlh, String jash, HttpServletResponse response) {
        if (StringUtils.isBlank(xqh)) {
            ServletUtilsEx.renderText(response,
                    JsonUtilEx.strToMoblieJson(new Result(Result.REQUIRED, "必要参数为空", null)));
            return;
        }

        List<Map<String, Object>> list = jwCjcxService.findJASBList(xqh, jxlh, jash);
        ServletUtilsEx.renderText(response,
                JsonUtilEx.strToMoblieJson(new Result(Result.SUCCESS, "成功", list)));
    }
}
