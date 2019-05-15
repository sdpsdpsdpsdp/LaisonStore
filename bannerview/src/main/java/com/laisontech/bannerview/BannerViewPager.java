package com.laisontech.bannerview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by zhouwei on 17/8/16.
 */

public class BannerViewPager extends ViewPager {
    private ArrayList<Integer> childCenterXAbs = new ArrayList<>();
    private SparseArray<Integer> childIndex = new SparseArray<>();

    public BannerViewPager(Context context) {
        super(context);
        init();
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        this.setClipToPadding(false);
        this.setOverScrollMode(2);
    }

    protected int getChildDrawingOrder(int childCount, int n) {
        if (n == 0 || this.childIndex.size() != childCount) {
            this.childCenterXAbs.clear();
            this.childIndex.clear();
            int viewCenterX = this.getViewCenterX(this);

            for(int i = 0; i < childCount; ++i) {
                int indexAbs = Math.abs(viewCenterX - this.getViewCenterX(this.getChildAt(i)));
                if (this.childIndex.get(indexAbs) != null) {
                    ++indexAbs;
                }

                this.childCenterXAbs.add(indexAbs);
                this.childIndex.append(indexAbs, i);
            }

            Collections.sort(this.childCenterXAbs);
        }

        return (Integer)this.childIndex.get((Integer)this.childCenterXAbs.get(childCount - 1 - n));
    }

    private int getViewCenterX(View view) {
        int[] array = new int[2];
        view.getLocationOnScreen(array);
        return array[0] + view.getWidth() / 2;
    }
}
