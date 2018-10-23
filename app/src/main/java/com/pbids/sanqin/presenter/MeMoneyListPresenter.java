package com.pbids.sanqin.presenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.ui.activity.me.MeMoneyListFragment;
import com.pbids.sanqin.ui.activity.me.MeMoneyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 12:03
 * @desscribe 类描述:我的资金列表
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeMoneyListFragment
 */
public class MeMoneyListPresenter extends BaasePresenter<MeMoneyListView>{

    //MeMoneyListView meMoneyListView;

/*    public MeMoneyListPresenter(MeMoneyListView meMoneyListView){
        //this.meMoneyListView = meMoneyListView;
    }*/
    public MeMoneyListPresenter(){

    }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode,  HttpJsonResponse rescb) {
        switch (requestCode){
            case MeMoneyListFragment.ME_MONEY_HTTP_REQUEST_LIST_CODE:
                if (resultCode == Const.OK) {
                    String dataStr = rescb.getData().toString();
                    long id=0;
                    float accountBalance=0;
                    int fee = 0;
                    int minAmount = 0;
                    int withdrawalThreshold = 0;
                    List<Accounts> moneyHistories;
                    try {
                        if(!dataStr.equals("")){
                            JSONObject data = rescb.getJsonData();
                            id = data.getLong("id");
                            accountBalance = data.getFloat("accountBalance");
                            fee = data.getInteger("fee");
                            minAmount = data.getInteger("minAmount");
                            withdrawalThreshold = data.getInteger("withdrawalThreshold");
//                            withdrawalThreshold = 2000;
                            moneyHistories=   JSONArray.parseArray( data.get("orders").toString(),Accounts.class) ;
                            updataPurpose(moneyHistories);
                            System.out.print(moneyHistories);
                            mView.getMoneyHistoriesInfo(id, accountBalance, fee, minAmount, moneyHistories,withdrawalThreshold);
                        }else{
                            moneyHistories = new ArrayList<>();
                            mView.getMoneyHistoriesInfo(id, accountBalance, fee, minAmount, moneyHistories,withdrawalThreshold);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mView.loadError();
                        moneyHistories = new ArrayList<>();
                        mView.getMoneyHistoriesInfo(id, accountBalance, fee, minAmount, moneyHistories,withdrawalThreshold);
                    }
                }
                break;
            case MeMoneyListFragment.GET_WITH_DRAWTIME_REQUEST_CODE:
                if(resultCode==Const.OK){
                    int status = rescb.getStatus();
                    if(status==Const.OK){
                        mView.allowWithDraw();
                    }else{
                        mView.errorWithDraw("不在提现时间范围内");
                    }
                }else{
                    mView.errorWithDraw("获取提现时间异常，请稍后重试！");
                }
                break;
            default:
                break;
        }

    }

    /**
     * 更改订单显示状态
     * ordertype 5  orderstatus 1 显示 充值
     * ordertype 6  orderstatus 1 显示 推荐奖励
     * ordertype 1  orderstatus 1 显示 打赏
     * ordertype 3  orderstatus 1 显示 购买超级表情
     * ordertype 2  orderstatus 1 显示 活动报名支付
     * ordertype -1  orderstatus 1 显示 提现
     * ordertype 4  orderstatus 1 显示 提现手续费
     * ordertype -1  orderstatus -2或-1 显示 提现失败退款
     * ordertype 4  orderstatus -2或-1 显示 手续费退款
     * ordertype 2  orderstatus -2或-1 显示 活动退款
     * ordertype 5  orderstatus 1 显示 充值
     * @param moneyHistories
     */
    private void updataPurpose(List<Accounts> moneyHistories) {
        for (Accounts accounts : moneyHistories) {
            if (accounts.getOrderType() == 5 && accounts.getOrderStatus() == 1) {
                accounts.setPurpose("充值");
            } else if (accounts.getOrderType() == 6 && accounts.getOrderStatus() == 1) {
                accounts.setPurpose("推荐奖励");
            } else if (accounts.getOrderType() == 1 && accounts.getOrderStatus() == 1) {
                accounts.setPurpose("打赏");
            } else if (accounts.getOrderType() == 3 && accounts.getOrderStatus() == 1) {
                accounts.setPurpose(" 购买超级表情");
            } else if (accounts.getOrderType() == 2 && accounts.getOrderStatus() == 1) {
                accounts.setPurpose("活动报名支付");
            } else if (accounts.getOrderType() == -1 && accounts.getOrderStatus() == 1) {
                accounts.setPurpose("提现申请");
            } else if (accounts.getOrderType() == 4 && accounts.getOrderStatus() == 1) {
                accounts.setPurpose("提现手续费");
                moneyHistories.remove(accounts);
                updataPurpose(moneyHistories);
                return;
            } else if (accounts.getOrderType() == -1 && accounts.getOrderStatus() == -2) {
                accounts.setPurpose("提现申请");
            } else if (accounts.getOrderType() == -1 && accounts.getOrderStatus() == -1) {
                accounts.setPurpose("提现申请");
            } else if (accounts.getOrderType() == 4 && accounts.getOrderStatus() == -2) {
                accounts.setPurpose("手续费退款");
                moneyHistories.remove(accounts);
                updataPurpose(moneyHistories);
                return;
            } else if (accounts.getOrderType() == 4 && accounts.getOrderStatus() == -1) {
                accounts.setPurpose("手续费退款");
                moneyHistories.remove(accounts);
                updataPurpose(moneyHistories);
                return;
            } else if (accounts.getOrderType() == 2 && accounts.getOrderStatus() == -2) {
                accounts.setPurpose("活动退款");
            } else if (accounts.getOrderType() == 2 && accounts.getOrderStatus() == -1) {
                accounts.setPurpose("活动退款");
            } else {
            }
        }

    /*
    public DisposableObserver<Response<String>> submitInfotmation(String url, HttpParams httpParams, final String type){
        final Context context = ((BaseFragment)meMoneyListView).getActivity();
        if(context==null){
            return null;
        }
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                Log.i("wzh","stringResponse.body(): "+stringResponse.body());
                JsonObject jsonObject = new JsonParser().parse(stringResponse.body()).getAsJsonObject();
//                            JSONObject jsonObject = new JSONObject(stringResponse.body());
                int status = jsonObject.get("status").getAsInt();
                if(status == MyApplication.OK){
                    JsonObject data = jsonObject.getAsJsonObject("data");
//                        JSONObject data = jsonObject.getJSONObject("data");
                    long id = data.get("id").getAsLong();
                    Log.i("wzh","data.get(\"balance\"): "+data.get("balance"));
                    float accountBalance = data.get("accountBalance").getAsFloat();
                    int fee = data.get("fee").getAsInt();
                    int minAmount = data.get("minAmount").getAsInt();
//                        float balance = (float) data.get("balance");
                    Log.i("wzh","data.get(\"balance\"): ----------"+data.get("balance"));
                    JsonArray orders = data.get("orders").getAsJsonArray();
                    Gson gson = new GsonBuilder().create();
                    List<Accounts> moneyHistories = new ArrayList<>();
                    for(int i=0;i<orders.size();i++){
                        Accounts moneyHistory = gson.fromJson(orders.get(i).toString(), Accounts.class);
                        moneyHistories.add(moneyHistory);
                    }
                    meMoneyListView.getMoneyHistoriesInfo(id,accountBalance,fee,minAmount,moneyHistories);
                    meMoneyListView.onHttpSuccess(type);
                }else{
                    meMoneyListView.onHttpError(jsonObject.get("message").getAsString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                meMoneyListView.onHttpError(e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
//        OkGoUtil.getStringObservableForGet(url,httpParams,observer);
        OkGoUtil.getStringObservableForPost(url,httpParams,observer);
        return observer;
    }
    */
    }
}
