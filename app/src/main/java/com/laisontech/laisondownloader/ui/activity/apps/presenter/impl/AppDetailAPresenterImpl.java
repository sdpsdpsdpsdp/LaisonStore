package com.laisontech.laisondownloader.ui.activity.apps.presenter.impl;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.callback.OnAssignmentResultListener;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.activity.apps.presenter.inter.IAppDetailAPresenter;
import com.laisontech.laisondownloader.ui.activity.apps.view.inter.IAppDetailAView;

public class AppDetailAPresenterImpl implements IAppDetailAPresenter {
    private IAppDetailAView mIAppDetailAView;

    public AppDetailAPresenterImpl(IAppDetailAView aIAppDetailAView) {
        mIAppDetailAView = aIAppDetailAView;
    }

    /**
     * 从服务器获取App详情信息
     */
    @Override
    public void queryAppDetail(final int apkId) {
        if (mIAppDetailAView == null) return;
        mIAppDetailAView.startLoad();
        HttpClient.queryAppDetailByID(apkId, new OnAssignmentResultListener<LaisonAppBean>() {
            @Override
            public void onSuccess(int errorCode, LaisonAppBean appBean) {
                if (mIAppDetailAView != null) {
                    mIAppDetailAView.hideLoad();
                    if (errorCode != HttpConst.CODE_SUCCESS || appBean == null) {
                        mIAppDetailAView.showMsg(R.string.GetDataError);
                    }
                    mIAppDetailAView.showAppDetail(appBean);
                }
            }

            @Override
            public void onFailed(String failMsg) {
                if (mIAppDetailAView != null) {
                    mIAppDetailAView.hideLoad();
                    mIAppDetailAView.showMsg(failMsg);
                    mIAppDetailAView.showAppDetail(null);
                }
            }
        });
    }
}
