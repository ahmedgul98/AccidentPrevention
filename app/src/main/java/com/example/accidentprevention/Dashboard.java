package com.example.accidentprevention;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;

import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//
//import java.util.HashMap;
//import java.util.Map;

public class Dashboard extends AppCompatActivity  implements SensorEventListener{

//    public Gyroscope gyroscope;
//    public Accelerometer accelerometer;
private TriggerEventListener triggerEventListener;
    TextView proximityText;
    TextView accelerometerText;
    TextView significantMotionText;
    TextView gyroscopeText;
    //Button push;
    Sensor acceloremeter;
    Sensor proximitySensor;
    Sensor significantMotion;
    Sensor gyroscope;
//    String url="";
//    String x="jsjfh";
Boolean isTouch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        isTouch=false;
        accelerometerText=(TextView)findViewById(R.id.accelerometer);
        proximityText=(TextView)findViewById(R.id.proximityView);
        significantMotionText=(TextView)findViewById(R.id.significantMotionView);
        gyroscopeText=(TextView)findViewById(R.id.gyroscopeView);
      //  push=(Button)findViewById(R.id.pushButton);
        SensorManager sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        acceloremeter=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        significantMotion=sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        gyroscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        triggerEventListener=new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent triggerEvent) {
                significantMotionText.setText("Triggered");

            }
        };

//        push.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                StringRequest stringRequest=new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
//                 //   @Override
//                    public void onResponse(String response){
//
//                    }
//                },
//                 new Response.ErrorListener() {
//                    @Override
//                        public void onErrorResponse(VolleyError error){
//
//                        }
//
//                 }){
//                  protected Map<String,String> getParams() throws AuthFailureError{
//                    Map<String,String> params= new HashMap<String,String>();
//                    //params.put("name",x);
//                      params.put("name","this");
//                      params.put("email","that");
//                      return params;
//                  }
//                };
//            }
//        });

        sensorManager.registerListener(this,acceloremeter,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,proximitySensor,2*1000*1000);
        sensorManager.registerListener(this,gyroscope,SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.requestTriggerSensor(triggerEventListener,significantMotion);


        if(significantMotion==null){
            significantMotionText.setText("Not detected");
        }
        if(gyroscope==null){
            gyroscopeText.setText("Not detected");
        }
        if(acceloremeter==null){
            accelerometerText.setText("Not detected");
        }
        if(proximitySensor==null){
            proximityText.setText("0");
        }
    }
   @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            gyroscopeText.setText("v"+event.values[0]);
        }
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if(event.values[0]<proximitySensor.getMaximumRange()){
                        proximityText.setText("You are talking to someone on call");


                }
                else{
                        proximityText.setText("Nothing detected");
                }
           }
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerText.setText("X:" + event.values[0] + "Y" + event.values[1] + "Z" + event.values[2]);
                }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();

        int eventaction = event.getAction();
        switch (eventaction) {

            case MotionEvent.ACTION_DOWN:
                Toast.makeText(this, "ACTION_DOWN AT COORDS " + "X: " + X + " Y: " + Y, Toast.LENGTH_SHORT).show();
                isTouch = true;
                break;

            case MotionEvent.ACTION_MOVE:
                Toast.makeText(this, "MOVE "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
                break;

            case MotionEvent.ACTION_UP:
                Toast.makeText(this, "ACTION_UP "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
