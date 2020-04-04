package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.social_network.pnu_app.R;

public class FriendsActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Button btnBackFromFrinedList;

    private ViewPager viewPagerFriends;
    private TabLayout tabLayoutFriends;
    private TabsFriendsAdapter friendsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

/*        mToolbar = (Toolbar) findViewById(R.id.friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.FriendsActivity);*/

        // Tabs for FriendsActivity Activity

        viewPagerFriends = findViewById(R.id.friend_tabs_pages);
        friendsAdapter = new TabsFriendsAdapter(getSupportFragmentManager());
        viewPagerFriends.setAdapter(friendsAdapter);
        tabLayoutFriends = findViewById(R.id.tabs_friends);
        tabLayoutFriends.setupWithViewPager(viewPagerFriends);
/*        viewPagerFriends.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutFriends));
        tabLayoutFriends.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerFriends));*/

    /*    BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_friends);
        bottomNavigationView.setSelectedItemId((R.id.action_main_student_page));
        menuChanges(bottomNavigationView);*/


      /*  Button btnFindNewFriends;
        btnFindNewFriends = findViewById(R.id.btnFindNewFriends);
        btnBackFromFrinedList = findViewById(R.id.btnBackFromFriendList);

        btnFindNewFriends.setOnClickListener(listenerBtn);
        btnBackFromFrinedList.setOnClickListener(listenerBtn);*/
    }

/*
    View.OnClickListener listenerBtn = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnFindNewFriends:
                    Intent intentFindNewFriends;
                    intentFindNewFriends = new Intent( "com.social_network.pnu_app.pages.FindNewFriends");
                    startActivity(intentFindNewFriends);
                    break;
                case R.id.btnBackFromFriendList:
                    Intent intentBackFriendList;
                    intentBackFriendList = new Intent( "com.social_network.pnu_app.pages.MainStudentPage");
                    startActivity(intentBackFriendList);
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
*/
}