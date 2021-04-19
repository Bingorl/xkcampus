/**
 * <p>Title: PasswordUtil.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: biu</p>
 *
 * @author King
 * @date 2016年10月13日
 * @version 1.0
 */
package com.biu.wifi.campus.Tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Title: PasswordUtil</p>
 * <p>Description:密码工具类 </p>
 * <p>Company: biu</p> 
 * @author King
 * @date 2016年10月13日
 */

public class PasswordUtil {

    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    /**
     *
     * <p>Title: MD5</p>
     * <p>Description:将明文密码进行MD5加密 </p>
     * @param password
     * @return String[] 0是私钥  1是加密密码
     * @author King
     * @throws NoSuchAlgorithmException
     * @date 2016年10月13日
     */
    public static String[] MD5(String password) throws NoSuchAlgorithmException {
        String temp = RandomUtil.randomNumber(1, 10);
        StringBuffer sb = new StringBuffer();
        sb.append(temp.substring(0, 5));
        sb.append(password);
        sb.append(temp.substring(5, 10));
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String finalPassword = byteToString(messageDigest.digest(sb.toString().getBytes()));
        String[] result = new String[2];
        result[0] = temp;
        result[1] = finalPassword;
        return result;
    }

    /**
     *
     * <p>Title: checkPassword</p>
     * <p>Description:验证MD5密码是否正确 </p>
     * @param oldPassword  用户输入的密码
     * @param key 私钥
     * @param password  数据库存储的密码
     * @return
     * @throws NoSuchAlgorithmException
     * @author King
     * @date 2016年10月13日
     */
    public static boolean checkPassword(String oldPassword, String key, String password) throws NoSuchAlgorithmException {
        boolean temp = false;
        StringBuffer sb = new StringBuffer();
        sb.append(key.substring(0, 5));
        sb.append(oldPassword);
        sb.append(key.substring(5, 10));
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String finalPassword = byteToString(messageDigest.digest(sb.toString().getBytes()));
        if (finalPassword.equals(password)) {
            temp = true;
        }
        return temp;
    }


    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    public static String md5(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String sig = byteToString(messageDigest.digest(str.getBytes()));
        return sig;
    }
}
