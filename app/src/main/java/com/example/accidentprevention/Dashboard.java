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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity  implements SensorEventListener{
    private RequestQueue requestQueue;

    private static Dashboard mInstance;
//    public Gyroscope gyroscope;
//    public Accelerometer accelerometer;
private TriggerEventListener triggerEventListener;
    TextView proximityText;
    JSONObject postparams;
    TextView accelerometerText;
    TextView significantMotionText;
    TextView gyroscopeText;
    Button push;
    JsonObjectRequest jsonObjReq;
    Sensor acceloremeter;
    Sensor proximitySensor;
    Sensor significantMotion;
    Sensor gyroscope;
    String url="";
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
        mInstance = this;
        push=(Button)findViewById(R.id.pushbutton);
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

        url="http://192.168.2.104:5000/";

        postparams = new JSONObject();

        try {
            postparams.put("city", "london");
            postparams.put("timestamp", "1500134255");

        } catch (JSONException e) {
            e.printStackTrace();
        }

         jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, postparams,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                        //Failure Callback

                    }
                });
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){
            //   @Override
            public void onResponse(String response){
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){


                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }

                }){
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params= new HashMap<String,String>();
                //params.put("name",x);
                params.put("name","this");
                params.put("email","that");
                return params;
            }
        };

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInstance.addToRequestQueue(jsonObjReq, "postRequest");
//

            }
        });

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

    public static synchronized Dashboard getInstance() {
        return mInstance;
    }
    /*
    Create a getRequestQueue() method to return the instance of
    RequestQueue.This kind of implementation ensures that
    the variable is instatiated only once and the same
    instance is used throughout the application
    */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());

        return requestQueue;
    }

    /*
    public method to add the Request to the the single
    instance of RequestQueue created above.Setting a tag to every
    request helps in grouping them. Tags act as identifier
    for requests and can be used while cancelling them
    */
    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);

    }

    /*
    Cancel all the requests matching with the given tag
    */

    public void cancelAllRequests(String tag) {
        getRequestQueue().cancelAll(tag);
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
