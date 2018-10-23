package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.District;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.ui.adapter.MeCityCityAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.FullyLinearLayoutManager;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;
import com.pbids.sanqin.utils.eventbus.LocationEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2017/12/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:18
 * @desscribe 类描述:我的个人信息-所在地界面
 * @remark 备注:
 * @see
 */
public class MeCitySelectorFragment extends ToolbarFragment implements OnItemClickListenerUtil.OnItemClickListener
        ,AppToolBar.OnToolBarClickLisenear {
    public static final int REQUEST_CODE = 702;
    public static final String BUNDLE_KEY = "702";
    ArrayList<District> mDistrictList;
    Province province;
//    @Bind(R.id.me_title_left_layout)
//    View meTitleLeftLayout;
//    @Bind(R.id.me_title_text)
//    TextView meTitleText;

    @Bind(R.id.me_city_cit_selected)
    TextView meCityCitSelected;
    @Bind(R.id.me_city_cit_rv)
    RecyclerView meCityCitRv;
    MeCityCityAdapter meCityCityAdapter;

    public static MeCitySelectorFragment newInstance() {
        MeCitySelectorFragment fragment = new MeCitySelectorFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_city_city, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("选择地区",_mActivity);
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public void initView(){
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(_mActivity);
//        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        meCityCityAdapter = new MeCityCityAdapter(mDistrictList);
        meCityCityAdapter.setOnItemClickListener(this);
        meCityCitRv.setNestedScrollingEnabled(false);
        meCityCitRv.setLayoutManager(manager);
        meCityCitRv.setAdapter(meCityCityAdapter);

    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    public void setmDistrictList(ArrayList<District> mDistrictList) {
        this.mDistrictList = mDistrictList;
    }

    @Override
    public void onClick(View v, int position) {
//        popTo(MeInformationFragment.class,false);
        popTo(MeInformationFragment.class,false);
        MeInformationFragment meInformationFragment = findFragment(MeInformationFragment.class);
//        meInformationFragment.locationCall();
        meInformationFragment.locationCall(province.getProvinceName()+","+mDistrictList.get(position).getDistrictName()
                ,province.getProvinceId()+","+mDistrictList.get(position).getDistrictId());
//        meInformationFragment.getArguments().putString(MeInformationFragment.ME_LOCATION,mProvice+" "+mDistrictList.get(position));
//        Log.i("wzh","meInformationFragment.getArguments().getString(MeInformationFragment.ME_LOCATION): "+meInformationFragment.getArguments().getString(MeInformationFragment.ME_LOCATION));
    }

    @Override
    public void onLongClick(View v, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @OnClick(R.id.me_title_left_layout)
//    public void onViewClicked() {
//        pop();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
        }
    }
}
