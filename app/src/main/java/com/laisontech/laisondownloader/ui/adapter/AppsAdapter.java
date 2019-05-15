package com.laisontech.laisondownloader.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.callback.RecyclerItemClickListener;
import com.laisontech.laisondownloader.customeview.DownloadProgressBar;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.loader.BannerImageLoader;
import com.laisontech.laisondownloader.loader.DownloadTaskLoader;
import com.laisontech.laisondownloader.loader.ImageUrlLoader;
import com.laisontech.laisondownloader.utils.CalculateUtils;
import com.laisontech.laisondownloader.ui.activity.common.CompanyWebViewActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：首页App显示的Adapter
 */
public class AppsAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;
    private Context mContext;
    private List<LaisonAppAdData> mHeaderData;
    private List<LaisonAppBean> mContentData;
    private RecyclerItemClickListener<LaisonAppBean> mItemClickListener;

    public void setItemClickListener(RecyclerItemClickListener<LaisonAppBean> itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public AppsAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(mContext, LayoutInflater.from(mContext).inflate(getItemLayoutId(viewType), parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return getHeaderCount() == 0 ? TYPE_CONTENT : (position == 0 ? TYPE_HEADER : TYPE_CONTENT);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        int itemViewType = holder.getItemViewType();
        switch (itemViewType) {
            case TYPE_HEADER:
                setHeaderInfo(holder);
                break;
            default:
                setContentInfo(holder, position);
                break;
        }
    }

    /**
     * 设置头布局数据
     */
    private void setHeaderInfo(RecyclerViewHolder holder) {
        if (holder == null) return;
        final List<String> imageUrls = buildHeaderStringData();
        if (imageUrls == null) return;
        Banner banner = (Banner) holder.getView(R.id.bannerView);
        banner.setImages(imageUrls);
        banner.setImageLoader(new BannerImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                CompanyWebViewActivity.openWebActivity(mContext, mHeaderData.get(position).getLinkedAddress());
            }
        });
        banner.start();
    }

    /**
     * 设置内容数据
     */
    private void setContentInfo(RecyclerViewHolder holder, int position) {
        if (holder == null || position < 0) return;
        int contentPosition = getHeaderCount() == 0 ? position : position - 1;
        final LaisonAppBean laisonAppBean = mContentData.get(contentPosition);
        //设置App的logo
        ImageUrlLoader.loadImage(mContext, holder.getImageView(R.id.iv_app_icon), laisonAppBean.getAppLogoLink());
        //设置Title
        holder.getTextView(R.id.tv_app_name).setText(laisonAppBean.getAppName());
        holder.getTextView(R.id.tv_version_name).setText("V" + laisonAppBean.getVersionName());
        holder.getTextView(R.id.tv_app_size).setText(CalculateUtils.getAppSize(laisonAppBean.getApkFileSize()));
        holder.getTextView(R.id.tv_company).setText(laisonAppBean.getCompanyName());
        final DownloadProgressBar progressBar = (DownloadProgressBar) holder.getView(R.id.progress_bar);
        //显示ProgressBar的状态
        DownloadTaskLoader.getLoader().checkAppStatus( progressBar, laisonAppBean);
        //item的点击事件
        holder.getView(R.id.card_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行跳转到App详情界面
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(laisonAppBean);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getContentCount();
    }

    private int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return R.layout.rc_apps_header_item;
            default:
                return R.layout.rc_apps_content_item;
        }
    }


    /**
     * 获取头布局的数量
     */
    private int getHeaderCount() {
        if (mHeaderData == null || mHeaderData.size() < 1) return 0;
        return 1;
    }

    /**
     * 获取内容布局的数量
     */
    private int getContentCount() {
        return mContentData == null ? 0 : mContentData.size();
    }

    /**
     * 设置头布局数据
     */
    public void setHeaderData(List<LaisonAppAdData> headerData) {
        this.mHeaderData = headerData;
        notifyDataSetChanged();
    }

    /**
     * 设置内容布局数据
     */
    public void addContentData(List<LaisonAppBean> contentData) {
        if (this.mContentData == null) {
            this.mContentData = contentData;
        } else {
            if (contentData == null || contentData.size() < 1) return;
            this.mContentData.addAll(contentData);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置内容布局数据
     */
    public void setContentData(List<LaisonAppBean> contentData) {
        this.mContentData = contentData;
        notifyDataSetChanged();
    }

    private List<String> buildHeaderStringData() {
        if (mHeaderData == null || mHeaderData.size() < 1) return null;
        List<String> imageUrls = new ArrayList<>();
        for (LaisonAppAdData appAdData : mHeaderData) {
            imageUrls.add(appAdData.getImageDownloadLink());
        }
        return imageUrls;
    }

    public boolean hadHeaderData() {
        return mHeaderData != null && mHeaderData.size() > 0;
    }

    public boolean hadContentData() {
        return mContentData != null && mContentData.size() > 0;
    }
}
