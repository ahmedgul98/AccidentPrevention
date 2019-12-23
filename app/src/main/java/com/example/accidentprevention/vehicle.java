package com.example.accidentprevention;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.openxc.VehicleManager;
import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.BaseMeasurement;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.messages.VehicleMessage;

public class vehicle extends Activity {
    private static final String TAG = "StarterActivity";

    private VehicleManager mVehicleManager;
    private TextView mEngineSpeedView;
    private ServiceConnection mConnection;
    BaseMeasurement.Listener listener= new BaseMeasurement.Listener() {
        @Override
        public void receive(Measurement measurement) {


        }
    };


    VehicleSpeed.Listener mSpeedListener = new VehicleSpeed.Listener() {
        public void receive(Measurement measurement) {
            final VehicleSpeed speed = (VehicleSpeed) measurement;
            // do stuff with the measurement
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    }
