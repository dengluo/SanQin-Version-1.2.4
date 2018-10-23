package com.pbids.sanqin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.netease.nim.uikit.business.eventbus.InviteMessage;
import com.netease.nim.uikit.business.eventbus.RefreshUnreadCountEvent;
import com.netease.nim.uikit.custom.TeamInfoMain;
import com.netease.nim.uikit.custom.TeamNumber;
import com.netease.nim.uikit.custom.TransFerTeam;
import com.netease.nim.uikit.custom.TransFerTeamSuccess;
import com.netease.nim.uikit.custom.ValJoinTeam;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.component.BadgeView;
import com.pbids.sanqin.helper.SystemMessageUnreadManager;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.BindBackMessage;
import com.pbids.sanqin.model.entity.BindPhoneBack;
import com.pbids.sanqin.model.entity.SystemMessage;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.VersionInfo;
import com.pbids.sanqin.presenter.MainFragmentPresenter;
import com.pbids.sanqin.reminder.ReminderItem;
import com.pbids.sanqin.reminder.ReminderManager;
import com.pbids.sanqin.ui.activity.me.MeFragment;
import com.pbids.sanqin.ui.activity.me.MeInviteFragment;
import com.pbids.sanqin.ui.activity.me.MeScanBankFragment;
import com.pbids.sanqin.ui.activity.me.PostDownDialog;
import com.pbids.sanqin.ui.activity.news.NewsIMFragment;
import com.pbids.sanqin.ui.activity.zhizong.GenealogyFragment;
import com.pbids.sanqin.ui.activity.zhizong.PickUpBrickDialog;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebSideslipFragment;
import com.pbids.sanqin.ui.activity.zhizong.component.PickUpBrickView;
import com.pbids.sanqin.ui.activity.zongquan.BrickFragment;
import com.pbids.sanqin.ui.activity.zongquan.ContactListFragment;
import com.pbids.sanqin.ui.activity.zongquan.MainFragmentView;
import com.pbids.sanqin.ui.activity.zongquan.ZhiZongCasesCrowdFragment;
import com.pbids.sanqin.ui.adapter.HomePageVPNameAdapter;
import com.pbids.sanqin.ui.view.BottomBarView;
import com.pbids.sanqin.ui.view.OneTextTwoBtPop;
import com.pbids.sanqin.ui.view.dialog.LoadingDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.CheckUpdateUtil;
import com.pbids.sanqin.utils.eventbus.NimSystemMessageEvent;
import com.pbids.sanqin.utils.eventbus.SystemMessageHandleEvent;
import com.pbids.sanqin.utils.eventbus.ToWindowFocusChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:21
 * @desscribe 类描述:包含主页四个界面的主fragment
 * @remark 备注:
 * @see
 */
public class MainFragment extends BaseFragment implements BottomBarView.OnBottomBarViewClickLisener
        , HomePageVPNameAdapter.OnVpNameClickLisener, ReminderManager.UnreadNumChangedCallback, PickUpBrickView, MainFragmentView {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    //博古架
    HomePageVPNameAdapter mHomePageVPNameAdapter;

    private MainFragmentPresenter mainFragmentPresenter;

    private DisposableObserver observer;

    @Bind(R.id.hp_vp_name_left_bt)
    Button hpVpNameLeftBt;
    @Bind(R.id.hp_vp_name_right_bt)
    Button hpVpNameRightBt;
    @Bind(R.id.hp_vp_name)
    ViewPager hpVpName;
    @Bind(R.id.hp_vp_name_layout)
    FrameLayout hpVpNameLayout;
    @Bind(R.id.hp_vp_name_top_layout)
    View hpVpNameTopLayout;
    @Bind(R.id.hp_vp_name_left_layout)
    LinearLayout hpVpNameLeftLayout;
    @Bind(R.id.hp_vp_name_right_layout)
    LinearLayout hpVpNameRightLayout;
    @Bind(R.id.main_bottom_tab)
    BottomBarView mBottomBarView;
    ArrayList<String> surnames;
    int surnameSize = 0;
    int currentSelectorNamePage = 0;

    private SupportFragment[] mFragments = new SupportFragment[5];

    private int systemMessageNum = 0;

    private Handler mHandler = new Handler();


    private int pagePosition = 0;

    private boolean isNewVersion = false;

    private File apkFile = null;

    private LoadingDialog loadingPop;

    private static final int REQUEST_CODE_UNKNOWN_APP = 529;

    private OneTextTwoBtPop oneTextTwoBtPop;

    private SharedPreferences sharedPreferences;

    private int versionCode;

    public static MainFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type",type);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        init(view);
        // eventbux
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        sharedPreferences = _mActivity.getSharedPreferences("sanqin", Context.MODE_PRIVATE);
        boolean isGuide = sharedPreferences.getBoolean("isGuide", false);
        if(isGuide){
//            有引导指示
            final AppLeadPageDialog appLeadPageDialog = new AppLeadPageDialog(getContext(), R.style.pick_dialog);
            appLeadPageDialog.show();
            appLeadPageDialog.setOnClick(new AppLeadPageDialog.OnClick() {
                @Override
                public void onClick() {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("isGuide",false);
                    edit.commit();
                    appLeadPageDialog.dismiss();
                    //后台检查更新
                    checkUpdate();
                    //是否需要更新UserInfo，用于在版本大更新的时候，由于后台新增了很多字段，供前端使用，如果没有更新信息，可能会导致OOM
                    checkUpdateUser();
                }
            });
        }else{
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("isGuide",false);
            edit.commit();
            //后台检查更新
            checkUpdate();
            //是否需要更新UserInfo，用于在版本大更新的时候，由于后台新增了很多字段，供前端使用，如果没有更新信息，可能会导致OOM
            checkUpdateUser();
        }
        Bundle bundle = getArguments();
        String type = bundle.getString("type");
        if(type.equals("choujiang")){
            ZhiZongWebSideslipFragment zhiZongWebFragment = ZhiZongWebSideslipFragment.newInstance();
            Bundle arguments = zhiZongWebFragment.getArguments();
            arguments.putString("link","https://www.baidu.com");
            start(zhiZongWebFragment);
        }
        return view;
    }

    private void checkUpdateUser() {
        sharedPreferences = _mActivity.getSharedPreferences("sanqin-version", Context.MODE_PRIVATE);
        versionCode = AppUtils.getVersionCode(_mActivity);
        int beforeVersion = sharedPreferences.getInt("version", 0);
        if(versionCode>beforeVersion){
            //需要更新UserInfo
            addDisposable(mainFragmentPresenter.updateUserInfo(AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_USER_QUERYUSERINFO));
        }

    }

    private void checkUpdate() {
        HttpParams params = new HttpParams();
        params.put("appType", 1);
        String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_QUERY_APPVERSION;
        addDisposable(mainFragmentPresenter.checkAppVersion(url, params, "check"));
    }

    private void valDialog() {
        UserInfo userInfo = MyApplication.getUserInfo();

        if (userInfo.getPhone().equals("")) {
            showBindPhoneDialog(this, DEFAULTTYPE);
        } else if (userInfo.getIsRealName() != 1) {
            showRealNameDialog(this, DEFAULTTYPE);
        } else {
            //领砖头
            observer = mainFragmentPresenter.pickBrick();
        }



        /*if(MyApplication.getUserInfo().getIsRealName()!=1){
            final OneTextTwoBtDialog oneTextTwoBtDialog = new OneTextTwoBtDialog(_mActivity);
            oneTextTwoBtDialog.setGrayCenter();
            oneTextTwoBtDialog.setComfirmText("前往");
            oneTextTwoBtDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                @Override
                public void confirm(View view) {
                    oneTextTwoBtDialog.dismiss();
                    if(MyApplication.getUserInfo().getPhone().equals("")){
                        final OneTextTwoBtDialog oneTextTwoBtDialogPhone = new OneTextTwoBtDialog(_mActivity);
                        oneTextTwoBtDialogPhone.setGrayCenter();
                        oneTextTwoBtDialogPhone.setComfirmText("前往");
                        oneTextTwoBtDialogPhone.setOnDialogClickLisenrar(new OnDialogClickListener() {
                            @Override
                            public void confirm(View view) {
                                oneTextTwoBtDialogPhone.dismiss();
                                start(MeBindingNumberFragment.newInstance());
                            }
                            @Override
                            public void cancel(View view) {
                                oneTextTwoBtDialogPhone.dismiss();
                            }
                        });
                        oneTextTwoBtDialogPhone.setContentText("您还未绑定手机号码，是否前往手机绑定页面");
                        oneTextTwoBtDialogPhone.show();
                    }else{
                        start(MeAuthenticationFragment.newInstance());
                    }
                }

                @Override
                public void cancel(View view) {
                    oneTextTwoBtDialog.dismiss();
                }
            });
            oneTextTwoBtDialog.setContentText("您尚未实名认证，完成认证将获得更多福利！是否前往实名认证界面？");
            TextView contentTv = oneTextTwoBtDialog.getTextView();
            Button twoButtonOne = oneTextTwoBtDialog.getTwoButtonOne();
            Button twoButtonTwo = oneTextTwoBtDialog.getTwoButtonTwo();
            contentTv.setTextSize(13);
            twoButtonOne.setTextSize(13);
            twoButtonTwo.setTextSize(13);
            oneTextTwoBtDialog.show();
        } else {
            //领砖头
            observer = mainFragmentPresenter.pickBrick();
        }*/
    }

    public void init(View view) {
//        mNameViews = new ArrayList<View>();
        EventBusActivityScope.getDefault(_mActivity).register(this);
//        mBottomBarView = (BottomBarView) view.findViewById(R.id.main_bottom_tab);
//        hpVpName = (ViewPager) view.findViewById(R.id.hp_vp_name);
//        hpVpNameTopLayout = view_donate_records.findViewById(R.id.hp_vp_name_top_layout);

        mBottomBarView.setOnBottomBarViewClickLisener(this);
        //3秒后收起
        mBottomBarView.startNameViewPagerAnimation();

        //添加中间ViewPager元素
        surnames = initSurnames();
        ArrayList<String> surnameIds = initSurnameIds();
//        surnameSize = surnames.size();
//        if (surnames.size() > 1) {
//            hpVpNameRightLayout.setVisibility(View.VISIBLE);
//        }

        mHomePageVPNameAdapter = new HomePageVPNameAdapter(_mActivity, surnames, surnameIds);
        mHomePageVPNameAdapter.setVpNameClickLisener(this);
        hpVpName.setOnPageChangeListener(onNamePageChangeListener);
        hpVpName.setAdapter(mHomePageVPNameAdapter);
        hpVpName.setOnTouchListener(mNameOnTouchListener);
        hpVpNameTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomBarView.startNameViewPagerAnimationNow();
            }
        });
        mBottomBarView.setOnBottomBarViewAnimLisenear(new BottomBarView.OnBottomBarViewAnimLisenear() {
            @Override
            public void openAnimStart() {
                hpVpNameTopLayout.setClickable(true);
            }

            @Override
            public void openAnimEnd() {
//                hpVpNameTopLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void closeAnimStart() {
                hpVpNameTopLayout.setClickable(false);
            }

            @Override
            public void closeAnimEnd() {
//                hpVpNameTopLayout.setVisibility(View.GONE);
            }
        });
        //读取系统消息数量
        readSystemMessageNum();

        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();

    }

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        //子类响应 onFragmentResult
        for (int i = 0; i < mFragments.length; i++) {
            SupportFragment item = mFragments[i];
            if (item != null) {
                item.onFragmentResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            //Log.v("cgl",""+unreadCount);
            //SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            //ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };

    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 未读消息数量观察者实现
     */
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        //System.out.print(item);
        if (mBottomBarView != null) {
            BadgeView badgeContact = (BadgeView) mBottomBarView.findViewById(R.id.badge_contact);
            List<SystemMessageType> types = new ArrayList<>();
            types.add(SystemMessageType.AddFriend);
            types.add(SystemMessageType.TeamInvite);
            // 查询“添加好友”类型的系统通知未读数总和
            int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountByType(types);
            if (unread > 0) {
                //Todo 暂时去掉红点
                badgeContact.setVisibility(View.VISIBLE);
            } else {
                badgeContact.setVisibility(View.GONE);
            }
        }
        //更新角标
        EventBus.getDefault().post(new RefreshUnreadCountEvent());
        //消息
        showImBadage();

       /* MainTab tab = MainTab.fromReminderId(item.getId());
        if (tab != null) {
            tabs.updateTab(tab.tabIndex, item);
        }*/
    }

    @Subscribe
    public void onToWindowFocusChanged(ToWindowFocusChanged toWindowFocusChanged) {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams params = hpVpNameTopLayout.getLayoutParams();
        params.height = ((ZhiZongFragment) mFragments[FIRST]).getAutoViewPagerHeight() + (int) _mActivity.getResources().getDimension(R.dimen.dp_40);
        hpVpNameTopLayout.setLayoutParams(params);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    int currentPosition = 0;

    @Override
    public void OnTabClick(int hiddePosition, int showPosition) {
        showHideFragment(mFragments[showPosition], mFragments[hiddePosition]);
        currentPosition = showPosition;
    }

    @Override
    public FrameLayout returnHpVpNameLayout() {
        return hpVpNameLayout;
    }

    //更新博古架内容
    public void updateVPNameLayout(UserInfo userInfo) {
        UserInfo userInfo_ = MyApplication.getUserInfo();
        userInfo_.updateInfo(userInfo);
        UserInfoManager.updateUserInfo(_mActivity, userInfo_);
        MyApplication.setUserInfo(userInfo_);

        mHomePageVPNameAdapter.updateInfomation(initSurnames(), initSurnameIds());
        hpVpName.setAdapter(mHomePageVPNameAdapter);
        hpVpName.setCurrentItem(0);


    }

    private ArrayList<String> initSurnames() {
        UserInfo userInfo = MyApplication.getUserInfo();
        String[] noticeName = userInfo.getNoticeSurnames().split("[,]");
        ArrayList<String> surnames = new ArrayList<String>();
        surnames.add(0, userInfo.getSurname());
        if (!"".equals(userInfo.getNoticeSurnames())) {
            for (int i = 0; i < noticeName.length; i++) {
                //Log.i("wzw","noticeName"+i+": "+ noticeName[i]);
                surnames.add(noticeName[i]);
            }
        }
        if (surnames.size() > 1) {
            hpVpNameRightLayout.setVisibility(View.VISIBLE);
        } else {
            hpVpNameRightLayout.setVisibility(View.GONE);
        }
        surnameSize = surnames.size();
        //Log.i("wzh","surnames: "+surnames.toString());
        return surnames;
    }

    private ArrayList<String> initSurnameIds() {
        UserInfo userInfo = MyApplication.getUserInfo();
        String[] noticeSurnameIds = userInfo.getNoticeSurnameIds().split("[,]");
        ArrayList<String> surnameIds = new ArrayList<String>();
        surnameIds.add(0, "" + userInfo.getSurnameId());
        if (!"".equals(userInfo.getNoticeSurnameIds())) {
            for (int i = 0; i < noticeSurnameIds.length; i++) {
                //Log.i("wzw","noticeSurnameIds"+i+": "+ noticeSurnameIds[i]);
                surnameIds.add(noticeSurnameIds[i]);
            }
        }
        //Log.i("wzh","surnameIds: "+surnameIds.toString());
        return surnameIds;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(ZhiZongFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = ZhiZongFragment.newInstance();
//            mFragments[SECOND] = ZongQuanFragment.newInstance();
//            mFragments[SECOND] = ContactsFragment.newInstance();
            mFragments[SECOND] = ContactListFragment.newInstance();
            mFragments[THIRD] = NewsIMFragment.newInstance();
            mFragments[FOURTH] = MeFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]
//                    mFragments[FIVE]
            );
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
//            mFragments[SECOND] = findChildFragment(ZongQuanFragment.class);
//            mFragments[SECOND] = findChildFragment(ContactsFragment.class);
            mFragments[SECOND] = findChildFragment(ContactListFragment.class);
            mFragments[THIRD] = findChildFragment(NewsIMFragment.class);
            mFragments[FOURTH] = findChildFragment(MeFragment.class);
//            mFragments[FIVE] = findChildFragment(GenealogyFragment.class);
        }
    }

    public void startFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

    public void startFragmentForResult(SupportFragment targetFragment, int code) {
        startForResult(targetFragment, code);
    }

    @Override
    public void nameClick(View v, String surname, String surnameId) {
        //Log.i("wzh", surname+" id:"+surnameId );
//        showHideFragment(mFragments[4], mFragments[currentPosition]);
        GenealogyFragment fragment = GenealogyFragment.instance();
        fragment.getArguments().putString("surname", surname);
        fragment.getArguments().putString("surnameId", surnameId);
        start(fragment);
        mBottomBarView.startNameViewPagerAnimationNow();
//        mBottomBarView.stopNameViewPagerAnimation();
    }

    @Override
    public void otherClick(View v) {
        final UserInfo userInfo = MyApplication.getUserInfo();
        switch (v.getId()) {
            case R.id.hp_history_people:
                //ADDRESS_NEWARTICLE_JIACI
//                startFragment(BrickFragment.newInstance());
                if (currentSelectorNamePage < surnames.size()) {
                    ZhiZongWebFragment fragment5 = ZhiZongWebFragment.newInstance();
                    fragment5.getArguments().putString("link",
                            AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_NEWARTICLE_JIACI +
                                    "surname=" + surnames.get(currentSelectorNamePage));
                    start(fragment5);
                }
                break;
            case R.id.hp_customs:
                ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                fragment.getArguments().putString("link",
                        AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_NEWARTICLE_FOUR + "arctype=11");
                start(fragment);
                break;
            case R.id.hp_history_reilc:
                ZhiZongWebFragment fragment1 = ZhiZongWebFragment.newInstance();

                String surname = userInfo.getSurname();
                long uid = userInfo.getUserId();
                fragment1.getArguments().putString("link",
                        AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_NEWARTICLE_FOUR + "arctype=12");
                start(fragment1);
                break;
            case R.id.hp_now_people:
//                if(currentSelectorNamePage < surnames.size()){
//                    ZhiZongWebFragment fragment4 = ZhiZongWebFragment.newInstance();
//
//                    fragment4.getArguments().putString("link",
//                            AddrConst.SERVER_ADDRESS_NEWS+ AddrConst.ADDRESS_NEWARTICLE_ZONGREN +
//                                    "surname="+surnames.get(currentSelectorNamePage));
//                    start(fragment4);
//                }
                final MainFragment mainFragment = this;
                if (userInfo.getPhone().equals("")) {
                    showBindPhoneDialog(mainFragment, TOTEAMTYPE);
                } else if (userInfo.getIsRealName() == 0) {
                    showRealNameDialog(mainFragment, TOTEAMTYPE);
                } else {
                    //实名认证后
                    final PostDownDialog postDownDialog = new PostDownDialog(getContext(), R.style.post_down_dialog);
                    postDownDialog.show();
                    final ImageView postDownStepIv = postDownDialog.getPostDownStepIv();
                    Glide.with(this).load(R.drawable.team_first_img).into(postDownStepIv);
                    AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.open_team_alpha);
                    postDownStepIv.startAnimation(alphaAnimation);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            postDownDialog.dismiss();
                            start(ZhiZongCasesCrowdFragment.instance());
                        }
                    }, 2000);
                }
                break;
            case R.id.hp_jiaxun:
                ZhiZongWebFragment fragment3 = ZhiZongWebFragment.newInstance();
                fragment3.getArguments().putString("link",
                        AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_NEWARTICLE_FOUR + "arctype=14");
                start(fragment3);
                break;
            case R.id.hp_fanily_news:
                ZhiZongWebFragment fragment2 = ZhiZongWebFragment.newInstance();
                fragment2.getArguments().putString("link",
                        AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_NEWARTICLE_FOUR + "arctype=13");
                start(fragment2);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        dismiss();
        ButterKnife.unbind(this);
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
    }

    ViewPager.OnPageChangeListener onNamePageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Log.i("wzh", "onPageScrolled");
        }

        @Override
        public void onPageSelected(int position) {
            currentSelectorNamePage = position;
            initLeftRightNameLayout(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initLeftRightNameLayout(int position) {
        pagePosition = position;
        //Log.i("wzw","position: "+position);
        if (position == 0) {
            hpVpNameRightLayout.setVisibility(View.VISIBLE);
            hpVpNameLeftLayout.setVisibility(View.GONE);
        } else if (position > 0 && position < surnameSize - 1) {
            hpVpNameRightLayout.setVisibility(View.VISIBLE);
            hpVpNameLeftLayout.setVisibility(View.VISIBLE);
        } else if (position == surnameSize - 1) {
            hpVpNameRightLayout.setVisibility(View.GONE);
            hpVpNameLeftLayout.setVisibility(View.VISIBLE);
        }
    }

    View.OnTouchListener mNameOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Log.i("wzh", "mBottomBarView.isShowing()" + mBottomBarView.isShowing());
            if (!mBottomBarView.isShowing()) {
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBottomBarView.stopNameViewPagerAnimation();
                    break;
                case MotionEvent.ACTION_UP:
                    //mBottomBarView.startNameViewPagerAnimationForClear();
                    mBottomBarView.startNameViewPagerAnimation();
                    break;
                default:
                    break;
            }
            //Log.i("wzh", "mNameOnTouchListener");
            return false;
        }
    };

    @OnClick({R.id.hp_vp_name_left_layout, R.id.hp_vp_name_right_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.hp_vp_name_left_layout:
                hpVpName.setCurrentItem(pagePosition - 1);
                break;
            case R.id.hp_vp_name_right_layout:
                hpVpName.setCurrentItem(pagePosition + 1);
                break;
        }
    }

    public void showImBadage() {
        //系统消息变动
        //公众号订阅变动
        //int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        int unread = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        //LogUtil.i("cgl", "getTotalUnreadCount:"+unread);
        if (mBottomBarView != null) {
            BadgeView badgeIm = (BadgeView) mBottomBarView.findViewById(R.id.badge_im);
            if (systemMessageNum > 0 || unread > 0) {
                badgeIm.setVisibility(View.VISIBLE);
            } else {
                badgeIm.setVisibility(View.GONE);
            }
        }

    }

    //读取系统消息数量
    private int readSystemMessageNum() {
        List<SystemMessage> sysLists = SystemMessageManager.query(getContext(), SystemMessageManager.TYPE_SYSTEM, false);
        List<SystemMessage> subLists = SystemMessageManager.query(getContext(), SystemMessageManager.TYPE_TOPIC, false);
        systemMessageNum = Math.max(sysLists.size(), subLists.size());
        return systemMessageNum;
    }

    //事件接收
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickNotificationEvent(SystemMessageHandleEvent mssageHandle) {
        if (mssageHandle.getType() == SystemMessageManager.TYPE_SYSTEM || mssageHandle.getType() == SystemMessageManager.TYPE_TOPIC) {
            readSystemMessageNum();
            showImBadage();
        }
    }

    //显示系统消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNimSystemMessageEvent(NimSystemMessageEvent evt) {
        // if (evt.getType() == NimSystemMessageEvent.ADD_FRIEND) {
        //MainFragment mainFragment = findFragment(MainFragment.class);
        //  if (mainFragment != null) {
        //      mainFragment.OnTabClick(mainFragment.currentPosition, 1);
        //  }
        //  }
//        this.OnTabClick(currentPosition, 1);
        if(mBottomBarView!=null){
            mBottomBarView.onclickContact();
        }

    }


    /**
     * 跳新人邀请页面
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(InviteMessage messageEvent) {
        start(MeInviteFragment.instance());
    }

    /**
     * 绑定手机号
     * @param bindPhoneBack
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BindPhoneBack bindPhoneBack) {
//        start(MeAuthenticationFragment.newInstance());
        showRealNameDialog(this, bindPhoneBack.getToPage());
    }

    /**
     * 绑定银行卡
     * @param bindBackMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BindBackMessage bindBackMessage){
        start(MeScanBankFragment.newInstance(BaseFragment.TOBANKTYPE));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(TeamInfoMain teamInfo) {
//        start(MeAuthenticationFragment.newInstance());
        String id = teamInfo.getId();
        String name = teamInfo.getName();
        addDisposable(mainFragmentPresenter.updataUserGroup(id, name, ""));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void transFerTeamEvent(TransFerTeam transFerTeam) {
        String account = transFerTeam.getAccount();
        String teamId = transFerTeam.getTeamId();
        addDisposable(mainFragmentPresenter.tranfeTeam(teamId, Integer.valueOf(account)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void transFerTeamEvent(ValJoinTeam valJoinTeam) {
        addDisposable(mainFragmentPresenter.valJoinTeam());
    }


    @Override
    public void pickBrickSuccess(String message) {
        toast(message);
        start(BrickFragment.newInstance());
    }

    @Override
    public void pickBrickFailed(String message) {
        toast(message);
    }

    @Override
    public BasePresenter initPresenter() {
        return this.mainFragmentPresenter = new MainFragmentPresenter(this);
    }

    @Override
    public void showDialog(int number) {
        //弹出领取砖头页面 TODO
        PickUpBrickDialog picUpBrickDialog = new PickUpBrickDialog(getActivity(), R.style.pick_dialog, this, number);
        picUpBrickDialog.show();
    }

    /**
     * 群资料修改名称成功回调
     *
     * @param message
     * @param type
     */
    @Override
    public void updateSuccess(String message, int type) {
        if (type == 1) {
            EventBus.getDefault().post(new TransFerTeamSuccess(true));
        }

    }

    /**
     * 群资料修改名称错误回调
     *
     * @param message
     * @param type
     */
    @Override
    public void updateFailed(String message, int type) {
        toast(message);
        if (type == 1) {
            EventBus.getDefault().post(new TransFerTeamSuccess(false));
        }
    }

    /**
     * 消息页面点击加入群同意，后台人数限制成功回调
     *
     * @param number
     */
    @Override
    public void joinTeamOk(int number) {
        EventBus.getDefault().post(new TeamNumber(number));
    }

    /**
     * 消息页面点击加入群同意，后台人数限制错误回调
     */
    @Override
    public void joinTeamNo(String message) {
        toast(message);
    }

    @Override
    public void versionInfo(final VersionInfo versionInfo) {
        int versionCode = AppUtils.getVersionCode(_mActivity);
        if (versionInfo.getVersionCode() > versionCode) {
            isNewVersion = true;
        }
        if(isNewVersion){
            oneTextTwoBtPop  = new OneTextTwoBtPop(_mActivity, "点击确定下载最新版本app");
            //强制更新，将返回隐藏
            if(versionInfo.getForceupdate()==1) oneTextTwoBtPop.getCancelLin().setVisibility(View.GONE);
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
                    valDialog();
//                    meSettingClearCacheSize.setText("0 KB");
                }
            });
            oneTextTwoBtPop.show();
        }else{
            valDialog();
        }
    }

    @Override
    public void checkError(String message) {
        valDialog();
    }

    /**
     * 更新用户信息
     * @param userInfo
     */
    @Override
    public void updateUserInfo(UserInfo userInfo) {
        Long id = MyApplication.getUserInfo().get_id();
        userInfo.set_id(id);
        MyApplication.setUserInfo(userInfo);
        UserInfoManager.updateUserInfo(_mActivity, MyApplication.getUserInfo());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("version",versionCode);
        edit.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_UNKNOWN_APP:
                CheckUpdateUtil.installAPK(_mActivity, apkFile);
                break;
        }
    }

    private void startUpdate() {
        oneTextTwoBtPop.show();
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