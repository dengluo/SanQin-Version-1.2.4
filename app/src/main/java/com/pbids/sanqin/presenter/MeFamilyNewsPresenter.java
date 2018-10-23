package com.pbids.sanqin.presenter;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.activity.me.MeFamilyNewsView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:59
 * @desscribe 类描述:我的界面-家族管理-家族资讯数据
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeFamilyNewsFragment
 */
public class MeFamilyNewsPresenter extends BasePresenter{
    MeFamilyNewsView meFamilyNewsView;
    public MeFamilyNewsPresenter(MeFamilyNewsView meFamilyNewsView){
        this.meFamilyNewsView = meFamilyNewsView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        jsonArray.length();
                        ArrayList<NewsArticle> newsArticles = new ArrayList<NewsArticle>();
                        for(int i = 0;i<jsonArray.length();i++){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            NewsArticle newsArticle = gsonBuilder.create().fromJson(jsonArray.get(i).toString(), NewsArticle.class);
                            newsArticles.add(newsArticle);
                        }
                        meFamilyNewsView.getNewsArticles(newsArticles);
                        meFamilyNewsView.onHttpSuccess(type);
                    }else {
                        meFamilyNewsView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meFamilyNewsView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meFamilyNewsView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
