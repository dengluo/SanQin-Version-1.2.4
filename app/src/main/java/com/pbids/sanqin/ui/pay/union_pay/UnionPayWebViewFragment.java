package com.pbids.sanqin.ui.pay.union_pay;

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

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.base.CommonPresenter;
import com.pbids.sanqin.ui.activity.me.MeFragment;
import com.pbids.sanqin.ui.view.AppToolBar;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:01
 * @desscribe 类描述:银生宝界面（无用）
 * @remark 备注:
 * @see
 */
public class UnionPayWebViewFragment extends BaaseToolBarFragment<CommonPresenter> implements AppToolBar.OnToolBarClickLisenear {

    // web view
    protected WebView mWebView;

    protected String webUrl ;

    public static UnionPayWebViewFragment instance(String url) {
        UnionPayWebViewFragment fragment = new UnionPayWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_unionpay, container, false);
        mWebView = (WebView)view.findViewById(R.id.me_authentication_webview);
        Bundle bundle =  getArguments();
        if(  bundle!=null){
            webUrl = getArguments().getString("url","");
        }
        initView();
        return view;
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
        mWebView.addJavascriptInterface(new UnionPayWebViewFragment.JavaScriptInterface(), "android");
//        newsDetailWebview.setWebChromeClient(new WebChromeClient());
//        mWebView.get
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view_donate_records.loadUrl(view_donate_records.getUrl());
//                if(view_donate_records!=null){
//                    Log.i("wzw", "shouldOverrideUrlLoading: " + view_donate_records.getUrl());
//                }
//                return true;
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if(mWebView==null){
                    return;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                getHtml();
//                Log.d("wzw", "shouldOverrideUrlLoading:" + url);
//                view_donate_records.loadUrl(url);
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                return super.shouldInterceptRequest(view_donate_records, request);
//                if(view_donate_records!=null){
//                    Log.d("wzw", "shouldInterceptRequest:" + view_donate_records.getUrl());
//                }
                return null;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url,
                                      Bitmap favicon) {
//                Log.d("TAG", "onPageStarted--url:" + url);
                //支付完成后,点返回关闭界面
//                    if(url.endsWith("http://120.1.1.1/xxx/xx/xxx")){
//                        finish();
//                    }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("wzh", "onPageFinished: " );
//                String  js = "var script = document.createElement('script');";
//                js+= "script.type = 'text/javascript';";
//                js+="var child=document.getElementsByTagName('a')[0];";
////                mWebView.loadUrl("javascript:window.android.save_localhostPaht(child.href)");
//                js+="child.onclick=function(){userIdClick();};";
//                js+= "function userIdClick(){document.write('hello');};";
////                js+= "javascript:window.android.save_localhostPaht(child.href);";
//                if(mWebView!=null){
//                    mWebView.loadUrl("javascript:" +js);
//                }
                super.onPageFinished(view, url);
               // getUserInformation();
            }

        });

        if(webUrl!=null && !"".equals(webUrl)){
            mWebView.loadUrl(webUrl);
        }

    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("银生宝支付",_mActivity);
    }

    @Override
    public CommonPresenter initPresenter() {
        return new CommonPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                _mActivity.onBackPressed();
                break;
        }
    }

    public class JavaScriptInterface {
        String mPasswrod;
        String mUsername;

        @JavascriptInterface
        public void getHTML(final String html) {
            Log.i("wzh", "html: " + html);
            if (!TextUtils.isEmpty(html)) {

            }
        }
    }
}
