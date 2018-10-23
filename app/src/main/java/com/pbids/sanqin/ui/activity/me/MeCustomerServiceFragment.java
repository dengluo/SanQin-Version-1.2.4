package com.pbids.sanqin.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.view.AppToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:20
 * @desscribe 类描述:联系客服界面
 * @remark 备注:
 * @see
 */
public class MeCustomerServiceFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    @Bind(R.id.qq_webview)
    WebView qqWebview;

    public static MeCustomerServiceFragment newInstance() {
        MeCustomerServiceFragment fragment = new MeCustomerServiceFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_customer_service, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
//        pop();
        WebSettings settings = qqWebview.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setDefaultTextEncodingName("utf-8");//设置网页默认编码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setDomStorageEnabled(true);

        WebViewClient mWebviewclient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                if(url == null) return false;

                try {
                    if(url.startsWith("weixin://") //微信
                            || url.startsWith("alipays://") //支付宝
                            || url.startsWith("mailto://") //邮件
                            || url.startsWith("tel://")//电话
                            || url.startsWith("dianping://")//大众点评
                            || url.startsWith("mqqapi://")//大众点评
                            || url.startsWith("mqqwpa://")//大众点评
                        //其他自定义的scheme
                            ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                        MeCustomerServiceFragment.this.pop();
//                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//                        Bundle bundle = new Bundle();
//                        bundle.putString("url",url);
//                        setFragmentResult(RESULT_OK,bundle);
//                        pop();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                MeCustomerServiceFragment.this.pop();
//                            }
//                        },500);
//                        _mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                MeCustomerServiceFragment.this.pop();
//                            }
//                        });

                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                //处理http和https开头的url
                if(qqWebview!=null){
                    qqWebview.loadUrl(url);
                }
                return true;


//                if(url == null) return false;
//                if (url.startsWith("mqqapi") || url.startsWith("mqqwpa")){
//                    Uri data = Uri.parse(url);
//                    Intent intent = new Intent(Intent.ACTION_VIEW,data);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    MeCustomerServiceFragment.this.pop();
//                    try {
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        return true;
//                    }
//                    return true;
//                }
//                Log.i("wzh","url: "+url);
//                qqWebview.loadUrl(url);
//                return true;
//                return super.shouldOverrideUrlLoading(view_donate_records, url);
            }
        };
        qqWebview.setWebViewClient(mWebviewclient);

//        String text = "<script id=\"qd28521519927e7558cbfa4da16337d2d03696276cb7\" src=\"https://wp.qiye.qq.com/qidian/2852151992/7e7558cbfa4da16337d2d03696276cb7 \n" +
//                "\" charset=\"utf-8\" async defer></script>;";
//        qqWebview.loadUrl("http://192.168.5.32:9092/view/server/qq.html");
//        qqWebview.loadUrl("https://www.baidu.com/");
        //qqWebview.loadUrl("http://q.url.cn/abLxuR?_type=wpa&qidian=true");
        qqWebview.loadUrl("http://q.url.cn/ab7hHf?_type=wpa&qidian=true");
//        qqWebview.loadUrl("https://admin.qidian.qq.com/template/blue/mp/menu/qr-code-jump.html?linkType=0&env=ol&kfuin=2852151992&fid=76&key=0d6ab911edb91a46bd3b5e246fdaccab&cate=1&type=16&ftype=1&_type=wpa&qidian=true");
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("QQ客服",_mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
