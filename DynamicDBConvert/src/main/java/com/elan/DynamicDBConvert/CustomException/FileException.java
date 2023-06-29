package com.elan.DynamicDBConvert.CustomException;

public class FileException extends Throwable {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public FileException(String msg){
        this.msg=msg;
    }
}
