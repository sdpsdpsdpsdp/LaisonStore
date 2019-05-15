package com.laisontech.laisondownloader.customeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;


/**
 * Created by SDP on 2017/12/28.
 */

public class TaskTitleView extends LinearLayout {
    private String mCentText;
    private TextView tvCent;

    public TaskTitleView(Context context) {
        this(context, null);
    }

    public TaskTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaskTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TaskTitleView);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = ta.getIndex(i);
            if (index == R.styleable.TaskTitleView_centEventText) {
                mCentText = ta.getString(index);
            }
        }
        ta.recycle();

        View view = View.inflate(context, R.layout.include_layout_read_event_title, this);
        tvCent = (TextView) view.findViewById(R.id.tv_read_meter_event_title);
        tvCent.setText(mCentText);
    }
    public void setText(String txt) {
        tvCent.setText(txt);
    }

}
