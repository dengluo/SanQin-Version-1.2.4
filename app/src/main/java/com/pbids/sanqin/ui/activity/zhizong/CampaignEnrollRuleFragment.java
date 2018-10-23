package com.pbids.sanqin.ui.activity.zhizong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.presenter.CampaignEnrollRulePresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:07
 * @desscribe 类描述:活动须知界面
 * @remark 备注:
 * @see
 */
public class CampaignEnrollRuleFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,CampaignEnrollRuleView {

    @Bind(R.id.tv)
    TextView tv;
    CampaignEnrollRulePresenter presenter;

    public static CampaignEnrollRuleFragment newInstance() {
        CampaignEnrollRuleFragment fragment = new CampaignEnrollRuleFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaign_enrollrule, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        long aid = getArguments().getLong("aid");
        HttpParams params = new HttpParams();
        params.put("aid",aid);
        //addDisposable(presenter.submitInformation(MyApplication.SERVER_ADDRESS_NEWS+"http://192.168.5.32:9092/activity/rule",params,""));
        String url = AddrConst.SERVER_ADDRESS_NEWS+"/activity/rule";
        addDisposable(presenter.submitInformation(url,params,""));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("活动须知",_mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new CampaignEnrollRulePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
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

    }

    @Override
    public void showRuleData(String ruleData) {
        tv.setText(Html.fromHtml(ruleData));
    }
}
