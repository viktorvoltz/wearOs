package com.chinonso.wearos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity implements SensorEventListener {

    private TextView display;


    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;
    double distance;

    private static final String TAG = "MainActivity";
    private TextView mTextViewStepCount;
    private TextView mTextViewStepDetect;
    private TextView mTextViewHeart;
    private TextView mPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Keep the Wear screen always on (for testing only!)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
            return;
        }
        lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, Loclist);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextViewStepCount = (TextView) stub.findViewById(R.id.step_count);
                mTextViewStepDetect = (TextView) stub.findViewById(R.id.step_detect);
                mTextViewHeart = (TextView) stub.findViewById(R.id.heart);
                display = (TextView) stub.findViewById(R.id.Relative_Humidity);
                getStepCount();


                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, Loclist);
                @SuppressLint("MissingPermission") Location loc = lm.getLastKnownLocation(lm.GPS_PROVIDER);

                if(loc==null){
                    display.setText("No GPS location found");
                }
                else{
                    //set Current latitude and longitude
                    currentLon=loc.getLongitude();
                    currentLat=loc.getLatitude();
                    Log.d(TAG, "Longitude: " + currentLon);
                    Log.d(TAG, "Latitude: " + currentLat);
                    display.setText("Long" + currentLon);


                }
                //Set the last latitude and longitude
                lastLat=currentLat;
                lastLon=currentLon ;
            }
        });
    }

    LocationListener Loclist = new LocationListener(){
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            //start location manager
            LocationManager lm =(LocationManager) getSystemService(LOCATION_SERVICE);

            //Get last location
            @SuppressLint("MissingPermission") Location loc = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //Request new location
            lm.requestLocationUpdates(lm.GPS_PROVIDER, 0,0, Loclist);

            //Get new location
            Location loc2 = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //get the current lat and long
            currentLat = loc.getLatitude();
            currentLon = loc.getLongitude();


            Location locationA = new Location("point A");
            locationA.setLatitude(lastLat);
            locationA.setLongitude(lastLon);

            Location locationB = new Location("point B");
            locationB.setLatitude(currentLat);
            locationB.setLongitude(currentLon);

            double distanceMeters = locationA.distanceTo(locationB);

            double distanceKm = distanceMeters / 1000f;

            display.setText(String.format("%.2f Km",distanceKm ));

        }



        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }



        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }



        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub



        }

    };


    private void getStepCount() {
        SensorManager mSensorManager = ((SensorManager)getSystemService(SENSOR_SERVICE));
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        Sensor mStepCountSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor mStepDetectSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor mPressureSenSor = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mStepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mStepDetectSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mPressureSenSor, SensorManager.SENSOR_DELAY_NORMAL);
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