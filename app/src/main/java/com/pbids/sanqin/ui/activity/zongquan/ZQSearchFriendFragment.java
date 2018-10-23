package com.pbids.sanqin.ui.activity.zongquan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.contact.activity.UserProfileActivity;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.presenter.ZQSearchFriendPresenter;
import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiFriendAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiGroupUserAddAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/2/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:13
 * @desscribe 类描述:搜索好友界面
 * @remark 备注:
 * @see
 */
public class ZQSearchFriendFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,ZQSearchFriendView{


    @Bind(R.id.news_title_search_bt)
    Button btn_search;  //搜索按钮
    @Bind(R.id.zq_search_et)
    EditText et_zqSearch ; //搜索文本
    @Bind(R.id.zongquan_friends_list)
    RecyclerView rv_friends;

    //
    private String lastKeyword = "";

    ZQSearchFriendPresenter mZQSearchFriendPresenter ;

    //分组列表 adapter
    private QinQinChiGroupUserAddAdapter userAddAdapter;


    public static ZQSearchFriendFragment newInstance(){
        ZQSearchFriendFragment fragment = new ZQSearchFriendFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return mZQSearchFriendPresenter = new ZQSearchFriendPresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zq_search_friend, container, false);
        ButterKnife.bind(this, view);

        //限制输入内容
        InputFilter switchFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String regEx = "^[\\u4e00-\\u9fa5a-zA-Z0-9]*$";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(source.toString());
                if(m.matches()){
                    return source.toString();
                }else {
                    return "";
                }
            }
        };
        et_zqSearch.setFilters(new InputFilter[]{switchFilter,new InputFilter.LengthFilter(40)});

        initView();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private void initView() {
        // recyclear list
        userAddAdapter = new QinQinChiGroupUserAddAdapter(_mActivity, mZQSearchFriendPresenter.getQinQinChiFriendGroups());
		userAddAdapter.setShowRadio(false); // 不显示radio
        userAddAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
//                Log.i("wzh", "onChildClick");
                if (null != rv_friends) {
					QinQinChiFriend friend = mZQSearchFriendPresenter.getQinQinChiFriendGroups().get(groupPosition).getSortUserModels().get(childPosition);
					ZQAddFriendFragment.addFriend(friend.getUserId()+"",_mActivity);
                }
            }
        });
		rv_friends.setLayoutManager(new LinearLayoutManager(_mActivity));//注意 不加这个list 不显示
        rv_friends.setAdapter(userAddAdapter);

    }

	@Override
	public void onStop() {
		super.onStop();
		//隐藏键盘
		hideSoftInput();
	}

    @Override
    public RecyclerView getRecyclerView() {
        return rv_friends;
    }
	@Override
	public QinQinChiGroupUserAddAdapter getFrendAdapter() {
		return userAddAdapter;
	}

    @OnClick({R.id.news_title_search_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_title_search_bt:
                //点击搜索按钮
                if(null!=et_zqSearch){
                    final String username = et_zqSearch.getText().toString();
                    if (TextUtils.isEmpty(username)) {
                        Toast.makeText(_mActivity, R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
                    } else if (username.equals(MyApplication.getUserInfo().getName())) {
                        Toast.makeText(_mActivity, R.string.add_friend_self_tip, Toast.LENGTH_SHORT).show();
                    } else {
                        //mZQSearchFriendPresenter.query(account.toLowerCase());
                        if(username.equals(lastKeyword)){
                            return;
                        }
                        lastKeyword = username;
                        getLoadingPop("loading...").show();
                        //查找
                        mZQSearchFriendPresenter.query(username,"-3");
                    }
                }
                break;
        }
    }
    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("搜索列表",_mActivity);
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
    public void onHttpSuccess(String type) {
		dismiss();
		if( type.equals("-3")){
			//列表数据请求返回
			if(mZQSearchFriendPresenter.hasData()){
				setContentLayoutVisible();

			}else{
				setNoDataLayoutVisible();
				Toast.makeText(_mActivity, "查询无记录", Toast.LENGTH_SHORT).show();
			}
		}
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
		Toast.makeText(_mActivity, type, Toast.LENGTH_SHORT).show();
    }
}
