package com.laisontech.laisondownloader.ui.fragment.presenter.inter;

import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;

import java.util.List;

public interface IManageFPresenter {
    void loadDataFromDB();

    void executeCheckUpdateTask(List<LaisonAppBean> appBeanList);
}
