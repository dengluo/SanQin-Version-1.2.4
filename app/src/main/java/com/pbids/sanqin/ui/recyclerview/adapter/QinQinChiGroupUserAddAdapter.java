package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.CircleImageView;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;
import com.pbids.sanqin.model.entity.QinQinChiFriend;

import java.util.List;

/**
 * 模块： 亲亲池 -> 组用户添加
 * Created by pbids903 on 2018/1/24.
 */

public class QinQinChiGroupUserAddAdapter extends GroupedRecyclerViewAdapter implements SectionIndexer {

    private List<QinQinChiFriendGroup> sortUserGroupModels = null;
    private Context mContext;
    private boolean showRadio = true ;

    public QinQinChiGroupUserAddAdapter(Context context, List<QinQinChiFriendGroup> sortUserGroupModels) {
        super(context);
        this.mContext = context;
        this.sortUserGroupModels = sortUserGroupModels;
    }

    public void updateListView(List<QinQinChiFriendGroup> list){
        this.sortUserGroupModels = list;
        notifyDataSetChanged();
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
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.zongquan_friends_list_header_item;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.zongquan_friends_list_item_add;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        TextView headerTv = holder.get(R.id.catalog);
        headerTv.setText(sortUserGroupModels.get(groupPosition).getSortLetters());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, final int groupPosition, final int childPosition) {

        TextView userName = holder.get(R.id.zongquan_fried_list_name);
        CircleImageView userPicture =  holder.get(R.id.zongquan_fried_list_touxiang);
//        ImageView userIsVip =  holder.get(R.id.zongquan_fried_list_vip);
        TextView userLevel = holder.get(R.id.zongquan_fried_list_level);
        ImageView radio =  holder.get(R.id.zongquan_fried_list_radio); //选择状态

        final QinQinChiFriend userItem = sortUserGroupModels.get(groupPosition).getSortUserModels().get(childPosition);
        userName.setText(userItem.getName());
        userLevel.setText(userItem.getLevelName());
        String faceurl = userItem.getFaceUrl();
        Glide.with(mContext).load(userItem.getFaceUrl()).into(userPicture); //头像
//        if (faceurl == null || faceurl.isEmpty()) {
//            Glide.with(mContext).load(userItem.getFaceUrl()).into(userPicture); //头像
//        }
        if(showRadio){
            //选择状态
            if(userItem.isSelected()){
                radio.setImageResource(R.drawable.me_btn_dagou_selected);
            }else {
                radio.setImageResource(R.drawable.me_btn_dagou_default);
            }
        }else{
            // 不显示 radio
            radio.setVisibility(View.GONE);
        }
    }

    public boolean isShowRadio() {
        return showRadio;
    }

    public void setShowRadio(boolean showRadio) {
        this.showRadio = showRadio;
    }
}
