package com.laisontech.laisondownloader.ui.activity.common;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.customeview.TouchLinearLayout;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;
import com.laisontech.laisondownloader.customeview.TouchLinearLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity2 {
    @BindView(R.id.tel_item)
    TouchLinearLayout telItem;
    @BindView(R.id.email_item)
    TouchLinearLayout emailItem;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {
        super.initData();
        setCustomActionBar(this, R.string.AboutUs);
        telItem.setRightShowInfo(getStringRes(R.string.CompanyTelphone));
        emailItem.setRightShowInfo(getStringRes(R.string.CompanyEmail));
    }

    @OnClick({R.id.function_item, R.id.tel_item, R.id.email_item, R.id.website_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.function_item:
                openActivity(CompanyProfileActivity.class);
                break;
            case R.id.tel_item:

                break;
            case R.id.email_item:
                sendEmail();
                break;
            case R.id.website_item:
                CompanyWebViewActivity.openWebActivity(this, getStringRes(R.string.official_websiteURL));
                break;
        }
    }

    //调用邮件应用，发送邮件
    private void sendEmail() {

       /* Uri uri = Uri.parse("mailto:"+getResStr(R.string.CompanyEmail));
        Intent intent = new Intent(Intent.ACTION_SEND, uri);
        //intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        // intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        // intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        startActivity(Intent.createChooser(intent, "Please"));*/
        Uri uri = Uri.parse("mailto:" + getStringRes(R.string.CompanyEmail));
        String[] email = {getStringRes(R.string.CompanyEmail)};
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        startActivity(Intent.createChooser(intent, getStringRes(R.string.EmailChoose)));
    }
}
