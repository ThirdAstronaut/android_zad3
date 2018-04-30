package lab.swim.pwr.android_zad3;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
        MapStyleOptions style;
        if (lightValue == "Dark") {
            style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json);
        } else
            style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_retro_json);
        googleMap.setMapStyle(style);
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("LightValue");
            Log.d("receiver", "Got message: " + message);
            /*setTheme(android.R.style.Theme_Black);
            findViewById(R.id.main_layout).setBackgroundColor(4242);


*/

            lightValue = message;

        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}

