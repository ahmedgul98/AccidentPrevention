package com.example.accidentprevention;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {

    public interface Listener{
        void onTranslation(float tx,float ty,float tz);
    }

    public Listener listener;

    public void setListener(Listener l){
        listener=l;
    }

    public SensorManager sensorManager;
    public Sensor linearAcceleration;
    public SensorEventListener sensorEventListener;


    Accelerometer(Context context){
        sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        linearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                    if(listener !=null){
                        listener.onTranslation(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
                    }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    public void register(){
        sensorManager.registerListener(sensorEventListener,linearAcceleration,sensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unregister(){
        sensorManager.unregisterListener(sensorEventListener);
    }


}
