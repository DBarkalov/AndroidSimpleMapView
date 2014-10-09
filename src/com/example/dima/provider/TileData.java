package com.example.dima.provider;

/**
 * Created by dima on 28.09.2014.
 */
public class TileData {
    private final int x;
    private final int y;

    public TileData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TileData tileData = (TileData) o;

        if (x != tileData.x) return false;
        if (y != tileData.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
