package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.VisibilitySetterAction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.Student;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.social_network.pnu_app.network.NetworkStatus;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;



public class ProfileStudent extends AppCompatActivity {


    TextView tvPIBvalue;
    TextView tvFacultyValue;
    TextView tvGroupValue;
    TextView tvDateOfEntryValue;
    TextView tvFormStudyingValue;

    Button btnAddToFriends;
    Button btnlistFriends;


    private String CurrentStateFriend;

    private DatabaseReference FriendRequestsReferenceReceiver;  //  Отримувач
    private DatabaseReference FriendRequestsReferenceSender;  //  Відправник

    String senderUserId;
    String ReceiverStudentKey;

    public CircleImageView imStudentMainPhoto;
    public ImageView imSendPhotoWall;

    String name;
    String lastName;
    String group;
    String dateOfEntry;
    String formStudying;
    String faculty;
    String linkFirebaseStorageMainPhoto;
    ValueEventListener valueEventListener;
    NetworkStatus network = new NetworkStatus();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ReceiverStudentKey = getIntent().getExtras().get("VisitedStudentKey").toString();
        senderUserId = getKeyCurrentStudend(AppDatabase.getAppDatabase(ProfileStudent.this));

        FriendRequestsReferenceReceiver = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey).
                child("FriendRequestReceiver").child(senderUserId).child("requestType");

        FriendRequestsReferenceSender = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).
                child("FriendRequestSender").child(ReceiverStudentKey).child("requestType");

        btnAddToFriends = findViewById(R.id.btnAddToFriends);
        btnlistFriends = findViewById(R.id.btnListFriendsProfile);

        tvPIBvalue = findViewById(R.id.tvPIBvalueProfile);
        tvFacultyValue = findViewById(R.id.tvFacultyValueProfile);
        tvGroupValue = findViewById(R.id.tvGroupValueProfile);
        tvDateOfEntryValue = findViewById(R.id.tvDateOfEntryValueProfile);
        tvFormStudyingValue = findViewById(R.id.tvFormStudyingValueProfile);


        btnAddToFriends.setOnClickListener(btnlistener);
        btnlistFriends.setOnClickListener(btnlistener);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_profileProfile);
        bottomNavigationView.setSelectedItemId((R.id.action_main_student_page));

        CurrentStateFriend = "notFriend";

        menuChanges(bottomNavigationView);

        imStudentMainPhoto = findViewById(R.id.imStudentMainPhotoProfile);
        imSendPhotoWall = findViewById(R.id.imSendPhotoWallProfile);


        BuildStudentPage();


    }

    private String getKeyCurrentStudend(final AppDatabase db) {
        String keyStudent = db.studentDao().getKeyStudent();
        return keyStudent;
    }


    public void BuildStudentPage(){
        Query queryByKey = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey);

        if(!network.isOnline()){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        else{
        queryByKey.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                System.out.println("dataSnapshot =" + dataSnapshot);

                name = dataSnapshot.child("name").getValue().toString();
                lastName = dataSnapshot.child("lastName").getValue().toString();
                group = dataSnapshot.child("group").getValue().toString();
                dateOfEntry = dataSnapshot.child("dateOfEntry").getValue().toString();
                formStudying = dataSnapshot.child("formStudying").getValue().toString();
                faculty = dataSnapshot.child("faculty").getValue().toString();
                linkFirebaseStorageMainPhoto = dataSnapshot.child("linkFirebaseStorageMainPhoto").getValue().toString();

                tvPIBvalue.setText(lastName + " " + name);
                tvFacultyValue.setText(" " + faculty);
                tvGroupValue.setText(" " + group);
                tvDateOfEntryValue.setText(" " + dateOfEntry);
                tvFormStudyingValue.setText(" " + formStudying);

                Picasso.with(ProfileStudent.this)
                        .load(linkFirebaseStorageMainPhoto)
                        .placeholder(R.drawable.logo_pnu)
                        .error(R.mipmap.ic_error2)
                        .centerCrop()
                        .fit()
                        // .resize(1920,2560)
                        .into(imStudentMainPhoto);

                Picasso.with(ProfileStudent.this)
                        .load(linkFirebaseStorageMainPhoto)
                        .placeholder(R.drawable.logo_pnu)
                        .error(R.mipmap.ic_error2)
                        .centerCrop()
                        .fit()
                        // .resize(1920,2560)
                        .into(imSendPhotoWall);

                DatabaseReference referenceCheckFriendRequestsReceiver;

                referenceCheckFriendRequestsReceiver = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey);
                referenceCheckFriendRequestsReceiver.child("FriendRequestReceiver").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(senderUserId)) {
                            String reqType = dataSnapshot.child(senderUserId).child("requestType").getValue().toString();

                            if (reqType.equals("sent")){
                                CurrentStateFriend = "requestSent";
                                btnAddToFriends.setText("Відхилити запит в друзі");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!network.isOnline()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        }


    }

    public void menuChanges(BottomNavigationView bottomNavigationView){

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent intentMenu;
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Search");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_message:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Messenger");
                                startActivity(intentMenu);


                                break;
                            case R.id.action_main_student_page:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.MainStudentPage");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_schedule:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Schedule");
                                startActivity(intentMenu);

                                break;
                            case R.id.action_settings:
                                intentMenu = new Intent( "com.social_network.pnu_app.pages.Settings");
                                startActivity(intentMenu);

                                break;
                        }
                        return false;
                    }
                });
    }

    View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddToFriends:
                    btnAddToFriends.setEnabled(false);

                    if(CurrentStateFriend.equals("notFriend")) {
                        SendRequestOnAFriendship();
                    }
                    if(CurrentStateFriend.equals("requestSent")) {
                        CancelFriendRequest();
                    }

                    break;
                case R.id.btnListFriends:

                    Intent intentlistMyFriends;
                    intentlistMyFriends = new Intent( "com.social_network.pnu_app.pages.Friends");
                    startActivity(intentlistMyFriends);
                    break;
            }
        }
    };

    private void CancelFriendRequest() {
        FriendRequestsReferenceReceiver.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FriendRequestsReferenceSender.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                btnAddToFriends.setEnabled(true);
                                CurrentStateFriend ="notFriend";
                                btnAddToFriends.setText("Додати в друзі");
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (!network.isOnline()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!network.isOnline()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SendRequestOnAFriendship() {
        FriendRequestsReferenceReceiver.setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FriendRequestsReferenceSender.setValue("receiver").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            btnAddToFriends.setEnabled(true);
                            CurrentStateFriend = "requestSent";
                            btnAddToFriends.setText("Відхилити запит в друзі");

                            }
                        }
                        }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (!network.isOnline()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!network.isOnline()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    }




