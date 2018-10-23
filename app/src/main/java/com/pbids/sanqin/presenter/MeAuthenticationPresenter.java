package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.ui.activity.me.MeAuthenticationView;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:50
 * @desscribe 类描述:我的界面，实名认证
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeAuthenticationFragment
 */
public class MeAuthenticationPresenter extends BasePresenter{
    MeAuthenticationView meAuthenticationView;
    public MeAuthenticationPresenter(MeAuthenticationView meAuthenticationView){
        this.meAuthenticationView = meAuthenticationView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    String name = data.get("name").getAsString();
                    String surname = data.get("surname").getAsString();
                    int surnameId = data.get("surnameId").getAsInt();
                    int isRealName = data.get("isRealName").getAsInt();
                    String idNumber = data.get("idNumber").getAsString();
                    meAuthenticationView.reviceUserNmae(name,surname,surnameId,isRealName,idNumber);
                    meAuthenticationView.onHttpSuccess(type);
                }if(status == 2){
                    meAuthenticationView.onHttpSuccess("10");
                }else{
                    meAuthenticationView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meAuthenticationView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }

}
