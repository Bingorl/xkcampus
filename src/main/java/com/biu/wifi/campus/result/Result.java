package com.biu.wifi.campus.result;

public class Result {
    public static final int SQL_EXECUTE_FAILURE = -1;//数据库操作失败
    public static final int SUCCESS = 1;//成功
    public static final int FAILURE = 2;//服务器异常(失败)
    public static final int NODATA = 3;//没有相关数据
    public static final int REQUIRED = 4;//必要参数为空
    public static final int SIGNATURE_ERROR = 5;//验证签名失败
    public static final int CUSTOM_MESSAGE = 6;//自定义服务器返回消息
    public static final int NO_LOGIN = 7;//用户未登录
    public static final int NO_Auth = 8;//用户未认证
    public static final int NO_DATA_RETURN_PAGE = 9;//没有数据，直接回跳上一个页面
    public static final int EDIT_PERSON_INFO = 10;//编辑个人信息

    private int key;
    private String message;
    private Object result;

    public Result(int key, String message, Object result) {
        super();
        this.key = key;
        this.message = message;
        this.result = result;
    }

    public Result(int key, String message) {
        super();
        this.key = key;
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
