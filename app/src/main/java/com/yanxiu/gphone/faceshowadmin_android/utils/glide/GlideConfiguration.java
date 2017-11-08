package com.yanxiu.gphone.faceshowadmin_android.utils.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by FengRongCheng on 2016/6/19 15:46.
 * powered by yanxiu.com
 * <p/>
 * Glide默认的Bitmap格式是RGB_565 ，比ARGB_8888格式的内存开销要小一半
 * 如果你对默认的RGB_565效果还比较满意，可以不做任何事
 * 果你觉得难以接受，可以创建一个新的GlideModule将Bitmap格式转换到ARGB_8888：
 *
 * 同时在AndroidManifest.xml中将GlideModule定义为meta-data
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
