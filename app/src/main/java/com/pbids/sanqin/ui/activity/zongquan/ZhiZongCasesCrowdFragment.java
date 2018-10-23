package com.pbids.sanqin.ui.activity.zongquan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamInfoActivity.UpdataNameCallBack;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.custom.NimEditTipsDialog;
import com.netease.nim.uikit.custom.TeamInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.netease.nimlib.sdk.team.model.Team;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.base.ComonGroupRecycerAdapter;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.model.entity.GroupList;
import com.pbids.sanqin.model.entity.TeamGroupInfo;
import com.pbids.sanqin.presenter.ZhiZongCasesCrowdPresenter;
import com.pbids.sanqin.team.TeamCreateHelper;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : 上官名鹏
 * Description : 宗人群首页
 * Date :Create in 2018/7/18 11:23
 * Modified By :
 */
public class ZhiZongCasesCrowdFragment extends BaaseToolBarFragment<ZhiZongCasesCrowdPresenter> implements UpdataNameCallBack,ZhiZongCasesCrowdView,TeamCreateHelper.SuccessCallBack {

    @Bind(R.id.cases_crowd_rcy)
    RecyclerView casesCrowdRcy;
    @Bind(R.id.cases_search_tv)
    TextView seartchTv;
    @Bind(R.id.cases_title_tv)
    TextView casesTitleTv;
    private ZhiZongCasesCrowdPresenter casesCrowdPresenter;

    private ComonRecycerGroup<GroupList> comonRecycerGroup;

    private ComonRecycerGroup<TeamGroupInfo> comonRecycerGroupItem;

    private List<ComonRecycerGroup> groupList,groupListItem;

    private ComonGroupRecycerAdapter mAdpter,mAdpterItem;

    private Animation mShowAction,mHiddenAction;

    private boolean isCreateTeam = true;

    private AppToolBar toolBar;

    public static final int REQUEST_CODE_CREATE_TEAM = 3;

    private MsgServiceObserve service;

    private String area;

    private String teamName;

    private ArrayList selectedAdv;

    public static ZhiZongCasesCrowdFragment instance(){
        ZhiZongCasesCrowdFragment zhiZongCasesCrowdFragment = new ZhiZongCasesCrowdFragment();
        Bundle args = new Bundle();
        zhiZongCasesCrowdFragment.setArguments(args);
        return zhiZongCasesCrowdFragment;
    }

    @Override
    public ZhiZongCasesCrowdPresenter initPresenter() {
        return casesCrowdPresenter = new ZhiZongCasesCrowdPresenter(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(messageReceiverObserver!=null){
            service.observeReceiveMessage(messageReceiverObserver, false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //继续监听消息变更
        //群消息未读数监听
        service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, true);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cases_crowd_main, container, false);
        ButterKnife.bind(this, view);
        initView();
        //查询登录用户是否有权限创建群
        casesCrowdPresenter.valFamilyMain();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitleRightText("宗氏群","创建群",getContext());
        toolBar.setOnToolBarClickLisenear(this);
        this.toolBar = toolBar;
        this.toolBar.getRightTv().setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                //创建高级群  //create_regular_team
                ContactSelectActivity.Option advancedOption = TeamHelper.getCreateContactSelectOption(null, 50);
                NimUIKit.startContactSelector(_mActivity, advancedOption, REQUEST_CODE_CREATE_TEAM);
                break;
        }
    }

    private void initView() {
        mShowAction = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_in);
        mHiddenAction = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_in);

    }

    private void initHeadView(){
        if (!isCreateTeam) {
            seartchTv.setHint(getString(R.string.cases_crowd_head_in_search_title));
            casesTitleTv.setText(getString(R.string.cases_title_in));
        }
        seartchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ZhiZongCasesCrowdSearchFragment.instance(isCreateTeam));
            }
        });
    }

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
//                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
//                        continue;
//                    }
                    if(comonRecycerGroupItem!=null){
                        final List<TeamGroupInfo> list = comonRecycerGroupItem.getList();
                        for (TeamGroupInfo teamGroupInfo:list){
                            if(imMessage.getSessionId().equals(teamGroupInfo.getGroupId())){
                                teamGroupInfo.setMessageCount(teamGroupInfo.getMessageCount()+imMessages.size());
                                mAdpterItem.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        }
    };


    @Override
    public void successQueryTeam(final List<GroupList> groupLists) {
        comonRecycerGroup = new ComonRecycerGroup<>();
        comonRecycerGroup.setLists(groupLists);
        groupList = new ArrayList<>();
        groupList.add(comonRecycerGroup);
        mAdpter = new ComonGroupRecycerAdapter(getContext(), groupList,R.layout.item_cases_crowd_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        casesCrowdRcy.setLayoutManager(layoutManager);
        casesCrowdRcy.setAdapter(mAdpter);
        mAdpter.setViewHolder(new ComonGroupRecycerAdapter.ViewHolder() {
            @Override
            public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
//                TextView seartchTv = holder.get(R.id.cases_search_tv);
//                TextView casesTitleTv =  holder.get(R.id.cases_title_tv);
//                if(!isCreateTeam){
//                    seartchTv.setHint(getString(R.string.cases_crowd_head_in_search_title));
//                    casesTitleTv.setText(getString(R.string.cases_title_in));
//                }
//                seartchTv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        start(ZhiZongCasesCrowdSearchFragment.instance(isCreateTeam));
//                    }
//                });
            }

            @Override
            public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

            }

            @Override
            public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
                ComonRecycerGroup<GroupList> comonRecycerGroup = groupList.get(groupPosition);
                final GroupList groupList = comonRecycerGroup.getList().get(childPosition);
                LinearLayout itemLin = holder.get(R.id.item_cases_xiala_lin);
                final RecyclerView itemListRcy = holder.get(R.id.item_cases_crowd_rcy);
                if (isCreateTeam) {
                    TextView titlteTv = holder.get(R.id.item_cases_crowd_title);
                    TextView countTv = holder.get(R.id.item_cases_crowd_count);
                    final ImageView arrowIcon = holder.get(R.id.me_home_campaign_right_arrow);
                    titlteTv.setText(groupList.getArea()+"宗氏群");
                    countTv.setText("("+groupList.getGroupList().size()+")");
                    itemLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemListRcy.getVisibility() == View.VISIBLE) {
                                itemListRcy.setVisibility(View.GONE);
//                            itemListRcy.startAnimation(mHiddenAction);
                                arrowIcon.animate().rotation(0);
                            } else {
                                itemListRcy.setVisibility(View.VISIBLE);
//                            itemListRcy.startAnimation(mShowAction);
//                                loadItemList(casesCrowd.getCasesCrowds(), itemListRcy);
                                loadItemList(groupList.getGroupList(),itemListRcy);
                                arrowIcon.animate().rotation(90);
                            }

                        }
                    });
                } else {
                    //不是总管不显示下拉
                    itemLin.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void failed(String message) {
        showToast(message);
    }

    /**
     * 查询我的群成功
     * @param teamGroupInfos
     */
    @Override
    public void successQueryMyTeam(List<TeamGroupInfo> teamGroupInfos) {
        loadItemList(teamGroupInfos,casesCrowdRcy);
    }


    /**
     * 群列表加载
     * @param teamGroupInfos
     */
    private void loadItemList(List<TeamGroupInfo> teamGroupInfos, RecyclerView itemListRcy) {
        if(!teamGroupInfos.isEmpty()||teamGroupInfos.size()!=0){
            itemListRcy.setVisibility(View.VISIBLE);
            comonRecycerGroupItem = new ComonRecycerGroup<>();
            comonRecycerGroupItem.setLists(teamGroupInfos);
            groupListItem = new ArrayList<>();
            groupListItem.add(comonRecycerGroupItem);
            if (isCreateTeam) {
                mAdpterItem = new ComonGroupRecycerAdapter(getContext(), groupListItem, R.layout.item_cases_crowd_item);
            } else {
                mAdpterItem = new ComonGroupRecycerAdapter(getContext(), groupListItem, R.layout.item_cases_crowd_item);
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            itemListRcy.setLayoutManager(layoutManager);
            itemListRcy.setAdapter(mAdpterItem);
            mAdpterItem.setViewHolder(new ComonGroupRecycerAdapter.ViewHolder() {
                @Override
                public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

                }

                @Override
                public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

                }

                @Override
                public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
                    ComonRecycerGroup<TeamGroupInfo> comonRecycerGroup = groupListItem.get(groupPosition);
                    final TeamGroupInfo teamGroupInfo = comonRecycerGroup.getList().get(childPosition);
                    TextView titlteTv = holder.get(R.id.item_cases_crowd_title);
                    TextView countTv = holder.get(R.id.item_cases_crowd_count);
                    LinearLayout xiaLaLin = holder.get(R.id.item_cases_xiala_lin);
                    final View messageDefault = holder.get(R.id.item_cases_crowd_message_default);
                    final TextView messageCountTv = holder.get(R.id.item_cases_crowd_message_count);
                    HeadImageView teamImg = holder.get(R.id.cases_crowd_team_img);
                    //查询消息未读数
                    titlteTv.setText(teamGroupInfo.getGroupName());
                    countTv.setText("(" + teamGroupInfo.getSize() + "人)");
                    if(teamGroupInfo.getIcon()==null||teamGroupInfo.getIcon().equals("")){
                        Glide.with(_mActivity).load(R.drawable.nim_avatar_group).into(teamImg);
                    }else{
                        Glide.with(_mActivity).load(teamGroupInfo.getIcon()).into(teamImg);
                    }
                    //进入群资料页
                    xiaLaLin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //进入群聊界面清空未读数 刷新列表
                            teamGroupInfo.setMessageCount(0);
                            mAdpterItem.notifyDataSetChanged();
                            messageCountTv.setVisibility(View.GONE);
                            messageDefault.setVisibility(View.GONE);
                            //停止监听消息变更
                            if(messageReceiverObserver!=null){
                                service.observeReceiveMessage(messageReceiverObserver, false);
                            }
                            NimUIKit.startTeamSession(getContext(), teamGroupInfo.getGroupId());
                        }
                    });
                    if (teamGroupInfo.getMessageCount() != 0) {
                        messageCountTv.setVisibility(View.VISIBLE);
                        messageDefault.setVisibility(View.GONE);
                        if(teamGroupInfo.getMessageCount()>99){
                            messageDefault.setVisibility(View.VISIBLE);
                            messageCountTv.setVisibility(View.GONE);
                        }else{
                            messageCountTv.setText(teamGroupInfo.getMessageCount() + "");
                        }
                    } else {
                        messageCountTv.setVisibility(View.GONE);
                        messageDefault.setVisibility(View.GONE);
                    }
                    //把已添加按钮和添加按钮隐藏
                    TextView addedTv = holder.get(R.id.added_team_bt);
                    addedTv.setVisibility(View.GONE);
                }
            });
        }else{
            showToast("您还没有群！");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(data == null) return;
        switch (requestCode){
            case REQUEST_CODE_CREATE_TEAM:
                final ArrayList<String> selectedAdv = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                final NimEditTipsDialog nimEditTipsDialog = new NimEditTipsDialog(getContext());
                nimEditTipsDialog.setButtonListenEvent(new NimEditTipsDialog.ButtonListenEvent() {
                    @Override
                    public void cancle(View v) {
                        nimEditTipsDialog.dismiss();
                    }

                    @Override
                    public void ok(View v) {
                        createAdvancedTeam(selectedAdv,nimEditTipsDialog);
                    }
                });
                showDialog(nimEditTipsDialog,1.0,getResources().getDimensionPixelSize(R.dimen.dp_260));
                nimEditTipsDialog.setViewType(NimEditTipsDialog.EDITINFO);
                nimEditTipsDialog.setTitle("请输入群名称");
                nimEditTipsDialog.setHitContent("例如：王氏深圳宗氏群");

        }
    }

    /**
     * 创建群
     * @param selectedAdv
     * @param nimEditTipsDialog
     */
    private void createAdvancedTeam(ArrayList selectedAdv, NimEditTipsDialog nimEditTipsDialog) {
        if(nimEditTipsDialog.getEditContent()!=null&&!nimEditTipsDialog.getEditContent().equals("")){
            teamName = nimEditTipsDialog.getEditContent();
            String[] str = teamName.split("氏");
            if(str.length>0){
                String str1 = str[1];
                String[] str2 = str1.split("宗");
                if(str2.length>0){
                    area = str2[0];
                    this.selectedAdv = selectedAdv;
                    TeamCreateHelper.createAdvancedTeam( _mActivity, selectedAdv,teamName, this);
                    nimEditTipsDialog.dismiss();
                }else{
                    showToast("群名称不合法，请按照格式取相应的名字");
                }
            }else{
                showToast("群名称不合法，请按照格式取相应的名字");
            }
        }else{
            showToast("请输入群名称！");
        }

    }

    /**
     * 查询是否是家族总管成功
     * @param familyMain
     */
    @Override
    public void valFamilyMainSuccess(boolean familyMain) {
        this.isCreateTeam = familyMain;
        initHeadView();
        if(isCreateTeam){
            //查询分组信息
            this.toolBar.getRightTv().setVisibility(View.VISIBLE);
            casesCrowdPresenter.loadTeamInfo();
        }else{
            this.toolBar.getRightTv().setVisibility(View.GONE);
            this.toolBar.setLeftArrowCenterTextTitle("宗氏群",getContext());
            //查询自己加入的群
            casesCrowdPresenter.queryMyTeam();
        }

    }

    /**
     * 查询是否是家族总管成功
     */
    @Override
    public void valFamilyMainFailed(String message) {
        showToast(message);
    }

    /**
     * 名称上传服务器成功
     */
    @Override
    public void updateSuccess() {
        showToast("群名称更新成功");
    }

    /**
     * 名称上传服务器失败
     */
    @Override
    public void updateFail() {
        showToast("群名称更新失败");
    }

    /**
     * 创建群成功回调
     * @param createTeamResult
     */
    @Override
    public void successTeam(CreateTeamResult createTeamResult) {
        Team team = createTeamResult.getTeam();
        String groupId = team.getId();
        String groupName = team.getName();
        casesCrowdPresenter.updataUserGroup(groupId, groupName, area);
    }

    /**
     * 创建失败回调
     * @param tips
     */
    @Override
    public void failedTeam(String tips) {
        showToast(tips);
    }


    /**
     * 在群资料页面更改群名称回调
     */
    @Override
    public void updateTeamSuccess(String groupName, String groupId) {
        casesCrowdPresenter.updataUserGroup(groupId,groupName,"");
    }

    @Override
    public void updateTeamaFailed(String message) {
        showToast(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(TeamInfo teamInfo) {
//        start(MeAuthenticationFragment.newInstance());
        String id = teamInfo.getId();
        String name = teamInfo.getName();
        casesCrowdPresenter.updataUserGroup(id, name, "");
    }
}
