package com.biu.wifi.campus.Tool;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhangbin.
 * @date 2018/10/19.
 */
public class DbfReaderUtil {

    public static void main(String[] args) {
        DbfReaderUtil dbfReaderUtil = new DbfReaderUtil();
        File file = new File("C:\\Users\\zhangbin\\Desktop\\student_sum_all.dbf");
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            dbfReaderUtil.readDbf(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> readDbf(InputStream inputStream) {
        List<String[]> list = new LinkedList<String[]>();
        String[] array;
        try {
            DBFReader dbfReader = new DBFReader(inputStream);
            dbfReader.setCharactersetName("GBK");

            int fieldCount = dbfReader.getFieldCount();// 读取字段个数
            int rowCount = dbfReader.getRecordCount();// 获取有多少条记录

            System.out.println("有" + rowCount + "条记录");
            Object[] rowObjects;
            while ((rowObjects = dbfReader.nextRecord()) != null) {
                array = new String[rowObjects.length];// array为新dbf每一行的所有值数组。
                for (int i = 0; i < fieldCount; i++) {
                    String s = rowObjects[i].toString().trim();
                    if (s.equals("") || s == null || "null".equals(s)) {
                        array[i] = "";
                    } else {
                        array[i] = s;
                    }
                }
                list.add(array);
            }

            return list;
        } catch (DBFException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
