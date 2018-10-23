package com.pbids.sanqin.ui.activity.zongquan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.view.AppToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/2/7.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:12
 * @desscribe 类描述:排行界面
 * @remark 备注:
 * @see
 */
public class ZQRankingFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    @Bind(R.id.zc_ranking_rv)
    RecyclerView zcRankingRv;

    public static ZQRankingFragment newInstance() {
        ZQRankingFragment fragment = new ZQRankingFragment();
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
        View view = inflater.inflate(R.layout.fragment_zc_rangking, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("排行",_mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
