package com.elan.DatabaseConverter.CustomException;

public class TimeOutException extends Throwable {
    private String msg;

    public TimeOutException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
