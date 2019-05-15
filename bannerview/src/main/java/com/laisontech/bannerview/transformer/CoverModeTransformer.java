package com.laisontech.bannerview.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhouwei on 17/8/20.
 */

public class CoverModeTransformer implements ViewPager.PageTransformer {
    private float reduceX = 0.0F;
    private float itemWidth = 0.0F;
    private float offsetPosition = 0.0F;
    private int mCoverWidth;
    private float mScaleMax = 1.0F;
    private float mScaleMin = 0.9F;
    private ViewPager mViewPager;

    public CoverModeTransformer(ViewPager pager) {
        this.mViewPager = pager;
    }

    public void transformPage(View view, float position) {
        float currentPos;
        float scale;
        float translationX;
        if (this.offsetPosition == 0.0F) {
            currentPos = (float)this.mViewPager.getPaddingLeft();
            scale = (float)this.mViewPager.getPaddingRight();
            translationX = (float)this.mViewPager.getMeasuredWidth();
            this.offsetPosition = currentPos / (translationX - currentPos - scale);
        }

        currentPos = position - this.offsetPosition;
        if (this.itemWidth == 0.0F) {
            this.itemWidth = (float)view.getWidth();
            this.reduceX = (2.0F - this.mScaleMax - this.mScaleMin) * this.itemWidth / 2.0F;
        }

        if (currentPos <= -1.0F) {
            view.setTranslationX(this.reduceX + (float)this.mCoverWidth);
            view.setScaleX(this.mScaleMin);
            view.setScaleY(this.mScaleMin);
        } else if ((double)currentPos <= 1.0D) {
            scale = (this.mScaleMax - this.mScaleMin) * Math.abs(1.0F - Math.abs(currentPos));
            translationX = currentPos * -this.reduceX;
            if ((double)currentPos <= -0.5D) {
                view.setTranslationX(translationX + (float)this.mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5F) / 0.5F);
            } else if (currentPos <= 0.0F) {
                view.setTranslationX(translationX);
            } else if ((double)currentPos >= 0.5D) {
                view.setTranslationX(translationX - (float)this.mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5F) / 0.5F);
            } else {
                view.setTranslationX(translationX);
            }

            view.setScaleX(scale + this.mScaleMin);
            view.setScaleY(scale + this.mScaleMin);
        } else {
            view.setScaleX(this.mScaleMin);
            view.setScaleY(this.mScaleMin);
            view.setTranslationX(-this.reduceX - (float)this.mCoverWidth);
        }

    }
}

