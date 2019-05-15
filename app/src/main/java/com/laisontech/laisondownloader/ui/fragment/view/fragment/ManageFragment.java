package com.laisontech.laisondownloader.ui.fragment.view.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.asynctask.LoadAppInfoTask;
import com.laisontech.laisondownloader.customeview.FloatingStickItemDecoration;
import com.laisontech.laisondownloader.ui.adapter.ManageAdapter;
import com.laisontech.laisondownloader.ui.base.BaseLazyFragment;
import com.laisontech.laisondownloader.ui.fragment.presenter.impl.ManageFPresenterImpl;
import com.laisontech.laisondownloader.ui.fragment.presenter.inter.IManageFPresenter;
import com.laisontech.laisondownloader.ui.fragment.view.inter.IManageFView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：管理Fragment,该界面显示安装中...已安装的App的显示
 * 1、获取本地所有是包名com.laisontech开头的App
 * 2、获取下载任务
 * 3、判断本地是否已经更新
 */
public class ManageFragment extends BaseLazyFragment implements IManageFView, OnRefreshListener {
    @BindView(R.id.rc)
    RecyclerView rc;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_show_no_data)
    LinearLayout llShowNoData;
    private LoadAppInfoTask mInfoTask;
    private IManageFPresenter mIManageFPresenter;
    private ManageAdapter mAdapter;
    private FloatingStickItemDecoration mItemDecoration;
    private volatile boolean isStopCheck = false;

    public static Fragment getFragment() {
        return new ManageFragment();
    }

    @Override
    protected void initPresenter() {
        mIManageFPresenter = new ManageFPresenterImpl(this);
    }

    @Override
    protected int getResLayoutId() {
        return R.layout.fragment_manage;
    }

    @Override
    protected void initEvents() {
        refreshLayout.setOnRefreshListener(this);
        rc.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mItemDecoration == null) {
            mItemDecoration = new FloatingStickItemDecoration(getContext());
            mItemDecoration.initParams(android.R.color.black, R.color.white, R.color.transparent, 15, 1, 10);
            mItemDecoration.setTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
            rc.addItemDecoration(mItemDecoration);
        }
        initAdapter();
        refreshLayout.autoRefresh();
    }

    private void initAdapter() {
        if (mAdapter != null) {
            mAdapter.clearData();
            mAdapter = null;
        }
        mAdapter = new ManageAdapter(getContext());
        mAdapter.setHasStableIds(true);
        rc.setAdapter(mAdapter);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        reloadData();
    }

    @Override
    protected void reloadData() {
        setStopCheckUpdate(true);
        mIManageFPresenter.loadDataFromDB();
        setStopCheckUpdate(false);
    }

    @Override
    public void startLoad() {
    }

    @Override
    public void hideLoad() {
        refreshLayout.finishRefresh();
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
    public LoadAppInfoTask getLoadAppInfoTask() {
        return mInfoTask;
    }

    @Override
    public void setLoadAppInfoTask(LoadAppInfoTask loadAppInfoTask) {
        mInfoTask = loadAppInfoTask;
    }

    /**
     * 获取本地所有的App信息包含下载和本地的数据
     */
    @Override
    public void showAppInfoList(List<LaisonAppBean> appBeanList, Map<Integer, String> stickKeys) {
        if (stickKeys == null) {
            llShowNoData.setVisibility(View.VISIBLE);
            rc.setVisibility(View.GONE);
        } else {
            llShowNoData.setVisibility(View.GONE);
            rc.setVisibility(View.VISIBLE);
            mItemDecoration.setKeys(stickKeys);
            if (mAdapter != null) {
                mAdapter.setData(appBeanList);
            }
            mIManageFPresenter.executeCheckUpdateTask(appBeanList);
        }

    }

    @Override
    public boolean isStopCheckUpdate() {
        return this.isStopCheck;
    }

    @Override
    public void setStopCheckUpdate(boolean isStopCheck) {
        this.isStopCheck = isStopCheck;
    }

    /**
     * 从服务端最近的App信息
     */
    @Override
    public void showUpdateAppInfo(LaisonAppBean appBean) {
        if (mAdapter != null) {
            mAdapter.checkUpdateAppInfo(appBean);
        }
    }
}
