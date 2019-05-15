package com.laisontech.laisondownloader.ui.fragment.presenter.impl;

import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.callback.OnAssignmentResultListener;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.fragment.presenter.inter.IAppsFPresenter;
import com.laisontech.laisondownloader.ui.fragment.view.inter.IAppsFView;

import java.util.List;

public class AppsFPresenterImpl implements IAppsFPresenter {
    private IAppsFView mIAppsFView;

    public AppsFPresenterImpl(IAppsFView fIAppsFView) {
        mIAppsFView = fIAppsFView;
    }

    /**
     * 从网络中获取滚动条的数据
     */
    @Override
    public void queryHeaderData() {
        HttpClient.queryBanner(new OnAssignmentResultListener<List<LaisonAppAdData>>() {
            @Override
            public void onSuccess(int errorCode, List<LaisonAppAdData> laisonAppAdData) {
                if (mIAppsFView != null) {
                    mIAppsFView.showBannerData(laisonAppAdData);
                }
            }

            @Override
            public void onFailed(String failMsg) {
                if (mIAppsFView != null) {
                    mIAppsFView.showMsg(failMsg);
                    mIAppsFView.showBannerData(null);
                }
            }
        });
    }

    /**
     * 从网络中获取列表的数据
     */
    @Override
    public void queryContentData() {
        if (mIAppsFView == null) return;
        if (mIAppsFView.isExecuteQuery()) return;
        mIAppsFView.setExecuteQuery(true);
        HttpClient.queryContentData(mIAppsFView.queryIndex(), new OnAssignmentResultListener<List<LaisonAppBean>>() {
            @Override
            public void onSuccess(int errorCode, List<LaisonAppBean> laisonAppBeans) {
                if (mIAppsFView != null) {
                    mIAppsFView.setExecuteQuery(false);
                    //展示数据
                    mIAppsFView.showContentData((errorCode == HttpConst.CODE_SUCCESS && laisonAppBeans != null && laisonAppBeans.size() > 0)
                            , laisonAppBeans);
                }
            }

            @Override
            public void onFailed(String failMsg) {
                if (mIAppsFView != null) {
                    mIAppsFView.setExecuteQuery(false);
                    mIAppsFView.showMsg(failMsg);
                    mIAppsFView.showContentData(false, null);
                }
            }
        });
    }
}
