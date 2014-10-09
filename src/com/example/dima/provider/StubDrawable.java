package com.example.dima.provider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;

/**
 * Created by dima on 28.09.2014.
 */
public class StubDrawable extends Drawable {

    private final WeakReference<TileLoadTask> bitmapWorkerTaskReference;
    private final TileData mTile;

    private Paint paint;
    private Paint paint2 = new Paint();

    public StubDrawable(TileData tile, TileLoadTask bitmapWorkerTask) {
        mTile = tile;
        bitmapWorkerTaskReference = new WeakReference<TileLoadTask>(bitmapWorkerTask);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint2.setAntiAlias(true);
        paint2.setTextSize(20);
        paint2.setColor(Color.BLACK);
    }

    public TileLoadTask getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }

    @Override
    public void draw(Canvas canvas) {
        int height = getBounds().height();
        int width = getBounds().width();
        RectF rect = new RectF(1.0f, 1.0f, width-1, height-1);
        canvas.drawRoundRect(rect, 30, 30, paint);
        canvas.drawText("x= " + mTile.getX(), width/2 -20, height/2, paint2);
        canvas.drawText("y=" + mTile.getY(), width/2 -20, height/2 + 30, paint2);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
