package com.pbids.sanqin.ui.activity.zhizong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.model.entity.Catlog;
import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.presenter.GenealogyPresenter;
import com.pbids.sanqin.ui.activity.zhizong.component.GenealogyPaperFragment;
import com.pbids.sanqin.ui.activity.zhizong.component.GenealogyRankFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.GenealogyCatalogPop;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.eventbus.OnClickGenealogCatalogEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author caiguoliang
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:我的族谱界面
 * @remark 备注:
 * @see
 */
public class GenealogyFragment extends BaaseToolBarFragment<GenealogyPresenter> implements AppToolBar.OnToolBarClickLisenear, GenealogyView {

    public static final int REQUEST_CODE_GENEALOGY = 2554;

    public static final int SHOW_GENEALOGY = 01;
    public static final int SHOW_GENEALOGY_RANK = 02;


    @Bind(R.id.ly_genealogy)
    FrameLayout lyGenealogy;
    @Bind(R.id.ly_loading)
    FrameLayout lyLoading;

    //排行
    GenealogyRankFragment rankFragment;
    //翻页
    GenealogyPaperFragment paperFragment;

    private int currentShow;
    FragmentTransaction mTransaction;
    private String surnameId;
    private String surname;

    GenealogyInformation genealogyInformation;

    public static GenealogyFragment instance() {
        GenealogyFragment fragment = new GenealogyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public GenealogyPresenter initPresenter() { return  new GenealogyPresenter(); }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genealogy, container, false);
        ButterKnife.bind(this, view);
        currentShow = SHOW_GENEALOGY;
        Bundle args = getArguments();

        surname = args.getString("surname");
        surnameId = args.getString("surnameId");
        initView();
        // eventbux
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterBook(_mActivity);
    }

    private void initView() {
        //字体初始化
        initTypeface();
        // 加载数据
        HttpParams params = new HttpParams();
        params.put("surid", surnameId);
        String url = AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_GENEALOGY;
        mPresenter.requestHttpPost(url,params,REQUEST_CODE_GENEALOGY);
    }

    //加载 Fragment
    private void loadFragmentView(Fragment fragment){
        mTransaction = _mActivity.getSupportFragmentManager().beginTransaction();
        if(fragment.isAdded()){
            mTransaction.replace(R.id.ly_genealogy,fragment);
        }else{
            mTransaction.add(R.id.ly_genealogy,fragment,fragment.getClass().getSimpleName());
        }
        //显示需要显示的fragment
        mTransaction.show(fragment);
        mTransaction.commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                //目录
                if (this.genealogyInformation == null) {
                    break;
                }
                final GenealogyCatalogPop catalogPop = new GenealogyCatalogPop(_mActivity, this.genealogyInformation.getGenealogy_list(),typeface);
                catalogPop.setCatalogClickListener(new GenealogyCatalogPop.OnCatalogClickListener() {
                    @Override
                    public void onClick(Catlog catlog, int position) {
                        if (currentShow != SHOW_GENEALOGY) {
                            //如果不是族谱页面 调入显示
                            currentShow = SHOW_GENEALOGY;
                            loadFragmentView(paperFragment);
                            toolBar.setTab( R.id.main_family_book_1);
                        }
                        paperFragment.setPage(position + 2);
                        catalogPop.dismiss();
                    }
                });
                catalogPop.showDown(v);
                break;
            case R.id.main_family_book_1:
                //族谱
                if (currentShow != SHOW_GENEALOGY) {
                    currentShow = SHOW_GENEALOGY;
                    loadFragmentView(paperFragment);
                    paperFragment.setPage(0);
                }

                break;
            case R.id.main_family_book_2:
                if (rankFragment == null) {
                    rankFragment = new GenealogyRankFragment(this);
                }
                //排行
                if (currentShow != SHOW_GENEALOGY_RANK) {
                    currentShow = SHOW_GENEALOGY_RANK;
                    loadFragmentView(rankFragment);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clickCatalogEvent(OnClickGenealogCatalogEvent event){
        paperFragment.setPage(event.getPosition());
    }


    //数据加载完成
    @Override
    public void loadData(GenealogyInformation genealogyInformation) {
        this.genealogyInformation = genealogyInformation;
        lyLoading.setVisibility(View.GONE);
        if(genealogyInformation==null){
            // 未建设

        } else {
            paperFragment = GenealogyPaperFragment.newInstance(genealogyInformation);
            loadFragmentView(paperFragment);
            // 排行数据
            //rankFragment.updateRankData(genealogyInformation.getRank());
        }
    }

    @Override
    public GenealogyInformation getData() {
        return this.genealogyInformation;
    }

    @Override
    public FragmentManager getFM(){
        return _mActivity.getSupportFragmentManager();
    }

}
