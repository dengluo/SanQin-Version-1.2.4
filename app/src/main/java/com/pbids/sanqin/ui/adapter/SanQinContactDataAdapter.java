package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.netease.nim.uikit.business.contact.core.model.UserInfoExtends;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.liv.LetterIndexView;
import com.netease.nim.uikit.common.ui.liv.LivRecyclerviewIndex;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseSwipeGroupRecycerViewAdapter;
import com.pbids.sanqin.base.ComonRecycerGroup;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.model.entity.FriendGroupMemberDb;
import com.pbids.sanqin.ui.activity.zongquan.FriendViewHolder;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.recyclerview.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 通讯录数据适配器
 * <p/>
 * Created by huangjun on 2015/2/10.
 */
public class SanQinContactDataAdapter extends BaaseSwipeGroupRecycerViewAdapter<ComonRecycerGroup<NimUserInfo>>  {

    protected final HashMap<String, Integer> indexes = new HashMap<>();
    private Context mContext;

    //三亲内容需要分组
    private boolean block = false;

    public SanQinContactDataAdapter(Context context) {
        super(context,new ArrayList<ComonRecycerGroup<NimUserInfo>>());
    }

    public List<NimUserInfo> getNIMClientUserInfoList(List<String> accounts) {
        List<NimUserInfo> mFriendList = new ArrayList<>();
        for (int i = 0; i < accounts.size(); i++) {
            NimUserInfo uInfos = NIMClient.getService(UserService.class).getUserInfo(accounts.get(i));
            if (uInfos != null) {
                mFriendList.add(uInfos);
            }
        }
        return mFriendList;
    }

    //创建一个分组
    public ComonRecycerGroup createBlock(FriendGroupDb groupDb){
        ComonRecycerGroup<NimUserInfo> groupItem = new ComonRecycerGroup();
        List<NimUserInfo> mFriendList = new ArrayList<>();
        List<String> accounts = groupDb.getAccounts();
        if( accounts.size()>0) {
            mFriendList.addAll(getNIMClientUserInfoList(accounts));
            //List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(accounts);
            //mFriendList.addAll(users);
        }
        groupItem.setLists(mFriendList);
        groupItem.setFag(groupDb.getGroupId());
        groupItem.addAttr("groupName",groupDb.getGroupName());
        if(block){
            groupItem.setHeader(groupDb.getGroupName());
        }
        gLists.add(groupItem);
        return groupItem;
    }

    public void setBlock(boolean s)
    {
        block = s ;
    }
    public boolean isBlock(){
        return block;
    }



    public boolean addMember(List accounts, int groupPosition){
        //List<NimUserInfo> uinfos = NIMClient.getService(UserService.class).getUserInfoList(accounts);
        List<NimUserInfo> uinfos =getNIMClientUserInfoList(accounts);
        if(uinfos.size()==0){
            return false;
        }else{
            gLists.get(groupPosition).addBath(uinfos);
            notifyDataSetChanged();
            return true;
        }
        //List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(mPresenter.loadGroupData().getAccidList());
    }

    public boolean addMember(FriendGroupMemberDb member, int groupPosition){
        String account = member.getAccount();
        NimUserInfo uinfo = NIMClient.getService(UserService.class).getUserInfo(account);
        if(uinfo==null){
            return false;
        }else{
            gLists.get(groupPosition).addUserInfo(uinfo);
            notifyDataSetChanged();
            return true;
        }
        //List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(mPresenter.loadGroupData().getAccidList());
    }


    // 删除成员
    public void removeMember( String account ){
        for (ComonRecycerGroup item : gLists) {
            List<NimUserInfo> users  = item.getList();
            for (NimUserInfo one : users) {
                if(one.getAccount().equals(account)){
                    users.remove(one);
                    //更新显示
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }
    public void removeMember(NimUserInfo ninfo ){
        for (ComonRecycerGroup item : gLists) {
            List<NimUserInfo> users  = item.getList();
            for (NimUserInfo one : users) {
                if(one == ninfo){
                    users.remove(one);
                    //更新显示
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    public int memeberCount(){
        int num = 0;
        if(gLists.size()==0){
            return 0;
        }
        for (int i=0;i<gLists.size();i++){
            num += gLists.get(i).getList().size();
        }
        return num;
    }

    //取所有用户
    public List<String> getAccounts(){
        List<String> accounts = new ArrayList<>();
        if(gLists.size()==0){
            return accounts;
        }

        for (int i=0;i<gLists.size();i++){
            List<NimUserInfo> uinfos = gLists.get(i).getList();
            if(uinfos!=null){
                for(NimUserInfo one:uinfos){
                    accounts.add(one.getAccount());
                }
            }
        }
        return accounts;
    }

    private Map<String, Integer> getIndexes() {
        return this.indexes;
    }

    public final LivRecyclerviewIndex createLivIndex(RecyclerView lv, LetterIndexView liv, TextView tvHit, ImageView ivBk) {
        return new LivRecyclerviewIndex(lv, liv, tvHit, ivBk, getIndexes());
    }

    public NimUserInfo getNimInfo( int groupPosition, int childPosition){
        return gLists.get(groupPosition).getList().get(childPosition);
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.zongquan_firends_list_item_swip;
    }

    //click
    public interface ChildClickListener {
        void onChildClickListener(FriendViewHolder holder, int position, View view);
    }

    private ChildClickListener childClickListener = null;

    public void setChildClickListener(ChildClickListener childClickListener) {
        this.childClickListener = childClickListener;
    }

    private void onChildClick(FriendViewHolder holder, int position, View view) {
        if (childClickListener != null) {
            childClickListener.onChildClickListener(holder, position, view);
        }
    }

    @Override
    public int getGroupCount() {
        return gLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return gLists.get(groupPosition).getList().size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        if(gLists.get(groupPosition).getHeader()!=null){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
       if(gLists.get(groupPosition).getFooter()!=null){
            return true;
        }
        return false;
    }

    //分组不能重名
    public boolean hasHeaderName(String name){
        for (ComonRecycerGroup item: gLists){
            if(item.getHeader().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.adapter_member_manager_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.zongquan_friends_list_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, final int groupPosition) {
        ImageView btnAdd = holder.get(R.id.img_add);
        TextView txtName = holder.get(R.id.tv_name);
        final String blockName = gLists.get(groupPosition).getHeader();
        txtName.setText(blockName);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("click header",blockName);
                if(onAddGroupAddListener!=null){
                    onAddGroupAddListener.onAdd(groupPosition);
                }
            }
        });
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, final int groupPosition, final int childPosition) {

        FrameLayout portraitPanel =  holder.get(com.netease.nim.uikit.R.id.portrait_panel);
        HeadImageView imgHead = holder.get(com.netease.nim.uikit.R.id.img_head);
        TextView tvNickname = holder.get(com.netease.nim.uikit.R.id.tv_nickname);


        TextView tvLevelname =   holder.get(com.netease.nim.uikit.R.id.tv_levelname); //等级名称
        TextView tvVip =  holder.get(com.netease.nim.uikit.R.id.tv_item_vip); //等级名称
        ImageView tvBusinessCooperation =  holder.get(com.netease.nim.uikit.R.id.tv_business_cooperation); //商务合作

        View bottomLine=holder.get(com.netease.nim.uikit.R.id.bottom_line);
        View topLine=holder.get(com.netease.nim.uikit.R.id.top_line);

        TextView tvOnlineState = holder.get(com.netease.nim.uikit.R.id.tv_online_state);
        LinearLayout contentLayout = holder.get(com.netease.nim.uikit.R.id.content_layout);;


        //final NimUserInfo nimUserInfo = getNimInfo(childPosition);
        final NimUserInfo nimUserInfo = gLists.get(groupPosition).getList().get(childPosition);
        if (childPosition < 1) {
            topLine.setVisibility(View.VISIBLE);
        }
        // 设置头像
        imgHead.loadBuddyAvatar(nimUserInfo.getAccount());
        //名称
        int labelWidth = ScreenUtil.screenWidth;
        labelWidth -= ScreenUtil.dip2px(50 + 70); // 减去固定的头像和时间宽度

        if (labelWidth > 0) {
            tvNickname.setMaxWidth(labelWidth);
        }
        tvNickname.setText(UserInfoHelper.getUserTitleName(nimUserInfo.getAccount(),  SessionTypeEnum.P2P));


        //在线信息
        tvOnlineState.setVisibility(View.GONE);

        //扩展信息
        //没有扩展信息不显示
        tvVip.setVisibility(View.GONE);
        tvBusinessCooperation.setVisibility(View.GONE);
        tvLevelname.setVisibility(View.GONE);

        //取用户扩展信息
        String extInfoStr = nimUserInfo.getExtension();
        Log.i("用户聊天信息：", extInfoStr);
        if (null != extInfoStr && !extInfoStr.isEmpty()) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            UserInfoExtends userInfoExtends = gsonBuilder.create().fromJson(extInfoStr, UserInfoExtends.class);

            // @liang
            if (userInfoExtends != null) {
                //vip
                if (userInfoExtends.getVip() > 0) {
                    tvVip.setVisibility(View.VISIBLE);
                    tvVip.setText("VIP" + userInfoExtends.getVip());
                }
                //商务合作
                if (userInfoExtends.getClanStatus() > 0) {
                    tvBusinessCooperation.setVisibility(View.VISIBLE);
                }
                //等级
                if (null != userInfoExtends.getLevelName() && !userInfoExtends.getLevelName().isEmpty()) {
                    tvLevelname.setVisibility(View.VISIBLE);
                    tvLevelname.setText(userInfoExtends.getLevelName());
                }
            }
        }

        SwipeLayout swipeLayout =  holder.get(R.id.zongquan_firends_list_item_swip);
        swipeLayout.setCustomOnClickListener(new SwipeLayout.CustomOnClickListener() {
            @Override
            public void onClick() {
                closeAllItems();
            }
        });
        //delete
        LinearLayout delete =  holder.get(R.id.message_center_item_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAllItems();
                deleteItemListener.deleteItem(nimUserInfo, groupPosition, childPosition);
            }
        });
        mItemManger.bindView(holder.itemView,getPositionForChild(groupPosition,childPosition));
    }
    private OnAddGroupAddListener onAddGroupAddListener ;

    public void setOnAddGroupAddListener(OnAddGroupAddListener onAddGroupAddListener) {
        this.onAddGroupAddListener = onAddGroupAddListener;
    }

    public interface OnAddGroupAddListener {
        void onAdd(  int groupPosition );
    }

   /* //取好友列表
    public List<NimUserInfo> getFriendList() {
        return mFriendList;
    }

    //清除好友
    public void clearFriend(){
        mFriendList.clear();
    }
    public void addFriend(NimUserInfo item){
        mFriendList.add(item);
    }

    //通过好友 id 添加
    public void addAll(List<String> accounts){
        mFriendList.clear();
        // 获取所有好友用户资
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(accounts);
        if(users!=null&&users.size()>0)
        mFriendList.addAll(users);
        // udpate view
        notifyDataSetChanged();

        if(block){
            gLists.get(0).setHeader("hello");
        }
    }*/


}
