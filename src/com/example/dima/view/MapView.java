package com.example.dima.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.dima.provider.TileData;
import com.example.dima.provider.TileProvider;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Dmitry Barkalov on 17.09.2014.
 */
public class MapView extends ViewGroup {

    private static String LOG_TAG =  "MapView";
    private float mDx;
    private float mDy;
    private GestureDetector mGestureDetector;
    private Bounds mBounds;
    private Deque<View> mRecycledViews;
    TileProvider mTileProvider;
    private Point startCoord;
    private int mTileSize;

    public MapView(Context context) {
        super(context);
        init(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return MapView.this.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return MapView.this.onFling(e1, e2, velocityX, velocityY);
            }
        });
        mRecycledViews = new ArrayDeque<View>();
    }

    public void setStartCoord(Point point, int tileSize){
        startCoord = point;
        mTileSize = tileSize;
    }

    private boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mDx = -distanceX;
        mDy = -distanceY;
        requestLayout();
        return true;
    }

    public void setTileProvider(TileProvider mTileProvider) {
        this.mTileProvider = mTileProvider;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mTileProvider == null){
            return;
        }

        invalidate();
        removeInvisibleItems(mDx, mDy);
        positionItems(mDx, mDy);
        addItems(mDx, mDy);
    }

    private void positionItems(float dx, float dy) {
        final int count = getChildCount();
        for(int i=0; i< count; i++){
            View child = getChildAt(i);
            int left = child.getLeft() + (int)dx;
            int top = child.getTop() + (int) dy;
            child.layout(left, top, left+mTileSize, top+mTileSize);
        }
    }

    private void fillBounds(Bounds newBounds){
        //Log.d( "Bounds=",  newBounds.toString() );
        int left;
        int top;
        for (int x = newBounds.getLeft(); x < newBounds.getRight(); x++) {
            for (int y = newBounds.getTop(); y < newBounds.getBottom(); y++) {
                if(mBounds == null || (mBounds != null && !mBounds.contains(x,y))) {
                    View v = createChild(x, y);
                    left = newBounds.getX() + (x - newBounds.getLeft()) * mTileSize;
                    top = newBounds.getY() + (y - newBounds.getTop()) * mTileSize;
                    v.layout(left, top, left + mTileSize, top + mTileSize);
                    addViewInLayout(v, -1, new TileLayoutParams(x, y));
                }
            }
        }
    }



    private void addItems(float dx, float dy) {
        Bounds newBounds;
        if(mBounds == null) {
            newBounds = new Bounds(getWidth(), getHeight(), mTileSize);
        } else {
            newBounds = mBounds.getNewBounds((int)dx,(int)dy,getWidth(), getHeight());
        }
        fillBounds(newBounds);
        mBounds = newBounds;
    }

    private TileData getTileData(int x, int y){
        return new TileData(startCoord.x+ x, startCoord.y+ y);
    }


    View createChild(int x, int y){
        Log.d("add child ", " x=" + x + "  y=" + y);
        View cached = null;
        if(!mRecycledViews.isEmpty()){
            cached = mRecycledViews.pop();
        }
        return mTileProvider.getTileView(getTileData(x,y),cached);
    }

    private void removeChild(View v) {
        final TileLayoutParams params = (TileLayoutParams)v.getLayoutParams();
        Log.d( "remove child", "x= " + params.getIndexX() + " y=" + params.getIndexY());
        mRecycledViews.addFirst(v);
        removeViewInLayout(v);
    }

    boolean outOfView(View v, float dx, float dy){
        return (((v.getBottom() + dy) < 0) || (v.getTop()+dy > getHeight())
                ||((v.getRight() + dx )< 0) || (v.getLeft()+dx > getWidth()));
    }

    private void removeInvisibleItems(float dx, float dy) {
        int count = getChildCount()-1;
        for(int i=count; i>=0; i--){
            View v = getChildAt(i);
            if(outOfView(v, dx,dy)){
                removeChild(v);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }


    public static class TileLayoutParams extends LayoutParams {
        private final int xIndex;
        private final int yIndex;

        public TileLayoutParams(int x, int y) {
            super(WRAP_CONTENT, WRAP_CONTENT);
            this.xIndex = x;
            this.yIndex = y;
        }
        public int getIndexX() {
            return this.xIndex;
        }
        public int getIndexY() {
            return this.yIndex;
        }
    }

}
