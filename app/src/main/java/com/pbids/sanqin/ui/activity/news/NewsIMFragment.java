package com.pbids.sanqin.ui.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.recent.UnreadCountProvice;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.SystemMessage;
import com.pbids.sanqin.presenter.NewsIMPresenter;
import com.pbids.sanqin.reminder.ReminderManager;
import com.pbids.sanqin.session.SessionHelper;
import com.pbids.sanqin.session.extension.GuessAttachment;
import com.pbids.sanqin.session.extension.RTSAttachment;
import com.pbids.sanqin.session.extension.RedPacketAttachment;
import com.pbids.sanqin.session.extension.RedPacketOpenedAttachment;
import com.pbids.sanqin.session.extension.SnapChatAttachment;
import com.pbids.sanqin.session.extension.StickerAttachment;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.adapter.NewsFriendsAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.eventbus.SystemMessageHandleEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:02
 * @desscribe 类描述:消息界面
 * @remark 备注:
 * @see
 */
public class NewsIMFragment extends ToolbarFragment implements NewsIMView {

    @Bind(R.id.news_friends_list)
    RecyclerView newsFriendsList;  //

	//系统消息
	@Bind(R.id.news_system_mess_red_bg)
	View newsRedBg;  //
	@Bind(R.id.news_system_messge_messge_number)
	TextView txtNewsMessage;  //

	//公众号消息
	@Bind(R.id.huati_system_mess_red_bg)
	View huatiRedBg;  //
	@Bind(R.id.news_huati_messge_number)
	TextView txtHuatiMessage;  //

    // adapter
	NewsFriendsAdapter newsFriendsAdapter;
	NewsIMPresenter mNewsIMPresenter ;

	public static NewsIMFragment newInstance() {
		NewsIMFragment fragment = new NewsIMFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

    @Override
    public BasePresenter initPresenter() {
        return mNewsIMPresenter = new NewsIMPresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
//		recentContactFragment = (ContactsFragment) view.         findViewById(R.id.recent_contact_fragment);
        initView();
		// eventbux
		EventBus.getDefault().register(this);
        return view;
    }

	@Override
	public void setToolBar(AppToolBar toolBar) {
		toolBar.setCenterTextTitle("消息",_mActivity);
	}

	public void initView() {

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		newsFriendsAdapter = new NewsFriendsAdapter(_mActivity, mNewsIMPresenter.getFriendGroups());
		newsFriendsList.setLayoutManager(linearLayoutManager);
		newsFriendsList.setAdapter(newsFriendsAdapter);
		newsFriendsAdapter.setOnChildClickListener(friendChildClickListener);
		addRecentContactFragment();
		//读取系统未读消息数量
		readSystemMessageNumber();
		//读取系统消息未读的数量
		readNewsTopicNumber() ;
	}

	//读取系统消息数量
	private int readSystemMessageNum(){
		List<SystemMessage> sysLists = SystemMessageManager.query(getContext(),SystemMessageManager.TYPE_SYSTEM,false);
		List<SystemMessage> subLists = SystemMessageManager.query(getContext(),SystemMessageManager.TYPE_TOPIC,false);
		return Math.max(sysLists.size(),subLists.size());
	}

	// 将通讯录列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
	private void addRecentContactFragment() {
		//动态加载
		RecentContactsFragment fragment = new RecentContactsFragment() ;
		fragment.setUnreadCountProvice(new UnreadCountProvice() {
			@Override
			public int getCount() {
				int sysNum = readSystemMessageNum();
				// 查询“添加好友”类型的系统通知未读数总和
				List<SystemMessageType> types = new ArrayList<>();
				types.add(SystemMessageType.AddFriend);
				int unreadNimSysMess = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountByType(types);
				return unreadNimSysMess+sysNum;
			}
		});
		fragment.setContainerId(R.id.recent_contact_fragment);
		FragmentManager manager = _mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(fragment.getContainerId(), fragment);
		transaction.commit();
		fragment.setCallback(new RecentContactsCallback() {
			@Override
			public void onRecentContactsLoaded() {
				// 最近联系人列表加载完毕
			}

			@Override
			public void onUnreadCountChange(int unreadCount) {
				ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
			}

			@Override
			public void onItemClick(RecentContact recent) {
				// 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
				switch (recent.getSessionType()) {
					case P2P:
						SessionHelper.startP2PSession(getActivity(), recent.getContactId());
						break;
					case Team:
						SessionHelper.startTeamSession(getActivity(), recent.getContactId());
						break;
					default:
						break;
				}
			}

			@Override
			public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
				// 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
				// 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
				if (attachment instanceof GuessAttachment) {
					GuessAttachment guess = (GuessAttachment) attachment;
					return guess.getValue().getDesc();
				} else if (attachment instanceof RTSAttachment) {
					return "[白板]";
				} else if (attachment instanceof StickerAttachment) {
					return "[贴图]";
				} else if (attachment instanceof SnapChatAttachment) {
					return "[阅后即焚]";
				} else if (attachment instanceof RedPacketAttachment) {
					return "[红包]";
				} else if (attachment instanceof RedPacketOpenedAttachment) {
					return ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId());
				}

				return null;
			}

			@Override
			public String getDigestOfTipMsg(RecentContact recent) {
				String msgId = recent.getRecentMessageId();
				List<String> uuids = new ArrayList<>(1);
				uuids.add(msgId);
				List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
				if (msgs != null && !msgs.isEmpty()) {
					IMMessage msg = msgs.get(0);
					Map<String, Object> content = msg.getRemoteExtension();
					if (content != null && !content.isEmpty()) {
						return (String) content.get("content");
					}
				}

				return null;
			}
		});
	}

    //点击好友列表 打开聊天窗口
    private GroupedRecyclerViewAdapter.OnChildClickListener friendChildClickListener = new GroupedRecyclerViewAdapter.OnChildClickListener(){
		@Override
		public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
			QinQinChiFriend friend = mNewsIMPresenter.getFriend(groupPosition,childPosition) ;
			RecentContact recent = friend.getRecentContact();
			if (recent.getSessionType() == SessionTypeEnum.Team) {
				NimUIKit.startTeamSession(getActivity(), recent.getContactId()); // 群聊
			} else if (recent.getSessionType() == SessionTypeEnum.P2P) {
				NimUIKit.startP2PSession(getActivity(), recent.getContactId()); // 点对点
			}
		}
	};

	// get adapter
    @Override
	public NewsFriendsAdapter getNewsFriendsAdapter (){
    	return newsFriendsAdapter;
	}
	//更新显示列表
	@Override
	public void showRecentList (){
		newsFriendsAdapter.notifyDataSetChanged();
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
		EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        mNewsIMPresenter.registerObservers(false);
        super.onDestroy();
    }

    @OnClick({R.id.news_system_messge_layout, R.id.news_huati_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_system_messge_layout:
            	//点击系统消息
                ((MainFragment) getParentFragment()).start(NewsSystemMassageFragment.newInstance());
                break;
            case R.id.news_huati_layout:
            	//点击公众话题订阅
                ((MainFragment) getParentFragment()).start(NewsTopicListFragment.newInstance());
                break;
        }
    }

	@Override
	public void onHttpSuccess(String type) {

	}

	@Override
	public void onHttpError(String type) {

	}
	public void refreshViewHolderByIndex(final int index) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				//adapter.notifyItemChanged(index);
				showRecentList();
			}
		});
	}

	//读取系统消息未读的数量
	private void readSystemMessageNumber(){
		List<SystemMessage> messLists = SystemMessageManager.query(getContext(),SystemMessageManager.TYPE_SYSTEM,false);
		int num = messLists.size();
		if(num<1){
			newsRedBg.setVisibility(View.GONE);
			txtNewsMessage.setVisibility(View.GONE);
		}else{
			newsRedBg.setVisibility(View.VISIBLE);
			txtNewsMessage.setVisibility(View.VISIBLE);
			txtNewsMessage.setText(num+"");
		}
	}

	//读取公众号未读的数量
	private void readNewsTopicNumber(){
		List<SystemMessage> messLists = SystemMessageManager.query(getContext(),SystemMessageManager.TYPE_TOPIC,false);
		int num = messLists.size();
		if(num<1){
			huatiRedBg.setVisibility(View.GONE);
			txtHuatiMessage.setVisibility(View.GONE);
		}else{
			huatiRedBg.setVisibility(View.VISIBLE);
			txtHuatiMessage.setVisibility(View.VISIBLE);
			txtHuatiMessage.setText(num+"");
		}
	}

	//事件接收
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onClickNotificationEvent(SystemMessageHandleEvent mssageHandle){
		if(mssageHandle.getType()==SystemMessageManager.TYPE_SYSTEM){
			readSystemMessageNumber();
			//系统消息变动
		}else if(mssageHandle.getType()==SystemMessageManager.TYPE_TOPIC){
			//公众号订阅变动
			readNewsTopicNumber();
		}
	}

}
