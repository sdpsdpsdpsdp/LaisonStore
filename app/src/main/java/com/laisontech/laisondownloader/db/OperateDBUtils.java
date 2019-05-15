package com.laisontech.laisondownloader.db;


import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfo;
import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfoDao;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.http.HttpConst;

import java.util.List;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class OperateDBUtils {
    /**
     * 获取服务器地址
     */
    public static String getServerAddress() {
        return GlobalData.getInstance().getSp().getString(HttpConst.KEY_SERVER_URL, HttpConst.DEFAULT_SERVER_ADDRESS);
    }

    public static void setServerAddress(String hostaddr) {
        GlobalData.getInstance().getSp().edit().putString(HttpConst.KEY_SERVER_URL, hostaddr).apply();
    }

    private static LastSearchInfoDao querySearchDao() {
        return new LastSearchInfoDao(GlobalData.getInstance().getLocalContext());
    }

    /**
     * 查询上次搜索的内容
     */
    public static List<LastSearchInfo> queryLastSearchList() {
        return querySearchDao().queryInfoList(0, 20);
    }

    /**
     * 保存搜索结果
     */
    public static boolean saveSearchResult(String record) {
        return querySearchDao().saveSearchResult(record);
    }
}
