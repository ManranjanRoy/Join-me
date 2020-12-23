package com.manoranjan.wolwou.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.manoranjan.wolwou.R;

public class Forgot_pass extends AppCompatActivity {
    EditText send_email;
    Button btn_rst;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog=new ProgressDialog(Forgot_pass.this);
        btn_rst=findViewById(R.id.btn_rst);
        send_email=findViewById(R.id.send_email);

        firebaseAuth=FirebaseAuth.getInstance();
        btn_rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                String email=send_email.getText().toString();
                if (email.equals(""))
                {progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Enter email ",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                finish();
                                Toast.makeText(getApplicationContext(),"Please Check your email ",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Forgot_pass.this,Login.class));
                                progressDialog.dismiss();
                            }
                            else{
                                progressDialog.dismiss();
                                String error=task.getException().toString();
                                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });

    }
}
