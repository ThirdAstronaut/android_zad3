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
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    boolean activityRunning;
    private float mPreviousNumOfSteps = 0;
    private String duration;
    private static String lightValue;
    private Sensor mSensor;
    private TextView nameTextView;
    private EditText nameEditText;
    private boolean stepCounterFrozen;
    static    String currentTheme="";

    private GestureDetectorCompat gestureDetectorCompat;

    private TextView numOfStepsTextView;
    private TextView timerTextView;
    private Button startButton;
    private Button resetButton;
    private Button saveButton;
    private TextView tripTimeTextView;
    private TextView countTextView;
    private RecyclerView mWalksRecyclerlist;
    long startTime = 0;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            String time = String.format("%d:%02d", minutes, seconds);
            timerTextView.setText(time);
            duration = time;
            timerHandler.postDelayed(this, 500);
        }
    };
    private float lastStepNum = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numOfStepsTextView = findViewById(R.id.numOfStepsTextView);
        timerTextView = findViewById(R.id.timerTextView);
        countTextView = findViewById(R.id.count);
        tripTimeTextView = findViewById(R.id.tripTimeTextView);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        saveButton = findViewById(R.id.saveButton);
        nameEditText = findViewById(R.id.nameEditText);
        nameTextView = findViewById(R.id.nameTextView);
        mWalksRecyclerlist = findViewById(R.id.recycler_view);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        saveButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);

        gestureDetectorCompat = new GestureDetectorCompat(this, new MyGestureListener());

        if (sensorManager != null) {
            mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        }

        countTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapsActivity.start(MainActivity.this, lightValue);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("light-change-event"));

        startButton.setText(getResources().getString(R.string.start_string));
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals(getResources().getString(R.string.stop_string))) {
                    timerHandler.removeCallbacks(timerRunnable);
                    countTextView.setText(getResources().getString(R.string.zero_string));
                    b.setText(getResources().getString(R.string.start_string));
                    saveButton.setVisibility(View.VISIBLE);
                    resetButton.setVisibility(View.VISIBLE);
                    saveButton.setClickable(true);
                    resetButton.setClickable(true);
                    lastStepNum = 0;
                    startButton.setVisibility(View.GONE);
                    stepCounterFrozen = true;

                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText(getResources().getString(R.string.stop_string));
                    saveButton.setVisibility(View.GONE);
                    stepCounterFrozen = false;
                    lastStepNum = mPreviousNumOfSteps;
                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String countVal = (String) countTextView.getText();
                String timerVal = (String) timerTextView.getText();
                String name = String.valueOf(nameEditText.getText());
                WalksKeeper.getInstance().getWalksList().add(0, new Walk(name, timerVal, countVal));
                WalksListActivity.start(MainActivity.this, lightValue);
                saveButton.setClickable(false);
                saveButton.setVisibility(View.INVISIBLE);

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                resetButton.setClickable(false);
                startButton.setVisibility(View.VISIBLE);
                countTextView.setText(getResources().getString(R.string.zero_string));
                timerTextView.setText(getResources().getString(R.string.zeros_string));
                nameEditText.setText("");
                resetButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                lastStepNum = 0;

            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

            if (event2.getX() > event1.getX()) {

                MapsActivity.start(MainActivity.this, lightValue);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }

            if (event2.getX() < event1.getX()) {

                WalksListActivity.start(MainActivity.this, lightValue);
            }
            return true;
        }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            lightValue = intent.getStringExtra("LightValue");
            setActivityTheme();
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setActivityTheme() {
        if (lightValue != null) {

            if (!currentTheme.equals("Dark") && lightValue.equals("Dark")) {
                findViewById(R.id.main_layout).setBackgroundResource(R.color.colorPrimaryDark);
                numOfStepsTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                timerTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                countTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                tripTimeTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                nameTextView.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                nameEditText.setTextColor(getResources().getColor(R.color.myCustomFontColor, null));
                nameEditText.setHintTextColor(getResources().getColor(R.color.myCustomFontColor, null));


                currentTheme = "Dark";
            } else if(!currentTheme.equals("Light") && lightValue.equals("Light")){
                findViewById(R.id.main_layout).setBackgroundResource(R.color.colorPrimary);
                numOfStepsTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                timerTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                countTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                startButton.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                tripTimeTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                nameTextView.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                nameEditText.setTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));
                nameEditText.setHintTextColor(getResources().getColor(R.color.myCustomDarkFontColor, null));


                currentTheme = "Light";

            }
        }
    }

    @Override
    protected void onDestroy() {
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
        timerHandler.removeCallbacks(timerRunnable);
        Button b = findViewById(R.id.startButton);
        b.setText(R.string.start_string);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mPreviousNumOfSteps = event.values[0];
        if (activityRunning && !stepCounterFrozen) {
            countTextView.setText(String.valueOf((int) (mPreviousNumOfSteps - lastStepNum)));
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