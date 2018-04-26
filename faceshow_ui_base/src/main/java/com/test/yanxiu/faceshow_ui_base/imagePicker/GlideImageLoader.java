package com.test.yanxiu.faceshow_ui_base.imagePicker;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

/**
 * @author frc on 2018/1/12.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(path).override(width, height).fitCenter().into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        //此处如果不设置 在选择图片时无法显示预览图片
        Glide.with(activity).load(path).override(width, height).fitCenter().into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
