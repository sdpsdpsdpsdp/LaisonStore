package com.laisontech.laisondownloader.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by SDP on 2017/3/27.
 * 向栈中添加和移除activity
 * 使用方法
 * 在 oncreate()方法中
 * pushActivity
 * 在 onDestroy()方法总
 * popActivity
 */
public class ActivityStack {
    private static Stack<Activity> mActivityStack;//每次打开的同一个activity会认为是不同的activity对象，所以无法判断是否打开同一个
    private static ActivityStack instance;
    private static Stack<String> mActivityName;

    private ActivityStack() {
        mActivityStack = new Stack<Activity>();
        mActivityName = new Stack<>();
    }

    public static ActivityStack getScreenManager() {
        if (instance == null) {
            synchronized (ActivityStack.class) {
                if (instance == null) {
                    instance = new ActivityStack();
                }
            }
        }
        return instance;
    }

    // 弹出当前activity并销毁
    public synchronized void popActivity(Activity activity) {
        if (activity != null && mActivityStack.contains(activity) && mActivityName.contains(activity.getLocalClassName())) {
            mActivityStack.remove(activity);
            mActivityName.remove(activity.getLocalClassName());
            activity.finish();
        }

    }

    // 将当前Activity推入栈中
    public synchronized void pushActivity(Activity activity) {
        String localClassName = activity.getLocalClassName();
        if (mActivityName.contains(localClassName)) {
            activity.finish();
        } else {
            mActivityName.add(localClassName);
            mActivityStack.add(activity);
        }
    }

    // 退出栈中所有Activity
    public void clearAllActivity() {
        while (!mActivityStack.isEmpty()) {
            Activity activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
        mActivityStack.clear();
        mActivityName.clear();
    }
    public void exitApp(){
        clearAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}