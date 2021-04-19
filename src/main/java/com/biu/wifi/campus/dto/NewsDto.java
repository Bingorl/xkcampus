package com.biu.wifi.campus.dto;

import com.biu.wifi.campus.dao.model.News;

import java.io.Serializable;

/**
 * @author zhangbin.
 * @date 2019/3/22.
 */
public class NewsDto implements Serializable {

    private News current;
    private News next;

    public News getCurrent() {
        return current;
    }

    public void setCurrent(News current) {
        this.current = current;
    }

    public News getNext() {
        return next;
    }

    public void setNext(News next) {
        this.next = next;
    }
}
