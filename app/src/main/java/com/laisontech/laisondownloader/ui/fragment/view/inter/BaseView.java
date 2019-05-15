package com.laisontech.laisondownloader.ui.fragment.view.inter;

import android.support.annotation.StringRes;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public interface BaseView {
    void startLoad();

    void hideLoad();

    void showMsg(@StringRes int resID);

    void showMsg(String msg);
}
