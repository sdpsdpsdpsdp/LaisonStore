package com.laisontech.laisondownloader.callback;

import android.content.Context;

import com.laisontech.mvp.net.okserver.download.DownloadListener;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

import java.io.File;

/**
 * Created by SDP on 2018/1/11.
 */

public class DownloadTaskListenerImpl extends DownloadListener {
    private OnShowDownloadProgressListener listener;
    private Context mContext;

    public DownloadTaskListenerImpl(Context context, Object tag, OnShowDownloadProgressListener listener) {
        super(tag);
        this.listener = listener;
        mContext = context;
    }

    @Override
    public void onStart(Progress progress) {

    }

    @Override
    public void onProgress(Progress progress) {
        listener.onShowProgress(progress);
    }

    @Override
    public void onError(Progress progress) {
        Throwable throwable = progress.exception;
        if (throwable != null) throwable.printStackTrace();
    }

    @Override
    public void onFinish(File file, Progress progress) {
//        Toast.makeText(mContext, R.string.DownloadInstall + progress.filePath, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemove(Progress progress) {

    }
}
