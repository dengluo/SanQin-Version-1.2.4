package com.pbids.sanqin.ui.activity.me;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.config.preference.Preferences;
import com.pbids.sanqin.helper.LogoutHelper;
import com.pbids.sanqin.model.db.FriendGroupManager;
import com.pbids.sanqin.model.db.FriendGroupMemberManager;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.VersionInfo;
import com.pbids.sanqin.presenter.MeSettingPresenter;
import com.pbids.sanqin.ui.activity.LoginPageActivity;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.OneImageOneBtPop;
import com.pbids.sanqin.ui.view.OneTextTwoBtPop;
import com.pbids.sanqin.ui.view.dialog.LoadingDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.CacheUtil;
import com.pbids.sanqin.utils.CheckUpdateUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by pbids903 on 2017/11/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:00
 * @desscribe 类描述:我的设置界面
 * @remark 备注:
 * @see
 */
public class MeSettingFragment extends ToolbarFragment implements MeSettingView, AppToolBar.OnToolBarClickLisenear {

    private static final int REQUEST_CODE_UNKNOWN_APP = 529;

    @Bind(R.id.me_setting_about)
    RelativeLayout meSettingAbout;
    @Bind(R.id.me_setting_feedback)
    RelativeLayout meSettingFeedback;
    @Bind(R.id.me_setting_updata)
    RelativeLayout meSettingUpdata;
    @Bind(R.id.me_setting_clear)
    RelativeLayout meSettingClear;
    @Bind(R.id.me_setting_logout)
    Button meSettingLogout;
    @Bind(R.id.me_setting_clear_cache_size)
    TextView meSettingClearCacheSize;
    MeSettingPresenter meSettingPresenter;

    private LoadingDialog loadingPop;

    private File apkFile = null;

    public static MeSettingFragment newInstance() {
        MeSettingFragment fragment = new MeSettingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_setting, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("设置", _mActivity);
    }

    public void initView() {
        //检查更新
        RxView.clicks(meSettingUpdata).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                getLoadingPop("更新检查中...").show();
                HttpParams params = new HttpParams();
                params.put("appType",1);
                String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_QUERY_APPVERSION;
                addDisposable(meSettingPresenter.checkAppVersion(url,params,"check"));
            }
        });

        String size = "";
        try {
            size = CacheUtil.getTotalCacheSize(_mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!size.equals("")) {
            meSettingClearCacheSize.setText(size);
        }else{
            meSettingClearCacheSize.setText("0 KB");
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return meSettingPresenter = new MeSettingPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_setting_about, R.id.me_setting_feedback, R.id.me_setting_clear, R.id.me_setting_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_setting_about:
                //关于我们
                start(MeAboutFragment.newInstance());
                break;
            case R.id.me_setting_feedback:
                //意见反馈
                start(MeFeedbackFragment.newInstance());
                break;
            case R.id.me_setting_clear:
                //清除缓存
                final OneTextTwoBtPop oneTextTwoBtPop = new OneTextTwoBtPop(_mActivity,null);
                oneTextTwoBtPop.setCancelable(true);
                oneTextTwoBtPop.setIsAnimation(false);
                oneTextTwoBtPop.setOnOneTextTwoBtPopClickLisenear(new OneTextTwoBtPop.OnOneTextTwoBtPopClickLisenear() {
                    @Override
                    public void comfirm() {
                        oneTextTwoBtPop.dismiss();
                        final OneImageOneBtPop imageOneBtPop = new OneImageOneBtPop(_mActivity,OneImageOneBtPop.POP_CLEAR_CRASH);
                        imageOneBtPop.setCancelable(true);
                        imageOneBtPop.setIsAnimation(false);
                        imageOneBtPop.show();
                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                CacheUtil.clearAllCache(_mActivity);
                                _mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageOneBtPop.setClearSuccess();
                                    }
                                });
                                Flowable.timer(2000,TimeUnit.MILLISECONDS)
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long aLong) throws Exception {
                                        imageOneBtPop.dismiss();
                                        if(meSettingClearCacheSize!=null){
                                            meSettingClearCacheSize.setText("0 KB");
                                        }
                                    }
                                });
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
                    }

                    @Override
                    public void cancel() {
                        oneTextTwoBtPop.dismiss();
                    }
                });
                oneTextTwoBtPop.show();
                break;
            case R.id.me_setting_logout:
                //即出登陆
                final OneTextTwoBtPop oneTextTwoBtPop1 = new OneTextTwoBtPop(_mActivity,"确定退出当前账户吗?");
                oneTextTwoBtPop1.setCancelable(true);
                oneTextTwoBtPop1.setIsAnimation(false);
                oneTextTwoBtPop1.setOnOneTextTwoBtPopClickLisenear(new OneTextTwoBtPop.OnOneTextTwoBtPopClickLisenear() {

                    @Override
                    public void comfirm(){
                        //退出帐户
                        MyApplication.logoutApp(MyApplication.getApplication());
                        _mActivity.finish();
                    }

                    @Override
                    public void cancel(){
                        oneTextTwoBtPop1.dismiss();
                    }
                });
                oneTextTwoBtPop1.show();
//                Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
//                weixin.removeAccount(true);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UNKNOWN_APP) {
            //if (resultCode == Activity.RESULT_OK) {
                CheckUpdateUtil.installAPK(_mActivity,apkFile);
           // }else {
            //    toast("权限请求失败，取消安装");
           // }
        }

    }

    private void startUpdate(){
        boolean hasInstallPerssion = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hasInstallPerssion = _mActivity.getPackageManager().canRequestPackageInstalls();
            if (hasInstallPerssion ) {
                //安装应用的逻辑
                CheckUpdateUtil.installAPK(_mActivity,apkFile);
            } else {

                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
            }
        } else {
            CheckUpdateUtil.installAPK(_mActivity,apkFile);
        }

    }


    //下载更新
    @Override
    public void versionInfo(final VersionInfo versionInfo) {
        //Log.v("cgl",versionInfo.getVersionCode() +"--"+ AppUtils.getVersionCode(_mActivity));
        if(versionInfo.getVersionCode() > AppUtils.getVersionCode(_mActivity)){
            final OneTextTwoBtPop oneTextTwoBtPop = new OneTextTwoBtPop(_mActivity,"点击确定下载最新版本app");
            oneTextTwoBtPop.setIsAnimation(false);
            oneTextTwoBtPop.setCancelable(false);
            oneTextTwoBtPop.setOnOneTextTwoBtPopClickLisenear(new OneTextTwoBtPop.OnOneTextTwoBtPopClickLisenear() {

                @Override
                public void comfirm() {
                    //meSettingPresenter.checkUpdate(_mActivity,versionInfo.getDownLoadPath());
                    // shou update pop
                    CheckUpdateUtil.showUpdatePop(_mActivity, versionInfo.getDownLoadPath() , new CheckUpdateUtil.OnStartUpdateListener() {
                        @Override
                        public void onUpdate(final File file) {
                            apkFile = file;
                            dismiss();
                            startUpdate();

                            // 申请多个权限。
                            //第二个参数是需要申请的权限
                           /* if (ContextCompat.checkSelfPermission(_mActivity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED)
                            {
                                //权限还没有授予，需要在这里写申请权限的代码
                                ActivityCompat.requestPermissions(_mActivity,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MAIN_PERMISSIONS_REQUEST_CALL);
                            }else {
                                //权限已经被授予，在这里直接写要执行的相应方法即可

                            }
*/
                            // run ui
                            /*_mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // start update
                                    dismiss();
                                    CheckUpdateUtil.installAPK(_mActivity,file);
                                }
                            });*/

                        }

                        @Override
                        public void onFraction(int fraction) {
                            loadingPop.setText("更新中..."+fraction+"%");
                        }
                    });
                    oneTextTwoBtPop.dismiss();
                    loadingPop = getLoadingPop("更新中...");
                    loadingPop.show();
                }

                @Override
                public void cancel() {
                    oneTextTwoBtPop.dismiss();
                    meSettingClearCacheSize.setText("0 KB");
                }
            });
            oneTextTwoBtPop.show();
        }else{
            OneImageOneBtPop imageOneBtPop = new OneImageOneBtPop(_mActivity,OneImageOneBtPop.APP_UPDATA);
            imageOneBtPop.setIsAnimation(false);
            imageOneBtPop.setCancelable(false);
            imageOneBtPop.show();
        }
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

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
    }
}
