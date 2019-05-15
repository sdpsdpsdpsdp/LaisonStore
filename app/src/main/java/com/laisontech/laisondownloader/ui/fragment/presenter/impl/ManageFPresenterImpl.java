package com.laisontech.laisondownloader.ui.fragment.presenter.impl;


import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.asynctask.LoadAppInfoTask;
import com.laisontech.laisondownloader.callback.LoadDataFromDBListener;
import com.laisontech.laisondownloader.callback.OnAssignmentResultListener;
import com.laisontech.laisondownloader.entitys.bean.BuildManagerBean;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.fragment.presenter.inter.IManageFPresenter;
import com.laisontech.laisondownloader.ui.fragment.view.inter.IManageFView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ManageFPresenterImpl implements IManageFPresenter {
    private IManageFView mIManageFView;

    public ManageFPresenterImpl(IManageFView fIManageFView) {
        mIManageFView = fIManageFView;
    }

    /**
     * 从数据库中获取App信息
     */
    @Override
    public void loadDataFromDB() {
        if (mIManageFView == null) return;
        LoadAppInfoTask loadAppInfoTask = mIManageFView.getLoadAppInfoTask();
        if (loadAppInfoTask != null) {
            loadAppInfoTask.cancelQuery();
        }
        loadAppInfoTask = new LoadAppInfoTask(new LoadDataFromDBListener<List<BuildManagerBean>>() {
            @Override
            public void startLoading() {
                if (mIManageFView != null) {
                    mIManageFView.startLoad();
                }
            }

            @Override
            public void loadDataFinished(List<BuildManagerBean> managerBeanList) {
                if (mIManageFView != null) {
                    mIManageFView.hideLoad();
                    if (managerBeanList == null || managerBeanList.size() < 1) {
                        mIManageFView.showAppInfoList(null, null);
                        return;
                    }
                    Map<Integer, String> keys = new LinkedHashMap<>();
                    List<LaisonAppBean> totalApps = new ArrayList<>();
                    for (int i = 0; i < managerBeanList.size(); i++) {
                        BuildManagerBean managerBean = managerBeanList.get(i);
                        if (managerBean == null) continue;
                        List<LaisonAppBean> apps = managerBean.getApps();
                        keys.put(totalApps.size(), managerBean.getTitle());
                        totalApps.addAll(apps);
                    }
                    mIManageFView.showAppInfoList(totalApps, keys);
                }
            }
        });
        mIManageFView.setLoadAppInfoTask(loadAppInfoTask);
        loadAppInfoTask.execute();
    }

    /**
     * 联网查询App状态
     */
    @Override
    public void executeCheckUpdateTask(List<LaisonAppBean> appBeanList) {
        if (appBeanList == null || appBeanList.size() < 1) return;
        for (LaisonAppBean appBean : appBeanList) {
            if (mIManageFView == null) {
                return;
            }
            if (mIManageFView.isStopCheckUpdate()) {
                HttpClient.cancelTag(HttpConst.TAG_APP_DETAIL_BY_PACKAGE);
                return;
            }
            HttpClient.queryAppDetailByPackage(appBean.getProjectName(), new OnAssignmentResultListener<LaisonAppBean>() {
                @Override
                public void onSuccess(int errorCode, LaisonAppBean appBean) {
                    if (mIManageFView == null || mIManageFView.isStopCheckUpdate()) return;
                    if (errorCode == HttpConst.CODE_SUCCESS && appBean != null) {
                        mIManageFView.showUpdateAppInfo(appBean);
                    } else {
                        mIManageFView.showUpdateAppInfo(null);
                    }
                }

                @Override
                public void onFailed(String failMsg) {
                    if (mIManageFView == null || mIManageFView.isStopCheckUpdate()) return;
                    mIManageFView.showUpdateAppInfo(null);
                }
            });
        }
    }
}
