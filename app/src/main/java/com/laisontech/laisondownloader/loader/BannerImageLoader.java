package com.laisontech.laisondownloader.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.laisontech.laisondownloader.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by SDP
 * on 2019/5/8
 * Des：
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageUrlLoader.loadImage(context, imageView, path);
    }
}
