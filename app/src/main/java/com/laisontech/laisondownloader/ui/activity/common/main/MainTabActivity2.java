package com.laisontech.laisondownloader.ui.activity.common.main;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.entitys.event.EventSender;
import com.laisontech.laisondownloader.callback.OnNetConnectStatusListener;
import com.laisontech.laisondownloader.service.AppInstallReceiver;
import com.laisontech.laisondownloader.service.NetConnectReceiver;
import com.laisontech.laisondownloader.entitys.event.EventSender;
import com.laisontech.laisondownloader.service.UpdateDownloadProgressService;
import com.laisontech.laisondownloader.ui.activity.apps.view.activity.SearchActivity;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;
import com.laisontech.laisondownloader.ui.base.MainTabFactory;
import com.laisontech.laisondownloader.utils.StatusBarUtil;
import com.laisontech.laisondownloader.utils.ViewUtils;
import com.laisontech.laisondownloader.utils.NetCheckUtils;
import com.laisontech.laisondownloader.ui.activity.apps.view.activity.SearchActivity;
import com.laisontech.laisondownloader.ui.base.MainTabFactory;
import com.laisontech.laisondownloader.utils.ViewUtils;
import com.laisontech.mvp.net.okserver.OkHttpDownload;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：
 */
public class MainTabActivity2 extends BaseActivity2 implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.ll_no_net_warn)
    LinearLayout llNoNetWarn;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.et_search)
    EditText etSearch;
    private boolean isRegisterNet = false;
    private boolean isRegisterApp = false;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main_tab2;
    }


    @Override
    protected void initEvent() {
        setNeedOnKeyDown(true);
        tvSearch.setVisibility(View.VISIBLE);
        StatusBarUtil.setStatusBarColor(this, R.color.white);
        //模式默认显示第一个
        onTabSelected(MainTabFactory.DEFAULT_ID);
        //监听选择事件
        navigation.setOnNavigationItemSelectedListener(this);
        //监听网络信息
        if (!isRegisterNet) {
            isRegisterNet = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mConnectReceiver, filter);
        }
        //监听App安装信息
        if (!isRegisterApp) {
            isRegisterApp = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(AppInstallReceiver.INSTALL);
            filter.addAction(AppInstallReceiver.UNINSTALL);
            filter.addAction(AppInstallReceiver.REPLACED);
            filter.addDataScheme(AppInstallReceiver.PACKAGE);
            registerReceiver(mInstallReceiver, filter);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        onTabSelected(item.getItemId());
        return true;
    }

    /**
     * 设置选中的position
     */
    private void onTabSelected(@IdRes int itemId) {
        MainTabFactory tabFactory = MainTabFactory.getInstance();
        Fragment selectFragment = tabFactory.getFragment(itemId);
        if (selectFragment == null) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!selectFragment.isAdded()) {
            transaction.add(R.id.fl_container, selectFragment, itemId + "");
        }
        transaction.show(selectFragment);

        Set<Integer> unSelectIds = tabFactory.getUnSelectIds(itemId);
        if (unSelectIds != null && unSelectIds.size() > 0) {
            for (int unSelectId : unSelectIds) {
                transaction.hide(tabFactory.getFragment(unSelectId));
            }
        }
        transaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterNet) {
            unregisterReceiver(mConnectReceiver);
            isRegisterNet = false;
        }
        if (isRegisterApp) {
            unregisterReceiver(mInstallReceiver);
            isRegisterApp = false;
        }
        UpdateDownloadProgressService.closeNotify();
        MainTabFactory.getInstance().clear();
        OkHttpDownload.getInstance().pauseAll();
    }

    //注册网络监听
    private NetConnectReceiver mConnectReceiver = new NetConnectReceiver(new OnNetConnectStatusListener() {
        @Override
        public void onConnectStatus(boolean isConnect) {
            llNoNetWarn.setVisibility(isConnect ? View.GONE : View.VISIBLE);
            EventSender.sendNetConnect(isConnect);
        }
    });
    //注册App安装广播
    AppInstallReceiver mInstallReceiver = new AppInstallReceiver();

    @OnClick({R.id.ll_no_net_warn, R.id.ll_search_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_no_net_warn: //打开网络设置界面
                if (!NetCheckUtils.getNetworkState(this) && llNoNetWarn.getVisibility() == View.VISIBLE) {
                    ViewUtils.openConnectNetActivity(this);
                }
                break;
            case R.id.ll_search_app: //搜索App
                openActivity(SearchActivity.class);
                overridePendingTransition(0, 0);
                break;
        }

    }
}
