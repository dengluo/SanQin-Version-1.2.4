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
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.model.entity.Accounts;
import com.pbids.sanqin.presenter.MeAccountsPresenter;
import com.pbids.sanqin.ui.adapter.NewsMoneyAdapter;
import com.pbids.sanqin.ui.recyclerview.widget.StickyHeaderLayout;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:13
 * @desscribe 类描述:资金-流水
 * @remark 备注:
 * @see
 */
public class MeAccountsFragment extends BaaseToolBarFragment<MeAccountsPresenter> implements AppToolBar.OnToolBarClickLisenear, MeAccountsView {

    public static final int ME_ACCOUNTS_HTTP_REQUEST_LIST_CODE = 2921;

    private RecyclerView rvList;
    private StickyHeaderLayout stickyLayout;

    MeAccountsPresenter meAccountsPresenter;

    //adapter
    NewsMoneyAdapter mAdapter;

    public static MeAccountsFragment newInstance(){
        MeAccountsFragment fragment = new MeAccountsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sticky_list, container, false);
        ButterKnife.bind(view);
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        stickyLayout = (StickyHeaderLayout) view.findViewById(R.id.sticky_layout);
        initView();
        return view;
    }

    private void initView() {
        setContentLayoutGone();

        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        mAdapter = new NewsMoneyAdapter(_mActivity);
        rvList.setAdapter(mAdapter);

        //设置是否吸顶。
        stickyLayout.setSticky(false);
        // get http
        HttpParams params = new HttpParams();
        String url = AddrConst.SERVER_ADDRESS_PAYMENT +"/account/queryTradingWater";
        //meAccountsPresenter.submitInformation(url,params,"");
        mPresenter.requestHttpPost(url,params,ME_ACCOUNTS_HTTP_REQUEST_LIST_CODE);
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("流水", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public MeAccountsPresenter initPresenter() {
        return meAccountsPresenter = new MeAccountsPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                //返回
                pop();
                break;
        }
    }

 /*   @Override
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void getAccounts(Map<String, List<Accounts>> accountsMap) {

    }

    @Override
    public void getAccountsGroup(List<ComonRecycerGroup<Accounts>> accountsGroups) {
        if(accountsGroups.size()>0){
            setContentLayoutVisible();
        }else{
            setNoDataLayoutVisible();
            return;
        }
        mAdapter.getList().addAll(accountsGroups);
        mAdapter.notifyDataSetChanged();
          /*
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));

        AccountsGroupListAdapter adapter = new AccountsGroupListAdapter(_mActivity,accountsGroups);
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                //Toast.makeText(_mActivity, "组头：groupPosition = " + groupPosition, Toast.LENGTH_LONG).show();
                //Log.e("eee", adapter.toString() + "  " + holder.toString());
            }
        });

        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                     int groupPosition, int childPosition) {
               // Toast.makeText(_mActivity, "子项：groupPosition = " + groupPosition+ ", childPosition = " + childPosition,Toast.LENGTH_LONG).show();
            }
        });
        rvList.setAdapter(adapter);

        //设置是否吸顶。
        stickyLayout.setSticky(false);
        */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
