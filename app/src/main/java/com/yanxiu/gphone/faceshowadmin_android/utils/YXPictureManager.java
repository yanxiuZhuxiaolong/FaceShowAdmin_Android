package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanxiu.gphone.faceshowadmin_android.utils.glide.GlideCricleTransform;
import com.yanxiu.gphone.faceshowadmin_android.utils.glide.GlideRoundTransform;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by FengRongCheng on 2016/6/19 15:14.
 * powered by yanxiu.com
 * 基于Glide封装的关于图片加载显示的类
 */
public class YXPictureManager {

    private static YXPictureManager mInstance;

    public static YXPictureManager getInstance() {
        if (mInstance == null) {
            synchronized (YXPictureManager.class) {
                if (mInstance == null)
                    mInstance = new YXPictureManager();
            }
        }
        return mInstance;
    }

    private static class YXPictureManagerHodler {
        private final static YXPictureManager instance = new YXPictureManager();
    }

    public void showPic(Context context, String url, ImageView view, int id) {
        Glide.with(context).load(url).crossFade().placeholder(id).into(view);
    }

    /**
     * 显示圆形图片
     *
     * @param context
     * @param url
     * @param
     * @param view
     * @param id
     */
    public void showCirclePic(Context context, String url, ImageView view, int id) {
        if (id == 0) {
            Glide.with(context).load(url).crossFade().transform(new GlideCricleTransform(context)).placeholder(null).crossFade().into(view);
        } else {
            Glide.with(context).load(url).crossFade().transform(new GlideCricleTransform(context)).placeholder(id).crossFade().into(view);

        }

    }

    /**
     * 显示圆形图片
     *
     * @param context
     * @param
     * @param view
     * @param id
     */
    public void showCirclePic(Context context, ImageView view, int id) {
        Glide.with(context).load(id).crossFade().transform(new GlideCricleTransform(context)).placeholder(null).crossFade().into(view);

    }

    /**
     * 显示圆形图片
     *
     * @param context
     * @param bitmap
     * @param
     * @param view
     * @param id
     */
    public void showCirclePic(Context context, Bitmap bitmap, ImageView view, int id) {
        byte[] bytes = new byte[]{};
        if (bitmap != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bytes = bos.toByteArray();
        }

        if (id == 0) {
            Glide.with(context).load(bytes).crossFade().transform(new GlideCricleTransform(context)).placeholder(null).crossFade().into(view);

        } else {
            Glide.with(context).load(bytes).crossFade().transform(new GlideCricleTransform(context)).placeholder(id).crossFade().into(view);

        }

    }

    /**
     * 显示圆角图片
     *
     * @param context
     * @param url
     * @param view
     * @param dp            圆角大小
     * @param defaultViewId
     */
    public void showRoundPic(Context context, String url, ImageView view, int dp, int defaultViewId) {

        if (defaultViewId == 0) {
            Glide.with(context).load(url).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(context, dp)).placeholder(null).crossFade().into(view);

        } else {
            Glide.with(context).load(url).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(context, dp)).placeholder(defaultViewId).crossFade().into(view);

        }
    }


    /**
     * 显示圆角图片
     *
     * @param context
     * @param DrawableId
     * @param view
     * @param dp            圆角大小
     * @param defaultViewId
     */
    public void showRoundPic(Context context, int DrawableId, ImageView view, int dp, int defaultViewId) {

        if (defaultViewId == 0) {
            Glide.with(context).load(DrawableId).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(context, dp)).placeholder(null).crossFade().into(view);

        } else {
            Glide.with(context).load(DrawableId).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(context, dp)).placeholder(defaultViewId).crossFade().into(view);

        }
    }

    /**
     * 显示圆角图片
     *
     * @param context
     * @param view
     * @param bitmap
     * @param dp            圆角大小
     * @param defaultViewId
     */
    public void showRoundPic(Context context, Bitmap bitmap, ImageView view, int dp, int defaultViewId) {
        byte[] bytes = new byte[]{};
        if (bitmap != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bytes = bos.toByteArray();
        }
        if (defaultViewId == 0) {
            Glide.with(context).load(bytes).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(context, dp)).placeholder(null).crossFade().into(view);

        } else {
            Glide.with(context).load(bytes).skipMemoryCache(true).crossFade().transform(new GlideRoundTransform(context, dp)).placeholder(defaultViewId).crossFade().into(view);

        }
    }

    public void showRoundPic(Context context, String url, ImageView view, int defaultViewId) {
        if (defaultViewId == 0) {
            Glide.with(context).load(url).transform(new GlideRoundTransform(context, 2)).placeholder(null).crossFade().into(view);

        } else {
            Glide.with(context).load(url).transform(new GlideRoundTransform(context, 2)).placeholder(defaultViewId).crossFade().into(view);

        }
    }

    public void showRoundPic(Context context, File file, ImageView view) {
        Glide.with(context).load(file).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).transform(new GlideRoundTransform(context, 2)).crossFade().into(view);

    }

    public void showPic(Context context, String url, ImageView view, Drawable drawable) {
        Glide.with(context).load(url).placeholder(drawable).into(view);

    }


    public void showPic(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);

    }

    public void showPic(Context context, byte[] model, ImageView view) {
        Glide.with(context).load(model).into(view);

    }

    public void showPic(Context context, Uri uri, ImageView view) {
        Glide.with(context).load(uri).into(view);

    }

    public void showPic(Context context, File file, ImageView view) {
        Glide.with(context).load(file).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(view);

    }

}
