package com.example.accidentprevention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class Signup extends AppCompatActivity {
    EditText emailtxt,passtxt,confirmpasstxt;
    Button signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailtxt=(EditText) findViewById(R.id.usernameText);
        passtxt=(EditText)findViewById(R.id.passwordText);
        confirmpasstxt=(EditText)findViewById(R.id.repasswordText);
        signup=(Button)findViewById(R.id.signup) ;

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailtxt.getText().toString().trim();
                String password=passtxt.getText().toString().trim();
                String confirmpass=confirmpasstxt.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Signup.this, "Please Enter username",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Signup.this, "Please Enter password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(confirmpass)){
                    Toast.makeText(Signup.this, "Please Confirm password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(Signup.this,"Password too short",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals(confirmpass)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                        Toast.makeText(Signup.this, "Authentication successfull.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Signup.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
