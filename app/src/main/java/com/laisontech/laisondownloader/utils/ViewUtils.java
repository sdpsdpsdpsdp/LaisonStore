package com.laisontech.laisondownloader.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.laisontech.laisondownloader.ui.activity.apps.view.activity.SearchActivity;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class ViewUtils {
    public static void hideSoft(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int getWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;// 屏幕宽度（像素）
    }

    public interface OnEditTextChangeListener {
        void onChange(CharSequence cs);
    }

    public static void setEditClickEvent(EditText editText, boolean focusable) {
        if (editText == null) return;
        editText.setCursorVisible(focusable);
        editText.setFocusable(focusable);
        editText.setFocusableInTouchMode(focusable);
        if (focusable) {
            editText.requestFocus();
        }
    }

    //打开网络连接界面
    public static void openConnectNetActivity(Context context) {
        Intent intent = null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    public static void setEditFourceListener(EditText etSearch, final OnEditTextChangeListener listener) {
        if (etSearch == null) return;
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null) {
                    listener.onChange(s);
                }
            }
        });
    }

    public static boolean isEmptyStr(String msg) {
        if (TextUtils.isEmpty(msg)) return true;
        if (msg.contains(" ")) {
            msg = msg.replace(" ", "");
        }
        return TextUtils.isEmpty(msg);
    }
}
