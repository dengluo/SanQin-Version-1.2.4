package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.SanQinViewFooter;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.model.entity.BindBackMessage;
import com.pbids.sanqin.presenter.MeMoneyListPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.adapter.NewsMoneyAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;
import com.pbids.sanqin.utils.AddrConst;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:38
 * @desscribe 类描述:我的资金列表界面
 * @remark 备注:
 * @see
 */
public class MeMoneyListFragment extends BaaseToolBarFragment<MeMoneyListPresenter> implements AppToolBar.OnToolBarClickLisenear,MeMoneyListView{

    public static final int ME_MONEY_LIST_REQUEST_CODE = 21451;
    public static final int ME_MONEY_HTTP_REQUEST_LIST_CODE = 2651;
    public static final int GET_WITH_DRAWTIME_REQUEST_CODE = 1312;
    private static final String TAG = "MeMoneyListFragment:";

    @Bind(R.id.me_money_cash_list_tv)
    TextView meMoneyCashListTv;
    @Bind(R.id.me_money_cash_list_bt)
    Button meMoneyCashListBt;
    @Bind(R.id.me_money_cash_rv)
    RecyclerView meMoneyCashRv;
    @Bind(R.id.me_money_cash_refresh_view)
    XRefreshView moneyCashReFreshView;
    NewsMoneyAdapter newsMoneyAdapter;

    private int currentIndexPage = 1;

    private DisposableObserver<Response<String>> observer;


    MeMoneyListPresenter meMoneyListPresenter;
    float balance;
    private int fee;
    private int minAmount;
    private int isIsLastPage;
    private int withdrawalThreshold;

    public static MeMoneyListFragment newInstance() {
        MeMoneyListFragment fragment = new MeMoneyListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_money_cash_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitleRightText("我的资金","支付设置",_mActivity);
    }

    public void initView() {
        setContentLayoutGone();
        newsMoneyAdapter = new NewsMoneyAdapter(_mActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        meMoneyCashRv.setLayoutManager(linearLayoutManager);
        meMoneyCashRv.setAdapter(newsMoneyAdapter);
        moneyCashReFreshView.setPullRefreshEnable(false);// 设置是否可以下拉刷新
        moneyCashReFreshView.setPullLoadEnable(true);
        moneyCashReFreshView.setPinnedTime(1000);
        moneyCashReFreshView.setCustomFooterView(new SanQinViewFooter(_mActivity));
        moneyCashReFreshView.enableReleaseToLoadMore(true);
        moneyCashReFreshView.enableRecyclerViewPullUp(true);
        moneyCashReFreshView.enablePullUpWhenLoadCompleted(true);
        moneyCashReFreshView.setPinnedContent(true);
        // homeXrefreshview.enablePullUp(true);
        moneyCashReFreshView.enablePullUp(false);
        moneyCashReFreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh: ");
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                Log.i(TAG, "onRefresh: ");
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                Log.i(TAG, "onLoadMore: ");
                if(isIsLastPage!=1){
                    currentIndexPage++;
                    loadMoneyList(currentIndexPage);
                }else{
                    //停止动画，并设置可刷新，如果用户在此页面
                    moneyCashReFreshView.stopRefresh();
                    moneyCashReFreshView.setLoadComplete(false);
                    showToast("没有更多数据啦！");
                }
            }

            @Override
            public void onRelease(float direction) {
                Log.i(TAG, "onRelease: ");
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                Log.i(TAG, "onHeaderMove: ");
            }
        });
        // load data
        loadMoneyList(currentIndexPage);

    }
    //加载数据
    private void loadMoneyList(int currentIndexPage){
        HttpParams params = new HttpParams();
        String url = AddrConst.SERVER_ADDRESS_PAYMENT +"/account/queryAccountInfo?pageIndex="+currentIndexPage;
        observer = mPresenter.requestHttpPost(url, params, ME_MONEY_HTTP_REQUEST_LIST_CODE);

    }

    @Override
    public MeMoneyListPresenter initPresenter() {
        return meMoneyListPresenter = new MeMoneyListPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_money_cash_list_tv
            , R.id.me_money_cash_list_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_money_cash_list_tv:
                break;
            case R.id.me_money_cash_list_bt:
                if ("".equals(MyApplication.getUserInfo().getCardNumber())) {
//                    Toast.makeText(_mActivity, "请先绑定银行卡", Toast.LENGTH_SHORT).show();
                    final OneTextTwoBtDialog oneTextTwoBtDialog = new OneTextTwoBtDialog(_mActivity);
                    oneTextTwoBtDialog.setGrayCenter();
                    oneTextTwoBtDialog.setComfirmText("前往");
                    oneTextTwoBtDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                        @Override
                        public void confirm(View view) {
                            EventBus.getDefault().post(new BindBackMessage());
                            oneTextTwoBtDialog.dismiss();
                        }

                        @Override
                        public void cancel(View view) {
                            oneTextTwoBtDialog.dismiss();
                        }
                    });
                    oneTextTwoBtDialog.setContentText("您还未绑定银行卡哦，点击前往将跳转到绑定银行卡页面！");
                    oneTextTwoBtDialog.show();
                    TextView contentTv = oneTextTwoBtDialog.getTextView();
                    Button twoButtonOne = oneTextTwoBtDialog.getTwoButtonOne();
                    Button twoButtonTwo = oneTextTwoBtDialog.getTwoButtonTwo();
                    contentTv.setTextSize(15);
                    twoButtonOne.setTextSize(15);
                    twoButtonTwo.setTextSize(15);
                    return;
                }else{
                    HttpParams params = new HttpParams();
                    mPresenter.requestHttpGet(AddrConst.SERVER_ADDRESS_PAYMENT + AddrConst.ADDRESS_GETWITHDRAWTIME, params,GET_WITH_DRAWTIME_REQUEST_CODE);
                }
                // 提现
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if( requestCode == ME_MONEY_LIST_REQUEST_CODE && data!=null){
            int con = data.getInt("con");
            if(con==1){
                if(newsMoneyAdapter!=null){
                    newsMoneyAdapter.getList().clear();
                    newsMoneyAdapter.notifyDataSetChanged();
                }
                //延时返回解决刚提现的数据加载不出来问题
                showLoading();
                _mActivity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(800);
                            dismiss();
                            //支付完成后要刷新数据
                            currentIndexPage = 1;
                            loadMoneyList(currentIndexPage);
                            isIsLastPage = 0;
                            //回到顶部
                            meMoneyCashRv.scrollToPosition(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                });

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                if(MyApplication.getUserInfo().getIsSetPayword() == 0){
                    MePayPasswordSetFragment fragment = MePayPasswordSetFragment.newInstance();
                    fragment.getArguments().putString("type","set");
                    start(fragment,SINGLETOP);
                }else if(MyApplication.getUserInfo().getIsSetPayword() == 1){
                    start(MePayPasswordSetOverFragment.newInstance(),SINGLETOP);
                }
                break;
        }
    }

    @Override
    public void getMoneyHistoriesInfo(long id, float balance,int fee,int minAmount, List<Accounts> moneyHistories, int withdrawalThreshold) {
//        moneyCashReFreshView.setLoadComplete(false);
        moneyCashReFreshView.stopRefresh();
        moneyCashReFreshView.setLoadComplete(false);
        if(moneyHistories.size()<10){
            this.isIsLastPage = 1;
        }
        this.balance = balance;
        this.fee = fee;
        this.minAmount= minAmount;
        this.withdrawalThreshold = withdrawalThreshold;
        setContentLayoutVisible();
        updataBlance(balance);
        //资金显示两位小数
        meMoneyCashListTv.setText(String.format("%.2f", balance) + "元");
        //更新数据列表
        ComonRecycerGroup groupItem = new ComonRecycerGroup();
        groupItem.setLists(moneyHistories);
        newsMoneyAdapter.getList().add(groupItem);
        newsMoneyAdapter.notifyDataSetChanged();
    }

    @Override
    public void isLastData() {
        moneyCashReFreshView.stopRefresh();
        moneyCashReFreshView.setLoadComplete(false);
        this.isIsLastPage=1;
    }

    @Override
    public void loadError() {
        moneyCashReFreshView.stopRefresh();
        moneyCashReFreshView.stopLoadMore();
    }

    @Override
    public void allowWithDraw() {
        MeMoneyFragment fragment = MeMoneyFragment.newInstance();
        fragment.getArguments().putFloat("balance",balance);
        fragment.getArguments().putInt("fee",fee);
        fragment.getArguments().putInt("minAmount",minAmount);
        fragment.getArguments().putInt("withdrawalThreshold",withdrawalThreshold);
        //start(fragment);
        startForResult(fragment,ME_MONEY_LIST_REQUEST_CODE);
    }

    @Override
    public void errorWithDraw(String message) {
        showToast(message);
    }

    //更新我的金额
    public void updataBlance(float balance){
        MyApplication.getUserInfo().setAccountBalance(balance);
        UserInfoManager.updateUserInfo(_mActivity,MyApplication.getUserInfo());
        MainFragment mainFragment = findFragment(MainFragment.class);
        MeFragment meFragment = mainFragment.findChildFragment(MeFragment.class);
        meFragment.updateAccountBalance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}