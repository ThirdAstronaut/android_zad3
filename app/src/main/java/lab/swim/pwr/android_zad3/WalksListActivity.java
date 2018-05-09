package lab.swim.pwr.android_zad3;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class WalksListActivity extends AppCompatActivity implements SensorEventListener {
    private SimpleCursorAdapter adapter;
    private SensorManager sensorManager;
    private Sensor acceleroMeter;
    private float[] history = new float[3];
    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    static    String currentTheme="";

    private static String lightValue;
    public static void start(Context context, String lightVal) {
        Intent starter = new Intent(context, WalksListActivity.class);
        lightValue = lightVal;
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walks_list);


        mAdapter = new CustomAdapter();
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.Callback callback = new SwipeHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        ContentResolver cr = getContentResolver();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, acceleroMeter, 1000000);


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("light-change-event"));
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        sensorManager.registerListener(this, acceleroMeter, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xChange = history[0] - event.values[0];
            float yChange = history[1] - event.values[1];
            float zChange = history[2] - event.values[2];

            history[0] = event.values[0];
            history[1] = event.values[1];
            history[2] = event.values[2];

            if (xChange > 2) {
                //Links
            } else if (xChange < -2) {
                //Rechts
            }

            if (zChange > 12) {
                mRecyclerView.smoothScrollBy(mRecyclerView.getHeight() * mAdapter.getCount(), 1000);
                mRecyclerView.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollBy(0, 0);
                        mRecyclerView.getLayoutManager().scrollToPosition(mAdapter.getCount() - 1);
                    }
                }, 500);
            } else if (zChange < -12) {
                mRecyclerView.smoothScrollBy(mRecyclerView.getHeight() * mAdapter.getCount(), 1000);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollBy(0, 0);
                        mRecyclerView.getLayoutManager().scrollToPosition(0);
                    }
                }, 500);
            }
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            lightValue = intent.getStringExtra("LightValue");

            setListStyle();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setListStyle() {
        if (lightValue != null) {

            if (!currentTheme.equals("Dark") && lightValue.equals("Dark")) {
                findViewById(R.id.recycler_view).setBackgroundResource(R.color.listDark);
                currentTheme = "Dark";
            } else if(!currentTheme.equals("Light") && lightValue.equals("Light")){
                findViewById(R.id.recycler_view).setBackgroundResource(R.color.colorPrimary);
                currentTheme = "Light";

            }
        }
    }
}
