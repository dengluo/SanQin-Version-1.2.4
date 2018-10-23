package com.pbids.sanqin.helper;

import com.pbids.sanqin.DemoCache;
//import  com.pbids.sanqin.redpacket.NIMRedPacketClient;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.drop.DropManager;

/**
 * 注销帮助类
 * Created by huangjun on 2015/10/8.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        DemoCache.clear();
        DropManager.getInstance().destroy();
//        NIMRedPacketClient.clear();

    }
}
