package com.laisontech.laisondownloader.entitys.event;

import android.text.TextUtils;

import com.laisontech.laisondownloader.constants.Const;
import com.laisontech.laisondownloader.constants.Const;
import com.laisontech.laisondownloader.global.GlobalData;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：event bus 发送数据
 */
public class EventSender {
    /**
     * 发送网络连接的广播
     */
    public static void sendNetConnect(boolean netConnect) {
        EventBus.getDefault().post(new NetConnectEvent(netConnect));
    }

    /**
     * 发送下载操作的广播
     */
    public static void sendOperateDownloadEvent(@OperateEvent.OperateType int operateType) {
        EventBus.getDefault().post(new OperateEvent(operateType));
    }

    /**
     * 发送App安装的广播
     */
    public static void sendAppOperateEvent(@AppOperateEvent.Operate int type, String packageName) {
        if (TextUtils.isEmpty(packageName)) return;
        //过滤掉不是公司的app
        if (!packageName.contains(Const.BASE_PACKAGE_NAME)) return;
        //过滤掉当前app
        if (packageName.contains(GlobalData.getInstance().getLocalContext().getPackageName()))
            return;
        EventBus.getDefault().post(new AppOperateEvent(type, packageName));
    }
}
