package com.pbids.sanqin.ui.activity.zongquan;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.core.model.UserInfoExtends;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.base.BaaseRecyclerViewHolder;

public class FriendViewHolder extends BaaseRecyclerViewHolder {

    private int lastUnreadCount = 0;

    protected FrameLayout portraitPanel;
    protected HeadImageView imgHead;
    protected TextView tvNickname;


    protected TextView tvLevelname; //等级名称
    protected TextView tvVip; //等级名称
    protected ImageView tvBusinessCooperation; //商务合作

    protected View bottomLine;
    protected View topLine;

    private ImageView imgUnreadExplosion;

    protected TextView tvOnlineState;

    private LinearLayout contentLayout;


    public FriendViewHolder(View itemView) {
        super(itemView);
        initView();
    }

    public void initView(){
        this.portraitPanel =  getView(R.id.portrait_panel);
        this.imgHead =  getView(R.id.img_head);
        this.tvNickname =  getView(R.id.tv_nickname);
        this.imgUnreadExplosion = getView(R.id.unread_number_explosion);
        this.bottomLine =  getView(R.id.bottom_line);
        this.topLine =  getView(R.id.top_line);
        this.tvOnlineState =  getView(R.id.tv_online_state);
        this.tvLevelname =  getView(R.id.tv_levelname);//
        this.tvVip =  getView(R.id.tv_item_vip);//
        this.tvBusinessCooperation =  getView(R.id.tv_business_cooperation);//
        this.contentLayout = getView(R.id.content_layout);//

        //holder.addOnClickListener(R.id.unread_number_tip);


    }



    // refresh 更新信息
    public void refresh( NimUserInfo nimUserInfo, final int position) {
        if (position < 1) {
            topLine.setVisibility(View.VISIBLE);
        }
        // 设置头像
        loadPortrait(nimUserInfo);
        //名称
        updateNickLabel(UserInfoHelper.getUserTitleName(nimUserInfo.getAccount(),  SessionTypeEnum.P2P));
        //扩展信息
        updateUserExtend(nimUserInfo);
        //在线信息
        updateOnlineState(nimUserInfo);
    }

    //更新扩展信息
	private void updateUserExtend(NimUserInfo user) {
        //没有扩展信息不显示
        this.tvVip.setVisibility(View.GONE);
        this.tvBusinessCooperation.setVisibility(View.GONE);
        this.tvLevelname.setVisibility(View.GONE);

        //取用户扩展信息
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
                    this.tvBusinessCooperation.setVisibility(View.VISIBLE);
                }
                //等级
                if (null != userInfoExtends.getLevelName() && !userInfoExtends.getLevelName().isEmpty()) {
                    this.tvLevelname.setVisibility(View.VISIBLE);
                    this.tvLevelname.setText(userInfoExtends.getLevelName());
                }
            }
        }
	}

    // 设置头像
    protected void loadPortrait(NimUserInfo nimUserInfo) {
        imgHead.loadBuddyAvatar(nimUserInfo.getAccount());
    }



    protected void updateOnlineState(NimUserInfo nimUserInfo) {
        tvOnlineState.setVisibility(View.GONE);

    }

    protected void updateNickLabel(String nick) {
        int labelWidth = ScreenUtil.screenWidth;
        labelWidth -= ScreenUtil.dip2px(50 + 70); // 减去固定的头像和时间宽度

        if (labelWidth > 0) {
            tvNickname.setMaxWidth(labelWidth);
        }
		tvNickname.setText(nick);
	}

    protected String unreadCountShowRule(int unread) {
        unread = Math.min(unread, 99);
        return String.valueOf(unread);
    }

/*    protected RecentContactsCallback getCallback() {
        return ((RecentContactAdapter) getAdapter()).getCallback();
    }*/
}
