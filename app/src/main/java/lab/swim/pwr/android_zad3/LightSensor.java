package lab.swim.pwr.android_zad3;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
/*public class service extends Service implements SensorEventListener {
@Override

public void onCreate() {
    super.onCreate();
//register your sensor manager listener here
}

@Override
public void onDestroy() {
//unregister your listener here
}

@Override
public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        //detect the shake and do your work here
    }

}*/
public class LightSensor extends Service implements SensorEventListener {

    @Override
    public void onCreate() {
        super.onCreate();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (light == null)
            stopSelf();
        else {
            sensorManager
                    .registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    /*
        @Override
        protected void onResume() {
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            super.onResume();
        }

        @Override
        protected void onPause() {
            mSensorManager.unregisterListener(this);
            super.onPause();
        }
    */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.i("Sensor Changed", "Accuracy :" + accuracy);
        }

    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
 //           Log.i("Sensor Changed", "onSensor Change :" + event.values[0]);
            Intent intent = new Intent("custom-event-name");
            if(event.values[0] < 5.0){

                //findViewById(R.id.main_layout).setBackgroundColor(4242);

                // You can also include some extra data.

                intent.putExtra("LightValue", "Dark");
            } else

                intent.putExtra("LightValue", "Light");

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}