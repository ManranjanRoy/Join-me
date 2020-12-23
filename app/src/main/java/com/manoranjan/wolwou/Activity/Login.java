package com.manoranjan.wolwou.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manoranjan.wolwou.Static.Configss;
import com.manoranjan.wolwou.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity{
    EditText username,password,email;
    Button register;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    TextView txt_forgot;
    String loginrole;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loginrole = sharedPreferences.getString(Configss.login_role, "0");
        FirebaseApp.initializeApp(Login.this);


        mAuth= FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("User");
       /* FirebaseUser user=mAuth.getInstance().getCurrentUser();
        if (user!=null){
            Intent intent=new Intent(Login.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (loginrole.equals("1")){
            Intent intent=new Intent(Login.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }*/

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(Login.this);


        email=findViewById(R.id.emailid);
        password=findViewById(R.id.password);
        txt_forgot=findViewById(R.id.forgot_txt);
        TextView signup=findViewById(R.id.signup_txt);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Emailverify.class));
            }
        });
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Forgot_pass.class));
            }
        });
        register=(Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                login();

            }
        });


    }

    void login() {
        String email1 = email.getText().toString();
        String pass = password.getText().toString();
        if (email1.equals("") || pass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Username & Password ", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        } else {
            mAuth.signInWithEmailAndPassword(email1, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("logincheck","1");
                        progressDialog.dismiss();
                        if (mAuth.getCurrentUser().isEmailVerified()){
                            Log.d("logincheck","2");
                            SharedPreferences sharedPreferences = getSharedPreferences
                                    (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //Adding values to editor
                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Configss.login_role,"2");
                            editor.commit();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else{
                            Log.d("logincheck","3");
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please verify your email to login ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("logincheck","4");
                        Toast.makeText(getApplicationContext(), "task failure ", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("logincheck","5");
                    Toast.makeText(getApplicationContext(), "Faileure "+e.toString(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
