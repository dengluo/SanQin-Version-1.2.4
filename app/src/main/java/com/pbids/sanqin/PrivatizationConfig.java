package com.pbids.sanqin;

import com.netease.nimlib.sdk.ServerAddresses;

/**
 * 网易云信私有化配置项
 */
class PrivatizationConfig {

    static ServerAddresses getServerAddresses() {
        return null;
    }

    static String getAppKey() {
        //return null;
        return "2CBB1FEA9A00CF006543A466BB72C8B2";
    }

    private static ServerAddresses get() {
        ServerAddresses addresses = new ServerAddresses();
        addresses.nosDownload = "nos.netease.com";
        addresses.nosAccess = "{bucket}.nosdn.127.net/{object}";
        return addresses;
    }
}
