package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.CampaignEnrollEntity;
import com.pbids.sanqin.model.entity.CampaignEnrollExtendEntity;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollOverView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:47
 * @desscribe 类描述:我的活动报名详细报名信息
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollOverFragment
 */
public class CampaignEnrollOverPresenter extends BasePresenter{
    CampaignEnrollOverView enrollOverView;

    public CampaignEnrollOverPresenter (CampaignEnrollOverView enrollOverView){
        this.enrollOverView = enrollOverView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>(){
            @Override
            public void onNext(@NonNull Response<String> stringResponse){
                Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    JsonObject data =jsonObject.get("data").getAsJsonObject();
                    JsonArray members = data.get("members").getAsJsonArray();
                    JsonObject user_activity = data.get("user_activity").getAsJsonObject();
                    JsonObject activity_info = data.get("activity_info").getAsJsonObject();
                    List<CampaignEnrollEntity> enrollEntities = new ArrayList<>();
                    for(int i=0;i<members.size();i++){
                        CampaignEnrollEntity enrollEntity = new GsonBuilder().create().fromJson(members.get(i).toString(), CampaignEnrollEntity.class);
                        enrollEntities.add(enrollEntity);
                    }
                    CampaignEnrollExtendEntity enrollExtendEntity =  new GsonBuilder().create().fromJson(user_activity.toString(), CampaignEnrollExtendEntity.class);

                    NewsArticle newsArticle = new GsonBuilder().create().fromJson(activity_info.toString(), NewsArticle.class);

                    enrollOverView.getInfomation(enrollEntities,enrollExtendEntity,newsArticle);
                    enrollOverView.onHttpSuccess(type);
//                    new GsonBuilder().create().fromJson(data.toString(), CampaignEnrollEntity.class)
//                    enrollOverView.getInfomation();
                }else{
                    enrollOverView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                enrollOverView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
