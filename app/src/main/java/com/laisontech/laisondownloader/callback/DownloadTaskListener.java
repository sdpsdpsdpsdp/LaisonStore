package com.laisontech.laisondownloader.callback;

import com.laisontech.mvp.net.okserver.download.DownloadListener;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

import java.io.File;

/**
 * Created by SDP
 * on 2019/5/9
 * Des：
 */
public class DownloadTaskListener extends DownloadListener {
    private OnDownloadListener mDownloadListener;

    public DownloadTaskListener(OnDownloadListener listener) {
        super("test");
        mDownloadListener = listener;
    }

    @Override
    public void onStart(Progress progress) {

    }

    @Override
    public void onProgress(Progress progress) {
        if (mDownloadListener != null) {
            mDownloadListener.progress(progress);
        }
    }

    @Override
    public void onError(Progress progress) {

    }

    @Override
    public void onFinish(File file, Progress progress) {
    }

    @Override
    public void onRemove(Progress progress) {

    }

}
