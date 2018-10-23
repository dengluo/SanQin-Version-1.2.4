package com.pbids.sanqin.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.HomeNewsInformation;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongView;
import com.pbids.sanqin.ui.view.PopupAdView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by pbids903 on 2017/11/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:12
 * @desscribe 类描述:知宗界面
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.ZhiZongFragment
 */
public class ZhiZongPresenter extends BasePresenter {
    ZhiZongView mZhiZongView;
    public ZhiZongPresenter(ZhiZongView zhiZongView){
        this.mZhiZongView = zhiZongView;
    }

    public DisposableObserver loadingInformation(final Context context, String url, HttpParams params, final String type){
        final HomeNewsInformation[] homeNewsInformation = new HomeNewsInformation[1];
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    JSONObject data = jsonObject.getJSONObject("data");
                    if(status == MyApplication.OK){
                        GsonBuilder builder = new GsonBuilder();
                        String dataStr = data.toString();
                        SharedPreferences sp = context.getSharedPreferences("sanqin", Context.MODE_PRIVATE);
                        if("1".equals(type)){
                            Log.i("wzh","dataStr: "+dataStr);
                            homeNewsInformation[0] = builder.create().fromJson(dataStr, HomeNewsInformation.class);
                            filterNotInterst(homeNewsInformation[0].getList().getList(),sp);
                            filterNotInterst(homeNewsInformation[0].getSurname_list().getList(),sp);
                            filterNotInterst(homeNewsInformation[0].getZhichong_list().getList(),sp);
                            mZhiZongView.loadingInformation(homeNewsInformation[0]);
                            mZhiZongView.onHttpSuccess(type);
                        }else{
                            JSONObject listJsonObject =  data.getJSONObject("list");
                            NewsInformation list = builder.create().fromJson(listJsonObject.toString(), NewsInformation.class);
                            filterNotInterst(list.getList(),sp);
                            mZhiZongView.loadingInformation(list);
                            mZhiZongView.onHttpSuccess(type);
                        }
                    }else{
                        mZhiZongView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    mZhiZongView.onHttpError(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mZhiZongView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }

    private void filterNotInterst(List<NewsArticle> list, SharedPreferences preferences){
        String ad_not_interest = preferences.getString("ad_not_interest", "");
        Log.i("wzh","ad_not_interest: "+ad_not_interest);
        List<String> strings = Arrays.asList(ad_not_interest.split("[,]"));
        for(int i=0;i<list.size();i++){
            String id = ""+list.get(i).getId();
            for(int j=0;j<strings.size();j++){
                if(id.equals(strings.get(j))){
                    list.remove(i);
                }
            }
        }
    }

}
