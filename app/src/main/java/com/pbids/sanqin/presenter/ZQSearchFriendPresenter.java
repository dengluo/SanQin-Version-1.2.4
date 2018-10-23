package com.pbids.sanqin.presenter;

import com.google.gson.GsonBuilder;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;
import com.pbids.sanqin.ui.activity.zongquan.ZQSearchFriendView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * presenter
 * 类描述:搜索好友界面
 * Created by pbids903 on 2018/1/5.
 */

public class ZQSearchFriendPresenter extends BasePresenter{

    ZQSearchFriendView mZQSearchFriendView;
    //列表数据
    List<QinQinChiFriendGroup> qinQinChiFriendGroups = new ArrayList<>();
	QinQinChiFriendGroup friendGroup ; //
	List<QinQinChiFriend> friendList = new ArrayList<>();

    public ZQSearchFriendPresenter(ZQSearchFriendView mZQSearchFriendView){
        this.mZQSearchFriendView = mZQSearchFriendView;
        //初始化分组列表数据
		friendGroup = new QinQinChiFriendGroup();
		friendGroup.setSortUserModels(friendList);
		qinQinChiFriendGroups.add(friendGroup);
    }

    public List<QinQinChiFriendGroup> getQinQinChiFriendGroups(){
    	return qinQinChiFriendGroups;
	}

    // 查找  用户id
	public DisposableObserver<Response<String>>  query(String username, final String type) {
    	friendList.clear();//先清除列表
		final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
			@Override
			public void onNext(@NonNull Response<String> stringResponse) {
				try {
					JSONObject jsonObject = new JSONObject(stringResponse.body());
					int status = jsonObject.getInt("status");
					if (status == MyApplication.OK) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						for (int i = 0; i < jsonArray.length(); i++) {
							GsonBuilder gsonBuilder = new GsonBuilder();
							QinQinChiFriend friend = gsonBuilder.create().fromJson(jsonArray.get(i).toString(), QinQinChiFriend.class);
							friendList.add(friend);
						}

						//list view_donate_records 更新
						mZQSearchFriendView.getFrendAdapter().notifyDataSetChanged();
						mZQSearchFriendView.onHttpSuccess(type);

						//System.out.print(qinQinChiFriendGroups);

					} else {
						mZQSearchFriendView.onHttpError(jsonObject.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mZQSearchFriendView.onHttpError(e.getMessage());
				}
			}

			@Override
			public void onError(@NonNull Throwable e) {
				mZQSearchFriendView.onHttpError(Const.REQUEST_ERROR_NETWOR_DISCONNECT_MESSAGE);
			}

			@Override
			public void onComplete() {

			}
		};
//		String url = MyApplication.SERVER_ADDRESS_IM+"/im/findbyname";
		String url = AddrConst.SERVER_ADDRESS_USER+"/user/queryUserByName";
		HttpParams params = new HttpParams();
		params.put("name",username);
		//http post
		OkGoUtil.getStringObservableForPost(url,params,observer);
		return observer;
	}

	// 添加好友
	public void addFriend(QinQinChiFriend friend){
		/*String account = friend.getId()+"";


		NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {
			@Override
			public void onResult(boolean success, NimUserInfo result, int code) {
				DialogMaker.dismissProgressDialog();
				if (success) {
					if (result == null) {
						EasyAlertDialogHelper.showOneButtonDiolag(AddFriendActivity.this, R.string.user_not_exsit,
								R.string.user_tips, R.string.ok, false, null);
					} else {
						UserProfileActivity.start(AddFriendActivity.this, account);
					}
				} else if (code == 408) {
					Toast.makeText(AddFriendActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
				} else if (code == ResponseCode.RES_EXCEPTION) {
					Toast.makeText(AddFriendActivity.this, "on exception", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AddFriendActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
				}
			}
		});*/
	}



    /**
     * 判断列表是否有数据
     * @return
     */
    public boolean hasData() {
        return friendList.size()>0;
    }



}
