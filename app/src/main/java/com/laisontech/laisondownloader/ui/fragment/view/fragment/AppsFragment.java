package com.laisontech.laisondownloader.ui.fragment.view.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.customeview.SpaceItemDecoration;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppAdData;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.callback.RecyclerItemClickListener;
import com.laisontech.laisondownloader.customeview.SpaceItemDecoration;
import com.laisontech.laisondownloader.ui.activity.apps.view.activity.AppDetailActivity;
import com.laisontech.laisondownloader.ui.adapter.AppsAdapter;
import com.laisontech.laisondownloader.ui.base.BaseLazyFragment;
import com.laisontech.laisondownloader.ui.fragment.presenter.impl.AppsFPresenterImpl;
import com.laisontech.laisondownloader.ui.fragment.presenter.inter.IAppsFPresenter;
import com.laisontech.laisondownloader.ui.fragment.view.inter.IAppsFView;
import com.laisontech.laisondownloader.ui.base.BaseLazyFragment;
import com.laisontech.laisondownloader.ui.fragment.presenter.impl.AppsFPresenterImpl;
import com.laisontech.laisondownloader.ui.fragment.presenter.inter.IAppsFPresenter;
import com.laisontech.laisondownloader.ui.fragment.view.inter.IAppsFView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：App首页展示界面，显示广告轮播，以及从服务器上获取的Apps界面显示
 */
public class AppsFragment extends BaseLazyFragment implements IAppsFView, OnRefreshListener, OnLoadMoreListener, RecyclerItemClickListener<LaisonAppBean> {
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rc)
    RecyclerView rcApps;
    @BindView(R.id.ll_show_no_data)
    LinearLayout llShowNoData;
    private boolean mExecuteQuery;
    private IAppsFPresenter mIAppsFPresenter;
    private AppsAdapter mAppsAdapter;
    private int mCurrentIndex = 0;
    private boolean mRefreshData = false;

    public static Fragment getFragment() {
        return new AppsFragment();
    }

    @Override
    protected void initPresenter() {
        mIAppsFPresenter = new AppsFPresenterImpl(this);
    }

    @Override
    protected int getResLayoutId() {
        return R.layout.include_refresh_layout;
    }

    @Override
    protected void initEvents() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        rcApps.setLayoutManager(new LinearLayoutManager(getContext()));
        rcApps.addItemDecoration(new SpaceItemDecoration(getContext()));
        if (mAppsAdapter == null) {
            mAppsAdapter = new AppsAdapter(getContext());
            mAppsAdapter.setHasStableIds(true);
            rcApps.setAdapter(mAppsAdapter);
            mAppsAdapter.setItemClickListener(this);
        }
    }

    @Override
    public void startLoad() {

    }

    @Override
    public void hideLoad() {

    }

    @Override
    public void showMsg(int resID) {
        showToast(resID);
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public boolean isExecuteQuery() {
        return mExecuteQuery;
    }

    @Override
    public void setExecuteQuery(boolean isExecute) {
        mExecuteQuery = isExecute;
    }

    @Override
    public int queryIndex() {
        return mCurrentIndex;
    }

    @Override
    public void setQueryIndex(int currentIndex) {
        mCurrentIndex = currentIndex;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadData(true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadData(false);
    }


    @Override
    public void showBannerData(List<LaisonAppAdData> appBannerData) {
        if (mAppsAdapter != null) {
            mAppsAdapter.setHeaderData(appBannerData);
        }
    }

    @Override
    public void showContentData(boolean hasContentData, List<LaisonAppBean> laisonAppBeans) {
        if (!hasContentData) {
            if (mRefreshData) {
                refreshLayout.finishRefreshWithNoMoreData();
            } else {
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
            return;
        }
        if (mAppsAdapter != null) {
            if (mRefreshData) {
                mAppsAdapter.setContentData(laisonAppBeans);
                refreshLayout.finishRefresh();
            } else {
                mAppsAdapter.addContentData(laisonAppBeans);
                refreshLayout.finishLoadMore();
            }
        }
    }

    /**
     * 列表点击事件
     */
    @Override
    public void onItemClick(LaisonAppBean appBean) {
        AppDetailActivity.openAppActivity(getContext(), appBean);
    }

    @Override
    protected void netConnectStatus(boolean connect) {
        if (connect) {
            if (!hadContentData()) {
                refreshLayout.autoRefresh();
            } else {
                if (!hadHeaderData()) {
                    mIAppsFPresenter.queryHeaderData();
                }
            }
        }
    }

    /*
    加载数据
     */
    private void loadData(boolean isRefreshData) {
        mRefreshData = isRefreshData;
        setQueryIndex(mRefreshData ? 0 : mCurrentIndex + 1);
        if (!hadHeaderData()) {
            mIAppsFPresenter.queryHeaderData();
        }
        mIAppsFPresenter.queryContentData();
    }

    /*
    是否有头文件
     */
    private boolean hadHeaderData() {
        return mAppsAdapter != null && mAppsAdapter.hadHeaderData();
    }

    /*
    是否已经加载了内容数据
     */
    private boolean hadContentData() {
        return mAppsAdapter != null && mAppsAdapter.hadContentData();
    }

    //重新获取数据
    @Override
    protected void reloadData() {
        notifyData();
    }

    //更新界面
    @Override
    protected void notifyData() {
        if (mAppsAdapter != null) {
            mAppsAdapter.notifyDataSetChanged();
        }
    }
}

