package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollRuleView;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:48
 * @desscribe 类描述:我的活动报名，活动须知
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollRuleFragment
 */
public class CampaignEnrollRulePresenter extends BasePresenter{
    CampaignEnrollRuleView campaignEnrollRuleView;

    public CampaignEnrollRulePresenter (CampaignEnrollRuleView campaignEnrollRuleView){
        this.campaignEnrollRuleView = campaignEnrollRuleView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>(){
            @Override
            public void onNext(@NonNull Response<String> stringResponse){
                //Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    String data = jsonObject.get("data").getAsString();
                    campaignEnrollRuleView.showRuleData(data);
                    campaignEnrollRuleView.onHttpSuccess(type);
                }else{
                    campaignEnrollRuleView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                campaignEnrollRuleView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
