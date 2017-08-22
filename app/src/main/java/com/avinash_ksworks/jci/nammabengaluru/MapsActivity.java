package com.avinash_ksworks.jci.nammabengaluru;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.avinash_ksworks.jci.nammabengaluru.adapter.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Context context;
    Cursor PlaceCursor;
    DatabaseHelper myDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = getApplicationContext();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bangalore = new LatLng(12.9716, 77.5946);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangalore,  (float)11.5));


        myDBHelper = new DatabaseHelper(context);

        PlaceCursor = myDBHelper.getAllTemples();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(8),
                    PlaceCursor.getDouble(9));
            mMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title(PlaceCursor.getString(2)+" Temple")
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("temple",50,50))));
        }


        PlaceCursor = myDBHelper.getAllKids();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(8),
                    PlaceCursor.getDouble(9));
            mMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title(PlaceCursor.getString(2))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("kids",50,50))));
        }

        PlaceCursor = myDBHelper.getAllTrekking();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(8),
                    PlaceCursor.getDouble(9));
            mMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title(PlaceCursor.getString(2))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("trek_maps",50,50))));
        }

        PlaceCursor = myDBHelper.getAllLakes();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(8),
                    PlaceCursor.getDouble(9));
            mMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title(PlaceCursor.getString(2))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("lake_maps",50,50))));
        }

        PlaceCursor = myDBHelper.getAllParks();
        while (PlaceCursor.moveToNext()){
            LatLng place = new LatLng(PlaceCursor.getDouble(8),
                    PlaceCursor.getDouble(9));
            mMap.addMarker(new MarkerOptions()
                    .position(place)
                    .title(PlaceCursor.getString(2))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("park_maps",50,50))));
        }


    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
