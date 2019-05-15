package com.laisontech.laisondownloader.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;
import com.laisontech.laisondownloader.db.savelastsearchlist.LastSearchInfo;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class FlowLayoutAdapter extends TagAdapter<LastSearchInfo> {
    private Context mContext;

    public FlowLayoutAdapter(Context context, List<LastSearchInfo> datas) {
        super(datas);
        mContext = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, LastSearchInfo lastSearchInfo) {
        TextView tvFlow = (TextView) View.inflate(mContext, R.layout.flow_textview, null);
        tvFlow.setText(lastSearchInfo.getQueryCode());
        return tvFlow;
    }
}
