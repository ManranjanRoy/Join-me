package com.manoranjan.wolwou.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manoranjan.wolwou.R;

public class Phonelogin extends AppCompatActivity {
    EditText mobileno;
    Button sendopt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonelogin);
        mobileno=findViewById(R.id.edittextmobileno);
        sendopt=findViewById(R.id.sendotp);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  number=mobileno.getText().toString();
                if (TextUtils.isEmpty(number)){
                    Toast.makeText(getApplicationContext(),"Enter Mobile Number",Toast.LENGTH_LONG).show();
                }else if (number.length() <10){
                    Toast.makeText(getApplicationContext(),"Enter Valid Mobile Number",Toast.LENGTH_LONG).show();
                }else{

                    Intent intent = new Intent(Phonelogin.this, OtpVerifyActivity.class);
                    intent.putExtra("mobile", number);
                    startActivity(intent);;
                }
            }
        });


    }
}
