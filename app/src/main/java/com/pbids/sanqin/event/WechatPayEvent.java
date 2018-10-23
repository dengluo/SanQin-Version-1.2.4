package com.pbids.sanqin.event;

import com.tencent.mm.opensdk.modelbase.BaseResp;

public class WechatPayEvent {
     private BaseResp resp;

     public WechatPayEvent(BaseResp resp){
         this.resp = resp;
     }

    public BaseResp getResp() {
        return resp;
    }

    public void setResp(BaseResp resp) {
        this.resp = resp;
    }
}
