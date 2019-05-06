package com.lovesickness.o2o.util;

import java.io.Serializable;

public class ResultBean<T> implements Serializable {
    public static final int SUCCESS = 1;

    public static final int FAIL = 0;

    private static final long serialVersionUID = 1L;

    private String msg = "success";

    private boolean success = true;

    private int code = SUCCESS;
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;

    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.success = false;
        this.code = FAIL;
        e.printStackTrace();
    }

    public ResultBean(boolean success, int code, String msg) {
        this.msg = msg;
        this.success = success;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
