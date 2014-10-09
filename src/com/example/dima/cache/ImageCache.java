package com.example.dima.cache;

import android.graphics.Bitmap;

import com.example.dima.provider.TileData;

/**
 * Created by d.barkalov on 02.10.2014.
 */
public interface ImageCache {
    void addBitmap(TileData key, Bitmap bitmap);
    Bitmap getBitmap(TileData key);
}
