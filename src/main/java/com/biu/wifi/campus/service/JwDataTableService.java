package com.biu.wifi.campus.service;

import com.biu.wifi.campus.Tool.SqlUtils;
import com.biu.wifi.campus.dao.JwDataTableMapper;
import com.biu.wifi.campus.dao.model.JwDataTable;
import com.biu.wifi.campus.dao.model.JwDataTableCriteria;
import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import com.biu.wifi.component.datastore.FileSupportService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.support.cache.CacheSupport;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/11/27.
 */
@Service
public class JwDataTableService {

    private static Logger logger = LoggerFactory.getLogger(JwDataTableService.class);
    @Autowired
    private JwDataTableMapper jwDataTableMapper;
    @Autowired
    private JdbcTemplate ojdbcTemplate;
    @Autowired
    private FileSupportService fileSupportService;

    public void importSqlData(List<String> sqlList, String tableName, String tableDDL, String fileId) {

        try {
            Connection connection = ojdbcTemplate.getDataSource().getConnection();
            if (!SqlUtils.tableExist(connection, tableName)) {
                //表不存在，则建表
                ojdbcTemplate.update(tableDDL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("创建数据表失败...");
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            //更新数据
            ojdbcTemplate.update("TRUNCATE TABLE " + tableName);
            ojdbcTemplate.batchUpdate(sqlList.toArray(new String[]{}));
        }

        //更改同步状态
        changeSyncStatus(tableName, fileId);
        //删除文件信息记录
        delete(tableName);
        //删除文件
        fileSupportService.remove(fileId);
    }

    public void add(Integer schoolId, String fileId, String tableName, String tableDDL) {
        //删除旧的
        JwDataTableCriteria example = new JwDataTableCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andSchoolIdEqualTo(schoolId)
                .andTableNameEqualTo(tableName)
                .andTableDdlEqualTo(tableDDL)
                .andIsDeleteEqualTo((short) 2);
        List<JwDataTable> tableList = jwDataTableMapper.selectByExample(example);
        for (JwDataTable table : tableList) {
            String fid = CacheSupport.get("jwDataTableSyncTask", "fileId_" + fileId, String.class);
            if (fid != null) {
                //在同步队列中的不能删除
                throw new BizException(Result.CUSTOM_MESSAGE, "该数据表正在进行数据同步，不能进行导入操作");
            }

            delete(tableName);
            try {
                fileSupportService.remove(table.getFileId());
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("要删除的文件未找到，{}", e.getMessage());
            }

        }

        //新增新纪录
        JwDataTable table = new JwDataTable();
        table.setSchoolId(schoolId);
        table.setFileId(fileId);
        table.setTableName(tableName);
        table.setTableDdl(tableDDL);
        table.setCreateTime(new Date());
        jwDataTableMapper.insertSelective(table);
    }

    public JwDataTable get(String tableName) {
        JwDataTableCriteria example = new JwDataTableCriteria();
        example.createCriteria()
                .andTableDdlEqualTo(tableName);
        List<JwDataTable> list = jwDataTableMapper.selectByExample(example);
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    public List<JwDataTable> findList(JwDataTableCriteria example) {
        return jwDataTableMapper.selectByExample(example);
    }

    public void delete(String tableName) {
        //删除文件信息
        JwDataTableCriteria example = new JwDataTableCriteria();
        example.createCriteria()
                .andTableNameEqualTo(tableName)
                .andIsDeleteEqualTo((short) 2);

        JwDataTable table = new JwDataTable();
        table.setDeleteTime(new Date());
        table.setIsDelete((short) 1);
        jwDataTableMapper.updateByExampleSelective(table, example);
    }

    public void changeSyncStatus(String tableName, String fileId) {
        JwDataTableCriteria example = new JwDataTableCriteria();
        example.createCriteria()
                .andTableNameEqualTo(tableName)
                .andIsSyncEqualTo((short) 2);

        JwDataTable table = new JwDataTable();
        table.setIsSync((short) 1);
        jwDataTableMapper.updateByExample(table, example);
        //删除文件
        fileSupportService.remove(fileId);
    }

    /**
     * 复制数据，将sql文件的数据导入到数据库
     *
     * @param table
     */
    @Async
    public void syncData(JwDataTable table) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tableName = table.getTableName();
        String fileId = table.getFileId();
        //查询教务系统的对应表的总数据量
        int total = ojdbcTemplate.queryForInt("SELECT COUNT(*) FROM " + tableName);
        logger.info("数据表【{}】的总数据量为{}条", new Object[]{tableName, total});
        try {
            //创建一张临时表,将上传的sql文件的数据导入临时表
            String tmpTableName = "tmp_" + tableName;
            FileIoEntity fileIoEntity = fileSupportService.get(fileId);
            List<String> list = SqlUtils.readSqlFile(new ByteArrayInputStream(fileIoEntity.getContent()));
            importSqlData(list, tmpTableName, table.getTableDdl(), fileId);
            //修改表名
            String backUpTableName = tableName + "_" + sdf.format(new Date());
            ojdbcTemplate.update("ALTER TABLE " + tableName + " RENAME " + backUpTableName);
            ojdbcTemplate.update("ALTER TABLE " + tmpTableName + " RENAME " + tableName);
            //每个数据表只保留最近的一个备份
            String sql = "select TABLE_NAME as tableName from information_schema.`TABLES` where TABLE_SCHEMA = '54campus_jw'";
            List<Map<String, Object>> tableNameList = ojdbcTemplate.queryForList(sql);
            List<String> dropTableSqlList = new ArrayList<>();
            for (Map<String, Object> map : tableNameList) {
                String name = map.get("tableName").toString();
                if (name.startsWith(tableName)) {
                    if (!name.equals(tableName) && !name.equals(backUpTableName)) {
                        dropTableSqlList.add("DROP TABLE " + map.get("tableName").toString());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(dropTableSqlList)) {
                ojdbcTemplate.batchUpdate(dropTableSqlList.toArray(new String[]{}));
            }

            //数据导入完成，更新任务同步状态并删除
            changeSyncStatus(tableName, fileId);
            delete(tableName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //删除同步任务记录
            CacheSupport.remove("jwDataTableSyncTask", "fileId_" + fileId);
        }
    }

    /**
     * 复制数据，将sql文件的数据导入到数据库
     *
     * @param fileId
     */
    public void syncData(String fileId) {
        JwDataTableCriteria example = new JwDataTableCriteria();
        example.setOrderByClause("create_time desc");
        example.createCriteria()
                .andIsDeleteEqualTo((short) 2)
                .andFileIdEqualTo(fileId);
        List<JwDataTable> tableList = findList(example);
        if (CollectionUtils.isEmpty(tableList)) {
            //创建任务记录
            CacheSupport.put("jwDataTableSyncTask", "fileId_" + tableList.get(0).getFileId(), tableList.get(0).getFileId());
            syncData(tableList.get(0));
        }
    }

    /**
     * 复制数据，将sql文件的数据导入到数据库
     *
     * @param dataTableId
     */
    public void syncData(Integer dataTableId) {
        JwDataTable table = jwDataTableMapper.selectByPrimaryKey(dataTableId);
        //创建任务记录
        CacheSupport.put("jwDataTableSyncTask", "fileId_" + table.getFileId(), table.getFileId());
        syncData(table);
    }
}
