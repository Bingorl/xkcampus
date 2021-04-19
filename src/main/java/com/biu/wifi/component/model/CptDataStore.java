package com.biu.wifi.component.model;

import com.biu.wifi.core.base.CoreEntity;

public class CptDataStore extends CoreEntity {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column HFMIS_V8.CPT_DATASTORE.ID
     *
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column HFMIS_V8.CPT_DATASTORE.NAME
     *
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column HFMIS_V8.CPT_DATASTORE.TYPE
     *
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column HFMIS_V8.CPT_DATASTORE.PATH
     *
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    private String path;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column HFMIS_V8.CPT_DATASTORE.BAK_PATH
     *
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    private String bakPath;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column HFMIS_V8.CPT_DATASTORE.ID
     *
     * @return the value of HFMIS_V8.CPT_DATASTORE.ID
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */

    private String typeName;

    public String getTypeName() {

        if ("1".equals(getType())) {
            return "数据库";
        } else {
            return "本地";
        }
    }

    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column HFMIS_V8.CPT_DATASTORE.ID
     *
     * @param id the value for HFMIS_V8.CPT_DATASTORE.ID
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column HFMIS_V8.CPT_DATASTORE.NAME
     *
     * @return the value of HFMIS_V8.CPT_DATASTORE.NAME
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column HFMIS_V8.CPT_DATASTORE.NAME
     *
     * @param name the value for HFMIS_V8.CPT_DATASTORE.NAME
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column HFMIS_V8.CPT_DATASTORE.TYPE
     *
     * @return the value of HFMIS_V8.CPT_DATASTORE.TYPE
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column HFMIS_V8.CPT_DATASTORE.TYPE
     *
     * @param type the value for HFMIS_V8.CPT_DATASTORE.TYPE
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column HFMIS_V8.CPT_DATASTORE.PATH
     *
     * @return the value of HFMIS_V8.CPT_DATASTORE.PATH
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public String getPath() {
        return path;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column HFMIS_V8.CPT_DATASTORE.PATH
     *
     * @param path the value for HFMIS_V8.CPT_DATASTORE.PATH
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column HFMIS_V8.CPT_DATASTORE.BAK_PATH
     *
     * @return the value of HFMIS_V8.CPT_DATASTORE.BAK_PATH
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public String getBakPath() {
        return bakPath;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column HFMIS_V8.CPT_DATASTORE.BAK_PATH
     *
     * @param bakPath the value for HFMIS_V8.CPT_DATASTORE.BAK_PATH
     * @mbggenerated Wed Jun 05 09:51:42 CST 2013
     */
    public void setBakPath(String bakPath) {
        this.bakPath = bakPath == null ? null : bakPath.trim();
    }
}