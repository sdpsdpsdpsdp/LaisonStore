package com.laisontech.laisondownloader.entitys.bean;

import java.util.List;

/**
 * Created by SDP
 * on 2019/5/10
 * Des：构建显示Manager界面的数据类
 */
public class BuildManagerBean {
    private String mTitle;
    private List<LaisonAppBean> mApps;

    public BuildManagerBean(String title, List<LaisonAppBean> data) {
        this.mTitle = title;
        this.mApps = data;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public List<LaisonAppBean> getApps() {
        return mApps;
    }

    public void setApps(List<LaisonAppBean> apps) {
        this.mApps = apps;
    }
}
