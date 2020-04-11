package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.social_network.pnu_app.network.NetworkStatus;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private DatabaseReference FriendRequestsReferenceAlienReceiver;  //  Отримувач
    private DatabaseReference FriendRequestsReferenceMySender;  //  Відправник

    private DatabaseReference FriendReferenceAlien; //  Отримувач
    private DatabaseReference FriendReferenceMy;  //  Відправник;

    DatabaseReference CheckFriendReferenceRequestMyReceiver;  // Check
    DatabaseReference CheckFriendReferenceRequestMySender;     // Check
    DatabaseReference CheckFriendReferenceRequestAlienSender;  // Check

    DatabaseReference SubscribersReferenceMy;                 // Subscribers My
    DatabaseReference SubscribersReferenceAlien;              // Subscribers Alien
    DatabaseReference SubscribedReferenceMy;                  // Subscribed My
    DatabaseReference SubscribedReferenceAlien;               // Subscribed Alien
    DatabaseReference studentsReference;                  // Notification

    String senderUserId;
    String ReceiverStudentKey;

    String SendeRequestType;
    String ReceiverRequestType;

    public CircleImageView imStudentMainPhoto;
    public ImageView imSendPhotoWall;

    String name;
    String lastName;
    String group;
    String dateOfEntry;
    String formStudying;
    String faculty;
    String linkFirebaseStorageMainPhoto;
    long countFriends;
    ValueEventListener valueEventListener;
    NetworkStatus network = new NetworkStatus();


    int activeColorButtonAddToFriends;
    int activeColorTextButtonAddToFriends;

    int disactivateColorButtonAddToFriends;
    int disactivateColorTextButtonAddToFriends;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ReceiverStudentKey = getIntent().getExtras().get("VisitedStudentKey").toString();
        senderUserId = getKeyCurrentStudend(AppDatabase.getAppDatabase(ProfileStudent.this));



        // SEND AND CANCEL REQUESTS
        FriendRequestsReferenceAlienReceiver = FirebaseDatabase.getInstance().getReference("studentsCollection").child(ReceiverStudentKey).
                child("FriendRequestReceiver").child(senderUserId).child("requestType");
        FriendRequestsReferenceAlienReceiver.keepSynced(true);

        FriendRequestsReferenceMySender = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId).
                child("FriendRequestSender").child(ReceiverStudentKey).child("requestType");
        FriendRequestsReferenceMySender.keepSynced(true);

        // CHECK MY AND ALIEN REFERENCE

        CheckFriendReferenceRequestMyReceiver = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId).
        child("FriendRequestReceiver");
        CheckFriendReferenceRequestMyReceiver.keepSynced(true);

        CheckFriendReferenceRequestMySender = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId)
        .child("FriendRequestSender");
        CheckFriendReferenceRequestMySender.keepSynced(true);

        // -> DELETE ALIEN SENDER WHEN ACCEPT FRIEND
        CheckFriendReferenceRequestAlienSender = FirebaseDatabase.getInstance().getReference("studentsCollection").child(ReceiverStudentKey)
                .child("FriendRequestSender");
        CheckFriendReferenceRequestAlienSender.keepSynced(true);


        // FRIENDS

        FriendReferenceAlien = FirebaseDatabase.getInstance().getReference("studentsCollection").child(ReceiverStudentKey).
                child("Friends");
        FriendReferenceAlien.keepSynced(true);

        FriendReferenceMy = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId).
                child("Friends");
        FriendReferenceMy.keepSynced(true);

       // Subscribers

        SubscribersReferenceMy = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId).child("Subscribers");
        SubscribersReferenceMy.keepSynced(true);

        SubscribersReferenceAlien = FirebaseDatabase.getInstance().getReference("studentsCollection").child(ReceiverStudentKey).child("Subscribers");
        SubscribersReferenceAlien.keepSynced(true);

        // Subscribed

        SubscribedReferenceAlien = FirebaseDatabase.getInstance().getReference("studentsCollection").child(ReceiverStudentKey).child("Subscribed");
        SubscribedReferenceAlien.keepSynced(true);

        SubscribedReferenceMy = FirebaseDatabase.getInstance().getReference("studentsCollection").child(senderUserId).child("Subscribed");
        SubscribedReferenceMy.keepSynced(true);

        // Notification
        studentsReference = FirebaseDatabase.getInstance().getReference("students");
        studentsReference.keepSynced(true);




        // Colors
        activeColorButtonAddToFriends = getResources().getColor(R.color.lines);
        activeColorTextButtonAddToFriends = getResources().getColor(R.color.btn_sign_in);

        disactivateColorButtonAddToFriends = getResources().getColor(R.color.btn_sign_in);
        disactivateColorTextButtonAddToFriends = getResources().getColor(R.color.colorAccentWhite);

        // Views

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

      public String getKeyCurrentStudend(final AppDatabase db) {
        String keyStudent = db.studentDao().getKeyStudent();
        return keyStudent;
    }


    public void BuildStudentPage(){
        Query queryByKey = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey);

  /*      if(!network.isOnline()){
            Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                    Toast.LENGTH_LONG).show();*/
      //  }
     //   else{
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
                try {
                    linkFirebaseStorageMainPhoto = dataSnapshot.child("linkFirebaseStorageMainPhoto").getValue().toString();
                }
                catch (NullPointerException nullLinkPhoto){
                    linkFirebaseStorageMainPhoto = "";
                }

                FriendReferenceAlien.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        countFriends = dataSnapshot.getChildrenCount();

                        btnlistFriends.setText(countFriends + " " + "Друзі");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        if (!network.isOnline()) {
                            //   progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
                tvPIBvalue.setText(lastName + " " + name);
                tvFacultyValue.setText(" " + faculty);
                tvGroupValue.setText(" " + group);
                tvDateOfEntryValue.setText(" " + dateOfEntry);
                tvFormStudyingValue.setText(" " + formStudying);

                if (!linkFirebaseStorageMainPhoto.isEmpty()){
                Picasso.with(ProfileStudent.this)
                        .load(linkFirebaseStorageMainPhoto)
                        .placeholder(R.drawable.logo_pnu)
                        .error(R.drawable.com_facebook_close)
                        .centerCrop()
                        .fit()
                        // .resize(1920,2560)
                        .into(imStudentMainPhoto);

                Picasso.with(ProfileStudent.this)
                        .load(linkFirebaseStorageMainPhoto)
                        .placeholder(R.drawable.logo_pnu)
                        .error(R.drawable.com_facebook_close)
                        .centerCrop()
                        .fit()
                        // .resize(1920,2560)
                        .into(imSendPhotoWall);
                }
                else {
                    Picasso.with(ProfileStudent.this)
                            .load(R.drawable.com_facebook_profile_picture_blank_square)
                            .placeholder(R.drawable.logo_pnu)
                            .error(R.drawable.com_facebook_close)
                            .centerCrop()
                            .fit()
                            // .resize(1920,2560)
                            .into(imStudentMainPhoto);

                    Picasso.with(ProfileStudent.this)
                            .load(R.drawable.com_facebook_profile_picture_blank_square)
                            .placeholder(R.drawable.logo_pnu)
                            .error(R.drawable.com_facebook_close)
                            .centerCrop()
                            .fit()
                            // .resize(1920,2560)
                            .into(imSendPhotoWall);
                }

               // CHECK ON MY RECEIVED
                checkOnMyReceivedRequest();

                // CHECK ON MY SENT
                checkOnMySentRequest();

                // CHECK ON MY FRIEND
                checkOnMyFriend();

                // CHECK ON YOU SUBSCRIBED
                checkOnYouSubscribed();

                // CHECK YOU ARE SUBSCRIBED
                checkAreYouSubscribed();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!network.isOnline()) {
                 //   progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        }

    // CHECK ON MY RECEIVED
    private void checkOnMyReceivedRequest() {
        CheckFriendReferenceRequestMyReceiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(ReceiverStudentKey)) {
                        ReceiverRequestType = dataSnapshot.child(ReceiverStudentKey).child("requestType").getValue().toString();
                        if (ReceiverRequestType != null) {
                            if (ReceiverRequestType.equals("received")) {
                                CurrentStateFriend = "requestReceived";
                                btnAddToFriends.setText("Відповісти на заявку в друзі");
                                btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                                btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);

                        }
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
                if (!network.isOnline()) {
                    //      progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    // CHECK ON MY SENT
    private void checkOnMySentRequest() {
        CheckFriendReferenceRequestMySender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(ReceiverStudentKey)) {
                        SendeRequestType = dataSnapshot.child(ReceiverStudentKey).child("requestType").getValue().toString();

                        if (SendeRequestType != null) {
                            if (SendeRequestType.equals("sent")) {
                                CurrentStateFriend = "requestSent";
                                btnAddToFriends.setText("Скасувати заявку");
                                btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                                btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);

                        }
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
                if (!network.isOnline()) {
                    //    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    // CHECK ON MY FRIEND
    private void checkOnMyFriend() {
        FriendReferenceMy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(ReceiverStudentKey)) {
                        CurrentStateFriend = "friends";
                        btnAddToFriends.setText("Видалити з друзів");
                        btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                        btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!network.isOnline()) {
                    //    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // CHECK ON YOU SUBSCRIBED
    private void checkOnYouSubscribed(){
        SubscribersReferenceMy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(ReceiverStudentKey)) {
                        CurrentStateFriend ="onYouSubscribed";
                        btnAddToFriends.setText("Ваш підписник");
                        btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                        btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!network.isOnline()) {
                    //    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // CHECK YOU ARE SUBSCRIBED
    private void checkAreYouSubscribed(){

        SubscribersReferenceAlien.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(senderUserId)) {
                        CurrentStateFriend ="youAreSubscribed";
                        btnAddToFriends.setText("Ви підписані");
                        btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                        btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!network.isOnline()) {
                    //    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

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

                    if(CurrentStateFriend.equals("notFriend")) {
                        btnAddToFriends.setEnabled(false);
                        btnAddToFriends.setBackgroundColor(disactivateColorButtonAddToFriends);
                        btnAddToFriends.setTextColor(disactivateColorTextButtonAddToFriends);
                        SendRequestOnAFriendship();
                    }
                    if(CurrentStateFriend.equals("requestSent")) {
                        btnAddToFriends.setEnabled(false);
                        CancelFriendRequest();
                    }
                    if(CurrentStateFriend.equals("requestReceived")){
                        // add or subscribe friend
                         alertDialogRespondToAFrinedRequest();

                    }
                    if(CurrentStateFriend.equals("friends")){
                        btnAddToFriends.setEnabled(false);
                        DeleteFriend();
                    }
                    if(CurrentStateFriend.equals("onYouSubscribed")) {
                        // add  friend
                        alertDialogRespondOnYouSubscribed();
                    }
                    if(CurrentStateFriend.equals("youAreSubscribed")) {
                        // Un subscribe
                        alertDialogRespondUnSubscribed();
                    }
                    break;
                case R.id.btnListFriendsProfile:
                    Intent intentlistMyFriends;
                    intentlistMyFriends = new Intent( "com.social_network.pnu_app.pages.FriendsActivity");
                    startActivity(intentlistMyFriends);
                    break;
            }
        }
    };

    private void alertDialogRespondUnSubscribed() {
        String mass[] = new String[] {"Відписатися"};
        AlertDialog.Builder a_builder = new AlertDialog.Builder(ProfileStudent.this);

        a_builder.setTitle("Ви підписник")
                .setItems( mass, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            // Unsubscribe
                            UnSubscribed();
                        }
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }

    private void alertDialogRespondOnYouSubscribed() {
        String mass[] = new String[] {"Добавити в друзі"};
        AlertDialog.Builder a_builder = new AlertDialog.Builder(ProfileStudent.this);

        a_builder.setTitle("Добавити підписника в друзі")
                .setItems( mass, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                           // Add Subscriber On a FriendsActivity
                            AcceptFriendRequst();
                        }
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }

    public void alertDialogRespondToAFrinedRequest(){
        String mass[] = new String[] {"Добавити в друзі", "Відхилити (добавити в підписники)"};
        AlertDialog.Builder a_builder = new AlertDialog.Builder(ProfileStudent.this);

        a_builder.setTitle("Відповісти на заявку в друзі")
                .setItems( mass, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            // Accept frined
                            AcceptFriendRequst();
                        }
                        else if (which == 1){
                            // Subscribe
                            SubscribeFriendRequest();
                        }
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }

    private void UnSubscribed() {
        btnAddToFriends.setEnabled(false);
    SubscribedReferenceMy.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            SubscribersReferenceAlien.child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    btnAddToFriends.setEnabled(true);
                    CurrentStateFriend = "notFriend";
                    btnAddToFriends.setText("Додати в друзі");
                    btnAddToFriends.setBackgroundColor(disactivateColorButtonAddToFriends);
                    btnAddToFriends.setTextColor(disactivateColorTextButtonAddToFriends);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (!network.isOnline()) {
                        //        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            if (!network.isOnline()) {
                //        progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                        Toast.LENGTH_LONG).show();
            }
        }
    });
    }

    public void SubscribeFriendRequest() {
        btnAddToFriends.setEnabled(false);
        DateFormat calForDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        final String saveCurrentDate = calForDate.format(currentDate);

        SubscribersReferenceMy.child(ReceiverStudentKey).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SubscribedReferenceAlien.child(senderUserId).child("date").setValue(saveCurrentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        CheckFriendReferenceRequestMyReceiver.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    CheckFriendReferenceRequestAlienSender.child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                btnAddToFriends.setEnabled(true);
                                                CurrentStateFriend ="onYouSubscribed";
                                                btnAddToFriends.setText("Ваш підписник");
                                                btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                                                btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);

                                                // Remove My Sender and Alien Receiver (if in receiver happens interrupt internet connection)

                                                FriendRequestsReferenceMySender.getParent().removeValue();
                                                FriendRequestsReferenceAlienReceiver.getParent().removeValue();


                                                Toast.makeText(ProfileStudent.this, "Додано в підписники",
                                                        Toast.LENGTH_LONG).show();

                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (!network.isOnline()) {
                                                //        progressBar.setVisibility(View.GONE);
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
                                    //     progressBar.setVisibility(View.GONE);
                                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (!network.isOnline()) {
                            //        progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            //////////
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!network.isOnline()) {
                    //        progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //////////


    }

    private void DeleteFriend() {
        FriendReferenceMy.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendReferenceAlien.child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            SubscribeFriendRequest();

                         /*   btnAddToFriends.setEnabled(true);
                            CurrentStateFriend = "notFriend";
                            btnAddToFriends.setText("Додати в друзі");
                            btnAddToFriends.setBackgroundColor(disactivateColorButtonAddToFriends);
                            btnAddToFriends.setTextColor(disactivateColorTextButtonAddToFriends);*/
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (!network.isOnline()) {
                                //     progressBar.setVisibility(View.GONE);
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
                    //     progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void AcceptFriendRequst() {
        DateFormat calForDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        final String saveCurrentDate = calForDate.format(currentDate);

        FriendReferenceAlien.child(senderUserId).child("date").setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FriendReferenceAlien.child(senderUserId).child("online").setValue(false);
           FriendReferenceMy.child(ReceiverStudentKey).child("date").setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   FriendReferenceMy.child(ReceiverStudentKey).child("online").setValue(false);

                   CheckFriendReferenceRequestMyReceiver.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               CheckFriendReferenceRequestAlienSender.child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()){

                                           // Remove My Sender and Alien Receiver (if in receiver happens interrupt internet connection)

                                           FriendRequestsReferenceMySender.getParent().removeValue();
                                           FriendRequestsReferenceAlienReceiver.getParent().removeValue();


                                           // Remove Subscribers and Subscribed if this method calls from alertDialogRespondOnYouSubscribed()


                                           SubscribersReferenceMy.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   SubscribedReferenceAlien.child(senderUserId).removeValue().addOnFailureListener(new OnFailureListener() {
                                                       // Кінець  Можливо тут треба додаткових умов для видалення ? --> Тут видаляємо підписника з групи підписників якщо він є
                                                       @Override
                                                       public void onFailure(@NonNull Exception e) {
                                                           if (!network.isOnline()) {
                                                               //        progressBar.setVisibility(View.GONE);
                                                               Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                                                       Toast.LENGTH_LONG).show();
                                                           }
                                                       }
                                                   });
                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   if (!network.isOnline()) {
                                                       //        progressBar.setVisibility(View.GONE);
                                                       Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                                               Toast.LENGTH_LONG).show();
                                                   }
                                               }
                                           });


                                           btnAddToFriends.setEnabled(true);
                                           CurrentStateFriend ="friends";
                                           btnAddToFriends.setText("Видалити з друзів");
                                           btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                                           btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);

                                           Toast.makeText(ProfileStudent.this, "Додано в друзі",
                                                   Toast.LENGTH_LONG).show();
                                       }

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       if (!network.isOnline()) {
                                   //        progressBar.setVisibility(View.GONE);
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
                          //     progressBar.setVisibility(View.GONE);
                               Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                       Toast.LENGTH_LONG).show();
                           }
                       }
                   });

               }

           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   if (!network.isOnline()) {
                    //   progressBar.setVisibility(View.GONE);
                       Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                               Toast.LENGTH_LONG).show();
                   }
               }
           });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (!network.isOnline()) {
                 //   progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void CancelFriendRequest() {
        FriendRequestsReferenceAlienReceiver.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FriendRequestsReferenceMySender.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                btnAddToFriends.setEnabled(true);
                                CurrentStateFriend ="notFriend";
                                btnAddToFriends.setText("Додати в друзі");
                                btnAddToFriends.setBackgroundColor(disactivateColorButtonAddToFriends);
                                btnAddToFriends.setTextColor(disactivateColorTextButtonAddToFriends);

                                CheckFriendReferenceRequestMyReceiver.child(ReceiverStudentKey).removeValue();
                                CheckFriendReferenceRequestAlienSender.child(senderUserId).removeValue();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (!network.isOnline()) {
                         //       progressBar.setVisibility(View.GONE);
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
                 //   progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SendRequestOnAFriendship() {
        FriendRequestsReferenceAlienReceiver.setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FriendRequestsReferenceMySender.setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                HashMap<String, String> notificationData = new HashMap<String, String>();
                                notificationData.put("from", senderUserId);
                                notificationData.put("type", "requestSent");

                                studentsReference.child(ReceiverStudentKey).child("Notification").push().setValue(notificationData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {

                                                    btnAddToFriends.setEnabled(true);
                                                    CurrentStateFriend = "requestSent";
                                                    btnAddToFriends.setText("Скасувати заявку");
                                                    btnAddToFriends.setBackgroundColor(activeColorButtonAddToFriends);
                                                    btnAddToFriends.setTextColor(activeColorTextButtonAddToFriends);
                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (!network.isOnline()) {
                                            //            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                            }
                        }
                        }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (!network.isOnline()) {
                    //            progressBar.setVisibility(View.GONE);
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
                //    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void onlineStatus(final boolean online) {
        studentsReference.child("online").setValue(online);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // TODO delete comment  if (currentStudent != null){
        onlineStatus(false);
        //  }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO delete comment     if (currentStudent != null){
        onlineStatus(true);
        //    }
    }


    }




