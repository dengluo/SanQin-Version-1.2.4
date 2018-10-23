package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.VersionInfo;
import com.pbids.sanqin.ui.activity.BootPageView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * 广告页
 * @see com.pbids.sanqin.ui.activity.BootPageFragment
 */

public class BootPagePresenter extends BasePresenter{
    BootPageView bootPageView;
    public BootPagePresenter(BootPageView bootPageView){
        this.bootPageView = bootPageView;
    }

    public DisposableObserver<Response<String>> submitInformation(){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse.body(): "+stringResponse.body());
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status == MyApplication.OK){
                        String url = jsonObject.getString("data");
                        bootPageView.onHttpSuccess(url);
                    }else{
                        bootPageView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                bootPageView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_LINKPAGE,new HttpParams(),observer);
        return observer;
    }

    public DisposableObserver<Response<String>> checkAppVersion(final String url, final HttpParams params, final String type){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        JSONObject data = jsonObject.getJSONObject("data");
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        VersionInfo versionInfo = gsonBuilder.create().fromJson(data.toString(), VersionInfo.class);
                        bootPageView.versionInfo(versionInfo);
                        bootPageView.checkedUpdate(type);
                    }else {
                        bootPageView.checkError(jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    bootPageView.checkError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                bootPageView.checkError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {
//                bootPageView.checkError("完成");
            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }


}
