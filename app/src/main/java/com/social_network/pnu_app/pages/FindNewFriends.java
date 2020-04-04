package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.AllAuthUsers;
import com.social_network.pnu_app.localdatabase.AppDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindNewFriends extends AppCompatActivity {

    RecyclerView allUsersList;
    private Query allDatabaseUsersReference;
    Button btnBackFromlUserList;
    ValueEventListener valueEventListener;
    String SerieIDCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_friends);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_users);
        bottomNavigationView.setSelectedItemId((R.id.action_main_student_page));
        menuChanges(bottomNavigationView);

        btnBackFromlUserList = findViewById(R.id.btnBackFromUserList);
        btnBackFromlUserList.setOnClickListener(btnlistener);

        allUsersList = findViewById(R.id.recyclerViewAllUsers);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));

         allDatabaseUsersReference = FirebaseDatabase.getInstance().getReference().child("students")
                 .orderByChild("verify")
                 .equalTo(true);

         allDatabaseUsersReference.keepSynced(true);

    }

    public String getStudentSeriesIDCard(final AppDatabase db){
        int currentStudent = Integer.parseInt(db.studentDao().getCurrentStudent());
        String SeriesIDCard = db.studentDao().getSeriesBYId(currentStudent);
        return SeriesIDCard;
    }

   @Override
    public void onStart() {
        super.onStart();
        SerieIDCard = getStudentSeriesIDCard(AppDatabase.getAppDatabase(FindNewFriends.this));
        FirebaseRecyclerAdapter<AllAuthUsers, AllAuthUsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllAuthUsers, AllAuthUsersViewHolder>
                (
                        AllAuthUsers.class,
                        R.layout.all_users_display_layout,
                        AllAuthUsersViewHolder.class,
                        allDatabaseUsersReference

                ) {
            @Override
            protected void populateViewHolder(AllAuthUsersViewHolder allAuthUsersViewHolder, final AllAuthUsers allAuthUsers, final int i) {

                allAuthUsersViewHolder.setStudentName(allAuthUsers.getName(), allAuthUsers.getLastName());
                allAuthUsersViewHolder.setStudentGroup(allAuthUsers.getGroup());
                allAuthUsersViewHolder.setStudentImage(getApplicationContext(), (allAuthUsers.getLinkFirebaseStorageMainPhoto()));
                allAuthUsersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!allAuthUsers.getSeriesIDcard().equals(SerieIDCard)){
                                String VisitedStudentKey = getRef(i).getKey();
                                Intent profileIntent = new Intent(FindNewFriends.this, ProfileStudent.class);
                                profileIntent.putExtra("VisitedStudentKey", VisitedStudentKey);
                                startActivity(profileIntent);
                            } else {
                                Intent myProfileIntent = new Intent("com.social_network.pnu_app.pages.MainStudentPage");
                                startActivity(myProfileIntent);

                           }
                        }
                    });

            }
        };
        allUsersList.setAdapter(firebaseRecyclerAdapter);

    }


    View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnBackFromUserList:
                    Intent intentlistMyFriends;
                    intentlistMyFriends = new Intent( "com.social_network.pnu_app.pages.FriendsActivity");
                    startActivity(intentlistMyFriends);
                    break;
            }
        }
    };

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
}

 class AllAuthUsersViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public AllAuthUsersViewHolder(View itemView){
        super(itemView);

        mView= itemView;
    }
    public void setStudentName(String studentName, String studentLastName){
        TextView nameAndLastName = mView.findViewById(R.id.all_users_username);
        nameAndLastName.setText(studentName + " " + studentLastName);
    }
     public void setStudentGroup(String studentGroup){
         TextView group = mView.findViewById(R.id.all_users_status);
         group.setText(studentGroup);
     }

     public void setStudentImage( final Context context, final String studentImage) {
         final CircleImageView image = mView.findViewById(R.id.all_users_profile_image);
         Picasso.with(context)
                 .load(studentImage)
                 .networkPolicy(NetworkPolicy.OFFLINE)
                 .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                 .error(R.drawable.com_facebook_close)
                 .centerCrop()
                 .fit()
                 //.resize(1920,2560)
                 .into(image, new Callback() {
                     @Override
                     public void onSuccess() {

                     }

                     @Override
                     public void onError() {
                         if (studentImage != null) {
                             if (!studentImage.isEmpty()) {
                         Picasso.with(context)
                                 .load(studentImage)
                                 .placeholder(R.drawable.logo_pnu)
                                 .error(R.drawable.com_facebook_close)
                                 .centerCrop()
                                 .fit()
                                 //.resize(1920,2560)
                                 .into(image);
                             }
                         } else {
                             Picasso.with(context)
                                     .load(R.drawable.com_facebook_profile_picture_blank_square)
                                     .placeholder(R.drawable.logo_pnu)
                                     .error(R.drawable.com_facebook_close)
                                     .centerCrop()
                                     .fit()
                                     //.resize(1920,2560)
                                     .into(image);
                         }

                     }
                 });

     }






 }
