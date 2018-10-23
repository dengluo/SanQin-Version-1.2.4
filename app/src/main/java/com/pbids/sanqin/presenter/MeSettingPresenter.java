package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.VersionInfo;
import com.pbids.sanqin.ui.activity.me.MeSettingView;
import com.pbids.sanqin.utils.CheckUpdateUtil;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:06
 * @desscribe 类描述:我的界面-设置
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeSettingFragment
 */
public class MeSettingPresenter extends BasePresenter{
    MeSettingView meSettingView;
    public MeSettingPresenter(MeSettingView meSettingView){
        this.meSettingView = meSettingView;
    }

    //下载apk 文件更新
    public void checkUpdate(Context context,String url){

       /* CheckUpdateUtil.showUpdatePop(context, url, new CheckUpdateUtil.OnStartUpdateListener() {
            @Override
            public void onUpdate(File file) {

            }
        });*/
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
                        meSettingView.versionInfo(versionInfo);
                        meSettingView.onHttpSuccess(type);
                    }else {
                        meSettingView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    meSettingView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meSettingView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}