package com.pbids.sanqin.presenter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongMoreView;
import com.pbids.sanqin.utils.OkGoUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:11
 * @desscribe 类描述:首页数据list，更多数据界面
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.ZhiZongMoreFragment
 */
public class ZhiZongMorePresenter extends BasePresenter{
    ZhiZongMoreView zhiZongMoreView;
    public ZhiZongMorePresenter(ZhiZongMoreView zhiZongMoreView){
        this.zhiZongMoreView = zhiZongMoreView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status==MyApplication.OK){
                    List<NewsArticle> newsArticles = new ArrayList<>();
                    List<NewsInformation> newsInformations = new ArrayList<>();
                    JsonArray data = jsonObject.get("data").getAsJsonArray();
                    for(int i=0;i<data.size();i++){
                        NewsArticle newsArticle = new GsonBuilder().create().fromJson(data.get(i).toString(),NewsArticle.class);
                        newsArticles.add(newsArticle);
                    }
                    NewsInformation newsInformation = new NewsInformation();
                    newsInformation.setList(newsArticles);
                    newsInformations.add(newsInformation);
                    if(zhiZongMoreView!=null) zhiZongMoreView.getMoreNewsInformation(newsInformations,type);
                    if(zhiZongMoreView!=null) zhiZongMoreView.onHttpSuccess(type);
                }else {
                    if(zhiZongMoreView!=null) zhiZongMoreView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if(zhiZongMoreView!=null) zhiZongMoreView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
