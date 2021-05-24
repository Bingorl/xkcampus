package com.biu.wifi.campus.Tool;

import java.math.BigDecimal;

/**
 * @Author MCX
 * @Version 1.0
 **/

    public class CurrencyUtil {
        public CurrencyUtil() {
        }

        public static String numToStr(int num) {
            String[] chinese = new String[]{"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万"};
            String[] numChinese = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
            String str = String.valueOf(num);
            char[] chars = str.toCharArray();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < chars.length; ++i) {
                sb.append(numChinese[Integer.valueOf(String.valueOf(chars[i])).intValue()]).append(chinese[chars.length - i - 1]);
            }

            return sb.toString();
        }

        public static String numToLocalStr(int num) {
            String[] chinese = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万"};
            String[] numChinese = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
            String str = String.valueOf(num);
            char[] chars = str.toCharArray();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < chars.length; ++i) {
                sb.append(numChinese[Integer.valueOf(String.valueOf(chars[i])).intValue()]).append(chinese[chars.length - i - 1]);
            }

            return sb.toString() + "元整";
        }

        public static String bigDecimalToLocalStr(BigDecimal bigDecimal) {
            String[] chinese = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万"};
            String[] numChinese = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
            String[] afterChinese = new String[]{"角", "分"};
            String str = String.valueOf(bigDecimal);
            String[] arr = str.split("\\.");
            char[] chars = arr[0].toCharArray();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < chars.length; ++i) {
                sb.append(numChinese[Integer.valueOf(String.valueOf(chars[i])).intValue()]).append(chinese[chars.length - i - 1]);
            }

            if (arr.length == 1) {
                return sb.toString() + "元整";
            } else if (arr[1].length() > 2) {
                throw new IllegalArgumentException("人民币大写转换BigDecimal只能保留2位小数");
            } else {
                sb.append("元");
                char[] chars1 = arr[1].toCharArray();

                for(int i = 0; i < chars1.length; ++i) {
                    sb.append(numChinese[Integer.valueOf(String.valueOf(chars1[i])).intValue()]).append(afterChinese[i]);
                }

                return sb.toString();
            }
        }

        public static void main(String[] args) {
            int num = 1234;
            System.out.println(numToStr(num));
            System.out.println(numToLocalStr(num));

            BigDecimal bigDecimal = new BigDecimal("3421234.56");
            System.out.println(bigDecimalToLocalStr(bigDecimal));
        }
    }

