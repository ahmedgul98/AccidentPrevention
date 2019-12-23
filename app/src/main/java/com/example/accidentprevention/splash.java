package com.example.accidentprevention;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth=FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                FirebaseUser  fuser=mAuth.getCurrentUser();
                Intent mainIntent;
                if(fuser!=null){
                    mainIntent = new Intent(splash.this,Dashboard.class);

                }else {
                    mainIntent = new Intent(splash.this,MainActivity.class);

                }
                /* Create an Intent that will start the Menu-Activity. */

                splash.this.startActivity(mainIntent);
                splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
