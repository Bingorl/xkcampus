/**
 * <p>Title: TimeUtil.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: biu</p>
 *
 * @author King
 * @date 2016年10月11日
 * @version 1.0
 */
package com.biu.wifi.campus.Tool;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Title: TimeUtil</p>
 * <p>Description:时间工具类 </p>
 * <p>Company: biu</p>
 *
 * @author King
 * @date 2016年10月11日
 */

public class TimeUtil {

    private static Logger log = Logger.getLogger(TimeUtil.class);

    /**
     * 完整的年月日时分秒
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 完整的年月日
     */
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * <p>Title: getNowTime</p>
     * <p>Description:获取当前系统时间 </p>
     *
     * @return 10位的秒级别
     * @author King
     * @date 2016年10月12日
     */
    public static long getNowTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * <p>Title: getBeginTimeForDay</p>
     * <p>Description:获取一天的开始时间 </p>
     *
     * @param time 10位长度，秒级别
     * @return 10位长度，秒级别
     * @throws ParseException
     * @author King
     * @date 2016年10月12日
     */
    public static long getBeginTimeForDay(Long time) {
        Date date = null;
        if (null == time || 0 == time.longValue()) {
            date = new Date();
        } else {
            date = new Date(time * 1000);
        }
        StringBuffer first = new StringBuffer().append(df.format(date)).append(" 00:00:00");
        long beginTime;
        try {
            beginTime = sdf.parse(first.toString()).getTime() / 1000;
        } catch (ParseException e) {
            log.error("时间格式化错误：getBeginTimeForDay" + time);
            return 0;
        }
        return beginTime;
    }

    /**
     * <p>Title: getEndTimeForDay</p>
     * <p>Description:获取一天的结束时间  </p>
     *
     * @param time 10位长度，秒级别
     * @return 10位长度，秒级别
     * @throws ParseException
     * @author King
     * @date 2016年10月12日
     */
    public static long getEndTimeForDay(Long time) {
        Date date = null;
        if (null == time || 0 == time.longValue()) {
            date = new Date();
        } else {
            date = new Date(time * 1000);
        }
        StringBuffer end = new StringBuffer().append(df.format(date)).append(" 23:59:59");
        long beginTime;
        try {
            beginTime = sdf.parse(end.toString()).getTime() / 1000;
        } catch (ParseException e) {
            log.error("时间格式化错误：getEndTimeForDay" + time);
            return 0;
        }
        return beginTime;
    }

    /**
     * <p>Title: getDay</p>
     * <p>Description:根据时间获取是周几 </p>
     *
     * @param time
     * @return
     * @author King
     * @date 2016年10月19日
     */
    public static Integer getDay(long time) {
        if (time == 0) {
            time = getNowTime();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        int number = calendar.get(Calendar.DAY_OF_WEEK);
        if (number == 1) {
            //周日返回7
        } else {
            number = number - 1;
        }
        return number;
    }

    /**
     * <p>Title: getDay</p>
     * <p>Description:根据时间获取是周几 </p>
     *
     * @param date
     * @return
     * @author King
     * @date 2016年10月19日
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == 1) {
            return 7;
        } else {
            return week - 1;
        }
    }

    /**
     * <p>Title: getDay</p>
     * <p>Description:根据时间获取是周几 </p>
     *
     * @param date
     * @return
     * @author King
     * @date 2016年10月19日
     */
    public static String getDayStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String e = sdf.format(date);
        String number;
        if ("星期一".equals(e)) {
            number = "周一";
        } else if ("星期二".equals(e)) {
            number = "周二";
        } else if ("星期三".equals(e)) {
            number = "周三";
        } else if ("星期四".equals(e)) {
            number = "周四";
        } else if ("星期五".equals(e)) {
            number = "周五";
        } else if ("星期六".equals(e)) {
            number = "周六";
        } else {
            number = "周日";
        }
        return number;
    }

    /**
     * 获取两日期相差的天数
     *
     * @param date1 开始日期
     * @param date2 结束日期
     * @return
     */
    public static int getDayDiff(Date date1, Date date2) {
        long diff = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
        return Integer.valueOf(String.valueOf(diff)).intValue();
    }

    /**
     * 获取两日期相差的天数
     *
     * @param date1 开始日期
     * @param date2 结束日期
     * @return
     */
    public static int getDayDiff(String date1, String date2) {
        try {
            Date d1 = DateUtils.parseDate(date1, new String[]{"yyyy-MM-dd"});
            Date d2 = DateUtils.parseDate(date2, new String[]{"yyyy-MM-dd"});

            long diff = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);
            return Integer.valueOf(String.valueOf(diff)).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 日期格式化成字符串
     *
     * @param date 日期
     * @param fmt  格式
     * @return
     */
    public static String format(Date date, String fmt) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        return sdf.format(date);
    }

    /**
     * 给指定日期增加或减少天数
     *
     * @param date 指定日期
     * @param days 增加或减少的天数
     * @return
     */
    public static String addDays(String date, int days) {
        try {
            Date d = DateUtils.parseDate(date, new String[]{"yyyy-MM-dd"});
            Date d2 = DateUtils.addDays(d, days);
            return format(d2, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取一段时间内的星期集合
     *
     * @param start         开始时间
     * @param end           结束时间
     * @param plan          天数间隔
     * @param excludeSatSun 是否排除周六周日
     * @return
     */
    public static String getQXSet(Date start, Date end, int plan, boolean excludeSatSun) {
        Set<Integer> set = new HashSet<>();
        int m;
        for (int i = 0; i < plan; i++) {
            m = getDay(start);
            start = DateUtils.addDays(start, 1);
            if (excludeSatSun && (m == 6 || m == 7)) {
                //排除周六和周日
                continue;
            }
            set.add(m);
            if (start.compareTo(end) > 0) {
                //超过结束时间停止循环
                break;
            }
        }
        List<Integer> list = new ArrayList(set);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static List<Date> getDateList(String shortDateStr, String timeStr) {
        List<Date> dateList = new ArrayList<>();
        //筛选日期时间
        List<String> timeList = Arrays.asList(timeStr.split(","));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (String time : timeList) {
            try {
                Date date = sdf.parse(shortDateStr + " " + time);
                dateList.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("上课时间解析错误：{}", e.getCause());
            }
        }
        return dateList;
    }

    public static void main(String[] args) {
    }
}
