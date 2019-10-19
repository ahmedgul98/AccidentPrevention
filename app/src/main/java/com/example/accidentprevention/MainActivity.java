package com.example.accidentprevention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText emailtxt,passwordtext;
    Button signin,signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailtxt=(EditText)findViewById(R.id.usernameText);
        passwordtext=(EditText)findViewById(R.id.passwordText);
        signin=(Button)findViewById(R.id.signinButton);
        signup=(Button)findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Signup.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String email=emailtxt.getText().toString().trim();
             String password=passwordtext.getText().toString().trim();
             if(TextUtils.isEmpty(email)){
                 Toast.makeText(MainActivity.this, "Please enter Email",
                         Toast.LENGTH_SHORT).show();
                 return;
             }

             if(TextUtils.isEmpty(password)){
                 Toast.makeText(MainActivity.this, "Please enter password",
                         Toast.LENGTH_SHORT).show();
             return;
             }
                if(password.length()<6){
                    Toast.makeText(MainActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });
            }
        });
       }
    }
