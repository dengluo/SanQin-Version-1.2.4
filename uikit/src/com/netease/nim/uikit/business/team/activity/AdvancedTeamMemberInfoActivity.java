package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.MenuDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.custom.NimEditTipsDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群成员详细信息界面
 * Created by hzxuwen on 2015/3/19.
 */
public class AdvancedTeamMemberInfoActivity extends UI implements View.OnClickListener {

    private static final String TAG = AdvancedTeamMemberInfoActivity.class.getSimpleName();
    // constant
    public static final int REQ_CODE_REMOVE_MEMBER = 11;
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TID = "EXTRA_TID";
    public static final String EXTRA_ISADMIN = "EXTRA_ISADMIN";
    public static final String EXTRA_ISREMOVE = "EXTRA_ISREMOVE";
    private final String KEY_MUTE_MSG = "mute_msg";

    // data
    private String account;
    private String teamId;
    private TeamMember viewMember;
    private boolean isSetAdmin;
    private Map<String, Boolean> toggleStateMap;

    // view
    private HeadImageView headImageView;
    private TextView memberName;
    private TextView nickName;
    private TextView identity;
    private View nickContainer;
    private Button removeBtn;
    private View identityContainer;
    private MenuDialog setAdminDialog;
    private MenuDialog cancelAdminDialog;
    private ViewGroup toggleLayout;
    private SwitchButton muteSwitch;

    private AlertView teamSystemTipDialog;

    private NimEditTipsDialog nimEditTipsDialog;

    // state
    private boolean isSelfCreator = false;
    private boolean isSelfManager = false;

    public static void startActivityForResult(Activity activity, String account, String tid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, account);
        intent.putExtra(EXTRA_TID, tid);
        intent.setClass(activity, AdvancedTeamMemberInfoActivity.class);
        activity.startActivityForResult(intent, REQ_CODE_REMOVE_MEMBER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_member_info_layout);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.team_card;
        setToolBar(R.id.toolbar, options);

        parseIntentData();

        findViews();

        loadMemberInfo();

        initMemberInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateToggleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (setAdminDialog != null) {
            setAdminDialog.dismiss();
        }
        if (cancelAdminDialog != null) {
            cancelAdminDialog.dismiss();
        }
    }

    private void parseIntentData() {
        account = getIntent().getStringExtra(EXTRA_ID);
        teamId = getIntent().getStringExtra(EXTRA_TID);
    }

    private void findViews() {
        nickContainer = findViewById(R.id.nickname_container);
        identityContainer = findViewById(R.id.identity_container);
        headImageView = (HeadImageView) findViewById(R.id.team_member_head_view);
        memberName = (TextView) findViewById(R.id.team_member_name);
        nickName = (TextView) findViewById(R.id.team_nickname_detail);
        identity = (TextView) findViewById(R.id.team_member_identity_detail);
        removeBtn = (Button) findViewById(R.id.team_remove_member);
        toggleLayout = findView(R.id.toggle_layout);
        setClickListener();
    }

    private void setClickListener() {
        nickContainer.setOnClickListener(this);
        identityContainer.setOnClickListener(this);
        removeBtn.setOnClickListener(this);
    }

    private void updateToggleView() {
        if (getMyPermission()) {
            boolean isMute = NimUIKit.getTeamProvider().getTeamMember(teamId, account).isMute();
            if (muteSwitch == null) {
                addToggleBtn(isMute);
            } else {
                setToggleBtn(muteSwitch, isMute);
            }
            Log.i(TAG, "mute=" + isMute);
        }

    }

    // 判断是否有权限
    private boolean getMyPermission() {
        if (isSelfCreator && !isSelf(account)) {
            return true;
        }
        if (isSelfManager && identity.getText().toString().equals(getString(R.string.team_member))) {
            return true;
        }
        return false;
    }

    private void addToggleBtn(boolean isMute) {
        muteSwitch = addToggleItemView(KEY_MUTE_MSG, R.string.mute_msg, isMute);
    }

    private void setToggleBtn(SwitchButton btn, boolean isChecked) {
        btn.setCheck(isChecked);
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

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(AdvancedTeamMemberInfoActivity.this)) {
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                if (key.equals(KEY_MUTE_MSG)) {
                    muteSwitch.setCheck(!checkState);
                }
                return;
            }

            updateStateMap(checkState, key);

            if (key.equals(KEY_MUTE_MSG)) {
                NIMClient.getService(TeamService.class).muteTeamMember(teamId, account, checkState).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
                            Toast.makeText(AdvancedTeamMemberInfoActivity.this, "群禁言成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdvancedTeamMemberInfoActivity.this, "取消群禁言成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 408) {
                            Toast.makeText(AdvancedTeamMemberInfoActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdvancedTeamMemberInfoActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                        updateStateMap(!checkState, key);
                        muteSwitch.setCheck(!checkState);
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

    private void loadMemberInfo() {
        viewMember = NimUIKit.getTeamProvider().getTeamMember(teamId, account);
        if (viewMember != null) {
            updateMemberInfo();
        } else {
            requestMemberInfo();
        }
    }

    /**
     * 查询群成员的信息
     */
    private void requestMemberInfo() {
        NimUIKit.getTeamProvider().fetchTeamMember(teamId, account, new SimpleCallback<TeamMember>() {
            @Override
            public void onResult(boolean success, TeamMember member, int code) {
                if (success && member != null) {
                    viewMember = member;
                    updateMemberInfo();
                }
            }
        });
    }

    private void initMemberInfo() {
        memberName.setText(UserInfoHelper.getUserDisplayName(account));
        headImageView.loadBuddyAvatar(account);
    }

    private void updateMemberInfo() {
        updateMemberIdentity();
        updateMemberNickname();
        updateSelfIndentity();
        updateRemoveBtn();
    }

    /**
     * 更新群成员的身份
     */
    private void updateMemberIdentity() {
        if (viewMember.getType() == TeamMemberType.Manager) {
            identity.setText(R.string.team_admin);
            isSetAdmin = true;
        } else {
            isSetAdmin = false;
            if (viewMember.getType() == TeamMemberType.Owner) {
                identity.setText(R.string.team_creator);
            } else {
                identity.setText(R.string.team_member);
            }
        }
    }

    /**
     * 更新成员群昵称
     */
    private void updateMemberNickname() {
        nickName.setText(viewMember.getTeamNick() != null ? viewMember.getTeamNick() : getString(R.string.team_nickname_none));
    }

    /**
     * 获得用户自己的身份
     */
    private void updateSelfIndentity() {
        TeamMember selfTeamMember = NimUIKit.getTeamProvider().getTeamMember(teamId, NimUIKit.getAccount());
        if (selfTeamMember == null) {
            return;
        }
        if (selfTeamMember.getType() == TeamMemberType.Manager) {
            isSelfManager = true;
        } else if (selfTeamMember.getType() == TeamMemberType.Owner) {
            isSelfCreator = true;
        }
    }

    /**
     * 更新是否显移除本群按钮
     */
    private void updateRemoveBtn() {
        if (viewMember.getAccount().equals(NimUIKit.getAccount())) {
            removeBtn.setVisibility(View.GONE);
        } else {
            if (isSelfCreator) {
                removeBtn.setVisibility(View.VISIBLE);
            } else if (isSelfManager) {
                if (viewMember.getType() == TeamMemberType.Owner) {
                    removeBtn.setVisibility(View.GONE);
                } else if (viewMember.getType() == TeamMemberType.Normal) {
                    removeBtn.setVisibility(View.VISIBLE);
                } else {
                    removeBtn.setVisibility(View.GONE);
                }
            } else {
                removeBtn.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 更新群昵称
     *
     * @param name
     */
    private void setNickname(final String name) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty), true);
        NIMClient.getService(TeamService.class).updateMemberNick(teamId, account, name).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                nickName.setText(name != null ? name : getString(R.string.team_nickname_none));
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, String.format(getString(R.string.update_failed), code),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nickname_container) {
            editNickname();

        } else if (i == R.id.identity_container) {
            showManagerButton();

        } else if (i == R.id.team_remove_member) {
            showConfirmButton();
        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AdvancedTeamNicknameActivity.REQ_CODE_TEAM_NAME && resultCode == Activity.RESULT_OK) {
            final String teamName = data.getStringExtra(AdvancedTeamNicknameActivity.EXTRA_NAME);
            setNickname(teamName);
        }
    }

    /**
     * 设置群昵称
     */
    private void editNickname() {
        if (isSelfCreator || isSelf(account)) {
            AdvancedTeamNicknameActivity.start(AdvancedTeamMemberInfoActivity.this, nickName.getText().toString());
        } else if (isSelfManager && identity.getText().toString().equals(getString(R.string.team_member))) {
            AdvancedTeamNicknameActivity.start(AdvancedTeamMemberInfoActivity.this, nickName.getText().toString());
        } else {
            Toast.makeText(this, R.string.no_permission, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 显示设置管理员按钮
     */
    private void showManagerButton() {
        if (identity.getText().toString().equals(getString(R.string.team_creator))) {
            return;
        }
        if (!isSelfCreator)
            return;

        if (identity.getText().toString().equals(getString(R.string.team_member))) {
            switchManagerButton(true);
        } else {
            switchManagerButton(false);
        }
    }

    /**
     * 转换设置或取消管理员按钮
     *
     * @param isSet 是否设置
     */
    private void switchManagerButton(boolean isSet) {
        if (isSet) {
            teamSystemTipDialog = new AlertView("设置个人身份", null, "取消", null, new String[]{"设为管理员"}, AdvancedTeamMemberInfoActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if(position==-1){
                        teamSystemTipDialog.dismiss();
                    }else if (position==0){
                        addManagers();
                    }

                }
            });
            teamSystemTipDialog.show();
        } else {
//            if (cancelAdminDialog == null) {
//                List<String> btnNames = new ArrayList<>();
//                btnNames.add(getString(R.string.cancel_team_admin));
//                cancelAdminDialog = new MenuDialog(this, btnNames, new MenuDialog.MenuDialogOnButtonClickListener() {
//                    @Override
//                    public void onButtonClick(String name) {
//                        removeManagers();
//                        cancelAdminDialog.dismiss();
//                    }
//                });
//            }
//            cancelAdminDialog.show();
            teamSystemTipDialog = new AlertView("设置个人身份", null, "取消", null, new String[]{"取消管理员"}, AdvancedTeamMemberInfoActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if(position==-1){
                        teamSystemTipDialog.dismiss();
                    }else if (position==0){
                        removeManagers();
                    }
                }
            });
            teamSystemTipDialog.show();
        }
    }

    /**
     * 添加管理员权限
     */
    private void addManagers() {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        ArrayList<String> accountList = new ArrayList<>();
        accountList.add(account);
        NIMClient.getService(TeamService.class).addManagers(teamId, accountList).setCallback(new RequestCallback<List<TeamMember>>() {
            @Override
            public void onSuccess(List<TeamMember> managers) {
                DialogMaker.dismissProgressDialog();
                identity.setText(R.string.team_admin);
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();

                viewMember = managers.get(0);
                updateMemberInfo();
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    /**
     * 撤销管理员权限
     */
    private void removeManagers() {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        ArrayList<String> accountList = new ArrayList<>();
        accountList.add(account);
        NIMClient.getService(TeamService.class).removeManagers(teamId, accountList).setCallback(new RequestCallback<List<TeamMember>>() {
            @Override
            public void onSuccess(List<TeamMember> members) {
                DialogMaker.dismissProgressDialog();
                identity.setText(R.string.team_member);
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();

                viewMember = members.get(0);
                updateMemberInfo();
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    /**
     * 移除群成员确认
     */
    private void showConfirmButton() {
//        EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {
//
//            @Override
//            public void doCancelAction() {
//            }
//
//            @Override
//            public void doOkAction() {
//
//            }
//        };
//        final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, null, getString(R.string.team_member_remove_confirm),
//                getString(R.string.remove), getString(R.string.cancel), true, listener);
//        dialog.show();
        nimEditTipsDialog = new NimEditTipsDialog(AdvancedTeamMemberInfoActivity.this);
        nimEditTipsDialog.setButtonListenEvent(new NimEditTipsDialog.ButtonListenEvent() {
            @Override
            public void cancle(View v) {
                nimEditTipsDialog.dismiss();
            }

            @Override
            public void ok(View v) {
                removeMember();
            }
        });
        nimEditTipsDialog.show();
        nimEditTipsDialog.setViewType(NimEditTipsDialog.CHOSES);

    }

    /**
     * 移除群成员
     */
    private void removeMember() {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).removeMember(teamId, account).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                makeIntent(account, isSetAdmin, true);
                finish();
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AdvancedTeamMemberInfoActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        makeIntent(account, isSetAdmin, false);
        super.onBackPressed();
    }

    /**
     * 设置返回的Intent
     *
     * @param account    帐号
     * @param isSetAdmin 是否设置为管理员
     * @param value      是否移除群成员
     */
    private void makeIntent(String account, boolean isSetAdmin, boolean value) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, account);
        intent.putExtra(EXTRA_ISADMIN, isSetAdmin);
        intent.putExtra(EXTRA_ISREMOVE, value);
        setResult(RESULT_OK, intent);
    }

    private boolean isSelf(String account) {
        return NimUIKit.getAccount().equals(account);
    }
}