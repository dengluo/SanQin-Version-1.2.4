package com.netease.nim.uikit.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.core.item.ContactIdFilter;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamInfoActivity;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamMemberActivity;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamMemberInfoActivity;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamNicknameActivity;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.team.ui.TeamInfoGridView;
import com.netease.nim.uikit.business.team.viewholder.TeamMemberHolder;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.MenuDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamBeInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamInviteModeEnum;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.constant.TeamUpdateModeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *@author : 上官名鹏
 *Description : 群设置页面
 *Date :Create in 2018/7/20 11:19
 *Modified By :
 */

public class NimTeamSystemActivity extends UI implements TAdapterDelegate ,TeamMemberHolder.TeamMemberHolderEventListener, TeamMemberAdapter.AddMemberCallback{

    private TextView inviteInTeamTitle;
    private TextView inviteInTeamDetail;
    private TextView inviteInTeamValTitle;
    private TextView inviteInTeamValDetail;
    private TextView applyInTeamValJurTitle;
    private TextView applyInTeamValJurDetail;
    private TextView teamEditJurTitle;
    private TextView teamEditJurDetail;
    private TextView makeOverTeamTitle;
    private TextView makeOverTeamDetail;
    private View inviteInTeamSystem;
    private View inviteInTeamVal;
    private View applyInTeamValJur;
    private View teamEditJur;
    private View makeOverTeam;

//    private TextView teamBusinessCard; // 我的群名片
//
//    private TextView memberCountText;
//
//
//    private View layoutMime;
//
//    private TextView announcementEdit;
//
//    private View layoutTeamAnnouncement;

    private MenuDialog teamInviteeDialog;

    private MenuDialog teamInfoUpdateDialog;

    private MenuDialog authenDialog;

    private AlertView teamSystemTipDialog;

    private List<String> memberAccounts;

    private List<TeamMember> members;

    private List<TeamMemberAdapter.TeamMemberItem> dataSource;

    private List<String> managerList;

    private TeamMemberAdapter adapter;

//    private TeamInfoGridView gridView;

    private MenuDialog dialog;

    private Team team;

    private String teamId;

    private MenuDialog inviteDialog;

    private Toolbar toolbar;

    private String creator;

    public static final String TEAMID = "teamId";

    private static final int REQUEST_CODE_TRANSFER = 101;
    private static final int REQUEST_CODE_MEMBER_LIST = 102;
    private static final int REQUEST_CODE_CONTACT_SELECT = 103;
    private static final int REQUEST_PICK_ICON = 104;

    private static final int TEAM_MEMBERS_SHOW_LIMIT = 5;

    private boolean isSelfAdmin = false;
    private boolean isSelfManager = false;
    private String account;


    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.putExtra(TEAMID, tid);
        intent.setClass(context, NimTeamSystemActivity.class);
        context.startActivity(intent);
    }

    /**
     * ************************ TAdapterDelegate **************************
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return TeamMemberHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    @Override
    public void onHeadImageViewClick(String account) {
        // 打开群成员信息详细页面
        AdvancedTeamMemberInfoActivity.startActivityForResult(NimTeamSystemActivity.this, account, teamId);
    }

    /**
     * 从联系人选择器发起邀请成员
     */
    @Override
    public void onAddMember() {
        ContactSelectActivity.Option option = TeamHelper.getContactSelectOption(memberAccounts);
        NimUIKit.startContactSelector(NimTeamSystemActivity.this, option, REQUEST_CODE_CONTACT_SELECT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_activity_cases_team_system);
        //初始化标题栏
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
        //初始化控件
        initViews();
        //加载群组信息
        loadTeamInfo();
        //设置标题栏
        initActionbar();
        //初始化
        initAdapter();
        //加载群成员信息
        requestMembers();
        EventBus.getDefault().register(this);
    }

    private void initAdapter() {
        memberAccounts = new ArrayList<>();
        members = new ArrayList<>();
        dataSource = new ArrayList<>();
        managerList = new ArrayList<>();
        adapter = new TeamMemberAdapter(NimTeamSystemActivity.this, dataSource, this, null, this);
        adapter.setEventListener(this);

//        gridView.setSelector(R.color.transparent);
//        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == 0) {
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
//        gridView.setAdapter(adapter);
    }

    /**
     * *************************** 加载&变更数据源 ********************************
     */
    private void requestMembers() {
        NimUIKit.getTeamProvider().fetchTeamMemberList(teamId, new SimpleCallback<List<TeamMember>>() {
            @Override
            public void onResult(boolean success, List<TeamMember> members, int code) {
                if (success && members != null && !members.isEmpty()) {
                    updateTeamMember(members);
                }
            }
        });
    }

    /**
     * 更新群成员信息
     *
     * @param m
     */
    private void updateTeamMember(final List<TeamMember> m) {
        if (m != null && m.isEmpty()) {
            return;
        }

//        updateTeamBusinessCard(m);
        addTeamMembers(m, true);
    }

    /**
     * 添加群成员到列表
     *
     * @param m     群成员列表
     * @param clear 是否清除
     */
    private void addTeamMembers(final List<TeamMember> m, boolean clear) {
        if (m == null || m.isEmpty()) {
            return;
        }

        isSelfManager = false;
        isSelfAdmin = false;

        if (clear) {
            this.members.clear();
            this.memberAccounts.clear();
        }

        // add
        if (this.members.isEmpty()) {
            this.members.addAll(m);
        } else {
            for (TeamMember tm : m) {
                if (!this.memberAccounts.contains(tm.getAccount())) {
                    this.members.add(tm);
                }
            }
        }

        // sort
        Collections.sort(this.members, TeamHelper.teamMemberComparator);

        // accounts, manager, creator
        this.memberAccounts.clear();
        this.managerList.clear();
        for (TeamMember tm : members) {
            if (tm == null) {
                continue;
            }
            if (tm.getType() == TeamMemberType.Manager) {
                managerList.add(tm.getAccount());
            }
            if (tm.getAccount().equals(NimUIKit.getAccount())) {
                if (tm.getType() == TeamMemberType.Manager) {
                    isSelfManager = true;
                } else if (tm.getType() == TeamMemberType.Owner) {
                    isSelfAdmin = true;
                    creator = NimUIKit.getAccount();
                }
            }
            this.memberAccounts.add(tm.getAccount());
        }

//        updateAuthenView();
        updateTeamMemberDataSource();
    }

    /**
     * 更新成员信息
     */
    private void updateTeamMemberDataSource() {

        dataSource.clear();

        // add item
        if (team.getTeamInviteMode() == TeamInviteModeEnum.All || isSelfAdmin || isSelfManager) {
            dataSource.add(new TeamMemberAdapter.TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag.ADD, null, null,
                    null));
        }

        // member item
        int count = 0;
        String identity = null;
        for (String account : memberAccounts) {
            int limit = TEAM_MEMBERS_SHOW_LIMIT;
            if (team.getTeamInviteMode() == TeamInviteModeEnum.All || isSelfAdmin || isSelfManager) {
                limit = TEAM_MEMBERS_SHOW_LIMIT - 1;
            }
            if (count < limit) {
                identity = getIdentity(account);
                dataSource.add(new TeamMemberAdapter.TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag
                        .NORMAL, teamId, account, identity));
            }
            count++;
        }

        // refresh
        adapter.notifyDataSetChanged();
//        memberCountText.setText(String.format("共%d人", count));
    }

    private String getIdentity(String account) {
        String identity;
        if (creator.equals(account)) {
            identity = TeamMemberHolder.OWNER;
        } else if (managerList.contains(account)) {
            identity = TeamMemberHolder.ADMIN;
        } else {
            identity = null;
        }
        return identity;
    }

    /**
     * 初始化群组基本信息
     */
    private void loadTeamInfo() {
        Intent intent = getIntent();
        teamId = intent.getStringExtra(TEAMID);
        Team t = NimUIKit.getTeamProvider().getTeamById(teamId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onGetTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText(" ");
//        toolbarView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AdvancedTeamCreateAnnounceActivity.startActivityForResult(AdvancedTeamAnnounceActivity.this, teamId, RES_ANNOUNCE_CREATE_CODE);
//            }
//        });
    }

    private void updateTeamInfo(Team t) {
        this.team = t;
        String inviteInTeamDetailStr="管理员";
        String inviteInTeamValDetailStr="需要验证";
        String applyInTeamValJurDetailStr="不需要验证";
        String teamEditJurDetailStr="管理员/群主";
        if(t.getTeamInviteMode().getValue()==1)  inviteInTeamDetailStr = "所有人";
        if(t.getTeamBeInviteMode().getValue()==1) inviteInTeamValDetailStr = "不需要被邀请方同意";
        if(t.getVerifyType().getValue()==1||t.getVerifyType().getValue()==2) applyInTeamValJurDetailStr = "需要验证";
        if(t.getTeamUpdateMode().getValue()==1) teamEditJurDetailStr = "所有人";
        inviteInTeamDetail.setText(inviteInTeamDetailStr);
        inviteInTeamValDetail.setText(inviteInTeamValDetailStr);
        applyInTeamValJurDetail.setText(applyInTeamValJurDetailStr);
        teamEditJurDetail.setText(teamEditJurDetailStr);

    }


    private void initViews() {

        setTitle("群设置");

        inviteInTeamSystem = findViewById(R.id.team_system_invite_in);
        inviteInTeamVal = findViewById(R.id.team_system_invite_in_val);
        applyInTeamValJur = findViewById(R.id.team_system_invite_in_val_jur);
        teamEditJur = findViewById(R.id.team_system_invite_in_edit_jur);
        makeOverTeam = findViewById(R.id.team_system_make_over_team);

        //邀请入群设置
        inviteInTeamTitle = inviteInTeamSystem.findViewById(R.id.item_title);
        inviteInTeamDetail = inviteInTeamSystem.findViewById(R.id.item_detail);
        inviteInTeamTitle.setText("邀请入群设置");
        inviteInTeamSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamInviteMenu();
            }
        });

        //被邀请人身份验证
        inviteInTeamValTitle = inviteInTeamVal.findViewById(R.id.item_title);
        inviteInTeamValDetail = inviteInTeamVal.findViewById(R.id.item_detail);
        inviteInTeamValTitle.setText("被邀请身份验证");
        inviteInTeamVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamInviteeAuthenMenu();
            }
        });

        //申请入群验证权限
        applyInTeamValJurTitle = applyInTeamValJur.findViewById(R.id.item_title);
        applyInTeamValJurDetail = applyInTeamValJur.findViewById(R.id.item_detail);
        applyInTeamValJurTitle.setText("申请入群验证权限");
        applyInTeamValJur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamAuthenMenu();
            }
        });


        //群资料修改权限
        teamEditJurTitle = teamEditJur.findViewById(R.id.item_title);
        teamEditJurDetail = teamEditJur.findViewById(R.id.item_detail);
        teamEditJurTitle.setText("群资料修改权限");
        teamEditJur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTeamInfoUpdateMenu();
            }
        });


        //转让群
        makeOverTeamTitle = makeOverTeam.findViewById(R.id.item_title);
        makeOverTeamDetail = makeOverTeam.findViewById(R.id.item_detail);
        makeOverTeamTitle.setText("转让群");
        makeOverTeamDetail.setText("  ");

        makeOverTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTransferTeam();
            }
        });

//        gridView = (TeamInfoGridView) findViewById(R.id.team_member_grid_view);
//        gridView.setVisibility(View.GONE);

//        layoutTeamMember = findViewById(R.id.team_memeber_layout);
//        ((TextView) layoutTeamMember.findViewById(R.id.item_title)).setText(R.string.team_member);
//        memberCountText = (TextView) layoutTeamMember.findViewById(R.id.item_detail);

//        memberCountText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AdvancedTeamMemberActivity.startActivityForResult(NimTeamSystemActivity.this, teamId, REQUEST_CODE_MEMBER_LIST);
//            }
//        });


//        layoutMime = findViewById(R.id.team_mime_layout);
//        teamBusinessCard = (TextView) layoutMime.findViewById(R.id.item_detail);
//        layoutMime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AdvancedTeamNicknameActivity.start(NimTeamSystemActivity.this, teamBusinessCard.getText().toString());
//            }
//        });

//        layoutTeamAnnouncement = findViewById(R.id.team_announcement_layout);
//        announcementEdit = ((TextView) layoutTeamAnnouncement.findViewById(R.id.item_detail));
//        announcementEdit.setHint(R.string.team_announce_hint);


    }

    private void onGetTeamInfoFailed() {
        Toast.makeText(this, getString(com.netease.nim.uikit.R.string.team_not_exist), Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 从联系人选择器选择群转移对象
     */
    private void onTransferTeam() {
        if (memberAccounts.size() <= 1) {
            Toast.makeText(NimTeamSystemActivity.this, R.string.team_transfer_without_member, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        ContactSelectActivity.Option option = new ContactSelectActivity.Option();
        option.title = "选择群转移的对象";
        option.type = ContactSelectActivity.ContactSelectType.TEAM_MEMBER;
        option.teamId = teamId;
        option.multi = false;
        option.maxSelectNum = 1;
        ArrayList<String> includeAccounts = new ArrayList<>();
        includeAccounts.addAll(memberAccounts);
        option.itemFilter = new ContactIdFilter(includeAccounts, false);
        NimUIKit.startContactSelector(this, option, REQUEST_CODE_TRANSFER);
//        dialog.dismiss();
    }

    /**
     * 显示验证菜单
     */
    private void showTeamAuthenMenu() {
//        if (authenDialog == null) {
//            List<String> btnNames = TeamHelper.createAuthenMenuStrings();
//
//            int type = team.getVerifyType().getValue();
//            authenDialog = new MenuDialog(NimTeamSystemActivity.this, btnNames, type, 3, new MenuDialog
//                    .MenuDialogOnButtonClickListener() {
//                @Override
//                public void onButtonClick(String name) {
//                    authenDialog.dismiss();
//
//                    if (name.equals(getString(R.string.cancel))) {
//                        return; // 取消不处理
//                    }
//                    VerifyTypeEnum type = TeamHelper.getVerifyTypeEnum(name);
//                    if (type != null) {
//                        setAuthen(type);
//                    }
//
//                }
//            });
//        }
//        authenDialog.show();

        teamSystemTipDialog = new AlertView("申请入群身份权限", null, "取消", null, new String[]{"允许任何人加入", "需要身份验证","不允许任何人申请加入"}, NimTeamSystemActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                VerifyTypeEnum type = TeamHelper.getVerifyTypeEnum(position);
                setAuthen(type);
            }
        });
        teamSystemTipDialog.show();
    }

    private void showTeamInviteMenu() {
//        if (inviteDialog == null) {
//            List<String> btnNames = TeamHelper.createInviteMenuStrings();
//
//            int type = team.getTeamInviteMode().getValue();
//            inviteDialog = new MenuDialog(this, btnNames, type, 2, new MenuDialog
//                    .MenuDialogOnButtonClickListener() {
//                @Override
//                public void onButtonClick(String name) {
//                    inviteDialog.dismiss();
//
//                    if (name.equals(getString(R.string.cancel))) {
//                        return; // 取消不处理
//                    }
//                    TeamInviteModeEnum type = TeamHelper.getInviteModeEnum(name);
//                    if (type != null) {
//                        updateInviteMode(type);
//                    }
//                }
//            });
//        }
//        inviteDialog.show();

        teamSystemTipDialog = new AlertView("邀请入群方式", null, "取消", null, new String[]{"管理员邀请", "所有人邀请"}, NimTeamSystemActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                TeamInviteModeEnum type = TeamHelper.getInviteModeEnum(position);
                updateInviteMode(type);
            }
        });
        teamSystemTipDialog.show();
    }

    private void showTeamInviteeAuthenMenu() {
//        if (teamInviteeDialog == null) {
//            List<String> btnNames = TeamHelper.createTeamInviteeAuthenMenuStrings();
//
//            int type = team.getTeamBeInviteMode().getValue();
//            teamInviteeDialog = new MenuDialog(this, btnNames, type, 2, new MenuDialog
//                    .MenuDialogOnButtonClickListener() {
//                @Override
//                public void onButtonClick(String name) {
//                    teamInviteeDialog.dismiss();
//
//                    if (name.equals(getString(com.netease.nim.uikit.R.string.cancel))) {
//                        return; // 取消不处理
//                    }
//                    TeamBeInviteModeEnum type = TeamHelper.getBeInvitedModeEnum(name);
//                    if (type != null) {
//                        updateBeInvitedMode(type);
//                    }
//                }
//            });
//        }
//        teamInviteeDialog.show();

        teamSystemTipDialog = new AlertView("被邀请人身份验证", null, "取消", null, new String[]{"需要验证", "不需要验证"}, NimTeamSystemActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                TeamBeInviteModeEnum type = TeamHelper.getBeInvitedModeEnum(position);
                updateBeInvitedMode(type);
            }
        });
        teamSystemTipDialog.show();
    }

    // 显示群资料修改权限菜单
    private void showTeamInfoUpdateMenu() {
//        if (teamInfoUpdateDialog == null) {
//            List<String> btnNames = TeamHelper.createTeamInfoUpdateMenuStrings();
//
//            int type = team.getTeamUpdateMode().getValue();
//            teamInfoUpdateDialog = new MenuDialog(NimTeamSystemActivity.this, btnNames, type, 2, new MenuDialog
//                    .MenuDialogOnButtonClickListener() {
//                @Override
//                public void onButtonClick(String name) {
//                    teamInfoUpdateDialog.dismiss();
//
//                    if (name.equals(getString(R.string.cancel))) {
//                        return; // 取消不处理
//                    }
//                    TeamUpdateModeEnum type = TeamHelper.getUpdateModeEnum(name);
//                    if (type != null) {
//                        updateInfoUpdateMode(type);
//                    }
//                }
//            });
//        }
//        teamInfoUpdateDialog.show();

        teamSystemTipDialog = new AlertView("群资料修改权限", null, "取消", null, new String[]{"管理员修改", "所有人修改"}, NimTeamSystemActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                TeamUpdateModeEnum type = TeamHelper.getUpdateModeEnum(position);
                updateInfoUpdateMode(type);
            }
        });
        teamSystemTipDialog.show();

    }

    /**
     * 设置验证模式
     *
     * @param type 验证类型
     */
    private void setAuthen(final VerifyTypeEnum type) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.VerifyType, type).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                setAuthenticationText(type);
                Toast.makeText(NimTeamSystemActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                authenDialog.undoLastSelect(); // 撤销选择
                DialogMaker.dismissProgressDialog();
                Toast.makeText(NimTeamSystemActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    /**
     * 更新群资料修改权限
     *
     * @param type 群资料修改类型
     */
    private void updateInfoUpdateMode(final TeamUpdateModeEnum type) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.TeamUpdateMode, type).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                updateInfoUpateText(type);
                Toast.makeText(NimTeamSystemActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                teamInfoUpdateDialog.undoLastSelect(); // 撤销选择
                DialogMaker.dismissProgressDialog();
                Toast.makeText(NimTeamSystemActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    /**
     * 更新邀请他人权限
     *
     * @param type 邀请他人类型
     */
    private void updateInviteMode(final TeamInviteModeEnum type) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.InviteMode, type).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                updateInviteText(type);
                Toast.makeText(NimTeamSystemActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                inviteDialog.undoLastSelect(); // 撤销选择
                DialogMaker.dismissProgressDialog();
                Toast.makeText(NimTeamSystemActivity.this, String.format(getString(R.string.update_failed), code), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    /**
     * 更新邀请他人detail显示
     *
     * @param type 邀请他人类型
     */
    private void updateInviteText(TeamInviteModeEnum type) {
        inviteInTeamDetail.setText(TeamHelper.getInviteModeString(type));
    }

    /**
     * 更新被邀请人detail显示
     *
     * @param type 被邀请人类型
     */
    private void updateBeInvitedText(TeamBeInviteModeEnum type) {
        inviteInTeamValDetail.setText(TeamHelper.getBeInvitedModeString(type));
    }

    /**
     * 设置验证模式detail显示
     *
     * @param type 验证类型
     */
    private void setAuthenticationText(VerifyTypeEnum type) {
        applyInTeamValJurDetail.setText(TeamHelper.getVerifyString(type));
    }

    /**
     * 更新群资料修改detail显示
     *
     * @param type 群资料修改类型
     */
    private void updateInfoUpateText(TeamUpdateModeEnum type) {
        teamEditJurDetail.setText(TeamHelper.getInfoUpdateModeString(type));
    }

    /**
     * 更新被邀请人权限
     *
     * @param type 被邀请人类型
     */
    private void updateBeInvitedMode(final TeamBeInviteModeEnum type) {
        DialogMaker.showProgressDialog(this, getString(com.netease.nim.uikit.R.string.empty));
        NIMClient.getService(TeamService.class).updateTeam(teamId, TeamFieldEnum.BeInviteMode, type).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                updateBeInvitedText(type);
                Toast.makeText(getApplicationContext(),R.string.update_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int code) {
                teamInviteeDialog.undoLastSelect(); // 撤销选择
                DialogMaker.dismissProgressDialog();
                Toast.makeText(getApplicationContext(), String.format(getString(com.netease.nim.uikit.R.string.update_failed), code), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_TRANSFER:
                final ArrayList<String> target = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (target != null && !target.isEmpty()) {
                    EventBus.getDefault().post(new TransFerTeam(target.get(0),teamId));
                    this.account = target.get(0);
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void transFerTeamEvent(TransFerTeamSuccess transFerTeamSuccess){
        if(!account.equals("")&&transFerTeamSuccess.isFamilyMain()){
            transferTeam(account);
        }
    }

    /**
     * 转让群
     *
     * @param account 转让的帐号
     */
    private void transferTeam(final String account) {

        TeamMember member = NimUIKit.getTeamProvider().getTeamMember(teamId, account);
        if (member == null) {
            Toast.makeText(NimTeamSystemActivity.this, "成员不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        if (member.isMute()) {
            Toast.makeText(NimTeamSystemActivity.this, "该成员已被禁言，请先取消禁言", Toast.LENGTH_LONG).show();
            return;
        }
        NIMClient.getService(TeamService.class).transferTeam(teamId, account, false)
                .setCallback(new RequestCallback<List<TeamMember>>() {
                    @Override
                    public void onSuccess(List<TeamMember> members) {
                        creator = account;
                        updateTeamMember(NimUIKit.getTeamProvider().getTeamMemberList(teamId));
                        Toast.makeText(NimTeamSystemActivity.this, R.string.team_transfer_success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code) {
                        Toast.makeText(NimTeamSystemActivity.this, R.string.team_transfer_failed, Toast.LENGTH_SHORT).show();
//                        Log.e(TAG, "team transfer failed, code=" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
    }
}
