package com.laisontech.laisondownloader.ui.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.ui.fragment.view.fragment.AppsFragment;
import com.laisontech.laisondownloader.ui.fragment.view.fragment.ManageFragment;
import com.laisontech.laisondownloader.ui.fragment.view.fragment.SettingsFragment;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class MainTabFactory {
    public static final int DEFAULT_ID = R.id.navigation_apps;
    private Fragment mAppsFragment;
    private Fragment mManagerFragment;
    private Fragment mSettingsFragment;
    /**
     * 对象锁方法第一次启动时比类锁方法快2秒
     */
    private static final Object LOCK = new Object();
    private static volatile MainTabFactory instance = null;
    private Set<Integer> mTotalIds;

    public static MainTabFactory getInstance() {
        if (null == instance) {
            synchronized (LOCK) {
                if (null == instance) {
                    instance = new MainTabFactory();
                }
            }
        }
        return instance;
    }

    private MainTabFactory() {
        mTotalIds = new HashSet<>();
    }

    public Fragment getFragment(@IdRes int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.navigation_apps:
                if (mAppsFragment == null) {
                    mAppsFragment = AppsFragment.getFragment();
                    mTotalIds.add(itemId);
                }
                fragment = mAppsFragment;
                break;
            case R.id.navigation_manager:
                if (mManagerFragment == null) {
                    mManagerFragment = ManageFragment.getFragment();
                    mTotalIds.add(itemId);
                }
                fragment = mManagerFragment;
                break;

            case R.id.navigation_settings:
                if (mSettingsFragment == null) {
                    mSettingsFragment = SettingsFragment.getFragment();
                    mTotalIds.add(itemId);
                }
                fragment = mSettingsFragment;
                break;
            default:
                break;
        }
        return fragment;
    }

    /**
     * 获取未选中的ID
     */
    public Set<Integer> getUnSelectIds(@IdRes int selectId) {
        if (mTotalIds == null || mTotalIds.size() < 1) return null;
        Set<Integer> unSelectIds = new HashSet<>();
        for (int id : mTotalIds) {
            if (selectId != id) {
                unSelectIds.add(id);
            }
        }
        return unSelectIds;
    }

    public void clear() {
        if (mTotalIds != null) {
            mTotalIds.clear();
            mTotalIds = null;
        }
        mAppsFragment = null;
        mManagerFragment = null;
        mSettingsFragment = null;
        instance = null;
    }
}
