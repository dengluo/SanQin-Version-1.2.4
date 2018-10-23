package com.pbids.sanqin.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.andview.refreshview.utils.Utils;
import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;

import java.util.Calendar;

public class SanQinViewHeader extends LinearLayout implements IHeaderCallBack {
    private ViewGroup mContent;
//    private Animation rotate;
    ImageView refreshImage;
    Context context;

    public SanQinViewHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public SanQinViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        mContent = (ViewGroup) LayoutInflater.from(context).inflate(
                com.andview.refreshview.R.layout.sanqin_header, this);
        refreshImage = (ImageView) findViewById(com.andview.refreshview.R.id.sanqin_refresh_header_iv);
//        Glide.get()
//        rotate = AnimationUtils.loadAnimation(context, R.anim.refresh_header_rote);
//        rotate.setDuration(0);
//        rotate.setFillAfter(true);
//        rotate = new RotateAnimation(0.0f, -360.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setDuration(180);
//        rotate.setFillAfter(true);
//        rotate.setRepeatMode(Animation.RESTART);
//        rotate.start();
//        refreshImage.startAnimation(rotate);
    }

    public void setRefreshTime(long lastRefreshTime) {

    }

    /**
     * hide footer when disable pull refresh
     */
    public void hide() {
        setVisibility(View.GONE);
        refreshImage.clearAnimation();
        new CommonGlideInstance().setImageViewBackgroundForUrl(context,refreshImage, "");
    }

    public void show() {
        setVisibility(View.VISIBLE);
        refreshImage.clearAnimation();
        new CommonGlideInstance().setImageViewBackgroundForUrl(context,refreshImage, R.drawable.loading_gif);
//        refreshImage.startAnimation(rotate);
    }

    @Override
    public void onStateNormal() {
//        refreshImage.clearAnimation();
//        refreshImage.startAnimation(rotate);
    }

    @Override
    public void onStateReady() {

    }

    @Override
    public void onStateRefreshing() {

    }

    @Override
    public void onStateFinish(boolean success) {
//        refreshImage.clearAnimation();
    }

    @Override
    public void onHeaderMove(double headerMovePercent, int offsetY, int deltaY) {

    }

    @Override
    public int getHeaderHeight() {
        return getMeasuredHeight();
    }
}
