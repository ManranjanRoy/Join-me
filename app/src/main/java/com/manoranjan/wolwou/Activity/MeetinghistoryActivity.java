package com.manoranjan.wolwou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manoranjan.wolwou.Static.Configss;
import com.manoranjan.wolwou.Adaptor.MeetingHistoryAdaptor;
import com.manoranjan.wolwou.Model.MeetingModel;
import com.manoranjan.wolwou.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MeetinghistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<MeetingModel> meetinglist;
    MeetingHistoryAdaptor meetingHistoryAdaptor;
    String tokencode;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    boolean loggedin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetinghistory);
        progressDialog=new ProgressDialog(MeetinghistoryActivity.this);
        progressDialog.setMessage("Please Wait");
        SharedPreferences sharedPreferences = getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loggedin=sharedPreferences.getBoolean(Configss.LOGGEDIN_SHARED_PREF,false);
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
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth= FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.recyclerview1);
        meetinglist=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
         if (loggedin){
          fetchdata();}
         else{
             Toast.makeText(getApplicationContext(),"Login to save history ",Toast.LENGTH_SHORT).show();
         }
    }

    private void fetchdata() {
        ProgressDialog progressDialog=new ProgressDialog(MeetinghistoryActivity.this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference =    mFirebaseDatabase.getReference("Meetings").child(mAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    progressDialog.dismiss();
                    Log.v("firebaseresult", "" + childDataSnapshot.getKey()); //displays the key for the node
                    MeetingModel meetingModel=childDataSnapshot.getValue(MeetingModel.class);
                    meetinglist.add(meetingModel);
                }
                if (meetinglist.size()==0){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_SHORT).show();
                }else {
                    meetingHistoryAdaptor = new MeetingHistoryAdaptor(getApplicationContext(), meetinglist);
                    recyclerView.setAdapter(meetingHistoryAdaptor);


                    meetingHistoryAdaptor.setonItemClickListner(new MeetingHistoryAdaptor.OnitemClickListner() {
                        @Override
                        public void OnItemCLiCK(int position) {

                        }

                        @Override
                        public void Joinmeetinh(int position) {
                            String text = meetinglist.get(position).getMeetingcode();
                            JitsiMeetConferenceOptions options
                                    = new JitsiMeetConferenceOptions.Builder()
                                    .setRoom(text)
                                    .build();
                            JitsiMeetActivity.launch(MeetinghistoryActivity.this, options);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
