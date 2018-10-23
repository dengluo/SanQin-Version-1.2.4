package com.pbids.sanqin.ui.activity.zongquan;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiFriendAdapter;

/**
 * 类描述:宗圈首页界面
 * Created by pbids903 on 2018/2/7.
 */

public interface ZongQuanView extends BaseView{

	QinQinChiFriendAdapter getFriendAdapter();

	void updateFrendList();

}
