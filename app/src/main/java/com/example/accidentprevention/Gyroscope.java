package com.example.accidentprevention;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class Gyroscope {

//    public interface Listener{
//        void onTranslation(float tx,float ty,float tz);
//    }
//
//    public Listener listener;
//
//    public void setListener(Listener l){
//        listener=l;
//    }
    public interface Listener{
        void onRotation(float rx,float ry,float rz);
    }
    public  Listener listener;

    public  void setListener(Listener l){
        listener=l;
    }

    SensorManager sensorManager;
    Sensor gyroscope;
    SensorEventListener sensorEventListener;

    Gyroscope(Context context){
        sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener =new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(listener!=null){
                    listener.onRotation(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    public void register(){
        sensorManager.registerListener(sensorEventListener,gyroscope,sensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unregister(){
        sensorManager.unregisterListener(sensorEventListener);
    }

}
