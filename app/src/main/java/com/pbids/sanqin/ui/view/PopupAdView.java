package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.OnDialogClickListener;

/**
 * Created by pbids903 on 2017/11/21.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:22
 * @desscribe 类描述:首页资讯list中广告弹窗
 * @remark 备注:
 * @see com.pbids.sanqin.ui.recyclerview.adapter.NewsHomeAdapter
 */
public class PopupAdView extends PopupWindow {
    private int popupWidth;
    private int popupHeight;
    private int viewWidth;
    private int viewHeight;
    TextView adInterest;
    TextView adNotInterest;
//    private int
    OnDialogClickListener onDialogClickLisenrar;

    public PopupAdView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_hp_news_ad, null);
        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        ImageView imageView = (ImageView) view_donate_records.findViewById(R.id.login_pop_bg);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
//        setWidth(popupWidth);
//        setHeight(popupHeight);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        viewHeight = (int)context.getResources().getDimension(R.dimen.dp_66);
        viewWidth = (int)context.getResources().getDimension(R.dimen.dp_129);

        adInterest = (TextView) view.findViewById(R.id.ad_interest);
        adNotInterest = (TextView) view.findViewById(R.id.ad_not_interest);
        adInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDialogClickLisenrar!=null){
                    onDialogClickLisenrar.cancel(v);
                }
            }
        });

        adNotInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDialogClickLisenrar!=null){
                    onDialogClickLisenrar.confirm(v);
                }
            }
        });

        setWidth(viewWidth);
        setHeight(viewHeight);
//        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(
//                (int)mContext.getResources().getDimension(R.dimen.dp_50)
//                ,(int)mContext.getResources().getDimension(R.dimen.dp_106));
//        imageView.setLayoutParams(layoutParams);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.main_bg_color));
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.HpNewsAdPopAnimation);
        setContentView(view);
    }
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
//        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        showAtLocation(v, Gravity.NO_GRAVITY, location[0]-(viewWidth-v.getWidth())
                , location[1] - viewHeight);
    }

    public void setOnDialogClickLisenrar(OnDialogClickListener onDialogClickLisenrar) {
        this.onDialogClickLisenrar = onDialogClickLisenrar;
    }
}
