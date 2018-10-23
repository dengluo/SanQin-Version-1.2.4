package com.pbids.sanqin.ui.activity.zongquan;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.DonateRecord;
import com.pbids.sanqin.model.entity.ZongCiInfo;
import com.pbids.sanqin.presenter.ZCAncestralHallPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.CircleImageView;
import com.pbids.sanqin.ui.view.MarqueeView;
import com.pbids.sanqin.utils.AddrConst;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/3/1.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:09
 * @desscribe 类描述:宗祠界面
 * @remark 备注:
 * @see
 */
public class ZCAncestralHallFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, ZCAncestralHallView {

    ZCAncestralHallPresenter presenter;
    @Bind(R.id.level_iv)
    ImageView levelIv;
    @Bind(R.id.contribution_tv)
    TextView contributionTv;
    @Bind(R.id.upgrade_tv)
    TextView upgradeTv;
    @Bind(R.id.next_level_name)
    TextView nextLevelName;
    @Bind(R.id.zc_surname)
    TextView zcSurname;
    @Bind(R.id.marquee_view)
    MarqueeView marqueeView;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.zc_surname_level)
    TextView zcSurnameLevel;

    public static ZCAncestralHallFragment newInstance() {
        ZCAncestralHallFragment fragment = new ZCAncestralHallFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new ZCAncestralHallPresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ancestral_hall, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setContentLayoutGone();
        HttpParams params = new HttpParams();
        addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_SURNAMEINFO_QUERYSURNAMEINFO, params, "1"));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitleRightText("家族宗祠", "排行", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                start(ZCRankingFragment.newInstance());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {
        Toast.makeText(_mActivity, type, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getZongCiInfo(ZongCiInfo zongCiInfo) {

        Log.i("wzh","zongCiInfo.toString(): "+zongCiInfo.toString());
        setContentLayoutVisible();
        SpannableStringBuilder builder = new SpannableStringBuilder(zongCiInfo.getTotalDonateBrickCount() + "");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xB83F26);
        builder.setSpan(colorSpan,0,builder.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String count = "共贡献"+"<font color = \"#B83F26\">"+zongCiInfo.getTotalDonateBrickCount()+"</font>"+"块砖";
        contributionTv.setText(Html.fromHtml(count));
        upgradeTv.setText(""+zongCiInfo.getUpgradeBrickCount());
//        zcSurname.setText(zongCiInfo.getSurname());
        nextLevelName.setText(zongCiInfo.getNextLevelName());
        Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/华文行楷.ttf");
        zcSurname.getPaint().setTypeface(typeface);
        zcSurnameLevel.getPaint().setTypeface(typeface);
        String surname = zongCiInfo.getSurname();
//        surname="欧阳";
        if(surname.length()==1){
            imageView2.setImageResource(R.drawable.zongci_bg_wangshizongci_defalt);
        }else if(surname.length()==2){
            imageView2.setImageResource(R.drawable.zongci_bg_wangshizongci2_defalt);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,(int)_mActivity.getResources().getDimension(R.dimen.dp_160));
            imageView2.setLayoutParams(params);
        }
        zcSurname.setText(surname);
//        zcSurname.setText("王");
//        zongCiInfo.getDonateRecords()
        initLevelHouse(zongCiInfo.getCurrentLevelCode());
        initMarqueeView(zongCiInfo.getDonateRecords());
    }

    private void initMarqueeView(List<DonateRecord> donateRecords) {
        for(int i=0;i<donateRecords.size();i++){
            DonateRecord donateRecord = donateRecords.get(i);
            View view = LayoutInflater.from(_mActivity).inflate(R.layout.view_donate_records,null,false);
            CircleImageView touxiang = (CircleImageView) view.findViewById(R.id.zc_view_touxiang);
            TextView zcUsername = (TextView) view.findViewById(R.id.zc_username);
            TextView zcBricknumber = (TextView) view.findViewById(R.id.zc_bricknumber);
            Glide.with(_mActivity).load(donateRecord.getFaceUrl()).into(touxiang);
            zcUsername.setText(donateRecord.getName());
            zcBricknumber.setText(""+donateRecord.getDonateBrickCount());
            marqueeView.addViewInQueue(view);
        }

        marqueeView.setScrollSpeed(15);
        marqueeView.setScrollDirection(MarqueeView.RIGHT_TO_LEFT);
        marqueeView.setViewMargin(15);
        marqueeView.startScroll();
    }

    private void initLevelHouse(int level){
        switch (level){
            case 1:
                Glide.with(_mActivity).load(R.drawable.zongci_bg1_1).into(levelIv);
                zcSurnameLevel.setText("一");
                break;
            case 2:
                Glide.with(_mActivity).load(R.drawable.zongci_bg1_2).into(levelIv);
                zcSurnameLevel.setText("二");
                break;
            case 3:
                Glide.with(_mActivity).load(R.drawable.zongci_bg1_3).into(levelIv);
                zcSurnameLevel.setText("三");
                break;
            case 4:
                Glide.with(_mActivity).load(R.drawable.zongci_bg2_1).into(levelIv);
                zcSurnameLevel.setText("四");
                break;
            case 5:
                Glide.with(_mActivity).load(R.drawable.zongci_bg2_2).into(levelIv);
                zcSurnameLevel.setText("五");
                break;
            case 6:
                Glide.with(_mActivity).load(R.drawable.zongci_bg2_3).into(levelIv);
                zcSurnameLevel.setText("六");
                break;
            case 7:
                Glide.with(_mActivity).load(R.drawable.zongci_bg3_1).into(levelIv);
                zcSurnameLevel.setText("七");
                break;
            case 8:
                Glide.with(_mActivity).load(R.drawable.zongci_bg3_2).into(levelIv);
                zcSurnameLevel.setText("八");
                break;
            case 9:
                Glide.with(_mActivity).load(R.drawable.zongci_bg3_3).into(levelIv);
                zcSurnameLevel.setText("九");
                break;
            default:
                break;
        }
    }
}
