package com.biu.wifi.campus.Tool;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private StringUtil() {
    }

    public static final String EMPTY_STRING = "";

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private final static String[] chineseNumerals = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private static Random random = new Random();

    //随机字符数组
    private static char[] charSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    /*随机数字*/
    private static int[] intSequence = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }

    /**
     * 获取随机字符
     *
     * @return
     */
    public static String getRandomChar() {
        int index = random.nextInt(charSequence.length);
        return String.valueOf(charSequence[index]);
    }

    /**
     * 获取随机n位字符
     *
     * @return
     */
    //生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数  
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字  
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 获取随机数字
     *
     * @return
     */
    public static int getRandomInt() {
        int index = random.nextInt(intSequence.length);
        return intSequence[index];
    }


    /**
     * 判断一个字符串是否为空，null也会返回true
     *
     * @param str 需要判断的字符串
     * @return 是否为空，null也会返回true
     */
    public static boolean isBlank(String str) {
        return null == str || "".equals(str.trim());
    }

    /**
     * 判断一个字符串是否不为空
     *
     * @param str 需要判断的字符串
     * @return 是否为空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断一组字符串是否有空值
     *
     * @param strs 需要判断的一组字符串
     * @return 判断结果，只要其中一个字符串为null或者为空，就返回true
     */
    public static boolean hasBlank(String... strs) {
        if (null == strs || 0 == strs.length) {
            return true;
        } else {
            //这种代码如果用java8就会很优雅了
            for (String str : strs) {
                if (isBlank(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static String mapToXmlFile(Map map) {
        Document document = DocumentHelper.createDocument();
        Element nodeElement = document.addElement("node");
        for (Object obj : map.keySet()) {
            Element keyElement = nodeElement.addElement("key");
            keyElement.addAttribute("label", String.valueOf(obj));
            keyElement.setText(String.valueOf(map.get(obj)));
        }
        return doc2String(document);
    }

    @SuppressWarnings("rawtypes")
    public static String mapToXmlStr(Map map) {
        StringBuilder sb = new StringBuilder("<xml>");
        for (Object obj : map.keySet()) {
            sb.append("<" + String.valueOf(obj) + ">").append(map.get(obj)).append("</" + String.valueOf(obj) + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * @param document
     * @return
     */
    public static String doc2String(Document document) {
        String s = "";
        try {
            // 使用输出流来进行转化
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用UTF-8编码
            OutputFormat format = new OutputFormat("   ", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            s = out.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return s;
    }

    /**
     * json to xml {"node":{"key":{"@label":"key1","#text":"value1"}}} conver
     * <o><node class="object"><key class="object"
     * label="key1">value1</key></node></o>
     *
     * @param json
     * @return
     */
/*    public static String jsontoXml(String json) {
        try {
            XMLSerializer serializer = new XMLSerializer();
            JSON jsonObject = JSONSerializer.toJSON(json);
            return serializer.write(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     * xml to map xml <node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node>
     *
     * @param xml
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map xmltoMap(String xml) {
        try {
            Map map = new HashMap();
            Document document = DocumentHelper.parseText(xml);
            Element nodeElement = document.getRootElement();
            List node = nodeElement.elements();
            for (Iterator it = node.iterator(); it.hasNext(); ) {
                Element elm = (Element) it.next();
                map.put(elm.attributeValue("label"), elm.getText());
                elm = null;
            }
            node = null;
            nodeElement = null;
            document = null;
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to list xml <nodes><node><key label="key1">value1</key><key
     * label="key2">value2</key>......</node><node><key
     * label="key1">value1</key><key
     * label="key2">value2</key>......</node></nodes>
     *
     * @param xml
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List xmltoList(String xml) {
        try {
            List<Map> list = new ArrayList<Map>();
            Document document = DocumentHelper.parseText(xml);
            Element nodesElement = document.getRootElement();
            List nodes = nodesElement.elements();
            for (Iterator its = nodes.iterator(); its.hasNext(); ) {
                Element nodeElement = (Element) its.next();
                Map map = xmltoMap(nodeElement.asXML());
                list.add(map);
                map = null;
            }
            nodes = null;
            nodesElement = null;
            document = null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * xml to json <node><key label="key1">value1</key></node> 转化为
     * {"key":{"@label":"key1","#text":"value1"}}
     *
     * @param xml
     * @return
     */
/*    public static String xmltoJson(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.read(xml).toString();
    }*/
    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    public static Double DoubleTo2(Double d) {
        String format = new DecimalFormat("#.00").format(d);
        return Double.valueOf(format);
    }

    /**
     * 取两个Double之间的最大值
     *
     * @param d1
     * @param d2
     */
    public static Double compareTo(Double d1, Double d2) {
        Double t = d1.compareTo(d2) >= 0 ? d1 : d2;
        return t;
    }

    /**
     * 返回 已经过了多少时间
     *
     * @param bean
     * @return
     * @throws ParseException
     */
    public static String returnOverDate(String time) throws Exception {
        Date lasttime = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lasttime = df.parse(time);
        Date currenttemp = df.parse(df.format(new Date()));
        long between = Math.abs((currenttemp.getTime() - lasttime.getTime()) / 1000);// 除以1000是为了转换成秒
        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
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
                    return "1分钟前";
                }
            }
        }
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr = htmlStr.replaceAll("\r", "");
        htmlStr = htmlStr.replaceAll("\t", "");
        htmlStr = htmlStr.replaceAll("\n", "");
        return htmlStr.trim(); //返回文本字符串
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)
            throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * 阿拉伯数字转中文数字(暂时只支持两位)
     *
     * @param number 阿拉伯数字
     * @return
     */
    public static String numberFormat(int number) {
        String str = String.valueOf(number);
        char[] array = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        if (array.length == 1) {
            //一位数字
            return chineseNumerals[Integer.valueOf(array[0] + "")];
        } else if (array.length == 2) {
            //两位数字
            sb.append(chineseNumerals[Integer.valueOf(array[0] + "")])
                    .append("十")
                    .append(chineseNumerals[Integer.valueOf(array[1] + "")]);
        }
        return sb.toString();
    }

    public static List<Integer> stringArrayToIntegerList(String[] arrays) {
        if (arrays == null)
            return new ArrayList<>();

        if (arrays.length == 0)
            return new ArrayList<>();

        List<Integer> list = new ArrayList<>();
        for (String str : arrays)
            list.add(Integer.valueOf(str));

        return list;
    }

    public static boolean isUrl(String url) {
        if (StringUtils.isBlank(url))
            return false;

        if (url.startsWith("http://"))
            return true;
        else if (url.startsWith("https://"))
            return true;
        else
            return false;
    }
}
