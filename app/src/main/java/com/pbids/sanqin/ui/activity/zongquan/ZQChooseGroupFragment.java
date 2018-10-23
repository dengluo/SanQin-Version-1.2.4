package com.pbids.sanqin.ui.activity.zongquan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.WarpLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/2/23.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:11
 * @desscribe 类描述:选择分组界面
 * @remark 备注:
 * @see
 */
public class ZQChooseGroupFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    @Bind(R.id.zq_group_name_et)
    EditText zqGroupNameEt;
    @Bind(R.id.zq_group_warplayout)
    WarpLinearLayout zqGroupWarplayout;

    List<Button> buttons;

    public static ZQChooseGroupFragment newInstance() {
        ZQChooseGroupFragment fragment = new ZQChooseGroupFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zq_choose_group, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        buttons = new ArrayList<>();
        for(int i=0;i<7;i++){
            View view1 = LayoutInflater.from(_mActivity).inflate(R.layout.item_zq_choose_group,null,false);
            final Button button = (Button) view1.findViewById(R.id.item_zq_group_bt);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i<buttons.size();i++){
                        Button button1 = buttons.get(i);
                        button1.setSelected(false);
                    }
                    button.setSelected(true);
                }
            });
            buttons.add(button);
            zqGroupWarplayout.addView(view1);
        }
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftTextCenterTextTitleRightText("取消","所属组","完成",_mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
