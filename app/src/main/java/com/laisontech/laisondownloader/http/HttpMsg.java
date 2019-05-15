package com.laisontech.laisondownloader.http;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.global.GlobalData;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class HttpMsg {
    /**
     * 获取数据内容列表
     */
    public static String getListContentMsg(int errorCode) {
        return "";
    }

    public static String getAppUpdateMsg(int errorCode) {
        switch (errorCode) {
            case HttpConst.CODE_SUCCESS:
                return "";
            default:
                return GlobalData.getInstance().getRes(R.string.getUpLoadDataFailed);
        }
    }
}
