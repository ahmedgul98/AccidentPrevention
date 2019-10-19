package com.example.accidentprevention;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.EventListener;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final TextView proximityText;
        proximityText=(TextView)findViewById(R.id.proximityView);
        //proximity
        SensorManager sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        final Sensor proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        SensorEventListener sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.values[0]<proximitySensor.getMaximumRange()){
                        proximityText.setText("You are talking to someone on call");
                }
                else{
                        proximityText.setText("Nothing detected");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorEventListener,proximitySensor,2*1000*1000);


        //accelerometer
        final TextView accelerometer;
        accelerometer=(TextView)findViewById(R.id.accelerometer);
        final Sensor acceloremeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener sensorEventListener1=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                accelerometer.setText("X:"+ sensorEvent.values[0]+
                        "Y"+ sensorEvent.values[1]+
                        "Z"+ sensorEvent.values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sensorManager.registerListener(sensorEventListener1,acceloremeter,SensorManager.SENSOR_DELAY_NORMAL);

    }
}
