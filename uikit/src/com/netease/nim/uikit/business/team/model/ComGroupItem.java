package com.netease.nim.uikit.business.team.model;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.viewholder.AbsContactViewHolder;
import com.netease.nim.uikit.common.CommonGroup;
import com.netease.nimlib.sdk.team.model.Team;


import java.util.ArrayList;
import java.util.List;

import com.netease.nim.uikit.R;

public class ComGroupItem  extends AbsContactItem  {

    //自定义组
    public static final int TYPE_COM_GROUP_ITEM = 2024 ;

    //三亲
    public static final ComGroupItem GROUP_SANQIN = new ComGroupItem (CommonGroup.GROUP_SANQIN);
    //家族
    public static final ComGroupItem GROUP_JIAZU = new ComGroupItem (CommonGroup.GROUP_JIAZU);
    //宗亲会
    public static final ComGroupItem GROUP_ZONGQIN = new ComGroupItem (CommonGroup.GROUP_ZONGQIN);

    private String grounpame = "";

    public ComGroupItem(String gname){
        this.grounpame = gname;
    }

    @Override
    public int getItemType() {
        return TYPE_COM_GROUP_ITEM;
    }

    @Override
    public String belongsGroup() {
        return null;
    }


    public static final class ComGroupViewHolder extends AbsContactViewHolder<ComGroupItem> {
        private ImageView image;
        private TextView tv_name;
        private TextView unreadNum;


        @Override
        public View inflate(LayoutInflater inflater) {
            View view = inflater.inflate(R.layout.com_group_contacts_item, null);
            this.image = (ImageView) view.findViewById(R.id.contacts_item_head);
            this.tv_name = (TextView) view.findViewById(R.id.contacts_item_name);
            this.unreadNum = (TextView) view.findViewById(R.id.tab_new_msg_label);
            return view;
        }

        @Override
        public void refresh(ContactDataAdapter contactAdapter, int position,  ComGroupItem item) {
            if (item == GROUP_SANQIN) {
                tv_name.setText(CommonGroup.GROUP_SANQIN);
                image.setImageResource(R.drawable.nim_avatar_group);
            } else if (item == GROUP_JIAZU) {
                tv_name.setText(CommonGroup.GROUP_JIAZU);
                image.setImageResource(R.drawable.nim_avatar_group);
            } else if (item == GROUP_ZONGQIN) {
                tv_name.setText(CommonGroup.GROUP_ZONGQIN);
                image.setImageResource(R.drawable.nim_avatar_group);
            }
        }

        private void updateUnreadNum(int unreadCount) {
            // 2.*版本viewholder复用问题
            if (unreadCount > 0 ) {
                unreadNum.setVisibility(View.VISIBLE);
                unreadNum.setText("" + unreadCount);
            } else {
                unreadNum.setVisibility(View.GONE);
            }
        }
    }

    public String getGrounpame() {
        return grounpame;
    }

    public void setGrounpame(String grounpame) {
        this.grounpame = grounpame;
    }

    //自定群组列表
    public static List<AbsContactItem> provide() {
        List<AbsContactItem> curlist = new ArrayList<>();
        //一般群组列表
        List<Team> teams = CommonGroup.getNormalGroups();
        if (!CommonGroup.hasGroupName(CommonGroup.GROUP_SANQIN, teams)) {
            curlist.add(GROUP_SANQIN );
        }
        if (!CommonGroup.hasGroupName(CommonGroup.GROUP_JIAZU, teams)) {
            curlist.add(GROUP_JIAZU );
        }
        if (!CommonGroup.hasGroupName(CommonGroup.GROUP_ZONGQIN, teams)) {
            curlist.add(GROUP_ZONGQIN );
        }
        return curlist;

    }
}
