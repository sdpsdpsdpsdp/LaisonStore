package com.laisontech.laisondownloader.global;

import android.app.Application;
import android.content.Context;

import com.laisontech.laisondownloader.constants.Const;
import com.laisontech.laisondownloader.constants.Const;
import com.laisontech.laisondownloader.crash.CrashHandler;
import com.laisontech.laisondownloader.utils.ActivityStack;
import com.laisontech.laisondownloader.utils.PackageUtils;
import com.laisontech.mvp.net.okserver.OkHttpDownload;
import com.laisontech.mvp.net.okserver.okhttp.OkServer;
import com.laisontech.mvp.net.okserver.okhttp.db.DownloadManager;


/**
 * Created by SDP
 * on 2019/5/7
 * Des：
 */
public class LSApp extends Application {
    public static Context mLocalContext;
    public static boolean mCanUploadApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalContext = getApplicationContext();
        mCanUploadApp = true;
        initParams();
    }

    private void initParams() {
        initCrash();
        initDownloader();
    }

    private void initCrash() {
        CrashHandler.getInstance().init(this, new CrashHandler.OnExceptionListener() {
            @Override
            public void onException() {
                ActivityStack.getScreenManager().exitApp();
            }
        });
    }

    private void initDownloader() {
        //1.网络设置
        OkServer.getInstance().init(this);
        //2.设置下载路径
        OkHttpDownload.getInstance().setFolder(PackageUtils.getLaisonProjectPath(this, "apks"));
        //3.设置同时下载个数
        OkHttpDownload.getInstance().getThreadPool().setCorePoolSize(Const.SYNC_DOWNLOAD_COUNT);
        //4.从数据库中恢复数据到内存中
        OkHttpDownload.restore(DownloadManager.getInstance().getAll());
    }
}
