package com.pbids.sanqin.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pbids.sanqin.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : 上官名鹏
 * Description : 引导页dialog
 * Date :Create in 2018/8/16 16:34
 * Modified By :
 */
public class AppLeadPageDialog extends Dialog {

    @Bind(R.id.app_six_lead_img)
    ImageView appSixLeadImg;
    @Bind(R.id.app_five_lead_img)
    ImageView appFiveLeadImg;
    @Bind(R.id.app_fore_lead_img)
    ImageView appForeLeadImg;
    @Bind(R.id.app_three_lead_img)
    ImageView appThreeLeadImg;
    @Bind(R.id.app_two_lead_img)
    ImageView appTwoLeadImg;
    @Bind(R.id.app_first_lead_img)
    ImageView appFirstLeadImg;
    @Bind(R.id.app_six_lead_lin)
    FrameLayout appSixLeadLin;
    @Bind(R.id.app_five_lead_lin)
    FrameLayout appFiveLeadLin;
    @Bind(R.id.app_fore_lead_lin)
    FrameLayout appForeLeadLin;
    @Bind(R.id.app_three_lead_lin)
    FrameLayout appThreeLeadLin;
    @Bind(R.id.app_two_lead_lin)
    FrameLayout appTwoLeadLin;
    @Bind(R.id.app_first_lead_lin)
    FrameLayout appFirstLeadLin;
    private Context mContext;

    public AppLeadPageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.dialog_app_lead_cover, null);
        setContentView(view);
        ButterKnife.bind(this, view);


        onClick();
    }

    private void onClick() {
        appFirstLeadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appFirstLeadLin.setVisibility(View.GONE);
                appTwoLeadLin.setVisibility(View.VISIBLE);
                appTwoLeadImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appTwoLeadLin.setVisibility(View.GONE);
                        appThreeLeadLin.setVisibility(View.VISIBLE);
                        appThreeLeadImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appThreeLeadLin.setVisibility(View.GONE);
                                appForeLeadLin.setVisibility(View.VISIBLE);
                                appForeLeadImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        appForeLeadLin.setVisibility(View.GONE);
                                        appFiveLeadLin.setVisibility(View.VISIBLE);
                                        appFiveLeadImg.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                appFiveLeadLin.setVisibility(View.GONE);
                                                appSixLeadLin.setVisibility(View.VISIBLE);
                                                appSixLeadImg.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        appSixLeadLin.setVisibility(View.GONE);
                                                        onClick.onClick();

                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }

    private OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public interface OnClick {
        void onClick();
    }

}
