package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;
import com.pbids.sanqin.ui.activity.news.NewsIMFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.List;

/**
 * Created by pbids903 on 2017/12/5.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:44
 * @desscribe 类描述:消息列表设配器
 * @remark 备注:
 * @see NewsIMFragment
 */
public class NewsFriendsAdapter extends GroupedRecyclerViewAdapter {

	private List<QinQinChiFriendGroup> sortUserGroupModels = null;
	private Context mContext;

	public NewsFriendsAdapter(Context context, List<QinQinChiFriendGroup> sortUserGroupModels){
		super(context);
		this.mContext = context;
		this.sortUserGroupModels = sortUserGroupModels;
	}

/*    @Override
    public NewsFriendsAdapter.FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_friends_message_item,parent,false);
        FriendsViewHolder friendsViewHolder = new FriendsViewHolder(view);
        return friendsViewHolder;
    }*/

/*    @Override
    public int getItemCount() {
        return 7;
    }

	@Override
	public int getGroupCount() {
		return 0;
	}*/

	@Override
	public int getGroupCount() {
		return sortUserGroupModels == null?0:sortUserGroupModels.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		QinQinChiFriendGroup groupItem = sortUserGroupModels.get(groupPosition);
		if(groupItem==null) return 0;
		if(groupItem.getSortUserModels()==null) return 0;
		return groupItem.getSortUserModels().size();
	}

	@Override
	public boolean hasHeader(int groupPosition) {
		return false;
	}

	@Override
	public boolean hasFooter(int groupPosition) {
		return false;
	}

	@Override
	public int getHeaderLayout(int viewType) {
		return 0;
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
	public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

	}

	@Override
	public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

	}

	@Override
	public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
		QinQinChiFriendGroup groupItem = sortUserGroupModels.get(groupPosition);
		QinQinChiFriend friend =  sortUserGroupModels.get(groupPosition).getSortUserModels().get(childPosition);
		RecentContact recent = friend.getRecentContact();
		//view_donate_records
		TextView name = (TextView) holder.get(R.id.zongquan_fried_list_name);
		TextView tvOnline = (TextView) holder.get(R.id.tv_online_state);
		TextView tvMessage = (TextView) holder.get(R.id.tv_message);
		TextView tvDatetime = (TextView) holder.get(R.id.tv_date_time);

		DropFake unreadNumberTip =  (DropFake) holder.get(R.id.unread_number_tip);

		name.setText(friend.getName());
		tvOnline.setText("state");
		tvMessage.setText(recent.getContent());
		tvDatetime.setText(recent.getTime()+"");

		int cnum = recent.getUnreadCount();
		if(cnum>0){
			unreadNumberTip.setVisibility(View.VISIBLE);
			unreadNumberTip.setText(cnum+"");
		}else {
			unreadNumberTip.setVisibility(View.GONE);
		}

	}

/*	class FriendsViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView level;
        TextView time;
        TextView content;
        TextView number;

        public FriendsViewHolder(View itemView) {
            super(itemView);
        }
    }*/

}
