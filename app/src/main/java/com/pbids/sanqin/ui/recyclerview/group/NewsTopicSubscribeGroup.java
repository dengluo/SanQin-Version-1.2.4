package com.pbids.sanqin.ui.recyclerview.group;

import com.pbids.sanqin.model.entity.NewsTopicSubscribe;

import java.util.List;

/**
 * Created by pbids903 on 2018/1/25.
 * 公众号订阅的消息group
 */

public class NewsTopicSubscribeGroup {

	private String title;

	private List<NewsTopicSubscribe> lists;

	public List<NewsTopicSubscribe> getLists() {
		return lists;
	}

	public void setLists(List<NewsTopicSubscribe> lists) {
		this.lists = lists;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
