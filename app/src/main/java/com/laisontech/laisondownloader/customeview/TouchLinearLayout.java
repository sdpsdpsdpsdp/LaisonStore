package com.laisontech.laisondownloader.customeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laisontech.laisondownloader.R;

import java.util.Locale;

/**
 * Created by SDP on 2017/5/8.
 *
 */

public class TouchLinearLayout extends LinearLayout {
    private Bitmap mLeftIcon;
    private String mLeftText;
    private TextView mTv;
    private ImageView mIvBack;
    private TextView mTvShowInfo;

    private RelativeLayout mRlDefault;
    private LinearLayout mLLRight;
    TextView mTvShowInfoRight;

    public TouchLinearLayout(Context context) {
        this(context, null);
    }

    public TouchLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TouchLinearLayout);
        BitmapDrawable drawable = (BitmapDrawable) ta.getDrawable(R.styleable.TouchLinearLayout_leftIcon);
        mLeftIcon = drawable.getBitmap();
        mLeftText = ta.getString(R.styleable.TouchLinearLayout_centerText);
        ta.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.linearlayout_item, TouchLinearLayout.this);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_ll_item);
        ImageView ivBack = (ImageView) view.findViewById(R.id.iv_back);
        TextView tv = (TextView) view.findViewById(R.id.tv_ll_item);
        TextView tvShowInfo = (TextView) view.findViewById(R.id.tv_showInfo);
        RelativeLayout rlShowDefault = (RelativeLayout) view.findViewById(R.id.rl_show_default);

        LinearLayout llShowRight = (LinearLayout) view.findViewById(R.id.ll_show_right);
        TextView tvShowInfoRight = (TextView) view.findViewById(R.id.tv_showInfo_right);

        iv.setImageBitmap(mLeftIcon);
        tv.setText(mLeftText);
        String language = Locale.getDefault().getLanguage();
        if (language.equals("ar") || language.equals("fa")) {
            ivBack.setImageResource(R.drawable.icon_fa_back);
        }
        mTv = tv;
        mIvBack = ivBack;
        mTvShowInfo = tvShowInfo;
        mRlDefault = rlShowDefault;

        mLLRight = llShowRight;
        mTvShowInfoRight = tvShowInfoRight;
    }

    public void setText(String text) {
        mTv.setText(text);
    }

    public void setIvBackVisible(int visible) {
        mIvBack.setVisibility(visible);
    }

    public void setTvShowInfoTxt(Object object) {
        if (mIvBack.getVisibility() == VISIBLE) {
            return;
        }
        mTvShowInfo.setVisibility(VISIBLE);
        if (object instanceof String) {
            mTvShowInfo.setText((String) object);
        } else if (object instanceof Integer) {
            mTvShowInfo.setText((Integer) object);
        } else {
            mTvShowInfo.setText("");
        }
    }

    public void setRightShowInfo(Object object) {
        if (object==null) return;
        mRlDefault.setVisibility(GONE);
        mLLRight.setVisibility(VISIBLE);
        if (object instanceof String) {
            mTvShowInfoRight.setText((String) object);
        } else if (object instanceof Integer) {
            mTvShowInfoRight.setText((Integer) object);
        } else {
            mTvShowInfoRight.setText("");
        }
    }
}
