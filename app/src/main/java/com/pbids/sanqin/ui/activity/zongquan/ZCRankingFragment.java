package com.pbids.sanqin.ui.activity.zongquan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.ZCRankingInfo;
import com.pbids.sanqin.presenter.ZCRankingPresenter;
import com.pbids.sanqin.ui.recyclerview.adapter.ZCRankingListAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/5 17:01
 * @desscribe 类描述:家族宗祠贡献排行
 * @remark 备注:
 * @see
 */

public class ZCRankingFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,ZCRankingView{

    ZCRankingPresenter presenter;

    @Bind(R.id.zc_ranking_rv)
    RecyclerView zcRankingRv;

    public static ZCRankingFragment newInstance(){
        ZCRankingFragment fragment = new ZCRankingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new ZCRankingPresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zc_rangking, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setContentLayoutGone();
        HttpParams params = new HttpParams();
        addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER+ AddrConst.ADDRESS_SURNAMEINFO_QUERYDONATERANK,params,"1"));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("排行",_mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                // 返回即出
                pop();
                break;
        }
    }

    @Override
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getZCRankInfo(List<ZCRankingInfo> rankingInfos) {
        if (rankingInfos.size() > 0) {
            setContentLayoutVisible();
        } else {
            setNoDataLayoutVisible();
        }
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ZCRankingListAdapter adapter = new ZCRankingListAdapter(_mActivity, rankingInfos);
        zcRankingRv.setLayoutManager(manager);
        zcRankingRv.setAdapter(adapter);
    }

}
