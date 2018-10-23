package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.OnItemClickListener;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.utils.ButtonUtil;

import java.util.List;

/**
 * Created by pbids903 on 2018/3/23.
 */

public class ShakePeopleAdapter extends GroupedRecyclerViewAdapter {

    private List<UserInfo> userInfos;

    Context context;
    public ShakePeopleAdapter(Context context,List<UserInfo> userInfos) {
        super(context);
        this.context = context;
        this.userInfos = userInfos;
    }

    public List<UserInfo> getUserInfos(){
        return userInfos;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return userInfos==null?0:userInfos.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return 0;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_shake_people;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, final int childPosition) {
        HeadImageView head = holder.get(R.id.shake_people_item_head);
        ImageView cooperation = holder.get(R.id.shake_people_cooperation);
        TextView name = holder.get(R.id.shake_people_item_name);
        TextView levelname = holder.get(R.id.shake_people_levelname);
        TextView vip = holder.get(R.id.shake_people_item_vip);
//        Button bt = holder.get(R.id.shake_people_bt);

        UserInfo userInfo = userInfos.get(childPosition);

        new CommonGlideInstance().setImageViewBackgroundForUrl(context,
                head,userInfo.getFaceUrl(),R.drawable.me_avatar_moren_default,R.drawable.me_avatar_moren_default);
        if(userInfo.getClanStatus()==0){
            cooperation.setVisibility(View.INVISIBLE);
        }else if(userInfo.getClanStatus()==1){
            cooperation.setVisibility(View.VISIBLE);
        }

        switch (userInfo.getVip()){
            case 0:
                vip.setVisibility(View.INVISIBLE);
                break;
            case 1:
                vip.setVisibility(View.VISIBLE);
                vip.setText("VIP"+userInfo.getVip());
                break;
            case 2:
                vip.setVisibility(View.VISIBLE);
                vip.setText("VIP"+userInfo.getVip());
                break;
        }

        name.setText(userInfo.getName());
        levelname.setText(userInfo.getLevelName());

        boolean isMyFriend = NIMClient.getService(FriendService.class).isMyFriend(""+userInfo.getUserId());
//
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickListener.onItemChildClick(ShakePeopleAdapter.this,view,childPosition);
//            }
//        });
//
//        if(isMyFriend){
//            ButtonUtil.setOnClickFalse(bt);
//        }else{
//            ButtonUtil.setOnClickTrue(bt);
//        }
    }

    OnItemClickListener<ShakePeopleAdapter> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<ShakePeopleAdapter> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
