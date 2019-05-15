package com.laisontech.laisondownloader.ui.fragment.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.callback.OnAssignmentResultListener;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.http.HttpMsg;
import com.laisontech.laisondownloader.loader.DownloadTaskLoader;
import com.laisontech.laisondownloader.ui.base.BaseLazyFragment;
import com.laisontech.laisondownloader.ui.dialog.CheckUpdateDialog;
import com.laisontech.laisondownloader.utils.ViewUtils;
import com.laisontech.laisondownloader.ui.activity.common.AboutUsActivity;
import com.laisontech.laisondownloader.ui.activity.common.SetServerAddressActivity;
import com.laisontech.laisondownloader.ui.activity.common.AdviceActivity;
import com.laisontech.laisondownloader.utils.PackageUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by SDP
 * on 2019/5/7
 * Des：
 */
public class SettingsFragment extends BaseLazyFragment {
    @BindView(R.id.tv_VersionName)
    TextView tvCheckUpdate;
    @BindView(R.id.iv_need_update)
    ImageView ivCheckUpdate;

    public static Fragment getFragment() {
        return new SettingsFragment();
    }

    @Override
    protected int getResLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initEvents() {
        //设置右边的按钮显示 更新的显示
        tvCheckUpdate.setText("V" + PackageUtils.getAppVersionName());
        queryAppUpdateInfo(false);
    }

    @OnClick({R.id.checkUpdate, R.id.feedBack, R.id.aboutUs, R.id.setServiceUrl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.checkUpdate:
                queryAppUpdateInfo(true);
                break;
            case R.id.feedBack:
                openActivity(AdviceActivity.class);
                break;
            case R.id.aboutUs:
                openActivity(AboutUsActivity.class);
                break;
            case R.id.setServiceUrl:
                openActivity(SetServerAddressActivity.class);
                break;
        }
    }

    private void queryAppUpdateInfo(final boolean handleUpdate) {
        if (getContext() == null) return;
        if (handleUpdate) {
            showWaitingDialog(R.string.Loading, false);
        }
        HttpClient.queryAppStoreInfo(new OnAssignmentResultListener<LaisonAppBean>() {
            @Override
            public void onSuccess(int errorCode, LaisonAppBean appBean) {
                boolean needUpdate =  DownloadTaskLoader.queryAppNeedUpdate(appBean);
                ivCheckUpdate.setVisibility(needUpdate ? View.VISIBLE : View.GONE);
                if (handleUpdate) {
                    hideWaitingDialog();
                    if (errorCode != HttpConst.CODE_SUCCESS || appBean == null) {
                        showToast(HttpMsg.getAppUpdateMsg(errorCode));
                        return;
                    }
                    if (!needUpdate) {
                        showToast(R.string.CurrentNewVersion);
                        return;
                    }
                    FragmentActivity activity = getActivity();
                    if (activity == null) return;
                    new CheckUpdateDialog(activity, ViewUtils.getWidth(activity), appBean)
                            .setCanCancel(false)
                            .show();
                }
            }

            @Override
            public void onFailed(String failMsg) {
                if (handleUpdate) {
                    showToast(failMsg);
                    hideWaitingDialog();
                }
            }
        });
    }

}
