package com.biu.wifi.campus.Tool;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：根据sql导出的数据文件生成建表DDL和insert sql语句
 *
 * @author zhangbin.
 * @date 2018/11/27.
 */
public class SqlUtils {

    /**
     * 判断数据表是否存在
     *
     * @param connection 数据库连接
     * @param tableName  表名
     * @return
     */
    public static boolean tableExist(Connection connection, String tableName) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = databaseMetaData.getTables(null, null, tableName, new String[]{"TABLE"});
            return tables.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成创建表的语句，
     * 除了ID为整型自增，其余字段均为字符串类型，长度为100
     *
     * @param tableName
     * @param insertSql
     * @return
     */
    public static String generateTableDDL(String tableName, String insertSql) {
        StringBuffer createTable = new StringBuffer("create table " + tableName + " (\n");
        createTable.append("\t`ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,\n");
        String fieldStr = insertSql.substring(insertSql.indexOf("(") + 1, insertSql.indexOf(")"));
        for (String field : fieldStr.split(",")) {
            createTable.append("\t`" + StringUtils.deleteWhitespace(field) + "`")
                    .append(" varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,\n");
        }
        createTable.append("\tPRIMARY KEY (`ID`) USING BTREE\n)\t")
                .append("ENGINE = InnoDB AUTO_INCREMENT = 218 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '' ROW_FORMAT = Dynamic;");
        return createTable.toString();
    }

    /**
     * 生成insert语句
     *
     * @param list
     * @return
     */
    public static List<String> generateInsertSql(List<String> list) {
        List<String> sqlList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            int j = i + 1;
            if (j >= 2 && j % 2 == 0) {
                sqlList.add(sb.toString());
                sb = new StringBuffer();
            }
        }
        return sqlList;
    }

    /**
     * 获取表名
     *
     * @param insertSql
     * @return
     */
    public static String getTableName(String insertSql) {
        String tableName = insertSql.substring(insertSql.indexOf("insert into") + 12, insertSql.indexOf("(") - 1);
        return tableName;
    }

    /**
     * 读取sql文件
     *
     * @return
     */
    public static List<String> readSqlFile(InputStream in) {
        try {
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            //读取sql文件
            List<String> list = new ArrayList<>();
            String str;
            while ((str = br.readLine()) != null) {
                if (str.contains("prompt Importing") || str.contains("set feedback off")
                        || str.contains("set define off") || str.contains("prompt Done.") || str.equals("")) {
                    continue;
                }
                list.add(str);
            }

            br.close();
            in.close();
            br = null;
            in = null;
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
