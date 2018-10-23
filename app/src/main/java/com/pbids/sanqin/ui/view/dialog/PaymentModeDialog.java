package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.PayView;
import com.pbids.sanqin.event.WechatPayEvent;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.WechatPayInfo;
import com.pbids.sanqin.presenter.PayPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.me.MeFragment;
import com.pbids.sanqin.ui.activity.me.MePayPasswordSetFragment;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.eventbus.SetFullScreenEvent;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:10
 * @desscribe 类描述:支付dialog
 * @remark 备注:
 * @see
 */
public class PaymentModeDialog extends BaseDialog implements PayView{

    @Bind(R.id.news_pay_pop_cancel)
    ImageView newsPayPopCancel;
    @Bind(R.id.news_pay_pop_tv)
    TextView newsPayPopTv;
    @Bind(R.id.news_pay_pop_weixin)
    ImageView newsPayPopWeixin;
    @Bind(R.id.news_pay_pop_weixin_layout)
    LinearLayout newsPayPopWeixinLayout;
    @Bind(R.id.news_pay_pop_zhifubao)
    ImageView newsPayPopZhifubao;
    @Bind(R.id.news_pay_pop_zhifubao_layout)
    LinearLayout newsPayPopZhifubaoLayout;
    @Bind(R.id.news_pay_pop_yinlian)
    ImageView newsPayPopYinlian;
    @Bind(R.id.news_pay_pop_yinlian_layout)
    LinearLayout newsPayPopYinlianLayout;
    @Bind(R.id.news_pay_pop_qianbao)
    ImageView newsPayPopQianbao;
    @Bind(R.id.news_pay_pop_qianbao_layout)
    LinearLayout newsPayPopQianbaoLayout;
    @Bind(R.id.news_pay_pop_confirm)
    Button newsPayPopConfirm;
    @Bind(R.id.tv_pay_title)
    TextView tvPayTitle;

    private String payment = "";
    Context mContext;
    private String account;
    private BaseFragment mFragment;
    private HttpParams mHttpParams;
    private PayPresenter payPresenter;
    PayPasswordDialog payPasswordDialog;
    private IWXAPI wechatApi;

    public static final String PAYMENT_WECHAT = "Wechat";
    public static final String PAYMENT_ZHIFUBAO= "zhifubao";
    public static final String PAYMENT_YINLIAN= "yinlian";
    public static final String PAYMENT_QIANBAO = "qianbao";

    //打赏类型
    public static final int PAY_TYPE_REWARD = 1 ; //打赏
    public static final int PAY_TYPE_SUPERFACE = 2 ; //超级表情

    Map<String,String> map = new HashMap<String, String>();

    //默认为打赏
    public int payType = PAY_TYPE_REWARD;

    public PaymentModeDialog(@NonNull Context context,String account) {
        super(context);
        mContext = context;
        this.account = account;
        init();
    }

    public PaymentModeDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PaymentModeDialog(@NonNull BaseFragment fragment) {
        super(fragment.getContext());
        this.mFragment = fragment;
        payPresenter = new PayPresenter(this);
        wechatApi = WXAPIFactory.createWXAPI(mFragment.getActivity().getApplication(), "wxd61c8cd581e28ad7");
        wechatApi.registerApp("wxd61c8cd581e28ad7");
        init();
    }

    //复位
    public void reset(){
        payType = PAY_TYPE_REWARD;
        this.onPaymentModeLisenear = null;
        this.setPayTitle("打赏金额");

        newsPayPopWeixin.setSelected(false);
        newsPayPopZhifubao.setSelected(false);
        newsPayPopYinlian.setSelected(false);
        newsPayPopQianbao.setSelected(false);
        payment = "";

        //setAccountTv("0.00");
    }

    private void init(){
        // eventbux
        EventBus.getDefault().register(this);
    }


    public void setHttpParams(HttpParams httpParams){
        this.mHttpParams = httpParams;
    }

    @Override
    public void initView() {
        setContentView(R.layout.pop_news_pay);
        ButterKnife.bind(this);
        newsPayPopTv = (TextView) findViewById(R.id.news_pay_pop_tv);
        setGrayCenter();
        setCanceledOnTouchOutside(false);
        newsPayPopTv.setText(""+account);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void dispose(){
        if(mFragment!=null){
            mFragment.dispose();
        }
    }

    //设置金额
    public void setAccount(String account) {
        this.account = account;
    }

    public void setAccountTv(String account){
        this.account = account;
        newsPayPopTv.setText(""+account);
    }

    OnPaymentModeLisenear onPaymentModeLisenear;

    //支付方式
    public void setOnPaymentModeLisenear(OnPaymentModeLisenear onPaymentModeLisenear) {
        this.onPaymentModeLisenear = onPaymentModeLisenear;
    }

    //toast
    private void toast (String message){
        Toast.makeText(mFragment.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.news_pay_pop_cancel, R.id.news_pay_pop_weixin_layout, R.id.news_pay_pop_zhifubao_layout
            , R.id.news_pay_pop_yinlian_layout, R.id.news_pay_pop_qianbao_layout, R.id.news_pay_pop_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_pay_pop_cancel:
                dismiss();
                break;
            case R.id.news_pay_pop_weixin_layout:
                newsPayPopWeixin.setSelected(true);
                newsPayPopZhifubao.setSelected(false);
                newsPayPopYinlian.setSelected(false);
                newsPayPopQianbao.setSelected(false);
                payment = PAYMENT_WECHAT;
                break;
            case R.id.news_pay_pop_zhifubao_layout:
                newsPayPopWeixin.setSelected(false);
                newsPayPopZhifubao.setSelected(true);
                newsPayPopYinlian.setSelected(false);
                newsPayPopQianbao.setSelected(false);
                payment = PAYMENT_ZHIFUBAO;
                break;
            case R.id.news_pay_pop_yinlian_layout:
                newsPayPopWeixin.setSelected(false);
                newsPayPopZhifubao.setSelected(false);
                newsPayPopYinlian.setSelected(true);
                newsPayPopQianbao.setSelected(false);
                payment = "yinlian";
                break;
            case R.id.news_pay_pop_qianbao_layout:
                newsPayPopWeixin.setSelected(false);
                newsPayPopZhifubao.setSelected(false);
                newsPayPopYinlian.setSelected(false);
                newsPayPopQianbao.setSelected(true);
                payment = PAYMENT_QIANBAO;
                break;
            case R.id.news_pay_pop_confirm:
                switch (payment){
                    case "":
                        Toast.makeText(mFragment.getContext(),"请选择付款方式",Toast.LENGTH_SHORT).show();
                        break;
                    case PAYMENT_WECHAT:
                        //微信支付
//                        boolean wechatSupported = wechatApi.isWXAppInstalled()&&wechatApi.isWXAppSupportAPI();
                        boolean wechatSupported = wechatApi.isWXAppInstalled();
                        if(!wechatSupported){
                            //没有安装微信
                            toast("请先安装微信");
                            break;
                        }
                        dismiss();
                        if(onPaymentModeLisenear!=null){
                            onPaymentModeLisenear.onPaymentModeClick(PAYMENT_WECHAT);
                        }
                        mFragment.getLoadingPop("正在提交订单").show();
                        String url = AddrConst.SERVER_ADDRESS_PAYMENT + "/pay/getPayOrder";
                        mFragment.addDisposable(payPresenter.submitInformation( url, mHttpParams, PaymentModeDialog.PAYMENT_WECHAT));
                        break;
                    case PAYMENT_ZHIFUBAO:
                        //支付宝
                        dismiss();
                        if(onPaymentModeLisenear!=null){
                            onPaymentModeLisenear.onPaymentModeClick(PAYMENT_ZHIFUBAO);
                        }
                        mFragment.getLoadingPop("正在提交订单").show();
                        mFragment.addDisposable(payPresenter.submitInformation(
                                AddrConst.SERVER_ADDRESS_PAYMENT + "/pay/getPayOrder", mHttpParams, PaymentModeDialog.PAYMENT_ZHIFUBAO));
                        break;
                    case PAYMENT_YINLIAN:
                        //银联
                        dismiss();
                        //toast(this.account+"");
                        if(onPaymentModeLisenear!=null){
                            onPaymentModeLisenear.onPaymentModeClick(PAYMENT_YINLIAN);
                        }
                        break;
                    case PAYMENT_QIANBAO:
                        //余款
                        if(onPaymentModeLisenear!=null){
                            onPaymentModeLisenear.onPaymentModeClick(PAYMENT_QIANBAO);
                        }
                        int isSetPayword = MyApplication.getUserInfo().getIsSetPayword();
                        //如果没有设置支付密码 去设置支付密码
                        if (isSetPayword == 0){
                            dismiss();
                            //如果是横屏 需要转为竖屏
                            SetFullScreenEvent  evt = new SetFullScreenEvent(false,SetFullScreenEvent.TYPE_IN_WBE);
                            EventBus.getDefault().post(evt);
                            if(onPaymentModeLisenear!=null){
                                onPaymentModeLisenear.onPaymentModeClick(PAYMENT_QIANBAO);
                            }
                            MePayPasswordSetFragment setFragment = MePayPasswordSetFragment.newInstance();
                            setFragment.getArguments().putString("type", "set");
                            setFragment.getArguments().putString("jump", "1");
                            mFragment.setOnPayPasswordSetLisenear(new BaseFragment.OnPayPasswordSetLisenear() {
                                @Override
                                public void setOver() {
                                    payPasswordDialogShow();
                                }
                            });
                            mFragment.start(setFragment);
                        } else if (isSetPayword == 1) {
                            dismiss();
                            payPasswordDialogShow();
                        }
                        break;
                }
                break;
        }
    }


    public void stopEvent(){
        EventBus.getDefault().unregister(this);
    }


    public void payPasswordDialogShow(){
        if(payPasswordDialog==null){
            payPasswordDialog = new PayPasswordDialog(mFragment.getContext());
        }
        payPasswordDialog.setOnScanOverLisenear(new PayPasswordDialog.OnScanOverLisenear(){
            @Override
            public void onOver(String password) {
                mFragment.getLoadingPop("正在提交订单").show();
                mHttpParams.put("payword",password);
                mFragment.addDisposable(payPresenter.submitInformation(AddrConst.SERVER_ADDRESS_PAYMENT
                        + AddrConst.ADDRESS_ACCOUNT_BALANCEPAY, mHttpParams, PaymentModeDialog.PAYMENT_QIANBAO));
            }
        });
        payPasswordDialog.show();
    }

    @Override
    public void getPayInfo(final String info) {
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Message> message) throws Exception {
                PayTask alipay = new PayTask(mFragment.getActivity());
                Map<String, String> map = alipay.payV2(info, true);
                //Log.i("wzh", "map: " + map.toString());
                String resultStatus = map.get("resultStatus");
                Message msg = new Message();
                msg.obj = resultStatus;
                message.onNext(msg);
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Message>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Message message) throws Exception {
                //Log.i("wzh", "((String)message.obj): " + ((String) message.obj));
                String resultStatus = (String) message.obj;
//                zzHomeWebview.loadUrl("javascript:rewardCb(1);");
                map.clear();
                if(resultStatus.equals("9000")){
                    map.put("status","1");
                }else{
                    map.put("status","0");
                    map.put("errorMessage","支付失败");
                }
                map.put("payCode",PAYMENT_ZHIFUBAO);
                if(onPaymentModeLisenear!=null){
                    onPaymentModeLisenear.payOverInfo(map);
                }

            }
        });
    }

    @Override
    public void getPayInfoForBalance(String info) {
        //更新用户信息显示
        updateUserInfo(info);
        map.clear();
        map.put("status","1");
        map.put("payCode",PAYMENT_QIANBAO);
        if(onPaymentModeLisenear!=null){
            onPaymentModeLisenear.payOverInfo(map);
        }
    }

    @Override
    public void getPayInfoForBalanceError(String info) {
        map.clear();
        map.put("status","0");
        map.put("errorMessage",info);
        map.put("payCode",PAYMENT_QIANBAO);
        if(onPaymentModeLisenear!=null){
            onPaymentModeLisenear.payOverInfo(map);
        }
    }

    //微信支付
    @Override
    public void getPayInfoForWechat(final WechatPayInfo payInfo) {
        //调起微信APP的对象
        // 下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
//        request.appId = payInfo.getAppid();
//        request.partnerId = payInfo.getPartnerid();
//        request.prepayId = payInfo.getPrepayid();
//        request.packageValue = "Sign=WXPay";
//        request.nonceStr = payInfo.getNoncestr();
//        request.timeStamp = String.valueOf(new Date().getTime());
//        request.sign = payInfo.getSign();
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayReq request = new PayReq();
                request.appId			= payInfo.getAppid();
                request.partnerId		= payInfo.getPartnerid();
                request.prepayId		= payInfo.getPrepayid();
                request.nonceStr		= payInfo.getNoncestr();
                request.timeStamp		= payInfo.getTimeStamp();
                request.packageValue	= payInfo.getPackageValue();
                request.sign			= payInfo.getSign();
//        request.extData			= "app data"; // optional
                wechatApi.sendReq(request);
            }
        }).start();

//        Runnable payRunnable = new Runnable() {
//            //这里注意要放在子线程
//            @Override public void run() {
//                Log.i("wzh","payInfo: "+payInfo.toString());
//                Log.i("wzh","timeStamp: "+String.valueOf(new Date().getTime()));
//                PayReq request = new PayReq();
//                //调起微信APP的对象
//                // 下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
//                request.appId = payInfo.getAppid();
//                request.partnerId = payInfo.getPartnerid();
//                request.prepayId = payInfo.getPrepayid();
//                request.packageValue = "Sign=WXPay";
//                request.nonceStr = payInfo.getNoncestr();
//                request.timeStamp = String.valueOf(new Date().getTime());
//                request.sign = payInfo.getSign();
//                wechatApi.sendReq(request);
//                //发送调起微信的请求
//                } };
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();

//        Observable.create(new ObservableOnSubscribe<Message>() {
//            @Override
//            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Message> message) throws Exception {
//
////                WechatPayInfo payInfo = new GsonBuilder().create().fromJson(data.toString(),WechatPayInfo.class);
////                if(payInfo==null){
////                    return;
////                }
//                wechatApi = WXAPIFactory.createWXAPI(mFragment.getActivity().getApplication(), "wxd61c8cd581e28ad7");
//                Log.i("wzh","payInfo: "+payInfo.toString());
//                Log.i("wzh","timeStamp: "+String.valueOf(new Date().getTime()));
//                PayReq req = new PayReq();
//
//                req.appId = payInfo.getAppid();//appid
//                req.partnerId = payInfo.getPartnerid();//商户号
//                req.prepayId = payInfo.getPrepayid();//预支付交易会话ID
//                req.nonceStr = payInfo.getNoncestr();//随机字符串
//                req.timeStamp = String.valueOf(new Date().getTime());//时间戳
//                req.packageValue = "Sign=WXPay";//固定字符串
//                req.sign = payInfo.getSign();//签名
////                req.extData = "app data"; // optional，微信不处理该字段，会在PayResp结构体中回传该字段
////                req.
//
//                wechatApi.sendReq(req);
////                Message msg = new Message();
////                msg.obj = resultStatus;
////                message.onNext(msg);
////                msg.what = SDK_PAY_FLAG;
////                msg.obj = result;
//            }
//        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Message>() {
//            @Override
//            public void accept(@io.reactivex.annotations.NonNull Message message) throws Exception {
//            }
//        });
    }

    public void updateUserInfo(String info){
        float balance = Float.valueOf(info);
        UserInfo userInfo = MyApplication.getUserInfo();
        userInfo.setAccountBalance(Float.valueOf(info));
        UserInfoManager.updateUserInfo(mFragment.getContext(), userInfo);

        MainFragment mainFragment = mFragment.findFragment(MainFragment.class);
        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        meFragment.updateAccountBalance(balance);
    }

    public void payPasswordDialogDismiss(){
        if(payPasswordDialog!=null && payPasswordDialog.isShowing()){
            payPasswordDialog.dismiss();
            payPasswordDialog.clearPassword();
        }
    }

    @Override
    public void onHttpSuccess(String type) {
        mFragment.dismiss();
        if (type.equals(PAYMENT_QIANBAO)) {
            //余款支付返回
            if (payPasswordDialog != null && payPasswordDialog.isShowing()) {
                payPasswordDialog.dismiss();
                payPasswordDialog.clearPassword();
            }
        }
    }

    @Override
    public void onHttpError(String type) {
        mFragment.dismiss();
        if(payPasswordDialog!=null){
            payPasswordDialog.clearPassword();
        }
    }

    //设置支付标题
    public void setPayTitle(String payTitle) {
        if(tvPayTitle!=null){
            tvPayTitle.setText(payTitle);
        }
    }

    //map status0,1
    public interface OnPaymentModeLisenear {
        void onPaymentModeClick(String paymentMode);
        void payOverInfo(Map<String,String> map);
    }

    //支持成功
    private void onPaySuccess(String payCode) {
        map.clear();
        map.put("status","1");
        map.put("payCode",payCode);
        if(onPaymentModeLisenear!=null){
            onPaymentModeLisenear.payOverInfo(map);
        }
    }

    //支付失败
    private void onPayError(String payCode,String errorMsg) {
        map.clear();
        map.put("status","0");
        map.put("errorMessage",errorMsg);
        map.put("payCode",payCode);
        if(onPaymentModeLisenear!=null) {
            onPaymentModeLisenear.payOverInfo(map);
        }
    }

    //微信支付后处理显示结果
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWechatPayEvent(WechatPayEvent evt){
        BaseResp resp = evt.getResp();
        if (resp.errCode==0){
            onPaySuccess(PAYMENT_WECHAT);
            // Toast.makeText(getContext(),"微信支付成功",Toast.LENGTH_LONG).show();
        }else if (resp.errCode==-2){
            onPayError(PAYMENT_WECHAT,"用户取消支付");
            // Toast.makeText(getContext(),"用户取消支付",Toast.LENGTH_LONG).show();
        }else {
            onPayError(PAYMENT_WECHAT,"微信支付失败");
            // Toast.makeText(getContext(),"微信支付失败",Toast.LENGTH_LONG).show();
        }
    }
}
