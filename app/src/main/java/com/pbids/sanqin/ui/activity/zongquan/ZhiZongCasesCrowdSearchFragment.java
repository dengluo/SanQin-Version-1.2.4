package com.pbids.sanqin.ui.activity.zongquan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.custom.NimEditTipsDialog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseToolBarFragment;
import com.pbids.sanqin.base.ComonGroupRecycerAdapter;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.model.entity.GroupList;
import com.pbids.sanqin.model.entity.TeamGroupInfo;
import com.pbids.sanqin.presenter.ZhiZongCasesCrowdSearchPresenter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : 上官名鹏
 * Description : 群搜索页面
 * Date :Create in 2018/7/19 11:09
 * Modified By :
 */
public class ZhiZongCasesCrowdSearchFragment extends BaaseToolBarFragment<ZhiZongCasesCrowdSearchPresenter> implements ZhiZongCasesCrowdSearchView {


    @Bind(R.id.cases_search_tv)
    TextView casesSearchTv;
    @Bind(R.id.cases_search_et)
    EditText casesSearchEt;
    @Bind(R.id.search_team_list_rcy)
    RecyclerView teamListRcy;
    @Bind(R.id.search_result_null_lin)
    LinearLayout resultNullLin;

    private ComonRecycerGroup<GroupList> comonRecycerGroup;

    private ComonRecycerGroup<TeamGroupInfo> comonRecycerGroupItem;

    private List<ComonRecycerGroup> groupList,groupListItem;

    private ComonGroupRecycerAdapter mAdpter,mAdpterItem;

    private ZhiZongCasesCrowdSearchPresenter searchPresenter;

    public static final String ISFAMILYMIAN = "isFamilyMain";

    private boolean isFamilyMain;

    private String teamId;

    public static ZhiZongCasesCrowdSearchFragment instance(boolean isFamilyMain) {
        ZhiZongCasesCrowdSearchFragment zhiZongCasesCrowdSearchFragment = new ZhiZongCasesCrowdSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ISFAMILYMIAN,isFamilyMain);
        zhiZongCasesCrowdSearchFragment.setArguments(bundle);
        return zhiZongCasesCrowdSearchFragment;
    }

    @Override
    protected ZhiZongCasesCrowdSearchPresenter initPresenter() {
        return searchPresenter = new ZhiZongCasesCrowdSearchPresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cases_crowd_search_page, container, false);
        ButterKnife.bind(this, view);
        paresIntentData();
        initView();
        return view;
    }

    private void paresIntentData() {
        Bundle bundle = getArguments();
        isFamilyMain = (boolean) bundle.get(ISFAMILYMIAN);
    }

    private void initView() {
        casesSearchTv.setVisibility(View.GONE);
        casesSearchEt.setVisibility(View.VISIBLE);

    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("宗氏群", getContext());
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        pop();
    }

    @Override
    public void success(List<GroupList> groupLists) {
        if(groupLists.size()>0){
            comonRecycerGroup = new ComonRecycerGroup<>();
            comonRecycerGroup.setLists(groupLists);
            groupList = new ArrayList<>();
            groupList.add(comonRecycerGroup);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mAdpter = new ComonGroupRecycerAdapter(getContext(),groupList,R.layout.item_cases_crowd_main);
            teamListRcy.setLayoutManager(layoutManager);
            teamListRcy.setAdapter(mAdpter);
            mAdpter.setViewHolder(new ComonGroupRecycerAdapter.ViewHolder() {
                @Override
                public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

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
                    if(isFamilyMain){
                        TextView titlteTv = holder.get(R.id.item_cases_crowd_title);
                        TextView countTv = holder.get(R.id.item_cases_crowd_count);
                        final ImageView arrowIcon = holder.get(R.id.me_home_campaign_right_arrow);
                        titlteTv.setText(groupList.getArea()+"宗氏群");
                        countTv.setText(String.valueOf("("+groupList.getGroupList().size()+")"));
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
                                    loadItemList(groupList.getGroupList(), itemListRcy);
                                    arrowIcon.animate().rotation(90);
                                }
                            }
                        });
                    }else{
                        itemLin.setVisibility(View.GONE);
                        itemListRcy.setVisibility(View.VISIBLE);
                        loadItemList(groupList.getGroupList(),itemListRcy);
                    }
                }
            });
        }else{
            teamListRcy.setVisibility(View.GONE);
            resultNullLin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void failed(String message) {

    }

    @Override
    public void successTeamInfo(List<TeamGroupInfo> teamGroupInfos) {
        loadItemList(teamGroupInfos,teamListRcy);
    }

    @Override
    public void joinTeamSuccess(int number) {
        int joinedTeamNumber = searchPresenter.queryMyTeamSize();
        if(joinedTeamNumber<number){
            joinTeam(teamId);
        }else if(number==-1){
            joinTeam(teamId);
        } else{
            final NimEditTipsDialog nimEditTipsDialog = new NimEditTipsDialog(_mActivity);
            nimEditTipsDialog.setButtonListenEvent(new NimEditTipsDialog.ButtonListenEvent() {
                @Override
                public void cancle(View v) {

                }

                @Override
                public void ok(View v) {
                    nimEditTipsDialog.dismiss();
                }
            });
            showDialog(nimEditTipsDialog,0.8, getResources().getDimensionPixelSize(R.dimen.dp_230));
            nimEditTipsDialog.setViewType(NimEditTipsDialog.CHOSES);
            nimEditTipsDialog.getCancle().setVisibility(View.GONE);
            nimEditTipsDialog.setDescTitle("您的入群数量已达到3个，如需加入新群需先退出已加入的群，方可加入新群！");
            nimEditTipsDialog.setOkBtTitle("好的");
        }
    }

    private void joinTeam(final String teamId) {
        NIMClient.getService(TeamService.class).applyJoinTeam(teamId, null).setCallback(new RequestCallback<Team>() {
            @Override
            public void onSuccess(Team team) {

                // 申请成功, 等待验证入群
//                NimUIKit.startTeamSession(getContext(),team.getId());
                showToast("申请成功");
            }

            @Override
            public void onFailed(int code) {
                //ToDo 第三方问题，需要自己做判断
                if (code==801){
                    showToast("群人数达到上限");
                }else if (code==802){
                    showToast("没有权限");
                }else if (code==803){
                    showToast("群不存在");
                }else if (code==804){
                    showToast("用户不在群");
                }else if (code==805){
                    showToast("群类型不匹配");
                }else if (code==806){
                    showToast("创建群数量达到限制");
                }else if (code==807){
                    showToast("群成员状态错误");
                }else if (code==808){
//                    NimUIKit.startTeamSession(getContext(),teamId);
                    showToast("申请成功");
                }else if (code==809){
                    showToast("已经在群内");
                }else if (code==810){
                    showToast("邀请成功");
                }else{
                    showToast("未知错误");
                }
            }

            @Override
            public void onException(Throwable exception) {
                showToast("加入群异常");
            }
        });
    }

    @Override
    public void joinTeamFailed() {
        showToast("系统异常！");
    }


    @OnClick(R.id.team_search_img)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.team_search_img:
                //搜索群
                searchPresenter.searchTeam(casesSearchEt.getText().toString().trim(),isFamilyMain);
                break;
        }
    }

    /**
     * 点击后渲染item
     * @param teamGroupInfos
     */
    private void loadItemList(List<TeamGroupInfo> teamGroupInfos, RecyclerView itemListRcy) {
        if(teamGroupInfos.size()>0){
            //为空背景隐藏
            teamListRcy.setVisibility(View.VISIBLE);
            resultNullLin.setVisibility(View.GONE);
            itemListRcy.setVisibility(View.VISIBLE);
            comonRecycerGroupItem = new ComonRecycerGroup<>();
            comonRecycerGroupItem.setLists(teamGroupInfos);
            groupListItem = new ArrayList<>();
            groupListItem.add(comonRecycerGroupItem);
            mAdpterItem = new ComonGroupRecycerAdapter(getContext(), groupListItem,R.layout.item_cases_crowd_item);
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
                    View messageDefault = holder.get(R.id.item_cases_crowd_message_default);
                    TextView messageCountTv = holder.get(R.id.item_cases_crowd_message_count);
                    Button addTeamBt = holder.get(R.id.add_team_bt);
                    TextView addedTeamBt = holder.get(R.id.added_team_bt);
                    messageDefault.setVisibility(View.GONE);
                    messageCountTv.setVisibility(View.GONE);
                    ImageView teamImg = holder.get(R.id.cases_crowd_team_img);
                    titlteTv.setText(teamGroupInfo.getGroupName());
                    if(teamGroupInfo.getIcon()==null||teamGroupInfo.getIcon().equals("")){
                        Glide.with(_mActivity).load(R.drawable.nim_avatar_group).into(teamImg);
                    }else{
                        Glide.with(_mActivity).load(teamGroupInfo.getIcon()).into(teamImg);
                    }
                    countTv.setText("("+teamGroupInfo.getSize()+")");
                    if(teamGroupInfo.isMyTeam()){
                        addTeamBt.setVisibility(View.GONE);
                        addedTeamBt.setVisibility(View.VISIBLE);
                        xiaLaLin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击进入群聊页面
                                NimUIKit.startTeamSession(getContext(), teamGroupInfo.getGroupId());
                            }
                        });
                    } else {
                        addedTeamBt.setVisibility(View.GONE);
                        addTeamBt.setVisibility(View.VISIBLE);
                        addTeamBt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                          //加入群
                          searchPresenter.valJoinTeamNumber();
                          teamId = teamGroupInfo.getGroupId();
                            }
                        });
                    }
                }
            });
        }else {
            teamListRcy.setVisibility(View.GONE);
            resultNullLin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
