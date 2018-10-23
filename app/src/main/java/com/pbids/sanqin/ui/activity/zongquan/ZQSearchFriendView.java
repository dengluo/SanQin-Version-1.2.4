package com.pbids.sanqin.ui.activity.zongquan;

import android.support.v7.widget.RecyclerView;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiGroupUserAddAdapter;

/**
 * 类描述:搜索好友界面
 * Created by pbids903 on 2018/2/7.
 */

public interface ZQSearchFriendView extends BaseView{
	//查找
	//void findFriend(String name);

	RecyclerView getRecyclerView();
	QinQinChiGroupUserAddAdapter getFrendAdapter();

}
