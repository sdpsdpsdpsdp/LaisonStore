package com.laisontech.laisondownloader.ui.fragment.view.inter;

import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.asynctask.LoadAppInfoTask;

import java.util.List;
import java.util.Map;

public interface IManageFView extends BaseView {

    LoadAppInfoTask getLoadAppInfoTask();

    void setLoadAppInfoTask(LoadAppInfoTask loadAppInfoTask);

    void showAppInfoList(List<LaisonAppBean> appBeanList, Map<Integer, String> stickKeys);

    boolean isStopCheckUpdate();

    void setStopCheckUpdate(boolean stopCheck);

    void showUpdateAppInfo(LaisonAppBean appBean);

}
