package com.biu.wifi.campus.Tool;

import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * 通过excel表格数据生成创建表的sql
 *
 * @author zhangbin.
 * @date 2018/11/7.
 */
public class TableGenerator {

    public static void main(String[] args) throws Exception {
        String sql = "create table RW_DDWSXK_VIEW\n(\n`ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,\n";
        String[] fields = "ZXJXJHH, KCH, KXH, SKJS, KCM, JSH, SKZC, SKXQ, SKJC, CXJC, XQH, XQM, JXLH, JXLM, JASH, JASM, XSH, XSM, XSJC, RN".split(",");
        for (String field : fields) {
            if (StringUtils.isBlank(field)) {
                continue;
            }

            field = StringUtils.deleteWhitespace(field);
            sql += "`" + field + "` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '',\n";
        }
        sql += "PRIMARY KEY (`ID`) USING BTREE\n)";
        System.out.println(sql);
    }

    public static String createTableSql(String excelPath, String tableName) throws Exception {
        excelPath = "C:\\Users\\zhangbin\\Desktop\\test.xls";
        File file = new File(excelPath);
        tableName = "XS_XJB_VIEW";

        byte[] bytes = file2byte(file);
        ExcelUtils excel = ExcelUtils.getInstance(bytes, file.getName());
        int rowCount = excel.getSheetRow(0);

        StringBuffer sb = new StringBuffer("create table ");
        sb.append(tableName.toLowerCase());
        sb.append(" (\n");
        sb.append("`ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,\n");
        for (int i = 0; i < rowCount; i++) {

            String comment = excel.read(0, 0, i);
            String field = excel.read(0, 1, i);
            sb.append("`")
                    .append(field)
                    .append("` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '")
                    .append(comment).append("',\n");
        }
        sb.append("PRIMARY KEY (`ID`) USING BTREE\n)");
        return sb.toString();
    }

    public static byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
