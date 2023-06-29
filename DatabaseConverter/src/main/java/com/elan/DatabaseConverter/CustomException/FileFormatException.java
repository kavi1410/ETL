package com.elan.DatabaseConverter.CustomException;

public class FileFormatException extends Throwable {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public FileFormatException(String msg){
        this.msg=msg;
    }
}
