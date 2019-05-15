package com.laisontech.laisondownloader.callback;

/**
 * Created by SDP
 * on 2019/5/10
 * Des：
 */
public interface LoadDataFromDBListener<T> {
    void startLoading();
    void loadDataFinished(T object);
}