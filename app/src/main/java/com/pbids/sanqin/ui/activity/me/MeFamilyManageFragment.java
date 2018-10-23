package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.presenter.MeFamilyManagePresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.NumberUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2017/12/14.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:21
 * @desscribe 类描述:家族管理界面
 * @remark 备注:
 * @see
 */
public class MeFamilyManageFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeFamilyManageView {

//    @Bind(R.id.me_title_left_layout)
//    View meTitleLeftLayout;
//    @Bind(R.id.me_title_text)
//    TextView meTitleText;
    @Bind(R.id.me_manage_family_number)
    TextView meManageFamilyNumber;
    @Bind(R.id.me_manage_family_number1)
    TextView meManageFamilyNumber1;
    @Bind(R.id.me_manage_family_money)
    TextView meManageFamilyMoney;
    @Bind(R.id.me_manage_family_money1)
    TextView meManageFamilyMoney1;
    @Bind(R.id.me_manage_family_number_add)
    TextView meManageFamilyNumberAdd;
    @Bind(R.id.me_manage_family_money_add)
    TextView meManageFamilyMoneyAdd;
    @Bind(R.id.me_manage_family_news)
    TextView meManageFamilyNews;
    @Bind(R.id.me_manage_family_news_layout)
    RelativeLayout meManageFamilyNewsLayout;
    @Bind(R.id.me_manage_family_member)
    TextView meManageFamilyMember;
    @Bind(R.id.me_manage_family_member_layout)
    RelativeLayout meManageFamilyMemberLayout;
    @Bind(R.id.me_manage_campaign)
    TextView meManageCampaign;
    @Bind(R.id.me_manage_family_campaign_layout)
    RelativeLayout meManageFamilyCampaignLayout;

    MeFamilyManagePresenter meFamilyManagePresenter;

    public static MeFamilyManageFragment newInstance() {
        MeFamilyManageFragment fragment = new MeFamilyManageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view_donate_records = inflater.inflate(R.layout.fragment_me_family_manage, container, false);
//        ButterKnife.bind(this, view_donate_records);
//        initView();
//        return view_donate_records;
//    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_family_manage, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("家族管理",_mActivity);
    }

    public void initView(){
//        meTitleText.setText("家族管理");
        HttpParams params = new HttpParams();
        addDisposable(meFamilyManagePresenter.submitInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_CLAN_INFO,params,""));
    }

    @Override
    public BasePresenter initPresenter() {
        return meFamilyManagePresenter = new MeFamilyManagePresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_manage_family_news_layout
            , R.id.me_manage_family_member_layout, R.id.me_manage_family_campaign_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.me_title_left_layout:
//                pop();
//                break;
            case R.id.me_manage_family_news_layout:
                start(MeFamilyNewsFragment.newInstance());
                break;
            case R.id.me_manage_family_member_layout:
                start(MeMamberDistributionFragment.newInstance());
                break;
            case R.id.me_manage_family_campaign_layout:
                start(MeFamilyCampaignFragment.newInstance());
                break;
        }
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
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {

    }

    @Override
    public void getFamilyManageInformation(int peopleMonthlyGrow, int peopleNum, int wealth, int wealthMonthlyGrow, String surname) {
        String peopleNumStr = NumberUtil.scalerNoWan(peopleNum);

        String wealthStr = NumberUtil.scalerNoWan(wealth);

        if(peopleNumStr.indexOf(".")==-1){
            meManageFamilyNumber1.setVisibility(View.INVISIBLE);
        }
        if(wealthStr.indexOf(".")==-1){
            meManageFamilyMoney1.setVisibility(View.INVISIBLE);
        }

        meManageFamilyNumber.setText(""+peopleNumStr);
        meManageFamilyMoney.setText(""+wealthStr);
        meManageFamilyNumberAdd.setText(""+peopleMonthlyGrow+"人");
        meManageFamilyMoneyAdd.setText(""+wealthMonthlyGrow+"元");
    }
}
