package com.andy.orange.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by Andy lau on 2017/8/14.
 */
public class GlideLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageLoaderUtils.display(context,imageView,(String)path);
    }
}
