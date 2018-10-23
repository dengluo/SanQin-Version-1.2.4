package com.pbids.sanqin.ui.activity.zhizong;

import android.support.v4.app.FragmentManager;

import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.GenealogyInformation;

/**
 * caiguoliang
 * 我的族谱
 */

public interface GenealogyView extends BaaseView {

    //void updataInformation(GenealogyInformation genealogyInformation);

    void loadData(GenealogyInformation genealogyInformation);

    GenealogyInformation getData();

    FragmentManager getFM();

}
