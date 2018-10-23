package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.netease.nim.uikit.business.contact.core.model.IContact;
import com.netease.nim.uikit.business.contact.core.model.UserInfoExtends;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;
import com.pbids.sanqin.ui.recyclerview.adapter.base.SwipeGroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.recyclerview.swipe.SwipeLayout;
import com.pbids.sanqin.ui.view.CircleImageView;

import java.util.List;

/**
 * adapter
 * 模块：亲亲池
 * Created by pbids903 on 2018/1/24.
 */

public class QinQinChiFriendAdapter extends SwipeGroupedRecyclerViewAdapter implements SectionIndexer {

    private List<QinQinChiFriendGroup> sortUserGroupModels = null;
    private Context mContext;

    public QinQinChiFriendAdapter(Context context, List<QinQinChiFriendGroup> sortUserGroupModels) {
        super(context);
        this.mContext = context;
        this.sortUserGroupModels = sortUserGroupModels;
    }

    public void updateListView(List<QinQinChiFriendGroup> list){
        this.sortUserGroupModels = list;
        notifyDataSetChanged();
    }

    public QinQinChiFriend getFriend(int groupPosition,int childPosition){
        return this.sortUserGroupModels.get(groupPosition).getSortUserModels().get(childPosition);
    }


    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        int childCount = 0;
        for(int i=0;i<getGroupCount();i++){
            char firstChar = sortUserGroupModels.get(i).getSortLetters().toUpperCase().charAt(0);
            if(firstChar == sectionIndex){
                Log.i("wzh","childCountNumber: "+childCount);
                Log.i("wzh","childCount: "+sortUserGroupModels.get(i).getSortLetters());
                return childCount;
            }
            childCount+=(getChildrenCount(i)+1);
        }
        Log.i("wzh","sectionIndex: "+sectionIndex);
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
//        return sortUserGroupModels.get(position).getSortLetters().charAt(0);
        return -1;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.zongquan_firends_list_item_swip;
    }

    @Override
    public int getGroupCount() {
        return sortUserGroupModels==null?0:sortUserGroupModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return sortUserGroupModels.get(groupPosition)==null?0:sortUserGroupModels.get(groupPosition).getSortUserModels().size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        QinQinChiFriendGroup item = sortUserGroupModels.get(groupPosition);
        if(item.getUserCount()>0){
            return true ;
        }
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.zongquan_friends_list_header_item;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.adapter_footer;
    }

    @Override
    public int getChildLayout(int viewType) {
        //return R.layout.zongquan_friends_list_item;
        return com.netease.nim.uikit.R.layout.nim_contacts_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        TextView headerTv = holder.get(R.id.catalog);
        headerTv.setText(sortUserGroupModels.get(groupPosition).getSortLetters());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {
        TextView tvFooter = holder.get(R.id.tv_footer);
        QinQinChiFriendGroup item = sortUserGroupModels.get(groupPosition);
        tvFooter.setText("共有好友" + item.getUserCount() + "名");
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, final int groupPosition, final int childPosition) {
        RelativeLayout headLayout = holder.get(com.netease.nim.uikit.R.id.head_layout);
        HeadImageView head = holder.get(com.netease.nim.uikit.R.id.contacts_item_head);
        TextView name = holder.get(com.netease.nim.uikit.R.id.contacts_item_name);
        TextView desc = holder.get(com.netease.nim.uikit.R.id.contacts_item_desc);

        ImageView ImgBusinessCooperation = holder.get(com.netease.nim.uikit.R.id.contacts_business_cooperation);
        TextView tvVip = holder.get(com.netease.nim.uikit.R.id.contacts_item_vip);
        TextView tvLevelName = holder.get(com.netease.nim.uikit.R.id.contacts_levelname);

        /*IContact contact
        //更新扩展信息
        if(contact.getContactType() == IContact.Type.Friend ){
            //取用户扩展信息
            NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(contact.getContactId());
            String extInfoStr = user.getExtension();
            if (null != extInfoStr && !extInfoStr.isEmpty()) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                UserInfoExtends userInfoExtends = gsonBuilder.create().fromJson(extInfoStr, UserInfoExtends.class);
                // @liang
                if (userInfoExtends != null) {
                    //vip
                    if (userInfoExtends.getVip() > 0) {
                        this.tvVip.setVisibility(View.VISIBLE);
                        this.tvVip.setText("VIP" + userInfoExtends.getVip());
                    }
                    //商务合作
                    if (userInfoExtends.getClanStatus() > 0) {
                        this.ImgBusinessCooperation.setVisibility(View.VISIBLE);
                    }
                    //等级
                    if (null != userInfoExtends.getLevelName() && !userInfoExtends.getLevelName().isEmpty()) {
                        this.tvLevelName.setVisibility(View.VISIBLE);
                        this.tvLevelName.setText(userInfoExtends.getLevelName());
                    }
                }
            }
        }*/

        /*
        SwipeLayout swipeLayout =  holder.get(R.id.zongquan_firends_list_item_swip);
        TextView userName = holder.get(R.id.zongquan_fried_list_name);
        CircleImageView userPicture =  holder.get(R.id.zongquan_fried_list_touxiang);
        ImageView userIsVip =  holder.get(R.id.zongquan_fried_list_vip);
        LinearLayout delete =  holder.get(R.id.message_center_item_delete);
        final QinQinChiFriend sortUserModel = sortUserGroupModels.get(groupPosition).getSortUserModels().get(childPosition);
        userName.setText(sortUserModel.getName());

        swipeLayout.setCustomOnClickListener(new SwipeLayout.CustomOnClickListener() {
            @Override
            public void onClick() {
                Log.i("wzh","click=-=-===");
                Log.i("wzh","getOpenItems().size(): "+getOpenItems().size());
                closeAllItems();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wzh","delete=-=-===");
                int position = getPositionForChild(groupPosition,childPosition);
                sortUserGroupModels.get(groupPosition).getSortUserModels().remove(childPosition);
                notifyItemRemoved(position);
                if (position != getItemCount()) {
                    notifyItemRangeChanged(position, getItemCount() - position);
                }
                if(sortUserGroupModels.get(groupPosition).getSortUserModels().size()==0){
                    sortUserGroupModels.remove(groupPosition);
                    notifyItemRemoved(position-1);
                    if (position != getItemCount()) {
                        notifyItemRangeChanged(position-1, getItemCount() - position);
                    }
                }
                closeAllItems();
            }
        });
        mItemManger.bindView(holder.itemView,getPositionForChild(groupPosition,childPosition));
        */
    }

}
