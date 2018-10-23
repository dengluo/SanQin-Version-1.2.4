package com.pbids.sanqin.base;

// 公共组 管理

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComonRecycerGroup<T> extends BaaseRecycerViewListGroup<T> {

    //保存扩展 自定义信息
    private Map<String,Object> attrs = new HashMap<>();
    private String fag ;

    public void addAttr (String k, Object v){
        attrs.put(k,v);
    }
    public Object getAttr (String k){
        if(attrs.containsKey(k)){
            return attrs.get(k);
        }
        return null;
    }

    // 添加 多个
    public void addBath(List<T> userList) {
        //mList.clear();
        if (userList != null && userList.size() > 0) {
            mList.addAll(userList);
        }
    }
    // 添加 单个
    public void addUserInfo(T uinfo) {
        mList.add(uinfo);
    }

    public String getFag() {
        return fag;
    }

    public void setFag(String fag) {
        this.fag = fag;
    }


}
