package com.biu.wifi.campus.Tool;


import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import com.biu.wifi.campus.result.Result;

import net.sf.json.JSONArray;


public class JsonUtilEx {
    /**
     * @param @param  o
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: strToWebJson
     * @Description: TODO(返回页面请求的数据json格式)
     * @author 韩建东
     * @date 2015年5月14日 下午11:23:58
     */
    public static String strToWebJson(Object o) {
        String fDate = BaseUtil.fDate(o);
        return StringUtilEx.chinaToUnicode(fDate);
    }

    /**
     * @param @param  result
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: strToBaseJson
     * @Description: TODO 返回result判断是否成功(约定0是成功,100是异常)
     * @author 韩建东
     * @date 2015年5月14日 下午7:55:55
     */
    public static String strToBaseJson(Result result) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(result);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"Result\":" + (jsonArray + "").substring(1, (jsonArray + "").length() - 1) + "}");
        return StringUtilEx.chinaToUnicode(sb.toString());
    }

    /**
     * @param @param  result
     * @param @param  o
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: strToMoblieJson
     * @Description: TODO 返回手机端result带对象的
     * @author 韩建东
     * @date 2015年5月14日 下午7:56:54
     */
    public static String strToMoblieJson(Result result) {
//		JSONArray jsonArray = new JSONArray();
//		jsonArray.add(result);
        String fDate = BaseUtil.fDate(result);
        StringBuffer sb = new StringBuffer();
        sb.append(fDate.substring(1, fDate.length() - 1));
        return sb.toString();
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)
            throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);
        return obj;
    }

    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null)
            return null;
        return new BeanMap(obj);
    }
}
