package com.pbids.sanqin.ui.activity.zongquan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.contact.activity.UserProfileActivity;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.QinQinChiUserGroup;
import com.pbids.sanqin.model.entity.QinQinChiUserGroupItem;
import com.pbids.sanqin.presenter.ZongQuanPresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiFriendAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiGroupListAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.letter.SideBar;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.eventbus.PermissionEvent;
import com.pbids.sanqin.utils.zxing.CaptureActivity;
import com.pbids.sanqin.utils.zxing.TestDbUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:10
 * @desscribe 类描述:宗圈首页界面
 * @remark 备注:
 * @see
 */
public class ZongQuanFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,ZongQuanView {

    private static final String Tag ="ZONG_QUAN_FRAGMENT" ;

	private static final String EXTRA_APP_QUIT = "APP_QUIT";
	private static final int REQUEST_CODE_NORMAL = 1;
	private static final int REQUEST_CODE_ADVANCED = 2;
	private static final int BASIC_PERMISSION_REQUEST_CODE = 100;

    @Bind(R.id.zongquan_group_list)
    RecyclerView zongquanGroupList;  //分组列表
    @Bind(R.id.zongquan_friends_list)
    RecyclerView zongquanFriendsList;  //好友列表
    @Bind(R.id.sidrbar)
    SideBar sidrbar;
    @Bind(R.id.zongquan_sv)
    NestedScrollView zongquanSv;
    @Bind(R.id.item_sanqin)
    RelativeLayout itemSanqin;
    @Bind(R.id.item_family)
    RelativeLayout itemFamily;
    @Bind(R.id.item_clan)
    RelativeLayout itemClan;
    @Bind(R.id.rl1)
    RelativeLayout rl1;
    @Bind(R.id.zongquan_group_text)
    TextView zongquanGroupText;

    //分组列表 adapter
    private QinQinChiGroupListAdapter mGroupRecyclerViewAdapter;
    private List<QinQinChiUserGroup> mRecyClearGroups = new ArrayList<>();
	private List<QinQinChiUserGroupItem> mGroupList = new ArrayList<>();

    //好友列表 adapter
    private QinQinChiFriendAdapter mFriendRecyclerViewAdapter;
	private ZongQuanPresenter zongQuanPresenter;

    int topHeight;


    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zong_quan, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftImageCenterViewTitleRightImage(_mActivity);
    }

    //好友列表
	@Override
    public QinQinChiFriendAdapter getFriendAdapter(){
    	return mFriendRecyclerViewAdapter;
	}

	//更新好友列表
	@Override
	public void updateFrendList() {
		mFriendRecyclerViewAdapter.updateListView(zongQuanPresenter.getGroupsList());
	}

	private void initView() {

        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mFriendRecyclerViewAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    View view = zongquanFriendsList.getChildAt(position);
                    zongquanSv.smoothScrollTo(0, view.getTop() + topHeight);
                }
            }
        });

		mGroupList = TestDbUtil.fillGroupData(getResources().getStringArray(R.array.group));
		// init group data test
		QinQinChiUserGroup groupItem = new QinQinChiUserGroup();
		groupItem.setGroups(mGroupList);
		mRecyClearGroups.add(groupItem);


		//init adapter
		mFriendRecyclerViewAdapter = new QinQinChiFriendAdapter(_mActivity, zongQuanPresenter.getGroupsList() );
		mFriendRecyclerViewAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
			@Override
			public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
				//显示用户信息面板
				QinQinChiFriend friend = mFriendRecyclerViewAdapter.getFriend(groupPosition,childPosition);
				String account = friend.getId()+"";
				UserProfileActivity.start(_mActivity, account);
			}
		});

		mGroupRecyclerViewAdapter = new QinQinChiGroupListAdapter(_mActivity, mRecyClearGroups);
		LinearLayoutManager mGroupManager = new LinearLayoutManager(_mActivity);
		mGroupManager.setOrientation(LinearLayoutManager.VERTICAL);
        //初始化分组列表
        zongquanGroupList.setLayoutManager(mGroupManager);
        zongquanGroupList.setAdapter(mGroupRecyclerViewAdapter);
        zongquanGroupList.setNestedScrollingEnabled(false);
        mGroupRecyclerViewAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
			@Override
			public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
				QinQinChiUserGroupItem groupItem =  mRecyClearGroups.get(groupPosition).getGroups().get(childPosition);
				ZQMemberManagerFragment fragment2 = ZQMemberManagerFragment.newInstance();
				fragment2.getArguments().putString("group",groupItem.getName());
				((MainFragment) getParentFragment()).start(fragment2);
			}
		});

		LinearLayoutManager mFriendManager = new LinearLayoutManager(_mActivity);
		mFriendManager.setOrientation(LinearLayoutManager.VERTICAL);
        //初始化好友列表
        zongquanFriendsList.setLayoutManager(mFriendManager);
        zongquanFriendsList.setAdapter(mFriendRecyclerViewAdapter);
        zongquanFriendsList.setNestedScrollingEnabled(false);
        rl1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                topHeight = (int) rl1.getHeight();
				// Log.i("wzh","topHeight: "+topHeight);
            }
        });

		// LoginPagePresenter.doLogin(); //test
		//IM
		zongQuanPresenter.registerObserver(true);
		zongQuanPresenter.registerOnlineStateChangeListener(true);
		zongQuanPresenter.initIM();
		// 加载本地数据
		zongQuanPresenter.reload(false);
    }



	/**
	 * 若增加第三方推送免打扰（V3.2.0新增功能），则：
	 * 1.添加下面逻辑使得 push 免打扰与先前的设置同步。
	 * 2.设置界面 以及
	 * 免打扰设置界面  也应添加 push 免打扰的逻辑
	 * <p>
	 * 注意：isPushDndValid 返回 false， 表示未设置过push 免打扰。
	 */
	private void syncPushNoDisturb(StatusBarNotificationConfig staConfig) {
		//显示好友资料
		List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts(); // 获取所有好友帐号
		List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(accounts); // 获取所有好友用户资料
		if(users!=null){
			for (NimUserInfo nimUserInfo: users){
				QinQinChiFriend friendItem = new QinQinChiFriend();
				friendItem.setId(Long.parseLong(nimUserInfo.getAccount()) );
				friendItem.setName(nimUserInfo.getName());
				//头像
				friendItem.setFaceUrl("http://sanqin-upload.oss-cn-shenzhen.aliyuncs.com/2018/03/08/upfile1479283252839766191.png");
			}
		}
		System.out.print(users);
	}

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static ZongQuanFragment newInstance() {
        Bundle args = new Bundle();
        ZongQuanFragment fragment = new ZongQuanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return zongQuanPresenter = new ZongQuanPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        StatusCode imStatus =  NIMClient.getStatus();
        Log.v(Tag, "im status:"+imStatus);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
		zongQuanPresenter.registerObserver(true);
		zongQuanPresenter.registerOnlineStateChangeListener(false);
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
            	// 扫码
                if (AppUtils.checkCameraPermission(_mActivity, PermissionEvent.REQUEST_CODE_CAMERA)) {
                    startCaptureActivity();
                }
                break;
            case R.id.hp_search:
				// 查找好友
                //((MainFragment) getParentFragment()).start(NewsSearchFragment.newInstandce());
                break;
            case R.id.main_right_layout:
            	// 添加好友
                ((MainFragment) getParentFragment()).start(ZQAddFriendFragment.newInstance());
                break;
        }
    }

    //启动扫码
    public void startCaptureActivity() {
//        if(AppUtils.checkCameraPermission(_mActivity, PermissionEvent.REQUEST_CODE_CAMERA)){
        Intent intent = new Intent(_mActivity, CaptureActivity.class);
        startActivity(intent);
//        }
    }

    @OnClick({R.id.item_sanqin, R.id.item_family, R.id.item_clan,R.id.zongquan_group_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_sanqin:
            	//点击三亲
                ZQMemberManagerFragment fragment1 = ZQMemberManagerFragment.newInstance();
                fragment1.getArguments().putString("group","sanqin");
                ((MainFragment) getParentFragment()).start(fragment1);
                break;
            case R.id.item_family:
            	//点击家族
                ZQMemberManagerFragment fragment2 = ZQMemberManagerFragment.newInstance();
                fragment2.getArguments().putString("group","family");
                ((MainFragment) getParentFragment()).start(fragment2);
                break;
            case R.id.item_clan:
            	//烧砖
                Log.i("wzh","BrickFragment: "+findFragment(BrickFragment.class));
                ((MainFragment) getParentFragment()).start(BrickFragment.newInstance());
                break;
            case R.id.zongquan_group_text:
            	//添加分组
                //((MainFragment) getParentFragment()).start(ZQAddGroupFragment.newInstance());
                break;
        }
    }

	@Override
	public void onHttpSuccess(String type) {

	}

	@Override
	public void onHttpError(String type) {

	}

}
