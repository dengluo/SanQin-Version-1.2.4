package com.pbids.sanqin.ui.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.TimeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2017/12/5.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:05
 * @desscribe 类描述:系统消息详情界面
 * @remark 备注:
 * @see
 */
public class NewsSystemDetialFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear{

    @Bind(R.id.news_updata_title)
    TextView newsUpdataTitle;
    @Bind(R.id.news_updata_data)
    TextView newsUpdataData;
    @Bind(R.id.news_updata_content)
    TextView newsUpdataContent;

    public static NewsSystemDetialFragment newInstance() {
        NewsSystemDetialFragment newsSystemDetialFragment = new NewsSystemDetialFragment();
        Bundle bundle = new Bundle();
        newsSystemDetialFragment.setArguments(bundle);
        return newsSystemDetialFragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_system_message_detial, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("系统消息",_mActivity);
    }

    public void initView() {
//        meTitleText.setText("系统消息");
        Bundle bundle = getArguments();
        if(null!=bundle){
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            long time = bundle.getLong("time");

            newsUpdataTitle.setText(title);
            newsUpdataData.setText(TimeUtil.getMessageTimeFormat(time));
            newsUpdataContent.setText(content);

        }else{
        	//没有传值不显示
        	pop();
		}
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                //点击返回按钮
                pop();
                break;
        }
    }

}
