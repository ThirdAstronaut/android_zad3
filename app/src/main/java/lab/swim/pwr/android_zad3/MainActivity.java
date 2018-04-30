package lab.swim.pwr.android_zad3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    boolean activityRunning;
    private float numOfSteps;
    private String duration;
    private String name;

    private static String lightValue;
    private Sensor mSensor;


    private TextView numOfStepsTextView;
    private TextView timerTextView;
    private Button startButton;
    private TextView tripTimeTextView;
    private TextView countTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
String time = String.format("%d:%02d", minutes, seconds);
            timerTextView.setText(time);
            duration= time;
            timerHandler.postDelayed(this, 500);
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numOfStepsTextView = findViewById(R.id.numOfStepsTextView);
        timerTextView = findViewById(R.id.timerTextView);
        countTextView = findViewById(R.id.count);
        tripTimeTextView = findViewById(R.id.tripTimeTextView);
        startButton = findViewById(R.id.startButton);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        SensorManager sensorManager1 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager1 != null) {
            mSensor = sensorManager1.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        }
        /*TriggerEventListener triggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
         //       WalksListActivity.start(MainActivity.this, numOfSteps, duration, "aaa");    //TODO name

            }
        };*/

      //  sensorManager1.requestTriggerSensor(triggerEventListener, mSensor);

        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity.start(MainActivity.this, lightValue);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));


        startButton.setText("start");
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                    startActivity(new Intent(getApplicationContext(), WalksListActivity.class));
                }
            }
        });





    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            lightValue = intent.getStringExtra("LightValue");
            ;
            //          Log.d("receiver", "Got message: " + message);
            //setTheme(android.R.style.Theme_Black);
            setActivityTheme();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setActivityTheme() {
        if (lightValue != null) {
            if (lightValue.equals("Dark")) {
                findViewById(R.id.main_layout).setBackgroundResource(R.color.colorPrimaryDark);
                numOfStepsTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                timerTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                countTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                tripTimeTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
            } else {
                findViewById(R.id.main_layout).setBackgroundResource(R.color.colorPrimary);
                numOfStepsTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                timerTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                countTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                startButton.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                tripTimeTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);

        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.startButton);
        b.setText("start");

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            countTextView.setText(String.valueOf(event.values[0]));
            numOfSteps = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, LightSensor.class));
    }
}



/*
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends Activity {

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        timerTextView = (TextView) findViewById(R.id.timerTextView);

        Button b = (Button) findViewById(R.id.button);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }
            }
        });
    }

  @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.button);
        b.setText("start");
    }

}*/