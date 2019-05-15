package com.laisontech.laisondownloader.ui.activity.apps.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfo;
import com.laisontech.laisondownloader.asynctask.LoadSearchRecordTask;
import com.laisontech.laisondownloader.asynctask.SaveSearchRecordTask;
import com.laisontech.laisondownloader.callback.RecyclerItemClickListener;
import com.laisontech.laisondownloader.customeview.SpaceItemDecoration;
import com.laisontech.laisondownloader.entitys.event.EventSender;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.activity.apps.presenter.impl.SearchAPresenterImpl;
import com.laisontech.laisondownloader.ui.activity.apps.presenter.inter.ISearchAPresenter;
import com.laisontech.laisondownloader.ui.activity.apps.view.inter.ISearchAView;
import com.laisontech.laisondownloader.ui.adapter.AppsAdapter;
import com.laisontech.laisondownloader.ui.adapter.FlowLayoutAdapter;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;
import com.laisontech.laisondownloader.utils.AnimUtils;
import com.laisontech.laisondownloader.utils.StatusBarUtil;
import com.laisontech.laisondownloader.utils.ViewUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class SearchActivity extends BaseActivity2 implements OnRefreshListener, OnLoadMoreListener, RecyclerItemClickListener<LaisonAppBean>, ISearchAView, ViewUtils.OnEditTextChangeListener, TagFlowLayout.OnTagClickListener {
    @BindView(R.id.tv_no_search_record)
    TextView tvNoSearchRecord;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.ll_search_tag_layout)
    LinearLayout llSearchTagLayout;
    @BindView(R.id.rc)
    RecyclerView rc;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_show_no_data)
    LinearLayout llShowNoData;
    @BindView(R.id.rl_search_layout)
    RelativeLayout rlSearchResultLayout;
    private LoadSearchRecordTask mLoadTask;
    private SaveSearchRecordTask mSaveTask;
    private AppsAdapter mAppsAdapter;
    private int mCurrentIndex = 0;
    private String mSearchRecord;
    private boolean mLoadMore = false;
    private ISearchAPresenter mISearchAPresenter;

    @Override
    protected void initPresenter() {
        mISearchAPresenter = new SearchAPresenterImpl(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initEvent() {
        StatusBarUtil.setStatusBarColor(this, R.color.white);
        etSearch.setVisibility(View.VISIBLE);
        ViewUtils.setEditFourceListener(etSearch, this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.addItemDecoration(new SpaceItemDecoration(this));
        if (mAppsAdapter == null) {
            mAppsAdapter = new AppsAdapter(this);
            mAppsAdapter.setHasStableIds(true);
            rc.setAdapter(mAppsAdapter);
            mAppsAdapter.setItemClickListener(this);
        }
    }

    @Override
    protected void justLoadOnce() {
        mISearchAPresenter.querySearchRecord();
    }

    @Override
    public void onChange(CharSequence cs) {
        setSearchRecord(cs.toString());
        executeLoadContentData(false);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        executeLoadContentData(false);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        executeLoadContentData(true);
    }

    @Override
    public void onItemClick(LaisonAppBean appBean) {
        AppDetailActivity.openAppActivity(this, appBean);
    }

    @Override
    public void startLoad() {
        AnimUtils.startRotateAnim(ivSearch);
    }

    @Override
    public void hideLoad() {
        AnimUtils.stopRotateAnim(ivSearch);
    }

    @Override
    public void showMsg(int resID) {
    }

    @Override
    public void showMsg(String msg) {
    }

    @Override
    public void setLoadTask(LoadSearchRecordTask loadTask) {
        mLoadTask = loadTask;
    }

    @Override
    public LoadSearchRecordTask getLoadTask() {
        return mLoadTask;
    }

    @Override
    public void setSaveTask(SaveSearchRecordTask baseTask) {
        mSaveTask = baseTask;
    }

    @Override
    public SaveSearchRecordTask getSaveTask() {
        return mSaveTask;
    }

    @Override
    public void showSearchRecord(List<LastSearchInfo> searchInfoList) {
        if (searchInfoList == null || searchInfoList.size() < 1) {
            tvNoSearchRecord.setVisibility(View.VISIBLE);
            return;
        }
        tvNoSearchRecord.setVisibility(View.GONE);
        flowlayout.setAdapter(new FlowLayoutAdapter(this, searchInfoList));
        flowlayout.setOnTagClickListener(this);
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        String queryCode = ((TextView) view).getText().toString().trim();
        if (!TextUtils.isEmpty(queryCode)) {
            etSearch.setText(queryCode);
            etSearch.setSelection(etSearch.getText().length());
        } else {
            showToast(R.string.NotEmpty);
        }
        return true;
    }

    @Override
    public int getSearchIndex() {
        return mCurrentIndex;
    }

    @Override
    public void setQueryIndex(int currentIndex) {
        mCurrentIndex = currentIndex;
    }

    @Override
    public void setSearchRecord(String searchRecord) {
        mSearchRecord = searchRecord;
    }

    @Override
    public String getSearchRecord() {
        return mSearchRecord;
    }

    private void executeLoadContentData(boolean loadMore) {
        mLoadMore = loadMore;
        setQueryIndex(loadMore ? (mCurrentIndex + 1) : 0);
        mISearchAPresenter.searchAppList();
    }

    @Override
    public void showSearchResult(List<LaisonAppBean> appBeanList) {
        if (mAppsAdapter != null) {
            if (appBeanList == null || appBeanList.size() < 1) {
                if (mLoadMore) {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    showLayout(false);
                    refreshLayout.finishRefreshWithNoMoreData();
                }
                return;
            }
            mISearchAPresenter.saveSearchRecord();
            showLayout(true);
            if (mLoadMore) {
                mAppsAdapter.addContentData(appBeanList);
                refreshLayout.finishLoadMore();
            } else {
                mAppsAdapter.setContentData(appBeanList);
                refreshLayout.finishRefresh();
            }
        }
    }

    private void showLayout(boolean hadResult) {
        rlSearchResultLayout.setVisibility(hadResult ? View.VISIBLE : View.GONE);
        llSearchTagLayout.setVisibility(hadResult ? View.GONE : View.VISIBLE);
        if (!hadResult) {
            mISearchAPresenter.querySearchRecord();
        }
    }

    //重新获取数据
    @Override
    protected void reloadData() {
        if (mAppsAdapter != null) {
            mAppsAdapter.notifyDataSetChanged();
        }
    }

    //更新界面
    @Override
    protected void notifyData() {
        if (mAppsAdapter != null) {
            mAppsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpClient.cancelTag(HttpConst.TAG_SEARCH);
        EventSender.sendOperateDownloadEvent(OperateEvent.START);
    }
}
