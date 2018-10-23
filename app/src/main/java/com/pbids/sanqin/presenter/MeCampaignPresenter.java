package com.pbids.sanqin.presenter;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.activity.me.MeCampaignView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by pbids903 on 2017/12/28.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:54
 * @desscribe 类描述:我的活动
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeCampaignFragment
 */
public class MeCampaignPresenter extends BasePresenter{
    MeCampaignView meCampaignView;

    public MeCampaignPresenter(MeCampaignView meCampaignView){
        this.meCampaignView = meCampaignView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams params,String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","stringResponse: "+stringResponse.body());
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        ArrayList<NewsArticle> newsArticles = new ArrayList<NewsArticle>();
                        for(int i = 0;i<jsonArray.length();i++){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            NewsArticle newsArticle = gsonBuilder.create().fromJson(jsonArray.get(i).toString(), NewsArticle.class);
                            newsArticles.add(newsArticle);
                        }
                        meCampaignView.onUpdataInformation(newsArticles);
                        meCampaignView.onHttpSuccess("");
                    }else {
                        meCampaignView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
//                meCampaignView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
}
