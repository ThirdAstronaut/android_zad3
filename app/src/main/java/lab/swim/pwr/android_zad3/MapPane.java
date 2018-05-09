package lab.swim.pwr.android_zad3;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MapPane extends Activity implements OnMapReadyCallback {


    private String lightValue;

    public static void start(Context context) {
        Intent starter = new Intent(context, MapPane.class);

        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style;
        if (lightValue.equals("Dark")) {
            style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        } else
            style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_retro_json);
        googleMap.setMapStyle(style);
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            lightValue = intent.getStringExtra("LightValue");
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}

