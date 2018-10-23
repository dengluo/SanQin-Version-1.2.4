package com.pbids.sanqin.ui.activity.zhizong;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonFinalVariable;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.SanQinViewFooter;
import com.pbids.sanqin.common.SanQinViewHeader;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.presenter.ZhiZongMorePresenter;
import com.pbids.sanqin.ui.recyclerview.adapter.NewsMoreAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:首页list的更多界面
 * @remark 备注:
 * @see
 */
public class ZhiZongMoreFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,ZhiZongMoreView{

    @Bind(R.id.zhizong_more_rv)
    RecyclerView zhizongMoreRv;
    @Bind(R.id.zhizong_more_xr)
    XRefreshView zhizongMoreXr;
    List<NewsInformation> mNewsInformation;

    ZhiZongMorePresenter zhiZongMorePresenter;
    NewsMoreAdapter newsMoreAdapter;
    private int currentIndexPage = 1;
    DisposableObserver observer;
    int type = 0;

    public static ZhiZongMoreFragment newInstance() {
        ZhiZongMoreFragment fragment = new ZhiZongMoreFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setNewsArticles(List<NewsInformation> newsInformation){
        this.mNewsInformation = newsInformation;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhizong_more, container, false);
        ButterKnife.bind(view);
        zhizongMoreRv = (RecyclerView) view.findViewById(R.id.zhizong_more_rv);
        zhizongMoreXr = (XRefreshView) view.findViewById(R.id.zhizong_more_xr);
        initView();
        return view;
    }

    private void initView() {
        type = getArguments().getInt("type");
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        zhizongMoreRv.setLayoutManager(manager);
        zhizongMoreRv.setAdapter(null);
        zhizongMoreRv.setNestedScrollingEnabled(false);
        zhizongMoreRv.setFocusable(false);
        zhizongMoreRv.setFocusableInTouchMode(false);
        zhizongMoreRv.requestFocus();
        initXRefreshView();
        initAdapter();
        refreshNews(currentIndexPage);
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("查看更多", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return zhiZongMorePresenter = new ZhiZongMorePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop(); //返回
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
            if("1".equals(type)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(zhizongMoreXr!=null) {
                            zhizongMoreXr.setLoadComplete(false);
                            zhizongMoreXr.stopRefresh();
                        }
                    }
                },1000);
            }
    }

    @Override
    public void onHttpError(String type) {
        zhizongMoreXr.stopRefresh();
        zhizongMoreXr.stopLoadMore();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    private void initAdapter(){
        newsMoreAdapter = new NewsMoreAdapter(_mActivity,new ArrayList<NewsInformation>());
        newsMoreAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder
                    , int groupPosition, int childPosition) {
                ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                NewsArticle arc = newsMoreAdapter.getNewsInformations().get(groupPosition).getList().get(childPosition);
                fragment.getArguments().putString("link",arc.getLink());
                start(fragment);
            }
        });
        zhizongMoreRv.setAdapter(newsMoreAdapter);
    }

    // 数据加载完成
    @Override
    public void getMoreNewsInformation(List<NewsInformation> newsInformations, String type) {
        if(type.equals("1")){
            if(newsMoreAdapter!=null){
                newsMoreAdapter.getNewsInformations().clear();
                newsMoreAdapter.getNewsInformations().addAll(newsInformations);
                newsMoreAdapter.notifyDataSetChanged();
            }
            if(zhizongMoreXr!=null){
                zhizongMoreXr.setCustomFooterView(new SanQinViewFooter(_mActivity));
            }
        }else{
            if(newsInformations.get(0).getList().size()==0){
                //已经加载完成全部列表数据
                if(zhizongMoreXr!=null) {
                    zhizongMoreXr.setLoadComplete(true);
                }
            }else{
                if(newsMoreAdapter!=null) {
                    newsMoreAdapter.getNewsInformations().addAll(newsInformations);
                    newsMoreAdapter.notifyDataSetChanged();
                }
                if(zhizongMoreXr!=null){
                    zhizongMoreXr.setLoadComplete(false);
                    zhizongMoreXr.setCustomFooterView(new SanQinViewFooter(_mActivity));
                }
            }
        }
    }

    public void initXRefreshView(){
//        homeXrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        zhizongMoreXr.setPinnedTime(0);
        zhizongMoreXr.setMoveForHorizontal(true);
        zhizongMoreXr.setPullLoadEnable(true);
        zhizongMoreXr.setCustomHeaderView(new SanQinViewHeader(_mActivity));
        zhizongMoreXr.setCustomFooterView(new SanQinViewFooter(_mActivity));
//        homeXrefreshview.setAutoLoadMore(true);
        zhizongMoreXr.enableReleaseToLoadMore(true);
        zhizongMoreXr.enableRecyclerViewPullUp(true);
        zhizongMoreXr.enablePullUpWhenLoadCompleted(true);
        zhizongMoreXr.setPinnedContent(true);
//        homeXrefreshview.enablePullUp(true);
//        homeXrefreshview.ena
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);
        //设置Recyclerview的滑动监听
        zhizongMoreXr.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        zhizongMoreXr.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                // 下拉刷新
                currentIndexPage = 1;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        homeXrefreshview.stopRefresh();
//                    }
//                }, 3000);
                if (observer != null) {
                    removeDisposable(observer);
                }
                refreshNews(1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                // 上拉加载更多
                if(observer!=null){
                    removeDisposable(observer);
                }
                currentIndexPage++;
                refreshNews(currentIndexPage);
            }
        });
    }

    public void refreshNews(int indexPage) {
        HttpParams params = new HttpParams();
        if(type == CommonFinalVariable.MORE_ME){
            params.put("surname", MyApplication.getUserInfo().getSurname());
        }else if(type == CommonFinalVariable.MORE_ZHIZONG){
            params.put("surname", "知崇");
        }
        params.put("pageIndex",indexPage);
        String url = AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_NEWSMORE;
        observer = zhiZongMorePresenter.submitInformation(url, params, "" + indexPage);
        addDisposable(observer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
