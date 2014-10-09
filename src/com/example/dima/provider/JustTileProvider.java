package com.example.dima.provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.example.dima.cache.ImageCache;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dima on 27.09.2014.
 */
public class JustTileProvider implements TileProvider {

    private final Context mContext;
    private final Executor exec = Executors.newFixedThreadPool(4);
    private final ImageCache mImageMemoryCache;

    public JustTileProvider(Context mContext, ImageCache imageCache) {
        this.mContext = mContext;
        this.mImageMemoryCache = imageCache;
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public View getTileView(TileData tile, View convertView) {
        ImageView v;
        if(convertView != null){
            v = (ImageView) convertView;
        } else {
            v = new ImageView(getContext());
        }
        loadTileBitmap(tile, v);
        return v;
    }

    private void loadTileBitmap(TileData tile, ImageView imageView) {
        Bitmap bitmap;
        if((bitmap = mImageMemoryCache.getBitmap(tile)) != null){
            imageView.setImageBitmap(bitmap);
        } else {
            if (cancelPotentialWork(tile, imageView)) {
                final TileLoadTask task = new TileLoadTask(imageView, mImageMemoryCache);
                final StubDrawable asyncDrawable = new StubDrawable(tile, task);
                imageView.setImageDrawable(asyncDrawable);
                task.executeOnExecutor(exec, tile);
            }
        }
    }

    private boolean cancelPotentialWork(TileData tile, ImageView imageView) {
        final TileLoadTask tileTask = TileLoadTask.getTileLoadTask(imageView);
        if (tileTask != null) {
            final TileData taskTile = tileTask.getTile();
            // If bitmapData is not yet set or it dif1fers from the new data
            if (taskTile == null || !taskTile.equals(tile)) {
                // Cancel previous task
                tileTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }



}
