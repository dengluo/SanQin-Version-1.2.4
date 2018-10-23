package com.pbids.sanqin.ui.activity.zongquan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.contact.activity.UserProfileActivity;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.zxing.CaptureActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/2/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:10
 * @desscribe 类描述:宗圈-添加好友界面
 * @remark 备注:
 * @see
 */
public class ZQAddFriendFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear {

	public static final int  CAPTURE_REQUEST_CODE = 6702 ;

    @Bind(R.id.search_layout)
    LinearLayout searchLayout;
    @Bind(R.id.zq_scan_layout)
    RelativeLayout zqScanLayout;
    @Bind(R.id.zq_add_phone_number)
    RelativeLayout zqAddPhoneNumber;
    @Bind(R.id.zongquan_friends_list)
    RecyclerView zongquanFriendsList;
    @Bind(R.id.zongquan_sv)
    NestedScrollView zongquanSv;



    public static ZQAddFriendFragment newInstance() {
        ZQAddFriendFragment fragment = new ZQAddFriendFragment();
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
        View view = inflater.inflate(R.layout.fragment_zq_add_friend, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitle("添加新好友", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //扫描二维码后返回
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode==1){
			switch (requestCode){
				case CAPTURE_REQUEST_CODE:
					String qrcode = intent.getStringExtra("qrcode");
					//添加好友
					addFriend(qrcode,_mActivity);
					break;
			}
		}
	}

	@OnClick({R.id.search_layout, R.id.zq_scan_layout, R.id.zq_add_phone_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_layout:
                //查找好友
                start(ZQSearchFriendFragment.newInstance());
                break;
            case R.id.zq_scan_layout:
                //扫描二维码加好友
				Intent intent = new Intent(_mActivity,CaptureActivity.class);
				intent.putExtra("ctrl",3);
//				startActivity(intent);
				startActivityForResult(intent, CAPTURE_REQUEST_CODE);
                break;
            case R.id.zq_add_phone_number:
            	//手机联系人

                break;
        }
    }

	//添加好友
	public static void addFriend(final String account, final Context context ){
		DialogMaker.showProgressDialog( context, null, false);
		NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {
			@Override
			public void onResult(boolean success, NimUserInfo result, int code) {
				DialogMaker.dismissProgressDialog();
				if (success) {
					if (result == null) {
						EasyAlertDialogHelper.showOneButtonDiolag(context , R.string.user_not_exsit,
								R.string.user_tips, R.string.ok, false, null);
					} else {
						UserProfileActivity.start(context, account);
					}
				} else if (code == 408) {
					Toast.makeText(context, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
				} else if (code == ResponseCode.RES_EXCEPTION) {
					Toast.makeText(context, "on exception", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "on failed:" + code, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
