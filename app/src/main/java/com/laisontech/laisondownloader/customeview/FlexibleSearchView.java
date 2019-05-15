package com.laisontech.laisondownloader.customeview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.laisontech.laisondownloader.R;

/**
 * Created by SDP on 2018/1/16.
 * 可伸缩的搜索布局
 */

public class FlexibleSearchView extends View {
    //状态
    private static final int STATUS_OPEN = 0;//打开
    private static final int STATUS_CLOSE = 1;//关闭
    private static final int STATUS_PROCESS = 2;//打开或者关闭的过程中
    //颜色
    private static final int DEFAULT_SEARCH_COLOR = Color.WHITE;
    private static final int DEFAULT_SEARCH_TEXT_COLOR = Color.RED;
    //默认在右侧的位置
    private static final int DEFAULT_IN_LEFT_POSITION = 0;
    private static final int DEFAULT_IN_RIGHT_POSITION = 1;
    //默认的字体大小
    private static final int DEFAULT_TEXT_SIZE = 15;
    //默认的高度
    private static final int DEFAULT_VIEW_HEIGHT = 40;
    //默认市场
    private static final int DEFAULT_ANIMATION_DURATION = 300;
    private int mSearchViewStatus;
    private int mPosition;
    private int mSearchViewIcon;
    private int mSearchViewColor;
    private CharSequence mSearchViewText;
    private int mDuration;
    private int mSearchViewTextColor;
    private float mSearchViewTextSize;
    private float mDefaultHeight;
    private RectF mRectF;
    private RectF mDstRectF;
    private Bitmap mBitmap;
    //动画
    private int mOffsetX;
    private ValueAnimator mOpenAnimator;
    private ValueAnimator mCloseAnimator;
    private AccelerateInterpolator mInterpolator;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;

    private int mWidth;
    private int mHeight;
    private int mRadius;
    private Paint mPaint;

    public FlexibleSearchView(Context context) {
        this(context, null);
    }

    public FlexibleSearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlexibleSearchView);
        //状态
        mSearchViewStatus = ta.getInteger(R.styleable.FlexibleSearchView_flexible_search_view_status, STATUS_CLOSE);
        //默认位置
        mPosition = ta.getInteger(R.styleable.FlexibleSearchView_flexible_search_view_position, DEFAULT_IN_RIGHT_POSITION);
        mSearchViewIcon = ta.getResourceId(R.styleable.FlexibleSearchView_flexible_search_view_icon, android.R.drawable.ic_search_category_default);
        mSearchViewColor = ta.getColor(R.styleable.FlexibleSearchView_flexible_search_view_color, DEFAULT_SEARCH_COLOR);
        mSearchViewText = ta.getText(R.styleable.FlexibleSearchView_flexible_search_view_text);
        mDuration = ta.getInteger(R.styleable.FlexibleSearchView_flexible_search_view_duration, DEFAULT_ANIMATION_DURATION);
        mSearchViewTextColor = ta.getColor(R.styleable.FlexibleSearchView_flexible_search_view_text_color, DEFAULT_SEARCH_TEXT_COLOR);
        float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics());
        mSearchViewTextSize = ta.getDimension(R.styleable.FlexibleSearchView_flexible_search_view_text_size, defaultTextSize);
        mDefaultHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_VIEW_HEIGHT, getResources().getDisplayMetrics());
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mSearchViewColor);
        mPaint.setTextSize(mSearchViewTextSize);
        mRectF = new RectF();
        mDstRectF = new RectF();
        mBitmap = BitmapFactory.decodeResource(getResources(), mSearchViewIcon);

        initAnim();
    }

    private void initAnim() {
        mInterpolator = new AccelerateInterpolator();
        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mOffsetX = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        };
        mOpenAnimator = new ValueAnimator();
        setAnimStatus(mOpenAnimator, true);

        mCloseAnimator = new ValueAnimator();
        setAnimStatus(mCloseAnimator, false);
    }


    private void setAnimStatus(ValueAnimator animator, final boolean isOpen) {
        animator.setInterpolator(mInterpolator);
        animator.setDuration(mDuration);
        animator.addUpdateListener(mAnimatorUpdateListener);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                mSearchViewStatus = STATUS_PROCESS;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    mSearchViewStatus = STATUS_OPEN;
                } else {
                    mSearchViewStatus = STATUS_CLOSE;
                }
                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getSize(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = (int) mDefaultHeight;
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = (int) (Math.min(heightSize, mDefaultHeight));
            }
        }
        mRadius = Math.min(mWidth - getPaddingLeft() - getPaddingRight(),
                mHeight - getPaddingTop() - getPaddingBottom()) / 2;
        if (mSearchViewStatus == STATUS_OPEN) {
            mOffsetX = mWidth - mRadius * 2 - getPaddingRight() - getPaddingLeft();
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mSearchViewColor);
        int left = mPosition == DEFAULT_IN_RIGHT_POSITION ?
                mWidth - getPaddingRight() - 2 * mRadius - mOffsetX : getPaddingLeft();
        int right = mPosition == DEFAULT_IN_RIGHT_POSITION ?
                mWidth - getPaddingRight() : 2 * mRadius + mOffsetX + getPaddingLeft();
        int top = getPaddingTop();
        int bottom = mHeight - getPaddingBottom();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom, mRadius, mRadius, mPaint);
        } else {
            mRectF.set(left, top, right, bottom);
            canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
        }
        mDstRectF.set(left + (int) ((1 - Math.sqrt(2) / 2) * mRadius), top + (int) ((1 - Math.sqrt(2) / 2) * mRadius),
                left + (int) ((1 + Math.sqrt(2) / 2) * mRadius), top + (int) ((1 + Math.sqrt(2) / 2) * mRadius));
        canvas.drawBitmap(mBitmap, null, mDstRectF, mPaint);

        if (mSearchViewStatus == STATUS_OPEN && !TextUtils.isEmpty(mSearchViewText)) {
            mPaint.setColor(mSearchViewTextColor);
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            double textHeight = Math.ceil(fm.descent - fm.ascent);
            canvas.drawText(mSearchViewText.toString(), left + 2 * mRadius, top + (float) (mRadius + textHeight / 2 - fm.descent), mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 如果事件不是在search bar区域内，那么不响应
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float actionX = event.getX();
            // 计算search bar的左右边界
            int left = mPosition == DEFAULT_IN_RIGHT_POSITION ? mWidth - 2 * mRadius - mOffsetX : 0;
            int right = mPosition == DEFAULT_IN_RIGHT_POSITION ? mWidth : 2 * mRadius + mOffsetX;
            if (actionX < left || actionX > right) {
                return false;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 判断搜索栏是否为打开状态
     */
    public boolean isOpen() {
        return mSearchViewStatus == STATUS_OPEN;
    }

    /**
     * 判断搜索栏是否为关闭状态
     */
    public boolean isClose() {
        return mSearchViewStatus == STATUS_CLOSE;
    }

    /**
     * 打开搜索栏
     */
    public void startOpen() {
        if (isOpen()) {
            return;
        } else if (mOpenAnimator.isStarted()) {
            return;
        } else if (mCloseAnimator.isStarted()) {
            mCloseAnimator.cancel();
        }
        mOpenAnimator.setIntValues(mOffsetX, mWidth - mRadius * 2 - getPaddingLeft() - getPaddingRight());
        mOpenAnimator.start();
    }

    /**
     * 关闭搜索栏
     */
    public void startClose() {
        if (isClose()) {
            return;
        } else if (mCloseAnimator.isStarted()) {
            return;
        } else if (mOpenAnimator.isStarted()) {
            mOpenAnimator.cancel();
        }
        mCloseAnimator.setIntValues(mOffsetX, 0);
        mCloseAnimator.start();
    }
}
