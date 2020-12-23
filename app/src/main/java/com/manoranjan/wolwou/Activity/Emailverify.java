package com.manoranjan.wolwou.Activity;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manoranjan.wolwou.R;
import com.manoranjan.wolwou.Model.Usermodel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Emailverify extends AppCompatActivity {
    EditText email,password;
    String data,password1;
    ProgressDialog progressDialog;
    FirebaseAuth mauth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailverify);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog=new ProgressDialog(Emailverify.this);
        progressDialog.setMessage("please wait");
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        mauth=FirebaseAuth.getInstance();
        //final TextView output=findViewById(R.id.verify);
        Button verify=findViewById(R.id.verify);
        progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Please wait .....");
        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data=email.getText().toString();
                password1=password.getText().toString();
                if ( !data.equals("")){
                    String value=data.substring(data.length()-10);
                    String abc=value.toLowerCase();
                    sendverification();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Enter All fields ",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void sendverification() {
        ProgressDialog progressDialog=new ProgressDialog(Emailverify.this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
//        progressDialog.show();
        mauth.createUserWithEmailAndPassword(data,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){

                                String keyid=mauth.getInstance().getCurrentUser().getUid();
                                Usermodel meetingModel=new Usermodel(data,password1,keyid,"firebase");
                                databaseReference.child(mauth.getInstance().getCurrentUser().getUid()).setValue(meetingModel);
                                //Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                                mauth.signOut();
                               // progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"registered sucessfully please verify your email ",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"registered errorrrrrr "+e,Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"registered failed"+e,Toast.LENGTH_LONG).show();
            }
        });

    }
}
