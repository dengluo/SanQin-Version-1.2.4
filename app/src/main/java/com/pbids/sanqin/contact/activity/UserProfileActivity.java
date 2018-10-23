package com.pbids.sanqin.contact.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.GsonBuilder;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.core.model.UserInfoExtends;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.MuteListChangedNotify;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.DemoCache;
import com.pbids.sanqin.R;
import com.pbids.sanqin.contact.constant.UserConstant;
import com.pbids.sanqin.session.SessionHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资料页面
 * Created by huangjun on 2015/8/11.
 */
public class UserProfileActivity extends UI {

    private static final String TAG = UserProfileActivity.class.getSimpleName();

    private final boolean FLAG_ADD_FRIEND_DIRECTLY = false; // 是否直接加为好友开关，false为需要好友申请
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";

    private String account;

    // 基本信息
    private HeadImageView headImageView;
    private TextView nameText;
    private ImageView genderImage;
    private TextView accountText;
    private TextView birthdayText;
    private TextView mobileText;
    private TextView emailText;
    private TextView signatureText;
    private RelativeLayout birthdayLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout signatureLayout;
    private RelativeLayout aliasLayout;
    private TextView nickText;
    //等级名称
	private TextView tvUserLevelname ;
	//vip
	private TextView tvUserVip ;
	//商务合作
	private ImageView userBusinessCooperation ;

    // 开关
    private ViewGroup toggleLayout;
    private Button addFriendBtn;
    private Button removeFriendBtn;
    private Button chatBtn;
    private SwitchButton blackSwitch;
    private SwitchButton noticeSwitch;
    private Map<String, Boolean> toggleStateMap;


    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消全屏   不加这名会出现全屏问题
        getWindow().clearFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.user_profile_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.user_profile;
        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);

        initActionbar();
        

        findViews();
        registerObserver(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserInfo();
        updateToggleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObserver(false);
    }

    private void registerObserver(boolean register) {
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        NIMClient.getService(FriendServiceObserve.class).observeMuteListChangedNotify(muteListChangedNotifyObserver, register);
    }

    Observer<MuteListChangedNotify> muteListChangedNotifyObserver = new Observer<MuteListChangedNotify>() {
        @Override
        public void onEvent(MuteListChangedNotify notify) {
            setToggleBtn(noticeSwitch, !notify.isMute());
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onDeletedFriends(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            updateUserOperatorView();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            updateUserOperatorView();
        }
    };

    private void findViews() {
        headImageView = findView(R.id.user_head_image);
        nameText = findView(R.id.user_name);
        genderImage = findView(R.id.gender_img);
        accountText = findView(R.id.user_account);
        toggleLayout = findView(R.id.toggle_layout);
        addFriendBtn = findView(R.id.add_buddy);
        chatBtn = findView(R.id.begin_chat);
        removeFriendBtn = findView(R.id.remove_buddy);
        birthdayLayout = findView(R.id.birthday);
        nickText = findView(R.id.user_nick);
        birthdayText = (TextView) birthdayLayout.findViewById(R.id.value);
        phoneLayout = findView(R.id.phone);
        mobileText = (TextView) phoneLayout.findViewById(R.id.value);
        emailLayout = findView(R.id.email);
        emailText = (TextView) emailLayout.findViewById(R.id.value);
        signatureLayout = findView(R.id.signature);
        signatureText = (TextView) signatureLayout.findViewById(R.id.value);
        aliasLayout = findView(R.id.alias);
        ((TextView) birthdayLayout.findViewById(R.id.attribute)).setText(R.string.birthday);
        ((TextView) phoneLayout.findViewById(R.id.attribute)).setText(R.string.phone);
        ((TextView) emailLayout.findViewById(R.id.attribute)).setText(R.string.email);
        ((TextView) signatureLayout.findViewById(R.id.attribute)).setText(R.string.signature);
        ((TextView) aliasLayout.findViewById(R.id.attribute)).setText(R.string.alias);

		tvUserLevelname =  findView(R.id.user_levelname);
		tvUserVip =  findView(R.id.user_vip);
		userBusinessCooperation =   findView(R.id.user_business_cooperation);

        addFriendBtn.setOnClickListener(onClickListener);
        chatBtn.setOnClickListener(onClickListener);
        removeFriendBtn.setOnClickListener(onClickListener);
        aliasLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileEditItemActivity.startActivity(UserProfileActivity.this, UserConstant.KEY_ALIAS, account);
            }
        });
    }

    private void initActionbar() {
        //action bar 上的删除按钮
        TextView actionbarRemoveFriendView = findView(R.id.action_bar_right_removefriend_textview);
        if(actionbarRemoveFriendView!=null){
            actionbarRemoveFriendView.setVisibility(View.VISIBLE);
            actionbarRemoveFriendView.setText("删除好友");
            actionbarRemoveFriendView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    onRemoveFriend();
                    //UserProfileSettingActivity.start(UserProfileActivity.this, account);
                }
            });
        }

        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        if (!DemoCache.getAccount().equals(account)) {
            toolbarView.setVisibility(View.GONE);
            return;
        } else {
            toolbarView.setVisibility(View.VISIBLE);
        }
        toolbarView.setText(R.string.edit);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileSettingActivity.start(UserProfileActivity.this, account);
            }
        });


    }

    private void addToggleBtn(boolean black, boolean notice) {
//        blackSwitch = addToggleItemView(KEY_BLACK_LIST, R.string.black_list, black);
        //解决多个消息提醒按钮问题
        if(noticeSwitch==null){
            noticeSwitch = addToggleItemView(KEY_MSG_NOTICE, R.string.msg_notice, notice);
        }
    }

    private void setToggleBtn(SwitchButton btn, boolean isChecked) {
        btn.setCheck(isChecked);
    }

    private void updateUserInfo() {
        if (NimUIKit.getUserInfoProvider().getUserInfo(account) != null) {
            updateUserInfoView();

            return;
        }

        NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {

            @Override
            public void onResult(boolean success, NimUserInfo result, int code) {
                updateUserInfoView();
            }
        });
    }

	//更新扩展信息
	private void updateUserExtend( ) {
		//没有扩展信息不显示
		this.tvUserVip.setVisibility(View.GONE);
		this.userBusinessCooperation.setVisibility(View.GONE);
		this.tvUserLevelname.setVisibility(View.GONE);
		//取用户扩展信息
		NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(account);
		String extInfoStr = user.getExtension();
		if(null!=extInfoStr && !extInfoStr.isEmpty()){
			GsonBuilder gsonBuilder = new GsonBuilder();
			UserInfoExtends userInfoExtends = gsonBuilder.create().fromJson(extInfoStr, UserInfoExtends.class);
			// @liang
			if(userInfoExtends!=null){
				//vip
				if(userInfoExtends.getVip()>0){
					this.tvUserVip.setVisibility(View.VISIBLE);
					this.tvUserVip.setText("VIP"+userInfoExtends.getVip());
				}else{
					this.tvUserVip.setVisibility(View.GONE);
				}
				//商务合作
				if (userInfoExtends.getClanStatus() > 0) {
					this.userBusinessCooperation.setVisibility(View.VISIBLE);
				} else {
					this.userBusinessCooperation.setVisibility(View.GONE);
				}
				//等级
				if(null==userInfoExtends.getLevelName() || userInfoExtends.getLevelName().isEmpty()){
					this.tvUserLevelname.setVisibility(View.GONE);
				}else{
					this.tvUserLevelname.setVisibility(View.VISIBLE);
					this.tvUserLevelname.setText(userInfoExtends.getLevelName());
				}
			}
		}
	}
    private void updateUserInfoView() {
    	// hide
        accountText.setText("帐号：" + account);
		accountText.setVisibility(View.GONE);

        headImageView.loadBuddyAvatar(account);

        if (DemoCache.getAccount().equals(account)) {
            nameText.setText(UserInfoHelper.getUserName(account));
        }

        final NimUserInfo userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo == null) {
            LogUtil.e(TAG, "userInfo is null when updateUserInfoView");
            return;
        }
		//更新用户扩展信息
		updateUserExtend();

        //性别显示
/*        if (userInfo.getGenderEnum() == GenderEnum.MALE) {
            genderImage.setVisibility(View.VISIBLE);
            genderImage.setBackgroundResource(R.drawable.nim_male);
        } else if (userInfo.getGenderEnum() == GenderEnum.FEMALE) {
            genderImage.setVisibility(View.VISIBLE);
            genderImage.setBackgroundResource(R.drawable.nim_female);
        } else {
            genderImage.setVisibility(View.GONE);
        }*/
        genderImage.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(userInfo.getBirthday())) {
            birthdayLayout.setVisibility(View.VISIBLE);
            birthdayText.setText(userInfo.getBirthday());
        } else {
            birthdayLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            phoneLayout.setVisibility(View.VISIBLE);
            mobileText.setText(userInfo.getMobile());
        } else {
            phoneLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getEmail())) {
            emailLayout.setVisibility(View.VISIBLE);
            emailText.setText(userInfo.getEmail());
        } else {
            emailLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(userInfo.getSignature())) {
            signatureLayout.setVisibility(View.VISIBLE);
            signatureText.setText(userInfo.getSignature());
        } else {
            signatureLayout.setVisibility(View.GONE);
        }

    }

    //更新操作按钮
    private void updateUserOperatorView() {
        TextView actionbarRemoveFriendView = findView(R.id.action_bar_right_removefriend_textview);
        chatBtn.setVisibility(View.VISIBLE);
        if (NIMClient.getService(FriendService.class).isMyFriend(account)) {
            //removeFriendBtn.setVisibility(View.VISIBLE); //修改删除按钮
            removeFriendBtn.setVisibility(View.GONE);
            if(actionbarRemoveFriendView!=null){
                actionbarRemoveFriendView.setVisibility(View.VISIBLE);
            }
            addFriendBtn.setVisibility(View.GONE);
            updateAlias(true);
        } else {
            addFriendBtn.setVisibility(View.VISIBLE);
            removeFriendBtn.setVisibility(View.GONE);
            //显示action bar 上的删除按钮
            if(actionbarRemoveFriendView!=null){
                actionbarRemoveFriendView.setVisibility(View.GONE);
            }
            updateAlias(false);
        }
    }

    private void updateToggleView() {
        if (DemoCache.getAccount() != null && !DemoCache.getAccount().equals(account)) {
            boolean black = NIMClient.getService(FriendService.class).isInBlackList(account);
            boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
            if (blackSwitch == null || noticeSwitch == null) {
                addToggleBtn(black, notice);
            } else {
                setToggleBtn(blackSwitch, black);
                setToggleBtn(noticeSwitch, notice);
            }
            Log.i(TAG, "black=" + black + ", notice=" + notice);
            updateUserOperatorView();
        }
    }

    private SwitchButton addToggleItemView(String key, int titleResId, boolean initState) {
        ViewGroup vp = (ViewGroup) getLayoutInflater().inflate(R.layout.nim_user_profile_toggle_item, null);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.isetting_item_height));
        vp.setLayoutParams(vlp);

        TextView titleText = ((TextView) vp.findViewById(R.id.user_profile_title));
        titleText.setText(titleResId);

        SwitchButton switchButton = (SwitchButton) vp.findViewById(R.id.user_profile_toggle);
        switchButton.setCheck(initState);
        switchButton.setOnChangedListener(onChangedListener);
        switchButton.setTag(key);

        toggleLayout.addView(vp);

        if (toggleStateMap == null) {
            toggleStateMap = new HashMap<>();
        }
        toggleStateMap.put(key, initState);
        return switchButton;
    }

    private void updateAlias(boolean isFriend) {
        if (isFriend) {
            aliasLayout.setVisibility(View.VISIBLE);
            aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.VISIBLE);
            String alias = NimUIKit.getContactProvider().getAlias(account);
            String name = UserInfoHelper.getUserName(account);
            if (!TextUtils.isEmpty(alias)) {
                nickText.setVisibility(View.VISIBLE);
                nameText.setText(alias);
                nickText.setText("昵称：" + name);
            } else {
                nickText.setVisibility(View.GONE);
                nameText.setText(name);
            }
            //hibe
			nickText.setVisibility(View.GONE);
        } else {
            aliasLayout.setVisibility(View.GONE);
            aliasLayout.findViewById(R.id.arrow_right).setVisibility(View.GONE);
            nickText.setVisibility(View.GONE);
            nameText.setText(UserInfoHelper.getUserName(account));
        }
    }

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(UserProfileActivity.this)) {
                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                if (key.equals(KEY_BLACK_LIST)) {
                    blackSwitch.setCheck(!checkState);
                } else if (key.equals(KEY_MSG_NOTICE)) {
                    noticeSwitch.setCheck(!checkState);
                }
                return;
            }

            updateStateMap(checkState, key);

            if (key.equals(KEY_BLACK_LIST)) {
                if (checkState) {
                    NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(UserProfileActivity.this, "加入黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "on failed：" + code, Toast.LENGTH_SHORT).show();
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    NIMClient.getService(FriendService.class).removeFromBlackList(account).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            Toast.makeText(UserProfileActivity.this, "移除黑名单成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(int code) {
                            if (code == 408) {
                                Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                            }
                            updateStateMap(!checkState, key);
                            blackSwitch.setCheck(!checkState);
                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                }
            } else if (key.equals(KEY_MSG_NOTICE)) {
                NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
                            Toast.makeText(UserProfileActivity.this, "开启消息提醒成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "关闭消息提醒成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                        updateStateMap(!checkState, key);
                        noticeSwitch.setCheck(!checkState);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
            }
        }
    };

    private void updateStateMap(boolean checkState, String key) {
        if (toggleStateMap.containsKey(key)) {
            toggleStateMap.put(key, checkState);  // update state
            Log.i(TAG, "toggle " + key + "to " + checkState);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == addFriendBtn) {
                if (FLAG_ADD_FRIEND_DIRECTLY) {
                    doAddFriend(null, true);  // 直接加为好友
                } else {
                    onAddFriendByVerify(); // 发起好友验证请求
                }
            } else if (v == removeFriendBtn) {
                onRemoveFriend();
            } else if (v == chatBtn) {
                onChat();
            }
        }
    };

    /**
     * 通过验证方式添加好友
     */
    private void onAddFriendByVerify() {
        final EasyEditDialog requestDialog = new EasyEditDialog(this);
        requestDialog.setEditTextMaxLength(32);
        requestDialog.setTitle(getString(R.string.add_friend_verify_tip));
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                String msg = requestDialog.getEditMessage();
                doAddFriend(msg, false);
            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        requestDialog.show();
    }

    private void doAddFriend(String msg, boolean addDirectly) {
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(account) && account.equals(DemoCache.getAccount())) {
            Toast.makeText(UserProfileActivity.this, "不能加自己为好友", Toast.LENGTH_SHORT).show();
            return;
        }
        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
        DialogMaker.showProgressDialog(this, "", true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        updateUserOperatorView();
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(UserProfileActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });

        Log.i(TAG, "onAddFriendByVerify");
    }

    private void onRemoveFriend() {
        Log.i(TAG, "onRemoveFriend");
        if (!NetworkUtil.isNetAvailable(this)) {
            Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, getString(R.string.remove_friend),
                getString(R.string.remove_friend_tip), true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(UserProfileActivity.this, "", true);
                        NIMClient.getService(FriendService.class).deleteFriend(account).setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                DialogMaker.dismissProgressDialog();
                                Toast.makeText(UserProfileActivity.this, R.string.remove_friend_success, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("del",account);
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                            @Override
                            public void onFailed(int code) {
                                DialogMaker.dismissProgressDialog();
                                if (code == 408) {
                                    Toast.makeText(UserProfileActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserProfileActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onException(Throwable exception) {
                                DialogMaker.dismissProgressDialog();
                            }
                        });
                    }
                });
        if (!isFinishing() && !isDestroyedCompatible()) {
            dialog.show();
        }
    }

    private void onChat() {
        Log.i(TAG, "onChat");
        SessionHelper.startP2PSession(this, account);
        finish();
    }
}
