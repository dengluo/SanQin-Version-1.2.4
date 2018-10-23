package com.pbids.sanqin.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.activity.me.MeFamilyCampaignView;
import com.pbids.sanqin.ui.activity.me.MeFamilyNewsView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:57
 * @desscribe 类描述:我的界面-家族管理-家族活动
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeFamilyCampaignFragment
 */
public class MeFamilyCampaignPresenter extends BasePresenter{
    MeFamilyCampaignView meFamilyCampaignView;
    public MeFamilyCampaignPresenter(MeFamilyCampaignView meFamilyCampaignView){
        this.meFamilyCampaignView = meFamilyCampaignView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@android.support.annotation.NonNull Response<String> stringResponse) {
                try {
                    HttpJsonResponse rescb =  JSON.parseObject(stringResponse.body(),HttpJsonResponse.class);
                    if(rescb.getStatus()== Const.OK){
                        /*JSONArray jsonArray = (JSONArray)rescb.getData();
                        List<NewsArticle> newsArticles  = new ArrayList<>();
                        for(int k=0;k<jsonArray.size();k++){
                            com.alibaba.fastjson.JSONObject itemJson = ( JSONObject) jsonArray.get(k);
                            newsArticles.add(  JSONObject.toJavaObject(itemJson,NewsArticle.class));
                        }
                        System.out.print(jsonArray.toJSONString());*/
                        //List<NewsArticle> newsArticles =  JSONArray.parseArray (rescb.getData().toJSONString(),NewsArticle.class);
                        List<NewsArticle> newsArticles = rescb.getDataList(NewsArticle.class);
                        meFamilyCampaignView.getNewsArticles(newsArticles);
                        meFamilyCampaignView.onHttpSuccess(type);
                    }else {
                        meFamilyCampaignView.onHttpError(rescb.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    meFamilyCampaignView.onHttpError(Const.JSONERROR);
                }
            }

            @Override
            public void onError(@android.support.annotation.NonNull Throwable e) {
                meFamilyCampaignView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
