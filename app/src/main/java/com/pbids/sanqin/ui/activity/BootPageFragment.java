package com.pbids.sanqin.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.VersionInfo;
import com.pbids.sanqin.presenter.BootPagePresenter;
import com.pbids.sanqin.ui.adapter.BootPageVPAdapter;
import com.pbids.sanqin.ui.view.OneTextTwoBtPop;
import com.pbids.sanqin.ui.view.dialog.LoadingDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.CheckUpdateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:14
 * @desscribe 类描述:广告页界面
 * @remark 备注:
 * @see
 */
public class BootPageFragment extends BaseFragment implements BootPageView {
    int[] drawables = {R.drawable.launchimage1, R.drawable.launchimage2, R.drawable.launchimage3};

    int currentItem = 0;
    ArrayList<ImageView> imageViews;
    @Bind(R.id.bp_vp_install)
    ViewPager bpVpInstall;
    @Bind(R.id.bp_iv_layout)
    LinearLayout bpIvLayout;
    @Bind(R.id.bp_bt)
    Button bpBt;
    @Bind(R.id.bp_next_bt)
    Button bpNextBt;
    @Bind(R.id.bp_ad_iv)
    ImageView bpAdIv;
    @Bind(R.id.bp_qidong_iv)
    ImageView bpQidongIv;
    BootPagePresenter bootPagePresenter;
    String mUrl = null;
    public CountDownTimer countDownTimer;
    private File apkFile = null;
    private static final int REQUEST_CODE_UNKNOWN_APP = 529;
    private boolean isNewVersion = false;
    private LoadingDialog loadingPop;
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1000;
    public static final int PERMISSIONS_REQUEST_LOCATION = 2000;

    public static BootPageFragment newInstance() {
        BootPageFragment fragment = new BootPageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_boot_page, container, false);
        if (ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //请求存储
            ActivityCompat.requestPermissions(_mActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void checkUpdate() {
        getLoadingPop("更新检查中...").show();
        HttpParams params = new HttpParams();
        params.put("appType", 1);
        String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_QUERY_APPVERSION;
        addDisposable(bootPagePresenter.checkAppVersion(url, params, "check"));
    }

    public void initView(View view) {
        //启动图片
        //设置背景
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (bpQidongIv == null) {
                    return;
                }
              /*  // 取 drawable 的长宽
                int w = resource.getIntrinsicWidth();
                int h = resource.getIntrinsicHeight();
                Log.v("cgl", "网络图片--->宽:" + w + " 高:" + h);
//                Rect rect = resource.getCurrent().get;
//                Log.v("cgl", "网络图片--->宽:" + rect.width() + " 高:" + rect.height());
                //等比例显示
                if (bpQidongIv.getScaleType() != ImageView.ScaleType.FIT_XY) {
                    bpQidongIv.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                ViewGroup.LayoutParams params = bpQidongIv.getLayoutParams();
                int vw = bpQidongIv.getWidth() - bpQidongIv.getPaddingLeft() - bpQidongIv.getPaddingRight();
                float scale = (float) vw / (float) resource.getIntrinsicWidth();
                int vh = Math.round(resource.getIntrinsicHeight() * scale);
                params.height = vh + bpQidongIv.getPaddingTop() + bpQidongIv.getPaddingBottom();
                bpQidongIv.setLayoutParams(params);
                bpQidongIv.setBackground(resource);*/
                if (bpQidongIv.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
                    bpQidongIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                bpQidongIv.setImageDrawable(resource);
            }
        };
        Glide.with(_mActivity).load(R.drawable.launchimage_).into(simpleTarget);
        startPage();
        //        检查版本
//        checkUpdate();

//        new CommonGlideInstance().setImageViewBackgroundForUrl(
//                _mActivity,bpQidongIv,R.drawable.launchimage_,
//                ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(),true);
    }

    private void startPage() {
        final SharedPreferences sp = _mActivity.getSharedPreferences("sanqin", Context.MODE_PRIVATE);
        final String firstLogin = sp.getString("first_login", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bpQidongIv != null) {
                    bpQidongIv.setVisibility(View.GONE);
                    if (firstLogin.equals("")) {
                        if(bpBt!=null) {
                            bpBt.setVisibility(View.GONE);
                        }
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("first_login", "no_fitst");
                        editor.commit();
                        initViewPager(null);
                        initIndex();
                        return;
                    }
                    next();
                }
            }
        }, 2000);
        if (!firstLogin.equals("")) {
            addDisposable(bootPagePresenter.submitInformation());
        }
    }

    //等待展示广告图
    private void next() {
        if(bpBt!=null){
            bpBt.setVisibility(View.VISIBLE);
            bpBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countDownTimer.cancel();
                    startToNext();
                }
            });
        }
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(bpBt!=null){
                    bpBt.setText("跳过" + "(" + millisUntilFinished / 1000 + ")");
                }
            }

            @Override
            public void onFinish() {
                if(bpBt!=null){
                    bpBt.setText("跳过" + "(0)");
                }
                startToNext();
            }
        };
        countDownTimer.start();
    }

    BootPageVPAdapter bootPageVPAdapter;

    public void initViewPager(final String url) {
        mUrl = url;
        bootPageVPAdapter = new BootPageVPAdapter(_mActivity, drawables);
        bpNextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = UserInfoManager.queryUserInfo(_mActivity);
                if (userInfo == null) {
                    Intent intent = new Intent(_mActivity, LoginPageActivity.class);
                    startActivity(intent);
                    _mActivity.finish();
                } else {
                    MyApplication.setUserInfo(userInfo);
                    int status = MyApplication.getUserInfo().getSurnameStatus();
                    if (status == 0) {
                        start(AuthenticationFragment.newInstance());
                        return;
                    }
                    HomePageActivity.start(_mActivity,null);
                    _mActivity.finish();
                }
            }
        });
        bpVpInstall.setAdapter(bootPageVPAdapter);
        bpVpInstall.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (url == null) {
                    for (int i = 0; i < drawables.length; i++) {
                        if (i == position) {
                            imageViews.get(i).setSelected(true);
                        } else {
                            imageViews.get(i).setSelected(false);
                        }
                    }
                    currentItem = position;
                }
                if (position == drawables.length - 1) {
                    bpNextBt.setVisibility(View.VISIBLE);
                } else {
                    bpNextBt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initIndex() {
        imageViews = new ArrayList<ImageView>();
        int iamgeSize = (int) getResources().getDimension(R.dimen.dp_8);
        for (int i = 0; i < drawables.length; i++) {
            ImageView imageView = new ImageView(_mActivity);
            ;
            if (i == 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iamgeSize, iamgeSize);
                imageView.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iamgeSize, iamgeSize);
                params.setMargins((int) getResources().getDimension(R.dimen.dp_20), 0, 0, 0);
                imageView.setLayoutParams(params);
            }
            imageView.setBackgroundResource(R.drawable.selector_bp_circle);
            bpIvLayout.addView(imageView);
            imageViews.add(imageView);
        }
        imageViews.get(0).setSelected(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (bootPageVPAdapter != null) {
            if (bootPageVPAdapter.countDownTimer != null) {
                bootPageVPAdapter.countDownTimer.cancel();
            }
        }
        Glide.get(_mActivity).clearMemory();
    }

    private void recycleBitmap(ArrayList<ImageView> views) {
        if (views != null) {
            int count = views.size();
            for (int i = 0; i < count; i++) {
                ImageView img = views.get(i);
                if (img != null) {
                    Drawable drawable = img.getDrawable();
                    if (drawable != null) {
                        if (drawable instanceof BitmapDrawable) {
                            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                            Bitmap bitmap = bitmapDrawable.getBitmap();
                            if (bitmap != null) {
                                bitmap.recycle();
                                bitmap = null;
                            }
                        }
                    }
                }
                img = null;
            }
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return bootPagePresenter = new BootPagePresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        dispose();
    }

    @Override
    public void onHttpSuccess(String type) {
        SharedPreferences sp = getContext().getSharedPreferences("sanqin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("link_page", type);
        editor.putString("first_login", "no_fitst");
        editor.putString("requst_link_page_time", "" + new Date().getTime());
        editor.commit();
        //广告图
        bpAdIv.setVisibility(View.VISIBLE);
/*        new CommonGlideInstance().setImageViewBackgroundForUrl(
                _mActivity,bpAdIv,type,ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(),true);*/

        //设置背景
        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (bpAdIv == null) {
                    return;
                }
                // 取 drawable 的长宽
              /*  int w = resource.getIntrinsicWidth();
                int h = resource.getIntrinsicHeight();
                Log.v("cgl", " ad -- 网络图片--->宽:" + w + " 高:" + h);*/
                //等比例显示
/*                if (bpAdIv.getScaleType() != ImageView.ScaleType.FIT_XY) {
                    bpAdIv.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/
                if (bpAdIv.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
                    bpAdIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
  /*              ViewGroup.LayoutParams params = bpAdIv.getLayoutParams();
                int vw = bpAdIv.getWidth() - bpAdIv.getPaddingLeft() - bpAdIv.getPaddingRight();
                int vh = bpAdIv.getHeight() - bpAdIv.getPaddingTop() - bpAdIv.getPaddingBottom();
                float scalew = (float) vw / (float) resource.getIntrinsicWidth();
                float scaleh = (float) vh / (float) resource.getIntrinsicHeight();
                float scale = Math.max(scalew, scaleh);
                Log.v("cgl", " scale  :" + scale);
                params.width = (int) ((float) resource.getIntrinsicWidth() * scale);
                params.height = (int) ((float) resource.getIntrinsicHeight() * scale);*/

               /* LinearLayout.LayoutParams lParams = (  LinearLayout.LayoutParams) params;
                lParams.gravity = Gravity.CENTER_VERTICAL;
                int ml = (params.width-bpAdIv.getWidth() )/2;
                lParams.setMargins(ml ,0,ml,0);*/
                //Log.v("cgl", " ad to -- 网络图片--->宽:" + params.width + " 高:" + params.height);

/*                ViewGroup.LayoutParams params = bpAdIv.getLayoutParams();
                int vw = bpAdIv.getWidth() - bpAdIv.getPaddingLeft() - bpAdIv.getPaddingRight();
                int vh = bpAdIv.getHeight() - bpAdIv.getPaddingTop() - bpAdIv.getPaddingBottom();
                float scalew =   (float) vw / (float) resource.getIntrinsicWidth()  ;
                float scaleh =  (float) vh / (float) resource.getIntrinsicHeight() ;
                float scale = Math.max(scalew, scaleh);
                Log.v("cgl", " scale  :" + scale  );
                //int vh = Math.round(resource.getIntrinsicHeight() * scale);
                params.width = (int) ((float)(vw / scale)) + bpAdIv.getPaddingTop() + bpAdIv.getPaddingBottom();
                params.height = (int) ((float)(vh / scale)) + bpAdIv.getPaddingTop() - bpAdIv.getPaddingBottom();
                Log.v("cgl", " ad to -- 网络图片--->宽:" +    params.width + " 高:" + params.height);
                bpAdIv.setLayoutParams(params);*/
                //bpAdIv.setBackground(resource);
                bpAdIv.setImageDrawable(resource);
            }
        };
        Glide.with(_mActivity).load(type).into(simpleTarget);

    }

    @Override
    public void onHttpError(String type) {

    }

    public void startToNext() {
        //隐藏路过按钮 防止重复点击按钮
        if(bpBt!=null){
            bpBt.setVisibility(View.GONE);
        }
        UserInfo userInfo = UserInfoManager.queryUserInfo(_mActivity);
        if (userInfo == null) {
            //未登陆 跳转到登陆页面
            Intent intent = new Intent(_mActivity, LoginPageActivity.class);
            startActivity(intent);
            _mActivity.finish();
        } else {
            //已登陆
            MyApplication.setUserInfo(userInfo);
            int status = MyApplication.getUserInfo().getSurnameStatus();
            if (status == 0) {
                //没有姓氏认证的 跳转到姓氏认证                //start(AuthenticationFragment.newInstance());
                Intent authIntel = new Intent(_mActivity, AuthenticationActivity.class);
                //它可以关掉所要到的界面中间的activity
                authIntel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(authIntel);
                _mActivity.finish();
                return;
            }
            //进入app 主页
            Intent intent = new Intent(_mActivity, HomePageActivity.class);
            startActivity(intent);
            _mActivity.finish();
        }
    }

    @Override
    public void versionInfo(final VersionInfo versionInfo) {
        int versionCode = AppUtils.getVersionCode(_mActivity);
        if (versionInfo.getVersionCode() > versionCode) {
            isNewVersion = true;
        }
        //如果没有新版本
        if (!isNewVersion) {
            startPage();
        } else {
            final OneTextTwoBtPop oneTextTwoBtPop = new OneTextTwoBtPop(_mActivity, "点击确定下载最新版本app");
            oneTextTwoBtPop.setIsAnimation(false);
            oneTextTwoBtPop.setCancelable(false);
            oneTextTwoBtPop.setOnOneTextTwoBtPopClickLisenear(new OneTextTwoBtPop.OnOneTextTwoBtPopClickLisenear() {

                @Override
                public void comfirm() {
                    //meSettingPresenter.checkUpdate(_mActivity,versionInfo.getDownLoadPath());
                    // shou update pop
                    CheckUpdateUtil.showUpdatePop(_mActivity, versionInfo.getDownLoadPath(), new CheckUpdateUtil.OnStartUpdateListener() {
                        @Override
                        public void onUpdate(final File file) {
                            apkFile = file;
                            dismiss();
                            startUpdate();
                            startPage();

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
                            loadingPop.setText("更新中..." + fraction + "%");
                        }
                    });
                    oneTextTwoBtPop.dismiss();
                    loadingPop = getLoadingPop("更新中...");
                    loadingPop.show();
                }

                @Override
                public void cancel() {
                    oneTextTwoBtPop.dismiss();
                    startPage();
//                    meSettingClearCacheSize.setText("0 KB");
                }
            });
            oneTextTwoBtPop.show();
        }
    }

    @Override
    public void checkedUpdate(String type) {
        dismiss();
    }

    @Override
    public void checkError(String type) {
        dismiss();
        startPage();
    }


    private void startUpdate() {
        boolean hasInstallPerssion = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hasInstallPerssion = _mActivity.getPackageManager().canRequestPackageInstalls();
            if (hasInstallPerssion) {
                //安装应用的逻辑
                CheckUpdateUtil.installAPK(_mActivity, apkFile);
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP);
            }
        } else {
            CheckUpdateUtil.installAPK(_mActivity, apkFile);
        }

    }
}
