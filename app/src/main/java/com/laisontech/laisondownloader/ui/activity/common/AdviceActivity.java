package com.laisontech.laisondownloader.ui.activity.common;

import android.widget.EditText;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;
import com.laisontech.laisondownloader.utils.EmailSend;

import butterknife.BindView;
import butterknife.OnClick;

//意见反馈界面
public class AdviceActivity extends BaseActivity2 {
    @BindView(R.id.et_advice)
    EditText etAdvice;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_advice;
    }

    @Override
    protected void initData() {
        super.initData();
        //设置标题栏
        setCustomActionBar(this,R.string.Feedback);
    }

    @OnClick(R.id.btn_send_advice_info)
    public void onViewClicked() {
        //获取输入框中的信息
        String Msg = etAdvice.getText().toString().trim();
        //将信息通过邮件的形式进行发送
        try{
            EmailSend.SendEmailCode(HttpConst.ToEmail,Msg);
            showToast(R.string.SendAdiceSuccess);
            finish();
        }catch (Exception e){
            showToast(R.string.EmailError);
        }
    }
}
