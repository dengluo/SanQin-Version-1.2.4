package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.utils.FileUtil;
import com.pbids.sanqin.utils.OkGoUtil;

import java.io.File;

import io.reactivex.observers.DisposableObserver;

/**
 *@author : 上官名鹏
 *Description : 新人邀请p-layer
 *Date :Create in 2018/6/29 20:46
 *Modified By :
 */
public class MeInvitePresenter extends BaasePresenter {

    private MeInviteView meInviteView;

    public MeInvitePresenter(MeInviteView meInviteView) {
        this.meInviteView = meInviteView;
    }

    public void generatingPoster(final Context context, String url) {
        File file = FileUtil.getFile(context, FileUtil.PATH_IMAGES);
        String fileDir = file.getAbsolutePath();
        // 如果文件 存在 ---- 删除
        if(file.exists())
        {
            file.delete();
        }
        OkGo.<File>get(url).tag(context).execute(new FileCallback(fileDir, "poster.png") {
            @Override
            public void onStart(Request<File, ? extends Request> request) {
                super.onStart(request);


//
            }

            @Override
            public void onSuccess(Response<File> response) {
                meInviteView.generatingPoster(response.body());

                meInviteView.dismissDown();
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                int fraction = (int) (progress.fraction * 100);
                meInviteView.downSchedule(fraction);
                Log.i("fraction", fraction + "");
            }
        });
    }

        public DisposableObserver<Response<String>> posterUrl (String url, HttpParams params){
            DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
                @Override
                public void onNext(Response<String> stringResponse) {
                    HttpJsonResponse rescb = JSON.parseObject(stringResponse.body(), HttpJsonResponse.class);
                    Log.i("response", ""+rescb);
                    if (rescb.getStatus() == Const.OK) {
                        JSONObject jsonObject = rescb.getJsonData();
                        String shareUrl = jsonObject.getString("shareUrl");
                       /* String url = (String) rescb.getData();
                        meInviteView.getUrlSuccess(url);*/
                        meInviteView.getUrlSuccess(shareUrl);
                    } else {
                        meInviteView.error();
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            };
            OkGoUtil.getStringObservableForGet(url,params,observer);
            return observer;
        }
}
