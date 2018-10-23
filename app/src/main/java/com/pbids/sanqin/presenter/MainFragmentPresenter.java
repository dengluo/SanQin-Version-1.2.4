package com.pbids.sanqin.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.PickBrick;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.VersionInfo;
import com.pbids.sanqin.ui.activity.zongquan.MainFragmentView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 *@author : 上官名鹏
 *Description :
 *Date :Create in 2018/6/13 12:03
 *Modified By :
 */

public class MainFragmentPresenter extends BasePresenter {

    private MainFragmentView mainFragmentView;

    private String url;

    public static final int REQUEST_CODE_UPDATE_GROUP = 3659;


    public MainFragmentPresenter(MainFragmentView mainFragmentView) {
        this.mainFragmentView = mainFragmentView;
    }

    /**
     * 领取砖块
     * @return
     */
    public DisposableObserver pickBrick(){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(Response<String> stringResponse) {
                HttpJsonResponse<PickBrick> pickBrickHttpJsonResponse = JSON.parseObject(stringResponse.body(), new TypeReference<HttpJsonResponse<PickBrick>>(){});
                int status = pickBrickHttpJsonResponse.getStatus();
                if(status == Const.OK){
                    PickBrick pickBrick = pickBrickHttpJsonResponse.getJavaData(PickBrick.class);
                    if(pickBrick!=null){
                        if(pickBrick.getState()==1&&pickBrick.getIsRecived().equals("0")){
                            mainFragmentView.showDialog(pickBrick.getValue());
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        HttpParams httpParams = new HttpParams();
        OkGoUtil.getStringObservableForGet(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_PICK_UP_BRICK_NUMBER, httpParams, observer);
        return observer;
    }

    /**
     * 更新群名称
     */
    public DisposableObserver updataUserGroup(String groupId, String groupName, String area){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(Response<String> stringResponse) {
                HttpJsonResponse rescb =  JSON.parseObject(stringResponse.body(),HttpJsonResponse.class);
                if(rescb.getStatus()== Const.OK){
                    if(mainFragmentView != null) mainFragmentView.updateSuccess("群名称更新成功",0);
                }else {
                    if(mainFragmentView != null) mainFragmentView.updateFailed("群名称更新失败",0);
                }
            }
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };
        HttpParams httpParams = new HttpParams();
        httpParams.put("groupId", groupId);
        httpParams.put("groupName", groupName);
        httpParams.put("area", area);
        url = AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_VAL_UPDATE_GROUP;
        OkGoUtil.getStringObservableForPost(url, httpParams, observer);
        return observer;
    }

    /**
     * 转让群
     */
    public DisposableObserver tranfeTeam(String groupId, int account){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(Response<String> stringResponse) {
                HttpJsonResponse rescb =  JSON.parseObject(stringResponse.body(),HttpJsonResponse.class);
                if(rescb.getStatus()== Const.OK){
                    if(mainFragmentView != null) mainFragmentView.updateSuccess("转让群成功",1);
                }else {
                    if(mainFragmentView != null) mainFragmentView.updateFailed(rescb.getMessage(),1);
                }
            }
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };
        HttpParams httpParams = new HttpParams();
        httpParams.put("groupId", groupId);
        httpParams.put("transferId", account);
        url = AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_TRANSFERGROUPS;
        OkGoUtil.getStringObservableForPost(url, httpParams, observer);
        return observer;
    }

    /**
     * 验证是否加入群
     */
    public DisposableObserver valJoinTeam(){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(Response<String> stringResponse) {
                HttpJsonResponse rescb =  JSON.parseObject(stringResponse.body(),HttpJsonResponse.class);
                if(rescb.getStatus()==Const.OK){
                    JSONObject jsonData = rescb.getJsonData();
                    if(jsonData!=null){
                        int applyGroupNumber = (int) jsonData.get("applyGroupNumber");
                        mainFragmentView.joinTeamOk(applyGroupNumber);
                    }else{
                        mainFragmentView.joinTeamNo("数据出错！");
                    }
                }else{
                    mainFragmentView.joinTeamNo("系统异常");
                }
            }
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        HttpParams httpParams = new HttpParams();
        url = AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_VAL_JOIN_TEAM;
        OkGoUtil.getStringObservableForGet(url, httpParams, observer);
        return observer;
    }


    /**
     * 检查版本更新
     * @param url
     * @param params
     * @param type
     * @return
     */
    public DisposableObserver<Response<String>> checkAppVersion(final String url, final HttpParams params, final String type){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        org.json.JSONObject data = jsonObject.getJSONObject("data");
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        VersionInfo versionInfo = gsonBuilder.create().fromJson(data.toString(), VersionInfo.class);
                        mainFragmentView.versionInfo(versionInfo);
                    }else {
                        mainFragmentView.checkError(jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    mainFragmentView.checkError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {



                mainFragmentView.checkError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {
//                bootPageView.checkError("完成");
            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }

    /**
     * 更新用户信息
     * @return
     */
    public DisposableObserver<Response<String>> updateUserInfo(String url){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        org.json.JSONObject data = jsonObject.getJSONObject("data");
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        UserInfo userInfo = gsonBuilder.create().fromJson(data.toString(), UserInfo.class);
                        mainFragmentView.updateUserInfo(userInfo);
                    }else {
                        mainFragmentView.checkError(jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    mainFragmentView.checkError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {



                mainFragmentView.checkError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {
//                bootPageView.checkError("完成");
            }
        };

        OkGoUtil.getStringObservableForPost(url,null,observer);
        return observer;
    }
}
