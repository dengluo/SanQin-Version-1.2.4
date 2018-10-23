package com.pbids.sanqin.ui.activity.zhizong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.CampaignEnrollEntity;
import com.pbids.sanqin.model.entity.CampaignEnrollExtendEntity;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.presenter.CampaignEnrollOverPresenter;
import com.pbids.sanqin.ui.activity.me.MeCustomerServiceFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.CampaignEnrollOverAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/23.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:07
 * @desscribe 类描述:活动报名详情界面
 * @remark 备注:
 * @see
 */
public class CampaignEnrollOverFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear , CampaignEnrollOverView{

    @Bind(R.id.name_tv)
    TextView nameEt;
    @Bind(R.id.idcard_tv)
    TextView idcardEt;
    @Bind(R.id.sex_tv)
    TextView sexEt;
    @Bind(R.id.enroll_rv)
    RecyclerView enrollRv;
    @Bind(R.id.enroll_remark_tv)
    TextView enrollRemarkTv;
    @Bind(R.id.campaign_enroll_unitprice)
    TextView campaignEnrollUnitprice;
    @Bind(R.id.campaign_enroll_number)
    TextView campaignEnrollNumber;
    @Bind(R.id.campaign_enroll_total)
    TextView campaignEnrollTotal;
    @Bind(R.id.campaign_enroll_discount)
    TextView campaignEnrollDiscount;
    @Bind(R.id.campaign_enroll_matter)
    View campaignEnrollMatter;
    @Bind(R.id.campaign_enroll_title)
    TextView campaignEnrollTitle;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.enroll_remark_layout_tv)
    TextView enrollRemarkLayoutTv;
    @Bind(R.id.enroll_remark_layout)
    LinearLayout enrollRemarkLayout;
    @Bind(R.id.qq_customer_service)
    LinearLayout qqCustomerService;
    @Bind(R.id.weixin_customer_service)
    LinearLayout weixinCustomerService;
    private long aid;
    private long tid;
    private float price;
    private float discountNum;
    private String title;

    private float moeny;
    float blance;

    CampaignEnrollOverPresenter presenter;
    CampaignEnrollOverAdapter adapter;

    public static CampaignEnrollOverFragment newInstance() {
        CampaignEnrollOverFragment fragment = new CampaignEnrollOverFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaign_enroll_over, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setContentLayoutGone();
        tid = getArguments().getLong("tid",-1);
        if(tid==-1){
            return;
        }
        HttpParams params = new HttpParams();
        params.put("tid",tid);
        addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_ACTIVITY_ORDER,params,"1"));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("活动信息", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new CampaignEnrollOverPresenter(this);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 301){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getString("url")));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @OnClick({R.id.campaign_enroll_matter,R.id.qq_customer_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.campaign_enroll_matter:
                CampaignEnrollRuleFragment fragment = CampaignEnrollRuleFragment.newInstance();
                fragment.getArguments().putLong("aid", aid);
                start(fragment);
                break;
            case R.id.qq_customer_service:
                startForResult(MeCustomerServiceFragment.newInstance(),301);
//                start(MeCustomerServiceFragment.newInstance());
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
    public void getInfomation(List<CampaignEnrollEntity> enrollEntity, CampaignEnrollExtendEntity enrollExtendEntity, NewsArticle newsArticle) {
        if(enrollEntity.size()==0){
            return;
        }
        //Log.i("wzh","newsArticle: "+newsArticle.toString());
        setContentLayoutVisible();
        campaignEnrollTitle.setText(newsArticle.getTitle());
        for(int i=0;i<enrollEntity.size();i++){
            CampaignEnrollEntity enrollEntity_ = enrollEntity.get(i);
            if(enrollEntity_.getMaster()==1){
                nameEt.setText(enrollEntity_.getUsername());
                idcardEt.setText(""+enrollEntity_.getIdNumber());
                sexEt.setText(enrollEntity_.getSex());
                enrollEntity.remove(i);
            }
        }
        aid = newsArticle.getAid();
        phoneTv.setText(enrollExtendEntity.getPhone());
        if("".equals(enrollExtendEntity.getRemark())){
            enrollRemarkTv.setText("无");
        }else {
            enrollRemarkTv.setText(enrollExtendEntity.getRemark());
        }

        campaignEnrollUnitprice.setText(""+enrollExtendEntity.getPrice());
        calculateMoeny(enrollEntity.size()+1,enrollExtendEntity.getPrice(),enrollExtendEntity.getDiscountNum());

        if(adapter==null){
            adapter = new CampaignEnrollOverAdapter(_mActivity,enrollEntity);
        }

        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        enrollRv.setNestedScrollingEnabled(false);
        enrollRv.setLayoutManager(manager);
        enrollRv.setAdapter(adapter);
    }

    public void calculateMoeny(int size,float price,float discountNum) {
        float favorable = price * size * discountNum;
        //Log.i("wzh","discountNum: "+discountNum);
        float count = price * size;
        blance = favorable;
        campaignEnrollDiscount.setText(String.format("%.2f", count-favorable));
        campaignEnrollTotal.setText(String.format("%.2f", blance));
        campaignEnrollNumber.setText("x"+size);
    }
}