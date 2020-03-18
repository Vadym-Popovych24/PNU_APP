package com.social_network.pnu_app.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.social_network.pnu_app.R;
import com.social_network.pnu_app.localdatabase.AppDatabase;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation_settings);
        bottomNavigationView.setSelectedItemId(R.id.action_settings);

        menuChanges(bottomNavigationView);

        Button Exit;
        Exit = findViewById(R.id.btnExit);
        Exit.setOnClickListener(listenerBtnExit);
    }


    View.OnClickListener listenerBtnExit = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnExit) {
                Exit(AppDatabase.getAppDatabase(Settings.this));
            }
        }
    };

    private void Exit(final AppDatabase db) {
        FirebaseAuth.getInstance().signOut();
        db.studentDao().updateCurrentStudent(Integer.parseInt(db.studentDao().getCurrentStudent()));
        Intent intentSignOut = new Intent(this, MainActivity.class);
        intentSignOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentSignOut);
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
