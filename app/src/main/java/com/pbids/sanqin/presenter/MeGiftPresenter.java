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
import com.pbids.sanqin.ui.activity.me.MeGiftView;
import com.pbids.sanqin.utils.AddrConst;
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
 * 模块：我的 -> 我的礼券
 * Created by pbids903 on 2018/1/5.
 */

public class MeGiftPresenter extends BasePresenter{

    MeGiftView meGiftView;
    //列表数据
    List<GiftGroup> giftGroup = new ArrayList<GiftGroup>(2);
    GiftGroup giftGroupAvaliable ; //可用列表
    GiftGroup giftGroupUnavailable ; //不可用列表

    public MeGiftPresenter(MeGiftView meGiftView){
        this.meGiftView = meGiftView;
        //初始化分组列表数据
        giftGroupAvaliable = new GiftGroup();
        giftGroupUnavailable = new GiftGroup();
        giftGroupUnavailable.setHeader("已失效的券");
        giftGroup.add(giftGroupAvaliable);
        giftGroup.add(giftGroupUnavailable);
    }

    public void clearListData(){
        giftGroupAvaliable.getGifts().clear();
        giftGroupUnavailable.getGifts().clear();
    }

    public DisposableObserver<Response<String>> submitInformationGiftList(final String url, final HttpParams params, final String type){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","stringResponse: "+stringResponse);
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    if(status== MyApplication.OK){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        //ArrayList<Gift> gifts = new ArrayList<Gift>();
                        for(int i = 0;i<jsonArray.length();i++){
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gift gift = gsonBuilder.create().fromJson(jsonArray.get(i).toString(), Gift.class);
                            //gifts.insert(gift);
                            if(gift.getState()==1){
                                giftGroupAvaliable.addGift(gift);
                            }else{
                                giftGroupUnavailable.addGift(gift);
                            }
                        }
                        //list view_donate_records 更新
                        meGiftView.getMeGiftGroupListAdapter().notifyDataSetChanged();
                        //meGiftView.getGiftList(gifts);
                        meGiftView.onHttpSuccess(type);

                    }else {
                        meGiftView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meGiftView.onHttpError(e.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meGiftView.onHttpError("网络未连接，请检查您的网络！");
            }

            @Override
            public void onComplete() {

            }
        };
        //http post
        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }

    // get group list
    public List<GiftGroup> getGiftGroup(){
        return giftGroup;
    }

    public DisposableObserver<Response<String>> submitInformationUserGift(final String url, final HttpParams params, final String type, final Gift gift){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(stringResponse.body());
                    int status = jsonObject.getInt("status");
                    //Log.i("wzh","submitInformationUserGift: "+stringResponse.body());
                    if(status== MyApplication.OK){
                        giftGroupAvaliable.getGifts().remove(gift);
                        gift.setState(0);
                        giftGroupUnavailable.addGift(gift);
                        //list view_donate_records 更新
                        meGiftView.getMeGiftGroupListAdapter().notifyDataSetChanged();
                        meGiftView.onHttpSuccess(type);
                    }else {
                        meGiftView.onHttpError(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    meGiftView.onHttpError(Const.JSONERROR);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meGiftView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
            }

            @Override
            public void onComplete() {

            }
        };

        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }


    /**
     * 判断列表是否有数据
     * @return
     */
    public boolean hasGiftData() {
        return giftGroupAvaliable.getGifts().size() > 0 || giftGroupUnavailable.getGifts().size() > 0;
    }

    /**
     * 使用礼券
     * @param groupPosition 分组
     * @param childPosition item postion
     */
    public void useCoupon(int groupPosition, int childPosition) {
        Gift gift = giftGroupAvaliable.getGifts().get(childPosition);
        HttpParams params = new HttpParams();
        params.put("id",gift.getId());
        submitInformationUserGift(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USECOUPON,params, ""+childPosition,gift);
    }

    public Gift getGiftAvaliable(int childPosition){
        return giftGroupAvaliable.getGifts().get(childPosition);
    }
}
