package com.test.yanxiu.common_base.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.HashMap;

/**
 * Created by cailei on 14/03/2018.
 */

public class SharedSingleton {
    private static SharedSingleton instance;
    private HashMap hashMap = new HashMap();


    /**
     * 模拟本地磁盘缓存功能 等加入真实磁盘缓存后 废弃该部分
     * */
    private LruCache<String,Bitmap> bitmapLruCache=new LruCache<>(5);

    public Bitmap getCachedBitmap(String key){
       return bitmapLruCache.get(key);
    }
    public void cacheBitmap(String url,Bitmap bitmap){
        bitmapLruCache.put(url,bitmap);
    }


    private SharedSingleton() {}
    public static SharedSingleton getInstance() {
        if( instance == null ) {
            synchronized( SharedSingleton.class ) {
                if( instance == null ) {
                    instance = new SharedSingleton();
                }
            }

        }
        return instance;
    }

    public void destroyInstance() {
        hashMap.clear();
        bitmapLruCache.evictAll();
        hashMap = null;
        instance = null;
    }

    public synchronized <T> T get(String key) {
        return (T)hashMap.get(key);
    }

    public synchronized <T> void set(String key, T t) {
        hashMap.put(key, t);
    }
}
