package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.presenter.MeFamilyNewsPresenter;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.adapter.MeFamilyNewsAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;
import com.pbids.sanqin.utils.eventbus.ShareArticleEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:22
 * @desscribe 类描述:家族资讯界面
 * @remark 备注:
 * @see
 */
public class MeFamilyNewsFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeFamilyNewsView {


    @Bind(R.id.me_family_news_rv)
    RecyclerView meFamilyNewsRv;

    MeFamilyNewsPresenter meFamilyNewsPresenter;
    @Bind(R.id.no_data_iv)
    ImageView noDataIv;

    MeFamilyNewsAdapter meFamilyNewsAdapter;

    public static MeFamilyNewsFragment newInstance() {
        MeFamilyNewsFragment fragment = new MeFamilyNewsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_family_news, container, false);
        ButterKnife.bind(this, view);
        initView();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("家族资讯数据", _mActivity);
    }

    public void initView() {
        HttpParams params = new HttpParams();
        params.put("surname", MyApplication.getUserInfo().getSurname());
        params.put("pageIndex", 1);
        String url = AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_FAMILY_NEWS ;
        addDisposable(meFamilyNewsPresenter.submitInformation(url , params, ""));
    }

    @Override
    public BasePresenter initPresenter() {
        return meFamilyNewsPresenter = new MeFamilyNewsPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        dispose();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    //分享计算加1
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareArticle(ShareArticleEvent evt) {
        if (meFamilyNewsAdapter != null) {
            boolean has = false;
            List<NewsArticle> arcList = meFamilyNewsAdapter.getNewsArticles();
            if (arcList != null && arcList.size() > 0) {
                for (NewsArticle one : arcList) {
                    //比较aid
                    if (one.getId() == evt.getAid()) {
                        one.setFromNum(one.getFromNum() + 1);
                        has = true;
                        break;
                    }
                }
            }
            if (has) {
                meFamilyNewsAdapter.notifyDataSetChanged();
            }
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
        if(newsArticles.size()==0){
            noDataIv.setVisibility(View.VISIBLE);
            new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,noDataIv,R.drawable.me_icon_zhanwushujuxianshi_default);
            return;
        }
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        meFamilyNewsAdapter= new MeFamilyNewsAdapter(newsArticles);
        meFamilyNewsAdapter.setOnItemClickListener(new OnItemClickListenerUtil.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                fragment.getArguments().putString("link", newsArticles.get(position).getLink());
                start(fragment);
            }

            @Override
            public void onLongClick(View v, int position) {

            }
        });
        meFamilyNewsRv.setLayoutManager(manager);
        meFamilyNewsRv.setAdapter(meFamilyNewsAdapter);
    }

}
