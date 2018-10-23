package com.pbids.sanqin.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.me.MeNickSaveView;
import com.pbids.sanqin.ui.view.MeLoadingPop;

import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;

/**
 * @author : 上官名鹏
 * Description :
 * Date :Create in 2018/8/9 10:29
 * Modified By :
 */
public class MeNickSavaPresenter extends BaasePresenter<MeNickSaveView>{

    private MeNickSaveView meNickSaveView;

    private static final int REQUEST_UPDATE_NICK_NAME = 1;

    private MeLoadingPop[] meLoadingPops;

    public MeNickSavaPresenter(MeNickSaveView meNickSaveView) {
        this.meNickSaveView = meNickSaveView;
    }

    public Observer updateNickName(String url,String nickName){
        meLoadingPops = new MeLoadingPop[1];
        meLoadingPops[0] = new MeLoadingPop(mContext,"正在保存");
//        meLoadingPops[0].setBgColor(context.getResources().getColor(R.color.transparent));
        meLoadingPops[0].setIsAnimation(false);
        meLoadingPops[0].setCancelable(false);
        meLoadingPops[0].show();
        DisposableObserver<Response<String>> observer = requestHttpGet(url + "?nickName=" + nickName, null, REQUEST_UPDATE_NICK_NAME);
        return observer;
    }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {
        switch (requestCode){
            case REQUEST_UPDATE_NICK_NAME:
                meLoadingPops[0].dismiss();
                if(resultCode== Const.OK){
                    JSONObject jsonData = rescb.getJsonData();
                    if(jsonData!=null){
                        MyApplication.getUserInfo().setNickName(jsonData.getString("nickName"));
                        UserInfoManager.updateUserInfo(mContext,MyApplication.getUserInfo());
                        meNickSaveView.success();
                    }else{
                        meNickSaveView.failed();
                    }
                }else{
                    meNickSaveView.failed();
                }
                break;
        }
    }

    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        super.onHttpError(resultCode, requestCode, errorCode, errorMessage);
        meLoadingPops[0].dismiss();
    }
}
