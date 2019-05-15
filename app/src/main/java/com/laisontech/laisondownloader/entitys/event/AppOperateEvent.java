package com.laisontech.laisondownloader.entitys.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class AppOperateEvent {
    public static final int INSTALL = 0x220;
    public static final int UNINSTALL = 0x221;
    public static final int REPLACED = 0x222;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({INSTALL, UNINSTALL, REPLACED})
    public @interface Operate {

    }

    private int operateType;
    private String packageName;

    public AppOperateEvent(@Operate int operateType, String packageName) {
        this.operateType = operateType;
        this.packageName = packageName;
    }

    public @Operate
    int getOperateType() {
        return operateType;
    }

    public String getPackageName() {
        return packageName;
    }
}
