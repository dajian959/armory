package cn.armory.common.base;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private String msg;
    private int code;
    private T data;

    public Result(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String reason) {
        this.msg = reason;
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