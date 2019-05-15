package com.laisontech.laisondownloader.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.mvp.net.okserver.OkHttpDownload;
import com.laisontech.mvp.net.okserver.download.DownloadTask;

/**
 * Created by SDP
 * on 2019/5/10
 * Des：
 */
public class OperateTaskDialog extends Dialog implements View.OnClickListener {
    private CardView cardDelete;
    private CardView cardReDownload;
    private DownloadTask mAppTask;

    public OperateTaskDialog(@NonNull Context context, LaisonAppBean appBean) {
        super(context, R.style.CustomCircleDialog);
        initData(appBean);
    }

    private void initData(LaisonAppBean appBean) {
        if (appBean != null) {
            String apkDownloadLink = appBean.getApkDownloadLink();
            OkHttpDownload instance = OkHttpDownload.getInstance();
            boolean hasTask = instance.hasTask(apkDownloadLink);
            if (hasTask) {
                mAppTask = instance.getTask(apkDownloadLink);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete);
        initViews();
        initEvents();
    }

    private void initViews() {
        cardDelete = findViewById(R.id.card_delete);
        cardReDownload = findViewById(R.id.card_reload);
    }

    private void initEvents() {
        cardDelete.setOnClickListener(this);
        cardReDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int operateType = OperateEvent.NONE;
        if (mAppTask == null) {
            operateType = OperateEvent.NONE;
        } else {
            switch (view.getId()) {
                case R.id.card_delete:
                    mAppTask.remove(true);
                    operateType = OperateEvent.DELETE;
                    break;
                case R.id.card_reload:
                    mAppTask.restart();
                    operateType = OperateEvent.RELOAD;
                    break;
            }
        }
        if (mListener != null) {
            mListener.onOperate(operateType);
        }
        dismiss();
    }

    public interface OnOperateListener {
        void onOperate(@OperateEvent.OperateType int type);
    }

    private OnOperateListener mListener;

    public OperateTaskDialog setOperateListener(OnOperateListener listener) {
        this.mListener = listener;
        return this;
    }

    public OperateTaskDialog setCanCancel(boolean canCancel) {
        this.setCanceledOnTouchOutside(canCancel);
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mAppTask = null;
    }
}
