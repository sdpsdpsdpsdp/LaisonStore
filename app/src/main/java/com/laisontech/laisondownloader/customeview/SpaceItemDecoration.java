package com.laisontech.laisondownloader.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Dimension;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.laisontech.laisondownloader.ui.adapter.AppsAdapter;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private int dividerHeight;

    public SpaceItemDecoration(Context context) {
        this.mContext = context;
        this.dividerHeight = dp2dx(10);
    }

    public SpaceItemDecoration(Context context, @Dimension float dividerHeight) {
        this.mContext = context;
        this.dividerHeight = dp2dx(dividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildViewHolder(view).getAdapterPosition();
        int itemViewType = parent.getAdapter().getItemViewType(pos);
        if (itemViewType == AppsAdapter.TYPE_HEADER) return;
        outRect.set(0, dividerHeight, 0, 0);
    }

    private int dp2dx(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
    }

    private int colorRes(@ColorRes int resId) {
        return mContext.getResources().getColor(resId);
    }
}
