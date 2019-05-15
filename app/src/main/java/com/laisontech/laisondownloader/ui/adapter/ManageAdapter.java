package com.laisontech.laisondownloader.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.callback.OnProgressStateChangeListener;
import com.laisontech.laisondownloader.customeview.DownloadProgressBar;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.event.EventSender;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.loader.DownloadTaskLoader;
import com.laisontech.laisondownloader.loader.ImageUrlLoader;
import com.laisontech.laisondownloader.callback.OnAssignmentResultListener;
import com.laisontech.laisondownloader.ui.activity.apps.view.activity.AppDetailActivity;
import com.laisontech.laisondownloader.utils.CalculateUtils;
import com.laisontech.laisondownloader.ui.dialog.OperateTaskDialog;
import com.laisontech.laisondownloader.utils.PackageUtils;
import com.laisontech.mvp.net.okserver.download.DownloadListener;
import com.laisontech.mvp.net.okserver.okhttp.model.Progress;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by SDP
 * on 2019/5/10
 * Des：
 */
public class ManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<LaisonAppBean> mData;
    private LayoutInflater mInflater;

    public ManageAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

//    @NonNull
//    @Override
//    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case LaisonAppBean.TYPE_SERVER:
//                return mInflater.inflate( R.layout.rc_manager_server,parent,false);
//            default:
//                return R.layout.rc_manager_local;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//        int type = holder.getItemViewType();
//        TextView tvName = holder.getTextView(R.id.tv_app_name);
//        TextView tvVersionName = holder.getTextView(R.id.tv_version_name);
//        ImageView ivLogo = holder.getImageView(R.id.iv_app_icon);
//        DownloadProgressBar progressBar = (DownloadProgressBar) holder.getView(R.id.progress_bar);
//        TextView tvDownloadSpeed = null;
//        final LaisonAppBean appBean = mData.get(position);
//        tvName.setText(appBean.getAppName());
//        String localVersionName = PackageUtils.getAppVersionName(appBean.getProjectName());
//        String loadVersionName = appBean.getVersionName();
//        if (!TextUtils.isEmpty(localVersionName) && !localVersionName.equals(loadVersionName)) {
//            tvVersionName.setText("V" + localVersionName + "-V" + loadVersionName);
//        } else {
//            tvVersionName.setText("V" + loadVersionName);
//        }
//
//        switch (type) {
//            case LaisonAppBean.TYPE_SERVER:
//                holder.getTextView(R.id.tv_company).setText(appBean.getCompanyName());
//                tvDownloadSpeed = holder.getTextView(R.id.tv_download_speed);
//                final ImageView ivTouchDown = holder.getImageView(R.id.iv_touch_down);
//                final LinearLayout llShowIntroduce = holder.getLayoutView(R.id.ll_show_app_introduce);
//                final TextView tvIntroduce = holder.getTextView(R.id.tv_update_introduce);
//
//                ImageUrlLoader.loadImage(mContext, ivLogo, appBean.getAppLogoLink());
//
//                ivLogo.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AppDetailActivity.openAppActivity(mContext, appBean);
//                    }
//                });
//
//                View viewClick = holder.getView(R.id.ll_click_app_introduce);
//                viewClick.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {    //点击刷新显示
//                        boolean hadShow = llShowIntroduce.getVisibility() == View.VISIBLE;
//                        llShowIntroduce.setVisibility(hadShow ? View.GONE : View.VISIBLE);
//                        ImageUrlLoader.loadImage(mContext, ivTouchDown, hadShow ? R.drawable.icon_down : R.drawable.icon_up);
//                        queryAppUpdateInfo(tvIntroduce, appBean);
//                    }
//                });
//                viewClick.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        new OperateTaskDialog(mContext, appBean).setCanCancel(true)
//                                .setOperateListener(new OperateTaskDialog.OnOperateListener() {
//                                    @Override
//                                    public void onOperate(@OperateEvent.OperateType int type) {
//                                        EventSender.sendOperateDownloadEvent(type);
//                                    }
//                                }).show();
//                        return false;
//                    }
//                });
//
//                break;
//            default:
//                ivLogo.setImageDrawable(appBean.getLocalDrawable());
//                holder.getTextView(R.id.tv_app_size).setText(CalculateUtils.getAppSize(appBean.getApkFileSize()));
//                break;
//        }
//        DownloadTaskLoader.getLoader().checkAppStatus(progressBar, appBean, new DownloadTaskListener(progressBar, tvDownloadSpeed));
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LaisonAppBean.TYPE_SERVER:
                return new ServerHolder(mInflater.inflate(R.layout.rc_manager_server, parent, false));
            default:
                return new LocalHolder(mInflater.inflate(R.layout.rc_manager_local, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final LaisonAppBean appBean = mData.get(position);
        String appName = appBean.getAppName();
        String localVersionName = PackageUtils.getAppVersionName(appBean.getProjectName());
        String loadVersionName = appBean.getVersionName();
        int type = holder.getItemViewType();
        switch (type) {
            case LaisonAppBean.TYPE_SERVER:
                final ServerHolder serverHolder = (ServerHolder) holder;
                serverHolder.tvName.setText(appName);
                setVersionName(serverHolder.tvVersionName, localVersionName, loadVersionName);
                serverHolder.tvCompany.setText(appBean.getCompanyName());
                ImageView ivLogo = serverHolder.ivLogo;
                DownloadProgressBar progressBar = serverHolder.progressBar;

                ImageUrlLoader.loadImage(mContext, ivLogo, appBean.getAppLogoLink());
                queryAppUpdateInfo(serverHolder, position);

                DownloadTaskLoader.getLoader().checkAppStatus(progressBar, appBean,
                        new DownloadTaskListener(progressBar, serverHolder.tvDownloadSpeed));

                ivLogo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppDetailActivity.openAppActivity(mContext, appBean);
                    }
                });

                View viewClick = serverHolder.viewClick;
                viewClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {    //点击刷新显示
                        LinearLayout llShowIntroduce = serverHolder.llShowIntroduce;
                        boolean hadShow = llShowIntroduce.getVisibility() == View.VISIBLE;
                        llShowIntroduce.setVisibility(hadShow ? View.GONE : View.VISIBLE);
                        ImageUrlLoader.loadImage(mContext, serverHolder.ivTouchDown, hadShow ? R.drawable.icon_down : R.drawable.icon_up);
                    }
                });
                viewClick.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new OperateTaskDialog(mContext, appBean).setCanCancel(true)
                                .setOperateListener(new OperateTaskDialog.OnOperateListener() {
                                    @Override
                                    public void onOperate(@OperateEvent.OperateType int type) {
                                        EventSender.sendOperateDownloadEvent(type);
                                    }
                                }).show();
                        return false;
                    }
                });
                break;
            default:
                LocalHolder localHolder = (LocalHolder) holder;
                localHolder.tvName.setText(appName);
                setVersionName(localHolder.tvVersionName, localVersionName, loadVersionName);
                localHolder.tvAppSize.setText(CalculateUtils.getAppSize(appBean.getApkFileSize()));
                localHolder.ivLogo.setImageDrawable(appBean.getLocalDrawable());
                DownloadTaskLoader.getLoader().checkAppStatus(localHolder.tvProgress, appBean);
                break;
        }
    }

    private void setVersionName(TextView tvVersionName, String localVersionName, String
            loadVersionName) {
        if (!TextUtils.isEmpty(localVersionName) && !localVersionName.equals(loadVersionName)) {
            tvVersionName.setText("V" + localVersionName + "-V" + loadVersionName);
        } else {
            tvVersionName.setText("V" + loadVersionName);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData != null) {
            return mData.get(position).getAppType();
        }
        return 0;
    }

    public void setData(List<LaisonAppBean> data) {
        mData = data;
        notifyDataSetChanged();
    }


    /**
     * 从服务器获取最近的App信息
     */
    public void checkUpdateAppInfo(LaisonAppBean appNet) {
        if (appNet == null) return;
        LaisonAppBean appLoad = DownloadTaskLoader.queryAppInfoFromList(mData, appNet.getProjectName());
        if (appLoad == null) return;
        int codeFromNet = appNet.getVersioncode();
        int codeFromLoad = appLoad.getVersioncode();
        if (codeFromLoad >= codeFromNet) return;
        appLoad.setNewVersionInfo(appNet);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }


    /*
    下载监听事件
     */
    private class DownloadTaskListener extends DownloadListener {
        private DownloadProgressBar mProgressBar;
        private TextView mTvSpeed;

        DownloadTaskListener(DownloadProgressBar progressBar, TextView tvSpeed) {
            super("download");
            mProgressBar = progressBar;
            mTvSpeed = tvSpeed;
        }

        @Override
        public void onStart(Progress progress) {

        }

        @Override
        public void onProgress(Progress progress) {
            DownloadTaskLoader.getLoader().refreshDownloadProgress(progress, mProgressBar);
            if (mTvSpeed == null || progress == null) return;
            switch (progress.status) {
                case Progress.PAUSE:
                case Progress.NONE:
                    mTvSpeed.setText(GlobalData.getInstance().getRes(R.string.AlreayPause));
                    break;
                case Progress.LOADING:
                    String speed = Formatter.formatFileSize(mContext, progress.speed);
                    mTvSpeed.setText(String.format("%s/s", speed));
                    break;
                case Progress.FINISH:
                    mTvSpeed.setText("");
                    break;
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

    /*
    网络请求获取App的详情
     */
    private void queryAppUpdateInfo(ServerHolder holder, int position) {
        if (holder == null || position < 0 || mData == null) return;
        final LaisonAppBean localAppBean = mData.get(position);
        final TextView tvIntroduce = holder.tvIntroduce;
        HttpClient.queryAppDetailByID(localAppBean.getApkId(), new OnAssignmentResultListener<LaisonAppBean>() {
            @Override
            public void onSuccess(int errorCode, LaisonAppBean appBean) {
                if (errorCode == HttpConst.CODE_SUCCESS && appBean != null) {
                    if (appBean.getProjectName().equals(localAppBean.getProjectName())) {
                        tvIntroduce.setText(appBean.getUpdateDescription());
                    }
                }
            }

            @Override
            public void onFailed(String failMsg) {

            }
        });
    }

    private class ServerHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvVersionName;
        ImageView ivLogo;
        DownloadProgressBar progressBar;
        TextView tvDownloadSpeed;
        TextView tvCompany;
        ImageView ivTouchDown;
        LinearLayout llShowIntroduce;
        TextView tvIntroduce;
        View viewClick;

        ServerHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_app_name);
            tvVersionName = itemView.findViewById(R.id.tv_version_name);
            ivLogo = itemView.findViewById(R.id.iv_app_icon);
            progressBar = itemView.findViewById(R.id.progress_bar);
            tvDownloadSpeed = itemView.findViewById(R.id.tv_download_speed);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvDownloadSpeed = itemView.findViewById(R.id.tv_download_speed);
            ivTouchDown = itemView.findViewById(R.id.iv_touch_down);
            llShowIntroduce = itemView.findViewById(R.id.ll_show_app_introduce);
            tvIntroduce = itemView.findViewById(R.id.tv_update_introduce);
            viewClick = itemView.findViewById(R.id.ll_click_app_introduce);
        }
    }

    private class LocalHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        TextView tvName;
        TextView tvVersionName;
        TextView tvAppSize;
        TextView tvProgress;

        LocalHolder(View itemView) {
            super(itemView);
            ivLogo = itemView.findViewById(R.id.iv_app_icon);
            tvName = itemView.findViewById(R.id.tv_app_name);
            tvVersionName = itemView.findViewById(R.id.tv_version_name);
            tvAppSize = itemView.findViewById(R.id.tv_app_size);
            tvProgress = itemView.findViewById(R.id.tv_operate);
        }
    }
}
