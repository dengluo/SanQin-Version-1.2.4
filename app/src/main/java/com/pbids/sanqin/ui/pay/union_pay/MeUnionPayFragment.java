package com.pbids.sanqin.ui.pay.union_pay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.activity.me.MeFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:01
 * @desscribe 类描述:银生宝界面（无用）
 * @remark 备注:
 * @see
 */
public class MeUnionPayFragment extends BaaseToolBarFragment<MeUnionPayPresenter> implements AppToolBar.OnToolBarClickLisenear,MeUnionPayView {

    public static final String FAG_UNION_PAY_COMPANY = "FAG_UNION_PAY_COMPANY";

    @Bind(R.id.me_authentication_webview)
    WebView mWebView;

    MeUnionPayPresenter meAuthenticationPresenter;
    private String iDCard="";
    private String bankCard="";
    private String phone="";
    private String name="";
    private String blanceName = "";
    private String cardType = "";
    private boolean upUserInformation = false;

    private String totalAmount ;
    private String aid;
    private String payMode;


    public static MeUnionPayFragment instance(String pay,String totalAmount,String aid) {
        MeUnionPayFragment fragment = new MeUnionPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pay",pay);
        bundle.putString("totalAmount",totalAmount);
        bundle.putString("aid",aid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_unionpay, container, false);
        ButterKnife.bind(this, view);
        initView();
        getInformation();
        return view;
    }

    public void getInformation(){
        totalAmount = getArguments().getString("totalAmount");
        aid = getArguments().getString("aid");
        payMode = getArguments().getString("pay");

        if(totalAmount==null || "".equals(totalAmount)){
            showToast("金额错误");
            pop();
        }
//        String url = "https://dlog.kmwlyy.com:8142/pay/getPayOrder?body=%E6%B5%8B%E8%AF%95&outTradeNo="+System.currentTimeMillis()
//                +"&subject=%E6%B5%8B%E8%AF%95&totalAmount=0.1&customerId=1112&payCode=unspay";
        HttpParams params = new HttpParams();
        //params.put("userId", MyApplication.getUserInfo().getUserId());
        params.put("aid", aid);
        //支付用途
        params.put("purpose", Const.PURPOSE_REWARD);
        // 银生宝测试:unspay_test,银生宝线上:unspay）
        if (AddrConst.SERVER_ADDRESS_PAYMENT.indexOf("app.huaqinchi") == -1) {
            params.put("payCode", Const.PAY_CODE_UNION_TEST);
            params.put("totalAmount", "0.02");
        } else {
            //构造方法的字符格式这里如果小数不足2位,会以0补足.
            //DecimalFormat decimalFormat=new DecimalFormat(".00");
            params.put("payCode", Const.PAY_CODE_UNION);
            //params.put("totalAmount", decimalFormat.format(Float.parseFloat(totalAmount)));
//            params.put("totalAmount", String.format("%.2f", totalAmount));
            params.put("totalAmount",  totalAmount );
//            params.put("totalAmount", "0.02");
        }
        //定单类型
        params.put("orderType",Const.ORDER_TYPE_REWARD);
        mPresenter.requestPayOrder(params);
       // addDisposable(meAuthenticationPresenter.getInformation(url,params,"1"));

    }

    //打开支付页面
    public void openPayUrl(String payUrl){
//        payUrl = payUrl.replace("orderNo","orderId");
//        payUrl = payUrl.replace("commodityName","purpose");
//        payUrl += "&"+"name="+MyApplication.getUserInfo().getName();
//        payUrl += "&"+"cardNo="+MyApplication.getUserInfo().getCardNumber();
        Log.d("cgl", "payurl ww:"+payUrl);
        mWebView.loadUrl(payUrl);
    }

    public void initView( ) {
        mWebView.clearCache(true);
        //初始化webview
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setDefaultTextEncodingName("utf-8");//设置网页默认编码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);//缓存
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存
        }
        settings.setDomStorageEnabled(true);
//        newsDetailWebview.addJavascriptInterface(new WebPlugin(), "WebPlugin");
        Log.i("wzh", "webView: " + MeFragment.str);
//        newsDetailWebview.loadData(MeFragment.str, "text/html; charset=UTF-8", null);
//        String url = "http://180.166.114.155:18083/quickpay-front/quickPayWap/prePay";
//        String url = "http://114.80.54.74:8082/quickpay-front/quickPayWap/prePay";
//        String[] strarray = data.split("[?]");
//        String urlParameter = "accountId=1120171201093412001&customerId=14772247&orderNo=201712121703019&commodityName=理财&amount=0.01&responseUrl=http://log.kmwlyy.com&pageResponseUrl=http://log.kmwlyy.com&key=123456abc&mac=123456abc&mac=13CF4A1C97C6BC06CF0CC24D23C00334";
//        String urlParameter = "";
//        String urlParameter = "accountId=2120171212101105001&customerId=1112&orderNo=12345645657833&commodityName=测试&amount=0.1&responseUrl=http://log.kmwlyy.com&pageResponseUrl=http://log.kmwlyy.com&key=zhichongwenhua001&mac=07775D27168E4E1A272D89D995BF3417";
//        mWebView.postUrl(url, urlParameter.getBytes());
//        mWebView.postUrl(strarray[0], strarray[1].getBytes());
//        webView.loadUrl(url);//get
//        newsDetailWebview.setWebChromeClient(new MyWebChromeClient());// 设置浏览器可弹窗
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.addJavascriptInterface(new MeUnionPayFragment.JavaScriptInterface(), "sanqin");
//        newsDetailWebview.setWebChromeClient(new WebChromeClient());
//        mWebView.get
        mWebView.setWebViewClient(new WebViewClient() {

/*            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i("cgl", "shouldOverrideUrlLoading: " + request);
//                view_donate_records.loadUrl(view_donate_records.getUrl());
//                if(view_donate_records!=null){
//                    Log.i("wzw", "shouldOverrideUrlLoading: " + view_donate_records.getUrl());
//                }
//                return true;
                return super.shouldOverrideUrlLoading(view, request);
            }*/

            @Override
            public void onLoadResource(WebView view, String url) {
                if(mWebView==null){
                    return;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                //Log.d("cgl", "shouldOverrideUrlLoading --:" + url);
                if (url.indexOf("xieyi") != -1 || url.indexOf("unspay_bankcard") != -1) {
                    //协议页面 重新打开
                    start(UnionPayWebViewFragment.instance(url));
                    return true;
                }
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return null;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Log.i("wzh", "onPageFinished: " );
                super.onPageFinished(view, url);
                //quickpaywap/prepay
                if(url.toLowerCase().indexOf("quickpaywap/prepay")!=-1){
                    //---输卡号
                    //自动填入卡号
                    //mWebView.loadUrl("javascript:$('input#IDcard').val('" + MyApplication.getUserInfo().getCardNumber() + "')");
                }else if(url.toLowerCase().indexOf("quickpaywap/inputcardno")!=-1){
                    //quickPayWap/inputCardNo
                    //自动输入 姓名 身份证 手机号
                    //mWebView.loadUrl("javascript:$('input#username').val('" + MyApplication.getUserInfo().getName() + "')");
                    //mWebView.loadUrl("javascript:$('input#IDcard').val('" + MyApplication.getUserInfo().getIdNumber() + "')");
                    //mWebView.loadUrl("javascript:$('input#phone').val('" + MyApplication.getUserInfo().getPhone() + "')");
                    mWebView.loadUrl("javascript:$('input#agree').prop('checked', true);");
                } else if(url.toLowerCase().indexOf("quickpaywap/payresult")!=-1) {
                    //判断支付完成
                    //判断有无 -- 支付成功!
                    mWebView.loadUrl("javascript:if ( window.sanqin && window.sanqin.getHTML  ){ window.sanqin.getHTML($('body').html(),'"+FAG_UNION_PAY_COMPANY+"');}");
                } else {

                }

                //取手机验证码
                //http://180.166.114.155:18083/quickpay-front/quickPayWap/sendVericode
                //支付结果
                //http://180.166.114.155:18083/quickpay-front/quickPayWap/payResult?flag=0&orderId=5345

                //$('body').html(),'"+FAG_UNION_PAY_COMPANY+"'
                //mWebView.loadUrl("javascript:if ( window.sanqin && window.sanqin.getHTML  ){ window.sanqin.getHTML($('body').html(),'"+FAG_UNION_PAY_COMPANY+"');}");
            }

        });
    }
/*
    void getUserInformation(){
        if(mWebView!=null){
            mWebView.loadUrl("javascript:window.android.save_card_name($('.icbc span').eq(0).text()+\",\"+$('.icbc span').eq(1).text())");
//        mWebView.loadUrl("javascript:window.android.save_card_name($('.icbc span').eq(0).text()+\",\"+$('.icbc span').eq(1).text())");
            mWebView.loadUrl("javascript:window.android.update_information($('.qrbody').html())");
//                super.onLoadResource(view_donate_records, url);
        }
    }

    private void getHtml() {
        if(mWebView!=null){
            mWebView.loadUrl("javascript:window.android.getHTML('<html>'+document.body.innerHTML+'</html>');");
        }
    }*/

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("银生宝支付",_mActivity);
    }

    @Override
    public MeUnionPayPresenter initPresenter() {
        return meAuthenticationPresenter = new MeUnionPayPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
//                if(mWebView.canGoBack()){
//                    mWebView.loadUrl("javascript:window.android.save_username(window.history.back())");
//                }else{
//                    pop();
//                }
//                if(mWebView.canGoBack()){
//                    mWebView.goBack();
//                }
                //pop();
                _mActivity.onBackPressed();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        //dispose();
    }

/*    @Override
    public void getPayInformationForYSB(final String info) {
//        initView(info);
//        final String orderInfo = info;   // 订单信息
//
//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(_mActivity);
//                Map<String,String> map = alipay.payV2(info,true);
//
//                Log.i("wzh","map: "+map.toString());
////                Message msg = new Message();
////                msg.what = SDK_PAY_FLAG;
////                msg.obj = result;
////                mHandler.sendMessage(msg);
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
    }*/

    // 支付成功
    private void paySuccess(String fag){
        if("1".contains(fag)){
            //银生宝支付完成
            _mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.putExtra("pay","union");
                    intent.putExtra("status","1");
                    intent.putExtra("purpose",((UnionPayActivity)_mActivity).purpose);
                    _mActivity.setResult(ZhiZongWebFragment.UNION_PAY_REUQEST_CODE,intent);
                    _mActivity.finish();
                }
            });
        }
    }

    public class JavaScriptInterface {
        String mPasswrod;
        String mUsername;

        @JavascriptInterface
        public void getHTML(final String html, String fag) {
            Log.i("wzh", "html: " + html);
            if (!TextUtils.isEmpty(html)) {
                if(FAG_UNION_PAY_COMPANY.contains(fag)){
                    //判断支付完成
                    if(html.indexOf("支付成功!")!=-1){
                        paySuccess("1");
                    }
                }
            }
        }

        @JavascriptInterface
        public void save_username(final String username) {
            if (username != null&&!username.equals("")) {
                Log.i("wzw", "save_username: " + username);
                name = username;
            }
        }

        @JavascriptInterface
        public void save_phone(final String phone) {
            if (phone != null && !phone.equals("")) {
                Log.i("wzw", "save_phone: " + phone);
                MeUnionPayFragment.this.phone = phone;
            }
        }

        @JavascriptInterface
        public void save_idcard(final String idcard) {
            if (idcard != null && !idcard.equals("")) {
                Log.i("wzw", "save_idcard: " + idcard);
                if(bankCard.equals("")){
                    bankCard = idcard;
                }else{
                    iDCard = idcard;
                }
            }
        }

        @JavascriptInterface
        public void save_localhostPaht(final String path){
//            Log.i("wzw", "save_localhostPaht: " + path);
        }

        @JavascriptInterface
        public void save_card_name(final String cardName){
//            Log.i("wzw", "cardName: " + cardName);
            String blanceNameStr = "";
            String cardTypeStr = "";
            try {
                String[] strings = cardName.split("[,]");
                blanceNameStr = strings[0];
                cardTypeStr = strings[1];
            }catch (Exception e){

            }
            if(blanceNameStr!=null && !"".equals(blanceNameStr) && !"undefined".equals(blanceNameStr)
                    && cardTypeStr!=null && !"".equals(cardTypeStr) && !"undefined".equals(cardTypeStr)){
                blanceName = blanceNameStr;
                cardType = cardTypeStr;
            }
        }

        @JavascriptInterface
        public void update_information(final String info){
            if(!"undefined".equals(info) && !"".equals(info) && info.indexOf("验证码")!=-1){
                if(!upUserInformation){
                    if(!MeUnionPayFragment.this.iDCard.equals("")&&!MeUnionPayFragment.this.phone.equals("")
                            &&!MeUnionPayFragment.this.name.equals("")&&!MeUnionPayFragment.this.bankCard.equals("")){
                        if(blanceName!=null && !"".equals(blanceName) && !"undefined".equals(blanceName)
                                && cardType!=null && !"".equals(cardType) && !"undefined".equals(cardType)){
                            upUserInformation = true;
                            HttpParams params = new HttpParams();
                            params.put("name",MeUnionPayFragment.this.name);
                            params.put("phone",MeUnionPayFragment.this.phone);
                            params.put("cardNumber",MeUnionPayFragment.this.bankCard);
                            params.put("idNumber",MeUnionPayFragment.this.iDCard);
                            params.put("bankName",MeUnionPayFragment.this.blanceName);
                            if(cardType.indexOf("借记卡")!=-1){
                                params.put("cardType",1);
                            }else{
                                params.put("cardType",2);
                            }
                            //meAuthenticationPresenter.getInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_BINDCARD_SAVEBINDCARD,params,"2");
                        }
                    }
                }
            }
        }
        @JavascriptInterface
        public void unionPayComplete( String fag ){

        }

    }
}
