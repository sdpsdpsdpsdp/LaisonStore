package com.laisontech.laisondownloader.asynctask;

import android.os.AsyncTask;

import com.laisontech.laisondownloader.callback.LoadDataFromDBListener;

/**
 * Created by SDP
 * on 2019/5/10
 * Des：
 */
public abstract class BaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private LoadDataFromDBListener<Result> mListener;

    public BaseTask(LoadDataFromDBListener<Result> listener) {
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.startLoading();
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (mListener != null) {
            mListener.loadDataFinished(result);
        }
    }

    /**
     * 取消请求
     */
    public void cancelQuery() {
        if (!isCancelled() && getStatus() == AsyncTask.Status.RUNNING) {
            cancel(true);
        }
    }
}