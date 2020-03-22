package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.entity.AllAuthUsers;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindNewFriends extends AppCompatActivity {

    RecyclerView allUsersList;
    private DatabaseReference allDatabaseUsersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_friends);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_users);
        bottomNavigationView.setSelectedItemId((R.id.action_main_student_page));
        menuChanges(bottomNavigationView);

        allUsersList = findViewById(R.id.recyclerViewAllUsers);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));

        allDatabaseUsersReference = FirebaseDatabase.getInstance().getReference().child("students");

    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<AllAuthUsers, AllAuthUsersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllAuthUsers, AllAuthUsersViewHolder>
                (
                  AllAuthUsers.class,
                  R.layout.all_users_display_layout,
                  AllAuthUsersViewHolder.class,
                  allDatabaseUsersReference

        ) {
            @Override
            protected void populateViewHolder(AllAuthUsersViewHolder allAuthUsersViewHolder, AllAuthUsers allAuthUsers, int i) {
                allAuthUsersViewHolder.setStudentName(allAuthUsers.getName(), allAuthUsers.getLastName());
                allAuthUsersViewHolder.setStudentGroup(allAuthUsers.getGroup());
                allAuthUsersViewHolder.setStudentImage(getApplicationContext(), allAuthUsers.getLinkMainStudentPage());
            }
        };
        allUsersList.setAdapter(firebaseRecyclerAdapter);
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
     public void setStudentImage(Context context, String studentImage) {
         CircleImageView image = mView.findViewById(R.id.all_users_profile_image);
         if (!studentImage.isEmpty()) {
             Picasso.with(context)
                     .load(studentImage)
                     .placeholder(R.drawable.logo_pnu)
                     .error(R.mipmap.ic_error2)
                     .centerCrop()
                     .fit()
                     //.resize(1920,2560)
                     .into(image);
         }
     }



}
