package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.ui.activity.me.MeMoneyView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ErrorCode;
import com.pbids.sanqin.utils.OkGoUtil;


import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:02
 * @desscribe 类描述:我的资金
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeMoneyFragment
 */
public class MeMoneyPresenter extends BaasePresenter {
    MeMoneyView meMoneyView;

    public static final int WITHDRAWLS_REQUEST_CODE_SMS = 3201;

    public MeMoneyPresenter(MeMoneyView meMoneyView){
        this.meMoneyView = meMoneyView;
    }

    @Override
    public void onCreate(BaaseView v, Context context) {
        super.onCreate(v, context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

   /* public DisposableObserver<Response<String>> submitInfotmation(String url, HttpParams httpParams, final String type){
        final Context context = ((BaseFragment)meMoneyView).getActivity();
        if(context==null){
            return null;
        }
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse.body(): "+stringResponse.body());
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status == MyApplication.OK){
                        JSONObject data = jsonObject.getJSONObject("data");
                        meMoneyView.onHttpSuccess(type);
                    }else {
                        meMoneyView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meMoneyView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meMoneyView.onHttpError(e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
//        OkGoUtil.getStringObservableForGet(url,httpParams,observer);
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }*/


   public DisposableObserver isWithDrawTime(float balance){
       HttpParams params = new HttpParams();
       params.put("amount", "" + balance);
       String url = AddrConst.SERVER_ADDRESS_PAYMENT + "/account/getWithdrawSMSCode";
       DisposableObserver disposableObserver = requestHttpPost(url, params, WITHDRAWLS_REQUEST_CODE_SMS);
       return disposableObserver;
   }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {
        switch (requestCode){
            case WITHDRAWLS_REQUEST_CODE_SMS:
                if(resultCode== Const.OK){
                    //提现验证码请求返回
                    try {
                        //String phone = body.getString("phone");
                        JSONObject jsonBody = rescb.getJsonData();
                        Log.i("sdf", "onHttpSuccess: "+jsonBody);
                        meMoneyView.allowWithDraw();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    //提现验证码请求返回
                    mView.showToast("请求失败！");
                }
        }
    }
}
