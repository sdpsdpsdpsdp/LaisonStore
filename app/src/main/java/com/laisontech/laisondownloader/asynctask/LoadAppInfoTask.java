package com.laisontech.laisondownloader.asynctask;


import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.callback.LoadDataFromDBListener;
import com.laisontech.laisondownloader.entitys.bean.BuildManagerBean;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.loader.DownloadTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SDP
 * on 2019/5/10
 * Des：
 */
public class LoadAppInfoTask extends BaseTask<Void, Void, List<BuildManagerBean>> {
    public LoadAppInfoTask(LoadDataFromDBListener<List<BuildManagerBean>> listener) {
        super(listener);
    }

    /**
     * 构建App的信息数据
     */
    @Override
    protected List<BuildManagerBean> doInBackground(Void... voids) {
        List<LaisonAppBean> serverApps = DownloadTaskLoader.buildServerApps();
        List<LaisonAppBean> localApps = DownloadTaskLoader.buildLocalApps(serverApps);
        List<BuildManagerBean> beanList = new ArrayList<>();
        if (serverApps != null && serverApps.size() > 0) {
            BuildManagerBean serverBean = new BuildManagerBean(GlobalData.getInstance().getRes(R.string.DownloadTask), serverApps);
            beanList.add(serverBean);
        }
        if (localApps != null && localApps.size() > 0) {
            BuildManagerBean localBean = new BuildManagerBean(GlobalData.getInstance().getRes(R.string.LocalManager), localApps);
            beanList.add(localBean);
        }
        return beanList;
    }
}
