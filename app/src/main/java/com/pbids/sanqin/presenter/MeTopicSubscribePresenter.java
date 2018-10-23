package com.pbids.sanqin.presenter;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsTopicSubscribe;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.me.MeTopicSubscribeView;
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
 * 模块：消息 -> 公众号消息
 * Created by pbids903 on 2018/1/5.
 */

public class MeTopicSubscribePresenter extends BasePresenter{

    MeTopicSubscribeView mView;


    public MeTopicSubscribePresenter(MeTopicSubscribeView v){
        this.mView = v;
    }

    //加载列表数据
    public DisposableObserver<Response<String>> getTopicSubscribeList(final String url, final HttpParams params, final String type){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","stringResponse: "+stringResponse.body());
                try {
                    JSONObject cbJson = new JSONObject(stringResponse.body());
                    int status = cbJson.getInt("status");
                    if(status== MyApplication.OK){
						JSONObject jdata = cbJson.getJSONObject("data");
                        //tags
                        JSONArray jarray = jdata.getJSONArray("tags");
                        List<NewsTopicSubscribe> dalist = new ArrayList<>();
                        for(int i = 0;i<jarray.length();i++){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            NewsTopicSubscribe item = gsonBuilder.create().fromJson(jarray.get(i).toString(), NewsTopicSubscribe.class);
                            dalist.add(item);
                        }
                        mView.updateTagsList(dalist);

						//surnames
						dalist.clear();
						jarray = jdata.getJSONArray("surnames");
						for(int i = 0;i<jarray.length();i++){
							GsonBuilder gsonBuilder = new GsonBuilder();
							NewsTopicSubscribe item = gsonBuilder.create().fromJson(jarray.get(i).toString(), NewsTopicSubscribe.class);
							dalist.add(item);
						}
						mView.updateSurnameList(dalist);
                        mView.onHttpSuccess(type);

                    }else {
                        mView.onHttpError(cbJson.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mView.onHttpError(Const.JSONERROR);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };
        //http post
        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }
	//删除标签
	public DisposableObserver<Response<String>> delTagsById(final String url, final HttpParams params, final NewsTopicSubscribe one, final String type){
		final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
			@Override
			public void onNext(@NonNull Response<String> stringResponse) {
				//Log.i("wzh","stringResponse: "+stringResponse);
				try {
					JSONObject cbJson = new JSONObject(stringResponse.body());
					int status = cbJson.getInt("status");
					if(status== MyApplication.OK){
						MeTopicSubscribeView.DelCb delCb = new MeTopicSubscribeView.DelCb();
						delCb.setResultCode(MeTopicSubscribeView.DelCb.REQUEST_OK);
						delCb.setData(one);
						mView.onDeleteCb(delCb,type);
					}else {
						mView.onHttpError(cbJson.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mView.onHttpError(Const.JSONERROR);
				}
			}
			@Override
			public void onError(@NonNull Throwable e) {
				mView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
			}

			@Override
			public void onComplete() {

			}
		};
		//http post
		OkGoUtil.getStringObservableForPost(url,params,observer);
		return observer;
	}
	//删除姓氏
	public DisposableObserver<Response<String>> delSurnameBySid(final String url, final HttpParams params, final NewsTopicSubscribe one, final String type){
		final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
			@Override
			public void onNext(@NonNull Response<String> stringResponse) {
				//Log.i("wzh","stringResponse: "+stringResponse);
				try {
					JSONObject cbJson = new JSONObject(stringResponse.body());
					int status = cbJson.getInt("status");
					if(status== MyApplication.OK){
						//读取返回的用户信息
						GsonBuilder gsonBuilder = new GsonBuilder();
						UserInfo userInfo = gsonBuilder.create().fromJson(cbJson.getJSONObject("data").toString() , UserInfo.class);
						MeTopicSubscribeView.DelCb delCb = new MeTopicSubscribeView.DelCb();
						delCb.setUserInfo(userInfo);
						delCb.setResultCode(MeTopicSubscribeView.DelCb.REQUEST_OK);
						delCb.setData(one);
						mView.onDeleteCb(delCb,type);
					}else {
						mView.onHttpError(cbJson.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
				}
			}
			@Override
			public void onError(@NonNull Throwable e) {
				mView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
			}

			@Override
			public void onComplete() {

			}
		};
		//http post
		OkGoUtil.getStringObservableForPost(url,params,observer);
		return observer;
	}
}
