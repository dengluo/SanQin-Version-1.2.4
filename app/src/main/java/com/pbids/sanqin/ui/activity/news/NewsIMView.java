package com.pbids.sanqin.ui.activity.news;

import android.app.Activity;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.ui.adapter.NewsFriendsAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.NewsSearchAdapter;

/**
 * view
 * 模块：消息
 * Created by caiguoliang on 2017/12/11.
 */

public interface NewsIMView extends BaseView{

	NewsFriendsAdapter getNewsFriendsAdapter ();

	//更新显示最近联系人列表
	void showRecentList();

	//	Activity getActivity();
	void refreshViewHolderByIndex(final int index);
}