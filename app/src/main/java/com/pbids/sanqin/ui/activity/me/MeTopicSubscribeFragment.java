package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.NewsTopicSubscribe;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.MeTopicSubscribePresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.adapter.MeTopicSubscribeAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:06
 * @desscribe 类描述:我的订阅界面
 * @remark 备注:
 * @see
 */
public class MeTopicSubscribeFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, MeTopicSubscribeView, MeTopicSubscribeAdapter.DeleteListItemListener {

	private static final String HTTP_GET_TYPE_LIST = "-6";
	private static final int RESULT_CB_CODE = 7101;

    //TopicSubscribe
    @Bind(R.id.news_topic_list_rv)
    RecyclerView newsTopicListRv;

	// adapter
	MeTopicSubscribeAdapter meTopicSubscribeAdapter;
	private List<NewsTopicSubscribe> tagsList;
	private List<NewsTopicSubscribe> surnamesList;

	MeTopicSubscribePresenter mPresenter ;

    public static MeTopicSubscribeFragment newInstance() {
        MeTopicSubscribeFragment newsTopicListFragment = new MeTopicSubscribeFragment();
        Bundle bundle = new Bundle();
        newsTopicListFragment.setArguments(bundle);
        return newsTopicListFragment;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_topic_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("我的关注",_mActivity);
    }

    public void initView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsTopicListRv.setLayoutManager(linearLayoutManager);

		meTopicSubscribeAdapter = new MeTopicSubscribeAdapter(getContext());
		meTopicSubscribeAdapter.setDeleteListItemListener(this );
		tagsList = meTopicSubscribeAdapter.getTagsList();
		surnamesList = meTopicSubscribeAdapter.getSurnamesList();
		newsTopicListRv.setLayoutManager(linearLayoutManager);
		newsTopicListRv.setAdapter(meTopicSubscribeAdapter);
		meTopicSubscribeAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
			@Override
			public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
				//点击列表事件
				NewsTopicSubscribe message = meTopicSubscribeAdapter.getMessage(groupPosition,childPosition);
				//打开webview 展示
				String pageurl = message.getUrlLink();
				if (null != pageurl && !pageurl.isEmpty()) {
					ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
					fragment.getArguments().putString("link", pageurl);
					startForResult(fragment,RESULT_CB_CODE);
				} else {
					Toast.makeText(_mActivity,"url错误",Toast.LENGTH_SHORT).show();
				}
			}
		});

    }

	@Override
	public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
		super.onFragmentResult(requestCode, resultCode, data);
		switch (requestCode){
			case RESULT_CB_CODE:
				reloadData();
				break;
		}
	}

	//加载数据
	private void reloadData(){
		tagsList.clear();
//		meTopicSubscribeAdapter.notifyDataSetChanged();
		//加载数据表  --  取话题订阅消息
		String url  = AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_TAG_FAVOR;
		HttpParams params = new HttpParams();
		params.put("uid",MyApplication.getUserInfo().getUserId());
		//请求数据
		mPresenter.getTopicSubscribeList(url,params,HTTP_GET_TYPE_LIST);
	}


	@Override
	public void onStart() {
		super.onStart();
		reloadData();
	}

    @Override
    public BasePresenter initPresenter() {
        return mPresenter = new MeTopicSubscribePresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({ R.id.news_topic_list_rv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_topic_list_rv:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
            	//返回
                pop();
                break;
			case R.id.main_right_layout:
				//点击右侧
				break;
        }
    }

	@Override
	public void onHttpSuccess(String type) {
		//列表数据请求返回
		if (tagsList.size() > 0 || surnamesList.size() > 0) {
			setContentLayoutVisible();
		} else {
			setContentLayoutGone();
			setNoDataLayoutVisible();
		}
		dismiss(); //close loading
	}

	@Override
	public void onHttpError(String type) {
		dismiss(); //close loading
		Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
	}

	//更新数据
	@Override
	public void updateTagsList(List<NewsTopicSubscribe> topicSubscribeList){
		tagsList.clear();
		tagsList.addAll(topicSubscribeList);
		// update list
		meTopicSubscribeAdapter.notifyDataSetChanged();
	}

	@Override
	public void updateSurnameList(List<NewsTopicSubscribe> surnameList) {
		surnamesList.clear();
		surnamesList.addAll(surnameList);
		// update list
		meTopicSubscribeAdapter.notifyDataSetChanged();
	}

	//删除完成后回调
	@Override
	public void onDeleteCb(DelCb cb,String type) {
		dismiss(); //close loading
		//删除回调
		if(cb.getResultCode()==DelCb.REQUEST_OK){
			NewsTopicSubscribe one = cb.getData();
			if(one.getTopicType()==NewsTopicSubscribe.TOPIC_TYPE_SURNAME){
				//删除姓氏
				surnamesList.remove(one);
				//这里要处理首页博古架上的内容更新
				//用户
				UserInfo userInfo = cb.getUserInfo();
				MainFragment mainFragment = findFragment(MainFragment.class);
				mainFragment.updateVPNameLayout(userInfo);
				//
			}else if(one.getTopicType()==NewsTopicSubscribe.TOPIC_TYPE_TAGS){
				//删除标签
				tagsList.remove(one);
			}
			//更新列表显示
			meTopicSubscribeAdapter.notifyDataSetChanged();
		}
	}

	//删除操作
	@Override
	public void deleteItem(NewsTopicSubscribe one) {
		getLoadingPop("请求中").show(); // show loading
		if(one.getTopicType()==NewsTopicSubscribe.TOPIC_TYPE_SURNAME){
			//删除姓氏
			String url  = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_DEL_NOTICE_SURNAME;
			HttpParams params = new HttpParams();
			params.put("surnameId",one.getSid());
			mPresenter.delSurnameBySid(url,params,one, "3");
		}else if(one.getTopicType()==NewsTopicSubscribe.TOPIC_TYPE_TAGS){
			//删除标签
			String url  = AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_TAG_DEL;
			HttpParams params = new HttpParams();
			params.put("uid",MyApplication.getUserInfo().getUserId());
			params.put("tid",one.getTid());
			mPresenter.delTagsById(url,params,one, "4");
		}
	}
}
