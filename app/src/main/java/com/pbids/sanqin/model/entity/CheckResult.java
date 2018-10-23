package com.pbids.sanqin.model.entity;

public class CheckResult<T> {

    private boolean pass = false;
    private String message ;
    private T value ;

    public CheckResult( ){
        pass = true;
    }

    public CheckResult( boolean pass,String message){
        this.pass = pass;
        this.message = message;
    }

    public CheckResult( T value){
        this.pass = true;
        this.value = value;
    }

    public boolean isPass() {
        return pass;
    }

    public String getMessage() {
        return message;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
