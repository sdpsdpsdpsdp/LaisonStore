package com.laisontech.laisondownloader.ui.activity.apps.view.inter;

import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfo;
import com.laisontech.laisondownloader.asynctask.LoadSearchRecordTask;
import com.laisontech.laisondownloader.asynctask.SaveSearchRecordTask;
import com.laisontech.laisondownloader.ui.fragment.view.inter.BaseView;

import java.util.List;

public interface ISearchAView extends BaseView {
    void setLoadTask(LoadSearchRecordTask baseTask);

    LoadSearchRecordTask getLoadTask();

    void setSaveTask(SaveSearchRecordTask baseTask);

    SaveSearchRecordTask getSaveTask();

    void showSearchRecord(List<LastSearchInfo> searchInfoList);

    int getSearchIndex();

    void setQueryIndex(int currentIndex);

    void setSearchRecord(String searchRecord);

    String getSearchRecord();

    void showSearchResult(List<LaisonAppBean> appBeanList);

}
