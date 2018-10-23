package com.pbids.sanqin.ui.activity.news;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.VersionInfo;
import com.pbids.sanqin.ui.recyclerview.adapter.NewsSearchAdapter;

/**
 * view
 * Created by caiguoliang on 2017/12/11.
 */

public interface NewsSearchView extends BaseView{

    NewsSearchAdapter getNewsSearchAdapter();

    void ListLoadDataNum(int loadNum);
}