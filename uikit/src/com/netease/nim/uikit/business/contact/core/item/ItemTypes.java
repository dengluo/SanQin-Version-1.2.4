package com.netease.nim.uikit.business.contact.core.item;

/**
 * 通讯录列表项类型
 * Created by huangjun on 2015/2/10.
 */
public interface ItemTypes {

    /**
     * 基础类型
     */
    int TEXT = -2;

    int LABEL = -1;

    /**
     * 扩展类型
     */
    int FUNC = 0; // 功能项

    int FRIEND = 1; // 好友项

    int TEAM = 2; // 群组项

    int TEAM_MEMBER = 3; // 群成员

    int MSG = 4; // 消息

    int FRIEND_GROUP = 9; // 固定分组
    int FRIEND_GROUP_ADD = 10; // 添加分组
    int FRIEND_GROUP_CUSTOM = 11; // 自定义分组
    int SUB_GROUP_CUSTOM = 12; // 自定义二级分组
    /**
     * 子类型
     */
    interface TEAMS {
        int BASE = ItemTypes.TEAM << 16;

        int NORMAL_TEAM = BASE + 1; // 普通群

        int ADVANCED_TEAM = BASE + 2; // 高级群
    }
}
