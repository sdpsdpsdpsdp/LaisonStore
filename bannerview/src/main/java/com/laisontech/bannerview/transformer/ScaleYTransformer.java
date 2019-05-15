package com.laisontech.bannerview.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhouwei on 17/5/26.
 */

public class ScaleYTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9F;

    public ScaleYTransformer() {
    }

    public void transformPage(View page, float position) {
        if (position < -1.0F) {
            page.setScaleY(0.9F);
        } else if (position <= 1.0F) {
            float scale = Math.max(0.9F, 1.0F - Math.abs(position));
            page.setScaleY(scale);
        } else {
            page.setScaleY(0.9F);
        }

    }
}
