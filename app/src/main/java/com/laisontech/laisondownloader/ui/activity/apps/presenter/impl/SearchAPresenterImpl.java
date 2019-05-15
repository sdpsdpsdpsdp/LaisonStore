package com.laisontech.laisondownloader.ui.activity.apps.presenter.impl;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.callback.LoadDataFromDBListener;
import com.laisontech.laisondownloader.callback.OnAssignmentResultListener;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfo;
import com.laisontech.laisondownloader.asynctask.LoadSearchRecordTask;
import com.laisontech.laisondownloader.asynctask.SaveSearchRecordTask;
import com.laisontech.laisondownloader.ui.activity.apps.presenter.inter.ISearchAPresenter;
import com.laisontech.laisondownloader.ui.activity.apps.view.inter.ISearchAView;
import com.laisontech.laisondownloader.utils.ViewUtils;

import java.util.List;

public class SearchAPresenterImpl implements ISearchAPresenter {
    private ISearchAView mISearchAView;

    public SearchAPresenterImpl(ISearchAView aISearchAView) {
        mISearchAView = aISearchAView;
    }

    @Override
    public void querySearchRecord() {
        if (mISearchAView == null) return;
        LoadSearchRecordTask loadTask = mISearchAView.getLoadTask();
        if (loadTask != null) {
            loadTask.cancelQuery();
        }
        loadTask = new LoadSearchRecordTask(new LoadDataFromDBListener<List<LastSearchInfo>>() {
            @Override
            public void startLoading() {
                if (mISearchAView != null) {
                    mISearchAView.startLoad();
                }
            }

            @Override
            public void loadDataFinished(List<LastSearchInfo> searchInfoList) {
                if (mISearchAView != null) {
                    mISearchAView.hideLoad();
                    mISearchAView.showSearchRecord(searchInfoList);
                }
            }
        });
        mISearchAView.setLoadTask(loadTask);
        loadTask.execute();
    }

    @Override
    public void saveSearchRecord() {
        if (mISearchAView == null) return;
        SaveSearchRecordTask saveTask = mISearchAView.getSaveTask();
        if (saveTask != null) {
            saveTask.cancelQuery();
        }
        saveTask = new SaveSearchRecordTask(new LoadDataFromDBListener<Boolean>() {
            @Override
            public void startLoading() {
                if (mISearchAView != null) {
                    mISearchAView.startLoad();
                }
            }

            @Override
            public void loadDataFinished(Boolean object) {
                if (mISearchAView != null) {
                    mISearchAView.hideLoad();
                }
            }
        });
        mISearchAView.setSaveTask(saveTask);
        saveTask.execute(mISearchAView.getSearchRecord());
    }

    @Override
    public void searchAppList() {
        if (mISearchAView == null) return;
        HttpClient.cancelTag(HttpConst.TAG_SEARCH);
        String searchRecord = mISearchAView.getSearchRecord();
        if (ViewUtils.isEmptyStr(searchRecord)) {
            searchRecord = GlobalData.getInstance().getRes(R.string.ErrorParams);
        }
        mISearchAView.startLoad();
        HttpClient.searchAppList(searchRecord, mISearchAView.getSearchIndex(), new OnAssignmentResultListener<List<LaisonAppBean>>() {
            @Override
            public void onSuccess(int errorCode, List<LaisonAppBean> appBeanList) {
                if (mISearchAView != null) {
                    mISearchAView.hideLoad();
                    mISearchAView.showSearchResult(appBeanList);
                }
            }

            @Override
            public void onFailed(String failMsg) {
                if (mISearchAView != null) {
                    mISearchAView.hideLoad();
                    mISearchAView.showMsg(failMsg);
                    mISearchAView.showSearchResult(null);
                }
            }
        });
    }
}
