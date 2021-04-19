package com.biu.wifi.campus.vo;

/**
 * 学年学期
 *
 * @author zhangbin.
 * @date 2018/11/13.
 */
public class Term {
    public String key;
    public String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Term(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
