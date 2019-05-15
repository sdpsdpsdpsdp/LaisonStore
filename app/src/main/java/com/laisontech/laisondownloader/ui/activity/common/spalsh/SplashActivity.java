package com.laisontech.laisondownloader.ui.activity.common.spalsh;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;
import com.laisontech.laisondownloader.ui.activity.common.main.MainTabActivity2;
import com.laisontech.laisondownloader.utils.PackageUtils;
import com.laisontech.laisondownloader.ui.activity.common.main.MainTabActivity2;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;
import com.laisontech.laisondownloader.utils.PackageUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by SDP on 2017/9/28.
 */

public class SplashActivity extends BaseActivity2 {
    private static final int DELAY_TIME = 1500;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initEvent() {
        //完美解决：APP下载安装后，点击“直接打开”，启动应用后，按下HOME键，再次点击桌面上的应用，会重启一个新的应用问题
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        //获取版本号
        ((TextView) findViewById(R.id.tv_app_versionName)).setText(PackageUtils.getAppVersionName());
        initRxPermissions();
    }

    private void initRxPermissions() {
        Disposable subscribe = new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.BROADCAST_STICKY)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) {
                        if (granted) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    skip();
                                }
                            },DELAY_TIME);
                        } else {
                            finish();
                        }
                    }
                });
    }


    private void skip() {
        openActivity(MainTabActivity2.class);
        overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
