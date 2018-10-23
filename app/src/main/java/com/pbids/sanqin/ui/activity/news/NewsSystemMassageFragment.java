package com.pbids.sanqin.ui.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.entity.SystemMessage;
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
 * @author 巫哲豪
 * @date on 2018/3/2 15:06
 * @desscribe 类描述:系统消息界面
 * @remark 备注:
 * @see
 */
public class NewsSystemMassageFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

	//背景执行处理在显示不发通知
	public static Boolean isBackground = true;

	public static final int REQUEST_CODE = 7001 ;

    @Bind(R.id.news_system_messge_rv)
    RecyclerView newsSystemMessgeRv;

    // adapter
    NewsSystemMessageAdapter newsSystemMessageAdapter ;
	List<SystemMessage>  messageList ;

	private SystemMessageManager sysMessManager ;

    public static NewsSystemMassageFragment newInstance() {
        NewsSystemMassageFragment newsSystemMassageFragment = new NewsSystemMassageFragment();
        Bundle bundle = new Bundle();
        newsSystemMassageFragment.setArguments(bundle);
        return newsSystemMassageFragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_system_message_list, container, false);
        ButterKnife.bind(this, view);
		// eventbux
		EventBus.getDefault().register(this);
        initView();
        return view;
    }

    // 设置顶部栏
    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("系统消息",_mActivity);
    }


	public void initView() {
		sysMessManager = SystemMessageManager.instance();

//        meTitleText.setText("系统消息");
		newsSystemMessageAdapter = new NewsSystemMessageAdapter(getContext());
		messageList = newsSystemMessageAdapter.getMessageList();
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		newsSystemMessgeRv.setLayoutManager(linearLayoutManager);
		newsSystemMessgeRv.setAdapter(newsSystemMessageAdapter);
		newsSystemMessageAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
			@Override
			public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {

				//点击消息 展示内容
				SystemMessage message = newsSystemMessageAdapter.getMessage(groupPosition, childPosition);

				//先修改状态标记
				message.setIsread(true);
				//SystemMessageManager.update(getContext(), message);
				sysMessManager.update(message);
				//发出变动通知
				SystemMessageHandleEvent evt = new SystemMessageHandleEvent();
				evt.setMsgTpme(SystemMessageManager.MSG_TYPE_SYSTEM);
				evt.setType(SystemMessageManager.TYPE_SYSTEM);
				EventBus.getDefault().post(evt);

				//启动详情页
				NewsSystemDetialFragment fragment = NewsSystemDetialFragment.newInstance();
				//传参数启动
				fragment.getArguments().putString("title", message.getTitle());
				fragment.getArguments().putString("content", message.getContent());
				fragment.getArguments().putLong("time", message.getTime());
				fragment.getArguments().putLong("id", message.get_id());
//                fragment.getArguments().putLong("id", 1);
//				start(fragment);

				startForResult(fragment, REQUEST_CODE);
			}
		});
	}

    private void reloadData(){
		messageList.clear();
		//加载数据表
//		List<SystemMessage> list = new ArrayList<>();
//		SystemMessage systemMessage = new SystemMessage();
//		systemMessage.setTitle("sdfdsf");
//		systemMessage.setContent("sdfsdfds");
//		list.add(systemMessage);
//		List<SystemMessage> messLists = list;
		List<SystemMessage> messLists = SystemMessageManager.query(getContext(),SystemMessageManager.TYPE_SYSTEM);
		messageList.addAll(messLists);
		newsSystemMessageAdapter.notifyDataSetChanged();
	}

	@Override
	public void onStart() {
		super.onStart();
		reloadData();
		isBackground = false;
		//如果有广播要删除广播显示
		//移除标记为id的通知 (只是针对当前Context下的所有Notification)
		MyApplication.systemMessageNotificationAdmain.clear();
	}
	//通知栏点击显示消息窗口
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onClickNotificationEvent(SystemMessageHandleEvent mssageHandle){
//    	String m = notificationEvent.getNotifyType();
		if(mssageHandle.getType()==SystemMessageManager.TYPE_SYSTEM){
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
                //点击左边返回按钮
                pop();
                break;
        }
    }

}
