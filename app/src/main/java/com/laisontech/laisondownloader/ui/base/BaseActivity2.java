package com.laisontech.laisondownloader.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.customeview.CustomCircleDialog;
import com.laisontech.laisondownloader.entitys.event.NetConnectEvent;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.customeview.CustomCircleDialog;
import com.laisontech.laisondownloader.entitys.event.NetConnectEvent;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.global.GlobalData;
import com.laisontech.laisondownloader.utils.ActionBarSetter;
import com.laisontech.laisondownloader.utils.ActivityStack;
import com.laisontech.laisondownloader.utils.ActionBarSetter;
import com.laisontech.laisondownloader.utils.ActivityStack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：
 */
public abstract class BaseActivity2 extends AppCompatActivity {
    private long currentTime = 0;
    private boolean sNeedOnKeyDown = false;
    private CustomCircleDialog mDialogWaiting;
    private boolean justLoadOnceData = false;

    //设置是否需要退出提示
    protected void setNeedOnKeyDown(boolean isNeedOnKeyDown) {
        this.sNeedOnKeyDown = isNeedOnKeyDown;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        setContentView(setLayoutId());
        ButterKnife.bind(this);
        ActivityStack.getScreenManager().pushActivity(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initEvent();
    }

    protected void initPresenter() {
    }

    protected abstract @LayoutRes
    int setLayoutId();


    protected void initData() {

    }

    protected void initEvent() {

    }

    /**
     * onResume的时候只加载一次数据
     */
    protected void justLoadOnce() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(NetConnectEvent event) {
        if (event == null) return;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveOperateEvent(OperateEvent operateEvent) {
        if (operateEvent == null) return;
        switch (operateEvent.getOperateType()) {
            case OperateEvent.NONE:
                break;
            case OperateEvent.ADD:
            case OperateEvent.DELETE:
            case OperateEvent.FILE_NOT_EXIST:
                reloadData();
                break;
            case OperateEvent.START:
            case OperateEvent.PARSE:
            case OperateEvent.RELOAD:
                notifyData();
                break;
        }
    }

    protected void reloadData() {
    }

    protected void notifyData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!justLoadOnceData) {
            justLoadOnceData = true;
            justLoadOnce();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sNeedOnKeyDown = false;
        justLoadOnceData = false;
        mDialogWaiting = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        ActivityStack.getScreenManager().popActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (sNeedOnKeyDown) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (System.currentTimeMillis() - currentTime > 2000) {
                    showToast(R.string.exit);
                    currentTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void showToast(Object obj) {
        showToast(obj, Toast.LENGTH_SHORT);
    }

    protected void showToast(Object obj, int duration) {
        String msg = getString(obj);
        if (msg.isEmpty()) return;
        Toast.makeText(GlobalData.getInstance().getLocalContext(), msg, duration).show();
    }

    private String getString(Object obj) {
        String msg = "";
        if (obj instanceof Integer) {
            msg = getStringRes((Integer) obj);
        } else if (obj instanceof String) {
            msg = (String) obj;
        }
        return msg;
    }

    protected String getStringRes(@StringRes int resId) {
        return getResources().getString(resId);
    }

    /**
     * 显示等待提示框
     */
    protected void showWaitingDialog(Object msg) {
        showWaitingDialog(msg, false);
    }

    /**
     * 显示等待提示框
     */
    protected void showWaitingDialog(Object tip, boolean needCancelable) {
        if (isFinishing()) return;
        View view = View.inflate(this, R.layout.include_waiting_dialog, null);
        if (mDialogWaiting == null) {
            mDialogWaiting = new CustomCircleDialog(this, R.style.CustomCircleDialog);
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

    /**
     * 隐藏等待提示框
     */
    protected void hideWaitingDialog() {
        if (mDialogWaiting != null && mDialogWaiting.isShowing()) {
            mDialogWaiting.dismiss();
        }
    }

    //设置自定义的actionBar,子类只需要复写此方法即可
    protected void setCustomActionBar(final AppCompatActivity activity, Object obj) {
        ActionBarSetter.ActionBarView actionBarView = ActionBarSetter.setCustomActionBar(activity);
        actionBarView.leftLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = activity.getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                activity.finish();
            }
        });
        setTvRes(obj, actionBarView);
    }

    private void setTvRes(Object obj, ActionBarSetter.ActionBarView actionBarView) {
        if (obj instanceof Integer) {
            actionBarView.tvTitle.setText(getString((int) obj));
        } else if (obj instanceof String) {
            actionBarView.tvTitle.setText((String) obj);
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    //传入Bundle的方式打开activity
    protected void openActivity(Bundle bundle, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String keyName, String valueName, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(keyName, valueName);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String parcelableName, Parcelable parcelable, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(parcelableName, parcelable);
        startActivity(intent);
    }

    //传入Bundle的方式打开activity
    protected void openActivity(String serializableName, Serializable serializable, Class<?> clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtra(serializableName, serializable);
        startActivity(intent);
    }

    //打开Activity
    protected void openActivity(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    //startActivityForResult
    protected void openActivityForResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    //startActivityForResult 传入Bundle
    protected void openActivityForResult(Bundle bundle, Class<?> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }
}
