package com.biu.wifi.campus.Tool;

import com.biu.wifi.core.util.ServletUtilsEx;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaseUtil {

    public static String getService_PIC_Name(String pid, HttpServletRequest request) {
        if (StringUtils.isNotBlank(pid))
            return ServletUtilsEx.getHostURL(request) + "version/downloadFile.do?id=" + pid;
        else
            return "";
    }

    public static String DateToStr(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public static String DateToStrAll(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public static Date StrToDate(String ttr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(ttr);
    }

    public static Date StrToDateAll(String ttr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(ttr);
    }

    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String returnDate(Date lasttime) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currenttemp = df.parse(df.format(new Date()));
        long between = Math.abs((currenttemp.getTime() - lasttime.getTime()) / 1000);// 除以1000是为了转换成秒
        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between;
        if (day1 != 0) {
            // 直接显示日期
            return day1 + "天前";
        } else {
            if (hour1 != 0) {
                return hour1 + "小时前";
            } else {
                if (minute1 != 0) {
                    return minute1 + "分钟前";
                } else {
                    if (second1 != 0) {
                        return second1 + "秒前";
                    } else {
                        return "刚刚";
                    }
                }
            }
        }
    }

    public static Date getNextDay(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        date = calendar.getTime();
        return date;
    }

    // 解密  
    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 处理emoji表情
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (source == null || source.length() < 1) {
            return source;
        }

        if (!containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }

        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return "";
        } else {
            if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    //判别是否包含Emoji表情  
    private static boolean containsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    //格式化时间 转json
    public static String fDate(Object o) {
        JsonConfig jf = new JsonConfig();
        jf.registerJsonValueProcessor(java.sql.Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        jf.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        jf.registerJsonValueProcessor(java.sql.Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        jf.registerJsonValueProcessor(java.sql.Time.class, new DateJsonValueProcessor("HH:mm:ss"));
        return JSONArray.fromObject(o, jf).toString();
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }
    /** end */
}
