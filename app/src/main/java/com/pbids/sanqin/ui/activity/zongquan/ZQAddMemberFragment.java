package com.pbids.sanqin.ui.activity.zongquan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.QinQinChiGroupUserAddAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.letter.PinyinComparator;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.utils.zxing.TestDbUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/2/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:11
 * @desscribe 类描述:添加成员界面
 * @remark 备注:
 * @see
 */
public class ZQAddMemberFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear{


    @Bind(R.id.zongquan_friends_list)
    RecyclerView rvFriendsList;  //好友列表

    //好友列表 adapter
    private QinQinChiGroupUserAddAdapter mUserAddAdapter;
    private List<QinQinChiFriend> mFriendList;
    List<QinQinChiFriendGroup> friendGroupModels;

    public static ZQAddMemberFragment newInstance(){
        ZQAddMemberFragment fragment = new ZQAddMemberFragment();
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
        View view = inflater.inflate(R.layout.fragment_zq_add_number, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        //赋值测试用户数据
        mFriendList = TestDbUtil.fillFrendData(getResources().getStringArray(R.array.date));
        for(  QinQinChiFriend item : mFriendList  ){
            item.setSelected(false); //清除选择
        }

        friendGroupModels = PinyinComparator.groupTo(mFriendList);
        mUserAddAdapter = new QinQinChiGroupUserAddAdapter(_mActivity, friendGroupModels);

        mUserAddAdapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
                //Log.i("wzh", "onChildClick");
                QinQinChiFriend userItem = friendGroupModels.get(groupPosition).getSortUserModels().get(childPosition);
                //反选或取消
                userItem.setSelected(!userItem.isSelected());
                //更新list
                mUserAddAdapter.notifyDataSetChanged();
            }
        });

        //init adapter
        LinearLayoutManager mFriendManager = new LinearLayoutManager(_mActivity);
        mFriendManager.setOrientation(LinearLayoutManager.VERTICAL);
        //初始化好友列表
        rvFriendsList.setLayoutManager(mFriendManager);
        rvFriendsList.setAdapter(mUserAddAdapter);
        rvFriendsList.setNestedScrollingEnabled(false);
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitleRightText("添加成员","完成",_mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                //finsh 完成
                //取出已选择的用户
                ArrayList<String> selFriend = new ArrayList<>();
                for(  QinQinChiFriend item : mFriendList  ){
                    if(item.isSelected()){
                        selFriend.add(item.getId()+"");
                    }
                }
                if(selFriend.size()<1){
                    Toast.makeText(_mActivity, "您没有选择好友，请选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                //回传选择的结果
                Bundle bundle= new Bundle();
                bundle.putString("type","1");
                bundle.putStringArrayList("list",selFriend);
                setFragmentResult(ZQMemberManagerFragment.CODE_SELECT_USER,bundle);
                pop();
                break;
        }
    }
}
