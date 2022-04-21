package com.chinonso.wearos;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private TextView mTextViewStepCount;
    private TextView mTextViewStepDetect;
    private TextView mTextViewHeart;
    private TextView mRelativeHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Keep the Wear screen always on (for testing only!)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextViewStepCount = (TextView) stub.findViewById(R.id.step_count);
                mTextViewStepDetect = (TextView) stub.findViewById(R.id.step_detect);
                mTextViewHeart = (TextView) stub.findViewById(R.id.heart);
                mRelativeHumidity = (TextView) stub.findViewById(R.id.Relative_Humidity);
                getStepCount();
            }
        });
    }

    private void getStepCount() {
        SensorManager mSensorManager = ((SensorManager)getSystemService(SENSOR_SERVICE));
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        Sensor mStepCountSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor mStepDetectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor mRelativeHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mStepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mStepDetectSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mRelativeHumidity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private String currentTimeStr() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        return df.format(c.getTime());
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged - accuracy: " + accuracy);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String msg = "Heart Rate: " + (int)event.values[0];
            mTextViewHeart.setText(msg);
            Log.d(TAG, msg);
        }
        else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
            String msg = "Humidity: " + (int)event.values[0];
            mTextViewStepCount.setText(msg);
            Log.d(TAG, msg);
        }
        else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            String msg = "Steps Made: " + (int)event.values[0];
            mTextViewStepCount.setText(msg);
            Log.d(TAG, msg);
        }
        else if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            String msg = "Steps Detected at " + currentTimeStr();
            mTextViewStepDetect.setText(msg);
            Log.d(TAG, msg);
        }
        else
            Log.d(TAG, "Unknown sensor type");
    }

}