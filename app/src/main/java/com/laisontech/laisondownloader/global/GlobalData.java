package com.laisontech.laisondownloader.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;

import com.laisontech.laisondownloader.http.HttpConst;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：
 */
public class GlobalData {
    private static volatile GlobalData mInstance;
    private SharedPreferences mSp;

    public static GlobalData getInstance() {
        if (null == mInstance) {
            synchronized (GlobalData.class) {
                if (null == mInstance) {
                    mInstance = new GlobalData();
                }
            }
        }
        return mInstance;
    }

    public Context getLocalContext() {
        return LSApp.mLocalContext;
    }


    public SharedPreferences getSp() {
        if (mSp == null) {
            mSp = getLocalContext().getSharedPreferences(HttpConst.KEY_LAISON_SP_NAME, Context.MODE_PRIVATE);
        }
        return mSp;
    }

    public String getRes(@StringRes int resId) {
        return getLocalContext().getResources().getString(resId);
    }
}
