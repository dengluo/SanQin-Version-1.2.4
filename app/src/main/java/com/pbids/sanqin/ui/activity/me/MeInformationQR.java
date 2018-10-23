package com.pbids.sanqin.ui.activity.me;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.CircleImageView;
import com.pbids.sanqin.utils.MemoryReleaseUtil;
import com.pbids.sanqin.utils.QRCodeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2017/12/14.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:36
 * @desscribe 类描述:我的个人信息-二维码界面
 * @remark 备注:
 * @see
 */
public class MeInformationQR extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    //    @Bind(R.id.me_title_left_layout)
//    View meTitleLeftLayout;
//    @Bind(R.id.me_title_text)
//    TextView meTitleText;
//    @Bind(R.id.me_information_qr_touxiang_vip)
//    ImageView meInformationQrTouxiangVip;
    @Bind(R.id.me_information_qr_bg)
    ImageView meInformationQrBg;
    @Bind(R.id.me_information_qr_name)
    TextView meInformationQrName;
    //    @Bind(R.id.me_information_qr_vip)
//    ImageView meInformationQrVip;
    @Bind(R.id.me_information_qr_autograph)
    TextView meInformationQrAutograph;
    @Bind(R.id.me_information_qr)
    ImageView meInformationQr;
    @Bind(R.id.fl_no_vip)
    FrameLayout flNoVip;
    @Bind(R.id.fl_vip)
    FrameLayout flVip;
    @Bind(R.id.me_information_qr_vip_bg)
    ImageView meInformationQrVipBg;
    @Bind(R.id.me_information_vip_qr)
    ImageView meInformationVipQr;
    Bitmap bitmapQR;
    @Bind(R.id.me_information_qr_vip)
    TextView meInformationQrVip;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.me_home_autopragh)
    CircleImageView meHomeAutopragh;
    @Bind(R.id.me_home_person_firstname)
    TextView meHomePersonFirstname;
    @Bind(R.id.me_home_zong)
    ImageView meHomeZong;

    public static MeInformationQR newInstance() {
        MeInformationQR fragment = new MeInformationQR();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_information_qr, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("我的二维码", _mActivity);
    }

    public void initView() {
        //生成二维码
        bitmapQR = QRCodeUtil.createImage("ZC-USER|" + MyApplication.getUserInfo().getUserId(),
                (int) getResources().getDimension(R.dimen.dp_142), (int) getResources().getDimension(R.dimen.dp_142), null);
        initUserInformation();
    }

    public void initUserInformation() {
        int size = (int) getResources().getDimension(R.dimen.dp_50);
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity, meHomeAutopragh, MyApplication.getUserInfo().getFaceUrl(), new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                if(meHomePersonFirstname!=null){
                    meHomePersonFirstname.setVisibility(View.VISIBLE);
                    Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/汉仪颜楷繁.ttf");
                    meHomePersonFirstname.getPaint().setTypeface(typeface);
//                MyApplication.getUserInfo().setSurname("速度");
                    if (MyApplication.getUserInfo().getSurname().length() == 1) {
                        meHomePersonFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_8));
                        meHomePersonFirstname.setLineSpacing(0, 0.8F);
                    } else if (MyApplication.getUserInfo().getSurname().length() == 2) {
                        meHomePersonFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_6));
                        meHomePersonFirstname.setLineSpacing(0, 0.8F);
                    }
                    meHomePersonFirstname.setText(MyApplication.getUserInfo().getSurname() + "府");
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                meHomePersonFirstname.setVisibility(View.INVISIBLE);
                return false;
            }
        });
//        Glide.with(_mActivity).load(MyApplication.getUserInfo().getFaceUrl()).override(size,size)
//                .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default)
//                .dontAnimate()
//                .into(meInformationQrTouxiang1);

//        MyApplication.getUserInfo().setVip(1);
        if (MyApplication.getUserInfo().getVip() == 0) {
            flNoVip.setVisibility(View.VISIBLE);
            flVip.setVisibility(View.GONE);
//            meInformationQrVip.setVisibility(View.INVISIBLE);
//            meInformationQrTouxiangVip.setVisibility(View.INVISIBLE);
            if (bitmapQR != null) {
                meInformationQr.setImageBitmap(bitmapQR);
            }
        } else {
            flNoVip.setVisibility(View.GONE);
            flVip.setVisibility(View.VISIBLE);
//            meInformationQrVip.setVisibility(View.VISIBLE);
//            meInformationQrTouxiangVip.setVisibility(View.VISIBLE);
            if (bitmapQR != null) {
                meInformationVipQr.setImageBitmap(bitmapQR);
            }

            if (MyApplication.getUserInfo().getVip() != 0) {
                ll3.setVisibility(View.VISIBLE);
                meInformationQrVip.setText("VIP" + MyApplication.getUserInfo().getVip());
            }
        }

        if (MyApplication.getUserInfo().getClanStatus() == 1) {
            ll3.setVisibility(View.VISIBLE);
        }

        meInformationQrName.setText(MyApplication.getUserInfo().getName());
        meInformationQrAutograph.setText(MyApplication.getUserInfo().getSignature());
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.get(_mActivity).clearMemory();
        MemoryReleaseUtil.releaseRelease(meInformationQrBg, meInformationQr
                , meHomeAutopragh, meInformationQrVipBg, meInformationVipQr, bitmapQR);
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

//    @OnClick(R.id.me_title_left_layout)
//    public void onViewClicked() {
//        pop();
//    }
}
