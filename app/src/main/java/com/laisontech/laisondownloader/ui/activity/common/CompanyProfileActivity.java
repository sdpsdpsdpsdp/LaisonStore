package com.laisontech.laisondownloader.ui.activity.common;

import android.view.View;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;

import butterknife.OnClick;

public class CompanyProfileActivity extends BaseActivity2 {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_company_profile;
    }


    @Override
    protected void initData() {
        setCustomActionBar(this, R.string.CompanyProfile);
    }

    @OnClick({R.id.iv_link, R.id.iv_facebook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_link:
                openActivity(HttpConst.COMPANY_LINK_ADDRESS);
                break;
            case R.id.iv_facebook:
                openActivity(HttpConst.COMPANY_FACE_BOOK_ADDRESS);
                break;
        }
    }

    //打开网页
    private void openActivity(String str) {
        CompanyWebViewActivity.openWebActivity(this, str);
    }
}
