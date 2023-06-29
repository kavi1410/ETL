package com.elan.DynamicDBConvert.CustomException;

public class FileNotFoundException extends Throwable{
    String msg;
    public String getMsg() {
        return msg;
    }
    public FileNotFoundException(String msg){
        this.msg = msg;
    }
}
