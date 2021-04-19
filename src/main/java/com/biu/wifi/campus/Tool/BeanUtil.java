/**
 *
 */
package com.biu.wifi.campus.Tool;

import org.springframework.cglib.beans.BeanMap;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName: BeanUtil
 *
 * @author King
 * @Description: 对象方法工具类
 * @date 2016年10月9日
 */
public class BeanUtil {

    /**
     * <p>Title: isNull</p>
     * <p>Description:判断对象是否为空 </p>
     *
     * @param object
     * @return
     * @author King
     * @date 2016年10月14日
     */
    public static boolean isNull(Object object) {
        return null == object;
    }

    /**
     * <p>Title: isBlank</p>
     * <p>Description:判断字符串是否是空 </p>
     *
     * @param str
     * @return
     * @author King
     * @date 2016年10月14日
     */
    public static boolean isBlank(String str) {
        return null == str || "".equals(str.trim());
    }

    /**
     * 判断对象是否为空，如果为空，直接抛出异常
     *
     * @param object       需要检查的对象
     * @param errorMessage 异常信息
     * @return 非空的对象
     */
    public static Object requireNonNull(Object object, String errorMessage) {
        if (null == object) {
            throw new NullPointerException(errorMessage);
        }
        return object;
    }

    /**
     * <p>Title: isNullList</p>
     * <p>Description:判断list是否为空List </p>
     *
     * @param list
     * @return
     * @author King
     * @date 2016年10月12日
     */
    @SuppressWarnings("rawtypes")
    public static boolean isNullList(List list) {
        return list == null || list.size() == 0;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNullMap(Map map) {
        return null == map || map.size() == 0;
    }

    //生成项目批次号码
    public static String generateOrderId() {
        Calendar a = Calendar.getInstance();
        int month = a.get(Calendar.MONTH) + 1;//得到月份
        String key = "";
        if (month <= 6) {
            key = "01";
        } else {
            key = "02";
        }
        StringBuilder sRand = new StringBuilder(20);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);
        str = str.substring(0, 4) + key + str.substring(4);
        sRand.append(str);
        for (int i = 0; i < 6; i++) {
            // 取得一个随机数字
            String tmp = StringUtil.getRandomInt() + "";
            sRand.append(tmp);
        }
        return sRand.toString();
    }

    //生成订单号码
    public static String getOrderId() {
        StringBuilder sRand = new StringBuilder(20);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(date);
        sRand.append(str);
        for (int i = 0; i < 6; i++) {
            // 取得一个随机数字
            String tmp = StringUtil.getRandomInt() + "";
            sRand.append(tmp);
        }
        return sRand.toString();
    }

    public static Short getDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day == 0) {
            return Short.parseShort("7");
        } else {
            return Short.parseShort(day + "");
        }
    }

    /**
     * 判别字段中是否包含了emojicon表情以及过滤相关内容
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

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /**
     * 将bean转化为map
     *
     * @param bean bean对象
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean) {
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> map = new HashMap();
        map.putAll(beanMap);
        return map;
    }

    /**
     * 将bean转化为map
     *
     * @param bean         bean对象
     * @param extendParams 额外参数map
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean, Map<String, Object> extendParams) {
        Map<String, Object> map = beanToMap(bean);
        map.putAll(extendParams);
        return map;
    }

    /**
     * 将bean转化为map
     *
     * @param bean          bean对象
     * @param excludeParams 要排除的key
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean, String... excludeParams) {
        Map<String, Object> map = beanToMap(bean);
        return removeKeys(map, excludeParams);
    }


    /**
     * 将bean转化为map
     *
     * @param bean          bean对象
     * @param extendParams  额外参数map
     * @param excludeParams 要排除的key
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean, Map<String, Object> extendParams, String... excludeParams) {
        Map<String, Object> map = beanToMap(bean, extendParams);
        return removeKeys(map, excludeParams);
    }

    /**
     * 将map中的指定key移除
     *
     * @param map
     * @param excludeParams 要排除的key
     * @return
     */
    public static Map<String, Object> removeKeys(Map<String, Object> map, String... excludeParams) {
        List<String> keyList = Arrays.asList(excludeParams);
        for (String key : map.keySet()) {
            if (keyList.contains(key)) {
                map.remove(key);
            }
        }
        return map;
    }

    /**
     * 保留map中的指定key
     *
     * @param map
     * @param retainParams 要保留的key
     * @return
     */
    public static Map<String, Object> retainKeys(Map<String, Object> map, String... retainParams) {
        List<String> keyList = Arrays.asList(retainParams);
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            if (!keyList.contains(iterator.next())) {
                iterator.remove();
            }
        }
        return map;
    }

    public static <T> List<Map> beanListToMapList(List<T> list, String methodName, String... params) {
        List<Map> mapList = new ArrayList<>();
        for (Object object : list) {
            Map map = null;
            if ("retainKeys".equals(methodName)) {
                map = retainKeys(beanToMap(object), params);
            } else if ("removeKeys".equals(methodName)) {
                map = removeKeys(beanToMap(object), params);
            }

            if (map != null) {
                mapList.add(map);
            }
        }
        return mapList;
    }

    public static <T> List<Map> beanListToMapList(List<T> list) {
        List<Map> mapList = new ArrayList<>();
        for (Object object : list) {
            Map map = new HashMap();
            BeanMap beanMap = BeanMap.create(object);
            map.putAll(beanMap);
            mapList.add(map);
        }
        return mapList;
    }
}
