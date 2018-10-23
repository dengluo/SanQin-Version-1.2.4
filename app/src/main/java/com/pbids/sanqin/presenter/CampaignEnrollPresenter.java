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
import com.pbids.sanqin.model.entity.CampaignEnrollExtendEntity;
import com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollView;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by pbids903 on 2018/1/30.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:47
 * @desscribe 类描述:我的活动报名填写报名信息
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollFragment
 */
public class CampaignEnrollPresenter extends BasePresenter{
    CampaignEnrollView enrollView;
    public CampaignEnrollPresenter (CampaignEnrollView enrollView){
        this.enrollView = enrollView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>(){
            @Override
            public void onNext(@NonNull Response<String> stringResponse){
                Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    JsonObject user_activity = data.get("user_activity").getAsJsonObject();
                    CampaignEnrollExtendEntity enrollExtendEntity =  new GsonBuilder().create().fromJson(user_activity.toString(), CampaignEnrollExtendEntity.class);
                    enrollView.getInfomation(enrollExtendEntity);
                    enrollView.onHttpSuccess(type);
                }else{
                    enrollView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                enrollView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
