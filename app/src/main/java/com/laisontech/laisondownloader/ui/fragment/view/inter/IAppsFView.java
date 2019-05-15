package com.laisontech.laisondownloader.ui.fragment.view.inter;

import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;

import java.util.List;

public interface IAppsFView extends BaseView {
    boolean isExecuteQuery();

    void setExecuteQuery(boolean isExecute);

    int queryIndex();

    void setQueryIndex(int currentIndex);

    void showBannerData(List<LaisonAppAdData> appBannerData);

    void showContentData(boolean hasContentData, List<LaisonAppBean> laisonAppBeans);
}
