package com.pbids.sanqin.ui.activity.zhizong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pbids.sanqin.common.SanQinViewFooter;
import com.pbids.sanqin.common.SanQinViewHeader;
import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonFinalVariable;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.HomeNewsInformation;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.presenter.ZhiZongPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.me.MeInviteFragment;
import com.pbids.sanqin.ui.activity.news.NewsSearchFragment;
import com.pbids.sanqin.ui.activity.zongquan.ZQAddFriendFragment;
import com.pbids.sanqin.ui.adapter.BaseViewPagerAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.NewsHomeAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.eventbus.PermissionEvent;
import com.pbids.sanqin.utils.eventbus.ShareArticleEvent;
import com.pbids.sanqin.utils.eventbus.ToWindowFocusChanged;
import com.pbids.sanqin.utils.eventbus.ZhiZongFragmentEvent;
import com.pbids.sanqin.utils.zxing.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:知宗首页
 * @remark 备注:
 * @see
 */
public class ZhiZongFragment extends ToolbarFragment implements ZhiZongView, AppToolBar.OnToolBarClickLisenear {

    public static final int  ZQ_CAPTURE_REQUEST_CODE = 4202 ;

    @Bind(R.id.zh_rv)
    RecyclerView zhRv;
    @Bind(R.id.home_xrefreshview)
    XRefreshView homeXrefreshview;

    private ZhiZongPresenter zhiZongPresenter;
    ZhiZongFragmentEvent zhiZongFragmentEvent;
    DisposableObserver observer;
    private int currentIndexPage= 1;
    NewsHomeAdapter homeAdapter;

    public static ZhiZongFragment newInstance() {
        Bundle args = new Bundle();
        ZhiZongFragment fragment = new ZhiZongFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return zhiZongPresenter = new ZhiZongPresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhi_zong, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        // eventbux
        EventBus.getDefault().register(this);
        refreshNews(1);
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftImageCenterViewTitleRightImage(_mActivity);
    }

    public void initView(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        zhRv.setLayoutManager(manager);
        zhRv.setAdapter(null);
        zhRv.setNestedScrollingEnabled(false);
        zhRv.setFocusable(false);
        zhRv.setFocusableInTouchMode(false);
        zhRv.requestFocus();
        zhRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    if (foodsArrayList.get(firstItemPosition) instanceof Foods) {
//                        int foodTypePosion = ((Foods) foodsArrayList.get(firstItemPosition)).getFood_stc_posion();
//                        FoodsTypeListview.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
//                    }
//                    System.out.println(lastItemPosition + "   " + firstItemPosition);
//
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        initXRefreshView();
        zhiZongFragmentEvent = new ZhiZongFragmentEvent();
        EventBusActivityScope.getDefault(_mActivity).register(this);
    }


    public void refreshNews(int indexPage) {
        HttpParams params = new HttpParams();
        params.put("surname",MyApplication.getUserInfo().getSurname());
        params.put("uid",MyApplication.getUserInfo().getUserId());
        params.put("pageIndex",indexPage);
        observer = zhiZongPresenter.loadingInformation(
                _mActivity, AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_HOME,params,""+indexPage);
        addDisposable(observer);
    }

    @Subscribe
    public void onToWindowFocusChanged(ToWindowFocusChanged toWindowFocusChanged) {

    }

    public int getAutoViewPagerHeight() {
        return (int)getResources().getDimension(R.dimen.dp_180);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
//        zhSv.smoothScrollTo(0, 0);
    }

    BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new BaseViewPagerAdapter.OnAutoViewPagerItemClickListener<NewsArticle>() {

        @Override
        public void onItemClick(int position, NewsArticle newsArticle) {
            ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
            fragment.getArguments().putString("link",newsArticle.getLink());
            ((MainFragment) getParentFragment()).start(fragment);
            Log.i("wzh","newsArticle: "+newsArticle.toString());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
            	//点击扫描二维码
                if(AppUtils.checkCameraPermission(_mActivity, PermissionEvent.REQUEST_CODE_CAMERA)){
                    startCaptureActivity();
                }
                break;
            case R.id.hp_search:
                ((MainFragment) getParentFragment()).start(NewsSearchFragment.newInstandce());
                break;
            case R.id.main_right_layout:
            	//新人邀请
				((MainFragment) getParentFragment()).start(MeInviteFragment.instance());
                break;
        }
    }

    //扫描二维码后返回
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==1){
            switch (requestCode){
                case ZQ_CAPTURE_REQUEST_CODE:
                    String qrcode = intent.getStringExtra("qrcode");
                    int cbtype = intent.getIntExtra("cbtype",0);
                    switch (cbtype){
                        case CaptureActivity.SCAB_CB_USER:
                            //扫描用户
                            ZQAddFriendFragment.addFriend(qrcode,_mActivity);
                            break;
                        case CaptureActivity.SCAB_CB_URL:
                            //扫描网址
                            ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                            fragment.getArguments().putString("link",qrcode);
                            ((MainFragment) getParentFragment()).start(fragment);
                            break;
                    }

                    break;
            }
        }
    }

    //启动二维码扫描
    public void startCaptureActivity(){
//        if(AppUtils.checkCameraPermission(_mActivity, PermissionEvent.REQUEST_CODE_CAMERA)){
            Intent intent = new Intent(_mActivity,CaptureActivity.class);
            intent.putExtra("ctrl",2);
            startActivityForResult(intent, ZQ_CAPTURE_REQUEST_CODE);
//        }
    }

    @Override
    public void loadingInformation(NewsInformation information) {
        //
        if(homeXrefreshview==null){
            return;
        }
        if(information.getList().size()==0){
            homeXrefreshview.setLoadComplete(true);
        }else{
            homeXrefreshview.setLoadComplete(false);
        }
        if(homeAdapter!=null){
            List<NewsArticle> list = homeAdapter.getNewsInformations().get(2).getList();
            int orginalIndex = list.size();
            list.addAll(information.getList());
            homeAdapter.insertRangeChild(2,orginalIndex,information.getList().size());
        }
    }

    //分享文章
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareArticle(ShareArticleEvent evt) {
        if (homeAdapter != null) {
            boolean has = false;
            List<NewsInformation> newsInformations = homeAdapter.getNewsInformations();
            for (NewsInformation item : newsInformations) {
                List<NewsArticle> arcList = item.getList();
                if (arcList != null && arcList.size() > 0) {
                    for (NewsArticle one : arcList) {
                        //比较aid
                        if (one.getId() == evt.getAid()) {
                            one.setFromNum(one.getFromNum() + 1);
                            has = true;
                            break;
                        }
                    }
                    if (has) {
                        break;
                    }
                }
            }
            if (has) {
                homeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadingInformation(HomeNewsInformation homeNewsInformation) {
        final List<NewsArticle> bannerList = homeNewsInformation.getBanner();
        NewsInformation Surname_list= homeNewsInformation.getSurname_list();
        NewsInformation Zhichong_list= homeNewsInformation.getZhichong_list();
        NewsInformation List= homeNewsInformation.getList();

        List.setIsmore(0);
        final List<NewsInformation> newsInformations = new ArrayList<>();
        newsInformations.add(Surname_list);
        newsInformations.add(Zhichong_list);
        newsInformations.add(List);

        homeAdapter = new NewsHomeAdapter(_mActivity,bannerList,newsInformations);
        homeAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder
                    , int groupPosition, int childPosition) {
                NewsArticle arc = newsInformations.get(groupPosition).getList().get(childPosition);
                arc.setClickNum(arc.getClickNum()+1);
                if(arc.getRedirect()){
                    //广告跳外部链接
                    ZhiZongWebSideslipFragment fragment = ZhiZongWebSideslipFragment.newInstance();
                    fragment.getArguments().putString("link",arc.getRedirectUrl());
                    ((MainFragment) getParentFragment()).start(fragment);
                }else{
                    ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                    fragment.getArguments().putString("link",arc.getLink());
                    ((MainFragment) getParentFragment()).start(fragment);
                }
                homeAdapter.notifyDataSetChanged();
            }
        });
        homeAdapter.setOnFooterClickListener(new GroupedRecyclerViewAdapter.OnFooterClickListener() {
            @Override
            public void onFooterClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition) {
                //显示更多
                ZhiZongMoreFragment fragment = ZhiZongMoreFragment.newInstance();
                fragment.setNewsArticles(newsInformations);
                if(groupPosition==0){
                    fragment.getArguments().putInt("type", CommonFinalVariable.MORE_ME);
                }else if(groupPosition == 1){
                    fragment.getArguments().putInt("type", CommonFinalVariable.MORE_ZHIZONG);
                }
                ((MainFragment) getParentFragment()).start(fragment);
            }
        });
        homeAdapter.setOnAutoViewPagerItemClickListener(new BaseViewPagerAdapter.OnAutoViewPagerItemClickListener() {
            @Override
            public void onItemClick(int position, Object o) {
                ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                fragment.getArguments().putString("link",bannerList.get(position).getLink());
                ((MainFragment) getParentFragment()).start(fragment);
            }
        });
        zhRv.setAdapter(homeAdapter);
        homeXrefreshview.setCustomFooterView(new SanQinViewFooter(_mActivity));
    }

    public void initXRefreshView(){
//        homeXrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        homeXrefreshview.setPinnedTime(0);
        homeXrefreshview.setMoveForHorizontal(true);
        homeXrefreshview.setPullLoadEnable(true);
//        homeXrefreshview.setCustomFooterView(new XRefreshViewFooter(_mActivity));
        homeXrefreshview.setCustomFooterView(new SanQinViewFooter(_mActivity));
//        homeXrefreshview.setCustomHeaderView(new XRefreshViewHeader(_mActivity));
        homeXrefreshview.setCustomHeaderView(new SanQinViewHeader(_mActivity));
//        homeXrefreshview.setAutoLoadMore(true);
        homeXrefreshview.enableReleaseToLoadMore(true);
        homeXrefreshview.enableRecyclerViewPullUp(true);
        homeXrefreshview.enablePullUpWhenLoadCompleted(true);
        homeXrefreshview.setPinnedContent(true);
//        homeXrefreshview.enablePullUp(true);
//        homeXrefreshview.ena
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);
        //设置Recyclerview的滑动监听
        homeXrefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        homeXrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                currentIndexPage = 1;
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        homeXrefreshview.stopRefresh();
//                    }
//                }, 3000);
                if(observer!=null){
                    removeDisposable(observer);
                }
                refreshNews(1);
//                zhiZongPresenter.loadingInformation()
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if(observer!=null){
                    removeDisposable(observer);
                }
                currentIndexPage++;
                refreshNews(currentIndexPage);
            }
        });
    }

    @Subscribe
    public void onPermissionEvent(PermissionEvent permissionEvent){
        int request = permissionEvent.getPessionRequestCode();
        Log.i("wzh","REQUEST_CODE_TAKE_PICTURE_PESSION");
        switch (request){
            case PermissionEvent.REQUEST_CODE_CAMERA:
                Log.i("wzh","REQUEST_CODE_TAKE_PICTURE_PESSION");
                if(permissionEvent.getPessionResultCode()!=PermissionEvent.RESULT_ERROR_CODE){
                    startCaptureActivity();
                }
                break;
        }
    }

    @Override
    public void onHttpSuccess(String type) {
        if("1".equals(type)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            homeXrefreshview.setLoadComplete(false);
            homeXrefreshview.stopRefresh();
                }
            },1000);
        }else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    homeXrefreshview.stopLoadMore();
//                }
//            },1000);
        }
    }

    @Override
    public void onHttpError(String type) {
        homeXrefreshview.stopRefresh();
        homeXrefreshview.stopLoadMore();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    private void clear(){
        if(zhRv!=null){
            if(zhRv.getAdapter()!=null){
                ((NewsHomeAdapter)zhRv.getAdapter()).clearQuote();
            }
            zhRv.setAdapter(null);
            zhRv = null;
        }
        Glide.get(_mActivity).clearMemory();
        System.gc();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        clear();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
