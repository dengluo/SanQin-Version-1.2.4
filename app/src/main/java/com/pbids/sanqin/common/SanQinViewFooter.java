package com.pbids.sanqin.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;
import com.pbids.sanqin.R;

public class SanQinViewFooter extends LinearLayout implements IFooterCallBack {
    private ViewGroup mContent;
//    private Animation rotate;
    ImageView refreshImage;
    Context context;

    private boolean showing = true;

    public SanQinViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public SanQinViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void callWhenNotAutoLoadMore(final XRefreshView xRefreshView) {

    }

    @Override
    public void onStateReady() {
        setVisibility(View.GONE);
//        refreshImage.clearAnimation();
        new CommonGlideInstance().setImageViewBackgroundForUrl(context,refreshImage, "");
//        show(true);
    }

    @Override
    public void onStateRefreshing() {
        setVisibility(View.VISIBLE);
        refreshImage.clearAnimation();
//        refreshImage.startAnimation(rotate);
        new CommonGlideInstance().setImageViewBackgroundForUrl(context,refreshImage, R.drawable.loading_gif);
        show(true);
    }

    @Override
    public void onReleaseToLoadMore() {
        setVisibility(View.GONE);
        refreshImage.clearAnimation();
    }

    @Override
    public void onStateFinish(boolean hideFooter) {

        if (hideFooter) {
//            mHintView.setText(R.string.xrefreshview_footer_hint_normal);
        } else {
            //处理数据加载失败时ui显示的逻辑，也可以不处理，看自己的需求
//            mHintView.setText(R.string.xrefreshview_footer_hint_fail);
        }
        setVisibility(View.GONE);
        refreshImage.clearAnimation();
    }

    @Override
    public void onStateComplete() {
        setVisibility(View.GONE);
        refreshImage.clearAnimation();
    }

    @Override
    public void show(final boolean show) {
        if (show == showing) {
            return;
        }
        showing = show;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContent
                .getLayoutParams();
        lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
        mContent.setLayoutParams(lp);
//        LayoutParams lp = (LayoutParams) mContentView
//                .getLayoutParams();
//        lp.height = show ? LayoutParams.WRAP_CONTENT : 0;
//        mContentView.setLayoutParams(lp);
//        setVisibility(show?VISIBLE:GONE);

    }

    @Override
    public boolean isShowing() {
        return showing;
    }

    private void initView(Context context) {
        this.context = context;
        mContent = (ViewGroup) LayoutInflater.from(context).inflate(
                com.andview.refreshview.R.layout.sanqin_header, this);
        refreshImage = (ImageView) findViewById(com.andview.refreshview.R.id.sanqin_refresh_header_iv);
//        rotate = AnimationUtils.loadAnimation(context, com.andview.refreshview.R.anim.refresh_header_rote);
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
