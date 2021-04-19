package com.biu.wifi.component.datastore;

import com.biu.wifi.component.dao.CptDataStoreMapper;
import com.biu.wifi.component.model.CptDataStore;
import com.biu.wifi.component.model.CptDataStoreExample;
import com.biu.wifi.component.model.CptDataStoreExample.Criteria;
import com.biu.wifi.core.base.CoreService;
import com.biu.wifi.core.support.cache.CacheSupport;
import com.biu.wifi.core.support.exception.ServiceException;
import com.biu.wifi.core.support.orm.mybatis3.util.IbatisServiceUtils;
import com.biu.wifi.core.util.FileUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class DataStoreService extends CoreService {

    @Autowired
    private CptDataStoreMapper dao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ���
     *
     * @param record
     * @return
     */
    public int insertDataStore(CptDataStore record) {

        if ("2".equals(record.getType())) {
            if (!isExists(record.getPath())) {
                throw new ServiceException("�ļ�·��������");
            }

            record.setPath(FileUtilsEx.filterPath(record.getPath()));

            // ����
            if (!isExists(record.getBakPath())) {
                throw new ServiceException("�����ļ�·��������");
            }

            record.setBakPath(FileUtilsEx.filterPath(record.getBakPath()));
        }

        return dao.insert(record);
    }

    /**
     * �ļ�·���Ƿ����
     *
     * @param path
     * @return
     */
    private boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * ɾ��
     *
     * @param id
     * @return
     */
    public int deleteDataStore(String id) {

        // TODO
        // ɾ��ǰ�ж��Ƿ����ļ�,�������,���޷�ɾ��

        return dao.deleteByPrimaryKey(id);
    }

    /**
     * ����
     *
     * @param record
     * @return
     */
    public int updateDataStore(CptDataStore record) {

        if ("2".equals(record.getType())) {
            record.setPath(FileUtilsEx.filterPath(record.getPath()));
            record.setBakPath(FileUtilsEx.filterPath(record.getBakPath()));
        }

        return dao.updateByPrimaryKey(record);
    }

    /**
     * ��ѯ
     *
     * @param record
     * @return
     * @throws Exception
     */
    public List<CptDataStore> selectDataStores(CptDataStore record) throws Exception {
        CptDataStoreExample example = new CptDataStoreExample();
        Criteria criteria = example.createCriteria();
        //Criteria criteria = example.createCriteria();

        IbatisServiceUtils.createCriteriaByEntity(criteria, record);

        example.setOrderByClause("id");

        return dao.selectByExample(example);
    }

    /**
     * ��ѯ
     *
     * @param id
     * @return
     */
    public CptDataStore selectDataStore(String id) {
        return dao.selectByPrimaryKey(id);
    }

    /**
     * ͨ���洢���Ƶõ��洢��Ϣ
     *
     * @param name
     * @return
     */
    public CptDataStore getInfoByName(String name) {

        CptDataStore result = CacheSupport.get("DataStoreService.getInfoByName", name, CptDataStore.class);

        if (result == null) {
            result = jdbcTemplate.queryForObject("select id, name, type, path, type, bak_path from cpt_datastore where name = ?",
                    new Object[]{name}, new BeanPropertyRowMapper<CptDataStore>(CptDataStore.class));
            CacheSupport.put("DataStoreService.getInfoByName", name, result);
        }

        return result;
    }
}
