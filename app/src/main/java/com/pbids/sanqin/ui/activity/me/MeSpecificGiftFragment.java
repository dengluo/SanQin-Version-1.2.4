package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.presenter.MeSpecificGiftPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.QRCodeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/3/15.
 * 实物礼券
 */

public class MeSpecificGiftFragment extends BaaseToolBarFragment<MeSpecificGiftPresenter> implements AppToolBar.OnToolBarClickLisenear,MeSpecificGiftView {

    String redemptionCode;
    float totalAmount;
    Long gid;

    @Bind(R.id.specific_gift_amount)
    TextView specificGiftAmount;
    @Bind(R.id.specific_gift_code_iv)
    ImageView specificGiftCodeIv;
    @Bind(R.id.specific_gift_code_tv)
    TextView specificGiftCodeTv;

    public static MeSpecificGiftFragment newInstance() {
        MeSpecificGiftFragment fragment = new MeSpecificGiftFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public MeSpecificGiftPresenter initPresenter() {
        return new MeSpecificGiftPresenter();
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specific_gift, container, false);
        //图片
        ImageView giftBanner = view.findViewById(R.id.img_gift_banner);
        Glide.with(_mActivity).load(R.drawable.activity_picture_changbaishan_default).into(giftBanner);

        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        redemptionCode = getArguments().getString("redemptionCode","");
        totalAmount = getArguments().getFloat("totalAmount",0);
        gid = getArguments().getLong("id");
        //Log.i("wzh","redemptionCode: "+redemptionCode);
        specificGiftAmount.setText(String.format("%.2f",totalAmount));
        specificGiftCodeTv.setText(redemptionCode);
        int w = (int)_mActivity.getResources().getDimension(R.dimen.dp_260);
        int h = (int)_mActivity.getResources().getDimension(R.dimen.dp_60);
        specificGiftCodeIv.setImageBitmap(QRCodeUtil.creatBarcode(redemptionCode,w,h));
//        new CommonGlideInstance().setImageViewBackgroundForUrl(
//                _mActivity,specificGiftCodeIv,QRCodeUtil.creatBarcode(_mActivity,redemptionCode,w,h,true));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        //toolBar.setLeftArrowCenterTextTitle("支付状态", _mActivity); //实物礼券
        toolBar.setLeftArrowCenterTextTitleRightText("支付状态","完成", _mActivity); //实物礼券
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                //返回
                pop();
                break;
            case R.id.main_right_layout:
                //兑换
                //Toast.makeText(_mActivity,"兑换中",Toast.LENGTH_SHORT).show();
                getLoadingPop("兑换中").show();
                mPresenter.useCoupon(gid );
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void loadComplete() {
        dismiss();
    }
}
