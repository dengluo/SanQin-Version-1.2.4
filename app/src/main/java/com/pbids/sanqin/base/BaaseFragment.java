package com.pbids.sanqin.base;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.view.dialog.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author caiguoliang
 * @date on 2018/3/2 11:34
 * @desscribe 类描述:基础的Fragment，大部分Fragment都继承它
 * @remark 备注
 * @see
 */
public abstract class BaaseFragment<P extends BaasePresenter> extends SupportFragment implements BaaseView{
    protected P mPresenter;
    protected LoadingDialog loadingDialog;
    protected View rootView ;

    // app 有多处使用这个艺术字体  所以在公共类写个方法初始
    protected Typeface typeface;
    protected void initTypeface(){
        //华文行楷
        typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/wenyue.ttf");
    }

    public BaaseFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        if(mPresenter!=null){
            mPresenter.onCreate(this,_mActivity);
            //mPresenter.attachView(this);
        }
    }
    protected abstract P initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    public LoadingDialog getLoadingPop(String s){
        if(loadingDialog==null){
            loadingDialog = new LoadingDialog(_mActivity,s);
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        if(s!=null){
            loadingDialog.setText(s);
        }
        return loadingDialog;
    }

    public void dismiss(){
        if(loadingDialog!=null){
            if(loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if(loadingDialog!=null && loadingDialog.isShowing()){
            return true;
        }
        return super.onBackPressedSupport();
    }

    OnPayPasswordSetLisenear onPayPasswordSetLisenear;

/*    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {

    }

    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        //请求错误信息
        showToast(errorMessage);
    }*/

    public interface OnPayPasswordSetLisenear{
        void setOver();
    }

    public OnPayPasswordSetLisenear getOnPayPasswordSetLisenear(){
        return onPayPasswordSetLisenear;
    }

    public void setOnPayPasswordSetLisenear(OnPayPasswordSetLisenear onPayPasswordSetLisenear){
        this.onPayPasswordSetLisenear = onPayPasswordSetLisenear;
    }

    //显示提示信息
    @Override
    public void showToast(String message){
        Toast.makeText(_mActivity,message,Toast.LENGTH_LONG).show();
    }

    // open web page
    @Override
    public void openWebFragment(String url){
        ZhiZongWebFragment fragment1 = ZhiZongWebFragment.newInstance();
        fragment1.getArguments().putString("link", url);
        start(fragment1);
    }

    //confirm
    public ConfirmDialog confirm(String msg, ConfirmListener listener){
        final ConfirmDialog dialog = new ConfirmDialog(_mActivity);
        dialog.setContentText(msg);
        dialog.setOnDialogClickLisenrar(listener);
        dialog.show();
        return dialog;
    }


    public WebSettings initWebViewSttting(WebView webview){
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setDefaultTextEncodingName("utf-8");//设置网页默认编码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);


        settings.setSupportZoom(false); //禁止缩放
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);

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

        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        //webview.addJavascriptInterface(this, "sanqin");
        return settings;
    }

    //设置图片资源
    public void intoImg(int resourcesId, ImageView img){
        //设置图片
        Glide.with(_mActivity).load(resourcesId).into(img);
    }

    //设置图片资源
    public void intoImgGif(int resourcesId, ImageView img){
        //设置图片
        Glide.with(_mActivity).asGif().load(resourcesId).into(img);
    }




    //
    public void showLoading(){

    }

    public void showDialog(Dialog dialog, double width, double height) {
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        p.height = (int) (height);
        p.width = (int) (d.getWidth() * width);
        window.setAttributes(p);
    }
}
