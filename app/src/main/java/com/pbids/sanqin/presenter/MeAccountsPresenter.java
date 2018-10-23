package com.pbids.sanqin.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.netease.nim.uikit.common.CommonGroup;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.model.entity.AccountsGroup;
import com.pbids.sanqin.ui.activity.me.MeAccountsFragment;
import com.pbids.sanqin.ui.activity.me.MeAccountsView;
import com.pbids.sanqin.ui.activity.me.MeMoneyListFragment;
import com.pbids.sanqin.utils.OkGoUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:50
 * @desscribe 类描述:我的界面，流水
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeAccountsFragment
 */
public class MeAccountsPresenter extends BaasePresenter<MeAccountsView> {
/*    MeAccountsView meAccountsView;
    public MeAccountsPresenter(MeAccountsView meAccountsView){
        this.meAccountsView = meAccountsView;
    }*/

    public MeAccountsPresenter(){
    }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode,HttpJsonResponse rescb) {
        if ( resultCode == Const.OK && requestCode == MeAccountsFragment.ME_ACCOUNTS_HTTP_REQUEST_LIST_CODE ) {
            //Log.i("wzh","stringResponse: "+ );
            //System.out.print(body);
            JSONObject jsonData = rescb.getJsonData();
            Set keySet = jsonData.keySet();
            Iterator<String> it = keySet.iterator();
            List <ComonRecycerGroup<Accounts>> groups = new ArrayList<>();
            while (it.hasNext()){
                String key = String.valueOf(it.next());
                JSONArray jsonArray = jsonData.getJSONArray(key);
                List<Accounts> accounts = JSONArray.parseArray(jsonArray.toJSONString(), Accounts.class);
                ComonRecycerGroup<Accounts> item = new ComonRecycerGroup();
                item.setHeader(key);
                item.setLists(accounts);
                groups.add(item);
            }
            //解决排序问题
            //groups.sort();
            Comparator comp = new Comparator() {
                public int compare(Object o1, Object o2) {
                    ComonRecycerGroup p1 = (ComonRecycerGroup) o1;
                    ComonRecycerGroup p2 = (ComonRecycerGroup) o2;

                    String t1 = p1.getHeader();
                    String t2 = p2.getHeader();

                    // 字符串=======>时间戳
                    long d1 = getTime(t1);
                    long d2 = getTime(t2);

                    if(d1<d2) {
                        return 1;
                    }else if(d1==d2) {
                        return 0;
                    }else if(d1>d2) {
                        return -1;
                    }
                    return 0;
                }
            };
            Collections.sort(groups,comp);
            mView.getAccountsGroup(groups);
            //List<AccountsGroup> accountsGroups = new ArrayList<>();
            //List<Accounts> moneyHistories =   JSONArray.parseArray( data.get("orders").toString(),Accounts.class) ;
        }
    }
    // 将字符串转为时间戳
    public static Long getTime(String user_time) {
        //String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            return l;
            //String str = String.valueOf(l);
            //re_time = str.substring(0, 10);
        }catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        //return re_time;
        return 0L;
    }


/*
    public DisposableObserver<Response<String>> submitInformation(String url, HttpParams httpParams, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse: "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    List<AccountsGroup> accountsGroups = new ArrayList<>();
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
                    Set keySet = data.keySet();
                    Iterator<String> it = keySet.iterator();
                    while (it.hasNext()){
                        AccountsGroup accountsGroup = new AccountsGroup();
                        String key = String.valueOf(it.next());
                        accountsGroup.setHeader(key);
                        List<Accounts> accountses = new ArrayList<>();
                        JsonArray array =  data.get(key).getAsJsonArray();
                        for(int i =0;i<array.size();i++){
                            Accounts accounts = new GsonBuilder().create().fromJson(array.get(i).toString().toString(), Accounts.class);
                            accountses.add(accounts);
                        }
                        accountsGroup.setAccountses(accountses);
                        accountsGroups.add(accountsGroup);
                    }

                    meAccountsView.getAccountsGroup(accountsGroups);
                    meAccountsView.onHttpSuccess(type);
                }else{
                    meAccountsView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meAccountsView.onHttpError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
*/
}
