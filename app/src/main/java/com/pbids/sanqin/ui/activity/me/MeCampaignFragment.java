package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.presenter.MeCampaignPresenter;
import com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollFragment;
import com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollOverFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.adapter.MeCampaignListAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:17
 * @desscribe 类描述:我的活动界面
 * @remark 备注:
 * @see
 */
public class MeCampaignFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeCampaignView{

    @Bind(R.id.me_campaign_enroll_image)
    ImageView meCampaignEnrollImage;
    @Bind(R.id.me_campaign_enroll_status)
    TextView meCampaignEnrollStatus;
//    @Bind(R.id.me_campaign_enroll_content)
//    TextView meCampaignEnrollContent;
    @Bind(R.id.me_campaign_enroll_list)
    RecyclerView meCampaignEnrollList;
    MeCampaignListAdapter meCampaignListAdapter;
    MeCampaignPresenter presenter;

    public static MeCampaignFragment newInstance() {
        MeCampaignFragment fragment = new MeCampaignFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view_donate_records = inflater.inflate(R.layout.me_campaign, container, false);
//        ButterKnife.bind(this, view_donate_records);
//        initView();
//        return view_donate_records;
//    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_campaign, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("我的活动",_mActivity);
    }

    public void initView(){
        setContentLayoutGone();
        HttpParams params = new HttpParams();
        params.put("uid",MyApplication.getUserInfo().getUserId());
        params.put("pageIndex",1);
        addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_ACTIVITY_LIST,params,""));
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new MeCampaignPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick({ R.id.me_campaign_enroll_image,
            R.id.me_campaign_enroll_status
            , R.id.me_campaign_enroll_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_title_text:
                break;
            case R.id.me_campaign_enroll_image:
                break;
            case R.id.me_campaign_enroll_status:
                break;
            case R.id.me_campaign_enroll_list:
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
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdataInformation(final ArrayList<NewsArticle> newsArticles) {
        if(newsArticles.size()>0){
            setContentLayoutVisible();
        }else{
            setNoDataLayoutVisible();
            return;
        }

        if(newsArticles.get(0).getIsPay()==0){
            if(newsArticles.get(0).getFinish()==1){
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity
                        ,meCampaignEnrollImage,R.drawable.me_icon_baomingchenggong_default);
                meCampaignEnrollStatus.setText("已结束");
            }else{
                new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity
                        ,meCampaignEnrollImage,R.drawable.me_icon_shenhezhong_default);
                meCampaignEnrollStatus.setText("待支付");
            }
        }else if(newsArticles.get(0).getIsPay()==1){
            new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity
                    ,meCampaignEnrollImage,R.drawable.me_icon_tijiaochenggong_default);
            meCampaignEnrollStatus.setText("已支付");
        }
        meCampaignListAdapter = new MeCampaignListAdapter(newsArticles);
        meCampaignListAdapter.setOnItemClickListener(new OnItemClickListenerUtil.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                NewsArticle newsArticle = newsArticles.get(position);

                ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                //Log.i("wzh","newsArticles.get(position).getLink(): "+newsArticles.get(position).getLink());
                fragment.getArguments().putString("link",newsArticles.get(position).getLink());
                start(fragment);
            }

            @Override
            public void onLongClick(View v, int position) {
                NewsArticle newsArticle = newsArticles.get(position);
                if(newsArticle.getIsPay()==1){
                    CampaignEnrollOverFragment enrollOverFragment = CampaignEnrollOverFragment.newInstance();
                    enrollOverFragment.getArguments().putLong("tid",Long.valueOf(""+newsArticle.getId()));
                    enrollOverFragment.getArguments().putLong("aid",newsArticle.getAid());
                    start(enrollOverFragment);
                    return;
                }

                if(newsArticle.getIsPay()==0 && newsArticle.getFinish()!=1){
                    CampaignEnrollFragment fragment = CampaignEnrollFragment.newInstance();
                    fragment.getArguments().putString("key","me");
                    fragment.getArguments().putLong("tid",newsArticle.getId());
                    fragment.getArguments().putLong("aid",newsArticle.getAid());
                    fragment.getArguments().putString("title",newsArticle.getTitle());
                    startForResult(fragment,position);
//                    start(fragment_zc_rangking);
                    return;
                }else {
                    toast("活动已结束");
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        meCampaignEnrollList.setLayoutManager(linearLayoutManager);
        meCampaignEnrollList.setAdapter(meCampaignListAdapter);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            ArrayList<NewsArticle> newsArticles = meCampaignListAdapter.getNewsArticles();
            NewsArticle newsArticle = newsArticles.get(requestCode);
            newsArticle.setIsPay(1);
            meCampaignListAdapter.updateList(newsArticles);
            new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity
                    ,meCampaignEnrollImage,R.drawable.me_icon_tijiaochenggong_default);
            meCampaignEnrollStatus.setText("已支付");
        }
    }
}