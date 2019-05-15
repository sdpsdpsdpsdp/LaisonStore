package com.laisontech.laisondownloader.callback;


import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

/**
 * Created by SDP on 2018/1/11.
 */

public interface OnShowDownloadProgressListener {
    void onShowProgress(Progress progress);
}
