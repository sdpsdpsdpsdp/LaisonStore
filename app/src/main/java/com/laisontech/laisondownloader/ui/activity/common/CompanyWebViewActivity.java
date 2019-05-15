package com.laisontech.laisondownloader.ui.activity.common;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.customeview.ProgressWebView;
import com.laisontech.laisondownloader.customeview.ProgressWebView;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;

import butterknife.BindView;
import butterknife.OnClick;

public class CompanyWebViewActivity extends BaseActivity2 {
    @BindView(R.id.iv_back_wed)
    ImageButton ivBackWed;
    @BindView(R.id.tv_web_address)
    TextView tvWebAddress;
    @BindView(R.id.iv_refresh_web)
    ImageButton ivRefreshWeb;
    @BindView(R.id.progress_webView)
    ProgressWebView progressWebView;
    private static final String LINK_URL = "linkUrl";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_company_web_view;
    }

    @Override
    protected void initData() {
        super.initData();
        String mLinkUrl = getIntent().getStringExtra(LINK_URL);
        tvWebAddress.setText(mLinkUrl);
        progressWebView.loadUrl(mLinkUrl);
    }

    @OnClick({R.id.iv_back_wed, R.id.iv_refresh_web})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back_wed:
                progressWebView.destroyDrawingCache();
                finish();
                break;
            case R.id.iv_refresh_web:
                progressWebView.refreshWeb();
                break;
        }
    }

    public static void openWebActivity(Context context, String url) {
        if (context == null) return;
        Intent intent = new Intent(context, CompanyWebViewActivity.class);
        intent.putExtra(LINK_URL, url);
        context.startActivity(intent);
    }
}
