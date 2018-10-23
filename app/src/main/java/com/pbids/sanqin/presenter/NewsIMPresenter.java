package com.pbids.sanqin.presenter;

import android.os.Handler;
import android.text.TextUtils;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.TeamMemberAitHelper;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;
import com.pbids.sanqin.ui.activity.news.NewsIMView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * presenter
 * 模块: 消息
 * Created by liang on 2018/3/12.
 * ---> RecentContactsFragment
 */

public class NewsIMPresenter extends BasePresenter {

	// 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
	public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

	NewsIMView mNewsIMView ;

	List<QinQinChiFriendGroup> sortUserGroupModels = new ArrayList<>();
	QinQinChiFriendGroup recentGroup = new QinQinChiFriendGroup();
	List<QinQinChiFriend> recentFriends = new ArrayList<>();

	// IM
	private static final Handler handler = new Handler();
	private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）
	private boolean msgLoaded = false;
	private List<RecentContact> loadedRecents;
	// data
	private List<RecentContact> items;
	private RecentContactsCallback callback;

	private UserInfoObserver userInfoObserver;

	public NewsIMPresenter(NewsIMView newsView ){
		this.mNewsIMView = newsView;
		recentGroup.setSortUserModels(recentFriends );
		sortUserGroupModels.add(recentGroup);
		items = new ArrayList<>();
		cached = new HashMap<>(3);
	}

	public List<QinQinChiFriendGroup> getFriendGroups (){
		return sortUserGroupModels;
	}

	public QinQinChiFriend getFriend(int groupPosition, int childPosition){
		return sortUserGroupModels.get(groupPosition).getSortUserModels().get(childPosition);
	}

	protected final Handler getHandler() {
		return handler;
	}

	public void intIM(){
//		initCallBack();
		requestMessages(false);
	}

/*
	private void initCallBack() {
		if (callback != null) {
			return;
		}
		callback = new RecentContactsCallback() {
			@Override
			public void onRecentContactsLoaded() {

			}

			@Override
			public void onUnreadCountChange(int unreadCount) {

			}

			@Override
			public void onItemClick(RecentContact recent) {
				if (recent.getSessionType() == SessionTypeEnum.Team) {
					//NimUIKit.startTeamSession(getActivity(), recent.getContactId());
				} else if (recent.getSessionType() == SessionTypeEnum.P2P) {
					//NimUIKit.startP2PSession(getActivity(), recent.getContactId());
				}
			}

			@Override
			public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
				return null;
			}

			@Override
			public String getDigestOfTipMsg(RecentContact recent) {
				return null;
			}
		};
	}
*/


	private void requestMessages(boolean delay) {
		if (msgLoaded) {
			return;
		}
		getHandler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (msgLoaded) {
					return;
				}
				// 查询最近联系人列表数据
				NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

					@Override
					public void onResult(int code, List<RecentContact> recents, Throwable exception) {
						if (code != ResponseCode.RES_SUCCESS || recents == null) {
							return;
						}
						loadedRecents = recents;
						// 初次加载，更新离线的消息中是否有@我的消息
						for (RecentContact loadedRecent : loadedRecents) {
							if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
								updateOfflineContactAited(loadedRecent);
							}
						}
						// 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
						//
						msgLoaded = true;
						//if (isAdded()) {
							onRecentContactsLoaded();
						//}
					}
				});
			}
		}, delay ? 250 : 0);
	}


	private void onRecentContactsLoaded() {
		items.clear();
		if (loadedRecents != null) {
			items.addAll(loadedRecents);
			loadedRecents = null;
		}
		updateToFrendList();
		refreshMessages(true);

		if (callback != null) {
			callback.onRecentContactsLoaded();
		}
	}

	private void refreshMessages(boolean unreadChanged) {
		sortRecentContacts(items);
		//notifyDataSetChanged();
		mNewsIMView.showRecentList();

		if (unreadChanged) {

			// 方式一：累加每个最近联系人的未读（快）

			int unreadNum = 0;
			for (RecentContact r : items) {
				unreadNum += r.getUnreadCount();
			}

			// 方式二：直接从SDK读取（相对慢）
			//int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

			if (callback != null) {
				callback.onUnreadCountChange(unreadNum);
			}

			Badger.updateBadgerCount(unreadNum);
		}
	}

	/**
	 * **************************** 排序 ***********************************
	 */
	private void sortRecentContacts(List<RecentContact> list) {
		if (list.size() == 0) {
			return;
		}
		Collections.sort(list, comp);
	}

	private void updateToFrendList(){
		recentFriends.clear();
		if (items != null) {
			// recents参数即为最近联系人列表（最近会话列表）
			for (int i = 0; i < items.size(); i++) {
				RecentContact recent = items.get(i);
				NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(recent.getContactId());//取好友用户信息
				QinQinChiFriend friend = new QinQinChiFriend();
//				friend.setId(Long.parseLong(recent.getContactId()));
//				friend.setName(user.getName()); //用户名
								friend.setId(123456);
				friend.setName("dddddd"); //用户名
				friend.setRecentContact(recent);
				recentFriends.add(0,friend);
			}
//			mNewsIMView.showRecentList();
		}
	}

	private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

		@Override
		public int compare(RecentContact o1, RecentContact o2) {
			// 先比较置顶tag
			long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
			if (sticky != 0) {
				return sticky > 0 ? -1 : 1;
			} else {
				long time = o1.getTime() - o2.getTime();
				return time == 0 ? 0 : (time > 0 ? -1 : 1);
			}
		}
	};


	/**
	 * ********************** 收消息，处理状态变化 ************************
	 */
	public void registerObservers(boolean register) {
		MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
		service.observeReceiveMessage(messageReceiverObserver, register); // 消息
		service.observeRecentContact(messageObserver, register); // 最近联系人列表变化
		service.observeMsgStatus(statusObserver, register); // 状态
		service.observeRecentContactDeleted(deleteObserver, register); //删除最近联系人

		registerTeamUpdateObserver(register);
		registerTeamMemberUpdateObserver(register);
		//好友数据改变
		NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
		if (register) {
			registerUserInfoObserver();
		} else {
			unregisterUserInfoObserver();
		}
	}

	/**
	 * 注册群信息&群成员更新监听
	 */
	private void registerTeamUpdateObserver(boolean register) {
		NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
	}

	private void registerTeamMemberUpdateObserver(boolean register) {
		NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
	}

	private void registerDropCompletedListener(boolean register) {
		if (register) {
			DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
		} else {
			DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
		}
	}

	// 暂存消息，当RecentContact 监听回来时使用，结束后清掉
	private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

	//监听在线消息中是否有@我
	private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
		@Override
		public void onEvent(List<IMMessage> imMessages) {
			if (imMessages != null) {
				for (IMMessage imMessage : imMessages) {
					if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
						continue;
					}
					Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
					if (cacheMessageSet == null) {
						cacheMessageSet = new HashSet<>();
						cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
					}
					cacheMessageSet.add(imMessage);
				}
			}
		}
	};

	// 处理消息红点
	Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
		@Override
		public void onEvent(List<RecentContact> recentContacts) {
			if (!DropManager.getInstance().isTouchable()) {
				// 正在拖拽红点，缓存数据
				for (RecentContact r : recentContacts) {
					cached.put(r.getContactId(), r);
				}

				return;
			}

			onRecentContactChanged(recentContacts);
		}
	};

	//最近联系人改变
	private void onRecentContactChanged(List<RecentContact> recentContacts) {
		int index;
		for (RecentContact r : recentContacts) {
			index = -1;
			for (int i = 0; i < items.size(); i++) {
				if (r.getContactId().equals(items.get(i).getContactId())
						&& r.getSessionType() == (items.get(i).getSessionType())) {
					index = i;
					break;
				}
			}

			if (index >= 0) {
				items.remove(index);
			}

			items.add(r);
			if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
				TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
			}
		}
		updateToFrendList();
		cacheMessages.clear();
		refreshMessages(true);
	}

	DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
		@Override
		public void onCompleted(Object id, boolean explosive) {
			if (cached != null && !cached.isEmpty()) {
				// 红点爆裂，已经要清除未读，不需要再刷cached
				if (explosive) {
					if (id instanceof RecentContact) {
						RecentContact r = (RecentContact) id;
						cached.remove(r.getContactId());
					} else if (id instanceof String && ((String) id).contentEquals("0")) {
						cached.clear();
					}
				}

				// 刷cached
				if (!cached.isEmpty()) {
					List<RecentContact> recentContacts = new ArrayList<>(cached.size());
					recentContacts.addAll(cached.values());
					cached.clear();
					onRecentContactChanged(recentContacts);
				}
			}
		}
	};

	Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
		@Override
		public void onEvent(IMMessage message) {
			int index = getItemIndex(message.getUuid());
			if (index >= 0 && index < items.size()) {
				RecentContact item = items.get(index);
				item.setMsgStatus(message.getStatus());
				refreshViewHolderByIndex(index);
			}
		}
	};

	Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
		@Override
		public void onEvent(RecentContact recentContact) {
			if (recentContact != null) {
				for (RecentContact item : items) {
					if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
							&& item.getSessionType() == recentContact.getSessionType()) {
						items.remove(item);
						refreshMessages(true);
						break;
					}
				}
			} else {
				items.clear();
				refreshMessages(true);
			}
			updateToFrendList();
		}
	};

	TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {

		@Override
		public void onUpdateTeams(List<Team> teams) {
			//adapter.notifyDataSetChanged();
			mNewsIMView.showRecentList();
		}

		@Override
		public void onRemoveTeam(Team team) {

		}
	};

	TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {
		@Override
		public void onUpdateTeamMember(List<TeamMember> members) {
			//adapter.notifyDataSetChanged();
			mNewsIMView.showRecentList();
		}

		@Override
		public void onRemoveTeamMember(List<TeamMember> member) {

		}
	};

	private int getItemIndex(String uuid) {
		for (int i = 0; i < items.size(); i++) {
			RecentContact item = items.get(i);
			if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
				return i;
			}
		}

		return -1;
	}

	protected void refreshViewHolderByIndex(final int index) {
		mNewsIMView.refreshViewHolderByIndex(index);
		/*mNewsIMView.getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				//adapter.notifyItemChanged(index);
				mNewsIMView.showRecentList();
			}
		});*/
	}

	public void setCallback(RecentContactsCallback callback) {
		this.callback = callback;
	}

	private void registerUserInfoObserver() {
		if (userInfoObserver == null) {
			userInfoObserver = new UserInfoObserver() {
				@Override
				public void onUserInfoChanged(List<String> accounts) {
					refreshMessages(false);
				}
			};
		}
		NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
	}

	private void unregisterUserInfoObserver() {
		if (userInfoObserver != null) {
			NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
		}
	}

	ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
		@Override
		public void onAddedOrUpdatedFriends(List<String> accounts) {
			refreshMessages(false);
		}

		@Override
		public void onDeletedFriends(List<String> accounts) {
			refreshMessages(false);
		}

		@Override
		public void onAddUserToBlackList(List<String> account) {
			refreshMessages(false);
		}

		@Override
		public void onRemoveUserFromBlackList(List<String> account) {
			refreshMessages(false);
		}
	};

	private void updateOfflineContactAited(final RecentContact recentContact) {
		if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
				|| recentContact.getUnreadCount() <= 0) {
			return;
		}

		// 锚点
		List<String> uuid = new ArrayList<>(1);
		uuid.add(recentContact.getRecentMessageId());

		List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);

		if (messages == null || messages.size() < 1) {
			return;
		}
		final IMMessage anchor = messages.get(0);

		// 查未读消息
		NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
				recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

			@Override
			public void onResult(int code, List<IMMessage> result, Throwable exception) {
				if (code == ResponseCode.RES_SUCCESS && result != null) {
					result.add(0, anchor);
					Set<IMMessage> messages = null;
					// 过滤存在的@我的消息
					for (IMMessage msg : result) {
						if (TeamMemberAitHelper.isAitMessage(msg)) {
							if (messages == null) {
								messages = new HashSet<>();
							}
							messages.add(msg);
						}
					}

					// 更新并展示
					if (messages != null) {
						TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
						//notifyDataSetChanged();
						mNewsIMView.showRecentList();
					}
				}
			}
		});

	}

}
