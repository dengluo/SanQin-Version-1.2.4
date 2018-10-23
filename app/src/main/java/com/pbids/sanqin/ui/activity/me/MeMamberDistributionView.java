package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.Province;

import java.util.List;

/**
 * Created by pbids903 on 2017/12/28.
 */

public interface MeMamberDistributionView extends BaseView{
    void getProvinceList(List<Province> provinces);
}
