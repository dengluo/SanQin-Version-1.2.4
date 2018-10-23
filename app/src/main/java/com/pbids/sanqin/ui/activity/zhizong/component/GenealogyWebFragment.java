package com.pbids.sanqin.ui.activity.zhizong.component;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseFragment;
import com.pbids.sanqin.model.entity.Catlog;
import com.pbids.sanqin.presenter.GenealogyCatalogPresenter;
import com.pbids.sanqin.presenter.GenealogyWebPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author caiguoliang
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:我的族谱界面 ->组件-排行
 * @remark 备注:
 * @see
 */
@SuppressLint("ValidFragment")
public class GenealogyWebFragment extends BaaseFragment<GenealogyWebPresenter> implements GenealogyWebView {

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.wv_web)
    WebView webView;

    Catlog catlog;

    public GenealogyWebFragment(Catlog catlog ){
        super();
        this.catlog = catlog;
    }

    @Override
    public GenealogyWebPresenter initPresenter() { return  new GenealogyWebPresenter(); }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_genealogy_web, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    public void initView() {
        tvTitle.setText(this.catlog.getCatlog());

        initWebViewSttting(webView);
        //不显示滚动条
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示

//        webView.loadData( this.catlog.getGenealogy().getBody(), "text/html" ,  null);
        String htmlBody = "<!DOCTYPE html>" ;
        htmlBody += "<html lang=\"zh-cn\">" ;
        htmlBody += "<head>" ;
        htmlBody += "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0\" />" ;
        htmlBody += "<script type=\"text/javascript\" src=\"http://app.huaqinchi.com:8083/static/js/zepto.min.js\"></script>" ;
        htmlBody += "<link href=\"http://app.huaqinchi.com:8083/static/css/base.css\" type=\"text/css\" rel=\"stylesheet\">" ;
        htmlBody += "</head>" ;
        htmlBody += "<style>img { max-width:100% !important; }</style>" ;
        htmlBody += "<body>" ;
        htmlBody += this.catlog.getGenealogy().getBody();
        htmlBody += "</body>" ;
        htmlBody += "<script>" ;
        htmlBody += "$(\"body img\").each(function (index,item) {\n" +
                "            $(item).removeAttr(\"width\");\n" +
                "            $(item).removeAttr(\"height\");\n" +
                "        })";
        htmlBody += "</script>" ;
        htmlBody += " </html>";
        webView.loadDataWithBaseURL(null, htmlBody , "text/html" , "utf-8", null);
        //viewHeight = ScreenUtils.getScreenHeight() - (int) getResources().getDimension(R.dimen.dp_40) - StatusBarUtils.getStatusBarHeight(_mActivity);
        //pageFactory = new PageFactory(_mActivity,ScreenUtils.getScreenWidth(),viewHeight,50);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(webView!=null){
            webView.loadUrl("about:blank");
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.clearCache(true);
            webView.freeMemory();
            webView.destroy();
            webView = null;
        }
        ButterKnife.unbind(this);
    }


}
