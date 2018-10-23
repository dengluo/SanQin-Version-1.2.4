package com.pbids.sanqin.ui.activity.zhizong;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.HomeNewsInformation;
import com.pbids.sanqin.model.entity.NewsInformation;

/**
 * Created by pbids903 on 2017/11/22.
 */

public interface ZhiZongView extends BaseView{
    final static String ONE_TYPE = "one_type";
    final static String TWO_TYPE = "two_type";
    final static String THREE_TYPE = "three_type";
    final static String FOUR_TYPE = "four_type";
    final static String FIVE_TYPE = "five_type";
    final static String SIX_TYPE = "six_type";
    final static String SEVEN_TYPE = "seven_type";
    final static String EIGHT_TYPE = "eight_type";
    final static String NINE_TYPE = "nine_type";

    public void loadingInformation(HomeNewsInformation homeNewsInformation);
    public void loadingInformation(NewsInformation information);
}
