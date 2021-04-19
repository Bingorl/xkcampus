package com.biu.wifi.campus.Tool;

import java.util.Random;

public class RandomUtil {

    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    /**
     * @param type   0纯数字  1数字和字母组合
     * @param number 产生随机数的长度
     * @return String
     * @author ycf
     * @see 生成随机数
     */
    public static String randomNumber(int type, int number) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        if (type == 0) {
            for (int i = 0; i < number; i++) {
                sb.append(strDigits[random.nextInt(10)]);
            }
        } else {
            for (int i = 0; i < number; i++) {
                sb.append(strDigits[random.nextInt(16)]);
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(strDigits[random.nextInt(16)]);
        }
        System.out.println(sb.toString());
    }
}
