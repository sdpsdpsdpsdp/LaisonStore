package com.laisontech.laisondownloader.asynctask;

import com.laisontech.laisondownloader.callback.LoadDataFromDBListener;
import com.laisontech.laisondownloader.db.OperateDBUtils;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class SaveSearchRecordTask extends BaseTask<String, Void, Boolean> {
    public SaveSearchRecordTask(LoadDataFromDBListener<Boolean> listener) {
        super(listener);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if (strings == null || strings.length != 1) return false;
        String record = strings[0];
        return OperateDBUtils.saveSearchResult(record);
    }
}
