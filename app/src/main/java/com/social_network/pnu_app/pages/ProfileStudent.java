package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    DatabaseReference CheckFriendReferenceRequestsMyReceiver;  // Check
    DatabaseReference CheckFriendReferenceRequestMySender;     // Check
    DatabaseReference CheckFriendReferenceRequestAlienSender;  // Check

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
    ValueEventListener valueEventListener;
    NetworkStatus network = new NetworkStatus();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ReceiverStudentKey = getIntent().getExtras().get("VisitedStudentKey").toString();
        senderUserId = getKeyCurrentStudend(AppDatabase.getAppDatabase(ProfileStudent.this));

        // SEND AND CANCEL REQUESTS
        FriendRequestsReferenceAlienReceiver = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey).
                child("FriendRequestReceiver").child(senderUserId).child("requestType");
        FriendRequestsReferenceAlienReceiver.keepSynced(true);

        FriendRequestsReferenceMySender = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).
                child("FriendRequestSender").child(ReceiverStudentKey).child("requestType");
        FriendRequestsReferenceMySender.keepSynced(true);

        // CHECK MY AND ALIEN REFERENCE

        CheckFriendReferenceRequestsMyReceiver = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).
        child("FriendRequestReceiver");
        CheckFriendReferenceRequestsMyReceiver.keepSynced(true);

        CheckFriendReferenceRequestMySender = FirebaseDatabase.getInstance().getReference("students").child(senderUserId)
        .child("FriendRequestSender");
        CheckFriendReferenceRequestMySender.keepSynced(true);

        // -> DELETE ALIEN SENDER WHEN ACCEPT FRIEND
        CheckFriendReferenceRequestAlienSender = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey)
                .child("FriendRequestSender");
        CheckFriendReferenceRequestAlienSender.keepSynced(true);


        // FRIENDS

        FriendReferenceAlien = FirebaseDatabase.getInstance().getReference("students").child(ReceiverStudentKey).
                child("Friends");
        FriendReferenceAlien.keepSynced(true);

        FriendReferenceMy = FirebaseDatabase.getInstance().getReference("students").child(senderUserId).
                child("Friends");
        FriendReferenceMy.keepSynced(true);



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
                } catch (Exception castToBool) {
                    linkFirebaseStorageMainPhoto = "";
                }
                tvPIBvalue.setText(lastName + " " + name);
                tvFacultyValue.setText(" " + faculty);
                tvGroupValue.setText(" " + group);
                tvDateOfEntryValue.setText(" " + dateOfEntry);
                tvFormStudyingValue.setText(" " + formStudying);

                if (!linkFirebaseStorageMainPhoto.isEmpty()){
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
                }
                else {
                    Picasso.with(ProfileStudent.this)
                            .load(R.drawable.com_facebook_profile_picture_blank_square)
                            .placeholder(R.drawable.logo_pnu)
                            .error(R.mipmap.ic_error2)
                            .centerCrop()
                            .fit()
                            // .resize(1920,2560)
                            .into(imStudentMainPhoto);

                    Picasso.with(ProfileStudent.this)
                            .load(R.drawable.com_facebook_profile_picture_blank_square)
                            .placeholder(R.drawable.logo_pnu)
                            .error(R.mipmap.ic_error2)
                            .centerCrop()
                            .fit()
                            // .resize(1920,2560)
                            .into(imSendPhotoWall);
                }

                // CHECK ON MY RECEIVED
                CheckFriendReferenceRequestsMyReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(ReceiverStudentKey)) {
                                ReceiverRequestType = dataSnapshot.child(ReceiverStudentKey).child("requestType").getValue().toString();
                                if (ReceiverRequestType != null) {
                                    if (ReceiverRequestType.equals("received")) {
                                        CurrentStateFriend = "requestReceived";
                                        btnAddToFriends.setText("Прийняти заявку ");
                                    }
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

                // CHECK ON MY SENT
                CheckFriendReferenceRequestMySender.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(ReceiverStudentKey)) {
                                SendeRequestType = dataSnapshot.child(ReceiverStudentKey).child("requestType").getValue().toString();

                                if (SendeRequestType != null) {
                                    if (SendeRequestType.equals("sent")) {
                                        CurrentStateFriend = "requestSent";
                                        btnAddToFriends.setText("Скасувати заявку");
                                    }
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

                // CHECK ON FRIEND
                FriendReferenceMy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(ReceiverStudentKey)) {
                                        CurrentStateFriend = "friends";
                                        btnAddToFriends.setText("Видалити з друзів");
                            }
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


 //   }

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
                    if(CurrentStateFriend.equals("requestReceived")){
                        AcceptFriendRequst();
                    }
                    if(CurrentStateFriend.equals("friends")){
                        DeleteFriend();
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

    private void DeleteFriend() {
        FriendReferenceMy.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendReferenceAlien.child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            btnAddToFriends.setEnabled(true);
                            CurrentStateFriend = "notFriend";
                            btnAddToFriends.setText("Додати в друзі  ");
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

    private void AcceptFriendRequst() {
        DateFormat calForDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        final String saveCurrentDate = calForDate.format(currentDate);

        FriendReferenceAlien.child(senderUserId).setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
           FriendReferenceMy.child(ReceiverStudentKey).setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {


                   CheckFriendReferenceRequestsMyReceiver.child(ReceiverStudentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               CheckFriendReferenceRequestAlienSender.child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()){

                                           btnAddToFriends.setEnabled(true);
                                           CurrentStateFriend ="friends";
                                           btnAddToFriends.setText("Видалити з друзів");
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
                                btnAddToFriends.setText("Додати в друзі  ");
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

                            btnAddToFriends.setEnabled(true);
                            CurrentStateFriend = "requestSent";
                            btnAddToFriends.setText("Скасувати заявку");

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

    }




