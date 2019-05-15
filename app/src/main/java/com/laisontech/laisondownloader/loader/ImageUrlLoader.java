package com.laisontech.laisondownloader.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.laisontech.laisondownloader.R;

/**
 * Created by SDP
 * on 2019/5/13
 * Des：
 */
public class ImageUrlLoader {
    public static void loadImage(Context context, ImageView iv, Object resource) {
        if (context == null || iv == null) return;
        Glide.with(context)
                .load(resource)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_logo)
                .into(iv);
    }
}
