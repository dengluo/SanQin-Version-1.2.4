package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.activity.me.MeCollectionView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:56
 * @desscribe 类描述:我的收藏界面
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeCollectionFragment
 */
public class MeCollectionPresenter extends BasePresenter{
    MeCollectionView meCollectionView;
    public MeCollectionPresenter(MeCollectionView meCollectionView){
        this.meCollectionView = meCollectionView;
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
                        ArrayList<NewsArticle> newsArticles = new ArrayList<NewsArticle>();
                        for(int i = 0;i<jsonArray.length();i++){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            NewsArticle familyRank = gsonBuilder.create().fromJson(jsonArray.get(i).toString(), NewsArticle.class);
                            newsArticles.add(familyRank);
                        }
                        meCollectionView.getNewsArticles(newsArticles);
                        meCollectionView.onHttpSuccess(type);
                    }else {
                        meCollectionView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meCollectionView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meCollectionView.onHttpError("网络未连接，请检查您的网络！");
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }

    public DisposableObserver<Response<String>> submitInformationDelete(String url, HttpParams params, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        meCollectionView.onHttpSuccess(type);
                    }else {
                        meCollectionView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meCollectionView.onHttpError(Const.JSONERROR);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meCollectionView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
