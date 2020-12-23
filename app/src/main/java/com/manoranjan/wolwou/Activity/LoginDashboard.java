package com.manoranjan.wolwou.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manoranjan.wolwou.Static.Configss;
import com.manoranjan.wolwou.R;
import com.manoranjan.wolwou.Model.Usermodel;

public class LoginDashboard extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {
Button siskip,sigoogle,siemail,siphone;
    FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;
    TextView textView;
    private static final int RC_SIGN_IN = 1;
    //private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    String loginrole;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dashboard);
        siskip=findViewById(R.id.siskip);
        sigoogle=findViewById(R.id.sigoogle);
        siemail=findViewById(R.id.siemail);
        siphone=findViewById(R.id.siphoneno);
        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loginrole = sharedPreferences.getString(Configss.login_role, "0");

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getInstance().getCurrentUser();
        if (user!=null  && loginrole.equals("2")){
            Intent intent=new Intent(LoginDashboard.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else  if (user!=null  && loginrole.equals("3")){
            Intent intent=new Intent(LoginDashboard.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (loginrole.equals("1")){
            Intent intent=new Intent(LoginDashboard.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        //google sign in
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,LoginDashboard.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        siskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(LoginDashboard.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        sigoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
        siphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginDashboard.this,Phonelogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        siemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginDashboard.this,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();

            String thisString=account.getDisplayName();

            String[] parts = thisString.split(" ");
            String first = parts[0];//"hello"
            String second = parts[1];//"World"
            registertofirebase(account);
            // loginbyfborgmail(account.getEmail(),first,second,"2");
            Log.d("glogin",account.getDisplayName()+account.getEmail()+account.getId());
           /* Intent intent=new Intent(LoginDashboard.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            //userName.setText(account.getDisplayName());
            //userEmail.setText(account.getEmail());
            //userId.setText(account.getId());
            // gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void registertofirebase(GoogleSignInAccount account) {
        ProgressDialog progressDialog=new ProgressDialog(LoginDashboard.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(account.getEmail(), "123456")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            String keyid=mAuth.getInstance().getCurrentUser().getUid();
                            Usermodel usermodel=new Usermodel(account.getEmail(),"123456",keyid,"gmail");
                            databaseReference.child(mAuth.getInstance().getCurrentUser().getUid()).setValue(usermodel);
                            Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences
                                    (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //Adding values to editor
                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Configss.Email_id,account.getEmail());
                            editor.putString(Configss.login_role,"1");
                            editor.commit();
                            Intent intent = new Intent(LoginDashboard.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }else{
                            login(account);
                            //display some message here
                            //Toast.makeText(LoginDashboard.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    void login(GoogleSignInAccount account) {
        ProgressDialog progressDialog=new ProgressDialog(LoginDashboard.this);
        progressDialog.setMessage("please wait ....");
        progressDialog.show();
            mAuth.signInWithEmailAndPassword(account.getEmail(), "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        SharedPreferences sharedPreferences = getSharedPreferences
                                    (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //Adding values to editor
                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Configss.Email_id,account.getEmail());
                            editor.putString(Configss.login_role,"1");
                            editor.commit();
                            Intent intent = new Intent(LoginDashboard.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "error ", Toast.LENGTH_LONG).show();
                        }
                }
            });
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
