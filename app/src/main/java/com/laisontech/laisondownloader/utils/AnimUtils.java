package com.laisontech.laisondownloader.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.laisontech.laisondownloader.R;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class AnimUtils {
    public static void startRotateAnim(ImageView imageView) {
        if (imageView == null) return;
        imageView.setVisibility(View.VISIBLE);
        startSearchAnim(imageView, R.drawable.icon_loading, "null");
    }

    public static void stopRotateAnim(ImageView imageView) {
        if (imageView == null) return;
        stopSearchAnim(imageView, R.drawable.ic_logo, "null");
    }

    //对ImageView开启旋转动画
    public static void startSearchAnim(ImageView imageView, int animResId, String tag) {
        if (imageView == null) return;
        imageView.setEnabled(false);
        imageView.setImageResource(animResId);
        imageView.setTag(tag);
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());//让旋转动画一直转，不停顿
        animation.setRepeatCount(-1);
        imageView.startAnimation(animation);
    }

    //清除ImageView的动画
    public static void stopSearchAnim(ImageView imageView, int resDefaultId, String tag) {
        if (imageView == null) return;
        imageView.setEnabled(true);
        imageView.clearAnimation();
        imageView.setImageResource(resDefaultId);
        imageView.setTag(tag);
    }
}
