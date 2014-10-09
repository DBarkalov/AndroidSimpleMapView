package com.example.dima.provider;

import android.view.View;

/**
 * Created by dima on 27.09.2014.
 */
public interface TileProvider {
    public View getTileView(TileData tileData, View convertView);
}
