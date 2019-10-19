package com.example.accidentprevention;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.EventListener;

import static android.view.View.X;

public class Dashboard extends AppCompatActivity implements SensorEventListener {
    TextView proximityText;
    TextView accelerometer;
    Sensor acceloremeter;
    Sensor proximitySensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        accelerometer=(TextView)findViewById(R.id.accelerometer);

        proximityText=(TextView)findViewById(R.id.proximityView);
        //proximity
        SensorManager sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        acceloremeter=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,acceloremeter,SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this,proximitySensor,2*1000*1000);
    accelerometer.setText("hello");


        //        SensorEventListener sensorEventListener=new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                if(sensorEvent.values[0]<proximitySensor.getMaximumRange()){
//                        proximityText.setText("You are talking to someone on call");
//                }
//                else{
//                        proximityText.setText("Nothing detected");
//                }
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//
//            }
//        };


        //accelerometer
//        acceloremeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        SensorEventListener sensorEventListener1=new SensorEventListener() {
//            @Override
//            public void onSensorChanged(SensorEvent sensorEvent) {
//                accelerometer.setText("X:"+ sensorEvent.values[0]+
//                        "Y"+ sensorEvent.values[1]+
//                        "Z"+ sensorEvent.values[2]);
//            }
//
//            @Override
//            public void onAccuracyChanged(Sensor sensor, int i) {
//
//            }
//        };

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if(event.values[0]<proximitySensor.getMaximumRange()){
                        proximityText.setText("You are talking to someone on call");
                }
                else{
                        proximityText.setText("Nothing detected");
                }
           }
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        accelerometer.setText("X:" + event.values[0] + "Y" + event.values[1] + "Z" + event.values[2]);
    }

//            }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
