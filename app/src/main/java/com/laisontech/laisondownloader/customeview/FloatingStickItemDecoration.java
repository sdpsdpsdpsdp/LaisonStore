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
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by SDP
 * on 2019/4/26
 * Des：
 */
public class FloatingStickItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "FloatingItemDecoration";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int mTextSize = 14;
    private int dividerHeight = 1;
    private int dividerWidth = 1;
    private Map<Integer, String> keys = new LinkedHashMap<>();
    private int mTitleHeight;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private float mTextHeight;
    private float mTextBaselineOffset;
    private Context mContext;
    private int mTextColorRes = Color.parseColor("#ffffff");
    private int mBgColorRes = Color.parseColor("#f23061");
    private int mDividerColorRes = Color.parseColor("#e3e6eb");
    private int mDecorationWidth = 5;
    private Paint mDecorationPaint;
    private int mDecorationColorRes = Color.parseColor("#1D84ED");
    /**
     * 滚动列表的时候是否一直显示悬浮头部
     */
    private boolean showFloatingHeaderOnScrolling = true;
    //
//    public FloatingStickItemDecoration(Context context) {
//        this.mContext = context;
//        final TypedArray a = context.obtainStyledAttributes(ATTRS);
//        mDivider = a.getDrawable(0);
//        a.recycle();
//        this.dividerHeight = mDivider.getIntrinsicHeight();
//        this.dividerWidth = mDivider.getIntrinsicWidth();
//        init();
//    }


    /**
     * 自定义分割线
     *
     * @param context
     * @param drawableId 分割线图片
     */
    public FloatingStickItemDecoration(Context context, @DrawableRes int drawableId) {
        this.mContext = context;
        this.mDivider = ContextCompat.getDrawable(context, drawableId);
        this.dividerHeight = mDivider.getIntrinsicHeight();
        this.dividerWidth = mDivider.getIntrinsicWidth();
        init();
    }

    /**
     * 自定义分割线
     * 也可以使用{@link Canvas#drawRect(float, float, float, float, Paint)}或者{@link Canvas#drawText(String, float, float, Paint)}等等
     * 结合{@link Paint}去绘制各式各样的分割线
     */
    public FloatingStickItemDecoration(Context context) {
        this.mContext = context;
        this.mDivider = new ColorDrawable(mDividerColorRes);
        this.dividerWidth = px2dp(dividerWidth);
        this.dividerHeight = px2dp(dividerHeight);
        this.mTextSize = px2sp(mTextSize);
        this.mDecorationWidth = px2dp(mDecorationWidth);
        init();
    }

    public void initParams(@ColorRes int textColorRes, @ColorRes int bgColorRes, @ColorRes int dividerColorRes, @Dimension int textSize, @Dimension float dividerWidth, @Dimension float dividerHeight) {
        this.mTextColorRes = colorRes(textColorRes);
        this.mBgColorRes = colorRes(bgColorRes);
        this.mDividerColorRes = colorRes(dividerColorRes);
        this.mDivider = new ColorDrawable(mDividerColorRes);
        this.mTextSize = px2sp(textSize);
        this.dividerWidth = px2dp(dividerWidth);
        this.dividerHeight = px2dp(dividerHeight);
        init();
    }

    private void init() {
        if (mTextPaint == null) {
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
        }
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColorRes);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = fm.bottom - fm.top;//计算文字高度
        mTextBaselineOffset = fm.bottom;

        if (mBackgroundPaint == null) {
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setAntiAlias(true);
        }
        mBackgroundPaint.setColor(mBgColorRes);

        if (mDecorationPaint == null) {
            mDecorationPaint = new Paint();
            mDecorationPaint.setAntiAlias(true);
        }
        mDecorationPaint.setStrokeWidth(mDecorationWidth);
        mDecorationPaint.setColor(mDecorationColorRes);
        mDecorationPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (keys == null) return;
        int pos = parent.getChildViewHolder(view).getAdapterPosition();
        int itemViewType = parent.getAdapter().getItemViewType(pos);
        if (itemViewType == 0) return;//不是 一种类型，不处理
        if (keys.containsKey(pos)) {//留出头部偏移    0,2  ---0,1,2,3,4
            outRect.set(0, mTitleHeight, 0, 0);
        } else {
            outRect.set(0, dividerHeight, 0, 0);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (!showFloatingHeaderOnScrolling) {
            return;
        }
        int firstVisiblePos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        int itemViewType = parent.getAdapter().getItemViewType(firstVisiblePos);
        if (firstVisiblePos == RecyclerView.NO_POSITION || itemViewType == 0) {//是第0个 不绘制
            return;
        }
        String title = getTitle(firstVisiblePos);
        if (TextUtils.isEmpty(title)) {
            return;
        }
        boolean flag = false;
        if (getTitle(firstVisiblePos + 1) != null && !title.equals(getTitle(firstVisiblePos + 1))) {
            //说明是当前组最后一个元素，但不一定碰撞了
            View child = parent.findViewHolderForAdapterPosition(firstVisiblePos).itemView;
            if (child.getTop() + child.getMeasuredHeight() < mTitleHeight) {
                //进一步检测碰撞
                c.save();//保存画布当前的状态
                flag = true;
                c.translate(0, child.getTop() + child.getMeasuredHeight() - mTitleHeight);//负的代表向上
            }
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + mTitleHeight;


        c.drawRect(left, top, right, bottom, mBackgroundPaint);

        //画一个开始的标记样式
        float startX = px2dp(15);
        c.drawLine(startX, top + px2dp(11), startX, bottom - px2dp(11), mDecorationPaint);
        //画Text
        float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
        float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
        c.drawText(title, x, y, mTextPaint);
        if (flag) {
            //还原画布为初始状态
            c.restore();
        }
    }

    /**
     * *如果该位置没有，则往前循环去查找标题，找到说明该位置属于该分组
     */
    private String getTitle(int position) {
        if (this.keys == null) return null;
        while (position >= 0) {//0 2
            if (keys.containsKey(position)) {
                return keys.get(position);
            }
            position--;
        }
        return null;
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        if (this.keys == null) return;
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();//控件右边距--父控件左边距
        int top = 0;
        int bottom = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {//TODO绘制
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (!keys.containsKey(params.getViewLayoutPosition())) {
                //画普通分割线
                top = child.getTop() - params.topMargin - dividerHeight;
                bottom = top + dividerHeight;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            } else {
                //画头部
                top = child.getTop() - params.topMargin - mTitleHeight;
                bottom = top + mTitleHeight;
                c.drawRect(left, top, right, bottom, mBackgroundPaint);
                //画一个开始的标记样式
                float startX = px2dp(15);
                c.drawLine(startX, top + px2dp(11), startX, bottom - px2dp(11), mDecorationPaint);
                //画Text
                float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());
                float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
                c.drawText(keys.get(params.getViewLayoutPosition()), x, y, mTextPaint);
            }
        }
    }


    public void setShowFloatingHeaderOnScrolling(boolean showFloatingHeaderOnScrolling) {
        this.showFloatingHeaderOnScrolling = showFloatingHeaderOnScrolling;
    }

    public void setKeys(Map<Integer, String> keys) {
        if (this.keys == null || keys == null) return;
        this.keys.clear();
        this.keys.putAll(keys);
    }

    public void setTitleHeight(int titleHeight) {
        this.mTitleHeight = titleHeight;
    }


    private int px2sp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, mContext.getResources().getDisplayMetrics());
    }

    private int px2dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
    }

    private int colorRes(@ColorRes int resId) {
        return mContext.getResources().getColor(resId);
    }
}
