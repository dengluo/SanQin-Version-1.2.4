package com.pbids.sanqin.presenter;

import android.os.Handler;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.core.provider.ContactDataProvider;
import com.netease.nim.uikit.business.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nim.uikit.impl.cache.UIKitLogTag;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;
import com.pbids.sanqin.ui.activity.zongquan.ZongQuanView;
import com.pbids.sanqin.ui.view.letter.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * presenter
 * 类描述:搜索好友界面
 * Created by pbids903 on 2018/1/5.
 */

public class ZongQuanPresenter extends BasePresenter{

	ZongQuanView mZongQuanView;
    //列表数据
    List<QinQinChiFriendGroup> qinQinChiFriendGroups = new ArrayList<>(); //好友
	QinQinChiFriendGroup friendGroup ; //
	List<QinQinChiFriend> friendList = new ArrayList<>();


	List<QinQinChiFriendGroup> myGroups;  //分组

	private ReloadFrequencyControl reloadControl = new ReloadFrequencyControl();

	private PinyinComparator pinyinComparator;  //右侧拼音索引
//	private CharacterParser characterParser;


	private static final Handler handler = new Handler();
	protected final Handler getHandler() {
		return handler;
	}

	public ZongQuanPresenter(ZongQuanView zongQuanView){
        this.mZongQuanView = zongQuanView;
        //初始化分组列表数据
		friendGroup = new QinQinChiFriendGroup();
		friendGroup.setSortUserModels(friendList);
		qinQinChiFriendGroups.add(friendGroup);

//		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		//赋值测试用户数据
		List<QinQinChiFriend>  mFriendList = new ArrayList<>(); //TestDbUtil.fillFrendData(getResources().getStringArray(R.array.date));
		myGroups = PinyinComparator.groupTo(mFriendList);
		Collections.sort(myGroups, pinyinComparator);
	}
	public List<QinQinChiFriendGroup> getGroupsList(){
		return myGroups;
	}

	public List<QinQinChiFriendGroup> getQinQinChiFriendGroups(){
    	return qinQinChiFriendGroups;
	}

	// init
	public void initIM(){
		if(NIMClient.getStatus()!= StatusCode.CONNECTING){
			return;
		}
		//显示好友资料
		IContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.FRIEND);
		int userCount = NimUIKit.getContactProvider().getMyFriendsCount();
		//countText.setText("共有好友" + userCount + "名");
		Log.v("zongQuanPresenter","共有好友" + userCount + "名");
		List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts(); // 获取所有好友帐号
		List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(accounts); // 获取所有好友用户资料

		if(users!=null){
			List<QinQinChiFriend>  mFriendList = new ArrayList<>();
			for (NimUserInfo nimUserInfo: users){
				QinQinChiFriend friendItem = new QinQinChiFriend();
				friendItem.setId(Long.parseLong(nimUserInfo.getAccount()) );
				friendItem.setName(nimUserInfo.getName());
				//头像
				friendItem.setFaceUrl("http://sanqin-upload.oss-cn-shenzhen.aliyuncs.com/2018/03/08/upfile1479283252839766191.png");
				mFriendList.add(friendItem);

			}
			myGroups = PinyinComparator.groupTo(mFriendList); // .get(0).getSortUserModels().insert(friendItem);
			Collections.sort(myGroups, pinyinComparator);
			mZongQuanView.updateFrendList();
		}
	}


	/**
	 * *********************************** 通讯录加载控制 *******************************
	 */

	/**
	 * 加载通讯录数据并刷新
	 *
	 * @param reload true则重新加载数据；false则判断当前数据源是否空，若空则重新加载，不空则不加载
	 */
	public void reload(boolean reload) {
		if (!reloadControl.canDoReload(reload)) {
			return;
		}
		/*// 开始加载
		if (!this.load(reload)) {
			// 如果不需要加载，则直接当完成处理
			onReloadCompleted();
		}*/
		// 开始加载
		onReloadCompleted();
	}


	public void onReloadCompleted() {
		if (reloadControl.continueDoReloadWhenCompleted()) {
			// 计划下次加载，稍有延迟
			getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					boolean reloadParam = reloadControl.getReloadParam();
					Log.i(UIKitLogTag.CONTACT, "continue reload " + reloadParam);
					reloadControl.resetStatus();
					reload(reloadParam);
				}
			}, 50);
		} else {
			// 本次加载完成
			reloadControl.resetStatus();
		}
		LogUtil.i(UIKitLogTag.CONTACT, "contact load completed");
	}

	/**
	 * 通讯录加载频率控制
	 */
	class ReloadFrequencyControl {
		boolean isReloading = false;
		boolean needReload = false;
		boolean reloadParam = false;

		boolean canDoReload(boolean param) {
			if (isReloading) {
				// 正在加载，那么计划加载完后重载
				needReload = true;
				if (param) {
					// 如果加载过程中又有多次reload请求，多次参数只要有true，那么下次加载就是reload(true);
					reloadParam = true;
				}
				LogUtil.i(UIKitLogTag.CONTACT, "pending reload task");
				return false;
			} else {
				// 如果当前空闲，那么立即开始加载
				isReloading = true;
				return true;
			}
		}

		boolean continueDoReloadWhenCompleted() {
			return needReload;
		}

		void resetStatus() {
			isReloading = false;
			needReload = false;
			reloadParam = false;
		}

		boolean getReloadParam() {
			return reloadParam;
		}
	}


	/**
	 * *********************************** 用户资料、好友关系变更、登录数据同步完成观察者 *******************************
	 */

	public void registerObserver(boolean register) {
		NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, register);
		NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
		LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(loginSyncCompletedObserver);
	}

	ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
		@Override
		public void onAddedOrUpdatedFriends(List<String> accounts) {
			reloadWhenDataChanged(accounts, "onAddedOrUpdatedFriends", true);
		}

		@Override
		public void onDeletedFriends(List<String> accounts) {
			reloadWhenDataChanged(accounts, "onDeletedFriends", true);
		}

		@Override
		public void onAddUserToBlackList(List<String> accounts) {
			reloadWhenDataChanged(accounts, "onAddUserToBlackList", true);
		}

		@Override
		public void onRemoveUserFromBlackList(List<String> accounts) {
			reloadWhenDataChanged(accounts, "onRemoveUserFromBlackList", true);
		}
	};

	private UserInfoObserver userInfoObserver = new UserInfoObserver() {
		@Override
		public void onUserInfoChanged(List<String> accounts) {
			reloadWhenDataChanged(accounts, "onUserInfoChanged", true, false); // 非好友资料变更，不用刷新界面
		}
	};

	private Observer<Void> loginSyncCompletedObserver = new Observer<Void>() {
		@Override
		public void onEvent(Void aVoid) {
			getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					reloadWhenDataChanged(null, "onLoginSyncCompleted", false);
				}
			}, 50);
		}
	};

	private void reloadWhenDataChanged(List<String> accounts, String reason, boolean reload) {
		reloadWhenDataChanged(accounts, reason, reload, true);
	}

	private void reloadWhenDataChanged(List<String> accounts, String reason, boolean reload, boolean force) {
		if (accounts == null || accounts.isEmpty()) {
			return;
		}

		boolean needReload = false;
		if (!force) {
			// 非force：与通讯录无关的（非好友）变更通知，去掉
			for (String account : accounts) {
				if (NimUIKit.getContactProvider().isMyFriend(account)) {
					needReload = true;
					break;
				}
			}
		} else {
			needReload = true;
		}

		if (!needReload) {
			Log.d(UIKitLogTag.CONTACT, "no need to reload contact");
			return;
		}

		// log
		StringBuilder sb = new StringBuilder();
		sb.append("ContactFragment received data changed as [" + reason + "] : ");
		if (accounts != null && !accounts.isEmpty()) {
			for (String account : accounts) {
				sb.append(account);
				sb.append(" ");
			}
			sb.append(", changed size=" + accounts.size());
		}
		Log.i(UIKitLogTag.CONTACT, sb.toString());

		// reload
		reload(reload);
	}

	/**
	 * *********************************** 在线状态 *******************************
	 */

	OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
		@Override
		public void onlineStateChange(Set<String> accounts) {
			// 更新
			mZongQuanView.getFriendAdapter().notifyDataSetChanged();
		}
	};

	public void registerOnlineStateChangeListener(boolean register) {
		if (!NimUIKitImpl.enableOnlineState()) {
			return;
		}
		NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
	}

}
