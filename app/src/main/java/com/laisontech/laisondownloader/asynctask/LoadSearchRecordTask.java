package com.laisontech.laisondownloader.asynctask;

import com.laisontech.laisondownloader.callback.LoadDataFromDBListener;
import com.laisontech.laisondownloader.db.OperateDBUtils;
import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfo;

import java.util.List;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class LoadSearchRecordTask extends BaseTask<Void, Void, List<LastSearchInfo>> {
    public LoadSearchRecordTask(LoadDataFromDBListener<List<LastSearchInfo>> listener) {
        super(listener);
    }

    @Override
    protected List<LastSearchInfo> doInBackground(Void... voids) {
        return OperateDBUtils.queryLastSearchList();
    }
}
