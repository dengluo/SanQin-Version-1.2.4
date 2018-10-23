package com.pbids.sanqin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.model.entity.FamilyRank;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.presenter.AuthenticationPresenter;
import com.pbids.sanqin.ui.view.AreaSelectorView;
import com.pbids.sanqin.ui.view.WacomView;
import com.pbids.sanqin.ui.view.WriteCoverPopView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.CityUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:13
 * @desscribe 类描述:姓氏认证界面
 * @remark 备注:
 * @see
 */
public class AuthenticationFragment extends BaseFragment implements
        AreaSelectorView.AreaShowLinsener,CityUtil.OnCityUtilLisenear,AuthenticationView
        ,WacomView.OnConfirmClickLisenear {

    @Bind(R.id.app_write_bt)
    ImageView appWriteBt;
    @Bind(R.id.app_write_bg)
    ImageView appWriteBg;

    OptionsPickerView mPickerView;
    AreaSelectorView areaSelectorView;
    WacomView wacomView;
    @Bind(R.id.app_write_name_small_tv)
    TextView appWriteNameSmallTv;
    @Bind(R.id.authentucation_layout)
    LinearLayout authentucationLayout;
    AuthenticationPresenter authenticationPresenter;
    ArrayList<FamilyRank> mFamilyRanks;

    private SharedPreferences sharedPreferences;


    public static AuthenticationFragment newInstance() {
        AuthenticationFragment authenticationFragment = new AuthenticationFragment();
        Bundle bundle = new Bundle();
        authenticationFragment.setArguments(bundle);
        return authenticationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentucation, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData() {
        Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/wenyue.ttf");
        // 设置字体类型
        appWriteNameSmallTv.getPaint().setTypeface(typeface);

        ViewTreeObserver vto = appWriteBg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(appWriteBg!=null){
                    new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,appWriteBg,R.drawable.app_first_name_bg);
                }
            }
        });
        WriteCoverPopView writeCoverPopView = new WriteCoverPopView(_mActivity);
        writeCoverPopView.setIsAnimation(false);
        writeCoverPopView.setCancelable(true);
        writeCoverPopView.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                appWriteBt.setVisibility(View.VISIBLE);
            }
        });
        writeCoverPopView.show();
        sharedPreferences = _mActivity.getSharedPreferences("sanqin", Context.MODE_PRIVATE);
        boolean isGuide = sharedPreferences.getBoolean("isGuide", true);
        if(isGuide){
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("isGuide",true);
            edit.commit();
        }
        CityUtil cityUtil = new CityUtil(_mActivity,1);
        cityUtil.setOnCityUtilLisenear(this);
        cityUtil.doCityUtilAsyncTask();
    }


    @Override
    public BasePresenter initPresenter() {
        return authenticationPresenter = new AuthenticationPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.authentucation_layout})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.authentucation_layout:
                //wellcome
                if(wacomView==null){
                    wacomView = new WacomView(_mActivity);
                }
                wacomView.setOnConfirmClickLisenear(this);
                wacomView.show();

                break;
        }
    }

    @Override
    public void show() {
        mPickerView.show();
    }

    @Override
    public void submitInfotmation(){
        //Log.i("wzh","areaSelectorView.getArea():"+areaSelectorView.getArea());
        if("请选择".equals(areaSelectorView.getArea())){
            Toast.makeText(_mActivity,"请选择籍贯",Toast.LENGTH_SHORT).show();
            return;
        }//所在地区
        getLoadingPop("正在提交").show();
        HttpParams params = new HttpParams();
        params.put("surname",wacomView.getSurname());
        params.put("nativePlace",areaSelectorView.getArea());
        authenticationPresenter.submitInformation(AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_SURNAME_AUTHENTICATION,params,"2");
    }

    @Override
    public void cityCall(final ArrayList<String> provinceNameList, final ArrayList<ArrayList<String>> districtList
            , final ArrayList<ArrayList<ArrayList<String>>> countyListList, ArrayList<String> provinceIdList
            , ArrayList<ArrayList<String>> districtIdList, ArrayList<ArrayList<ArrayList<String>>> countyIdListList) {
        if (null == mPickerView) {
            mPickerView = new OptionsPickerView(new OptionsPickerView.Builder(_mActivity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                areaSelectorView.setArea(provinceNameList.get(options1)
                        + "-" + districtList.get(options1).get(options2)
                        + "-" + countyListList.get(options1).get(options2).get(options3));
            }
        }));
        mPickerView.setPicker(provinceNameList, districtList, countyListList);
        }
    }

    @Override
    public void cityCall(ArrayList<Province> provinces) {

    }

    @Override
    public void onHttpSuccess(String type) {
        if("1".equals(type)){
            dismiss();
            wacomView.dismiss();
            areaSelectorView = new AreaSelectorView(_mActivity,mFamilyRanks);
            areaSelectorView.setAreaLinsener(AuthenticationFragment.this);
            areaSelectorView.show();
        }else if("2".equals(type)){
            dismiss();
            areaSelectorView.dismiss();
            Intent intent = new Intent(_mActivity,HomePageActivity.class);
            startActivity(intent);
            _mActivity.finish();
            Glide.get(_mActivity).clearMemory();
        }
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getFamilyRanks(ArrayList<FamilyRank> familyRanks) {
        if(familyRanks!=null){
            mFamilyRanks = familyRanks;
        }
    }

    //提交姓氏认证
    @Override
    public void onConfirmClick() {
        getLoadingPop("正在提交").show();
        HttpParams params = new HttpParams();
        params.put("surname",wacomView.getSurname());
        String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_SURNAME_AUTHENTICATION;
        authenticationPresenter.submitInformation(url,params,"1");
    }

    @Override
    public boolean onBackPressedSupport() {
        if(mPickerView!=null && mPickerView.isShowing()){
            mPickerView.dismiss();
        }
        if(areaSelectorView!=null && areaSelectorView.isShowing()){
            areaSelectorView.dismiss();
        }
        if(wacomView!=null && wacomView.isShowing()){
            wacomView.dismiss();
        }
        return true;
    }
}
