package com.pbids.sanqin.ui.activity.me;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.MeInformationUpdatePresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.CircleImageView;
import com.pbids.sanqin.utils.AddrConst;

import java.text.NumberFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2017/12/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:26
 * @desscribe 类描述:我的个人信息-等级界面
 * @remark 备注:
 * @see
 */
public class MeInformationLevelFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear ,MeInformationUpdateView {

    MeInformationUpdatePresenter updatePresenter;

    @Bind(R.id.me_information_head)
    CircleImageView meInformationHead;
    @Bind(R.id.me_level_progress)
    ProgressBar meLevelProgress;
    @Bind(R.id.me_level_progress_number)
    TextView meLevelProgressNumber;
    @Bind(R.id.me_level_current_exp)
    TextView meLevelCurrentExp;
    @Bind(R.id.me_level_need_exp)
    TextView meLevelNeedExp;
    @Bind(R.id.me_level_username)
    TextView meLevelUsername;
    @Bind(R.id.me_information_firstname)
    TextView meInformationFirstname;
    @Bind(R.id.me_level_levelname)
    TextView meLevelLevelname;
    @Bind(R.id.me_home_zong)
    ImageView meHomeZong;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.me_information_vip)
    TextView meInformationVip;

    public static MeInformationLevelFragment newInstance() {
        MeInformationLevelFragment fragment = new MeInformationLevelFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_level, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("我的等级",_mActivity);
    }

    public void initView(){
        HttpParams httpParams = new HttpParams();
        addDisposable(updatePresenter.submitInformation(AddrConst.SERVER_ADDRESS_USER
                + AddrConst.ADDRESS_USER_QUERYUSERINFO,httpParams,"1"));

        updateHeadPortrait();
        initDate();
        UserInfo userInfo = MyApplication.getUserInfo();
        if(userInfo.getVip()!=0){
            ll3.setVisibility(View.VISIBLE);
            meInformationVip.setText("VIP"+userInfo.getVip());
        }
        if(userInfo.getClanStatus()==1){
            meHomeZong.setVisibility(View.VISIBLE);
        }
    }

    public void initDate(){
        int size = (int)getResources().getDimension(R.dimen.dp_120);
        int currentEmpiric = MyApplication.getUserInfo().getCurrentEmpiric();
        int upgradeEx = MyApplication.getUserInfo().getUpgradeEx();
        Log.i("wzh", "currentEx: "+currentEmpiric);
        Log.i("wzh", "upgradeEx: "+upgradeEx);

        meLevelUsername.setText(MyApplication.getUserInfo().getName());
        meLevelLevelname.setText("("+MyApplication.getUserInfo().getLevelName()+")");
        meLevelCurrentExp.setText(""+currentEmpiric);
        meLevelNeedExp.setText(""+(upgradeEx-currentEmpiric));

        meLevelProgressNumber.setText(currentEmpiric+"/"+upgradeEx);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        String exStr = numberFormat.format((float)currentEmpiric/(float)upgradeEx *100);
        Log.i("wzh", "exStr: "+exStr);
        meLevelProgress.setProgress(("".equals(exStr)||null ==exStr)?0:Integer.valueOf(exStr));
    }

    @Override
    public BasePresenter initPresenter() {
        return updatePresenter = new MeInformationUpdatePresenter(this);
    }

    public void updateHeadPortrait(){
        int size = (int)getResources().getDimension(R.dimen.dp_110);
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity
                ,meInformationHead,MyApplication.getUserInfo().getFaceUrl(), new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                //ToDo 发生空指针异常
                try{
                    meInformationFirstname.setVisibility(View.VISIBLE);
                    Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/华文行楷.ttf");
                    meInformationFirstname.getPaint().setTypeface(typeface);
//                MyApplication.getUserInfo().setSurname("速度");
                    if(MyApplication.getUserInfo().getSurname().length()==1){
                        meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_15));
                        meInformationFirstname.setLineSpacing(0,0.9F);
                    }else if(MyApplication.getUserInfo().getSurname().length()==2){
                        meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_11));
                        meInformationFirstname.setLineSpacing(0,0.8F);
                    }
                    meInformationFirstname.setText(MyApplication.getUserInfo().getSurname()+"府");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                meInformationFirstname.setVisibility(View.INVISIBLE);
                return false;
            }
        });
//        Glide.with(_mActivity).load(MyApplication.getUserInfo().getFaceUrl())
//                .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default_)
//                .dontAnimate()
//                .override(size,size).listener(new RequestListener<String, GlideDrawable>() {
//            @Override
//            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                meInformationFirstname.setVisibility(View.VISIBLE);
//                Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/华文行楷.ttf");
//                meInformationFirstname.getPaint().setTypeface(typeface);
////                MyApplication.getUserInfo().setSurname("速度");
//                if(MyApplication.getUserInfo().getSurname().length()==1){
//                    meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_15));
//                    meInformationFirstname.setLineSpacing(0,0.9F);
//                }else if(MyApplication.getUserInfo().getSurname().length()==2){
//                    meInformationFirstname.setTextSize(_mActivity.getResources().getDimension(R.dimen.sp_11));
//                    meInformationFirstname.setLineSpacing(0,0.8F);
//                }
//                meInformationFirstname.setText(MyApplication.getUserInfo().getSurname()+"府");
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                meInformationFirstname.setVisibility(View.INVISIBLE);
//                return false;
//            }
//        }).into(meInformationHead);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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

    }

    @Override
    public void getUserInfo(UserInfo userInfo) {
        MyApplication.getUserInfo().updateInfo(userInfo);
        UserInfoManager.updateUserInfo(_mActivity,MyApplication.getUserInfo());
        initDate();
    }

//    @OnClick(R.id.me_title_left_layout)
//    public void onViewClicked() {
//        pop();
//    }
}
