package com.pbids.sanqin.ui.activity.zongquan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactsCustomization;
import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.business.contact.ContactsFragment;
import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.viewholder.AbsContactViewHolder;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.CommonGroup;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.pbids.sanqin.DemoCache;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.config.preference.UserPreferences;
import com.pbids.sanqin.contact.activity.BlackListActivity;
import com.pbids.sanqin.helper.MessageHelper;
import com.pbids.sanqin.helper.SystemMessageUnreadManager;
import com.pbids.sanqin.model.db.FriendGroupManager;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.reminder.ReminderId;
import com.pbids.sanqin.reminder.ReminderItem;
import com.pbids.sanqin.reminder.ReminderManager;
import com.pbids.sanqin.session.SessionHelper;
import com.pbids.sanqin.team.activity.AdvancedTeamSearchActivity;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.SettingsActivity;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 集成通讯录列表
 * <p/>
 * Created by huangjun on 2015/9/7.
 */
public class ContactListFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

	public static final int REQUEST_CODE_NORMAL = 1;
	public static final int REQUEST_CODE_ADVANCED = 2;
    public static final int FRIEND_GROUP_START_CODE = 27104 ;
    public static final int CREATE_GROUP_REQUEST_CODE = 27105 ;

    public static final int ACT_DEL = 86; // 删除操作
    public static final int ACT_UPDATE = 87; //更新操作
    public static final int ACT_CREATE = 88; //创建操作

    private ContactsFragment contactsFragment ;

    //分组数据
	FriendGroupManager groupManager = new FriendGroupManager();
	List<FriendGroupDb> friendGroupList ;

    //检查名称 不能是公共的
    public static Boolean isCommonGroupName(String gname) {
        if (CommonGroup.GROUP_SANQIN.equals(gname) || CommonGroup.GROUP_JIAZU.equals(gname) || CommonGroup.GROUP_ZONGQIN.equals(gname)) {
            return true;
        }
        return false;
    }


	/**
	 * ******************************** 功能项定制 ***********************************
	 */
	final static class FuncItem extends AbsContactItem {

        static final FuncItem VERIFY = new FuncItem("验证提醒");
        static final FuncItem ROBOT = new FuncItem("智能机器人");
//        static final FuncItem NORMAL_TEAM = new FuncItem("讨论组");
//        static final FuncItem ADVANCED_TEAM = new FuncItem("高级群");
        static final FuncItem BLACK_LIST = new FuncItem("黑名单");
        static final FuncItem MY_COMPUTER = new FuncItem("我的电脑");
        static final FuncItem CONTACT_BRICK = new FuncItem("烧砖");//烧砖

        private String funcName ;
        private int funType;
        public FuncItem(String funcName){
            this.funcName = funcName;
            this.funType = ItemTypes.FUNC;
        }

        public FuncItem(String funcName,int type){
            this.funcName = funcName;
            this.funType = type;
        }
        public boolean testFunName(String gname){
            return funcName.equals(gname);
        }

        public String getFuncName() {
            return funcName;
        }

        @Override
		public int getItemType() {
			return ItemTypes.FUNC;
		}

		@Override
		public String belongsGroup() {
			return null;
		}

        public int getFunType() {
            return funType;
        }

        public static final class FuncViewHolder extends AbsContactViewHolder<FuncItem> {
			private ImageView image;
			private TextView tvFuncName;
			private TextView unreadNum;

			@Override
			public View inflate(LayoutInflater inflater) {
				View view = inflater.inflate(R.layout.func_contacts_item, null);
				this.image = (ImageView) view.findViewById(R.id.img_head);
				this.image.setVisibility(View.GONE);//hide
				this.tvFuncName = (TextView) view.findViewById(R.id.tv_func_name);
				this.unreadNum = (TextView) view.findViewById(R.id.tab_new_msg_label);
				return view;
			}

			@Override
			public void refresh(ContactDataAdapter contactAdapter, int position, FuncItem item) {
                tvFuncName.setText(item.getFuncName());
                tvFuncName.setGravity(Gravity.LEFT);
				if (item == VERIFY) {
					//tvFuncName.setText("验证提醒");
					image.setImageResource(R.drawable.icon_verify_remind);
					image.setScaleType(ScaleType.FIT_XY);
					int unreadCount = SystemMessageUnreadManager.getInstance().getSysMsgUnreadCount();
					updateUnreadNum(unreadCount);

					ReminderManager.getInstance().registerUnreadNumChangedCallback(new ReminderManager.UnreadNumChangedCallback() {
						@Override
						public void onUnreadNumChanged(ReminderItem item) {
							if (item.getId() != ReminderId.CONTACT) {
								return;
							}
							updateUnreadNum(item.getUnread());
						}
					});
				} else if (item == ROBOT) {
					//tvFuncName.setText("智能机器人");
					image.setImageResource(R.drawable.ic_robot);
				}
//				else if (item == NORMAL_TEAM) {
//					//tvFuncName.setText("讨论组");
//					image.setImageResource(R.drawable.ic_secretary);
//				}
//				else if (item == ADVANCED_TEAM) {
//					//tvFuncName.setText("高级群");
//					image.setImageResource(R.drawable.ic_advanced_team);
//				}
				else if (item == BLACK_LIST) {
					//tvFuncName.setText("黑名单");
					image.setImageResource(R.drawable.ic_black_list);
				} else if (item == MY_COMPUTER) {
					//tvFuncName.setText("我的电脑");
					image.setImageResource(R.drawable.ic_my_computer);
				} else if(item == CONTACT_BRICK ){
					//tvFuncName.setText("烧砖");
					image.setImageResource(R.drawable.ic_brick);
                } else if (item.getFunType() == ItemTypes.FRIEND_GROUP) {
                    //分组
                    image.setImageResource(R.drawable.ic_secretary);

                } else if (item.getFunType() == ItemTypes.FRIEND_GROUP_ADD) {
                    //添加分组
                    image.setImageResource(R.drawable.ic_secretary);
                    tvFuncName.setGravity(Gravity.CENTER);
                } else if (item.getFunType() == ItemTypes.FRIEND_GROUP_CUSTOM) {
                    //自定义分组
                    image.setImageResource(R.drawable.ic_secretary);

                }

				if (item != VERIFY) {
					image.setScaleType(ScaleType.FIT_XY);
					unreadNum.setVisibility(View.GONE);
				}
			}

			private void updateUnreadNum(int unreadCount) {
				// 2.*版本viewholder复用问题
				if (unreadCount > 0 && tvFuncName.getText().toString().equals("验证提醒")) {
					unreadNum.setVisibility(View.VISIBLE);
					unreadNum.setText("" + unreadCount);
				} else {
					unreadNum.setVisibility(View.GONE);
				}
			}
		}

		static List<AbsContactItem> provide() {
			List<AbsContactItem> items = new ArrayList<AbsContactItem>();
			items.add(VERIFY);
//			items.insert(ROBOT);
//			items.add(NORMAL_TEAM);
//			items.add(ADVANCED_TEAM);
//			items.insert(BLACK_LIST);
//			items.insert(MY_COMPUTER);
//			items.insert(CONTACT_BRICK);烧砖

            items.add(new FuncItem(CommonGroup.GROUP_SANQIN,ItemTypes.FRIEND_GROUP));
            items.add(new FuncItem(CommonGroup.GROUP_JIAZU,ItemTypes.FRIEND_GROUP));
            items.add(new FuncItem(CommonGroup.GROUP_ZONGQIN,ItemTypes.FRIEND_GROUP));


			return items;
		}

		/*static void handle(Context context, AbsContactItem item) {
			if (item == VERIFY) {
				SystemMessageActivity.start(context);
			} else if (item == ROBOT) {
				RobotListActivity.start(context);
			} else if (item == NORMAL_TEAM) {
			    //讨论组
				TeamListActivity.start(context, ItemTypes.TEAMS.NORMAL_TEAM);
			} else if (item == ADVANCED_TEAM) {
				TeamListActivity.start(context, ItemTypes.TEAMS.ADVANCED_TEAM);
			} else if (item == MY_COMPUTER) {
				SessionHelper.startP2PSession(context, DemoCache.getAccount());
			} else if (item == BLACK_LIST) {
				BlackListActivity.start(context);
			} else if (item == CONTACT_BRICK) {
				//烧砖
//				context.startFragment(BrickFragment.newInstance());
			}
		}*/
	}


	// instance
	public static ContactListFragment newInstance() {
		ContactListFragment fragment = new ContactListFragment();
		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@Override
	public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.contacts_list, container, false);
		ButterKnife.bind(this, view);
		initView();
		return view;
	}

	private void initView() {

//		 LoginPagePresenter.doLogin(); //test
		//
		// 等待同步数据完成
		boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
			@Override
			public void onEvent(Void v) {
				syncPushNoDisturb(UserPreferences.getStatusConfig());
				DialogMaker.dismissProgressDialog();
			}
		});
		if (!syncCompleted) {
			DialogMaker.showProgressDialog(_mActivity, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
		} else {
			syncPushNoDisturb(UserPreferences.getStatusConfig());
		}

        //registerSystemObserver(true);
	}
//    private void registerSystemObserver(boolean register) {
//        NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(systemMessageObserver, register);
//    }

    Observer<SystemMessage> systemMessageObserver = new Observer<SystemMessage>() {
        @Override
        public void onEvent(SystemMessage systemMessage) {
            //onIncomingMessage(systemMessage);
            //Log.v("cgl","ddd");
            if(systemMessage.getType()== SystemMessageType.AddFriend){
                //好友验证提示
                Toast.makeText(getContext(),MessageHelper. getVerifyNotificationText(systemMessage),Toast.LENGTH_LONG);
            }
        }
    };
	/**
	 * 若增加第三方推送免打扰（V3.2.0新增功能），则：
	 * 1.添加下面逻辑使得 push 免打扰与先前的设置同步。
	 * 2.设置界面  以及
	 * 免打扰设置界面 也应添加 push 免打扰的逻辑
	 * <p>
	 * 注意：isPushDndValid 返回 false， 表示未设置过push 免打扰。
	 */
	private void syncPushNoDisturb(StatusBarNotificationConfig staConfig) {
		//ToDo IllegalStateException
		try {
			boolean isNoDisbConfigExist = NIMClient.getService(MixPushService.class).isPushNoDisturbConfigExist();

			if (!isNoDisbConfigExist && staConfig.downTimeToggle) {
				NIMClient.getService(MixPushService.class).setPushNoDisturbConfig(staConfig.downTimeToggle,
						staConfig.downTimeBegin, staConfig.downTimeEnd);
			}
			addContactFragment();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
        //registerSystemObserver(true);
	}
	@Override
	public void setToolBar(AppToolBar toolBar) {
		toolBar.setOnToolBarClickLisenear(this);
		//toolBar.setIMContactToolbar("联系人",_mActivity);
		toolBar.setLeftImageCenterTitleRightImage(_mActivity ,R.drawable.kiss_button_yaoyiyao_default,"联系人",R.drawable.home_button_add_default);
//		toolBar.setLeftImageCenterViewTitleRightImage(_mActivity);
	}

	public void showPopFormBottom(View view) {
		Bundle bundle = new Bundle();
		PopIMContactMenu popSetting = new PopIMContactMenu(_mActivity, new IMContactsMenuCb() {
			@Override
			public void settingCb(Bundle bundle) {
				int ctrl = bundle.getInt("ctrl");
				if(ctrl==102){
					int type = bundle.getInt("type");
					switch (type){
						case 1:
							//添加好友
//							AddFriendActivity.start(_mActivity);
							((MainFragment) getParentFragment()).start(ZQAddFriendFragment.newInstance());
							break;
						case 2:
							//创建讨论组 create_normal_team
							ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(null, 50);
							NimUIKit.startContactSelector(_mActivity, option, REQUEST_CODE_NORMAL);
							break;
						case 3:
							//创建高级群  //create_regular_team
							ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
							NimUIKit.startContactSelector(_mActivity, advancedOption, REQUEST_CODE_ADVANCED);
							break;
						case 4:
							//搜索高级群 //search_advanced_team
							AdvancedTeamSearchActivity.start(_mActivity);
							break;
						case 5:
							//设置
							startActivity(new Intent(_mActivity, SettingsActivity.class));
							break;
					}
				}

			}
		}, bundle);
		//设置Popupwindow显示位置（从底部弹出）
/*		popSetting.showAtLocation(findViewById(R.id.mlight_main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		WindowManager.LayoutParams params = getWindow().getAttributes();*/
		popSetting.showAtLocation( view , Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
		WindowManager.LayoutParams params = _mActivity.getWindow().getAttributes();

		//当弹出Popupwindow时，背景变半透明
		//params.alpha=0.7f;
		_mActivity.getWindow().setAttributes(params);
		//设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                params = getWindow().getAttributes();
//                params.alpha=1f;
//                getWindow().setAttributes(params);
//            }
//        });
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popSetting.setBackgroundDrawable(getResources().getDrawable(R.color.black_mask));
		// 设置好参数之后再show
		popSetting.showAsDropDown(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_im_contacts_menu:
				// 显示菜单
				showPopFormBottom(v);
				break;
			case R.id.hp_search:
				// 查找好友
				//((MainFragment) getParentFragment()).start(NewsSearchFragment.newInstandce());
				break;
			case R.id.main_right_layout:
				// 显示菜单
				showPopFormBottom(v);
				// 添加好友
				//((MainFragment) getParentFragment()).start(ZQAddFriendFragment.newInstance());
				break;
			case R.id.main_left_layout:
				//摇一摇
				((MainFragment) getParentFragment()).start(ShakeFragment.newInstance());
				break;
		}
	}


	//好友分组
	private void showFriendGroup(String gname,int ctrl,int requestCode){
        ZQMemberManagerFragment friendGroupFragment = ZQMemberManagerFragment.newInstance();
        friendGroupFragment.getArguments().putInt("ctrl",ctrl);
        friendGroupFragment.getArguments().putString("group",gname);
        ((MainFragment) getParentFragment()).startForResult(friendGroupFragment,requestCode);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        switch (requestCode){
            case FRIEND_GROUP_START_CODE :
            case CREATE_GROUP_REQUEST_CODE :
                //创建组
                if(resultCode==RESULT_OK){
                    //成功
                }
                if(data!=null){
                    int act = data.getInt("act");
                    if(act>0){
                        String groupName = data.getString("groupId",null);
                        if(groupName!=null){
                            //这里提交服务更新
                        }

                    }
                    //删除或添加组时更新列表
                    if (act == ACT_CREATE || act == ACT_DEL) {
                        //重新加载数据
                        contactsFragment.reload(true);
                    }
                }
                break;
        }

    }



    // 将通讯录列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
	private void addContactFragment() {
		//动态加载
        contactsFragment = new ContactsFragment();
		contactsFragment.setContainerId(R.id.contact_fragment);
		FragmentManager manager = _mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(contactsFragment.getContainerId(), contactsFragment);
		transaction.commit();
		// 功能项定制
		contactsFragment.setContactsCustomization(new ContactsCustomization() {
			@Override
			public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
				return FuncItem.FuncViewHolder.class;
			}

			@Override
			public List<AbsContactItem> onGetFuncItems() {
                List<AbsContactItem> contactItems = FuncItem.provide();
                //加载自定义组
                friendGroupList = groupManager.queryByType(ItemTypes.FRIEND_GROUP_CUSTOM);
                for (FriendGroupDb item: friendGroupList){
                    contactItems.add(new FuncItem(item.getGroupName(),ItemTypes.FRIEND_GROUP_CUSTOM));
                }
                //添加分组按钮
                contactItems.add(new FuncItem("+ 添加分组",ItemTypes.FRIEND_GROUP_ADD));
                return contactItems;
			}

			@Override
			public void onFuncItemClick(AbsContactItem item) {
				if (item == FuncItem.VERIFY) {
					//系统消息
					SystemMessageActivity.start(getActivity());
				} else if (item == FuncItem.ROBOT) {
					RobotListActivity.start(getActivity());
				}
//				else if (item == FuncItem.NORMAL_TEAM) {
//                    //讨论组
//					TeamListActivity.start(getActivity(), ItemTypes.TEAMS.NORMAL_TEAM);
//				} else if (item == FuncItem.ADVANCED_TEAM) {
//					TeamListActivity.start(getActivity(), ItemTypes.TEAMS.ADVANCED_TEAM);
//				}
				else if (item == FuncItem.MY_COMPUTER) {
					SessionHelper.startP2PSession(getActivity(), DemoCache.getAccount());
				} else if (item == FuncItem.BLACK_LIST) {
					BlackListActivity.start(getActivity());
				} else if (item == FuncItem.CONTACT_BRICK) {
					//烧砖
					((MainFragment) getParentFragment()).start(BrickFragment.newInstance());
				} else {
                    FuncItem fitem = (FuncItem) item;
                    if(item!=null) {
                        if (fitem.getFunType() == ItemTypes.FRIEND_GROUP) {
                            //固定分组
                            String groupName = fitem.getFuncName();
                            //按名称查找
							FriendGroupDb friendGroupDb = groupManager.queryByGroupName(groupName);
							if (friendGroupDb == null) {
								//如果为空就创建一个
								friendGroupDb = new FriendGroupDb();
								friendGroupDb.setGroupName(groupName);
								friendGroupDb.setType(ItemTypes.FRIEND_GROUP);
								friendGroupDb.setArea("");
								friendGroupDb.setRemarks("");
								friendGroupDb.setPid("");
								friendGroupDb.setGroupId(MyApplication.getUserInfo().getUserId() + "_" + RandomUtil.randomString(8));
								groupManager.insert(friendGroupDb);
							}
                            showFriendGroup(friendGroupDb.getGroupId(), ZQMemberManagerFragment.GROUP_SHOW,FRIEND_GROUP_START_CODE);
                        } else if (fitem.getFunType() == ItemTypes.FRIEND_GROUP_ADD) {
                        	//检查自定义分组数量不超过8个
							if(FriendGroupManager.newInstance().queryByType(ItemTypes.FRIEND_GROUP_CUSTOM).size()>7){
								Toast.makeText(_mActivity,"自定分组最多只能添加8个分组",Toast.LENGTH_LONG).show();
							}else{
								//添加分组
								showFriendGroup("创建分组", ZQMemberManagerFragment.GROUP_EDIT,CREATE_GROUP_REQUEST_CODE);
							}

                        } else if (fitem.getFunType() == ItemTypes.FRIEND_GROUP_CUSTOM) {
                            //自定义分组
                            String groupName = fitem.getFuncName();
                            //按名称查找
                            FriendGroupDb friendGroupDb = groupManager.queryByGroupName(groupName);
                            if (friendGroupDb == null) {
                                toast("未知错误 code:6590");
                            } else {
                                showFriendGroup(friendGroupDb.getGroupId(), ZQMemberManagerFragment.GROUP_SHOW,FRIEND_GROUP_START_CODE);
                            }
                        }
                    }
                }



				/*else if (item == FuncItem.GROUP_SANQIN) {
                    showFriendGroup(FuncItem.GROUP_SANQIN.getFuncName());
                } else if (item == FuncItem.GROUP_JIAZU) {
                    showFriendGroup(FuncItem.GROUP_JIAZU.getFuncName());
                } else if (item == FuncItem.GROUP_ZONGQIN) {
                    showFriendGroup(FuncItem.GROUP_ZONGQIN.getFuncName());
                } else if (item == FuncItem.GROUP_ZONGQIN) {
                    showFriendGroup(FuncItem.GROUP_ZONGQIN.getFuncName());
                }*/
                //添加分组
                //((MainFragment) getParentFragment()).start(ZQAddGroupFragment.newInstance());
			}
		});
	}

}
