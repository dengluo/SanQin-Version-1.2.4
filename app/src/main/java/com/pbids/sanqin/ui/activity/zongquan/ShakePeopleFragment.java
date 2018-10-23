package com.pbids.sanqin.ui.activity.zongquan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.OnItemClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.contact.activity.UserProfileActivity;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.recyclerview.adapter.ShakePeopleAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/3/23.
 */

public class ShakePeopleFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

    ShakePeopleAdapter shakePeopleAdapter;

    @Bind(R.id.shake_people_rv)
    RecyclerView shakePeopleRv;

    public static ShakePeopleFragment newInstance() {
        ShakePeopleFragment fragment = new ShakePeopleFragment();
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
        View view = inflater.inflate(R.layout.fragment_shake_people, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        String userJson = getArguments().getString("user_json","");
        JsonArray data = new JsonParser().parse(userJson).getAsJsonArray();
        final List<UserInfo> userInfos = new ArrayList<>();
        for(int i =0;i<data.size();i++){
            UserInfo user = new GsonBuilder().create().fromJson(data.get(i).toString(),UserInfo.class);
            userInfos.add(user);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shakePeopleAdapter = new ShakePeopleAdapter(_mActivity,userInfos);
        shakePeopleAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                final String account = userInfos.get(childPosition).getUserId()+"";
                //Toast.makeText(_mActivity, "account", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(_mActivity, UserProfileActivity.class);
                intent.putExtra(Extras.EXTRA_ACCOUNT, account);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                /*DialogMaker.showProgressDialog( _mActivity, null, false);
                NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {
                    @Override
                    public void onResult(boolean success, NimUserInfo result, int code) {
                        DialogMaker.dismissProgressDialog();
                        if (success) {
                            if (result == null) {
                                EasyAlertDialogHelper.showOneButtonDiolag(_mActivity , R.string.user_not_exsit,
                                        R.string.user_tips, R.string.ok, false, null);
                            } else {
                                UserProfileActivity.start(_mActivity, account);
                            }
                        } else if (code == 408) {
                            Toast.makeText(_mActivity, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else if (code == ResponseCode.RES_EXCEPTION) {
                            Toast.makeText(_mActivity, "on exception", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(_mActivity, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            }
        });
       /* shakePeopleAdapter.setOnItemClickListener(new OnItemClickListener<ShakePeopleAdapter>() {
            @Override
            public void onItemChildClick(ShakePeopleAdapter adapter, View view, int position) {

                final String account = userInfos.get(position).getUserId()+"";
                DialogMaker.showProgressDialog( _mActivity, null, false);
                NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {
                    @Override
                    public void onResult(boolean success, NimUserInfo result, int code) {
                        DialogMaker.dismissProgressDialog();
                        if (success) {
                            if (result == null) {
                                EasyAlertDialogHelper.showOneButtonDiolag(_mActivity , R.string.user_not_exsit,
                                        R.string.user_tips, R.string.ok, false, null);
                            } else {
                                UserProfileActivity.start(_mActivity, account);
                            }
                        } else if (code == 408) {
                            Toast.makeText(_mActivity, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                        } else if (code == ResponseCode.RES_EXCEPTION) {
                            Toast.makeText(_mActivity, "on exception", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(_mActivity, "on failed:" + code, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });*/
        shakePeopleRv.setLayoutManager(layoutManager);
        shakePeopleRv.setAdapter(shakePeopleAdapter);
    }

    @Override
    public void onSupportVisible() {
        if(shakePeopleAdapter!=null){
            shakePeopleRv.setAdapter(shakePeopleAdapter);
        }
        super.onSupportVisible();
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        //toolBar.setLeftArrowCenterTextTitle("我摇到的人", _mActivity);
        toolBar.setLeftArrowCenterTextTitleRightText("我摇到的人", "清空", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                shakePeopleAdapter.getUserInfos().clear();
                shakePeopleAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
