package com.pbids.sanqin.ui.activity.me;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.MeSettingScorePop;
import com.pbids.sanqin.utils.AppMarketUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2017/11/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:12
 * @desscribe 类描述:我的界面-设置-关于我们
 * @remark 备注:
 * @see
 */
public class MeAboutFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

//    @Bind(R.id.me_title_text)
//    TextView meTitleText;
    @Bind(R.id.me_about_service_number)
    RelativeLayout meAboutServiceNumber;
    @Bind(R.id.me_about_cooperation)
    RelativeLayout meAboutCooperation;
    @Bind(R.id.me_setting_feedback)
    RelativeLayout meSettingFeedback;
    @Bind(R.id.me_about_service_number_tv)
    TextView meAboutServiceNumberTv;

    public static MeAboutFragment newInstance() {
        MeAboutFragment meAboutFragment = new MeAboutFragment();
        Bundle bundle = new Bundle();
        meAboutFragment.setArguments(bundle);
        return meAboutFragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view_donate_records = inflater.inflate(R.layout.me_about, container, false);
//        ButterKnife.bind(this, view_donate_records);
//        initView();
//        return view_donate_records;
//    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_about, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("关于我们",_mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    public void initView() {
//        meTitleText.setText("关于我们");
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_about_service_number, R.id.me_about_cooperation
            , R.id.me_setting_feedback,R.id.me_about_customer_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.me_title_left_layout:
//                pop();
//                break;
            case R.id.me_about_service_number:
                String number = meAboutServiceNumberTv.getText().toString();
                call(number.replace("-",""));
                break;
            case R.id.me_about_cooperation:
                start(MeCooperationFragment.newInstance());
                break;
            case R.id.me_setting_feedback:
//                Log.i("wzh","getInstallAppMarkets");
                ArrayList<String> strings = AppMarketUtil.getInstallAppMarkets(_mActivity);
//                AppMarketUtil.getFilterInstallMarkets(_mActivity,strings);
//                for(int i=0;i<strings.size();i++){
//                    Log.i("wzh","strings.toArray().toString(): "+strings.get(i));
//                }
                MeSettingScorePop meSettingScorePop = new MeSettingScorePop(_mActivity,AppMarketUtil.getFilterInstallMarkets(_mActivity,strings));
                meSettingScorePop.setCancelable(true);
                meSettingScorePop.show();
                break;
            case R.id.me_about_customer_service:
//                if (!checkApkExist(_mActivity, "com.tencent.mobileqq")){
//                    Toast.makeText(_mActivity,"本机未安装QQ应用",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                startForResult(MeCustomerServiceFragment.newInstance(),301);
//                start(MeCustomerServiceFragment.newInstance());
//                ((MainFragment) (getParentFragment().getParentFragment())).start(MeCustomerServiceFragment.newInstance());
                break;
        }
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

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }
}
