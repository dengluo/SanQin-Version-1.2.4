package com.pbids.sanqin.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.event.WechatPayEvent;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, "wxd61c8cd581e28ad7");
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	//微信支付回调
	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			EventBus.getDefault().post(new WechatPayEvent(resp));
		}
		finish();
	}










	/*@Override
	public void onResp(BaseResp resp) {
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//		Log.i("wzh","onPayFinish, errCode = " + resp.errCode);
		int result = 0;
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage("微信支付结果："+String.valueOf(resp.errCode));
//			builder.show();
		*//*	if (resp.errCode==0){
				Toast.makeText(this,"微信支付成功",Toast.LENGTH_LONG).show();
			}else if (resp.errCode==-2){
				Toast.makeText(this,"微信取消支付",Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(this,"微信支付失败",Toast.LENGTH_LONG).show();
			}*//*
            EventBus.getDefault().post(new WechatPayEvent(resp));
			//Toast.makeText(this,"微信支付结果:"+String.valueOf(resp.errCode),Toast.LENGTH_SHORT).show();
		}
		finish();
	}*/
}