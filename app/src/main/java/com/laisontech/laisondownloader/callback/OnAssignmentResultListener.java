package com.laisontech.laisondownloader.callback;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public interface OnAssignmentResultListener<T> {
    void onSuccess(int errorCode,T t);

    void onFailed(String failMsg);
}
