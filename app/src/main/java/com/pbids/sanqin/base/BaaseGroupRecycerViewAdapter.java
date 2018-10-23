package com.pbids.sanqin.base;

import android.content.Context;

import com.pbids.sanqin.R;
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
public abstract class BaaseGroupRecycerViewAdapter<T extends BaaseRecycerViewListGroup> extends GroupedRecyclerViewAdapter {

	protected List<T> gLists = null;
	protected Context mContext;

	public BaaseGroupRecycerViewAdapter(Context context, List<T> glist){
		super(context);
		this.mContext = context;
		this.gLists = glist;
	}

	@Override
	public int getGroupCount() {
		return gLists == null?0:gLists.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		T groupItem = gLists.get(groupPosition);
		if(groupItem==null) return 0;
		return groupItem.getListSize();
	}

	public List<T> getList() {
		return gLists;
	}

	/*	@Override
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

	}*/

}
