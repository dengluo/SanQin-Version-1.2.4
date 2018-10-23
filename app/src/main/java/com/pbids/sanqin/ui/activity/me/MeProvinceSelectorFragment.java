package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.District;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.ui.adapter.MeCityProvinceAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.CityUtil;
import com.pbids.sanqin.utils.FullyLinearLayoutManager;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;
import com.pbids.sanqin.utils.eventbus.LocationEvent;
import com.pbids.sanqin.utils.eventbus.PermissionEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by pbids903 on 2017/12/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:42
 * @desscribe 类描述:所在地界面-省份选择界面
 * @remark 备注:
 * @see
 */
public class MeProvinceSelectorFragment extends ToolbarFragment implements CityUtil.OnCityUtilLisenear
        , OnItemClickListenerUtil.OnItemClickListener, AppToolBar.OnToolBarClickLisenear {

    //    @Bind(R.id.me_title_left_layout)
//    View meTitleLeftLayout;
//    @Bind(R.id.me_title_text)
//    TextView meTitleText;
    @Bind(R.id.me_city_pro_ctiy_layout)
    RelativeLayout meCityProCtiyLayout;
    @Bind(R.id.me_city_pro_rv)
    RecyclerView meCityProRv;
    MeCityProvinceAdapter meCityProvinceAdapter;
    @Bind(R.id.me_city_location)
    TextView meCityLocation;
    //    ArrayList<String> mProvinceNameList;
    //    ArrayList<ArrayList<String>> mDistrictList;
    int currentPosition = 0;
    private String provice;
    private String city;

    public static MeProvinceSelectorFragment newInstance() {
        MeProvinceSelectorFragment fragment = new MeProvinceSelectorFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_city_province, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("地区选择", _mActivity);
    }

    public void initView() {
        provice = getArguments().getString("provice");
        city = getArguments().getString("city");
        if(provice!=null && !"".equals(provice)){
            if(city!=null && !"".equals(city)){
                Log.i("wzw","provice: "+provice);
                Log.i("wzw","city: "+city);
                meCityLocation.setText(provice+" "+city);
            }
        }else{
            if(AppUtils.checkLocationPermission(_mActivity)){
                MyApplication.getApplication().startLocation();
            }
        }
        CityUtil cityUtil = new CityUtil(_mActivity, 0);
        cityUtil.setOnCityUtilLisenear(this);
        cityUtil.doCityUtilAsyncTask();
        EventBusActivityScope.getDefault(_mActivity).register(this);
//        findFragment(MeCitySelectorFragment.class);
//        Log.i("wzh","findFragment(MeCitySelectorFragment.class): "+findFragment(MeCitySelectorFragment.class));
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Subscribe
    public void onPermissionEvent(PermissionEvent permissionEvent){
        int request = permissionEvent.getPessionRequestCode();
        Log.i("wzh","request："+request);
        Log.i("wzh","permissionEvent.getPessionResultCode():"+permissionEvent.getPessionResultCode());
        Log.i("wzh","pPermissionEvent.REQUEST_CODE_TAKE_PICTURE_PESSION:"+PermissionEvent.REQUEST_CODE_TAKE_PICTURE_PESSION);
        Log.i("wzh","pPermissionEvent.REQUEST_CODE_IMAGES_PESSION:"+PermissionEvent.REQUEST_CODE_IMAGES_PESSION);
        switch (request){
            case PermissionEvent.REQUEST_CODE_LOCATION:
                if(permissionEvent.getPessionResultCode()!=PermissionEvent.RESULT_ERROR_CODE){
//                    meInformationLocationPlace.setText("");
                    MyApplication.getApplication().startLocation();
                }else{
                    meCityLocation.setText("无法获得定位信息");
                }
                break;
        }
    }

    @Subscribe
    public void onLocationEvent(LocationEvent locationEvent){
        Log.i("wzw","onLocationEvent-------22222");
        if(locationEvent.getProvince()!=null && locationEvent.getCtiy()!=null
                &&!"".equals(locationEvent.getProvince()) && !"".equals(locationEvent.getCtiy())){
            meCityLocation.setText(locationEvent.getProvince()+" "+locationEvent.getCtiy());
        }else{
            meCityLocation.setText("无法获得定位信息");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_city_pro_ctiy_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.me_title_left_layout:
//                pop();
//                break;
            case R.id.me_city_pro_ctiy_layout:

                break;
        }
    }


    @Override
    public void onClick(View v, int position) {
        Province province = meCityProvinceAdapter.getmProvinceNameList().get(position);
        ArrayList<District> districts = province.getDistrict();
        if (districts.size() == 1) {
            MeInformationFragment meInformationFragment = findFragment(MeInformationFragment.class);
//            meInformationFragment.locationCall(mProvinceNameList.get(position),);
            meInformationFragment.locationCall(province.getProvinceName(), province.getProvinceId());
//            meInformationFragment.getArguments().putString(MeInformationFragment.ME_LOCATION,mProvinceNameList.get(position));
            pop();
            return;
        }
//        currentPosition = position;
//        meCityProProvince.setText(mProvinceNameList.get(position));
        MeCitySelectorFragment fragment = MeCitySelectorFragment.newInstance();
        fragment.setmDistrictList(districts);
        fragment.setProvince(province);
        start(fragment);
    }

    @Override
    public void onLongClick(View v, int position) {

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
    public void cityCall(ArrayList<String> provinceNameList, ArrayList<ArrayList<String>> districtList
            , ArrayList<ArrayList<ArrayList<String>>> countyListList, ArrayList<String> provinceIdList
            , ArrayList<ArrayList<String>> districtIdList, ArrayList<ArrayList<ArrayList<String>>> countyIdListList) {

    }

    @Override
    public void cityCall(ArrayList<Province> provinces) {
//        mProvinceNameList = provinceNameList;
//        mDistrictList = districtList;
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(_mActivity);
//        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        meCityProvinceAdapter = new MeCityProvinceAdapter(provinces);
        meCityProvinceAdapter.setOnItemClickListener(this);
        meCityProRv.setLayoutManager(manager);
        meCityProRv.setAdapter(meCityProvinceAdapter);
        meCityProRv.setNestedScrollingEnabled(false);
//        for(int i=0;i<provinces.size();i++){
//            Log.i("wzh",""+provinces.get(i).getProvinceName()+": "+provinces.get(i).getProvinceId());
//        }
    }
}