package com.netease.nim.uikit.business.contact.core.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.core.item.ContactItem;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.model.IContact;
import com.netease.nim.uikit.business.contact.core.model.UserInfoExtends;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

public class ContactHolder extends AbsContactViewHolder<ContactItem> {

    protected HeadImageView head;

    protected TextView name;

    protected TextView desc;

    protected RelativeLayout headLayout;
    //@liang
    protected ImageView ImgBusinessCooperation;
    protected TextView tvVip;
    protected TextView tvLevelName ;

    @Override
    public void refresh(ContactDataAdapter adapter, int position, final ContactItem item) {
        // contact info
        //ToDo NullPointerException
        try {
            final IContact contact = item.getContact();
            if (contact.getContactType() == IContact.Type.Friend) {
                head.loadBuddyAvatar(contact.getContactId());
            } else {
                Team team = NimUIKit.getTeamProvider().getTeamById(contact.getContactId());
                head.loadTeamIconByTeam(team);
            }
            name.setText(contact.getDisplayName());
            headLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contact.getContactType() == IContact.Type.Friend) {
                        if (NimUIKitImpl.getContactEventListener() != null) {
                            NimUIKitImpl.getContactEventListener().onAvatarClick(context, item.getContact().getContactId());
                        }
                    }
                }
            });
            //更新用户扩展信息
            updateUserExtend(contact);

            // query result
            desc.setVisibility(View.GONE);
        /*
        TextQuery query = adapter.getQuery();
        HitInfo hitInfo = query != null ? ContactSearch.hitInfo(contact, query) : null;
        if (hitInfo != null && !hitInfo.text.equals(contact.getDisplayName())) {
            desc.setVisibility(View.VISIBLE);
        } else {
            desc.setVisibility(View.GONE);
        }
        */
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.nim_contacts_item, null);

        headLayout = (RelativeLayout) view.findViewById(R.id.head_layout);
        head = (HeadImageView) view.findViewById(R.id.contacts_item_head);
        name = (TextView) view.findViewById(R.id.contacts_item_name);
        desc = (TextView) view.findViewById(R.id.contacts_item_desc);

        ImgBusinessCooperation = (ImageView) view.findViewById(R.id.contacts_business_cooperation);
        tvVip = (TextView) view.findViewById(R.id.contacts_item_vip);
        tvLevelName = (TextView) view.findViewById(R.id.contacts_levelname);
        return view;
    }

    //更新扩展信息
    private void updateUserExtend(IContact contact) {
		//没有扩展信息不显示
		this.tvVip.setVisibility(View.GONE);
		this.ImgBusinessCooperation.setVisibility(View.GONE);
		this.tvLevelName.setVisibility(View.GONE);
    	//只更新好以类型的扩展信息
    	if(contact.getContactType() == IContact.Type.Friend ){
			//取用户扩展信息
			NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(contact.getContactId());
			String extInfoStr = user.getExtension();
			if (null != extInfoStr && !extInfoStr.isEmpty()) {
				GsonBuilder gsonBuilder = new GsonBuilder();
				UserInfoExtends userInfoExtends = gsonBuilder.create().fromJson(extInfoStr, UserInfoExtends.class);
				// @liang
				if (userInfoExtends != null) {
					//vip
					if (userInfoExtends.getVip() > 0) {
						this.tvVip.setVisibility(View.VISIBLE);
						this.tvVip.setText("VIP" + userInfoExtends.getVip());
					}
					//商务合作
					if (userInfoExtends.getClanStatus() > 0) {
						this.ImgBusinessCooperation.setVisibility(View.VISIBLE);
					}
					//等级
					if (null != userInfoExtends.getLevelName() && !userInfoExtends.getLevelName().isEmpty()) {
						this.tvLevelName.setVisibility(View.VISIBLE);
						this.tvLevelName.setText(userInfoExtends.getLevelName());
					}
				}
			}
		}
	}
}
