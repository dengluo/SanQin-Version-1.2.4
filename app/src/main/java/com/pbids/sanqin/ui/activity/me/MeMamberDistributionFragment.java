package com.pbids.sanqin.ui.activity.me;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.Provice;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.presenter.MeMamberDistributionPresent;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.ChinaMapView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.NumberUtil;
import com.pbids.sanqin.utils.eventbus.OnLoadMapDistributionDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:37
 * @desscribe 类描述:家族管理-人员分布界面
 * @remark 备注:
 * @see
 */
public class MeMamberDistributionFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeMamberDistributionView {


    MeMamberDistributionPresent meMamberDistributionPresent;
    @Bind(R.id.top1_name)
    TextView top1Name;
    @Bind(R.id.top1_with)
    View top1With;
    @Bind(R.id.top1_number)
    TextView top1Number;
    @Bind(R.id.top2_name)
    TextView top2Name;
    @Bind(R.id.top2_with)
    View top2With;
    @Bind(R.id.top2_number)
    TextView top2Number;
    @Bind(R.id.top3_name)
    TextView top3Name;
    @Bind(R.id.top3_with)
    View top3With;
    @Bind(R.id.top3_number)
    TextView top3Number;
    @Bind(R.id.top4_name)
    TextView top4Name;
    @Bind(R.id.top4_with)
    View top4With;
    @Bind(R.id.top4_number)
    TextView top4Number;
    @Bind(R.id.top5_name)
    TextView top5Name;
    @Bind(R.id.top5_with)
    View top5With;
    @Bind(R.id.top5_number)
    TextView top5Number;
    @Bind(R.id.top1_interval)
    TextView top1Interval;
    @Bind(R.id.top2_interval)
    TextView top2Interval;
    @Bind(R.id.top3_interval)
    TextView top3Interval;
    @Bind(R.id.top4_interval)
    TextView top4Interval;
    @Bind(R.id.china_map)
    ChinaMapView chinaMap;
    @Bind(R.id.top1_layout)
    LinearLayout top1Layout;
    @Bind(R.id.top2_layout)
    LinearLayout top2Layout;
    @Bind(R.id.top3_layout)
    LinearLayout top3Layout;
    @Bind(R.id.top4_layout)
    LinearLayout top4Layout;
    @Bind(R.id.top5_layout)
    LinearLayout top5Layout;

    List<Province> provinceList ;

    public static MeMamberDistributionFragment newInstance() {
        MeMamberDistributionFragment fragment = new MeMamberDistributionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_mamber_distribution, container, false);
        ButterKnife.bind(this, view);
        initView();
        EventBus.getDefault().register(this);
        return view;
    }

    private void initView() {
        setContentLayoutGone();

        HttpParams params = new HttpParams();
        String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_CLAN_Distributed;
        addDisposable(meMamberDistributionPresent.submitInformation(url, params, ""));
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("家族人员分布", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public BasePresenter initPresenter() {
        return meMamberDistributionPresent = new MeMamberDistributionPresent(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getProvinceList(List<Province> provinces) {
        this.provinceList = provinces;

        if (provinces.size() > 0) {
            setContentLayoutVisible();
            setDataToMap();
        }
    }


    //事件接收
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadMapData(OnLoadMapDistributionDataEvent evt){
         if(evt.isLoadComplete()){
             setDataToMap();
             // update view
             chinaMap.invalidate();
         }
    }

    private void setDataToMap(){
        Resources resources = getResources();
        List<Province> provinces = this.provinceList;
        if(provinces==null||provinces.size()<1){
            return;
        }
        int maxNumberStr = provinces.get(0).getPeopleNum();
        float maxWith = resources.getDimension(R.dimen.dp_200);
        top1Layout.setVisibility(View.VISIBLE);
        top1Name.setText(handleProviceStr(provinces.get(0).getProvinceName()));
        top1Number.setText(NumberUtil.scalerWanForEn(maxNumberStr));
        if (provinces.size() > 1) {
            top2Layout.setVisibility(View.VISIBLE);
            top2Name.setText(handleProviceStr(provinces.get(1).getProvinceName()));
            String top2NumberStr = NumberUtil.scalerWanForEn(provinces.get(1).getPeopleNum());
            top2Number.setText(top2NumberStr);
            setTopFiveViewWith(top2With, maxNumberStr, maxWith, provinces.get(1).getPeopleNum(), resources);
            if (provinces.size() > 2) {
                top3Layout.setVisibility(View.VISIBLE);
                top3Name.setText(handleProviceStr(provinces.get(2).getProvinceName()));
                top3Number.setText(NumberUtil.scalerWanForEn(provinces.get(2).getPeopleNum()));
                setTopFiveViewWith(top3With, maxNumberStr, maxWith, provinces.get(2).getPeopleNum(), resources);
                if (provinces.size() > 3) {
                    top4Layout.setVisibility(View.VISIBLE);
                    top4Name.setText(handleProviceStr(provinces.get(3).getProvinceName()));
                    top4Number.setText(NumberUtil.scalerWanForEn(provinces.get(3).getPeopleNum()));
                    setTopFiveViewWith(top4With, maxNumberStr, maxWith, provinces.get(3).getPeopleNum(), resources);
                    if (provinces.size() > 4) {
                        top5Layout.setVisibility(View.VISIBLE);
                        top5Name.setText(handleProviceStr(provinces.get(4).getProvinceName()));
                        top5Number.setText(NumberUtil.scalerWanForEn(provinces.get(4).getPeopleNum()));
                        setTopFiveViewWith(top5With, maxNumberStr, maxWith, provinces.get(4).getPeopleNum(), resources);
                    }
                }
            }
        }
        SparseArray<Provice> proviceMap = chinaMap.getProviceMap();
        int[] colorArray = chinaMap.getColorArray();
        for (int i = 0; i < provinces.size(); i++) {
            Province province = provinces.get(i);
            int number = province.getPeopleNum();
            try {
                int color = colorArray[4];
                if(number>0 && number<=10000){
                    color = colorArray[3];
                }
                if(number>10000 && number<=100000){
                    color = colorArray[2];
                }else if(number>100000 && number<=500000){
                    color = colorArray[1];
                }else if(number>500000){
                    color = colorArray[0];
                }
                Provice provice = proviceMap.get(Integer.valueOf(province.getProvinceId()));
                provice.setDrawColor(color);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        // update view
        chinaMap.invalidate();
    }

    private String handleProviceStr(String proviceNmae){
        if(proviceNmae.indexOf("黑龙江")!=-1 || proviceNmae.indexOf("内蒙古")!=-1){
            return proviceNmae.substring(0,3);
        }
        return proviceNmae.substring(0,2);
    }

    public void setTopFiveViewWith(View view, int maxNumberStr, float maxWith, int topNumberInt, Resources resources) {
        float maxNumber = (float) maxNumberStr;
        float topNumber = (float) topNumberInt;
        float topWith = (topNumber / maxNumber) * maxWith;
        if(topWith<(int) resources.getDimension(R.dimen.dp_1)){
            topWith = (int) resources.getDimension(R.dimen.dp_1);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) topWith, (int) resources.getDimension(R.dimen.dp_10));
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
    }

}
