package com.manoranjan.wolwou.Activity;



import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manoranjan.wolwou.BuildConfig;
import com.manoranjan.wolwou.Static.Configss;
import com.manoranjan.wolwou.Model.MeetingModel;
import com.manoranjan.wolwou.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView joinbutton,createbutton;
    EditText editmeeting,edittmeeting1;
    int jionorcreate=1;
    Button btnjoin;
    ImageView user,designimage;
    LinearLayout linearLayout;
    BottomSheetDialog dialog;
    TextView signin,signout;
    FirebaseAuth mAuth;
    GoogleApiClient googleApiClient;
    String loginrole,email;
    boolean loggedin;
    DatabaseReference databaseReference;
    LinearLayout etlayout,etlayout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinbutton=findViewById(R.id.joinneeting);
        createbutton=findViewById(R.id.createmeeting);
        editmeeting=findViewById(R.id.etmeeting);
        edittmeeting1=findViewById(R.id.etmeeting1);
        etlayout=findViewById(R.id.etlayout);
        etlayout1=findViewById(R.id.et1layout);
        btnjoin=findViewById(R.id.btnjoin);
        user=findViewById(R.id.user);
        designimage=findViewById(R.id.designimage);
        signin=findViewById(R.id.signin);
        signout=findViewById(R.id.signout);
        joinbutton.setOnClickListener(this);
        createbutton.setOnClickListener(this);
        btnjoin.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loginrole = sharedPreferences.getString(Configss.login_role, "0");
        email = sharedPreferences.getString(Configss.Email_id, "default");
        loggedin=sharedPreferences.getBoolean(Configss.LOGGEDIN_SHARED_PREF,false);

        databaseReference= FirebaseDatabase.getInstance().getReference("Meetings");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_SHORT).show();
                init_modal_bottomsheet();
            }
        });
        //init the bottom sheet view
        linearLayout = findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser userauth=mAuth.getInstance().getCurrentUser();
        //Toast.makeText(getApplicationContext(),loginrole,Toast.LENGTH_SHORT).show();
        if (loginrole.equals("2")){
           signin.setText(userauth.getEmail());
           signout.setVisibility(View.VISIBLE);
        }else if (loginrole.equals("1")){
                signin.setText(email);
            signout.setVisibility(View.VISIBLE);
        }else  if (loginrole.equals("3")){
            signin.setText(userauth.getPhoneNumber());
            signout.setVisibility(View.VISIBLE);
        }else{
            signin.setText("Sign in ");
            signout.setVisibility(View.GONE);
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginrole.equals("0")) {
                    Intent intent=new Intent(MainActivity.this,LoginDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userauth!=null && loginrole.equals("2")) {
                    SharedPreferences sharedPreferences = getSharedPreferences
                            (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //Adding values to editor
                    editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, false);
                    editor.putString(Configss.login_role,"0");
                    editor.commit();
                    mAuth.signOut();
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (userauth!=null && loginrole.equals("3")) {
                    SharedPreferences sharedPreferences = getSharedPreferences
                            (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //Adding values to editor
                    editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, false);
                    editor.putString(Configss.login_role,"0");
                    editor.commit();
                    mAuth.signOut();
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    if (loginrole.equals("1")) {
                        mAuth.signOut();
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        if (status.isSuccess()) {
                                            SharedPreferences sharedPreferences = getSharedPreferences
                                                    (Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                            //Creating editor to store values to shared preferences
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            //Adding values to editor
                                            editor.putBoolean(Configss.LOGGEDIN_SHARED_PREF, false);
                                            editor.putString(Configss.login_role,"0");
                                            editor.commit();
                                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                            return;
                                            //gotoMainActivity();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }

                }
                //closing activity

            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        findViewById(R.id.meetinghistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MeetinghistoryActivity.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        findViewById(R.id.sendfeedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","abc@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for App");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        findViewById(R.id.rateapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
            }
        });
        findViewById(R.id.shareapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        edittmeeting1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edittmeeting1.getRight() - edittmeeting1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        String text=edittmeeting1.getText().toString();
                        if (text.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Enter Meeting Code First",Toast.LENGTH_SHORT).show();
                        }else {
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Meeting Code");
                                String shareMessage = "\nMy Meeting code is : \n"+ text;
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(shareIntent, "Share Meeting Code"));
                            } catch (Exception e) {
                                //e.toString();
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        URL serverURL;
        try {
            serverURL = new URL(Configss.serverurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setWelcomePageEnabled(false)
                .setServerURL(serverURL)
                //.setFeatureFlag("call-integration.enabled", false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.joinneeting:
                jionorcreate=1;
               etlayout.setVisibility(View.VISIBLE);
               etlayout1.setVisibility(View.GONE);
                designimage.setImageDrawable(getResources().getDrawable(R.drawable.img1));
                joinbutton.setBackgroundResource(R.drawable.jion_button_border);
                createbutton.setBackgroundResource(R.drawable.jion_button_border_unselect);
                joinbutton.setTextColor(getResources().getColor(R.color.white));
                createbutton.setTextColor(getResources().getColor(R.color.tab_disable));
                 btnjoin.setText("Join");


                break;
            case R.id.createmeeting:
                jionorcreate=2;
                etlayout.setVisibility(View.GONE);
                etlayout1.setVisibility(View.VISIBLE);
               // editmeeting.setHint("Enter Meeting Code");
                designimage.setImageDrawable(getResources().getDrawable(R.drawable.img2));

                createbutton.setBackgroundResource(R.drawable.jion_button_border);
                joinbutton.setBackgroundResource(R.drawable.jion_button_border_unselect);
                createbutton.setTextColor(getResources().getColor(R.color.white));
                joinbutton.setTextColor(getResources().getColor(R.color.tab_disable));

                btnjoin.setText("Create");
                break;
            case R.id.btnjoin:
                String text="null";
                if (jionorcreate==1) {
                    text = editmeeting.getText().toString();
                }else
                if (jionorcreate==2) {
                    text = edittmeeting1.getText().toString();
                }
                if (text.length() > 0 && !text.equals("null")) {

                    if (loggedin) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String currentDateandTime = sdf.format(new Date()); // Find todays date

                        String keyid = databaseReference.push().getKey();
                        MeetingModel meetingModel = new MeetingModel(text, currentDateandTime, keyid);

                        databaseReference.child(mAuth.getInstance().getCurrentUser().getUid()).child(keyid).setValue(meetingModel);
                        //Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JitsiMeetConferenceOptions options
                                = new JitsiMeetConferenceOptions.Builder()
                                .setRoom(text)
                                .build();
                        JitsiMeetActivity.launch(this, options);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    break;
                }else{
                    Toast.makeText(getApplicationContext(),"Please Enter Meeting code",Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }
    public void init_modal_bottomsheet() {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.dialog_my_rounded_bottom_sheet, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(modalbottomsheet);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
      /* =modalbottomsheet.findViewById(R.id.recyclerview);
        ImageView cancel=modalbottomsheet.findViewById(R.id.cancel);
        LinearLayout addaddress=modalbottomsheet.findViewById(R.id.addaddress);*/
    }
}
