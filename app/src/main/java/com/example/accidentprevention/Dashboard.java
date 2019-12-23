package com.example.accidentprevention;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;

import android.hardware.TriggerEventListener;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.openxc.VehicleManager;
import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.messages.VehicleMessage;
import com.openxc.units.Unit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity  implements SensorEventListener{
    private RequestQueue requestQueue;
    private static final String TAG = "StarterActivity";

    private VehicleManager mVehicleManager;
    private TextView mEngineSpeedView;
    private ServiceConnection mConnection;
    private static Dashboard mInstance;
//    public Gyroscope gyroscope;
//    public Accelerometer accelerometer;
private TriggerEventListener triggerEventListener;
    TextView proximityText;
    JSONObject postparams;
    TextView accelerometerText;
    driveractivity obj;
    TextView significantMotionText;
    TextView gyroscopeText;
    Button push;
    JsonObjectRequest jsonObjReq;
    Sensor acceloremeter;
    int touches=0,proxies=0;
    Sensor proximitySensor;
    Sensor significantMotion;
    Gson gson;
    Sensor gyroscope;
    String url="";
    int sh=0,noi;
driveractivity dr;
    EditText edittext;
    float gx,gy,gz,ax,ay,az;
    double noisedb=0;

    //    public Gyroscope gyroscope;
//    public Accelerometer accelerometer;

    TextView amplitude;
    RelativeLayout shakes,noise;

    float last_x=0,last_y=0,last_z=0;
    long lastUpdate=0;
    private static final int SHAKE_THRESHOLD = 800;
    MediaRecorder mRecorder;

    VehicleSpeed carspeed;


    VehicleSpeed.Listener mSpeedListener= new VehicleSpeed.Listener() {
        @Override
        public void receive(Measurement measurement) {
            carspeed= (VehicleSpeed) measurement;
        }
    };


    String urledit;
    int liftup=0;
    //    String url="";
    boolean pushed=false;
    //    String x="jsjfh";
    PowerManager.WakeLock mWakeLock;
    Boolean isTouch;
    VehicleMessage.Listener mlistener=new VehicleMessage.Listener() {
        @Override
        public void receive(VehicleMessage message) {

            Toast.makeText(getApplicationContext(),message.toString(),Toast.LENGTH_LONG).show();
        }
    };
    Handler mHandler = new Handler();


    private Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");
            mRecorder.start();

            if (!mWakeLock.isHeld()) {
                mWakeLock.acquire();
            }

            //Noise monitoring start
            // Runnable(mPollTask) will execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, 300);
        }
    };
    private boolean proximity=false;
    private float mAccelLast;
    private float mAccelCurrent;
    private float mAccel;

    private void shakeItBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
        }
    }
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mRecorder.getMaxAmplitude();
            double amps=20 * Math.log10(amp / 2700.0);
            //Log.i("Noise", "runnable mPollTask");
            noisedb=amps;
            updateDisplay(amps);
            senddata();
//
//            if ((amp > mThreshold)) {
//                callForHelp(amp);
//                //Log.i("Noise", "==== onCreate ===");
//            }
            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, 300);
        }




        private void senddata()
        {

            dr=new driveractivity(gx,gy,gz,ax,ay,az,noi,sh);
            try {
                if(isTouch) touches=1;
                else touches=0;
                isTouch=false;

                if(proximity) proxies=1;
                else proxies=0;
                postparams.put("gyrox", gx);
                postparams.put("gyroy",gy);
                postparams.put("gyroz",gz);
                postparams.put("accelx",ax);
                postparams.put("accely",ay);
                postparams.put("accelz",az);
                postparams.put("noise",noisedb);
                postparams.put("Shake",sh);
                postparams.put("Carspeed",carspeed.getValue().doubleValue());
                postparams.put("touch",touches);
                postparams.put("proximity",proxies);




                jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, postparams,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {


                                if(response.toString().equals("{\"out\":\"Distracted\"}")){
                                    shakeItBaby();
                                    Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                                }else if(response.equals("{\"out\":\"Distracted\"}")){
                                    Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                                //Failure Callback

                            }
                        });

                mInstance.addToRequestQueue(jsonObjReq, "postRequest");

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            String drjson=gson.toJson(dr);


        }

   // Car data listeners

   AcceleratorPedalPosition.Listener accelerator= new AcceleratorPedalPosition.Listener() {
       @Override
       public void receive(Measurement measurement) {

       }
   };



        private void updateDisplay(double amp) {
            if(amp>18) {
                amplitude.setText("Tooo loud ");
                noise.setVisibility(View.VISIBLE);
                noi=1;

            }else{
                amplitude.setText("LOW Noise");
                noise.setVisibility(View.INVISIBLE);
                noi=0;

            }



        }
    };
    private void validateMicAvailability()  {
        AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);
        try{
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED ){

            }

            mHandler.postDelayed(mSleepTask,300);
            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING){
                recorder.stop();

            }
            recorder.stop();
        } finally{
            recorder.release();
            recorder = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // When the activity starts up or returns from the background,
        // re-connect to the VehicleManager so we can receive updates.
        if(mVehicleManager == null) {
            Intent intent = new Intent(this, VehicleManager.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        isTouch=false;
        accelerometerText=(TextView)findViewById(R.id.accelerometer);
        proximityText=(TextView)findViewById(R.id.proximityView);
        significantMotionText=(TextView)findViewById(R.id.significantMotionView);
        gyroscopeText=(TextView)findViewById(R.id.gyroscopeView);
        amplitude=(TextView) findViewById(R.id.amplitude);
        //  push=(Button)findViewById(R.id.pushButton);
        mInstance = this;
        edittext=(EditText) findViewById(R.id.editText);
        noise=( RelativeLayout) findViewById(R.id.alarm1);
        shakes=( RelativeLayout) findViewById(R.id.alarm2);

        mConnection = new ServiceConnection() {
            // Called when the connection with the VehicleManager service is established
            public void onServiceConnected(ComponentName className, IBinder service) {
                Log.i(TAG, "Bound to VehicleManager");
                // When the VehicleManager starts up, we store a reference to it
                // here in "mVehicleManager" so we can call functions on it
                // elsewhere in our code.
                mVehicleManager = ((VehicleManager.VehicleBinder) service)
                        .getService();

                // We want to receive updates whenever the EngineSpeed changes. We
                // have an EngineSpeed.Listener (see above, mSpeedListener) and here
                // we request that the VehicleManager call its receive() method
                // whenever the EngineSpeed changes
                mVehicleManager.addListener(VehicleSpeed.class, mSpeedListener);
            }

            // Called when the connection with the service disconnects unexpectedly
            public void onServiceDisconnected(ComponentName className) {
                Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
                mVehicleManager = null;
            }
        };


        mInstance = this;
        push=(Button)findViewById(R.id.pushButton);
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
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    0);

        }else {

        }
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile("/dev/null");
        try {
            mRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        url="http://192.168.10.24:5000/";

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

                        if(response.toString().equals("{\"out\":\"Distracted\"}")){
                           shakeItBaby();
                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                        }else if(response.equals("Notdistracted")){
                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                        }

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
                if(!pushed){
                    mHandler.postDelayed(mSleepTask,300);
                    pushed=true;
                }else {
                    mHandler.removeCallbacks(mSleepTask);
                    pushed=false;
                }

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
            gx=event.values[0];
            gy=event.values[1];
            gz=event.values[2];

        }
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if(event.values[0]<proximitySensor.getMaximumRange()){
                proximityText.setText("You are talking to someone on call");
                proximity=true;


            }
            else{
                proximityText.setText("Nothing detected");
                proximity=false;
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float[] mGravity = event.values.clone();
                // Shake detection
                float a = mGravity[0];
                float b= mGravity[1];
                float c = mGravity[2];

                float yAbs = Math.abs(mGravity[1]);


                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;

                if (yAbs > 2.0 && yAbs < 4.0 && !proximity) {
                    liftup=1;
                }else {
                    liftup=0;
                }
                ax=x;
                ay=y;
                az=z;
                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.d("sensor", "shake detected w/ speed: " + speed);
                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
                    accelerometerText.setText("Phone Movement");
                    shakes.setVisibility(View.VISIBLE);
                    sh=1;



                }else {
                    accelerometerText.setText("No Movement");
                    shakes.setVisibility(View.INVISIBLE);
                    sh=0;
                }

                ax=x;
                ay=y;
                az=z;
                last_x = x;
                last_y = y;
                last_z = z;
            }
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

        isTouch = true;

        int eventaction = event.getAction();

        //        switch (eventaction) {
//
//            case MotionEvent.ACTION_DOWN:
//                Toast.makeText(this, "ACTION_DOWN AT COORDS " + "X: " + X + " Y: " + Y, Toast.LENGTH_SHORT).show();
//                isTouch = true;
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                Toast.makeText(this, "MOVE "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
//                break;
//
//            case MotionEvent.ACTION_UP:
//                Toast.makeText(this, "ACTION_UP "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                isTouch=false;
//
//        }


        return true;
    }
}
