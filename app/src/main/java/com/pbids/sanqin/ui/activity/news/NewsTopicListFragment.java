package com.pbids.sanqin.ui.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.entity.SystemMessage;
import com.pbids.sanqin.ui.activity.me.MeTopicSubscribeFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.adapter.NewsSystemMessageAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.eventbus.SystemMessageHandleEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2017/12/5.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:06
 * @desscribe 类描述:公众话题订阅界面
 * @remark 备注:
 * @see
 */
public class NewsTopicListFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    //背景执行处理在显示不发通知
    public static Boolean isBackground = true;
	public static final int REQUEST_CODE = 8001 ;

    //TopicSubscribe
    @Bind(R.id.news_topic_list_rv)
    RecyclerView newsTopicListRv;

	// adapter
	NewsSystemMessageAdapter newsSystemMessageAdapter ;
	List<SystemMessage> messageList ;

	private SystemMessageManager sysMessManager ;

    public static NewsTopicListFragment newInstance() {
        NewsTopicListFragment newsTopicListFragment = new NewsTopicListFragment();
        Bundle bundle = new Bundle();
        newsTopicListFragment.setArguments(bundle);
        return newsTopicListFragment;
    }


    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_topic_list, container, false);
        ButterKnife.bind(this, view);
		// eventbux
		EventBus.getDefault().register(this);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
//        toolBar.setLeftArrowCenterTextTitleRightText("公众话题订阅","我的订阅",_mActivity);
        toolBar.setLeftArrowCenterTextTitle("公众话题订阅",_mActivity);
    }

    public void initView(){
		sysMessManager = SystemMessageManager.instance();
//        meTitleText.setText("公众话题订阅");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsTopicListRv.setLayoutManager(linearLayoutManager);

		newsSystemMessageAdapter = new NewsSystemMessageAdapter(getContext());
		messageList = newsSystemMessageAdapter.getMessageList();
		newsTopicListRv.setLayoutManager(linearLayoutManager);
		newsTopicListRv.setAdapter(newsSystemMessageAdapter);
		newsSystemMessageAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
			@Override
			public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {

				//点击消息 展示内容
				SystemMessage message = newsSystemMessageAdapter.getMessage(groupPosition,childPosition);

				//先修改状态标记
				message.setIsread(true);
				//SystemMessageManager.update(getContext(),message);
				sysMessManager.update(message);
				//发出变动通知
				SystemMessageHandleEvent evt= new SystemMessageHandleEvent();
				evt.setMsgTpme(SystemMessageManager.MSG_TYPE_SYSTEM);
				evt.setType(SystemMessageManager.TYPE_TOPIC);
				EventBus.getDefault().post(evt);

				//打开webview 展示
				String pageurl = message.getUrl();
				ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
				fragment.getArguments().putString("link",pageurl);
//				((MainFragment) getParentFragment()).start(fragment);
				startForResult(fragment,REQUEST_CODE);
			}
		});

		//侧滑删除事件
		newsSystemMessageAdapter.setSideslipDelelteListener(new NewsSystemMessageAdapter.SideslipDelelte() {
			@Override
			public void sideslipDelete(View view,SystemMessage message) {
				Log.i("删除了", "删除的信息："+message.getTitle());
				SystemMessageManager.del(getContext(),message);
				messageList.remove(message);
				newsSystemMessageAdapter.notifyDataSetChanged();
			}
		});

    }

	//加载数据
	private void reloadData(){
		messageList.clear();
		//加载数据表  --  取话题订阅消息
		List<SystemMessage> messLists = SystemMessageManager.query(getContext(),SystemMessageManager.TYPE_TOPIC);
		messageList.addAll(messLists);
		newsSystemMessageAdapter.notifyDataSetChanged();
}

	//通知栏点击显示消息窗口
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onClickNotificationEvent(SystemMessageHandleEvent mssageHandle){
		if(mssageHandle.getType()==SystemMessageManager.TYPE_TOPIC){
			reloadData();
		}
	}

	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		super.onFragmentResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_CODE){
			isBackground = false;
			reloadData();
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		reloadData();
		isBackground = false;
		//如果有广播要删除广播显示
		//移除标记为id的通知 (只是针对当前Context下的所有Notification)
		MyApplication.newsTopicSubscribeNotificationAdmain.clear();
	}

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
		isBackground = true;
		EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
            	//返回
                pop();
                break;
			case R.id.main_right_layout:
				//我的订阅
				start(MeTopicSubscribeFragment.newInstance());
				break;
        }
    }
}
