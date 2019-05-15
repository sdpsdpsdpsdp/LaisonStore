package com.laisontech.laisondownloader.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;


import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.callback.DownloadTaskListenerImpl;
import com.laisontech.laisondownloader.callback.OnShowDownloadProgressListener;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.callback.OnShowDownloadProgressListener;
import com.laisontech.laisondownloader.callback.DownloadTaskListenerImpl;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.utils.PackageUtils;
import com.laisontech.mvp.net.okserver.OkHttpDownload;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

import java.io.Serializable;


/**
 * ..................................................................
 * .         The Buddha said: I guarantee you have no bug!          .
 * .                                                                .
 * .                            _ooOoo_                             .
 * .                           o8888888o                            .
 * .                           88" . "88                            .
 * .                           (| -_- |)                            .
 * .                            O\ = /O                             .
 * .                        ____/`---'\____                         .
 * .                      .   ' \\| |// `.                          .
 * .                       / \\||| : |||// \                        .
 * .                     / _||||| -:- |||||- \                      .
 * .                       | | \\\ - /// | |                        .
 * .                     | \_| ''\---/'' | |                        .
 * .                      \ .-\__ `-` ___/-. /                      .
 * .                   ___`. .' /--.--\ `. . __                     .
 * .                ."" '< `.___\_<|>_/___.' >'"".                  .
 * .               | | : `- \`.;`\ _ /`;.`/ - ` : | |               .
 * .                 \ \ `-. \_ __\ /__ _/ .-` / /                  .
 * .         ======`-.____`-.___\_____/___.-`____.-'======          .
 * .                            `=---='                             .
 * ..................................................................
 * Created by SDP on 2018/6/5.
 */

public class UpdateDownloadProgressService extends Service implements OnShowDownloadProgressListener {
    private static final String ACTION_START_UPDATE_NOTIFY = "com.laisontech.start_update";
    private static final String ACTION_STOP_UPDATE_NOTIFY = "com.laisontech.stop_update";
    //定义notification实用的ID
    private static final int MSG_NOTIFY_DOWNLOAD = 0x110;
    private NotificationCompat.Builder mNotifyBuilder;
    private NotificationManager mNotificationManager;
    private static final String KEY_APP_INFO = "AppInfo";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //初始化notification
        mNotifyBuilder = new NotificationCompat.Builder(this);
        mNotifyBuilder.setSmallIcon(R.mipmap.logo);
        mNotifyBuilder.setContentTitle(PackageUtils.getAppName());
        mNotifyBuilder.setContentText(getResources().getString(R.string.Downloading));
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MSG_NOTIFY_DOWNLOAD, mNotifyBuilder.build());
        mNotifyBuilder.setProgress(100, 0, false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && !action.isEmpty()) {
                switch (action) {
                    case ACTION_START_UPDATE_NOTIFY:
                        Serializable serializable = intent.getSerializableExtra(KEY_APP_INFO);
                        LaisonAppBean appUpdateInfo = (LaisonAppBean) serializable;
                        startUpdate(appUpdateInfo);
                        break;
                }
            }
        }
        return START_STICKY;
    }

    //注册下载监听事件
    private void startUpdate(LaisonAppBean appUpdateInfo) {
        if (appUpdateInfo == null) return;
        OkHttpDownload.getInstance().getTask(appUpdateInfo.getApkDownloadLink()).register(new DownloadTaskListenerImpl(this, "null", this));
    }

    //获取进度更新进度条
    @Override
    public void onShowProgress(Progress progress) {
        String currentSize = Formatter.formatFileSize(this, progress.currentSize);
        String totalSize = Formatter.formatFileSize(this, progress.totalSize);
        String speed = Formatter.formatFileSize(this, progress.speed);
        int currentProgress = (int) (progress.fraction * 100);
        mNotifyBuilder.setProgress(100, currentProgress, false);
        String sb = (currentSize + "/" + totalSize + "    ") + String.format("%s/s", speed);
        mNotifyBuilder.setContentText(sb);
        mNotificationManager.notify(MSG_NOTIFY_DOWNLOAD, mNotifyBuilder.build());
        if (currentProgress == 100) {  //取消显示
            closeService();
            mNotificationManager.cancel(MSG_NOTIFY_DOWNLOAD);
            PackageUtils.install(progress.filePath);
        }
    }

    public static void openNotify(LaisonAppBean appUpdateInfo) {
        Context context = GlobalData.getInstance().getLocalContext();
        Intent intent = new Intent(context, UpdateDownloadProgressService.class);
        intent.setAction(ACTION_START_UPDATE_NOTIFY);
        intent.putExtra(KEY_APP_INFO, appUpdateInfo);
        context.startService(intent);
    }

    public static void closeNotify() {
        Context context = GlobalData.getInstance().getLocalContext();
        Intent intent = new Intent(context, UpdateDownloadProgressService.class);
        intent.setAction(ACTION_STOP_UPDATE_NOTIFY);
        context.stopService(intent);
    }

    private void closeService() {
        this.stopSelf();
    }
}
