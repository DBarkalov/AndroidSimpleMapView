package com.example.dima;

import android.app.Activity;
import android.os.Bundle;

import com.example.dima.myapplication.R;
import com.example.dima.cache.ImageCache;
import com.example.dima.cache.ImageMemoryCache;
import com.example.dima.provider.OsmTileProvider;
import com.example.dima.provider.TileProvider;
import com.example.dima.view.MapView;


public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        MapView mapView = (MapView)findViewById(R.id.mapview);
        ImageCache imageCache = ImageMemoryCache.getInstance(getFragmentManager());
        TileProvider tileProvider = new OsmTileProvider(this, imageCache);
        mapView.setStartCoord(OsmTileProvider.getTileNumber(55.800087, 37.532917), OsmTileProvider.getTileSize());
        mapView.setTileProvider(tileProvider);
    }



}
