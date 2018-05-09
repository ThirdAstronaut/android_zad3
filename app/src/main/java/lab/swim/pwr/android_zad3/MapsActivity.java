package lab.swim.pwr.android_zad3;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private static String lightValue;
    private GoogleMap mMap;


    private GestureDetectorCompat gestureDetectorCompat;


    public static void start(Context context, String lightVal) {
        Intent starter = new Intent(context, MapsActivity.class);
        lightValue = lightVal;
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("light-change-event"));

        //      gestureDetectorCompat = new GestureDetectorCompat(this, new My2ndGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

  /*  class My2ndGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

            if (event2.getX() > event1.getX()) {
                Toast.makeText(getBaseContext(), "Swipe right - finish()", Toast.LENGTH_SHORT).show();

                finish();
            }
            return true;
        }
    }
*/

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng d1Marker = new LatLng(51.110445, 17.058728);
        mMap.addMarker(new MarkerOptions().position(d1Marker).title("Uwaga"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(d1Marker));
/*
        CameraUpdate current = CameraUpdateFactory.newLatLngZoom(d1Marker,15);
            googleMap.animateCamera(current);
*/
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        setMapStyle();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            lightValue = intent.getStringExtra("LightValue");

            setMapStyle();
        }
    };

    private void setMapStyle() {

        if (lightValue != null) {
            MapStyleOptions style;
            switch (lightValue) {
                case "Dark":
                    style = MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_json);
                    break;
                case "Light":
                    style = MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_retro_json);
                    break;
                default:
                    style = MapStyleOptions.loadRawResourceStyle(
                            MapsActivity.this, R.raw.style_retro_json);
                    break;
            }
            mMap.setMapStyle(style);
        }
    }

    @Override
    protected void onDestroy() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
