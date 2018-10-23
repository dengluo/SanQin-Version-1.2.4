package com.pbids.sanqin.utils.zxing;

import com.pbids.sanqin.model.entity.QinQinChiUserGroupItem;
import com.pbids.sanqin.ui.view.letter.CharacterParser;
import com.pbids.sanqin.model.entity.QinQinChiFriend;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要是生成测试数据
 * Created by liang on 2018/3/6.
 */

public class TestDbUtil {

	private static CharacterParser characterParser = CharacterParser.getInstance();
	private static List<QinQinChiFriend> dbFriend ;
	private static List<QinQinChiUserGroupItem> dbGroup ;

	public static List<QinQinChiUserGroupItem> fillGroupData(String[] data) {
		if(dbGroup!=null){
			return dbGroup;
		}
		dbGroup = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			QinQinChiUserGroupItem item = new QinQinChiUserGroupItem();
			item.setId(i+600);
			item.setName(data[i]);
			dbGroup.add(item);
		}
		return dbGroup;
	}

	public static List<QinQinChiFriend> fillFrendData(String[] date) {
		if(dbFriend!=null){
			return dbFriend;
		}
		dbFriend = new ArrayList<QinQinChiFriend>();
		for (int i = 0; i < date.length; i++) {
			QinQinChiFriend sortModel = new QinQinChiFriend();
			sortModel.setName(date[i]);
			sortModel.setId(i+20);
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			dbFriend.add(sortModel);
		}
		return dbFriend;
	}


}
