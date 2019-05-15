package com.laisontech.laisondownloader.callback;

import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

/**
 * Created by SDP
 * on 2019/5/9
 * Des：
 */
public interface OnDownloadListener {
    void progress(Progress progress);
}