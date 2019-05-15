package com.laisontech.laisondownloader.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.laisontech.laisondownloader.entitys.event.EventSender;
import com.laisontech.laisondownloader.entitys.event.AppOperateEvent;
import com.laisontech.laisondownloader.entitys.event.EventSender;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：App的安装和卸载广播
 */
public class AppInstallReceiver extends BroadcastReceiver {
    public static final String INSTALL = "android.intent.action.PACKAGE_ADDED";
    public static final String UNINSTALL = "android.intent.action.PACKAGE_REMOVED";
    public static final String REPLACED = "android.intent.action.PACKAGE_REPLACED";
    public static final String PACKAGE = "package";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) return;
        //接收安装广播
        switch (intent.getAction()) {
            case INSTALL:
                EventSender.sendAppOperateEvent(AppOperateEvent.INSTALL, intent.getDataString());
                break;
            case UNINSTALL:
                EventSender.sendAppOperateEvent(AppOperateEvent.UNINSTALL, intent.getDataString());
                break;
            case REPLACED:
                EventSender.sendAppOperateEvent(AppOperateEvent.REPLACED, intent.getDataString());
                break;
        }
    }
}
