package com.test.yanxiu.im_ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.test.yanxiu.im_ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/23 14:26.
 * Function :
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<String> mPaths = new ArrayList<>();
    private List<View> mImageViews = new ArrayList<>();

    public PhotoPagerAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<String> paths, List<View> images) {
        if (paths == null || images == null || paths.size() != images.size()) {
            return;
        }
        this.mImageViews.clear();
        this.mPaths.clear();
        this.mImageViews.addAll(images);
        this.mPaths.addAll(paths);
        this.notifyDataSetChanged();
    }

    public List<View> getImageViews() {
        return this.mImageViews;
    }

    public void deleteItem(int position) {
        if (position > -1 && position < mPaths.size()) {
            this.mPaths.remove(position);
            this.mImageViews.remove(position);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mImageViews != null ? mImageViews.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mImageViews.get(position);
        final ZoomImageView imageView = mImageViews.get(position).findViewById(R.id.photo_preview_item_zoom_imageview);
        final ProgressBar progressBar = mImageViews.get(position).findViewById(R.id.photo_preview_item_progress_bar);
        String path = mPaths.get(position);
        Glide.with(mContext)
                .load(path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(resource);
                    }
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageDrawable(mContext.getDrawable(R.drawable.bg_im_pic_holder_view));
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickPhotoListener != null) {
                    mOnClickPhotoListener.onClick(v, position);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    onClickPhotoListener mOnClickPhotoListener;

    public void setOnClickPhotoListener(onClickPhotoListener iOnClickPhotoListener) {
        mOnClickPhotoListener = iOnClickPhotoListener;
    }

    public interface onClickPhotoListener {
        void onClick(View view, int position);
    }
}
