package com.pbids.sanqin.presenter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.PayView;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.WechatPayInfo;
import com.pbids.sanqin.ui.view.dialog.PaymentModeDialog;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by pbids903 on 2018/1/14.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:08
 * @desscribe 类描述:支付
 * @remark 备注:
 * @see PaymentModeDialog
 */
public class PayPresenter extends BasePresenter{
    PayView payView;
    public PayPresenter(PayView payView){
        this.payView = payView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    if(type == PaymentModeDialog.PAYMENT_ZHIFUBAO){
                        //支付宝
                        payView.onHttpSuccess(type);
                        payView.getPayInfo(jsonObject.get("data").getAsString());
                    }else if(type == PaymentModeDialog.PAYMENT_QIANBAO){
                        //余款
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        payView.onHttpSuccess(type);
                        payView.getPayInfoForBalance(""+data.get("accountBalance").getAsFloat());
                    }else if(type == PaymentModeDialog.PAYMENT_WECHAT){
                        //微信支付
                        JsonObject dataWechat = jsonObject.get("data").getAsJsonObject();
                        WechatPayInfo wechatPayInfo = new GsonBuilder().create().fromJson(dataWechat.toString(),WechatPayInfo.class);
                        if(wechatPayInfo!=null){
                            payView.onHttpSuccess(type);
                            payView.getPayInfoForWechat(wechatPayInfo);
                        }else{
                            payView.onHttpError(jsonObject.get("message").getAsString());
                        }
                    }
                }else {
                    if(type == PaymentModeDialog.PAYMENT_QIANBAO){
                        //余款
                        payView.getPayInfoForBalanceError(jsonObject.get("message").getAsString());
                    }
                    payView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                payView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }

    public DisposableObserver<Response<String>> submitInformationForBalance(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    if(type == PaymentModeDialog.PAYMENT_QIANBAO){
                        String accountBalance = data.get("accountBalance").getAsString();
                    }
                    payView.onHttpSuccess(type);
                }else {
                    payView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                payView.onHttpError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
