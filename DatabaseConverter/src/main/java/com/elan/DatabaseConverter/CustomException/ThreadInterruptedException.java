package com.elan.DatabaseConverter.CustomException;

public class ThreadInterruptedException extends RuntimeException{
    private String msg;

    public ThreadInterruptedException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
