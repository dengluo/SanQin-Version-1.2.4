package com.pbids.sanqin.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.util.List;

// http 请求返回
public class HttpJsonResponse<T>  {

    private String message ="";
    private int status ;
    private int code ;
    private Object data ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    //取json
    public JSONObject getJsonData(){
        return (JSONObject)this.data;
    }

    //返回 list
    public <P> List<P> getDataList(Class<P> entityClass) {
        //Class<P> entityClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        JSONArray jsonArray = (JSONArray)this.getData();
        List<P> list = JSONArray.parseArray(jsonArray.toJSONString(), entityClass);
        return list;
    }

    //返回对象
    public <P> P getJavaData(Class<P> entityClass) {
        //Class<P> entityClass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        JSONObject jsonData = (JSONObject)this.getData();
        P da = JSONObject.toJavaObject(jsonData, entityClass);
        return da;
    }

}
