package com.yanxiu.gphone.faceshowadmin_android.common.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshowadmin_android.R;
import com.yanxiu.gphone.faceshowadmin_android.customView.ZoomImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/23 14:26.
 * Function :
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;

    private List<String> mPaths=new ArrayList<>();
    private List<ZoomImageView> mImageViews=new ArrayList<>();

    public PhotoPagerAdapter(Context context){
        this.mContext=context;
    }

    public void setData(List<String> paths, List<ZoomImageView> images){
        if (paths==null||images==null||paths.size()!=images.size()){
            return;
        }
        this.mImageViews.clear();
        this.mPaths.clear();
        this.mImageViews.addAll(images);
        this.mPaths.addAll(paths);
        this.notifyDataSetChanged();
    }

    public List<ZoomImageView> getImageViews(){
        return this.mImageViews;
    }

    public void deleteItem(int position){
        if (position>-1&&position<mPaths.size()){
            this.mPaths.remove(position);
            this.mImageViews.remove(position);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mImageViews!=null?mImageViews.size():0;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ZoomImageView imageView=mImageViews.get(position);
        Glide.with(mContext).load(mPaths.get(position)).error(R.drawable.net_error_picture).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
