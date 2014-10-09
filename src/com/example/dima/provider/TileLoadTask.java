package com.example.dima.provider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.dima.cache.ImageCache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dima on 28.09.2014.
 */
public class TileLoadTask  extends AsyncTask<TileData, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private TileData tileData;
    private ImageCache imageMemoryCache;

    public TileLoadTask(ImageView imageView, ImageCache imageMemoryCache) {
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.imageMemoryCache = imageMemoryCache;
    }

    @Override
    protected Bitmap doInBackground(TileData... params) {
        Bitmap bitmap = loadBitmap(params[0]);
        imageMemoryCache.addBitmap(tileData, bitmap);
        return bitmap;
    }

    private Bitmap loadBitmap(TileData param) {
        tileData = param;
        String src = getUrl(tileData);
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final TileLoadTask bitmapWorkerTask = getTileLoadTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static TileLoadTask getTileLoadTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof StubDrawable) {
                final StubDrawable stubBitmapDrawable = (StubDrawable) drawable;
                return stubBitmapDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    private String getUrl(TileData tileData) {
        return "http://b.tile.opencyclemap.org/cycle/16/" + tileData.getX() + "/" + tileData.getY() + ".png";
    }

    public TileData getTile() {
        return tileData;
    }
}
