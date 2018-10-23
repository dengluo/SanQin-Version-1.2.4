package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.FeedErrorType;
import com.pbids.sanqin.ui.activity.me.MeFeedbackView;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 12:00
 * @desscribe 类描述:我的界面-设置-反馈信息
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeFeedbackFragment
 */
public class MeFeedbackPresenter extends BasePresenter{
    MeFeedbackView meFeedbackView;
    public MeFeedbackPresenter(MeFeedbackView meFeedbackView){
        this.meFeedbackView = meFeedbackView;
    }

    public DisposableObserver<Response<String>> submitInfotmation(String url, HttpParams httpParams, final String type){
        final Context context = ((BaseFragment)meFeedbackView).getActivity();
        if(context==null){
            return null;
        }
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse.body(): "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    if("1".equals(type)){

                    }else if("2".equals(type)){
                        JsonArray data = jsonObject.get("data").getAsJsonArray();
                        List<FeedErrorType> errorTypes = new ArrayList<>();
                        for(int i=0;i<data.size();i++){
                            FeedErrorType feedErrorType = new GsonBuilder().create().fromJson(data.get(i).toString(),FeedErrorType.class);
                            errorTypes.add(feedErrorType);
                        }
                        meFeedbackView.getFeedErrorTypes(errorTypes);
                    }
                    meFeedbackView.onHttpSuccess(type);
                }else{
                    meFeedbackView.onHttpError(jsonObject.get("message").getAsString());
                }
//                try {
//                    JSONObject jsonObject = new JSONObject(stringResponse.body());
//                    int status = jsonObject.getInt("status");
//                    if(status == MyApplication.OK){
//                        if("1".equals(type)){
//
//                        }else if("2".equals(type)){
//
//                        }
//                        meFeedbackView.onHttpSuccess("");
//                    }else{
//                        meFeedbackView.onHttpError(jsonObject.getString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    meFeedbackView.onHttpError(e.getMessage());
//                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
//                meLoadingPops[0].dismiss();
                meFeedbackView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {
//                meLoadingPops[0].dismiss();
//                meFeedbackView.onHttpSuccess("");
            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
}
