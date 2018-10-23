package com.pbids.sanqin.ui.activity.zongquan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.pbids.sanqin.R;
import com.netease.nim.uikit.common.CommonGroup;
import com.netease.nim.uikit.business.team.model.ComGroupItem;
import com.pbids.sanqin.session.SessionHelper;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItem;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.model.ContactGroupStrategy;
import com.netease.nim.uikit.business.contact.core.provider.ContactDataProvider;
import com.netease.nim.uikit.business.contact.core.query.IContactDataProvider;
import com.netease.nim.uikit.business.contact.core.viewholder.ContactHolder;
import com.netease.nim.uikit.business.contact.core.viewholder.LabelHolder;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.pbids.sanqin.team.TeamCreateHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 群列表(通讯录)
 * <p/>
 * Created by huangjun on 2015/4/21.
 */
public class TeamListActivity extends UI implements AdapterView.OnItemClickListener {

    public static final int OPEN_CREATE_GROUP_REQUEST_CODE_NORMAL = 2071;

    private static final String EXTRA_DATA_ITEM_TYPES = "EXTRA_DATA_ITEM_TYPES";

    private ContactDataAdapter adapter;

    private ListView lvContacts;

    private int itemType;

    public static final void start(Context context, int teamItemTypes) {
        Intent intent = new Intent();
        intent.setClass(context, TeamListActivity.class);
        intent.putExtra(EXTRA_DATA_ITEM_TYPES, teamItemTypes);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消全屏   不加这会出现全屏问题
        getWindow().clearFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN);
        itemType = getIntent().getIntExtra(EXTRA_DATA_ITEM_TYPES, ItemTypes.TEAMS.ADVANCED_TEAM);
        setContentView(R.layout.group_list_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = itemType == ItemTypes.TEAMS.ADVANCED_TEAM ? R.string.advanced_team : R.string.normal_team;
        setToolBar(R.id.toolbar, options);

        lvContacts = (ListView) findViewById(R.id.group_list);

        GroupStrategy groupStrategy = new GroupStrategy();
        IContactDataProvider dataProvider = new ContactDataProvider(itemType);

        //自定义
        adapter = new ContactDataAdapter(this, groupStrategy, dataProvider) {
            @Override
            protected List<AbsContactItem> onNonDataItems() {
                //return ComGroupItem.provide();
                return null;
            }

            @Override
            protected void onPreReady() {
            }

            @Override
            protected void onPostLoad(boolean empty, String queryText, boolean all) {
            }
        };
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.TEAM, ContactHolder.class);
        adapter.addViewHolder(ComGroupItem.TYPE_COM_GROUP_ITEM,ComGroupItem.ComGroupViewHolder.class);

        lvContacts.setAdapter(adapter);
        lvContacts.setOnItemClickListener(this);
        lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                showKeyboard(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        // load data
        int count = NIMClient.getService(TeamService.class).queryTeamCountByTypeBlock(itemType == ItemTypes.TEAMS
                .ADVANCED_TEAM ? TeamTypeEnum.Advanced : TeamTypeEnum.Normal);
        if (count == 0) {
            if (itemType == ItemTypes.TEAMS.ADVANCED_TEAM) {
                Toast.makeText(TeamListActivity.this, R.string.no_team, Toast.LENGTH_SHORT).show();
            } else if (itemType == ItemTypes.TEAMS.NORMAL_TEAM) {
                Toast.makeText(TeamListActivity.this, R.string.no_normal_team, Toast.LENGTH_SHORT).show();
            }
        }

        adapter.load(true);

        registerTeamUpdateObserver(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerTeamUpdateObserver(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if(data!=null){
                switch (requestCode) {
                    case OPEN_CREATE_GROUP_REQUEST_CODE_NORMAL:
                        //讨论组
                        final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                        if (selected != null && !selected.isEmpty()) {
                            String groupName = data.getStringExtra("teamId");
                            if(groupName==null||groupName.isEmpty()){
                                Toast.makeText(this, "未知错误！", Toast.LENGTH_SHORT).show();
                            }else{
                                TeamCreateHelper.createNormalTeam(this, groupName, selected, false, null);
                            }
                        } else {
                            Toast.makeText(this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                        }
                    break;
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AbsContactItem item = (AbsContactItem) adapter.getItem(position);
        switch (item.getItemType()) {
            case ItemTypes.TEAM:
                //点击组
                SessionHelper.startTeamSession(this, ((ContactItem) item).getContact().getContactId());
                break;
            case ComGroupItem.TYPE_COM_GROUP_ITEM:
                //点击自定义组
                ComGroupItem curgroup = null;
                try{
                    curgroup = (ComGroupItem)item;
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (curgroup != null) {
                    List<Team> groups = CommonGroup.getNormalGroups();
                    String myGroupname = curgroup.getGrounpame();
                    if (!CommonGroup.hasGroupName(myGroupname, groups)) {
                        //点击
                        if (CommonGroup.hasGroupName(myGroupname, groups)) {
                            //有 -- 打开聊天室
                            SessionHelper.startTeamSession(this, ((ContactItem) item).getContact().getContactId());
                        } else {
                            //无 -- 创建组
                            ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(null, 50);
                            option.teamId = myGroupname;
                            NimUIKit.startContactSelector(this, option, OPEN_CREATE_GROUP_REQUEST_CODE_NORMAL);
                        }
                    }
                }
                break;
        }
    }



    //讨论级事件
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }
    //事件处理
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.load(true);
        }

        @Override
        public void onRemoveTeam(Team team) {
            adapter.load(true);
        }
    };

    private static class GroupStrategy extends ContactGroupStrategy {
        GroupStrategy() {
            add(ContactGroupStrategy.GROUP_NULL, 0, ""); // 默认分组
        }

        @Override
        public String belongs(AbsContactItem item) {
            switch (item.getItemType()) {
                case ItemTypes.TEAM:
                    return GROUP_NULL;
                default:
                    return null;
            }
        }
    }

}
