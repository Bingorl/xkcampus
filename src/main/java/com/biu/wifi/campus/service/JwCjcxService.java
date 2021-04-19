package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.DbfReaderUtil;
import com.biu.wifi.campus.Tool.TimeUtil;
import com.biu.wifi.campus.dao.model.JwCjcx;
import com.biu.wifi.campus.result.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Service
public class JwCjcxService extends JwService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ClassService classService;

    /**
     * 校验学号是否合法
     * <p>
     * 如果在学籍表中可以查询到，则是合法学号，返回true
     * 如果在学籍表中不可以查询到，则是非法学号，返回false
     *
     * @param schoolId 学校ID
     * @param stuNo    学号
     * @return
     */
    public boolean validStuNo(Integer schoolId, String stuNo) {
        List<Map<String, Object>> list = findXSXJList("", stuNo);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 导入所有学生成绩
     *
     * @param diskFileItem 上传的dbf类型的数据库文件
     * @param schoolId     学校ID
     * @return
     */
    public Result addByImportFromDbfFile(DiskFileItem diskFileItem, String schoolId) {
        // 读取上传的所有学生成绩表的dbf文件
        DbfReaderUtil readerUtil = new DbfReaderUtil();
        List<String[]> list;
        try {
            list = readerUtil.readDbf(diskFileItem.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(Result.CUSTOM_MESSAGE, "读取dbf文件失败", null);
        }

        // 将大的集合分割成若干个小的集合进行批量录入
        int len = 1000;
        int size = list.size();
        int count = (size + len - 1) / len;

        boolean success = true;

        for (int i = 0; i < count; i++) {
            List<String[]> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            StringBuffer sb = new StringBuffer();
            sb.append("insert into biu_jw_cjcx (school_id, real_name, sex,");
            sb.append("stu_no, id_type, id_no,");
            sb.append("edu_bg, edu_sys, enter_year,");
            sb.append("grade, institute_name, major_name,");
            sb.append("class_name, lan_type, is_apply,");
            sb.append("class_code, class_no, major_no,");
            sb.append("institute_no, exam_date, exam_score");
            sb.append(") values ");
            for (int j = 0; j < subList.size(); j++) {
                String[] arrayList = subList.get(j);
                sb.append("(");
                // 将第一个元素替换成学校ID
                arrayList[0] = schoolId;
                // 校验dbf数据库文件中的字段和数据库中的字段的数量是否相同，如果少于数据库的字段自动补空字符串
                String str = checkValues(arrayList);
                sb.append(str);
                sb.append(")");
                if (j != subList.size() - 1) {
                    sb.append(",");
                }
            }
            String sql = sb.toString();
            System.out.println(sql);
            try {
                jdbcTemplate.execute(sql);
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
                break;
            }
        }

        if (success) {
            return new Result(Result.SUCCESS, "成功", null);
        } else {
            return new Result(Result.FAILURE, "失败", null);
        }
    }

    /**
     * 校验导入的数据库文件的字段数量和数据库中的数据库字段数量是否相同 如果少于数据库中的数量则自动补空字符串
     *
     * @param array
     * @return
     */
    public String checkValues(String[] array) {
        // 如果dbf文件中获取的字段数少于表中字段数则补全
        Field[] fields = JwCjcx.class.getDeclaredFields();
        int fieldCount = fields.length;
        fieldCount -= 1;

        List<String> newArray = new ArrayList<>();
        newArray.addAll(Arrays.asList(array));

        int len = array.length;
        // 如果数据表中的字段数大于dbf文件中的字段数则多余的字段数补空格

        if (fieldCount > len) {
            int diff = fieldCount - len;
            for (int i = 0; i < diff; i++) {
                newArray.add("");
            }
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < newArray.size(); i++) {
            sb.append("'" + newArray.get(i) + "'");
            if (i != newArray.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }


    /**
     * 获取学年学期列表
     *
     * @param xh 学号
     * @return
     */
    public List<Map<String, String>> findZXJXJHHList(String xh) {
        List<Map<String, Object>> xjList = findXSXJList("", xh);
        if (CollectionUtils.isEmpty(xjList)) {
            return new ArrayList<>();
        }

        //获取入学日期
        String rxrq = xjList.get(0).get("RXRQ").toString().substring(0, 4);
        //获取当前日期
        Date date = new Date();
        String now = DateUtil.formatDate(date, "yyyy");

        int start = Integer.valueOf(rxrq).intValue();
        int end = Integer.valueOf(now).intValue();

        //学制
        //TODO 需要根据学校动态获取
        int termCount = 4;
        int countIndex = 0;

        Map<String, String> data;
        List<Map<String, String>> dataList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            //学年不能超过学制规定的学年
            if (countIndex >= termCount) {
                break;
            }
            int j = i + 1;
            data = new LinkedMap();
            data.put(i + "-" + j + "-1-1", i + "-" + j + "学年  第一学期");
            dataList.add(data);
            Date temp;
            try {
                //第二学期的成绩只有在学期结束后才能进行查询
                temp = DateUtils.parseDate(j + "-09-01", new String[]{"yyyy-MM-dd"});
            } catch (Exception e) {
                e.printStackTrace();
                temp = new Date();
            }
            if (date.compareTo(temp) == 1) {
                data = new LinkedMap();
                data.put(i + "-" + j + "-2-1", i + "-" + j + "学年  第二学期");
                dataList.add(data);
            }
            countIndex++;
        }
        return dataList;
    }

    /**
     * 课程成绩列表
     *
     * @param zxjxjhh 学年学期
     * @param xh      学号
     * @return
     */
    @Override
    public List<Map<String, Object>> findKCCJList(String zxjxjhh, String xh) {
        //考试成绩元数据，可能包含补考信息，即单门课程有多条成绩
        List<Map<String, Object>> metaDataList = super.queryKCCJList(zxjxjhh, xh);
        //去重集合
        Set<String> container = new HashSet<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        for (Map<String, Object> data : metaDataList) {
            //每次只添加第一条
            if (container.contains(data.get("KCM").toString())) {
                continue;
            }
            map = new HashedMap();
            map.put("ID", data.get("ID"));
            map.put("KCM", data.get("KCM"));
            map.put("KCH", data.get("KCH"));
            map.put("KCSXMC", data.get("KCSXMC"));
            map.put("XF", data.get("XF"));
            map.put("KCCJ", data.get("KCCJ"));
            map.put("DJM", data.get("DJM"));
            list.add(map);
            container.add(data.get("KCM").toString());
        }
        return list;
    }

    /**
     * 计算推优绩点
     * <p>
     * 计算方式：
     * 必修课按非重修的最高分认定计算
     * <p>
     * 加权计算规则
     * 绩点对应：
     * 分数          绩点
     * 59分及以下      0
     * 60~64          2.0
     * 65~69          2.5
     * 70~74          3.0
     * 75~79          3.5
     * 80~84          4.0
     * 85~89          4.5
     * 90及分以上      5.0
     * 例如：高等数学考试90分，6学分，绩点5.0
     * 通信原理考试与补考最高分50分，3学分，绩点0
     * 推优绩点:(5*6+0*3)/(3+6)=3.3
     *
     * @param list 课程成绩列表
     * @return
     */
    public double getTYJD(List<Map<String, Object>> list) {
        Map<String, Map<String, Object>> temp = new HashedMap();

        Iterator<Map<String, Object>> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            if (map.get("KCSXMC") != null && "选修".equals(map.get("KCSXMC").toString())) {
                //去掉选修课的成绩
                iterator.remove();
                continue;
            }

            if (map.get("XDFSMC") != null && "重修".equals(map.get("XDFSMC").toString())) {
                //去掉必修课重修的成绩
                iterator.remove();
                continue;
            }

            //按课程名称分组，保留最高分的纪录
            if (temp.containsKey(map.get("KCH").toString())) {
                //比较分数，保存较高的成绩
                double c1 = Double.valueOf(map.get("KCCJ").toString());
                double c2 = Double.valueOf(temp.get(map.get("KCH").toString()).get("KCCJ").toString());
                if (c1 > c2) {
                    temp.put(map.get("KCH").toString(), map);
                }
            } else {
                temp.put(map.get("KCH").toString(), map);
            }
        }

        //计算推优绩点
        double xfjd = 0, xf = 0;
        for (String key : temp.keySet()) {
            //学分绩点
            xfjd += Double.valueOf(temp.get(key).get("XF").toString()) * Double.valueOf(temp.get(key).get("JDCJ").toString());
            //学分
            xf += Double.valueOf(temp.get(key).get("XF").toString());
        }

        if (xf == 0) {
            xf = 1;
        }

        double result = xfjd / xf;
        BigDecimal bigDecimal = new BigDecimal(result);
        result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    /**
     * 学生学籍列表
     *
     * @param xm 姓名
     * @param xh 学号
     * @return
     */
    @Override
    public List<Map<String, Object>> findXSXJList(String xm, String xh) {
        return super.queryXSXJList(xm, xh);
    }

    /**
     * 教室列表
     *
     * @param xqh  校区号
     * @param jxlh 教学楼号
     * @param jash 教室号
     * @return
     */
    @Override
    public List<Map<String, Object>> findJASBList(String xqh, String jxlh, String jash) {
        return super.queryJASBList(xqh, jxlh, jash);
    }

    /**
     * 格式化课程表数据
     *
     * @param isTeacher   是否是教师
     * @param thisMonday  每周一的日期：yyyy-MM-dd
     * @param classPKList 排课列表元数据
     */
    public List<Map<String, Object>> formatKcbData(boolean isTeacher, String thisMonday, List<Map<String, Object>> classPKList) {
        List<Map<String, Object>> kcMapList = new ArrayList();

        //按周一到周日顺序排课
        for (int i = 1; i < 8; i++) {
            String day = TimeUtil.addDays(thisMonday, i - 1);
            List<Map<String, Object>> kcList = new ArrayList<>();
            Map<String, Object> kcMap = new HashMap<>();
            Iterator<Map<String, Object>> iterator = classPKList.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> tmp = iterator.next();
                if (String.valueOf(i).equals(tmp.get("SKXQ").toString())) {
                    Map<String, Object> a = new HashedMap();
                    a.put("ZXJXJHH", tmp.get("ZXJXJHH"));
                    a.put("KCH", tmp.get("KCH"));
                    a.put("KXH", tmp.get("KXH"));
                    a.put("KCM", tmp.get("KCM"));
                    a.put("SKJS", tmp.get("SKJS"));
                    a.put("JSH", tmp.get("JSH"));
                    a.put("SKJC", tmp.get("SKJC"));
                    a.put("CXJC", tmp.get("CXJC"));
                    a.put("JXLM", tmp.get("JXLM"));
                    a.put("JASH", tmp.get("JASH"));

                    if (isTeacher) {
                        //教职工添加班级
                        List<Map<String, Object>> classMapList = super.queryPKMXBJBList(tmp.get("ZXJXJHH").toString(),
                                tmp.get("KCH").toString(), tmp.get("KXH").toString(), null, null);
                        String className = "";
                        for (int j = 0; j < classMapList.size(); j++) {
                            // TODO 根据班级号查询班级信息表拿到班级名称,目前数据不完善，暂时显示班级号，等数据完善后显示班级名称
                            className += classMapList.get(j).get("BJH").toString();
                            if (j != classMapList.size() - 1) {
                                className += ",";
                            }

                            /*Class classz = classService.findByCode(classMapList.get(j).get("BJH").toString());
                            if (classz != null) {
                                className += classz.getName();
                                if (j != classMapList.size() - 1) {
                                    className += ",";
                                }
                            }*/
                        }
                        a.put("BJM", className);
                    }
                    kcList.add(a);
                    iterator.remove();
                }
            }

            String year = day.substring(0, 4) + "年";
            String month = day.substring(5, 7) + "月";
            String date = day.substring(8, 10) + "日";
            kcMap.put("year", year);
            kcMap.put("month", month);
            kcMap.put("date", date);
            kcMap.put("kcList", kcList);
            kcMapList.add(kcMap);
        }

        //每天按照上课节次升序排课
        for (Map<String, Object> kc : kcMapList) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) kc.get("kcList");
            Collections.sort(list, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Integer jc1 = Integer.valueOf(o1.get("SKJC").toString());
                    Integer jc2 = Integer.valueOf(o2.get("SKJC").toString());
                    return jc1.compareTo(jc2);
                }
            });
        }

        return kcMapList;
    }

    /**
     * 教师课程排课表
     *
     * @param zxjxjhh  学年学期
     * @param skzcList 上课周次
     * @param skxq     上课星期
     * @return
     */
    public List<Map<String, Object>> findJSPKBList(String zxjxjhh, String jsh, List<Integer> skzcList, String skxq) {
        return super.queryJSPKBList(zxjxjhh, skzcList, skxq, jsh, null);
    }

    /**
     * 学生课程表
     *
     * @param zxjxjhh  学年学期
     * @param skzcList 上课周次
     * @param skxq     上课星期
     * @return
     */
    public List<Map<String, Object>> findXSKCBList(String zxjxjhh, String xh, List<Integer> skzcList, String skxq) {
        List<Map<String, Object>> xsxkList = findXSXKList(zxjxjhh, xh);
        if (CollectionUtils.isEmpty(xsxkList)) {
            return xsxkList;
        }

        List<Map<String, Object>> paramList = new ArrayList<>();
        for (Map<String, Object> xk : xsxkList) {
            Map<String, Object> param = new HashedMap();
            param.put("KCH", xk.get("KCH"));
            param.put("KXH", xk.get("KXH"));
            paramList.add(param);
        }

        List<Map<String, Object>> pkList = findPKBList(zxjxjhh, skzcList, skxq, null, paramList);
        return pkList;
    }

    /**
     * @param zxjxjhh   学年学期
     * @param skzcList  上课周次
     * @param skxq      上课星期
     * @param jsh       教师号
     * @param paramList 课程号和课序号的key-value集合
     * @return
     */
    public List<Map<String, Object>> findPKBList(String zxjxjhh, List<Integer> skzcList, String skxq, String jsh, List<Map<String, Object>> paramList) {
        return super.queryJSPKBList(zxjxjhh, skzcList, skxq, jsh, paramList);
    }

    /**
     * 学生选课列表
     *
     * @param zxjxjhh 学期学年
     * @param xh      学号
     * @return
     */
    public List<Map<String, Object>> findXSXKList(String zxjxjhh, String xh) {
        return super.queryXSXKList(zxjxjhh, xh, null, null);
    }

    /**
     * 教师查看空课时间表
     * <p>
     * 功能：指定教师查看指定的上课时间点，所有学生的上课状态(有课/没课)
     *
     * @param zxjxjhh
     * @param jsh
     * @param skzcList
     * @param skxq
     * @return
     */
    public Map findKKPlan(String zxjxjhh, String jsh, List<Integer> skzcList, String skxq) {
        Map data = new HashedMap();
        //根据教师号查询任课的课程列表(课程号和课序号)
        List<Map<String, String>> kcMapList = new ArrayList<>();
        List<Map<String, Object>> kcList = super.queryJSPKBList(zxjxjhh, skzcList, skxq, jsh, null);
        for (Map<String, Object> kc : kcList) {
            Map<String, String> kcMap = new HashedMap();
            kcMap.put("KCH", kc.get("KCH").toString());
            kcMap.put("KXH", kc.get("KXH").toString());
            kcMap.put("KCM", kc.get("KCM").toString());
            kcMapList.add(kcMap);
        }
        data.put("kcMapList", kcMapList);

        //根据课程号和课序号查询上课的学生总数
        for (Map<String, String> kcMap : kcMapList) {
            //该门课程的学生列表
            List<Map<String, Object>> skxsList = findSKXSList(zxjxjhh, kcMap.get("KCH").toString(), kcMap.get("KXH").toString());
            int total = CollectionUtils.isNotEmpty(skxsList) ? skxsList.size() : 0;
            int count = 0;
            //有课的学生数量
            for (Map<String, Object> skxs : skxsList) {
                List<Map<String, Object>> xsskList = findXSKCBList(zxjxjhh, skxs.get("XH").toString(), skzcList, skxq);
                count = CollectionUtils.isNotEmpty(xsskList) ? xsskList.size() : 0;
            }
            kcMap.put("studentTotalCount", String.valueOf(total));
            kcMap.put("studentHasNoClassCount", String.valueOf(total - count));
        }
        return data;
    }

    /**
     * 老师查询上课班级的学生列表
     *
     * @param zxjxjhh
     * @param kch
     * @param kxh
     * @return
     */
    public List<Map<String, Object>> findSKXSList(String zxjxjhh, String kch, String kxh) {
        return super.queryXSXKList(zxjxjhh, null, kch, kxh);
    }
}
