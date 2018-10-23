package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.Gift;
import com.pbids.sanqin.model.entity.GiftGroup;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.activity.me.MeGiftView;
import com.pbids.sanqin.ui.activity.news.NewsSearchView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * presenter
 * 模块：我的 -> 新闻搜索
 * Created by pbids903 on 2018/1/5.
 */

public class NewsSearchPresenter extends BasePresenter{

    NewsSearchView newsSearchView;
    //list
    List<NewsInformation> newsInformation = new ArrayList<>();
    NewsInformation  newsInfoItem = new NewsInformation() ;
    List<NewsArticle> newsList = new ArrayList<>();

    public NewsSearchPresenter(NewsSearchView newsSearchView){
        this.newsSearchView = newsSearchView;
        newsInformation.add(newsInfoItem);
        newsInfoItem.setList(newsList);
    }

    //数据列表
    public List<NewsInformation> getNewsInformation(){
        return newsInformation;
    }



    public DisposableObserver<Response<String>> submit(final String url, final HttpParams params, final String type){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","stringResponse: "+stringResponse);
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        //ArrayList<NewsArticle> articleList = new ArrayList<NewsArticle>();
                        for(int i = 0;i<jsonArray.length();i++){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            NewsArticle item = gsonBuilder.create().fromJson(jsonArray.get(i).toString(), NewsArticle.class);
                            //System.out.print(newsInformation);
                            newsList.add(item);
                        }
                        //if(jsonArray.length()>0){
                            newsSearchView.ListLoadDataNum(jsonArray.length());
                            newsSearchView.getNewsSearchAdapter().notifyDataSetChanged(); // 更新list视图
                            newsSearchView.onHttpSuccess(type);
                        //}
                    }else {
                        newsSearchView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    newsSearchView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                newsSearchView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        //http post
        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }


    //清除列表数据
    public void clearNewsInfo() {
        newsList.clear();
    }

    public boolean hasData() {
        return newsList.size()>0;
    }
}
