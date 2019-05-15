package com.laisontech.laisondownloader.ui.activity.common;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.db.OperateDBUtils;
import com.laisontech.laisondownloader.http.HttpConst;
import com.laisontech.laisondownloader.ui.base.BaseActivity2;

import butterknife.BindView;
import butterknife.OnClick;

public class SetServerAddressActivity extends BaseActivity2 {
    @BindView(R.id.et_port_address)
    EditText etPortAddress;
    private String mCurrentAddress;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_set_server_address;
    }

    @Override
    protected void initData() {
        super.initData();
        setCustomActionBar(this, R.string.SetServiceAddress);
        //将缓存中数据设置到界面中去
        mCurrentAddress = OperateDBUtils.getServerAddress();
        if (mCurrentAddress.isEmpty()) {
            showToast(R.string.CurrentAddressIsError);
            finish();
        }
        etPortAddress.setText(mCurrentAddress);
        etPortAddress.setSelection(etPortAddress.getText().toString().length());
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_reset_address) {
            etPortAddress.setText(HttpConst.DEFAULT_SERVER_ADDRESS);
            etPortAddress.setSelection(etPortAddress.getText().length());
        }
        return true;
    }

    //点击保存
    @OnClick(R.id.btn_change_address)
    public void onViewClicked() {
        String hostaddr = etPortAddress.getText().toString().trim();
        if (hostaddr.length() < 10 || (!hostaddr.startsWith("http://") && !hostaddr.startsWith("https://"))) {
            showToast(R.string.InputAddressError);
            return;
        }
        if (!mCurrentAddress.equals(hostaddr)) {
            OperateDBUtils.setServerAddress(hostaddr);
            showToast(R.string.ServerChanged);
        }
        finish();
    }
}
