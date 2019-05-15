package com.laisontech.laisondownloader.ui.activity.apps.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.customeview.DownloadProgressBar;
import com.laisontech.laisondownloader.entitys.bean.LaisonAppBean;
import com.laisontech.laisondownloader.entitys.event.EventSender;
import com.laisontech.laisondownloader.entitys.event.OperateEvent;
import com.laisontech.laisondownloader.http.HttpClient;
import com.laisontech.laisondownloader.loader.DownloadTaskLoader;
import com.laisontech.laisondownloader.loader.ImageUrlLoader;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.activity.apps.presenter.impl.AppDetailAPresenterImpl;
import com.laisontech.laisondownloader.ui.activity.apps.presenter.inter.IAppDetailAPresenter;
import com.laisontech.laisondownloader.ui.activity.apps.view.inter.IAppDetailAView;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;
import com.laisontech.laisondownloader.utils.CalculateUtils;
import com.laisontech.laisondownloader.utils.DateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by SDP
 * on 2019/5/9
 * Des：
 */
public class AppDetailActivity extends BaseActivity2 implements IAppDetailAView {
    private static final String APP_DETAIL = "appDetail";
    @BindView(R.id.app_icon)
    ImageView appIcon;
    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.app_company)
    TextView appCompany;
    @BindView(R.id.app_price)
    TextView appPrice;
    @BindView(R.id.app_size)
    TextView appSize;
    @BindView(R.id.app_version)
    TextView appVersion;
    @BindView(R.id.app_releaseDate)
    TextView appReleaseDate;
    @BindView(R.id.app_rating)
    TextView appRating;
    @BindView(R.id.app_brief)
    TextView appBrief;
    @BindView(R.id.app_updates)
    TextView appUpdates;
    @BindView(R.id.app_introduction)
    TextView appIntroduction;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.progress_bar)
    DownloadProgressBar progressBar;
    @BindView(R.id.netSpeed)
    TextView netSpeed;
    @BindView(R.id.downloadSize)
    TextView downloadSize;
    @BindView(R.id.ll_download_speed)
    LinearLayout llDownloadSpeed;
    @BindView(R.id.fl_delete)
    FrameLayout flClose;
    private LaisonAppBean mAppBean;
    private IAppDetailAPresenter mIAppDetailAPresenter;

    @Override
    protected void initPresenter() {
        mIAppDetailAPresenter = new AppDetailAPresenterImpl(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_app_detail;
    }

    @Override
    protected void initEvent() {
        flClose.setVisibility(View.GONE);
        llDownloadSpeed.setVisibility(View.GONE);
    }

    @Override
    protected void justLoadOnce() {
        mAppBean = (LaisonAppBean) getIntent().getSerializableExtra(APP_DETAIL);
        if (mAppBean == null) {
            this.finish();
            return;
        }
        setUI(mAppBean);
    }

    @Override
    public void startLoad() {
        showWaitingDialog(R.string.Loading, true);
    }

    @Override
    public void hideLoad() {
        hideWaitingDialog();
    }

    @Override
    public void showMsg(int resID) {
        showToast(resID);
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    private void setUI(LaisonAppBean appBean) {
        setCustomActionBar(this, appBean.getAppName());
        ImageUrlLoader.loadImage(this, appIcon, appBean.getAppLogoLink());
        //App名称
        appName.setText(appBean.getAppName());
        //公司名称
        appCompany.setText(appBean.getCompanyName());
        //App大小
        appSize.setText(CalculateUtils.getAppSize(appBean.getApkFileSize()));
        //App版本号
        appVersion.setText(appBean.getVersionName());
        //App更新时间
        appReleaseDate.setText(DateUtils.formatDetailDate(appBean.getUpdateDate()));
        //从服务器获取详情信息
        mIAppDetailAPresenter.queryAppDetail(mAppBean.getApkId());
    }


    @Override
    public void showAppDetail(LaisonAppBean appBean) {
        if (appBean == null) return;
        mAppBean = appBean;
        //更新说明
        appUpdates.setText("-" + appBean.getUpdateDescription());
        //App描述说明
        appIntroduction.setText(appBean.getAppIntroduction());
    }

    @OnClick(R.id.fl_delete)
    public void onClick() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        //更新当前的状态
        DownloadTaskLoader.getLoader().checkAppStatus( progressBar, mAppBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpClient.cancelTag(HttpConst.TAG_APP_DETAIL_BY_ID);
        EventSender.sendOperateDownloadEvent(OperateEvent.START);
    }

    public static void openAppActivity(Context context, LaisonAppBean appBean) {
        if (context == null || appBean == null) return;
        Intent intent = new Intent(context, AppDetailActivity.class);
        intent.putExtra(APP_DETAIL, appBean);
        context.startActivity(intent);
    }

}
