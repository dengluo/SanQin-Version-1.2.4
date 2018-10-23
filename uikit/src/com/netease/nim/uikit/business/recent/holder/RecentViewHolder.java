package com.netease.nim.uikit.business.recent.holder;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

public abstract class RecentViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, RecentContact> {

    public RecentViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    private int lastUnreadCount = 0;

    protected FrameLayout portraitPanel;

    protected HeadImageView imgHead;

    protected TextView tvNickname;

    protected TextView tvMessage;

    protected TextView tvDatetime;

    protected TextView tvLevelname; //等级名称
    protected TextView tvVip; //等级名称
    protected ImageView tvBusinessCooperation; //商务合作


    // 消息发送错误状态标记，目前没有逻辑处理
    protected ImageView imgMsgStatus;

    protected View bottomLine;

    protected View topLine;

    // 未读红点（一个占坑，一个全屏动画）
    protected DropFake tvUnread;

    private ImageView imgUnreadExplosion;

    protected TextView tvOnlineState;

    // 子类覆写
    protected abstract String getContent(RecentContact recent);

    @Override
    public void convert(BaseViewHolder holder, RecentContact data, int position, boolean isScrolling) {
        inflate(holder, data);
        refresh(holder, data, position);
    }

    public void inflate(BaseViewHolder holder, final RecentContact recent) {
        this.portraitPanel = holder.getView(R.id.portrait_panel);
        this.imgHead = holder.getView(R.id.img_head);
        this.tvNickname = holder.getView(R.id.tv_nickname);
        this.tvMessage = holder.getView(R.id.tv_message);
        this.tvUnread = holder.getView(R.id.unread_number_tip);
        this.imgUnreadExplosion = holder.getView(R.id.unread_number_explosion);
        this.tvDatetime = holder.getView(R.id.tv_date_time);
        this.imgMsgStatus = holder.getView(R.id.img_msg_status);
        this.bottomLine = holder.getView(R.id.bottom_line);
        this.topLine = holder.getView(R.id.top_line);
        this.tvOnlineState = holder.getView(R.id.tv_online_state);
        this.tvLevelname = holder.getView(R.id.tv_levelname);//
        this.tvVip = holder.getView(R.id.tv_item_vip);//
        this.tvBusinessCooperation = holder.getView(R.id.tv_business_cooperation);//
        holder.addOnClickListener(R.id.unread_number_tip);

        this.tvUnread.setTouchListener(new DropFake.ITouchListener() {
            @Override
            public void onDown() {
                DropManager.getInstance().setCurrentId(recent);
                DropManager.getInstance().down(tvUnread, tvUnread.getText());
            }

            @Override
            public void onMove(float curX, float curY) {
                DropManager.getInstance().move(curX, curY);
            }

            @Override
            public void onUp() {
                DropManager.getInstance().up();
            }
        });
    }

    // refresh 更新信息
    public void refresh(BaseViewHolder holder, RecentContact recent, final int position) {
        // unread count animation
        boolean shouldBoom = lastUnreadCount > 0 && recent.getUnreadCount() == 0; // 未读数从N->0执行爆裂动画;
        lastUnreadCount = recent.getUnreadCount();

        updateBackground(holder, recent, position);

        loadPortrait(recent);

        updateNickLabel(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));

        updateOnlineState(recent);

        updateMsgLabel(holder, recent);

        updateNewIndicator(recent);

        updateUserExtend(recent);

        if (shouldBoom) {
            Object o = DropManager.getInstance().getCurrentId();
            if (o instanceof String && o.equals("0")) {
                imgUnreadExplosion.setImageResource(R.drawable.nim_explosion);
                imgUnreadExplosion.setVisibility(View.VISIBLE);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((AnimationDrawable) imgUnreadExplosion.getDrawable()).start();
                        // 解决部分手机动画无法播放的问题（例如华为荣耀）
                        getAdapter().notifyItemChanged(getAdapter().getViewHolderPosition(position));
                    }
                });
            }
        } else {
            imgUnreadExplosion.setVisibility(View.GONE);
        }
    }

    //更新扩展信息
	private void updateUserExtend(RecentContact recent) {
		//没有扩展信息不显示
		this.tvVip.setVisibility(View.GONE);
		this.tvBusinessCooperation.setVisibility(View.GONE);
		this.tvLevelname.setVisibility(View.GONE);
		//只更新好以类型的扩展信息
		if(recent.getSessionType() == SessionTypeEnum.P2P ) {
			//取用户扩展信息
			NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(recent.getContactId());
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
	}

	private void updateBackground(BaseViewHolder holder, RecentContact recent, int position) {
        topLine.setVisibility(getAdapter().isFirstDataItem(position) ? View.GONE : View.VISIBLE);
        bottomLine.setVisibility(getAdapter().isLastDataItem(position) ? View.VISIBLE : View.GONE);
        if ((recent.getTag() & RecentContactsFragment.RECENT_TAG_STICKY) == 0) {
            holder.getConvertView().setBackgroundResource(R.drawable.nim_touch_bg);
        } else {
            holder.getConvertView().setBackgroundResource(R.drawable.nim_recent_contact_sticky_selecter);
        }
    }

    protected void loadPortrait(RecentContact recent) {
        // 设置头像
        if (recent.getSessionType() == SessionTypeEnum.P2P) {
            imgHead.loadBuddyAvatar(recent.getContactId());
        } else if (recent.getSessionType() == SessionTypeEnum.Team) {
            Team team = NimUIKit.getTeamProvider().getTeamById(recent.getContactId());
            imgHead.loadTeamIconByTeam(team);
        }
    }

    private void updateNewIndicator(RecentContact recent) {
        int unreadNum = recent.getUnreadCount();
        tvUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
        tvUnread.setText(unreadCountShowRule(unreadNum));
    }

    private void updateMsgLabel(BaseViewHolder holder, RecentContact recent) {
        // 显示消息具体内容
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), tvMessage, getContent(recent), -1, 0.45f);
        //tvMessage.setText(getContent());

        MsgStatusEnum status = recent.getMsgStatus();
        switch (status) {
            case fail:
                imgMsgStatus.setImageResource(R.drawable.nim_g_ic_failed_small);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            case sending:
                imgMsgStatus.setImageResource(R.drawable.nim_recent_contact_ic_sending);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            default:
                imgMsgStatus.setVisibility(View.GONE);
                break;
        }

        String timeString = TimeUtil.getTimeShowString(recent.getTime(), true);
        tvDatetime.setText(timeString);
    }

    protected String getOnlineStateContent(RecentContact recent) {
        return "";
    }

    protected void updateOnlineState(RecentContact recent) {
        if (recent.getSessionType() == SessionTypeEnum.Team) {
            tvOnlineState.setVisibility(View.GONE);
        } else {
            String onlineStateContent = getOnlineStateContent(recent);
            if (TextUtils.isEmpty(onlineStateContent)) {
                tvOnlineState.setVisibility(View.GONE);
            } else {
                tvOnlineState.setVisibility(View.VISIBLE);
                tvOnlineState.setText(getOnlineStateContent(recent));
            }
        }
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

    protected RecentContactsCallback getCallback() {
        return ((RecentContactAdapter) getAdapter()).getCallback();
    }
}
