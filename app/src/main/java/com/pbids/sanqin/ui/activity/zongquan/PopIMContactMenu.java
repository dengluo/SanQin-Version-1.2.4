package com.pbids.sanqin.ui.activity.zongquan;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;

public class PopIMContactMenu extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    private View view;

    private Button btn_add_friend;
    private Button btn_add_group;
    private Button btn_add_basegroup;
    private Button btn_im_setting;

    RelativeLayout popLayout;

    private IMContactsMenuCb set;

    // setting
    public PopIMContactMenu(Context mContext, IMContactsMenuCb set, Bundle bundle) {
        this.set =set;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.pop_im_contact_menu, null);

        btn_add_friend = (Button) view.findViewById(R.id.btn_add_friend);
        btn_add_group = (Button) view.findViewById(R.id.btn_add_group);
        btn_add_basegroup = (Button) view.findViewById(R.id.btn_add_basegroup);
        btn_im_setting = (Button) view.findViewById(R.id.btn_im_setting);
        // 取消按钮
        btn_add_friend.setOnClickListener(this);
        btn_add_group.setOnClickListener(this);
        btn_add_basegroup.setOnClickListener(this);
        btn_im_setting.setOnClickListener(this);

        popLayout = (RelativeLayout) view.findViewById(R.id.pop_layout);
//        popLayout.setOnClickListener(this);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }else if(v.getId()==R.id.pop_layout){
						dismiss();
					}
                }
                return true;
            }
        });
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("ctrl",102);
        switch (view.getId()){
            case R.id.btn_add_friend:
                bundle.putInt("type",1);
                set.settingCb(bundle);
                // 销毁弹出框
                dismiss();
                break;
            case R.id.btn_add_group:
                bundle.putInt("type",2);
                set.settingCb(bundle);
                // 销毁弹出框
                dismiss();
                break;
            case R.id.btn_add_basegroup:
                bundle.putInt("type",3);
                set.settingCb(bundle);
                // 销毁弹出框
                dismiss();
                break;
            case R.id.btn_im_setting:
                bundle.putInt("type",5);
                set.settingCb(bundle);
                // 销毁弹出框
                dismiss();
                break;
            case R.id.pop_layout:
                // 销毁弹出框
                dismiss();
                break;
        }
    }


}