package com.biu.wifi.campus.exception;

/**
 * @author zhangbin.
 * @date 2018/10/30.
 */
public class BizException extends RuntimeException {

    public Integer key;

    public String message;

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BizException(Integer key, String message) {
        this.key = key;
        this.message = message;
    }
}
