package com.laisontech.laisondownloader.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.callback.DownloadTaskListener;
import com.laisontech.laisondownloader.callback.OnDownloadListener;
import com.laisontech.laisondownloader.callback.OnProgressStateChangeListener;
import com.laisontech.laisondownloader.customeview.DownloadProgressBar;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.loader.DownloadTaskLoader;
import com.laisontech.laisondownloader.service.UpdateDownloadProgressService;
import com.laisontech.laisondownloader.utils.CalculateUtils;
import com.laisontech.laisondownloader.utils.DateUtils;
import com.laisontech.laisondownloader.utils.DisplayUtil;
import com.laisontech.mvp.net.okserver.OkHttpDownload;
import com.laisontech.mvp.net.okserver.download.DownloadTask;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class CheckUpdateDialog extends Dialog implements View.OnClickListener{
    private TextView tvTitle;
    private TextView tvCompany;
    private TextView tvAppVersionName;
    private TextView tvAppSize;
    private TextView tvReleaseDate;
    private LinearLayout ll_updateContent;
    private TextView tvUpdates;
    private TextView tvUpdateContent;
    private Button btnCancel;
    private DownloadProgressBar downloadBar;
    private FrameLayout flClose;
    private Activity mContext;
    private int mRequestWidth;
    private LaisonAppBean mUpdateInfo;
    private boolean isForceUpdating = false;//是否可以关闭dialog
    private OkHttpDownload mHttpDownload;//任务下载器

    public CheckUpdateDialog(@NonNull Activity context, int requestWidth, LaisonAppBean appUpdateInfo) {
        super(context, R.style.CustomCircleDialog);
        mContext = context;
        mUpdateInfo = appUpdateInfo;
        mRequestWidth = DisplayUtil.px2dip(mContext, requestWidth) - 20 * 2;//先转换为dp
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_app_layout);
        if (mRequestWidth != 0) {
            Window window = getWindow();
            if (window != null) {
                window.setLayout(DisplayUtil.dip2px(mContext, mRequestWidth), LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initViews();
        initData();
        initEvents();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvAppVersionName = (TextView) findViewById(R.id.tv_app_versionName);
        tvAppSize = (TextView) findViewById(R.id.tv_app_size);
        tvReleaseDate = (TextView) findViewById(R.id.tv_app_ReleaseDate);
        ll_updateContent = (LinearLayout) findViewById(R.id.ll_app_update_content);
        tvUpdates = (TextView) findViewById(R.id.tv_updates);
        tvUpdateContent = (TextView) findViewById(R.id.tv_app_update_content);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        flClose = (FrameLayout) findViewById(R.id.fl_close);
        downloadBar = (DownloadProgressBar) findViewById(R.id.progress_bar);
    }

    private void initData() {
        isForceUpdating = false;//1立即更新
        mHttpDownload = OkHttpDownload.getInstance();
    }

    private void initEvents() {
        btnCancel.setOnClickListener(this);
        flClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_cancel) {
            if (btnCancel.getText().equals(mContext.getResources().getString(R.string.backgroundDownload))) {
                //开始service发送消息
                UpdateDownloadProgressService.openNotify(mUpdateInfo);
                this.dismiss();
            } else if (btnCancel.getText().equals(mContext.getResources().getString(R.string.update_notnow))) {
                //下次再说：暂停下载，结束界面。
                boolean hasTask = mHttpDownload.hasTask(mUpdateInfo.getApkDownloadLink());
                if (hasTask) {
                    DownloadTask task = mHttpDownload.getTask(mUpdateInfo.getApkDownloadLink());
                    task.pause();
                }
                this.dismiss();
            } else {
                this.dismiss();
            }
        } else if (i == R.id.fl_close) {
        }
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    private void refreshView() {
        tvTitle.setText(R.string.update_updatetitle);
        setText(tvCompany, R.string.Company, mUpdateInfo.getCompanyName());
        setText(tvAppVersionName, R.string.Version, "V" + mUpdateInfo.getVersionName());
        if (mUpdateInfo.getApkFileSize() != 0) {
            setText(tvAppSize, R.string.Size, CalculateUtils.getAppSize(mUpdateInfo.getApkFileSize()));
        } else {
            tvAppSize.setVisibility(View.GONE);
        }
        setText(tvReleaseDate, R.string.ReleaseDate, DateUtils.formatDetailDate(mUpdateInfo.getUpdateDate()));
        String updateinfo = mUpdateInfo.getUpdateDescription();
        if (updateinfo == null || updateinfo.isEmpty()) {
            ll_updateContent.setVisibility(View.GONE);
        } else {
            ll_updateContent.setVisibility(View.VISIBLE);
            tvUpdates.setText(R.string.Updates);
            tvUpdateContent.setText(updateinfo);
        }
        if (isForceUpdating) {//强制更新不可点击取消
            btnCancel.setVisibility(View.GONE);
            flClose.setVisibility(View.VISIBLE);
        } else {//不是强制更新关闭按钮不显示
            flClose.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setText(R.string.update_notnow);
        }
        DownloadTaskLoader.getLoader().checkAppStatus(downloadBar, mUpdateInfo, mDownloadTaskListener);
    }

    private DownloadTaskListener mDownloadTaskListener = new DownloadTaskListener(new OnDownloadListener() {
        @Override
        public void progress(Progress progress) {
            switch (progress.status) {
                case Progress.PAUSE:
                case Progress.FINISH:
                    btnCancel.setText(R.string.update_notnow);
                    break;
                case Progress.LOADING:
                    btnCancel.setText(R.string.backgroundDownload);
                    break;

            }
            DownloadTaskLoader.getLoader().refreshDownloadProgress(progress, downloadBar);
        }
    });

    public CheckUpdateDialog setCanCancel(boolean canCancel) {
        this.setCancelable(canCancel);
        return this;
    }


    public interface OnDialogClickListener {
        void onClick(boolean isConfirm);
    }

    public interface OnForceCancelUpdateListener {
        void onForceCancelUpdate(boolean forceCancel);
    }

    private void setText(TextView tv, int resBeforeId, String msg) {
        if (TextUtils.isEmpty(msg)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(getStr(resBeforeId, msg));
        }
    }

    private String getStr(int resId, String msg) {
        return mContext.getResources().getString(resId) + " " + msg;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveOperateEvent(OperateEvent operateEvent) {
        if (operateEvent == null) return;
        if (operateEvent.getOperateType() == OperateEvent.FILE_NOT_EXIST) {
            DownloadTaskLoader.getLoader().checkAppStatus(downloadBar, mUpdateInfo, mDownloadTaskListener);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
