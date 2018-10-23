package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.presenter.UserInformationPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.CityUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:20
 * @desscribe 类描述:籍贯选择界面
 * @remark 备注:
 * @see
 */
public class MeDescentFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MePageView,CityUtil.OnCityUtilLisenear {

    UserInformationPresenter userInformationPresenter;
    @Bind(R.id.descent_tv)
    TextView descentTv;
    @Bind(R.id.descent_layout)
    RelativeLayout descentLayout;
    @Bind(R.id.descent_bt)
    Button descentBt;
    OptionsPickerView mPickerView;

    public static MeDescentFragment newInstance() {
        MeDescentFragment fragment = new MeDescentFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_descent, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setArea();
        CityUtil cityUtil = new CityUtil(_mActivity,1);
        cityUtil.setOnCityUtilLisenear(this);
        cityUtil.doCityUtilAsyncTask();
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("我的籍贯", _mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return userInformationPresenter = new UserInformationPresenter(this);
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
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.descent_layout, R.id.descent_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.descent_layout:
                mPickerView.show();
                break;
            case R.id.descent_bt:
                if("请选择".equals(getArea())
                        || MyApplication.getUserInfo().getNativePlace().equals(getArea())){
                    Toast.makeText(_mActivity,"请选择籍贯",Toast.LENGTH_SHORT).show();
                    return;
                }
                getLoadingPop("正在提交").show();
                HttpParams params = new HttpParams();
//                params.put("surname",wacomView.getSurname());
                params.put("userId",MyApplication.getUserInfo().getUserId());
                params.put("nativePlace",getArea());
                addDisposable(userInformationPresenter.reviseUserInformation(params,MePageView.ME_REVICE_NATIVEPLACE));
                break;
        }
    }

    @Override
    public void cityCall(final ArrayList<String> provinceNameList, final ArrayList<ArrayList<String>> districtList
            , final ArrayList<ArrayList<ArrayList<String>>> countyListList, ArrayList<String> provinceIdList
            , ArrayList<ArrayList<String>> districtIdList, ArrayList<ArrayList<ArrayList<String>>> countyIdListList) {
        if (null == mPickerView) {
            OptionsPickerView.Builder builder = new OptionsPickerView.Builder(_mActivity, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    descentTv.setText(provinceNameList.get(options1)
                            + "-" + districtList.get(options1).get(options2)
                            + "-" + countyListList.get(options1).get(options2).get(options3));
                }
            });
            builder.setSubmitColor(getResources().getColor(R.color.main_remind_color));
            builder.setCancelColor(getResources().getColor(R.color.main_title_text_color));
//            builder.isDialog(true);
            mPickerView = new OptionsPickerView(builder);
            mPickerView.setPicker(provinceNameList, districtList, countyListList);
        }
    }

    @Override
    public void cityCall(ArrayList<Province> provinces) {

    }

    public String getArea(){
        String[] strings = descentTv.getText().toString().split("[-]");
        String s ="";
        for(int i=0;i<strings.length;i++){
            if(i==0){
                s +=strings[i];
            }else{
                s +=","+strings[i];
            }
        }
        return s;
    }

    public void setArea(){
        String[] strings = MyApplication.getUserInfo().getNativePlace().split("[,]");
        String s ="";
        for(int i=0;i<strings.length;i++){
            if(i==0){
                s +=strings[i];
            }else{
                s +="-"+strings[i];
            }
        }
        descentTv.setText(s);
    }


    @Override
    public void reviseSuccess(String type) {
        dismiss();
        Toast.makeText(_mActivity,"修改成功",Toast.LENGTH_SHORT).show();
        pop();
    }

    @Override
    public void reviseError(String type) {
        dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }
}
