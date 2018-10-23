package com.netease.nim.uikit.business.contact.core.viewholder;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.core.item.MsgItem;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.business.contact.core.model.IContact;
import com.netease.nim.uikit.business.contact.core.model.UserInfoExtends;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.search.model.MsgIndexRecord;
import com.netease.nimlib.sdk.search.model.RecordHitInfo;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

public class MsgHolder extends AbsContactViewHolder<MsgItem> {

    private static final String PREFIX = "...";

    protected HeadImageView head;

    protected TextView name;

    protected TextView time;

    protected TextView desc;
//    protected ImageView ImgBusinessCooperation;
//    protected TextView tvVip;
//    protected TextView tvLevelName ;


    protected int descTextViewWidth; // 当前ViewHolder desc TextView 最大的宽度px

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.nim_contacts_item, null);

        head = (HeadImageView) view.findViewById(R.id.contacts_item_head);
        name = (TextView) view.findViewById(R.id.contacts_item_name);
        time = (TextView) view.findViewById(R.id.contacts_item_time);
        desc = (TextView) view.findViewById(R.id.contacts_item_desc);
//        ImgBusinessCooperation = (ImageView) view.findViewById(R.id.contacts_business_cooperation);
//        tvVip = (TextView) view.findViewById(R.id.contacts_item_vip);
//        tvLevelName = (TextView) view.findViewById(R.id.contacts_levelname);

        // calculate
        View parent = (View) desc.getParent();
        if (parent.getMeasuredWidth() == 0) {
            // xml中：50dp，包括头像的长度还有左右间距大小
            parent.measure(View.MeasureSpec.makeMeasureSpec(ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(50.0f), View.MeasureSpec.EXACTLY), 0);
        }
        descTextViewWidth = (int) (parent.getMeasuredWidth() - desc.getPaint().measureText(PREFIX));

        return view;
    }

    @Override
    public void refresh(ContactDataAdapter adapter, int position, final MsgItem item) {
        // contact info
        final IContact contact = item.getContact();
        final MsgIndexRecord record = item.getRecord();

        if (record.getSessionType() == SessionTypeEnum.P2P) {
            head.loadBuddyAvatar(contact.getContactId());
        } else {
            Team team = NimUIKit.getTeamProvider().getTeamById(contact.getContactId());
            head.loadTeamIconByTeam(team);
        }
        name.setText(contact.getDisplayName());

        //更新用户扩展信息
		updateUserExtend(contact);

        if (item.isQuerySession()) {
            time.setVisibility(View.VISIBLE);
            time.setText(TimeUtil.getTimeShowString(record.getTime(), false));
        } else {
            time.setVisibility(View.GONE);
        }

        // query result
        if (record.getCount() > 1) {
            desc.setText(String.format("%d条相关聊天记录", record.getCount()));
        } else {
            String text = record.getText(); // 原串
            List<RecordHitInfo> clone = record.cloneHitInfo(); // 计算高亮区域并clone

            // 异常情况，没有高亮击中的区间，直接设置原串
            if (clone == null || clone.isEmpty()) {
                desc.setText(text);
                return;
            }

            int firstIndex = clone.get(0).start; // 首个需要高亮的关键字的起始位置
            int firstHitInfoLength = clone.get(0).end - clone.get(0).start + 1; // 首个需要高亮的关键字的长度

            // 判断是否需要截取
            Object[] result = needCutText(record.getText(), firstIndex, firstHitInfoLength);
            Boolean needCut = (Boolean) result[0];
            int extractPreStrNum = (Integer) result[1];
            if (needCut) {
                // 文本截取
                int newStartIndex = firstIndex - extractPreStrNum;
                text = PREFIX + text.substring(newStartIndex);

                // 矫正hitInfo
                int delta = newStartIndex - PREFIX.length();
                for (RecordHitInfo rh : clone) {
                    rh.start -= delta;
                    rh.end -= delta;
                }
            }

            display(desc, text, clone);
        }
    }

	//更新扩展信息
	private void updateUserExtend(IContact contact) {
	/*	//取用户扩展信息

			//没有扩展信息不显示
			this.tvVip.setVisibility(View.GONE);
			this.ImgBusinessCooperation.setVisibility(View.GONE);
			this.ImgBusinessCooperation.setVisibility(View.GONE);

	//只更新好以类型的扩展信息
		if(recent.getSessionType() == SessionTypeEnum.P2P ) {
		NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(contact.getContactId());
		String extInfoStr = user.getExtension();
		if(null!=extInfoStr && !extInfoStr.isEmpty()){
			GsonBuilder gsonBuilder = new GsonBuilder();
			UserInfoExtends userInfoExtends = gsonBuilder.create().fromJson(extInfoStr, UserInfoExtends.class);
			// @liang
			if(userInfoExtends!=null){
				//vip
				if(userInfoExtends.getVip()>0){
					this.tvVip.setVisibility(View.VISIBLE);
					this.tvVip.setText("VIP"+userInfoExtends.getVip());
				}else{
					this.tvVip.setVisibility(View.GONE);
				}
				//商务合作
				if (userInfoExtends.getClanStatus() > 0) {
					this.ImgBusinessCooperation.setVisibility(View.VISIBLE);
				} else {
					this.ImgBusinessCooperation.setVisibility(View.GONE);
				}
				//等级
				if(null==userInfoExtends.getLevelName() || userInfoExtends.getLevelName().isEmpty()){
					this.tvLevelName.setVisibility(View.GONE);
				}else{
					this.tvLevelName.setText(userInfoExtends.getLevelName());
				}
			}
		}
		}
*/
	}

    /**
     * 假如第一个索引之前的文字宽度>layout宽度，就需要把text截断
     *
     * @param text               原文本
     * @param firstIndex         首个需要高亮的关键字的起始位置
     * @param firstHitInfoLength 首个需要高亮的关键字的长度
     * @return [0]是否需要截取字符串;[1]如果要截取，那么需要提取前面几个字符
     */
    private Object[] needCutText(String text, int firstIndex, int firstHitInfoLength) {
        Boolean r0;
        Integer r1;
        float descLength = desc.getPaint().measureText(text.substring(0, firstIndex + firstHitInfoLength));
        float avg = descLength / (firstIndex + firstHitInfoLength); // 前firstIndex+firstHitInfoLength个字符，平均每个字符的长度

        if (descLength >= descTextViewWidth) {
            r0 = true;
            int extractPreStrNum = (int) (1.0f * descTextViewWidth / avg); // 当前viewHolder desc TextView能容纳多少个字符（前firstIndex区间的字符）
            extractPreStrNum = extractPreStrNum - PREFIX.length() - firstHitInfoLength; // 可以提取前面几个字符
            if (extractPreStrNum < 0) {
                extractPreStrNum = 0; // 有可能为负数，例如firstHitInfo特别长
            }

            // 新的文本
            int newStartIndex = firstIndex - extractPreStrNum;
            text = PREFIX + text.substring(newStartIndex, firstIndex + firstHitInfoLength);

            // extractPreStrNum 校准
            if (extractPreStrNum > 0) {
                descLength = desc.getPaint().measureText(text);
                if (descLength > descTextViewWidth) {
                    int delta = (int) ((descLength - descTextViewWidth) / (descLength / text.length())) + 1;
                    extractPreStrNum -= delta; // 減少提取的字符数
                }

                if (extractPreStrNum < 0) {
                    extractPreStrNum = 0; // 修正
                }
            }

            r1 = extractPreStrNum;
        } else {
            r0 = false;
            r1 = 0;
        }
        return new Object[]{r0, r1};
    }

    public static final void display(TextView tv, String text, List<RecordHitInfo> hitInfos) {
        if (hitInfos == null || hitInfos.isEmpty()) {
            tv.setText(text);
            return;
        }

        SpannableStringBuilder sb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString(text);
        for (RecordHitInfo r : hitInfos) {
            ss.setSpan(new ForegroundColorSpan(tv.getResources().getColor(R.color.contact_search_hit)), r.start, r.end + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        sb.append(ss);
        tv.setText(sb);
    }
}
