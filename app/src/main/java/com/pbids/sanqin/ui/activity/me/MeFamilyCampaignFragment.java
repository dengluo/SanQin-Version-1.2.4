package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.presenter.MeFamilyCampaignPresenter;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.adapter.MeFamilyCampaginAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:21
 * @desscribe 类描述:家族活动界面
 * @remark 备注:
 * @see
 */
public class MeFamilyCampaignFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeFamilyCampaignView{

    @Bind(R.id.me_family_campaign_rv1)
    RecyclerView meFamilyCampaignRv1;

    MeFamilyCampaignPresenter meFamilyCampaignPresenter;

    public static MeFamilyCampaignFragment newInstance() {
        MeFamilyCampaignFragment fragment = new MeFamilyCampaignFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_family_campaign, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("家族活动",_mActivity);
    }

    public void initView(){
        setContentLayoutGone();
        HttpParams params = new HttpParams();
        params.put("surname", MyApplication.getUserInfo().getSurname());
        params.put("pageIndex",1);
        String url = AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_FAMILY_ACTIVITY;
        addDisposable(meFamilyCampaignPresenter.submitInformation(url, params, ""));
    }

    @Override
    public BasePresenter initPresenter() {
        return meFamilyCampaignPresenter = new MeFamilyCampaignPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
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
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {

    }

    @Override
    public void getNewsArticles(final List<NewsArticle> newsArticles) {
        if(newsArticles.size()>0){
            setContentLayoutVisible();
        }else{
            setNoDataLayoutVisible();
            return;
        }
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        MeFamilyCampaginAdapter meFamilyCampaginAdapter = new MeFamilyCampaginAdapter(newsArticles,_mActivity);
        meFamilyCampaginAdapter.setOnItemClickListener(new OnItemClickListenerUtil.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                //Log.i("wzh","newsArticles.get(position).getLink(): "+newsArticles.get(position).getLink());
                fragment.getArguments().putString("link",newsArticles.get(position).getLink());
                start(fragment);
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        });
        meFamilyCampaignRv1.setLayoutManager(manager);
        meFamilyCampaignRv1.setAdapter(meFamilyCampaginAdapter);
        meFamilyCampaignRv1.setNestedScrollingEnabled(false);
    }

}
