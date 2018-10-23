package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.NewsTopicSubscribe;
import com.pbids.sanqin.model.entity.UserInfo;

import java.util.List;

/**
 * view
 * Created by caiguoliang on 2017/12/11.
 */

public interface MeTopicSubscribeView extends BaseView{

    void updateTagsList(List<NewsTopicSubscribe> tagsList);

    void updateSurnameList(List<NewsTopicSubscribe> surnameList);

	public void onDeleteCb(DelCb cb,String type);

	//数据返回
	public class DelCb  {
		public static final int REQUEST_OK = 1;
		private int requestCode ;
		private int resultCode ;
		private NewsTopicSubscribe data;
		private UserInfo userInfo;

		public int getRequestCode() {
			return requestCode;
		}

		public void setRequestCode(int requestCode) {
			this.requestCode = requestCode;
		}

		public int getResultCode() {
			return resultCode;
		}

		public void setResultCode(int resultCode) {
			this.resultCode = resultCode;
		}


		public NewsTopicSubscribe getData() {
			return data;
		}

		public void setData(NewsTopicSubscribe data) {
			this.data = data;
		}

		public UserInfo getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(UserInfo userInfo) {
			this.userInfo = userInfo;
		}
	}

}