package com.yanxiu.gphone.faceshowadmin_android.newclasscircle.nineGridView;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.newclasscircle.ninegrid.NineGridView;

/**
 * @author frc on 2018/1/15.
 */

public class GlideNineImageLoader implements NineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).centerCrop().placeholder(R.drawable.ic_default_color).error(R.drawable.net_error_picture).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);

    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
