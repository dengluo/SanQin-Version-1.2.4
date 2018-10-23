package com.pbids.sanqin.ui.view.letter;

import com.pbids.sanqin.model.entity.QinQinChiFriend;
import com.pbids.sanqin.model.entity.QinQinChiFriendGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<QinQinChiFriendGroup> {

//	public int compare(SortUserModel o1, SortUserModel o2) {
//		if (o1.getSortLetters().equals("@")
//				|| o2.getSortLetters().equals("#")) {
//			return -1;
//		} else if (o1.getSortLetters().equals("#")
//				|| o2.getSortLetters().equals("@")) {
//			return 1;
//		} else {
//			return o1.getSortLetters().compareTo(o2.getSortLetters());
//		}
//	}
	public int compare(QinQinChiFriendGroup o1, QinQinChiFriendGroup o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

	public static List<QinQinChiFriendGroup> groupTo(List<QinQinChiFriend> list) {
		if (null == list) {
			return null;
		}
		Map<String, List<QinQinChiFriend>> map = new HashMap<>();
		List<QinQinChiFriendGroup> sortUserGroupModels = new ArrayList<>();
		// 按name开始分组
		String key;
		List<QinQinChiFriend> listTmp;
		for (QinQinChiFriend val : list) {
			key = val.getSortLetters();
			listTmp = map.get(key);
			if (null == listTmp) {
				listTmp = new ArrayList<QinQinChiFriend>();
				map.put(key, listTmp);
			}
			listTmp.add(val);
		}

		Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key1 = (String) iter.next();
			List<QinQinChiFriend> listTmp1 = map.get(key1);
			QinQinChiFriendGroup sortUserGroupModel = new QinQinChiFriendGroup();
			sortUserGroupModel.setSortLetters(key1);
			sortUserGroupModel.setSortUserModels(listTmp1);
			sortUserGroupModels.add(sortUserGroupModel);
		}
		return sortUserGroupModels;
	}

}
