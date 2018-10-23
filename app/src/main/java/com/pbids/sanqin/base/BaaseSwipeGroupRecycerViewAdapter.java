package com.pbids.sanqin.base;

import android.content.Context;

import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.ui.activity.news.NewsIMFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.SwipeGroupedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:44
 * @desscribe 类描述:消息列表设配器
 * @remark 备注:
 * @see NewsIMFragment
 */
public abstract class BaaseSwipeGroupRecycerViewAdapter<T extends BaaseRecycerViewListGroup> extends SwipeGroupedRecyclerViewAdapter {

	protected List<T> gLists = null;
	protected Context mContext;

	protected int headerLayoutId,footerLayoutId,childLayoutId;

	// delete listener
	protected DeleteListItemListener deleteItemListener;

	public BaaseSwipeGroupRecycerViewAdapter(Context context, List<T> glist){
		super(context);
		this.mContext = context;
		this.gLists = glist;
	}

    public List<T> getList() {
        return gLists;
    }

    public void setList(List<T> gLists) {
        this. gLists = gLists ;
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

	// 删除事件
	public void setDeleteItemListener(DeleteListItemListener listener){
		deleteItemListener = listener;
	}

	// 删除成员
	public interface DeleteListItemListener<T> {
		void deleteItem( T item , int groupPosition, int childPosition );
	}

}
