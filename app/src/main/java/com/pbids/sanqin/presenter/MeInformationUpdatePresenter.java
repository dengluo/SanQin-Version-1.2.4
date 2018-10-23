package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.ZongCiInfo;
import com.pbids.sanqin.ui.activity.me.MeInformationUpdateView;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by pbids903 on 2018/3/21.
 */

public class MeInformationUpdatePresenter extends BasePresenter{
    MeInformationUpdateView updateView;
    public MeInformationUpdatePresenter(MeInformationUpdateView updateView){
        this.updateView = updateView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>(){
            @Override
            public void onNext(@NonNull Response<String> stringResponse){
                Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    if("1".equals(type)){
                        JsonObject data = jsonObject.get("data").getAsJsonObject();
                        UserInfo userInfo = new GsonBuilder().create().fromJson(data.toString(),UserInfo.class);
                        updateView.getUserInfo(userInfo);
                    }
//                    data.toString()
                    updateView.onHttpSuccess(type);
                }else{
                    updateView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                updateView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
