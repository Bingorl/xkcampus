package com.biu.wifi.campus.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin.
 * @date 2018/11/6.
 */
@Service
public abstract class JwService {

    @Autowired
    private JdbcTemplate ojdbcTemplate;

    public abstract List<Map<String, Object>> findKCCJList(String zxjxjhh, String xh);

    public abstract List<Map<String, Object>> findXSXJList(String xm, String xh);

    public abstract List<Map<String, Object>> findJASBList(String xqh, String jxlh, String jash);


    /**
     * 学生成绩原始信息
     *
     * @param zxjxjhh
     * @param xh
     * @return
     */
    public List<Map<String, Object>> queryKCCJList(String zxjxjhh, String xh) {
        String sql = "SELECT * FROM xs_kccjb_view  WHERE ZXJXJHH LIKE '%" + zxjxjhh + "%' AND XH = '" + xh + "' ORDER BY KSSJ DESC";
        List<Map<String, Object>> list = ojdbcTemplate.queryForList(sql);
        return list;
    }

    /**
     * 课程原始信息
     *
     * @param zxjxjhh 学年学期
     * @return
     */
    public List<Map<String, Object>> queryKCList(String zxjxjhh) {
        String sql = "SELECT * FROM XS_KCCJB_VIEW WHERE ZXJXJHH LIKE '%" + zxjxjhh + "%'";
        List<Map<String, Object>> list = ojdbcTemplate.queryForList(sql.toString());
        return list;
    }

    /**
     * 学生学籍原始信息
     *
     * @param xm 姓名
     * @param xh 学号
     * @return
     */
    public List<Map<String, Object>> queryXSXJList(String xm, String xh) {
        /*StringBuffer sql = new StringBuffer("SELECT * FROM XS_XJB_VIEW WHERE 1=1");*/
        StringBuffer sql = new StringBuffer("SELECT * FROM XS_XJB WHERE 1=1");
        if (StringUtils.isNotBlank(xm)) {
            sql.append(" AND XM LIKE '%").append(xm).append("%'");
        }
        if (StringUtils.isNotBlank(xh)) {
            sql.append(" AND XH LIKE '%").append(xh).append("%'");
        }

        List<Map<String, Object>> list = ojdbcTemplate.queryForList(sql.toString());
        return list;
    }

    /**
     * 教室原始信息
     *
     * @param xqh  校区号
     * @param jxlh 教学楼号
     * @param jash 教室号
     * @return
     */
    public List<Map<String, Object>> queryJASBList(String xqh, String jxlh, String jash) {
        StringBuffer sql = new StringBuffer("SELECT * FROM CODE_JASB WHERE 1=1");
        if (StringUtils.isNotBlank(xqh)) {
            sql.append(" AND XQH = '").append(xqh).append("'");
        }
        if (StringUtils.isNotBlank(jxlh)) {
            sql.append(" AND JXLH = '").append(jxlh).append("'");
        }
        if (StringUtils.isNotBlank(jash)) {
            sql.append(" AND JASH = '").append(jash).append("'");
        }
        List<Map<String, Object>> list = ojdbcTemplate.queryForList(sql.toString());
        return list;
    }

    /**
     * 教师课程排课表
     *
     * @param zxjxjhh   学年学期
     * @param skzcList  上课周次
     * @param skxq      上课星期
     * @param jsh       教师号
     * @param paramList 课程号和课序号的key-value集合
     * @return
     */
    public List<Map<String, Object>> queryJSPKBList(String zxjxjhh, List<Integer> skzcList, String skxq, String jsh, List<Map<String, Object>> paramList) {
        StringBuffer sql = new StringBuffer("SELECT * FROM RW_DDWSXK_VIEW WHERE  ZXJXJHH = '" + zxjxjhh + "'");
        if (StringUtils.isNotBlank(jsh)) {
            sql.append(" AND JSH = '").append(jsh).append("'");
        }
        if (CollectionUtils.isNotEmpty(skzcList)) {
            sql.append(" AND(");
            for (int i = 0; i < skzcList.size(); i++) {
                Integer skzc = skzcList.get(i);
                sql.append(" LEFT(SKZC,").append(skzc).append(")").append(" LIKE '%1'");
                if (i != skzcList.size() - 1) {
                    sql.append(" OR ");
                }
            }
            sql.append(")");
        }
        //上课星期
        if (StringUtils.isNotBlank(skxq)) {
            sql.append(" AND SKXQ IN (").append(skxq).append(")");
        }
        //根据课程号和课序号确定教师
        if (CollectionUtils.isNotEmpty(paramList)) {
            sql.append(" AND (");
            for (int i = 0; i < paramList.size(); i++) {
                Map<String, Object> param = paramList.get(i);
                sql.append(" (");
                List<String> keyList = new ArrayList<>(param.keySet());
                sql.append(keyList.get(0)).append(" = '").append(param.get(keyList.get(0).toString())).append("'");
                sql.append(" AND ");
                sql.append(keyList.get(1)).append(" = '").append(param.get(keyList.get(1).toString())).append("'");
                sql.append(")");
                if (i != paramList.size() - 1) {
                    sql.append(" OR ");
                }
            }
            sql.append(")");
        }
        List<Map<String, Object>> list = ojdbcTemplate.queryForList(sql.toString());
        return list;
    }

    /**
     * 排课面向班级表
     *
     * @param zxjxjhh 学期学年
     * @param kch     课程号
     * @param kxh     课序号
     * @param bjh     班级号
     * @param bz      备注
     * @return
     */
    public List<Map<String, Object>> queryPKMXBJBList(String zxjxjhh, String kch, String kxh, String bjh, String bz) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PK_MXBJB WHERE  ZXJXJHH = '" + zxjxjhh + "'");
        if (StringUtils.isNotBlank(kch)) {
            sql.append(" AND KCH = ").append(kch);
        }
        if (StringUtils.isNotBlank(kxh)) {
            sql.append(" AND KXH = ").append(kxh);
        }
        if (StringUtils.isNotBlank(bjh)) {
            sql.append(" AND BJH = ").append(bjh);
        }
        if (StringUtils.isNotBlank(bz)) {
            sql.append(" AND BZ LIKE '%").append(bz).append("%'");
        }
        List<Map<String, Object>> list = ojdbcTemplate.queryForList(sql.toString());
        return list;
    }

    /**
     * 学生选课表
     *
     * @param zxjxjhh 学期学年
     * @param xh      学号
     * @return
     */
    public List<Map<String, Object>> queryXSXKList(String zxjxjhh, String xh, String kch, String kxh) {
        StringBuffer sql = new StringBuffer("SELECT * FROM XK_XKB WHERE ZXJXJHH = '" + zxjxjhh + "'");
        if (StringUtils.isNotBlank(xh)) {
            sql.append(" AND XH = '")
                    .append(xh)
                    .append("'");
        }

        if (StringUtils.isNotBlank(kch)) {
            sql.append(" AND KCH = '")
                    .append(kch)
                    .append("'");
        }

        if (StringUtils.isNotBlank(kxh)) {
            sql.append(" AND KXH = '")
                    .append(kxh)
                    .append("'");
        }

        List<Map<String, Object>> list = ojdbcTemplate.queryForList(sql.toString());
        return list;
    }
}
