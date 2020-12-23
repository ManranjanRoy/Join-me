package com.manoranjan.wolwou.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manoranjan.wolwou.Static.Configss;
import com.manoranjan.wolwou.Static.PinEntryEditText;
import com.manoranjan.wolwou.R;
import com.manoranjan.wolwou.Model.Usermodel;

import java.util.concurrent.TimeUnit;

public class OtpVerifyActivity extends AppCompatActivity {
    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private PinEntryEditText txtPinEntry;

    //firebase auth object
    private FirebaseAuth mAuth;
    String mobile;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* if (s.toString().equals("1234")) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OtpVerifyActivity.this, Login.class));
                } else if (s.length() == "1234".length()) {
                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
                    txtPinEntry.setText(null);

                }*/
                String code = s.toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Enter valid code", Toast.LENGTH_SHORT).show();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                txtPinEntry.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        ProgressDialog progressDialog=new ProgressDialog(OtpVerifyActivity.this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            String keyid = mAuth.getInstance().getCurrentUser().getUid();
                            Usermodel usermodel = new Usermodel(mobile, "123456", keyid, "phone");
                            databaseReference.child(mAuth.getInstance().getCurrentUser().getUid()).setValue(usermodel);
                            Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences
                                    (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //Adding values to editor
                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Configss.Email_id,mobile);
                            editor.putString(Configss.login_role,"3");
                            editor.commit();
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(OtpVerifyActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                        }
                    }
                });
    }

}
