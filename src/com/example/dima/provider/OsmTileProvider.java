package com.example.dima.provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;

import com.example.dima.cache.ImageCache;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dima on 27.09.2014.
 */
public class OsmTileProvider implements TileProvider {

    private final Context mContext;
    private final Executor exec = Executors.newFixedThreadPool(4);
    private final ImageCache mImageMemoryCache;
    private final TileLoadTask.TileUrl mTileUrl;

    public OsmTileProvider(Context mContext, ImageCache imageCache) {
        this.mContext = mContext;
        this.mImageMemoryCache = imageCache;
        this.mTileUrl = new TileLoadTask.TileUrl() {
            @Override
            public String getUrl(int x, int y) {
                return OsmTileProvider.getUrl(x, y);
            }
        };
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
                final TileLoadTask task = new TileLoadTask(imageView, mImageMemoryCache, mTileUrl);
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



    //System.out.println("http://tile.openstreetmap.org/" + getTileNumber(lat, lon, zoom) + ".png");
    public static Point getTileNumber(final double lat, final double lon) {
            final int zoom = 17;
            int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
            int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
            if (xtile < 0)
                xtile=0;
            if (xtile >= (1<<zoom))
                xtile=((1<<zoom)-1);
            if (ytile < 0)
                ytile=0;
            if (ytile >= (1<<zoom))
                ytile=((1<<zoom)-1);
            return new Point(xtile,ytile);
    }

    public static String getUrl(int x, int y) {
        int z = 17;
        return "http://b.tile.opencyclemap.org/cycle/" + z + "/" + x + "/" + y + ".png";
    }

    public static int getTileSize() {
        return 256;
    }
}
