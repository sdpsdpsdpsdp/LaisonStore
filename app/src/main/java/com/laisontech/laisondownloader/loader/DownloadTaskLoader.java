package com.laisontech.laisondownloader.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.callback.OnProgressStateChangeListener;
import com.laisontech.laisondownloader.constants.Const;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.customeview.DownloadProgressBar;
import com.laisontech.laisondownloader.callback.DownloadTaskListener;
import com.laisontech.laisondownloader.callback.OnDownloadListener;
import com.laisontech.laisondownloader.entitys.event.EventSender;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.utils.PackageUtils;
import com.laisontech.mvp.net.okserver.OkHttpDownload;
import com.laisontech.mvp.net.okserver.download.DownloadListener;
import com.laisontech.mvp.net.okserver.download.DownloadTask;
import com.laisontech.mvp.net.okserver.okhttp.OkServer;
import com.laisontech.mvp.net.okserver.okhttp.db.DownloadManager;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;
import com.laisontech.mvp.net.okserver.okhttp.request.GetRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SDP
 * on 2019/5/9
 * Des：下载任务的管理类
 */
public class DownloadTaskLoader {
    private volatile static DownloadTaskLoader mLoader;
    private static final Object LOCK = new Object();

    public static DownloadTaskLoader getLoader() {
        if (mLoader == null) {
            synchronized (LOCK) {
                if (mLoader == null) {
                    mLoader = new DownloadTaskLoader();
                }
            }
        }
        return mLoader;
    }

    private DownloadTaskLoader() {
    }

    /**
     * 判断App的状态
     */
    public void checkAppStatus(DownloadProgressBar progressBar, LaisonAppBean appBean) {
        checkAppStatus(progressBar, appBean, null);
    }

    /**
     * 判断App的状态
     */
    public void checkAppStatus(final DownloadProgressBar progressBar, final LaisonAppBean appBean, DownloadListener listener) {
        if (appBean == null || progressBar == null) return;
        if (listener == null) {
            listener = new DownloadTaskListener(new OnDownloadListener() {
                @Override
                public void progress(Progress progress) {
                    refreshDownloadProgress(progress, progressBar);
                }
            });
        }
        String packageName = appBean.getProjectName();
        int serverVersionCode = appBean.getVersioncode();
        boolean appInstalled = PackageUtils.isAppInstalled(packageName);
        if (appInstalled) { //本地安装
            int localVersionCode = PackageUtils.getAppVersionCode(packageName);
            if (localVersionCode != serverVersionCode) {
                checkDownloadStatus(true, progressBar, appBean, serverVersionCode, listener);
            } else {
                progressBar.setState(DownloadProgressBar.STATUS_PROGRESS_BAR_OPEN);
            }
        } else { //本地没有安装
            checkDownloadStatus(false, progressBar, appBean, serverVersionCode, listener);
        }
        //注册Progress点击的事件
        registerProgressBarClickListener(appBean, progressBar, listener);
    }

    /*
    查看本地App的状态
     */
    public void checkAppStatus(final TextView tvProgress, final LaisonAppBean appBean) {
        if (tvProgress == null || appBean == null) return;
        String projectName = appBean.getProjectName();
        int localVersionCode = PackageUtils.getAppVersionCode(projectName);
        int queryVersionCode = appBean.getVersioncode();
        if (localVersionCode >= queryVersionCode) {
            tvProgress.setText(R.string.Open);
        } else {
            tvProgress.setText(R.string.Update);
        }
        tvProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = tvProgress.getText().toString();
                if (status.equals(GlobalData.getInstance().getRes(R.string.Open))) {
                    PackageUtils.launcherApp(appBean.getProjectName());
                } else {
                    DownloadTask downloadTask = buildNewDownloadTask(appBean);
                    hadExecuteOperate(downloadTask, OperateEvent.ADD, null);
                }
            }
        });
    }

    /**
     * 刷新进度
     */
    public void refreshDownloadProgress(Progress progress, DownloadProgressBar progressBar) {
        if (progressBar == null || progress == null)
            return;
        progressBar.setProgress((int) (progress.fraction * 100));
        switch (progress.status) {
            case Progress.NONE:
            case Progress.PAUSE:
                progressBar.setState(DownloadProgressBar.STATUS_PROGRESS_BAR_PAUSE);
                break;
            case Progress.ERROR:
                progressBar.setState(DownloadProgressBar.STATUS_PROGRESS_BAR_ERROR);
                break;
            case Progress.WAITING:
                progressBar.setState(DownloadProgressBar.STATUS_PROGRESS_BAR_WAIT);
                break;
            case Progress.FINISH:
                progressBar.setState(DownloadProgressBar.STATUS_PROGRESS_BAR_INSTALL);
                break;
            case Progress.LOADING:
                progressBar.setState(DownloadProgressBar.STATUS_PROGRESS_BAR_DOWNLOADING);
                break;
        }
    }


    /*
    注册progressBar点击事件
     */
    private void registerProgressBarClickListener(final LaisonAppBean appBean, DownloadProgressBar progressBar, final DownloadListener listener) {
        if (progressBar == null || listener == null) return;
        progressBar.setStateChangeListener(new OnProgressStateChangeListener() {
            @Override
            public void onProgressBarStateChange(int state) {
                operateProgressBar(appBean, state, listener);
            }
        });
    }

    /*
      操作ProgressBar的点击事件
     */
    private void operateProgressBar(LaisonAppBean appBean, int state, DownloadListener listener) {
        if (appBean == null || listener == null) return;
        DownloadTask downloadTask = getHadDownloadTask(appBean.getApkDownloadLink());
        //不是打开的时候添加新的任务到数据中
        if (state != DownloadProgressBar.STATUS_PROGRESS_BAR_OPEN) {
            if (downloadTask == null) {
                downloadTask = buildNewDownloadTask(appBean);
            }
        }
        switch (state) {
            case DownloadProgressBar.STATUS_PROGRESS_BAR_START_DOWNLOAD:
            case DownloadProgressBar.STATUS_PROGRESS_BAR_UPDATE:   //下载
                hadExecuteOperate(downloadTask, OperateEvent.ADD, listener);
                break;
            case DownloadProgressBar.STATUS_PROGRESS_BAR_DOWNLOADING:   //下载中
                hadExecuteOperate(downloadTask, OperateEvent.PARSE, listener);
                break;
            case DownloadProgressBar.STATUS_PROGRESS_BAR_PAUSE:  //暂停
                hadExecuteOperate(downloadTask, OperateEvent.START, listener);
                break;
            case DownloadProgressBar.STATUS_PROGRESS_BAR_ERROR: //出错
                hadExecuteOperate(buildNewDownloadTask(appBean), OperateEvent.ADD, listener);
                break;
            case DownloadProgressBar.STATUS_PROGRESS_BAR_INSTALL:  //安装
                if (downloadTask != null && downloadFileHadExist(downloadTask, listener)) {
                    PackageUtils.install(downloadTask.progress.filePath);
                }
                break;
            case DownloadProgressBar.STATUS_PROGRESS_BAR_OPEN:  //打开
                PackageUtils.launcherApp(appBean.getProjectName());
                break;

        }
    }

    /**
     * 构建新的下载任务
     */
    private DownloadTask buildNewDownloadTask(LaisonAppBean laisonAppBean) {
        if (laisonAppBean == null) return null;
        String apkDownloadLink = laisonAppBean.getApkDownloadLink();
        if (TextUtils.isEmpty(apkDownloadLink)) return null;
        DownloadTask downloadTask = getHadDownloadTask(apkDownloadLink);
        if (downloadTask != null) {
            downloadTask.remove(true);
        }
        GetRequest<File> request = OkServer.<File>get(apkDownloadLink)
                .headers("LaisonDownloader", "laisontech")
                .params("laisontechApp", "laison");
        downloadTask = OkHttpDownload.request(apkDownloadLink, request)
                .priority(laisonAppBean.getPriority())
                .extra1(laisonAppBean)
                .save();
        return downloadTask;
    }

    /*
    获取执行过下载的Task
     */
    private DownloadTask getHadDownloadTask(String link) {
        if (TextUtils.isEmpty(link)) return null;
        boolean hasTask = OkHttpDownload.getInstance().hasTask(link);
        if (hasTask) {
            return OkHttpDownload.getInstance().getTask(link);
        }
        return null;
    }

    /**
     * 设置执行的操作，并且发送消息
     */
    private void hadExecuteOperate(DownloadTask downloadTask, @OperateEvent.OperateType int type, DownloadListener listener) {
        if (downloadTask != null) {
            switch (type) {
                case OperateEvent.ADD:
                    downloadTask.start();
                    downloadTask.register(listener);
                    break;
                case OperateEvent.START:
                    if (downloadFileHadExist(downloadTask, listener)) {
                        downloadTask.start();
                    }
                    break;
                case OperateEvent.PARSE:
                    if (downloadFileHadExist(downloadTask, listener)) {
                        downloadTask.pause();
                    }
                    break;
                case OperateEvent.FILE_NOT_EXIST:
                case OperateEvent.DELETE:
                    downloadTask.remove(true);
                    break;
                case OperateEvent.RELOAD:
                    downloadTask.restart();
                    break;
                case OperateEvent.NONE:
                    break;

            }
            EventSender.sendOperateDownloadEvent(type);
        }
    }

    /*
    判断文件是否存在
     */
    private boolean downloadFileHadExist(DownloadTask downloadTask, DownloadListener listener) {
        if (downloadTask == null) return false;
        String filePath = downloadTask.progress.filePath;
        if (TextUtils.isEmpty(filePath)) {
            hadExecuteOperate(downloadTask, OperateEvent.FILE_NOT_EXIST, listener);
            return false;
        }
        File file = new File(filePath);
        if (!file.isFile()) {
            hadExecuteOperate(downloadTask, OperateEvent.FILE_NOT_EXIST, listener);
            return false;
        }
        return true;
    }


    /**
     * 判断本地是否下载了
     */
    private void checkDownloadStatus(boolean localHasInstall, final DownloadProgressBar progressBar, LaisonAppBean appBean, int serverVersionCode, DownloadListener listener) {
        if (appBean == null) return;
        DownloadTask downloadAppTask = getHadDownloadTask(appBean.getApkDownloadLink());
        if (downloadAppTask == null) {
            setProgressUpdateStatus(progressBar, localHasInstall);
        } else {
            LaisonAppBean downloadAppBean = (LaisonAppBean) downloadAppTask.progress.extra1;
            if (downloadAppBean == null) {
                hadExecuteOperate(downloadAppTask, OperateEvent.FILE_NOT_EXIST, listener);
            } else {
                int downloadVersionCode = downloadAppBean.getVersioncode();
                if (downloadVersionCode != serverVersionCode) {
                    hadExecuteOperate(downloadAppTask, OperateEvent.DELETE, listener);
                } else {
                    refreshDownloadProgress(downloadAppTask.progress, progressBar);
                }
            }
            if (listener != null) {
                downloadAppTask.register(listener);
            }
        }
    }

    /**
     * 设置更新或者下载状态
     */
    private void setProgressUpdateStatus(DownloadProgressBar progressBar, boolean localHasInstall) {
        progressBar.setState(!localHasInstall ? DownloadProgressBar.STATUS_PROGRESS_BAR_START_DOWNLOAD : DownloadProgressBar.STATUS_PROGRESS_BAR_UPDATE);
    }

    /*
       获取下载的软件
        */
    public static List<LaisonAppBean> buildServerApps() {
        List<DownloadTask> downloadTasks = OkHttpDownload.restore(DownloadManager.getInstance().getAll());
        if (downloadTasks == null || downloadTasks.size() < 1) return null;
        List<LaisonAppBean> serverApps = new ArrayList<>();
        for (int i = downloadTasks.size() - 1; i >= 0; i--) {
            DownloadTask downloadTask = downloadTasks.get(i);
            if (downloadTask == null) continue;
            LaisonAppBean appBean = (LaisonAppBean) downloadTask.progress.extra1;
            if (!canAddServerList(appBean)) continue;
            appBean.setAppType(LaisonAppBean.TYPE_SERVER);
            serverApps.add(appBean);
        }
        return serverApps;
    }

    /*
    获取本地安装过的软件，不包含系统自带软件以及不是本公司的app不显示，根据固定的前缀包名
     */
    public static List<LaisonAppBean> buildLocalApps(List<LaisonAppBean> serverApps) {
        try {
            Context context = GlobalData.getInstance().getLocalContext();
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            if (packageInfos == null || packageInfos.size() < 1) return null;
            List<LaisonAppBean> apps = new ArrayList<>();
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                String packageName = packageInfo.packageName;
                //过滤掉不是公司的app
                if (!packageName.contains(Const.BASE_PACKAGE_NAME)) {
                    continue;
                }
                //过滤掉当前app
                if (packageName.contains(context.getPackageName())) {
                    continue;
                }
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                //App信息
                LaisonAppBean laisonAppBean = new LaisonAppBean(
                        applicationInfo.loadLabel(packageManager).toString(),
                        packageName,
                        packageInfo.versionCode,
                        packageInfo.versionName,
                        PackageUtils.getPkgSize(applicationInfo),
                        applicationInfo.loadIcon(packageManager),
                        LaisonAppBean.TYPE_LOCAL);
                //如果下载了，判断版本号
                if (!canAddLocalList(serverApps, laisonAppBean)) continue;
                apps.add(laisonAppBean);
            }
            return apps;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从集合中获取某个App信息
     */
    public static LaisonAppBean queryAppInfoFromList(List<LaisonAppBean> list, String packageName) {
        if (list == null || list.size() < 1 || TextUtils.isEmpty(packageName)) {
            return null;
        }
        LaisonAppBean query = null;
        for (LaisonAppBean appBean : list) {
            if (packageName.equals(appBean.getProjectName())) {
                query = appBean;
                break;
            }
        }
        return query;
    }

    /**
     * 从集合中获取某个App信息
     */
    public static int queryAppIndexFromList(List<LaisonAppBean> list, String packageName) {
        if (list == null || list.size() < 1 || TextUtils.isEmpty(packageName)) {
            return -1;
        }
        int queryIndex = -1;
        for (int index = 0; index < list.size(); index++) {
            if (packageName.equals(list.get(index).getProjectName())) {
                queryIndex = index;
                break;
            }
        }
        return queryIndex;
    }

    /**
     * 判断当前App是否需要更新
     */
    public static boolean queryAppNeedUpdate(LaisonAppBean appBean) {
        if (appBean == null) return false;
        int netCode = appBean.getVersioncode();
        int localCode = PackageUtils.getAppVersionCode();
        return netCode > localCode;
    }

    /**
     * 是否可以添加在下载的列表中
     */
    private static boolean canAddServerList(LaisonAppBean appBean) {
        if (appBean == null) return false;
        String packageName = appBean.getProjectName();
        if (TextUtils.isEmpty(packageName)) return false;
        Context localContext = GlobalData.getInstance().getLocalContext();
        if (packageName.equals(localContext.getPackageName())) return false;
        int localVersionCode = PackageUtils.getAppVersionCode(packageName);
        int serverVersionCode = appBean.getVersioncode();
        return localVersionCode < serverVersionCode;
    }

    /**
     * 是否可以添加到本地的列表中
     */
    private static boolean canAddLocalList(List<LaisonAppBean> serverApps, LaisonAppBean localApp) {
        if (localApp == null) return false;
        LaisonAppBean serverApp = queryAppInfoFromList(serverApps, localApp.getProjectName());
        return serverApp == null || serverApp.getVersioncode() <= localApp.getVersioncode();
    }

}
