package com.laisontech.laisondownloader.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.customeview.CustomCircleDialog;
import com.laisontech.laisondownloader.entitys.event.NetConnectEvent;
import com.laisontech.laisondownloader.global.LSApp;
import com.laisontech.laisondownloader.customeview.CustomCircleDialog;
import com.laisontech.laisondownloader.entitys.event.AppOperateEvent;
import com.laisontech.laisondownloader.entitys.event.NetConnectEvent;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.global.LSApp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：
 */
public abstract class BaseLazyFragment extends Fragment {
    private boolean hasCreateView;
    private boolean isFragmentVisible;
    private View mContentView;
    private Unbinder mUnbinder;
    private CustomCircleDialog mDialogWaiting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        initPresenter();
    }

    protected void initPresenter() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getResLayoutId(), container, false);
    }

    protected abstract
    @LayoutRes
    int getResLayoutId();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView = view;
        mUnbinder = ButterKnife.bind(this, mContentView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initEvents();
        loadData();
        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mContentView == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }

    }

    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            lazyData();
        }
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }

    protected void loadData() {
    }

    /**
     * 懒加载数据，可以用于联网请求数据
     */
    protected void lazyData() {
    }

    protected void initEvents() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(NetConnectEvent event) {
        if (event == null) return;
        netConnectStatus(event.isNetConnect());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveOperateEvent(OperateEvent operateEvent) {
        if (operateEvent == null) return;
        switch (operateEvent.getOperateType()) {
            case OperateEvent.NONE:
                break;
            case OperateEvent.ADD:
            case OperateEvent.DELETE:
                reloadData();
                break;
            case OperateEvent.START:
            case OperateEvent.PARSE:
            case OperateEvent.RELOAD:
                notifyData();
                break;
            case OperateEvent.FILE_NOT_EXIST:
                showToast(R.string.FileHadDelete);
                reloadData();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppOperateEvent(AppOperateEvent event) {
        if (event == null) return;
        switch (event.getOperateType()) {
            case AppOperateEvent.INSTALL:
            case AppOperateEvent.UNINSTALL:
            case AppOperateEvent.REPLACED:
                reloadData();
                break;
        }
    }

    protected void reloadData() {
    }

    protected void notifyData() {
    }

    /**
     * 网络连接状态
     */
    protected void netConnectStatus(boolean connect) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDialogWaiting = null;
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 显示等待提示框
     */
    protected void showWaitingDialog(Object tip, boolean needCancelable) {
        View view = View.inflate(getContext(), R.layout.include_waiting_dialog, null);
        if (mDialogWaiting == null) {
            mDialogWaiting = new CustomCircleDialog(getContext(), R.style.CustomCircleDialog);
        }
        String msg = getString(tip);
        if (!TextUtils.isEmpty(msg)) {
            ((TextView) view.findViewById(R.id.tv_load_title)).setText(msg);
        }
        mDialogWaiting.setView(view);
        mDialogWaiting.setCanceledOnTouchOutside(false);
        mDialogWaiting.setCancelable(needCancelable);
        if (mDialogWaiting != null && !mDialogWaiting.isShowing()) {
            mDialogWaiting.show();
        }
    }

    private String getString(Object obj) {
        String msg = "";
        if (obj instanceof Integer) {
            msg = getResStr((Integer) obj);
        } else if (obj instanceof String) {
            msg = (String) obj;
        }
        return msg;
    }

    /**
     * 隐藏等待提示框
     */
    protected void hideWaitingDialog() {
        if (mDialogWaiting != null && mDialogWaiting.isShowing()) {
            mDialogWaiting.dismiss();
        }
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String serializableName, Serializable serializable, Class<?> clz) {
        Intent intent = new Intent(LSApp.mLocalContext, clz);
        intent.putExtra(serializableName, serializable);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String key, String value, Class<?> clz) {
        Intent intent = new Intent(LSApp.mLocalContext, clz);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    //打开Activity
    protected void openActivity(Class<?> clz) {
        Intent intent = new Intent(LSApp.mLocalContext, clz);
        startActivity(intent);
    }

    //打开Activity
    protected void openActivity(String key, boolean values, Class<?> clz) {
        Intent intent = new Intent(LSApp.mLocalContext, clz);
        intent.putExtra(key, values);
        startActivity(intent);
    }

    //startActivityForResult
    protected void openActivityForResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent(LSApp.mLocalContext, clz);
        startActivityForResult(intent, requestCode);
    }

    //startActivityForResult 传入Bundle
    protected void openActivityForResult(Bundle bundle, Class<?> clz, int requestCode) {
        Intent intent = new Intent(LSApp.mLocalContext, clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 使用反射打开activity
     *
     * @param activityName 要打开的activity全路径 com.laisontech.xxx.xxxActivity
     * @param intent       传参使用的Intent
     */
    public void navigatorTo(final String activityName, final Intent intent) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(activityName);
            if (clazz != null) {
                intent.setClass(LSApp.mLocalContext, clazz);
                this.startActivity(intent);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getResStr(int resID) {
        return getContext().getResources().getString(resID);
    }

    //添加title的布局显示
    protected void setTitleBar(TextView tv, Object obj) {
        String title = "";
        if (obj instanceof Integer) {
            title = getResStr((Integer) obj);
        } else if (obj instanceof String) {
            title = (String) obj;
        }
        tv.setText(title);
    }


    protected void showToast(Object object) {
        String diffResStr = getDiffResStr(object);
        if (diffResStr.isEmpty()) return;
        Toast.makeText(getContext(), diffResStr, Toast.LENGTH_SHORT).show();
    }

    private String getDiffResStr(Object obj) {
        String title = "";
        if (obj instanceof Integer) {
            title = getResStr((Integer) obj);
        } else if (obj instanceof String) {
            title = (String) obj;
        }
        return title;
    }
}
