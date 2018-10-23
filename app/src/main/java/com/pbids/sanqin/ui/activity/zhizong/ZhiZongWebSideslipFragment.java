package com.pbids.sanqin.ui.activity.zhizong;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.base.CommonPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : 上官名鹏
 * Description : 广告页外部链接页面
 * Date :Create in 2018/8/21 18:20
 * Modified By :
 */
public class ZhiZongWebSideslipFragment extends BaaseToolBarFragment<CommonPresenter> {

    @Bind(R.id.zz_home_webview)
    WebView zzHomeWebview;

    private List<String> loadHistoryUrls=new ArrayList<>();

    public static ZhiZongWebSideslipFragment newInstance() {
        ZhiZongWebSideslipFragment fragment = new ZhiZongWebSideslipFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhizong_web, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        WebSettings settings = zzHomeWebview.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setDefaultTextEncodingName("utf-8");//设置网页默认编码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true);//支持缩放
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

        zzHomeWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                mToolBar.setLeftArrowCenterTextTitle("", getContext());
//                mToolBar.setOnToolBarClickLisenear(ZhiZongWebSideslipFragment.this);
            }

        });
        zzHomeWebview.setWebViewClient(new WebViewClient());



//        zzHomeWebview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                String title = view.getTitle();
//                mToolBar.setLeftArrowCenterTextTitle(title,getContext());
//                return false;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                String title = view.getTitle();
//                mToolBar.setLeftArrowCenterTextTitle(title,getContext());
//                loadHistoryUrls.add(url);
//            }
//        });

        zzHomeWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (zzHomeWebview.canGoBack()) {
                        if (loadHistoryUrls.size() > 1) {
                            //重新加载之前的页面,这里为了让标题也能正常显示
                            String url = loadHistoryUrls.get(loadHistoryUrls.size() - 2);
                            loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
                            if (loadHistoryUrls.size() > 0) {
                                loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
                            }
                            zzHomeWebview.loadUrl(url);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        Bundle arguments = getArguments();
        String url = (String) arguments.get("link");
        zzHomeWebview.loadUrl(url);


    }



    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("",getContext());
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    protected CommonPresenter initPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        pop();
    }

}
