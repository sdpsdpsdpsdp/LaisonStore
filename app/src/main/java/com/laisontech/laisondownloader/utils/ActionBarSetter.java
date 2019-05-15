package com.laisontech.laisondownloader.utils;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;

import java.util.Locale;

/**
 * Created by SDP on 2017/10/19.
 */

public class ActionBarSetter {
    //设置自定义的actionBar
    public static ActionBarView setCustomActionBar(AppCompatActivity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_actionbar, null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_actionBar_back);
        TextView tv = (TextView) view.findViewById(R.id.tv_actionBar_title);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_actionBar_back);
        //初始化ActionBarView
        ActionBarView viewList = new ActionBarView();
        viewList.tvTitle = tv;
        viewList.leftIvBack = iv;
        viewList.leftLl = ll;
        String language = Locale.getDefault().getLanguage();
        if (language.equals("ar") || language.equals("fa")) {
            viewList.leftIvBack.setImageResource(R.drawable.actionbar_iv_fa_selector);
        }
        setParams(activity, view);

        return viewList;
    }

    private static void setParams(AppCompatActivity activity, View view) {
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        ActionBar actionBar = setActionBarParams(activity);
        actionBar.setCustomView(view, params);
    }

    private static ActionBar setActionBarParams(AppCompatActivity activity) {
        //获取ActionBar对象
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setElevation(0);
        //设置ActionBar可以自定义
        actionBar.setDisplayShowCustomEnabled(true);
        //设置actionBar的显示选项为自定义
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        return actionBar;
    }


    public static class ActionBarView {
        public LinearLayout leftLl;
        public ImageView leftIvBack;
        public TextView tvTitle;
    }
}
