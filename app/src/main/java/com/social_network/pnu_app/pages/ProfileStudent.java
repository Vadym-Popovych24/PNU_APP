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
    public CircleImageView imStudentMainPhoto;
    public ImageView imSendPhotoWall;

    String name;
    String lastName;
    String group;
    String dateOfEntry;
    String formStudying;
    String faculty;
    String linkFirebaseStorageMainPhoto;
    private HashMap student = new HashMap();
    ValueEventListener valueEventListener;
    NetworkStatus network = new NetworkStatus();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


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

        menuChanges(bottomNavigationView);

        imStudentMainPhoto = findViewById(R.id.imStudentMainPhotoProfile);
        imSendPhotoWall = findViewById(R.id.imSendPhotoWallProfile);


        BuildStudentPage();


    }


    public void BuildStudentPage(){
        String VisitStudentKey = getIntent().getExtras().get("VisitStudentKey").toString();
        Query queryByKey = FirebaseDatabase.getInstance().getReference("students").child(VisitStudentKey);

        if(!network.isOnline()){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ProfileStudent.this, " Please Connect to Internet",
                    Toast.LENGTH_LONG).show();
        }
        else{
        queryByKey.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student = (HashMap) dataSnapshot.getValue();

                System.out.println("dataSnapshot =" + dataSnapshot);

                name = student.get("name").toString();
                lastName = student.get("lastName").toString();
                group = student.get("group").toString();
                dateOfEntry = student.get("dateOfEntry").toString();
                formStudying = student.get("formStudying").toString();
                faculty = student.get("faculty").toString();
                linkFirebaseStorageMainPhoto = student.get("linkFirebaseStorageMainPhoto").toString();

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
                case R.id.btnLoadPhotoStudent:

                    NetworkStatus network = new NetworkStatus();
                    if (!network.isOnline()) {
                        // progressBar.setVisibility(View.GONE);
                      //  Toast.makeText(MainStudentPage.this, " Please Connect to Internet",
                        //        Toast.LENGTH_LONG).show();
                    } else {
                     //   addPhoto();
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


}


