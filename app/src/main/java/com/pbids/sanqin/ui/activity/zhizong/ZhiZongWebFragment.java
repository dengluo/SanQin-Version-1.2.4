package com.pbids.sanqin.ui.activity.zhizong;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.NewsArticleManager;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.me.MeFeedbackFragment;
import com.pbids.sanqin.ui.activity.zongquan.BrickFragment;
import com.pbids.sanqin.ui.pay.union_pay.UnionPayActivity;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.DaShangSuccessDialog;
import com.pbids.sanqin.ui.view.dialog.PaymentModeDialog;
import com.pbids.sanqin.ui.view.dialog.RewardDialog;
import com.pbids.sanqin.ui.view.dialog.SharedDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.ScreenUtils;
import com.pbids.sanqin.utils.Shotter;
import com.pbids.sanqin.utils.eventbus.DeleteArticleFavorItemEvent;
import com.pbids.sanqin.utils.eventbus.SetFullScreenEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Decoder.BASE64Decoder;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:09
 * @desscribe 类描述:资讯详情界面
 * @remark 备注:
 * @see
 */
public class ZhiZongWebFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, ZhiZongWebView {

    public static final int UNION_PAY_REUQEST_CODE = 2074;

    @Bind(R.id.zz_home_webview)
    WebView zzHomeWebview;
    SharedDialog sharedDialog;
    @Bind(R.id.view_stub)
    View viewStub;

    //支付方式
    PaymentModeDialog paymentModeDialog;

    boolean first = false;
    AppToolBar mToolBar;
    String right="";

    private String faceUrl ;
    private String blance;
    private String faceName;
    private long aid ;
    private int faceId ;
    private String link;

    public static ZhiZongWebFragment newInstance() {
        ZhiZongWebFragment fragment = new ZhiZongWebFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = _mActivity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = _mActivity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhizong_web, container, false);
        ButterKnife.bind(this, view);
        initView();
        EventBus.getDefault().register(this);
        return view;
    }

    @JavascriptInterface
    private void initView() {
        link = getArguments().getString("link");
        if(!isNetworkConnected(getContext())){
            toast("网络已断开，请检查您的网络");
            return;
        }
        WebSettings settings = zzHomeWebview.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setDefaultTextEncodingName("utf-8");//设置网页默认编码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);
        /*
        5.0 以后的WebView加载的链接为Https开头，但是链接里面的内容，比如图片为Http链接，这时候，图片就会加载不出来，怎么解决？
        Android 5.0上Webview默认不允许加载Http与Https混合内容：
        MIXED_CONTENT_ALWAYS_ALLOW 允许从任何来源加载内容，即使起源是不安全的；
        MIXED_CONTENT_NEVER_ALLOW 不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
        MIXED_CONTENT_COMPLTIBILITY_MODE 当涉及到混合式内容时，WebView会尝试去兼容最新Web浏览器的风格；
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            settings.setMixedContentMode(settings.getMixedContentMode());
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        zzHomeWebview.setWebChromeClient(new WebChromeClient(

        ));
        zzHomeWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
/*                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                Log.i("wzh", "url: " + url);
                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                    Map<String, String> extraHeaders = new HashMap<String, String>();
                    extraHeaders.put("token", getToken());
                    view.loadUrl(url, extraHeaders);
                    return true;
                }*/
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        zzHomeWebview.addJavascriptInterface(this, "sanqin");
//        settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式

//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setAppCacheEnabled(true);//缓存
//        if (Build.VERSION.SDK_INT >= 19) {
//            settings.setCacheMode(WebSettings.);//缓存
//        }
//        Map<String,String> extraHeaders = new HashMap<String, String>();
        //extraHeaders.put("Referer", "http://www.google.com");
//        extraHeaders.put("token", getToken());
//        zzHomeWebview.loadUrl(link,extraHeaders);
        zzHomeWebview.loadUrl(link);

    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        mToolBar=toolBar;
        toolBar.setOnToolBarClickLisenear(this);
        if(link.contains(AddrConst.ADDRESS_NEWARTICLE_JIACI)){
            mToolBar.setLeftArrowCenterTextTitleRightText("","烧砖",_mActivity);
            right = "brick";
        }else{
            toolBar.setLeftArrowCenterTextTitle("", _mActivity);
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                if (zzHomeWebview.canGoBack()) {
                    zzHomeWebview.goBack();
                } else {
                    pop();
                }
                break;
            case R.id.main_right_layout:
                switch (right){
                    case "brick":
                        start(BrickFragment.newInstance());
                        break;
                }
                break;
        }
//        zzHomeWebview.reload();
//        pop();
    }

    @Override
    public void onPause() {
        super.onPause();
        zzHomeWebview.loadUrl("javascript:stopAllMedia();");
        //zzHomeWebview.onPause();
        //zzHomeWebview.pauseTimers();
    }

    @Override
    public void onStart() {
        super.onStart();
        //zzHomeWebview.onResume();
        //zzHomeWebview.resumeTimers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        if(paymentModeDialog!=null){
            paymentModeDialog.dispose();
        }
        if(zzHomeWebview != null) {
            // zzHomeWebview.clearHistory();
            // zzHomeWebview.clearCache(true);
            zzHomeWebview.loadUrl("about:blank");
            zzHomeWebview.freeMemory();

            // zzHomeWebview.pauseTimers();
            zzHomeWebview.destroy();
            zzHomeWebview = null;
        }
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        _mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @JavascriptInterface
    public String userFavor(final String sta, final String b64Str) {
        try{
            //处理收藏文章
            _mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String articleJson = decoderBase64(b64Str);
                    NewsArticle article = com.alibaba.fastjson.JSONObject.parseObject(articleJson,NewsArticle.class);
                    int status = Integer.parseInt(sta);
                    DeleteArticleFavorItemEvent evt = new DeleteArticleFavorItemEvent();
                    evt.setStatus( status );
                    evt.setArticle(article);
                    EventBus.getDefault().post(evt);
                }
            });

        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    @JavascriptInterface
    public String getUid() {
        return "" + MyApplication.getUserInfo().getUserId();
    }

    @JavascriptInterface
    public void setTopBar(final String left, final String title, final String right) {
        this.right = right;
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToolBar.removeAllViews();
                if("back".equals(left) && "".equals(right)){
                    mToolBar.setLeftArrowCenterTextTitle(title,_mActivity);
                }else if("back".equals(left) && "brick".equals(right)){
                    mToolBar.setLeftArrowCenterTextTitleRightText(title,"烧砖",_mActivity);
                }else if(!"back".equals(left) && "".equals(right)){
                    mToolBar.setLeftTextCenterTextTitle(left,title,_mActivity);
                }
            }
        });
    }

    @JavascriptInterface
    public String getUsername() {
        return "" + MyApplication.getUserInfo().getName();
    }

    // 打开新的web
    @JavascriptInterface
    public void openWeb(final String url, String fag) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ZhiZongWebFragment webFragment = ZhiZongWebFragment.newInstance();
                webFragment.getArguments().putString("link", url);
                start(webFragment);
            }
        });

    }

    @JavascriptInterface
    public String getToken() {
        return "" + MyApplication.getUserInfo().getToken();
    }

	@JavascriptInterface
	public String getUserInfo() {
		Gson gson = new Gson();
		return gson.toJson(MyApplication.getUserInfo());
	}

    @JavascriptInterface
    public void saveHistory(String json) {
        String str = decoderBase64(json);
        if (!"".equals(str)) {
            NewsArticle newsArticle = new GsonBuilder().create().fromJson(str, NewsArticle.class);
            if (newsArticle != null) {
                newsArticle.setBrowseTime(new Date().getTime());
                newsArticle.set_id(Long.valueOf("" + newsArticle.getId()));
                NewsArticleManager.insertNewsArticle(_mActivity, newsArticle);
            }
        }
    }

    @JavascriptInterface
    public void surnameFollow(String result){
        if (!"".equals(result)) {
            JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
            int status = jsonObject.get("status").getAsInt();
            if(status == MyApplication.OK){
                JsonObject data = jsonObject.get("data").getAsJsonObject();
                final UserInfo userInfo = new GsonBuilder().create().fromJson(data.toString(), UserInfo.class);
                if (userInfo != null) {
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainFragment mainFragment = findFragment(MainFragment.class);
                            mainFragment.updateVPNameLayout(userInfo);
                        }
                    });
                }
            }
        }
    }

    @JavascriptInterface
    public void participateInActivities(String json){
        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        long aid = data.get("aid").getAsLong();
        float price = data.get("price").getAsFloat();
        float discountNum = data.get("discountNum").getAsFloat();
        long tid = data.get("tid").getAsLong();
        String title = data.get("title").getAsString();

        CampaignEnrollFragment fragment = CampaignEnrollFragment.newInstance();
        fragment.getArguments().putString("key","webview");
        fragment.getArguments().putLong("aid",aid);
        fragment.getArguments().putFloat("price",price);
        fragment.getArguments().putFloat("discountNum",discountNum);
        fragment.getArguments().putLong("tid",tid);
        fragment.getArguments().putString("title",title);
        startForResult(fragment,1);
    }

    @JavascriptInterface
    public void enterCrossScreen(String val){
        switch (val) {
            case "0":
                if(first){
                    isFullScreen = "0";
                    _mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setToolBarVisiable();
                            boolean isNavigationBar = checkDeviceHasNavigationBar(_mActivity);
                            if(isNavigationBar){
                                showNavigationBar(true);
                            }
                            ScreenUtils.setFullScreen(_mActivity, 0);
                        }
                    });
                }
                break;
            case "1":
                first = true;
                isFullScreen = "1";
                _mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setToolBarGone();
                        boolean isNavigationBar = checkDeviceHasNavigationBar(_mActivity);
                        if(isNavigationBar){
                            showNavigationBar(false);
                        }
                        ScreenUtils.setFullScreen(_mActivity, 1);
                    }
                });
                break;
        }
    }

    @JavascriptInterface
    public void sendMess(String val) {
        //Log.i("wzh", "sendMess: " + val);
    }


    // 打赏文章
    RewardDialog rewardDialog;

    private PaymentModeDialog getPaymentModeDialog() {
        if (paymentModeDialog != null) {
            paymentModeDialog.setOnPaymentModeLisenear(null);
            paymentModeDialog.stopEvent(); // stop eventbus
            paymentModeDialog = null;
        }
        paymentModeDialog = new PaymentModeDialog(this);
        return paymentModeDialog;
    }

    @JavascriptInterface
    public void reward(final String aid) {
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //paymentModeDialog = new PaymentModeDialog(ZhiZongWebFragment.this);
                rewardDialog = new RewardDialog(_mActivity);
                rewardDialog.setType("cash");
                rewardDialog.setOnDialogClickLisenrar(new OnDialogClickListener(){
                    @Override
                    public void confirm(View view) {
                        if (view.getTag() == null) {
                            return;
                        }
                        final String blance = (String) view.getTag();
                        if ("".equals(blance)) {
                            Toast.makeText(_mActivity, "请输入打赏金额", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if ("0".equals(blance)) {
                            Toast.makeText(_mActivity, "请输入打赏金额", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        rewardDialog.dismiss();

                        paymentModeDialog = getPaymentModeDialog();
                        paymentModeDialog.setAccountTv(blance);
                        paymentModeDialog.setOnPaymentModeLisenear(new PaymentModeDialog.OnPaymentModeLisenear() {
                            @Override
                            public void onPaymentModeClick(String paymentMode) {
                                HttpParams params = new HttpParams();
                                params.put("totalAmount", blance);
                                params.put("purpose", Const.PURPOSE_REWARD);
                                params.put("orderType", "1");
                                params.put("clientIp", AppUtils.getIp());
                                params.put("aid", Long.valueOf(aid));
                                switch (paymentMode) {
                                    case PaymentModeDialog.PAYMENT_WECHAT:
                                        //微信
                                        params.put("payCode", "wechatpay");
/*                                        HttpParams params3 = new HttpParams();
                                        params3.put("totalAmount", blance);
                                        params3.put("payCode", "wechatpay");
                                        params3.put("purpose", "打赏");
                                        params3.put("orderType", "1");
                                        params3.put("clientIp", AppUtils.getIp());
                                        params3.put("aid", Long.valueOf(aid));
                                        paymentModeDialog.setHttpParams(params3);*/

                                        break;
                                    case PaymentModeDialog.PAYMENT_ZHIFUBAO:
                                        //支付宝
                                        params.put("payCode", "alipay");
/*                                        HttpParams params = new HttpParams();
                                        params.put("totalAmount", blance);
                                        params.put("payCode", "alipay");
                                        params.put("purpose", "打赏");
                                        params.put("orderType", "1");
                                        params.put("aid", Long.valueOf(aid));
                                        paymentModeDialog.setHttpParams(params);*/

                                        break;
                                    case PaymentModeDialog.PAYMENT_YINLIAN:
                                        //银联
                                        paymentModeDialog.dismiss();
                                        /*MeUnionPayFragment fragment_zc_rangking = MeUnionPayFragment.newInstance();
                                        fragment_zc_rangking.getArguments().putString("totalAmount", blance);
                                        start(fragment_zc_rangking);*/
                                        Intent intent = new Intent(_mActivity, UnionPayActivity.class);
                                        intent.putExtra("pay",PaymentModeDialog.PAYMENT_YINLIAN);
                                        intent.putExtra("totalAmount", blance);
                                        intent.putExtra("purpose", Const.PURPOSE_REWARD);
                                        intent.putExtra("aid", aid);
                                        startActivityForResult(intent,UNION_PAY_REUQEST_CODE);
                                        return;
                                    case PaymentModeDialog.PAYMENT_QIANBAO:
                                        //钱包
/*                                        HttpParams params2 = new HttpParams();
                                        params2.put("totalAmount", blance);
                                        params2.put("purpose", "打赏");
                                        params2.put("orderType", "1");
                                        params2.put("aid", Long.valueOf(aid));
                                        paymentModeDialog.setHttpParams(params2);*/
                                        break;
                                }
                                paymentModeDialog.setHttpParams(params);
                            }

                            @Override
                            public void payOverInfo(final Map<String, String> map) {
                                final String status = map.get("status");
                                final String payCode = map.get("payCode");
                                _mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if("1".equals(status)){
                                            Bundle bundle = new Bundle();
                                            bundle.putString("payCode",payCode);
                                            DaShangSuccessDialog successDialog = new DaShangSuccessDialog(_mActivity);
                                            successDialog.show();
                                            //回调 webview
                                            //zzHomeWebview.loadUrl("javascript:rewardCb(1);");
                                            rewardPayOver();
                                        }else if("0".equals(status)){
                                            String errorMessage = map.get("errorMessage");
                                            Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        paymentModeDialog.show();
                    }

                    //
                    @Override
                    public void cancel(View view) {

                    }
                });
                rewardDialog.show();
            }
        });
    }

    //显示系统消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onSetFullScreenEvent(SetFullScreenEvent evt){
        if(zzHomeWebview!=null){
            if(evt.getType() == SetFullScreenEvent.TYPE_IN_WBE){
                if(evt.isSta()){
                    if(!isFullScreen.equals("1")){
                        // enter full screen
                        zzHomeWebview.loadUrl("javascript:enterCrossScreen();");
                    }
                }else {
                    if(!isFullScreen.equals("0")) {
                        // exit full screen
                        zzHomeWebview.loadUrl("javascript:exitCrossScreen();");
                    }
                }
            }
        }

    }

    //表情支付
    @JavascriptInterface
    public void superFacePay(final String json){
        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                JsonObject data = new JsonParser().parse(json).getAsJsonObject();
//                final long aid = data.get("aid").getAsLong();
//                final int faceId = data.get("face_id").getAsInt();
//                final String faceUrl = data.get("face_url").getAsString();
//                final String blance = data.get("face_price").getAsString();
//                final String faceName = data.get("face_name").getAsString();

                faceUrl = data.get("face_url").getAsString();
                blance = data.get("face_price").getAsString();
                faceName = data.get("face_name").getAsString();
                faceId = data.get("face_id").getAsInt();
                aid = data.get("aid").getAsLong();

                //paymentModeDialog = new PaymentModeDialog(ZhiZongWebFragment.this);
                paymentModeDialog = getPaymentModeDialog();
                paymentModeDialog.setAccountTv(blance);
                paymentModeDialog.setPayTitle("超级表情");
                //设置支付类型
                paymentModeDialog.payType = PaymentModeDialog.PAY_TYPE_SUPERFACE;
                paymentModeDialog.setOnPaymentModeLisenear(new PaymentModeDialog.OnPaymentModeLisenear() {
                    @Override
                    public void onPaymentModeClick(String paymentMode) {
                        HttpParams params = new HttpParams();
                        params.put("totalAmount", blance);
                        //params.put("payCode", "wechatpay");
                        params.put("purpose", Const.PURPOSE_SUPER_EXPRESSION);
                        params.put("orderType", "3");
                        params.put("clientIp", AppUtils.getIp());
                        params.put("faceId", faceId);
                        params.put("aid", Long.valueOf(aid));

                        switch (paymentMode) {
                            case PaymentModeDialog.PAYMENT_WECHAT:
                                //微信
                                params.put("payCode", "wechatpay");
                              /*  HttpParams params3 = new HttpParams();
                                params3.put("totalAmount", blance);
                                params3.put("payCode", "wechatpay");
                                params3.put("purpose", "超级表情");
                                params3.put("orderType", "3");
                                params3.put("clientIp", AppUtils.getIp());
                                params3.put("faceId", faceId);
                                params3.put("aid", Long.valueOf(aid));
                                paymentModeDialog.setHttpParams(params3);*/
                                break;
                            case PaymentModeDialog.PAYMENT_ZHIFUBAO:
                                //支付宝
                                params.put("payCode", "alipay");
                               /* HttpParams params = new HttpParams();
                                params.put("totalAmount", blance);
                                params.put("payCode", "alipay");
                                params.put("purpose", "超级表情");
                                params.put("orderType", "3");
                                params.put("faceId", faceId);
                                params.put("aid", Long.valueOf(aid));
                                paymentModeDialog.setHttpParams(params);*/
                                break;
                            case PaymentModeDialog.PAYMENT_QIANBAO:
                                //余款
                               /* HttpParams params2 = new HttpParams();
                                params2.put("totalAmount", blance);
                                params2.put("purpose", "超级表情");
                                params2.put("orderType", "3");
                                params2.put("faceId", faceId);
                                params2.put("aid", Long.valueOf(aid));
                                paymentModeDialog.setHttpParams(params2);*/
                                break;

                            case PaymentModeDialog.PAYMENT_YINLIAN:
                                //银联
                                paymentModeDialog.dismiss();
                                        /*MeUnionPayFragment fragment_zc_rangking = MeUnionPayFragment.newInstance();
                                        fragment_zc_rangking.getArguments().putString("totalAmount", blance);
                                        start(fragment_zc_rangking);*/
                                Intent intent = new Intent(_mActivity, UnionPayActivity.class);
                                intent.putExtra("pay",PaymentModeDialog.PAYMENT_YINLIAN);
                                intent.putExtra("totalAmount", blance);
                                intent.putExtra("purpose", Const.PURPOSE_SUPER_EXPRESSION);
                                intent.putExtra("aid",  aid+"");
                                startActivityForResult(intent,UNION_PAY_REUQEST_CODE);
                                return;
                        }
                        paymentModeDialog.setHttpParams(params);
                    }

                    @Override
                    public void payOverInfo(Map<String, String> map) {
                        String status = map.get("status");
                        String payCode = map.get("payCode");
                        if("1".equals(status)){
                            Bundle bundle = new Bundle();
                            bundle.putString("payCode",payCode);
                            //表情支付回调
                            superFacePayOver( );

                        }else if("0".equals(status)){
                            String errorMessage = map.get("errorMessage");
                            Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                paymentModeDialog.show();
            }
        });
    }
    //打赏支付完成
    private void rewardPayOver(){
        if(zzHomeWebview !=null){
            //回调 webview 打赏
            zzHomeWebview.loadUrl("javascript:rewardCb(1);");
        }
    }

    //表情支付回调
    private void superFacePayOver(){
        Map<String,String> resultMap = new HashMap<String, String>();
        resultMap.put("face_url",faceUrl);
        resultMap.put("face_name",faceName);
        //超级表情支付完成显示表情
        final String gsonStr = new Gson().toJson(resultMap).toString();

        //解决如果不是横屏先转向横屏，只有横屏才可以展示表情 ，
        //解决设置密码后没有恢复横屏
        //延时显示 解决横屏切换问题
        if(zzHomeWebview !=null){
            if( !isFullScreen.equals("1") ) {
                 zzHomeWebview.loadUrl("javascript:enterCrossScreen();");
            }
        }

        _mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1200);
                    if(zzHomeWebview !=null){
                        zzHomeWebview.loadUrl("javascript:showSuperFace("+gsonStr+");");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @JavascriptInterface
    public void feedback() {
        start(MeFeedbackFragment.newInstance());
    }

    //直播截屏
    @JavascriptInterface
    public void screenShot(String aid) {
        Log.i("cgl", "screenShot .... "  );
        startShot(); // 截屏
    }

    @JavascriptInterface
    public void shareTo(String newsArticleStr) {
        String str = decoderBase64(newsArticleStr);
        if ("".equals(str)) {
            Toast.makeText(_mActivity, "类解析失败", Toast.LENGTH_SHORT).show();
            return;
        }
        NewsArticle newsArticle = new GsonBuilder().create().fromJson(str, NewsArticle.class);
        if (newsArticle == null) {
            Toast.makeText(_mActivity, "类解析失败", Toast.LENGTH_SHORT).show();
            return;
        }
        sharedDialog = new SharedDialog(_mActivity, newsArticle);
        sharedDialog.show();
    }

    public String decoderBase64(String newsArticleStr) {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] b = new byte[0];
        //Log.i("wzh", "b: " + b.length);
        try {
            b = base64Decoder.decodeBuffer(newsArticleStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (b.length == 0) {
            return "";
        }
        return new String(b);
    }

    private void showNavigationBar(boolean show){
        try {
            if(show){
                _mActivity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                );
//                _mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }else{
                // Set the IMMERSIVE flag.
                // Set the content to appear under the system bars so that the content
                // doesn't resize when the system bars hide and show.
                _mActivity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
//                                | View.SYSTEM_UI_FLAG
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
//        zzHomeWebview.loadUrl("javascript:window.sanqin.isFullScreen(javascript:isFullScreen());");
        if("1".equals(isFullScreen)){
            zzHomeWebview.loadUrl("javascript:exitCrossScreen();");
//            enterCrossScreen("0");
            return true;
        }
        if (zzHomeWebview.canGoBack()) {
            zzHomeWebview.goBack();
        }else{
            pop();
        }
        return true;
//        return super.onBackPressedSupport();
    }
    //全屏状态
    private String isFullScreen = "0";

    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            zzHomeWebview.loadUrl("javascript:participateInActivitiesCb(1);");
        }
    }





    /*****************************截屏*******************************/
    public static final int REQUEST_MEDIA_PROJECTION = 0x2893;
    private static final int MAIN_PERMISSIONS_REQUEST_CALL = 6591;
    private static Intent mResultData = null;
    private Shotter shotter = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode == RESULT_OK && data != null) {
                    if (mResultData == null) {
                        mResultData = data;
                        shotter = new Shotter(_mActivity, data);
                        requestStoragePermission();
                    }
                } else {
                    toast("系统截图授权失败");
                }
                break;
            case UNION_PAY_REUQEST_CODE:
                if (data != null && data.hasExtra("pay") && data.hasExtra("status") && data.hasExtra("purpose")) {
                    String pay = data.getStringExtra("pay");
                    String status = data.getStringExtra("status");
                    final String purpose = data.getStringExtra("purpose");
                    if (pay.contains("union") && status.contains("1")) {
                        //银生宝支付完成
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //分为打赏 和 超级表情
                                // todo
                                if(purpose.contains(Const.PURPOSE_REWARD)){
                                    //回调 webview 打赏
                                    //zzHomeWebview.loadUrl("javascript:rewardCb(1);");
                                    rewardPayOver();
                                }else if(purpose.contains(Const.PURPOSE_SUPER_EXPRESSION)){
                                    //回调 webview 超级表情
                                    /*Map<String,String> resultMap = new HashMap<String, String>();
                                    resultMap.put("face_url",faceUrl);
                                    resultMap.put("face_name",faceName);

                                    //超级表情支付完成显示表情
                                    String gsonStr = new Gson().toJson(resultMap).toString();*/
                                    superFacePayOver();
                                }else if(purpose.contains(Const.PURPOSE_ACTIVE_PAYMENT)){
                                    //回调 webview 活动支付

                                }
                            }
                        });
                    }
                }
        }
    }

    private void requestStoragePermission(){
        //第二个参数是需要申请的权限
        if (ContextCompat.checkSelfPermission(_mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(_mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MAIN_PERMISSIONS_REQUEST_CALL);
        }else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            onStartShot();
        }
    }

    @TargetApi( Build.VERSION_CODES.LOLLIPOP)
    public void requestScreenShot() {
        startActivityForResult( getMediaProjectionManager() .createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
    }
    @TargetApi( Build.VERSION_CODES.LOLLIPOP)
    private MediaProjectionManager getMediaProjectionManager() {
        return (MediaProjectionManager) _mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }
    private void startShot(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            toast("版本过低,无法截屏");
            return;
        }
        if(shotter==null){
            //截屏权限请求
            requestScreenShot();
            return;
        }
        //第二个参数是需要申请的权限
        requestStoragePermission();
    }

    private void onStartShot(){
        shotter.startScreenShot(new Shotter.OnShotListener() {
            @Override
            public void onFinish() {
                toast("截屏完成,已保存在相册里");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MAIN_PERMISSIONS_REQUEST_CALL){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //截屏
                onStartShot();
            } else {
                // Permission Denied
                toast("授权失败");
            }
        }
    }

    /*****************************截屏end*******************************/


    //给WebView加一个加载进度条  后面优化
    private class ArcticleCromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                //加载完毕进度条消失
                //progressView.setVisibility(View.GONE);
            } else {
                //更新进度
                //progressView.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

}
