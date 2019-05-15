package com.laisontech.laisondownloader.entitys.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by SDP
 * on 2019/5/9
 * Des：
 */
public class OperateEvent {
    public static final int ADD = 0x120;
    public static final int START = 0x121;
    public static final int PARSE = 0x122;
    public static final int DELETE = 0x123;
    public static final int RELOAD = 0x124;
    public static final int NONE = 0x125;
    public static final int FILE_NOT_EXIST = 0x126;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ADD, START, PARSE, DELETE, RELOAD, NONE, FILE_NOT_EXIST})
    public @interface OperateType {

    }

    private int operateType;

    public OperateEvent(@OperateType int operateType) {
        this.operateType = operateType;
    }

    public @OperateType
    int getOperateType() {
        return operateType;
    }
}
