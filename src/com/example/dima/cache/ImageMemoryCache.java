package com.example.dima.cache;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;

import com.example.dima.provider.TileData;

/**
 * Created by d.barkalov on 02.10.2014.
 */
public class ImageMemoryCache implements ImageCache {
    private final LruCache<TileData, Bitmap> mMemoryCache;
    private static final String TAG = "ImageCacheFragment";

    private ImageMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        mMemoryCache = new LruCache<TileData, Bitmap>(maxMemory / 2) {
            @Override
            protected int sizeOf(TileData key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }


    public static ImageCache getInstance(FragmentManager fragmentManager) {

        // Search for, or create an instance of the non-UI RetainFragment
        final RetainFragment mRetainFragment = findOrCreateRetainFragment(fragmentManager);

        // See if we already have an ImageCache stored in RetainFragment
        ImageCache imageCache = (ImageCache) mRetainFragment.getObject();

        // No existing ImageCache, create one and store it in RetainFragment
        if (imageCache == null) {
            imageCache = new ImageMemoryCache();
            mRetainFragment.setObject(imageCache);
        }

        return imageCache;
    }

    private static RetainFragment findOrCreateRetainFragment(FragmentManager fragmentManager) {
        RetainFragment mRetainFragment = (RetainFragment) fragmentManager.findFragmentByTag(TAG);
        if (mRetainFragment == null) {
            mRetainFragment = new RetainFragment();
            fragmentManager.beginTransaction().add(mRetainFragment, TAG).commitAllowingStateLoss();
        }
        return mRetainFragment;
    }


    @Override
    public void addBitmap(TileData key, Bitmap bitmap) {
        if (getBitmap(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @Override
    public Bitmap getBitmap(TileData key) {
        return mMemoryCache.get(key);
    }


    /**
     * A simple non-UI Fragment that stores a single Object and is retained over configuration
     * changes. It will be used to retain the ImageCache object.
     */
    public static class RetainFragment extends Fragment {
        private Object mObject;
        public RetainFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        public void setObject(Object object) {
            mObject = object;
        }
        public Object getObject() {
            return mObject;
        }
    }
}
