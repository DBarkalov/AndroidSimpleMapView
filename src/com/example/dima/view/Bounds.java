package com.example.dima.view;

import android.graphics.Point;

/**
* tile indexes
*/
class Bounds {
    private final int mLeft;
    private final int mRight;
    private final int mTop;
    private final int mBottom;

    private final int tileSize;
    private final Point startPoint;

    public Bounds(int left, int right, int top, int bottom, int tileSize, int x, int y) {
        this.mLeft = left;
        this.mRight = right;
        this.mTop = top;
        this.mBottom = bottom;
        this.tileSize = tileSize;
        startPoint = new Point(x, y);
    }

    public Bounds(int width, int height,  int tileSize) {
        this.mLeft = 0;
        this.mRight = div(width, tileSize);
        this.mTop = 0;
        this.mBottom = div(height, tileSize);
        this.tileSize = tileSize;
        startPoint = new Point(0, 0);
    }

    @Override
    public String toString() {
        return "x=" + getX() + " y=" + getY() +   " horiz[" + mLeft + ":" + mRight +"]  vert[" + mTop + ":" + mBottom +"]";
    }

    public int getLeft() {
        return mLeft;
    }


    public int getRight() {
        return mRight;
    }


    public int getTop() {
        return mTop;
    }


    public int getBottom() {
        return mBottom;
    }


    public int getX(){
        return startPoint.x;
    }

    public int getY(){
        return startPoint.y;
    }

    int div(int a, int b){
        return a % b == 0 ? a/b : a / b + 1;
    }

    public Bounds getNewBounds(int dx, int dy, int width, int height) {
        int newX =  getX() + dx;
        int x = newX;
        int left = getLeft();
        if(newX > 0){
            int tiles = div(newX,tileSize);
            left = mLeft-tiles;
            x = getX()-tiles*tileSize + dx;
        } else if((newX + tileSize) <= 0){
            int tiles = Math.abs(newX / tileSize);
            left = mLeft + tiles;
            x = getX() + tiles*tileSize + dx;
        }

        int newY =  getY() + dy;
        int y = newY;
        int top = getTop();
        if(newY > 0){
            int tiles = div(newY,tileSize);
            top = mTop-tiles;
            y = getY()-tiles*tileSize + dy;
        } else if((newY + tileSize) <= 0){
            int tiles = Math.abs(newY / tileSize);
            top = mTop + tiles;
            y = getY() + tiles*tileSize + dy;
        }

        if(x>0 || x < -tileSize){
            throw new IllegalStateException ("x illegal value");
        }
        if(y>0 || y < -tileSize){
            throw new IllegalStateException ("y illegal value");
        }

        int right =  left + div((width - x), tileSize);
        int bottom = top + div((height - y), tileSize);

        return new Bounds(left,right,top,bottom,tileSize, x, y);
    }

    public boolean contains(int x, int y) {
        if(x>=mLeft && x< mRight && y>= mTop && y< mBottom) {
            return true;
        }
        return false;
    }
}
