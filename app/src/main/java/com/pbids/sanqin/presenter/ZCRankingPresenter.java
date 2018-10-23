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
import com.pbids.sanqin.model.entity.ZCRankingInfo;
import com.pbids.sanqin.ui.activity.zongquan.ZCRankingView;
import com.pbids.sanqin.utils.OkGoUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/5 17:13
 * @desscribe 类描述:
 * @remark 备注:
 * @see
 */

public class ZCRankingPresenter extends BasePresenter{

    ZCRankingView zcRankingView;
    public ZCRankingPresenter(ZCRankingView zcRankingView){
        this.zcRankingView = zcRankingView;
    }

    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>(){
            @Override
            public void onNext(@NonNull Response<String> stringResponse){
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if (status == MyApplication.OK) {
                    if ("1".equals(type)) {
                        JsonArray data = jsonObject.get("data").getAsJsonArray();
                        List<ZCRankingInfo> rankingInfos = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            rankingInfos.add(new GsonBuilder().create().fromJson(data.get(i).toString(), ZCRankingInfo.class));
                        }
                        zcRankingView.getZCRankInfo(rankingInfos);
                    }
                    zcRankingView.onHttpSuccess(type);
                } else {
                    zcRankingView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                zcRankingView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
